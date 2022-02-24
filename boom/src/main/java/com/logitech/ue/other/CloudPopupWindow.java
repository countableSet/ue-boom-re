// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.view.View;
import android.content.Context;

public abstract class CloudPopupWindow extends AdvancedPopupWindow
{
    public CloudPopupWindow(final Context context) {
        super(context);
    }
    
    @Override
    public void setContentView(final View view) {
        final FrameLayout contentView = (FrameLayout)this.mInflater.inflate(2130968616, (ViewGroup)null);
        final FrameLayout$LayoutParams layoutParams = new FrameLayout$LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        contentView.addView(view);
        view.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        super.setContentView((View)contentView);
    }
}
