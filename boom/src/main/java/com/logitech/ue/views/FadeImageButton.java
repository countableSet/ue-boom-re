// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.views;

import android.content.res.TypedArray;
import com.logitech.ue.R;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.ImageView;

public class FadeImageButton extends ImageView
{
    float fadeInValue;
    float fadeOutValue;
    
    public FadeImageButton(final Context context) {
        this(context, null);
    }
    
    public FadeImageButton(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public FadeImageButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.fadeInValue = 1.0f;
        this.fadeOutValue = 0.5f;
        this.setClickable(true);
        final TypedArray obtainStyledAttributes = this.getContext().getTheme().obtainStyledAttributes(set, R.styleable.FadeImageButton, n, 0);
        this.fadeInValue = obtainStyledAttributes.getFloat(0, this.fadeInValue);
        this.fadeOutValue = obtainStyledAttributes.getFloat(1, this.fadeOutValue);
    }
    
    public void setActivated(final boolean activated) {
        super.setActivated(activated);
        if (activated) {
            this.setAlpha(this.fadeInValue);
        }
        else {
            this.setAlpha(this.fadeOutValue);
        }
    }
    
    public void setPressed(final boolean pressed) {
        super.setPressed(pressed);
        if (this.isActivated()) {
            if (pressed) {
                this.setAlpha(this.fadeOutValue);
            }
            else {
                this.setAlpha(this.fadeInValue);
            }
        }
    }
}
