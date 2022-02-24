// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

public class mf
{
    private static final String a;
    private static boolean b;
    
    static {
        a = mf.class.getSimpleName();
    }
    
    public static void a() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: monitorenter   
        //     3: getstatic       com/flurry/sdk/mf.b:Z
        //     6: istore_0       
        //     7: iload_0        
        //     8: ifeq            15
        //    11: ldc             Lcom/flurry/sdk/mf;.class
        //    13: monitorexit    
        //    14: return         
        //    15: ldc             Lcom/flurry/sdk/jk;.class
        //    17: invokestatic    com/flurry/sdk/ko.a:(Ljava/lang/Class;)V
        //    20: ldc             Lcom/flurry/sdk/hr;.class
        //    22: invokestatic    com/flurry/sdk/ko.a:(Ljava/lang/Class;)V
        //    25: ldc             Lcom/flurry/sdk/md;.class
        //    27: invokestatic    com/flurry/sdk/ko.a:(Ljava/lang/Class;)V
        //    30: ldc             "com.flurry.sdk.i"
        //    32: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //    35: invokestatic    com/flurry/sdk/ko.a:(Ljava/lang/Class;)V
        //    38: iconst_1       
        //    39: putstatic       com/flurry/sdk/mf.b:Z
        //    42: goto            11
        //    45: astore_1       
        //    46: ldc             Lcom/flurry/sdk/mf;.class
        //    48: monitorexit    
        //    49: aload_1        
        //    50: athrow         
        //    51: astore_1       
        //    52: iconst_3       
        //    53: getstatic       com/flurry/sdk/mf.a:Ljava/lang/String;
        //    56: ldc             "Analytics module not available"
        //    58: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //    61: goto            25
        //    64: astore_1       
        //    65: iconst_3       
        //    66: getstatic       com/flurry/sdk/mf.a:Ljava/lang/String;
        //    69: ldc             "Crash module not available"
        //    71: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //    74: goto            30
        //    77: astore_1       
        //    78: iconst_3       
        //    79: getstatic       com/flurry/sdk/mf.a:Ljava/lang/String;
        //    82: ldc             "Ads module not available"
        //    84: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //    87: goto            38
        //    90: astore_1       
        //    91: goto            78
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                              
        //  -----  -----  -----  -----  ----------------------------------
        //  3      7      45     51     Any
        //  15     20     45     51     Any
        //  20     25     51     64     Ljava/lang/NoClassDefFoundError;
        //  20     25     45     51     Any
        //  25     30     64     77     Ljava/lang/NoClassDefFoundError;
        //  25     30     45     51     Any
        //  30     38     90     94     Ljava/lang/NoClassDefFoundError;
        //  30     38     77     78     Ljava/lang/ClassNotFoundException;
        //  30     38     45     51     Any
        //  38     42     45     51     Any
        //  52     61     45     51     Any
        //  65     74     45     51     Any
        //  78     87     45     51     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 46 out of bounds for length 46
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
