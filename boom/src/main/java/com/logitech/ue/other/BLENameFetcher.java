// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import android.support.annotation.NonNull;
import android.util.Log;
import com.logitech.ue.centurion.utils.MAC;
import android.os.Looper;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import android.os.Handler;
import java.util.concurrent.ThreadPoolExecutor;
import android.content.Context;

public class BLENameFetcher
{
    public static final int MAX_CONCURRENT_CONNECTIONS = 2;
    private static final String TAG;
    Context mContext;
    ThreadPoolExecutor mExecutor;
    Handler mUIHandler;
    
    static {
        TAG = BLENameFetcher.class.getSimpleName();
    }
    
    public BLENameFetcher() {
        this.mExecutor = new ThreadPoolExecutor(2, 2, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        this.mUIHandler = new Handler(Looper.getMainLooper());
    }
    
    public void fetchName(final MAC obj, final int n, final OnSuccessListener onSuccessListener) {
        Log.d(BLENameFetcher.TAG, "Add to queue. Address " + obj);
        this.mExecutor.submit(new NameFetch(obj, n, onSuccessListener));
    }
    
    public void start(@NonNull final Context mContext) {
        this.mContext = mContext;
    }
    
    public void stop() {
        while (!this.mExecutor.getQueue().isEmpty()) {
            this.mExecutor.remove(this.mExecutor.getQueue().peek());
        }
    }
    
    public class NameFetch implements Runnable
    {
        MAC mAddress;
        OnSuccessListener mListener;
        int mRevision;
        
        public NameFetch(final MAC mAddress, final int mRevision, final OnSuccessListener mListener) {
            this.mAddress = mAddress;
            this.mListener = mListener;
            this.mRevision = mRevision;
        }
        
        @Override
        public void run() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: new             Ljava/lang/StringBuilder;
            //     6: dup            
            //     7: invokespecial   java/lang/StringBuilder.<init>:()V
            //    10: ldc             "Begin name fetching. Address: "
            //    12: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    15: aload_0        
            //    16: getfield        com/logitech/ue/other/BLENameFetcher$NameFetch.mAddress:Lcom/logitech/ue/centurion/utils/MAC;
            //    19: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
            //    22: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //    25: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
            //    28: pop            
            //    29: aconst_null    
            //    30: astore_1       
            //    31: aconst_null    
            //    32: astore_2       
            //    33: aconst_null    
            //    34: astore_3       
            //    35: aload_2        
            //    36: astore          4
            //    38: invokestatic    com/logitech/ue/Utils.isCoarseLocationPermissionGranted:()Z
            //    41: ifeq            170
            //    44: aload_2        
            //    45: astore          4
            //    47: new             Lcom/logitech/ue/centurion/connection/UEBLEConnector;
            //    50: astore_3       
            //    51: aload_2        
            //    52: astore          4
            //    54: aload_3        
            //    55: aload_0        
            //    56: getfield        com/logitech/ue/other/BLENameFetcher$NameFetch.mAddress:Lcom/logitech/ue/centurion/utils/MAC;
            //    59: aload_0        
            //    60: getfield        com/logitech/ue/other/BLENameFetcher$NameFetch.this$0:Lcom/logitech/ue/other/BLENameFetcher;
            //    63: getfield        com/logitech/ue/other/BLENameFetcher.mContext:Landroid/content/Context;
            //    66: invokespecial   com/logitech/ue/centurion/connection/UEBLEConnector.<init>:(Lcom/logitech/ue/centurion/utils/MAC;Landroid/content/Context;)V
            //    69: aload_3        
            //    70: invokevirtual   com/logitech/ue/centurion/connection/UEBLEConnector.connectToDevice:()V
            //    73: new             Lcom/logitech/ue/centurion/device/UEBLEDevice;
            //    76: astore          4
            //    78: aload           4
            //    80: aload_0        
            //    81: getfield        com/logitech/ue/other/BLENameFetcher$NameFetch.mAddress:Lcom/logitech/ue/centurion/utils/MAC;
            //    84: aload_3        
            //    85: invokespecial   com/logitech/ue/centurion/device/UEBLEDevice.<init>:(Lcom/logitech/ue/centurion/utils/MAC;Lcom/logitech/ue/centurion/connection/IUEConnector;)V
            //    88: aload           4
            //    90: invokevirtual   com/logitech/ue/centurion/device/UEBLEDevice.getBluetoothName:()Ljava/lang/String;
            //    93: astore          4
            //    95: invokestatic    com/logitech/ue/other/BLENameFetcher.access$000:()Ljava/lang/String;
            //    98: astore_1       
            //    99: new             Ljava/lang/StringBuilder;
            //   102: astore_2       
            //   103: aload_2        
            //   104: invokespecial   java/lang/StringBuilder.<init>:()V
            //   107: aload_1        
            //   108: aload_2        
            //   109: ldc             "Success. Name: "
            //   111: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   114: aload           4
            //   116: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   119: ldc             " Address: "
            //   121: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   124: aload_0        
            //   125: getfield        com/logitech/ue/other/BLENameFetcher$NameFetch.mAddress:Lcom/logitech/ue/centurion/utils/MAC;
            //   128: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
            //   131: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   134: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
            //   137: pop            
            //   138: aload_0        
            //   139: getfield        com/logitech/ue/other/BLENameFetcher$NameFetch.mListener:Lcom/logitech/ue/other/BLENameFetcher$OnSuccessListener;
            //   142: ifnull          170
            //   145: aload_0        
            //   146: getfield        com/logitech/ue/other/BLENameFetcher$NameFetch.this$0:Lcom/logitech/ue/other/BLENameFetcher;
            //   149: getfield        com/logitech/ue/other/BLENameFetcher.mUIHandler:Landroid/os/Handler;
            //   152: astore_1       
            //   153: new             Lcom/logitech/ue/other/BLENameFetcher$NameFetch$1;
            //   156: astore_2       
            //   157: aload_2        
            //   158: aload_0        
            //   159: aload           4
            //   161: invokespecial   com/logitech/ue/other/BLENameFetcher$NameFetch$1.<init>:(Lcom/logitech/ue/other/BLENameFetcher$NameFetch;Ljava/lang/String;)V
            //   164: aload_1        
            //   165: aload_2        
            //   166: invokevirtual   android/os/Handler.post:(Ljava/lang/Runnable;)Z
            //   169: pop            
            //   170: aload_3        
            //   171: ifnull          178
            //   174: aload_3        
            //   175: invokevirtual   com/logitech/ue/centurion/connection/UEBLEConnector.disconnectFromDevice:()V
            //   178: return         
            //   179: astore_2       
            //   180: aload_1        
            //   181: astore_3       
            //   182: aload_3        
            //   183: astore          4
            //   185: invokestatic    com/logitech/ue/other/BLENameFetcher.access$000:()Ljava/lang/String;
            //   188: ldc             "Fail. Address: %s Exception: %s"
            //   190: iconst_2       
            //   191: anewarray       Ljava/lang/Object;
            //   194: dup            
            //   195: iconst_0       
            //   196: aload_0        
            //   197: getfield        com/logitech/ue/other/BLENameFetcher$NameFetch.mAddress:Lcom/logitech/ue/centurion/utils/MAC;
            //   200: aastore        
            //   201: dup            
            //   202: iconst_1       
            //   203: aload_2        
            //   204: invokevirtual   java/lang/Exception.getMessage:()Ljava/lang/String;
            //   207: aastore        
            //   208: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
            //   211: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
            //   214: pop            
            //   215: aload_3        
            //   216: astore          4
            //   218: aload_0        
            //   219: getfield        com/logitech/ue/other/BLENameFetcher$NameFetch.mListener:Lcom/logitech/ue/other/BLENameFetcher$OnSuccessListener;
            //   222: ifnull          263
            //   225: aload_3        
            //   226: astore          4
            //   228: aload_0        
            //   229: getfield        com/logitech/ue/other/BLENameFetcher$NameFetch.this$0:Lcom/logitech/ue/other/BLENameFetcher;
            //   232: getfield        com/logitech/ue/other/BLENameFetcher.mUIHandler:Landroid/os/Handler;
            //   235: astore          5
            //   237: aload_3        
            //   238: astore          4
            //   240: new             Lcom/logitech/ue/other/BLENameFetcher$NameFetch$2;
            //   243: astore_1       
            //   244: aload_3        
            //   245: astore          4
            //   247: aload_1        
            //   248: aload_0        
            //   249: aload_2        
            //   250: invokespecial   com/logitech/ue/other/BLENameFetcher$NameFetch$2.<init>:(Lcom/logitech/ue/other/BLENameFetcher$NameFetch;Ljava/lang/Exception;)V
            //   253: aload_3        
            //   254: astore          4
            //   256: aload           5
            //   258: aload_1        
            //   259: invokevirtual   android/os/Handler.post:(Ljava/lang/Runnable;)Z
            //   262: pop            
            //   263: aload_3        
            //   264: ifnull          178
            //   267: aload_3        
            //   268: invokevirtual   com/logitech/ue/centurion/connection/UEBLEConnector.disconnectFromDevice:()V
            //   271: goto            178
            //   274: astore_3       
            //   275: goto            178
            //   278: astore_3       
            //   279: aload           4
            //   281: astore_2       
            //   282: aload_2        
            //   283: ifnull          290
            //   286: aload_2        
            //   287: invokevirtual   com/logitech/ue/centurion/connection/UEBLEConnector.disconnectFromDevice:()V
            //   290: aload_3        
            //   291: athrow         
            //   292: astore_3       
            //   293: goto            178
            //   296: astore          4
            //   298: goto            290
            //   301: astore          4
            //   303: aload_3        
            //   304: astore_2       
            //   305: aload           4
            //   307: astore_3       
            //   308: goto            282
            //   311: astore_2       
            //   312: goto            182
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  38     44     179    182    Ljava/lang/Exception;
            //  38     44     278    282    Any
            //  47     51     179    182    Ljava/lang/Exception;
            //  47     51     278    282    Any
            //  54     69     179    182    Ljava/lang/Exception;
            //  54     69     278    282    Any
            //  69     170    311    315    Ljava/lang/Exception;
            //  69     170    301    311    Any
            //  174    178    292    296    Ljava/lang/Exception;
            //  185    215    278    282    Any
            //  218    225    278    282    Any
            //  228    237    278    282    Any
            //  240    244    278    282    Any
            //  247    253    278    282    Any
            //  256    263    278    282    Any
            //  267    271    274    278    Ljava/lang/Exception;
            //  286    290    296    301    Ljava/lang/Exception;
            // 
            // The error that occurred was:
            // 
            // java.util.ConcurrentModificationException
            //     at java.base/java.util.ArrayList$Itr.checkForComodification(ArrayList.java:1042)
            //     at java.base/java.util.ArrayList$Itr.next(ArrayList.java:996)
            //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2863)
            //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
    }
    
    public interface OnSuccessListener
    {
        void onError(final MAC p0, final int p1, final Exception p2);
        
        void onSuccess(final MAC p0, final int p1, final String p2);
    }
}
