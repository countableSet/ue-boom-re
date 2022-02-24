// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.views;

import android.util.Log;
import android.graphics.Paint$Style;
import android.graphics.Canvas;
import android.content.res.TypedArray;
import android.util.TypedValue;
import com.logitech.ue.R;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class BatteryIndicatorView extends View
{
    private final String TAG;
    private RectF bodyRect;
    private RectF fillRect;
    private int mBatteryLevel;
    private Paint mPaint;
    private float mStrokeWidth;
    private float roundRadius;
    private float tailHeight;
    private float tailWidth;
    
    public BatteryIndicatorView(final Context context) {
        this(context, null, 0);
    }
    
    public BatteryIndicatorView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public BatteryIndicatorView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.TAG = this.getClass().getSimpleName();
        this.mStrokeWidth = 2.0f;
        this.mPaint = new Paint();
        this.mBatteryLevel = 0;
        this.bodyRect = new RectF();
        this.fillRect = new RectF();
        final TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(set, R.styleable.BatteryIndicatorView, 0, 0);
        this.setBatteryLevel(obtainStyledAttributes.getInt(0, 15));
        this.mStrokeWidth = obtainStyledAttributes.getDimension(1, TypedValue.applyDimension(1, 2.0f, this.getResources().getDisplayMetrics()));
        this.tailHeight = TypedValue.applyDimension(1, 6.0f, this.getResources().getDisplayMetrics());
        this.tailWidth = TypedValue.applyDimension(1, 3.0f, this.getResources().getDisplayMetrics());
        this.roundRadius = TypedValue.applyDimension(1, 2.0f, this.getResources().getDisplayMetrics());
        obtainStyledAttributes.recycle();
    }
    
    public int getBatteryLevel() {
        return this.mBatteryLevel;
    }
    
    public void onDraw(final Canvas canvas) {
        if (this.mBatteryLevel != -1) {
            this.bodyRect.left = this.mStrokeWidth / 2.0f + this.tailWidth;
            this.bodyRect.top = this.mStrokeWidth / 2.0f;
            this.bodyRect.right = this.getWidth() - this.bodyRect.left - this.tailWidth;
            this.bodyRect.bottom = this.getHeight() - this.bodyRect.top;
            this.mPaint.setAntiAlias(true);
            this.mPaint.setColor(-1);
            this.mPaint.setStyle(Paint$Style.FILL);
            if (this.mBatteryLevel < 20) {
                Log.d(this.TAG, "Battery level is LOW");
                this.mBatteryLevel = 19;
                this.mPaint.setColor(-65536);
            }
            this.fillRect.left = this.bodyRect.left;
            this.fillRect.top = this.bodyRect.top;
            this.fillRect.right = this.bodyRect.left + this.mBatteryLevel / 100.0f * (this.bodyRect.right - this.bodyRect.left);
            this.fillRect.bottom = this.bodyRect.bottom;
            canvas.drawRect(this.fillRect, this.mPaint);
            this.mPaint.setColor(-1);
            this.mPaint.setStyle(Paint$Style.STROKE);
            this.mPaint.setStrokeWidth(this.mStrokeWidth);
            canvas.drawRoundRect(this.bodyRect, this.roundRadius, this.roundRadius, this.mPaint);
            this.mPaint.setStyle(Paint$Style.FILL);
            canvas.drawRect(this.bodyRect.right, (this.bodyRect.height() - this.tailHeight) / 2.0f + this.bodyRect.top, this.tailWidth + this.bodyRect.right, (this.bodyRect.height() + this.tailHeight) / 2.0f + this.bodyRect.top, this.mPaint);
        }
    }
    
    public void setBatteryLevel(final int mBatteryLevel) {
        this.mBatteryLevel = mBatteryLevel;
        this.invalidate();
    }
}
