// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion;

import android.support.annotation.NonNull;
import android.content.IntentFilter;
import java.util.Set;
import com.logitech.ue.centurion.utils.MAC;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Parcelable;
import android.text.TextUtils;
import com.logitech.ue.centurion.utils.UEUtils;
import android.bluetooth.BluetoothProfile$ServiceListener;
import android.bluetooth.BluetoothDevice;
import java.util.Iterator;
import android.bluetooth.le.ScanResult;
import java.util.List;
import android.os.Build$VERSION;
import android.os.AsyncTask;
import android.util.Log;
import android.content.Intent;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.BluetoothAdapter$LeScanCallback;
import java.util.concurrent.Semaphore;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothAdapter;
import java.util.concurrent.locks.ReentrantLock;
import android.annotation.TargetApi;

@TargetApi(21)
public class UEDiscoveryManager
{
    public static final String ACTION_BLE_DEVICE_FOUND = "com.logitech.ue.bluetooth.le.ACTION_BLE_DEVICE_FOUND";
    public static final String ACTION_BLE_PACKAGE_RECEIVED = "com.logitech.ue.bluetooth.le.ACTION_BLE_PACKAGE_RECEIVED";
    public static final String EXTRA_DEVICE = "device";
    public static final String EXTRA_RSSI = "rssi";
    public static final String EXTRA_SCAN_RECORD = "scan_record";
    private static final String LOGITECH_MAC_ADDRESS_PREFIX = "00:02:5B";
    private static final String LOGITECH_MAC_ADDRESS_PREFIX_2 = "88:C6:26";
    private static final String LOGITECH_MAC_ADDRESS_PREFIX_3 = "00:0D:44";
    private static final String TAG;
    private static UEDiscoveryManager mInstance;
    private volatile String mBLEListeningFilter;
    private ReentrantLock mBLELock;
    private volatile String mBLESearchingFilter;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothProfile mBluetoothProfile;
    BroadcastReceiver mBroadcastReceiver;
    private Context mContext;
    private final Semaphore mInitialisationSemaphore;
    private volatile boolean mIsBLEListening;
    private volatile boolean mIsBLESearching;
    private volatile boolean mIsScanning;
    private BluetoothAdapter$LeScanCallback mLeScanCallback;
    private ScanCallback mScanCallback;
    
    static {
        TAG = UEDiscoveryManager.class.getSimpleName();
        UEDiscoveryManager.mInstance = null;
    }
    
    private UEDiscoveryManager() {
        this.mInitialisationSemaphore = new Semaphore(0, true);
        this.mBLELock = new ReentrantLock(true);
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent obj) {
                Log.d(UEDiscoveryManager.TAG, "Broadcast receive " + obj);
                if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(obj.getAction()) && obj.getIntExtra("android.bluetooth.adapter.extra.STATE", -1) == 12) {
                    new AsyncTask<Void, Void, Void>() {
                        protected Void doInBackground(final Void... array) {
                            if (UEDiscoveryManager.this.mBluetoothAdapter.isEnabled()) {
                                UEDiscoveryManager.this.connectToBluetoothProfile();
                                UEDiscoveryManager.this.unRegisterReceiver();
                            }
                            return null;
                        }
                    }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, (Object[])new Void[0]);
                }
            }
        };
        if (Build$VERSION.SDK_INT >= 21) {
            this.mScanCallback = new ScanCallback() {
                public void onBatchScanResults(final List<ScanResult> list) {
                    if (list.size() > 0) {
                        for (final ScanResult scanResult : list) {
                            UEDiscoveryManager.this.onLeScan(scanResult.getDevice(), scanResult.getRssi(), scanResult.getScanRecord().getBytes());
                        }
                    }
                }
                
                public void onScanFailed(final int n) {
                    Log.w(UEDiscoveryManager.TAG, "BLE scan could NOT be started");
                    super.onScanFailed(n);
                }
                
                public void onScanResult(final int n, final ScanResult scanResult) {
                    UEDiscoveryManager.this.onLeScan(scanResult.getDevice(), scanResult.getRssi(), scanResult.getScanRecord().getBytes());
                }
            };
        }
        else if (Build$VERSION.SDK_INT >= 18) {
            this.mLeScanCallback = (BluetoothAdapter$LeScanCallback)new BluetoothAdapter$LeScanCallback() {
                public void onLeScan(final BluetoothDevice bluetoothDevice, final int n, final byte[] array) {
                    UEDiscoveryManager.this.onLeScan(bluetoothDevice, n, array);
                }
            };
        }
    }
    
    private void connectToBluetoothProfile() {
        if (!this.mBluetoothAdapter.getProfileProxy(this.mContext, (BluetoothProfile$ServiceListener)new BluetoothProfile$ServiceListener() {
            public void onServiceConnected(final int n, final BluetoothProfile bluetoothProfile) {
                Log.d(UEDiscoveryManager.TAG, "A2DP Service Connected");
                UEDiscoveryManager.this.mBluetoothProfile = bluetoothProfile;
                UEDiscoveryManager.this.mInitialisationSemaphore.release();
            }
            
            public void onServiceDisconnected(final int n) {
                Log.d(UEDiscoveryManager.TAG, "A2DP Service Disconnected");
            }
        }, 2)) {
            return;
        }
        try {
            this.mInitialisationSemaphore.acquire();
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    public static UEDiscoveryManager getInstance() {
        Label_0030: {
            if (UEDiscoveryManager.mInstance != null) {
                break Label_0030;
            }
            synchronized (UEDiscoveryManager.class) {
                if (UEDiscoveryManager.mInstance == null) {
                    UEDiscoveryManager.mInstance = new UEDiscoveryManager();
                }
                return UEDiscoveryManager.mInstance;
            }
        }
    }
    
    private void onLeScan(final BluetoothDevice bluetoothDevice, final int i, final byte[] array) {
        Log.d(UEDiscoveryManager.TAG, String.format("BLE scan. Address:%s RSSI:%d Record data:%s", bluetoothDevice.getAddress(), i, UEUtils.byteArrayToHexString(array)));
        if (this.mIsBLEListening && (this.mBLEListeningFilter == null || TextUtils.equals((CharSequence)this.mBLEListeningFilter, (CharSequence)bluetoothDevice.getAddress()))) {
            Log.d(UEDiscoveryManager.TAG, "BLE listening. Broadcast this package. Address: " + bluetoothDevice.getAddress());
            final Intent intent = new Intent("com.logitech.ue.bluetooth.le.ACTION_BLE_PACKAGE_RECEIVED");
            intent.putExtra("device", (Parcelable)bluetoothDevice);
            intent.putExtra("rssi", i);
            intent.putExtra("scan_record", array);
            LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent);
        }
        if (this.mIsBLESearching) {
            if (TextUtils.equals((CharSequence)this.mBLESearchingFilter, (CharSequence)bluetoothDevice.getAddress())) {
                Log.d(UEDiscoveryManager.TAG, "BLE search. Targeted device found. Stop BLE search. Address: " + bluetoothDevice.getAddress());
                this.stopBLESearch();
                final Intent intent2 = new Intent("com.logitech.ue.bluetooth.le.ACTION_BLE_DEVICE_FOUND");
                intent2.putExtra("device", (Parcelable)bluetoothDevice);
                LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent2);
            }
            else {
                Log.d(UEDiscoveryManager.TAG, "BLE search. Non targeted device found. Address: " + bluetoothDevice.getAddress());
            }
        }
    }
    
    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    private void startScan() {
        if (!this.mIsScanning) {
            if (Build$VERSION.SDK_INT >= 21) {
                Log.d(UEDiscoveryManager.TAG, "Starting scan on API >= 21");
                this.mBluetoothAdapter.getBluetoothLeScanner().startScan(this.mScanCallback);
            }
            else {
                Log.d(UEDiscoveryManager.TAG, "Starting scan on API < 21");
                this.mBluetoothAdapter.startLeScan(this.mLeScanCallback);
            }
        }
    }
    
    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    private void stopScan() {
        if (this.mIsScanning) {
            Log.w(UEDiscoveryManager.TAG, "Can't stop BLE scan. Scan is not running");
        }
        else if (!this.mIsBLEListening && !this.mIsBLESearching) {
            if (!this.mBluetoothAdapter.isEnabled()) {
                Log.e(UEDiscoveryManager.TAG, "Phone Bluetooth is NOT enabled");
            }
            else {
                this.mIsScanning = false;
                if (Build$VERSION.SDK_INT >= 21) {
                    Log.d(UEDiscoveryManager.TAG, "Stopping scan on API >= 21");
                    this.mBluetoothAdapter.getBluetoothLeScanner().stopScan(this.mScanCallback);
                }
                else {
                    Log.d(UEDiscoveryManager.TAG, "Stopping scan on API < 21");
                    this.mBluetoothAdapter.stopLeScan(this.mLeScanCallback);
                }
            }
        }
    }
    
    public MAC getConnectedSPPDevice() {
        final MAC mac = null;
        MAC mac2;
        if (this.mBluetoothProfile == null) {
            mac2 = mac;
        }
        else {
            final List devicesMatchingConnectionStates = this.mBluetoothProfile.getDevicesMatchingConnectionStates(new int[] { 2 });
            mac2 = mac;
            if (devicesMatchingConnectionStates != null) {
                final Iterator<BluetoothDevice> iterator = devicesMatchingConnectionStates.iterator();
                String upperCase;
                while (true) {
                    mac2 = mac;
                    if (!iterator.hasNext()) {
                        return mac2;
                    }
                    upperCase = iterator.next().getAddress().toUpperCase();
                    Log.d(UEDiscoveryManager.TAG, "Device BT address is: " + upperCase);
                    if (this.isBTAddressValid(upperCase)) {
                        break;
                    }
                    Log.w(UEDiscoveryManager.TAG, "Trying to connect to a non-UE device.");
                }
                Log.d(UEDiscoveryManager.TAG, "Device is UE device");
                mac2 = new MAC(upperCase);
            }
        }
        return mac2;
    }
    
    public Set<BluetoothDevice> getPairedDevices() {
        Set<BluetoothDevice> bondedDevices = (Set<BluetoothDevice>)this.mBluetoothAdapter.getBondedDevices();
        if (bondedDevices.size() <= 0) {
            bondedDevices = null;
        }
        return bondedDevices;
    }
    
    public void init(final Context mContext) {
        while (true) {
            Label_0073: {
                synchronized (this) {
                    if (this.isReady()) {
                        Log.w(UEDiscoveryManager.TAG, "Discovery manager has already been initialised");
                    }
                    else {
                        Log.d(UEDiscoveryManager.TAG, "Begin discovery manager initialisation");
                        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        this.mContext = mContext;
                        if (this.mBluetoothAdapter != null) {
                            if (!this.mBluetoothAdapter.isEnabled()) {
                                break Label_0073;
                            }
                            this.connectToBluetoothProfile();
                        }
                    }
                    return;
                }
            }
            this.registerReceiver();
        }
    }
    
    public boolean isBLEListening() {
        return this.mIsBLEListening;
    }
    
    public boolean isBLESearching() {
        return this.mIsBLESearching;
    }
    
    public boolean isBTAddressValid(String upperCase) {
        upperCase = upperCase.toUpperCase();
        return upperCase.startsWith("00:02:5B") || upperCase.startsWith("88:C6:26") || upperCase.startsWith("00:0D:44");
    }
    
    public boolean isReady() {
        return this.mContext != null;
    }
    
    public void registerReceiver() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter);
    }
    
    public void startBLEPackageListening(final String mbleListeningFilter) {
        this.mBLELock.lock();
        try {
            if (!this.isReady()) {
                Log.w(UEDiscoveryManager.TAG, "Can't start BLE listening. Manager is not initialised");
            }
            else if (!UEDeviceManager.getInstance().isBleSupported()) {
                Log.w(UEDiscoveryManager.TAG, "Can't start BLE listening. BLE is not supported");
                this.mBLELock.unlock();
            }
            else if (this.mIsBLEListening) {
                Log.w(UEDiscoveryManager.TAG, "Can't start BLE listening. Listening already launched");
                this.mBLELock.unlock();
            }
            else if (!this.mBluetoothAdapter.isEnabled()) {
                Log.w(UEDiscoveryManager.TAG, "Phone Bluetooth is NOT enabled");
                this.mBLELock.unlock();
            }
            else {
                this.mIsBLEListening = true;
                this.mBLEListeningFilter = mbleListeningFilter;
                this.startScan();
                this.mBLELock.unlock();
            }
        }
        finally {
            this.mBLELock.unlock();
        }
    }
    
    public void startBLESearch(@NonNull final String s) {
        this.mBLELock.lock();
        try {
            if (!this.isReady()) {
                Log.w(UEDiscoveryManager.TAG, "Can't start BLE device search. Manager is not initialised");
            }
            else if (!UEDeviceManager.getInstance().isBleSupported()) {
                Log.w(UEDiscoveryManager.TAG, "Can't start BLE device search. BLE is not supported");
                this.mBLELock.unlock();
            }
            else if (!this.mBluetoothAdapter.isEnabled()) {
                Log.e(UEDiscoveryManager.TAG, "Phone Bluetooth is NOT enabled");
                this.mBLELock.unlock();
            }
            else {
                if (this.mIsBLESearching) {
                    Log.d(UEDiscoveryManager.TAG, "Search is already running. Update device filter to " + this.mBLESearchingFilter);
                    this.mBLESearchingFilter = s;
                }
                else {
                    this.mIsBLESearching = true;
                    this.mBLESearchingFilter = s;
                    this.startScan();
                }
                this.mBLELock.unlock();
            }
        }
        finally {
            this.mBLELock.unlock();
        }
    }
    
    public void stopBLEPackageListening() {
        this.mBLELock.lock();
        try {
            if (!this.mIsBLEListening) {
                Log.w(UEDiscoveryManager.TAG, "Can't stop BLE listening. Listening is not running");
            }
            else {
                this.mIsBLEListening = false;
                this.mBLEListeningFilter = null;
                this.stopScan();
                this.mBLELock.unlock();
            }
        }
        finally {
            this.mBLELock.unlock();
        }
    }
    
    public void stopBLESearch() {
        this.mBLELock.lock();
        try {
            if (!this.mIsBLESearching) {
                Log.w(UEDiscoveryManager.TAG, "Can't stop BLE device search. Search is not running");
            }
            else {
                this.mIsBLESearching = false;
                this.mBLESearchingFilter = null;
                this.stopScan();
                this.mBLELock.unlock();
            }
        }
        finally {
            this.mBLELock.unlock();
        }
    }
    
    public void unRegisterReceiver() {
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
    }
}
