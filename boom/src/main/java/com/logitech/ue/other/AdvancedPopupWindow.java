// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import android.annotation.TargetApi;
import android.view.View$OnLayoutChangeListener;
import android.os.Build$VERSION;
import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

public abstract class AdvancedPopupWindow extends PopupWindow
{
    protected View anchor;
    protected int gravity;
    protected Context mContext;
    protected LayoutInflater mInflater;
    
    public AdvancedPopupWindow(final Context mContext) {
        super(mContext);
        this.mContext = mContext;
        this.mInflater = (LayoutInflater)mContext.getSystemService("layout_inflater");
        this.setWidth(-2);
        this.setHeight(-2);
        this.setBackgroundDrawable((Drawable)null);
    }
    
    protected abstract View buildContentView();
    
    protected int[] calculateXYOffset(int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7) {
        int n8 = 0;
        final int n9 = 0;
        if ((n7 & 0x3) == 0x3) {
            n8 = -n;
        }
        else if ((n7 & 0x5) == 0x5) {
            n8 = n3;
        }
        else if ((n7 & 0x1) == 0x1) {
            n8 = (n3 - n) / 2;
        }
        if ((n7 & 0x30) == 0x30) {
            n = -(n4 + n2);
        }
        else {
            n = n9;
            if ((n7 & 0x50) != 0x50) {
                n = n9;
                if ((n7 & 0x10) == 0x10) {
                    n = -((n4 + n2) / 2);
                }
            }
        }
        return new int[] { n8 + n5, n + n6 };
    }
    
    public View getAnchor() {
        return this.anchor;
    }
    
    public int getGravity() {
        return this.gravity;
    }
    
    public void setContentView(final View contentView) {
        contentView.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                return false;
            }
        });
        super.setContentView(contentView);
    }
    
    public void showAsDropDown(final View view, final int n) {
        this.showAsDropDownWithGravity(view, 0, 0, n);
    }
    
    public void showAsDropDown(final View view, final int n, final int n2) {
        if (this.isShowing()) {
            this.dismiss();
        }
        this.setContentView(this.buildContentView());
        super.showAsDropDown(view, n, n2);
    }
    
    @TargetApi(11)
    public void showAsDropDownWithGravity(final View anchor, final int n, final int n2, final int gravity) {
        if (this.isShowing()) {
            this.dismiss();
        }
        this.anchor = anchor;
        this.gravity = gravity;
        final View buildContentView = this.buildContentView();
        this.setContentView(buildContentView);
        final int[] calculateXYOffset = this.calculateXYOffset(this.getContentView().getMeasuredWidth(), this.getContentView().getMeasuredHeight(), anchor.getWidth(), anchor.getHeight(), n, n2, gravity);
        if (Build$VERSION.SDK_INT >= 11) {
            buildContentView.addOnLayoutChangeListener((View$OnLayoutChangeListener)new View$OnLayoutChangeListener() {
                public void onLayoutChange(final View view, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
                    if (n4 != n8 || n3 != n7) {
                        AdvancedPopupWindow.this.updateWithGravity(anchor, n, n2, gravity);
                    }
                }
            });
        }
        super.showAsDropDown(anchor, calculateXYOffset[0], calculateXYOffset[1]);
    }
    
    public void showAtLocation(final View view, final int n, final int n2, final int n3) {
        if (this.isShowing()) {
            this.dismiss();
        }
        this.setContentView(this.buildContentView());
        super.showAtLocation(view, n, n2, n3);
    }
    
    public void updateWithGravity(final View view, final int n, final int n2, final int n3) {
        final int[] calculateXYOffset = this.calculateXYOffset(this.getContentView().getWidth(), this.getContentView().getHeight(), view.getWidth(), view.getHeight(), n, n2, n3);
        super.update(view, calculateXYOffset[0], calculateXYOffset[1], -1, -1);
    }
}
