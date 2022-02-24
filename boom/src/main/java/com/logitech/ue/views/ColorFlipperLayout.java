// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.views;

import android.graphics.Bitmap$Config;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup;
import android.content.res.TypedArray;
import com.logitech.ue.R;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.widget.FrameLayout;

public class ColorFlipperLayout extends FrameLayout
{
    public static final int MAX_LEVEL = 10000;
    private Bitmap mForegroundBitmap;
    private Canvas mForegroundCanvas;
    private Rect mHelperRect;
    private int mLevel;
    private Paint mPaint;
    private int mPrimaryColor;
    private int mSecondaryColor;
    
    public ColorFlipperLayout(final Context context) {
        this(context, null);
    }
    
    public ColorFlipperLayout(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public ColorFlipperLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mLevel = 0;
        this.mPrimaryColor = -1;
        this.mSecondaryColor = -16777216;
        this.mPaint = new Paint();
        this.mHelperRect = new Rect();
        this.initAttributes(context, set);
    }
    
    private void initAttributes(final Context context, final AttributeSet set) {
        final TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(set, R.styleable.ColorFlipperLayout, 0, 0);
        this.mPrimaryColor = obtainStyledAttributes.getColor(0, this.mPrimaryColor);
        this.mSecondaryColor = obtainStyledAttributes.getColor(1, this.mSecondaryColor);
        this.mLevel = obtainStyledAttributes.getInt(2, this.mLevel);
        obtainStyledAttributes.recycle();
    }
    
    private void setColorFilterToViewGroup(final ViewGroup viewGroup, final int n) {
        for (int i = 0; i < viewGroup.getChildCount(); ++i) {
            final View child = viewGroup.getChildAt(i);
            if (child instanceof TextView) {
                final TextView textView = (TextView)child;
                textView.setTextColor(n);
                textView.invalidate();
            }
            else if (child instanceof ImageView) {
                ((ImageView)child).setColorFilter(n);
            }
            else if (child instanceof ViewGroup) {
                this.setColorFilterToViewGroup((ViewGroup)child, n);
            }
        }
    }
    
    protected void dispatchDraw(final Canvas canvas) {
        this.mForegroundCanvas.drawColor(this.mSecondaryColor);
        this.setColorFilterToViewGroup((ViewGroup)this, this.mPrimaryColor);
        super.dispatchDraw(this.mForegroundCanvas);
        canvas.drawColor(this.mPrimaryColor);
        this.setColorFilterToViewGroup((ViewGroup)this, this.mSecondaryColor);
        super.dispatchDraw(canvas);
        final int round = Math.round((10000 - this.mLevel) / 10000.0f * canvas.getHeight());
        this.mHelperRect.left = 0;
        this.mHelperRect.top = round;
        this.mHelperRect.right = this.mForegroundBitmap.getWidth();
        this.mHelperRect.bottom = this.mForegroundBitmap.getHeight();
        canvas.drawBitmap(this.mForegroundBitmap, this.mHelperRect, this.mHelperRect, this.mPaint);
    }
    
    public int getLevel() {
        return this.mLevel;
    }
    
    public int getPrimaryColor() {
        return this.mPrimaryColor;
    }
    
    public int getSecondaryColor() {
        return this.mSecondaryColor;
    }
    
    public void invalidate() {
        super.invalidate();
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (this.mForegroundBitmap == null) {
            this.mForegroundBitmap = Bitmap.createBitmap(n, n2, Bitmap$Config.ARGB_8888);
            this.mForegroundCanvas = new Canvas(this.mForegroundBitmap);
        }
        else if (this.mForegroundBitmap.getWidth() != n || this.mForegroundBitmap.getHeight() != n2) {
            this.mForegroundBitmap.recycle();
            this.mForegroundBitmap = Bitmap.createBitmap(n, n2, Bitmap$Config.ARGB_8888);
            this.mForegroundCanvas = new Canvas(this.mForegroundBitmap);
        }
    }
    
    public void setLevel(final int mLevel) {
        this.mLevel = mLevel;
        this.invalidate();
    }
    
    public void setPrimaryColor(final int mPrimaryColor) {
        this.mPrimaryColor = mPrimaryColor;
        this.invalidate();
    }
    
    public void setSecondaryColor(final int mSecondaryColor) {
        this.mSecondaryColor = mSecondaryColor;
        this.invalidate();
    }
}
