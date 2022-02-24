// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue;

import com.logitech.ue.ecomm.model.NotificationStory;
import com.logitech.ue.tasks.SafeTask;
import android.content.DialogInterface$OnCancelListener;
import android.content.DialogInterface$OnDismissListener;
import com.logitech.ue.fragments.MessageDialogFragment;
import android.content.DialogInterface$OnClickListener;
import com.logitech.ue.testing.BroadcastEmulator;
import com.logitech.ue.service.IUEService;
import android.os.Bundle;
import com.logitech.ue.firmware.FirmwareManager;
import com.logitech.ue.content.ContentManager;
import com.logitech.ue.service.ServiceLocator;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import com.logitech.ue.activities.AlarmPopupActivity;
import com.logitech.ue.fragments.InfiniteProgressDialogFragment;
import com.logitech.ue.devicedata.AlarmSettings;
import java.io.Serializable;
import com.logitech.ue.activities.MainActivity;
import com.logitech.ue.centurion.device.devicedata.UELanguage;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.DialogFragment;
import com.logitech.ue.centurion.device.devicedata.UEDeviceType;
import android.os.AsyncTask$Status;
import android.view.Display;
import com.logitech.ue.centurion.exceptions.UEException;
import com.logitech.ue.ecomm.NotificationManager;
import java.util.TimeZone;
import android.os.Build$VERSION;
import android.os.Build;
import android.util.DisplayMetrics;
import android.graphics.Point;
import android.view.WindowManager;
import com.logitech.ue.centurion.connection.UEConnectionType;
import com.logitech.ue.ecomm.model.Speaker;
import java.util.ArrayList;
import java.util.Locale;
import com.logitech.ue.ecomm.model.NotificationParams;
import com.logitech.ue.tasks.CheckFirmwareUpdateTask;
import com.logitech.ue.other.DeviceInfo;
import com.logitech.ue.tasks.GetDeviceInfoTask;
import android.content.SharedPreferences;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import android.preference.PreferenceManager;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.manifest.ManifestManager;
import com.logitech.ue.centurion.UEDiscoveryManager;
import com.logitech.ue.activities.UpdaterActivity;
import com.logitech.ue.centurion.utils.UEUtils;
import com.logitech.ue.tasks.GetPhoneBluetoothAddressTask;
import com.logitech.ue.tasks.SetDeviceBLEStateTask;
import com.logitech.ue.tasks.DeviceFirstTimeConnectTask;
import android.util.Log;
import com.logitech.ue.centurion.device.UEDevice;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.logitech.ue.devicedata.DeviceFirmwareSerialInfo;
import com.logitech.ue.tasks.GetDeviceFirmwareAndSerialTask;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;
import android.content.Intent;
import android.content.Context;
import com.logitech.ue.centurion.utils.MAC;
import java.util.HashSet;
import com.logitech.ue.activities.BaseActivity;
import com.logitech.ue.tasks.CheckForDeviceTask;
import android.content.BroadcastReceiver;
import com.logitech.ue.tasks.AppExitTask;
import com.logitech.ue.tasks.AppEnterTask;
import android.app.Application;

public class App extends Application
{
    public static final int APP_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    public static final int APP_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    public static final String HEADER_NAME = "header";
    private static final String PREF_DEVICES = "known devices";
    private static final String TAG;
    public static final String THEME_NAME = "UEPartyUp";
    private static App mInstance;
    private AppEnterTask mAppEnterTask;
    private AppExitTask mAppExitTask;
    final BroadcastReceiver mBroadcastReceiver;
    private CheckForDeviceTask mCheckForDeviceTask;
    private BaseActivity mCurrentActivity;
    private boolean mIsWelcomeToPartyUpAllreadyShown;
    private HashSet<MAC> mKnowDevices;
    private int mProgressDialogCounter;
    
    static {
        TAG = App.class.getSimpleName();
    }
    
    public App() {
        this.mCurrentActivity = null;
        this.mIsWelcomeToPartyUpAllreadyShown = false;
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getAction().equals("com.logitech.ue.centurion.CONNECTION_CHANGED")) {
                    final UEDeviceStatus status = UEDeviceStatus.getStatus(intent.getIntExtra("status", UEDeviceStatus.getValue(UEDeviceStatus.DISCONNECTED)));
                    final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
                    if (connectedDevice != null && status.isBtClassicConnectedState()) {
                        App.this.updateLastSeenDevice();
                        if (connectedDevice.getDeviceConnectionStatus() == UEDeviceStatus.SINGLE_CONNECTED) {
                            new GetDeviceFirmwareAndSerialTask() {
                                public void onSuccess(final DeviceFirmwareSerialInfo deviceFirmwareSerialInfo) {
                                    super.onSuccess((T)deviceFirmwareSerialInfo);
                                    if (deviceFirmwareSerialInfo != null && !TextUtils.isEmpty((CharSequence)deviceFirmwareSerialInfo.getFirmwareVersion())) {
                                        UserPreferences.getInstance().setLastSeenSpeakerVersion(deviceFirmwareSerialInfo.getFirmwareVersion());
                                    }
                                    else {
                                        UserPreferences.getInstance().setLastSeenSpeakerVersion("No data");
                                    }
                                }
                            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
                            if (!App.this.isDeviceKnown(connectedDevice)) {
                                Log.i(App.TAG, "New device connected");
                                App.this.rememberThisDevice(connectedDevice);
                                new DeviceFirstTimeConnectTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
                            }
                            else if (!UEDeviceManager.getInstance().isBleSupported()) {
                                Log.i(App.TAG, "Device supports BLE. Turning OFF device BLE...");
                                new SetDeviceBLEStateTask(false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
                            }
                            ((SafeTask<Void, Progress, Result>)new GetPhoneBluetoothAddressTask() {
                                public void onSuccess(final byte[] array) {
                                    super.onSuccess((T)(Object)array);
                                    UserPreferences.getInstance().setPhoneMAC(UEUtils.byteArrayToMACString(array));
                                }
                            }).executeOnThreadPoolExecutor(new Void[0]);
                            App.this.beginCheckForFirmwareUpdate();
                        }
                        else {
                            Log.w(App.TAG, "Caution: device status is an unhandled " + connectedDevice.getDeviceConnectionStatus().name());
                        }
                    }
                    if (status == UEDeviceStatus.DISCONNECTED) {
                        if (App.this.isAppActive()) {
                            if (UpdaterActivity.class != null && UpdaterActivity.isOTAOngoing()) {
                                Log.w(App.TAG, "OTA ongoing. Do not launch BLE scanning!");
                            }
                            else {
                                App.this.checkForDevice();
                            }
                        }
                        if (UEDiscoveryManager.getInstance().isBLEListening()) {
                            UEDiscoveryManager.getInstance().stopBLEPackageListening();
                        }
                    }
                }
                else if (intent.getAction().equals("com.logitech.ue.manifest.MANIFEST_UPDATED")) {
                    Log.d(App.TAG, "App manifest updated");
                    while (true) {
                        try {
                            UserPreferences.getInstance().setManifest(ManifestManager.getInstance().getManifest().toXMLString());
                            UserPreferences.getInstance().setLastManifestSyncUpdate(System.currentTimeMillis());
                            if (App.getDeviceConnectionState().isBtClassicConnectedState()) {
                                App.this.beginCheckForFirmwareUpdate();
                            }
                        }
                        catch (Exception ex) {
                            Log.d(App.TAG, "Failed to save manifest. Clear manifest data");
                            UserPreferences.getInstance().setManifest(null);
                            UserPreferences.getInstance().setLastManifestSyncUpdate(0L);
                            continue;
                        }
                        break;
                    }
                }
            }
        };
    }
    
    public static UEDeviceStatus getDeviceConnectionState() {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        UEDeviceStatus ueDeviceStatus;
        if (connectedDevice != null) {
            ueDeviceStatus = connectedDevice.getDeviceConnectionStatus();
        }
        else {
            ueDeviceStatus = UEDeviceStatus.DISCONNECTED;
        }
        return ueDeviceStatus;
    }
    
    public static App getInstance() {
        return App.mInstance;
    }
    
    private HashSet<MAC> getKnowDevices() {
        if (this.mKnowDevices == null) {
            final List<String> list = Arrays.asList(PreferenceManager.getDefaultSharedPreferences((Context)this).getString("known devices", "").split(","));
            this.mKnowDevices = new HashSet<MAC>(list.size());
            for (final String s : list) {
                try {
                    this.mKnowDevices.add(new MAC(s));
                }
                catch (Exception ex) {}
            }
        }
        return this.mKnowDevices;
    }
    
    private boolean isDeviceKnown(final UEDevice ueDevice) {
        return ueDevice != null && this.getKnowDevices().contains(ueDevice.getAddress());
    }
    
    private void rememberThisDevice(final UEDevice ueDevice) {
        if (ueDevice != null) {
            Log.d(App.TAG, "Remember device with address " + ueDevice.getAddress());
            final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences((Context)this);
            this.mKnowDevices.add(ueDevice.getAddress());
            defaultSharedPreferences.edit().putString("known devices", TextUtils.join((CharSequence)",", (Iterable)this.mKnowDevices)).apply();
        }
    }
    
    private void updateLastSeenDevice() {
        ((SafeTask<Void, Progress, Result>)new GetDeviceInfoTask(false) {
            public void onSuccess(final DeviceInfo lastSeenDevice) {
                super.onSuccess((T)lastSeenDevice);
                UserPreferences.getInstance().setLastSeenDevice(lastSeenDevice);
            }
        }).executeOnThreadPoolExecutor(new Void[0]);
    }
    
    public void beginCheckForFirmwareUpdate() {
        ((SafeTask<Void, Progress, Result>)new CheckFirmwareUpdateTask()).executeOnThreadPoolExecutor(new Void[0]);
    }
    
    public NotificationParams buildNotificationRequestParams(String s) {
        final NotificationParams notificationParams = new NotificationParams();
        notificationParams.promotionAllowed = Boolean.toString(UserPreferences.getInstance().isNotificationsEnable());
        notificationParams.appName = this.getString(2131165477);
        notificationParams.appVersion = "5.0.166";
        notificationParams.inAppPosition = s;
        notificationParams.locale = Locale.getDefault().toString();
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        Object connectedSpeakers = new ArrayList<Speaker>();
        Label_0127: {
            if (connectedDevice == null || connectedDevice.getConnectionType() != UEConnectionType.SPP) {
                break Label_0127;
            }
            s = (String)new Speaker();
            ((Speaker)s).speakerModel = this.detectSpeakerModel(connectedDevice);
            while (true) {
                try {
                    ((Speaker)s).colorCode = connectedDevice.getDeviceColor();
                    ((Speaker)s).firmwareVersion = connectedDevice.getFirmwareVersion();
                    ((ArrayList<Speaker>)connectedSpeakers).add((Speaker)s);
                    notificationParams.connectedSpeakers = (List<Speaker>)connectedSpeakers;
                    notificationParams.totalSpeakersSeen = this.getKnowDevices().size();
                    final Display defaultDisplay = ((WindowManager)this.getSystemService("window")).getDefaultDisplay();
                    s = (String)new Point();
                    defaultDisplay.getSize((Point)s);
                    connectedSpeakers = new DisplayMetrics();
                    defaultDisplay.getMetrics((DisplayMetrics)connectedSpeakers);
                    notificationParams.displayWidth = ((Point)s).x;
                    notificationParams.displayHeight = ((Point)s).y;
                    notificationParams.displayDpi = ((DisplayMetrics)connectedSpeakers).densityDpi;
                    notificationParams.phoneMaker = Build.MANUFACTURER;
                    notificationParams.phoneModel = Build.MODEL;
                    notificationParams.phoneOS = "Android";
                    notificationParams.phoneOSVersion = Build$VERSION.RELEASE;
                    notificationParams.phoneTimeZone = TimeZone.getDefault().getRawOffset() / 3600000.0f;
                    s = (String)NotificationManager.getInstance().getHistory().getLastStory();
                    if (s != null) {
                        notificationParams.lastNoticeTime = ((NotificationStory)s).timestamp;
                        notificationParams.lastNoticeNotificationId = ((NotificationStory)s).notification.notificationID;
                        notificationParams.lastNoticeAction = ((NotificationStory)s).userAction;
                        s = (String)NotificationManager.getInstance().getHistory().getLastClickedStory();
                        if (s != null) {
                            notificationParams.lastClickedTime = ((NotificationStory)s).timestamp;
                            notificationParams.lastClickedId = ((NotificationStory)s).notification.notificationID;
                            notificationParams.lastClickedURL = ((NotificationStory)s).notification.url;
                        }
                        s = (String)NotificationManager.getInstance().getHistory().getDismissedStory();
                        if (s != null) {
                            notificationParams.lastDismissedTime = ((NotificationStory)s).timestamp;
                            notificationParams.lastNoticeNotificationId = ((NotificationStory)s).notification.notificationID;
                        }
                    }
                    return notificationParams;
                }
                catch (UEException ex) {
                    ex.printStackTrace();
                    continue;
                }
                break;
            }
        }
    }
    
    public void checkForDevice() {
        synchronized (this) {
            if (UEDeviceManager.getInstance().getConnectedDevice() == null && (this.mCheckForDeviceTask == null || this.mCheckForDeviceTask.getStatus() == AsyncTask$Status.FINISHED)) {
                ((SafeTask<Void, Progress, Result>)(this.mCheckForDeviceTask = new CheckForDeviceTask())).executeOnThreadPoolExecutor(new Void[0]);
            }
        }
    }
    
    public void claimCurrentActivity(final BaseActivity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }
    
    public String detectSpeakerModel(final UEGenericDevice ueGenericDevice) {
        final String s = null;
        String s2;
        if (ueGenericDevice == null) {
            s2 = s;
        }
        else {
            try {
                UEDeviceType ueDeviceType;
                if ((ueDeviceType = UEColourHelper.getDeviceTypeByColour(ueGenericDevice.getDeviceColor())) == UEDeviceType.Unknown) {
                    ueDeviceType = ueGenericDevice.getDeviceType();
                }
                if (ueDeviceType == UEDeviceType.Kora) {
                    if (UEUtils.compareVersions(ueGenericDevice.getHardwareRevision(), "1.5.0") >= 0) {
                        s2 = "kora cr";
                    }
                    else {
                        s2 = "kora";
                    }
                }
                else if (ueDeviceType == UEDeviceType.Maximus) {
                    s2 = "maximus";
                }
                else if (ueDeviceType == UEDeviceType.Titus) {
                    s2 = "titus";
                }
                else {
                    s2 = s;
                    if (ueDeviceType == UEDeviceType.Caribe) {
                        s2 = "caribe";
                    }
                }
            }
            catch (UEException ex) {
                s2 = s;
            }
        }
        return s2;
    }
    
    public void dismissMessageDialog() {
        if (this.mCurrentActivity != null) {
            final FragmentTransaction beginTransaction = this.mCurrentActivity.getSupportFragmentManager().beginTransaction();
            final Fragment fragmentByTag = this.mCurrentActivity.getSupportFragmentManager().findFragmentByTag("MessageDialogFragment");
            if (fragmentByTag != null) {
                ((DialogFragment)fragmentByTag).dismissAllowingStateLoss();
                if (this.mCurrentActivity != null) {
                    beginTransaction.remove(fragmentByTag).commitAllowingStateLoss();
                }
                else {
                    Log.e(App.TAG, "Current Activity is NULL");
                }
            }
            Log.d(App.TAG, "Dialog fragment dismissed");
        }
    }
    
    public void dismissProgressDialog() {
        --this.mProgressDialogCounter;
        if (this.mProgressDialogCounter <= 0) {
            this.killProgressDialog();
        }
        Log.d(App.TAG, "Progress dialog counter " + this.mProgressDialogCounter);
    }
    
    public MAC getBluetoothMacAddress() {
        final String phoneMAC = UserPreferences.getInstance().getPhoneMAC();
        MAC mac;
        if (phoneMAC != null) {
            mac = new MAC(phoneMAC);
        }
        else {
            mac = new MAC("02:00:00:00:00:00");
        }
        return mac;
    }
    
    public byte[] getBluetoothMacAddressArray() {
        return this.getBluetoothMacAddress().getBytes();
    }
    
    public String getBluetoothMacAddressTail() {
        final String string = this.getBluetoothMacAddress().toString();
        String substring;
        if (string == null) {
            substring = null;
        }
        else {
            substring = string.substring(string.length() - 5, string.length());
        }
        return substring;
    }
    
    public String getBluetoothName() {
        final BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        String name;
        if (defaultAdapter == null) {
            Log.d(App.TAG, "device does not support bluetooth");
            name = null;
        }
        else {
            name = defaultAdapter.getName();
        }
        return name;
    }
    
    public UEDeviceType getConnectedDeviceType() {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        Label_0028: {
            if (connectedDevice == null) {
                break Label_0028;
            }
            while (true) {
                UEDeviceType ueDeviceType;
                try {
                    if (connectedDevice.getDeviceConnectionStatus().isBtClassicConnectedState()) {
                        ueDeviceType = connectedDevice.getDeviceType();
                    }
                    else {
                        ueDeviceType = UEDeviceType.Kora;
                    }
                    return ueDeviceType;
                }
                catch (UEException ex) {
                    ueDeviceType = UEDeviceType.Kora;
                    return ueDeviceType;
                }
                return ueDeviceType;
            }
        }
    }
    
    public Activity getCurrentActivity() {
        return this.mCurrentActivity;
    }
    
    public String getLanguageName(final UELanguage ueLanguage) {
        return this.getResources().getStringArray(2131230730)[ueLanguage.getCode()];
    }
    
    public String getLanguageTechnicalName(final UELanguage ueLanguage) {
        return this.getResources().getStringArray(2131230730)[ueLanguage.getCode()];
    }
    
    public void gotoDisconnectedHome() {
        if (this.mCurrentActivity != null) {
            Log.d(App.TAG, "Show Disconnected home");
            final Intent intent = new Intent((Context)this, (Class)MainActivity.class);
            intent.addFlags(268435456);
            intent.addFlags(67108864);
            intent.putExtra("disconnected", true);
            this.startActivity(intent);
        }
    }
    
    public void gotoNuclearHome(final Exception ex) {
        if (this.mCurrentActivity != null) {
            Log.d(App.TAG, "Show nuclear dialog");
            final Intent intent = new Intent((Context)this, (Class)MainActivity.class);
            intent.addFlags(268435456);
            intent.addFlags(67108864);
            intent.putExtra("error", (Serializable)ex);
            this.startActivity(intent);
        }
    }
    
    public boolean isAlarmInConflict() {
        final String bluetoothMacAddressTail = getInstance().getBluetoothMacAddressTail();
        String s2;
        final String s = s2 = AlarmSettings.getHostAddress();
    Label_0051:
        while (true) {
            if (s != null) {
                break Label_0051;
            }
            Log.w(App.TAG, "No local alarm record. Checking with speaker...");
            final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
            s2 = s;
            if (connectedDevice == null) {
                break Label_0051;
            }
            while (true) {
                boolean b = false;
            Label_0125:
                while (true) {
                    try {
                        s2 = connectedDevice.getAlarmInfo().getAlarmHostAddress();
                        if (s2 != null && !s2.equals("00:00") && !s2.equals(bluetoothMacAddressTail)) {
                            b = true;
                            if (b) {
                                Log.d(App.TAG, String.format("Alarm conflict. Device MAC: %s Alarm host MAC: %s", bluetoothMacAddressTail, s2));
                                return b;
                            }
                            break Label_0125;
                        }
                    }
                    catch (UEException ex) {
                        ex.printStackTrace();
                        s2 = s;
                        continue Label_0051;
                    }
                    b = false;
                    continue;
                }
                Log.d(App.TAG, String.format("No alarm conflict. Device MAC: %s Alarm host MAC: %s", bluetoothMacAddressTail, s2));
                return b;
            }
            break;
        }
    }
    
    public boolean isAppActive() {
        return this.mCurrentActivity != null;
    }
    
    public void killProgressDialog() {
        if (this.mCurrentActivity != null) {
            final Fragment fragmentByTag = this.mCurrentActivity.getSupportFragmentManager().findFragmentByTag(InfiniteProgressDialogFragment.TAG);
            if (fragmentByTag != null) {
                final FragmentTransaction beginTransaction = this.mCurrentActivity.getSupportFragmentManager().beginTransaction();
                ((DialogFragment)fragmentByTag).dismissAllowingStateLoss();
                if (this.mCurrentActivity != null) {
                    beginTransaction.remove(fragmentByTag).commitAllowingStateLoss();
                    Log.d(App.TAG, "Dialog fragment dismissed");
                }
                else {
                    Log.e(App.TAG, "Current Activity is NULL");
                }
            }
            else {
                Log.d(App.TAG, "There is no progress dialog");
            }
        }
        this.mProgressDialogCounter = 0;
    }
    
    public void onAppPause(final Activity activity) {
        if (!(activity instanceof AlarmPopupActivity)) {
            final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
            if (connectedDevice != null) {
                connectedDevice.dropSecondLevelCache();
            }
            if (this.mAppExitTask != null && this.mAppExitTask.getStatus() != AsyncTask$Status.FINISHED) {
                this.mAppExitTask.cancel(true);
                this.mAppExitTask = null;
            }
            if (this.mAppEnterTask != null && this.mAppEnterTask.getStatus() != AsyncTask$Status.FINISHED) {
                this.mAppEnterTask.cancel(true);
                this.mAppEnterTask = null;
            }
            (this.mAppExitTask = new AppExitTask()).execute((Object[])new Void[0]);
            if (UEDiscoveryManager.getInstance().isReady()) {
                UEDiscoveryManager.getInstance().stopBLESearch();
                Log.d(App.TAG, "BLE scanning paused.");
            }
        }
    }
    
    public void onAppResume(final Activity activity) {
        if (!(activity instanceof AlarmPopupActivity)) {
            final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
            if (connectedDevice != null) {
                connectedDevice.dropSecondLevelCache();
            }
            if (this.mAppExitTask != null && this.mAppExitTask.getStatus() != AsyncTask$Status.FINISHED) {
                this.mAppExitTask.cancel(true);
                this.mAppExitTask = null;
            }
            if (this.mAppEnterTask != null && this.mAppEnterTask.getStatus() != AsyncTask$Status.FINISHED) {
                this.mAppEnterTask.cancel(true);
                this.mAppEnterTask = null;
            }
            (this.mAppEnterTask = new AppEnterTask()).execute((Object[])new Void[0]);
            if (ContextCompat.checkSelfPermission((Context)this, "android.permission.ACCESS_COARSE_LOCATION") != 0) {
                ActivityCompat.requestPermissions(activity, new String[] { "android.permission.ACCESS_COARSE_LOCATION" }, 0);
            }
            this.checkForDevice();
        }
    }
    
    public void onCreate() {
        super.onCreate();
        App.mInstance = this;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/GT-Walsheim-Pro-Regular.ttf").setFontAttrId(2130771979).build());
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.logitech.ue.centurion.CONNECTION_CHANGED");
        intentFilter.addAction("com.logitech.ue.firmware.UPDATE_READY");
        intentFilter.addAction("com.logitech.ue.manifest.MANIFEST_UPDATED");
        LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.mBroadcastReceiver, intentFilter);
        ServiceLocator.getInstance().initService((Context)this, ServiceLocator.buildParamBundle("http://files.logitech.com/audio/mobileApps/resources/UEServiceLocator.xml"));
        ContentManager.getInstance().initService((Context)this, ContentManager.buildParamBundle("http://files.logitech.com/audio/mobileApps/resources/boweryManifest.xml", "http://files.logitech.com/audio/mobileApps/resources/"));
        FirmwareManager.getInstance().initService((Context)this, "http://uemobileservices.logitech.com");
        NotificationManager.getInstance().initService((Context)this, null);
        ManifestManager.getInstance().initService((Context)this, ManifestManager.buildParamBundle("http://files.logitech.com/audio/mobileApps/resources/boweryManifest.xml"));
        ServiceLocator.getInstance().registerServiceForSync(NotificationManager.getInstance());
        ServiceLocator.getInstance().beginSync();
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(final Void... array) {
                final String manifest = UserPreferences.getInstance().getManifest();
                if (manifest != null) {
                    ManifestManager.getInstance().setManifest(manifest);
                    ManifestManager.getInstance().getManifest().setUpdateTime(UserPreferences.getInstance().getLastManifestSyncTime());
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
        BroadcastEmulator.getInstance().init((Context)this);
        ManifestManager.getInstance().beginUpdateManifest();
    }
    
    public void processNuclearException(final Exception ex) {
        Log.e(App.TAG, "Nuclear exception!", (Throwable)ex);
        FlurryTracker.logError(App.TAG, ex.getMessage());
        this.gotoNuclearHome(ex);
    }
    
    public void releaseCurrentActivity(final BaseActivity baseActivity) {
        if (baseActivity != null && baseActivity == this.mCurrentActivity) {
            this.mCurrentActivity = null;
        }
    }
    
    public void showAlertDialog(final String message, final int image, final int n, final int n2, final DialogInterface$OnClickListener clickListener) {
        final FragmentTransaction beginTransaction = this.mCurrentActivity.getSupportFragmentManager().beginTransaction();
        if (this.mCurrentActivity.getSupportFragmentManager().findFragmentByTag("MessageDialogFragment") == null) {
            final MessageDialogFragment create = new MessageDialogFragment.Builder().setMessage(message).setImage(image).setPositiveButton(this.getString(n2)).setNegativeButton(this.getString(n)).setClickListener(clickListener).create();
            if (this.mCurrentActivity != null) {
                beginTransaction.add(create, "MessageDialogFragment").commitAllowingStateLoss();
                Log.d(App.TAG, "Dialog fragment shown");
            }
            else {
                Log.e(App.TAG, "Current Activity is NULL");
            }
        }
    }
    
    public void showAlertDialog(final String message, final int n, final int n2, final DialogInterface$OnClickListener clickListener) {
        final FragmentTransaction beginTransaction = this.mCurrentActivity.getSupportFragmentManager().beginTransaction();
        if (this.mCurrentActivity.getSupportFragmentManager().findFragmentByTag("MessageDialogFragment") == null) {
            final MessageDialogFragment create = new MessageDialogFragment.Builder().setMessage(message).setPositiveButton(this.getString(n2)).setNegativeButton(this.getString(n)).setClickListener(clickListener).create();
            if (this.mCurrentActivity != null) {
                beginTransaction.add(create, "MessageDialogFragment").commitAllowingStateLoss();
                Log.d(App.TAG, "Dialog fragment shown");
            }
            else {
                Log.e(App.TAG, "Current Activity is NULL");
            }
        }
    }
    
    public void showAlertDialog(final String title, final String message, final int n, final int n2, final int selectedButton, final DialogInterface$OnClickListener clickListener) {
        final FragmentTransaction beginTransaction = this.mCurrentActivity.getSupportFragmentManager().beginTransaction();
        if (this.mCurrentActivity.getSupportFragmentManager().findFragmentByTag("MessageDialogFragment") == null) {
            final MessageDialogFragment create = new MessageDialogFragment.Builder().setTitle(title).setMessage(message).setPositiveButton(this.getString(n2)).setNegativeButton(this.getString(n)).setSelectedButton(selectedButton).setClickListener(clickListener).create();
            if (this.mCurrentActivity != null) {
                beginTransaction.add(create, "MessageDialogFragment").commitAllowingStateLoss();
                Log.d(App.TAG, "Dialog fragment shown");
            }
            else {
                Log.e(App.TAG, "Current Activity is NULL");
            }
        }
    }
    
    public void showAlertDialog(final String s, final String s2, final String s3, final String s4, final DialogInterface$OnClickListener dialogInterface$OnClickListener) {
        this.showAlertDialog(s, s2, s3, s4, dialogInterface$OnClickListener, null);
    }
    
    public void showAlertDialog(final String s, final String s2, final String s3, final String s4, final DialogInterface$OnClickListener dialogInterface$OnClickListener, final DialogInterface$OnDismissListener dialogInterface$OnDismissListener) {
        this.showAlertDialog(s, s2, s3, s4, dialogInterface$OnClickListener, dialogInterface$OnDismissListener, null);
    }
    
    public void showAlertDialog(final String title, final String message, final String negativeButton, final String positiveButton, final DialogInterface$OnClickListener clickListener, final DialogInterface$OnDismissListener dismissListener, final DialogInterface$OnCancelListener cancelListener) {
        final FragmentTransaction beginTransaction = this.mCurrentActivity.getSupportFragmentManager().beginTransaction();
        if (this.mCurrentActivity.getSupportFragmentManager().findFragmentByTag("MessageDialogFragment") == null) {
            final MessageDialogFragment create = new MessageDialogFragment.Builder().setTitle(title).setMessage(message).setPositiveButton(positiveButton).setNegativeButton(negativeButton).setClickListener(clickListener).setDismissListener(dismissListener).setCancelListener(cancelListener).create();
            if (this.mCurrentActivity != null) {
                beginTransaction.add(create, "MessageDialogFragment").commitAllowingStateLoss();
                Log.d(App.TAG, "Dialog fragment shown");
            }
            else {
                Log.e(App.TAG, "Current Activity is NULL");
            }
        }
    }
    
    public void showErrorDialog(final DialogInterface$OnDismissListener dismissListener) {
        if (this.mCurrentActivity == null) {
            Log.e(App.TAG, "Original activity is gone.");
        }
        else {
            final FragmentTransaction beginTransaction = this.mCurrentActivity.getSupportFragmentManager().beginTransaction();
            if (this.mCurrentActivity.getSupportFragmentManager().findFragmentByTag("MessageDialogFragment") == null) {
                final MessageDialogFragment create = new MessageDialogFragment.Builder().setMessage(this.getString(2131165361)).setPositiveButton(this.getString(2131165376)).setDismissListener(dismissListener).create();
                if (this.mCurrentActivity != null) {
                    beginTransaction.add(create, "MessageDialogFragment").commitAllowingStateLoss();
                    Log.d(App.TAG, "Dialog fragment shown");
                }
                else {
                    Log.e(App.TAG, "Current Activity is NULL");
                }
            }
            else {
                Log.d(App.TAG, "Dialog fragment is already shown");
            }
        }
    }
    
    public void showMessageDialog(final String message, final DialogInterface$OnDismissListener dismissListener) {
        if (this.mCurrentActivity != null) {
            if (this.mCurrentActivity.getSupportFragmentManager().findFragmentByTag("MessageDialogFragment") == null) {
                this.mCurrentActivity.getSupportFragmentManager().beginTransaction().add(new MessageDialogFragment.Builder().setMessage(message).setNegativeButton(this.getString(2131165285)).setDismissListener(dismissListener).create(), "MessageDialogFragment").commitAllowingStateLoss();
                Log.d(App.TAG, "Dialog fragment shown");
            }
        }
        else {
            Log.w(App.TAG, "Can't show message dialog because no current activity is present");
        }
    }
    
    public void showMessageDialog(final String title, final String message, final DialogInterface$OnDismissListener dismissListener) {
        if (this.mCurrentActivity != null) {
            if (this.mCurrentActivity.getSupportFragmentManager().findFragmentByTag("MessageDialogFragment") == null) {
                new MessageDialogFragment.Builder().setTitle(title).setMessage(message).setNegativeButton(this.getString(2131165285)).setDismissListener(dismissListener).create().show(this.mCurrentActivity.getSupportFragmentManager(), "MessageDialogFragment");
                Log.d(App.TAG, "Dialog fragment shown");
            }
        }
        else {
            Log.w(App.TAG, "Can't show message dialog because no current activity is present");
        }
    }
    
    public void showMessageDialog(final String title, final String message, final String negativeButton, final DialogInterface$OnDismissListener dismissListener) {
        if (this.mCurrentActivity != null) {
            if (this.mCurrentActivity.getSupportFragmentManager().findFragmentByTag("MessageDialogFragment") == null) {
                new MessageDialogFragment.Builder().setTitle(title).setMessage(message).setNegativeButton(negativeButton).setDismissListener(dismissListener).create().show(this.mCurrentActivity.getSupportFragmentManager(), "MessageDialogFragment");
                Log.d(App.TAG, "Dialog fragment shown");
            }
        }
        else {
            Log.w(App.TAG, "Can't show message dialog because no current activity is present");
        }
    }
    
    public DialogFragment showProgressDialog(final String message) {
        final FragmentTransaction beginTransaction = this.mCurrentActivity.getSupportFragmentManager().beginTransaction();
        Label_0088: {
            if (this.mCurrentActivity.getSupportFragmentManager().findFragmentByTag(InfiniteProgressDialogFragment.TAG) != null) {
                break Label_0088;
            }
            final MessageDialogFragment create = ((MessageDialogFragment.Builder)new InfiniteProgressDialogFragment.Builder()).setMessage(message).create();
            if (this.mCurrentActivity == null) {
                Log.e(App.TAG, "Current Activity is NULL");
                break Label_0088;
            }
            beginTransaction.add(create, InfiniteProgressDialogFragment.TAG).commitAllowingStateLoss();
            Log.d(App.TAG, "Progress dialog fragment shown");
            this.mProgressDialogCounter = 0;
            return create;
        }
        ++this.mProgressDialogCounter;
        Log.d(App.TAG, "Progress dialog counter " + this.mProgressDialogCounter);
        return null;
    }
}
