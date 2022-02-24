// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.activities;

import android.content.res.Resources;
import android.widget.TextView;
import android.view.View$OnClickListener;
import butterknife.internal.DebouncingOnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import com.logitech.ue.views.UEDeviceView;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;

public class XUPOnBoardingActivity$$ViewBinder<T extends XUPOnBoardingActivity> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        t.mOnBoardingMainView = finder.castView(finder.findRequiredView(o, 2131624048, "field 'mOnBoardingMainView'"), 2131624048, "field 'mOnBoardingMainView'");
        t.mOnBoardingViewPager = finder.castView(finder.findRequiredView(o, 2131624049, "field 'mOnBoardingViewPager'"), 2131624049, "field 'mOnBoardingViewPager'");
        t.mFirstDeviceView = finder.castView(finder.findRequiredView(o, 2131624050, "field 'mFirstDeviceView'"), 2131624050, "field 'mFirstDeviceView'");
        t.mSecondDeviceView = finder.castView(finder.findRequiredView(o, 2131624052, "field 'mSecondDeviceView'"), 2131624052, "field 'mSecondDeviceView'");
        t.mThirdDeviceView = finder.castView(finder.findRequiredView(o, 2131624055, "field 'mThirdDeviceView'"), 2131624055, "field 'mThirdDeviceView'");
        t.mFourthDeviceView = finder.castView(finder.findRequiredView(o, 2131624054, "field 'mFourthDeviceView'"), 2131624054, "field 'mFourthDeviceView'");
        t.mFifthDeviceView = finder.castView(finder.findRequiredView(o, 2131624051, "field 'mFifthDeviceView'"), 2131624051, "field 'mFifthDeviceView'");
        t.mSwapIcon = finder.castView(finder.findRequiredView(o, 2131624053, "field 'mSwapIcon'"), 2131624053, "field 'mSwapIcon'");
        t.mOnBoardingDrawerView = finder.findRequiredView(o, 2131624056, "field 'mOnBoardingDrawerView'");
        t.mFirstDrawerDeviceView = finder.castView(finder.findRequiredView(o, 2131624057, "field 'mFirstDrawerDeviceView'"), 2131624057, "field 'mFirstDrawerDeviceView'");
        t.mSecondDrawerDeviceView = finder.castView(finder.findRequiredView(o, 2131624058, "field 'mSecondDrawerDeviceView'"), 2131624058, "field 'mSecondDrawerDeviceView'");
        t.mThirdDrawerDeviceView = finder.castView(finder.findRequiredView(o, 2131624059, "field 'mThirdDrawerDeviceView'"), 2131624059, "field 'mThirdDrawerDeviceView'");
        t.mFourthDrawerDeviceView = finder.castView(finder.findRequiredView(o, 2131624060, "field 'mFourthDrawerDeviceView'"), 2131624060, "field 'mFourthDrawerDeviceView'");
        t.mFifthDrawerDeviceView = finder.castView(finder.findRequiredView(o, 2131624061, "field 'mFifthDrawerDeviceView'"), 2131624061, "field 'mFifthDrawerDeviceView'");
        t.mSixthDrawerDeviceView = finder.castView(finder.findRequiredView(o, 2131624062, "field 'mSixthDrawerDeviceView'"), 2131624062, "field 'mSixthDrawerDeviceView'");
        t.mSeventhDrawerDeviceView = finder.castView(finder.findRequiredView(o, 2131624063, "field 'mSeventhDrawerDeviceView'"), 2131624063, "field 'mSeventhDrawerDeviceView'");
        t.mEighthDrawerDeviceView = finder.castView(finder.findRequiredView(o, 2131624064, "field 'mEighthDrawerDeviceView'"), 2131624064, "field 'mEighthDrawerDeviceView'");
        t.mNinthDrawerDeviceView = finder.castView(finder.findRequiredView(o, 2131624065, "field 'mNinthDrawerDeviceView'"), 2131624065, "field 'mNinthDrawerDeviceView'");
        t.mTenthDrawerDeviceView = finder.castView(finder.findRequiredView(o, 2131624066, "field 'mTenthDrawerDeviceView'"), 2131624066, "field 'mTenthDrawerDeviceView'");
        t.mEleventhDrawerDeviceView = finder.castView(finder.findRequiredView(o, 2131624067, "field 'mEleventhDrawerDeviceView'"), 2131624067, "field 'mEleventhDrawerDeviceView'");
        t.mTwelfthDrawerDeviceView = finder.castView(finder.findRequiredView(o, 2131624068, "field 'mTwelfthDrawerDeviceView'"), 2131624068, "field 'mTwelfthDrawerDeviceView'");
        final View view = finder.findRequiredView(o, 2131624072, "field 'mGetThePartyStartedButton' and method 'onGetThePartyStartedButtonClicked'");
        t.mGetThePartyStartedButton = (Button)finder.castView(view, 2131624072, "field 'mGetThePartyStartedButton'");
        view.setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onGetThePartyStartedButtonClicked(view);
            }
        });
        t.mZacksBoomCloud = finder.castView(finder.findRequiredView(o, 2131624069, "field 'mZacksBoomCloud'"), 2131624069, "field 'mZacksBoomCloud'");
        t.mSeansBoomCloud = finder.castView(finder.findRequiredView(o, 2131624070, "field 'mSeansBoomCloud'"), 2131624070, "field 'mSeansBoomCloud'");
        t.mBrettsBoomCloud = finder.castView(finder.findRequiredView(o, 2131624071, "field 'mBrettsBoomCloud'"), 2131624071, "field 'mBrettsBoomCloud'");
        final Resources resources = finder.getContext(o).getResources();
        t.mDrawerSpeakerWidth = resources.getDimensionPixelSize(2131361915);
        t.mDrawerSpeakerSpacing = resources.getDimensionPixelSize(2131361870);
        t.mDrawerHeight = resources.getDimensionPixelSize(2131361924);
        t.mGlobalPadding = resources.getDimensionPixelSize(2131361881);
    }
    
    public void unbind(final T t) {
        t.mOnBoardingMainView = null;
        t.mOnBoardingViewPager = null;
        t.mFirstDeviceView = null;
        t.mSecondDeviceView = null;
        t.mThirdDeviceView = null;
        t.mFourthDeviceView = null;
        t.mFifthDeviceView = null;
        t.mSwapIcon = null;
        t.mOnBoardingDrawerView = null;
        t.mFirstDrawerDeviceView = null;
        t.mSecondDrawerDeviceView = null;
        t.mThirdDrawerDeviceView = null;
        t.mFourthDrawerDeviceView = null;
        t.mFifthDrawerDeviceView = null;
        t.mSixthDrawerDeviceView = null;
        t.mSeventhDrawerDeviceView = null;
        t.mEighthDrawerDeviceView = null;
        t.mNinthDrawerDeviceView = null;
        t.mTenthDrawerDeviceView = null;
        t.mEleventhDrawerDeviceView = null;
        t.mTwelfthDrawerDeviceView = null;
        t.mGetThePartyStartedButton = null;
        t.mZacksBoomCloud = null;
        t.mSeansBoomCloud = null;
        t.mBrettsBoomCloud = null;
    }
}
