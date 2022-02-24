// 
// Decompiled by Procyon v0.5.36
// 

package com.caverock.androidsvg;

import android.content.res.TypedArray;
import java.io.IOException;
import java.io.FileNotFoundException;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.util.Log;
import android.net.Uri;
import android.util.AttributeSet;
import android.graphics.Paint;
import android.view.View;
import android.content.Context;
import java.lang.reflect.Method;
import android.widget.ImageView;

public class SVGImageView extends ImageView
{
    private static Method setLayerTypeMethod;
    
    static {
        SVGImageView.setLayerTypeMethod = null;
    }
    
    public SVGImageView(final Context context) {
        super(context);
        try {
            SVGImageView.setLayerTypeMethod = View.class.getMethod("setLayerType", Integer.TYPE, Paint.class);
        }
        catch (NoSuchMethodException ex) {}
    }
    
    public SVGImageView(final Context context, final AttributeSet set) {
        super(context, set, 0);
        while (true) {
            try {
                SVGImageView.setLayerTypeMethod = View.class.getMethod("setLayerType", Integer.TYPE, Paint.class);
                this.init(set, 0);
            }
            catch (NoSuchMethodException ex) {
                continue;
            }
            break;
        }
    }
    
    public SVGImageView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        while (true) {
            try {
                SVGImageView.setLayerTypeMethod = View.class.getMethod("setLayerType", Integer.TYPE, Paint.class);
                this.init(set, n);
            }
            catch (NoSuchMethodException ex) {
                continue;
            }
            break;
        }
    }
    
    private void init(AttributeSet obtainStyledAttributes, int resourceId) {
        obtainStyledAttributes = (AttributeSet)this.getContext().getTheme().obtainStyledAttributes(obtainStyledAttributes, R$styleable.SVGImageView, resourceId, 0);
        try {
            resourceId = ((TypedArray)obtainStyledAttributes).getResourceId(0, -1);
            if (resourceId != -1) {
                this.setImageResource(resourceId);
            }
            else {
                final String string = ((TypedArray)obtainStyledAttributes).getString(0);
                if (string != null) {
                    if (this.internalSetImageURI(Uri.parse(string), false)) {
                        ((TypedArray)obtainStyledAttributes).recycle();
                        return;
                    }
                    this.setImageAsset(string);
                }
                ((TypedArray)obtainStyledAttributes).recycle();
            }
        }
        finally {
            ((TypedArray)obtainStyledAttributes).recycle();
        }
    }
    
    private boolean internalSetImageURI(final Uri p0, final boolean p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: istore_3       
        //     2: aconst_null    
        //     3: astore          4
        //     5: aconst_null    
        //     6: astore          5
        //     8: aconst_null    
        //     9: astore          6
        //    11: aload_0        
        //    12: invokevirtual   com/caverock/androidsvg/SVGImageView.getContext:()Landroid/content/Context;
        //    15: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //    18: aload_1        
        //    19: invokevirtual   android/content/ContentResolver.openInputStream:(Landroid/net/Uri;)Ljava/io/InputStream;
        //    22: astore          7
        //    24: aload           7
        //    26: astore          6
        //    28: aload           7
        //    30: astore          4
        //    32: aload           7
        //    34: astore          5
        //    36: aload           7
        //    38: invokestatic    com/caverock/androidsvg/SVG.getFromInputStream:(Ljava/io/InputStream;)Lcom/caverock/androidsvg/SVG;
        //    41: astore          8
        //    43: aload           7
        //    45: astore          6
        //    47: aload           7
        //    49: astore          4
        //    51: aload           7
        //    53: astore          5
        //    55: aload_0        
        //    56: invokespecial   com/caverock/androidsvg/SVGImageView.setSoftwareLayerType:()V
        //    59: aload           7
        //    61: astore          6
        //    63: aload           7
        //    65: astore          4
        //    67: aload           7
        //    69: astore          5
        //    71: new             Landroid/graphics/drawable/PictureDrawable;
        //    74: astore          9
        //    76: aload           7
        //    78: astore          6
        //    80: aload           7
        //    82: astore          4
        //    84: aload           7
        //    86: astore          5
        //    88: aload           9
        //    90: aload           8
        //    92: invokevirtual   com/caverock/androidsvg/SVG.renderToPicture:()Landroid/graphics/Picture;
        //    95: invokespecial   android/graphics/drawable/PictureDrawable.<init>:(Landroid/graphics/Picture;)V
        //    98: aload           7
        //   100: astore          6
        //   102: aload           7
        //   104: astore          4
        //   106: aload           7
        //   108: astore          5
        //   110: aload_0        
        //   111: aload           9
        //   113: invokevirtual   com/caverock/androidsvg/SVGImageView.setImageDrawable:(Landroid/graphics/drawable/Drawable;)V
        //   116: aload           7
        //   118: ifnull          126
        //   121: aload           7
        //   123: invokevirtual   java/io/InputStream.close:()V
        //   126: iconst_1       
        //   127: istore_2       
        //   128: iload_2        
        //   129: ireturn        
        //   130: astore          5
        //   132: iload_2        
        //   133: ifeq            175
        //   136: aload           6
        //   138: astore          5
        //   140: new             Ljava/lang/StringBuilder;
        //   143: astore          4
        //   145: aload           6
        //   147: astore          5
        //   149: aload           4
        //   151: ldc             "File not found: "
        //   153: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   156: aload           6
        //   158: astore          5
        //   160: ldc             "SVGImageView"
        //   162: aload           4
        //   164: aload_1        
        //   165: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   168: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   171: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   174: pop            
        //   175: iload_3        
        //   176: istore_2       
        //   177: aload           6
        //   179: ifnull          128
        //   182: aload           6
        //   184: invokevirtual   java/io/InputStream.close:()V
        //   187: iload_3        
        //   188: istore_2       
        //   189: goto            128
        //   192: astore_1       
        //   193: iload_3        
        //   194: istore_2       
        //   195: goto            128
        //   198: astore          7
        //   200: aload           4
        //   202: astore          5
        //   204: new             Ljava/lang/StringBuilder;
        //   207: astore          6
        //   209: aload           4
        //   211: astore          5
        //   213: aload           6
        //   215: ldc             "Error loading file "
        //   217: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   220: aload           4
        //   222: astore          5
        //   224: ldc             "SVGImageView"
        //   226: aload           6
        //   228: aload_1        
        //   229: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   232: ldc             ": "
        //   234: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   237: aload           7
        //   239: invokevirtual   com/caverock/androidsvg/SVGParseException.getMessage:()Ljava/lang/String;
        //   242: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   245: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   248: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   251: pop            
        //   252: iload_3        
        //   253: istore_2       
        //   254: aload           4
        //   256: ifnull          128
        //   259: aload           4
        //   261: invokevirtual   java/io/InputStream.close:()V
        //   264: iload_3        
        //   265: istore_2       
        //   266: goto            128
        //   269: astore_1       
        //   270: iload_3        
        //   271: istore_2       
        //   272: goto            128
        //   275: astore_1       
        //   276: aload           5
        //   278: ifnull          286
        //   281: aload           5
        //   283: invokevirtual   java/io/InputStream.close:()V
        //   286: aload_1        
        //   287: athrow         
        //   288: astore_1       
        //   289: goto            126
        //   292: astore          5
        //   294: goto            286
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                       
        //  -----  -----  -----  -----  -------------------------------------------
        //  11     24     130    198    Ljava/io/FileNotFoundException;
        //  11     24     198    275    Lcom/caverock/androidsvg/SVGParseException;
        //  11     24     275    288    Any
        //  36     43     130    198    Ljava/io/FileNotFoundException;
        //  36     43     198    275    Lcom/caverock/androidsvg/SVGParseException;
        //  36     43     275    288    Any
        //  55     59     130    198    Ljava/io/FileNotFoundException;
        //  55     59     198    275    Lcom/caverock/androidsvg/SVGParseException;
        //  55     59     275    288    Any
        //  71     76     130    198    Ljava/io/FileNotFoundException;
        //  71     76     198    275    Lcom/caverock/androidsvg/SVGParseException;
        //  71     76     275    288    Any
        //  88     98     130    198    Ljava/io/FileNotFoundException;
        //  88     98     198    275    Lcom/caverock/androidsvg/SVGParseException;
        //  88     98     275    288    Any
        //  110    116    130    198    Ljava/io/FileNotFoundException;
        //  110    116    198    275    Lcom/caverock/androidsvg/SVGParseException;
        //  110    116    275    288    Any
        //  121    126    288    292    Ljava/io/IOException;
        //  140    145    275    288    Any
        //  149    156    275    288    Any
        //  160    175    275    288    Any
        //  182    187    192    198    Ljava/io/IOException;
        //  204    209    275    288    Any
        //  213    220    275    288    Any
        //  224    252    275    288    Any
        //  259    264    269    275    Ljava/io/IOException;
        //  281    286    292    297    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 148 out of bounds for length 148
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
    
    private void setSoftwareLayerType() {
        if (SVGImageView.setLayerTypeMethod != null) {
            try {
                SVGImageView.setLayerTypeMethod.invoke(this, View.class.getField("LAYER_TYPE_SOFTWARE").getInt(new View(this.getContext())), null);
            }
            catch (Exception ex) {
                Log.w("SVGImageView", "Unexpected failure calling setLayerType", (Throwable)ex);
            }
        }
    }
    
    public void setImageAsset(final String str) {
        try {
            final SVG fromAsset = SVG.getFromAsset(this.getContext().getAssets(), str);
            this.setSoftwareLayerType();
            this.setImageDrawable((Drawable)new PictureDrawable(fromAsset.renderToPicture()));
        }
        catch (SVGParseException ex) {
            Log.e("SVGImageView", "Error loading file " + str + ": " + ex.getMessage());
        }
        catch (FileNotFoundException ex3) {
            Log.e("SVGImageView", "File not found: " + str);
        }
        catch (IOException ex2) {
            Log.e("SVGImageView", "Unable to load asset file: " + str, (Throwable)ex2);
        }
    }
    
    public void setImageResource(final int i) {
        try {
            final SVG fromResource = SVG.getFromResource(this.getContext(), i);
            this.setSoftwareLayerType();
            this.setImageDrawable((Drawable)new PictureDrawable(fromResource.renderToPicture()));
        }
        catch (SVGParseException ex) {
            Log.e("SVGImageView", String.format("Error loading resource 0x%x: %s", i, ex.getMessage()));
        }
    }
    
    public void setImageURI(final Uri uri) {
        this.internalSetImageURI(uri, true);
    }
    
    public void setSVG(final SVG svg) {
        if (svg == null) {
            throw new IllegalArgumentException("Null value passed to setSVG()");
        }
        this.setSoftwareLayerType();
        this.setImageDrawable((Drawable)new PictureDrawable(svg.renderToPicture()));
    }
}
