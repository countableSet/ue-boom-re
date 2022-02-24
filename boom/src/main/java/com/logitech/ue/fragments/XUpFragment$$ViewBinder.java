// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.SeekBar;
import android.view.View$OnClickListener;
import butterknife.internal.DebouncingOnClickListener;
import com.logitech.ue.views.UEDeviceView;
import android.support.v7.widget.RecyclerView;
import com.logitech.ue.views.SpiderFrameLayout;
import android.view.View;
import butterknife.ButterKnife;

public class XUpFragment$$ViewBinder<T extends XUpFragment> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        t.mDoubleUpPanel = finder.findRequiredView(o, 2131624115, "field 'mDoubleUpPanel'");
        t.mContainer = finder.castView(finder.findRequiredView(o, 2131624117, "field 'mContainer'"), 2131624117, "field 'mContainer'");
        t.mDrawerRecyclerView = finder.castView(finder.findRequiredView(o, 2131624198, "field 'mDrawerRecyclerView'"), 2131624198, "field 'mDrawerRecyclerView'");
        t.mMainDeviceView = finder.castView(finder.findRequiredView(o, 2131624118, "field 'mMainDeviceView'"), 2131624118, "field 'mMainDeviceView'");
        (t.mSwapSpeakers = finder.findRequiredView(o, 2131624116, "field 'mSwapSpeakers' and method 'onSwapSpeakersClick'")).setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onSwapSpeakersClick(view);
            }
        });
        t.mBalancePanel = finder.findRequiredView(o, 2131624113, "field 'mBalancePanel'");
        t.mBalanceSeekBar = finder.castView(finder.findRequiredView(o, 2131624096, "field 'mBalanceSeekBar'"), 2131624096, "field 'mBalanceSeekBar'");
        final View view = finder.findRequiredView(o, 2131624097, "field 'mBalanceLeftLabel' and method 'onBalanceLeftClicked'");
        t.mBalanceLeftLabel = (TextView)finder.castView(view, 2131624097, "field 'mBalanceLeftLabel'");
        view.setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onBalanceLeftClicked(view);
            }
        });
        final View view2 = finder.findRequiredView(o, 2131624098, "field 'mBalanceLabel' and method 'onBalanceCenterClicked'");
        t.mBalanceLabel = (TextView)finder.castView(view2, 2131624098, "field 'mBalanceLabel'");
        view2.setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onBalanceCenterClicked(view);
            }
        });
        final View view3 = finder.findRequiredView(o, 2131624099, "field 'mBalanceRightLabel' and method 'onBalanceRightClicked'");
        t.mBalanceRightLabel = (TextView)finder.castView(view3, 2131624099, "field 'mBalanceRightLabel'");
        view3.setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onBalanceRightClicked(view);
            }
        });
        final View view4 = finder.findRequiredView(o, 2131624107, "field 'mDoubleUpButton' and method 'onDoubleUpClicked'");
        t.mDoubleUpButton = (TextView)finder.castView(view4, 2131624107, "field 'mDoubleUpButton'");
        view4.setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onDoubleUpClicked(view);
            }
        });
        final View view5 = finder.findRequiredView(o, 2131624108, "field 'mStereoButton' and method 'onStereoClicked'");
        t.mStereoButton = (TextView)finder.castView(view5, 2131624108, "field 'mStereoButton'");
        view5.setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onStereoClicked(view);
            }
        });
        t.mHostNameCloud = finder.castView(finder.findRequiredView(o, 2131624114, "field 'mHostNameCloud'"), 2131624114, "field 'mHostNameCloud'");
        (t.mVolumeSyncButton = finder.findRequiredView(o, 2131624193, "field 'mVolumeSyncButton' and method 'onVolumeSyncClicked'")).setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onVolumeSyncClicked(view);
            }
        });
        t.mVolumeSyncIcon = finder.castView(finder.findRequiredView(o, 2131624194, "field 'mVolumeSyncIcon'"), 2131624194, "field 'mVolumeSyncIcon'");
        t.mVolumeSyncText = finder.castView(finder.findRequiredView(o, 2131624195, "field 'mVolumeSyncText'"), 2131624195, "field 'mVolumeSyncText'");
        t.mSpeakersFoundLabel = finder.castView(finder.findRequiredView(o, 2131624196, "field 'mSpeakersFoundLabel'"), 2131624196, "field 'mSpeakersFoundLabel'");
        t.mTutorialCloud = finder.findRequiredView(o, 2131624197, "field 'mTutorialCloud'");
        final View view6 = finder.findRequiredView(o, 2131624120, "field 'mStartButton' and method 'onStartClicked'");
        t.mStartButton = (Button)finder.castView(view6, 2131624120, "field 'mStartButton'");
        view6.setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onStartClicked(view);
            }
        });
        t.mDrawerHeight = finder.getContext(o).getResources().getDimension(2131361924);
    }
    
    public void unbind(final T t) {
        t.mDoubleUpPanel = null;
        t.mContainer = null;
        t.mDrawerRecyclerView = null;
        t.mMainDeviceView = null;
        t.mSwapSpeakers = null;
        t.mBalancePanel = null;
        t.mBalanceSeekBar = null;
        t.mBalanceLeftLabel = null;
        t.mBalanceLabel = null;
        t.mBalanceRightLabel = null;
        t.mDoubleUpButton = null;
        t.mStereoButton = null;
        t.mHostNameCloud = null;
        t.mVolumeSyncButton = null;
        t.mVolumeSyncIcon = null;
        t.mVolumeSyncText = null;
        t.mSpeakersFoundLabel = null;
        t.mTutorialCloud = null;
        t.mStartButton = null;
    }
}
