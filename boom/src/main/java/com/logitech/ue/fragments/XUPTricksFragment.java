// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.webkit.WebViewClient;
import butterknife.ButterKnife;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.logitech.ue.App;
import butterknife.Bind;
import android.webkit.WebView;

public class XUPTricksFragment extends NavigatableFragment
{
    public static final String TAG;
    @Bind({ 2131624199 })
    WebView mTricksWebView;
    
    static {
        TAG = XUPTricksFragment.class.getSimpleName();
    }
    
    @Override
    public String getTitle() {
        return App.getInstance().getString(2131165371);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2130968641, viewGroup, false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }
    
    @Override
    public void onDestroyView() {
        this.mTricksWebView.stopLoading();
        ButterKnife.unbind(this);
        super.onDestroyView();
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mTricksWebView.setWebViewClient(new WebViewClient());
        this.mTricksWebView.getSettings().setJavaScriptEnabled(true);
        this.mTricksWebView.loadUrl(this.getString(2131165494));
    }
}
