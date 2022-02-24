// 
// Decompiled by Procyon v0.5.36
// 

package com.h6ah4i.android.widget.verticalseekbar;

import android.view.View$MeasureSpec;
import android.widget.FrameLayout$LayoutParams;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.FrameLayout;

public class VerticalSeekBarWrapper extends FrameLayout
{
    public VerticalSeekBarWrapper(final Context context) {
        this(context, null, 0);
    }
    
    public VerticalSeekBarWrapper(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public VerticalSeekBarWrapper(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    private void applyViewRotation(final int n, final int width) {
        final VerticalSeekBar childSeekBar = this.getChildSeekBar();
        if (childSeekBar != null) {
            final int rotationAngle = childSeekBar.getRotationAngle();
            final ViewGroup$LayoutParams layoutParams = childSeekBar.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = -2;
            childSeekBar.setLayoutParams(layoutParams);
            if (rotationAngle == 90) {
                final int paddingEnd = ViewCompat.getPaddingEnd((View)childSeekBar);
                ViewCompat.setRotation((View)childSeekBar, 90.0f);
                ViewCompat.setTranslationX((View)childSeekBar, (float)(-(width - n) / 2));
                ViewCompat.setTranslationY((View)childSeekBar, (float)(width / 2 - paddingEnd));
            }
            else if (rotationAngle == 270) {
                final int paddingStart = ViewCompat.getPaddingStart((View)childSeekBar);
                ViewCompat.setRotation((View)childSeekBar, -90.0f);
                ViewCompat.setTranslationX((View)childSeekBar, (float)(-(width - n) / 2));
                ViewCompat.setTranslationY((View)childSeekBar, (float)(width / 2 - paddingStart));
            }
        }
    }
    
    private VerticalSeekBar getChildSeekBar() {
        View child;
        if (this.getChildCount() > 0) {
            child = this.getChildAt(0);
        }
        else {
            child = null;
        }
        VerticalSeekBar verticalSeekBar;
        if (child instanceof VerticalSeekBar) {
            verticalSeekBar = (VerticalSeekBar)child;
        }
        else {
            verticalSeekBar = null;
        }
        return verticalSeekBar;
    }
    
    private void onSizeChangedTraditionalRotation(final int n, final int height, final int n2, final int n3) {
        final VerticalSeekBar childSeekBar = this.getChildSeekBar();
        if (childSeekBar != null) {
            final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)childSeekBar.getLayoutParams();
            frameLayout$LayoutParams.width = -2;
            frameLayout$LayoutParams.height = height;
            childSeekBar.setLayoutParams((ViewGroup$LayoutParams)frameLayout$LayoutParams);
            childSeekBar.measure(0, 0);
            final int measuredWidth = childSeekBar.getMeasuredWidth();
            childSeekBar.measure(View$MeasureSpec.makeMeasureSpec(n, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(height, 1073741824));
            frameLayout$LayoutParams.gravity = 51;
            frameLayout$LayoutParams.leftMargin = (n - measuredWidth) / 2;
            childSeekBar.setLayoutParams((ViewGroup$LayoutParams)frameLayout$LayoutParams);
        }
        super.onSizeChanged(n, height, n2, n3);
    }
    
    private void onSizeChangedUseViewRotation(final int n, final int n2, final int n3, final int n4) {
        final VerticalSeekBar childSeekBar = this.getChildSeekBar();
        if (childSeekBar != null) {
            childSeekBar.measure(View$MeasureSpec.makeMeasureSpec(n2, 1073741824), View$MeasureSpec.makeMeasureSpec(n, Integer.MIN_VALUE));
        }
        this.applyViewRotation(n, n2);
        super.onSizeChanged(n, n2, n3, n4);
    }
    
    private boolean useViewRotation() {
        final VerticalSeekBar childSeekBar = this.getChildSeekBar();
        return childSeekBar != null && childSeekBar.useViewRotation();
    }
    
    void applyViewRotation() {
        this.applyViewRotation(this.getWidth(), this.getHeight());
    }
    
    protected void onMeasure(final int n, final int n2) {
        final VerticalSeekBar childSeekBar = this.getChildSeekBar();
        final int mode = View$MeasureSpec.getMode(n);
        if (childSeekBar != null && mode != 1073741824) {
            int n3;
            int n4;
            if (this.useViewRotation()) {
                childSeekBar.measure(n2, n);
                n3 = childSeekBar.getMeasuredHeight();
                n4 = childSeekBar.getMeasuredWidth();
            }
            else {
                childSeekBar.measure(n, n2);
                n3 = childSeekBar.getMeasuredWidth();
                n4 = childSeekBar.getMeasuredHeight();
            }
            this.setMeasuredDimension(ViewCompat.resolveSizeAndState(n3, n, 0), ViewCompat.resolveSizeAndState(n4, n2, 0));
        }
        else {
            super.onMeasure(n, n2);
        }
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        if (this.useViewRotation()) {
            this.onSizeChangedUseViewRotation(n, n2, n3, n4);
        }
        else {
            this.onSizeChangedTraditionalRotation(n, n2, n3, n4);
        }
    }
}
