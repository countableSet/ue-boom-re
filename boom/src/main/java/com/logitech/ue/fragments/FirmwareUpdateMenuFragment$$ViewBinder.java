// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.view.View$OnClickListener;
import butterknife.internal.DebouncingOnClickListener;
import android.view.View;
import android.webkit.WebView;
import butterknife.ButterKnife;

public class FirmwareUpdateMenuFragment$$ViewBinder<T extends FirmwareUpdateMenuFragment> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        t.mWebView = finder.castView(finder.findRequiredView(o, 2131624152, "field 'mWebView'"), 2131624152, "field 'mWebView'");
        finder.findRequiredView(o, 2131624154, "method 'onUpdateClicked'").setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onUpdateClicked(view);
            }
        });
        finder.findRequiredView(o, 2131624153, "method 'onAskLaterClicked'").setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onAskLaterClicked(view);
            }
        });
    }
    
    public void unbind(final T t) {
        t.mWebView = null;
    }
}
