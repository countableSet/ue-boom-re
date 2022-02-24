// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.activities;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import butterknife.OnClick;
import com.logitech.ue.UserPreferences;
import android.view.Display;
import android.graphics.Point;
import android.support.v4.view.PagerAdapter;
import android.app.Activity;
import butterknife.ButterKnife;
import android.os.Bundle;
import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ObjectAnimator;
import android.view.animation.AccelerateInterpolator;
import java.util.Collection;
import java.util.Iterator;
import android.view.ViewGroup$LayoutParams;
import android.widget.RelativeLayout$LayoutParams;
import android.content.Context;
import java.util.ArrayList;
import com.logitech.ue.centurion.device.devicedata.UEColour;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;
import android.view.View;
import android.widget.Button;
import butterknife.BindDimen;
import java.util.List;
import android.widget.ImageView;
import butterknife.Bind;
import android.widget.TextView;
import android.animation.AnimatorSet;
import com.logitech.ue.views.UEDeviceView;

public class XUPOnBoardingActivity extends BaseActivity
{
    private static final float BIG_DOT_SCALE = 1.3f;
    private static final int COLOR_FIFTH;
    private static final int COLOR_FIRST;
    private static final int COLOR_FOURTH;
    private static final int COLOR_SECOND;
    private static final int COLOR_THIRD;
    private static final int DEVICES_COUNT = 5;
    private static final float DRAG_ROTATION = 15.0f;
    private static final int PAGES_COUNT = 4;
    private static final int PAGE_FIRST = 0;
    private static final int PAGE_FOURTH = 3;
    private static final int PAGE_SECOND = 1;
    private static final int PAGE_THIRD = 2;
    public static final String TAG;
    private float[] devicePage0CoordX;
    private float[] devicePage1CoordX;
    private UEDeviceView[] devicesViews;
    private int mAnimationDuration;
    AnimatorSet mAnimatorSetDragNDrop;
    private AnimatorSet mAnimatorSetIntro;
    @Bind({ 2131624071 })
    TextView mBrettsBoomCloud;
    private float mBrettsBoomCloudHeight;
    private float mBrettsBoomCloudWidth;
    private int mCurrentPage;
    private ImageView mCurrentPageIndicator;
    private List<ImageView> mDotDevices;
    private List<UEDeviceView> mDrawerDevices;
    @BindDimen(2131361924)
    int mDrawerHeight;
    private int mDrawerSpeakerHeight;
    @BindDimen(2131361870)
    int mDrawerSpeakerSpacing;
    @BindDimen(2131361915)
    int mDrawerSpeakerWidth;
    @Bind({ 2131624064 })
    UEDeviceView mEighthDrawerDeviceView;
    @Bind({ 2131624067 })
    UEDeviceView mEleventhDrawerDeviceView;
    @Bind({ 2131624051 })
    UEDeviceView mFifthDeviceView;
    @Bind({ 2131624061 })
    UEDeviceView mFifthDrawerDeviceView;
    @Bind({ 2131624050 })
    UEDeviceView mFirstDeviceView;
    private ImageView mFirstDotDevice;
    private float mFirstDotDeviceDeltaXFromCenter;
    private float mFirstDotDeviceDeltaYFromCenter;
    private float mFirstDotDeviceEndX;
    private float mFirstDotDeviceEndY;
    @Bind({ 2131624057 })
    UEDeviceView mFirstDrawerDeviceView;
    @Bind({ 2131624054 })
    UEDeviceView mFourthDeviceView;
    @Bind({ 2131624060 })
    UEDeviceView mFourthDrawerDeviceView;
    @Bind({ 2131624072 })
    Button mGetThePartyStartedButton;
    @BindDimen(2131361881)
    int mGlobalPadding;
    @Bind({ 2131624065 })
    UEDeviceView mNinthDrawerDeviceView;
    @Bind({ 2131624056 })
    View mOnBoardingDrawerView;
    @Bind({ 2131624048 })
    RelativeLayout mOnBoardingMainView;
    private OnBoardingPageTransformer mOnBoardingPageTransformer;
    private OnBoardingPagerAdapter mOnBoardingPagerAdapter;
    @Bind({ 2131624049 })
    ViewPager mOnBoardingViewPager;
    private List<ImageView> mPageIndicatorViews;
    private float mPageIndicatorWidth;
    private int mPageWidth;
    private float mPrevPositivePosition;
    private float mScreenCenterX;
    private float mScreenCenterY;
    private int mScreenHeight;
    private int mScreenWidth;
    @Bind({ 2131624070 })
    TextView mSeansBoomCloud;
    private float mSeansBoomCloudHeight;
    private float mSeansBoomCloudWidth;
    @Bind({ 2131624052 })
    UEDeviceView mSecondDeviceView;
    private ImageView mSecondDotDevice;
    private float mSecondDotDeviceDeltaXFromCenter;
    private float mSecondDotDeviceDeltaYFromCenter;
    private float mSecondDotDeviceEndX;
    private float mSecondDotDeviceEndY;
    @Bind({ 2131624058 })
    UEDeviceView mSecondDrawerDeviceView;
    @Bind({ 2131624063 })
    UEDeviceView mSeventhDrawerDeviceView;
    @Bind({ 2131624062 })
    UEDeviceView mSixthDrawerDeviceView;
    private int mSpeakerHeight;
    private int mSpeakerWidth;
    @Bind({ 2131624053 })
    ImageView mSwapIcon;
    private SwipeDirection mSwipeDirection;
    @Bind({ 2131624066 })
    UEDeviceView mTenthDrawerDeviceView;
    @Bind({ 2131624055 })
    UEDeviceView mThirdDeviceView;
    private ImageView mThirdDotDevice;
    private float mThirdDotDeviceDeltaXFromCenter;
    private float mThirdDotDeviceDeltaYFromCenter;
    private float mThirdDotDeviceEndX;
    private float mThirdDotDeviceEndY;
    @Bind({ 2131624059 })
    UEDeviceView mThirdDrawerDeviceView;
    private int mTranslationDistance;
    @Bind({ 2131624068 })
    UEDeviceView mTwelfthDrawerDeviceView;
    @Bind({ 2131624069 })
    TextView mZacksBoomCloud;
    private float mZacksBoomCloudHeight;
    private float mZacksBoomCloudWidth;
    private final int[] pagesTitleIds;
    
    static {
        TAG = XUPOnBoardingActivity.class.getSimpleName();
        COLOR_FIRST = UEColour.MAXIMUS_WHITE_WHITE_BLACK.getCode();
        COLOR_SECOND = UEColour.MAXIMUS_TEAL_GREEN_YELLOW.getCode();
        COLOR_THIRD = UEColour.MAXIMUS_VIOLET_ORANGE_YELLOW.getCode();
        COLOR_FOURTH = UEColour.MAXIMUS_BLUE_TEAL_RED.getCode();
        COLOR_FIFTH = UEColour.MAXIMUS_RED_PINK_BLUE.getCode();
    }
    
    public XUPOnBoardingActivity() {
        this.devicesViews = new UEDeviceView[5];
        this.pagesTitleIds = new int[] { 2131165223, 2131165338, 2131165337, 2131165286 };
        this.mDrawerDevices = new ArrayList<UEDeviceView>(12);
        this.mDotDevices = new ArrayList<ImageView>(20);
        this.mAnimatorSetIntro = new AnimatorSet();
        this.mAnimatorSetDragNDrop = new AnimatorSet();
        this.mCurrentPage = 0;
        this.devicePage0CoordX = new float[5];
        this.devicePage1CoordX = new float[5];
        this.mPrevPositivePosition = -1.0f;
        this.mSwipeDirection = SwipeDirection.NONE;
        this.mPageIndicatorViews = new ArrayList<ImageView>(5);
    }
    
    private void completeFirstPage() {
        this.mCurrentPageIndicator.setTranslationX(-0.75f * this.mPageIndicatorWidth);
        this.initDotDevices();
        this.mSwapIcon.setAlpha(0.0f);
        this.mOnBoardingDrawerView.setTranslationY((float)this.mDrawerHeight);
        this.mFirstDeviceView.setTranslationX(this.devicePage0CoordX[0]);
        this.mSecondDeviceView.setTranslationX(this.devicePage0CoordX[1]);
        this.mThirdDeviceView.setTranslationX(this.devicePage0CoordX[2]);
        this.mFourthDeviceView.setTranslationX(this.devicePage0CoordX[3]);
        this.mFifthDeviceView.setTranslationX(this.devicePage0CoordX[4]);
    }
    
    private void completeFourthPage() {
        this.mZacksBoomCloud.setTranslationX(0.0f);
        this.mZacksBoomCloud.setTranslationY(0.0f);
        this.mZacksBoomCloud.setAlpha(0.0f);
        this.mSeansBoomCloud.setTranslationX(0.0f);
        this.mSeansBoomCloud.setTranslationY(0.0f);
        this.mSeansBoomCloud.setAlpha(0.0f);
        this.mBrettsBoomCloud.setTranslationX(0.0f);
        this.mBrettsBoomCloud.setTranslationY(0.0f);
        this.mBrettsBoomCloud.setAlpha(0.0f);
        this.mFirstDotDevice.setTranslationX(this.mFirstDotDeviceEndX);
        this.mFirstDotDevice.setTranslationY(this.mFirstDotDeviceEndY);
        this.mFirstDotDevice.setScaleX(1.0f);
        this.mFirstDotDevice.setScaleY(1.0f);
        this.mFirstDotDevice.setAlpha(1.0f);
        this.mSecondDotDevice.setTranslationX(this.mSecondDotDeviceEndX);
        this.mSecondDotDevice.setTranslationY(this.mSecondDotDeviceEndY);
        this.mSecondDotDevice.setScaleX(1.0f);
        this.mSecondDotDevice.setScaleY(1.0f);
        this.mSecondDotDevice.setAlpha(1.0f);
        this.mThirdDotDevice.setTranslationX(this.mThirdDotDeviceEndX);
        this.mThirdDotDevice.setTranslationY(this.mThirdDotDeviceEndY);
        this.mThirdDotDevice.setScaleX(1.0f);
        this.mThirdDotDevice.setScaleY(1.0f);
        this.mThirdDotDevice.setAlpha(1.0f);
        this.mCurrentPageIndicator.setTranslationX(0.75f * this.mPageIndicatorWidth);
        this.mSwapIcon.setAlpha(0.0f);
        this.mOnBoardingDrawerView.setTranslationY((float)this.mDrawerHeight);
        this.setDrawerDevicesTranslationY(0.0f);
        this.initDrawerDevices();
        this.mGetThePartyStartedButton.setTranslationY(0.0f);
        this.setDotDevicesAlpha(1.0f);
        this.setDotDevicesScale(1.0f);
        this.mThirdDeviceView.setTranslationX(0.0f);
        this.mFourthDeviceView.setTranslationX(0.0f);
        this.restoreDraggedDevicesScale();
    }
    
    private void completeSecondPage() {
        this.mCurrentPageIndicator.setTranslationX(-0.25f * this.mPageIndicatorWidth);
        this.mSwapIcon.setAlpha(1.0f);
        this.initDotDevices();
        this.mOnBoardingDrawerView.setTranslationY((float)this.mDrawerHeight);
        this.initDrawerDevices();
        this.devicePage1CoordX[0] = this.devicePage0CoordX[0] + (float)(-1.25 * this.mPageWidth);
        this.devicePage1CoordX[1] = this.devicePage0CoordX[1] + (float)(-1.55 * this.mPageWidth);
        this.devicePage1CoordX[2] = this.devicePage0CoordX[2] + -this.mSpeakerWidth;
        this.devicePage1CoordX[3] = this.devicePage0CoordX[3] + (float)(0.25 * this.mSpeakerWidth);
        this.devicePage1CoordX[4] = this.devicePage0CoordX[4] + (float)(-2.25 * this.mPageWidth);
        this.mFirstDeviceView.setTranslationX(this.devicePage1CoordX[0]);
        this.mSecondDeviceView.setTranslationX(this.devicePage1CoordX[1]);
        this.mThirdDeviceView.setTranslationX(this.devicePage1CoordX[2]);
        this.mFourthDeviceView.setTranslationX(this.devicePage1CoordX[3]);
        this.mFifthDeviceView.setTranslationX(this.devicePage1CoordX[4]);
        this.restoreDraggedDevicesScale();
    }
    
    private void completeThirdPage() {
        this.mZacksBoomCloud.setTranslationX(0.0f);
        this.mZacksBoomCloud.setTranslationY(0.0f);
        this.mZacksBoomCloud.setAlpha(0.0f);
        this.mSeansBoomCloud.setTranslationX(0.0f);
        this.mSeansBoomCloud.setTranslationY(0.0f);
        this.mSeansBoomCloud.setAlpha(0.0f);
        this.mBrettsBoomCloud.setTranslationX(0.0f);
        this.mBrettsBoomCloud.setTranslationY(0.0f);
        this.mBrettsBoomCloud.setAlpha(0.0f);
        this.mCurrentPageIndicator.setTranslationX(0.25f * this.mPageIndicatorWidth);
        this.mSwapIcon.setAlpha(0.0f);
        this.mOnBoardingDrawerView.setTranslationY(0.0f);
        this.setDrawerDevicesTranslationY((float)(-this.mDrawerHeight));
        this.mGetThePartyStartedButton.setTranslationY((float)(this.mDrawerHeight * 2 + this.mGlobalPadding));
        this.setDotDevicesAlpha(0.0f);
        this.setDotDevicesScale(1.0f);
        this.mThirdDeviceView.setTranslationX(0.0f);
        this.mFourthDeviceView.setTranslationX(0.0f);
        this.restoreDraggedDevicesScale();
    }
    
    private ImageView createDotDevice(final int imageResource) {
        final ImageView imageView = new ImageView((Context)this);
        imageView.setVisibility(0);
        imageView.setAlpha(0.0f);
        imageView.setImageResource(imageResource);
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams((int)this.getResources().getDimension(2131361873), (int)this.getResources().getDimension(2131361873));
        relativeLayout$LayoutParams.addRule(13, -1);
        this.mOnBoardingMainView.addView((View)imageView, (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
        return imageView;
    }
    
    private List<ImageView> createDotDevices(final int initialCapacity, final int n) {
        final ArrayList<ImageView> list = new ArrayList<ImageView>(initialCapacity);
        for (int i = 0; i < initialCapacity; ++i) {
            list.add(this.createDotDevice(n));
        }
        return list;
    }
    
    private void createDrawerDotDevices() {
        this.mFirstDotDevice = this.createDotDevice(2130837691);
        final RelativeLayout$LayoutParams layoutParams = new RelativeLayout$LayoutParams(this.mFifthDrawerDeviceView.getLayoutParams());
        layoutParams.height = (int)this.getResources().getDimension(2131361873);
        layoutParams.width = (int)this.getResources().getDimension(2131361874);
        layoutParams.addRule(12, 2131624048);
        this.mFirstDotDevice.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        this.mFirstDotDevice.setAlpha(0.0f);
        this.mSecondDotDevice = this.createDotDevice(2130837700);
        final RelativeLayout$LayoutParams layoutParams2 = new RelativeLayout$LayoutParams(this.mSecondDrawerDeviceView.getLayoutParams());
        layoutParams2.height = (int)this.getResources().getDimension(2131361873);
        layoutParams2.width = (int)this.getResources().getDimension(2131361874);
        layoutParams2.addRule(12, 2131624048);
        this.mSecondDotDevice.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
        this.mSecondDotDevice.setAlpha(0.0f);
        this.mThirdDotDevice = this.createDotDevice(2130837694);
        final RelativeLayout$LayoutParams layoutParams3 = new RelativeLayout$LayoutParams(this.mEighthDrawerDeviceView.getLayoutParams());
        layoutParams3.height = (int)this.getResources().getDimension(2131361873);
        layoutParams3.width = (int)this.getResources().getDimension(2131361874);
        layoutParams3.addRule(12, 2131624048);
        this.mThirdDotDevice.setLayoutParams((ViewGroup$LayoutParams)layoutParams3);
        this.mThirdDotDevice.setAlpha(0.0f);
    }
    
    private ImageView createPageIndicatorView(final int imageResource, final float translationX) {
        final ImageView imageView = new ImageView((Context)this);
        imageView.setVisibility(0);
        imageView.setAlpha(1.0f);
        imageView.setImageResource(imageResource);
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(this.mFifthDrawerDeviceView.getLayoutParams());
        relativeLayout$LayoutParams.height = (int)this.getResources().getDimension(2131361907);
        relativeLayout$LayoutParams.width = (int)this.getResources().getDimension(2131361907);
        relativeLayout$LayoutParams.addRule(10, -1);
        relativeLayout$LayoutParams.addRule(14, -1);
        this.mOnBoardingMainView.addView((View)imageView, (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
        imageView.setTranslationX(translationX);
        imageView.setTranslationY((float)(int)this.getResources().getDimension(2131361908));
        return imageView;
    }
    
    private void createPageIndicatorViews() {
        for (int i = 0; i < 4; ++i) {
            this.mPageIndicatorViews.add(this.createPageIndicatorView(2130837593, (0.5f * i - 0.75f) * this.mPageIndicatorWidth));
        }
        final ImageView pageIndicatorView = this.createPageIndicatorView(2130837594, this.mPageIndicatorWidth * -0.75f);
        this.mCurrentPageIndicator = pageIndicatorView;
        this.mPageIndicatorViews.add(pageIndicatorView);
    }
    
    private void initDotDevices() {
        this.mFirstDotDevice.setTranslationX(0.0f);
        this.mFirstDotDevice.setTranslationY(0.0f);
        this.mFirstDotDevice.setAlpha(0.0f);
        this.mSecondDotDevice.setTranslationX(0.0f);
        this.mSecondDotDevice.setTranslationY(0.0f);
        this.mSecondDotDevice.setAlpha(0.0f);
        this.mThirdDotDevice.setTranslationX(0.0f);
        this.mThirdDotDevice.setTranslationY(0.0f);
        this.mThirdDotDevice.setAlpha(0.0f);
    }
    
    private void initDrawerDevices() {
        this.mZacksBoomCloud.setTranslationX(0.0f);
        this.mZacksBoomCloud.setTranslationY(0.0f);
        this.mZacksBoomCloud.setAlpha(0.0f);
        this.mSeansBoomCloud.setTranslationX(0.0f);
        this.mSeansBoomCloud.setTranslationY(0.0f);
        this.mSeansBoomCloud.setAlpha(0.0f);
        this.mBrettsBoomCloud.setTranslationX(0.0f);
        this.mBrettsBoomCloud.setTranslationY(0.0f);
        this.mBrettsBoomCloud.setAlpha(0.0f);
        for (final UEDeviceView ueDeviceView : this.mDrawerDevices) {
            ueDeviceView.setTranslationX(0.0f);
            ueDeviceView.setTranslationY((float)this.mDrawerHeight);
            ueDeviceView.setRotation(0.0f);
            ueDeviceView.setScaleX(1.0f);
            ueDeviceView.setScaleY(1.0f);
            ueDeviceView.setAlpha(1.0f);
        }
    }
    
    private void initDrawerDotDevices() {
        final int[] array = new int[2];
        this.mFirstDotDeviceDeltaXFromCenter = 1.25f * this.mSpeakerWidth;
        this.mFirstDotDeviceDeltaYFromCenter = 0.85f * this.mSpeakerHeight;
        this.mFirstDotDevice.getLocationInWindow(array);
        this.mFirstDotDeviceEndX = this.mScreenCenterX - array[0] + this.mFirstDotDeviceDeltaXFromCenter;
        this.mFirstDotDeviceEndY = this.mScreenCenterY - array[1] + this.mFirstDotDeviceDeltaYFromCenter;
        this.mSecondDotDeviceDeltaXFromCenter = -2.0f * this.mSpeakerWidth;
        this.mSecondDotDeviceDeltaYFromCenter = 0.35f * this.mSpeakerHeight;
        this.mSecondDotDevice.getLocationInWindow(array);
        this.mSecondDotDeviceEndX = this.mScreenCenterX - array[0] + this.mSecondDotDeviceDeltaXFromCenter;
        this.mSecondDotDeviceEndY = this.mScreenCenterY - array[1] + this.mSecondDotDeviceDeltaYFromCenter;
        this.mThirdDotDeviceDeltaXFromCenter = 0.9f * this.mSpeakerWidth;
        this.mThirdDotDeviceDeltaYFromCenter = -0.25f * this.mSpeakerHeight;
        this.mThirdDotDevice.getLocationInWindow(array);
        this.mThirdDotDeviceEndX = this.mScreenCenterX - array[0] + this.mThirdDotDeviceDeltaXFromCenter;
        this.mThirdDotDeviceEndY = this.mScreenCenterY - array[1] + this.mThirdDotDeviceDeltaYFromCenter;
    }
    
    private void initalThirdPage() {
        this.mZacksBoomCloud.setTranslationX(0.0f);
        this.mZacksBoomCloud.setTranslationY(0.0f);
        this.mZacksBoomCloud.setAlpha(0.0f);
        this.mSeansBoomCloud.setTranslationX(0.0f);
        this.mSeansBoomCloud.setTranslationY(0.0f);
        this.mSeansBoomCloud.setAlpha(0.0f);
        this.mBrettsBoomCloud.setTranslationX(0.0f);
        this.mBrettsBoomCloud.setTranslationY(0.0f);
        this.mBrettsBoomCloud.setAlpha(0.0f);
        this.mCurrentPageIndicator.setTranslationX(0.25f * this.mPageIndicatorWidth);
        this.mSwapIcon.setAlpha(0.0f);
        this.mOnBoardingDrawerView.setTranslationY(0.0f);
        this.setDrawerDevicesTranslationY((float)(-this.mDrawerHeight));
        this.mGetThePartyStartedButton.setTranslationY((float)(this.mDrawerHeight * 2 + this.mGlobalPadding));
        this.setDotDevicesAlpha(0.0f);
        this.setDotDevicesScale(1.0f);
        this.mFirstDotDevice.setTranslationX(0.0f);
        this.mFirstDotDevice.setTranslationY(0.0f);
        this.mFirstDotDevice.setAlpha(0.0f);
        this.mSecondDotDevice.setTranslationX(0.0f);
        this.mSecondDotDevice.setTranslationY(0.0f);
        this.mSecondDotDevice.setAlpha(0.0f);
        this.mThirdDotDevice.setTranslationX(0.0f);
        this.mThirdDotDevice.setTranslationY(0.0f);
        this.mThirdDotDevice.setAlpha(0.0f);
        this.mThirdDeviceView.setTranslationX(0.0f);
        this.mFourthDeviceView.setTranslationX(0.0f);
        this.restoreDraggedDevicesScale();
    }
    
    private void place60DotDevices() {
        final List<ImageView> dotDevices = this.createDotDevices(2, 2130837688);
        final ImageView imageView = dotDevices.get(0);
        imageView.setTranslationX((float)(int)(-1.85f * this.mSpeakerWidth));
        imageView.setTranslationY((float)(int)(-1.05f * this.mSpeakerHeight));
        final ImageView imageView2 = dotDevices.get(1);
        imageView2.setTranslationX((float)(int)(2.1f * this.mSpeakerWidth));
        imageView2.setTranslationY((float)(int)(0.5f * this.mSpeakerHeight));
        this.mDotDevices.addAll(dotDevices);
    }
    
    private void place61DotDevices() {
        final List<ImageView> dotDevices = this.createDotDevices(2, 2130837691);
        final ImageView imageView = dotDevices.get(0);
        imageView.setTranslationX((float)(int)(-1.15f * this.mSpeakerWidth));
        imageView.setTranslationY((float)(int)(-0.4f * this.mSpeakerHeight));
        final ImageView imageView2 = dotDevices.get(1);
        imageView2.setTranslationX((float)(int)(2.05f * this.mSpeakerWidth));
        imageView2.setTranslationY((float)(int)(-0.9f * this.mSpeakerHeight));
        this.mDotDevices.addAll(dotDevices);
    }
    
    private void place62DotDevices() {
        final List<ImageView> dotDevices = this.createDotDevices(4, 2130837694);
        final ImageView imageView = dotDevices.get(0);
        imageView.setTranslationX((float)(int)(-2.1 * this.mSpeakerWidth));
        imageView.setTranslationY((float)(int)(-0.2 * this.mSpeakerHeight));
        final ImageView imageView2 = dotDevices.get(1);
        imageView2.setTranslationX((float)(int)(this.mSpeakerWidth * -1.05));
        imageView2.setTranslationY((float)(int)(0.65 * this.mSpeakerHeight));
        final ImageView imageView3 = dotDevices.get(2);
        imageView3.setTranslationX((float)(this.mSpeakerWidth * 0));
        imageView3.setTranslationY((float)(int)(this.mSpeakerHeight * -1.05));
        final ImageView imageView4 = dotDevices.get(3);
        imageView4.setTranslationX((float)(int)(1.2 * this.mSpeakerWidth));
        imageView4.setTranslationY((float)(int)(1.35 * this.mSpeakerHeight));
        this.mDotDevices.addAll(dotDevices);
    }
    
    private void place63DotDevices() {
        final List<ImageView> dotDevices = this.createDotDevices(3, 2130837697);
        final ImageView imageView = dotDevices.get(0);
        imageView.setTranslationX((float)(int)(-2.1 * this.mSpeakerWidth));
        imageView.setTranslationY((float)(int)(1.1 * this.mSpeakerHeight));
        final ImageView imageView2 = dotDevices.get(1);
        imageView2.setTranslationX((float)(int)(-2.0 * this.mSpeakerWidth));
        imageView2.setTranslationY((float)(int)(-0.6 * this.mSpeakerHeight));
        final ImageView imageView3 = dotDevices.get(2);
        imageView3.setTranslationX((float)(int)(1.75 * this.mSpeakerWidth));
        imageView3.setTranslationY((float)(int)(-0.5 * this.mSpeakerHeight));
        this.mDotDevices.addAll(dotDevices);
    }
    
    private void place64DotDevices() {
        final List<ImageView> dotDevices = this.createDotDevices(4, 2130837700);
        final ImageView imageView = dotDevices.get(0);
        imageView.setTranslationX((float)(int)(-1.05 * this.mSpeakerWidth));
        imageView.setTranslationY((float)(int)(-1.0 * this.mSpeakerHeight));
        final ImageView imageView2 = dotDevices.get(1);
        imageView2.setTranslationX((float)(int)(this.mSpeakerWidth * -0.75));
        imageView2.setTranslationY((float)(int)(1.15 * this.mSpeakerHeight));
        final ImageView imageView3 = dotDevices.get(2);
        imageView3.setTranslationX((float)(int)(0.8 * this.mSpeakerWidth));
        imageView3.setTranslationY((float)(int)(this.mSpeakerHeight * -0.75));
        final ImageView imageView4 = dotDevices.get(3);
        imageView4.setTranslationX((float)(int)(1.2 * this.mSpeakerWidth));
        imageView4.setTranslationY((float)(int)(0.4 * this.mSpeakerHeight));
        this.mDotDevices.addAll(dotDevices);
    }
    
    private void place65DotDevices() {
        final List<ImageView> dotDevices = this.createDotDevices(5, 2130837703);
        final ImageView imageView = dotDevices.get(0);
        imageView.setTranslationX((float)(int)(-1.25f * this.mSpeakerWidth));
        imageView.setTranslationY((float)(int)(this.mSpeakerHeight * 0.0f));
        final ImageView imageView2 = dotDevices.get(1);
        imageView2.setTranslationX((float)(int)(-0.49f * this.mSpeakerWidth));
        imageView2.setTranslationY((float)(int)(-0.75f * this.mSpeakerHeight));
        final ImageView imageView3 = dotDevices.get(2);
        imageView3.setTranslationX((float)(int)(0.5f * this.mSpeakerWidth));
        imageView3.setTranslationY((float)(int)(0.85f * this.mSpeakerHeight));
        final ImageView imageView4 = dotDevices.get(3);
        imageView4.setTranslationX((float)(int)(1.3f * this.mSpeakerWidth));
        imageView4.setTranslationY((float)(int)(-1.0f * this.mSpeakerHeight));
        final ImageView imageView5 = dotDevices.get(4);
        imageView5.setTranslationX((float)(int)(1.95f * this.mSpeakerWidth));
        imageView5.setTranslationY((float)(int)(this.mSpeakerHeight * 0.0f));
        this.mDotDevices.addAll(dotDevices);
    }
    
    private void resetOnBoardingFirstAnimation() {
        this.mFirstDeviceView.setTranslationX(0.0f);
        this.mSecondDeviceView.setTranslationX(0.0f);
        this.mFourthDeviceView.setTranslationX(0.0f);
        this.mFifthDeviceView.setTranslationX(0.0f);
    }
    
    private void restoreDraggedDevicesScale() {
        this.mFifthDrawerDeviceView.setScaleX(1.0f);
        this.mFifthDrawerDeviceView.setScaleY(1.0f);
        this.mSecondDrawerDeviceView.setScaleX(1.0f);
        this.mSecondDrawerDeviceView.setScaleY(1.0f);
        this.mEighthDrawerDeviceView.setScaleX(1.0f);
        this.mEighthDrawerDeviceView.setScaleY(1.0f);
    }
    
    private void setDotDevicesAlpha(final float alpha) {
        final Iterator<ImageView> iterator = this.mDotDevices.iterator();
        while (iterator.hasNext()) {
            iterator.next().setAlpha(alpha);
        }
    }
    
    private void setDotDevicesScale(final float n) {
        for (final ImageView imageView : this.mDotDevices) {
            imageView.setScaleX(n);
            imageView.setScaleY(n);
        }
    }
    
    private void setDraggedDotDevicesAlpha(final float alpha) {
        this.mFirstDotDevice.setAlpha(alpha);
        this.mSecondDotDevice.setAlpha(alpha);
        this.mThirdDotDevice.setAlpha(alpha);
    }
    
    private void setDraggedDotDevicesScale(final float n) {
        this.mFirstDotDevice.setScaleX(n);
        this.mFirstDotDevice.setScaleY(n);
        this.mSecondDotDevice.setScaleX(n);
        this.mSecondDotDevice.setScaleY(n);
        this.mThirdDotDevice.setScaleX(n);
        this.mThirdDotDevice.setScaleY(n);
    }
    
    private void setDrawerDevicesTranslationY(final float translationY) {
        final Iterator<UEDeviceView> iterator = this.mDrawerDevices.iterator();
        while (iterator.hasNext()) {
            iterator.next().setTranslationY(translationY);
        }
    }
    
    private void showOnBoardingFirstAnimation() {
        final AccelerateInterpolator interpolator = new AccelerateInterpolator(6.0f);
        this.mAnimatorSetIntro.cancel();
        this.mAnimatorSetIntro = new AnimatorSet();
        for (int i = 0; i < this.devicesViews.length; ++i) {
            final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.devicesViews[i], View.TRANSLATION_X, new float[] { this.devicePage0CoordX[i] });
            ofFloat.setInterpolator((TimeInterpolator)interpolator);
            this.mAnimatorSetIntro.play((Animator)ofFloat);
        }
        this.mAnimatorSetIntro.setDuration((long)this.mAnimationDuration);
        this.mAnimatorSetIntro.start();
    }
    
    private void startDragAndDropAnimation() {
        final int[] array = new int[2];
        this.mAnimatorSetDragNDrop.cancel();
        this.mAnimatorSetDragNDrop = new AnimatorSet();
        this.mFirstDotDevice.setScaleX(1.3f);
        this.mFirstDotDevice.setScaleY(1.3f);
        this.mFirstDotDevice.setTranslationX(this.mFirstDotDeviceEndX);
        this.mFirstDotDevice.setTranslationY(this.mFirstDotDeviceEndY);
        final ObjectAnimator setDuration = ObjectAnimator.ofFloat((Object)this.mFirstDotDevice, View.ALPHA, new float[] { 1.0f }).setDuration(200L);
        setDuration.setStartDelay(900L);
        this.mAnimatorSetDragNDrop.play((Animator)setDuration);
        this.mZacksBoomCloud.getLocationInWindow(array);
        this.mZacksBoomCloud.setAlpha(1.0f);
        this.mZacksBoomCloud.setTranslationX(this.mFifthDrawerDeviceView.getLeft() - this.mZacksBoomCloudWidth / 2.0f + this.mDrawerSpeakerWidth / 2);
        this.mZacksBoomCloud.setTranslationY((float)(-this.mDrawerHeight));
        this.mAnimatorSetDragNDrop.play((Animator)ObjectAnimator.ofFloat((Object)this.mZacksBoomCloud, View.TRANSLATION_X, new float[] { this.mScreenCenterX - array[0] + this.mFirstDotDeviceDeltaXFromCenter - this.mZacksBoomCloudWidth / 2.0f + this.mDrawerSpeakerWidth / 2 }).setDuration(900L));
        this.mAnimatorSetDragNDrop.play((Animator)ObjectAnimator.ofFloat((Object)this.mZacksBoomCloud, View.TRANSLATION_Y, new float[] { this.mScreenCenterY - array[1] + this.mFirstDotDeviceDeltaYFromCenter - this.mDrawerSpeakerHeight / 2 - this.mZacksBoomCloudHeight }).setDuration(900L));
        final ObjectAnimator setDuration2 = ObjectAnimator.ofFloat((Object)this.mZacksBoomCloud, View.ALPHA, new float[] { 0.0f }).setDuration(300L);
        setDuration2.setStartDelay(900L);
        this.mAnimatorSetDragNDrop.play((Animator)setDuration2);
        this.mFifthDrawerDeviceView.getLocationInWindow(array);
        this.mAnimatorSetDragNDrop.play((Animator)ObjectAnimator.ofFloat((Object)this.mFifthDrawerDeviceView, View.ROTATION, new float[] { -15.0f }).setDuration(200L));
        this.mAnimatorSetDragNDrop.play((Animator)ObjectAnimator.ofFloat((Object)this.mFifthDrawerDeviceView, View.TRANSLATION_X, new float[] { this.mScreenCenterX - array[0] + this.mFirstDotDeviceDeltaXFromCenter }).setDuration(900L));
        this.mAnimatorSetDragNDrop.play((Animator)ObjectAnimator.ofFloat((Object)this.mFifthDrawerDeviceView, View.TRANSLATION_Y, new float[] { this.mScreenCenterY - array[1] + this.mFirstDotDeviceDeltaYFromCenter - this.mDrawerSpeakerHeight }).setDuration(900L));
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.mFifthDrawerDeviceView, View.ALPHA, new float[] { 0.0f });
        ofFloat.setDuration(300L);
        ofFloat.setStartDelay(900L);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat);
        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)this.mFifthDrawerDeviceView, View.SCALE_X, new float[] { 0.5f });
        ofFloat2.setDuration(300L);
        ofFloat2.setStartDelay(900L);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat2);
        final ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat((Object)this.mFifthDrawerDeviceView, View.SCALE_Y, new float[] { 0.5f });
        ofFloat3.setDuration(300L);
        ofFloat3.setStartDelay(900L);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat3);
        final ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat((Object)this.mSixthDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-(this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2)) });
        ofFloat4.setDuration(1200L);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat4);
        final ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat((Object)this.mSeventhDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-(this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2)) });
        ofFloat5.setDuration(1200L);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat5);
        final ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat((Object)this.mEighthDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-(this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2)) });
        ofFloat6.setDuration(1200L);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat6);
        final ObjectAnimator ofFloat7 = ObjectAnimator.ofFloat((Object)this.mNinthDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-(this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2)) });
        ofFloat7.setDuration(1200L);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat7);
        final ObjectAnimator ofFloat8 = ObjectAnimator.ofFloat((Object)this.mTenthDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-(this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2)) });
        ofFloat8.setDuration(1200L);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat8);
        final ObjectAnimator ofFloat9 = ObjectAnimator.ofFloat((Object)this.mEleventhDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-(this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2)) });
        ofFloat9.setDuration(1200L);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat9);
        final ObjectAnimator ofFloat10 = ObjectAnimator.ofFloat((Object)this.mTwelfthDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-(this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2)) });
        ofFloat10.setDuration(1200L);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat10);
        this.mSecondDotDevice.setScaleX(1.3f);
        this.mSecondDotDevice.setScaleY(1.3f);
        this.mSecondDotDevice.setTranslationX(this.mSecondDotDeviceEndX);
        this.mSecondDotDevice.setTranslationY(this.mSecondDotDeviceEndY);
        final ObjectAnimator ofFloat11 = ObjectAnimator.ofFloat((Object)this.mSecondDotDevice, View.ALPHA, new float[] { 1.0f });
        ofFloat11.setDuration(200L);
        ofFloat11.setStartDelay((long)2100);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat11);
        this.mSeansBoomCloud.getLocationInWindow(array);
        final ObjectAnimator ofFloat12 = ObjectAnimator.ofFloat((Object)this.mSeansBoomCloud, View.ALPHA, new float[] { 1.0f });
        ofFloat12.setDuration(0L);
        ofFloat12.setStartDelay((long)1200);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat12);
        this.mSeansBoomCloud.setTranslationX(this.mSecondDrawerDeviceView.getLeft() - this.mSeansBoomCloudWidth / 2.0f + this.mDrawerSpeakerWidth / 2);
        this.mSeansBoomCloud.setTranslationY((float)(-this.mDrawerHeight));
        final ObjectAnimator ofFloat13 = ObjectAnimator.ofFloat((Object)this.mSeansBoomCloud, View.TRANSLATION_X, new float[] { this.mScreenCenterX - array[0] + this.mSecondDotDeviceDeltaXFromCenter - this.mSeansBoomCloudWidth / 2.0f + this.mDrawerSpeakerWidth / 2 });
        ofFloat13.setDuration(900L);
        ofFloat13.setStartDelay((long)1200);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat13);
        final ObjectAnimator ofFloat14 = ObjectAnimator.ofFloat((Object)this.mSeansBoomCloud, View.TRANSLATION_Y, new float[] { this.mScreenCenterY - array[1] + this.mSecondDotDeviceDeltaYFromCenter - this.mDrawerSpeakerHeight / 2 - this.mSeansBoomCloudHeight });
        ofFloat14.setDuration(900L);
        ofFloat14.setStartDelay((long)1200);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat14);
        final ObjectAnimator ofFloat15 = ObjectAnimator.ofFloat((Object)this.mSeansBoomCloud, View.ALPHA, new float[] { 0.0f });
        ofFloat15.setDuration(300L);
        ofFloat15.setStartDelay((long)2100);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat15);
        this.mSecondDrawerDeviceView.getLocationInWindow(array);
        final ObjectAnimator ofFloat16 = ObjectAnimator.ofFloat((Object)this.mSecondDrawerDeviceView, View.ROTATION, new float[] { 15.0f });
        ofFloat16.setDuration(200L);
        ofFloat16.setStartDelay((long)1200);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat16);
        final ObjectAnimator ofFloat17 = ObjectAnimator.ofFloat((Object)this.mSecondDrawerDeviceView, View.TRANSLATION_X, new float[] { this.mScreenCenterX - array[0] + this.mSecondDotDeviceDeltaXFromCenter });
        ofFloat17.setDuration(900L);
        ofFloat17.setStartDelay((long)1200);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat17);
        final ObjectAnimator ofFloat18 = ObjectAnimator.ofFloat((Object)this.mSecondDrawerDeviceView, View.TRANSLATION_Y, new float[] { this.mScreenCenterX - array[1] + this.mSecondDotDeviceDeltaYFromCenter + this.mDrawerSpeakerHeight / 2 });
        ofFloat18.setDuration(900L);
        ofFloat18.setStartDelay((long)1200);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat18);
        final ObjectAnimator ofFloat19 = ObjectAnimator.ofFloat((Object)this.mSecondDrawerDeviceView, View.ALPHA, new float[] { 0.0f });
        ofFloat19.setDuration(300L);
        ofFloat19.setStartDelay((long)2100);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat19);
        final ObjectAnimator ofFloat20 = ObjectAnimator.ofFloat((Object)this.mSecondDrawerDeviceView, View.SCALE_X, new float[] { 0.5f });
        ofFloat20.setDuration(300L);
        ofFloat20.setStartDelay((long)2100);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat20);
        final ObjectAnimator ofFloat21 = ObjectAnimator.ofFloat((Object)this.mSecondDrawerDeviceView, View.SCALE_Y, new float[] { 0.5f });
        ofFloat21.setDuration(300L);
        ofFloat21.setStartDelay((long)2100);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat21);
        final ObjectAnimator ofFloat22 = ObjectAnimator.ofFloat((Object)this.mThirdDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-(this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2)) });
        ofFloat22.setDuration(1200L);
        ofFloat22.setStartDelay((long)1200);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat22);
        final ObjectAnimator ofFloat23 = ObjectAnimator.ofFloat((Object)this.mFourthDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-(this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2)) });
        ofFloat23.setDuration(1200L);
        ofFloat23.setStartDelay((long)1200);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat23);
        final ObjectAnimator ofFloat24 = ObjectAnimator.ofFloat((Object)this.mSixthDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-2 * (this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2)) });
        ofFloat24.setDuration(1200L);
        ofFloat24.setStartDelay((long)1200);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat24);
        final ObjectAnimator ofFloat25 = ObjectAnimator.ofFloat((Object)this.mSeventhDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-2 * (this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2)) });
        ofFloat25.setDuration(1200L);
        ofFloat25.setStartDelay((long)1200);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat25);
        final ObjectAnimator ofFloat26 = ObjectAnimator.ofFloat((Object)this.mEighthDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-2 * (this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2)) });
        ofFloat26.setDuration(1200L);
        ofFloat26.setStartDelay((long)1200);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat26);
        final ObjectAnimator ofFloat27 = ObjectAnimator.ofFloat((Object)this.mNinthDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-2 * (this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2)) });
        ofFloat27.setDuration(1200L);
        ofFloat27.setStartDelay((long)1200);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat27);
        final ObjectAnimator ofFloat28 = ObjectAnimator.ofFloat((Object)this.mTenthDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-2 * (this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2)) });
        ofFloat28.setDuration(1200L);
        ofFloat28.setStartDelay((long)1200);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat28);
        final ObjectAnimator ofFloat29 = ObjectAnimator.ofFloat((Object)this.mEleventhDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-2 * (this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2)) });
        ofFloat29.setDuration(1200L);
        ofFloat29.setStartDelay((long)1200);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat29);
        final ObjectAnimator ofFloat30 = ObjectAnimator.ofFloat((Object)this.mTwelfthDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-2 * (this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2)) });
        ofFloat30.setDuration(1200L);
        ofFloat30.setStartDelay((long)1200);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat30);
        this.mThirdDotDevice.setScaleX(1.3f);
        this.mThirdDotDevice.setScaleY(1.3f);
        this.mThirdDotDevice.setTranslationX(this.mThirdDotDeviceEndX);
        this.mThirdDotDevice.setTranslationY(this.mThirdDotDeviceEndY);
        final ObjectAnimator ofFloat31 = ObjectAnimator.ofFloat((Object)this.mThirdDotDevice, View.ALPHA, new float[] { 1.0f });
        ofFloat31.setDuration(200L);
        ofFloat31.setStartDelay((long)3300);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat31);
        this.mBrettsBoomCloud.getLocationInWindow(array);
        final ObjectAnimator ofFloat32 = ObjectAnimator.ofFloat((Object)this.mBrettsBoomCloud, View.ALPHA, new float[] { 1.0f });
        ofFloat32.setDuration(0L);
        ofFloat32.setStartDelay((long)2400);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat32);
        this.mBrettsBoomCloud.setTranslationX(this.mEighthDrawerDeviceView.getLeft() - (this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2) * 2 - this.mBrettsBoomCloudWidth / 2.0f + this.mDrawerSpeakerWidth / 2);
        this.mBrettsBoomCloud.setTranslationY((float)(-this.mDrawerHeight));
        final ObjectAnimator ofFloat33 = ObjectAnimator.ofFloat((Object)this.mBrettsBoomCloud, View.TRANSLATION_X, new float[] { this.mScreenCenterX - array[0] + this.mThirdDotDeviceDeltaXFromCenter - this.mBrettsBoomCloudWidth / 2.0f + this.mDrawerSpeakerWidth / 2 });
        ofFloat33.setDuration(900L);
        ofFloat33.setStartDelay((long)2400);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat33);
        final ObjectAnimator ofFloat34 = ObjectAnimator.ofFloat((Object)this.mBrettsBoomCloud, View.TRANSLATION_Y, new float[] { this.mScreenCenterY - array[1] + this.mThirdDotDeviceDeltaYFromCenter - this.mDrawerSpeakerHeight / 2 - this.mSeansBoomCloudHeight });
        ofFloat34.setDuration(900L);
        ofFloat34.setStartDelay((long)2400);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat34);
        final ObjectAnimator ofFloat35 = ObjectAnimator.ofFloat((Object)this.mBrettsBoomCloud, View.ALPHA, new float[] { 0.0f });
        ofFloat35.setDuration(300L);
        ofFloat35.setStartDelay((long)3300);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat35);
        final ObjectAnimator ofFloat36 = ObjectAnimator.ofFloat((Object)this.mEighthDrawerDeviceView, View.SCALE_X, new float[] { 0.5f });
        ofFloat36.setDuration(300L);
        ofFloat36.setStartDelay((long)3300);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat36);
        final ObjectAnimator ofFloat37 = ObjectAnimator.ofFloat((Object)this.mEighthDrawerDeviceView, View.SCALE_Y, new float[] { 0.5f });
        ofFloat37.setDuration(300L);
        ofFloat37.setStartDelay((long)3300);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat37);
        this.mEighthDrawerDeviceView.getLocationInWindow(array);
        final ObjectAnimator ofFloat38 = ObjectAnimator.ofFloat((Object)this.mEighthDrawerDeviceView, View.ROTATION, new float[] { -15.0f });
        ofFloat38.setDuration(200L);
        ofFloat38.setStartDelay((long)2400);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat38);
        final ObjectAnimator ofFloat39 = ObjectAnimator.ofFloat((Object)this.mEighthDrawerDeviceView, View.TRANSLATION_X, new float[] { this.mScreenCenterX - array[0] + this.mFirstDotDeviceDeltaXFromCenter - this.mDrawerSpeakerWidth / 2 });
        ofFloat39.setDuration(900L);
        ofFloat39.setStartDelay((long)2400);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat39);
        final ObjectAnimator ofFloat40 = ObjectAnimator.ofFloat((Object)this.mEighthDrawerDeviceView, View.TRANSLATION_Y, new float[] { this.mScreenCenterY - array[1] + this.mThirdDotDeviceDeltaYFromCenter - this.mDrawerSpeakerHeight });
        ofFloat40.setDuration(900L);
        ofFloat40.setStartDelay((long)2400);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat40);
        final ObjectAnimator ofFloat41 = ObjectAnimator.ofFloat((Object)this.mEighthDrawerDeviceView, View.ALPHA, new float[] { 0.0f });
        ofFloat41.setDuration(300L);
        ofFloat41.setStartDelay((long)3300);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat41);
        final ObjectAnimator ofFloat42 = ObjectAnimator.ofFloat((Object)this.mNinthDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-3 * (this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2)) });
        ofFloat42.setDuration(1200L);
        ofFloat42.setStartDelay((long)2400);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat42);
        final ObjectAnimator ofFloat43 = ObjectAnimator.ofFloat((Object)this.mTenthDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-3 * (this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2)) });
        ofFloat43.setDuration(1200L);
        ofFloat43.setStartDelay((long)2400);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat43);
        final ObjectAnimator ofFloat44 = ObjectAnimator.ofFloat((Object)this.mEleventhDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-3 * (this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2)) });
        ofFloat44.setDuration(1200L);
        ofFloat44.setStartDelay((long)2400);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat44);
        final ObjectAnimator ofFloat45 = ObjectAnimator.ofFloat((Object)this.mTwelfthDrawerDeviceView, View.TRANSLATION_X, new float[] { (float)(-3 * (this.mDrawerSpeakerWidth + this.mDrawerSpeakerSpacing / 2)) });
        ofFloat45.setDuration(1200L);
        ofFloat45.setStartDelay((long)2400);
        this.mAnimatorSetDragNDrop.play((Animator)ofFloat45);
        this.mAnimatorSetDragNDrop.start();
    }
    
    private void transformationFirstSecond(final float n, final int n2) {
        this.mCurrentPageIndicator.setTranslationX(-0.75f * this.mPageIndicatorWidth + (1.0f - n) * 0.5f * this.mPageIndicatorWidth);
        this.mSwapIcon.setAlpha(1.0f - n);
        this.mThirdDeviceView.setTranslationX(this.devicePage0CoordX[2] + -(1.0f - n) * this.mSpeakerWidth);
        this.mFourthDeviceView.setTranslationX(this.devicePage0CoordX[3] + (float)((1.0f - n) * 0.25 * this.mSpeakerWidth));
        this.mFirstDeviceView.setTranslationX(this.devicePage0CoordX[0] + (float)(-(1.0f - n) * 1.25 * n2));
        this.mSecondDeviceView.setTranslationX(this.devicePage0CoordX[1] + (float)(-(1.0f - n) * 1.55 * n2));
        this.mFifthDeviceView.setTranslationX(this.devicePage0CoordX[4] + (float)(-(1.0f - n) * 2.25 * n2));
    }
    
    private void transformationFourthThird(final float n) {
        this.mCurrentPageIndicator.setTranslationX(0.25f * this.mPageIndicatorWidth + (1.0f - n) * 0.5f * this.mPageIndicatorWidth);
        this.setDotDevicesAlpha(1.0f - n);
        this.setDotDevicesScale((1.0f - n) / 2.0f + 0.5f);
        this.setDotDevicesAlpha(1.0f - n);
        this.setDraggedDotDevicesScale(1.3f - 0.3f * (1.0f - n));
        this.setDraggedDotDevicesAlpha(1.0f - n);
        this.mOnBoardingDrawerView.setTranslationY((1.0f - n) * this.mDrawerHeight);
        this.setDrawerDevicesTranslationY(-n * this.mDrawerHeight);
        this.mGetThePartyStartedButton.setTranslationY((this.mDrawerHeight * 2 + this.mGlobalPadding) * n);
    }
    
    private void transformationSecondThird(final float alpha) {
        this.initDrawerDevices();
        this.mCurrentPageIndicator.setTranslationX(-0.25f * this.mPageIndicatorWidth + (1.0f - alpha) * 0.5f * this.mPageIndicatorWidth);
        this.mSwapIcon.setAlpha(alpha);
        this.mOnBoardingDrawerView.setTranslationY(this.mDrawerHeight * alpha);
        this.setDrawerDevicesTranslationY(-(1.0f - alpha) * this.mDrawerHeight);
        this.mThirdDeviceView.setTranslationX(this.devicePage1CoordX[2] + (1.0f - alpha) * this.mSpeakerWidth);
        this.mFourthDeviceView.setTranslationX(this.devicePage1CoordX[3] + -(1.0f - alpha) * this.mSpeakerWidth);
    }
    
    private void transformationThirdFourth(final float n) {
        this.mZacksBoomCloud.setTranslationX(0.0f);
        this.mZacksBoomCloud.setTranslationY(0.0f);
        this.mZacksBoomCloud.setAlpha(0.0f);
        this.mSeansBoomCloud.setTranslationX(0.0f);
        this.mSeansBoomCloud.setTranslationY(0.0f);
        this.mSeansBoomCloud.setAlpha(0.0f);
        this.mBrettsBoomCloud.setTranslationX(0.0f);
        this.mBrettsBoomCloud.setTranslationY(0.0f);
        this.mBrettsBoomCloud.setAlpha(0.0f);
        this.mCurrentPageIndicator.setTranslationX(0.25f * this.mPageIndicatorWidth + (1.0f - n) * 0.5f * this.mPageIndicatorWidth);
        this.setDotDevicesAlpha(1.0f - n);
        this.setDotDevicesScale((1.0f - n) / 2.0f + 0.5f);
        this.setDraggedDotDevicesScale(1.3f - 0.3f * (1.0f - n));
        this.mOnBoardingDrawerView.setTranslationY((1.0f - n) * this.mDrawerHeight);
        this.setDrawerDevicesTranslationY(-n * this.mDrawerHeight);
        this.mGetThePartyStartedButton.setTranslationY((this.mDrawerHeight * 2 + this.mGlobalPadding) * n);
    }
    
    public void closeXUPOnBoarding() {
        this.finish();
    }
    
    @Override
    public void onBackPressed() {
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130968607);
        ButterKnife.bind(this);
        this.mOnBoardingPageTransformer = new OnBoardingPageTransformer();
        this.mOnBoardingPagerAdapter = new OnBoardingPagerAdapter((Context)this);
        this.mOnBoardingViewPager.setAdapter(this.mOnBoardingPagerAdapter);
        this.mOnBoardingViewPager.setPageTransformer(true, (ViewPager.PageTransformer)this.mOnBoardingPageTransformer);
        this.mOnBoardingViewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener)new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(final int n) {
                if (n == 0) {
                    XUPOnBoardingActivity.this.mSwipeDirection = SwipeDirection.NONE;
                    XUPOnBoardingActivity.this.mPrevPositivePosition = -1.0f;
                    switch (XUPOnBoardingActivity.this.mOnBoardingViewPager.getCurrentItem()) {
                        case 0: {
                            XUPOnBoardingActivity.this.completeFirstPage();
                            XUPOnBoardingActivity.this.mCurrentPage = 0;
                            break;
                        }
                        case 1: {
                            XUPOnBoardingActivity.this.completeSecondPage();
                            XUPOnBoardingActivity.this.mCurrentPage = 1;
                            break;
                        }
                        case 2: {
                            if (XUPOnBoardingActivity.this.mCurrentPage == 1 || XUPOnBoardingActivity.this.mCurrentPage == 2) {
                                XUPOnBoardingActivity.this.completeThirdPage();
                            }
                            else {
                                XUPOnBoardingActivity.this.initalThirdPage();
                            }
                            XUPOnBoardingActivity.this.startDragAndDropAnimation();
                            XUPOnBoardingActivity.this.mCurrentPage = 2;
                            break;
                        }
                        case 3: {
                            XUPOnBoardingActivity.this.completeFourthPage();
                            XUPOnBoardingActivity.this.mCurrentPage = 3;
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
        this.mSwapIcon.setAlpha(0.0f);
        this.mFirstDeviceView.setDeviceColor(XUPOnBoardingActivity.COLOR_FIRST, false);
        this.mSecondDeviceView.setDeviceColor(XUPOnBoardingActivity.COLOR_SECOND, false);
        this.mThirdDeviceView.setDeviceColor(XUPOnBoardingActivity.COLOR_THIRD, false);
        this.mFourthDeviceView.setDeviceColor(XUPOnBoardingActivity.COLOR_FOURTH, false);
        this.mFifthDeviceView.setDeviceColor(XUPOnBoardingActivity.COLOR_FIFTH, false);
        this.devicesViews[0] = this.mFirstDeviceView;
        this.devicesViews[1] = this.mSecondDeviceView;
        this.devicesViews[2] = this.mThirdDeviceView;
        this.devicesViews[3] = this.mFourthDeviceView;
        this.devicesViews[4] = this.mFifthDeviceView;
        this.mAnimationDuration = 600;
        final Display defaultDisplay = this.getWindowManager().getDefaultDisplay();
        final Point point = new Point();
        defaultDisplay.getSize(point);
        this.mScreenWidth = point.x;
        this.mScreenHeight = point.y;
        this.mScreenCenterX = (float)(this.mScreenWidth / 2);
        this.mScreenCenterY = (float)(this.mScreenHeight / 2);
        this.mFirstDeviceView.measure(-2, -2);
        this.mSpeakerWidth = this.mFirstDeviceView.getMeasuredWidth();
        this.mSpeakerHeight = this.mFirstDeviceView.getMeasuredHeight();
        this.mTranslationDistance = (int)(0.75 * this.mSpeakerWidth);
        this.mPageIndicatorWidth = 0.5f * this.mSpeakerWidth;
        this.mFirstDrawerDeviceView.measure(-2, -2);
        this.mDrawerSpeakerHeight = this.mFirstDrawerDeviceView.getMeasuredHeight();
        this.mZacksBoomCloud.measure(-2, -2);
        this.mZacksBoomCloudWidth = (float)this.mZacksBoomCloud.getMeasuredWidth();
        this.mZacksBoomCloudHeight = (float)this.mZacksBoomCloud.getMeasuredHeight();
        this.mSeansBoomCloud.measure(-2, -2);
        this.mSeansBoomCloudWidth = (float)this.mSeansBoomCloud.getMeasuredWidth();
        this.mSeansBoomCloudHeight = (float)this.mSeansBoomCloud.getMeasuredHeight();
        this.mBrettsBoomCloud.measure(-2, -2);
        this.mBrettsBoomCloudWidth = (float)this.mBrettsBoomCloud.getMeasuredWidth();
        this.mBrettsBoomCloudHeight = (float)this.mBrettsBoomCloud.getMeasuredHeight();
        this.mFirstDeviceView.getLocationInWindow(new int[2]);
        this.devicePage0CoordX[0] = (float)(this.mTranslationDistance * -2);
        this.devicePage0CoordX[1] = (float)(-this.mTranslationDistance);
        this.devicePage0CoordX[2] = 0.0f;
        this.devicePage0CoordX[3] = (float)this.mTranslationDistance;
        this.devicePage0CoordX[4] = (float)(this.mTranslationDistance * 2);
        this.mOnBoardingDrawerView.setTranslationY((float)this.mDrawerHeight);
        this.mGetThePartyStartedButton.setTranslationY((float)(this.mDrawerHeight * 2 + this.mGlobalPadding));
        this.mFirstDrawerDeviceView.setDeviceColor(XUPOnBoardingActivity.COLOR_SECOND, false);
        this.mSecondDrawerDeviceView.setDeviceColor(XUPOnBoardingActivity.COLOR_FIFTH, false);
        this.mThirdDrawerDeviceView.setDeviceColor(XUPOnBoardingActivity.COLOR_THIRD, false);
        this.mFourthDrawerDeviceView.setDeviceColor(XUPOnBoardingActivity.COLOR_FOURTH, false);
        this.mFifthDrawerDeviceView.setDeviceColor(XUPOnBoardingActivity.COLOR_FIRST, false);
        this.mSixthDrawerDeviceView.setDeviceColor(XUPOnBoardingActivity.COLOR_THIRD, false);
        this.mSeventhDrawerDeviceView.setDeviceColor(XUPOnBoardingActivity.COLOR_FOURTH, false);
        this.mEighthDrawerDeviceView.setDeviceColor(XUPOnBoardingActivity.COLOR_SECOND, false);
        this.mNinthDrawerDeviceView.setDeviceColor(XUPOnBoardingActivity.COLOR_FIFTH, false);
        this.mTenthDrawerDeviceView.setDeviceColor(XUPOnBoardingActivity.COLOR_THIRD, false);
        this.mEleventhDrawerDeviceView.setDeviceColor(XUPOnBoardingActivity.COLOR_FIRST, false);
        this.mTwelfthDrawerDeviceView.setDeviceColor(XUPOnBoardingActivity.COLOR_SECOND, false);
        this.createDrawerDotDevices();
        this.mDrawerDevices.add(this.mFirstDrawerDeviceView);
        this.mDrawerDevices.add(this.mSecondDrawerDeviceView);
        this.mDrawerDevices.add(this.mThirdDrawerDeviceView);
        this.mDrawerDevices.add(this.mFourthDrawerDeviceView);
        this.mDrawerDevices.add(this.mFifthDrawerDeviceView);
        this.mDrawerDevices.add(this.mSixthDrawerDeviceView);
        this.mDrawerDevices.add(this.mSeventhDrawerDeviceView);
        this.mDrawerDevices.add(this.mEighthDrawerDeviceView);
        this.mDrawerDevices.add(this.mNinthDrawerDeviceView);
        this.mDrawerDevices.add(this.mTenthDrawerDeviceView);
        this.mDrawerDevices.add(this.mEleventhDrawerDeviceView);
        this.mDrawerDevices.add(this.mTwelfthDrawerDeviceView);
        this.place60DotDevices();
        this.place61DotDevices();
        this.place62DotDevices();
        this.place63DotDevices();
        this.place64DotDevices();
        this.place65DotDevices();
        this.createPageIndicatorViews();
        this.showOnBoardingFirstAnimation();
    }
    
    @OnClick({ 2131624072 })
    public void onGetThePartyStartedButtonClicked(final View view) {
        if (UserPreferences.getInstance().setXUPOnBoardingWasSeen(true)) {
            this.closeXUPOnBoarding();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    public void onWindowFocusChanged(final boolean b) {
        super.onWindowFocusChanged(b);
        this.initDrawerDotDevices();
    }
    
    class OnBoardingPageTransformer implements PageTransformer
    {
        @Override
        public void transformPage(final View view, final float n) {
            XUPOnBoardingActivity.this.mPageWidth = view.getWidth();
            XUPOnBoardingActivity.this.mAnimatorSetDragNDrop.cancel();
            if (n > -1.0f && n < 0.0f) {
                switch (XUPOnBoardingActivity.this.mCurrentPage) {
                }
            }
            else if (n > 0.0f && n < 1.0f) {
                if (XUPOnBoardingActivity.this.mSwipeDirection == SwipeDirection.NONE && XUPOnBoardingActivity.this.mPrevPositivePosition > 0.0f) {
                    if (n < XUPOnBoardingActivity.this.mPrevPositivePosition) {
                        XUPOnBoardingActivity.this.mSwipeDirection = SwipeDirection.LEFT;
                    }
                    else if (n > XUPOnBoardingActivity.this.mPrevPositivePosition) {
                        XUPOnBoardingActivity.this.mSwipeDirection = SwipeDirection.RIGHT;
                    }
                }
                switch (XUPOnBoardingActivity.this.mCurrentPage) {
                    case 0: {
                        XUPOnBoardingActivity.this.transformationFirstSecond(n, XUPOnBoardingActivity.this.mPageWidth);
                        break;
                    }
                    case 1: {
                        if (XUPOnBoardingActivity.this.mSwipeDirection == SwipeDirection.LEFT) {
                            XUPOnBoardingActivity.this.transformationSecondThird(n);
                            break;
                        }
                        if (XUPOnBoardingActivity.this.mSwipeDirection == SwipeDirection.RIGHT) {
                            XUPOnBoardingActivity.this.transformationFirstSecond(n, XUPOnBoardingActivity.this.mPageWidth);
                            break;
                        }
                        break;
                    }
                    case 2: {
                        if (XUPOnBoardingActivity.this.mSwipeDirection == SwipeDirection.LEFT) {
                            XUPOnBoardingActivity.this.transformationThirdFourth(n);
                            break;
                        }
                        if (XUPOnBoardingActivity.this.mSwipeDirection == SwipeDirection.RIGHT) {
                            XUPOnBoardingActivity.this.initDotDevices();
                            XUPOnBoardingActivity.this.transformationSecondThird(n);
                            break;
                        }
                        break;
                    }
                    case 3: {
                        if (XUPOnBoardingActivity.this.mCurrentPage == 2) {
                            XUPOnBoardingActivity.this.transformationThirdFourth(n);
                            break;
                        }
                        XUPOnBoardingActivity.this.transformationFourthThird(n);
                        break;
                    }
                }
                XUPOnBoardingActivity.this.mPrevPositivePosition = n;
            }
        }
    }
    
    public class OnBoardingPagerAdapter extends PagerAdapter
    {
        private Context mContext;
        
        public OnBoardingPagerAdapter(final Context mContext) {
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
            final ViewGroup viewGroup2 = (ViewGroup)LayoutInflater.from(this.mContext).inflate(2130968675, viewGroup, false);
            ((TextView)viewGroup2.findViewById(2131624246)).setText((CharSequence)XUPOnBoardingActivity.this.getString(XUPOnBoardingActivity.this.pagesTitleIds[n]));
            if (n == 3) {
                ((TextView)viewGroup2.findViewById(2131624246)).setTextSize((float)(int)XUPOnBoardingActivity.this.getResources().getDimension(2131361919));
            }
            viewGroup.addView((View)viewGroup2);
            return viewGroup2;
        }
        
        @Override
        public boolean isViewFromObject(final View view, final Object o) {
            return view == o;
        }
    }
    
    private enum SwipeDirection
    {
        LEFT, 
        NONE, 
        RIGHT;
    }
}
