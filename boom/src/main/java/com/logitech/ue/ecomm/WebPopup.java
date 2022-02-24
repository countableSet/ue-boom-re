// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.ecomm;

import android.view.Display;
import android.graphics.Point;
import android.view.WindowManager;
import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import android.webkit.WebSettings$LayoutAlgorithm;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.webkit.WebView;
import android.os.Handler;
import android.content.Context;
import android.widget.PopupWindow;

public class WebPopup extends PopupWindow
{
    public static final String ERROR_OUT_OF_BOUNDS = "Notification popup out of bounds";
    Runnable hideRunnable;
    private Context mContext;
    private int mDuration;
    private Handler mHandler;
    private int mHeight;
    private PopupListener mListener;
    private String mURL;
    private WebView mWebView;
    private int mWidth;
    
    public WebPopup(final Context mContext, final String murl, final int mDuration, final int n, final int n2) {
        this.hideRunnable = new Runnable() {
            @Override
            public void run() {
                if (WebPopup.this.isShowing()) {
                    WebPopup.this.dismissSafe();
                }
            }
        };
        this.mContext = mContext;
        this.mURL = murl;
        this.mDuration = mDuration;
        this.mWidth = n;
        this.mHeight = n2;
        this.setWidth(n);
        this.setHeight(n2);
        this.setContentView(this.onCreateView());
    }
    
    public void dismiss() {
        super.dismiss();
        if (this.mListener != null) {
            this.mListener.onDismiss(this, false);
        }
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
    
    public PopupListener getListener() {
        return this.mListener;
    }
    
    protected View onCreateView() {
        (this.mWebView = new WebView(this.mContext)).setLayoutParams(new ViewGroup$LayoutParams(-1, -1));
        this.mWebView.loadUrl(this.mURL);
        this.mWebView.setVerticalScrollBarEnabled(false);
        this.mWebView.setHorizontalScrollBarEnabled(false);
        this.mWebView.getSettings().setLayoutAlgorithm(WebSettings$LayoutAlgorithm.SINGLE_COLUMN);
        this.mWebView.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                return motionEvent.getAction() == 2;
            }
        });
        return (View)this.mWebView;
    }
    
    public void setPopupListener(final PopupListener mListener) {
        this.mListener = mListener;
    }
    
    public void show(final View view) {
        this.showAtLocation(view, 0, 0, 0);
    }
    
    public void showAtLocation(final View view, final int n, final int n2, final int n3) {
        final float n4 = (float)this.mWidth;
        final float n5 = (float)this.mHeight;
        final Display defaultDisplay = ((WindowManager)this.mContext.getSystemService("window")).getDefaultDisplay();
        final Point point = new Point();
        defaultDisplay.getSize(point);
        if ((n4 > point.x * 1.1f || n5 > point.y * 1.1f) && this.mListener != null) {
            this.mListener.onError(this, new Exception("Notification popup out of bounds"));
        }
        super.showAtLocation(view, n, n2, n3);
        this.mHandler = new Handler();
        if (this.mDuration > 0) {
            this.mHandler.postDelayed(this.hideRunnable, (long)this.mDuration);
        }
        if (this.mListener != null) {
            this.mListener.onShow(this);
        }
    }
    
    public interface PopupListener
    {
        void onDismiss(final WebPopup p0, final boolean p1);
        
        void onError(final WebPopup p0, final Exception p1);
        
        void onShow(final WebPopup p0);
    }
}
