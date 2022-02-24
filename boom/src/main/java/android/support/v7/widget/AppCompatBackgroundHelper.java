// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.content.res.TypedArray;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.graphics.drawable.GradientDrawable;
import android.os.Build$VERSION;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.support.v4.view.ViewCompat;
import android.support.annotation.NonNull;
import android.graphics.drawable.Drawable;
import android.view.View;

class AppCompatBackgroundHelper
{
    private TintInfo mBackgroundTint;
    private final AppCompatDrawableManager mDrawableManager;
    private TintInfo mInternalBackgroundTint;
    private TintInfo mTmpInfo;
    private final View mView;
    
    AppCompatBackgroundHelper(final View mView, final AppCompatDrawableManager mDrawableManager) {
        this.mView = mView;
        this.mDrawableManager = mDrawableManager;
    }
    
    private void compatTintDrawableUsingFrameworkTint(@NonNull final Drawable drawable) {
        if (this.mTmpInfo == null) {
            this.mTmpInfo = new TintInfo();
        }
        final TintInfo mTmpInfo = this.mTmpInfo;
        mTmpInfo.clear();
        final ColorStateList backgroundTintList = ViewCompat.getBackgroundTintList(this.mView);
        if (backgroundTintList != null) {
            mTmpInfo.mHasTintList = true;
            mTmpInfo.mTintList = backgroundTintList;
        }
        final PorterDuff$Mode backgroundTintMode = ViewCompat.getBackgroundTintMode(this.mView);
        if (backgroundTintMode != null) {
            mTmpInfo.mHasTintMode = true;
            mTmpInfo.mTintMode = backgroundTintMode;
        }
        if (mTmpInfo.mHasTintList || mTmpInfo.mHasTintMode) {
            AppCompatDrawableManager.tintDrawable(drawable, mTmpInfo, this.mView.getDrawableState());
        }
    }
    
    private boolean shouldCompatTintUsingFrameworkTint(@NonNull final Drawable drawable) {
        return Build$VERSION.SDK_INT == 21 && drawable instanceof GradientDrawable;
    }
    
    void applySupportBackgroundTint() {
        final Drawable background = this.mView.getBackground();
        if (background != null) {
            if (this.mBackgroundTint != null) {
                AppCompatDrawableManager.tintDrawable(background, this.mBackgroundTint, this.mView.getDrawableState());
            }
            else if (this.mInternalBackgroundTint != null) {
                AppCompatDrawableManager.tintDrawable(background, this.mInternalBackgroundTint, this.mView.getDrawableState());
            }
            else if (this.shouldCompatTintUsingFrameworkTint(background)) {
                this.compatTintDrawableUsingFrameworkTint(background);
            }
        }
    }
    
    ColorStateList getSupportBackgroundTintList() {
        ColorStateList mTintList;
        if (this.mBackgroundTint != null) {
            mTintList = this.mBackgroundTint.mTintList;
        }
        else {
            mTintList = null;
        }
        return mTintList;
    }
    
    PorterDuff$Mode getSupportBackgroundTintMode() {
        PorterDuff$Mode mTintMode;
        if (this.mBackgroundTint != null) {
            mTintMode = this.mBackgroundTint.mTintMode;
        }
        else {
            mTintMode = null;
        }
        return mTintMode;
    }
    
    void loadFromAttributes(AttributeSet obtainStyledAttributes, final int n) {
        obtainStyledAttributes = (AttributeSet)this.mView.getContext().obtainStyledAttributes(obtainStyledAttributes, R.styleable.ViewBackgroundHelper, n, 0);
        try {
            if (((TypedArray)obtainStyledAttributes).hasValue(R.styleable.ViewBackgroundHelper_android_background)) {
                final ColorStateList tintList = this.mDrawableManager.getTintList(this.mView.getContext(), ((TypedArray)obtainStyledAttributes).getResourceId(R.styleable.ViewBackgroundHelper_android_background, -1));
                if (tintList != null) {
                    this.setInternalBackgroundTint(tintList);
                }
            }
            if (((TypedArray)obtainStyledAttributes).hasValue(R.styleable.ViewBackgroundHelper_backgroundTint)) {
                ViewCompat.setBackgroundTintList(this.mView, ((TypedArray)obtainStyledAttributes).getColorStateList(R.styleable.ViewBackgroundHelper_backgroundTint));
            }
            if (((TypedArray)obtainStyledAttributes).hasValue(R.styleable.ViewBackgroundHelper_backgroundTintMode)) {
                ViewCompat.setBackgroundTintMode(this.mView, DrawableUtils.parseTintMode(((TypedArray)obtainStyledAttributes).getInt(R.styleable.ViewBackgroundHelper_backgroundTintMode, -1), null));
            }
        }
        finally {
            ((TypedArray)obtainStyledAttributes).recycle();
        }
    }
    
    void onSetBackgroundDrawable(final Drawable drawable) {
        this.setInternalBackgroundTint(null);
    }
    
    void onSetBackgroundResource(final int n) {
        ColorStateList tintList;
        if (this.mDrawableManager != null) {
            tintList = this.mDrawableManager.getTintList(this.mView.getContext(), n);
        }
        else {
            tintList = null;
        }
        this.setInternalBackgroundTint(tintList);
    }
    
    void setInternalBackgroundTint(final ColorStateList mTintList) {
        if (mTintList != null) {
            if (this.mInternalBackgroundTint == null) {
                this.mInternalBackgroundTint = new TintInfo();
            }
            this.mInternalBackgroundTint.mTintList = mTintList;
            this.mInternalBackgroundTint.mHasTintList = true;
        }
        else {
            this.mInternalBackgroundTint = null;
        }
        this.applySupportBackgroundTint();
    }
    
    void setSupportBackgroundTintList(final ColorStateList mTintList) {
        if (this.mBackgroundTint == null) {
            this.mBackgroundTint = new TintInfo();
        }
        this.mBackgroundTint.mTintList = mTintList;
        this.mBackgroundTint.mHasTintList = true;
        this.applySupportBackgroundTint();
    }
    
    void setSupportBackgroundTintMode(final PorterDuff$Mode mTintMode) {
        if (this.mBackgroundTint == null) {
            this.mBackgroundTint = new TintInfo();
        }
        this.mBackgroundTint.mTintMode = mTintMode;
        this.mBackgroundTint.mHasTintMode = true;
        this.applySupportBackgroundTint();
    }
}
