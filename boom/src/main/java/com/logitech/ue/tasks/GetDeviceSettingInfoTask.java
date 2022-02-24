// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import java.util.concurrent.CancellationException;
import com.logitech.ue.devicedata.DeviceSettingsInfo;

public class GetDeviceSettingInfoTask extends GetDeviceDataTask<DeviceSettingsInfo>
{
    private static final String TAG;
    
    static {
        TAG = GetDeviceSettingInfoTask.class.getSimpleName();
    }
    
    private void checkCancellation() {
        if (this.isCancelled()) {
            throw new CancellationException();
        }
    }
    
    @Override
    public String getTag() {
        return GetDeviceSettingInfoTask.TAG;
    }
    
    @Override
    public DeviceSettingsInfo work(final Void... p0) throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: invokevirtual   com/logitech/ue/centurion/UEDeviceManager.getConnectedDevice:()Lcom/logitech/ue/centurion/device/UEGenericDevice;
        //     6: astore_1       
        //     7: new             Lcom/logitech/ue/devicedata/DeviceSettingsInfo;
        //    10: dup            
        //    11: invokespecial   com/logitech/ue/devicedata/DeviceSettingsInfo.<init>:()V
        //    14: astore_2       
        //    15: aload_2        
        //    16: aload_1        
        //    17: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.getDeviceColor:()I
        //    20: putfield        com/logitech/ue/devicedata/DeviceSettingsInfo.color:I
        //    23: aload_0        
        //    24: invokespecial   com/logitech/ue/tasks/GetDeviceSettingInfoTask.checkCancellation:()V
        //    27: aload_2        
        //    28: aload_1        
        //    29: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.getBluetoothName:()Ljava/lang/String;
        //    32: putfield        com/logitech/ue/devicedata/DeviceSettingsInfo.name:Ljava/lang/String;
        //    35: aload_0        
        //    36: invokespecial   com/logitech/ue/tasks/GetDeviceSettingInfoTask.checkCancellation:()V
        //    39: aload_2        
        //    40: aload_1        
        //    41: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.getSonificationProfile:()Lcom/logitech/ue/centurion/device/devicedata/UESonificationProfile;
        //    44: putfield        com/logitech/ue/devicedata/DeviceSettingsInfo.sonificationProfile:Lcom/logitech/ue/centurion/device/devicedata/UESonificationProfile;
        //    47: aload_0        
        //    48: invokespecial   com/logitech/ue/tasks/GetDeviceSettingInfoTask.checkCancellation:()V
        //    51: aload_2        
        //    52: aload_1        
        //    53: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.getCustomState:()Z
        //    56: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //    59: putfield        com/logitech/ue/devicedata/DeviceSettingsInfo.customSonification:Ljava/lang/Boolean;
        //    62: aload_0        
        //    63: invokespecial   com/logitech/ue/tasks/GetDeviceSettingInfoTask.checkCancellation:()V
        //    66: aload_2        
        //    67: aload_1        
        //    68: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.getEQSetting:()Lcom/logitech/ue/centurion/device/devicedata/UEEQSetting;
        //    71: putfield        com/logitech/ue/devicedata/DeviceSettingsInfo.EQSettings:Lcom/logitech/ue/centurion/device/devicedata/UEEQSetting;
        //    74: aload_0        
        //    75: invokespecial   com/logitech/ue/tasks/GetDeviceSettingInfoTask.checkCancellation:()V
        //    78: aload_2        
        //    79: aload_1        
        //    80: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.getLanguage:()Lcom/logitech/ue/centurion/device/devicedata/UELanguage;
        //    83: putfield        com/logitech/ue/devicedata/DeviceSettingsInfo.language:Lcom/logitech/ue/centurion/device/devicedata/UELanguage;
        //    86: aload_0        
        //    87: invokespecial   com/logitech/ue/tasks/GetDeviceSettingInfoTask.checkCancellation:()V
        //    90: aload_2        
        //    91: aload_1        
        //    92: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.getSupportedLanguageIndex:()Ljava/util/ArrayList;
        //    95: putfield        com/logitech/ue/devicedata/DeviceSettingsInfo.supportedLanguages:Ljava/util/ArrayList;
        //    98: aload_0        
        //    99: invokespecial   com/logitech/ue/tasks/GetDeviceSettingInfoTask.checkCancellation:()V
        //   102: aload_2        
        //   103: aload_1        
        //   104: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.getStickyTWSOrPartyUpFlag:()Z
        //   107: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   110: putfield        com/logitech/ue/devicedata/DeviceSettingsInfo.twsLockFlag:Ljava/lang/Boolean;
        //   113: aload_0        
        //   114: invokespecial   com/logitech/ue/tasks/GetDeviceSettingInfoTask.checkCancellation:()V
        //   117: aload_2        
        //   118: aload_1        
        //   119: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.getBLEState:()Z
        //   122: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   125: putfield        com/logitech/ue/devicedata/DeviceSettingsInfo.bleState:Ljava/lang/Boolean;
        //   128: aload_0        
        //   129: invokespecial   com/logitech/ue/tasks/GetDeviceSettingInfoTask.checkCancellation:()V
        //   132: aload_2        
        //   133: aload_1        
        //   134: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.checkPartitionState:()[B
        //   137: putfield        com/logitech/ue/devicedata/DeviceSettingsInfo.partitionInfo:[B
        //   140: aload_0        
        //   141: invokespecial   com/logitech/ue/tasks/GetDeviceSettingInfoTask.checkCancellation:()V
        //   144: aload_2        
        //   145: aload_1        
        //   146: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.getGestureState:()Z
        //   149: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   152: putfield        com/logitech/ue/devicedata/DeviceSettingsInfo.isGestureEnabled:Ljava/lang/Boolean;
        //   155: aload_0        
        //   156: invokespecial   com/logitech/ue/tasks/GetDeviceSettingInfoTask.checkCancellation:()V
        //   159: aload_2        
        //   160: aload_1        
        //   161: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.isBroadcastModeSupported:()Z
        //   164: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   167: putfield        com/logitech/ue/devicedata/DeviceSettingsInfo.isXUPSupported:Ljava/lang/Boolean;
        //   170: aload_2        
        //   171: areturn        
        //   172: astore_3       
        //   173: getstatic       com/logitech/ue/tasks/GetDeviceSettingInfoTask.TAG:Ljava/lang/String;
        //   176: ldc             "Custom Sonification fetch failed"
        //   178: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   181: pop            
        //   182: goto            62
        //   185: astore_3       
        //   186: aload_2        
        //   187: aconst_null    
        //   188: putfield        com/logitech/ue/devicedata/DeviceSettingsInfo.supportedLanguages:Ljava/util/ArrayList;
        //   191: goto            98
        //   194: astore_3       
        //   195: getstatic       com/logitech/ue/tasks/GetDeviceSettingInfoTask.TAG:Ljava/lang/String;
        //   198: ldc             "Device doesn't tws lock not supported"
        //   200: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   203: pop            
        //   204: goto            113
        //   207: astore_3       
        //   208: getstatic       com/logitech/ue/tasks/GetDeviceSettingInfoTask.TAG:Ljava/lang/String;
        //   211: ldc             "Device doesn't support ble"
        //   213: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   216: pop            
        //   217: goto            128
        //   220: astore_3       
        //   221: getstatic       com/logitech/ue/tasks/GetDeviceSettingInfoTask.TAG:Ljava/lang/String;
        //   224: ldc             "Device doesn't support ADK 3.0"
        //   226: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   229: pop            
        //   230: goto            140
        //   233: astore_3       
        //   234: getstatic       com/logitech/ue/tasks/GetDeviceSettingInfoTask.TAG:Ljava/lang/String;
        //   237: ldc             "Device doesn't support Gesture"
        //   239: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   242: pop            
        //   243: goto            155
        //    Exceptions:
        //  throws java.lang.Exception
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                                 
        //  -----  -----  -----  -----  ---------------------------------------------------------------------
        //  51     62     172    185    Lcom/logitech/ue/centurion/exceptions/UEException;
        //  90     98     185    194    Lcom/logitech/ue/centurion/exceptions/UEUnrecognisedCommandException;
        //  102    113    194    207    Lcom/logitech/ue/centurion/exceptions/UEUnrecognisedCommandException;
        //  117    128    207    220    Lcom/logitech/ue/centurion/exceptions/UEUnrecognisedCommandException;
        //  132    140    220    233    Lcom/logitech/ue/centurion/exceptions/UEException;
        //  144    155    233    246    Lcom/logitech/ue/centurion/exceptions/UEException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 119 out of bounds for length 119
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
