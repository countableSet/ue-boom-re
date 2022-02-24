// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.connection;

import java.io.Serializable;
import com.logitech.ue.centurion.ble.exceptions.UEBLECharactiristicException;
import com.logitech.ue.centurion.ble.UEBLECharacteristics;
import android.bluetooth.BluetoothDevice;
import com.logitech.ue.centurion.exceptions.UEConnectionTimeOutException;
import java.util.concurrent.TimeUnit;
import android.os.Build$VERSION;
import android.bluetooth.BluetoothAdapter;
import com.logitech.ue.centurion.ble.exceptions.UEBLEConnectionException;
import java.util.Iterator;
import java.util.List;
import android.bluetooth.BluetoothGattService;
import android.util.Log;
import com.logitech.ue.centurion.utils.UEUtils;
import android.bluetooth.BluetoothGattCharacteristic;
import java.util.concurrent.locks.ReentrantLock;
import android.bluetooth.BluetoothGattCallback;
import java.util.concurrent.Semaphore;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import com.logitech.ue.centurion.utils.MAC;
import java.util.UUID;
import android.annotation.TargetApi;

@TargetApi(18)
public class UEBLEConnector implements IUEConnector
{
    private static final int BLE_CONNECTION_TIMEOUT = 60000;
    private static final int BLE_READ_WRITE_TIMEOUT = 2000;
    public static final int SHORT_ADK4_UUID = 50553342;
    private static final String TAG;
    public static final UUID UUID_PRIMARY_SERVICE;
    public static final UUID UUID_PRIMARY_SERVICE_ADK4;
    private MAC mAddress;
    private volatile ConnectionState mConnectionState;
    private Context mContext;
    private volatile byte[] mCurrentResult;
    private BluetoothGatt mGatt;
    private final Semaphore mGattConnectSemaphore;
    BluetoothGattCallback mGattListener;
    private UEConnectorListener mListener;
    private final Semaphore mMessageSemaphore;
    private final ReentrantLock mThreadSafeWorkLock;
    private volatile UUID mWorkingUUID;
    
    static {
        TAG = UEBLEConnector.class.getSimpleName();
        UUID_PRIMARY_SERVICE = UUID.fromString("757ed3e4-1828-4a0c-8362-c229c3a6da72");
        UUID_PRIMARY_SERVICE_ADK4 = UUID.fromString("000061fe-0000-1000-8000-00805f9b34fb");
    }
    
    public UEBLEConnector(final MAC mAddress, final Context mContext) {
        this.mConnectionState = ConnectionState.READY;
        this.mGattConnectSemaphore = new Semaphore(0, true);
        this.mMessageSemaphore = new Semaphore(0, true);
        this.mThreadSafeWorkLock = new ReentrantLock();
        this.mGattListener = new BluetoothGattCallback() {
            public void onCharacteristicRead(final BluetoothGatt bluetoothGatt, final BluetoothGattCharacteristic bluetoothGattCharacteristic, final int i) {
                if (i == 0) {
                    Log.d(UEBLEConnector.TAG, String.format("Success on reading. Address: %s. Characteristic: %s. Value: %s", UEBLEConnector.this.mAddress, bluetoothGattCharacteristic.getUuid(), UEUtils.byteArrayToHexString(bluetoothGattCharacteristic.getValue())));
                    if (UEBLEConnector.this.mListener != null) {
                        UEBLEConnector.this.mListener.onCharacteristicsRead(UEBLEConnector.this, bluetoothGattCharacteristic);
                    }
                    if (bluetoothGattCharacteristic.getUuid().equals(UEBLEConnector.this.mWorkingUUID)) {
                        UEBLEConnector.this.mCurrentResult = bluetoothGattCharacteristic.getValue();
                        UEBLEConnector.this.mMessageSemaphore.release();
                    }
                }
                else {
                    Log.e(UEBLEConnector.TAG, String.format("Error on reading characteristic. Address: %s. Status: 0x%02X", UEBLEConnector.this.mAddress, i));
                }
            }
            
            public void onCharacteristicWrite(final BluetoothGatt bluetoothGatt, final BluetoothGattCharacteristic bluetoothGattCharacteristic, final int i) {
                if (i == 0) {
                    Log.d(UEBLEConnector.TAG, String.format("Success on writing. Address: %s. Characteristic: %s", UEBLEConnector.this.mAddress, bluetoothGattCharacteristic.getUuid()));
                    if (UEBLEConnector.this.mListener != null) {
                        UEBLEConnector.this.mListener.onCharacteristicsWrite(UEBLEConnector.this, bluetoothGattCharacteristic);
                    }
                    if (bluetoothGattCharacteristic.getUuid().equals(UEBLEConnector.this.mWorkingUUID)) {
                        UEBLEConnector.this.mMessageSemaphore.release();
                    }
                }
                else {
                    Log.e(UEBLEConnector.TAG, String.format("Error on reading characteristic. Address: %s. Status: 0x%02X", UEBLEConnector.this.mAddress, i));
                }
            }
            
            public void onConnectionStateChange(final BluetoothGatt bluetoothGatt, final int n, final int i) {
                if (i == 2) {
                    Log.d(UEBLEConnector.TAG, String.format("GATT Connected. Start service discovery. Address: %s", UEBLEConnector.this.mAddress));
                    UEBLEConnector.this.mConnectionState = ConnectionState.SEARCHING_SERVICES;
                    UEBLEConnector.this.mGatt.discoverServices();
                }
                else if (i == 0) {
                    Log.d(UEBLEConnector.TAG, String.format("GATT Disconnected. Address: %s", UEBLEConnector.this.mAddress));
                    if (UEBLEConnector.this.mConnectionState == ConnectionState.CONNECTING) {
                        UEBLEConnector.this.mGattConnectSemaphore.release();
                    }
                    else if (UEBLEConnector.this.mConnectionState == ConnectionState.CONNECTED && UEBLEConnector.this.mListener != null) {
                        UEBLEConnector.this.mListener.onDisconnect(UEBLEConnector.this);
                    }
                    UEBLEConnector.this.disconnectFromDevice();
                }
                else {
                    Log.e(UEBLEConnector.TAG, String.format("GATT error state 0x%02X. Address: %s", i, UEBLEConnector.this.mAddress));
                }
            }
            
            public void onServicesDiscovered(final BluetoothGatt bluetoothGatt, final int i) {
                if (i == 0) {
                    final List services = UEBLEConnector.this.mGatt.getServices();
                    final StringBuilder append = new StringBuilder().append("Finish service discovering. Address: ").append(UEBLEConnector.this.mAddress).append(". Services ").append(services.size()).append(" found. Services: ");
                    final Iterator<BluetoothGattService> iterator = services.iterator();
                    while (iterator.hasNext()) {
                        append.append(iterator.next().getUuid()).append(" ");
                    }
                    Log.d(UEBLEConnector.TAG, append.toString());
                    UEBLEConnector.this.mConnectionState = ConnectionState.CONNECTED;
                    if (UEBLEConnector.this.mListener != null) {
                        UEBLEConnector.this.mListener.onConnect(UEBLEConnector.this);
                    }
                    UEBLEConnector.this.mGattConnectSemaphore.release();
                }
                else {
                    Log.e(UEBLEConnector.TAG, String.format("Error services discovering. Address: %s Status: 0x%02X. Retry", UEBLEConnector.this.mAddress, i));
                    UEBLEConnector.this.mGatt.discoverServices();
                }
            }
        };
        this.mAddress = mAddress;
        this.mContext = mContext;
    }
    
    public void checkConnection() throws UEBLEConnectionException {
        if (this.mConnectionState != ConnectionState.CONNECTED) {
            throw new UEBLEConnectionException("No BLE device is currently connected");
        }
    }
    
    @TargetApi(23)
    @Override
    public void connectToDevice() throws UEConnectionTimeOutException {
        Log.d(UEBLEConnector.TAG, "Begin Gatt connection to device " + this.mAddress);
        this.mConnectionState = ConnectionState.CONNECTING;
        final BluetoothDevice remoteDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(this.mAddress.toString());
        if (Build$VERSION.SDK_INT >= 23) {
            this.mGatt = remoteDevice.connectGatt(this.mContext, false, this.mGattListener, 2);
        }
        else {
            this.mGatt = remoteDevice.connectGatt(this.mContext, false, this.mGattListener);
        }
        Label_0143: {
            try {
                if (!this.mGattConnectSemaphore.tryAcquire(60000L, TimeUnit.MILLISECONDS)) {
                    break Label_0143;
                }
                if (this.mConnectionState != ConnectionState.CONNECTED) {
                    Log.e(UEBLEConnector.TAG, "GATT connection failed");
                    throw new UEConnectionTimeOutException();
                }
            }
            catch (InterruptedException ex) {}
            return;
        }
        Log.e(UEBLEConnector.TAG, "GATT connection timeout");
        this.disconnectFromDevice();
        throw new UEConnectionTimeOutException();
    }
    
    @Override
    public void disconnectFromDevice() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        com/logitech/ue/centurion/connection/UEBLEConnector.mThreadSafeWorkLock:Ljava/util/concurrent/locks/ReentrantLock;
        //     4: invokevirtual   java/util/concurrent/locks/ReentrantLock.lock:()V
        //     7: aload_0        
        //     8: getfield        com/logitech/ue/centurion/connection/UEBLEConnector.mConnectionState:Lcom/logitech/ue/centurion/connection/UEBLEConnector$ConnectionState;
        //    11: getstatic       com/logitech/ue/centurion/connection/UEBLEConnector$ConnectionState.DISCONNECTED:Lcom/logitech/ue/centurion/connection/UEBLEConnector$ConnectionState;
        //    14: if_acmpne       34
        //    17: getstatic       com/logitech/ue/centurion/connection/UEBLEConnector.TAG:Ljava/lang/String;
        //    20: ldc             "Already disconnected from device"
        //    22: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //    25: pop            
        //    26: aload_0        
        //    27: getfield        com/logitech/ue/centurion/connection/UEBLEConnector.mThreadSafeWorkLock:Ljava/util/concurrent/locks/ReentrantLock;
        //    30: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
        //    33: return         
        //    34: getstatic       com/logitech/ue/centurion/connection/UEBLEConnector.TAG:Ljava/lang/String;
        //    37: ldc             "Disconnected from device"
        //    39: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    42: pop            
        //    43: aload_0        
        //    44: getstatic       com/logitech/ue/centurion/connection/UEBLEConnector$ConnectionState.DISCONNECTED:Lcom/logitech/ue/centurion/connection/UEBLEConnector$ConnectionState;
        //    47: putfield        com/logitech/ue/centurion/connection/UEBLEConnector.mConnectionState:Lcom/logitech/ue/centurion/connection/UEBLEConnector$ConnectionState;
        //    50: aload_0        
        //    51: getfield        com/logitech/ue/centurion/connection/UEBLEConnector.mGatt:Landroid/bluetooth/BluetoothGatt;
        //    54: astore_1       
        //    55: aload_1        
        //    56: ifnull          66
        //    59: aload_0        
        //    60: getfield        com/logitech/ue/centurion/connection/UEBLEConnector.mGatt:Landroid/bluetooth/BluetoothGatt;
        //    63: invokevirtual   android/bluetooth/BluetoothGatt.close:()V
        //    66: aload_0        
        //    67: getfield        com/logitech/ue/centurion/connection/UEBLEConnector.mListener:Lcom/logitech/ue/centurion/connection/UEBLEConnector$UEConnectorListener;
        //    70: ifnull          26
        //    73: aload_0        
        //    74: getfield        com/logitech/ue/centurion/connection/UEBLEConnector.mListener:Lcom/logitech/ue/centurion/connection/UEBLEConnector$UEConnectorListener;
        //    77: aload_0        
        //    78: invokeinterface com/logitech/ue/centurion/connection/UEBLEConnector$UEConnectorListener.onDisconnect:(Lcom/logitech/ue/centurion/connection/UEBLEConnector;)V
        //    83: goto            26
        //    86: astore_1       
        //    87: aload_0        
        //    88: getfield        com/logitech/ue/centurion/connection/UEBLEConnector.mThreadSafeWorkLock:Ljava/util/concurrent/locks/ReentrantLock;
        //    91: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
        //    94: aload_1        
        //    95: athrow         
        //    96: astore_1       
        //    97: aload_1        
        //    98: invokevirtual   java/lang/Exception.printStackTrace:()V
        //   101: goto            66
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  7      26     86     96     Any
        //  34     55     86     96     Any
        //  59     66     96     104    Ljava/lang/Exception;
        //  59     66     86     96     Any
        //  66     83     86     96     Any
        //  97     101    86     96     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0066:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public UEConnectionType getConnectionType() {
        return UEConnectionType.BLE;
    }
    
    public UEConnectorListener getListener() {
        return this.mListener;
    }
    
    public byte[] readCharacteristics(final String s) throws UEBLEConnectionException, UEBLECharactiristicException, UEConnectionTimeOutException {
        while (true) {
            this.checkConnection();
            this.mThreadSafeWorkLock.lock();
            long currentTimeMillis = 0L;
            Serializable uuid = null;
            Object o = null;
            final Throwable t;
            Label_0104: {
                try {
                    currentTimeMillis = System.currentTimeMillis();
                    uuid = UEBLECharacteristics.getUUID(s);
                    if ((o = this.mGatt.getService(UEBLEConnector.UUID_PRIMARY_SERVICE)) == null) {
                        o = this.mGatt.getService(UEBLEConnector.UUID_PRIMARY_SERVICE_ADK4);
                    }
                    if (o == null) {
                        Log.e(UEBLEConnector.TAG, "Requested service does NOT exist");
                        throw new UEBLECharactiristicException("no service");
                    }
                    break Label_0104;
                }
                catch (InterruptedException ex2) {
                    this.mWorkingUUID = null;
                    this.mCurrentResult = null;
                    this.mThreadSafeWorkLock.unlock();
                    return null;
                    while (true) {
                        Log.e(UEBLEConnector.TAG, String.format("Requested characteristic %s does NOT exist", t));
                        throw new UEBLECharactiristicException(((UUID)uuid).toString());
                        o = ((BluetoothGattService)o).getCharacteristic((UUID)uuid);
                        continue;
                    }
                }
                // iftrue(Label_0174:, o != null)
                finally {
                    this.mWorkingUUID = null;
                    this.mCurrentResult = null;
                    this.mThreadSafeWorkLock.unlock();
                }
            }
            Label_0174: {
                this.mWorkingUUID = (UUID)uuid;
            }
            final String tag = UEBLEConnector.TAG;
            uuid = new StringBuilder();
            Log.d(tag, ((StringBuilder)uuid).append("Begin read characteristic ").append(((BluetoothGattCharacteristic)o).getUuid().toString()).toString());
            if (this.mGatt.readCharacteristic((BluetoothGattCharacteristic)o)) {
                if (this.mMessageSemaphore.tryAcquire(2000L, TimeUnit.MILLISECONDS)) {
                    Log.d(UEBLEConnector.TAG, String.format("Read characteristic %s success. Time elapsed %d", t, System.currentTimeMillis() - currentTimeMillis));
                    final byte[] mCurrentResult = this.mCurrentResult;
                    this.mWorkingUUID = null;
                    this.mCurrentResult = null;
                    this.mThreadSafeWorkLock.unlock();
                    return mCurrentResult;
                }
                Log.w(UEBLEConnector.TAG, String.format("Read characteristic %s time out", t));
                throw new UEConnectionTimeOutException();
            }
            else {
                Log.e(UEBLEConnector.TAG, String.format("Read characteristic %s faild", t));
                final UEConnectionTimeOutException ex = new UEConnectionTimeOutException();
            }
        }
    }
    
    public void setListener(final UEConnectorListener mListener) {
        this.mListener = mListener;
    }
    
    public void writeCharacteristics(final String s, byte[] value) throws UEBLEConnectionException, UEBLECharactiristicException, UEConnectionTimeOutException {
        while (true) {
            this.checkConnection();
            this.mThreadSafeWorkLock.lock();
            long currentTimeMillis = 0L;
            UUID uuid = null;
            Object o = null;
            final Throwable t;
            Label_0101: {
                try {
                    currentTimeMillis = System.currentTimeMillis();
                    uuid = UEBLECharacteristics.getUUID(s);
                    if ((o = this.mGatt.getService(UEBLEConnector.UUID_PRIMARY_SERVICE)) == null) {
                        o = this.mGatt.getService(UEBLEConnector.UUID_PRIMARY_SERVICE_ADK4);
                    }
                    if (o == null) {
                        Log.e(UEBLEConnector.TAG, "Requested service does NOT exist");
                        throw new UEBLECharactiristicException("no service");
                    }
                    break Label_0101;
                }
                catch (InterruptedException ex2) {
                    this.mWorkingUUID = null;
                    this.mCurrentResult = null;
                    this.mThreadSafeWorkLock.unlock();
                    return;
                    while (true) {
                        Log.e(UEBLEConnector.TAG, String.format("Requested characteristic %s does NOT exist", t));
                        throw new UEBLECharactiristicException(uuid.toString());
                        o = ((BluetoothGattService)o).getCharacteristic(uuid);
                        continue;
                    }
                }
                // iftrue(Label_0171:, o != null)
                finally {
                    this.mWorkingUUID = null;
                    this.mCurrentResult = null;
                    this.mThreadSafeWorkLock.unlock();
                }
            }
            Label_0171: {
                this.mWorkingUUID = uuid;
            }
            ((BluetoothGattCharacteristic)o).setValue(value);
            final String tag = UEBLEConnector.TAG;
            value = (byte[])(Object)new StringBuilder();
            Log.d(tag, ((StringBuilder)(Object)value).append("Begin write characteristic ").append(((BluetoothGattCharacteristic)o).getUuid().toString()).toString());
            if (this.mGatt.writeCharacteristic((BluetoothGattCharacteristic)o)) {
                if (this.mMessageSemaphore.tryAcquire(2000L, TimeUnit.MILLISECONDS)) {
                    Log.d(UEBLEConnector.TAG, String.format("Write characteristic %s success. Time elapsed %d", t, System.currentTimeMillis() - currentTimeMillis));
                    this.mWorkingUUID = null;
                    this.mCurrentResult = null;
                    this.mThreadSafeWorkLock.unlock();
                    return;
                }
                Log.w(UEBLEConnector.TAG, String.format("Write characteristic %s time out", t));
                throw new UEConnectionTimeOutException();
            }
            else {
                Log.e(UEBLEConnector.TAG, String.format("Write characteristic %s failed", t));
                final UEConnectionTimeOutException ex = new UEConnectionTimeOutException();
            }
        }
    }
    
    enum ConnectionState
    {
        CONNECTED, 
        CONNECTING, 
        DISCONNECTED, 
        READY, 
        SEARCHING_SERVICES;
    }
    
    public interface UEConnectorListener
    {
        void onCharacteristicsRead(final UEBLEConnector p0, final BluetoothGattCharacteristic p1);
        
        void onCharacteristicsWrite(final UEBLEConnector p0, final BluetoothGattCharacteristic p1);
        
        void onConnect(final UEBLEConnector p0);
        
        void onDisconnect(final UEBLEConnector p0);
    }
}
