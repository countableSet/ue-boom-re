// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.widget.TextView;
import android.widget.ImageView;
import android.view.View$OnClickListener;
import butterknife.internal.DebouncingOnClickListener;
import com.logitech.ue.views.FadeImageButton;
import android.view.View;
import com.logitech.ue.views.VolumeController;
import butterknife.ButterKnife;

public class HomeMainFragment$$ViewBinder<T extends HomeMainFragment> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        t.mVolumeController = finder.castView(finder.findRequiredView(o, 2131624166, "field 'mVolumeController'"), 2131624166, "field 'mVolumeController'");
        final View view = finder.findRequiredView(o, 2131624173, "field 'mPowerButton' and method 'powerClick'");
        t.mPowerButton = (FadeImageButton)finder.castView(view, 2131624173, "field 'mPowerButton'");
        view.setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.powerClick(view);
            }
        });
        t.mConnectingIndicator = finder.findRequiredView(o, 2131624167, "field 'mConnectingIndicator'");
        t.mConnectingIndicatorImage = finder.castView(finder.findRequiredView(o, 2131624168, "field 'mConnectingIndicatorImage'"), 2131624168, "field 'mConnectingIndicatorImage'");
        t.mConnectingIndicatorText = finder.castView(finder.findRequiredView(o, 2131624169, "field 'mConnectingIndicatorText'"), 2131624169, "field 'mConnectingIndicatorText'");
        t.mBatteryChargeIndicator = finder.findRequiredView(o, 2131624170, "field 'mBatteryChargeIndicator'");
        t.mBatteryChargeIndicatorImage = finder.castView(finder.findRequiredView(o, 2131624171, "field 'mBatteryChargeIndicatorImage'"), 2131624171, "field 'mBatteryChargeIndicatorImage'");
        t.mBatteryChargeIndicatorText = finder.castView(finder.findRequiredView(o, 2131624172, "field 'mBatteryChargeIndicatorText'"), 2131624172, "field 'mBatteryChargeIndicatorText'");
    }
    
    public void unbind(final T t) {
        t.mVolumeController = null;
        t.mPowerButton = null;
        t.mConnectingIndicator = null;
        t.mConnectingIndicatorImage = null;
        t.mConnectingIndicatorText = null;
        t.mBatteryChargeIndicator = null;
        t.mBatteryChargeIndicatorImage = null;
        t.mBatteryChargeIndicatorText = null;
    }
}
