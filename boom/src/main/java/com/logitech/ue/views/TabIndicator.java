// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.views;

import android.graphics.Paint$Style;
import android.graphics.Canvas;
import android.content.res.TypedArray;
import android.util.TypedValue;
import com.logitech.ue.R;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Paint;
import android.view.View;

public class TabIndicator extends View
{
    private int mAccentColor;
    private Paint mPaint;
    private float mSlideProgress;
    private int mTabsCount;
    private float mUnderlineMargin;
    
    public TabIndicator(final Context context) {
        this(context, null);
    }
    
    public TabIndicator(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public TabIndicator(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mSlideProgress = 0.0f;
        this.mAccentColor = -1;
        this.mPaint = new Paint();
        final TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(set, R.styleable.TabIndicator, 0, 0);
        this.mUnderlineMargin = obtainStyledAttributes.getDimension(2, TypedValue.applyDimension(1, 1.0f, this.getResources().getDisplayMetrics()));
        this.mAccentColor = obtainStyledAttributes.getColor(1, -1);
        this.setSlideProgress(obtainStyledAttributes.getFloat(0, 0.0f));
        obtainStyledAttributes.recycle();
        this.setEnabled(true);
    }
    
    public float getSlideProgress() {
        return this.mSlideProgress;
    }
    
    public int getTabCount() {
        return this.mTabsCount;
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        this.mPaint.setAntiAlias(true);
        final float strokeWidth = (float)this.getHeight();
        this.mPaint.setStyle(Paint$Style.STROKE);
        this.mPaint.setStrokeWidth(strokeWidth);
        this.mPaint.setColor(-7829368);
        if (this.mTabsCount > 0) {
            final float n = (this.getWidth() - this.mUnderlineMargin - this.mUnderlineMargin * (this.mTabsCount - 1)) / this.mTabsCount;
            for (int i = 0; i < this.mTabsCount; ++i) {
                canvas.drawLine(i * n + (this.mUnderlineMargin / 2.0f + this.mUnderlineMargin * i), this.getHeight() - strokeWidth / 2.0f, (i + 1) * n + (this.mUnderlineMargin / 2.0f + this.mUnderlineMargin * i), this.getHeight() - strokeWidth / 2.0f, this.mPaint);
            }
            if (this.isEnabled()) {
                this.mPaint.setColor(this.mAccentColor);
                final float n2 = (float)Math.floor(this.mSlideProgress);
                float n3;
                if (n2 == (n3 = (float)Math.ceil(this.mSlideProgress))) {
                    n3 = n2 + 1.0f;
                }
                canvas.drawLine(this.mSlideProgress * n + (this.mUnderlineMargin / 2.0f + this.mUnderlineMargin * n2), this.getHeight() - strokeWidth / 2.0f, n * n3 + (this.mUnderlineMargin / 2.0f + this.mUnderlineMargin * n2), this.getHeight() - strokeWidth / 2.0f, this.mPaint);
                canvas.drawLine(n * n3 + (this.mUnderlineMargin / 2.0f + this.mUnderlineMargin * n3), this.getHeight() - strokeWidth / 2.0f, (this.mSlideProgress + 1.0f) * n + (this.mUnderlineMargin / 2.0f + this.mUnderlineMargin * n3), this.getHeight() - strokeWidth / 2.0f, this.mPaint);
            }
        }
        else {
            canvas.drawLine(0.0f, this.getHeight() - strokeWidth / 2.0f, (float)this.getWidth(), this.getHeight() - strokeWidth / 2.0f, this.mPaint);
        }
    }
    
    public void setAccentColor(final int mAccentColor) {
        this.mAccentColor = mAccentColor;
        this.invalidate();
    }
    
    public void setSlideProgress(final float n) {
        float mSlideProgress = n;
        if (n > this.mTabsCount) {
            mSlideProgress = (float)this.mTabsCount;
        }
        this.mSlideProgress = mSlideProgress;
        this.invalidate();
    }
    
    public void setTabCount(final int mTabsCount) {
        this.mTabsCount = mTabsCount;
        this.invalidate();
    }
    
    public static class CustomTab
    {
        public String mTitle;
        
        public CustomTab(final String mTitle) {
            this.mTitle = mTitle;
        }
    }
}
