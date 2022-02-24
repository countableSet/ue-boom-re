// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.webkit.WebView;
import android.widget.ViewFlipper;
import android.view.View$OnClickListener;
import butterknife.internal.DebouncingOnClickListener;
import android.widget.Button;
import com.logitech.ue.views.UEDeviceView;
import android.view.View;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;

public class PartyUpWelcomeDialogFragment$$ViewBinder<T extends PartyUpWelcomeDialogFragment> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        t.mBackgroundView = finder.castView(finder.findRequiredView(o, 2131624240, "field 'mBackgroundView'"), 2131624240, "field 'mBackgroundView'");
        t.mContent = finder.castView(finder.findRequiredView(o, 2131624137, "field 'mContent'"), 2131624137, "field 'mContent'");
        t.mMainDevice = finder.castView(finder.findRequiredView(o, 2131624118, "field 'mMainDevice'"), 2131624118, "field 'mMainDevice'");
        final View view = finder.findRequiredView(o, 2131624241, "field 'mUpdateButton' and method 'onUpdateClicked'");
        t.mUpdateButton = (Button)finder.castView(view, 2131624241, "field 'mUpdateButton'");
        view.setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onUpdateClicked(view);
            }
        });
        t.mViewFlipper = finder.castView(finder.findRequiredView(o, 2131624239, "field 'mViewFlipper'"), 2131624239, "field 'mViewFlipper'");
        t.mWebView = finder.castView(finder.findRequiredView(o, 2131624155, "field 'mWebView'"), 2131624155, "field 'mWebView'");
        finder.findRequiredView(o, 2131624242, "method 'onAskLaterClicked'").setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onAskLaterClicked(view);
            }
        });
        finder.findRequiredView(o, 2131624192, "method 'onBackClicked'").setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onBackClicked(view);
            }
        });
    }
    
    public void unbind(final T t) {
        t.mBackgroundView = null;
        t.mContent = null;
        t.mMainDevice = null;
        t.mUpdateButton = null;
        t.mViewFlipper = null;
        t.mWebView = null;
    }
}
