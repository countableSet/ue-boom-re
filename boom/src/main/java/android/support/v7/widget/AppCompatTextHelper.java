// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.text.method.TransformationMethod;
import android.support.v7.text.AllCapsTransformationMethod;
import android.content.res.TypedArray;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.graphics.drawable.Drawable;
import android.content.res.ColorStateList;
import android.content.Context;
import android.os.Build$VERSION;
import android.support.v7.appcompat.R;
import android.widget.TextView;

class AppCompatTextHelper
{
    private static final int[] TEXT_APPEARANCE_ATTRS;
    private static final int[] VIEW_ATTRS;
    private TintInfo mDrawableBottomTint;
    private TintInfo mDrawableLeftTint;
    private TintInfo mDrawableRightTint;
    private TintInfo mDrawableTopTint;
    final TextView mView;
    
    static {
        VIEW_ATTRS = new int[] { 16842804, 16843119, 16843117, 16843120, 16843118 };
        TEXT_APPEARANCE_ATTRS = new int[] { R.attr.textAllCaps };
    }
    
    AppCompatTextHelper(final TextView mView) {
        this.mView = mView;
    }
    
    static AppCompatTextHelper create(final TextView textView) {
        AppCompatTextHelper appCompatTextHelper;
        if (Build$VERSION.SDK_INT >= 17) {
            appCompatTextHelper = new AppCompatTextHelperV17(textView);
        }
        else {
            appCompatTextHelper = new AppCompatTextHelper(textView);
        }
        return appCompatTextHelper;
    }
    
    protected static TintInfo createTintInfo(final Context context, final AppCompatDrawableManager appCompatDrawableManager, final int n) {
        final ColorStateList tintList = appCompatDrawableManager.getTintList(context, n);
        TintInfo tintInfo;
        if (tintList != null) {
            tintInfo = new TintInfo();
            tintInfo.mHasTintList = true;
            tintInfo.mTintList = tintList;
        }
        else {
            tintInfo = null;
        }
        return tintInfo;
    }
    
    final void applyCompoundDrawableTint(final Drawable drawable, final TintInfo tintInfo) {
        if (drawable != null && tintInfo != null) {
            AppCompatDrawableManager.tintDrawable(drawable, tintInfo, this.mView.getDrawableState());
        }
    }
    
    void applyCompoundDrawablesTints() {
        if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
            final Drawable[] compoundDrawables = this.mView.getCompoundDrawables();
            this.applyCompoundDrawableTint(compoundDrawables[0], this.mDrawableLeftTint);
            this.applyCompoundDrawableTint(compoundDrawables[1], this.mDrawableTopTint);
            this.applyCompoundDrawableTint(compoundDrawables[2], this.mDrawableRightTint);
            this.applyCompoundDrawableTint(compoundDrawables[3], this.mDrawableBottomTint);
        }
    }
    
    void loadFromAttributes(final AttributeSet set, final int n) {
        final Context context = this.mView.getContext();
        final AppCompatDrawableManager value = AppCompatDrawableManager.get();
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, AppCompatTextHelper.VIEW_ATTRS, n, 0);
        final int resourceId = obtainStyledAttributes.getResourceId(0, -1);
        if (obtainStyledAttributes.hasValue(1)) {
            this.mDrawableLeftTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(1, 0));
        }
        if (obtainStyledAttributes.hasValue(2)) {
            this.mDrawableTopTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(2, 0));
        }
        if (obtainStyledAttributes.hasValue(3)) {
            this.mDrawableRightTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(3, 0));
        }
        if (obtainStyledAttributes.hasValue(4)) {
            this.mDrawableBottomTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(4, 0));
        }
        obtainStyledAttributes.recycle();
        if (!(this.mView.getTransformationMethod() instanceof PasswordTransformationMethod)) {
            boolean allCaps = false;
            final boolean b = false;
            int n2 = 0;
            final int n3 = 0;
            if (resourceId != -1) {
                final TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(resourceId, R.styleable.TextAppearance);
                allCaps = b;
                n2 = n3;
                if (obtainStyledAttributes2.hasValue(R.styleable.TextAppearance_textAllCaps)) {
                    n2 = 1;
                    allCaps = obtainStyledAttributes2.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
                }
                obtainStyledAttributes2.recycle();
            }
            final TypedArray obtainStyledAttributes3 = context.obtainStyledAttributes(set, AppCompatTextHelper.TEXT_APPEARANCE_ATTRS, n, 0);
            if (obtainStyledAttributes3.hasValue(0)) {
                n2 = 1;
                allCaps = obtainStyledAttributes3.getBoolean(0, false);
            }
            obtainStyledAttributes3.recycle();
            if (n2 != 0) {
                this.setAllCaps(allCaps);
            }
        }
    }
    
    void onSetTextAppearance(final Context context, final int n) {
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(n, AppCompatTextHelper.TEXT_APPEARANCE_ATTRS);
        if (obtainStyledAttributes.getBoolean(0, false)) {
            this.setAllCaps(true);
        }
        obtainStyledAttributes.recycle();
    }
    
    void setAllCaps(final boolean b) {
        final TextView mView = this.mView;
        Object transformationMethod;
        if (b) {
            transformationMethod = new AllCapsTransformationMethod(this.mView.getContext());
        }
        else {
            transformationMethod = null;
        }
        mView.setTransformationMethod((TransformationMethod)transformationMethod);
    }
}
