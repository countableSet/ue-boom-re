// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.ecomm;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View$OnTouchListener;
import android.webkit.WebSettings$LayoutAlgorithm;
import android.view.ViewGroup$LayoutParams;
import android.content.Intent;
import android.net.Uri;
import android.content.Context;
import android.webkit.WebViewClient;
import android.webkit.WebView;
import com.logitech.ue.ecomm.model.NotificationStory;
import com.logitech.ue.ecomm.model.Notification;
import android.os.Handler;
import android.widget.PopupWindow;

public class NotificationPopup extends PopupWindow
{
    public static final String DISMISS_URL = "ueapplink://dismiss";
    public static final String ERROR_OUT_OF_BOUNDS = "Notification popup out of bounds";
    Runnable hideRunnable;
    private Handler mHandler;
    private NotificationPopupListener mListener;
    private Notification mNotification;
    private NotificationStory mNotificationStory;
    private WebView mWebView;
    WebViewClient webViewClient;
    
    @SuppressLint({ "SetJavaScriptEnabled" })
    public NotificationPopup(final Context context, final Notification mNotification) {
        this.webViewClient = new WebViewClient() {
            public boolean mainPageLoaded;
            
            public void onPageFinished(final WebView webView, final String s) {
                super.onPageFinished(webView, s);
                this.mainPageLoaded = true;
            }
            
            public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
                boolean shouldOverrideUrlLoading = true;
                if (s.startsWith("ueapplink://dismiss")) {
                    if (NotificationPopup.this.mListener != null) {
                        NotificationPopup.this.mListener.onDismiss(NotificationPopup.this, true);
                        NotificationPopup.this.mNotificationStory.userAction = UserAction.Dismiss;
                        NotificationManager.getInstance().updateStory(NotificationPopup.this.mNotificationStory);
                    }
                    NotificationPopup.this.dismissSafe();
                }
                else if (webView != null && webView.getHitTestResult() != null && webView.getHitTestResult().getType() > 0) {
                    if (NotificationPopup.this.mListener != null) {
                        NotificationPopup.this.mListener.onExternalLinkClicked(NotificationPopup.this, s);
                    }
                    NotificationPopup.this.mNotificationStory.userAction = UserAction.UrlClicked;
                    NotificationPopup.this.mNotificationStory.additionParams.put("clicked_url", s);
                    NotificationManager.getInstance().updateStory(NotificationPopup.this.mNotificationStory);
                    webView.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(s)));
                    NotificationPopup.this.dismissSafe();
                }
                else {
                    shouldOverrideUrlLoading = super.shouldOverrideUrlLoading(webView, s);
                }
                return shouldOverrideUrlLoading;
            }
        };
        this.hideRunnable = new Runnable() {
            @Override
            public void run() {
                if (NotificationPopup.this.isShowing()) {
                    NotificationPopup.this.mNotificationStory.userAction = UserAction.None;
                    NotificationManager.getInstance().updateStory(NotificationPopup.this.mNotificationStory);
                    NotificationPopup.this.dismissSafe();
                }
            }
        };
        this.mNotification = mNotification;
        this.mNotificationStory = new NotificationStory(mNotification, UserAction.None);
        (this.mWebView = new WebView(context)).setLayoutParams(new ViewGroup$LayoutParams(-1, -1));
        this.mWebView.loadUrl(mNotification.url);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.getSettings().setLoadWithOverviewMode(true);
        this.mWebView.getSettings().setUseWideViewPort(true);
        this.mWebView.setWebViewClient(this.webViewClient);
        this.mWebView.setVerticalScrollBarEnabled(false);
        this.mWebView.setHorizontalScrollBarEnabled(false);
        this.mWebView.getSettings().setLayoutAlgorithm(WebSettings$LayoutAlgorithm.SINGLE_COLUMN);
        this.mWebView.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                return motionEvent.getAction() == 2;
            }
        });
        this.setContentView((View)this.mWebView);
        this.setWidth(Math.round(mNotification.params.displayWidth * mNotification.viewPort.width / 100.0f));
        this.setHeight(Math.round(mNotification.params.displayHeight * mNotification.viewPort.height / 100.0f));
    }
    
    public void dismissSafe() {
        this.mHandler.removeCallbacks(this.hideRunnable);
        try {
            if (this.isShowing()) {
                this.dismiss();
            }
        }
        catch (Exception ex) {}
    }
    
    public NotificationPopupListener getListener() {
        return this.mListener;
    }
    
    public void setNotificationPopupListener(final NotificationPopupListener mListener) {
        this.mListener = mListener;
    }
    
    public void show(final View view) {
        final float n = (float)(Math.round(this.mNotification.params.displayWidth * this.mNotification.viewPort.width / 100.0f) + Math.round(this.mNotification.params.displayWidth * this.mNotification.viewPort.x / 100.0f));
        final float n2 = (float)(Math.round(this.mNotification.params.displayHeight * this.mNotification.viewPort.height / 100.0f) + Math.round(this.mNotification.params.displayHeight * this.mNotification.viewPort.y / 100.0f));
        if (n > this.mNotification.params.displayWidth + 0.1f || n2 > this.mNotification.params.displayHeight + 0.1f) {
            if (this.mListener != null) {
                this.mListener.onError(this, new Exception("Notification popup out of bounds"));
            }
        }
        else {
            this.showAtLocation(view, 0, Math.round(this.mNotification.params.displayWidth * this.mNotification.viewPort.x / 100.0f), Math.round(this.mNotification.params.displayHeight * this.mNotification.viewPort.y / 100.0f));
        }
    }
    
    public void showAtLocation(final View view, final int n, final int n2, final int n3) {
        super.showAtLocation(view, n, n2, n3);
        this.mHandler = new Handler();
        if (this.mNotification.duration > 0) {
            this.mHandler.postDelayed(this.hideRunnable, (long)(this.mNotification.duration * 1000));
        }
        NotificationManager.getInstance().getHistory().add(this.mNotificationStory);
        if (this.mListener != null) {
            this.mListener.onShow(this);
        }
    }
    
    public interface NotificationPopupListener
    {
        void onDismiss(final NotificationPopup p0, final boolean p1);
        
        void onError(final NotificationPopup p0, final Exception p1);
        
        void onExternalLinkClicked(final NotificationPopup p0, final String p1);
        
        void onShow(final NotificationPopup p0);
    }
}
