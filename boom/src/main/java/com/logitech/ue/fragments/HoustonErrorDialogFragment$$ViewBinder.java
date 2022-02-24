// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.view.View$OnClickListener;
import butterknife.internal.DebouncingOnClickListener;
import android.widget.Button;
import android.widget.ViewFlipper;
import android.view.View;
import android.webkit.WebView;
import butterknife.ButterKnife;

public class HoustonErrorDialogFragment$$ViewBinder<T extends HoustonErrorDialogFragment> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        t.mWebView = finder.castView(finder.findRequiredView(o, 2131624155, "field 'mWebView'"), 2131624155, "field 'mWebView'");
        t.mViewFlipper = finder.castView(finder.findRequiredView(o, 2131624137, "field 'mViewFlipper'"), 2131624137, "field 'mViewFlipper'");
        final View view = finder.findRequiredView(o, 2131624201, "field 'mTryAgainButton' and method 'onUpdateClicked'");
        t.mTryAgainButton = (Button)finder.castView(view, 2131624201, "field 'mTryAgainButton'");
        view.setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onUpdateClicked(view);
            }
        });
        finder.findRequiredView(o, 2131624202, "method 'onUpdateViaUSBClicked'").setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onUpdateViaUSBClicked(view);
            }
        });
        finder.findRequiredView(o, 2131624200, "method 'onLaterClicked'").setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onLaterClicked(view);
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
        t.mWebView = null;
        t.mViewFlipper = null;
        t.mTryAgainButton = null;
    }
}
