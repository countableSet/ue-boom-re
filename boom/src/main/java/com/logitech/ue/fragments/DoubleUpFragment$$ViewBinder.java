// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.widget.TextView;
import android.widget.SeekBar;
import com.logitech.ue.views.FadeButton;
import android.view.View$OnClickListener;
import butterknife.internal.DebouncingOnClickListener;
import android.widget.Button;
import com.logitech.ue.views.UEDeviceView;
import android.view.View;
import com.logitech.ue.views.SpiderFrameLayout;
import butterknife.ButterKnife;

public class DoubleUpFragment$$ViewBinder<T extends DoubleUpFragment> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        t.mContainer = finder.castView(finder.findRequiredView(o, 2131624117, "field 'mContainer'"), 2131624117, "field 'mContainer'");
        t.mMainDeviceView = finder.castView(finder.findRequiredView(o, 2131624118, "field 'mMainDeviceView'"), 2131624118, "field 'mMainDeviceView'");
        t.mSecondaryDeviceView = finder.castView(finder.findRequiredView(o, 2131624119, "field 'mSecondaryDeviceView'"), 2131624119, "field 'mSecondaryDeviceView'");
        final View view = finder.findRequiredView(o, 2131624120, "field 'mStartStopButton' and method 'onStartScanClick'");
        t.mStartStopButton = (Button)finder.castView(view, 2131624120, "field 'mStartStopButton'");
        view.setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onStartScanClick(view);
            }
        });
        final View view2 = finder.findRequiredView(o, 2131624116, "field 'mSwapSpeakers' and method 'OnSwapSpeakersClick'");
        t.mSwapSpeakers = (FadeButton)finder.castView(view2, 2131624116, "field 'mSwapSpeakers'");
        view2.setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.OnSwapSpeakersClick(view);
            }
        });
        t.mBalancePanel = finder.findRequiredView(o, 2131624113, "field 'mBalancePanel'");
        t.mBalanceSeekBar = finder.castView(finder.findRequiredView(o, 2131624096, "field 'mBalanceSeekBar'"), 2131624096, "field 'mBalanceSeekBar'");
        final View view3 = finder.findRequiredView(o, 2131624097, "field 'mBalanceLeftLabel' and method 'onBalanceLeftClicked'");
        t.mBalanceLeftLabel = (TextView)finder.castView(view3, 2131624097, "field 'mBalanceLeftLabel'");
        view3.setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onBalanceLeftClicked(view);
            }
        });
        final View view4 = finder.findRequiredView(o, 2131624098, "field 'mBalanceLabel' and method 'onBalanceCenterClicked'");
        t.mBalanceLabel = (TextView)finder.castView(view4, 2131624098, "field 'mBalanceLabel'");
        view4.setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onBalanceCenterClicked(view);
            }
        });
        final View view5 = finder.findRequiredView(o, 2131624099, "field 'mBalanceRightLabel' and method 'onBalanceRightClicked'");
        t.mBalanceRightLabel = (TextView)finder.castView(view5, 2131624099, "field 'mBalanceRightLabel'");
        view5.setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onBalanceRightClicked(view);
            }
        });
        t.mHintLabel = finder.castView(finder.findRequiredView(o, 2131624112, "field 'mHintLabel'"), 2131624112, "field 'mHintLabel'");
        t.mPowerUpLabel = finder.castView(finder.findRequiredView(o, 2131624111, "field 'mPowerUpLabel'"), 2131624111, "field 'mPowerUpLabel'");
        t.mDoubleUpPanel = finder.findRequiredView(o, 2131624115, "field 'mDoubleUpPanel'");
        final View view6 = finder.findRequiredView(o, 2131624107, "field 'mDoubleUpButton' and method 'onDoubleUpClicked'");
        t.mDoubleUpButton = (TextView)finder.castView(view6, 2131624107, "field 'mDoubleUpButton'");
        view6.setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onDoubleUpClicked(view);
            }
        });
        final View view7 = finder.findRequiredView(o, 2131624108, "field 'mStereoButton' and method 'onStereoClicked'");
        t.mStereoButton = (TextView)finder.castView(view7, 2131624108, "field 'mStereoButton'");
        view7.setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onStereoClicked(view);
            }
        });
    }
    
    public void unbind(final T t) {
        t.mContainer = null;
        t.mMainDeviceView = null;
        t.mSecondaryDeviceView = null;
        t.mStartStopButton = null;
        t.mSwapSpeakers = null;
        t.mBalancePanel = null;
        t.mBalanceSeekBar = null;
        t.mBalanceLeftLabel = null;
        t.mBalanceLabel = null;
        t.mBalanceRightLabel = null;
        t.mHintLabel = null;
        t.mPowerUpLabel = null;
        t.mDoubleUpPanel = null;
        t.mDoubleUpButton = null;
        t.mStereoButton = null;
    }
}
