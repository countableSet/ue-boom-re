// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.support.annotation.NonNull;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.os.Parcelable;
import android.os.Bundle;

public class InfiniteProgressDialogFragment extends MessageDialogFragment
{
    public static final String TAG;
    
    static {
        TAG = InfiniteProgressDialogFragment.class.getSimpleName();
    }
    
    public static InfiniteProgressDialogFragment getInstance(final Params params) {
        final InfiniteProgressDialogFragment infiniteProgressDialogFragment = new InfiniteProgressDialogFragment();
        final Bundle arguments = new Bundle();
        arguments.putParcelable("params", (Parcelable)params);
        infiniteProgressDialogFragment.setArguments(arguments);
        return infiniteProgressDialogFragment;
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setCancelable(false);
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle bundle) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        final Params params = (Params)this.getArguments().getParcelable("params");
        builder.setTitle(params.mTitle).setMessage(params.mMessage).setPositiveButton(params.mPositiveButton, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                if (InfiniteProgressDialogFragment.this.mOnClickListener != null) {
                    InfiniteProgressDialogFragment.this.mOnClickListener.onClick((DialogInterface)InfiniteProgressDialogFragment.this.getDialog(), n);
                }
                InfiniteProgressDialogFragment.this.dismiss();
            }
        }).setNegativeButton(params.mNegativeButton, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                if (InfiniteProgressDialogFragment.this.mOnClickListener != null) {
                    InfiniteProgressDialogFragment.this.mOnClickListener.onClick((DialogInterface)InfiniteProgressDialogFragment.this.getDialog(), n);
                }
                InfiniteProgressDialogFragment.this.dismiss();
            }
        }).setNeutralButton(params.mNeutralButton, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                if (InfiniteProgressDialogFragment.this.mOnClickListener != null) {
                    InfiniteProgressDialogFragment.this.mOnClickListener.onClick((DialogInterface)InfiniteProgressDialogFragment.this.getDialog(), n);
                }
                InfiniteProgressDialogFragment.this.dismiss();
            }
        }).setCancelable(params.mIsCancelable);
        builder.setView(2130968669);
        final AlertDialog create = builder.create();
        create.getWindow().setGravity(80);
        return create;
    }
    
    public static class Builder extends MessageDialogFragment.Builder
    {
        public InfiniteProgressDialogFragment create() {
            return InfiniteProgressDialogFragment.getInstance(this.mParams);
        }
    }
}
