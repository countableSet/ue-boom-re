// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.views;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.content.res.TypedArray;
import android.animation.ObjectAnimator;
import android.graphics.Paint$Cap;
import android.graphics.Paint$Style;
import android.util.TypedValue;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class CircleSpinner extends View
{
    int mBackColor;
    float mBackStrokeWidth;
    RectF mCircleRect;
    int mFrontColor;
    float mFrontStrokeWidth;
    Paint mPaint;
    
    public CircleSpinner(final Context context) {
        this(context, null);
    }
    
    public CircleSpinner(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public CircleSpinner(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mCircleRect = new RectF();
        final TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(2130771969, typedValue, true);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(typedValue.data, new int[] { 2130772145, 2130772146 });
        this.mFrontColor = obtainStyledAttributes.getColor(0, this.getResources().getColor(17170443));
        this.mBackColor = obtainStyledAttributes.getColor(1, this.getResources().getColor(17170432));
        (this.mPaint = new Paint(1)).setStyle(Paint$Style.STROKE);
        this.mPaint.setStrokeCap(Paint$Cap.ROUND);
        obtainStyledAttributes.recycle();
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this, View.ROTATION, new float[] { 0.0f, 360.0f });
        ((ValueAnimator)ofFloat).setRepeatCount(-1);
        ((ValueAnimator)ofFloat).setDuration(600L);
        ((ValueAnimator)ofFloat).start();
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        this.mPaint.setColor(this.mBackColor);
        this.mPaint.setStrokeWidth(this.mBackStrokeWidth);
        canvas.drawArc(this.mCircleRect, 0.0f, 360.0f, false, this.mPaint);
        float n2;
        final float n = n2 = this.getRotation() + 180.0f;
        if (n > 360.0f) {
            n2 = n - 360.0f;
        }
        this.mPaint.setColor(this.mFrontColor);
        this.mPaint.setStrokeWidth(this.mFrontStrokeWidth);
        canvas.drawArc(this.mCircleRect, n2, 90.0f, false, this.mPaint);
    }
    
    protected void onSizeChanged(final int a, int max, final int n, final int n2) {
        super.onSizeChanged(a, max, n, n2);
        max = Math.max(a, max);
        this.mFrontStrokeWidth = (float)(a / 8);
        this.mBackStrokeWidth = this.mFrontStrokeWidth / 2.0f;
        this.mCircleRect = new RectF(this.mFrontStrokeWidth, this.mFrontStrokeWidth, max - this.mFrontStrokeWidth, max - this.mFrontStrokeWidth);
    }
}
