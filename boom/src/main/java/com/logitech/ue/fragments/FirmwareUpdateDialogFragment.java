// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.annotation.NonNull;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ColorDrawable;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import com.logitech.ue.views.CircularSeekBar;
import android.support.v4.app.DialogFragment;

public class FirmwareUpdateDialogFragment extends DialogFragment
{
    public static final String PARAM_DEVICE_TYPE_NAME = "device_name";
    public static final String TAG;
    private CircularSeekBar mCircleBar;
    private View mMainView;
    private int mProgress;
    private TextView mProgressDescriptionTextView;
    private TextView mProgressTextView;
    private View mSecondaryView;
    
    static {
        TAG = FirmwareUpdateDialogFragment.class.getSimpleName();
    }
    
    public FirmwareUpdateDialogFragment() {
        this.mProgress = 0;
    }
    
    public static FirmwareUpdateDialogFragment getInstance(final String s) {
        final FirmwareUpdateDialogFragment firmwareUpdateDialogFragment = new FirmwareUpdateDialogFragment();
        final Bundle arguments = new Bundle();
        arguments.putString("device_name", s);
        firmwareUpdateDialogFragment.setArguments(arguments);
        firmwareUpdateDialogFragment.setStyle(1, 0);
        firmwareUpdateDialogFragment.setCancelable(false);
        return firmwareUpdateDialogFragment;
    }
    
    public int getProgress() {
        return this.mProgress;
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle bundle) {
        final Dialog onCreateDialog = super.onCreateDialog(bundle);
        onCreateDialog.getWindow().setBackgroundDrawable((Drawable)new ColorDrawable(0));
        onCreateDialog.getWindow().getAttributes().windowAnimations = 2131427537;
        onCreateDialog.getWindow().clearFlags(2);
        return onCreateDialog;
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2130968629, viewGroup, false);
        this.mMainView = inflate.findViewById(2131624144);
        this.mSecondaryView = inflate.findViewById(2131624148);
        ((ImageView)inflate.findViewById(2131624030)).setBackgroundResource(17170443);
        this.mCircleBar = (CircularSeekBar)inflate.findViewById(2131624146);
        this.mProgressTextView = (TextView)inflate.findViewById(2131624147);
        (this.mProgressDescriptionTextView = (TextView)inflate.findViewById(2131624145)).setText((CharSequence)this.getString(2131165446, this.getArguments().getString("device_name", this.getString(2131165431))));
        this.mCircleBar.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                return true;
            }
        });
        this.setProgress(0);
        return inflate;
    }
    
    public void setProgress(final int i) {
        this.mProgress = i;
        this.mCircleBar.setProgress(i);
        this.mProgressTextView.setText((CharSequence)String.format("%d%%", i));
    }
    
    public void switchState(final int n) {
        if (n == 0) {
            this.mMainView.setVisibility(0);
            this.mSecondaryView.setVisibility(4);
        }
        else if (n == 1) {
            this.mMainView.setVisibility(4);
            this.mSecondaryView.setVisibility(0);
        }
    }
}
