// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.support.v4.view.ViewCompat;
import android.os.Build$VERSION;
import android.view.View$MeasureSpec;
import android.view.View;
import android.content.res.TypedArray;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.LinearLayout;

public class ButtonBarLayout extends LinearLayout
{
    private boolean mAllowStacking;
    private int mLastWidthSize;
    
    public ButtonBarLayout(final Context context, final AttributeSet set) {
        super(context, set);
        this.mLastWidthSize = -1;
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.ButtonBarLayout);
        this.mAllowStacking = obtainStyledAttributes.getBoolean(R.styleable.ButtonBarLayout_allowStacking, false);
        obtainStyledAttributes.recycle();
    }
    
    private boolean isStacked() {
        boolean b = true;
        if (this.getOrientation() != 1) {
            b = false;
        }
        return b;
    }
    
    private void setStacked(final boolean b) {
        int orientation;
        if (b) {
            orientation = 1;
        }
        else {
            orientation = 0;
        }
        this.setOrientation(orientation);
        int gravity;
        if (b) {
            gravity = 5;
        }
        else {
            gravity = 80;
        }
        this.setGravity(gravity);
        final View viewById = this.findViewById(R.id.spacer);
        if (viewById != null) {
            int visibility;
            if (b) {
                visibility = 8;
            }
            else {
                visibility = 4;
            }
            viewById.setVisibility(visibility);
        }
        for (int i = this.getChildCount() - 2; i >= 0; --i) {
            this.bringChildToFront(this.getChildAt(i));
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        final int n3 = 0;
        final int n4 = 0;
        final int size = View$MeasureSpec.getSize(n);
        if (this.mAllowStacking) {
            if (size > this.mLastWidthSize && this.isStacked()) {
                this.setStacked(false);
            }
            this.mLastWidthSize = size;
        }
        boolean b = false;
        int measureSpec;
        if (!this.isStacked() && View$MeasureSpec.getMode(n) == 1073741824) {
            measureSpec = View$MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE);
            b = true;
        }
        else {
            measureSpec = n;
        }
        super.onMeasure(measureSpec, n2);
        boolean b2 = b;
        if (this.mAllowStacking) {
            b2 = b;
            if (!this.isStacked()) {
                int n5;
                if (Build$VERSION.SDK_INT >= 11) {
                    n5 = n4;
                    if ((ViewCompat.getMeasuredWidthAndState((View)this) & 0xFF000000) == 0x1000000) {
                        n5 = 1;
                    }
                }
                else {
                    int n6 = 0;
                    for (int i = 0; i < this.getChildCount(); ++i) {
                        n6 += this.getChildAt(i).getMeasuredWidth();
                    }
                    n5 = n3;
                    if (this.getPaddingLeft() + n6 + this.getPaddingRight() > size) {
                        n5 = 1;
                    }
                }
                b2 = b;
                if (n5 != 0) {
                    this.setStacked(true);
                    b2 = true;
                }
            }
        }
        if (b2) {
            super.onMeasure(n, n2);
        }
    }
    
    public void setAllowStacking(final boolean mAllowStacking) {
        if (this.mAllowStacking != mAllowStacking) {
            this.mAllowStacking = mAllowStacking;
            if (!this.mAllowStacking && this.getOrientation() == 1) {
                this.setStacked(false);
            }
            this.requestLayout();
        }
    }
}
