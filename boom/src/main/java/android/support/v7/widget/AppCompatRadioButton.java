// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.annotation.DrawableRes;
import android.graphics.PorterDuff$Mode;
import android.support.annotation.Nullable;
import android.content.res.ColorStateList;
import android.widget.CompoundButton;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v4.widget.TintableCompoundButton;
import android.widget.RadioButton;

public class AppCompatRadioButton extends RadioButton implements TintableCompoundButton
{
    private AppCompatCompoundButtonHelper mCompoundButtonHelper;
    private AppCompatDrawableManager mDrawableManager;
    
    public AppCompatRadioButton(final Context context) {
        this(context, null);
    }
    
    public AppCompatRadioButton(final Context context, final AttributeSet set) {
        this(context, set, R.attr.radioButtonStyle);
    }
    
    public AppCompatRadioButton(final Context context, final AttributeSet set, final int n) {
        super(TintContextWrapper.wrap(context), set, n);
        this.mDrawableManager = AppCompatDrawableManager.get();
        (this.mCompoundButtonHelper = new AppCompatCompoundButtonHelper((CompoundButton)this, this.mDrawableManager)).loadFromAttributes(set, n);
    }
    
    public int getCompoundPaddingLeft() {
        int n = super.getCompoundPaddingLeft();
        if (this.mCompoundButtonHelper != null) {
            n = this.mCompoundButtonHelper.getCompoundPaddingLeft(n);
        }
        return n;
    }
    
    @Nullable
    public ColorStateList getSupportButtonTintList() {
        ColorStateList supportButtonTintList;
        if (this.mCompoundButtonHelper != null) {
            supportButtonTintList = this.mCompoundButtonHelper.getSupportButtonTintList();
        }
        else {
            supportButtonTintList = null;
        }
        return supportButtonTintList;
    }
    
    @Nullable
    public PorterDuff$Mode getSupportButtonTintMode() {
        PorterDuff$Mode supportButtonTintMode;
        if (this.mCompoundButtonHelper != null) {
            supportButtonTintMode = this.mCompoundButtonHelper.getSupportButtonTintMode();
        }
        else {
            supportButtonTintMode = null;
        }
        return supportButtonTintMode;
    }
    
    public void setButtonDrawable(@DrawableRes final int n) {
        Drawable buttonDrawable;
        if (this.mDrawableManager != null) {
            buttonDrawable = this.mDrawableManager.getDrawable(this.getContext(), n);
        }
        else {
            buttonDrawable = ContextCompat.getDrawable(this.getContext(), n);
        }
        this.setButtonDrawable(buttonDrawable);
    }
    
    public void setButtonDrawable(final Drawable buttonDrawable) {
        super.setButtonDrawable(buttonDrawable);
        if (this.mCompoundButtonHelper != null) {
            this.mCompoundButtonHelper.onSetButtonDrawable();
        }
    }
    
    public void setSupportButtonTintList(@Nullable final ColorStateList supportButtonTintList) {
        if (this.mCompoundButtonHelper != null) {
            this.mCompoundButtonHelper.setSupportButtonTintList(supportButtonTintList);
        }
    }
    
    public void setSupportButtonTintMode(@Nullable final PorterDuff$Mode supportButtonTintMode) {
        if (this.mCompoundButtonHelper != null) {
            this.mCompoundButtonHelper.setSupportButtonTintMode(supportButtonTintMode);
        }
    }
}
