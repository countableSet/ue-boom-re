// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.exceptions.UEUnrecognisedCommandException;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.firmware.FirmwareManager;
import com.logitech.ue.centurion.device.devicedata.UEColour;
import com.logitech.ue.tasks.SafeTask;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import java.util.Collection;
import java.util.Arrays;
import android.widget.ListAdapter;
import java.util.List;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import butterknife.OnItemClick;
import com.logitech.ue.UEColourHelper;
import android.app.Activity;
import com.logitech.ue.Utils;
import butterknife.ButterKnife;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.Menu;
import android.os.AsyncTask;
import android.os.AsyncTask$Status;
import android.os.Parcelable;
import com.logitech.ue.activities.UpdaterActivity;
import android.content.DialogInterface$OnDismissListener;
import android.net.Uri;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.util.Log;
import com.logitech.ue.App;
import android.content.Intent;
import android.content.Context;
import com.logitech.ue.firmware.UpdateInstruction;
import java.util.ArrayList;
import butterknife.Bind;
import android.widget.ListView;
import android.content.BroadcastReceiver;

public class MenuMainFragment extends NavigatableFragment
{
    public static final int MENU_ITEM_TYPE_REGULAR = 0;
    public static final int MENU_ITEM_TYPE_SOFTWARE_UPDATE = 1;
    public static final String TAG;
    MenuAdapter mAdapter;
    BroadcastReceiver mBroadcastReceiver;
    int mDeviceColor;
    String mFirmwareVersion;
    String mHardwareRevision;
    boolean mIsAlarmSupported;
    boolean mIsBlockPartySupported;
    boolean mIsOTASupported;
    boolean mIsUpdatable;
    boolean mIsXUPSupported;
    @Bind({ 16908298 })
    ListView mListView;
    final ArrayList<Integer> mRowStringsList;
    String mSerialNumber;
    UpdateInstruction mUpdateInstruction;
    UpdateTask mUpdateTask;
    
    static {
        TAG = MenuMainFragment.class.getSimpleName();
    }
    
    public MenuMainFragment() {
        this.mRowStringsList = new ArrayList<Integer>();
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getAction().equals("com.logitech.ue.centurion.CONNECTION_CHANGED") || intent.getAction().equals("com.logitech.ue.firmware.UPDATE_READY")) {
                    MenuMainFragment.this.beginUpdate();
                }
            }
        };
    }
    
    private void launchAlarm(final boolean b) {
        if (b) {
            if (App.getInstance().isAlarmInConflict()) {
                Log.d(MenuMainFragment.TAG, "Alarm conflict. Show \"Alarm conflict\" dialog");
                App.getInstance().showAlertDialog(this.getString(2131165212), 2131165358, 2131165470, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        if (n == -1) {
                            Log.d(MenuMainFragment.TAG, "Conflict resolved. Go to alarm");
                            MenuMainFragment.this.getNavigator().gotoFragment(AlarmMainFragment.class, null);
                        }
                        else {
                            Log.d(MenuMainFragment.TAG, "Conflict not resolved. Do nothing");
                        }
                    }
                });
            }
            else {
                Log.d(MenuMainFragment.TAG, "Go to Alarm");
                this.getNavigator().gotoFragment(AlarmMainFragment.class, null);
            }
        }
        else {
            Log.d(MenuMainFragment.TAG, "Alarm is not supported. Show \"Enable feature\" dialog");
            if (App.getDeviceConnectionState().isBtClassicConnectedState()) {
                App.getInstance().showAlertDialog(this.getString(2131165258), 2130837749, 2131165285, 2131165341, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        if (n == -1) {
                            MenuMainFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(MenuMainFragment.this.getString(2131165490))));
                        }
                    }
                });
            }
            else {
                Log.d(MenuMainFragment.TAG, "Speaker not connected. Show \"Connect speaker\" dialog");
                App.getInstance().showMessageDialog(this.getString(2131165407), null);
            }
        }
    }
    
    private void startUpdaterActivity() {
        final Intent intent = new Intent(this.getContext(), (Class)UpdaterActivity.class);
        intent.putExtra("extra_start_firmware_update", false);
        intent.putExtra("extra_device_color", this.mDeviceColor);
        intent.putExtra("extra_update_instructions", (Parcelable)this.mUpdateInstruction);
        intent.putExtra("extra_device_serial", this.mSerialNumber);
        this.startActivity(intent);
    }
    
    public void beginUpdate() {
        if (this.mUpdateTask == null || this.mUpdateTask.getStatus() == AsyncTask$Status.FINISHED) {
            (this.mUpdateTask = new UpdateTask()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, (Object[])new Void[0]);
        }
    }
    
    @Override
    public String getTitle() {
        return App.getInstance().getString(2131165352);
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setHasOptionsMenu(true);
    }
    
    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        this.getActivity().setTitle((CharSequence)this.getString(2131165348));
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2130968651, viewGroup, false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }
    
    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }
    
    @OnItemClick({ 16908298 })
    void onItemClicked(final int i) {
        final int intValue = (int)this.mAdapter.getItem(i);
        final boolean rowEnabled = this.mAdapter.isRowEnabled(i);
        final String tag = MenuMainFragment.TAG;
        String s;
        if (rowEnabled) {
            s = "enable";
        }
        else {
            s = "disable";
        }
        Log.d(tag, String.format("Item %d clicked. It is %s", i, s));
        switch (intValue) {
            case 2131165272: {
                Log.d(MenuMainFragment.TAG, "Go to EQ");
                if (rowEnabled) {
                    this.getNavigator().gotoFragment(EQMainFragment.class, null);
                    break;
                }
                Log.d(MenuMainFragment.TAG, "Speaker not connected. Show \"Connect speaker\" dialog");
                App.getInstance().showMessageDialog(this.getString(2131165407), null);
                break;
            }
            case 2131165208: {
                if (!Utils.isReadExternalStoragePermissionGranted()) {
                    Utils.checkReadExternalStoragePermission(this.getActivity(), 2);
                    break;
                }
                this.launchAlarm(rowEnabled);
                break;
            }
            case 2131165347: {
                Log.d(MenuMainFragment.TAG, "Go to Settings");
                this.getNavigator().gotoFragment(SettingsFragment.class, null);
                break;
            }
            case 2131165291: {
                Log.d(MenuMainFragment.TAG, "Go to How To");
                this.getNavigator().gotoFragment(HowToMenuFragment.class, HowToMenuFragment.buildParamBundle(UEColourHelper.getDeviceTypeByColour(this.mDeviceColor), this.mIsBlockPartySupported, this.mIsXUPSupported));
                break;
            }
            case 2131165420: {
                Log.d(MenuMainFragment.TAG, "Go to Support");
                this.getNavigator().gotoFragment(SupportFragment.class, null);
                break;
            }
            case 2131165404: {
                if (!rowEnabled) {
                    Log.d(MenuMainFragment.TAG, "Speaker not connected. Show \"Connect speaker\" dialog");
                    App.getInstance().showMessageDialog(this.getString(2131165407), null);
                    break;
                }
                if (!this.mIsUpdatable) {
                    break;
                }
                if (this.mIsOTASupported) {
                    Log.d(MenuMainFragment.TAG, "Go to Software Update");
                    this.startUpdaterActivity();
                    break;
                }
                App.getInstance().showAlertDialog(this.getResources().getString(2131165258), 2130837749, 2131165285, 2131165341, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        if (n == -1) {
                            MenuMainFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(MenuMainFragment.this.getString(2131165490))));
                        }
                    }
                });
                break;
            }
            case 2131165205: {
                Log.d(MenuMainFragment.TAG, "Go to About");
                this.getNavigator().gotoFragment(AboutFragment.class, null);
                break;
            }
        }
    }
    
    @Override
    public void onStart() {
        super.onStart();
        this.beginUpdate();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.logitech.ue.centurion.CONNECTION_CHANGED");
        intentFilter.addAction("com.logitech.ue.firmware.UPDATE_READY");
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
        this.mAdapter = new MenuAdapter((Context)this.getActivity(), 2130968648, 16908308, this.mRowStringsList);
        this.mListView.setAdapter((ListAdapter)this.mAdapter);
    }
    
    public void updateUI() {
        this.mRowStringsList.clear();
        this.mRowStringsList.addAll(Arrays.asList(2131165272, 2131165208, 2131165347, 2131165291, 2131165420));
        if (App.getDeviceConnectionState().isBtClassicConnectedState()) {
            this.mRowStringsList.add(2131165404);
        }
        this.mRowStringsList.add(2131165205);
        this.mAdapter.notifyDataSetChanged();
    }
    
    public class MenuAdapter extends ArrayAdapter<Integer>
    {
        LayoutInflater inflater;
        
        public MenuAdapter(final Context context, final int n, final int n2, final List<Integer> list) {
            super(context, n, n2, (List)list);
            this.inflater = (LayoutInflater)MenuMainFragment.this.getActivity().getSystemService("layout_inflater");
        }
        
        public boolean areAllItemsEnabled() {
            return false;
        }
        
        public int getItemViewType(int n) {
            if ((int)this.getItem(n) == 2131165404) {
                n = 1;
            }
            else {
                n = 0;
            }
            return n;
        }
        
        public View getView(final int n, View view, final ViewGroup viewGroup) {
            switch (this.getItemViewType(n)) {
                case 0: {
                    view = super.getView(n, view, viewGroup);
                    break;
                }
                case 1: {
                    view = this.inflater.inflate(2130968646, viewGroup, false);
                    break;
                }
            }
            view.setEnabled(true);
            final TextView textView = ButterKnife.findById(view, 16908308);
            final TextView textView2 = ButterKnife.findById(view, 16908309);
            textView2.setText((CharSequence)null);
            if ((int)this.getItem(n) == 2131165205) {
                textView.setText((CharSequence)MenuMainFragment.this.getString(2131165204));
            }
            else if ((int)this.getItem(n) == 2131165404) {
                textView.setText((CharSequence)UEColourHelper.getDeviceSpecificName(UEColourHelper.getDeviceTypeByColour(MenuMainFragment.this.mDeviceColor), this.getContext()));
                if (this.isRowEnabled(n)) {
                    if (MenuMainFragment.this.mIsUpdatable) {
                        textView2.setTextColor(ContextCompat.getColor((Context)MenuMainFragment.this.getActivity(), 2131558459));
                        textView2.setText((CharSequence)MenuMainFragment.this.getString(2131165445));
                    }
                    else {
                        textView2.setTextColor(ContextCompat.getColor((Context)MenuMainFragment.this.getActivity(), 2131558429));
                        textView2.setText((CharSequence)MenuMainFragment.this.getString(2131165439));
                    }
                }
            }
            else {
                textView.setText((CharSequence)MenuMainFragment.this.getString((int)this.getItem(n)));
            }
            view.setEnabled(this.isRowEnabled(n));
            return view;
        }
        
        public int getViewTypeCount() {
            return 2;
        }
        
        public boolean isRowEnabled(final int n) {
            boolean b = false;
            switch ((int)this.getItem(n)) {
                default: {
                    b = true;
                    break;
                }
                case 2131165272: {
                    b = App.getDeviceConnectionState().isBtClassicConnectedState();
                    break;
                }
                case 2131165208: {
                    b = ((App.getDeviceConnectionState().isBtClassicConnectedState() && MenuMainFragment.this.mIsAlarmSupported) || App.getDeviceConnectionState() == UEDeviceStatus.CONNECTED_OFF);
                    break;
                }
                case 2131165404: {
                    b = App.getDeviceConnectionState().isBtClassicConnectedState();
                    break;
                }
            }
            return b;
        }
    }
    
    public class UpdateTask extends SafeTask<Void, Void, Object[]>
    {
        @Override
        public String getTag() {
            return MenuMainFragment.TAG;
        }
        
        @Override
        public void onError(final Exception ex) {
            super.onError(ex);
            MenuMainFragment.this.mIsAlarmSupported = false;
            MenuMainFragment.this.mDeviceColor = UEColour.NO_SPEAKER.getCode();
            MenuMainFragment.this.mIsOTASupported = false;
            MenuMainFragment.this.mHardwareRevision = null;
            MenuMainFragment.this.mFirmwareVersion = null;
            MenuMainFragment.this.mUpdateInstruction = null;
            MenuMainFragment.this.mSerialNumber = null;
            MenuMainFragment.this.mIsXUPSupported = false;
            MenuMainFragment.this.updateUI();
        }
        
        @Override
        public void onSuccess(final Object[] array) {
            super.onSuccess(array);
            MenuMainFragment.this.mDeviceColor = (int)array[0];
            MenuMainFragment.this.mFirmwareVersion = (String)array[1];
            MenuMainFragment.this.mHardwareRevision = (String)array[2];
            MenuMainFragment.this.mIsAlarmSupported = (boolean)array[3];
            MenuMainFragment.this.mIsOTASupported = (boolean)array[4];
            MenuMainFragment.this.mSerialNumber = (String)array[5];
            MenuMainFragment.this.mIsBlockPartySupported = (boolean)array[6];
            MenuMainFragment.this.mIsXUPSupported = (boolean)array[7];
            if (MenuMainFragment.this.mIsOTASupported) {
                final FirmwareManager instance = FirmwareManager.getInstance();
                final UpdateInstruction updateInstructionFromCache = instance.getUpdateInstructionFromCache(Utils.buildUpdateInstructionPamars(MenuMainFragment.this.mDeviceColor, MenuMainFragment.this.mHardwareRevision, MenuMainFragment.this.mFirmwareVersion));
                if (updateInstructionFromCache != null && instance.isUpdateReady(updateInstructionFromCache)) {
                    MenuMainFragment.this.mIsUpdatable = true;
                    MenuMainFragment.this.mUpdateInstruction = updateInstructionFromCache;
                    MenuMainFragment.this.mAdapter.notifyDataSetChanged();
                }
                else {
                    MenuMainFragment.this.mIsUpdatable = false;
                }
            }
            else {
                MenuMainFragment.this.mIsUpdatable = true;
            }
            MenuMainFragment.this.updateUI();
        }
        
        @Override
        public Object[] work(Void... array) throws Exception {
            final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
            array = (Void[])new Object[] { connectedDevice.getDeviceColor(), connectedDevice.getFirmwareVersion(), connectedDevice.getHardwareRevision(), connectedDevice.isAlarmSupported(), connectedDevice.isOTASupported(), null, null, null };
            while (true) {
                try {
                    array[5] = (Void)connectedDevice.getSerialNumber();
                    array[6] = (Void)Boolean.valueOf(connectedDevice.isBlockPartySupported());
                    array[7] = (Void)Boolean.valueOf(connectedDevice.isBroadcastModeSupported());
                    return array;
                }
                catch (UEUnrecognisedCommandException ex) {
                    Log.d(MenuMainFragment.TAG, "Serial not supported");
                    array[5] = (Void)"";
                    continue;
                }
                break;
            }
        }
    }
}
