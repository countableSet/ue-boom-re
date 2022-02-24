// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import android.view.animation.Transformation;
import android.view.View;
import android.widget.LinearLayout$LayoutParams;
import android.view.animation.Animation;

public class ViewExpandAnimation extends Animation
{
    public static final int COLLAPSE = 1;
    public static final int EXPAND = 0;
    private int mEndHeight;
    private LinearLayout$LayoutParams mLayoutParams;
    private int mType;
    private View mView;
    
    public ViewExpandAnimation(final View mView, final int n, final int mType) {
        this.setDuration((long)n);
        (this.mView = mView).clearAnimation();
        this.mEndHeight = this.mView.getHeight();
        this.mLayoutParams = (LinearLayout$LayoutParams)mView.getLayoutParams();
        this.mType = mType;
        if (this.mType == 0) {
            this.mLayoutParams.height = 0;
        }
        else {
            this.mLayoutParams.height = -2;
        }
        mView.setVisibility(0);
    }
    
    protected void applyTransformation(final float n, final Transformation transformation) {
        super.applyTransformation(n, transformation);
        if (n < 1.0f) {
            if (this.mType == 0) {
                this.mLayoutParams.height = (int)(this.mEndHeight * n);
            }
            else {
                this.mLayoutParams.height = (int)(this.mEndHeight * (1.0f - n));
            }
            this.mView.requestLayout();
        }
        else if (this.mType == 0) {
            this.mLayoutParams.height = -2;
            this.mView.requestLayout();
        }
        else {
            this.mView.setVisibility(4);
        }
    }
    
    public int getHeight() {
        return this.mView.getHeight();
    }
    
    public void setHeight(final int mEndHeight) {
        this.mEndHeight = mEndHeight;
    }
}
