// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Bundle;

public class GestureTipFragment extends HelpTipFragment
{
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        this.mWebView.setWebViewClient((WebViewClient)new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
                boolean shouldOverrideUrlLoading = true;
                if (s.startsWith("tutorial://gesture")) {
                    GestureTutorialDialogFragment.getInstance(true).show(GestureTipFragment.this.getActivity().getSupportFragmentManager(), GestureTutorialDialogFragment.TAG);
                }
                else {
                    shouldOverrideUrlLoading = super.shouldOverrideUrlLoading(webView, s);
                }
                return shouldOverrideUrlLoading;
            }
        });
        return (View)this.mWebView;
    }
}
