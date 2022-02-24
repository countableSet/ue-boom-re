// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import com.logitech.ue.tasks.SafeTask;
import android.widget.TextView;
import android.widget.BaseAdapter;
import com.logitech.ue.centurion.device.UEGenericDevice;
import android.widget.ListAdapter;
import android.graphics.drawable.Drawable;
import butterknife.ButterKnife;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import butterknife.OnItemClick;
import com.logitech.ue.tasks.SetDeviceGestureTask;
import com.logitech.ue.tasks.SetDeviceBLEStateTask;
import android.content.DialogInterface$OnDismissListener;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.UserPreferences;
import android.os.AsyncTask;
import com.logitech.ue.tasks.SetDeviceStickyTask;
import android.net.Uri;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import com.logitech.ue.tasks.SetDeviceCustomSonificationTask;
import com.logitech.ue.tasks.SetDeviceSonificationTask;
import com.logitech.ue.centurion.device.devicedata.UESonificationProfile;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.Menu;
import android.os.Bundle;
import com.logitech.ue.tasks.GetDeviceNameViaBtleTask;
import com.logitech.ue.centurion.device.devicedata.UEDeviceType;
import com.logitech.ue.UEColourHelper;
import com.logitech.ue.App;
import android.os.AsyncTask$Status;
import android.content.Intent;
import android.content.Context;
import java.util.Collection;
import java.util.Arrays;
import com.logitech.ue.devicedata.DeviceSettingsInfo;
import android.widget.ListView;
import com.logitech.ue.tasks.GetDeviceSettingInfoTask;
import android.content.BroadcastReceiver;
import java.util.ArrayList;

public class SettingsFragment extends NavigatableFragment
{
    private final String TAG;
    private ArrayList<Integer> defaultRowStrings;
    private SettingsAdapter mAdapter;
    BroadcastReceiver mBroadcastReceiver;
    private GetDeviceSettingInfoTask mGetInfoTask;
    private ListView mListView;
    private DeviceSettingsInfo mSettingsInfo;
    private ArrayList<Integer> rowStrings;
    
    public SettingsFragment() {
        this.TAG = this.getClass().getSimpleName();
        this.defaultRowStrings = new ArrayList<Integer>(Arrays.asList(2131165393, 0, 2131165383, 0, 2131165384, 0, 2131165391, 0, 2131165385, 0, 2131165386));
        this.rowStrings = new ArrayList<Integer>(this.defaultRowStrings);
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getAction().equals("com.logitech.ue.centurion.CONNECTION_CHANGED")) {
                    SettingsFragment.this.update();
                }
            }
        };
    }
    
    protected void beginSettingsUpdate() {
        if (this.mGetInfoTask == null || this.mGetInfoTask.getStatus() == AsyncTask$Status.FINISHED) {
            ((SafeTask<Void, Progress, Result>)(this.mGetInfoTask = new GetDeviceSettingInfoTask() {
                @Override
                public void onError(final Exception ex) {
                    super.onError(ex);
                    App.getInstance().processNuclearException(ex);
                }
                
                public void onSuccess(final DeviceSettingsInfo deviceSettingsInfo) {
                    super.onSuccess((T)deviceSettingsInfo);
                    SettingsFragment.this.mSettingsInfo = deviceSettingsInfo;
                    SettingsFragment.this.rowStrings = (ArrayList<Integer>)new ArrayList(SettingsFragment.this.defaultRowStrings);
                    if (SettingsFragment.this.mSettingsInfo.customSonification != null) {
                        final int index = SettingsFragment.this.rowStrings.indexOf(2131165384);
                        SettingsFragment.this.rowStrings.add(index, 2131165387);
                        SettingsFragment.this.rowStrings.add(index + 1, 0);
                    }
                    if (SettingsFragment.this.mSettingsInfo.isXUPSupported) {
                        final int index2 = SettingsFragment.this.rowStrings.indexOf(2131165385);
                        SettingsFragment.this.rowStrings.add(index2, 2131165392);
                        SettingsFragment.this.rowStrings.add(index2 + 1, 0);
                    }
                    else {
                        final int index3 = SettingsFragment.this.rowStrings.indexOf(2131165391);
                        SettingsFragment.this.rowStrings.add(index3, 2131165389);
                        SettingsFragment.this.rowStrings.add(index3 + 1, 0);
                    }
                    if ("orpheum".equals("shoreline") || ("orpheum".equals("orpheum") && UEColourHelper.getDeviceTypeByColour(deviceSettingsInfo.color) == UEDeviceType.Maximus)) {
                        final int index4 = SettingsFragment.this.rowStrings.indexOf(2131165391);
                        SettingsFragment.this.rowStrings.add(index4, 2131165390);
                        SettingsFragment.this.rowStrings.add(index4 + 1, 0);
                    }
                    SettingsFragment.this.mAdapter.notifyDataSetChanged();
                }
            })).executeOnThreadPoolExecutor(new Void[0]);
        }
    }
    
    protected void beginSettingsUpdateViaBtle() {
        ((SafeTask<Void, Progress, Result>)new GetDeviceNameViaBtleTask() {
            @Override
            public void onError(final Exception ex) {
                super.onError(ex);
                App.getInstance().processNuclearException(ex);
            }
            
            public void onSuccess(final String name) {
                super.onSuccess((T)name);
                SettingsFragment.this.mSettingsInfo = new DeviceSettingsInfo();
                SettingsFragment.this.mSettingsInfo.name = name;
                SettingsFragment.this.mAdapter.notifyDataSetChanged();
            }
        }).executeOnThreadPoolExecutor(new Void[0]);
    }
    
    @Override
    public String getTitle() {
        return App.getInstance().getString(2131165395);
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setHasOptionsMenu(true);
    }
    
    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        this.getActivity().setTitle((CharSequence)this.getString(2131165395));
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return (View)(this.mListView = (ListView)layoutInflater.inflate(2130968651, viewGroup, false));
    }
    
    @OnItemClick({ 16908298 })
    public void onItemClicked(final int index) {
        final boolean b = true;
        final boolean b2 = true;
        final boolean b3 = true;
        boolean b4 = true;
        switch (this.rowStrings.get(index)) {
            case 2131165393: {
                if (App.getDeviceConnectionState() != UEDeviceStatus.CONNECTED_OFF) {
                    final Bundle bundle = new Bundle();
                    bundle.putString("initial_name", this.mSettingsInfo.name);
                    this.getNavigator().gotoFragment(NameSelectorFragment.class, bundle);
                    break;
                }
                break;
            }
            case 2131165383: {
                final Bundle bundle2 = new Bundle();
                bundle2.putInt("language", this.mSettingsInfo.language.getCode());
                bundle2.putIntegerArrayList("supported_languages", (ArrayList)this.mSettingsInfo.supportedLanguages);
                bundle2.putByteArray("partition_info", this.mSettingsInfo.partitionInfo);
                this.getNavigator().gotoFragment(LanguageSelectorFragment.class, bundle2);
                break;
            }
            case 2131165384: {
                final DeviceSettingsInfo mSettingsInfo = this.mSettingsInfo;
                UESonificationProfile sonificationProfile;
                if (this.mSettingsInfo.sonificationProfile != UESonificationProfile.NONE) {
                    sonificationProfile = UESonificationProfile.NONE;
                }
                else {
                    sonificationProfile = UESonificationProfile.getProfile(App.getInstance().getConnectedDeviceType());
                }
                mSettingsInfo.sonificationProfile = sonificationProfile;
                ((SafeTask<Void, Progress, Result>)new SetDeviceSonificationTask(this.mSettingsInfo.sonificationProfile)).executeOnThreadPoolExecutor(new Void[0]);
                this.mAdapter.notifyDataSetChanged();
                break;
            }
            case 2131165387: {
                this.mSettingsInfo.customSonification = !this.mSettingsInfo.customSonification;
                new SetDeviceCustomSonificationTask(this.mSettingsInfo.customSonification).execute((Object[])new Void[0]);
                this.mAdapter.notifyDataSetChanged();
                break;
            }
            case 2131165389: {
                if (this.mSettingsInfo.twsLockFlag == null) {
                    App.getInstance().showAlertDialog(this.getString(2131165258), 2130837749, 2131165285, 2131165341, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            if (n == -1) {
                                SettingsFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(SettingsFragment.this.getString(2131165490))));
                            }
                        }
                    });
                    break;
                }
                final DeviceSettingsInfo mSettingsInfo2 = this.mSettingsInfo;
                if (this.mSettingsInfo.twsLockFlag) {
                    b4 = false;
                }
                mSettingsInfo2.twsLockFlag = b4;
                new SetDeviceStickyTask(this.mSettingsInfo.twsLockFlag).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
                this.mAdapter.notifyDataSetChanged();
                break;
            }
            case 2131165392: {
                this.getNavigator().gotoFragment(XUPSettingsFragment.class, null);
                break;
            }
            case 2131165391: {
                UserPreferences.getInstance().setNotificationsEnable(!UserPreferences.getInstance().isNotificationsEnable() && b);
                this.mAdapter.notifyDataSetChanged();
                break;
            }
            case 2131165385: {
                if (!UEDeviceManager.getInstance().isBleSupported()) {
                    App.getInstance().showMessageDialog(this.getString(2131165400), null);
                    break;
                }
                if (this.mSettingsInfo.bleState == null) {
                    App.getInstance().showAlertDialog(this.getString(2131165258), 2130837749, 2131165285, 2131165341, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            if (n == -1) {
                                SettingsFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(SettingsFragment.this.getString(2131165490))));
                            }
                        }
                    });
                    break;
                }
                this.mSettingsInfo.bleState = (!this.mSettingsInfo.bleState && b2);
                ((SafeTask<Void, Progress, Result>)new SetDeviceBLEStateTask(this.mSettingsInfo.bleState)).executeOnThreadPoolExecutor(new Void[0]);
                UserPreferences.getInstance().setBleSetting(this.mSettingsInfo.bleState);
                this.mAdapter.notifyDataSetChanged();
                break;
            }
            case 2131165390: {
                if (this.mSettingsInfo.isGestureEnabled == null) {
                    App.getInstance().showMessageDialog(this.getString(2131165257), null);
                    break;
                }
                this.mSettingsInfo.isGestureEnabled = (!this.mSettingsInfo.isGestureEnabled && b3);
                ((SafeTask<Void, Progress, Result>)new SetDeviceGestureTask(this.mSettingsInfo.isGestureEnabled)).executeOnThreadPoolExecutor(new Void[0]);
                this.mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
    
    @Override
    public void onStart() {
        super.onStart();
        this.update();
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
        ButterKnife.bind(this, view);
        this.mAdapter = new SettingsAdapter();
        this.mListView.setDivider((Drawable)null);
        this.mListView.setAdapter((ListAdapter)this.mAdapter);
    }
    
    public void showDisabledSetting() {
        this.rowStrings = new ArrayList<Integer>(this.defaultRowStrings);
        this.mAdapter.notifyDataSetChanged();
    }
    
    public void update() {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        if (connectedDevice != null) {
            if (connectedDevice.getDeviceConnectionStatus().isBtClassicConnectedState()) {
                this.beginSettingsUpdate();
            }
            else if (connectedDevice.getDeviceConnectionStatus() == UEDeviceStatus.CONNECTED_OFF) {
                this.beginSettingsUpdateViaBtle();
            }
            else {
                this.mSettingsInfo = null;
                this.showDisabledSetting();
            }
        }
        else {
            this.mSettingsInfo = null;
            this.showDisabledSetting();
        }
    }
    
    public class SettingsAdapter extends BaseAdapter
    {
        public static final int ROW_TYPE_DIVIDER = 0;
        public static final int ROW_TYPE_HINT = 2;
        public static final int ROW_TYPE_NORMAL = 1;
        LayoutInflater inflater;
        
        public SettingsAdapter() {
            this.inflater = (LayoutInflater)SettingsFragment.this.getActivity().getSystemService("layout_inflater");
        }
        
        public int getCount() {
            return SettingsFragment.this.rowStrings.size();
        }
        
        public Object getItem(final int n) {
            String s = null;
            if (this.isEnabled(n)) {
                switch (SettingsFragment.this.rowStrings.get(n)) {
                    case 2131165393: {
                        s = SettingsFragment.this.mSettingsInfo.name;
                        return s;
                    }
                    case 2131165383: {
                        s = App.getInstance().getLanguageName(SettingsFragment.this.mSettingsInfo.language);
                        return s;
                    }
                    case 2131165384: {
                        if (SettingsFragment.this.mSettingsInfo.sonificationProfile != UESonificationProfile.NONE) {
                            s = SettingsFragment.this.getString(2131165406).toUpperCase();
                            return s;
                        }
                        s = SettingsFragment.this.getString(2131165405).toUpperCase();
                        return s;
                    }
                    case 2131165387: {
                        if (SettingsFragment.this.mSettingsInfo.customSonification == null) {
                            s = SettingsFragment.this.getString(2131165241).toUpperCase();
                            return s;
                        }
                        if (SettingsFragment.this.mSettingsInfo.customSonification) {
                            s = SettingsFragment.this.getString(2131165242).toUpperCase();
                            return s;
                        }
                        s = SettingsFragment.this.getString(2131165241).toUpperCase();
                        return s;
                    }
                    case 2131165389: {
                        if (SettingsFragment.this.mSettingsInfo.twsLockFlag == null) {
                            s = SettingsFragment.this.getString(2131165250).toUpperCase();
                            return s;
                        }
                        if (SettingsFragment.this.mSettingsInfo.twsLockFlag) {
                            s = SettingsFragment.this.getString(2131165251).toUpperCase();
                            return s;
                        }
                        s = SettingsFragment.this.getString(2131165250).toUpperCase();
                        return s;
                    }
                    case 2131165392: {
                        s = "";
                        return s;
                    }
                    case 2131165390: {
                        if (SettingsFragment.this.mSettingsInfo.isGestureEnabled == null) {
                            s = SettingsFragment.this.getString(2131165281).toUpperCase();
                            return s;
                        }
                        if (SettingsFragment.this.mSettingsInfo.isGestureEnabled) {
                            s = SettingsFragment.this.getString(2131165282).toUpperCase();
                            return s;
                        }
                        s = SettingsFragment.this.getString(2131165281).toUpperCase();
                        return s;
                    }
                    case 2131165385: {
                        if (!UEDeviceManager.getInstance().isBleSupported() || SettingsFragment.this.mSettingsInfo.bleState == null) {
                            s = SettingsFragment.this.getString(2131165401).toUpperCase();
                            return s;
                        }
                        if (SettingsFragment.this.mSettingsInfo.bleState) {
                            s = SettingsFragment.this.getString(2131165402).toUpperCase();
                            return s;
                        }
                        s = SettingsFragment.this.getString(2131165401).toUpperCase();
                        return s;
                    }
                }
            }
            switch (SettingsFragment.this.rowStrings.get(n)) {
                default: {
                    s = "";
                    break;
                }
                case 2131165391: {
                    if (UserPreferences.getInstance().isNotificationsEnable()) {
                        s = SettingsFragment.this.getString(2131165350).toUpperCase();
                        break;
                    }
                    s = SettingsFragment.this.getString(2131165349).toUpperCase();
                    break;
                }
            }
            return s;
        }
        
        public long getItemId(final int n) {
            return 0L;
        }
        
        public int getItemViewType(int index) {
            if (index % 2 == 1) {
                index = 0;
            }
            else if (SettingsFragment.this.rowStrings.get(index) == 2131165386) {
                index = 2;
            }
            else {
                index = 1;
            }
            return index;
        }
        
        public View getView(final int index, final View view, final ViewGroup viewGroup) {
            View inflate;
            if (this.getItemViewType(index) == 0) {
                if ((inflate = view) == null) {
                    inflate = new View((Context)SettingsFragment.this.getActivity());
                }
            }
            else if (this.getItemViewType(index) == 2) {
                if ((inflate = view) == null) {
                    inflate = this.inflater.inflate(2130968647, viewGroup, false);
                }
                ((TextView)inflate.findViewById(16908308)).setText((int)SettingsFragment.this.rowStrings.get(index));
            }
            else {
                inflate = view;
                if (this.getItemViewType(index) == 1) {
                    View inflate2;
                    if ((inflate2 = view) == null) {
                        inflate2 = this.inflater.inflate(2130968652, viewGroup, false);
                    }
                    final Object item = this.getItem(index);
                    inflate2.setEnabled(this.isEnabled(index));
                    ((TextView)inflate2.findViewById(16908308)).setText((int)SettingsFragment.this.rowStrings.get(index));
                    ((TextView)inflate2.findViewById(16908309)).setText((CharSequence)item.toString());
                    inflate = inflate2;
                    if (this.isEnabled(index)) {
                        if (SettingsFragment.this.rowStrings.get(index) == 2131165385 && (!UEDeviceManager.getInstance().isBleSupported() || (SettingsFragment.this.mSettingsInfo != null && SettingsFragment.this.mSettingsInfo.bleState == null))) {
                            inflate2.setEnabled(false);
                            inflate = inflate2;
                        }
                        else if (SettingsFragment.this.rowStrings.get(index) == 2131165389 && SettingsFragment.this.mSettingsInfo != null && SettingsFragment.this.mSettingsInfo.twsLockFlag == null) {
                            inflate2.setEnabled(false);
                            inflate = inflate2;
                        }
                        else {
                            inflate = inflate2;
                            if (SettingsFragment.this.rowStrings.get(index) == 2131165390) {
                                inflate = inflate2;
                                if (SettingsFragment.this.mSettingsInfo != null) {
                                    inflate = inflate2;
                                    if (SettingsFragment.this.mSettingsInfo.isGestureEnabled == null) {
                                        inflate2.setEnabled(false);
                                        inflate = inflate2;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return inflate;
        }
        
        public int getViewTypeCount() {
            return 3;
        }
        
        public boolean isEnabled(final int n) {
            final boolean b = true;
            boolean b2 = false;
            if (this.getItemViewType(n) == 0 || (SettingsFragment.this.mSettingsInfo == null && SettingsFragment.this.rowStrings.get(n) != 2131165391)) {
                b2 = false;
            }
            else {
                final boolean b3 = UEDeviceManager.getInstance().getConnectedDevice() != null && UEDeviceManager.getInstance().getConnectedDevice().getDeviceConnectionStatus().isBtClassicConnectedState();
                final boolean b4 = App.getDeviceConnectionState() == UEDeviceStatus.CONNECTED_OFF;
                switch (SettingsFragment.this.rowStrings.get(n)) {
                    default: {
                        b2 = b;
                        break;
                    }
                    case 2131165383: {
                        if (SettingsFragment.this.mSettingsInfo.language != null) {
                            b2 = b;
                            if (b3) {
                                break;
                            }
                        }
                        b2 = false;
                        break;
                    }
                    case 2131165393: {
                        if (SettingsFragment.this.mSettingsInfo.name != null) {
                            b2 = b;
                            if (b3) {
                                break;
                            }
                            b2 = b;
                            if (b4) {
                                break;
                            }
                        }
                        b2 = false;
                        break;
                    }
                    case 2131165384: {
                        if (SettingsFragment.this.mSettingsInfo.sonificationProfile != null) {
                            b2 = b;
                            if (b3) {
                                break;
                            }
                        }
                        b2 = false;
                        break;
                    }
                    case 2131165387: {
                        if (SettingsFragment.this.mSettingsInfo.customSonification != null) {
                            b2 = b;
                            if (b3) {
                                break;
                            }
                        }
                        b2 = false;
                        break;
                    }
                    case 2131165389: {
                        b2 = b3;
                        break;
                    }
                    case 2131165392: {
                        b2 = b3;
                        break;
                    }
                    case 2131165390: {
                        b2 = b3;
                        break;
                    }
                    case 2131165385: {
                        b2 = b3;
                        break;
                    }
                    case 2131165386: {
                        b2 = false;
                        break;
                    }
                }
            }
            return b2;
        }
    }
}
