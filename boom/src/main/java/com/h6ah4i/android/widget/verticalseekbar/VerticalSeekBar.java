// 
// Decompiled by Procyon v0.5.36
// 

package com.h6ah4i.android.widget.verticalseekbar;

import android.os.Build$VERSION;
import android.view.ViewGroup$LayoutParams;
import android.view.KeyEvent;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.content.res.TypedArray;
import android.view.View;
import android.support.v4.view.ViewCompat;
import android.view.ViewParent;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.drawable.Drawable;
import java.lang.reflect.Method;
import android.support.v7.widget.AppCompatSeekBar;

public class VerticalSeekBar extends AppCompatSeekBar
{
    public static final int ROTATION_ANGLE_CW_270 = 270;
    public static final int ROTATION_ANGLE_CW_90 = 90;
    private boolean mIsDragging;
    private Method mMethodSetProgress;
    private int mRotationAngle;
    private Drawable mThumb_;
    
    public VerticalSeekBar(final Context context) {
        super(context);
        this.mRotationAngle = 90;
        this.initialize(context, null, 0, 0);
    }
    
    public VerticalSeekBar(final Context context, final AttributeSet set) {
        super(context, set);
        this.mRotationAngle = 90;
        this.initialize(context, set, 0, 0);
    }
    
    public VerticalSeekBar(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mRotationAngle = 90;
        this.initialize(context, set, n, 0);
    }
    
    private void attemptClaimDrag(final boolean b) {
        final ViewParent parent = this.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(b);
        }
    }
    
    private Drawable getThumbCompat() {
        return this.mThumb_;
    }
    
    private VerticalSeekBarWrapper getWrapper() {
        final ViewParent parent = this.getParent();
        VerticalSeekBarWrapper verticalSeekBarWrapper;
        if (parent instanceof VerticalSeekBarWrapper) {
            verticalSeekBarWrapper = (VerticalSeekBarWrapper)parent;
        }
        else {
            verticalSeekBarWrapper = null;
        }
        return verticalSeekBarWrapper;
    }
    
    private void initialize(final Context context, final AttributeSet set, int integer, final int n) {
        ViewCompat.setLayoutDirection((View)this, 0);
        if (set != null) {
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.VerticalSeekBar, integer, n);
            integer = obtainStyledAttributes.getInteger(R.styleable.VerticalSeekBar_seekBarRotation, 0);
            if (isValidRotationAngle(integer)) {
                this.mRotationAngle = integer;
            }
            obtainStyledAttributes.recycle();
        }
    }
    
    private static boolean isValidRotationAngle(final int n) {
        return n == 90 || n == 270;
    }
    
    private void onStartTrackingTouch() {
        this.mIsDragging = true;
    }
    
    private void onStopTrackingTouch() {
        this.mIsDragging = false;
    }
    
    private boolean onTouchEventTraditionalRotation(final MotionEvent motionEvent) {
        boolean b = false;
        if (this.isEnabled()) {
            final Drawable thumbCompat = this.getThumbCompat();
            switch (motionEvent.getAction()) {
                case 0: {
                    this.setPressed(true);
                    if (thumbCompat != null) {
                        this.invalidate(thumbCompat.getBounds());
                    }
                    this.onStartTrackingTouch();
                    this.trackTouchEvent(motionEvent);
                    this.attemptClaimDrag(true);
                    break;
                }
                case 2: {
                    if (this.mIsDragging) {
                        this.trackTouchEvent(motionEvent);
                        break;
                    }
                    break;
                }
                case 1: {
                    if (this.mIsDragging) {
                        this.trackTouchEvent(motionEvent);
                        this.onStopTrackingTouch();
                        this.setPressed(false);
                    }
                    else {
                        this.onStartTrackingTouch();
                        this.trackTouchEvent(motionEvent);
                        this.onStopTrackingTouch();
                        this.attemptClaimDrag(false);
                    }
                    this.invalidate();
                    break;
                }
                case 3: {
                    if (this.mIsDragging) {
                        this.onStopTrackingTouch();
                        this.setPressed(false);
                    }
                    this.invalidate();
                    break;
                }
            }
            b = true;
        }
        return b;
    }
    
    private boolean onTouchEventUseViewRotation(final MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0: {
                this.attemptClaimDrag(true);
                break;
            }
            case 1: {
                this.attemptClaimDrag(false);
                break;
            }
        }
        return super.onTouchEvent(motionEvent);
    }
    
    private void refreshThumb() {
        this.onSizeChanged(super.getWidth(), super.getHeight(), 0, 0);
    }
    
    private void setProgress(final int p0, final boolean p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: monitorenter   
        //     2: aload_0        
        //     3: getfield        com/h6ah4i/android/widget/verticalseekbar/VerticalSeekBar.mMethodSetProgress:Ljava/lang/reflect/Method;
        //     6: astore_3       
        //     7: aload_3        
        //     8: ifnonnull       47
        //    11: aload_0        
        //    12: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //    15: ldc             "setProgress"
        //    17: iconst_2       
        //    18: anewarray       Ljava/lang/Class;
        //    21: dup            
        //    22: iconst_0       
        //    23: getstatic       java/lang/Integer.TYPE:Ljava/lang/Class;
        //    26: aastore        
        //    27: dup            
        //    28: iconst_1       
        //    29: getstatic       java/lang/Boolean.TYPE:Ljava/lang/Class;
        //    32: aastore        
        //    33: invokevirtual   java/lang/Class.getMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //    36: astore_3       
        //    37: aload_3        
        //    38: iconst_1       
        //    39: invokevirtual   java/lang/reflect/Method.setAccessible:(Z)V
        //    42: aload_0        
        //    43: aload_3        
        //    44: putfield        com/h6ah4i/android/widget/verticalseekbar/VerticalSeekBar.mMethodSetProgress:Ljava/lang/reflect/Method;
        //    47: aload_0        
        //    48: getfield        com/h6ah4i/android/widget/verticalseekbar/VerticalSeekBar.mMethodSetProgress:Ljava/lang/reflect/Method;
        //    51: astore_3       
        //    52: aload_3        
        //    53: ifnull          90
        //    56: aload_0        
        //    57: getfield        com/h6ah4i/android/widget/verticalseekbar/VerticalSeekBar.mMethodSetProgress:Ljava/lang/reflect/Method;
        //    60: aload_0        
        //    61: iconst_2       
        //    62: anewarray       Ljava/lang/Object;
        //    65: dup            
        //    66: iconst_0       
        //    67: iload_1        
        //    68: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    71: aastore        
        //    72: dup            
        //    73: iconst_1       
        //    74: iload_2        
        //    75: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //    78: aastore        
        //    79: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //    82: pop            
        //    83: aload_0        
        //    84: invokespecial   com/h6ah4i/android/widget/verticalseekbar/VerticalSeekBar.refreshThumb:()V
        //    87: aload_0        
        //    88: monitorexit    
        //    89: return         
        //    90: aload_0        
        //    91: iload_1        
        //    92: invokespecial   android/support/v7/widget/AppCompatSeekBar.setProgress:(I)V
        //    95: goto            83
        //    98: astore_3       
        //    99: aload_0        
        //   100: monitorexit    
        //   101: aload_3        
        //   102: athrow         
        //   103: astore_3       
        //   104: goto            83
        //   107: astore_3       
        //   108: goto            83
        //   111: astore_3       
        //   112: goto            83
        //   115: astore_3       
        //   116: goto            47
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                         
        //  -----  -----  -----  -----  ---------------------------------------------
        //  2      7      98     103    Any
        //  11     47     115    119    Ljava/lang/NoSuchMethodException;
        //  11     47     98     103    Any
        //  47     52     98     103    Any
        //  56     83     111    115    Ljava/lang/IllegalArgumentException;
        //  56     83     107    111    Ljava/lang/IllegalAccessException;
        //  56     83     103    107    Ljava/lang/reflect/InvocationTargetException;
        //  56     83     98     103    Any
        //  83     87     98     103    Any
        //  90     95     98     103    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0047:
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
    
    private void trackTouchEvent(final MotionEvent motionEvent) {
        final int paddingTop = super.getPaddingTop();
        final int paddingBottom = super.getPaddingBottom();
        final int height = this.getHeight();
        final int n = height - paddingTop - paddingBottom;
        final int n2 = (int)motionEvent.getY();
        float n3 = 0.0f;
        switch (this.mRotationAngle) {
            case 90: {
                n3 = (float)(n2 - paddingTop);
                break;
            }
            case 270: {
                n3 = (float)(height - paddingTop - n2);
                break;
            }
        }
        float n4;
        if (n3 < 0.0f || n == 0) {
            n4 = 0.0f;
        }
        else if (n3 > n) {
            n4 = 1.0f;
        }
        else {
            n4 = n3 / n;
        }
        this.setProgress((int)(n4 * this.getMax()), true);
    }
    
    public int getRotationAngle() {
        return this.mRotationAngle;
    }
    
    protected void onDraw(final Canvas canvas) {
        while (true) {
            while (true) {
                Label_0073: {
                    synchronized (this) {
                        if (!this.useViewRotation()) {
                            switch (this.mRotationAngle) {
                                case 90: {
                                    canvas.rotate(90.0f);
                                    canvas.translate(0.0f, (float)(-super.getWidth()));
                                    break;
                                }
                                case 270: {
                                    break Label_0073;
                                }
                            }
                        }
                        super.onDraw(canvas);
                        return;
                    }
                }
                canvas.rotate(-90.0f);
                canvas.translate((float)(-super.getHeight()), 0.0f);
                continue;
            }
        }
    }
    
    public boolean onKeyDown(int keyProgressIncrement, final KeyEvent keyEvent) {
        final boolean b = true;
        if (!this.isEnabled()) {
            return super.onKeyDown(keyProgressIncrement, keyEvent);
        }
        int n = 0;
        int n2 = 0;
        switch (keyProgressIncrement) {
            default: {
                n2 = 0;
                break;
            }
            case 20: {
                if (this.mRotationAngle == 90) {
                    n = 1;
                }
                else {
                    n = -1;
                }
                n2 = 1;
                break;
            }
            case 19: {
                if (this.mRotationAngle == 270) {
                    n = 1;
                }
                else {
                    n = -1;
                }
                n2 = 1;
                break;
            }
            case 21:
            case 22: {
                n2 = 1;
                break;
            }
        }
        if (n2 == 0) {
            return super.onKeyDown(keyProgressIncrement, keyEvent);
        }
        keyProgressIncrement = this.getKeyProgressIncrement();
        final int n3 = this.getProgress() + n * keyProgressIncrement;
        boolean onKeyDown = b;
        if (n3 >= 0) {
            onKeyDown = b;
            if (n3 <= this.getMax()) {
                this.setProgress(n3 - keyProgressIncrement, true);
                onKeyDown = b;
            }
        }
        return onKeyDown;
        onKeyDown = super.onKeyDown(keyProgressIncrement, keyEvent);
        return onKeyDown;
    }
    
    protected void onMeasure(final int n, final int n2) {
        while (true) {
            Label_0070: {
                synchronized (this) {
                    if (this.useViewRotation()) {
                        super.onMeasure(n, n2);
                    }
                    else {
                        super.onMeasure(n2, n);
                        final ViewGroup$LayoutParams layoutParams = this.getLayoutParams();
                        if (!this.isInEditMode() || layoutParams == null || layoutParams.height < 0) {
                            break Label_0070;
                        }
                        this.setMeasuredDimension(super.getMeasuredHeight(), this.getLayoutParams().height);
                    }
                    return;
                }
            }
            this.setMeasuredDimension(super.getMeasuredHeight(), super.getMeasuredWidth());
        }
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        if (this.useViewRotation()) {
            super.onSizeChanged(n, n2, n3, n4);
        }
        else {
            super.onSizeChanged(n2, n, n4, n3);
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        boolean b;
        if (this.useViewRotation()) {
            b = this.onTouchEventUseViewRotation(motionEvent);
        }
        else {
            b = this.onTouchEventTraditionalRotation(motionEvent);
        }
        return b;
    }
    
    public void setProgress(final int progress) {
        synchronized (this) {
            super.setProgress(progress);
            if (!this.useViewRotation()) {
                this.refreshThumb();
            }
        }
    }
    
    public void setRotationAngle(final int n) {
        if (!isValidRotationAngle(n)) {
            throw new IllegalArgumentException("Invalid angle specified :" + n);
        }
        if (this.mRotationAngle != n) {
            this.mRotationAngle = n;
            if (this.useViewRotation()) {
                final VerticalSeekBarWrapper wrapper = this.getWrapper();
                if (wrapper != null) {
                    wrapper.applyViewRotation();
                }
            }
            else {
                this.requestLayout();
            }
        }
    }
    
    public void setThumb(final Drawable mThumb_) {
        super.setThumb(this.mThumb_ = mThumb_);
    }
    
    boolean useViewRotation() {
        boolean b = true;
        boolean b2;
        if (Build$VERSION.SDK_INT >= 11) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        final boolean inEditMode = this.isInEditMode();
        if (!b2 || inEditMode) {
            b = false;
        }
        return b;
    }
}
