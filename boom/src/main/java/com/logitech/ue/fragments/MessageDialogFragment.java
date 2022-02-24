// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.support.annotation.NonNull;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.content.res.Resources$Theme;
import android.support.graphics.drawable.VectorDrawableCompat;
import butterknife.ButterKnife;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.util.Log;
import android.content.DialogInterface;
import android.os.Parcelable;
import android.os.Bundle;
import android.content.DialogInterface$OnDismissListener;
import android.content.DialogInterface$OnClickListener;
import android.content.DialogInterface$OnCancelListener;
import android.support.v4.app.DialogFragment;

public class MessageDialogFragment extends DialogFragment
{
    public static final String PARAM_PARAMS = "params";
    public static final String TAG = "MessageDialogFragment";
    protected DialogInterface$OnCancelListener mOnCancelListener;
    protected DialogInterface$OnClickListener mOnClickListener;
    protected DialogInterface$OnDismissListener mOnDismissListener;
    Params mParams;
    
    public static MessageDialogFragment getInstance(final Params params) {
        final MessageDialogFragment messageDialogFragment = new MessageDialogFragment();
        final Bundle arguments = new Bundle();
        arguments.putParcelable("params", (Parcelable)params);
        messageDialogFragment.setArguments(arguments);
        messageDialogFragment.setDismissListener(params.mOnDismissListener);
        messageDialogFragment.setOnClickListener(params.mOnClickListener);
        messageDialogFragment.setOnCancelListener(params.mOnCancelListener);
        return messageDialogFragment;
    }
    
    @Override
    public void onCancel(final DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        Log.d("MessageDialogFragment", "OnCancel dialog");
        if (this.mOnCancelListener != null) {
            this.mOnCancelListener.onCancel(dialogInterface);
        }
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle bundle) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        this.mParams = (Params)this.getArguments().getParcelable("params");
        builder.setTitle(this.mParams.mTitle).setMessage(this.mParams.mMessage).setPositiveButton(this.mParams.mPositiveButton, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                Log.d("MessageDialogFragment", "On positive button clicked");
                if (MessageDialogFragment.this.mOnClickListener != null) {
                    MessageDialogFragment.this.mOnClickListener.onClick((DialogInterface)MessageDialogFragment.this.getDialog(), n);
                }
                MessageDialogFragment.this.dismiss();
            }
        }).setNegativeButton(this.mParams.mNegativeButton, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                Log.d("MessageDialogFragment", "On negative button clicked");
                if (MessageDialogFragment.this.mOnClickListener != null) {
                    MessageDialogFragment.this.mOnClickListener.onClick((DialogInterface)MessageDialogFragment.this.getDialog(), n);
                }
                MessageDialogFragment.this.dismiss();
            }
        }).setNeutralButton(this.mParams.mNeutralButton, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                Log.d("MessageDialogFragment", "On neutral button clicked");
                if (MessageDialogFragment.this.mOnClickListener != null) {
                    MessageDialogFragment.this.mOnClickListener.onClick((DialogInterface)MessageDialogFragment.this.getDialog(), n);
                }
                MessageDialogFragment.this.dismiss();
            }
        }).setCancelable(this.mParams.mIsCancelable);
        if (this.mParams.mImage != 0) {
            final View inflate = ((LayoutInflater)this.getContext().getSystemService("layout_inflater")).inflate(2130968645, (ViewGroup)null);
            ((ImageView)ButterKnife.findById(inflate, 2131623989)).setImageDrawable((Drawable)VectorDrawableCompat.create(this.getResources(), this.mParams.mImage, null));
            builder.setView(inflate);
        }
        final AlertDialog create = builder.create();
        create.getWindow().setGravity(80);
        create.getWindow();
        return create;
    }
    
    @Override
    public void onDismiss(final DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        Log.d("MessageDialogFragment", "OnDismiss dialog");
        if (this.mOnDismissListener != null) {
            this.mOnDismissListener.onDismiss(dialogInterface);
        }
    }
    
    @Override
    public void onStart() {
        final boolean b = true;
        super.onStart();
        if (this.mParams != null) {
            this.getDialog().getWindow().findViewById(16908313).setSelected(this.mParams.mSelectedButton == -1);
            this.getDialog().getWindow().findViewById(16908314).setSelected(this.mParams.mSelectedButton == -2);
            this.getDialog().getWindow().findViewById(16908315).setSelected(this.mParams.mSelectedButton == -3 && b);
        }
    }
    
    public void setDismissListener(final DialogInterface$OnDismissListener mOnDismissListener) {
        this.mOnDismissListener = mOnDismissListener;
    }
    
    public void setOnCancelListener(final DialogInterface$OnCancelListener mOnCancelListener) {
        this.mOnCancelListener = mOnCancelListener;
    }
    
    public void setOnClickListener(final DialogInterface$OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }
    
    public static class Builder
    {
        Params mParams;
        
        public Builder() {
            this.mParams = new Params();
        }
        
        public MessageDialogFragment create() {
            return MessageDialogFragment.getInstance(this.mParams);
        }
        
        public Builder setCancelListener(final DialogInterface$OnCancelListener mOnCancelListener) {
            this.mParams.mOnCancelListener = mOnCancelListener;
            return this;
        }
        
        public Builder setCancelable(final boolean mIsCancelable) {
            this.mParams.mIsCancelable = mIsCancelable;
            return this;
        }
        
        public Builder setClickListener(final DialogInterface$OnClickListener mOnClickListener) {
            this.mParams.mOnClickListener = mOnClickListener;
            return this;
        }
        
        public Builder setDismissListener(final DialogInterface$OnDismissListener mOnDismissListener) {
            this.mParams.mOnDismissListener = mOnDismissListener;
            return this;
        }
        
        public Builder setImage(final int mImage) {
            this.mParams.mImage = mImage;
            return this;
        }
        
        public Builder setMessage(final String mMessage) {
            this.mParams.mMessage = mMessage;
            return this;
        }
        
        public Builder setNegativeButton(final String mNegativeButton) {
            this.mParams.mNegativeButton = mNegativeButton;
            return this;
        }
        
        public Builder setNeutralButton(final String mNeutralButton) {
            this.mParams.mNeutralButton = mNeutralButton;
            return this;
        }
        
        public Builder setPositiveButton(final String mPositiveButton) {
            this.mParams.mPositiveButton = mPositiveButton;
            return this;
        }
        
        public Builder setProgress(final boolean mIsProgress) {
            this.mParams.mIsProgress = mIsProgress;
            return this;
        }
        
        public Builder setSelectedButton(final int mSelectedButton) {
            this.mParams.mSelectedButton = mSelectedButton;
            return this;
        }
        
        public Builder setTitle(final String mTitle) {
            this.mParams.mTitle = mTitle;
            return this;
        }
    }
    
    protected static class Params implements Parcelable
    {
        public static final Parcelable$Creator CREATOR;
        int mImage;
        boolean mIsCancelable;
        boolean mIsProgress;
        String mMessage;
        String mNegativeButton;
        String mNeutralButton;
        DialogInterface$OnCancelListener mOnCancelListener;
        DialogInterface$OnClickListener mOnClickListener;
        DialogInterface$OnDismissListener mOnDismissListener;
        String mPositiveButton;
        int mSelectedButton;
        String mTitle;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator() {
                public Params createFromParcel(final Parcel parcel) {
                    return new Params(parcel);
                }
                
                public Params[] newArray(final int n) {
                    return new Params[n];
                }
            };
        }
        
        public Params() {
            this.mSelectedButton = -2;
        }
        
        public Params(final Parcel parcel) {
            final boolean b = true;
            this.mSelectedButton = -2;
            this.mTitle = parcel.readString();
            this.mMessage = parcel.readString();
            this.mPositiveButton = parcel.readString();
            this.mNegativeButton = parcel.readString();
            this.mNeutralButton = parcel.readString();
            this.mImage = parcel.readInt();
            this.mSelectedButton = parcel.readInt();
            this.mIsCancelable = (parcel.readInt() == 1);
            this.mIsProgress = (parcel.readInt() == 1 && b);
        }
        
        public int describeContents() {
            return 0;
        }
        
        public void writeToParcel(final Parcel parcel, int n) {
            final int n2 = 1;
            parcel.writeString(this.mTitle);
            parcel.writeString(this.mMessage);
            parcel.writeString(this.mPositiveButton);
            parcel.writeString(this.mNegativeButton);
            parcel.writeString(this.mNeutralButton);
            parcel.writeInt(this.mImage);
            parcel.writeInt(this.mSelectedButton);
            if (this.mIsCancelable) {
                n = 1;
            }
            else {
                n = 0;
            }
            parcel.writeInt(n);
            if (this.mIsProgress) {
                n = n2;
            }
            else {
                n = 0;
            }
            parcel.writeInt(n);
        }
    }
}
