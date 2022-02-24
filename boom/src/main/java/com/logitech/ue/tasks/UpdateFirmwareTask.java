// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import java.util.ArrayList;
import com.logitech.ue.centurion.device.devicedata.UESonificationProfile;
import com.logitech.ue.centurion.device.devicedata.UEAlarmInfo;
import com.logitech.ue.exceptions.ReconnectionException;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import com.logitech.ue.firmware.FirmwareManager;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import java.util.concurrent.CancellationException;
import java.util.Iterator;
import com.logitech.ue.util.Utils;
import com.logitech.ue.other.UECustomSonificationHelper;
import android.os.AsyncTask;
import com.logitech.ue.App;
import com.logitech.ue.UEColourHelper;
import java.util.Locale;
import java.util.Arrays;
import com.logitech.ue.firmware.UpdateStepInfo;
import com.logitech.ue.centurion.exceptions.UEConnectionException;
import com.logitech.ue.centurion.exceptions.UEOperationException;
import com.logitech.ue.centurion.connection.UEDeviceConnector;
import com.logitech.ue.centurion.device.devicedata.UEOTAStatus;
import android.os.SystemClock;
import com.logitech.ue.centurion.device.devicedata.UEDeviceType;
import com.logitech.ue.centurion.exceptions.UEUnrecognisedCommandException;
import com.logitech.ue.centurion.device.devicedata.UELanguage;
import android.util.Log;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;
import android.content.Intent;
import com.logitech.ue.firmware.UpdateInstruction;
import com.logitech.ue.other.StopWatch;
import java.util.concurrent.Semaphore;
import com.logitech.ue.centurion.utils.MAC;
import com.logitech.ue.centurion.device.UEGenericDevice;
import android.content.Context;
import android.content.BroadcastReceiver;

public class UpdateFirmwareTask extends SafeTask<Void, Float, FirmwareUpdateReport>
{
    protected static final int OPERATION_PROGRESS_VALUE_FAST = 1;
    protected static final int OPERATION_PROGRESS_VALUE_LONG = 10;
    protected static final int OPERATION_PROGRESS_VALUE_NORMAL = 4;
    protected static final int OPERATION_PROGRESS_VALUE_UPLOAD = 3200;
    public static final int PROGRESS_STAGE_BACKUP = 8;
    public static final int PROGRESS_STAGE_FLASHING = 3223;
    public static final int PROGRESS_STAGE_REBOOTING = 0;
    public static final int PROGRESS_STAGE_RESTORATION = 5;
    public static final int RECONNECTION_MINIMUM_TIMEOUT = 30000;
    private static final String TAG;
    public static final int WRITE_BUFFER_SIZE = 1024;
    BroadcastReceiver mBroadcastReceiver;
    protected final Context mContext;
    protected UEGenericDevice mDevice;
    protected MAC mDeviceMAC;
    protected float mProgress;
    protected Semaphore mReconnectWaitingSemaphore;
    private int mReconnectWaitingTimeout;
    protected UpdateStage mStage;
    protected int mStep;
    protected final StopWatch mStopWatch;
    protected FirmwareUpdateTimeReport mTimeReport;
    protected final String mUpdateDeviceSerialNumber;
    protected final UpdateInstruction mUpdateInstruction;
    
    static {
        TAG = UpdateFirmwareTask.class.getSimpleName();
    }
    
    public UpdateFirmwareTask(final UpdateInstruction mUpdateInstruction, final String mUpdateDeviceSerialNumber, final Context mContext) {
        this.mStopWatch = new StopWatch();
        this.mStage = UpdateStage.PREPARATION;
        this.mReconnectWaitingSemaphore = new Semaphore(0, true);
        this.mReconnectWaitingTimeout = 30000;
        this.mTimeReport = new FirmwareUpdateTimeReport();
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getAction().equals("com.logitech.ue.centurion.CONNECTION_CHANGED")) {
                    final UEDeviceStatus status = UEDeviceStatus.getStatus(intent.getIntExtra("status", UEDeviceStatus.getValue(UEDeviceStatus.DISCONNECTED)));
                    final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
                    if (connectedDevice != null) {
                        Log.d(UpdateFirmwareTask.TAG, "Device connection status changed. Status: " + status + " Address: " + connectedDevice.getAddress());
                        if (status.isBtClassicConnectedState()) {
                            ((SafeTask<Void, Progress, Result>)new GetDeviceSerialNumberTask() {
                                public void onSuccess(final String s) {
                                    super.onSuccess((T)s);
                                    if (s.equals(UpdateFirmwareTask.this.mUpdateDeviceSerialNumber)) {
                                        UpdateFirmwareTask.this.mDevice = UEDeviceManager.getInstance().getConnectedDevice();
                                        UpdateFirmwareTask.this.mReconnectWaitingSemaphore.release(1);
                                    }
                                }
                            }).executeOnThreadPoolExecutor(new Void[0]);
                        }
                    }
                    else {
                        Log.d(UpdateFirmwareTask.TAG, "Device connection status changed. Status: " + status);
                    }
                }
            }
        };
        this.mUpdateInstruction = mUpdateInstruction;
        this.mContext = mContext;
        this.mUpdateDeviceSerialNumber = mUpdateDeviceSerialNumber;
    }
    
    private DeviceBackupInfo backupStage(final UEGenericDevice ueGenericDevice) throws Exception {
        this.mStage = UpdateStage.PREPARATION;
        if (!ueGenericDevice.getSerialNumber().equals(this.mUpdateDeviceSerialNumber)) {
            throw new Exception("Wrong device connected fot update");
        }
        final DeviceBackupInfo deviceBackupInfo = new DeviceBackupInfo();
        deviceBackupInfo.name = ueGenericDevice.getBluetoothName();
        Log.d(UpdateFirmwareTask.TAG, "Back up. Name: " + deviceBackupInfo.name);
        this.increaseProgress(1.0f);
        deviceBackupInfo.language = ueGenericDevice.getLanguage();
        ueGenericDevice.setLanguage(UELanguage.ENGLISH);
        Log.d(UpdateFirmwareTask.TAG, "Back up. Language: " + deviceBackupInfo.language);
        this.increaseProgress(1.0f);
        deviceBackupInfo.sonificationProfile = ueGenericDevice.getSonificationProfile();
        Log.d(UpdateFirmwareTask.TAG, "Back up. Sonification: " + deviceBackupInfo.sonificationProfile);
        this.increaseProgress(1.0f);
        deviceBackupInfo.stickyTwsOrPartyUpFlag = ueGenericDevice.getStickyTWSOrPartyUpFlag();
        Log.d(UpdateFirmwareTask.TAG, "Back up. Sticky: " + deviceBackupInfo.stickyTwsOrPartyUpFlag);
        this.increaseProgress(1.0f);
        while (true) {
            try {
                deviceBackupInfo.isCustomEnabled = ueGenericDevice.getCustomState();
                Log.d(UpdateFirmwareTask.TAG, "Back up. Custom sonification: " + deviceBackupInfo.isCustomEnabled);
                this.increaseProgress(1.0f);
                deviceBackupInfo.alarmInfo = ueGenericDevice.getAlarmInfo();
                Log.d(UpdateFirmwareTask.TAG, String.format("Back up. Alarm info. State: %s Host: %s Timer: %s", deviceBackupInfo.alarmInfo.getAlarmState(), deviceBackupInfo.alarmInfo.getAlarmHostAddress(), deviceBackupInfo.alarmInfo.getLastTimer()));
                this.increaseProgress(1.0f);
                deviceBackupInfo.isAlarmDailyRepeat = ueGenericDevice.getRepeatAlarm();
                Log.d(UpdateFirmwareTask.TAG, "Back up. Alarm repeat: " + deviceBackupInfo.isAlarmDailyRepeat);
                this.increaseProgress(1.0f);
                deviceBackupInfo.bleState = ueGenericDevice.getBLEState();
                Log.d(UpdateFirmwareTask.TAG, "Back up. BLE state: " + deviceBackupInfo.bleState);
                this.increaseProgress(1.0f);
                if (ueGenericDevice.isGestureSupported()) {
                    deviceBackupInfo.gestureState = ueGenericDevice.getGestureState();
                }
                else {
                    deviceBackupInfo.gestureState = null;
                }
                Log.d(UpdateFirmwareTask.TAG, "Back up. Is Gesture supported: " + deviceBackupInfo.isXUPSupported);
                deviceBackupInfo.isXUPSupported = ueGenericDevice.isBroadcastModeSupported();
                Log.d(UpdateFirmwareTask.TAG, "Back up. Is X-UP supported: " + deviceBackupInfo.isXUPSupported);
                this.increaseProgress(1.0f);
                if (deviceBackupInfo.isXUPSupported) {
                    deviceBackupInfo.isInPromiscuousMode = ueGenericDevice.isXUPPromiscuousModelOn();
                    Log.d(UpdateFirmwareTask.TAG, "Back up. Promiscuous mode: " + deviceBackupInfo.isInPromiscuousMode);
                    this.increaseProgress(1.0f);
                }
                return deviceBackupInfo;
            }
            catch (UEUnrecognisedCommandException ex) {
                continue;
            }
            break;
        }
    }
    
    private int determinePartition(final UEGenericDevice ueGenericDevice, final UEDeviceType ueDeviceType, final byte[] array) throws Exception {
        int n;
        if (ueDeviceType == UEDeviceType.Kora && array.length > 1048576) {
            Log.d(UpdateFirmwareTask.TAG, "Big OTA file detected. Check partition 5 size");
            if (ueGenericDevice.getPartitionFiveInfo().getSize() * 2L < array.length) {
                throw new Exception("Partition five is too small");
            }
            this.increaseProgress(1.0f);
            n = 5;
        }
        else {
            n = 0;
        }
        return n;
    }
    
    private void enterOTAMode(final UEGenericDevice ueGenericDevice) throws UEOperationException, UEConnectionException {
        Log.d(UpdateFirmwareTask.TAG, "Attempting to enter OTA mode");
        SystemClock.sleep(1000L);
        ueGenericDevice.setOTAStatus(UEOTAStatus.START);
        ((UEDeviceConnector)ueGenericDevice.getConnector()).switchMode(UEDeviceConnector.Mode.OTA);
        this.increaseProgress(1.0f);
        Log.d(UpdateFirmwareTask.TAG, "Waiting 1 sec for OTA switch");
        SystemClock.sleep(1000L);
    }
    
    private void erasePartition(final UEGenericDevice ueGenericDevice, final int i) throws UEOperationException, UEConnectionException {
        Log.d(UpdateFirmwareTask.TAG, String.format("Erase partition %d", i));
        this.mStage = UpdateStage.ERASING;
        ueGenericDevice.erasePartition((byte)i);
        this.increaseProgress(10.0f);
    }
    
    private FirmwareFlashingTimeReport executeUpdateStep(final int n, final UpdateStepInfo updateStepInfo) throws Exception {
        Log.d(UpdateFirmwareTask.TAG, "Start Flashing stage " + n);
        this.mStep = n;
        final FirmwareFlashingTimeReport firmwareFlashingTimeReport = new FirmwareFlashingTimeReport();
        this.mStopWatch.reset();
        this.flashingStage(this.mDevice, this.prepareDFUData(updateStepInfo));
        firmwareFlashingTimeReport.flashingTime = this.mStopWatch.elapsedTime();
        Log.d(UpdateFirmwareTask.TAG, String.format("Flashing stage completed. Elapsed time %.3f s", firmwareFlashingTimeReport.flashingTime / 1000.0f));
        this.mStopWatch.reset();
        this.waitDeviceReconnection();
        firmwareFlashingTimeReport.rebootTime = this.mStopWatch.elapsedTime();
        return firmwareFlashingTimeReport;
    }
    
    private void exitOTAMode() {
        if (this.mDevice == null) {
            return;
        }
        while (true) {
            try {
                Log.d(UpdateFirmwareTask.TAG, "Stop OTA mode");
                this.mDevice.cancelOTA();
                ((UEDeviceConnector)this.mDevice.getConnector()).switchMode(UEDeviceConnector.Mode.Centurion);
            }
            catch (Exception ex) {
                Log.w(UpdateFirmwareTask.TAG, "Didn't managed to stop OTA");
                continue;
            }
            break;
        }
    }
    
    private void flashDFU(final UEGenericDevice ueGenericDevice, final byte[] a) throws UEOperationException, UEConnectionException {
        Log.d(UpdateFirmwareTask.TAG, "Writing dfu size " + Arrays.toString(a) + " bytes");
        this.mStage = UpdateStage.FLASHING;
        final float n = 1024.0f / a.length;
        int i = 0;
        final byte[] a2 = new byte[1024];
        while (i < a.length) {
            this.checkCancellation();
            Arrays.fill(a2, (byte)0);
            int n2;
            if (a.length - i < 1024) {
                n2 = a.length - i;
            }
            else {
                n2 = 1024;
            }
            System.arraycopy(a, i, a2, 0, n2);
            Log.d(UpdateFirmwareTask.TAG, String.format("Sending bytes with offset %d of %d", i, a.length));
            ueGenericDevice.writeSQIF(a2);
            i += 1024;
            this.increaseProgress(3200.0f * n);
        }
    }
    
    private void flashingStage(final UEGenericDevice ueGenericDevice, final byte[] array) throws Exception {
        this.mReconnectWaitingTimeout = this.getReconnectWaitingTimeout(array);
        Log.d(UpdateFirmwareTask.TAG, String.format(Locale.getDefault(), "Firmware size is %d bytes. Reconnect timeout will be %d ms", array.length, this.mReconnectWaitingTimeout));
        final int determinePartition = this.determinePartition(this.mDevice, UEColourHelper.getDeviceTypeByColour(ueGenericDevice.getDeviceColor()), array);
        this.checkCancellation();
        this.enterOTAMode(this.mDevice);
        this.checkCancellation();
        this.erasePartition(this.mDevice, determinePartition);
        this.checkCancellation();
        Log.d(UpdateFirmwareTask.TAG, "Wait for 1 second to ensure maximum stability.");
        Thread.sleep(1000L);
        this.flashDFU(this.mDevice, array);
        this.checkCancellation();
        this.validateDFU(this.mDevice);
        this.checkCancellation();
        this.runDFU(this.mDevice, determinePartition);
        this.checkCancellation();
    }
    
    private int getReconnectWaitingTimeout(final byte[] array) {
        return array.length / 23 + 30000;
    }
    
    private void restorationStage(final DeviceBackupInfo deviceBackupInfo) throws Exception {
        this.mStage = UpdateStage.RESTORATION;
        final int deviceColor = this.mDevice.getDeviceColor();
        Log.d(UpdateFirmwareTask.TAG, "Restoration. Name: " + deviceBackupInfo.name);
        this.mDevice.setBluetoothName(deviceBackupInfo.name);
        this.increaseProgress(1.0f);
        Log.d(UpdateFirmwareTask.TAG, "Restoration. Sonification: " + deviceBackupInfo.sonificationProfile);
        this.mDevice.setSonificationProfile(deviceBackupInfo.sonificationProfile);
        this.increaseProgress(1.0f);
        Log.d(UpdateFirmwareTask.TAG, "Restoration. Sticky: " + deviceBackupInfo.stickyTwsOrPartyUpFlag);
        this.mDevice.setStickyTWSOrPartyUpFlag(deviceBackupInfo.stickyTwsOrPartyUpFlag);
        this.increaseProgress(1.0f);
        if (deviceBackupInfo.alarmInfo.getLastTimer() > 0L) {
            long lng = 0L;
            for (final FirmwareFlashingTimeReport firmwareFlashingTimeReport : this.mTimeReport.flashingTimeReportList) {
                lng += firmwareFlashingTimeReport.flashingTime + firmwareFlashingTimeReport.rebootTime;
            }
            final MAC bluetoothMacAddress = App.getInstance().getBluetoothMacAddress();
            if (lng < deviceBackupInfo.alarmInfo.getLastTimer()) {
                Log.d(UpdateFirmwareTask.TAG, "Restoration. Alarm. Timer: " + (deviceBackupInfo.alarmInfo.getLastTimer() - lng));
                this.mDevice.setAlarm(deviceBackupInfo.alarmInfo.getLastTimer() - lng, bluetoothMacAddress);
                this.increaseProgress(1.0f);
            }
            this.mDevice.setRepeatAlarm(deviceBackupInfo.isAlarmDailyRepeat, bluetoothMacAddress);
            Log.d(UpdateFirmwareTask.TAG, "Restoration. Alarm Repeat: " + lng);
            this.increaseProgress(1.0f);
        }
        else {
            Log.d(UpdateFirmwareTask.TAG, "Restoration. Don't restore alarm because it was not set");
        }
        this.mDevice.setBLEState(deviceBackupInfo.bleState);
        Log.d(UpdateFirmwareTask.TAG, "Restoration. BLE state: " + deviceBackupInfo.bleState);
        this.increaseProgress(1.0f);
        if (this.mDevice.isGestureSupported()) {
            if (deviceBackupInfo.gestureState != null) {
                Log.d(UpdateFirmwareTask.TAG, "Restoration. Gestures: " + deviceBackupInfo.gestureState);
                this.mDevice.setGestureState(deviceBackupInfo.gestureState);
            }
            else {
                Log.d(UpdateFirmwareTask.TAG, "Restoration. Gestures: true");
                this.mDevice.setGestureState(true);
            }
        }
        else {
            Log.d(UpdateFirmwareTask.TAG, "Restoration. Don't restore gestures. They are not supported");
        }
        this.increaseProgress(1.0f);
        if (this.mDevice.isBroadcastModeSupported()) {
            if (deviceBackupInfo.isInPromiscuousMode != null) {
                Log.d(UpdateFirmwareTask.TAG, "Restoration. Promiscuous mode: " + deviceBackupInfo.isInPromiscuousMode);
                this.mDevice.setXUPPromiscuousModel(deviceBackupInfo.isInPromiscuousMode);
            }
            else {
                Log.d(UpdateFirmwareTask.TAG, "Restoration. Promiscuous mode: true");
                this.mDevice.setXUPPromiscuousModel(true);
            }
        }
        else {
            Log.d(UpdateFirmwareTask.TAG, "Restoration. Don't restore promiscuous mode. It is not supported");
        }
        this.increaseProgress(1.0f);
        if (UEColourHelper.getDeviceTypeByColour(this.mDevice.getDeviceColor()) == UEDeviceType.Kora) {
            if (!this.mDevice.getSupportedLanguageIndex().contains(deviceBackupInfo.language.getCode())) {
                Log.d(UpdateFirmwareTask.TAG, String.format("Restoration. Begin language %s recovery", deviceBackupInfo.language.name()));
                final Object value = new WriteLanguageToDeviceTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new UELanguage[] { deviceBackupInfo.language }).get();
                if (value instanceof Exception) {
                    throw (Exception)value;
                }
                this.waitDeviceReconnection();
            }
            if (UECustomSonificationHelper.isDeviceCustom(deviceColor)) {
                if (deviceColor != 7 || deviceBackupInfo.isCustomEnabled != null) {
                    Log.d(UpdateFirmwareTask.TAG, "Restoration. This is a non-Toro Rosso custom device.");
                    final int customSoundResourceForDevice = UECustomSonificationHelper.getCustomSoundResourceForDevice(deviceColor);
                    if (customSoundResourceForDevice != 0) {
                        Log.d(UpdateFirmwareTask.TAG, String.format("Restoration. Begin custom sonification recovery for speaker color 0x%02X", deviceColor));
                        final Object value2 = new WriteCustomSoundsToDeviceTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new byte[][] { Utils.readStreamAsByteArray(this.mContext.getResources().openRawResource(customSoundResourceForDevice)) }).get();
                        if (value2 instanceof Exception) {
                            throw (Exception)value2;
                        }
                        this.waitDeviceReconnection();
                        if (deviceBackupInfo.isCustomEnabled != null) {
                            this.mDevice.setCustomState(deviceBackupInfo.isCustomEnabled);
                            this.increaseProgress(1.0f);
                        }
                    }
                }
                else {
                    Log.w(UpdateFirmwareTask.TAG, "No Restoration. This is a Toro Rosso device or custom disabled");
                }
            }
        }
        Log.d(UpdateFirmwareTask.TAG, "Restoration. Language: " + deviceBackupInfo.language);
        this.mDevice.setLanguage(deviceBackupInfo.language);
        this.increaseProgress(1.0f);
    }
    
    private void runDFU(final UEGenericDevice ueGenericDevice, final int n) throws UEOperationException, UEConnectionException {
        Log.d(UpdateFirmwareTask.TAG, "Run DFU and restart the speaker");
        ueGenericDevice.runDFU((byte)n);
        this.mStage = UpdateStage.REBOOTING;
        this.increaseProgress(1.0f);
    }
    
    private void validateDFU(final UEGenericDevice ueGenericDevice) throws UEOperationException, UEConnectionException {
        Log.d(UpdateFirmwareTask.TAG, "Validate firmware");
        ueGenericDevice.validateSQIF();
        this.increaseProgress(4.0f);
    }
    
    protected void checkCancellation() {
        if (this.isCancelled()) {
            throw new CancellationException();
        }
    }
    
    protected int estimatedProgress(final UpdateInstruction updateInstruction) {
        return updateInstruction.updateStepInfoList.size() * 3223 + 8;
    }
    
    public int getCurrentStep() {
        return this.mStep;
    }
    
    public UpdateStage getStage() {
        return this.mStage;
    }
    
    @Override
    public String getTag() {
        return UpdateFirmwareTask.TAG;
    }
    
    public FirmwareUpdateTimeReport getTimeReport() {
        return this.mTimeReport;
    }
    
    public int getTotalProgress() {
        return this.estimatedProgress(this.mUpdateInstruction);
    }
    
    protected void increaseProgress(final float n) {
        this.mProgress += n;
        this.publishProgress((Object[])new Float[] { this.mProgress / this.getTotalProgress() * 100.0f });
    }
    
    protected void onCancelled(final Object o) {
        super.onCancelled(o);
        Log.d(UpdateFirmwareTask.TAG, "Task was canceled");
    }
    
    @Override
    public void onError(final Exception ex) {
        this.exitOTAMode();
        super.onError(ex);
    }
    
    @Override
    protected void onPostExecute(final Object o) {
        LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mBroadcastReceiver);
        super.onPostExecute(o);
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.logitech.ue.centurion.CONNECTION_CHANGED");
        LocalBroadcastManager.getInstance(this.mContext).registerReceiver(this.mBroadcastReceiver, intentFilter);
    }
    
    protected byte[] prepareDFUData(final UpdateStepInfo updateStepInfo) throws IOException {
        return FirmwareManager.getInstance().getFirmware(updateStepInfo);
    }
    
    protected void waitDeviceReconnection() throws Exception {
        Log.d(UpdateFirmwareTask.TAG, "Wait up to " + this.mReconnectWaitingTimeout / 1000 + " seconds for reconnection. Address: " + this.mDeviceMAC);
        if (!this.mReconnectWaitingSemaphore.tryAcquire(1, this.mReconnectWaitingTimeout, TimeUnit.MILLISECONDS)) {
            throw new ReconnectionException();
        }
    }
    
    @Override
    public FirmwareUpdateReport work(final Void... array) throws Exception {
        Log.d(UpdateFirmwareTask.TAG, "Begin firmware update task");
        this.mDevice = UEDeviceManager.getInstance().getConnectedDevice();
        this.mDeviceMAC = this.mDevice.getAddress();
        this.mTimeReport.startTime = SystemClock.elapsedRealtime();
        final FirmwareUpdateReport firmwareUpdateReport = new FirmwareUpdateReport();
        this.checkCancellation();
        Log.d(UpdateFirmwareTask.TAG, "Start backup stage");
        this.mStopWatch.reset();
        final DeviceBackupInfo backupStage = this.backupStage(this.mDevice);
        this.mTimeReport.backupTime = this.mStopWatch.elapsedTime();
        Log.d(UpdateFirmwareTask.TAG, String.format("Backup stage completed. Elapsed time %.3f s", this.mTimeReport.backupTime / 1000.0f));
        for (int i = 0; i < this.mUpdateInstruction.updateStepInfoList.size(); ++i) {
            this.checkCancellation();
            this.mTimeReport.flashingTimeReportList.add(this.executeUpdateStep(i, this.mUpdateInstruction.updateStepInfoList.get(i)));
        }
        this.checkCancellation();
        Log.d(UpdateFirmwareTask.TAG, "Start restoration stage");
        this.mStopWatch.reset();
        this.restorationStage(backupStage);
        if (!backupStage.isXUPSupported) {
            firmwareUpdateReport.isXUPUnlocked = this.mDevice.isBroadcastModeSupported();
        }
        this.mTimeReport.restorationTime = this.mStopWatch.elapsedTime();
        Log.d(UpdateFirmwareTask.TAG, String.format("Restoration stage completed. Elapsed time %.3f s", this.mTimeReport.restorationTime / 1000.0f));
        this.mStage = UpdateStage.FINISH;
        this.mTimeReport.totalTime = SystemClock.elapsedRealtime() - this.mTimeReport.startTime;
        Log.d(UpdateFirmwareTask.TAG, String.format("Finish firmware update task. Elapsed time %.3f", this.mTimeReport.totalTime / 1000.0f));
        return firmwareUpdateReport;
    }
    
    public static class DeviceBackupInfo
    {
        public UEAlarmInfo alarmInfo;
        public Boolean bleState;
        public Boolean gestureState;
        public Boolean isAlarmDailyRepeat;
        public Boolean isCustomEnabled;
        public Boolean isInPromiscuousMode;
        public Boolean isXUPSupported;
        public UELanguage language;
        public String name;
        public UESonificationProfile sonificationProfile;
        public Boolean stickyTwsOrPartyUpFlag;
    }
    
    public static class FirmwareFlashingTimeReport
    {
        public long flashingTime;
        public long rebootTime;
    }
    
    public static class FirmwareUpdateReport
    {
        public boolean isXUPUnlocked;
    }
    
    public static class FirmwareUpdateTimeReport
    {
        public long backupTime;
        public final ArrayList<FirmwareFlashingTimeReport> flashingTimeReportList;
        public long restorationTime;
        public long startTime;
        public long totalTime;
        
        public FirmwareUpdateTimeReport() {
            this.flashingTimeReportList = new ArrayList<FirmwareFlashingTimeReport>();
        }
    }
    
    public enum UpdateStage
    {
        ERASING, 
        FINISH, 
        FLASHING, 
        PREPARATION, 
        REBOOTING, 
        RESTORATION;
    }
}
