// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;
import java.lang.reflect.Method;

class DrawableCompatJellybeanMr1
{
    private static final String TAG = "DrawableCompatJellybeanMr1";
    private static Method sGetLayoutDirectionMethod;
    private static boolean sGetLayoutDirectionMethodFetched;
    private static Method sSetLayoutDirectionMethod;
    private static boolean sSetLayoutDirectionMethodFetched;
    
    public static int getLayoutDirection(final Drawable p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ifne            31
        //     6: ldc             Landroid/graphics/drawable/Drawable;.class
        //     8: ldc             "getLayoutDirection"
        //    10: iconst_0       
        //    11: anewarray       Ljava/lang/Class;
        //    14: invokevirtual   java/lang/Class.getDeclaredMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //    17: putstatic       android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod:Ljava/lang/reflect/Method;
        //    20: getstatic       android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod:Ljava/lang/reflect/Method;
        //    23: iconst_1       
        //    24: invokevirtual   java/lang/reflect/Method.setAccessible:(Z)V
        //    27: iconst_1       
        //    28: putstatic       android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sGetLayoutDirectionMethodFetched:Z
        //    31: getstatic       android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod:Ljava/lang/reflect/Method;
        //    34: ifnull          84
        //    37: getstatic       android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod:Ljava/lang/reflect/Method;
        //    40: aload_0        
        //    41: iconst_0       
        //    42: anewarray       Ljava/lang/Object;
        //    45: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //    48: checkcast       Ljava/lang/Integer;
        //    51: invokevirtual   java/lang/Integer.intValue:()I
        //    54: istore_1       
        //    55: iload_1        
        //    56: ireturn        
        //    57: astore_2       
        //    58: ldc             "DrawableCompatJellybeanMr1"
        //    60: ldc             "Failed to retrieve getLayoutDirection() method"
        //    62: aload_2        
        //    63: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    66: pop            
        //    67: goto            27
        //    70: astore_0       
        //    71: ldc             "DrawableCompatJellybeanMr1"
        //    73: ldc             "Failed to invoke getLayoutDirection() via reflection"
        //    75: aload_0        
        //    76: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    79: pop            
        //    80: aconst_null    
        //    81: putstatic       android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod:Ljava/lang/reflect/Method;
        //    84: iconst_m1      
        //    85: istore_1       
        //    86: goto            55
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  6      27     57     70     Ljava/lang/NoSuchMethodException;
        //  37     55     70     84     Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0055:
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
    
    public static boolean setLayoutDirection(final Drawable p0, final int p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: istore_2       
        //     2: getstatic       android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sSetLayoutDirectionMethodFetched:Z
        //     5: ifne            39
        //     8: ldc             Landroid/graphics/drawable/Drawable;.class
        //    10: ldc             "setLayoutDirection"
        //    12: iconst_1       
        //    13: anewarray       Ljava/lang/Class;
        //    16: dup            
        //    17: iconst_0       
        //    18: getstatic       java/lang/Integer.TYPE:Ljava/lang/Class;
        //    21: aastore        
        //    22: invokevirtual   java/lang/Class.getDeclaredMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //    25: putstatic       android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod:Ljava/lang/reflect/Method;
        //    28: getstatic       android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod:Ljava/lang/reflect/Method;
        //    31: iconst_1       
        //    32: invokevirtual   java/lang/reflect/Method.setAccessible:(Z)V
        //    35: iconst_1       
        //    36: putstatic       android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sSetLayoutDirectionMethodFetched:Z
        //    39: getstatic       android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod:Ljava/lang/reflect/Method;
        //    42: ifnull          93
        //    45: getstatic       android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod:Ljava/lang/reflect/Method;
        //    48: aload_0        
        //    49: iconst_1       
        //    50: anewarray       Ljava/lang/Object;
        //    53: dup            
        //    54: iconst_0       
        //    55: iload_1        
        //    56: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    59: aastore        
        //    60: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //    63: pop            
        //    64: iload_2        
        //    65: ireturn        
        //    66: astore_3       
        //    67: ldc             "DrawableCompatJellybeanMr1"
        //    69: ldc             "Failed to retrieve setLayoutDirection(int) method"
        //    71: aload_3        
        //    72: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    75: pop            
        //    76: goto            35
        //    79: astore_0       
        //    80: ldc             "DrawableCompatJellybeanMr1"
        //    82: ldc             "Failed to invoke setLayoutDirection(int) via reflection"
        //    84: aload_0        
        //    85: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    88: pop            
        //    89: aconst_null    
        //    90: putstatic       android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod:Ljava/lang/reflect/Method;
        //    93: iconst_0       
        //    94: istore_2       
        //    95: goto            64
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  8      35     66     79     Ljava/lang/NoSuchMethodException;
        //  45     64     79     93     Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0064:
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
