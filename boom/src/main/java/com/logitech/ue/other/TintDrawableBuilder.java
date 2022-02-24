// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.graphics.PorterDuff$Mode;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.graphics.drawable.Drawable;
import android.content.Context;

public class TintDrawableBuilder
{
    private int mColor;
    private Context mContext;
    private Drawable mDrawable;
    private Drawable mWrappedDrawable;
    
    public TintDrawableBuilder(final Context mContext) {
        this.mWrappedDrawable = null;
        this.mContext = mContext;
    }
    
    public static TintDrawableBuilder setContext(final Context context) {
        return new TintDrawableBuilder(context);
    }
    
    public Drawable build() {
        if (this.mDrawable != null) {
            this.mWrappedDrawable = this.mDrawable.mutate();
            DrawableCompat.setTint(this.mWrappedDrawable = DrawableCompat.wrap(this.mWrappedDrawable), this.mColor);
            DrawableCompat.setTintMode(this.mWrappedDrawable, PorterDuff$Mode.SRC_IN);
        }
        return this.mWrappedDrawable;
    }
    
    public TintDrawableBuilder setDrawable(final int n) {
        this.mDrawable = ContextCompat.getDrawable(this.mContext, n);
        return this;
    }
    
    public TintDrawableBuilder setDrawable(final Drawable mDrawable) {
        this.mDrawable = mDrawable;
        return this;
    }
    
    public TintDrawableBuilder setTintColor(@ColorInt final int mColor) {
        this.mColor = mColor;
        return this;
    }
}
