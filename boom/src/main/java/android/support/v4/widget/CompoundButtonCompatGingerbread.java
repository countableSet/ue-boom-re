// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.widget;

import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.widget.CompoundButton;
import java.lang.reflect.Field;

class CompoundButtonCompatGingerbread
{
    private static final String TAG = "CompoundButtonCompatGingerbread";
    private static Field sButtonDrawableField;
    private static boolean sButtonDrawableFieldFetched;
    
    static Drawable getButtonDrawable(final CompoundButton p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ifne            27
        //     6: ldc             Landroid/widget/CompoundButton;.class
        //     8: ldc             "mButtonDrawable"
        //    10: invokevirtual   java/lang/Class.getDeclaredField:(Ljava/lang/String;)Ljava/lang/reflect/Field;
        //    13: putstatic       android/support/v4/widget/CompoundButtonCompatGingerbread.sButtonDrawableField:Ljava/lang/reflect/Field;
        //    16: getstatic       android/support/v4/widget/CompoundButtonCompatGingerbread.sButtonDrawableField:Ljava/lang/reflect/Field;
        //    19: iconst_1       
        //    20: invokevirtual   java/lang/reflect/Field.setAccessible:(Z)V
        //    23: iconst_1       
        //    24: putstatic       android/support/v4/widget/CompoundButtonCompatGingerbread.sButtonDrawableFieldFetched:Z
        //    27: getstatic       android/support/v4/widget/CompoundButtonCompatGingerbread.sButtonDrawableField:Ljava/lang/reflect/Field;
        //    30: ifnull          73
        //    33: getstatic       android/support/v4/widget/CompoundButtonCompatGingerbread.sButtonDrawableField:Ljava/lang/reflect/Field;
        //    36: aload_0        
        //    37: invokevirtual   java/lang/reflect/Field.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    40: checkcast       Landroid/graphics/drawable/Drawable;
        //    43: astore_0       
        //    44: aload_0        
        //    45: areturn        
        //    46: astore_1       
        //    47: ldc             "CompoundButtonCompatGingerbread"
        //    49: ldc             "Failed to retrieve mButtonDrawable field"
        //    51: aload_1        
        //    52: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    55: pop            
        //    56: goto            23
        //    59: astore_0       
        //    60: ldc             "CompoundButtonCompatGingerbread"
        //    62: ldc             "Failed to get button drawable via reflection"
        //    64: aload_0        
        //    65: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    68: pop            
        //    69: aconst_null    
        //    70: putstatic       android/support/v4/widget/CompoundButtonCompatGingerbread.sButtonDrawableField:Ljava/lang/reflect/Field;
        //    73: aconst_null    
        //    74: astore_0       
        //    75: goto            44
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                              
        //  -----  -----  -----  -----  ----------------------------------
        //  6      23     46     59     Ljava/lang/NoSuchFieldException;
        //  33     44     59     73     Ljava/lang/IllegalAccessException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0044:
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
    
    static ColorStateList getButtonTintList(final CompoundButton compoundButton) {
        ColorStateList supportButtonTintList;
        if (compoundButton instanceof TintableCompoundButton) {
            supportButtonTintList = ((TintableCompoundButton)compoundButton).getSupportButtonTintList();
        }
        else {
            supportButtonTintList = null;
        }
        return supportButtonTintList;
    }
    
    static PorterDuff$Mode getButtonTintMode(final CompoundButton compoundButton) {
        PorterDuff$Mode supportButtonTintMode;
        if (compoundButton instanceof TintableCompoundButton) {
            supportButtonTintMode = ((TintableCompoundButton)compoundButton).getSupportButtonTintMode();
        }
        else {
            supportButtonTintMode = null;
        }
        return supportButtonTintMode;
    }
    
    static void setButtonTintList(final CompoundButton compoundButton, final ColorStateList supportButtonTintList) {
        if (compoundButton instanceof TintableCompoundButton) {
            ((TintableCompoundButton)compoundButton).setSupportButtonTintList(supportButtonTintList);
        }
    }
    
    static void setButtonTintMode(final CompoundButton compoundButton, final PorterDuff$Mode supportButtonTintMode) {
        if (compoundButton instanceof TintableCompoundButton) {
            ((TintableCompoundButton)compoundButton).setSupportButtonTintMode(supportButtonTintMode);
        }
    }
}
