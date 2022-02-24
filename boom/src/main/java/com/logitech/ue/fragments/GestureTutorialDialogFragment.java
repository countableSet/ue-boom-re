// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.webkit.WebSettings$LayoutAlgorithm;
import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import java.io.Serializable;
import android.content.Context;
import android.content.Intent;
import com.logitech.ue.activities.MenuActivity;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import butterknife.ButterKnife;
import android.support.annotation.Nullable;
import com.logitech.ue.App;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import butterknife.OnClick;
import com.logitech.ue.UserPreferences;
import android.os.Bundle;
import butterknife.Bind;
import android.webkit.WebView;
import android.os.Handler;

public class GestureTutorialDialogFragment extends FullScreenDialogFragment
{
    public static final String PARAM_VIEW_ONLY = "view_only";
    public static final String TAG;
    public static final int WEB_VIEW_GIF_ANIMATION_LENGTH = 15000;
    final Handler mHandler;
    protected boolean mIsViewOnly;
    @Bind({ 2131624155 })
    protected WebView mWebView;
    
    static {
        TAG = GestureTutorialDialogFragment.class.getSimpleName();
    }
    
    public GestureTutorialDialogFragment() {
        this.mHandler = new Handler();
    }
    
    public static GestureTutorialDialogFragment getInstance(final boolean b) {
        final GestureTutorialDialogFragment gestureTutorialDialogFragment = new GestureTutorialDialogFragment();
        final Bundle arguments = new Bundle();
        arguments.putBoolean("view_only", b);
        gestureTutorialDialogFragment.setArguments(arguments);
        return gestureTutorialDialogFragment;
    }
    
    @OnClick({ 2131624156 })
    void onClosedClicked() {
        if (!this.mIsViewOnly) {
            UserPreferences.getInstance().setGestureOn(false);
        }
        this.dismiss();
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mIsViewOnly = this.getArguments().getBoolean("view_only");
    }
    
    @SuppressLint({ "SetJavaScriptEnabled" })
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(2130968632, viewGroup, false);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        this.mWebView.clearCache(true);
        this.mHandler.removeCallbacksAndMessages((Object)null);
        App.getInstance().dismissMessageDialog();
        if (!this.isHidden()) {
            this.dismiss();
        }
    }
    
    @Override
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mHandler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                if (GestureTutorialDialogFragment.this.mIsViewOnly) {
                    GestureTutorialDialogFragment.this.dismiss();
                }
                else {
                    App.getInstance().showAlertDialog(GestureTutorialDialogFragment.this.getString(2131165279), 2131165363, 2131165353, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            UserPreferences.getInstance().setGestureCounter(-1);
                            if (n == -1) {
                                final Intent intent = new Intent((Context)GestureTutorialDialogFragment.this.getActivity(), (Class)MenuActivity.class);
                                final Bundle bundle = new Bundle();
                                bundle.putString("html_path", GestureTutorialDialogFragment.this.getString(2131165310));
                                bundle.putString("title", GestureTutorialDialogFragment.this.getString(2131165301));
                                intent.putExtra("initial_fragment", (Serializable)GestureTipFragment.class);
                                intent.putExtra("initial_fragment_args", bundle);
                                GestureTutorialDialogFragment.this.startActivity(intent);
                            }
                            else {
                                GestureTutorialDialogFragment.this.dismiss();
                            }
                        }
                    });
                }
            }
        }, 15000L);
        this.mWebView.setVerticalScrollBarEnabled(false);
        this.mWebView.setHorizontalScrollBarEnabled(false);
        this.mWebView.setScrollContainer(false);
        this.mWebView.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                return motionEvent.getAction() == 2;
            }
        });
        this.mWebView.getSettings().setLayoutAlgorithm(WebSettings$LayoutAlgorithm.SINGLE_COLUMN);
        this.mWebView.loadUrl(this.getString(2131165280));
        this.mWebView.setBackgroundColor(0);
    }
}
