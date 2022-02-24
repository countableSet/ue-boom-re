// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.activities;

import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Display;
import android.support.v4.view.PagerAdapter;
import android.graphics.Point;
import android.app.Activity;
import butterknife.ButterKnife;
import android.os.Bundle;
import butterknife.OnClick;
import android.view.animation.AccelerateInterpolator;
import android.animation.TimeInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.widget.RelativeLayout$LayoutParams;
import android.content.Context;
import java.util.ArrayList;
import com.logitech.ue.centurion.device.devicedata.UEColour;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;
import java.util.List;
import butterknife.Bind;
import com.logitech.ue.views.UEDeviceView;
import butterknife.BindDimen;
import android.widget.ImageView;
import android.animation.AnimatorSet;

public class XUPTutorialActivity extends BaseActivity
{
    private static final int COLOR_FIRST;
    private static final int COLOR_FOURTH;
    private static final int COLOR_MASTER;
    private static final int COLOR_SECOND;
    private static final int COLOR_THIRD;
    private static final float DRAG_ROTATION = 15.0f;
    private static final int DURATION_FADE = 200;
    private static final int DURATION_ROTATION = 150;
    private static final int DURATION_STAGE = 800;
    private static final int DURATION_TRANSLATION = 600;
    private static final float INDICATOR_START = -0.75f;
    private static final float INDICATOR_STEP = 0.5f;
    private static final int PAGES_COUNT = 4;
    private static final int PAGE_FIRST = 0;
    private static final int PAGE_FOURTH = 3;
    private static final int PAGE_SECOND = 1;
    private static final int PAGE_THIRD = 2;
    public static final String TAG;
    private final int[] PAGES_TITLES_IDS;
    private AnimatorSet mAnimatorSet;
    private int mCurrentPage;
    private ImageView mCurrentPageIndicator;
    @BindDimen(2131361873)
    int mDotDeviceSize;
    private int mDrawerSpeakerHeight;
    private int mDrawerSpeakerPlace;
    @BindDimen(2131361870)
    int mDrawerSpeakerSpacing;
    @BindDimen(2131361915)
    int mDrawerSpeakerWidth;
    private ImageView mFirstLeftDotDevice;
    private float mFirstLeftDotDeviceDeltaXFromCenter;
    private float mFirstLeftDotDeviceDeltaYFromCenter;
    private float mFirstLeftDotDeviceEndX;
    private float mFirstLeftDotDeviceEndY;
    @Bind({ 2131624083 })
    UEDeviceView mFirstLeftDrawerDeviceView;
    private ImageView mFirstRightDotDevice;
    private float mFirstRightDotDeviceDeltaXFromCenter;
    private float mFirstRightDotDeviceDeltaYFromCenter;
    private float mFirstRightDotDeviceEndX;
    private float mFirstRightDotDeviceEndY;
    @Bind({ 2131624082 })
    UEDeviceView mFirstRightDrawerDeviceView;
    private int mHalfDrawerSpeakerPlace;
    @Bind({ 2131624079 })
    UEDeviceView mMasterDeviceView;
    @BindDimen(2131361907)
    int mPageIndicatorDotSize;
    private List<ImageView> mPageIndicatorViews;
    private float mPageIndicatorWidth;
    private int mPageWidth;
    private float mPrevPositivePosition;
    private float mScreenCenterX;
    private float mScreenCenterY;
    private int mScreenHeight;
    private int mScreenWidth;
    private ImageView mSecondLeftDotDevice;
    private float mSecondLeftDotDeviceDeltaXFromCenter;
    private float mSecondLeftDotDeviceDeltaYFromCenter;
    private float mSecondLeftDotDeviceEndX;
    private float mSecondLeftDotDeviceEndY;
    @Bind({ 2131624085 })
    UEDeviceView mSecondLeftDrawerDeviceView;
    private ImageView mSecondRightDotDevice;
    private float mSecondRightDotDeviceDeltaXFromCenter;
    private float mSecondRightDotDeviceDeltaYFromCenter;
    private float mSecondRightDotDeviceEndX;
    private float mSecondRightDotDeviceEndY;
    @Bind({ 2131624084 })
    UEDeviceView mSecondRightDrawerDeviceView;
    private int mSpeakerHeight;
    private int mSpeakerWidth;
    private SwipeDirection mSwipeDirection;
    @Bind({ 2131624074 })
    RelativeLayout mTutorialMainView;
    @Bind({ 2131624078 })
    ViewPager mTutorialViewPager;
    
    static {
        TAG = XUPTutorialActivity.class.getSimpleName();
        COLOR_MASTER = UEColour.MAXIMUS_BLACK_BLACK_WHITE.getCode();
        COLOR_FIRST = UEColour.MAXIMUS_TEAL_GREEN_YELLOW.getCode();
        COLOR_SECOND = UEColour.MAXIMUS_VIOLET_ORANGE_YELLOW.getCode();
        COLOR_THIRD = UEColour.MAXIMUS_VIOLET_ORANGE_YELLOW.getCode();
        COLOR_FOURTH = UEColour.MAXIMUS_TEAL_GREEN_YELLOW.getCode();
    }
    
    public XUPTutorialActivity() {
        this.PAGES_TITLES_IDS = new int[] { 2131165370, 2131165429, 2131165336, 2131165428 };
        this.mPageIndicatorViews = new ArrayList<ImageView>(5);
        this.mAnimatorSet = new AnimatorSet();
        this.mSwipeDirection = SwipeDirection.NONE;
        this.mPrevPositivePosition = -1.0f;
        this.mCurrentPage = 0;
    }
    
    private void completeFirstPage() {
        this.mCurrentPageIndicator.setTranslationX(-0.75f * this.mPageIndicatorWidth - this.mPageIndicatorDotSize / 2);
        this.restoreDrawerDeviceViewInitialState(this.mFirstRightDrawerDeviceView);
        this.restoreDrawerDeviceViewInitialState(this.mFirstLeftDrawerDeviceView);
        this.restoreDrawerDeviceViewInitialState(this.mSecondRightDrawerDeviceView);
        this.restoreDrawerDeviceViewInitialState(this.mSecondLeftDrawerDeviceView);
        this.restoreDotDeviceViewInitialState(this.mFirstLeftDotDevice);
        this.restoreDotDeviceViewInitialState(this.mFirstRightDotDevice);
        this.restoreDotDeviceViewInitialState(this.mSecondLeftDotDevice);
        this.restoreDotDeviceViewInitialState(this.mSecondRightDotDevice);
    }
    
    private void completeSecondPage() {
        this.mCurrentPageIndicator.setTranslationX(-0.25f * this.mPageIndicatorWidth - this.mPageIndicatorDotSize / 2);
        this.mFirstRightDrawerDeviceView.setAlpha(1.0f);
        this.mFirstRightDrawerDeviceView.setTranslationX((float)(-this.mHalfDrawerSpeakerPlace));
        this.mFirstRightDrawerDeviceView.setTranslationY(0.0f);
        this.mFirstLeftDrawerDeviceView.setAlpha(0.0f);
        this.mFirstLeftDrawerDeviceView.setTranslationX(0.0f);
        this.mFirstLeftDrawerDeviceView.setTranslationY(0.0f);
        this.mSecondRightDrawerDeviceView.setAlpha(1.0f);
        this.mSecondRightDrawerDeviceView.setTranslationX((float)(-this.mHalfDrawerSpeakerPlace));
        this.mSecondRightDrawerDeviceView.setTranslationY(0.0f);
        this.mSecondLeftDrawerDeviceView.setAlpha(1.0f);
        this.mSecondLeftDrawerDeviceView.setTranslationX((float)this.mHalfDrawerSpeakerPlace);
        this.mSecondLeftDrawerDeviceView.setTranslationY(0.0f);
    }
    
    private void completeThirdPage() {
        this.mFirstRightDrawerDeviceView.setAlpha(1.0f);
        this.mFirstRightDrawerDeviceView.setTranslationX((float)(-this.mHalfDrawerSpeakerPlace));
        this.mFirstRightDrawerDeviceView.setTranslationY(0.0f);
        this.mFirstLeftDrawerDeviceView.setAlpha(0.0f);
        this.mFirstLeftDrawerDeviceView.setTranslationX(0.0f);
        this.mFirstLeftDrawerDeviceView.setTranslationY(0.0f);
        this.mSecondRightDrawerDeviceView.setAlpha(1.0f);
        this.mSecondRightDrawerDeviceView.setTranslationX((float)(-this.mHalfDrawerSpeakerPlace));
        this.mSecondRightDrawerDeviceView.setTranslationY(0.0f);
        this.mSecondLeftDrawerDeviceView.setAlpha(1.0f);
        this.mSecondLeftDrawerDeviceView.setTranslationX((float)this.mHalfDrawerSpeakerPlace);
        this.mSecondLeftDrawerDeviceView.setTranslationY(0.0f);
    }
    
    private ImageView createDotDevice(final int imageResource) {
        final ImageView imageView = new ImageView((Context)this);
        imageView.setVisibility(0);
        imageView.setAlpha(0.0f);
        imageView.setImageResource(imageResource);
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams((int)this.getResources().getDimension(2131361873), (int)this.getResources().getDimension(2131361873));
        relativeLayout$LayoutParams.addRule(13, -1);
        this.mTutorialMainView.addView((View)imageView, (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
        return imageView;
    }
    
    private void createDrawerDotDevices() {
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(this.mMasterDeviceView.getLayoutParams());
        final int mDotDeviceSize = this.mDotDeviceSize;
        relativeLayout$LayoutParams.width = mDotDeviceSize;
        relativeLayout$LayoutParams.height = mDotDeviceSize;
        relativeLayout$LayoutParams.addRule(12, 2131624048);
        (this.mFirstRightDotDevice = this.createDotDevice(2130837694)).setLayoutParams((ViewGroup$LayoutParams)relativeLayout$LayoutParams);
        this.mFirstRightDotDevice.setAlpha(0.0f);
        (this.mFirstLeftDotDevice = this.createDotDevice(2130837697)).setLayoutParams((ViewGroup$LayoutParams)relativeLayout$LayoutParams);
        this.mFirstLeftDotDevice.setAlpha(0.0f);
        (this.mSecondLeftDotDevice = this.createDotDevice(2130837694)).setLayoutParams((ViewGroup$LayoutParams)relativeLayout$LayoutParams);
        this.mSecondLeftDotDevice.setAlpha(0.0f);
        (this.mSecondRightDotDevice = this.createDotDevice(2130837697)).setLayoutParams((ViewGroup$LayoutParams)relativeLayout$LayoutParams);
        this.mSecondRightDotDevice.setAlpha(0.0f);
    }
    
    private ImageView createPageIndicatorView(int mPageIndicatorDotSize, final float translationX) {
        final ImageView imageView = new ImageView((Context)this);
        imageView.setVisibility(0);
        imageView.setAlpha(1.0f);
        imageView.setImageResource(mPageIndicatorDotSize);
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(this.mFirstRightDrawerDeviceView.getLayoutParams());
        mPageIndicatorDotSize = this.mPageIndicatorDotSize;
        relativeLayout$LayoutParams.width = mPageIndicatorDotSize;
        relativeLayout$LayoutParams.height = mPageIndicatorDotSize;
        relativeLayout$LayoutParams.addRule(10, -1);
        relativeLayout$LayoutParams.addRule(14, -1);
        this.mTutorialMainView.addView((View)imageView, (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
        imageView.setTranslationX(translationX);
        imageView.setTranslationY((float)(int)this.getResources().getDimension(2131361908));
        return imageView;
    }
    
    private void createPageIndicatorViews() {
        for (int i = 0; i < 4; ++i) {
            this.mPageIndicatorViews.add(this.createPageIndicatorView(2130837593, (0.5f * i - 0.75f) * this.mPageIndicatorWidth - this.mPageIndicatorDotSize / 2));
        }
        final ImageView pageIndicatorView = this.createPageIndicatorView(2130837594, this.mPageIndicatorWidth * -0.75f - this.mPageIndicatorDotSize / 2);
        this.mCurrentPageIndicator = pageIndicatorView;
        this.mPageIndicatorViews.add(pageIndicatorView);
    }
    
    private void initDrawerDotDevices() {
        final int[] array = new int[2];
        this.mFirstLeftDotDeviceDeltaXFromCenter = -0.5f * this.mSpeakerWidth;
        this.mFirstLeftDotDeviceDeltaYFromCenter = 1.0f * this.mSpeakerHeight;
        this.mFirstLeftDotDevice.getLocationInWindow(array);
        this.mFirstLeftDotDeviceEndX = this.mScreenCenterX - array[0] + this.mFirstLeftDotDeviceDeltaXFromCenter - this.mDotDeviceSize / 2;
        this.mFirstLeftDotDeviceEndY = this.mScreenCenterY - array[1] + this.mFirstLeftDotDeviceDeltaYFromCenter - this.mDotDeviceSize / 2;
        this.mFirstRightDotDeviceDeltaXFromCenter = 0.65f * this.mSpeakerWidth;
        this.mFirstRightDotDeviceDeltaYFromCenter = 1.08f * this.mSpeakerHeight;
        this.mFirstRightDotDevice.getLocationInWindow(array);
        this.mFirstRightDotDeviceEndX = this.mScreenCenterX - array[0] + this.mFirstRightDotDeviceDeltaXFromCenter - this.mDotDeviceSize / 2;
        this.mFirstRightDotDeviceEndY = this.mScreenCenterY - array[1] + this.mFirstRightDotDeviceDeltaYFromCenter - this.mDotDeviceSize / 2;
        this.mSecondLeftDotDeviceDeltaXFromCenter = -1.5f * this.mSpeakerWidth;
        this.mSecondLeftDotDeviceDeltaYFromCenter = 0.25f * this.mSpeakerHeight;
        this.mSecondLeftDotDevice.getLocationInWindow(array);
        this.mSecondLeftDotDeviceEndX = this.mScreenCenterX - array[0] + this.mSecondLeftDotDeviceDeltaXFromCenter - this.mDotDeviceSize / 2;
        this.mSecondLeftDotDeviceEndY = this.mScreenCenterY - array[1] + this.mSecondLeftDotDeviceDeltaYFromCenter - this.mDotDeviceSize / 2;
        this.mSecondRightDotDeviceDeltaXFromCenter = 1.4f * this.mSpeakerWidth;
        this.mSecondRightDotDeviceDeltaYFromCenter = 0.6f * this.mSpeakerHeight;
        this.mSecondRightDotDevice.getLocationInWindow(array);
        this.mSecondRightDotDeviceEndX = this.mScreenCenterX - array[0] + this.mSecondRightDotDeviceDeltaXFromCenter - this.mDotDeviceSize / 2;
        this.mSecondRightDotDeviceEndY = this.mScreenCenterY - array[1] + this.mSecondRightDotDeviceDeltaYFromCenter - this.mDotDeviceSize / 2;
    }
    
    private void initSecondPage() {
        this.completeFirstPage();
        this.mCurrentPageIndicator.setTranslationX(-0.25f * this.mPageIndicatorWidth - this.mPageIndicatorDotSize / 2);
    }
    
    private void initThirdPage() {
        this.mCurrentPageIndicator.setTranslationX(0.25f * this.mPageIndicatorWidth - this.mPageIndicatorDotSize / 2);
    }
    
    private void restoreDotDeviceViewInitialState(final ImageView imageView) {
        imageView.setTranslationX(0.0f);
        imageView.setTranslationY(0.0f);
        imageView.setScaleX(1.0f);
        imageView.setScaleY(1.0f);
        imageView.setAlpha(0.0f);
        imageView.setVisibility(0);
    }
    
    private void restoreDrawerDeviceViewInitialState(final UEDeviceView ueDeviceView) {
        ueDeviceView.setRotation(0.0f);
        ueDeviceView.setTranslationX(0.0f);
        ueDeviceView.setTranslationY(0.0f);
        ueDeviceView.setScaleX(1.0f);
        ueDeviceView.setScaleY(1.0f);
        ueDeviceView.setAlpha(1.0f);
        ueDeviceView.setVisibility(0);
    }
    
    private void startFourthPageAnimation() {
        final int[] array = new int[2];
        this.mMasterDeviceView.bringToFront();
        this.mAnimatorSet.cancel();
        this.mAnimatorSet = new AnimatorSet();
        this.mSecondLeftDrawerDeviceView.getLocationInWindow(array);
        this.mAnimatorSet.play((Animator)ObjectAnimator.ofFloat((Object)this.mMasterDeviceView, View.ROTATION, new float[] { 15.0f }).setDuration(150L));
        final ObjectAnimator setDuration = ObjectAnimator.ofFloat((Object)this.mMasterDeviceView, View.TRANSLATION_Y, new float[] { (float)(this.mScreenHeight - this.mMasterDeviceView.getHeight() - this.mDrawerSpeakerHeight * 3) }).setDuration(600L);
        setDuration.setInterpolator((TimeInterpolator)new AccelerateDecelerateInterpolator());
        this.mAnimatorSet.play((Animator)setDuration);
        final ObjectAnimator setDuration2 = ObjectAnimator.ofFloat((Object)this.mMasterDeviceView, View.ROTATION, new float[] { 0.0f }).setDuration(150L);
        setDuration2.setStartDelay(1200L);
        this.mAnimatorSet.play((Animator)setDuration2);
        final ObjectAnimator setDuration3 = ObjectAnimator.ofFloat((Object)this.mFirstLeftDotDevice, View.TRANSLATION_Y, new float[] { this.mScreenCenterY + this.mDotDeviceSize * 3 }).setDuration(300L);
        setDuration3.setInterpolator((TimeInterpolator)new AccelerateInterpolator());
        setDuration3.setStartDelay(600L);
        this.mAnimatorSet.play((Animator)setDuration3);
        final ObjectAnimator setDuration4 = ObjectAnimator.ofFloat((Object)this.mFirstRightDotDevice, View.TRANSLATION_Y, new float[] { this.mScreenCenterY + this.mDotDeviceSize * 3 }).setDuration(300L);
        setDuration4.setInterpolator((TimeInterpolator)new AccelerateInterpolator());
        setDuration4.setStartDelay(600L);
        this.mAnimatorSet.play((Animator)setDuration4);
        final ObjectAnimator setDuration5 = ObjectAnimator.ofFloat((Object)this.mSecondLeftDotDevice, View.TRANSLATION_Y, new float[] { this.mScreenCenterY + this.mDotDeviceSize * 3 }).setDuration(300L);
        setDuration5.setInterpolator((TimeInterpolator)new AccelerateInterpolator());
        setDuration5.setStartDelay(600L);
        this.mAnimatorSet.play((Animator)setDuration5);
        final ObjectAnimator setDuration6 = ObjectAnimator.ofFloat((Object)this.mSecondRightDotDevice, View.TRANSLATION_Y, new float[] { this.mScreenCenterY + this.mDotDeviceSize * 3 }).setDuration(300L);
        setDuration6.setInterpolator((TimeInterpolator)new AccelerateInterpolator());
        setDuration6.setStartDelay(600L);
        this.mAnimatorSet.play((Animator)setDuration6);
        final ObjectAnimator setDuration7 = ObjectAnimator.ofFloat((Object)this.mMasterDeviceView, View.TRANSLATION_Y, new float[] { 0.0f }).setDuration(600L);
        setDuration7.setStartDelay(1200L);
        setDuration7.setInterpolator((TimeInterpolator)new AccelerateDecelerateInterpolator());
        this.mAnimatorSet.play((Animator)setDuration7);
        this.mFirstLeftDrawerDeviceView.setScaleX(1.0f);
        this.mFirstLeftDrawerDeviceView.setScaleY(1.0f);
        this.mFirstLeftDrawerDeviceView.setRotation(0.0f);
        this.mFirstLeftDrawerDeviceView.setTranslationX(0.0f);
        this.mFirstLeftDrawerDeviceView.setAlpha(1.0f);
        this.mFirstLeftDrawerDeviceView.setTranslationY((float)this.mDrawerSpeakerHeight);
        final ObjectAnimator setDuration8 = ObjectAnimator.ofFloat((Object)this.mFirstLeftDrawerDeviceView, View.TRANSLATION_Y, new float[] { 0.0f }).setDuration(600L);
        setDuration8.setStartDelay(1200L);
        this.mAnimatorSet.play((Animator)setDuration8);
        this.mFirstRightDrawerDeviceView.setScaleX(1.0f);
        this.mFirstRightDrawerDeviceView.setScaleY(1.0f);
        this.mFirstRightDrawerDeviceView.setRotation(0.0f);
        this.mFirstRightDrawerDeviceView.setTranslationX(0.0f);
        this.mFirstRightDrawerDeviceView.setAlpha(1.0f);
        this.mFirstRightDrawerDeviceView.setTranslationY((float)this.mDrawerSpeakerHeight);
        final ObjectAnimator setDuration9 = ObjectAnimator.ofFloat((Object)this.mFirstRightDrawerDeviceView, View.TRANSLATION_Y, new float[] { 0.0f }).setDuration(600L);
        setDuration9.setStartDelay(1200L);
        this.mAnimatorSet.play((Animator)setDuration9);
        this.mSecondLeftDrawerDeviceView.setScaleX(1.0f);
        this.mSecondLeftDrawerDeviceView.setScaleY(1.0f);
        this.mSecondLeftDrawerDeviceView.setRotation(0.0f);
        this.mSecondLeftDrawerDeviceView.setTranslationX(0.0f);
        this.mSecondLeftDrawerDeviceView.setAlpha(1.0f);
        this.mSecondLeftDrawerDeviceView.setTranslationY((float)this.mDrawerSpeakerHeight);
        final ObjectAnimator setDuration10 = ObjectAnimator.ofFloat((Object)this.mSecondLeftDrawerDeviceView, View.TRANSLATION_Y, new float[] { 0.0f }).setDuration(600L);
        setDuration10.setStartDelay(1200L);
        this.mAnimatorSet.play((Animator)setDuration10);
        this.mSecondRightDrawerDeviceView.setScaleX(1.0f);
        this.mSecondRightDrawerDeviceView.setScaleY(1.0f);
        this.mSecondRightDrawerDeviceView.setRotation(0.0f);
        this.mSecondRightDrawerDeviceView.setTranslationX(0.0f);
        this.mSecondRightDrawerDeviceView.setAlpha(1.0f);
        this.mSecondRightDrawerDeviceView.setTranslationY((float)this.mDrawerSpeakerHeight);
        final ObjectAnimator setDuration11 = ObjectAnimator.ofFloat((Object)this.mSecondRightDrawerDeviceView, View.TRANSLATION_Y, new float[] { 0.0f }).setDuration(600L);
        setDuration11.setStartDelay(1200L);
        this.mAnimatorSet.play((Animator)setDuration11);
        this.mAnimatorSet.start();
    }
    
    private void startSecondPageAnimation() {
        final int[] array = new int[2];
        this.mAnimatorSet.cancel();
        this.mAnimatorSet = new AnimatorSet();
        this.mFirstLeftDotDevice.setScaleX(1.0f);
        this.mFirstLeftDotDevice.setScaleY(1.0f);
        this.mFirstLeftDotDevice.setAlpha(0.0f);
        this.mFirstLeftDotDevice.setTranslationX(this.mFirstLeftDotDeviceEndX);
        this.mFirstLeftDotDevice.setTranslationY(this.mFirstLeftDotDeviceEndY);
        final ObjectAnimator setDuration = ObjectAnimator.ofFloat((Object)this.mFirstLeftDotDevice, View.ALPHA, new float[] { 1.0f }).setDuration(150L);
        setDuration.setStartDelay(600L);
        this.mAnimatorSet.play((Animator)setDuration);
        this.mFirstLeftDrawerDeviceView.getLocationInWindow(array);
        this.mAnimatorSet.play((Animator)ObjectAnimator.ofFloat((Object)this.mFirstLeftDrawerDeviceView, View.ROTATION, new float[] { 15.0f }).setDuration(150L));
        this.mAnimatorSet.play((Animator)ObjectAnimator.ofFloat((Object)this.mFirstLeftDrawerDeviceView, View.TRANSLATION_X, new float[] { this.mScreenCenterX - array[0] + this.mFirstLeftDotDeviceDeltaXFromCenter - this.mDrawerSpeakerWidth / 2 }).setDuration(600L));
        this.mAnimatorSet.play((Animator)ObjectAnimator.ofFloat((Object)this.mFirstLeftDrawerDeviceView, View.TRANSLATION_Y, new float[] { this.mScreenCenterY - array[1] + this.mFirstLeftDotDeviceDeltaYFromCenter - this.mDrawerSpeakerSpacing - this.mDrawerSpeakerHeight / 2 }).setDuration(600L));
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.mFirstLeftDrawerDeviceView, View.ALPHA, new float[] { 0.0f });
        ofFloat.setDuration(200L);
        ofFloat.setStartDelay(600L);
        this.mAnimatorSet.play((Animator)ofFloat);
        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)this.mFirstLeftDrawerDeviceView, View.SCALE_X, new float[] { 0.5f });
        ofFloat2.setDuration(200L);
        ofFloat2.setStartDelay(600L);
        this.mAnimatorSet.play((Animator)ofFloat2);
        final ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat((Object)this.mFirstLeftDrawerDeviceView, View.SCALE_Y, new float[] { 0.5f });
        ofFloat3.setDuration(200L);
        ofFloat3.setStartDelay(600L);
        this.mAnimatorSet.play((Animator)ofFloat3);
        final ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat((Object)this.mSecondLeftDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)this.mHalfDrawerSpeakerPlace });
        ofFloat4.setDuration(800L);
        this.mAnimatorSet.play((Animator)ofFloat4);
        final ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat((Object)this.mFirstRightDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-this.mHalfDrawerSpeakerPlace) });
        ofFloat5.setDuration(800L);
        this.mAnimatorSet.play((Animator)ofFloat5);
        final ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat((Object)this.mSecondRightDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-this.mHalfDrawerSpeakerPlace) });
        ofFloat6.setDuration(800L);
        this.mAnimatorSet.play((Animator)ofFloat6);
        this.mAnimatorSet.start();
    }
    
    private void startThirdPageAnimation() {
        final int[] array = new int[2];
        this.mMasterDeviceView.bringToFront();
        this.mAnimatorSet.cancel();
        this.mAnimatorSet = new AnimatorSet();
        this.mSecondLeftDotDevice.setAlpha(0.0f);
        this.mSecondLeftDotDevice.setTranslationX(this.mSecondLeftDotDeviceEndX);
        this.mSecondLeftDotDevice.setTranslationY(this.mSecondLeftDotDeviceEndY);
        final ObjectAnimator setDuration = ObjectAnimator.ofFloat((Object)this.mSecondLeftDotDevice, View.ALPHA, new float[] { 1.0f }).setDuration(150L);
        setDuration.setStartDelay(600L);
        this.mAnimatorSet.play((Animator)setDuration);
        this.mSecondLeftDrawerDeviceView.getLocationInWindow(array);
        this.mAnimatorSet.play((Animator)ObjectAnimator.ofFloat((Object)this.mSecondLeftDrawerDeviceView, View.ROTATION, new float[] { 15.0f }).setDuration(150L));
        this.mAnimatorSet.play((Animator)ObjectAnimator.ofFloat((Object)this.mSecondLeftDrawerDeviceView, View.TRANSLATION_X, new float[] { this.mScreenCenterX - array[0] + this.mSecondLeftDotDeviceDeltaXFromCenter }).setDuration(600L));
        this.mAnimatorSet.play((Animator)ObjectAnimator.ofFloat((Object)this.mSecondLeftDrawerDeviceView, View.TRANSLATION_Y, new float[] { this.mScreenCenterY - array[1] + this.mSecondLeftDotDeviceDeltaYFromCenter - this.mDrawerSpeakerHeight / 2 }).setDuration(600L));
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.mSecondLeftDrawerDeviceView, View.ALPHA, new float[] { 0.0f });
        ofFloat.setDuration(200L);
        ofFloat.setStartDelay(600L);
        this.mAnimatorSet.play((Animator)ofFloat);
        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)this.mSecondLeftDrawerDeviceView, View.SCALE_X, new float[] { 0.5f });
        ofFloat2.setDuration(200L);
        ofFloat2.setStartDelay(600L);
        this.mAnimatorSet.play((Animator)ofFloat2);
        final ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat((Object)this.mSecondLeftDrawerDeviceView, View.SCALE_Y, new float[] { 0.5f });
        ofFloat3.setDuration(200L);
        ofFloat3.setStartDelay(600L);
        this.mAnimatorSet.play((Animator)ofFloat3);
        final ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat((Object)this.mFirstRightDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-this.mDrawerSpeakerPlace) });
        ofFloat4.setDuration(800L);
        this.mAnimatorSet.play((Animator)ofFloat4);
        final ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat((Object)this.mSecondRightDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-this.mDrawerSpeakerPlace) });
        ofFloat5.setDuration(800L);
        this.mAnimatorSet.play((Animator)ofFloat5);
        this.mSecondRightDotDevice.setAlpha(0.0f);
        this.mSecondRightDotDevice.setTranslationX(this.mSecondRightDotDeviceEndX);
        this.mSecondRightDotDevice.setTranslationY(this.mSecondRightDotDeviceEndY);
        final ObjectAnimator setDuration2 = ObjectAnimator.ofFloat((Object)this.mSecondRightDotDevice, View.ALPHA, new float[] { 1.0f }).setDuration(150L);
        setDuration2.setStartDelay(1400L);
        this.mAnimatorSet.play((Animator)setDuration2);
        this.mSecondRightDrawerDeviceView.getLocationInWindow(array);
        final ObjectAnimator setDuration3 = ObjectAnimator.ofFloat((Object)this.mSecondRightDrawerDeviceView, View.ROTATION, new float[] { -15.0f }).setDuration(150L);
        setDuration3.setStartDelay(800L);
        this.mAnimatorSet.play((Animator)setDuration3);
        final ObjectAnimator setDuration4 = ObjectAnimator.ofFloat((Object)this.mSecondRightDrawerDeviceView, View.TRANSLATION_X, new float[] { this.mScreenCenterX - array[0] + this.mSecondRightDotDeviceDeltaXFromCenter - this.mDrawerSpeakerHeight / 3 }).setDuration(600L);
        setDuration4.setStartDelay(800L);
        this.mAnimatorSet.play((Animator)setDuration4);
        final ObjectAnimator setDuration5 = ObjectAnimator.ofFloat((Object)this.mSecondRightDrawerDeviceView, View.TRANSLATION_Y, new float[] { this.mScreenCenterY - array[1] + this.mSecondRightDotDeviceDeltaYFromCenter - this.mDrawerSpeakerSpacing - this.mDrawerSpeakerHeight / 2 }).setDuration(600L);
        setDuration5.setStartDelay(800L);
        this.mAnimatorSet.play((Animator)setDuration5);
        final ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat((Object)this.mSecondRightDrawerDeviceView, View.ALPHA, new float[] { 0.0f });
        ofFloat6.setDuration(200L);
        ofFloat6.setStartDelay(1400L);
        this.mAnimatorSet.play((Animator)ofFloat6);
        final ObjectAnimator ofFloat7 = ObjectAnimator.ofFloat((Object)this.mSecondRightDrawerDeviceView, View.SCALE_X, new float[] { 0.5f });
        ofFloat7.setDuration(200L);
        ofFloat7.setStartDelay(1400L);
        this.mAnimatorSet.play((Animator)ofFloat7);
        final ObjectAnimator ofFloat8 = ObjectAnimator.ofFloat((Object)this.mSecondRightDrawerDeviceView, View.SCALE_Y, new float[] { 0.5f });
        ofFloat8.setDuration(200L);
        ofFloat8.setStartDelay(1400L);
        this.mAnimatorSet.play((Animator)ofFloat8);
        final ObjectAnimator ofFloat9 = ObjectAnimator.ofFloat((Object)this.mFirstRightDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-this.mHalfDrawerSpeakerPlace) });
        ofFloat9.setDuration(800L);
        ofFloat9.setStartDelay(800L);
        this.mAnimatorSet.play((Animator)ofFloat9);
        this.mFirstRightDotDevice.setAlpha(0.0f);
        this.mFirstRightDotDevice.setTranslationX(this.mFirstRightDotDeviceEndX);
        this.mFirstRightDotDevice.setTranslationY(this.mFirstRightDotDeviceEndY);
        final ObjectAnimator setDuration6 = ObjectAnimator.ofFloat((Object)this.mFirstRightDotDevice, View.ALPHA, new float[] { 1.0f }).setDuration(150L);
        setDuration6.setStartDelay(2200L);
        this.mAnimatorSet.play((Animator)setDuration6);
        this.mFirstRightDrawerDeviceView.getLocationInWindow(array);
        final ObjectAnimator setDuration7 = ObjectAnimator.ofFloat((Object)this.mFirstRightDrawerDeviceView, View.ROTATION, new float[] { -15.0f }).setDuration(150L);
        setDuration7.setStartDelay(1600L);
        this.mAnimatorSet.play((Animator)setDuration7);
        final ObjectAnimator setDuration8 = ObjectAnimator.ofFloat((Object)this.mFirstRightDrawerDeviceView, View.TRANSLATION_X, new float[] { this.mScreenCenterX - array[0] + this.mFirstRightDotDeviceDeltaXFromCenter - this.mDrawerSpeakerWidth }).setDuration(600L);
        setDuration8.setStartDelay(1600L);
        this.mAnimatorSet.play((Animator)setDuration8);
        final ObjectAnimator setDuration9 = ObjectAnimator.ofFloat((Object)this.mFirstRightDrawerDeviceView, View.TRANSLATION_Y, new float[] { this.mScreenCenterY - array[1] + this.mFirstRightDotDeviceDeltaYFromCenter - this.mDrawerSpeakerSpacing - this.mDrawerSpeakerHeight / 2 }).setDuration(600L);
        setDuration9.setStartDelay(1600L);
        this.mAnimatorSet.play((Animator)setDuration9);
        final ObjectAnimator ofFloat10 = ObjectAnimator.ofFloat((Object)this.mFirstRightDrawerDeviceView, View.ALPHA, new float[] { 0.0f });
        ofFloat10.setDuration(200L);
        ofFloat10.setStartDelay(2200L);
        this.mAnimatorSet.play((Animator)ofFloat10);
        final ObjectAnimator ofFloat11 = ObjectAnimator.ofFloat((Object)this.mFirstRightDrawerDeviceView, View.SCALE_X, new float[] { 0.5f });
        ofFloat11.setDuration(200L);
        ofFloat11.setStartDelay(2200L);
        this.mAnimatorSet.play((Animator)ofFloat11);
        final ObjectAnimator ofFloat12 = ObjectAnimator.ofFloat((Object)this.mFirstRightDrawerDeviceView, View.SCALE_Y, new float[] { 0.5f });
        ofFloat12.setDuration(200L);
        ofFloat12.setStartDelay(2200L);
        this.mAnimatorSet.play((Animator)ofFloat12);
        this.mAnimatorSet.start();
    }
    
    private void transformationFirstSecond(final float n) {
        this.mCurrentPageIndicator.setTranslationX(-0.75f * this.mPageIndicatorWidth + (1.0f - n) * 0.5f * this.mPageIndicatorWidth - this.mPageIndicatorDotSize / 2);
    }
    
    private void transformationFourthToThird(final float alpha) {
        if (this.mAnimatorSet.isRunning()) {
            this.mAnimatorSet.cancel();
            this.mMasterDeviceView.setRotation(0.0f);
            this.mMasterDeviceView.setTranslationX(0.0f);
            this.mMasterDeviceView.setTranslationY(0.0f);
            this.mFirstLeftDrawerDeviceView.setAlpha(0.0f);
            this.mFirstLeftDrawerDeviceView.setTranslationY(0.0f);
            this.mFirstRightDrawerDeviceView.setAlpha(1.0f);
            this.mFirstRightDrawerDeviceView.setTranslationY(0.0f);
            this.mSecondLeftDrawerDeviceView.setAlpha(1.0f);
            this.mSecondLeftDrawerDeviceView.setTranslationY(0.0f);
            this.mSecondRightDrawerDeviceView.setAlpha(1.0f);
            this.mSecondRightDrawerDeviceView.setTranslationY(0.0f);
        }
        this.mCurrentPageIndicator.setTranslationX(0.25f * this.mPageIndicatorWidth + (1.0f - alpha) * 0.5f * this.mPageIndicatorWidth - this.mPageIndicatorDotSize / 2);
        this.mFirstLeftDrawerDeviceView.setTranslationY(this.mDrawerSpeakerHeight * alpha);
        this.mFirstRightDrawerDeviceView.setTranslationX(-alpha * this.mHalfDrawerSpeakerPlace);
        this.mSecondRightDrawerDeviceView.setTranslationX(-alpha * this.mHalfDrawerSpeakerPlace);
        this.mSecondLeftDrawerDeviceView.setTranslationX(this.mHalfDrawerSpeakerPlace * alpha);
        this.mFirstLeftDotDevice.setTranslationX(this.mFirstLeftDotDeviceEndX);
        this.mFirstLeftDotDevice.setTranslationY(this.mFirstLeftDotDeviceEndY);
        this.mFirstLeftDotDevice.setAlpha(alpha);
    }
    
    private void transformationSecondFirst(final float alpha) {
        this.mAnimatorSet.cancel();
        this.mCurrentPageIndicator.setTranslationX(-0.75f * this.mPageIndicatorWidth + (1.0f - alpha) * 0.5f * this.mPageIndicatorWidth - this.mPageIndicatorDotSize / 2);
        this.mFirstLeftDotDevice.setAlpha(1.0f - alpha);
        this.mFirstLeftDrawerDeviceView.setTranslationX(0.0f);
        this.mFirstLeftDrawerDeviceView.setTranslationY(0.0f);
        this.mFirstLeftDrawerDeviceView.setRotation(0.0f);
        this.mFirstLeftDrawerDeviceView.setScaleX(alpha);
        this.mFirstLeftDrawerDeviceView.setScaleY(alpha);
        this.mFirstLeftDrawerDeviceView.setAlpha(alpha);
        this.mFirstRightDrawerDeviceView.setTranslationX(-(1.0f - alpha) * this.mHalfDrawerSpeakerPlace);
        this.mSecondRightDrawerDeviceView.setTranslationX(-(1.0f - alpha) * this.mHalfDrawerSpeakerPlace);
        this.mSecondLeftDrawerDeviceView.setTranslationX((1.0f - alpha) * this.mHalfDrawerSpeakerPlace);
    }
    
    private void transformationSecondToThird(final float n) {
        if (this.mAnimatorSet.isRunning()) {
            this.mAnimatorSet.cancel();
            this.mFirstLeftDotDevice.setAlpha(1.0f);
            this.mFirstLeftDrawerDeviceView.setAlpha(0.0f);
        }
        this.mCurrentPageIndicator.setTranslationX(-0.25f * this.mPageIndicatorWidth + (1.0f - n) * 0.5f * this.mPageIndicatorWidth - this.mPageIndicatorDotSize / 2);
    }
    
    private void transformationThirdToFourth(final float n) {
        if (this.mAnimatorSet.isRunning()) {
            this.mAnimatorSet.cancel();
            this.mFirstLeftDotDevice.setAlpha(1.0f);
            this.mFirstRightDotDevice.setAlpha(1.0f);
            this.mSecondLeftDotDevice.setAlpha(1.0f);
            this.mSecondRightDotDevice.setAlpha(1.0f);
            this.mFirstLeftDrawerDeviceView.setAlpha(0.0f);
            this.mFirstRightDrawerDeviceView.setAlpha(0.0f);
            this.mSecondLeftDrawerDeviceView.setAlpha(0.0f);
            this.mSecondRightDrawerDeviceView.setAlpha(0.0f);
        }
        this.mCurrentPageIndicator.setTranslationX(0.25f * this.mPageIndicatorWidth + (1.0f - n) * 0.5f * this.mPageIndicatorWidth - this.mPageIndicatorDotSize / 2);
    }
    
    private void transformationThirdToSecond(final float n) {
        this.mAnimatorSet.cancel();
        this.mCurrentPageIndicator.setTranslationX(-0.25f * this.mPageIndicatorWidth + (1.0f - n) * 0.5f * this.mPageIndicatorWidth - this.mPageIndicatorDotSize / 2);
        this.mFirstLeftDotDevice.setAlpha(1.0f - n);
        this.mFirstLeftDrawerDeviceView.setTranslationX(0.0f);
        this.mFirstLeftDrawerDeviceView.setTranslationY((1.0f - n) * this.mDrawerSpeakerHeight);
        this.mFirstLeftDrawerDeviceView.setRotation(0.0f);
        this.mFirstLeftDrawerDeviceView.setScaleX(1.0f);
        this.mFirstLeftDrawerDeviceView.setScaleY(1.0f);
        this.mFirstLeftDrawerDeviceView.setAlpha(n);
        this.mSecondLeftDotDevice.setAlpha(1.0f - n);
        this.mSecondLeftDrawerDeviceView.setTranslationX(0.0f);
        this.mSecondLeftDrawerDeviceView.setTranslationY((1.0f - n) * this.mDrawerSpeakerHeight);
        this.mSecondLeftDrawerDeviceView.setRotation(0.0f);
        this.mSecondLeftDrawerDeviceView.setScaleX(1.0f);
        this.mSecondLeftDrawerDeviceView.setScaleY(1.0f);
        this.mSecondLeftDrawerDeviceView.setAlpha(n);
        this.mFirstRightDotDevice.setAlpha(1.0f - n);
        this.mFirstRightDrawerDeviceView.setTranslationX(0.0f);
        this.mFirstRightDrawerDeviceView.setTranslationY((1.0f - n) * this.mDrawerSpeakerHeight);
        this.mFirstRightDrawerDeviceView.setRotation(0.0f);
        this.mFirstRightDrawerDeviceView.setScaleX(1.0f);
        this.mFirstRightDrawerDeviceView.setScaleY(1.0f);
        this.mFirstRightDrawerDeviceView.setAlpha(n);
        this.mSecondRightDotDevice.setAlpha(1.0f - n);
        this.mSecondRightDrawerDeviceView.setTranslationX(0.0f);
        this.mSecondRightDrawerDeviceView.setTranslationY((1.0f - n) * this.mDrawerSpeakerHeight);
        this.mSecondRightDrawerDeviceView.setRotation(0.0f);
        this.mSecondRightDrawerDeviceView.setScaleX(1.0f);
        this.mSecondRightDrawerDeviceView.setScaleY(1.0f);
        this.mSecondRightDrawerDeviceView.setAlpha(n);
    }
    
    public void closeXUPTutorial() {
        this.finish();
    }
    
    public void finish() {
        super.finish();
        this.overridePendingTransition(2131034132, 2131034128);
    }
    
    @OnClick({ 2131624077 })
    public void onCloseClicked(final View view) {
        this.closeXUPTutorial();
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130968608);
        ButterKnife.bind(this);
        this.overridePendingTransition(2131034124, 2131034132);
        final Display defaultDisplay = this.getWindowManager().getDefaultDisplay();
        final Point point = new Point();
        defaultDisplay.getSize(point);
        this.mScreenWidth = point.x;
        this.mScreenHeight = point.y;
        this.mScreenCenterX = (float)(this.mScreenWidth / 2);
        this.mScreenCenterY = (float)(this.mScreenHeight / 2);
        this.mMasterDeviceView.setDeviceColor(XUPTutorialActivity.COLOR_MASTER, false);
        this.mFirstRightDrawerDeviceView.setDeviceColor(XUPTutorialActivity.COLOR_FIRST, false);
        this.mFirstLeftDrawerDeviceView.setDeviceColor(XUPTutorialActivity.COLOR_SECOND, false);
        this.mSecondRightDrawerDeviceView.setDeviceColor(XUPTutorialActivity.COLOR_THIRD, false);
        this.mSecondLeftDrawerDeviceView.setDeviceColor(XUPTutorialActivity.COLOR_FOURTH, false);
        this.mFirstRightDrawerDeviceView.measure(-2, -2);
        this.mDrawerSpeakerHeight = this.mFirstRightDrawerDeviceView.getMeasuredHeight();
        this.mMasterDeviceView.measure(-2, -2);
        this.mSpeakerWidth = this.mMasterDeviceView.getMeasuredWidth();
        this.mSpeakerHeight = this.mMasterDeviceView.getMeasuredHeight();
        this.mPageIndicatorWidth = 0.5f * this.mSpeakerWidth;
        this.mDrawerSpeakerPlace = this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2;
        this.mHalfDrawerSpeakerPlace = this.mDrawerSpeakerPlace / 2;
        this.mTutorialViewPager.setAdapter(new TutorialPagerAdapter((Context)this));
        this.mTutorialViewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener)new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(final int n) {
                if (n == 0) {
                    XUPTutorialActivity.this.mSwipeDirection = SwipeDirection.NONE;
                    XUPTutorialActivity.this.mPrevPositivePosition = -1.0f;
                    switch (XUPTutorialActivity.this.mTutorialViewPager.getCurrentItem()) {
                        case 0: {
                            XUPTutorialActivity.this.completeFirstPage();
                            XUPTutorialActivity.this.mCurrentPage = 0;
                            break;
                        }
                        case 1: {
                            if (XUPTutorialActivity.this.mCurrentPage == 1 || XUPTutorialActivity.this.mCurrentPage == 2) {
                                XUPTutorialActivity.this.initSecondPage();
                                XUPTutorialActivity.this.startSecondPageAnimation();
                            }
                            else {
                                XUPTutorialActivity.this.startSecondPageAnimation();
                            }
                            XUPTutorialActivity.this.mCurrentPage = 1;
                            break;
                        }
                        case 2: {
                            if (XUPTutorialActivity.this.mCurrentPage == 1) {
                                XUPTutorialActivity.this.initThirdPage();
                            }
                            else {
                                XUPTutorialActivity.this.completeThirdPage();
                            }
                            XUPTutorialActivity.this.startThirdPageAnimation();
                            XUPTutorialActivity.this.mCurrentPage = 2;
                            break;
                        }
                        case 3: {
                            if (XUPTutorialActivity.this.mCurrentPage != 3) {
                                XUPTutorialActivity.this.startFourthPageAnimation();
                            }
                            XUPTutorialActivity.this.mCurrentPage = 3;
                            break;
                        }
                    }
                }
            }
            
            @Override
            public void onPageScrolled(final int n, final float n2, final int n3) {
                super.onPageScrolled(n, n2, n3);
            }
            
            @Override
            public void onPageSelected(final int n) {
                super.onPageSelected(n);
            }
        });
        this.mTutorialViewPager.setPageTransformer(true, (ViewPager.PageTransformer)new TutorialPageTransformer());
        this.createDrawerDotDevices();
        this.createPageIndicatorViews();
    }
    
    public void onWindowFocusChanged(final boolean b) {
        super.onWindowFocusChanged(b);
        this.initDrawerDotDevices();
    }
    
    private enum SwipeDirection
    {
        BACKWARD, 
        FORWARD, 
        NONE;
    }
    
    class TutorialPageTransformer implements PageTransformer
    {
        @Override
        public void transformPage(final View view, final float n) {
            XUPTutorialActivity.this.mPageWidth = view.getWidth();
            if (n > -1.0f && n < 0.0f) {
                switch (XUPTutorialActivity.this.mCurrentPage) {
                }
            }
            else if (n > 0.0f && n < 1.0f) {
                if (XUPTutorialActivity.this.mSwipeDirection == SwipeDirection.NONE && XUPTutorialActivity.this.mPrevPositivePosition > 0.0f) {
                    if (n < XUPTutorialActivity.this.mPrevPositivePosition) {
                        XUPTutorialActivity.this.mSwipeDirection = SwipeDirection.FORWARD;
                    }
                    else if (n > XUPTutorialActivity.this.mPrevPositivePosition) {
                        XUPTutorialActivity.this.mSwipeDirection = SwipeDirection.BACKWARD;
                    }
                }
                switch (XUPTutorialActivity.this.mCurrentPage) {
                    case 0: {
                        XUPTutorialActivity.this.transformationFirstSecond(n);
                        break;
                    }
                    case 1: {
                        if (XUPTutorialActivity.this.mSwipeDirection == SwipeDirection.FORWARD) {
                            XUPTutorialActivity.this.transformationSecondToThird(n);
                            break;
                        }
                        if (XUPTutorialActivity.this.mSwipeDirection == SwipeDirection.BACKWARD) {
                            XUPTutorialActivity.this.transformationSecondFirst(n);
                            break;
                        }
                        break;
                    }
                    case 2: {
                        if (XUPTutorialActivity.this.mSwipeDirection == SwipeDirection.FORWARD) {
                            XUPTutorialActivity.this.transformationThirdToFourth(n);
                            break;
                        }
                        if (XUPTutorialActivity.this.mSwipeDirection == SwipeDirection.BACKWARD) {
                            XUPTutorialActivity.this.transformationThirdToSecond(n);
                            break;
                        }
                        break;
                    }
                    case 3: {
                        if (XUPTutorialActivity.this.mCurrentPage != 2) {
                            XUPTutorialActivity.this.transformationFourthToThird(n);
                            break;
                        }
                        break;
                    }
                }
                XUPTutorialActivity.this.mPrevPositivePosition = n;
            }
        }
    }
    
    public class TutorialPagerAdapter extends PagerAdapter
    {
        private Context mContext;
        
        public TutorialPagerAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public void destroyItem(final ViewGroup viewGroup, final int n, final Object o) {
            viewGroup.removeView((View)o);
        }
        
        @Override
        public int getCount() {
            return 4;
        }
        
        @Override
        public Object instantiateItem(final ViewGroup viewGroup, final int n) {
            final ViewGroup viewGroup2 = (ViewGroup)LayoutInflater.from(this.mContext).inflate(2130968676, viewGroup, false);
            ((TextView)viewGroup2.findViewById(2131624247)).setText((CharSequence)XUPTutorialActivity.this.getString(XUPTutorialActivity.this.PAGES_TITLES_IDS[n]));
            viewGroup.addView((View)viewGroup2);
            return viewGroup2;
        }
        
        @Override
        public boolean isViewFromObject(final View view, final Object o) {
            return view == o;
        }
    }
}
