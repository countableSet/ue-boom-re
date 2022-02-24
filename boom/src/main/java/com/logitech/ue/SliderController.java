// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue;

import android.view.MotionEvent;
import android.animation.Animator$AnimatorListener;
import android.animation.ObjectAnimator;
import android.os.Build$VERSION;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.graphics.Bitmap$Config;
import android.graphics.drawable.ClipDrawable;
import android.animation.Animator;
import android.view.View;
import android.app.Activity;
import android.view.View$OnTouchListener;

public class SliderController implements View$OnTouchListener
{
    public static final int MAX_LEVEL = 10000;
    public static final int MIN_LEVEL = 0;
    private Activity mActivity;
    private View mActivityRootView;
    private Animator mAnimator;
    private float mCenterSnap;
    private ClipDrawable mLeftDrawable;
    private SliderListener mListener;
    private float mPadding;
    
    public SliderController(final Activity mActivity, final float n) {
        this.mActivity = mActivity;
        this.mActivityRootView = mActivity.findViewById(16908290);
        final Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap$Config.ARGB_8888);
        bitmap.eraseColor(-65536);
        final Bitmap bitmap2 = Bitmap.createBitmap(1, 1, Bitmap$Config.ARGB_8888);
        bitmap2.eraseColor(0);
        this.mLeftDrawable = new ClipDrawable((Drawable)new BitmapDrawable(mActivity.getResources(), bitmap), 3, 1);
        final LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] { (Drawable)new BitmapDrawable(mActivity.getResources(), bitmap2), (Drawable)this.mLeftDrawable });
        if (Build$VERSION.SDK_INT < 16) {
            this.mActivityRootView.setBackgroundDrawable((Drawable)layerDrawable);
        }
        else {
            this.mActivityRootView.setBackground((Drawable)layerDrawable);
        }
        this.mLeftDrawable.setLevel((int)(10000.0f * n));
        this.mLeftDrawable.setAlpha(0);
    }
    
    public SliderController(final Activity activity, final float n, final float mPadding, final float mCenterSnap) {
        this(activity, n);
        this.mPadding = mPadding;
        this.mCenterSnap = mCenterSnap;
    }
    
    public void begin() {
        (this.mAnimator = (Animator)ObjectAnimator.ofInt((Object)this.mLeftDrawable, "alpha", new int[] { 255 }).setDuration(300L)).start();
    }
    
    public void end() {
        if (this.mAnimator != null && this.mAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
        (this.mAnimator = (Animator)ObjectAnimator.ofInt((Object)this.mLeftDrawable, "alpha", new int[] { 0 }).setDuration(300L)).addListener((Animator$AnimatorListener)new Animator$AnimatorListener() {
            public void onAnimationCancel(final Animator animator) {
            }
            
            public void onAnimationEnd(final Animator animator) {
                if (Build$VERSION.SDK_INT < 16) {
                    SliderController.this.mActivityRootView.setBackgroundDrawable((Drawable)null);
                }
                else {
                    SliderController.this.mActivityRootView.setBackground((Drawable)null);
                }
            }
            
            public void onAnimationRepeat(final Animator animator) {
            }
            
            public void onAnimationStart(final Animator animator) {
            }
        });
        this.mAnimator.start();
    }
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
            case 2: {
                final float n = motionEvent.getRawX() / this.mActivityRootView.getWidth();
                float mPadding;
                if (n < this.mPadding) {
                    mPadding = this.mPadding;
                }
                else if (n > 1.0f - this.mPadding) {
                    mPadding = 1.0f - this.mPadding;
                }
                else {
                    mPadding = n;
                    if (n > 0.5f - this.mCenterSnap) {
                        mPadding = n;
                        if (n < this.mCenterSnap + 0.5f) {
                            mPadding = 0.5f;
                        }
                    }
                }
                this.mLeftDrawable.setLevel(Math.round(10000.0f * mPadding));
                if (this.mListener != null) {
                    this.mListener.onValueChanged(mPadding);
                    break;
                }
                break;
            }
            case 1: {
                this.end();
                break;
            }
        }
        return true;
    }
    
    public void setListener(final SliderListener mListener) {
        this.mListener = mListener;
    }
    
    public interface SliderListener
    {
        void onValueChanged(final float p0);
    }
}
