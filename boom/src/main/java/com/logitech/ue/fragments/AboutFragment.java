// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import com.logitech.ue.tasks.SafeTask;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.logitech.ue.App;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;
import android.os.AsyncTask$Status;
import com.logitech.ue.centurion.UEDeviceManager;
import android.content.Intent;
import android.content.Context;
import android.widget.ListView;
import com.logitech.ue.tasks.GetDeviceFirmwareAndSerialViaBtleTask;
import com.logitech.ue.tasks.GetDeviceFirmwareAndSerialTask;
import com.logitech.ue.devicedata.DeviceFirmwareSerialInfo;
import android.content.BroadcastReceiver;
import android.widget.BaseAdapter;

public class AboutFragment extends NavigatableFragment
{
    private BaseAdapter mAdapter;
    final BroadcastReceiver mBroadcastReceiver;
    private DeviceFirmwareSerialInfo mFirmwareSerialInfo;
    private GetDeviceFirmwareAndSerialTask mGetFirmwareAndSerialTask;
    private GetDeviceFirmwareAndSerialViaBtleTask mGetFirmwareAndSerialViaBleTask;
    private ListView mListView;
    
    public AboutFragment() {
        this.mFirmwareSerialInfo = null;
        this.mGetFirmwareAndSerialTask = null;
        this.mGetFirmwareAndSerialViaBleTask = null;
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getAction().equals("com.logitech.ue.centurion.CONNECTION_CHANGED")) {
                    AboutFragment.this.beginUpdate();
                }
            }
        };
    }
    
    public void beginUpdate() {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        if (connectedDevice != null) {
            if (connectedDevice.getDeviceConnectionStatus().isBtClassicConnectedState()) {
                if (this.mGetFirmwareAndSerialTask != null && this.mGetFirmwareAndSerialTask.getStatus() != AsyncTask$Status.FINISHED) {
                    this.mGetFirmwareAndSerialTask.cancel(true);
                    this.mGetFirmwareAndSerialTask = null;
                }
                ((SafeTask<Void, Progress, Result>)(this.mGetFirmwareAndSerialTask = new GetDeviceFirmwareAndSerialTask() {
                    public void onSuccess(final DeviceFirmwareSerialInfo deviceFirmwareSerialInfo) {
                        super.onSuccess((T)deviceFirmwareSerialInfo);
                        AboutFragment.this.mFirmwareSerialInfo = deviceFirmwareSerialInfo;
                        AboutFragment.this.mAdapter.notifyDataSetChanged();
                    }
                })).executeOnThreadPoolExecutor(new Void[0]);
            }
            else if (connectedDevice.getDeviceConnectionStatus() == UEDeviceStatus.CONNECTED_OFF) {
                if (this.mGetFirmwareAndSerialViaBleTask != null && this.mGetFirmwareAndSerialViaBleTask.getStatus() != AsyncTask$Status.FINISHED) {
                    this.mGetFirmwareAndSerialViaBleTask.cancel(true);
                    this.mGetFirmwareAndSerialViaBleTask = null;
                }
                ((SafeTask<Void, Progress, Result>)(this.mGetFirmwareAndSerialViaBleTask = new GetDeviceFirmwareAndSerialViaBtleTask() {
                    public void onSuccess(final DeviceFirmwareSerialInfo deviceFirmwareSerialInfo) {
                        super.onSuccess((T)deviceFirmwareSerialInfo);
                        AboutFragment.this.mFirmwareSerialInfo = deviceFirmwareSerialInfo;
                        AboutFragment.this.mAdapter.notifyDataSetChanged();
                    }
                })).executeOnThreadPoolExecutor(new Void[0]);
            }
        }
        else {
            this.mFirmwareSerialInfo = null;
            this.mAdapter.notifyDataSetChanged();
        }
    }
    
    @Override
    public String getTitle() {
        return App.getInstance().getString(2131165204);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return (View)(this.mListView = (ListView)layoutInflater.inflate(2130968651, viewGroup, false));
    }
    
    @Override
    public void onResume() {
        super.onResume();
        this.beginUpdate();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.logitech.ue.centurion.CONNECTION_CHANGED");
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(this.mBroadcastReceiver, intentFilter);
    }
    
    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(this.mBroadcastReceiver);
        super.onStop();
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mListView.setDivider((Drawable)null);
        this.mAdapter = new BaseAdapter() {
            final LayoutInflater inflater = (LayoutInflater)AboutFragment.this.getActivity().getSystemService("layout_inflater");
            final /* synthetic */ int[] val$rowStrings = { 2131165224, 2131165411, 2131165382 };
            
            public int getCount() {
                return this.val$rowStrings.length * 2;
            }
            
            public Object getItem(final int n) {
                Object o = null;
                switch (this.val$rowStrings[n]) {
                    case 2131165224: {
                        o = "5.0.166";
                        break;
                    }
                    case 2131165411: {
                        if (AboutFragment.this.mFirmwareSerialInfo != null) {
                            o = AboutFragment.this.mFirmwareSerialInfo.getFirmwareVersion();
                            break;
                        }
                        break;
                    }
                    case 2131165382: {
                        if (AboutFragment.this.mFirmwareSerialInfo != null) {
                            o = AboutFragment.this.mFirmwareSerialInfo.getSerialNumber();
                            break;
                        }
                        break;
                    }
                }
                return o;
            }
            
            public long getItemId(final int n) {
                return 0L;
            }
            
            public int getItemViewType(int n) {
                if (n % 2 == 0) {
                    n = 1;
                }
                else {
                    n = 0;
                }
                return n;
            }
            
            public View getView(int n, final View view, final ViewGroup viewGroup) {
                View view2;
                if (this.getItemViewType(n) == 1) {
                    View inflate;
                    if ((inflate = view) == null) {
                        inflate = this.inflater.inflate(2130968652, viewGroup, false);
                    }
                    inflate.setEnabled(this.isEnabled(n));
                    n /= 2;
                    final TextView textView = (TextView)inflate.findViewById(16908308);
                    final TextView textView2 = (TextView)inflate.findViewById(16908309);
                    textView.setText(this.val$rowStrings[n]);
                    final Object item = this.getItem(n);
                    if (item != null) {
                        textView2.setText((CharSequence)item.toString());
                    }
                    else {
                        textView2.setText((CharSequence)null);
                    }
                    view2 = inflate;
                }
                else if ((view2 = view) == null) {
                    view2 = new View((Context)AboutFragment.this.getActivity());
                }
                return view2;
            }
            
            public int getViewTypeCount() {
                return 3;
            }
            
            public boolean isEnabled(final int n) {
                boolean b = true;
                switch (this.val$rowStrings[n / 2]) {
                    case 2131165411: {
                        if (AboutFragment.this.mFirmwareSerialInfo == null) {
                            b = false;
                            break;
                        }
                        break;
                    }
                    case 2131165382: {
                        if (AboutFragment.this.mFirmwareSerialInfo == null) {
                            b = false;
                            break;
                        }
                        break;
                    }
                }
                return b;
            }
        };
        this.mListView.setAdapter((ListAdapter)this.mAdapter);
    }
}
