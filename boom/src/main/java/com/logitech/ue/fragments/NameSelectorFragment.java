// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import com.logitech.ue.tasks.SafeTask;
import com.logitech.ue.tasks.GetDeviceNameTask;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import android.os.Handler;
import android.view.View$OnKeyListener;
import com.logitech.ue.views.EditTextWithPreIme;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView$OnEditorActionListener;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import com.logitech.ue.App;
import com.logitech.ue.FlurryTracker;
import com.logitech.ue.tasks.SetDeviceNameTask;
import android.util.Log;
import android.widget.Toast;
import android.text.TextUtils;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;
import android.content.Intent;
import android.content.Context;
import android.widget.EditText;
import android.view.View;
import android.content.BroadcastReceiver;

public class NameSelectorFragment extends NavigatableFragment
{
    private static final int NAME_LENGTH_LIMIT = 30;
    public static final String PARAM_DEVICE_INITIAL_NAME = "initial_name";
    public static final String TAG = "NameSelector";
    BroadcastReceiver mBroadcastReceiver;
    private String mInitialDeviceName;
    private View mRoot;
    private EditText mTextEdit;
    
    public NameSelectorFragment() {
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getAction().equals("com.logitech.ue.centurion.CONNECTION_CHANGED")) {
                    final UEDeviceStatus status = UEDeviceStatus.getStatus(intent.getExtras().getInt("status"));
                    if (status == UEDeviceStatus.DISCONNECTED || status == UEDeviceStatus.DISCONNECTING) {
                        NameSelectorFragment.this.getActivity().finish();
                    }
                }
            }
        };
    }
    
    private void cancelResult() {
        if (!TextUtils.equals((CharSequence)this.mInitialDeviceName, (CharSequence)this.mTextEdit.getText().toString())) {
            Toast.makeText((Context)this.getActivity(), 2131165355, 1).show();
        }
    }
    
    private boolean saveResult(final String s) {
        if (s.length() == 0 || s.trim().length() == 0) {
            Log.d("NameSelector", "Invalid name set");
            final Toast text = Toast.makeText((Context)this.getActivity(), 2131165398, 0);
            text.setGravity(17, 0, 0);
            text.show();
        }
        else {
            Log.d("NameSelector", "Save device name");
            ((SafeTask<Void, Progress, Result>)new SetDeviceNameTask(s) {
                @Override
                public void onError(final Exception ex) {
                    super.onError(ex);
                    FlurryTracker.logError("NameSelector", ex.getMessage());
                    NameSelectorFragment.this.getActivity().finish();
                }
                
                public void onSuccess(final Void void1) {
                    super.onSuccess((T)void1);
                    NameSelectorFragment.this.getNavigator().goBack();
                }
            }).executeOnThreadPoolExecutor(new Void[0]);
        }
        return false;
    }
    
    @Override
    public String getTitle() {
        return App.getInstance().getResources().getString(2131165354);
    }
    
    void hideKeyboard() {
        ((InputMethodManager)this.getActivity().getSystemService("input_method")).hideSoftInputFromWindow(this.mTextEdit.getWindowToken(), 0);
    }
    
    @Override
    public boolean onBack() {
        super.onBack();
        this.cancelResult();
        return true;
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        this.mRoot = layoutInflater.inflate(2130968637, viewGroup, false);
        (this.mTextEdit = (EditText)this.mRoot.findViewById(2131624176)).setImeActionLabel((CharSequence)this.getString(2131165378), 6);
        this.mTextEdit.setOnEditorActionListener((TextView$OnEditorActionListener)new TextView$OnEditorActionListener() {
            public boolean onEditorAction(final TextView textView, final int n, final KeyEvent keyEvent) {
                if (((keyEvent != null && keyEvent.getKeyCode() == 66) || n == 6) && NameSelectorFragment.this.saveResult(NameSelectorFragment.this.mTextEdit.getText().toString())) {
                    NameSelectorFragment.this.getNavigator().goBack();
                }
                return true;
            }
        });
        this.mTextEdit.addTextChangedListener((TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
                if (editable.toString().getBytes().length > 30) {
                    editable.delete(editable.toString().length() - 1, editable.toString().length());
                }
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
        });
        ((EditTextWithPreIme)this.mTextEdit).setPreImeListener((View$OnKeyListener)new View$OnKeyListener() {
            public boolean onKey(final View view, final int n, final KeyEvent keyEvent) {
                if (n == 4 && keyEvent.getAction() == 1) {
                    NameSelectorFragment.this.cancelResult();
                    NameSelectorFragment.this.getNavigator().goBack();
                }
                return false;
            }
        });
        return this.mRoot;
    }
    
    @Override
    public void onStart() {
        super.onStart();
        new Handler().postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                NameSelectorFragment.this.showKeyboard();
            }
        }, 300L);
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.logitech.ue.centurion.CONNECTION_CHANGED");
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(this.mBroadcastReceiver, intentFilter);
    }
    
    @Override
    public void onStop() {
        this.hideKeyboard();
        LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(this.mBroadcastReceiver);
        super.onStop();
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        final Bundle arguments = this.getArguments();
        if (arguments != null) {
            this.mInitialDeviceName = arguments.getString("initial_name");
        }
        else {
            this.mInitialDeviceName = "";
            ((SafeTask<Void, Progress, Result>)new GetDeviceNameTask() {
                @Override
                public void onError(final Exception ex) {
                    super.onError(ex);
                    FlurryTracker.logError(NameSelectorFragment.this.getNavigator().getTag(), ex.getMessage());
                    NameSelectorFragment.this.getActivity().finish();
                }
                
                public void onSuccess(final String text) {
                    super.onSuccess((T)text);
                    NameSelectorFragment.this.mTextEdit.setText((CharSequence)text);
                }
            }).executeOnThreadPoolExecutor(new Void[0]);
        }
        this.mTextEdit.setText((CharSequence)this.mInitialDeviceName);
    }
    
    void showKeyboard() {
        ((InputMethodManager)this.getActivity().getSystemService("input_method")).showSoftInput((View)this.mTextEdit, 1);
    }
}
