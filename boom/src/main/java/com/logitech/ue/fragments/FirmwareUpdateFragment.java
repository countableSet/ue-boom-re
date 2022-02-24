// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import com.logitech.ue.centurion.device.devicedata.UEDeviceType;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import com.logitech.ue.UEColourHelper;
import butterknife.ButterKnife;
import android.view.View;
import android.view.LayoutInflater;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.widget.TextView;
import android.view.ViewGroup;
import butterknife.Bind;
import com.logitech.ue.views.ColorFlipperLayout;
import android.support.v4.app.Fragment;

public class FirmwareUpdateFragment extends Fragment
{
    public static final String PARAM_DEVICE_COLOR = "device_color";
    public static final String PARAM_UPDATE_VERSION = "update_version";
    public static final String TAG;
    @Bind({ 2131624136 })
    ColorFlipperLayout mColorFlipperView;
    @Bind({ 2131624137 })
    ViewGroup mContentView;
    public int mDeviceColor;
    @Bind({ 2131624141 })
    TextView mDeviceModelLabel;
    public float mProgress;
    @Bind({ 2131624139 })
    TextView mProgressLabel;
    @Bind({ 2131624143 })
    TextView mTimeLabel;
    public String mVersion;
    @Bind({ 2131624142 })
    TextView mVersionLabel;
    
    static {
        TAG = FirmwareUpdateFragment.class.getSimpleName();
    }
    
    public static FirmwareUpdateFragment getInstance(final int n, final String s) {
        final FirmwareUpdateFragment firmwareUpdateFragment = new FirmwareUpdateFragment();
        final Bundle arguments = new Bundle();
        arguments.putInt("device_color", n);
        arguments.putString("update_version", s);
        firmwareUpdateFragment.setArguments(arguments);
        return firmwareUpdateFragment;
    }
    
    public float getProgress() {
        return this.mProgress;
    }
    
    @Override
    public void onCreate(@Nullable Bundle arguments) {
        super.onCreate(arguments);
        arguments = this.getArguments();
        this.mDeviceColor = arguments.getInt("device_color", 0);
        this.mVersion = arguments.getString("update_version", "1.0.0");
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(2130968628, viewGroup, false);
    }
    
    @Override
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        this.updateVersion(this.mVersion);
        this.updateDeviceType(UEColourHelper.getDeviceTypeByColour(this.mDeviceColor));
        this.updateColors(ContextCompat.getColor((Context)this.getActivity(), 17170443), ContextCompat.getColor((Context)this.getActivity(), 2131558420));
    }
    
    public void setProgress(final float n) {
        float n2 = n;
        if (n > 99.0f) {
            n2 = 99.0f;
        }
        this.mProgress = n2;
        this.mProgressLabel.setText((CharSequence)String.valueOf(Math.round(n2)));
        this.mColorFlipperView.setLevel(Math.round(n2 / 100.0f * 10000.0f));
    }
    
    public void updateColors(final int n, int i) {
        this.mColorFlipperView.setPrimaryColor(n);
        this.mColorFlipperView.setSecondaryColor(i);
        View child;
        for (i = 0; i < this.mContentView.getChildCount(); ++i) {
            child = this.mContentView.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView)child).setTextColor(n);
            }
        }
    }
    
    public void updateDeviceType(final UEDeviceType ueDeviceType) {
        this.mDeviceModelLabel.setText(UEColourHelper.getDeviceSpecificNameResource(ueDeviceType));
    }
    
    public void updateVersion(final String s) {
        this.mVersionLabel.setText((CharSequence)this.getString(2131165277, s));
    }
}
