// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.content.Context;
import android.os.Build$VERSION;
import android.os.Environment;
import java.io.File;

public final class lx
{
    private static String a;
    
    static {
        lx.a = lx.class.getSimpleName();
    }
    
    public static File a() {
        final File file = null;
        final Context a = jy.a().a;
        File externalFilesDir = file;
        Label_0047: {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                if (Build$VERSION.SDK_INT < 19) {
                    externalFilesDir = file;
                    if (a.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                        break Label_0047;
                    }
                }
                externalFilesDir = a.getExternalFilesDir((String)null);
            }
        }
        File filesDir;
        if ((filesDir = externalFilesDir) == null) {
            filesDir = a.getFilesDir();
        }
        return filesDir;
    }
    
    @Deprecated
    public static void a(final File p0, final String p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ifnonnull       14
        //     4: iconst_4       
        //     5: getstatic       com/flurry/sdk/lx.a:Ljava/lang/String;
        //     8: ldc             "No persistent file specified."
        //    10: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //    13: return         
        //    14: aload_1        
        //    15: ifnonnull       52
        //    18: iconst_4       
        //    19: getstatic       com/flurry/sdk/lx.a:Ljava/lang/String;
        //    22: new             Ljava/lang/StringBuilder;
        //    25: dup            
        //    26: ldc             "No data specified; deleting persistent file: "
        //    28: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //    31: aload_0        
        //    32: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //    35: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    38: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    41: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //    44: aload_0        
        //    45: invokevirtual   java/io/File.delete:()Z
        //    48: pop            
        //    49: goto            13
        //    52: iconst_4       
        //    53: getstatic       com/flurry/sdk/lx.a:Ljava/lang/String;
        //    56: new             Ljava/lang/StringBuilder;
        //    59: dup            
        //    60: ldc             "Writing persistent data: "
        //    62: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //    65: aload_0        
        //    66: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //    69: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    72: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    75: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //    78: new             Ljava/io/FileOutputStream;
        //    81: astore_2       
        //    82: aload_2        
        //    83: aload_0        
        //    84: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //    87: aload_2        
        //    88: astore_0       
        //    89: aload_2        
        //    90: aload_1        
        //    91: invokevirtual   java/lang/String.getBytes:()[B
        //    94: invokevirtual   java/io/FileOutputStream.write:([B)V
        //    97: aload_2        
        //    98: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   101: goto            13
        //   104: astore_3       
        //   105: aconst_null    
        //   106: astore_1       
        //   107: aload_1        
        //   108: astore_0       
        //   109: bipush          6
        //   111: getstatic       com/flurry/sdk/lx.a:Ljava/lang/String;
        //   114: ldc             "Error writing persistent file"
        //   116: aload_3        
        //   117: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   120: aload_1        
        //   121: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   124: goto            13
        //   127: astore_1       
        //   128: aconst_null    
        //   129: astore_0       
        //   130: aload_0        
        //   131: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   134: aload_1        
        //   135: athrow         
        //   136: astore_1       
        //   137: goto            130
        //   140: astore_3       
        //   141: aload_2        
        //   142: astore_1       
        //   143: goto            107
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  78     87     104    107    Ljava/lang/Throwable;
        //  78     87     127    130    Any
        //  89     97     140    146    Ljava/lang/Throwable;
        //  89     97     136    140    Any
        //  109    120    136    140    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0107:
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
    
    public static boolean a(File parentFile) {
        final boolean b = false;
        boolean b2;
        if (parentFile == null) {
            b2 = b;
        }
        else {
            b2 = b;
            if (parentFile.getAbsoluteFile() != null) {
                parentFile = parentFile.getParentFile();
                if (parentFile == null) {
                    b2 = true;
                }
                else if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
                    km.a(6, lx.a, "Unable to create persistent dir: " + parentFile);
                    b2 = b;
                }
                else {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    public static File b() {
        final Context a = jy.a().a;
        File externalCacheDir = null;
        Label_0046: {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                if (Build$VERSION.SDK_INT < 19) {
                    externalCacheDir = externalCacheDir;
                    if (a.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                        break Label_0046;
                    }
                }
                externalCacheDir = a.getExternalCacheDir();
            }
        }
        File cacheDir;
        if ((cacheDir = externalCacheDir) == null) {
            cacheDir = a.getCacheDir();
        }
        return cacheDir;
    }
    
    public static boolean b(final File parent) {
        boolean delete = false;
        if (parent != null && parent.isDirectory()) {
            final String[] list = parent.list();
            for (int length = list.length, i = 0; i < length; ++i) {
                if (!b(new File(parent, list[i]))) {
                    return delete;
                }
            }
            return parent.delete();
        }
        return parent.delete();
        delete = parent.delete();
        return delete;
    }
    
    @Deprecated
    public static String c(final File p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_1       
        //     2: aload_0        
        //     3: ifnull          13
        //     6: aload_0        
        //     7: invokevirtual   java/io/File.exists:()Z
        //    10: ifne            26
        //    13: iconst_4       
        //    14: getstatic       com/flurry/sdk/lx.a:Ljava/lang/String;
        //    17: ldc             "Persistent file doesn't exist."
        //    19: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //    22: aload_1        
        //    23: astore_2       
        //    24: aload_2        
        //    25: areturn        
        //    26: iconst_4       
        //    27: getstatic       com/flurry/sdk/lx.a:Ljava/lang/String;
        //    30: new             Ljava/lang/StringBuilder;
        //    33: dup            
        //    34: ldc             "Loading persistent data: "
        //    36: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //    39: aload_0        
        //    40: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //    43: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    46: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    49: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //    52: new             Ljava/io/FileInputStream;
        //    55: astore_2       
        //    56: aload_2        
        //    57: aload_0        
        //    58: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    61: aload_2        
        //    62: astore_0       
        //    63: new             Ljava/lang/StringBuilder;
        //    66: astore_3       
        //    67: aload_2        
        //    68: astore_0       
        //    69: aload_3        
        //    70: invokespecial   java/lang/StringBuilder.<init>:()V
        //    73: aload_2        
        //    74: astore_0       
        //    75: sipush          1024
        //    78: newarray        B
        //    80: astore          4
        //    82: aload_2        
        //    83: astore_0       
        //    84: aload_2        
        //    85: aload           4
        //    87: invokevirtual   java/io/FileInputStream.read:([B)I
        //    90: istore          5
        //    92: iload           5
        //    94: ifle            162
        //    97: aload_2        
        //    98: astore_0       
        //    99: new             Ljava/lang/String;
        //   102: astore          6
        //   104: aload_2        
        //   105: astore_0       
        //   106: aload           6
        //   108: aload           4
        //   110: iconst_0       
        //   111: iload           5
        //   113: invokespecial   java/lang/String.<init>:([BII)V
        //   116: aload_2        
        //   117: astore_0       
        //   118: aload_3        
        //   119: aload           6
        //   121: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   124: pop            
        //   125: goto            82
        //   128: astore_3       
        //   129: aload_2        
        //   130: astore_0       
        //   131: bipush          6
        //   133: getstatic       com/flurry/sdk/lx.a:Ljava/lang/String;
        //   136: ldc             "Error when loading persistent file"
        //   138: aload_3        
        //   139: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   142: aload_2        
        //   143: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   146: aconst_null    
        //   147: astore_0       
        //   148: aload_1        
        //   149: astore_2       
        //   150: aload_0        
        //   151: ifnull          24
        //   154: aload_0        
        //   155: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   158: astore_2       
        //   159: goto            24
        //   162: aload_2        
        //   163: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   166: aload_3        
        //   167: astore_0       
        //   168: goto            148
        //   171: astore_0       
        //   172: aconst_null    
        //   173: astore_3       
        //   174: aload_0        
        //   175: astore_2       
        //   176: aload_3        
        //   177: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   180: aload_2        
        //   181: athrow         
        //   182: astore_2       
        //   183: aload_0        
        //   184: astore_3       
        //   185: goto            176
        //   188: astore_3       
        //   189: aconst_null    
        //   190: astore_2       
        //   191: goto            129
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  52     61     188    194    Ljava/lang/Throwable;
        //  52     61     171    176    Any
        //  63     67     128    129    Ljava/lang/Throwable;
        //  63     67     182    188    Any
        //  69     73     128    129    Ljava/lang/Throwable;
        //  69     73     182    188    Any
        //  75     82     128    129    Ljava/lang/Throwable;
        //  75     82     182    188    Any
        //  84     92     128    129    Ljava/lang/Throwable;
        //  84     92     182    188    Any
        //  99     104    128    129    Ljava/lang/Throwable;
        //  99     104    182    188    Any
        //  106    116    128    129    Ljava/lang/Throwable;
        //  106    116    182    188    Any
        //  118    125    128    129    Ljava/lang/Throwable;
        //  118    125    182    188    Any
        //  131    142    182    188    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0082:
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
}
