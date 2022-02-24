// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue;

import android.view.MotionEvent;
import android.view.View;
import android.os.Looper;
import android.os.Handler;
import android.view.View$OnTouchListener;

public abstract class LongPressGestureDetector implements View$OnTouchListener
{
    public static final int LONG_PRESS_TIMEOUT = 50;
    Handler mHandler;
    boolean mIsEventSend;
    boolean mIsLongPress;
    float mTapXPosition;
    float mTapYPosition;
    
    public LongPressGestureDetector() {
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mIsLongPress = false;
    }
    
    public abstract void onLongPress(final View p0, final MotionEvent p1);
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        boolean b = true;
        switch (motionEvent.getAction()) {
            default: {
                b = false;
                break;
            }
            case 0: {
                this.mIsLongPress = true;
                this.mTapXPosition = motionEvent.getX();
                this.mTapYPosition = motionEvent.getY();
                this.mHandler.postDelayed((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        LongPressGestureDetector.this.mIsLongPress = false;
                        LongPressGestureDetector.this.onLongPress(view, motionEvent);
                    }
                }, 50L);
                break;
            }
            case 2: {
                if (!this.mIsLongPress) {
                    b = false;
                    break;
                }
                if (Math.pow((int)(motionEvent.getX() - this.mTapXPosition), 2.0) + Math.pow((int)(motionEvent.getY() - this.mTapYPosition), 2.0) > 100.0) {
                    this.mHandler.removeCallbacksAndMessages((Object)null);
                    this.mIsLongPress = false;
                    b = false;
                    break;
                }
                break;
            }
            case 1:
            case 3: {
                this.mIsLongPress = false;
                this.mHandler.removeCallbacksAndMessages((Object)null);
                b = false;
                break;
            }
        }
        return b;
    }
}
