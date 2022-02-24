// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device;

import android.support.annotation.NonNull;
import com.logitech.ue.centurion.device.devicedata.UEAVRCP;
import com.logitech.ue.centurion.device.devicedata.UEFeature;
import com.logitech.ue.centurion.exceptions.UEException;
import com.logitech.ue.centurion.device.devicedata.UEVoiceState;
import com.logitech.ue.centurion.device.devicedata.UEVoiceCapabilities;
import com.logitech.ue.centurion.device.devicedata.UEPlaybackMetadataType;
import com.logitech.ue.centurion.device.devicedata.UEPartitionFiveInfo;
import com.logitech.ue.centurion.device.devicedata.UEOTAStatus;
import com.logitech.ue.centurion.exceptions.UEErrorResultException;
import com.logitech.ue.centurion.device.devicedata.UEAckResponse;
import com.logitech.ue.centurion.device.devicedata.UEDeviceType;
import com.logitech.ue.centurion.device.devicedata.UEChargingInfo;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastAudioOptions;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastState;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastConfiguration;
import java.io.UnsupportedEncodingException;
import com.logitech.ue.centurion.device.devicedata.UEBlockPartyState;
import com.logitech.ue.centurion.device.devicedata.UEBlockPartyInfo;
import com.logitech.ue.centurion.device.command.AlarmGetCommand;
import com.logitech.ue.centurion.device.devicedata.UEAlarmInfo;
import com.logitech.ue.centurion.utils.UEUtils;
import android.util.Log;
import com.logitech.ue.centurion.connection.UEDeviceConnector;
import com.logitech.ue.centurion.interfaces.IUEMessageFilter;
import com.logitech.ue.centurion.device.devicedata.UESoundProfile;
import java.util.Arrays;
import com.logitech.ue.centurion.exceptions.UEUnrecognisedCommandException;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastAudioMode;
import com.logitech.ue.centurion.interfaces.IUEDeviceCommand;
import com.logitech.ue.centurion.device.command.AlarmSetCommand;
import com.logitech.ue.centurion.device.command.UEDeviceCommand;
import com.logitech.ue.centurion.exceptions.UEConnectionException;
import com.logitech.ue.centurion.exceptions.UEOperationException;
import com.logitech.ue.centurion.device.command.UEOTADeviceCommand;
import com.logitech.ue.centurion.device.command.UEOTACommand;
import com.logitech.ue.centurion.device.devicedata.UEColour;
import com.logitech.ue.centurion.connection.IUEConnector;
import com.logitech.ue.centurion.utils.MAC;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStreamingStatus;
import com.logitech.ue.centurion.device.devicedata.UESonificationProfile;
import java.util.ArrayList;
import com.logitech.ue.centurion.device.devicedata.UELanguage;
import com.logitech.ue.centurion.device.devicedata.UEHardwareInfo;
import com.logitech.ue.centurion.device.devicedata.UEFeaturesInfo;
import com.logitech.ue.centurion.device.devicedata.UEEQSetting;
import com.logitech.ue.centurion.device.devicedata.UEAudioRouting;

public class UESPPDevice extends UEGenericDevice
{
    public static final int BATTERY_LOW = 20;
    public static final int BLUETOOTH_NAME_LENGTH_ACCEPTED = 48;
    private static final short RETRY_LIMIT = 10;
    private static final String TAG;
    protected UEAudioRouting mAudioRoutingCache;
    protected Byte mBalanceCache;
    protected String mBluetoothNameCache;
    protected byte[] mCustomEQCache;
    protected int mDeviceColorCache;
    protected String mDeviceIDCache;
    protected String mDeviceSerialNumberCache;
    protected UEEQSetting mEQSettingsCache;
    protected UEFeaturesInfo mFeatureInfoCache;
    protected String mFirmwareVersionCache;
    protected Boolean mGestureState;
    protected UEHardwareInfo mHardwareInfoCache;
    protected String mHardwareRevisionCache;
    protected Boolean mIs5BandSupportedCache;
    protected Boolean mIsAdjustVolumeSupported;
    protected Boolean mIsAlarmSupportedCache;
    protected Boolean mIsBTLESupported;
    protected Boolean mIsBalanceSupported;
    protected Boolean mIsBassBoostSupportedCache;
    protected Boolean mIsBleOnCache;
    protected Boolean mIsBlockPartySupported;
    protected Boolean mIsBroadcastModeSupported;
    protected Boolean mIsCustomStateOnCache;
    protected Boolean mIsFeatureSupportSupportedCache;
    protected Boolean mIsGestureSupported;
    protected Boolean mIsOTASupportedCache;
    protected Boolean mIsPromiscuousModeOnCache;
    protected Boolean mIsTWSSavePairOnCache;
    protected Boolean mIsVoiceSupported;
    protected Boolean mIsVolume32Supported;
    protected UELanguage mLanguageCache;
    protected ArrayList<Integer> mLanguageSupportCache;
    protected UESonificationProfile mSonificationProfileCache;
    protected UEDeviceStreamingStatus mStreamingStatusCache;
    
    static {
        TAG = UESPPDevice.class.getSimpleName();
    }
    
    public UESPPDevice(final MAC mac, final IUEConnector iueConnector) {
        super(mac, iueConnector);
        this.mDeviceColorCache = UEColour.UNKNOWN_COLOUR.getCode();
        this.mDeviceIDCache = null;
        this.mDeviceSerialNumberCache = null;
        this.mIsAlarmSupportedCache = null;
        this.mFirmwareVersionCache = null;
        this.mHardwareRevisionCache = null;
        this.mIsFeatureSupportSupportedCache = null;
        this.mIsVolume32Supported = null;
        this.mLanguageSupportCache = null;
        this.mIsBTLESupported = null;
        this.mIsOTASupportedCache = null;
        this.mIsAdjustVolumeSupported = null;
        this.mIsBlockPartySupported = null;
        this.mIsGestureSupported = null;
        this.mIsBassBoostSupportedCache = null;
        this.mIs5BandSupportedCache = null;
        this.mIsBalanceSupported = null;
        this.mIsBroadcastModeSupported = null;
        this.mIsVoiceSupported = null;
        this.mBluetoothNameCache = null;
        this.mLanguageCache = null;
        this.mSonificationProfileCache = null;
        this.mStreamingStatusCache = null;
        this.mAudioRoutingCache = null;
        this.mEQSettingsCache = null;
        this.mCustomEQCache = null;
        this.mIsTWSSavePairOnCache = null;
        this.mIsBleOnCache = null;
        this.mBalanceCache = null;
        this.mIsCustomStateOnCache = null;
        this.mGestureState = null;
        this.mIsPromiscuousModeOnCache = null;
        this.mHardwareInfoCache = null;
        this.mFeatureInfoCache = null;
    }
    
    @Override
    public void NOP() throws UEOperationException, UEConnectionException {
        this.execOTACommand(UEOTADeviceCommand.newCommand(UEOTACommand.NOP, null));
    }
    
    @Override
    public void ackAlarm() throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetAlarm, new byte[] { (byte)AlarmSetCommand.ACK_ALARM.getCode(), 0 }));
    }
    
    @Override
    public void addReceiverToBroadcast(final MAC mac, final UEBroadcastAudioMode ueBroadcastAudioMode) throws UEOperationException, UEConnectionException {
        final byte[] array = new byte[mac.getBytes().length + 1];
        System.arraycopy(mac.getBytes(), 0, array, 0, mac.getBytes().length);
        array[mac.getBytes().length] = (byte)ueBroadcastAudioMode.getCode();
        final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.AddReceiverToBroadcast, array);
        if (this.isEnableCache() && this.mIsBroadcastModeSupported != null && !this.mIsBroadcastModeSupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        this.execCommand(command);
    }
    
    @Override
    public void adjustVolume(final boolean b, final int n) throws UEOperationException, UEConnectionException {
        final UEDeviceCommand.UECommand adjustVolume = UEDeviceCommand.UECommand.AdjustVolume;
        byte b2;
        if (b) {
            b2 = 1;
        }
        else {
            b2 = 0;
        }
        final UEDeviceCommand command = UEDeviceCommand.newCommand(adjustVolume, new byte[] { b2, (byte)n });
        if (this.isEnableCache() && this.mIsAdjustVolumeSupported != null && !this.mIsAdjustVolumeSupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        try {
            this.execCommand(command);
            this.mIsAdjustVolumeSupported = true;
        }
        catch (UEUnrecognisedCommandException ex) {
            this.mIsAdjustVolumeSupported = false;
            throw ex;
        }
    }
    
    @Override
    public void announceBatteryLevel() throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.AnnounceBatteryLevel, null));
    }
    
    @Override
    public void beginDeviceDiscoveryMode() throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.BeginDeviceDiscovery, null));
    }
    
    @Override
    public void cancelOTA() throws UEOperationException, UEConnectionException {
        this.execOTACommand(UEOTADeviceCommand.newCommand(UEOTACommand.OTA_CANCEL, null));
    }
    
    @Override
    public byte[] checkPartitionState() throws UEOperationException, UEConnectionException {
        final byte[] execCommand = this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.CheckPartitionState, null));
        return Arrays.copyOfRange(execCommand, 3, execCommand.length);
    }
    
    @Override
    public void clearAlarm(final MAC mac) throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetAlarm, new byte[] { (byte)AlarmSetCommand.CLEAR.getCode(), 1 }));
    }
    
    @Override
    public void decreaseSlaveVolume() throws UEOperationException, UEConnectionException {
        this.adjustVolume(true, 1);
    }
    
    @Override
    public void decreaseVolume() throws UEOperationException, UEConnectionException {
        this.adjustVolume(false, 1);
    }
    
    @Override
    public void dropFirstLevelCache() {
        super.dropFirstLevelCache();
        this.mDeviceIDCache = null;
        this.mDeviceSerialNumberCache = null;
        this.mIsAlarmSupportedCache = null;
        this.mIsVolume32Supported = null;
        this.mFirmwareVersionCache = null;
        this.mIsFeatureSupportSupportedCache = null;
        this.mHardwareRevisionCache = null;
        this.mLanguageSupportCache = null;
        this.mIsBTLESupported = null;
        this.mIsOTASupportedCache = null;
        this.mIsAdjustVolumeSupported = null;
        this.mIsBlockPartySupported = null;
        this.mIsGestureSupported = null;
        this.mIsBroadcastModeSupported = null;
        this.mIsVoiceSupported = null;
    }
    
    @Override
    public void dropSecondLevelCache() {
        super.dropSecondLevelCache();
        this.mBluetoothNameCache = null;
        this.mLanguageCache = null;
        this.mSonificationProfileCache = null;
        this.mStreamingStatusCache = null;
        this.mAudioRoutingCache = null;
        this.mEQSettingsCache = null;
        this.mCustomEQCache = null;
        this.mIsTWSSavePairOnCache = null;
        this.mIsBleOnCache = null;
        this.mBalanceCache = null;
        this.mIsCustomStateOnCache = null;
        this.mGestureState = null;
        this.mIsPromiscuousModeOnCache = null;
    }
    
    @Override
    public void dropThirdLevelCache() {
        super.dropThirdLevelCache();
        this.mHardwareInfoCache = null;
        this.mFeatureInfoCache = null;
    }
    
    @Override
    public void emitSound(final UESoundProfile ueSoundProfile) throws UEOperationException, UEConnectionException {
        final int code = ueSoundProfile.getCode();
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.PlaySound, new byte[] { (byte)(code >> 8 & 0xFF), (byte)(code & 0xFF) }));
    }
    
    @Override
    public void erasePartition(final byte b) throws UEOperationException, UEConnectionException {
        this.execLongOTACommand(UEOTADeviceCommand.newCommand(UEOTACommand.ERASE_SQIF, new byte[] { b }));
    }
    
    public byte[] execCommand(final IUEDeviceCommand iueDeviceCommand) throws UEOperationException, UEConnectionException {
        return this.execCommand(iueDeviceCommand, null);
    }
    
    public byte[] execCommand(final IUEDeviceCommand iueDeviceCommand, final IUEMessageFilter iueMessageFilter) throws UEOperationException, UEConnectionException {
        if (this.getConnector() == null || !(this.getConnector() instanceof UEDeviceConnector)) {
            Log.d(UESPPDevice.TAG, "Device connector is not connected. Can't send messages.");
            throw new UEConnectionException("Connector is not connected. Call open connection first");
        }
        final long currentTimeMillis = System.currentTimeMillis();
        Log.d(UESPPDevice.TAG, "STARTING: " + iueDeviceCommand.getCommandName());
        final byte[] executeMessage = ((UEDeviceConnector)this.getConnector()).executeMessage(iueDeviceCommand, iueMessageFilter);
        Log.d(UESPPDevice.TAG, String.format("ENDING: %s. TIME ELAPSED: %d ms", iueDeviceCommand.getCommandName(), System.currentTimeMillis() - currentTimeMillis));
        return executeMessage;
    }
    
    public byte[] execCommand(final byte[] array) throws UEOperationException, UEConnectionException {
        return this.execCommand(array, 1000);
    }
    
    public byte[] execCommand(final byte[] array, final int n) throws UEOperationException, UEConnectionException {
        if (this.getConnector() == null || !(this.getConnector() instanceof UEDeviceConnector)) {
            Log.d(UESPPDevice.TAG, "Device connector is not connected. Can't send messages.");
            throw new UEConnectionException("Connector is not connected. Call open connection first");
        }
        final long currentTimeMillis = System.currentTimeMillis();
        Log.d(UESPPDevice.TAG, "STARTING: " + UEUtils.byteArrayToFancyHexString(array));
        final byte[] executeMessage = ((UEDeviceConnector)this.getConnector()).executeMessage(array, n);
        Log.d(UESPPDevice.TAG, String.format("ENDING: %s. TIME ELAPSED: %d ms", UEUtils.byteArrayToFancyHexString(array), System.currentTimeMillis() - currentTimeMillis));
        return executeMessage;
    }
    
    public byte[] execLongOTACommand(final UEOTADeviceCommand ueotaDeviceCommand) throws UEOperationException, UEConnectionException {
        synchronized (this) {
            if (this.getConnector() == null) {
                throw new UEConnectionException("Connector is not connected. Call open connection first");
            }
        }
        final IUEDeviceCommand iueDeviceCommand;
        // monitorexit(this)
        return ((UEDeviceConnector)this.getConnector()).executeOTAMessage(iueDeviceCommand, true);
    }
    
    public byte[] execOTACommand(final UEOTADeviceCommand ueotaDeviceCommand) throws UEOperationException, UEConnectionException {
        synchronized (this) {
            if (this.getConnector() == null) {
                throw new UEConnectionException("Connector is not connected. Call open connection first");
            }
        }
        final IUEDeviceCommand iueDeviceCommand;
        // monitorexit(this)
        return ((UEDeviceConnector)this.getConnector()).executeOTAMessage(iueDeviceCommand);
    }
    
    @Override
    public int get16Volume() throws UEOperationException, UEConnectionException {
        return this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryVolume, null))[3];
    }
    
    @Override
    public int get32Volume() throws UEOperationException, UEConnectionException {
        return this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.Query32Volume, null))[3];
    }
    
    @Override
    public UEAlarmInfo getAlarmInfo() throws UEOperationException, UEConnectionException {
        return UEAlarmInfo.buildFromCenturionMessage(this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryAlarm, new byte[] { (byte)AlarmGetCommand.ALARM_STATE.getCode() })));
    }
    
    @Override
    public int getAlarmVolume() throws UEOperationException, UEConnectionException {
        final byte[] array = { 0 };
        if (this.isVolume32Supported()) {
            array[0] = (byte)AlarmGetCommand.VOLUME32.getCode();
        }
        else {
            array[0] = (byte)AlarmGetCommand.VOLUME16.getCode();
        }
        return this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryAlarm, array))[4];
    }
    
    @Override
    public UEAudioRouting getAudioRouting() throws UEOperationException, UEConnectionException {
        UEAudioRouting ueAudioRouting;
        if (this.isEnableCache() && this.mAudioRoutingCache != null) {
            ueAudioRouting = this.mAudioRoutingCache;
        }
        else {
            final byte[] execCommand = this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryAudioRouting, null));
            final byte[] array = new byte[2];
            System.arraycopy(execCommand, 3, array, 0, 2);
            this.mAudioRoutingCache = UEAudioRouting.getRouting(array[0]);
            ueAudioRouting = this.mAudioRoutingCache;
        }
        return ueAudioRouting;
    }
    
    @Override
    public boolean getBLEState() throws UEOperationException, UEConnectionException {
        boolean b = true;
        final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryBLEState, null);
        if (this.isEnableCache() && this.mIsBTLESupported != null && !this.mIsBTLESupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        boolean b2;
        if (this.isEnableCache() && this.mIsBleOnCache != null) {
            b2 = this.mIsBleOnCache;
        }
        else {
            try {
                if (this.execCommand(command)[3] != 1) {
                    b = false;
                }
                this.mIsBleOnCache = b;
                this.mIsBTLESupported = true;
                b2 = this.mIsBleOnCache;
            }
            catch (UEUnrecognisedCommandException ex) {
                this.mIsBTLESupported = false;
                throw ex;
            }
        }
        return b2;
    }
    
    @Override
    public int getBatteryLevel() throws UEOperationException, UEConnectionException {
        return this.getChargingInfo().getCharge();
    }
    
    @Override
    public UEBlockPartyInfo getBlockPartyInfo() throws UEOperationException, UEConnectionException {
        return new UEBlockPartyInfo(this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryBlockPartyInfo, null)));
    }
    
    @Override
    public UEBlockPartyState getBlockPartyState() throws UEOperationException, UEConnectionException {
        final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryBlockPartyState, null);
        if (this.isEnableCache() && this.mIsBlockPartySupported != null && !this.mIsBlockPartySupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        final UEBlockPartyState state = UEBlockPartyState.getState(this.execCommand(command)[3]);
        this.mIsBlockPartySupported = true;
        return state;
    }
    
    @Override
    public String getBluetoothName() throws UEOperationException, UEConnectionException {
        String s;
        if (this.isEnableCache() && this.mBluetoothNameCache != null) {
            s = this.mBluetoothNameCache;
        }
        else {
            final byte[] execCommand = this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryBluetoothName, null));
            final int n = execCommand.length - 4;
            final byte[] bytes = new byte[n];
            System.arraycopy(execCommand, 3, bytes, 0, n);
            while (true) {
                try {
                    this.mBluetoothNameCache = new String(bytes, "UTF-8");
                    s = this.mBluetoothNameCache;
                }
                catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                    continue;
                }
                break;
            }
        }
        return s;
    }
    
    @Override
    public UEBroadcastConfiguration getBroadcastMode() throws UEOperationException, UEConnectionException {
        final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryBroadcastMode, null);
        if (this.isEnableCache() && this.mIsBroadcastModeSupported != null && !this.mIsBroadcastModeSupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        final byte[] execCommand = this.execCommand(command);
        final UEBroadcastConfiguration ueBroadcastConfiguration = new UEBroadcastConfiguration(UEBroadcastState.getState(execCommand[3]), UEBroadcastAudioOptions.getAudioOptions(execCommand[4]));
        this.mIsBroadcastModeSupported = true;
        return ueBroadcastConfiguration;
    }
    
    @Override
    public int getChargerRegister() throws UEOperationException, UEConnectionException {
        return this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryChargerRegister, null))[3];
    }
    
    @Override
    public UEChargingInfo getChargingInfo() throws UEOperationException, UEConnectionException {
        return new UEChargingInfo(this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryChargingInfo, null)));
    }
    
    @Override
    public String getConnectedDeviceNameRequest(final MAC mac) throws UEOperationException, UEConnectionException {
        final byte[] execCommand = this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryConnectedDeviceNameRequest, mac.getBytes()), new IUEMessageFilter() {
            @Override
            public boolean filter(final byte[] array) {
                boolean b = true;
                if (array[4] != mac.getBytes()[1] || array[5] != mac.getBytes()[2] || array[6] != mac.getBytes()[3] || array[7] != mac.getBytes()[4] || array[8] != mac.getBytes()[5]) {
                    b = false;
                }
                return b;
            }
        });
        final int n = execCommand.length - 10;
        final byte[] bytes = new byte[n];
        System.arraycopy(execCommand, 9, bytes, 0, n);
        try {
            return new String(bytes, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return "";
        }
    }
    
    @Override
    public byte[] getCustomEQ() throws UEOperationException, UEConnectionException {
        byte[] array;
        if (this.isEnableCache() && this.mCustomEQCache != null) {
            array = this.mCustomEQCache;
        }
        else {
            System.arraycopy(this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryEQTable, null)), 3, this.mCustomEQCache = new byte[6], 0, 6);
            array = this.mCustomEQCache;
        }
        return array;
    }
    
    @Override
    public boolean getCustomState() throws UEOperationException, UEConnectionException {
        boolean b = true;
        boolean b2;
        if (this.isEnableCache() && this.mIsCustomStateOnCache != null) {
            b2 = this.mIsCustomStateOnCache;
        }
        else {
            if (this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryCustomState, null))[3] != 1) {
                b = false;
            }
            this.mIsCustomStateOnCache = b;
            b2 = this.mIsCustomStateOnCache;
        }
        return b2;
    }
    
    @Override
    public int getDeviceColor() throws UEOperationException, UEConnectionException {
        if (this.mDeviceColorCache < 0) {
            this.getHardwareInfo();
        }
        return this.mDeviceColorCache;
    }
    
    @Override
    public String getDeviceID() throws UEOperationException, UEConnectionException {
        String s;
        if (this.isEnableCache() && this.mDeviceIDCache != null) {
            s = this.mDeviceIDCache;
        }
        else {
            final byte[] execCommand = this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryDeviceId, null));
            final int n = execCommand.length - 3 - 1;
            final byte[] bytes = new byte[n];
            System.arraycopy(execCommand, 3, bytes, 0, n);
            while (true) {
                try {
                    this.mDeviceIDCache = new String(bytes, "UTF-8");
                    if (this.mDeviceIDCache != null && this.mDeviceIDCache.startsWith("MEGABOO")) {
                        this.mDeviceIDCache = "MEGABOOM";
                    }
                    s = this.mDeviceIDCache;
                }
                catch (UnsupportedEncodingException ex) {
                    continue;
                }
                break;
            }
        }
        return s;
    }
    
    @Override
    public UEDeviceStreamingStatus getDeviceStreamingStatus() throws UEOperationException, UEConnectionException {
        UEDeviceStreamingStatus ueDeviceStreamingStatus;
        if (this.isEnableCache() && this.mStreamingStatusCache != null) {
            ueDeviceStreamingStatus = this.mStreamingStatusCache;
        }
        else {
            this.mStreamingStatusCache = UEDeviceStreamingStatus.getStatus(this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryDeviceStatus, null))[3]);
            ueDeviceStreamingStatus = this.mStreamingStatusCache;
        }
        return ueDeviceStreamingStatus;
    }
    
    @Override
    public UEDeviceType getDeviceType() throws UEOperationException, UEConnectionException {
        return UEDeviceType.getDeviceTypeByID(this.getDeviceID());
    }
    
    @Override
    public UEEQSetting getEQSetting() throws UEOperationException, UEConnectionException {
        return this.mEQSettingsCache = UEEQSetting.getEQSetting(this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryEQSetting, null))[3]);
    }
    
    @Override
    public int getEnableNotificationsMask() throws UEOperationException, UEConnectionException {
        final byte[] execCommand = this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryEnableNotifications, null));
        return 0x0 | execCommand[3] << 24 | execCommand[4] << 16 | execCommand[5] << 8 | execCommand[6];
    }
    
    @Override
    public UEFeaturesInfo getFeaturesInfo() throws UEOperationException, UEConnectionException {
        boolean b = true;
        UEFeaturesInfo mFeatureInfoCache;
        if (this.isEnableCache() && this.mFeatureInfoCache != null) {
            mFeatureInfoCache = this.mFeatureInfoCache;
        }
        else {
            final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryFeatures, null);
            if (this.isEnableCache() && this.mIsFeatureSupportSupportedCache != null && !this.mIsFeatureSupportSupportedCache) {
                throw new UEUnrecognisedCommandException(command);
            }
            while (true) {
                while (true) {
                    try {
                        final byte[] execCommand = this.execCommand(command);
                        final byte b2 = execCommand[3];
                        final byte b3 = execCommand[4];
                        final byte b4 = execCommand[5];
                        final byte b5 = execCommand[6];
                        final byte b6 = execCommand[7];
                        final byte b7 = execCommand[8];
                        final byte b8 = execCommand[9];
                        final byte b9 = execCommand[10];
                        if (execCommand[11] == 1) {
                            mFeatureInfoCache = new UEFeaturesInfo((b2 << 24 & b3 << 16) | b4 << 8 | b5, (b6 << 24 & b7 << 16) | b8 << 8 | b9, b);
                            break;
                        }
                    }
                    catch (UEUnrecognisedCommandException ex) {
                        this.mIsFeatureSupportSupportedCache = false;
                        throw ex;
                    }
                    b = false;
                    continue;
                }
            }
        }
        return mFeatureInfoCache;
    }
    
    @Override
    public String getFirmwareVersion() throws UEOperationException, UEConnectionException {
        String s;
        if (this.isEnableCache() && this.mFirmwareVersionCache != null) {
            s = this.mFirmwareVersionCache;
        }
        else {
            final byte[] execCommand = this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryFirmwareVersion, null));
            this.mFirmwareVersionCache = String.format("%d.%d.%d", execCommand[3] & 0xFF, execCommand[4] & 0xFF, execCommand[5] & 0xFF);
            s = this.mFirmwareVersionCache;
        }
        return s;
    }
    
    @Override
    public boolean getGestureState() throws UEOperationException, UEConnectionException {
        boolean b;
        if (this.isEnableCache() && this.mGestureState != null) {
            b = this.mGestureState;
        }
        else {
            final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryGestureEnable, null);
            if (this.isEnableCache() && this.mIsGestureSupported != null && !this.mIsGestureSupported) {
                throw new UEUnrecognisedCommandException(command);
            }
            this.mGestureState = (this.execCommand(command)[3] == 1);
            this.mIsGestureSupported = true;
            b = this.mGestureState;
        }
        return b;
    }
    
    @Override
    public UEHardwareInfo getHardwareInfo() throws UEOperationException, UEConnectionException {
        UEHardwareInfo mHardwareInfoCache;
        if (this.isEnableCache() && this.mHardwareInfoCache != null) {
            mHardwareInfoCache = this.mHardwareInfoCache;
        }
        else {
            final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryHardwareId, null);
            final byte[] execCommand = this.execCommand(command);
            int n = 0;
            if (execCommand.length < 7) {
                Log.e(UESPPDevice.TAG, "getHardwareInfo got faulty response " + UEUtils.byteArrayToNormalHexString(execCommand));
                throw new UEErrorResultException(command, UEAckResponse.COMMAND_FAIL);
            }
            mHardwareInfoCache = new UEHardwareInfo(execCommand);
        Label_0326_Outer:
            while (true) {
                Label_0383: {
                    Label_0161: {
                        if (mHardwareInfoCache.getSecondaryDeviceColour() == 0 && this.getDeviceStreamingStatus().isDevicePairedStatus()) {
                            if (n <= 10) {
                                break Label_0161;
                            }
                            Log.e(UESPPDevice.TAG, "Failed on correctly fetching secondary device info");
                        }
                        if (mHardwareInfoCache.getSecondaryDeviceColour() == 0 && this.getDeviceStreamingStatus().isDevicePairedStatus()) {
                            this.mHardwareInfoCache = null;
                            break;
                        }
                        break Label_0383;
                    }
                    Log.w(UESPPDevice.TAG, "Secondary device info error. Waiting 250ms for retrying...");
                    UEHardwareInfo ueHardwareInfo = mHardwareInfoCache;
                    int n2 = n;
                    while (true) {
                        byte[] execCommand2 = null;
                        Label_0357: {
                            try {
                                // monitorenter(this)
                                ueHardwareInfo = mHardwareInfoCache;
                                n2 = n;
                                try {
                                    this.wait(250L);
                                    ueHardwareInfo = mHardwareInfoCache;
                                    n2 = n;
                                    execCommand2 = this.execCommand(command);
                                    ueHardwareInfo = mHardwareInfoCache;
                                    n2 = n;
                                    if (execCommand2.length < 7) {
                                        ueHardwareInfo = mHardwareInfoCache;
                                        n2 = n;
                                        final String tag = UESPPDevice.TAG;
                                        ueHardwareInfo = mHardwareInfoCache;
                                        n2 = n;
                                        ueHardwareInfo = mHardwareInfoCache;
                                        n2 = n;
                                        final StringBuilder sb = new StringBuilder();
                                        ueHardwareInfo = mHardwareInfoCache;
                                        n2 = n;
                                        Log.e(tag, sb.append("getHardwareInfo got faulty response ").append(UEUtils.byteArrayToNormalHexString(execCommand2)).toString());
                                        ueHardwareInfo = mHardwareInfoCache;
                                        n2 = n;
                                        ueHardwareInfo = mHardwareInfoCache;
                                        n2 = n;
                                        final UEErrorResultException ex = new UEErrorResultException(command, UEAckResponse.COMMAND_FAIL);
                                        ueHardwareInfo = mHardwareInfoCache;
                                        n2 = n;
                                        throw ex;
                                    }
                                    break Label_0357;
                                }
                                finally {
                                    n = n2;
                                    mHardwareInfoCache = ueHardwareInfo;
                                }
                                ueHardwareInfo = mHardwareInfoCache;
                                n2 = n;
                                // monitorexit(this)
                                ueHardwareInfo = mHardwareInfoCache;
                                n2 = n;
                                throw;
                            }
                            catch (InterruptedException ex2) {
                                ex2.printStackTrace();
                                mHardwareInfoCache = ueHardwareInfo;
                                n = n2;
                                continue Label_0326_Outer;
                            }
                        }
                        n2 = n;
                        mHardwareInfoCache = new UEHardwareInfo(execCommand2);
                        n = (short)(n + 1);
                        try {
                            // monitorexit(this)
                            continue Label_0326_Outer;
                            this.mHardwareInfoCache = mHardwareInfoCache;
                            break;
                        }
                        finally {
                            final UEHardwareInfo ueHardwareInfo2 = ueHardwareInfo;
                            continue;
                        }
                        break;
                    }
                }
            }
            this.mDeviceColorCache = mHardwareInfoCache.getPrimaryDeviceColour();
        }
        return mHardwareInfoCache;
    }
    
    @Override
    public String getHardwareRevision() throws UEOperationException, UEConnectionException {
        String s;
        if (this.isEnableCache() && this.mHardwareRevisionCache != null) {
            s = this.mHardwareRevisionCache;
        }
        else {
            final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryHardwareRevision, null);
            while (true) {
                try {
                    final byte[] execCommand = this.execCommand(command);
                    this.mHardwareRevisionCache = String.format("%d.%d.%d", execCommand[3], execCommand[4], execCommand[5]);
                    s = this.mHardwareRevisionCache;
                }
                catch (UEUnrecognisedCommandException ex) {
                    Log.d(UESPPDevice.TAG, "hardware revision unsupported. Assuming version 1.0.0");
                    this.mHardwareRevisionCache = String.format("%d.%d.%d", 1, 0, 0);
                    continue;
                }
                break;
            }
        }
        return s;
    }
    
    @Override
    public UELanguage getLanguage() throws UEOperationException, UEConnectionException {
        UELanguage ueLanguage;
        if (this.isEnableCache() && this.mLanguageCache != null) {
            ueLanguage = this.mLanguageCache;
        }
        else {
            this.mLanguageCache = UELanguage.getLanguage(this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryLanguageSetting, null))[3]);
            ueLanguage = this.mLanguageCache;
        }
        return ueLanguage;
    }
    
    @Override
    public UEOTAStatus getOTAStatus() throws UEOperationException, UEConnectionException {
        return UEOTAStatus.getStatus(this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryOTAState, null))[3]);
    }
    
    @Override
    public UEPartitionFiveInfo getPartitionFiveInfo() throws UEOperationException, UEConnectionException {
        return new UEPartitionFiveInfo(this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryPartitionFiveInfo, null)));
    }
    
    @Override
    public int getPartitionState() throws UEOperationException, UEConnectionException {
        final byte[] execCommand = this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.GetPartitionMountState, null));
        return (execCommand[3] << 8) + execCommand[4];
    }
    
    @Override
    public MAC getPartyCurrentStreamingDeviceAddress() throws UEOperationException, UEConnectionException {
        return new MAC(this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryBlockPartyCurrentStreamingDeviceMAC, null)), 3);
    }
    
    @Override
    public byte[] getPhoneBluetoothAddress() throws UEOperationException, UEConnectionException {
        final byte[] execCommand = this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QuerySourceBluetoothAddress, null));
        final byte[] array = new byte[6];
        System.arraycopy(execCommand, 3, array, 0, 6);
        return array;
    }
    
    @Override
    public Object getPlaybackMetadataRequest(final MAC mac, final UEPlaybackMetadataType uePlaybackMetadataType) throws UEOperationException, UEConnectionException {
        final byte[] array = new byte[7];
        System.arraycopy(mac.getBytes(), 0, array, 0, 6);
        array[6] = (byte)uePlaybackMetadataType.getCode();
        final byte[] execCommand = this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryPlaybackMetadata, array), new IUEMessageFilter() {
            @Override
            public boolean filter(final byte[] array) {
                boolean b = true;
                final UEPlaybackMetadataType playbackDataType = UEPlaybackMetadataType.getPlaybackDataType(array[9]);
                if (array[4] != mac.getBytes()[1] || array[5] != mac.getBytes()[2] || array[6] != mac.getBytes()[3] || array[7] != mac.getBytes()[4] || array[8] != mac.getBytes()[5] || uePlaybackMetadataType != playbackDataType) {
                    b = false;
                }
                return b;
            }
        });
        Object o = new byte[execCommand.length - 11];
        System.arraycopy(execCommand, 10, o, 0, ((byte[])o).length);
        switch (uePlaybackMetadataType) {
            case TITLE: {
                o = UEUtils.byteArrayToUTF8String((byte[])o);
                break;
            }
            case ARTIST: {
                o = UEUtils.byteArrayToUTF8String((byte[])o);
                break;
            }
            case ALBUM: {
                o = UEUtils.byteArrayToUTF8String((byte[])o);
                break;
            }
        }
        return o;
    }
    
    @Override
    public String getProtocolVersion() throws UEOperationException, UEConnectionException {
        final byte[] execCommand = this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryProtocolVersion, new byte[] { Byte.parseByte("0100".substring(0, 2)), Byte.parseByte("0100".substring(2)) }));
        return String.format("%d.%d", execCommand[3], execCommand[4]);
    }
    
    @Override
    public void getReceiverFixedAttributes(final MAC mac) throws UEOperationException, UEConnectionException {
        final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryReceiverFixedAttributes, mac.getBytes());
        if (this.isEnableCache() && this.mIsBroadcastModeSupported != null && !this.mIsBroadcastModeSupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        this.execCommand(command);
    }
    
    @Override
    public void getReceiverOneAttribute(final MAC mac, final int n) throws UEOperationException, UEConnectionException {
        final byte[] array = new byte[8];
        System.arraycopy(mac.getBytes(), 0, array, 0, mac.getBytes().length);
        array[6] = (byte)(n >> 8 & 0xFF);
        array[7] = (byte)(n & 0xFF);
        final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryReceiverOneAttribute, array);
        if (this.isEnableCache() && this.mIsBroadcastModeSupported != null && !this.mIsBroadcastModeSupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        this.execCommand(command);
    }
    
    @Override
    public void getReceiverVariableAttributes(final MAC mac) throws UEOperationException, UEConnectionException {
        final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryReceiverVariableAttributes, mac.getBytes());
        if (this.isEnableCache() && this.mIsBroadcastModeSupported != null && !this.mIsBroadcastModeSupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        this.execCommand(command);
    }
    
    @Override
    public boolean getRepeatAlarm() throws UEOperationException, UEConnectionException {
        boolean b = true;
        if (this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryAlarm, new byte[] { (byte)AlarmGetCommand.REPEAT_DAY.getCode() }))[4] != 127) {
            b = false;
        }
        return b;
    }
    
    @Override
    public String getSerialNumber() throws UEOperationException, UEConnectionException {
        String s;
        if (this.isEnableCache() && this.mDeviceSerialNumberCache != null) {
            s = this.mDeviceSerialNumberCache;
        }
        else {
            final byte[] execCommand = this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QuerySerialNumber, null));
            final int n = execCommand.length - 4;
            final byte[] bytes = new byte[n];
            System.arraycopy(execCommand, 3, bytes, 0, n);
            while (true) {
                try {
                    this.mDeviceSerialNumberCache = new String(bytes, "US-ASCII");
                    s = this.mDeviceSerialNumberCache;
                }
                catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                    continue;
                }
                break;
            }
        }
        return s;
    }
    
    @Override
    public byte getSlaveBatteryLevel() throws UEOperationException, UEConnectionException {
        return this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QuerySlaveBatteryLevel, null))[3];
    }
    
    @Override
    public int getSlaveVolume() throws UEOperationException, UEConnectionException {
        return this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QuerySlaveVolume, null))[3];
    }
    
    @Override
    public int getSonificationFileSize(final short n) throws UEOperationException, UEConnectionException {
        final byte[] execCommand = this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.GetSonificationFileSize, new byte[] { (byte)(n >> 8 & 0xFF), (byte)(n & 0xFF) }));
        int n2;
        if (execCommand.length >= 7) {
            n2 = (execCommand[3] << 24 | (execCommand[4] & 0xFF) << 16 | (execCommand[5] & 0xFF) << 8 | (execCommand[6] & 0xFF));
        }
        else {
            n2 = -1;
        }
        return n2;
    }
    
    @Override
    public UESonificationProfile getSonificationProfile() throws UEOperationException, UEConnectionException {
        UESonificationProfile ueSonificationProfile;
        if (this.isEnableCache() && this.mSonificationProfileCache != null) {
            ueSonificationProfile = this.mSonificationProfileCache;
        }
        else {
            this.mSonificationProfileCache = UESonificationProfile.getProfile(this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QuerySonfication, null))[3]);
            ueSonificationProfile = this.mSonificationProfileCache;
        }
        return ueSonificationProfile;
    }
    
    @Override
    public boolean getStickyTWSOrPartyUpFlag() throws UEOperationException, UEConnectionException {
        boolean b = true;
        boolean b2;
        if (this.mIsTWSSavePairOnCache != null) {
            b2 = this.mIsTWSSavePairOnCache;
        }
        else {
            if (this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryTWSSavePairFlag, null))[3] != 1) {
                b = false;
            }
            this.mIsTWSSavePairOnCache = b;
            b2 = this.mIsTWSSavePairOnCache;
        }
        return b2;
    }
    
    @Override
    public ArrayList<Integer> getSupportedLanguageIndex() throws UEOperationException, UEConnectionException {
        ArrayList<Integer> list;
        if (this.isEnableCache() && this.mLanguageSupportCache != null) {
            list = this.mLanguageSupportCache;
        }
        else {
            final byte[] execCommand = this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QuerySupportedLanguage, null));
            this.mLanguageSupportCache = new ArrayList<Integer>();
            final int combineTwoBytesToOneInteger = UEUtils.combineTwoBytesToOneInteger(execCommand[3], execCommand[4]);
            for (int i = 0; i < 16; ++i) {
                if ((1 << i & combineTwoBytesToOneInteger) != 0x0) {
                    this.mLanguageSupportCache.add(i);
                }
            }
            list = this.mLanguageSupportCache;
        }
        return list;
    }
    
    @Override
    public byte getTWSBalance() throws UEOperationException, UEConnectionException {
        byte b;
        if (this.isEnableCache() && this.mBalanceCache != null) {
            b = this.mBalanceCache;
        }
        else {
            this.mBalanceCache = this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryTWSBalance, null))[3];
            b = this.mBalanceCache;
        }
        return b;
    }
    
    @Override
    public UEVoiceCapabilities getVoiceCapabilities() throws UEOperationException, UEConnectionException {
        final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.GetVoiceCapabilities, null);
        if (this.isEnableCache() && this.mIsVoiceSupported != null && !this.mIsVoiceSupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        final byte[] execCommand = this.execCommand(command);
        return UEVoiceCapabilities.getVoiceCapabilities((execCommand[3] << 8) + execCommand[4] & 0x3);
    }
    
    @Override
    public UEVoiceState getVoiceState() throws UEOperationException, UEConnectionException {
        final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.GetVoiceControlFlag, null);
        if (this.isEnableCache() && this.mIsVoiceSupported != null && !this.mIsVoiceSupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        return UEVoiceState.getVoiceSate(this.execCommand(command)[3]);
    }
    
    @Override
    public int getVolume() throws UEOperationException, UEConnectionException {
        Label_0039: {
            if (this.mIsVolume32Supported != null) {
                break Label_0039;
            }
            try {
                final int n = this.get32Volume();
                this.mIsVolume32Supported = true;
                return n;
            }
            catch (UEException ex) {
                this.mIsVolume32Supported = false;
                return this.get16Volume();
            }
        }
        if (this.mIsVolume32Supported) {
            return this.get32Volume();
        }
        return this.get32Volume();
    }
    
    @Override
    public void increaseSlaveVolume() throws UEOperationException, UEConnectionException {
        this.adjustVolume(true, 0);
    }
    
    @Override
    public void increaseVolume() throws UEOperationException, UEConnectionException {
        this.adjustVolume(false, 0);
    }
    
    @Override
    public boolean is5BandEQSupported() {
        boolean b = false;
        if (this.mIs5BandSupportedCache != null) {
            b = this.mIs5BandSupportedCache;
        }
        else {
        Label_0072_Outer:
            while (true) {
            Label_0072:
                while (true) {
                Label_0144:
                    while (true) {
                        Label_0133: {
                            try {
                                final String deviceID = this.getDeviceID();
                                if (deviceID.matches(UEDeviceType.Kora.getDeviceIDPattern())) {
                                    this.mIs5BandSupportedCache = this.getFeaturesInfo().doesMasterSupportFeature(UEFeature.Band5EQ);
                                }
                                else {
                                    if (!deviceID.matches(UEDeviceType.RedRocks.getDeviceIDPattern())) {
                                        break Label_0133;
                                    }
                                    this.mIs5BandSupportedCache = false;
                                }
                                if (this.mIs5BandSupportedCache) {
                                    Log.d(UESPPDevice.TAG, "5BandEQ is supported");
                                    b = this.mIs5BandSupportedCache;
                                    break;
                                }
                                break Label_0144;
                            }
                            catch (Exception ex) {
                                Log.d(UESPPDevice.TAG, "5BandEQ is not supported");
                                ex.printStackTrace();
                                this.mIs5BandSupportedCache = false;
                                continue Label_0072;
                            }
                        }
                        this.mIs5BandSupportedCache = true;
                        continue Label_0072_Outer;
                    }
                    Log.d(UESPPDevice.TAG, "5BandEQ is not supported");
                    continue Label_0072;
                }
            }
        }
        return b;
    }
    
    @Override
    public boolean isAlarmSupported() throws UEOperationException, UEConnectionException {
        boolean b;
        if (this.mIsAlarmSupportedCache != null) {
            b = this.mIsAlarmSupportedCache;
        }
        else {
            while (true) {
                try {
                    this.getAlarmInfo();
                    Log.i(UESPPDevice.TAG, "Alarm is supported");
                    this.mIsAlarmSupportedCache = true;
                    b = this.mIsAlarmSupportedCache;
                }
                catch (UEUnrecognisedCommandException ex) {
                    Log.w(UESPPDevice.TAG, "Alarm is not supported");
                    ex.printStackTrace();
                    this.mIsAlarmSupportedCache = false;
                    continue;
                }
                break;
            }
        }
        return b;
    }
    
    @Override
    public boolean isBLESupported() throws UEOperationException, UEConnectionException {
        boolean b;
        if (this.isEnableCache() && this.mIsBTLESupported != null) {
            b = this.mIsBTLESupported;
        }
        else {
            while (true) {
                try {
                    this.getBLEState();
                    this.mIsBTLESupported = true;
                    b = this.mIsBTLESupported;
                }
                catch (UEUnrecognisedCommandException ex) {
                    this.mIsBTLESupported = false;
                    continue;
                }
                break;
            }
        }
        return b;
    }
    
    @Override
    public boolean isBalanceSupported() {
        boolean b = false;
        if (this.mIsBalanceSupported != null) {
            b = this.mIsBalanceSupported;
        }
        else {
        Label_0072_Outer:
            while (true) {
            Label_0072:
                while (true) {
                Label_0144:
                    while (true) {
                        Label_0133: {
                            try {
                                final String deviceID = this.getDeviceID();
                                if (deviceID.matches(UEDeviceType.Kora.getDeviceIDPattern())) {
                                    this.mIsBalanceSupported = this.getFeaturesInfo().doesMasterSupportFeature(UEFeature.Balance);
                                }
                                else {
                                    if (!deviceID.matches(UEDeviceType.RedRocks.getDeviceIDPattern())) {
                                        break Label_0133;
                                    }
                                    this.mIsBalanceSupported = false;
                                }
                                if (this.mIsBalanceSupported) {
                                    Log.d(UESPPDevice.TAG, "Balance is supported");
                                    b = this.mIsBalanceSupported;
                                    break;
                                }
                                break Label_0144;
                            }
                            catch (Exception ex) {
                                Log.d(UESPPDevice.TAG, "Balance is not supported");
                                ex.printStackTrace();
                                this.mIsBalanceSupported = false;
                                continue Label_0072;
                            }
                        }
                        this.mIsBalanceSupported = true;
                        continue Label_0072_Outer;
                    }
                    Log.d(UESPPDevice.TAG, "Balance is not supported");
                    continue Label_0072;
                }
            }
        }
        return b;
    }
    
    @Override
    public boolean isBassBoostSupported() {
        final boolean b = true;
        boolean b2 = false;
        if (this.mIsBassBoostSupportedCache != null) {
            b2 = this.mIsBassBoostSupportedCache;
        }
        else {
        Label_0122_Outer:
            while (true) {
            Label_0122:
                while (true) {
                Label_0199:
                    while (true) {
                        Label_0188: {
                            try {
                                final String deviceID = this.getDeviceID();
                                if (deviceID.matches(UEDeviceType.Kora.getDeviceIDPattern())) {
                                    final String firmwareVersion = this.getFirmwareVersion();
                                    final String hardwareRevision = this.getHardwareRevision();
                                    boolean b3 = false;
                                    Label_0094: {
                                        if (UEUtils.compareVersions(firmwareVersion, "6.2") > 0) {
                                            b3 = b;
                                            if (UEUtils.compareVersions(hardwareRevision, "1.5.0") == 0) {
                                                break Label_0094;
                                            }
                                        }
                                        b3 = (UEUtils.compareVersions(firmwareVersion, "5.4") > 0 && UEUtils.compareVersions(hardwareRevision, "1.0.0") == 0 && b);
                                    }
                                    this.mIsBassBoostSupportedCache = b3;
                                }
                                else {
                                    if (!deviceID.matches(UEDeviceType.RedRocks.getDeviceIDPattern())) {
                                        break Label_0188;
                                    }
                                    this.mIsBassBoostSupportedCache = false;
                                }
                                if (this.mIsBassBoostSupportedCache) {
                                    Log.d(UESPPDevice.TAG, "Bass boost is supported");
                                    b2 = this.mIsBassBoostSupportedCache;
                                    break;
                                }
                                break Label_0199;
                            }
                            catch (Exception ex) {
                                Log.d(UESPPDevice.TAG, "Bass boost is not supported");
                                ex.printStackTrace();
                                this.mIsBassBoostSupportedCache = false;
                                continue Label_0122;
                            }
                        }
                        this.mIsBassBoostSupportedCache = true;
                        continue Label_0122_Outer;
                    }
                    Log.d(UESPPDevice.TAG, "Bass boost is not supported");
                    continue Label_0122;
                }
            }
        }
        return b2;
    }
    
    @Override
    public boolean isBlockPartySupported() throws UEOperationException, UEConnectionException {
        boolean b;
        if (this.isEnableCache() && this.mIsBlockPartySupported != null) {
            b = this.mIsBlockPartySupported;
        }
        else {
            while (true) {
                try {
                    this.getBlockPartyState();
                    Log.i(UESPPDevice.TAG, "Party mode is supported");
                    this.mIsBlockPartySupported = true;
                    b = this.mIsBlockPartySupported;
                }
                catch (UEUnrecognisedCommandException ex) {
                    Log.w(UESPPDevice.TAG, "Party mode is not supported");
                    ex.printStackTrace();
                    this.mIsBlockPartySupported = false;
                    continue;
                }
                break;
            }
        }
        return b;
    }
    
    @Override
    public boolean isBroadcastModeSupported() throws UEOperationException, UEConnectionException {
        boolean b;
        if (this.isEnableCache() && this.mIsBroadcastModeSupported != null) {
            b = this.mIsBroadcastModeSupported;
        }
        else {
            while (true) {
                try {
                    this.getBroadcastMode();
                    Log.i(UESPPDevice.TAG, "Broadcast Mode is supported");
                    this.mIsBroadcastModeSupported = true;
                    b = this.mIsBroadcastModeSupported;
                }
                catch (UEUnrecognisedCommandException ex) {
                    Log.w(UESPPDevice.TAG, "Broadcast Mode is not supported");
                    ex.printStackTrace();
                    this.mIsBroadcastModeSupported = false;
                    continue;
                }
                break;
            }
        }
        return b;
    }
    
    @Override
    public boolean isGestureSupported() throws UEOperationException, UEConnectionException {
        boolean b;
        if (this.isEnableCache() && this.mIsGestureSupported != null) {
            b = this.mIsGestureSupported;
        }
        else {
            while (true) {
                try {
                    this.getGestureState();
                    Log.i(UESPPDevice.TAG, "Gesture is supported");
                    this.mIsGestureSupported = true;
                    b = this.mIsGestureSupported;
                }
                catch (UEUnrecognisedCommandException ex) {
                    Log.w(UESPPDevice.TAG, "Gesture is not supported");
                    ex.printStackTrace();
                    this.mIsGestureSupported = false;
                    continue;
                }
                break;
            }
        }
        return b;
    }
    
    @Override
    public boolean isOTASupported() throws UEOperationException, UEConnectionException {
        boolean b;
        if (this.isEnableCache() && this.mIsOTASupportedCache != null) {
            b = this.mIsOTASupportedCache;
        }
        else {
            while (true) {
                try {
                    this.getOTAStatus();
                    Log.i(UESPPDevice.TAG, "OTA is supported");
                    this.mIsOTASupportedCache = true;
                    b = this.mIsOTASupportedCache;
                }
                catch (UEUnrecognisedCommandException ex) {
                    Log.w(UESPPDevice.TAG, "OTA is not supported");
                    ex.printStackTrace();
                    this.mIsOTASupportedCache = false;
                    continue;
                }
                break;
            }
        }
        return b;
    }
    
    @Override
    public boolean isPartitionValidForLanguage(final byte[] array) {
        return false;
    }
    
    @Override
    public boolean isVoiceSupported() throws UEOperationException, UEConnectionException {
        boolean b;
        if (this.isEnableCache() && this.mIsVoiceSupported != null) {
            b = this.mIsVoiceSupported;
        }
        else {
            while (true) {
                try {
                    this.getVoiceCapabilities();
                    Log.d(UESPPDevice.TAG, "Voice is supported");
                    this.mIsVoiceSupported = true;
                    b = this.mIsVoiceSupported;
                }
                catch (UEUnrecognisedCommandException ex) {
                    Log.w(UESPPDevice.TAG, "Voice is not supported");
                    ex.printStackTrace();
                    this.mIsVoiceSupported = false;
                    continue;
                }
                break;
            }
        }
        return b;
    }
    
    @Override
    public boolean isVolume32Supported() {
        boolean b;
        if (this.mIsVolume32Supported != null) {
            b = this.mIsVolume32Supported;
        }
        else {
            while (true) {
                try {
                    this.get32Volume();
                    this.mIsVolume32Supported = true;
                    b = this.mIsVolume32Supported;
                }
                catch (UEException ex) {
                    this.mIsVolume32Supported = false;
                    continue;
                }
                break;
            }
        }
        return b;
    }
    
    @Override
    public boolean isXUPPromiscuousModelOn() throws UEOperationException, UEConnectionException {
        final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.QueryXUPPromiscuousMode, null);
        if (this.isEnableCache() && this.mIsBroadcastModeSupported != null && !this.mIsBroadcastModeSupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        boolean b;
        if (this.mIsPromiscuousModeOnCache != null) {
            b = this.mIsPromiscuousModeOnCache;
        }
        else {
            this.mIsPromiscuousModeOnCache = (this.execCommand(command)[3] == 1);
            this.mIsBroadcastModeSupported = true;
            b = this.mIsPromiscuousModeOnCache;
        }
        return b;
    }
    
    @Override
    public void kickMemberFromParty(final MAC mac) throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.KickBlockPartyMember, mac.getBytes()));
    }
    
    @Override
    public void mountPartition(final int n, final int n2) throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.MountPartition, new byte[] { (byte)n, (byte)n2 }));
    }
    
    @Override
    public void remoteOff() throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.MasterRemoteOff, null));
    }
    
    @Override
    public void remoteOffSlave() throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SlaveRemoteOff, null));
    }
    
    @Override
    public void removeReceiverFromBroadcast(final MAC mac) throws UEOperationException, UEConnectionException {
        final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.RemoveReceiverFromBroadcast, mac.getBytes());
        if (this.isEnableCache() && this.mIsBroadcastModeSupported != null && !this.mIsBroadcastModeSupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        this.execCommand(command);
    }
    
    @Override
    public void runDFU(final byte b) throws UEOperationException, UEConnectionException {
        this.execOTACommand(UEOTADeviceCommand.newCommand(UEOTACommand.RUN_DFU, new byte[] { b }));
    }
    
    @Override
    public void sendAVRCPCommand(final UEAVRCP ueavrcp) throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SendAVRCPCommand, new byte[] { (byte)ueavrcp.getCode() }));
    }
    
    @Override
    public void sendAVRCPCommand(@NonNull final UEAVRCP ueavrcp, @NonNull final MAC mac) throws UEOperationException, UEConnectionException {
        final byte[] array = new byte[7];
        array[0] = (byte)ueavrcp.getCode();
        System.arraycopy(mac.getBytes(), 0, array, 1, 6);
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SendAVRCPCommand, array));
    }
    
    @Override
    public void set16Volume(final int n) throws UEOperationException, UEConnectionException {
        int n2;
        if (n < 0) {
            n2 = 0;
        }
        else if ((n2 = n) > 15) {
            n2 = 15;
        }
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetVolume, new byte[] { (byte)n2 }));
    }
    
    @Override
    public void set32Volume(final int n) throws UEOperationException, UEConnectionException {
        int n2;
        if (n < 0) {
            n2 = 0;
        }
        else if ((n2 = n) > 31) {
            n2 = 31;
        }
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.Set32Volume, new byte[] { (byte)n2 }));
    }
    
    @Override
    public void setAlarm(final long n, final MAC mac) throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetAlarm, new byte[] { (byte)AlarmSetCommand.SET_ALARM.getCode(), (byte)(n >> 24 & 0xFFL), (byte)(n >> 16 & 0xFFL), (byte)(n >> 8 & 0xFFL), (byte)(n & 0xFFL), 0 }));
    }
    
    @Override
    public void setAlarmBackupTone(final int n, final MAC mac) throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetAlarm, new byte[] { (byte)AlarmSetCommand.BACKUP_TONE.getCode(), 2 }));
    }
    
    @Override
    public void setAlarmVolume(final int n, final MAC mac) throws UEOperationException, UEConnectionException {
        final byte[] array = new byte[2];
        int n2;
        if (this.isVolume32Supported()) {
            array[0] = (byte)AlarmSetCommand.SET_ALARM_VOLUME_32.getCode();
            n2 = n;
            if (n < 0 && (n2 = n) > 31) {
                Log.e(UESPPDevice.TAG, "Volume value " + n + " is out of bound. Set volume to 10");
                n2 = 10;
            }
        }
        else {
            array[0] = (byte)AlarmSetCommand.SET_ALARM_VOLUME.getCode();
            n2 = n;
            if (n < 0 && (n2 = n) > 15) {
                Log.e(UESPPDevice.TAG, "Volume value " + n + " is out of bound. Set volume to 10");
                n2 = 10;
            }
        }
        array[1] = (byte)n2;
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetAlarm, array));
    }
    
    @Override
    public void setAudioRouting(final UEAudioRouting mAudioRoutingCache) throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetAudioRouting, new byte[] { (byte)mAudioRoutingCache.getCode(), 119 }));
        this.mAudioRoutingCache = mAudioRoutingCache;
    }
    
    @Override
    public void setBLEState(final boolean b) throws UEOperationException, UEConnectionException {
        boolean b2 = true;
        if (!b) {
            b2 = false;
        }
        final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetBTLEState, new byte[] { (byte)(b2 ? 1 : 0) });
        if (this.isEnableCache() && this.mIsBTLESupported != null && !this.mIsBTLESupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        try {
            this.execCommand(command);
            this.mIsBleOnCache = b;
            this.mIsBTLESupported = true;
        }
        catch (UEUnrecognisedCommandException ex) {
            this.mIsBTLESupported = false;
            throw ex;
        }
    }
    
    @Override
    public void setBlockPartyState(final boolean b) throws UEOperationException, UEConnectionException {
        boolean b2 = true;
        if (!b) {
            b2 = false;
        }
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetBlockPartyState, new byte[] { (byte)(b2 ? 1 : 0) }));
    }
    
    @Override
    public void setBluetoothName(final String mBluetoothNameCache) throws UEOperationException, UEConnectionException {
        try {
            final byte[] bytes = mBluetoothNameCache.getBytes("UTF-8");
            final int length = bytes.length;
            if (length != 0) {
                int n;
                if ((n = length) > 48) {
                    n = 48;
                }
                final byte[] array = new byte[n + 1];
                System.arraycopy(bytes, 0, array, 0, n);
                array[n] = 0;
                this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetBluetoothName, array));
                this.mBluetoothNameCache = mBluetoothNameCache;
            }
        }
        catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void setBroadcastMode(final UEBroadcastState ueBroadcastState, final UEBroadcastAudioOptions ueBroadcastAudioOptions) throws UEOperationException, UEConnectionException {
        final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetBroadcastMode, new byte[] { (byte)ueBroadcastState.getCode(), (byte)ueBroadcastAudioOptions.getCode() });
        if (this.isEnableCache() && this.mIsBroadcastModeSupported != null && !this.mIsBroadcastModeSupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        this.execCommand(command);
        this.mIsBroadcastModeSupported = true;
    }
    
    @Override
    public void setCustomEQ(final byte[] array) throws UEOperationException, UEConnectionException {
        if (array.length != 5) {
            throw new IllegalArgumentException("value param should have size 5");
        }
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetEQTable, array));
        this.mCustomEQCache = new byte[] { array[0], array[1], array[2], array[3], array[4], 1 };
    }
    
    @Override
    public void setCustomState(final boolean b) throws UEOperationException, UEConnectionException {
        boolean b2 = true;
        if (!b) {
            b2 = false;
        }
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetCustomState, new byte[] { (byte)(b2 ? 1 : 0) }));
        this.mIsCustomStateOnCache = b;
    }
    
    @Override
    public void setDeviceStreamingStatus(final UEDeviceStreamingStatus mStreamingStatusCache) {
        this.mStreamingStatusCache = mStreamingStatusCache;
    }
    
    @Override
    public void setEQSetting(final UEEQSetting meqSettingsCache) throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetEQSetting, new byte[] { (byte)UEEQSetting.getCode(meqSettingsCache) }));
        this.mEQSettingsCache = meqSettingsCache;
    }
    
    @Override
    public void setEnableNotificationsMask(final int n) throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetEnableNotifications, new byte[] { (byte)(n >> 24 & 0xFF), (byte)(n >> 16 & 0xFF), (byte)(n >> 8 & 0xFF), (byte)(n & 0xFF) }));
    }
    
    @Override
    public void setGestureState(final boolean b) throws UEOperationException, UEConnectionException {
        byte b2;
        if (b) {
            b2 = 1;
        }
        else {
            b2 = 0;
        }
        final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetGestureEnable, new byte[] { b2 });
        if (this.isEnableCache() && this.mIsGestureSupported != null && !this.mIsGestureSupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        this.execCommand(command);
        this.mGestureState = b;
        this.mIsGestureSupported = true;
    }
    
    @Override
    public void setLanguage(final UELanguage mLanguageCache) throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetLanguage, new byte[] { (byte)mLanguageCache.getCode() }));
        this.mLanguageCache = mLanguageCache;
    }
    
    @Override
    public void setOTAStatus(final UEOTAStatus ueotaStatus) throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetOTAState, new byte[] { (byte)ueotaStatus.getCode() }));
    }
    
    @Override
    public void setPartitionState(final int n) throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetPartitionMountState, new byte[] { (byte)(n >> 8 & 0xFF), (byte)(n & 0xFF) }));
    }
    
    @Override
    public void setReceiverIdentificationSignal(final MAC mac, final boolean b) throws UEOperationException, UEConnectionException {
        boolean b2 = false;
        final byte[] array = new byte[8];
        System.arraycopy(mac.getBytes(), 0, array, 0, mac.getBytes().length);
        if (!b) {
            b2 = true;
        }
        array[6] = (byte)(b2 ? 1 : 0);
        final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetReceiverIdentificationSignal, array);
        if (this.isEnableCache() && this.mIsBroadcastModeSupported != null && !this.mIsBroadcastModeSupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        this.execCommand(command);
    }
    
    @Override
    public void setReceiverOneAttribute(final MAC mac, final int n, final byte[] array) throws UEOperationException, UEConnectionException {
        final byte[] array2 = new byte[array.length + 8];
        System.arraycopy(mac.getBytes(), 0, array2, 0, mac.getBytes().length);
        array2[6] = (byte)(n >> 8 & 0xFF);
        array2[7] = (byte)(n & 0xFF);
        System.arraycopy(array2, 8, array, 0, array.length);
        final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetReceiverOneAttribute, array2);
        if (this.isEnableCache() && this.mIsBroadcastModeSupported != null && !this.mIsBroadcastModeSupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        this.execCommand(command);
    }
    
    @Override
    public void setRepeatAlarm(final boolean b, final MAC mac) throws UEOperationException, UEConnectionException {
        int n = 0;
        final byte[] array = { (byte)AlarmSetCommand.SET_REPEAT_DAY.getCode(), 0, 0 };
        if (b) {
            n = 127;
        }
        array[1] = (byte)n;
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetAlarm, array));
    }
    
    @Override
    public void setSnoozeTimeAlarm(final int n, final MAC mac) throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetAlarm, new byte[] { (byte)AlarmSetCommand.SET_SNOOZE_TIME.getCode(), 10, 1 }));
    }
    
    @Override
    public void setSonificationProfile(final UESonificationProfile mSonificationProfileCache) throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetSonfication, new byte[] { (byte)UESonificationProfile.getCode(mSonificationProfileCache) }));
        this.mSonificationProfileCache = mSonificationProfileCache;
    }
    
    @Override
    public void setStickyTWSOrPartyUpFlag(final boolean b) throws UEOperationException, UEConnectionException {
        boolean b2 = true;
        if (!b) {
            b2 = false;
        }
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetTWSSavePairFlag, new byte[] { (byte)(b2 ? 1 : 0) }));
        this.mIsTWSSavePairOnCache = b;
    }
    
    @Override
    public void setTWSBalance(final byte b) throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetTWSBalance, new byte[] { b }));
        this.mBalanceCache = b;
    }
    
    @Override
    public void setVoiceLEDAndTone(final byte b, final byte b2) throws UEOperationException, UEConnectionException {
        final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetVoiceLEDState, new byte[] { b, b2 });
        if (this.isEnableCache() && this.mIsVoiceSupported != null && !this.mIsVoiceSupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        this.execCommand(command);
    }
    
    @Override
    public void setVoiceState(final UEVoiceState ueVoiceState) throws UEOperationException, UEConnectionException {
        final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetVoiceControlFlag, new byte[] { (byte)ueVoiceState.getCode() });
        if (this.isEnableCache() && this.mIsVoiceSupported != null && !this.mIsVoiceSupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        this.execCommand(command);
    }
    
    @Override
    public void setVolume(final int n) throws UEOperationException, UEConnectionException {
        Label_0038: {
            if (this.mIsVolume32Supported != null) {
                break Label_0038;
            }
            try {
                this.set32Volume(n);
                this.mIsVolume32Supported = true;
                return;
            }
            catch (UEUnrecognisedCommandException ex) {
                this.mIsVolume32Supported = false;
                this.set16Volume(n);
                return;
            }
        }
        if (this.mIsVolume32Supported) {
            this.set32Volume(n);
            return;
        }
        this.set16Volume(n);
    }
    
    @Override
    public void setXUPPromiscuousModel(final boolean b) throws UEOperationException, UEConnectionException {
        boolean b2 = true;
        if (!b) {
            b2 = false;
        }
        final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetXUPPromiscuousMode, new byte[] { (byte)(b2 ? 1 : 0) });
        if (this.isEnableCache() && this.mIsBroadcastModeSupported != null && !this.mIsBroadcastModeSupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        this.execCommand(command);
        this.mIsPromiscuousModeOnCache = b;
    }
    
    @Override
    public void snoozeAlarm() throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetAlarm, new byte[] { (byte)AlarmSetCommand.SNOOZE.getCode() }));
    }
    
    @Override
    public void stopAlarm() throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetAlarm, new byte[] { (byte)AlarmSetCommand.STOP_CURRENT_ALARM.getCode(), 0 }));
    }
    
    @Override
    public void stopRestreamingMode() throws UEOperationException, UEConnectionException {
        this.execCommand(UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.StopRestreaming, null));
    }
    
    @Override
    public void syncBroadcastVolume() throws UEOperationException, UEConnectionException {
        final UEDeviceCommand command = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetBroadcastSyncVolume, null);
        if (this.isEnableCache() && this.mIsBroadcastModeSupported != null && !this.mIsBroadcastModeSupported) {
            throw new UEUnrecognisedCommandException(command);
        }
        this.execCommand(command);
    }
    
    @Override
    public void validateSQIF() throws UEOperationException, UEConnectionException {
        this.execOTACommand(UEOTADeviceCommand.newCommand(UEOTACommand.VALIDATE_SQIF, null));
    }
    
    @Override
    public void writeSQIF(final byte[] array) throws UEOperationException, UEConnectionException {
        this.execLongOTACommand(UEOTADeviceCommand.newCommand(UEOTACommand.WRITE_SQIF, array));
    }
}
