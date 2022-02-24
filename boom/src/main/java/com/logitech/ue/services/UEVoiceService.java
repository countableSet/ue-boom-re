// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.services;

import android.os.Binder;
import android.os.AsyncTask;
import com.logitech.ue.tasks.InitManagerTask;
import com.logitech.ue.centurion.UEDeviceManager;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import com.logitech.ue.activities.LightScreenUpActivity;
import com.logitech.ue.App;
import android.os.Build$VERSION;
import android.os.PowerManager;
import android.content.ActivityNotFoundException;
import com.logitech.ue.FlurryTracker;
import android.util.Log;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.content.BroadcastReceiver;
import android.os.IBinder;
import android.media.AudioManager;
import android.app.Service;

public class UEVoiceService extends Service
{
    private static final String TAG;
    public static final int VOICE_MODE_TIMEOUT = 15000;
    private static final int VOICE_NOTIFICATION = 0;
    private static final int VOICE_STARTING_TONE_TIMEOUT = 4000;
    private static final int VOICE_STARTS = 1;
    private long flurryEndingToneTime;
    private long flurryStartingToneTime;
    private long flurryTriggerTime;
    private boolean flurryWasMusicPlaying;
    private AudioManager mAudioManager;
    private final IBinder mBinder;
    final BroadcastReceiver mBluetoothScoReceiver;
    private volatile boolean mIfWaitingStartingTone;
    private Handler mStartingToneTimeoutHandler;
    private Handler mTimeoutHandler;
    private boolean mUserClicked;
    final BroadcastReceiver mVoiceBroadcastReceiver;
    
    static {
        TAG = UEVoiceService.class.getSimpleName();
    }
    
    public UEVoiceService() {
        this.mBinder = (IBinder)new VoiceServiceBinder();
        this.mUserClicked = false;
        this.flurryWasMusicPlaying = false;
        this.flurryTriggerTime = -1L;
        this.flurryStartingToneTime = -1L;
        this.flurryEndingToneTime = -1L;
        this.mTimeoutHandler = new Handler();
        this.mStartingToneTimeoutHandler = new Handler();
        this.mIfWaitingStartingTone = true;
        this.mVoiceBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context p0, final Intent p1) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: ldc             "status"
                //     3: iconst_0       
                //     4: invokevirtual   android/content/Intent.getIntExtra:(Ljava/lang/String;I)I
                //     7: istore_3       
                //     8: aload_2        
                //     9: ldc             "notification"
                //    11: iconst_0       
                //    12: invokevirtual   android/content/Intent.getIntExtra:(Ljava/lang/String;I)I
                //    15: istore          4
                //    17: invokestatic    com/logitech/ue/centurion/UEDeviceManager.getInstance:()Lcom/logitech/ue/centurion/UEDeviceManager;
                //    20: invokevirtual   com/logitech/ue/centurion/UEDeviceManager.getConnectedDevice:()Lcom/logitech/ue/centurion/device/UEGenericDevice;
                //    23: astore_1       
                //    24: invokestatic    com/logitech/ue/services/UEVoiceService.access$000:()Ljava/lang/String;
                //    27: new             Ljava/lang/StringBuilder;
                //    30: dup            
                //    31: invokespecial   java/lang/StringBuilder.<init>:()V
                //    34: ldc             "voice event received state: "
                //    36: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
                //    39: iload_3        
                //    40: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
                //    43: ldc             " cue: "
                //    45: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
                //    48: iload           4
                //    50: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
                //    53: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
                //    56: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;)I
                //    59: pop            
                //    60: iload_3        
                //    61: iconst_1       
                //    62: if_icmpne       308
                //    65: invokestatic    com/logitech/ue/services/UEVoiceService.access$000:()Ljava/lang/String;
                //    68: new             Ljava/lang/StringBuilder;
                //    71: dup            
                //    72: invokespecial   java/lang/StringBuilder.<init>:()V
                //    75: ldc             "New voice input starts at "
                //    77: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
                //    80: aload_0        
                //    81: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //    84: invokestatic    java/lang/System.currentTimeMillis:()J
                //    87: invokestatic    com/logitech/ue/services/UEVoiceService.access$102:(Lcom/logitech/ue/services/UEVoiceService;J)J
                //    90: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
                //    93: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
                //    96: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;)I
                //    99: pop            
                //   100: aload_0        
                //   101: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   104: iconst_1       
                //   105: invokestatic    com/logitech/ue/services/UEVoiceService.access$202:(Lcom/logitech/ue/services/UEVoiceService;Z)Z
                //   108: pop            
                //   109: aload_0        
                //   110: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   113: ldc2_w          -1
                //   116: invokestatic    com/logitech/ue/services/UEVoiceService.access$302:(Lcom/logitech/ue/services/UEVoiceService;J)J
                //   119: pop2           
                //   120: aload_0        
                //   121: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   124: ldc2_w          -1
                //   127: invokestatic    com/logitech/ue/services/UEVoiceService.access$402:(Lcom/logitech/ue/services/UEVoiceService;J)J
                //   130: pop2           
                //   131: aload_1        
                //   132: ifnonnull       145
                //   135: invokestatic    com/logitech/ue/services/UEVoiceService.access$000:()Ljava/lang/String;
                //   138: ldc             "Device is NULL"
                //   140: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
                //   143: pop            
                //   144: return         
                //   145: new             Lcom/logitech/ue/services/UEVoiceService$1$1;
                //   148: dup            
                //   149: aload_0        
                //   150: invokespecial   com/logitech/ue/services/UEVoiceService$1$1.<init>:(Lcom/logitech/ue/services/UEVoiceService$1;)V
                //   153: astore_2       
                //   154: aload_0        
                //   155: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   158: invokestatic    com/logitech/ue/services/UEVoiceService.access$600:(Lcom/logitech/ue/services/UEVoiceService;)Landroid/os/Handler;
                //   161: aload_2        
                //   162: ldc2_w          4000
                //   165: invokevirtual   android/os/Handler.postDelayed:(Ljava/lang/Runnable;J)Z
                //   168: pop            
                //   169: invokestatic    com/logitech/ue/services/UEVoiceService.access$000:()Ljava/lang/String;
                //   172: ldc             "Make speaker blinking with tone OFF"
                //   174: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                //   177: pop            
                //   178: aload_1        
                //   179: iconst_1       
                //   180: iconst_0       
                //   181: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.setVoiceLEDAndTone:(BB)V
                //   184: aload_0        
                //   185: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   188: invokestatic    com/logitech/ue/services/UEVoiceService.access$700:(Lcom/logitech/ue/services/UEVoiceService;)Landroid/media/AudioManager;
                //   191: invokevirtual   android/media/AudioManager.isMusicActive:()Z
                //   194: ifeq            296
                //   197: aload_0        
                //   198: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   201: iconst_1       
                //   202: invokestatic    com/logitech/ue/services/UEVoiceService.access$802:(Lcom/logitech/ue/services/UEVoiceService;Z)Z
                //   205: pop            
                //   206: aload_1        
                //   207: getstatic       com/logitech/ue/centurion/device/devicedata/UEAVRCP.STOP:Lcom/logitech/ue/centurion/device/devicedata/UEAVRCP;
                //   210: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.sendAVRCPCommand:(Lcom/logitech/ue/centurion/device/devicedata/UEAVRCP;)V
                //   213: invokestatic    com/logitech/ue/services/UEVoiceService.access$000:()Ljava/lang/String;
                //   216: ldc             "Pause music"
                //   218: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                //   221: pop            
                //   222: aload_0        
                //   223: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   226: invokestatic    com/logitech/ue/services/UEVoiceService.access$900:(Lcom/logitech/ue/services/UEVoiceService;)V
                //   229: goto            144
                //   232: astore_2       
                //   233: aload_2        
                //   234: invokevirtual   com/logitech/ue/centurion/exceptions/UEErrorResultException.printStackTrace:()V
                //   237: goto            184
                //   240: astore_2       
                //   241: aload_2        
                //   242: invokevirtual   com/logitech/ue/centurion/exceptions/UEConnectionException.printStackTrace:()V
                //   245: goto            184
                //   248: astore_2       
                //   249: aload_2        
                //   250: invokevirtual   com/logitech/ue/centurion/exceptions/UEUnrecognisedCommandException.printStackTrace:()V
                //   253: goto            184
                //   256: astore_2       
                //   257: aload_2        
                //   258: invokevirtual   com/logitech/ue/centurion/exceptions/UEOperationException.printStackTrace:()V
                //   261: goto            184
                //   264: astore_1       
                //   265: aload_1        
                //   266: invokevirtual   com/logitech/ue/centurion/exceptions/UEUnrecognisedCommandException.printStackTrace:()V
                //   269: goto            213
                //   272: astore_1       
                //   273: aload_1        
                //   274: invokevirtual   com/logitech/ue/centurion/exceptions/UEConnectionException.printStackTrace:()V
                //   277: goto            213
                //   280: astore_1       
                //   281: aload_1        
                //   282: invokevirtual   com/logitech/ue/centurion/exceptions/UEErrorResultException.printStackTrace:()V
                //   285: goto            213
                //   288: astore_1       
                //   289: aload_1        
                //   290: invokevirtual   com/logitech/ue/centurion/exceptions/UEOperationException.printStackTrace:()V
                //   293: goto            213
                //   296: aload_0        
                //   297: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   300: iconst_0       
                //   301: invokestatic    com/logitech/ue/services/UEVoiceService.access$802:(Lcom/logitech/ue/services/UEVoiceService;Z)Z
                //   304: pop            
                //   305: goto            222
                //   308: iload_3        
                //   309: ifne            144
                //   312: iload           4
                //   314: ifeq            144
                //   317: aload_0        
                //   318: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   321: invokestatic    com/logitech/ue/services/UEVoiceService.access$500:(Lcom/logitech/ue/services/UEVoiceService;)Z
                //   324: ifeq            394
                //   327: invokestatic    com/logitech/ue/services/UEVoiceService.access$000:()Ljava/lang/String;
                //   330: new             Ljava/lang/StringBuilder;
                //   333: dup            
                //   334: invokespecial   java/lang/StringBuilder.<init>:()V
                //   337: ldc             "Starting tone detected at "
                //   339: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
                //   342: aload_0        
                //   343: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   346: invokestatic    java/lang/System.currentTimeMillis:()J
                //   349: invokestatic    com/logitech/ue/services/UEVoiceService.access$302:(Lcom/logitech/ue/services/UEVoiceService;J)J
                //   352: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
                //   355: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
                //   358: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;)I
                //   361: pop            
                //   362: aload_0        
                //   363: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   366: iconst_0       
                //   367: invokestatic    com/logitech/ue/services/UEVoiceService.access$502:(Lcom/logitech/ue/services/UEVoiceService;Z)Z
                //   370: pop            
                //   371: aload_0        
                //   372: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   375: invokestatic    com/logitech/ue/services/UEVoiceService.access$600:(Lcom/logitech/ue/services/UEVoiceService;)Landroid/os/Handler;
                //   378: aconst_null    
                //   379: invokevirtual   android/os/Handler.removeCallbacksAndMessages:(Ljava/lang/Object;)V
                //   382: invokestatic    com/logitech/ue/services/UEVoiceService.access$000:()Ljava/lang/String;
                //   385: ldc             "Cancelling registered starting tone timeout event."
                //   387: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                //   390: pop            
                //   391: goto            144
                //   394: invokestatic    com/logitech/ue/services/UEVoiceService.access$000:()Ljava/lang/String;
                //   397: new             Ljava/lang/StringBuilder;
                //   400: dup            
                //   401: invokespecial   java/lang/StringBuilder.<init>:()V
                //   404: ldc             "Ending tone detected at "
                //   406: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
                //   409: aload_0        
                //   410: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   413: invokestatic    java/lang/System.currentTimeMillis:()J
                //   416: invokestatic    com/logitech/ue/services/UEVoiceService.access$402:(Lcom/logitech/ue/services/UEVoiceService;J)J
                //   419: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
                //   422: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
                //   425: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;)I
                //   428: pop            
                //   429: aload_0        
                //   430: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   433: iconst_1       
                //   434: invokestatic    com/logitech/ue/services/UEVoiceService.access$502:(Lcom/logitech/ue/services/UEVoiceService;Z)Z
                //   437: pop            
                //   438: aload_0        
                //   439: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   442: invokestatic    com/logitech/ue/services/UEVoiceService.access$1000:(Lcom/logitech/ue/services/UEVoiceService;)Landroid/os/Handler;
                //   445: aconst_null    
                //   446: invokevirtual   android/os/Handler.removeCallbacksAndMessages:(Ljava/lang/Object;)V
                //   449: invokestatic    com/logitech/ue/services/UEVoiceService.access$000:()Ljava/lang/String;
                //   452: ldc             "Cancelling registered stopping SCO event."
                //   454: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
                //   457: pop            
                //   458: aload_0        
                //   459: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   462: invokevirtual   com/logitech/ue/services/UEVoiceService.stopBluetoothSco:()V
                //   465: aload_0        
                //   466: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   469: invokestatic    com/logitech/ue/services/UEVoiceService.access$800:(Lcom/logitech/ue/services/UEVoiceService;)Z
                //   472: aload_0        
                //   473: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   476: invokestatic    com/logitech/ue/services/UEVoiceService.access$100:(Lcom/logitech/ue/services/UEVoiceService;)J
                //   479: aload_0        
                //   480: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   483: invokestatic    com/logitech/ue/services/UEVoiceService.access$300:(Lcom/logitech/ue/services/UEVoiceService;)J
                //   486: aload_0        
                //   487: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   490: invokestatic    com/logitech/ue/services/UEVoiceService.access$400:(Lcom/logitech/ue/services/UEVoiceService;)J
                //   493: iconst_0       
                //   494: invokestatic    com/logitech/ue/FlurryTracker.logVoiceSession:(ZJJJZ)V
                //   497: aload_0        
                //   498: getfield        com/logitech/ue/services/UEVoiceService$1.this$0:Lcom/logitech/ue/services/UEVoiceService;
                //   501: invokevirtual   com/logitech/ue/services/UEVoiceService.lightUpVoicePopup:()V
                //   504: goto            144
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                                                                 
                //  -----  -----  -----  -----  ---------------------------------------------------------------------
                //  169    184    232    240    Lcom/logitech/ue/centurion/exceptions/UEErrorResultException;
                //  169    184    240    248    Lcom/logitech/ue/centurion/exceptions/UEConnectionException;
                //  169    184    248    256    Lcom/logitech/ue/centurion/exceptions/UEUnrecognisedCommandException;
                //  169    184    256    264    Lcom/logitech/ue/centurion/exceptions/UEOperationException;
                //  206    213    264    272    Lcom/logitech/ue/centurion/exceptions/UEUnrecognisedCommandException;
                //  206    213    272    280    Lcom/logitech/ue/centurion/exceptions/UEConnectionException;
                //  206    213    280    288    Lcom/logitech/ue/centurion/exceptions/UEErrorResultException;
                //  206    213    288    296    Lcom/logitech/ue/centurion/exceptions/UEOperationException;
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Expression is linked from several locations: Label_0213:
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
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
        };
        this.mBluetoothScoReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final int intExtra = intent.getIntExtra("android.media.extra.SCO_AUDIO_STATE", -1);
                if (intExtra != 0) {
                    Log.d(UEVoiceService.TAG, "Bluetooth SCO established");
                    if (!UEVoiceService.this.mUserClicked) {
                        Log.w(UEVoiceService.TAG, "NOT USER TRIGGERED. DO NOTHING.");
                    }
                    else {
                        UEVoiceService.this.handleVoiceInput();
                    }
                }
                else if (intExtra == 0) {
                    Log.d(UEVoiceService.TAG, "Bluetooth SCO disconnected");
                }
            }
        };
    }
    
    private void handleVoiceInput() {
        Log.i(UEVoiceService.TAG, "handling voice input");
        this.launchGoogleNow();
        this.mTimeoutHandler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                UEVoiceService.this.stopBluetoothSco();
                UEVoiceService.this.mIfWaitingStartingTone = true;
                Log.d(UEVoiceService.TAG, "End voice session after 15s timeout");
                FlurryTracker.logVoiceSession(UEVoiceService.this.flurryWasMusicPlaying, UEVoiceService.this.flurryTriggerTime, UEVoiceService.this.flurryStartingToneTime, UEVoiceService.this.flurryEndingToneTime, true);
            }
        }, 15000L);
        this.mUserClicked = false;
    }
    
    private void launchGoogleNow() {
        Log.i(UEVoiceService.TAG, "launching google now");
        final Intent intent = new Intent("android.intent.action.VOICE_COMMAND");
        intent.addFlags(32768);
        intent.addFlags(268435456);
        try {
            this.startActivity(intent);
        }
        catch (ActivityNotFoundException ex) {
            Log.w(UEVoiceService.TAG, "Intended voice service NOT found");
        }
    }
    
    private void startVoiceInputMode() {
        Log.i(UEVoiceService.TAG, "starting voice input mode");
        if (!this.mAudioManager.isBluetoothScoOn()) {
            this.startBluetoothSco();
        }
        else {
            Log.d(UEVoiceService.TAG, "Bluetooth SCO mode is ready ON");
            this.handleVoiceInput();
        }
    }
    
    public void lightUpVoicePopup() {
        final PowerManager powerManager = (PowerManager)this.getSystemService("power");
        int n;
        if ((Build$VERSION.SDK_INT >= 20 && powerManager.isInteractive()) || (Build$VERSION.SDK_INT < 20 && powerManager.isScreenOn())) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (n != 0) {
            Log.d(UEVoiceService.TAG, "Screen is already on. Do nothing.");
        }
        else if (App.getInstance().getCurrentActivity() == null || !App.getInstance().getCurrentActivity().getClass().equals(LightScreenUpActivity.class)) {
            final Intent intent = new Intent((Context)this, (Class)LightScreenUpActivity.class);
            intent.addFlags(32768);
            intent.addFlags(268435456);
            this.startActivity(intent);
        }
    }
    
    @Nullable
    public IBinder onBind(final Intent intent) {
        Log.i(UEVoiceService.TAG, "onBind receives " + intent.getAction());
        return this.mBinder;
    }
    
    public void onCreate() {
        super.onCreate();
        this.mAudioManager = (AudioManager)this.getSystemService("audio");
        this.registerReceiver(this.mVoiceBroadcastReceiver, new IntentFilter("com.logitech.ue.centurion.ACTION_VOICE_REQUEST_NOTIFICATION"));
        this.registerReceiver(this.mBluetoothScoReceiver, new IntentFilter("android.media.ACTION_SCO_AUDIO_STATE_UPDATED"));
    }
    
    public void onDestroy() {
        this.stopBluetoothSco();
        Log.d(UEVoiceService.TAG, "Stop Bluetooth SCO onDestroy()");
        super.onDestroy();
    }
    
    public int onStartCommand(final Intent intent, final int n, final int n2) {
        Log.i(UEVoiceService.TAG, "Voice service launched");
        if (!UEDeviceManager.getInstance().isReady()) {
            Log.i(UEVoiceService.TAG, "device manager is not initialized");
            new InitManagerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
        }
        return 1;
    }
    
    public void startBluetoothSco() {
        if (!this.mAudioManager.isBluetoothScoOn()) {
            this.mAudioManager.setBluetoothScoOn(true);
            this.mAudioManager.startBluetoothSco();
            this.mAudioManager.setMode(3);
        }
        else {
            Log.d(UEVoiceService.TAG, "Do nothing. Bluetooth SCO mode is ready ON");
        }
    }
    
    public void stopBluetoothSco() {
        if (this.mAudioManager.isBluetoothScoOn()) {
            this.mAudioManager.setBluetoothScoOn(false);
            this.mAudioManager.stopBluetoothSco();
        }
        else {
            Log.d(UEVoiceService.TAG, "Do nothing. Bluetooth SCO mode is ready OFF");
        }
        this.mAudioManager.setMode(0);
    }
    
    public class VoiceServiceBinder extends Binder
    {
        public UEVoiceService getService() {
            return UEVoiceService.this;
        }
    }
}
