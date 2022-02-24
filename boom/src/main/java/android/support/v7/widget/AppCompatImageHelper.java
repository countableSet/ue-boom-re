// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.support.v4.content.ContextCompat;
import android.graphics.drawable.Drawable;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AppCompatImageHelper
{
    private final AppCompatDrawableManager mDrawableManager;
    private final ImageView mView;
    
    public AppCompatImageHelper(final ImageView mView, final AppCompatDrawableManager mDrawableManager) {
        this.mView = mView;
        this.mDrawableManager = mDrawableManager;
    }
    
    public void loadFromAttributes(AttributeSet obtainStyledAttributes, int resourceId) {
        obtainStyledAttributes = (AttributeSet)TintTypedArray.obtainStyledAttributes(this.mView.getContext(), obtainStyledAttributes, R.styleable.AppCompatImageView, resourceId, 0);
        try {
            final Drawable drawableIfKnown = ((TintTypedArray)obtainStyledAttributes).getDrawableIfKnown(R.styleable.AppCompatImageView_android_src);
            if (drawableIfKnown != null) {
                this.mView.setImageDrawable(drawableIfKnown);
            }
            resourceId = ((TintTypedArray)obtainStyledAttributes).getResourceId(R.styleable.AppCompatImageView_srcCompat, -1);
            if (resourceId != -1) {
                final Drawable drawable = this.mDrawableManager.getDrawable(this.mView.getContext(), resourceId);
                if (drawable != null) {
                    this.mView.setImageDrawable(drawable);
                }
            }
            final Drawable drawable2 = this.mView.getDrawable();
            if (drawable2 != null) {
                DrawableUtils.fixDrawable(drawable2);
            }
        }
        finally {
            ((TintTypedArray)obtainStyledAttributes).recycle();
        }
    }
    
    public void setImageResource(final int n) {
        if (n != 0) {
            Drawable imageDrawable;
            if (this.mDrawableManager != null) {
                imageDrawable = this.mDrawableManager.getDrawable(this.mView.getContext(), n);
            }
            else {
                imageDrawable = ContextCompat.getDrawable(this.mView.getContext(), n);
            }
            if (imageDrawable != null) {
                DrawableUtils.fixDrawable(imageDrawable);
            }
            this.mView.setImageDrawable(imageDrawable);
        }
        else {
            this.mView.setImageDrawable((Drawable)null);
        }
    }
}
