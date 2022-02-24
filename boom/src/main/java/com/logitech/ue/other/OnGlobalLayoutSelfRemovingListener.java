// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import android.os.Build$VERSION;
import android.view.View;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;

public abstract class OnGlobalLayoutSelfRemovingListener implements ViewTreeObserver$OnGlobalLayoutListener
{
    private View mHostView;
    
    public OnGlobalLayoutSelfRemovingListener(final View mHostView) {
        this.mHostView = mHostView;
    }
    
    public final void onGlobalLayout() {
        if (this.mHostView != null && this.mHostView.getViewTreeObserver().isAlive()) {
            if (Build$VERSION.SDK_INT >= 16) {
                this.mHostView.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
            }
            else {
                this.mHostView.getViewTreeObserver().removeGlobalOnLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
            }
            this.mHostView = null;
            this.onHandleGlobalLayout();
        }
    }
    
    public abstract void onHandleGlobalLayout();
}
