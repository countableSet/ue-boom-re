// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion;

import com.logitech.ue.centurion.device.UEDevice;
import com.logitech.ue.centurion.exceptions.UEConnectionException;
import com.logitech.ue.centurion.exceptions.UEException;
import com.logitech.ue.centurion.connection.UEConnectionType;
import com.logitech.ue.centurion.device.UESPPDevice;
import com.logitech.ue.centurion.connection.IUEConnector;
import com.logitech.ue.centurion.device.UEBLEDevice;
import android.os.HandlerThread;
import android.os.Build$VERSION;
import com.logitech.ue.centurion.notification.notificator.UEVoiceNotificator;
import com.logitech.ue.centurion.notification.UESetReceiverOneAttributeNotification;
import com.logitech.ue.centurion.notification.UEGetReceiverOneAttributeNotification;
import com.logitech.ue.centurion.notification.UEReceiverFixedAttributesNotification;
import com.logitech.ue.centurion.notification.UEReceiverVariableAttributesNotification;
import com.logitech.ue.centurion.notification.UEReceiverRemovedNotification;
import com.logitech.ue.centurion.notification.UEReceiverAddedNotification;
import com.logitech.ue.centurion.notification.UEBLEAvailableNotification;
import com.logitech.ue.centurion.notification.UEBroadcastStatusNotification;
import com.logitech.ue.centurion.notification.UEBlockPartyStateNotification;
import com.logitech.ue.centurion.notification.UETrackLengthInfoNotification;
import com.logitech.ue.centurion.notification.notificator.UEAlarmNotificator;
import com.logitech.ue.centurion.notification.notificator.IUENotificator;
import com.logitech.ue.centurion.notification.UENotification;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStreamingStatus;
import com.logitech.ue.centurion.notification.notificator.UEBroadcastNotificator;
import com.logitech.ue.centurion.notification.UEDeviceRestreamingStatusNotification;
import com.logitech.ue.centurion.device.command.UEDeviceCommand;
import com.logitech.ue.centurion.connection.UEDeviceConnector;
import android.content.IntentFilter;
import android.bluetooth.BluetoothGattCharacteristic;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;
import android.support.v4.content.LocalBroadcastManager;
import com.logitech.ue.centurion.utils.MAC;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.content.Intent;
import com.logitech.ue.centurion.interfaces.IUEDeviceFactory;
import android.content.Context;
import com.logitech.ue.centurion.connection.UEBluetoothConnector;
import android.os.Handler;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.connection.UEBLEConnector;
import android.content.BroadcastReceiver;

public class UEDeviceManager
{
    public static final String ACTION_ALARM_NOTIFICATION = "com.logitech.ue.centurion.ALARM_NOTIFICATION";
    public static final String ACTION_BLE_AVAILABLE_NOTIFICATION = "com.logitech.ue.centurion.ACTION_BLE_AVAILABLE_NOTIFICATION";
    public static final String ACTION_BLOCK_PARTY_CHANGE_STATE_NOTIFICATION = "com.logitech.ue.centurion.BLOCK_PARTY_CHANGE_STATE_NOTIFICATION";
    public static final String ACTION_BROADCAST_STATUS_NOTIFICATION = "com.logitech.ue.centurion.ACTION_BROADCAST_STATUS_NOTIFICATION";
    public static final String ACTION_COMMAND_COMPLETED = "com.logitech.ue.centurion.COMMAND_COMPLETED";
    public static final String ACTION_CONNECTION_CHANGED = "com.logitech.ue.centurion.CONNECTION_CHANGED";
    public static final String ACTION_DATA_RECEIVED = "com.logitech.ue.centurion.DATA_RECEIVED";
    public static final String ACTION_DATA_SEND = "com.logitech.ue.centurion.DATA_SEND";
    public static final String ACTION_GET_RECEIVER_ONE_ATTRIBUTE_NOTIFICATION = "com.logitech.ue.centurion.ACTION_GET_RECEIVER_ONE_ATTRIBUTE_NOTIFICATION";
    public static final String ACTION_RECEIVER_ADDED_NOTIFICATION = "com.logitech.ue.centurion.ACTION_RECEIVER_ADDED_NOTIFICATION";
    public static final String ACTION_RECEIVER_FIXED_ATTRIBUTES_NOTIFICATION = "com.logitech.ue.centurion.ACTION_RECEIVER_FIXED_ATTRIBUTES_NOTIFICATION";
    public static final String ACTION_RECEIVER_REMOVED_NOTIFICATION = "com.logitech.ue.centurion.ACTION_RECEIVER_REMOVED_NOTIFICATION";
    public static final String ACTION_RECEIVER_VARIABLE_ATTRIBUTES_NOTIFICATION = "com.logitech.ue.centurion.ACTION_RECEIVER_VARIABLE_ATTRIBUTES_NOTIFICATION";
    public static final String ACTION_RESTREAMING_STATUS_CHANGE_NOTIFICATION = "com.logitech.ue.centurion.RESTREAMING_STATUS_CHANGE_NOTIFICATION";
    public static final String ACTION_SET_RECEIVER_ONE_ATTRIBUTE_NOTIFICATION = "com.logitech.ue.centurion.ACTION_SET_RECEIVER_ONE_ATTRIBUTE_NOTIFICATION";
    public static final String ACTION_TRACK_LENGTH_INFO_NOTIFICATION = "com.logitech.ue.centurion.TRACK_LENGTH_INFO_NOTIFICATION";
    public static final String ACTION_VOICE_REQUEST_NOTIFICATION = "com.logitech.ue.centurion.ACTION_VOICE_REQUEST_NOTIFICATION";
    public static final String EXTRAS_DEVICE_COMMAND = "command";
    public static final String EXTRAS_DEVICE_DATA = "data";
    public static final String EXTRAS_DEVICE_NOTIFICATION = "notification";
    public static final String EXTRAS_DEVICE_STATUS = "status";
    public static final String EXTRAS_DEVICE_TIME = "time";
    public static final String EXTRAS_DEVICE_TIME_ELAPSED = "time_elapsed";
    private static final String TAG;
    private static volatile UEDeviceManager mInstance;
    protected BroadcastReceiver mA2DPReceiver;
    public final UEBLEConnector.UEConnectorListener mBLEConnectorListener;
    private final BroadcastReceiver mBLEReceiver;
    protected UEGenericDevice mConnectedDevice;
    protected Handler mConnectionHandler;
    public final UEBluetoothConnector.UEConnectorListener mConnectorListener;
    protected Context mContext;
    private IUEDeviceFactory mDeviceFactory;
    protected boolean mIsBleSupported;
    protected boolean mIsCacheEnabled;
    protected boolean mIsDebugMode;
    
    static {
        TAG = UEDeviceManager.class.getSimpleName();
        UEDeviceManager.mInstance = null;
    }
    
    protected UEDeviceManager() {
        this.mConnectedDevice = null;
        this.mIsCacheEnabled = true;
        this.mIsBleSupported = false;
        this.mIsDebugMode = true;
        this.mBLEReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String action = intent.getAction();
                Log.d(UEDeviceManager.TAG, "Received broadcast " + action);
                if ("com.logitech.ue.bluetooth.le.ACTION_BLE_DEVICE_FOUND".equals(action)) {
                    UEDeviceManager.this.mConnectionHandler.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            if (UEDeviceManager.this.mConnectedDevice != null) {
                                Log.e(UEDeviceManager.TAG, "Device manager already has device! BLE scan was not stoped");
                            }
                            else {
                                final BluetoothDevice bluetoothDevice = (BluetoothDevice)intent.getExtras().getParcelable("device");
                                if (bluetoothDevice != null) {
                                    UEDeviceManager.this.processBLEDeviceConnection(new MAC(bluetoothDevice.getAddress()));
                                }
                            }
                        }
                    });
                }
            }
        };
        this.mConnectorListener = new UEBluetoothConnector.UEConnectorListener() {
            @Override
            public void onDataReceived(final UEBluetoothConnector ueBluetoothConnector, final byte[] array) {
                if (UEDeviceManager.this.mIsDebugMode) {
                    final Intent intent = new Intent("com.logitech.ue.centurion.DATA_RECEIVED");
                    intent.putExtra("data", array);
                    intent.putExtra("time", System.currentTimeMillis());
                    LocalBroadcastManager.getInstance(UEDeviceManager.this.mContext).sendBroadcast(intent);
                }
            }
            
            @Override
            public void onDataSent(final UEBluetoothConnector ueBluetoothConnector, final byte[] array) {
                if (UEDeviceManager.this.mIsDebugMode) {
                    final Intent intent = new Intent("com.logitech.ue.centurion.DATA_SEND");
                    intent.putExtra("data", array);
                    intent.putExtra("time", System.currentTimeMillis());
                    LocalBroadcastManager.getInstance(UEDeviceManager.this.mContext).sendBroadcast(intent);
                }
            }
            
            @Override
            public void onDisconnect(final UEBluetoothConnector ueBluetoothConnector) {
                Log.d(UEDeviceManager.TAG, "SPP device disconnected");
                if (UEDeviceManager.this.mConnectedDevice != null && UEDeviceManager.this.mConnectedDevice.getConnector() == ueBluetoothConnector) {
                    UEDeviceManager.this.mConnectedDevice.setDeviceConnectionStatus(UEDeviceStatus.DISCONNECTED);
                    UEDeviceManager.this.mConnectedDevice = null;
                    final Intent intent = new Intent("com.logitech.ue.centurion.CONNECTION_CHANGED");
                    intent.putExtra("status", UEDeviceStatus.DISCONNECTED.getValue());
                    LocalBroadcastManager.getInstance(UEDeviceManager.this.mContext).sendBroadcast(intent);
                }
            }
        };
        this.mBLEConnectorListener = new UEBLEConnector.UEConnectorListener() {
            @Override
            public void onCharacteristicsRead(final UEBLEConnector uebleConnector, final BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            }
            
            @Override
            public void onCharacteristicsWrite(final UEBLEConnector uebleConnector, final BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            }
            
            @Override
            public void onConnect(final UEBLEConnector uebleConnector) {
            }
            
            @Override
            public void onDisconnect(final UEBLEConnector uebleConnector) {
                Log.d(UEDeviceManager.TAG, "BLE device disconnected");
                if (UEDeviceManager.this.mConnectedDevice != null && UEDeviceManager.this.mConnectedDevice.getConnector() == uebleConnector) {
                    UEDeviceManager.this.setConnectedDevice(null);
                    final Intent intent = new Intent("com.logitech.ue.centurion.CONNECTION_CHANGED");
                    intent.putExtra("status", UEDeviceStatus.DISCONNECTED.getValue());
                    LocalBroadcastManager.getInstance(UEDeviceManager.this.mContext).sendBroadcast(intent);
                }
            }
        };
    }
    
    private void addReceivers(final Handler handler) {
        if (this.mContext == null) {
            Log.e(UEDeviceManager.TAG, "Can't register receivers. Context is NULL");
        }
        else {
            Log.d(UEDeviceManager.TAG, "Register receivers");
            this.mA2DPReceiver = new UEA2DPStateChangeReceiver();
            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
            this.mContext.registerReceiver(this.mA2DPReceiver, intentFilter, (String)null, handler);
            final IntentFilter intentFilter2 = new IntentFilter();
            intentFilter2.addAction("com.logitech.ue.bluetooth.le.ACTION_BLE_DEVICE_FOUND");
            LocalBroadcastManager.getInstance(this.mContext).registerReceiver(this.mBLEReceiver, intentFilter2);
        }
    }
    
    public static UEDeviceManager getInstance() {
        Label_0030: {
            if (UEDeviceManager.mInstance != null) {
                break Label_0030;
            }
            synchronized (UEDeviceManager.class) {
                if (UEDeviceManager.mInstance == null) {
                    UEDeviceManager.mInstance = new UEDeviceManager();
                }
                return UEDeviceManager.mInstance;
            }
        }
    }
    
    private void removeReceivers() {
        try {
            this.mContext.unregisterReceiver(this.mA2DPReceiver);
            LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mBLEReceiver);
        }
        catch (Exception ex) {}
    }
    
    private void setupNotificators(final UEDeviceConnector ueDeviceConnector) {
        ueDeviceConnector.addNotificator(new UEBroadcastNotificator<UEDeviceRestreamingStatusNotification>(UEDeviceCommand.UECommand.ReturnDeviceStatus, UEDeviceRestreamingStatusNotification.class, "com.logitech.ue.centurion.RESTREAMING_STATUS_CHANGE_NOTIFICATION", "notification", this.mContext) {
            @Override
            public void onNotificationReceived(final UEDeviceRestreamingStatusNotification ueDeviceRestreamingStatusNotification) {
                final UEGenericDevice mConnectedDevice = UEDeviceManager.this.mConnectedDevice;
                final UEDeviceStreamingStatus devicesStreamingStatus = ueDeviceRestreamingStatusNotification.getDevicesStreamingStatus();
                Log.d(UEDeviceManager.TAG, String.format("Device status changed to %s(0x%02X)", devicesStreamingStatus.toString(), devicesStreamingStatus.getCode()));
                if (mConnectedDevice != null && mConnectedDevice.getDeviceConnectionStatus().isBtClassicConnectedState()) {
                    if (mConnectedDevice.isEnableCache()) {
                        Log.d(UEDeviceManager.TAG, "Drop Third level cache on device");
                        mConnectedDevice.dropThirdLevelCache();
                    }
                    if (devicesStreamingStatus.isDevicePairedStatus()) {
                        mConnectedDevice.setDeviceConnectionStatus(UEDeviceStatus.DOUBLEUP_CONNECTED);
                    }
                    else {
                        mConnectedDevice.setDeviceConnectionStatus(UEDeviceStatus.SINGLE_CONNECTED);
                    }
                    mConnectedDevice.setDeviceStreamingStatus(devicesStreamingStatus);
                }
                super.onNotificationReceived(ueDeviceRestreamingStatusNotification);
            }
        });
        ueDeviceConnector.addNotificator(new UEAlarmNotificator("com.logitech.ue.centurion.ALARM_NOTIFICATION", "notification", this.mContext));
        ueDeviceConnector.addNotificator(new UEBroadcastNotificator(UEDeviceCommand.UECommand.TrackLengthInfoNotification, UETrackLengthInfoNotification.class, "com.logitech.ue.centurion.TRACK_LENGTH_INFO_NOTIFICATION", "notification", this.mContext));
        ueDeviceConnector.addNotificator(new UEBroadcastNotificator(UEDeviceCommand.UECommand.BlockPartyNotification, UEBlockPartyStateNotification.class, "com.logitech.ue.centurion.BLOCK_PARTY_CHANGE_STATE_NOTIFICATION", "notification", this.mContext));
        ueDeviceConnector.addNotificator(new UEBroadcastNotificator(UEDeviceCommand.UECommand.BroadcastStatusNotification, UEBroadcastStatusNotification.class, "com.logitech.ue.centurion.ACTION_BROADCAST_STATUS_NOTIFICATION", "notification", this.mContext));
        ueDeviceConnector.addNotificator(new UEBroadcastNotificator(UEDeviceCommand.UECommand.BLEAvailableNotification, UEBLEAvailableNotification.class, "com.logitech.ue.centurion.ACTION_BLE_AVAILABLE_NOTIFICATION", "notification", this.mContext));
        ueDeviceConnector.addNotificator(new UEBroadcastNotificator(UEDeviceCommand.UECommand.ReceiverAddedToBroadcastNotification, UEReceiverAddedNotification.class, "com.logitech.ue.centurion.ACTION_RECEIVER_ADDED_NOTIFICATION", "notification", this.mContext));
        ueDeviceConnector.addNotificator(new UEBroadcastNotificator(UEDeviceCommand.UECommand.ReceiverRemovedFromBroadcastNotification, UEReceiverRemovedNotification.class, "com.logitech.ue.centurion.ACTION_RECEIVER_REMOVED_NOTIFICATION", "notification", this.mContext));
        ueDeviceConnector.addNotificator(new UEBroadcastNotificator(UEDeviceCommand.UECommand.VariableReceiverAttributesNotification, UEReceiverVariableAttributesNotification.class, "com.logitech.ue.centurion.ACTION_RECEIVER_VARIABLE_ATTRIBUTES_NOTIFICATION", "notification", this.mContext));
        ueDeviceConnector.addNotificator(new UEBroadcastNotificator(UEDeviceCommand.UECommand.FixedReceiverAttributesNotification, UEReceiverFixedAttributesNotification.class, "com.logitech.ue.centurion.ACTION_RECEIVER_FIXED_ATTRIBUTES_NOTIFICATION", "notification", this.mContext));
        ueDeviceConnector.addNotificator(new UEBroadcastNotificator(UEDeviceCommand.UECommand.GetOneReceiverAttributeNotification, UEGetReceiverOneAttributeNotification.class, "com.logitech.ue.centurion.ACTION_GET_RECEIVER_ONE_ATTRIBUTE_NOTIFICATION", "notification", this.mContext));
        ueDeviceConnector.addNotificator(new UEBroadcastNotificator(UEDeviceCommand.UECommand.SetOneReceiverAttributeNotification, UESetReceiverOneAttributeNotification.class, "com.logitech.ue.centurion.ACTION_SET_RECEIVER_ONE_ATTRIBUTE_NOTIFICATION", "notification", this.mContext));
        ueDeviceConnector.addNotificator(new UEVoiceNotificator(UEDeviceCommand.UECommand.VoiceNotification, this.mContext));
    }
    
    public void checkForDevice() {
        final MAC connectedSPPDevice = UEDiscoveryManager.getInstance().getConnectedSPPDevice();
        if (connectedSPPDevice != null) {
            this.processDeviceConnection(connectedSPPDevice);
        }
        else {
            Log.d(UEDeviceManager.TAG, "No UE device connected via BT Classic");
        }
    }
    
    public UEGenericDevice getConnectedDevice() {
        return this.mConnectedDevice;
    }
    
    public void init(final Context mContext, final IUEDeviceFactory mDeviceFactory, final boolean mIsDebugMode) {
        while (true) {
            while (true) {
                Label_0159: {
                    synchronized (this) {
                        if (this.isReady()) {
                            Log.w(UEDeviceManager.TAG, "Can't start initialised, because it is already initialised");
                        }
                        else {
                            Log.d(UEDeviceManager.TAG, "Begin initialisation");
                            this.mContext = mContext;
                            this.mIsDebugMode = mIsDebugMode;
                            this.setEnableCache(true);
                            this.mDeviceFactory = mDeviceFactory;
                            UEDiscoveryManager.getInstance().init(mContext);
                            if (Build$VERSION.SDK_INT < 19 || !mContext.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
                                break Label_0159;
                            }
                            Log.d(UEDeviceManager.TAG, "Bluetooth LE supported by this device");
                            this.mIsBleSupported = true;
                            this.checkForDevice();
                            final HandlerThread handlerThread = new HandlerThread("ConnectionThread");
                            handlerThread.start();
                            this.addReceivers(this.mConnectionHandler = new Handler(handlerThread.getLooper()));
                            Log.d(UEDeviceManager.TAG, "UEDeviceManager initialisation finished");
                        }
                        return;
                    }
                }
                Log.w(UEDeviceManager.TAG, "Bluetooth LE NOT supported by this device");
                this.mIsBleSupported = false;
                continue;
            }
        }
    }
    
    public boolean isBleSupported() {
        return this.mIsBleSupported;
    }
    
    public boolean isIsCacheEnabled() {
        return this.mIsCacheEnabled;
    }
    
    public boolean isReady() {
        return this.mContext != null;
    }
    
    public void processBLEDeviceConnection(MAC mac) {
        Log.d(UEDeviceManager.TAG, "Process new device BLE connection. Address: " + mac);
        final UEBLEConnector uebleConnector = new UEBLEConnector(mac, this.mContext);
        boolean b = false;
        while (true) {
            try {
                uebleConnector.connectToDevice();
                uebleConnector.setListener(this.mBLEConnectorListener);
                b = true;
                if (b) {
                    mac = (MAC)new UEBLEDevice(mac, uebleConnector);
                    ((UEDevice)mac).setDeviceConnectionStatus(UEDeviceStatus.CONNECTED_OFF);
                    this.setConnectedDevice((UEGenericDevice)mac);
                    mac = (MAC)new Intent("com.logitech.ue.centurion.CONNECTION_CHANGED");
                    ((Intent)mac).putExtra("status", UEDeviceStatus.CONNECTED_OFF.getValue());
                    LocalBroadcastManager.getInstance(this.mContext).sendBroadcast((Intent)mac);
                }
                else {
                    Log.e(UEDeviceManager.TAG, "Failed to connect the device via BLE");
                    this.setConnectedDevice(null);
                }
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
    }
    
    public void processDeviceConnection(MAC buildDevice) {
        Log.d(UEDeviceManager.TAG, "Process new device SPP connection. Address: " + buildDevice);
        final UEDeviceConnector ueDeviceConnector = new UEDeviceConnector((MAC)buildDevice);
        boolean b = false;
        while (true) {
            try {
                ueDeviceConnector.connectToDevice();
                ueDeviceConnector.setListener(this.mConnectorListener);
                b = true;
                if (!b) {
                    Log.e(UEDeviceManager.TAG, "Failed to connect the device via SPP");
                    this.setConnectedDevice(null);
                    return;
                }
                while (true) {
                    final UESPPDevice uesppDevice = new UESPPDevice((MAC)buildDevice, ueDeviceConnector);
                    String s;
                    try {
                        final String deviceID = uesppDevice.getDeviceID();
                        if (deviceID != null && deviceID.startsWith("MEGABOO")) {
                            s = "MEGABOOM";
                        }
                        else if ((s = deviceID) != null) {
                            s = deviceID;
                            if (deviceID.startsWith("UE Boom")) {
                                s = "UE Boom";
                            }
                        }
                        buildDevice = (UEException)this.mDeviceFactory.buildDevice(s, (MAC)buildDevice, ueDeviceConnector);
                        if (buildDevice != null) {
                            Log.d(UEDeviceManager.TAG, "Connected device matches client ID: " + s);
                            this.setupNotificators(ueDeviceConnector);
                            if (((UEGenericDevice)buildDevice).getDeviceStreamingStatus().isDevicePairedStatus()) {
                                uesppDevice.setDeviceConnectionStatus(UEDeviceStatus.DOUBLEUP_CONNECTED);
                            }
                            else {
                                uesppDevice.setDeviceConnectionStatus(UEDeviceStatus.SINGLE_CONNECTED);
                            }
                            if (this.mConnectedDevice != null && this.mConnectedDevice.getConnectionType() == UEConnectionType.BLE) {
                                this.mConnectedDevice.getConnector().disconnectFromDevice();
                            }
                            this.setConnectedDevice((UEGenericDevice)buildDevice);
                            final Intent intent = new Intent("com.logitech.ue.centurion.CONNECTION_CHANGED");
                            intent.putExtra("status", ((UEDevice)buildDevice).getDeviceConnectionStatus().getValue());
                            LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent);
                            return;
                        }
                    }
                    catch (UEException buildDevice) {
                        Log.e(UEDeviceManager.TAG, "Failed to get device ID");
                        ueDeviceConnector.disconnectFromDevice();
                        this.setConnectedDevice(null);
                        return;
                    }
                    final String tag = UEDeviceManager.TAG;
                    buildDevice = (UEException)new StringBuilder();
                    Log.e(tag, ((StringBuilder)buildDevice).append("Client device ID ").append(s).append(" did not match patterns").toString());
                    ueDeviceConnector.disconnectFromDevice();
                }
            }
            catch (UEConnectionException ex) {
                continue;
            }
            break;
        }
    }
    
    public void setConnectedDevice(final UEGenericDevice mConnectedDevice) {
        this.mConnectedDevice = mConnectedDevice;
    }
    
    public void setEnableCache(final boolean b) {
        this.mIsCacheEnabled = b;
        if (this.getConnectedDevice() != null) {
            this.getConnectedDevice().setEnableCache(b);
        }
    }
    
    private class UEA2DPStateChangeReceiver extends BroadcastReceiver
    {
        private UEDeviceStatus currentState;
        private UEDeviceStatus previousState;
        
        private UEA2DPStateChangeReceiver() {
            this.currentState = UEDeviceStatus.DISCONNECTED;
            this.previousState = UEDeviceStatus.DISCONNECTED;
        }
        
        public void onReceive(final Context context, final Intent intent) {
            final MAC mac = new MAC(((BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getAddress());
            if (!UEDiscoveryManager.getInstance().isBTAddressValid(mac.toString())) {
                Log.d(UEDeviceManager.TAG, "Event from non UE device(" + mac + "). Do ignore it.");
            }
            else {
                if (intent.getAction().equals("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED")) {
                    this.currentState = UEDeviceStatus.getStatus(intent.getIntExtra("android.bluetooth.profile.extra.STATE", this.currentState.getValue()));
                    this.previousState = UEDeviceStatus.getStatus(intent.getIntExtra("android.bluetooth.profile.extra.PREVIOUS_STATE", this.previousState.getValue()));
                }
                else if (intent.getAction().equals("android.bluetooth.a2dp.profile.action.PLAYING_STATE_CHANGED")) {
                    this.currentState = UEDeviceStatus.DISCONNECTED;
                }
                Log.d(UEDeviceManager.TAG, "Device : " + mac + " State " + this.previousState + " -> " + this.currentState);
                if (this.currentState == UEDeviceStatus.DISCONNECTED) {
                    if (UEDeviceManager.this.mConnectedDevice == null) {
                        Log.w(UEDeviceManager.TAG, "No UE device is connected. Do nothing");
                    }
                    else if (UEDeviceManager.this.mConnectedDevice.getAddress().equals(mac)) {
                        Log.d(UEDeviceManager.TAG, "Connected device session closed");
                        UEDeviceManager.this.mConnectedDevice.getConnector().disconnectFromDevice();
                        UEDeviceManager.this.mConnectedDevice = null;
                        final Intent intent2 = new Intent("com.logitech.ue.centurion.CONNECTION_CHANGED");
                        intent2.putExtra("status", UEDeviceStatus.DISCONNECTED.getValue());
                        LocalBroadcastManager.getInstance(UEDeviceManager.this.mContext).sendBroadcast(intent2);
                    }
                    else {
                        Log.e(UEDeviceManager.TAG, "Odd! Disconnected device is NOT previous connected UE device");
                    }
                }
                else if (this.currentState.isBtClassicConnectedState()) {
                    UEDeviceManager.this.processDeviceConnection(mac);
                    if (UEDeviceManager.this.mConnectedDevice != null && UEDiscoveryManager.getInstance().isBLESearching()) {
                        UEDiscoveryManager.getInstance().stopBLESearch();
                    }
                }
            }
        }
    }
}
