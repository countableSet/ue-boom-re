// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class le<ObjectType> extends ld<ObjectType>
{
    public le(final lg<ObjectType> lg) {
        super(lg);
    }
    
    @Override
    public final ObjectType a(final InputStream p0) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_2       
        //     2: aload_1        
        //     3: ifnull          27
        //     6: new             Ljava/util/zip/GZIPInputStream;
        //     9: astore_2       
        //    10: aload_2        
        //    11: aload_1        
        //    12: invokespecial   java/util/zip/GZIPInputStream.<init>:(Ljava/io/InputStream;)V
        //    15: aload_0        
        //    16: aload_2        
        //    17: invokespecial   com/flurry/sdk/ld.a:(Ljava/io/InputStream;)Ljava/lang/Object;
        //    20: astore_1       
        //    21: aload_2        
        //    22: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //    25: aload_1        
        //    26: astore_2       
        //    27: aload_2        
        //    28: areturn        
        //    29: astore_1       
        //    30: aconst_null    
        //    31: astore_2       
        //    32: aload_2        
        //    33: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //    36: aload_1        
        //    37: athrow         
        //    38: astore_1       
        //    39: goto            32
        //    Exceptions:
        //  throws java.io.IOException
        //    Signature:
        //  (Ljava/io/InputStream;)TObjectType;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  6      15     29     32     Any
        //  15     21     38     42     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0027:
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
    public final void a(final OutputStream p0, final ObjectType p1) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ifnull          23
        //     4: new             Ljava/util/zip/GZIPOutputStream;
        //     7: astore_3       
        //     8: aload_3        
        //     9: aload_1        
        //    10: invokespecial   java/util/zip/GZIPOutputStream.<init>:(Ljava/io/OutputStream;)V
        //    13: aload_0        
        //    14: aload_3        
        //    15: aload_2        
        //    16: invokespecial   com/flurry/sdk/ld.a:(Ljava/io/OutputStream;Ljava/lang/Object;)V
        //    19: aload_3        
        //    20: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //    23: return         
        //    24: astore_1       
        //    25: aconst_null    
        //    26: astore_2       
        //    27: aload_2        
        //    28: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //    31: aload_1        
        //    32: athrow         
        //    33: astore_1       
        //    34: aload_3        
        //    35: astore_2       
        //    36: goto            27
        //    Exceptions:
        //  throws java.io.IOException
        //    Signature:
        //  (Ljava/io/OutputStream;TObjectType;)V
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  4      13     24     27     Any
        //  13     19     33     39     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0023:
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
