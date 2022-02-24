// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.widget.TextView;
import android.view.View$OnClickListener;
import butterknife.internal.DebouncingOnClickListener;
import android.widget.ImageButton;
import android.view.View;
import com.logitech.ue.views.UEDeviceView;
import butterknife.ButterKnife;

public class LandingFragment$$ViewBinder<T extends LandingFragment> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        t.mGhostDeviceView = finder.castView(finder.findRequiredView(o, 2131624162, "field 'mGhostDeviceView'"), 2131624162, "field 'mGhostDeviceView'");
        final View view = finder.findRequiredView(o, 2131624164, "field 'mBulletsButton' and method 'onBulletsClicked'");
        t.mBulletsButton = (ImageButton)finder.castView(view, 2131624164, "field 'mBulletsButton'");
        view.setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onBulletsClicked(view);
            }
        });
        t.mCloudPopUp = finder.findRequiredView(o, 2131624165, "field 'mCloudPopUp'");
        t.mSpeakerVersionView = finder.castView(finder.findRequiredView(o, 2131624103, "field 'mSpeakerVersionView'"), 2131624103, "field 'mSpeakerVersionView'");
        t.mSpeakerVersionLabel = finder.castView(finder.findRequiredView(o, 2131624102, "field 'mSpeakerVersionLabel'"), 2131624102, "field 'mSpeakerVersionLabel'");
    }
    
    public void unbind(final T t) {
        t.mGhostDeviceView = null;
        t.mBulletsButton = null;
        t.mCloudPopUp = null;
        t.mSpeakerVersionView = null;
        t.mSpeakerVersionLabel = null;
    }
}
