// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.views;

import android.content.res.TypedArray;
import com.logitech.ue.R;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.FrameLayout;

public class FadeButton extends FrameLayout
{
    float fadeInValue;
    float fadeOutValue;
    boolean isClickableDisabled;
    
    public FadeButton(final Context context) {
        this(context, null);
    }
    
    public FadeButton(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public FadeButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.fadeInValue = 1.0f;
        this.fadeOutValue = 0.5f;
        this.isClickableDisabled = false;
        this.setClickable(true);
        final TypedArray obtainStyledAttributes = this.getContext().getTheme().obtainStyledAttributes(set, R.styleable.FadeButton, n, 0);
        this.fadeInValue = obtainStyledAttributes.getFloat(0, this.fadeInValue);
        this.fadeOutValue = obtainStyledAttributes.getFloat(1, this.fadeOutValue);
    }
    
    public boolean isClickableDisabled() {
        return this.isClickableDisabled;
    }
    
    public void setClickableDisableState(final boolean isClickableDisabled) {
        if (isClickableDisabled && this.isEnabled()) {
            this.setAlpha(this.fadeOutValue);
        }
        else {
            float alpha;
            if (this.isEnabled()) {
                alpha = this.fadeInValue;
            }
            else {
                alpha = this.fadeOutValue;
            }
            this.setAlpha(alpha);
        }
        this.isClickableDisabled = isClickableDisabled;
    }
    
    public void setPressed(final boolean pressed) {
        super.setPressed(pressed);
        if (!this.isClickableDisabled) {
            if (pressed) {
                this.animate().alpha(this.fadeOutValue).setDuration(0L).start();
            }
            else {
                this.animate().alpha(this.fadeInValue).setDuration(100L).start();
            }
        }
    }
}
