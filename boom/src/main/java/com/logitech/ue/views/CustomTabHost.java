// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.views;

import android.graphics.Paint$Cap;
import android.graphics.Paint$Style;
import android.graphics.Canvas;
import android.content.res.TypedArray;
import android.util.TypedValue;
import com.logitech.ue.R;
import android.util.AttributeSet;
import android.content.Context;
import java.util.ArrayList;
import android.graphics.Paint;
import android.view.View;

public class CustomTabHost extends View
{
    private int mAccentColor;
    private Paint mPaint;
    private float mSlideProgress;
    private ArrayList<CustomTab> mTabs;
    private float mTextSize;
    private float mUnderlineMargin;
    private float mUnderlineWidth;
    
    public CustomTabHost(final Context context) {
        this(context, null);
    }
    
    public CustomTabHost(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public CustomTabHost(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mTabs = new ArrayList<CustomTab>();
        this.mSlideProgress = 0.0f;
        this.mTextSize = 0.0f;
        this.mAccentColor = -65536;
        this.mPaint = new Paint();
        final TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(set, R.styleable.CustomTabHost, 0, 0);
        this.mUnderlineWidth = obtainStyledAttributes.getDimension(0, TypedValue.applyDimension(1, 4.0f, this.getResources().getDisplayMetrics()));
        this.mUnderlineMargin = obtainStyledAttributes.getDimension(1, TypedValue.applyDimension(1, 8.0f, this.getResources().getDisplayMetrics()));
        this.setSlideProgress(obtainStyledAttributes.getFloat(2, 0.0f));
        this.mTextSize = obtainStyledAttributes.getDimension(3, this.getResources().getDimension(2131361922));
        this.mAccentColor = obtainStyledAttributes.getColor(4, -65536);
        obtainStyledAttributes.recycle();
    }
    
    public void addTab(final CustomTab e) {
        this.mTabs.add(e);
    }
    
    public void clearTabs() {
        this.mTabs.clear();
    }
    
    public float getSlideProgress() {
        return this.mSlideProgress;
    }
    
    public CustomTab getTab(final int index) {
        return this.mTabs.get(index);
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint$Style.STROKE);
        this.mPaint.setStrokeWidth(this.mUnderlineWidth);
        this.mPaint.setColor(this.getResources().getColor(2131558429));
        this.mPaint.setStrokeCap(Paint$Cap.ROUND);
        if (this.mTabs.size() > 0) {
            final int size = this.mTabs.size();
            final float n = (this.getWidth() - this.mUnderlineMargin - this.mUnderlineMargin * (size - 1)) / size;
            for (int i = 0; i < size; ++i) {
                canvas.drawLine(i * n + (this.mUnderlineMargin / 2.0f + this.mUnderlineMargin * i), this.getHeight() - this.mUnderlineWidth / 2.0f, (i + 1) * n + (this.mUnderlineMargin / 2.0f + this.mUnderlineMargin * i), this.getHeight() - this.mUnderlineWidth / 2.0f, this.mPaint);
            }
            if (this.isEnabled()) {
                this.mPaint.setColor(this.mAccentColor);
                final float n2 = (float)Math.floor(this.mSlideProgress);
                float n3;
                if (n2 == (n3 = (float)Math.ceil(this.mSlideProgress))) {
                    n3 = n2 + 1.0f;
                }
                canvas.drawLine(this.mSlideProgress * n + (this.mUnderlineMargin / 2.0f + this.mUnderlineMargin * n2), this.getHeight() - this.mUnderlineWidth / 2.0f, n * n3 + (this.mUnderlineMargin / 2.0f + this.mUnderlineMargin * n2), this.getHeight() - this.mUnderlineWidth / 2.0f, this.mPaint);
                canvas.drawLine(n * n3 + (this.mUnderlineMargin / 2.0f + this.mUnderlineMargin * n3), this.getHeight() - this.mUnderlineWidth / 2.0f, (this.mSlideProgress + 1.0f) * n + (this.mUnderlineMargin / 2.0f + this.mUnderlineMargin * n3), this.getHeight() - this.mUnderlineWidth / 2.0f, this.mPaint);
            }
        }
        else {
            canvas.drawLine(0.0f, this.getHeight() - this.mUnderlineWidth / 2.0f, (float)this.getWidth(), this.getHeight() - this.mUnderlineWidth / 2.0f, this.mPaint);
        }
    }
    
    public void removeTab(final int index) {
        this.mTabs.remove(index);
    }
    
    public void setAccentColor(final int mAccentColor) {
        this.mAccentColor = mAccentColor;
        this.invalidate();
    }
    
    public void setSlideProgress(final float n) {
        float mSlideProgress = n;
        if (n > this.mTabs.size() - 1) {
            mSlideProgress = (float)this.mTabs.size();
        }
        this.mSlideProgress = mSlideProgress;
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
