// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.support.annotation.Nullable;
import android.content.DialogInterface;
import butterknife.ButterKnife;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import butterknife.OnClick;
import android.os.Bundle;
import android.content.DialogInterface$OnDismissListener;
import android.widget.ImageView;
import butterknife.Bind;
import android.view.View;
import android.support.v4.app.DialogFragment;

public class XUPTutorialDialogFragment extends DialogFragment
{
    public static final String TAG;
    @Bind({ 2131624156 })
    protected View mCloseButton;
    @Bind({ 2131624137 })
    protected ImageView mContentView;
    DialogInterface$OnDismissListener mDismissListener;
    
    static {
        TAG = XUPTutorialDialogFragment.class.getSimpleName();
    }
    
    public static XUPTutorialDialogFragment getInstance() {
        final XUPTutorialDialogFragment xupTutorialDialogFragment = new XUPTutorialDialogFragment();
        xupTutorialDialogFragment.setArguments(new Bundle());
        return xupTutorialDialogFragment;
    }
    
    public DialogInterface$OnDismissListener getDismissListener() {
        return this.mDismissListener;
    }
    
    @OnClick({ 2131624156 })
    public void onCloseClicked(final View view) {
        this.dismiss();
    }
    
    @OnClick({ 2131624137 })
    public void onContentClicked(final View view) {
        this.dismiss();
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setCancelable(false);
        this.setStyle(1, 2131427529);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(2130968642, viewGroup, false);
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    
    @Override
    public void onDismiss(final DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if (this.mDismissListener != null) {
            this.mDismissListener.onDismiss(dialogInterface);
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();
        if (this.isVisible()) {
            this.dismiss();
        }
    }
    
    @Override
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
    }
    
    public void setDismissListener(final DialogInterface$OnDismissListener mDismissListener) {
        this.mDismissListener = mDismissListener;
    }
}
