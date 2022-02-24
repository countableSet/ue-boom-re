// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.view.View$OnClickListener;
import butterknife.internal.DebouncingOnClickListener;
import android.view.View;
import android.webkit.WebView;
import butterknife.ButterKnife;

public class GestureTutorialDialogFragment$$ViewBinder<T extends GestureTutorialDialogFragment> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        t.mWebView = finder.castView(finder.findRequiredView(o, 2131624155, "field 'mWebView'"), 2131624155, "field 'mWebView'");
        finder.findRequiredView(o, 2131624156, "method 'onClosedClicked'").setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onClosedClicked();
            }
        });
    }
    
    public void unbind(final T t) {
        t.mWebView = null;
    }
}
