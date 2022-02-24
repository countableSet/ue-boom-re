// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.view;

import android.view.GestureDetector;
import android.os.Message;
import android.view.ViewConfiguration;
import android.view.VelocityTracker;
import android.view.GestureDetector$OnDoubleTapListener;
import android.view.MotionEvent;
import android.os.Build$VERSION;
import android.os.Handler;
import android.view.GestureDetector$OnGestureListener;
import android.content.Context;

public final class GestureDetectorCompat
{
    private final GestureDetectorCompatImpl mImpl;
    
    public GestureDetectorCompat(final Context context, final GestureDetector$OnGestureListener gestureDetector$OnGestureListener) {
        this(context, gestureDetector$OnGestureListener, null);
    }
    
    public GestureDetectorCompat(final Context context, final GestureDetector$OnGestureListener gestureDetector$OnGestureListener, final Handler handler) {
        if (Build$VERSION.SDK_INT > 17) {
            this.mImpl = (GestureDetectorCompatImpl)new GestureDetectorCompatImplJellybeanMr2(context, gestureDetector$OnGestureListener, handler);
        }
        else {
            this.mImpl = (GestureDetectorCompatImpl)new GestureDetectorCompatImplBase(context, gestureDetector$OnGestureListener, handler);
        }
    }
    
    public boolean isLongpressEnabled() {
        return this.mImpl.isLongpressEnabled();
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return this.mImpl.onTouchEvent(motionEvent);
    }
    
    public void setIsLongpressEnabled(final boolean isLongpressEnabled) {
        this.mImpl.setIsLongpressEnabled(isLongpressEnabled);
    }
    
    public void setOnDoubleTapListener(final GestureDetector$OnDoubleTapListener onDoubleTapListener) {
        this.mImpl.setOnDoubleTapListener(onDoubleTapListener);
    }
    
    interface GestureDetectorCompatImpl
    {
        boolean isLongpressEnabled();
        
        boolean onTouchEvent(final MotionEvent p0);
        
        void setIsLongpressEnabled(final boolean p0);
        
        void setOnDoubleTapListener(final GestureDetector$OnDoubleTapListener p0);
    }
    
    static class GestureDetectorCompatImplBase implements GestureDetectorCompatImpl
    {
        private static final int DOUBLE_TAP_TIMEOUT;
        private static final int LONGPRESS_TIMEOUT;
        private static final int LONG_PRESS = 2;
        private static final int SHOW_PRESS = 1;
        private static final int TAP = 3;
        private static final int TAP_TIMEOUT;
        private boolean mAlwaysInBiggerTapRegion;
        private boolean mAlwaysInTapRegion;
        MotionEvent mCurrentDownEvent;
        boolean mDeferConfirmSingleTap;
        GestureDetector$OnDoubleTapListener mDoubleTapListener;
        private int mDoubleTapSlopSquare;
        private float mDownFocusX;
        private float mDownFocusY;
        private final Handler mHandler;
        private boolean mInLongPress;
        private boolean mIsDoubleTapping;
        private boolean mIsLongpressEnabled;
        private float mLastFocusX;
        private float mLastFocusY;
        final GestureDetector$OnGestureListener mListener;
        private int mMaximumFlingVelocity;
        private int mMinimumFlingVelocity;
        private MotionEvent mPreviousUpEvent;
        boolean mStillDown;
        private int mTouchSlopSquare;
        private VelocityTracker mVelocityTracker;
        
        static {
            LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
            TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
            DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
        }
        
        public GestureDetectorCompatImplBase(final Context context, final GestureDetector$OnGestureListener mListener, final Handler handler) {
            if (handler != null) {
                this.mHandler = new GestureHandler(handler);
            }
            else {
                this.mHandler = new GestureHandler();
            }
            this.mListener = mListener;
            if (mListener instanceof GestureDetector$OnDoubleTapListener) {
                this.setOnDoubleTapListener((GestureDetector$OnDoubleTapListener)mListener);
            }
            this.init(context);
        }
        
        private void cancel() {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
            this.mIsDoubleTapping = false;
            this.mStillDown = false;
            this.mAlwaysInTapRegion = false;
            this.mAlwaysInBiggerTapRegion = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mInLongPress) {
                this.mInLongPress = false;
            }
        }
        
        private void cancelTaps() {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mIsDoubleTapping = false;
            this.mAlwaysInTapRegion = false;
            this.mAlwaysInBiggerTapRegion = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mInLongPress) {
                this.mInLongPress = false;
            }
        }
        
        private void init(final Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null");
            }
            if (this.mListener == null) {
                throw new IllegalArgumentException("OnGestureListener must not be null");
            }
            this.mIsLongpressEnabled = true;
            final ViewConfiguration value = ViewConfiguration.get(context);
            final int scaledTouchSlop = value.getScaledTouchSlop();
            final int scaledDoubleTapSlop = value.getScaledDoubleTapSlop();
            this.mMinimumFlingVelocity = value.getScaledMinimumFlingVelocity();
            this.mMaximumFlingVelocity = value.getScaledMaximumFlingVelocity();
            this.mTouchSlopSquare = scaledTouchSlop * scaledTouchSlop;
            this.mDoubleTapSlopSquare = scaledDoubleTapSlop * scaledDoubleTapSlop;
        }
        
        private boolean isConsideredDoubleTap(final MotionEvent motionEvent, final MotionEvent motionEvent2, final MotionEvent motionEvent3) {
            final boolean b = false;
            boolean b2;
            if (!this.mAlwaysInBiggerTapRegion) {
                b2 = b;
            }
            else {
                b2 = b;
                if (motionEvent3.getEventTime() - motionEvent2.getEventTime() <= GestureDetectorCompatImplBase.DOUBLE_TAP_TIMEOUT) {
                    final int n = (int)motionEvent.getX() - (int)motionEvent3.getX();
                    final int n2 = (int)motionEvent.getY() - (int)motionEvent3.getY();
                    b2 = b;
                    if (n * n + n2 * n2 < this.mDoubleTapSlopSquare) {
                        b2 = true;
                    }
                }
            }
            return b2;
        }
        
        void dispatchLongPress() {
            this.mHandler.removeMessages(3);
            this.mDeferConfirmSingleTap = false;
            this.mInLongPress = true;
            this.mListener.onLongPress(this.mCurrentDownEvent);
        }
        
        @Override
        public boolean isLongpressEnabled() {
            return this.mIsLongpressEnabled;
        }
        
        @Override
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            final int action = motionEvent.getAction();
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
            boolean b;
            if ((action & 0xFF) == 0x6) {
                b = true;
            }
            else {
                b = false;
            }
            int actionIndex;
            if (b) {
                actionIndex = MotionEventCompat.getActionIndex(motionEvent);
            }
            else {
                actionIndex = -1;
            }
            float n = 0.0f;
            float n2 = 0.0f;
            final int pointerCount = motionEvent.getPointerCount();
            for (int i = 0; i < pointerCount; ++i) {
                if (actionIndex != i) {
                    n += motionEvent.getX(i);
                    n2 += motionEvent.getY(i);
                }
            }
            int n3;
            if (b) {
                n3 = pointerCount - 1;
            }
            else {
                n3 = pointerCount;
            }
            final float n4 = n / n3;
            final float n5 = n2 / n3;
            final boolean b2 = false;
            final boolean b3 = false;
            final boolean b4 = false;
            boolean b6;
            final boolean b5 = b6 = false;
            switch (action & 0xFF) {
                default: {
                    b6 = b5;
                    return b6;
                }
                case 3: {
                    this.cancel();
                    b6 = b5;
                    return b6;
                }
                case 1: {
                    this.mStillDown = false;
                    final MotionEvent obtain = MotionEvent.obtain(motionEvent);
                    Label_0949: {
                        if (this.mIsDoubleTapping) {
                            b6 = (false | this.mDoubleTapListener.onDoubleTapEvent(motionEvent));
                        }
                        else if (this.mInLongPress) {
                            this.mHandler.removeMessages(3);
                            this.mInLongPress = false;
                            b6 = b4;
                        }
                        else if (this.mAlwaysInTapRegion) {
                            final boolean b7 = b6 = this.mListener.onSingleTapUp(motionEvent);
                            if (this.mDeferConfirmSingleTap) {
                                b6 = b7;
                                if (this.mDoubleTapListener != null) {
                                    this.mDoubleTapListener.onSingleTapConfirmed(motionEvent);
                                    b6 = b7;
                                }
                            }
                        }
                        else {
                            final VelocityTracker mVelocityTracker = this.mVelocityTracker;
                            final int pointerId = motionEvent.getPointerId(0);
                            mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                            final float yVelocity = VelocityTrackerCompat.getYVelocity(mVelocityTracker, pointerId);
                            final float xVelocity = VelocityTrackerCompat.getXVelocity(mVelocityTracker, pointerId);
                            if (Math.abs(yVelocity) <= this.mMinimumFlingVelocity) {
                                b6 = b4;
                                if (Math.abs(xVelocity) <= this.mMinimumFlingVelocity) {
                                    break Label_0949;
                                }
                            }
                            b6 = this.mListener.onFling(this.mCurrentDownEvent, motionEvent, xVelocity, yVelocity);
                        }
                    }
                    if (this.mPreviousUpEvent != null) {
                        this.mPreviousUpEvent.recycle();
                    }
                    this.mPreviousUpEvent = obtain;
                    if (this.mVelocityTracker != null) {
                        this.mVelocityTracker.recycle();
                        this.mVelocityTracker = null;
                    }
                    this.mIsDoubleTapping = false;
                    this.mDeferConfirmSingleTap = false;
                    this.mHandler.removeMessages(1);
                    this.mHandler.removeMessages(2);
                    return b6;
                }
                case 0: {
                    boolean b8 = b2;
                    if (this.mDoubleTapListener != null) {
                        final boolean hasMessages = this.mHandler.hasMessages(3);
                        if (hasMessages) {
                            this.mHandler.removeMessages(3);
                        }
                        if (this.mCurrentDownEvent != null && this.mPreviousUpEvent != null && hasMessages && this.isConsideredDoubleTap(this.mCurrentDownEvent, this.mPreviousUpEvent, motionEvent)) {
                            this.mIsDoubleTapping = true;
                            b8 = (false | this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent) | this.mDoubleTapListener.onDoubleTapEvent(motionEvent));
                        }
                        else {
                            this.mHandler.sendEmptyMessageDelayed(3, (long)GestureDetectorCompatImplBase.DOUBLE_TAP_TIMEOUT);
                            b8 = b2;
                        }
                    }
                    this.mLastFocusX = n4;
                    this.mDownFocusX = n4;
                    this.mLastFocusY = n5;
                    this.mDownFocusY = n5;
                    if (this.mCurrentDownEvent != null) {
                        this.mCurrentDownEvent.recycle();
                    }
                    this.mCurrentDownEvent = MotionEvent.obtain(motionEvent);
                    this.mAlwaysInTapRegion = true;
                    this.mAlwaysInBiggerTapRegion = true;
                    this.mStillDown = true;
                    this.mInLongPress = false;
                    this.mDeferConfirmSingleTap = false;
                    if (this.mIsLongpressEnabled) {
                        this.mHandler.removeMessages(2);
                        this.mHandler.sendEmptyMessageAtTime(2, this.mCurrentDownEvent.getDownTime() + GestureDetectorCompatImplBase.TAP_TIMEOUT + GestureDetectorCompatImplBase.LONGPRESS_TIMEOUT);
                    }
                    this.mHandler.sendEmptyMessageAtTime(1, this.mCurrentDownEvent.getDownTime() + GestureDetectorCompatImplBase.TAP_TIMEOUT);
                    b6 = (b8 | this.mListener.onDown(motionEvent));
                    return b6;
                }
                case 6: {
                    this.mLastFocusX = n4;
                    this.mDownFocusX = n4;
                    this.mLastFocusY = n5;
                    this.mDownFocusY = n5;
                    this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                    final int actionIndex2 = MotionEventCompat.getActionIndex(motionEvent);
                    final int pointerId2 = motionEvent.getPointerId(actionIndex2);
                    final float xVelocity2 = VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, pointerId2);
                    final float yVelocity2 = VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, pointerId2);
                    int n6 = 0;
                    while (true) {
                        b6 = b5;
                        if (n6 >= pointerCount) {
                            return b6;
                        }
                        if (n6 != actionIndex2) {
                            final int pointerId3 = motionEvent.getPointerId(n6);
                            if (xVelocity2 * VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, pointerId3) + yVelocity2 * VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, pointerId3) < 0.0f) {
                                break;
                            }
                        }
                        ++n6;
                    }
                    this.mVelocityTracker.clear();
                    b6 = b5;
                    return b6;
                }
                case 5: {
                    this.mLastFocusX = n4;
                    this.mDownFocusX = n4;
                    this.mLastFocusY = n5;
                    this.mDownFocusY = n5;
                    this.cancelTaps();
                    b6 = b5;
                }
                case 4: {
                    return b6;
                }
                case 2: {
                    b6 = b5;
                    if (this.mInLongPress) {
                        return b6;
                    }
                    final float a = this.mLastFocusX - n4;
                    final float a2 = this.mLastFocusY - n5;
                    if (this.mIsDoubleTapping) {
                        b6 = (false | this.mDoubleTapListener.onDoubleTapEvent(motionEvent));
                        return b6;
                    }
                    if (!this.mAlwaysInTapRegion) {
                        if (Math.abs(a) < 1.0f) {
                            b6 = b5;
                            if (Math.abs(a2) < 1.0f) {
                                return b6;
                            }
                        }
                        b6 = this.mListener.onScroll(this.mCurrentDownEvent, motionEvent, a, a2);
                        this.mLastFocusX = n4;
                        this.mLastFocusY = n5;
                        return b6;
                    }
                    final int n7 = (int)(n4 - this.mDownFocusX);
                    final int n8 = (int)(n5 - this.mDownFocusY);
                    final int n9 = n7 * n7 + n8 * n8;
                    boolean onScroll = b3;
                    if (n9 > this.mTouchSlopSquare) {
                        onScroll = this.mListener.onScroll(this.mCurrentDownEvent, motionEvent, a, a2);
                        this.mLastFocusX = n4;
                        this.mLastFocusY = n5;
                        this.mAlwaysInTapRegion = false;
                        this.mHandler.removeMessages(3);
                        this.mHandler.removeMessages(1);
                        this.mHandler.removeMessages(2);
                    }
                    b6 = onScroll;
                    if (n9 > this.mTouchSlopSquare) {
                        this.mAlwaysInBiggerTapRegion = false;
                        b6 = onScroll;
                        return b6;
                    }
                    return b6;
                }
            }
        }
        
        @Override
        public void setIsLongpressEnabled(final boolean mIsLongpressEnabled) {
            this.mIsLongpressEnabled = mIsLongpressEnabled;
        }
        
        @Override
        public void setOnDoubleTapListener(final GestureDetector$OnDoubleTapListener mDoubleTapListener) {
            this.mDoubleTapListener = mDoubleTapListener;
        }
        
        private class GestureHandler extends Handler
        {
            GestureHandler() {
            }
            
            GestureHandler(final Handler handler) {
                super(handler.getLooper());
            }
            
            public void handleMessage(final Message obj) {
                switch (obj.what) {
                    default: {
                        throw new RuntimeException("Unknown message " + obj);
                    }
                    case 1: {
                        GestureDetectorCompatImplBase.this.mListener.onShowPress(GestureDetectorCompatImplBase.this.mCurrentDownEvent);
                        break;
                    }
                    case 2: {
                        GestureDetectorCompatImplBase.this.dispatchLongPress();
                        break;
                    }
                    case 3: {
                        if (GestureDetectorCompatImplBase.this.mDoubleTapListener == null) {
                            break;
                        }
                        if (!GestureDetectorCompatImplBase.this.mStillDown) {
                            GestureDetectorCompatImplBase.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetectorCompatImplBase.this.mCurrentDownEvent);
                            break;
                        }
                        GestureDetectorCompatImplBase.this.mDeferConfirmSingleTap = true;
                        break;
                    }
                }
            }
        }
    }
    
    static class GestureDetectorCompatImplJellybeanMr2 implements GestureDetectorCompatImpl
    {
        private final GestureDetector mDetector;
        
        public GestureDetectorCompatImplJellybeanMr2(final Context context, final GestureDetector$OnGestureListener gestureDetector$OnGestureListener, final Handler handler) {
            this.mDetector = new GestureDetector(context, gestureDetector$OnGestureListener, handler);
        }
        
        @Override
        public boolean isLongpressEnabled() {
            return this.mDetector.isLongpressEnabled();
        }
        
        @Override
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return this.mDetector.onTouchEvent(motionEvent);
        }
        
        @Override
        public void setIsLongpressEnabled(final boolean isLongpressEnabled) {
            this.mDetector.setIsLongpressEnabled(isLongpressEnabled);
        }
        
        @Override
        public void setOnDoubleTapListener(final GestureDetector$OnDoubleTapListener onDoubleTapListener) {
            this.mDetector.setOnDoubleTapListener(onDoubleTapListener);
        }
    }
}
