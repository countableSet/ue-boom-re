// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import android.os.AsyncTask;

public class DeviceFirstTimeConnectTask extends AsyncTask<Void, Void, Void>
{
    public static final String TAG;
    public static boolean WasGestureEnabled;
    
    static {
        TAG = DeviceFirstTimeConnectTask.class.getSimpleName();
        DeviceFirstTimeConnectTask.WasGestureEnabled = false;
    }
    
    protected Void doInBackground(final Void... p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: invokevirtual   com/logitech/ue/centurion/UEDeviceManager.getConnectedDevice:()Lcom/logitech/ue/centurion/device/UEGenericDevice;
        //     6: astore_1       
        //     7: aload_1        
        //     8: ifnonnull       13
        //    11: aconst_null    
        //    12: areturn        
        //    13: invokestatic    java/util/Locale.getDefault:()Ljava/util/Locale;
        //    16: invokevirtual   java/util/Locale.getLanguage:()Ljava/lang/String;
        //    19: astore_2       
        //    20: aload_2        
        //    21: invokestatic    com/logitech/ue/centurion/device/devicedata/UELanguage.getLanguage:(Ljava/lang/String;)Lcom/logitech/ue/centurion/device/devicedata/UELanguage;
        //    24: astore_3       
        //    25: aload_3        
        //    26: ifnull          128
        //    29: getstatic       com/logitech/ue/tasks/DeviceFirstTimeConnectTask.TAG:Ljava/lang/String;
        //    32: new             Ljava/lang/StringBuilder;
        //    35: dup            
        //    36: invokespecial   java/lang/StringBuilder.<init>:()V
        //    39: ldc             "Set Kora language to app language "
        //    41: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    44: aload_2        
        //    45: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    48: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    51: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    54: pop            
        //    55: aload_1        
        //    56: aload_3        
        //    57: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.setLanguage:(Lcom/logitech/ue/centurion/device/devicedata/UELanguage;)V
        //    60: aload_1        
        //    61: iconst_1       
        //    62: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.setBLEState:(Z)V
        //    65: aload_1        
        //    66: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.getGestureState:()Z
        //    69: ifne            81
        //    72: aload_1        
        //    73: iconst_1       
        //    74: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.setGestureState:(Z)V
        //    77: iconst_1       
        //    78: putstatic       com/logitech/ue/tasks/DeviceFirstTimeConnectTask.WasGestureEnabled:Z
        //    81: aload_1        
        //    82: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.getGestureState:()Z
        //    85: ifne            11
        //    88: aload_1        
        //    89: iconst_1       
        //    90: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.setGestureState:(Z)V
        //    93: iconst_1       
        //    94: putstatic       com/logitech/ue/tasks/DeviceFirstTimeConnectTask.WasGestureEnabled:Z
        //    97: goto            11
        //   100: astore_1       
        //   101: getstatic       com/logitech/ue/tasks/DeviceFirstTimeConnectTask.TAG:Ljava/lang/String;
        //   104: ldc             "Device doesn't support x-up"
        //   106: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   109: pop            
        //   110: goto            11
        //   113: astore_3       
        //   114: getstatic       com/logitech/ue/tasks/DeviceFirstTimeConnectTask.TAG:Ljava/lang/String;
        //   117: aload_3        
        //   118: invokevirtual   java/lang/Exception.getMessage:()Ljava/lang/String;
        //   121: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   124: pop            
        //   125: goto            60
        //   128: getstatic       com/logitech/ue/tasks/DeviceFirstTimeConnectTask.TAG:Ljava/lang/String;
        //   131: new             Ljava/lang/StringBuilder;
        //   134: dup            
        //   135: invokespecial   java/lang/StringBuilder.<init>:()V
        //   138: ldc             "Don't change Kora language because "
        //   140: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   143: aload_2        
        //   144: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   147: ldc             " language is not unsupported"
        //   149: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   152: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   155: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   158: pop            
        //   159: goto            60
        //   162: astore_3       
        //   163: getstatic       com/logitech/ue/tasks/DeviceFirstTimeConnectTask.TAG:Ljava/lang/String;
        //   166: ldc             "Device doesn't support ble"
        //   168: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   171: pop            
        //   172: goto            65
        //   175: astore_3       
        //   176: getstatic       com/logitech/ue/tasks/DeviceFirstTimeConnectTask.TAG:Ljava/lang/String;
        //   179: ldc             "Device doesn't support gesture"
        //   181: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   184: pop            
        //   185: goto            81
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                              
        //  -----  -----  -----  -----  --------------------------------------------------
        //  55     60     113    128    Ljava/lang/Exception;
        //  60     65     162    175    Lcom/logitech/ue/centurion/exceptions/UEException;
        //  65     81     175    188    Lcom/logitech/ue/centurion/exceptions/UEException;
        //  81     97     100    113    Lcom/logitech/ue/centurion/exceptions/UEException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 88 out of bounds for length 88
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:458)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
}
