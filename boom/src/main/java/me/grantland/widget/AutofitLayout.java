// 
// Decompiled by Procyon v0.5.36
// 

package me.grantland.widget;

import android.widget.TextView;
import android.view.ViewGroup$LayoutParams;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import java.util.WeakHashMap;
import android.widget.FrameLayout;

public class AutofitLayout extends FrameLayout
{
    private boolean mEnabled;
    private WeakHashMap<View, AutofitHelper> mHelpers;
    private float mMinTextSize;
    private float mPrecision;
    
    public AutofitLayout(final Context context) {
        super(context);
        this.mHelpers = new WeakHashMap<View, AutofitHelper>();
        this.init(context, null, 0);
    }
    
    public AutofitLayout(final Context context, final AttributeSet set) {
        super(context, set);
        this.mHelpers = new WeakHashMap<View, AutofitHelper>();
        this.init(context, set, 0);
    }
    
    public AutofitLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mHelpers = new WeakHashMap<View, AutofitHelper>();
        this.init(context, set, n);
    }
    
    private void init(final Context context, final AttributeSet set, final int n) {
        boolean boolean1 = true;
        int dimensionPixelSize = -1;
        float float1 = -1.0f;
        if (set != null) {
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.AutofitTextView, n, 0);
            boolean1 = obtainStyledAttributes.getBoolean(R.styleable.AutofitTextView_sizeToFit, true);
            dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AutofitTextView_minTextSize, -1);
            float1 = obtainStyledAttributes.getFloat(R.styleable.AutofitTextView_precision, -1.0f);
            obtainStyledAttributes.recycle();
        }
        this.mEnabled = boolean1;
        this.mMinTextSize = (float)dimensionPixelSize;
        this.mPrecision = float1;
    }
    
    public void addView(final View view, final int n, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        super.addView(view, n, viewGroup$LayoutParams);
        final TextView key = (TextView)view;
        final AutofitHelper setEnabled = AutofitHelper.create(key).setEnabled(this.mEnabled);
        if (this.mPrecision > 0.0f) {
            setEnabled.setPrecision(this.mPrecision);
        }
        if (this.mMinTextSize > 0.0f) {
            setEnabled.setMinTextSize(0, this.mMinTextSize);
        }
        this.mHelpers.put((View)key, setEnabled);
    }
    
    public AutofitHelper getAutofitHelper(final int n) {
        return this.mHelpers.get(this.getChildAt(n));
    }
    
    public AutofitHelper getAutofitHelper(final TextView key) {
        return this.mHelpers.get(key);
    }
}
