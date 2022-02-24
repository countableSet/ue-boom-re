// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.views;

import android.view.MotionEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.graphics.Canvas;
import android.graphics.MaskFilter;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter$Blur;
import android.graphics.Paint$Cap;
import android.graphics.Paint$Join;
import android.graphics.Paint$Style;
import android.content.res.TypedArray;
import com.logitech.ue.R;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Path;
import android.graphics.Paint;
import android.view.View;

public class CircularSeekBar extends View
{
    private static final int DEFAULT_CIRCLE_COLOR = -12303292;
    private static final int DEFAULT_CIRCLE_FILL_COLOR = 0;
    private static final int DEFAULT_CIRCLE_PROGRESS_COLOR;
    private static final float DEFAULT_CIRCLE_STROKE_WIDTH = 5.0f;
    private static final float DEFAULT_CIRCLE_X_RADIUS = 30.0f;
    private static final float DEFAULT_CIRCLE_Y_RADIUS = 30.0f;
    private static final float DEFAULT_END_ANGLE = 270.0f;
    private static final boolean DEFAULT_LOCK_ENABLED = true;
    private static final boolean DEFAULT_MAINTAIN_EQUAL_CIRCLE = true;
    private static final int DEFAULT_MAX = 100;
    private static final boolean DEFAULT_MOVE_OUTSIDE_CIRCLE = false;
    private static final int DEFAULT_POINTER_ALPHA = 135;
    private static final int DEFAULT_POINTER_ALPHA_ONTOUCH = 100;
    private static final int DEFAULT_POINTER_COLOR;
    private static final float DEFAULT_POINTER_HALO_BORDER_WIDTH = 2.0f;
    private static final int DEFAULT_POINTER_HALO_COLOR;
    private static final int DEFAULT_POINTER_HALO_COLOR_ONTOUCH;
    private static final float DEFAULT_POINTER_HALO_WIDTH = 6.0f;
    private static final float DEFAULT_POINTER_RADIUS = 7.0f;
    private static final int DEFAULT_PROGRESS = 0;
    private static final float DEFAULT_START_ANGLE = 270.0f;
    private static final boolean DEFAULT_USE_CUSTOM_RADII = false;
    private final float DPTOPX_SCALE;
    private final float MIN_TOUCH_TARGET_DP;
    private float ccwDistanceFromEnd;
    private float ccwDistanceFromPointer;
    private float ccwDistanceFromStart;
    private float cwDistanceFromEnd;
    private float cwDistanceFromPointer;
    private float cwDistanceFromStart;
    private float lastCWDistanceFromStart;
    private boolean lockAtEnd;
    private boolean lockAtStart;
    private boolean lockEnabled;
    private int mCircleColor;
    private int mCircleFillColor;
    private Paint mCircleFillPaint;
    private float mCircleHeight;
    private Paint mCirclePaint;
    private Path mCirclePath;
    private int mCircleProgressColor;
    private Paint mCircleProgressGlowPaint;
    private Paint mCircleProgressPaint;
    private Path mCircleProgressPath;
    private RectF mCircleRectF;
    private float mCircleStrokeWidth;
    private float mCircleWidth;
    private float mCircleXRadius;
    private float mCircleYRadius;
    private boolean mCustomRadii;
    private float mEndAngle;
    private boolean mIsMovingCW;
    private boolean mMaintainEqualCircle;
    private int mMax;
    private boolean mMoveOutsideCircle;
    private OnCircularSeekBarChangeListener mOnCircularSeekBarChangeListener;
    private int mPointerAlpha;
    private int mPointerAlphaOnTouch;
    private int mPointerColor;
    private Paint mPointerHaloBorderPaint;
    private float mPointerHaloBorderWidth;
    private int mPointerHaloColor;
    private int mPointerHaloColorOnTouch;
    private Paint mPointerHaloPaint;
    private float mPointerHaloWidth;
    private Paint mPointerPaint;
    private float mPointerPosition;
    private float[] mPointerPositionXY;
    private float mPointerRadius;
    private int mProgress;
    private float mProgressDegrees;
    private float mStartAngle;
    private float mTotalCircleDegrees;
    private boolean mUserIsMovingPointer;
    
    static {
        DEFAULT_CIRCLE_PROGRESS_COLOR = Color.argb(235, 74, 138, 255);
        DEFAULT_POINTER_COLOR = Color.argb(235, 74, 138, 255);
        DEFAULT_POINTER_HALO_COLOR = Color.argb(135, 74, 138, 255);
        DEFAULT_POINTER_HALO_COLOR_ONTOUCH = Color.argb(135, 74, 138, 255);
    }
    
    public CircularSeekBar(final Context context) {
        super(context);
        this.DPTOPX_SCALE = this.getResources().getDisplayMetrics().density;
        this.MIN_TOUCH_TARGET_DP = 48.0f;
        this.mCircleRectF = new RectF();
        this.mPointerColor = CircularSeekBar.DEFAULT_POINTER_COLOR;
        this.mPointerHaloColor = CircularSeekBar.DEFAULT_POINTER_HALO_COLOR;
        this.mPointerHaloColorOnTouch = CircularSeekBar.DEFAULT_POINTER_HALO_COLOR_ONTOUCH;
        this.mCircleColor = -12303292;
        this.mCircleFillColor = 0;
        this.mCircleProgressColor = CircularSeekBar.DEFAULT_CIRCLE_PROGRESS_COLOR;
        this.mPointerAlpha = 135;
        this.mPointerAlphaOnTouch = 100;
        this.lockEnabled = true;
        this.lockAtStart = true;
        this.lockAtEnd = false;
        this.mUserIsMovingPointer = false;
        this.mPointerPositionXY = new float[2];
        this.init(null, 0);
    }
    
    public CircularSeekBar(final Context context, final AttributeSet set) {
        super(context, set);
        this.DPTOPX_SCALE = this.getResources().getDisplayMetrics().density;
        this.MIN_TOUCH_TARGET_DP = 48.0f;
        this.mCircleRectF = new RectF();
        this.mPointerColor = CircularSeekBar.DEFAULT_POINTER_COLOR;
        this.mPointerHaloColor = CircularSeekBar.DEFAULT_POINTER_HALO_COLOR;
        this.mPointerHaloColorOnTouch = CircularSeekBar.DEFAULT_POINTER_HALO_COLOR_ONTOUCH;
        this.mCircleColor = -12303292;
        this.mCircleFillColor = 0;
        this.mCircleProgressColor = CircularSeekBar.DEFAULT_CIRCLE_PROGRESS_COLOR;
        this.mPointerAlpha = 135;
        this.mPointerAlphaOnTouch = 100;
        this.lockEnabled = true;
        this.lockAtStart = true;
        this.lockAtEnd = false;
        this.mUserIsMovingPointer = false;
        this.mPointerPositionXY = new float[2];
        this.init(set, 0);
    }
    
    public CircularSeekBar(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.DPTOPX_SCALE = this.getResources().getDisplayMetrics().density;
        this.MIN_TOUCH_TARGET_DP = 48.0f;
        this.mCircleRectF = new RectF();
        this.mPointerColor = CircularSeekBar.DEFAULT_POINTER_COLOR;
        this.mPointerHaloColor = CircularSeekBar.DEFAULT_POINTER_HALO_COLOR;
        this.mPointerHaloColorOnTouch = CircularSeekBar.DEFAULT_POINTER_HALO_COLOR_ONTOUCH;
        this.mCircleColor = -12303292;
        this.mCircleFillColor = 0;
        this.mCircleProgressColor = CircularSeekBar.DEFAULT_CIRCLE_PROGRESS_COLOR;
        this.mPointerAlpha = 135;
        this.mPointerAlphaOnTouch = 100;
        this.lockEnabled = true;
        this.lockAtStart = true;
        this.lockAtEnd = false;
        this.mUserIsMovingPointer = false;
        this.mPointerPositionXY = new float[2];
        this.init(set, n);
    }
    
    private void calculatePointerAngle() {
        this.mPointerPosition = this.mTotalCircleDegrees * (this.mProgress / (float)this.mMax) + this.mStartAngle;
        this.mPointerPosition %= 360.0f;
    }
    
    private void calculatePointerXYPosition() {
        final PathMeasure pathMeasure = new PathMeasure(this.mCircleProgressPath, false);
        if (!pathMeasure.getPosTan(pathMeasure.getLength(), this.mPointerPositionXY, (float[])null)) {
            new PathMeasure(this.mCirclePath, false).getPosTan(0.0f, this.mPointerPositionXY, (float[])null);
        }
    }
    
    private void calculateProgressDegrees() {
        this.mProgressDegrees = this.mPointerPosition - this.mStartAngle;
        float mProgressDegrees;
        if (this.mProgressDegrees < 0.0f) {
            mProgressDegrees = 360.0f + this.mProgressDegrees;
        }
        else {
            mProgressDegrees = this.mProgressDegrees;
        }
        this.mProgressDegrees = mProgressDegrees;
    }
    
    private void calculateTotalDegrees() {
        this.mTotalCircleDegrees = (360.0f - (this.mStartAngle - this.mEndAngle)) % 360.0f;
        if (this.mTotalCircleDegrees <= 0.0f) {
            this.mTotalCircleDegrees = 360.0f;
        }
    }
    
    private void init(final AttributeSet set, final int n) {
        final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(set, R.styleable.CircularSeekBar, n, 0);
        this.initAttributes(obtainStyledAttributes);
        obtainStyledAttributes.recycle();
        this.initPaints();
    }
    
    private void initAttributes(final TypedArray typedArray) {
        this.mCircleXRadius = typedArray.getFloat(7, 30.0f) * this.DPTOPX_SCALE;
        this.mCircleYRadius = typedArray.getFloat(8, 30.0f) * this.DPTOPX_SCALE;
        this.mPointerRadius = typedArray.getFloat(10, 7.0f) * this.DPTOPX_SCALE;
        this.mPointerHaloWidth = typedArray.getFloat(11, 6.0f) * this.DPTOPX_SCALE;
        this.mPointerHaloBorderWidth = typedArray.getFloat(12, 2.0f) * this.DPTOPX_SCALE;
        this.mCircleStrokeWidth = typedArray.getFloat(9, 5.0f) * this.DPTOPX_SCALE;
        this.mPointerColor = typedArray.getColor(15, CircularSeekBar.DEFAULT_POINTER_COLOR);
        this.mPointerHaloColor = typedArray.getColor(16, CircularSeekBar.DEFAULT_POINTER_HALO_COLOR);
        this.mPointerHaloColorOnTouch = typedArray.getColor(17, CircularSeekBar.DEFAULT_POINTER_HALO_COLOR_ONTOUCH);
        this.mCircleColor = typedArray.getColor(13, -12303292);
        this.mCircleProgressColor = typedArray.getColor(14, CircularSeekBar.DEFAULT_CIRCLE_PROGRESS_COLOR);
        this.mCircleFillColor = typedArray.getColor(21, 0);
        this.mPointerAlpha = Color.alpha(this.mPointerHaloColor);
        this.mPointerAlphaOnTouch = typedArray.getInt(18, 100);
        if (this.mPointerAlphaOnTouch > 255 || this.mPointerAlphaOnTouch < 0) {
            this.mPointerAlphaOnTouch = 100;
        }
        this.mMax = typedArray.getInt(2, 100);
        this.mProgress = typedArray.getInt(0, 0);
        this.mCustomRadii = typedArray.getBoolean(5, false);
        this.mMaintainEqualCircle = typedArray.getBoolean(4, true);
        this.mMoveOutsideCircle = typedArray.getBoolean(3, false);
        this.lockEnabled = typedArray.getBoolean(6, true);
        this.mStartAngle = (typedArray.getFloat(19, 270.0f) % 360.0f + 360.0f) % 360.0f;
        this.mEndAngle = (typedArray.getFloat(20, 270.0f) % 360.0f + 360.0f) % 360.0f;
        if (this.mStartAngle == this.mEndAngle) {
            this.mEndAngle -= 0.1f;
        }
    }
    
    private void initPaints() {
        (this.mCirclePaint = new Paint()).setAntiAlias(true);
        this.mCirclePaint.setDither(true);
        this.mCirclePaint.setColor(this.mCircleColor);
        this.mCirclePaint.setStrokeWidth(this.mCircleStrokeWidth);
        this.mCirclePaint.setStyle(Paint$Style.STROKE);
        this.mCirclePaint.setStrokeJoin(Paint$Join.ROUND);
        this.mCirclePaint.setStrokeCap(Paint$Cap.ROUND);
        (this.mCircleFillPaint = new Paint()).setAntiAlias(true);
        this.mCircleFillPaint.setDither(true);
        this.mCircleFillPaint.setColor(this.mCircleFillColor);
        this.mCircleFillPaint.setStyle(Paint$Style.FILL);
        (this.mCircleProgressPaint = new Paint()).setAntiAlias(true);
        this.mCircleProgressPaint.setDither(true);
        this.mCircleProgressPaint.setColor(this.mCircleProgressColor);
        this.mCircleProgressPaint.setStrokeWidth(this.mCircleStrokeWidth);
        this.mCircleProgressPaint.setStyle(Paint$Style.STROKE);
        this.mCircleProgressPaint.setStrokeJoin(Paint$Join.ROUND);
        this.mCircleProgressPaint.setStrokeCap(Paint$Cap.ROUND);
        (this.mCircleProgressGlowPaint = new Paint()).set(this.mCircleProgressPaint);
        this.mCircleProgressGlowPaint.setMaskFilter((MaskFilter)new BlurMaskFilter(5.0f * this.DPTOPX_SCALE, BlurMaskFilter$Blur.NORMAL));
        (this.mPointerPaint = new Paint()).setAntiAlias(true);
        this.mPointerPaint.setDither(true);
        this.mPointerPaint.setStyle(Paint$Style.FILL);
        this.mPointerPaint.setColor(this.mPointerColor);
        this.mPointerPaint.setStrokeWidth(this.mPointerRadius);
        (this.mPointerHaloPaint = new Paint()).set(this.mPointerPaint);
        this.mPointerHaloPaint.setColor(this.mPointerHaloColor);
        this.mPointerHaloPaint.setAlpha(this.mPointerAlpha);
        this.mPointerHaloPaint.setStrokeWidth(this.mPointerRadius + this.mPointerHaloWidth);
        (this.mPointerHaloBorderPaint = new Paint()).set(this.mPointerPaint);
        this.mPointerHaloBorderPaint.setStrokeWidth(this.mPointerHaloBorderWidth);
        this.mPointerHaloBorderPaint.setStyle(Paint$Style.STROKE);
    }
    
    private void initPaths() {
        (this.mCirclePath = new Path()).addArc(this.mCircleRectF, this.mStartAngle, this.mTotalCircleDegrees);
        (this.mCircleProgressPath = new Path()).addArc(this.mCircleRectF, this.mStartAngle, this.mProgressDegrees);
    }
    
    private void initRects() {
        this.mCircleRectF.set(-this.mCircleWidth, -this.mCircleHeight, this.mCircleWidth, this.mCircleHeight);
    }
    
    private void recalculateAll() {
        this.calculateTotalDegrees();
        this.calculatePointerAngle();
        this.calculateProgressDegrees();
        this.initRects();
        this.initPaths();
        this.calculatePointerXYPosition();
    }
    
    private void setProgressBasedOnAngle(final float mPointerPosition) {
        this.mPointerPosition = mPointerPosition;
        this.calculateProgressDegrees();
        this.mProgress = Math.round(this.mMax * this.mProgressDegrees / this.mTotalCircleDegrees);
    }
    
    public int getCircleColor() {
        return this.mCircleColor;
    }
    
    public int getCircleFillColor() {
        return this.mCircleFillColor;
    }
    
    public int getCircleProgressColor() {
        return this.mCircleProgressColor;
    }
    
    public int getMax() {
        synchronized (this) {
            return this.mMax;
        }
    }
    
    public int getPointerAlpha() {
        return this.mPointerAlpha;
    }
    
    public int getPointerAlphaOnTouch() {
        return this.mPointerAlphaOnTouch;
    }
    
    public int getPointerColor() {
        return this.mPointerColor;
    }
    
    public int getPointerHaloColor() {
        return this.mPointerHaloColor;
    }
    
    public int getProgress() {
        return Math.round(this.mMax * this.mProgressDegrees / this.mTotalCircleDegrees);
    }
    
    public boolean isLockEnabled() {
        return this.lockEnabled;
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate((float)(this.getWidth() / 2), (float)(this.getHeight() / 2));
        canvas.drawPath(this.mCirclePath, this.mCirclePaint);
        canvas.drawPath(this.mCircleProgressPath, this.mCircleProgressGlowPaint);
        canvas.drawPath(this.mCircleProgressPath, this.mCircleProgressPaint);
        canvas.drawPath(this.mCirclePath, this.mCircleFillPaint);
        canvas.drawCircle(this.mPointerPositionXY[0], this.mPointerPositionXY[1], this.mPointerRadius + this.mPointerHaloWidth, this.mPointerHaloPaint);
        canvas.drawCircle(this.mPointerPositionXY[0], this.mPointerPositionXY[1], this.mPointerRadius, this.mPointerPaint);
        if (this.mUserIsMovingPointer) {
            canvas.drawCircle(this.mPointerPositionXY[0], this.mPointerPositionXY[1], this.mPointerRadius + this.mPointerHaloWidth + this.mPointerHaloBorderWidth / 2.0f, this.mPointerHaloBorderPaint);
        }
    }
    
    protected void onMeasure(int min, int defaultSize) {
        defaultSize = getDefaultSize(this.getSuggestedMinimumHeight(), defaultSize);
        final int defaultSize2 = getDefaultSize(this.getSuggestedMinimumWidth(), min);
        if (this.mMaintainEqualCircle) {
            min = Math.min(defaultSize2, defaultSize);
            this.setMeasuredDimension(min, min);
        }
        else {
            this.setMeasuredDimension(defaultSize2, defaultSize);
        }
        this.mCircleHeight = defaultSize / 2.0f - this.mCircleStrokeWidth - this.mPointerRadius - this.mPointerHaloBorderWidth * 1.5f;
        this.mCircleWidth = defaultSize2 / 2.0f - this.mCircleStrokeWidth - this.mPointerRadius - this.mPointerHaloBorderWidth * 1.5f;
        if (this.mCustomRadii) {
            if (this.mCircleYRadius - this.mCircleStrokeWidth - this.mPointerRadius - this.mPointerHaloBorderWidth < this.mCircleHeight) {
                this.mCircleHeight = this.mCircleYRadius - this.mCircleStrokeWidth - this.mPointerRadius - this.mPointerHaloBorderWidth * 1.5f;
            }
            if (this.mCircleXRadius - this.mCircleStrokeWidth - this.mPointerRadius - this.mPointerHaloBorderWidth < this.mCircleWidth) {
                this.mCircleWidth = this.mCircleXRadius - this.mCircleStrokeWidth - this.mPointerRadius - this.mPointerHaloBorderWidth * 1.5f;
            }
        }
        if (this.mMaintainEqualCircle) {
            final float min2 = Math.min(this.mCircleHeight, this.mCircleWidth);
            this.mCircleHeight = min2;
            this.mCircleWidth = min2;
        }
        this.recalculateAll();
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        final Bundle bundle = (Bundle)parcelable;
        super.onRestoreInstanceState(bundle.getParcelable("PARENT"));
        this.mMax = bundle.getInt("MAX");
        this.mProgress = bundle.getInt("PROGRESS");
        this.mCircleColor = bundle.getInt("mCircleColor");
        this.mCircleProgressColor = bundle.getInt("mCircleProgressColor");
        this.mPointerColor = bundle.getInt("mPointerColor");
        this.mPointerHaloColor = bundle.getInt("mPointerHaloColor");
        this.mPointerHaloColorOnTouch = bundle.getInt("mPointerHaloColorOnTouch");
        this.mPointerAlpha = bundle.getInt("mPointerAlpha");
        this.mPointerAlphaOnTouch = bundle.getInt("mPointerAlphaOnTouch");
        this.lockEnabled = bundle.getBoolean("lockEnabled");
        this.initPaints();
        this.recalculateAll();
    }
    
    protected Parcelable onSaveInstanceState() {
        final Parcelable onSaveInstanceState = super.onSaveInstanceState();
        final Bundle bundle = new Bundle();
        bundle.putParcelable("PARENT", onSaveInstanceState);
        bundle.putInt("MAX", this.mMax);
        bundle.putInt("PROGRESS", this.mProgress);
        bundle.putInt("mCircleColor", this.mCircleColor);
        bundle.putInt("mCircleProgressColor", this.mCircleProgressColor);
        bundle.putInt("mPointerColor", this.mPointerColor);
        bundle.putInt("mPointerHaloColor", this.mPointerHaloColor);
        bundle.putInt("mPointerHaloColorOnTouch", this.mPointerHaloColorOnTouch);
        bundle.putInt("mPointerAlpha", this.mPointerAlpha);
        bundle.putInt("mPointerAlphaOnTouch", this.mPointerAlphaOnTouch);
        bundle.putBoolean("lockEnabled", this.lockEnabled);
        return (Parcelable)bundle;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final float n = motionEvent.getX() - this.getWidth() / 2;
        final float n2 = motionEvent.getY() - this.getHeight() / 2;
        final float n3 = (float)Math.sqrt(Math.pow(this.mCircleRectF.centerX() - n, 2.0) + Math.pow(this.mCircleRectF.centerY() - n2, 2.0));
        final float n4 = 48.0f * this.DPTOPX_SCALE;
        float n5;
        if (this.mCircleStrokeWidth < n4) {
            n5 = n4 / 2.0f;
        }
        else {
            n5 = this.mCircleStrokeWidth / 2.0f;
        }
        final float n6 = Math.max(this.mCircleHeight, this.mCircleWidth) + n5;
        final float n7 = Math.min(this.mCircleHeight, this.mCircleWidth) - n5;
        if (this.mPointerRadius >= n4 / 2.0f) {
            final float mPointerRadius = this.mPointerRadius;
        }
        float n9;
        final float n8 = n9 = (float)(Math.atan2(n2, n) / 3.141592653589793 * 180.0 % 360.0);
        if (n8 < 0.0f) {
            n9 = n8 + 360.0f;
        }
        this.cwDistanceFromStart = n9 - this.mStartAngle;
        float cwDistanceFromStart;
        if (this.cwDistanceFromStart < 0.0f) {
            cwDistanceFromStart = 360.0f + this.cwDistanceFromStart;
        }
        else {
            cwDistanceFromStart = this.cwDistanceFromStart;
        }
        this.cwDistanceFromStart = cwDistanceFromStart;
        this.ccwDistanceFromStart = 360.0f - this.cwDistanceFromStart;
        this.cwDistanceFromEnd = n9 - this.mEndAngle;
        float cwDistanceFromEnd;
        if (this.cwDistanceFromEnd < 0.0f) {
            cwDistanceFromEnd = 360.0f + this.cwDistanceFromEnd;
        }
        else {
            cwDistanceFromEnd = this.cwDistanceFromEnd;
        }
        this.cwDistanceFromEnd = cwDistanceFromEnd;
        this.ccwDistanceFromEnd = 360.0f - this.cwDistanceFromEnd;
        switch (motionEvent.getAction()) {
            case 0: {
                final float n10 = (float)(this.mPointerRadius * 180.0f / (3.141592653589793 * Math.max(this.mCircleHeight, this.mCircleWidth)));
                this.cwDistanceFromPointer = n9 - this.mPointerPosition;
                float cwDistanceFromPointer;
                if (this.cwDistanceFromPointer < 0.0f) {
                    cwDistanceFromPointer = 360.0f + this.cwDistanceFromPointer;
                }
                else {
                    cwDistanceFromPointer = this.cwDistanceFromPointer;
                }
                this.cwDistanceFromPointer = cwDistanceFromPointer;
                this.ccwDistanceFromPointer = 360.0f - this.cwDistanceFromPointer;
                if (n3 >= n7 && n3 <= n6 && (this.cwDistanceFromPointer <= n10 || this.ccwDistanceFromPointer <= n10)) {
                    this.setProgressBasedOnAngle(this.mPointerPosition);
                    this.lastCWDistanceFromStart = this.cwDistanceFromStart;
                    this.mIsMovingCW = true;
                    this.mPointerHaloPaint.setAlpha(this.mPointerAlphaOnTouch);
                    this.mPointerHaloPaint.setColor(this.mPointerHaloColorOnTouch);
                    this.recalculateAll();
                    this.invalidate();
                    if (this.mOnCircularSeekBarChangeListener != null) {
                        this.mOnCircularSeekBarChangeListener.onStartTrackingTouch(this);
                    }
                    this.mUserIsMovingPointer = true;
                    this.lockAtEnd = false;
                    this.lockAtStart = false;
                    break;
                }
                if (this.cwDistanceFromStart > this.mTotalCircleDegrees) {
                    this.mUserIsMovingPointer = false;
                    return false;
                }
                if (n3 >= n7 && n3 <= n6) {
                    this.setProgressBasedOnAngle(n9);
                    this.lastCWDistanceFromStart = this.cwDistanceFromStart;
                    this.mIsMovingCW = true;
                    this.mPointerHaloPaint.setAlpha(this.mPointerAlphaOnTouch);
                    this.mPointerHaloPaint.setColor(this.mPointerHaloColorOnTouch);
                    this.recalculateAll();
                    this.invalidate();
                    if (this.mOnCircularSeekBarChangeListener != null) {
                        this.mOnCircularSeekBarChangeListener.onStartTrackingTouch(this);
                        this.mOnCircularSeekBarChangeListener.onProgressChanged(this, this.mProgress, true);
                    }
                    this.mUserIsMovingPointer = true;
                    this.lockAtEnd = false;
                    this.lockAtStart = false;
                    break;
                }
                this.mUserIsMovingPointer = false;
                return false;
            }
            case 2: {
                if (this.mUserIsMovingPointer) {
                    if (this.lastCWDistanceFromStart < this.cwDistanceFromStart) {
                        if (this.cwDistanceFromStart - this.lastCWDistanceFromStart > 180.0f && !this.mIsMovingCW) {
                            this.lockAtStart = true;
                            this.lockAtEnd = false;
                        }
                        else {
                            this.mIsMovingCW = true;
                        }
                    }
                    else if (this.lastCWDistanceFromStart - this.cwDistanceFromStart > 180.0f && this.mIsMovingCW) {
                        this.lockAtEnd = true;
                        this.lockAtStart = false;
                    }
                    else {
                        this.mIsMovingCW = false;
                    }
                    if (this.lockAtStart && this.mIsMovingCW) {
                        this.lockAtStart = false;
                    }
                    if (this.lockAtEnd && !this.mIsMovingCW) {
                        this.lockAtEnd = false;
                    }
                    if (this.lockAtStart && !this.mIsMovingCW && this.ccwDistanceFromStart > 90.0f) {
                        this.lockAtStart = false;
                    }
                    if (this.lockAtEnd && this.mIsMovingCW && this.cwDistanceFromEnd > 90.0f) {
                        this.lockAtEnd = false;
                    }
                    if (!this.lockAtEnd && this.cwDistanceFromStart > this.mTotalCircleDegrees && this.mIsMovingCW && this.lastCWDistanceFromStart < this.mTotalCircleDegrees) {
                        this.lockAtEnd = true;
                    }
                    if (this.lockAtStart && this.lockEnabled) {
                        this.mProgress = 0;
                        this.recalculateAll();
                        this.invalidate();
                        if (this.mOnCircularSeekBarChangeListener != null) {
                            this.mOnCircularSeekBarChangeListener.onProgressChanged(this, this.mProgress, true);
                        }
                    }
                    else if (this.lockAtEnd && this.lockEnabled) {
                        this.mProgress = this.mMax;
                        this.recalculateAll();
                        this.invalidate();
                        if (this.mOnCircularSeekBarChangeListener != null) {
                            this.mOnCircularSeekBarChangeListener.onProgressChanged(this, this.mProgress, true);
                        }
                    }
                    else {
                        if (!this.mMoveOutsideCircle && n3 > n6) {
                            break;
                        }
                        if (this.cwDistanceFromStart <= this.mTotalCircleDegrees) {
                            this.setProgressBasedOnAngle(n9);
                        }
                        this.recalculateAll();
                        this.invalidate();
                        if (this.mOnCircularSeekBarChangeListener != null) {
                            this.mOnCircularSeekBarChangeListener.onProgressChanged(this, this.mProgress, true);
                        }
                    }
                    this.lastCWDistanceFromStart = this.cwDistanceFromStart;
                    break;
                }
                return false;
            }
            case 1: {
                this.mPointerHaloPaint.setAlpha(this.mPointerAlpha);
                this.mPointerHaloPaint.setColor(this.mPointerHaloColor);
                if (!this.mUserIsMovingPointer) {
                    return false;
                }
                this.mUserIsMovingPointer = false;
                this.invalidate();
                if (this.mOnCircularSeekBarChangeListener != null) {
                    this.mOnCircularSeekBarChangeListener.onStopTrackingTouch(this);
                    break;
                }
                break;
            }
            case 3: {
                this.mPointerHaloPaint.setAlpha(this.mPointerAlpha);
                this.mPointerHaloPaint.setColor(this.mPointerHaloColor);
                this.mUserIsMovingPointer = false;
                this.invalidate();
                break;
            }
        }
        if (motionEvent.getAction() == 2 && this.getParent() != null) {
            this.getParent().requestDisallowInterceptTouchEvent(true);
        }
        return true;
    }
    
    public void setCircleColor(final int mCircleColor) {
        this.mCircleColor = mCircleColor;
        this.mCirclePaint.setColor(this.mCircleColor);
        this.invalidate();
    }
    
    public void setCircleFillColor(final int mCircleFillColor) {
        this.mCircleFillColor = mCircleFillColor;
        this.mCircleFillPaint.setColor(this.mCircleFillColor);
        this.invalidate();
    }
    
    public void setCircleProgressColor(final int mCircleProgressColor) {
        this.mCircleProgressColor = mCircleProgressColor;
        this.mCircleProgressPaint.setColor(this.mCircleProgressColor);
        this.invalidate();
    }
    
    public void setLockEnabled(final boolean lockEnabled) {
        this.lockEnabled = lockEnabled;
    }
    
    public void setMax(final int mMax) {
        if (mMax > 0) {
            if (mMax <= this.mProgress) {
                this.mProgress = 0;
                if (this.mOnCircularSeekBarChangeListener != null) {
                    this.mOnCircularSeekBarChangeListener.onProgressChanged(this, this.mProgress, false);
                }
            }
            this.mMax = mMax;
            this.recalculateAll();
            this.invalidate();
        }
    }
    
    public void setOnSeekBarChangeListener(final OnCircularSeekBarChangeListener mOnCircularSeekBarChangeListener) {
        this.mOnCircularSeekBarChangeListener = mOnCircularSeekBarChangeListener;
    }
    
    public void setPointerAlpha(final int mPointerAlpha) {
        if (mPointerAlpha >= 0 && mPointerAlpha <= 255) {
            this.mPointerAlpha = mPointerAlpha;
            this.mPointerHaloPaint.setAlpha(this.mPointerAlpha);
            this.invalidate();
        }
    }
    
    public void setPointerAlphaOnTouch(final int mPointerAlphaOnTouch) {
        if (mPointerAlphaOnTouch >= 0 && mPointerAlphaOnTouch <= 255) {
            this.mPointerAlphaOnTouch = mPointerAlphaOnTouch;
        }
    }
    
    public void setPointerColor(final int mPointerColor) {
        this.mPointerColor = mPointerColor;
        this.mPointerPaint.setColor(this.mPointerColor);
        this.invalidate();
    }
    
    public void setPointerHaloColor(final int mPointerHaloColor) {
        this.mPointerHaloColor = mPointerHaloColor;
        this.mPointerHaloPaint.setColor(this.mPointerHaloColor);
        this.invalidate();
    }
    
    public void setProgress(final int mProgress) {
        if (this.mProgress != mProgress) {
            this.mProgress = mProgress;
            if (this.mOnCircularSeekBarChangeListener != null) {
                this.mOnCircularSeekBarChangeListener.onProgressChanged(this, mProgress, false);
            }
            this.recalculateAll();
            this.invalidate();
        }
    }
    
    public interface OnCircularSeekBarChangeListener
    {
        void onProgressChanged(final CircularSeekBar p0, final int p1, final boolean p2);
        
        void onStartTrackingTouch(final CircularSeekBar p0);
        
        void onStopTrackingTouch(final CircularSeekBar p0);
    }
}
