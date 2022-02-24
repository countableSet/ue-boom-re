// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.content.DialogInterface$OnDismissListener;
import com.logitech.ue.App;
import android.util.Log;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStreamingStatus;
import com.logitech.ue.centurion.device.devicedata.UEChargingInfo;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.device.devicedata.UEDeviceType;
import com.logitech.ue.UEColourHelper;
import com.logitech.ue.centurion.UEDeviceManager;
import android.webkit.WebResourceResponse;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebViewClient;
import android.os.AsyncTask;
import android.os.AsyncTask$Status;
import butterknife.ButterKnife;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.annotation.Nullable;
import android.content.Context;
import butterknife.OnClick;
import android.view.View;
import android.os.Bundle;
import butterknife.Bind;
import android.webkit.WebView;
import android.support.v4.app.Fragment;

public class FirmwareUpdateMenuFragment extends Fragment
{
    public static final String PARAM_DETAILS_URL = "details_url";
    public static final String TAG;
    private CheckDeviceTask mCheckDeviceTask;
    Listener mListener;
    private String mUpdateDetailsURL;
    @Bind({ 2131624152 })
    WebView mWebView;
    
    static {
        TAG = FirmwareUpdateMenuFragment.class.getSimpleName();
    }
    
    public static FirmwareUpdateMenuFragment getInstance(final String s) {
        final FirmwareUpdateMenuFragment firmwareUpdateMenuFragment = new FirmwareUpdateMenuFragment();
        final Bundle arguments = new Bundle();
        arguments.putString("details_url", s);
        firmwareUpdateMenuFragment.setArguments(arguments);
        return firmwareUpdateMenuFragment;
    }
    
    @OnClick({ 2131624153 })
    void onAskLaterClicked(final View view) {
        this.mListener.onAskLaterClicked();
    }
    
    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (this.getActivity() instanceof Listener) {
            this.mListener = (Listener)this.getActivity();
            return;
        }
        throw new IllegalArgumentException();
    }
    
    @Override
    public void onCreate(@Nullable final Bundle bundle) {
        super.onCreate(bundle);
        this.mUpdateDetailsURL = this.getArguments().getString("details_url", "about:blank");
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(2130968630, viewGroup, false);
    }
    
    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }
    
    @OnClick({ 2131624154 })
    void onUpdateClicked(final View view) {
        if (this.mCheckDeviceTask == null || this.mCheckDeviceTask.getStatus() == AsyncTask$Status.FINISHED) {
            (this.mCheckDeviceTask = new CheckDeviceTask()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
        }
    }
    
    @Override
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.setBackgroundColor(0);
        this.mWebView.setWebViewClient((WebViewClient)new WebViewClient() {
            public void onReceivedError(final WebView webView, final int n, final String s, final String s2) {
                super.onReceivedError(webView, n, s, s2);
                if (FirmwareUpdateMenuFragment.this.getView() != null) {
                    FirmwareUpdateMenuFragment.this.mWebView.loadUrl("about:blank");
                }
            }
            
            public void onReceivedError(final WebView webView, final WebResourceRequest webResourceRequest, final WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                if (FirmwareUpdateMenuFragment.this.getView() != null) {
                    FirmwareUpdateMenuFragment.this.mWebView.loadUrl("about:blank");
                }
            }
            
            public void onReceivedHttpError(final WebView webView, final WebResourceRequest webResourceRequest, final WebResourceResponse webResourceResponse) {
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
                if (FirmwareUpdateMenuFragment.this.getView() != null) {
                    FirmwareUpdateMenuFragment.this.mWebView.loadUrl("about:blank");
                }
            }
        });
        this.setFirmwareURLDetails(this.mUpdateDetailsURL);
    }
    
    public void setFirmwareURLDetails(final String mUpdateDetailsURL) {
        this.mUpdateDetailsURL = mUpdateDetailsURL;
        this.mWebView.loadUrl(mUpdateDetailsURL);
    }
    
    public class CheckDeviceTask extends AsyncTask<Void, Void, Object>
    {
        protected Object doInBackground(final Void... array) {
            try {
                final Object[] array2 = new Object[3];
                final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
                final int deviceColor = connectedDevice.getDeviceColor();
                if (UEColourHelper.getDeviceTypeByColour(deviceColor) != UEDeviceType.Unknown) {
                    array2[0] = UEColourHelper.getDeviceTypeByColour(deviceColor);
                }
                else {
                    array2[0] = connectedDevice.getDeviceType();
                }
                array2[1] = connectedDevice.getChargingInfo();
                array2[2] = connectedDevice.getDeviceStreamingStatus();
                return array2;
            }
            catch (Exception array2) {
                return array2;
            }
        }
        
        protected void onPostExecute(final Object o) {
            super.onPostExecute(o);
            if (o instanceof Object[]) {
                final Object[] array = (Object[])o;
                final UEDeviceType ueDeviceType = (UEDeviceType)array[0];
                final UEChargingInfo ueChargingInfo = (UEChargingInfo)array[1];
                final UEDeviceStreamingStatus ueDeviceStreamingStatus = (UEDeviceStreamingStatus)array[2];
                if (ueChargingInfo.getCharge() < 20) {
                    Log.w(FirmwareUpdateMenuFragment.TAG, "Device can't be updated because battery level is low");
                    App.getInstance().showMessageDialog(FirmwareUpdateMenuFragment.this.getString(2131165226, UEColourHelper.getDeviceSpecificName(ueDeviceType, FirmwareUpdateMenuFragment.this.getContext())), null);
                }
                else if (ueDeviceStreamingStatus.isDevicePairedStatus()) {
                    Log.w(FirmwareUpdateMenuFragment.TAG, "Device can't be updated because it is in TWS state");
                    App.getInstance().showMessageDialog(FirmwareUpdateMenuFragment.this.getString(2131165430), null);
                }
                else {
                    Log.d(FirmwareUpdateMenuFragment.TAG, "Speaker is fine to update");
                    FirmwareUpdateMenuFragment.this.mListener.onUpdateClicked();
                }
            }
            else {
                Log.e(FirmwareUpdateMenuFragment.TAG, "Failed to check device");
                App.getInstance().gotoNuclearHome((Exception)o);
            }
        }
        
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(FirmwareUpdateMenuFragment.TAG, "Checking if device is ready for update");
        }
    }
    
    public interface Listener
    {
        void onAskLaterClicked();
        
        void onUpdateClicked();
    }
}
