// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.views;

import android.view.MotionEvent;
import android.graphics.Bitmap$Config;
import android.graphics.Canvas;
import android.content.res.TypedArray;
import com.logitech.ue.R;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Rect;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Property;
import android.view.View;

public class VolumeController extends View
{
    public static final Property<VolumeController, Integer> LEVEL;
    public static final Property<VolumeController, Float> LEVEL_ALPHA;
    public static final int MAX_LEVEL = 10000;
    private static final String TAG;
    private OnControlButtonsClickListener mControlClickListener;
    protected Handler mEventHandler;
    public Bitmap mForegroundBitmap;
    private Rect mHelperRect;
    public boolean mInvalidated;
    public int mLevel;
    public float mLevelAlpha;
    public RectF mMinusRect;
    private Paint mPaint;
    public float mPlusMinusPadding;
    public float mPlusMinusSize;
    public float mPlusMinusStroke;
    public RectF mPlusRect;
    public int mPrimaryColor;
    public int mSecondaryColor;
    
    static {
        TAG = VolumeController.class.getSimpleName();
        LEVEL = new Property<VolumeController, Integer>(Integer.class, "level") {
            public Integer get(final VolumeController volumeController) {
                return volumeController.getLevel();
            }
            
            public void set(final VolumeController volumeController, final Integer n) {
                volumeController.setLevel(n);
            }
        };
        LEVEL_ALPHA = new Property<VolumeController, Float>(Float.class, "levelAlpha") {
            public Float get(final VolumeController volumeController) {
                return volumeController.getLevelAlpha();
            }
            
            public void set(final VolumeController volumeController, final Float n) {
                volumeController.setLevelAlpha(n);
            }
        };
    }
    
    public VolumeController(final Context context) {
        this(context, null);
    }
    
    public VolumeController(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public VolumeController(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mPaint = new Paint();
        this.mHelperRect = new Rect();
        this.mEventHandler = new Handler();
        final TypedArray obtainStyledAttributes = this.getContext().getTheme().obtainStyledAttributes(set, R.styleable.VolumeController, n, 0);
        this.mPrimaryColor = obtainStyledAttributes.getColor(0, -1);
        this.mSecondaryColor = obtainStyledAttributes.getColor(1, -16777216);
        this.mLevel = obtainStyledAttributes.getInt(5, 0);
        this.mLevelAlpha = obtainStyledAttributes.getFloat(6, 1.0f);
        this.mPlusMinusStroke = obtainStyledAttributes.getDimension(2, 20.0f);
        this.mPlusMinusSize = obtainStyledAttributes.getDimension(3, 200.0f);
        this.mPlusMinusPadding = obtainStyledAttributes.getDimension(4, 20.0f);
        this.mPaint.setStrokeWidth(this.mPlusMinusStroke);
    }
    
    private void calculatePlusAndMinusPosition(final float n, final float n2, final float n3, final float n4) {
        this.mPlusRect = new RectF(n / 2.0f - n4 / 2.0f, n2 / 2.0f - n4 - n3 / 2.0f, n / 2.0f + n4 / 2.0f, n2 / 2.0f - n3 / 2.0f);
        this.mMinusRect = new RectF(n / 2.0f - n4 / 2.0f, n2 / 2.0f + n3 / 2.0f, n / 2.0f + n4 / 2.0f, n2 / 2.0f + n4 + n3 / 2.0f);
    }
    
    protected void drawMinus(final Canvas canvas, final int color, final Paint paint, final RectF rectF) {
        paint.setColor(color);
        canvas.drawLine(rectF.centerX(), rectF.top, rectF.centerX(), rectF.bottom, paint);
    }
    
    protected void drawPlus(final Canvas canvas, final int color, final Paint paint, final RectF rectF) {
        paint.setColor(color);
        canvas.drawLine(rectF.centerX(), rectF.bottom, rectF.centerX(), this.mPlusMinusStroke / 2.0f + rectF.centerY(), paint);
        canvas.drawLine(rectF.centerX(), rectF.centerY() - this.mPlusMinusStroke / 2.0f, rectF.centerX(), rectF.top, paint);
        canvas.drawLine(rectF.left, rectF.centerY(), rectF.right, rectF.centerY(), paint);
    }
    
    protected void drawPlusAndMinus(final Canvas canvas, final int n, final Paint paint) {
        this.drawPlus(canvas, n, paint, this.mPlusRect);
        this.drawMinus(canvas, n, paint, this.mMinusRect);
    }
    
    public int getLevel() {
        return this.mLevel;
    }
    
    public float getLevelAlpha() {
        return this.mLevelAlpha;
    }
    
    public int getPrimaryColor() {
        return this.mPrimaryColor;
    }
    
    public int getSecondaryColor() {
        return this.mSecondaryColor;
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.mInvalidated) {
            final Canvas canvas2 = new Canvas(this.mForegroundBitmap);
            this.mForegroundBitmap.eraseColor(this.mPrimaryColor);
            this.drawPlusAndMinus(canvas2, this.mSecondaryColor, this.mPaint);
            this.mInvalidated = false;
        }
        canvas.drawColor(this.mSecondaryColor);
        if (this.isEnabled()) {
            this.drawPlusAndMinus(canvas, this.mPrimaryColor, this.mPaint);
        }
        else {
            this.drawPlusAndMinus(canvas, 0x80FFFFFF & this.mPrimaryColor, this.mPaint);
        }
        final int round = Math.round((10000 - this.mLevel) / 10000.0f * this.mForegroundBitmap.getHeight());
        this.mPaint.setAlpha(Math.round(255.0f * this.mLevelAlpha));
        this.mHelperRect.left = 0;
        this.mHelperRect.top = round;
        this.mHelperRect.right = this.mForegroundBitmap.getWidth();
        this.mHelperRect.bottom = this.mForegroundBitmap.getHeight();
        canvas.drawBitmap(this.mForegroundBitmap, this.mHelperRect, this.mHelperRect, this.mPaint);
        this.mPaint.setAlpha(255);
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (this.mForegroundBitmap == null) {
            this.mForegroundBitmap = Bitmap.createBitmap(n, n2, Bitmap$Config.ARGB_8888);
            this.mInvalidated = true;
        }
        else if (this.mForegroundBitmap.getWidth() != n || this.mForegroundBitmap.getHeight() != n2) {
            this.mForegroundBitmap.recycle();
            this.mForegroundBitmap = Bitmap.createBitmap(n, n2, Bitmap$Config.ARGB_8888);
            this.mInvalidated = true;
        }
        this.calculatePlusAndMinusPosition((float)n, (float)n2, this.mPlusMinusPadding, this.mPlusMinusSize);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final boolean b = true;
        if (this.mControlClickListener == null) {
            return super.onTouchEvent(motionEvent);
        }
        final float x = motionEvent.getX();
        final float y = motionEvent.getY();
        boolean onTouchEvent;
        if (motionEvent.getAction() == 0) {
            if (!this.mPlusRect.contains(x, y) && !this.mMinusRect.contains(x, y)) {
                return super.onTouchEvent(motionEvent);
            }
            this.mEventHandler.postDelayed((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (VolumeController.this.mPlusRect.contains(x, y)) {
                        VolumeController.this.mControlClickListener.onPlusButtonHold();
                    }
                    else if (VolumeController.this.mMinusRect.contains(x, y)) {
                        VolumeController.this.mControlClickListener.onMinusButtonHold();
                    }
                    VolumeController.this.mEventHandler.postDelayed((Runnable)this, 200L);
                }
            }, 1000L);
            onTouchEvent = b;
        }
        else if (motionEvent.getAction() == 1) {
            if (this.mPlusRect.contains(x, y)) {
                this.mControlClickListener.onPlusButtonClicked();
            }
            else if (this.mMinusRect.contains(x, y)) {
                this.mControlClickListener.onMinusButtonClicked();
            }
            this.mEventHandler.removeCallbacksAndMessages((Object)null);
            onTouchEvent = b;
        }
        else if (motionEvent.getAction() == 2) {
            onTouchEvent = b;
            if (!this.mPlusRect.contains(x, y)) {
                onTouchEvent = b;
                if (!this.mMinusRect.contains(x, y)) {
                    this.mEventHandler.removeCallbacksAndMessages((Object)null);
                    onTouchEvent = false;
                }
            }
        }
        else {
            if (motionEvent.getAction() != 3) {
                return super.onTouchEvent(motionEvent);
            }
            this.mEventHandler.removeCallbacksAndMessages((Object)null);
            onTouchEvent = b;
        }
        return onTouchEvent;
        onTouchEvent = super.onTouchEvent(motionEvent);
        return onTouchEvent;
    }
    
    public void setEnabled(final boolean enabled) {
        this.mInvalidated = true;
        super.setEnabled(enabled);
    }
    
    public void setLevel(final int mLevel) {
        if (mLevel > 10000) {
            this.mLevel = 10000;
        }
        else if (mLevel < 0) {
            this.mLevel = 0;
        }
        else {
            this.mLevel = mLevel;
        }
        this.invalidate();
    }
    
    public void setLevelAlpha(final float mLevelAlpha) {
        this.mLevelAlpha = mLevelAlpha;
        this.invalidate();
    }
    
    public void setOnControlButtonsClickListener(final OnControlButtonsClickListener mControlClickListener) {
        this.mControlClickListener = mControlClickListener;
    }
    
    public void setPrimaryColor(final int mPrimaryColor) {
        if (mPrimaryColor != this.mPrimaryColor) {
            this.mPrimaryColor = mPrimaryColor;
            this.mInvalidated = true;
            this.invalidate();
        }
    }
    
    public void setSecondaryColor(final int mSecondaryColor) {
        if (mSecondaryColor != this.mSecondaryColor) {
            this.mSecondaryColor = mSecondaryColor;
            this.mInvalidated = true;
            this.invalidate();
        }
    }
    
    public interface OnControlButtonsClickListener
    {
        void onMinusButtonClicked();
        
        void onMinusButtonHold();
        
        void onPlusButtonClicked();
        
        void onPlusButtonHold();
    }
}
