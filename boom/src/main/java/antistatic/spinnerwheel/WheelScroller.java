// 
// Decompiled by Procyon v0.5.36
// 

package antistatic.spinnerwheel;

import android.view.animation.Interpolator;
import android.view.GestureDetector$OnGestureListener;
import android.view.MotionEvent;
import android.view.GestureDetector$SimpleOnGestureListener;
import android.os.Message;
import android.widget.Scroller;
import android.view.GestureDetector;
import android.content.Context;
import android.os.Handler;

public abstract class WheelScroller
{
    public static final int MIN_DELTA_FOR_SCROLLING = 1;
    private static final int SCROLLING_DURATION = 400;
    private final int MESSAGE_JUSTIFY;
    private final int MESSAGE_SCROLL;
    private final Handler animationHandler;
    private Context context;
    private GestureDetector gestureDetector;
    private boolean isScrollingPerformed;
    private int lastScrollPosition;
    private float lastTouchedPosition;
    private ScrollingListener listener;
    protected Scroller scroller;
    
    public WheelScroller(final Context context, final ScrollingListener listener) {
        this.MESSAGE_SCROLL = 0;
        this.MESSAGE_JUSTIFY = 1;
        this.animationHandler = new Handler() {
            public void handleMessage(final Message message) {
                WheelScroller.this.scroller.computeScrollOffset();
                final int currentScrollerPosition = WheelScroller.this.getCurrentScrollerPosition();
                final int n = WheelScroller.this.lastScrollPosition - currentScrollerPosition;
                WheelScroller.this.lastScrollPosition = currentScrollerPosition;
                if (n != 0) {
                    WheelScroller.this.listener.onScroll(n);
                }
                if (Math.abs(currentScrollerPosition - WheelScroller.this.getFinalScrollerPosition()) < 1) {
                    WheelScroller.this.scroller.forceFinished(true);
                }
                if (!WheelScroller.this.scroller.isFinished()) {
                    WheelScroller.this.animationHandler.sendEmptyMessage(message.what);
                }
                else if (message.what == 0) {
                    WheelScroller.this.justify();
                }
                else {
                    WheelScroller.this.finishScrolling();
                }
            }
        };
        (this.gestureDetector = new GestureDetector(context, (GestureDetector$OnGestureListener)new GestureDetector$SimpleOnGestureListener() {
            public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
                WheelScroller.this.lastScrollPosition = 0;
                WheelScroller.this.scrollerFling(WheelScroller.this.lastScrollPosition, (int)n, (int)n2);
                WheelScroller.this.setNextMessage(0);
                return true;
            }
            
            public boolean onScroll(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
                return true;
            }
        })).setIsLongpressEnabled(false);
        this.scroller = new Scroller(context);
        this.listener = listener;
        this.context = context;
    }
    
    private void clearMessages() {
        this.animationHandler.removeMessages(0);
        this.animationHandler.removeMessages(1);
    }
    
    private void justify() {
        this.listener.onJustify();
        this.setNextMessage(1);
    }
    
    private void setNextMessage(final int n) {
        this.clearMessages();
        this.animationHandler.sendEmptyMessage(n);
    }
    
    private void startScrolling() {
        if (!this.isScrollingPerformed) {
            this.isScrollingPerformed = true;
            this.listener.onStarted();
        }
    }
    
    protected void finishScrolling() {
        if (this.isScrollingPerformed) {
            this.listener.onFinished();
            this.isScrollingPerformed = false;
        }
    }
    
    protected abstract int getCurrentScrollerPosition();
    
    protected abstract int getFinalScrollerPosition();
    
    protected abstract float getMotionEventPosition(final MotionEvent p0);
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0: {
                this.lastTouchedPosition = this.getMotionEventPosition(motionEvent);
                this.scroller.forceFinished(true);
                this.clearMessages();
                this.listener.onTouch();
                break;
            }
            case 1: {
                if (this.scroller.isFinished()) {
                    this.listener.onTouchUp();
                    break;
                }
                break;
            }
            case 2: {
                final int n = (int)(this.getMotionEventPosition(motionEvent) - this.lastTouchedPosition);
                if (n != 0) {
                    this.startScrolling();
                    this.listener.onScroll(n);
                    this.lastTouchedPosition = this.getMotionEventPosition(motionEvent);
                    break;
                }
                break;
            }
        }
        if (!this.gestureDetector.onTouchEvent(motionEvent) && motionEvent.getAction() == 1) {
            this.justify();
        }
        return true;
    }
    
    public void scroll(final int n, int n2) {
        this.scroller.forceFinished(true);
        this.lastScrollPosition = 0;
        if (n2 == 0) {
            n2 = 400;
        }
        this.scrollerStartScroll(n, n2);
        this.setNextMessage(0);
        this.startScrolling();
    }
    
    protected abstract void scrollerFling(final int p0, final int p1, final int p2);
    
    protected abstract void scrollerStartScroll(final int p0, final int p1);
    
    public void setInterpolator(final Interpolator interpolator) {
        this.scroller.forceFinished(true);
        this.scroller = new Scroller(this.context, interpolator);
    }
    
    public void stopScrolling() {
        this.scroller.forceFinished(true);
    }
    
    public interface ScrollingListener
    {
        void onFinished();
        
        void onJustify();
        
        void onScroll(final int p0);
        
        void onStarted();
        
        void onTouch();
        
        void onTouchUp();
    }
}
