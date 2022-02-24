// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.views;

import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.util.TypedValue;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Paint;
import android.view.View;

public class SearchIndicator extends View
{
    protected float circleRadius;
    protected Paint mPaint;
    private float progress;
    protected float strokeWidth;
    
    public SearchIndicator(final Context context) {
        this(context, null);
    }
    
    public SearchIndicator(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public SearchIndicator(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mPaint = new Paint();
        this.playIndicatorAnimation();
        this.strokeWidth = TypedValue.applyDimension(1, 2.0f, this.getResources().getDisplayMetrics());
        this.mPaint.setStrokeWidth(this.strokeWidth);
        this.mPaint.setAntiAlias(true);
    }
    
    public float getProgress() {
        return this.progress;
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(this.getWidth() / 2 - (this.getWidth() / 2 - this.circleRadius - this.strokeWidth) * this.progress, (float)(this.getHeight() / 2), (this.getWidth() / 2 - this.circleRadius - this.strokeWidth) * this.progress + this.getWidth() / 2, (float)(this.getHeight() / 2), this.mPaint);
        canvas.drawCircle(this.getWidth() / 2 - (this.getWidth() / 2 - this.circleRadius - this.strokeWidth) * this.progress, (float)(this.getHeight() / 2), this.circleRadius, this.mPaint);
        canvas.drawCircle(this.getWidth() / 2 + (this.getWidth() / 2 - this.circleRadius - this.strokeWidth) * this.progress, (float)(this.getHeight() / 2), this.circleRadius, this.mPaint);
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        this.circleRadius = n2 / 2 - this.strokeWidth;
        super.onSizeChanged(n, n2, n3, n4);
    }
    
    public void playIndicatorAnimation() {
        final ObjectAnimator setDuration = ObjectAnimator.ofFloat((Object)this, "progress", new float[] { 0.0f, 1.0f }).setDuration(900L);
        final ObjectAnimator setDuration2 = ObjectAnimator.ofFloat((Object)this, "alpha", new float[] { 1.0f, 0.0f }).setDuration(600L);
        setDuration2.setStartDelay(300L);
        final AnimatorSet set = new AnimatorSet();
        set.playSequentially(new Animator[] { (Animator)setDuration, (Animator)setDuration2 });
        set.addListener((Animator$AnimatorListener)new Animator$AnimatorListener() {
            public void onAnimationCancel(final Animator animator) {
            }
            
            public void onAnimationEnd(final Animator animator) {
                animator.start();
            }
            
            public void onAnimationRepeat(final Animator animator) {
            }
            
            public void onAnimationStart(final Animator animator) {
                SearchIndicator.this.setProgress(0.0f);
                SearchIndicator.this.setAlpha(1.0f);
            }
        });
        set.start();
    }
    
    public void setProgress(final float progress) {
        this.progress = progress;
        this.invalidate();
    }
}
