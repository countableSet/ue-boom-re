// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import com.logitech.ue.tasks.SafeTask;
import android.widget.TextView;
import android.widget.BaseAdapter;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.tasks.GetDeviceDataTask;
import com.logitech.ue.tasks.SetDevicePromiscuousModeTask;
import com.logitech.ue.tasks.SetDeviceStickyTask;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;
import android.widget.ListAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.logitech.ue.App;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;
import android.content.Intent;
import android.content.Context;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import com.logitech.ue.xup.XUPSettingsInfo;
import android.widget.ListView;
import android.content.BroadcastReceiver;

public class XUPSettingsFragment extends NavigatableFragment
{
    private final String TAG;
    private PartyUPSettingsAdapter mAdapter;
    BroadcastReceiver mBroadcastReceiver;
    private ListView mListView;
    private XUPSettingsInfo mSettingsInfo;
    private ArrayList<Integer> rowStrings;
    
    public XUPSettingsFragment() {
        this.TAG = this.getClass().getSimpleName();
        this.rowStrings = new ArrayList<Integer>(Arrays.asList(2131165394, 2131165388));
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getAction().equals("com.logitech.ue.centurion.CONNECTION_CHANGED")) {
                    if (UEDeviceStatus.getStatus(intent.getIntExtra("status", UEDeviceStatus.getValue(UEDeviceStatus.DISCONNECTED))).isBtClassicConnectedState()) {
                        XUPSettingsFragment.this.beginUpdate();
                    }
                    else {
                        XUPSettingsFragment.this.getNavigator().goBack();
                        XUPSettingsFragment.this.mSettingsInfo = null;
                    }
                }
            }
        };
    }
    
    public void beginUpdate() {
        if (App.getDeviceConnectionState().isBtClassicConnectedState()) {
            ((SafeTask<Void, Progress, Result>)new GetXUPSettingsTask()).executeOnThreadPoolExecutor(new Void[0]);
        }
        else {
            this.getNavigator().goBack();
            this.mSettingsInfo = null;
        }
    }
    
    @Override
    public String getTitle() {
        return App.getInstance().getString(2131165392);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return (View)(this.mListView = (ListView)layoutInflater.inflate(2130968651, viewGroup, false));
    }
    
    @Override
    public void onStart() {
        super.onStart();
        this.beginUpdate();
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
        this.mAdapter = new PartyUPSettingsAdapter();
        this.mListView.setAdapter((ListAdapter)this.mAdapter);
        this.mListView.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int index, final long n) {
                boolean isXUPDiscoverable = true;
                switch (XUPSettingsFragment.this.rowStrings.get(index)) {
                    case 2131165394: {
                        XUPSettingsFragment.this.mSettingsInfo.isXUPSticky = !XUPSettingsFragment.this.mSettingsInfo.isXUPSticky;
                        XUPSettingsFragment.this.mAdapter.notifyDataSetChanged();
                        ((SafeTask<Void, Progress, Result>)new SetDeviceStickyTask(XUPSettingsFragment.this.mSettingsInfo.isXUPSticky)).executeOnThreadPoolExecutor(new Void[0]);
                        break;
                    }
                    case 2131165388: {
                        final XUPSettingsInfo access$100 = XUPSettingsFragment.this.mSettingsInfo;
                        if (XUPSettingsFragment.this.mSettingsInfo.isXUPDiscoverable) {
                            isXUPDiscoverable = false;
                        }
                        access$100.isXUPDiscoverable = isXUPDiscoverable;
                        XUPSettingsFragment.this.mAdapter.notifyDataSetChanged();
                        ((SafeTask<Void, Progress, Result>)new SetDevicePromiscuousModeTask(Boolean.valueOf(XUPSettingsFragment.this.mSettingsInfo.isXUPDiscoverable))).executeOnThreadPoolExecutor(new Void[0]);
                        break;
                    }
                }
            }
        });
    }
    
    public class GetXUPSettingsTask extends GetDeviceDataTask<XUPSettingsInfo>
    {
        @Override
        public String getTag() {
            return XUPSettingsFragment.this.TAG;
        }
        
        @Override
        public void onError(final Exception ex) {
            super.onError(ex);
            XUPSettingsFragment.this.getNavigator().goBack();
        }
        
        public void onSuccess(final XUPSettingsInfo xupSettingsInfo) {
            super.onSuccess((T)xupSettingsInfo);
            XUPSettingsFragment.this.mSettingsInfo = xupSettingsInfo;
            XUPSettingsFragment.this.mAdapter.notifyDataSetChanged();
        }
        
        @Override
        public XUPSettingsInfo work(final Void... array) throws Exception {
            final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
            final XUPSettingsInfo xupSettingsInfo = new XUPSettingsInfo();
            xupSettingsInfo.isXUPSticky = connectedDevice.getStickyTWSOrPartyUpFlag();
            xupSettingsInfo.isXUPDiscoverable = connectedDevice.isXUPPromiscuousModelOn();
            return xupSettingsInfo;
        }
    }
    
    public class PartyUPSettingsAdapter extends BaseAdapter
    {
        LayoutInflater inflater;
        
        public PartyUPSettingsAdapter() {
            this.inflater = (LayoutInflater)XUPSettingsFragment.this.getActivity().getSystemService("layout_inflater");
        }
        
        public int getCount() {
            return XUPSettingsFragment.this.rowStrings.size();
        }
        
        public Object getItem(final int index) {
            if (this.isEnabled(index)) {
                switch (XUPSettingsFragment.this.rowStrings.get(index)) {
                    case 2131165394: {
                        if (XUPSettingsFragment.this.mSettingsInfo.isXUPSticky) {
                            return XUPSettingsFragment.this.getString(2131165468);
                        }
                        return XUPSettingsFragment.this.getString(2131165467);
                    }
                    case 2131165388: {
                        if (XUPSettingsFragment.this.mSettingsInfo.isXUPDiscoverable) {
                            return XUPSettingsFragment.this.getString(2131165460);
                        }
                        return XUPSettingsFragment.this.getString(2131165459);
                    }
                }
            }
            return "";
        }
        
        public long getItemId(final int n) {
            return 0L;
        }
        
        public View getView(final int index, final View view, final ViewGroup viewGroup) {
            View inflate = view;
            if (view == null) {
                inflate = this.inflater.inflate(2130968652, viewGroup, false);
            }
            final Object item = this.getItem(index);
            inflate.setEnabled(this.isEnabled(index));
            ((TextView)inflate.findViewById(16908308)).setText((int)XUPSettingsFragment.this.rowStrings.get(index));
            ((TextView)inflate.findViewById(16908309)).setText((CharSequence)item.toString());
            return inflate;
        }
        
        public boolean isEnabled(final int n) {
            return XUPSettingsFragment.this.mSettingsInfo != null;
        }
    }
}
