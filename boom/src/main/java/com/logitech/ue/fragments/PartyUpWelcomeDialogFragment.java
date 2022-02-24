// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import butterknife.ButterKnife;
import com.logitech.ue.activities.MainActivity;
import com.logitech.ue.utils.AnimationUtils;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import butterknife.OnClick;
import android.util.Log;
import java.util.Iterator;
import java.util.Collection;
import android.os.Bundle;
import android.view.ViewGroup$LayoutParams;
import android.widget.RelativeLayout$LayoutParams;
import android.content.Context;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import java.util.ArrayList;
import android.webkit.WebViewClient;
import android.webkit.WebView;
import android.widget.ViewFlipper;
import android.widget.Button;
import com.logitech.ue.views.UEDeviceView;
import android.widget.ImageView;
import java.util.List;
import butterknife.Bind;
import android.widget.RelativeLayout;

public class PartyUpWelcomeDialogFragment extends FullScreenDialogFragment
{
    public static final String DISMISS_URL = "ueapp://dismiss";
    public static final int FLIPPER_SEND_LINK = 1;
    public static final int FLIPPER_START = 0;
    public static final String PARAM_DEVICE_COLOR = "device_color";
    public static final String PARAM_IS_OTA_SUPPORTED = "is_ota_supported";
    public static final String TAG;
    @Bind({ 2131624240 })
    RelativeLayout mBackgroundView;
    @Bind({ 2131624137 })
    RelativeLayout mContent;
    private List<ImageView> mDotDevices;
    @Bind({ 2131624118 })
    UEDeviceView mMainDevice;
    private int mSpeakerHeight;
    private int mSpeakerWidth;
    @Bind({ 2131624241 })
    Button mUpdateButton;
    @Bind({ 2131624239 })
    ViewFlipper mViewFlipper;
    @Bind({ 2131624155 })
    WebView mWebView;
    WebViewClient webViewClient;
    
    static {
        TAG = PartyUpWelcomeDialogFragment.class.getSimpleName();
    }
    
    public PartyUpWelcomeDialogFragment() {
        this.mDotDevices = new ArrayList<ImageView>(20);
        this.webViewClient = new WebViewClient() {
            public void onReceivedError(final WebView webView, final int n, final String s, final String s2) {
                PartyUpWelcomeDialogFragment.this.mWebView.loadUrl("about:blank");
            }
            
            public void onReceivedError(final WebView webView, final WebResourceRequest webResourceRequest, final WebResourceError webResourceError) {
                PartyUpWelcomeDialogFragment.this.mWebView.loadUrl("about:blank");
            }
            
            public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
                boolean shouldOverrideUrlLoading;
                if (s.startsWith("ueapp://dismiss")) {
                    PartyUpWelcomeDialogFragment.this.onAskLaterClicked((View)PartyUpWelcomeDialogFragment.this.mWebView);
                    shouldOverrideUrlLoading = true;
                }
                else {
                    shouldOverrideUrlLoading = super.shouldOverrideUrlLoading(webView, s);
                }
                return shouldOverrideUrlLoading;
            }
        };
    }
    
    private void createBackground() {
        this.mMainDevice.measure(-2, -2);
        this.mSpeakerWidth = this.mMainDevice.getMeasuredWidth();
        this.mSpeakerHeight = this.mMainDevice.getMeasuredHeight();
        this.place60DotDevices();
        this.place61DotDevices();
        this.place62DotDevices();
        this.place63DotDevices();
        this.place64DotDevices();
        this.place65DotDevices();
        this.setDotDevicesAlpha(1.0f);
    }
    
    private ImageView createDotDevice(final int imageResource) {
        final ImageView imageView = new ImageView((Context)this.getActivity());
        imageView.setVisibility(0);
        imageView.setAlpha(0.0f);
        imageView.setImageResource(imageResource);
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams((int)this.getResources().getDimension(2131361873), (int)this.getResources().getDimension(2131361873));
        relativeLayout$LayoutParams.addRule(13, -1);
        this.mBackgroundView.addView((View)imageView, (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
        return imageView;
    }
    
    private List<ImageView> createDotDevices(final int initialCapacity, final int n) {
        final ArrayList<ImageView> list = new ArrayList<ImageView>(initialCapacity);
        for (int i = 0; i < initialCapacity; ++i) {
            list.add(this.createDotDevice(n));
        }
        return list;
    }
    
    public static PartyUpWelcomeDialogFragment getInstance(final int n, final boolean b) {
        final PartyUpWelcomeDialogFragment partyUpWelcomeDialogFragment = new PartyUpWelcomeDialogFragment();
        final Bundle arguments = new Bundle();
        arguments.putInt("device_color", n);
        arguments.putBoolean("is_ota_supported", b);
        partyUpWelcomeDialogFragment.setArguments(arguments);
        partyUpWelcomeDialogFragment.setCancelable(false);
        return partyUpWelcomeDialogFragment;
    }
    
    private void place60DotDevices() {
        final List<ImageView> dotDevices = this.createDotDevices(2, 2130837688);
        final ImageView imageView = dotDevices.get(0);
        imageView.setTranslationX((float)(int)(-1.85f * this.mSpeakerWidth));
        imageView.setTranslationY((float)(int)(-1.05f * this.mSpeakerHeight));
        final ImageView imageView2 = dotDevices.get(1);
        imageView2.setTranslationX((float)(int)(2.1f * this.mSpeakerWidth));
        imageView2.setTranslationY((float)(int)(0.5f * this.mSpeakerHeight));
        this.mDotDevices.addAll(dotDevices);
    }
    
    private void place61DotDevices() {
        final List<ImageView> dotDevices = this.createDotDevices(3, 2130837691);
        final ImageView imageView = dotDevices.get(0);
        imageView.setTranslationX((float)(int)(-1.15f * this.mSpeakerWidth));
        imageView.setTranslationY((float)(int)(-0.4f * this.mSpeakerHeight));
        final ImageView imageView2 = dotDevices.get(1);
        imageView2.setTranslationX((float)(int)(1.4 * this.mSpeakerWidth));
        imageView2.setTranslationY((float)(int)(0.8 * this.mSpeakerHeight));
        final ImageView imageView3 = dotDevices.get(2);
        imageView3.setTranslationX((float)(int)(2.05f * this.mSpeakerWidth));
        imageView3.setTranslationY((float)(int)(-0.9f * this.mSpeakerHeight));
        this.mDotDevices.addAll(dotDevices);
    }
    
    private void place62DotDevices() {
        final List<ImageView> dotDevices = this.createDotDevices(5, 2130837694);
        final ImageView imageView = dotDevices.get(0);
        imageView.setTranslationX((float)(int)(-2.1 * this.mSpeakerWidth));
        imageView.setTranslationY((float)(int)(-0.2 * this.mSpeakerHeight));
        final ImageView imageView2 = dotDevices.get(1);
        imageView2.setTranslationX((float)(int)(this.mSpeakerWidth * -1.05));
        imageView2.setTranslationY((float)(int)(0.65 * this.mSpeakerHeight));
        final ImageView imageView3 = dotDevices.get(2);
        imageView3.setTranslationX((float)(this.mSpeakerWidth * 0));
        imageView3.setTranslationY((float)(int)(this.mSpeakerHeight * -1.05));
        final ImageView imageView4 = dotDevices.get(3);
        imageView4.setTranslationX((float)(int)(1.2 * this.mSpeakerWidth));
        imageView4.setTranslationY((float)(int)(1.35 * this.mSpeakerHeight));
        final ImageView imageView5 = dotDevices.get(4);
        imageView5.setTranslationX((float)(int)(1.21 * this.mSpeakerWidth));
        imageView5.setTranslationY((float)(int)(-0.25 * this.mSpeakerHeight));
        this.mDotDevices.addAll(dotDevices);
    }
    
    private void place63DotDevices() {
        final List<ImageView> dotDevices = this.createDotDevices(3, 2130837697);
        final ImageView imageView = dotDevices.get(0);
        imageView.setTranslationX((float)(int)(-2.1 * this.mSpeakerWidth));
        imageView.setTranslationY((float)(int)(1.1 * this.mSpeakerHeight));
        final ImageView imageView2 = dotDevices.get(1);
        imageView2.setTranslationX((float)(int)(-2.0 * this.mSpeakerWidth));
        imageView2.setTranslationY((float)(int)(-0.6 * this.mSpeakerHeight));
        final ImageView imageView3 = dotDevices.get(2);
        imageView3.setTranslationX((float)(int)(1.75 * this.mSpeakerWidth));
        imageView3.setTranslationY((float)(int)(-0.5 * this.mSpeakerHeight));
        this.mDotDevices.addAll(dotDevices);
    }
    
    private void place64DotDevices() {
        final List<ImageView> dotDevices = this.createDotDevices(5, 2130837700);
        final ImageView imageView = dotDevices.get(0);
        imageView.setTranslationX((float)(int)(-1.85 * this.mSpeakerWidth));
        imageView.setTranslationY((float)(int)(0.35 * this.mSpeakerHeight));
        final ImageView imageView2 = dotDevices.get(1);
        imageView2.setTranslationX((float)(int)(-1.05 * this.mSpeakerWidth));
        imageView2.setTranslationY((float)(int)(-1.0 * this.mSpeakerHeight));
        final ImageView imageView3 = dotDevices.get(2);
        imageView3.setTranslationX((float)(int)(this.mSpeakerWidth * -0.75));
        imageView3.setTranslationY((float)(int)(1.15 * this.mSpeakerHeight));
        final ImageView imageView4 = dotDevices.get(3);
        imageView4.setTranslationX((float)(int)(0.8 * this.mSpeakerWidth));
        imageView4.setTranslationY((float)(int)(this.mSpeakerHeight * -0.75));
        final ImageView imageView5 = dotDevices.get(4);
        imageView5.setTranslationX((float)(int)(1.2 * this.mSpeakerWidth));
        imageView5.setTranslationY((float)(int)(0.4 * this.mSpeakerHeight));
        this.mDotDevices.addAll(dotDevices);
    }
    
    private void place65DotDevices() {
        final List<ImageView> dotDevices = this.createDotDevices(5, 2130837703);
        final ImageView imageView = dotDevices.get(0);
        imageView.setTranslationX((float)(int)(-1.25f * this.mSpeakerWidth));
        imageView.setTranslationY((float)(int)(this.mSpeakerHeight * 0.0f));
        final ImageView imageView2 = dotDevices.get(1);
        imageView2.setTranslationX((float)(int)(-0.49f * this.mSpeakerWidth));
        imageView2.setTranslationY((float)(int)(-0.75f * this.mSpeakerHeight));
        final ImageView imageView3 = dotDevices.get(2);
        imageView3.setTranslationX((float)(int)(0.5f * this.mSpeakerWidth));
        imageView3.setTranslationY((float)(int)(0.85f * this.mSpeakerHeight));
        final ImageView imageView4 = dotDevices.get(3);
        imageView4.setTranslationX((float)(int)(1.3f * this.mSpeakerWidth));
        imageView4.setTranslationY((float)(int)(-1.0f * this.mSpeakerHeight));
        final ImageView imageView5 = dotDevices.get(4);
        imageView5.setTranslationX((float)(int)(1.95f * this.mSpeakerWidth));
        imageView5.setTranslationY((float)(int)(this.mSpeakerHeight * 0.0f));
        this.mDotDevices.addAll(dotDevices);
    }
    
    private void setDotDevicesAlpha(final float alpha) {
        final Iterator<ImageView> iterator = this.mDotDevices.iterator();
        while (iterator.hasNext()) {
            iterator.next().setAlpha(alpha);
        }
    }
    
    @OnClick({ 2131624242 })
    void onAskLaterClicked(final View view) {
        Log.d(PartyUpWelcomeDialogFragment.TAG, "onAskLater clicked");
        this.dismiss();
    }
    
    @OnClick({ 2131624192 })
    void onBackClicked(final View view) {
        Log.d(PartyUpWelcomeDialogFragment.TAG, "onBack clicked");
        this.switchFlipperView(0, false);
    }
    
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, @Nullable final ViewGroup viewGroup, @Nullable final Bundle bundle) {
        return layoutInflater.inflate(2130968668, viewGroup, false);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        this.mContent.setAlpha(0.0f);
        this.mBackgroundView.setScaleX(4.0f);
        this.mBackgroundView.setScaleY(4.0f);
        this.mBackgroundView.animate().scaleX(1.0f).scaleY(1.0f).setDuration((long)(AnimationUtils.getAndroidLongAnimationTime(this.getContext()) * 2)).start();
        this.mContent.animate().alpha(1.0f).setDuration((long)AnimationUtils.getAndroidLongAnimationTime(this.getContext())).setStartDelay((long)(AnimationUtils.getAndroidLongAnimationTime(this.getContext()) * 2)).start();
    }
    
    @OnClick({ 2131624241 })
    void onUpdateClicked(final View view) {
        Log.d(PartyUpWelcomeDialogFragment.TAG, "onUpdate clicked");
        if (this.getArguments().getBoolean("is_ota_supported")) {
            ((MainActivity)this.getActivity()).beginOTAUpdate();
            this.dismiss();
        }
        else {
            this.switchFlipperView(1, true);
        }
    }
    
    @Override
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        this.mMainDevice.setDeviceColor(this.getArguments().getInt("device_color"));
        if (this.getArguments().getBoolean("is_ota_supported")) {
            this.mUpdateButton.setText((CharSequence)this.getString(2131165343));
        }
        else {
            this.mWebView.getSettings().setJavaScriptEnabled(true);
            this.mWebView.setBackgroundColor(0);
            this.mWebView.setWebViewClient(this.webViewClient);
            this.mWebView.loadUrl(this.getString(2131165478));
            this.mUpdateButton.setText((CharSequence)this.getString(2131165449));
        }
        final Button mUpdateButton = this.mUpdateButton;
        String text;
        if (this.getArguments().getBoolean("is_ota_supported")) {
            text = this.getString(2131165343);
        }
        else {
            text = this.getString(2131165449);
        }
        mUpdateButton.setText((CharSequence)text);
        this.createBackground();
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
}
