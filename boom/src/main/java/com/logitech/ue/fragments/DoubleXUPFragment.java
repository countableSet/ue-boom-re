// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.Menu;
import android.os.Bundle;
import android.util.TypedValue;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.exceptions.UEMessageExecutionException;
import com.logitech.ue.tasks.SafeTask;
import com.logitech.ue.App;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastConfiguration;
import com.logitech.ue.activities.MainActivity;
import com.logitech.ue.tasks.GetDeviceBroadcastTask;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;
import android.util.Log;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
import com.logitech.ue.interfaces.IPage;
import android.support.v4.app.Fragment;

public class DoubleXUPFragment extends Fragment implements IPage
{
    private static final String TAG;
    BroadcastReceiver mBroadcastReceiver;
    Fragment mCurrentFragment;
    DoubleUpFragment mDoubleUpFragment;
    XUpFragment mXUpFragment;
    
    static {
        TAG = DoubleXUPFragment.class.getSimpleName();
    }
    
    public DoubleXUPFragment() {
        this.mDoubleUpFragment = new DoubleUpFragment();
        this.mXUpFragment = new XUpFragment();
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                Log.d(DoubleXUPFragment.TAG, "Receive broadcast " + intent.getAction());
                if (DoubleXUPFragment.this.getView() != null && intent.getAction().equals("com.logitech.ue.centurion.CONNECTION_CHANGED")) {
                    if (UEDeviceStatus.getStatus(intent.getExtras().getInt("status")).isBtClassicConnectedState()) {
                        ((SafeTask<Void, Progress, Result>)new GetDeviceBroadcastTask() {
                            @Override
                            public void onError(final Exception ex) {
                                super.onError(ex);
                                DoubleXUPFragment.this.switchFragment(DoubleXUPFragment.this.mDoubleUpFragment);
                                ((MainActivity)DoubleXUPFragment.this.getActivity()).onTitleChanged(DoubleXUPFragment.this, DoubleXUPFragment.this.getTitle());
                            }
                            
                            public void onSuccess(final UEBroadcastConfiguration ueBroadcastConfiguration) {
                                super.onSuccess((T)ueBroadcastConfiguration);
                                DoubleXUPFragment.this.switchFragment(DoubleXUPFragment.this.mXUpFragment);
                                ((MainActivity)DoubleXUPFragment.this.getActivity()).onTitleChanged(DoubleXUPFragment.this, DoubleXUPFragment.this.getTitle());
                            }
                        }).executeOnThreadPoolExecutor(new Void[0]);
                    }
                    else {
                        DoubleXUPFragment.this.switchFragment(DoubleXUPFragment.this.mDoubleUpFragment);
                    }
                }
            }
        };
    }
    
    public void beginUpdate() {
        if (App.getDeviceConnectionState().isBtClassicConnectedState()) {
            ((SafeTask<Void, Progress, Result>)new SafeTask<Void, Void, Boolean>() {
                @Override
                public String getTag() {
                    return "UpdateDoubleXUPTask";
                }
                
                @Override
                public void onError(final Exception ex) {
                    super.onError(ex);
                    if (ex instanceof UEMessageExecutionException) {
                        DoubleXUPFragment.this.switchFragment(DoubleXUPFragment.this.mXUpFragment);
                    }
                    else {
                        DoubleXUPFragment.this.switchFragment(DoubleXUPFragment.this.mDoubleUpFragment);
                    }
                }
                
                @Override
                public void onSuccess(final Boolean b) {
                    super.onSuccess(b);
                    final DoubleXUPFragment this$0 = DoubleXUPFragment.this;
                    IPage page;
                    if (b != null && b) {
                        page = DoubleXUPFragment.this.mXUpFragment;
                    }
                    else {
                        page = DoubleXUPFragment.this.mDoubleUpFragment;
                    }
                    this$0.switchFragment((Fragment)page);
                }
                
                @Override
                public Boolean work(final Void... array) throws Exception {
                    final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
                    Boolean value;
                    if (connectedDevice != null && connectedDevice.getDeviceConnectionStatus().isBtClassicConnectedState()) {
                        value = connectedDevice.isBroadcastModeSupported();
                    }
                    else {
                        value = null;
                    }
                    return value;
                }
            }).executeOnThreadPoolExecutor(new Void[0]);
        }
        else {
            this.switchFragment(this.mDoubleUpFragment);
        }
    }
    
    @Override
    public int getAccentColor() {
        final TypedValue typedValue = new TypedValue();
        this.getActivity().getTheme().resolveAttribute(17170446, typedValue, true);
        return typedValue.data;
    }
    
    @Override
    public String getTitle() {
        String title;
        if (this.mCurrentFragment != null) {
            title = ((IPage)this.mCurrentFragment).getTitle();
        }
        else {
            title = null;
        }
        return title;
    }
    
    @Override
    public void onActivityCreated(final Bundle bundle) {
        super.onActivityCreated(bundle);
        this.setHasOptionsMenu(true);
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
    }
    
    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        if (this.mCurrentFragment != null) {
            this.mCurrentFragment.onCreateOptionsMenu(menu, menuInflater);
        }
        super.onCreateOptionsMenu(menu, menuInflater);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(2130968615, viewGroup, false);
    }
    
    @Override
    public void onDeselected() {
        if (this.mCurrentFragment != null) {
            ((IPage)this.mCurrentFragment).onDeselected();
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        boolean b;
        if (this.mCurrentFragment != null) {
            b = this.mCurrentFragment.onOptionsItemSelected(menuItem);
        }
        else {
            b = super.onOptionsItemSelected(menuItem);
        }
        return b;
    }
    
    @Override
    public void onSelected() {
        if (this.mCurrentFragment != null) {
            ((IPage)this.mCurrentFragment).onSelected();
        }
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
    public void onTransition(final float n) {
        if (this.mCurrentFragment != null) {
            ((IPage)this.mCurrentFragment).onTransition(n);
        }
    }
    
    void switchFragment(final Fragment mCurrentFragment) {
        if (this.mCurrentFragment != mCurrentFragment) {
            this.mCurrentFragment = mCurrentFragment;
            this.getChildFragmentManager().beginTransaction().replace(2131624045, mCurrentFragment).commitAllowingStateLoss();
        }
    }
}
