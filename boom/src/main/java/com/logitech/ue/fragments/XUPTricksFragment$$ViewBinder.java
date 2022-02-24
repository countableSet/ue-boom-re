// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.view.View;
import android.webkit.WebView;
import butterknife.ButterKnife;

public class XUPTricksFragment$$ViewBinder<T extends XUPTricksFragment> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        t.mTricksWebView = finder.castView(finder.findRequiredView(o, 2131624199, "field 'mTricksWebView'"), 2131624199, "field 'mTricksWebView'");
    }
    
    public void unbind(final T t) {
        t.mTricksWebView = null;
    }
}
