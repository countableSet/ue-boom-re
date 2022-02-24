// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.graphics.Rect;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.LinearLayout;

public class FitWindowsLinearLayout extends LinearLayout implements FitWindowsViewGroup
{
    private OnFitSystemWindowsListener mListener;
    
    public FitWindowsLinearLayout(final Context context) {
        super(context);
    }
    
    public FitWindowsLinearLayout(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    protected boolean fitSystemWindows(final Rect rect) {
        if (this.mListener != null) {
            this.mListener.onFitSystemWindows(rect);
        }
        return super.fitSystemWindows(rect);
    }
    
    public void setOnFitSystemWindowsListener(final OnFitSystemWindowsListener mListener) {
        this.mListener = mListener;
    }
}
