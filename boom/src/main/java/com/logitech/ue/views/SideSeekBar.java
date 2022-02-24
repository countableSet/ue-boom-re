// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.views;

import android.graphics.Canvas;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import android.widget.SeekBar;

public class SideSeekBar extends SeekBar
{
    private int mBackColor;
    private int mFrontColor;
    private Paint mPaint;
    private int mStrokeWidth;
    private Drawable mThumb;
    
    public SideSeekBar(final Context context) {
        super(context);
        this.mPaint = new Paint();
        this.mFrontColor = -1;
        this.mBackColor = -1;
        this.mStrokeWidth = 4;
        this.mBackColor = this.getResources().getColor(2131558421);
    }
    
    public SideSeekBar(final Context context, final AttributeSet set) {
        super(context, set);
        this.mPaint = new Paint();
        this.mFrontColor = -1;
        this.mBackColor = -1;
        this.mStrokeWidth = 4;
        this.mBackColor = this.getResources().getColor(2131558421);
    }
    
    public SideSeekBar(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mPaint = new Paint();
        this.mFrontColor = -1;
        this.mBackColor = -1;
        this.mStrokeWidth = 4;
        this.mBackColor = this.getResources().getColor(2131558421);
    }
    
    public SideSeekBar(final Context context, final AttributeSet set, final int n, final int n2) {
        super(context, set, n, n2);
        this.mPaint = new Paint();
        this.mFrontColor = -1;
        this.mBackColor = -1;
        this.mStrokeWidth = 4;
        this.mBackColor = this.getResources().getColor(2131558421);
    }
    
    void drawThumb(final Canvas canvas) {
        if (this.mThumb != null) {
            canvas.save();
            canvas.translate((float)(this.getPaddingLeft() - this.getThumbOffset()), (float)this.getPaddingTop());
            this.mThumb.draw(canvas);
            canvas.restore();
        }
    }
    
    protected void onDraw(final Canvas canvas) {
        synchronized (this) {
            this.mPaint.setStrokeWidth((float)this.mStrokeWidth);
            this.mPaint.setColor(this.mBackColor);
            canvas.drawLine((float)(this.getPaddingLeft() - this.getThumbOffset()), (float)(this.getHeight() / 2), (float)(this.getWidth() - this.getPaddingRight() + this.getThumbOffset()), (float)(this.getHeight() / 2), this.mPaint);
            canvas.save();
            canvas.translate((float)(this.getPaddingLeft() - this.getThumbOffset()), (float)this.getPaddingTop());
            this.mPaint.setColor(this.mFrontColor);
            canvas.drawLine((float)((this.getWidth() - this.getPaddingLeft() - this.getPaddingRight()) / 2), (float)(this.getHeight() / 2), (float)this.mThumb.getBounds().centerX(), (float)(this.getHeight() / 2), this.mPaint);
            canvas.restore();
            this.drawThumb(canvas);
        }
    }
    
    public void setThumb(final Drawable drawable) {
        super.setThumb(drawable);
        this.mThumb = drawable;
    }
}
