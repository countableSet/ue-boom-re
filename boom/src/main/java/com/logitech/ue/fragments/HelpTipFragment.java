// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.annotation.SuppressLint;
import android.view.ViewGroup$LayoutParams;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.webkit.WebView;

public class HelpTipFragment extends NavigatableFragment
{
    public static final String PARAM_HTML_PATH = "html_path";
    public static final String PARAM_TITLE = "title";
    protected WebView mWebView;
    
    public HelpTipFragment() {
        this.mWebView = null;
    }
    
    @Override
    public String getTitle() {
        return this.getArguments().getString("title");
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
    }
    
    @SuppressLint({ "SetJavaScriptEnabled" })
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        (this.mWebView = new WebView((Context)this.getActivity())).setLayoutParams(new ViewGroup$LayoutParams(-1, -1));
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.loadUrl(this.getArguments().getString("html_path"));
        this.mWebView.setBackgroundColor(-1);
        return (View)this.mWebView;
    }
    
    @Override
    public void onDestroyView() {
        if (this.getView() != null) {
            this.getView().setVisibility(4);
        }
        super.onDestroyView();
    }
}
