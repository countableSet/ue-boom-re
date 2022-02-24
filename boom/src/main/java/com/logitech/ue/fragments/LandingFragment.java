// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.text.TextUtils;
import com.logitech.ue.UserPreferences;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import butterknife.ButterKnife;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import butterknife.OnClick;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;
import android.util.Log;
import android.content.Intent;
import android.content.Context;
import android.widget.TextView;
import com.logitech.ue.views.UEDeviceView;
import android.view.View;
import butterknife.Bind;
import android.widget.ImageButton;
import android.content.BroadcastReceiver;
import android.support.v4.app.Fragment;

public class LandingFragment extends Fragment
{
    public static final String TAG;
    BroadcastReceiver mBroadcastReceiver;
    @Bind({ 2131624164 })
    ImageButton mBulletsButton;
    @Bind({ 2131624165 })
    View mCloudPopUp;
    @Bind({ 2131624162 })
    UEDeviceView mGhostDeviceView;
    @Bind({ 2131624102 })
    TextView mSpeakerVersionLabel;
    @Bind({ 2131624103 })
    TextView mSpeakerVersionView;
    
    static {
        TAG = LandingFragment.class.getSimpleName();
    }
    
    public LandingFragment() {
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                Log.d(LandingFragment.TAG, "Receive broadcast " + intent.getAction());
                if (intent.getAction().equals("com.logitech.ue.centurion.CONNECTION_CHANGED")) {
                    LandingFragment.this.hideBulletsPopup();
                    if (UEDeviceStatus.getStatus(intent.getIntExtra("status", UEDeviceStatus.getValue(UEDeviceStatus.DISCONNECTED))) != UEDeviceStatus.DISCONNECTED) {
                        LandingFragment.this.getActivity().getSupportFragmentManager().beginTransaction().hide(LandingFragment.this).commitAllowingStateLoss();
                    }
                }
            }
        };
    }
    
    private void hideBulletsPopup() {
        this.mBulletsButton.setSelected(false);
        this.mCloudPopUp.setVisibility(8);
    }
    
    private void showBulletsAndPopUp() {
        this.mBulletsButton.setSelected(true);
        this.mCloudPopUp.setVisibility(0);
    }
    
    @OnClick({ 2131624164 })
    public void onBulletsClicked(final View view) {
        if (this.mBulletsButton.isSelected()) {
            this.hideBulletsPopup();
        }
        else {
            this.showBulletsAndPopUp();
        }
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(2130968634, viewGroup, false);
    }
    
    @Override
    public void onDestroyView() {
        this.hideBulletsPopup();
        ButterKnife.unbind(this);
        super.onDestroyView();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(this.mBroadcastReceiver, new IntentFilter("com.logitech.ue.centurion.CONNECTION_CHANGED"));
    }
    
    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(this.mBroadcastReceiver);
        super.onStop();
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        view.setClickable(true);
        ((TextView)this.mCloudPopUp.findViewById(2131624101)).setText((CharSequence)"5.0.166");
        final String lastSeenSpeakerVersion = UserPreferences.getInstance().getLastSeenSpeakerVersion();
        if (!TextUtils.isEmpty((CharSequence)lastSeenSpeakerVersion)) {
            this.mSpeakerVersionView.setText((CharSequence)lastSeenSpeakerVersion);
        }
        else {
            this.mSpeakerVersionView.setVisibility(8);
            this.mSpeakerVersionLabel.setVisibility(8);
        }
    }
}
