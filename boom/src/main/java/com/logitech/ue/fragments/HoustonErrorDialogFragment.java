// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import butterknife.ButterKnife;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import butterknife.OnClick;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebViewClient;
import android.webkit.WebView;
import android.widget.ViewFlipper;
import butterknife.Bind;
import android.widget.Button;

public class HoustonErrorDialogFragment extends FullScreenDialogFragment
{
    public static final String DISMISS_URL = "ueapp://dismiss";
    public static final int FLIPPER_SEND_LINK = 1;
    public static final int FLIPPER_START = 0;
    public static final String PARAM_WITH_RETRY = "with_retry";
    public static final String TAG;
    Listener mListener;
    @Bind({ 2131624201 })
    Button mTryAgainButton;
    @Bind({ 2131624137 })
    ViewFlipper mViewFlipper;
    @Bind({ 2131624155 })
    WebView mWebView;
    WebViewClient webViewClient;
    
    static {
        TAG = HoustonErrorDialogFragment.class.getSimpleName();
    }
    
    public HoustonErrorDialogFragment() {
        this.webViewClient = new WebViewClient() {
            public void onReceivedError(final WebView webView, final int n, final String s, final String s2) {
                HoustonErrorDialogFragment.this.mWebView.loadUrl("about:blank");
            }
            
            public void onReceivedError(final WebView webView, final WebResourceRequest webResourceRequest, final WebResourceError webResourceError) {
                HoustonErrorDialogFragment.this.mWebView.loadUrl("about:blank");
            }
            
            public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
                boolean shouldOverrideUrlLoading;
                if (s.startsWith("ueapp://dismiss")) {
                    HoustonErrorDialogFragment.this.mListener.onDoLaterClicked();
                    HoustonErrorDialogFragment.this.dismiss();
                    shouldOverrideUrlLoading = true;
                }
                else {
                    shouldOverrideUrlLoading = super.shouldOverrideUrlLoading(webView, s);
                }
                return shouldOverrideUrlLoading;
            }
        };
        this.setStyle(1, 2131427530);
    }
    
    public static HoustonErrorDialogFragment getInstance(final boolean b) {
        final HoustonErrorDialogFragment houstonErrorDialogFragment = new HoustonErrorDialogFragment();
        houstonErrorDialogFragment.setCancelable(false);
        final Bundle arguments = new Bundle();
        arguments.putBoolean("with_retry", b);
        houstonErrorDialogFragment.setArguments(arguments);
        return houstonErrorDialogFragment;
    }
    
    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Listener) {
            this.mListener = (Listener)activity;
            return;
        }
        throw new IllegalArgumentException();
    }
    
    @OnClick({ 2131624192 })
    void onBackClicked(final View view) {
        Log.d(HoustonErrorDialogFragment.TAG, "onBack clicked");
        this.switchFlipperView(0, false);
    }
    
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, @Nullable final ViewGroup viewGroup, @Nullable final Bundle bundle) {
        return layoutInflater.inflate(2130968644, viewGroup, false);
    }
    
    @OnClick({ 2131624200 })
    void onLaterClicked(final View view) {
        Log.d(HoustonErrorDialogFragment.TAG, "onLater clicked");
        this.dismiss();
        this.mListener.onDoLaterClicked();
    }
    
    @OnClick({ 2131624201 })
    void onUpdateClicked(final View view) {
        Log.d(HoustonErrorDialogFragment.TAG, "onUpdate clicked");
        this.dismiss();
        this.mListener.onRetryUpdateClicked();
    }
    
    @OnClick({ 2131624202 })
    void onUpdateViaUSBClicked(final View view) {
        Log.d(HoustonErrorDialogFragment.TAG, "onUpdateViaUSB clicked");
        this.switchFlipperView(1, true);
    }
    
    @Override
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.setBackgroundColor(0);
        this.mWebView.setWebViewClient(this.webViewClient);
        this.mWebView.loadUrl(this.getString(2131165478));
        if (!this.getArguments().getBoolean("with_retry")) {
            this.mTryAgainButton.setVisibility(8);
        }
    }
    
    public void switchFlipperView(final int displayedChild, final boolean b) {
        if (this.mViewFlipper.getDisplayedChild() != displayedChild) {
            if (b) {
                this.mViewFlipper.setInAnimation(this.getContext(), 2131034126);
                this.mViewFlipper.setOutAnimation(this.getContext(), 2131034129);
            }
            else {
                this.mViewFlipper.setInAnimation(this.getContext(), 2131034125);
                this.mViewFlipper.setOutAnimation(this.getContext(), 2131034130);
            }
            this.mViewFlipper.setDisplayedChild(displayedChild);
        }
    }
    
    public interface Listener
    {
        void onDoLaterClicked();
        
        void onRetryUpdateClicked();
    }
}
