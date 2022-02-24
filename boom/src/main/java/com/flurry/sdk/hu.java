// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.io.File;

public final class hu
{
    private static final String a;
    
    static {
        a = hu.class.getSimpleName();
    }
    
    public static ji a(final File p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_1       
        //     2: aload_1        
        //     3: astore_2       
        //     4: aload_0        
        //     5: ifnull          17
        //     8: aload_0        
        //     9: invokevirtual   java/io/File.exists:()Z
        //    12: ifne            19
        //    15: aload_1        
        //    16: astore_2       
        //    17: aload_2        
        //    18: areturn        
        //    19: new             Lcom/flurry/sdk/ji$a;
        //    22: dup            
        //    23: invokespecial   com/flurry/sdk/ji$a.<init>:()V
        //    26: astore_3       
        //    27: new             Ljava/io/FileInputStream;
        //    30: astore_2       
        //    31: aload_2        
        //    32: aload_0        
        //    33: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    36: new             Ljava/io/DataInputStream;
        //    39: astore_0       
        //    40: aload_0        
        //    41: aload_2        
        //    42: invokespecial   java/io/DataInputStream.<init>:(Ljava/io/InputStream;)V
        //    45: aload_0        
        //    46: astore          4
        //    48: aload_2        
        //    49: astore          5
        //    51: aload_0        
        //    52: invokevirtual   java/io/DataInputStream.readUnsignedShort:()I
        //    55: ldc             46586
        //    57: if_icmpeq       88
        //    60: aload_0        
        //    61: astore          4
        //    63: aload_2        
        //    64: astore          5
        //    66: iconst_3       
        //    67: getstatic       com/flurry/sdk/hu.a:Ljava/lang/String;
        //    70: ldc             "Unexpected file type"
        //    72: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //    75: aload_0        
        //    76: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //    79: aload_2        
        //    80: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //    83: aload_1        
        //    84: astore_2       
        //    85: goto            17
        //    88: aload_0        
        //    89: astore          4
        //    91: aload_2        
        //    92: astore          5
        //    94: aload_0        
        //    95: invokevirtual   java/io/DataInputStream.readUnsignedShort:()I
        //    98: istore          6
        //   100: iload           6
        //   102: iconst_2       
        //   103: if_icmpeq       174
        //   106: aload_0        
        //   107: astore          4
        //   109: aload_2        
        //   110: astore          5
        //   112: getstatic       com/flurry/sdk/hu.a:Ljava/lang/String;
        //   115: astore          7
        //   117: aload_0        
        //   118: astore          4
        //   120: aload_2        
        //   121: astore          5
        //   123: new             Ljava/lang/StringBuilder;
        //   126: astore_3       
        //   127: aload_0        
        //   128: astore          4
        //   130: aload_2        
        //   131: astore          5
        //   133: aload_3        
        //   134: ldc             "Unknown agent file version: "
        //   136: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   139: aload_0        
        //   140: astore          4
        //   142: aload_2        
        //   143: astore          5
        //   145: bipush          6
        //   147: aload           7
        //   149: aload_3        
        //   150: iload           6
        //   152: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   155: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   158: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //   161: aload_0        
        //   162: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   165: aload_2        
        //   166: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   169: aload_1        
        //   170: astore_2       
        //   171: goto            17
        //   174: aload_0        
        //   175: astore          4
        //   177: aload_2        
        //   178: astore          5
        //   180: aload_3        
        //   181: aload_0        
        //   182: invokeinterface com/flurry/sdk/lg.a:(Ljava/io/InputStream;)Ljava/lang/Object;
        //   187: checkcast       Lcom/flurry/sdk/ji;
        //   190: astore_1       
        //   191: aload_0        
        //   192: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   195: aload_2        
        //   196: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   199: aload_1        
        //   200: astore_0       
        //   201: aload_0        
        //   202: astore_2       
        //   203: goto            17
        //   206: astore_1       
        //   207: aconst_null    
        //   208: astore_0       
        //   209: aconst_null    
        //   210: astore_2       
        //   211: aload_0        
        //   212: astore          4
        //   214: aload_2        
        //   215: astore          5
        //   217: iconst_3       
        //   218: getstatic       com/flurry/sdk/hu.a:Ljava/lang/String;
        //   221: ldc             "Error loading legacy agent data."
        //   223: aload_1        
        //   224: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   227: aload_0        
        //   228: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   231: aload_2        
        //   232: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   235: aconst_null    
        //   236: astore_0       
        //   237: goto            201
        //   240: astore_0       
        //   241: aconst_null    
        //   242: astore          4
        //   244: aconst_null    
        //   245: astore_2       
        //   246: aload           4
        //   248: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   251: aload_2        
        //   252: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   255: aload_0        
        //   256: athrow         
        //   257: astore_0       
        //   258: aconst_null    
        //   259: astore          4
        //   261: goto            246
        //   264: astore_0       
        //   265: aload           5
        //   267: astore_2       
        //   268: goto            246
        //   271: astore_1       
        //   272: aconst_null    
        //   273: astore_0       
        //   274: goto            211
        //   277: astore_1       
        //   278: goto            211
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  27     36     206    211    Ljava/lang/Exception;
        //  27     36     240    246    Any
        //  36     45     271    277    Ljava/lang/Exception;
        //  36     45     257    264    Any
        //  51     60     277    281    Ljava/lang/Exception;
        //  51     60     264    271    Any
        //  66     75     277    281    Ljava/lang/Exception;
        //  66     75     264    271    Any
        //  94     100    277    281    Ljava/lang/Exception;
        //  94     100    264    271    Any
        //  112    117    277    281    Ljava/lang/Exception;
        //  112    117    264    271    Any
        //  123    127    277    281    Ljava/lang/Exception;
        //  123    127    264    271    Any
        //  133    139    277    281    Ljava/lang/Exception;
        //  133    139    264    271    Any
        //  145    161    277    281    Ljava/lang/Exception;
        //  145    161    264    271    Any
        //  180    191    277    281    Ljava/lang/Exception;
        //  180    191    264    271    Any
        //  217    227    264    271    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 161 out of bounds for length 161
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
