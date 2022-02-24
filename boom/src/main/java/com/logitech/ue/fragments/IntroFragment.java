// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import com.logitech.ue.tasks.SafeTask;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.animation.ArgbEvaluator;
import com.logitech.ue.UEColourHelper;
import android.support.v4.content.LocalBroadcastManager;
import android.content.Intent;
import android.animation.Animator$AnimatorListener;
import com.logitech.ue.centurion.device.devicedata.UEHardwareInfo;
import com.logitech.ue.tasks.GetDeviceHardwareInfoTask;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import butterknife.ButterKnife;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.app.Dialog;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import com.logitech.ue.App;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.tasks.InitManagerTask;
import android.os.Handler;
import android.animation.AnimatorSet;
import butterknife.Bind;
import android.view.View;
import android.support.v4.app.DialogFragment;

public class IntroFragment extends DialogFragment
{
    public static final String ACTION_ANIMATOR_STATE_CHANGED = "com.logitech.ue.fragments.STATE_CHANGED";
    public static final int ANIMATION_COLOR_CHANGE_DURATION = 500;
    public static final int ANIMATION_HIDE_MINUS_DELAY = 250;
    public static final int ANIMATION_HIDE_MINUS_DURATION = 250;
    public static final int ANIMATION_HIDE_PLUS_DELAY = 250;
    public static final int ANIMATION_HIDE_PLUS_DURATION = 250;
    public static final int ANIMATION_PERIOD = 2450;
    public static final int ANIMATION_SHOWING_MINUS = 250;
    public static final int ANIMATION_SHOWING_PLUS = 250;
    public static final int ANIMATION_SHOW_MINUS_DELAY = 250;
    public static final int ANIMATION_SHOW_MINUS_DURATION = 250;
    public static final int ANIMATION_SHOW_PLUS_DELAY = 200;
    public static final int ANIMATION_SHOW_PLUS_DURATION = 250;
    public static final int ANIMATION_START_HIDE_MINUS = 1200;
    public static final int ANIMATION_START_HIDE_PLUS = 950;
    public static final int ANIMATION_START_SHOW_MINUS = 700;
    public static final String PARAM_PRIMARY_DEVICE_COLOR_KEY = "primary_color";
    public static final String PARAM_SECONDARY_DEVICE_COLOR_KEY = "secondary_color";
    public static final String PARAM_STATE_KEY = "intro_state";
    public static final String TAG;
    @Bind({ 2131624030 })
    View mBackground;
    @Bind({ 2131624159 })
    View mMinusVerticalLine;
    @Bind({ 2131624158 })
    View mPlusHorizontalLine;
    @Bind({ 2131624157 })
    View mPlusVerticalLine;
    private IntroState mState;
    AnimatorSet mainAnimator;
    int minAnimationCount;
    
    static {
        TAG = IntroFragment.class.getSimpleName();
    }
    
    public IntroFragment() {
        this.mState = IntroState.beginning;
        this.minAnimationCount = 2;
    }
    
    public static IntroFragment getInstance() {
        final IntroFragment introFragment = new IntroFragment();
        introFragment.setStyle(1, 2131427529);
        introFragment.setCancelable(false);
        return introFragment;
    }
    
    @SuppressLint({ "NewApi" })
    public void beginIntro() {
        new Handler().postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                IntroFragment.this.playAnimators();
            }
        }, 600L);
        new InitManagerTask() {
            protected void onPostExecute(final UEDeviceManager ueDeviceManager) {
                super.onPostExecute((Object)ueDeviceManager);
                IntroFragment.this.mState = IntroState.finishing;
                if (UEDeviceManager.getInstance().getConnectedDevice() == null) {
                    App.getInstance().checkForDevice();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
    }
    
    public IntroState getState() {
        return this.mState;
    }
    
    @Override
    public Dialog onCreateDialog(final Bundle bundle) {
        final Dialog onCreateDialog = super.onCreateDialog(bundle);
        onCreateDialog.getWindow().getAttributes().windowAnimations = 2131427537;
        return onCreateDialog;
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(2130968633, viewGroup, false);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        this.beginIntro();
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
    }
    
    void playAnimators() {
        this.mainAnimator = new AnimatorSet();
        this.mPlusVerticalLine.setAlpha(0.0f);
        this.mPlusHorizontalLine.setAlpha(0.0f);
        this.mMinusVerticalLine.setAlpha(0.0f);
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.mPlusVerticalLine, View.ALPHA, new float[] { 0.0f, 1.0f });
        ofFloat.setStartDelay(200L);
        ofFloat.setDuration(250L);
        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)this.mPlusHorizontalLine, View.ALPHA, new float[] { 0.0f, 1.0f });
        ofFloat2.setStartDelay(200L);
        ofFloat2.setDuration(250L);
        final ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat((Object)this.mMinusVerticalLine, View.ALPHA, new float[] { 0.0f, 1.0f });
        ofFloat3.setStartDelay(700L);
        ofFloat3.setDuration(250L);
        final ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat((Object)this.mPlusVerticalLine, View.ALPHA, new float[] { 1.0f, 0.0f });
        ofFloat4.setStartDelay(950L);
        ofFloat4.setDuration(250L);
        final ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat((Object)this.mPlusHorizontalLine, View.ALPHA, new float[] { 1.0f, 0.0f });
        ofFloat5.setStartDelay(950L);
        ofFloat5.setDuration(250L);
        final ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat((Object)this.mMinusVerticalLine, View.ALPHA, new float[] { 1.0f, 0.0f });
        ofFloat6.setStartDelay(1200L);
        ofFloat6.setDuration(250L);
        this.mainAnimator.play((Animator)ofFloat);
        this.mainAnimator.play((Animator)ofFloat2);
        this.mainAnimator.play((Animator)ofFloat3);
        this.mainAnimator.play((Animator)ofFloat4);
        this.mainAnimator.play((Animator)ofFloat5);
        this.mainAnimator.play((Animator)ofFloat6);
        this.mainAnimator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                IntroFragment.this.mPlusVerticalLine.setVisibility(0);
                final IntroFragment this$0 = IntroFragment.this;
                --this$0.minAnimationCount;
                if (IntroFragment.this.mState == IntroState.finishing && IntroFragment.this.minAnimationCount < 0) {
                    if (UEDeviceManager.getInstance().getConnectedDevice() != null) {
                        ((SafeTask<Void, Progress, Result>)new GetDeviceHardwareInfoTask() {
                            @Override
                            public void onError(final Exception ex) {
                                super.onError(ex);
                                if (IntroFragment.this.getView() != null) {
                                    IntroFragment.this.playNoDeviceFinishAnimator();
                                }
                            }
                            
                            public void onSuccess(final UEHardwareInfo ueHardwareInfo) {
                                super.onSuccess((T)ueHardwareInfo);
                                if (IntroFragment.this.getView() != null) {
                                    IntroFragment.this.playDeviceFinishAnimator(ueHardwareInfo);
                                }
                            }
                        }).executeOnThreadPoolExecutor(new Void[0]);
                    }
                    else {
                        IntroFragment.this.playNoDeviceFinishAnimator();
                    }
                }
                else {
                    new Handler().postDelayed((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            IntroFragment.this.mainAnimator.start();
                        }
                    }, 100L);
                }
            }
        });
        this.mainAnimator.start();
        final Intent intent = new Intent("com.logitech.ue.fragments.STATE_CHANGED");
        intent.putExtra("intro_state", IntroState.beginning.ordinal());
        if (this.getActivity() != null) {
            LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(intent);
        }
    }
    
    void playDeviceFinishAnimator(final UEHardwareInfo ueHardwareInfo) {
        final Handler handler = new Handler();
        this.mainAnimator = new AnimatorSet();
        final Intent intent = new Intent("com.logitech.ue.fragments.STATE_CHANGED");
        intent.putExtra("intro_state", IntroState.finishing.ordinal());
        intent.putExtra("primary_color", ueHardwareInfo.getPrimaryDeviceColour());
        this.mPlusHorizontalLine.setBackgroundColor(UEColourHelper.getDeviceButtonsColor(ueHardwareInfo.getPrimaryDeviceColour()));
        this.mPlusVerticalLine.setBackgroundColor(UEColourHelper.getDeviceButtonsColor(ueHardwareInfo.getPrimaryDeviceColour()));
        this.mMinusVerticalLine.setBackgroundColor(UEColourHelper.getDeviceButtonsColor(ueHardwareInfo.getPrimaryDeviceColour()));
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.mPlusVerticalLine, View.ALPHA, new float[] { 0.0f, 1.0f });
        ofFloat.setStartDelay(200L);
        ofFloat.setDuration(250L);
        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)this.mPlusHorizontalLine, View.ALPHA, new float[] { 0.0f, 1.0f });
        ofFloat2.setStartDelay(200L);
        ofFloat2.setDuration(250L);
        final ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat((Object)this.mMinusVerticalLine, View.ALPHA, new float[] { 0.0f, 1.0f });
        ofFloat3.setStartDelay(700L);
        ofFloat3.setDuration(250L);
        final ValueAnimator ofObject = ValueAnimator.ofObject((TypeEvaluator)new ArgbEvaluator(), new Object[] { ContextCompat.getColor((Context)this.getActivity(), 17170443), UEColourHelper.getDeviceSpineColor(ueHardwareInfo.getPrimaryDeviceColour()) });
        ofObject.setStartDelay(2450L);
        ofObject.setDuration(500L);
        ofObject.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                IntroFragment.this.mBackground.setBackgroundColor((int)valueAnimator.getAnimatedValue());
            }
        });
        this.mainAnimator.play((Animator)ofFloat);
        this.mainAnimator.play((Animator)ofFloat2);
        this.mainAnimator.play((Animator)ofFloat3);
        this.mainAnimator.play((Animator)ofObject);
        this.mainAnimator.start();
        handler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                IntroFragment.this.playEndAnimation();
            }
        }, 2950L);
        intent.putExtra("secondary_color", ueHardwareInfo.getSecondaryDeviceColour());
        if (this.getActivity() != null) {
            LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(intent);
        }
    }
    
    void playEndAnimation() {
        if (this.getActivity() != null) {
            final Intent intent = new Intent("com.logitech.ue.fragments.STATE_CHANGED");
            intent.putExtra("intro_state", IntroState.finished.ordinal());
            LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(intent);
            this.dismissAllowingStateLoss();
        }
    }
    
    void playNoDeviceFinishAnimator() {
        final Intent intent = new Intent("com.logitech.ue.fragments.STATE_CHANGED");
        intent.putExtra("intro_state", IntroState.finishing.ordinal());
        if (this.getActivity() != null) {
            LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(intent);
            this.playEndAnimation();
        }
    }
    
    public enum IntroState
    {
        beginning, 
        finished, 
        finishing;
    }
}
