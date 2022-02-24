// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.widget.TextView;
import android.view.ViewGroup;
import android.view.View;
import com.logitech.ue.views.ColorFlipperLayout;
import butterknife.ButterKnife;

public class FirmwareUpdateFragment$$ViewBinder<T extends FirmwareUpdateFragment> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        t.mColorFlipperView = finder.castView(finder.findRequiredView(o, 2131624136, "field 'mColorFlipperView'"), 2131624136, "field 'mColorFlipperView'");
        t.mContentView = finder.castView(finder.findRequiredView(o, 2131624137, "field 'mContentView'"), 2131624137, "field 'mContentView'");
        t.mProgressLabel = finder.castView(finder.findRequiredView(o, 2131624139, "field 'mProgressLabel'"), 2131624139, "field 'mProgressLabel'");
        t.mDeviceModelLabel = finder.castView(finder.findRequiredView(o, 2131624141, "field 'mDeviceModelLabel'"), 2131624141, "field 'mDeviceModelLabel'");
        t.mVersionLabel = finder.castView(finder.findRequiredView(o, 2131624142, "field 'mVersionLabel'"), 2131624142, "field 'mVersionLabel'");
        t.mTimeLabel = finder.castView(finder.findRequiredView(o, 2131624143, "field 'mTimeLabel'"), 2131624143, "field 'mTimeLabel'");
    }
    
    public void unbind(final T t) {
        t.mColorFlipperView = null;
        t.mContentView = null;
        t.mProgressLabel = null;
        t.mDeviceModelLabel = null;
        t.mVersionLabel = null;
        t.mTimeLabel = null;
    }
}
