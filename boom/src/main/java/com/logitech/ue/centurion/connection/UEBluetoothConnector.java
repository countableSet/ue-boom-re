// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.connection;

import java.util.Arrays;
import java.io.IOException;
import android.util.Log;
import com.logitech.ue.centurion.exceptions.UEConnectionException;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.concurrent.locks.ReentrantLock;
import android.bluetooth.BluetoothSocket;
import com.logitech.ue.centurion.utils.MAC;
import java.util.UUID;

public class UEBluetoothConnector implements IUEConnector
{
    private static final int STREAM_BUFFER = 256;
    private static final String TAG;
    public static final UUID UE_BOOM_UUID;
    protected MAC mAddress;
    protected volatile ConnectionState mConnectionState;
    protected UEConnectorListener mListener;
    protected SessionThread mSessionThread;
    protected BluetoothSocket mSocket;
    protected final ReentrantLock mThreadSafeWorkLock;
    protected InputStream mmInStream;
    protected OutputStream mmOutStream;
    
    static {
        TAG = UEBluetoothConnector.class.getSimpleName();
        UE_BOOM_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    }
    
    public UEBluetoothConnector(final MAC mAddress) {
        this.mListener = null;
        this.mConnectionState = ConnectionState.READY;
        this.mThreadSafeWorkLock = new ReentrantLock(true);
        this.mAddress = mAddress;
        this.setState(ConnectionState.READY);
    }
    
    private ConnectionState getState() {
        return this.mConnectionState;
    }
    
    private void setState(final ConnectionState mConnectionState) {
        this.mThreadSafeWorkLock.lock();
        this.mConnectionState = mConnectionState;
        this.mThreadSafeWorkLock.unlock();
    }
    
    public void checkConnection() throws UEConnectionException {
        if (this.mConnectionState != ConnectionState.CONNECTED) {
            throw new UEConnectionException("Connector is not connected. Call open connection first");
        }
    }
    
    @Override
    public void connectToDevice() throws UEConnectionException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        com/logitech/ue/centurion/connection/UEBluetoothConnector.mThreadSafeWorkLock:Ljava/util/concurrent/locks/ReentrantLock;
        //     4: invokevirtual   java/util/concurrent/locks/ReentrantLock.lock:()V
        //     7: aload_0        
        //     8: getfield        com/logitech/ue/centurion/connection/UEBluetoothConnector.mConnectionState:Lcom/logitech/ue/centurion/connection/UEBluetoothConnector$ConnectionState;
        //    11: getstatic       com/logitech/ue/centurion/connection/UEBluetoothConnector$ConnectionState.READY:Lcom/logitech/ue/centurion/connection/UEBluetoothConnector$ConnectionState;
        //    14: if_acmpeq       39
        //    17: new             Lcom/logitech/ue/centurion/exceptions/UEConnectionException;
        //    20: astore_1       
        //    21: aload_1        
        //    22: ldc             "Connector is already connected"
        //    24: invokespecial   com/logitech/ue/centurion/exceptions/UEConnectionException.<init>:(Ljava/lang/String;)V
        //    27: aload_1        
        //    28: athrow         
        //    29: astore_1       
        //    30: aload_0        
        //    31: getfield        com/logitech/ue/centurion/connection/UEBluetoothConnector.mThreadSafeWorkLock:Ljava/util/concurrent/locks/ReentrantLock;
        //    34: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
        //    37: aload_1        
        //    38: athrow         
        //    39: getstatic       com/logitech/ue/centurion/connection/UEBluetoothConnector.TAG:Ljava/lang/String;
        //    42: ldc             "Connect to device"
        //    44: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    47: pop            
        //    48: aload_0        
        //    49: invokestatic    android/bluetooth/BluetoothAdapter.getDefaultAdapter:()Landroid/bluetooth/BluetoothAdapter;
        //    52: aload_0        
        //    53: getfield        com/logitech/ue/centurion/connection/UEBluetoothConnector.mAddress:Lcom/logitech/ue/centurion/utils/MAC;
        //    56: invokevirtual   com/logitech/ue/centurion/utils/MAC.toString:()Ljava/lang/String;
        //    59: invokevirtual   android/bluetooth/BluetoothAdapter.getRemoteDevice:(Ljava/lang/String;)Landroid/bluetooth/BluetoothDevice;
        //    62: getstatic       com/logitech/ue/centurion/connection/UEBluetoothConnector.UE_BOOM_UUID:Ljava/util/UUID;
        //    65: invokevirtual   android/bluetooth/BluetoothDevice.createInsecureRfcommSocketToServiceRecord:(Ljava/util/UUID;)Landroid/bluetooth/BluetoothSocket;
        //    68: putfield        com/logitech/ue/centurion/connection/UEBluetoothConnector.mSocket:Landroid/bluetooth/BluetoothSocket;
        //    71: aload_0        
        //    72: getfield        com/logitech/ue/centurion/connection/UEBluetoothConnector.mSocket:Landroid/bluetooth/BluetoothSocket;
        //    75: invokevirtual   android/bluetooth/BluetoothSocket.connect:()V
        //    78: aload_0        
        //    79: aload_0        
        //    80: getfield        com/logitech/ue/centurion/connection/UEBluetoothConnector.mSocket:Landroid/bluetooth/BluetoothSocket;
        //    83: invokevirtual   android/bluetooth/BluetoothSocket.getInputStream:()Ljava/io/InputStream;
        //    86: putfield        com/logitech/ue/centurion/connection/UEBluetoothConnector.mmInStream:Ljava/io/InputStream;
        //    89: aload_0        
        //    90: aload_0        
        //    91: getfield        com/logitech/ue/centurion/connection/UEBluetoothConnector.mSocket:Landroid/bluetooth/BluetoothSocket;
        //    94: invokevirtual   android/bluetooth/BluetoothSocket.getOutputStream:()Ljava/io/OutputStream;
        //    97: putfield        com/logitech/ue/centurion/connection/UEBluetoothConnector.mmOutStream:Ljava/io/OutputStream;
        //   100: new             Lcom/logitech/ue/centurion/connection/UEBluetoothConnector$SessionThread;
        //   103: astore_1       
        //   104: aload_1        
        //   105: aload_0        
        //   106: invokespecial   com/logitech/ue/centurion/connection/UEBluetoothConnector$SessionThread.<init>:(Lcom/logitech/ue/centurion/connection/UEBluetoothConnector;)V
        //   109: aload_0        
        //   110: aload_1        
        //   111: putfield        com/logitech/ue/centurion/connection/UEBluetoothConnector.mSessionThread:Lcom/logitech/ue/centurion/connection/UEBluetoothConnector$SessionThread;
        //   114: aload_0        
        //   115: getfield        com/logitech/ue/centurion/connection/UEBluetoothConnector.mSessionThread:Lcom/logitech/ue/centurion/connection/UEBluetoothConnector$SessionThread;
        //   118: bipush          10
        //   120: invokevirtual   com/logitech/ue/centurion/connection/UEBluetoothConnector$SessionThread.setPriority:(I)V
        //   123: aload_0        
        //   124: getfield        com/logitech/ue/centurion/connection/UEBluetoothConnector.mSessionThread:Lcom/logitech/ue/centurion/connection/UEBluetoothConnector$SessionThread;
        //   127: invokevirtual   com/logitech/ue/centurion/connection/UEBluetoothConnector$SessionThread.start:()V
        //   130: aload_0        
        //   131: getstatic       com/logitech/ue/centurion/connection/UEBluetoothConnector$ConnectionState.CONNECTED:Lcom/logitech/ue/centurion/connection/UEBluetoothConnector$ConnectionState;
        //   134: invokespecial   com/logitech/ue/centurion/connection/UEBluetoothConnector.setState:(Lcom/logitech/ue/centurion/connection/UEBluetoothConnector$ConnectionState;)V
        //   137: aload_0        
        //   138: getfield        com/logitech/ue/centurion/connection/UEBluetoothConnector.mThreadSafeWorkLock:Ljava/util/concurrent/locks/ReentrantLock;
        //   141: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
        //   144: return         
        //   145: astore_1       
        //   146: getstatic       com/logitech/ue/centurion/connection/UEBluetoothConnector.TAG:Ljava/lang/String;
        //   149: ldc             "Socket creation failed. Wait for 1s and retry once."
        //   151: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   154: pop            
        //   155: ldc2_w          1000
        //   158: invokestatic    java/lang/Thread.sleep:(J)V
        //   161: aload_0        
        //   162: getfield        com/logitech/ue/centurion/connection/UEBluetoothConnector.mSocket:Landroid/bluetooth/BluetoothSocket;
        //   165: invokevirtual   android/bluetooth/BluetoothSocket.connect:()V
        //   168: goto            78
        //   171: astore_1       
        //   172: aload_1        
        //   173: invokevirtual   java/io/IOException.printStackTrace:()V
        //   176: aload_0        
        //   177: invokevirtual   com/logitech/ue/centurion/connection/UEBluetoothConnector.disconnectFromDevice:()V
        //   180: new             Lcom/logitech/ue/centurion/exceptions/UEConnectionException;
        //   183: astore_1       
        //   184: aload_1        
        //   185: ldc             "Connector is not connected. Call open connection first"
        //   187: invokespecial   com/logitech/ue/centurion/exceptions/UEConnectionException.<init>:(Ljava/lang/String;)V
        //   190: aload_1        
        //   191: athrow         
        //   192: astore_1       
        //   193: aload_1        
        //   194: invokevirtual   java/lang/InterruptedException.printStackTrace:()V
        //   197: goto            161
        //   200: astore_1       
        //   201: getstatic       com/logitech/ue/centurion/connection/UEBluetoothConnector.TAG:Ljava/lang/String;
        //   204: ldc             "Fail to open I/O via socket"
        //   206: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   209: pop            
        //   210: new             Lcom/logitech/ue/centurion/exceptions/UEConnectionException;
        //   213: astore_1       
        //   214: aload_1        
        //   215: ldc             "Connector is not connected. Call open connection first"
        //   217: invokespecial   com/logitech/ue/centurion/exceptions/UEConnectionException.<init>:(Ljava/lang/String;)V
        //   220: aload_1        
        //   221: athrow         
        //    Exceptions:
        //  throws com.logitech.ue.centurion.exceptions.UEConnectionException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  7      29     29     39     Any
        //  39     48     29     39     Any
        //  48     78     145    200    Ljava/io/IOException;
        //  48     78     29     39     Any
        //  78     100    200    222    Ljava/io/IOException;
        //  78     100    29     39     Any
        //  100    137    29     39     Any
        //  146    155    29     39     Any
        //  155    161    192    200    Ljava/lang/InterruptedException;
        //  155    161    29     39     Any
        //  161    168    171    192    Ljava/io/IOException;
        //  161    168    29     39     Any
        //  172    192    29     39     Any
        //  193    197    29     39     Any
        //  201    222    29     39     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0078:
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
    public void disconnectFromDevice() {
        this.mThreadSafeWorkLock.lock();
        if (this.mConnectionState == ConnectionState.DISCONNECTED) {
            Log.w(UEBluetoothConnector.TAG, "Connection already closed");
        }
        else {
            Log.d(UEBluetoothConnector.TAG, "Disconnect from device");
            while (true) {
                try {
                    this.mmOutStream.close();
                    try {
                        this.mmInStream.close();
                        try {
                            this.mSocket.close();
                            if (this.mListener != null) {
                                this.mListener.onDisconnect(this);
                            }
                            this.setState(ConnectionState.DISCONNECTED);
                        }
                        catch (Exception ex) {}
                    }
                    catch (Exception ex2) {}
                }
                catch (Exception ex3) {
                    continue;
                }
                break;
            }
        }
        this.mThreadSafeWorkLock.unlock();
    }
    
    @Override
    public UEConnectionType getConnectionType() {
        return UEConnectionType.SPP;
    }
    
    public UEConnectorListener getListener() {
        return this.mListener;
    }
    
    protected void onDataReceived(final byte[] array) throws UEConnectionException {
        if (this.mListener != null) {
            this.mListener.onDataReceived(this, array);
        }
    }
    
    protected void onDataSent(final byte[] array) {
        if (this.mListener != null) {
            this.mListener.onDataSent(this, array);
        }
    }
    
    protected void sendData(final byte[] b) throws UEConnectionException {
        this.checkConnection();
        try {
            this.mmOutStream.write(b);
            this.mmOutStream.flush();
            this.onDataSent(b);
        }
        catch (IOException ex) {
            Log.w(UEBluetoothConnector.TAG, "Connection lost while sending data");
            this.disconnectFromDevice();
            throw new UEConnectionException("Failed to write to the stream. Stream connection lost.");
        }
    }
    
    public void setListener(final UEConnectorListener mListener) {
        this.mListener = mListener;
    }
    
    enum ConnectionState
    {
        CONNECTED, 
        CONNECTING, 
        DISCONNECTED, 
        READY;
    }
    
    private class SessionThread extends Thread
    {
        public SessionThread() {
        }
        
        @Override
        public void run() {
            Log.d(UEBluetoothConnector.TAG, "Begin session thread");
            final byte[] array = new byte[256];
            try {
                while (true) {
                    Arrays.fill(array, (byte)0);
                    final int read = UEBluetoothConnector.this.mmInStream.read(array);
                    final byte[] array2 = new byte[read];
                    System.arraycopy(array, 0, array2, 0, read);
                    UEBluetoothConnector.this.onDataReceived(array2);
                }
            }
            catch (UEConnectionException ex) {}
            catch (IOException ex2) {
                goto Label_0056;
            }
        }
    }
    
    public interface UEConnectorListener
    {
        void onDataReceived(final UEBluetoothConnector p0, final byte[] p1);
        
        void onDataSent(final UEBluetoothConnector p0, final byte[] p1);
        
        void onDisconnect(final UEBluetoothConnector p0);
    }
}
