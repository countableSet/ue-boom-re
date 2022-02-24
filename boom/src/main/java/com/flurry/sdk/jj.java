// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.text.TextUtils;

public class jj
{
    private static String a;
    
    static {
        jj.a = jj.class.getName();
    }
    
    public static String a(final String s) {
        String s2 = "a=" + jy.a().d;
        if (!TextUtils.isEmpty((CharSequence)s)) {
            s2 = String.format("%s&%s", s2, "cid=" + b(s));
        }
        return s2;
    }
    
    private static String b(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ifnull          135
        //     4: aload_0        
        //     5: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //     8: invokevirtual   java/lang/String.length:()I
        //    11: ifle            135
        //    14: aload_0        
        //    15: invokestatic    com/flurry/sdk/ly.f:(Ljava/lang/String;)[B
        //    18: astore_0       
        //    19: aload_0        
        //    20: ifnull          76
        //    23: aload_0        
        //    24: arraylength    
        //    25: istore_1       
        //    26: iload_1        
        //    27: bipush          20
        //    29: if_icmpne       76
        //    32: getstatic       com/flurry/sdk/jj.a:Ljava/lang/String;
        //    35: astore_2       
        //    36: new             Ljava/lang/StringBuilder;
        //    39: astore_3       
        //    40: aload_3        
        //    41: ldc             "syndication hashedId is:"
        //    43: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //    46: new             Ljava/lang/String;
        //    49: astore          4
        //    51: aload           4
        //    53: aload_0        
        //    54: invokespecial   java/lang/String.<init>:([B)V
        //    57: iconst_5       
        //    58: aload_2        
        //    59: aload_3        
        //    60: aload           4
        //    62: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    65: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    68: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //    71: aload_0        
        //    72: invokestatic    com/flurry/sdk/ly.a:([B)Ljava/lang/String;
        //    75: areturn        
        //    76: getstatic       com/flurry/sdk/jj.a:Ljava/lang/String;
        //    79: astore          4
        //    81: new             Ljava/lang/StringBuilder;
        //    84: astore_2       
        //    85: aload_2        
        //    86: ldc             "sha1 is not 20 bytes long: "
        //    88: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //    91: bipush          6
        //    93: aload           4
        //    95: aload_2        
        //    96: aload_0        
        //    97: invokestatic    java/util/Arrays.toString:([B)Ljava/lang/String;
        //   100: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   103: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   106: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //   109: aconst_null    
        //   110: astore_0       
        //   111: goto            71
        //   114: astore_0       
        //   115: aconst_null    
        //   116: astore_0       
        //   117: bipush          6
        //   119: getstatic       com/flurry/sdk/jj.a:Ljava/lang/String;
        //   122: ldc             "Exception in getHashedSyndicationIdString()"
        //   124: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //   127: goto            71
        //   130: astore          4
        //   132: goto            117
        //   135: aconst_null    
        //   136: astore_0       
        //   137: goto            71
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  14     19     114    117    Ljava/lang/Exception;
        //  23     26     114    117    Ljava/lang/Exception;
        //  32     71     130    135    Ljava/lang/Exception;
        //  76     109    114    117    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0071:
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
