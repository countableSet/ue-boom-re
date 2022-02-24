// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import com.logitech.ue.other.DeviceInfo;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.UserPreferences;
import com.logitech.ue.UEColourHelper;
import com.logitech.ue.centurion.connection.UEConnectionType;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.tasks.SafeTask;
import android.widget.ListAdapter;
import java.util.List;
import android.content.Context;
import android.widget.SimpleAdapter;
import butterknife.ButterKnife;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import butterknife.OnItemClick;
import android.view.View;
import com.logitech.ue.App;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Collection;
import com.logitech.ue.Utils;
import android.os.Bundle;
import com.logitech.ue.centurion.device.devicedata.UEDeviceType;
import butterknife.Bind;
import android.widget.ListView;
import android.widget.BaseAdapter;
import java.util.Map;
import java.util.ArrayList;

public class HowToMenuFragment extends NavigatableFragment
{
    public static final String PARAM_DEVICE_TYPE = "device_type";
    public static final String PARAM_IS_BLOCK_PARTY_SUPPORTED = "block_is_party_supported";
    public static final String PARAM_IS_XUP_SUPPORTED = "block_is_xup_supported";
    private static final String TAG = "HowToMenuFragment";
    final String ATTRIBUTE_NAME_TEXT;
    ArrayList<Map<String, Object>> mAdapterData;
    BaseAdapter mListAdapter;
    @Bind({ 16908298 })
    ListView mListView;
    ArrayList<Integer> rowStrings;
    ArrayList<Integer> rowUrls;
    
    public HowToMenuFragment() {
        this.ATTRIBUTE_NAME_TEXT = "text";
        this.mAdapterData = new ArrayList<Map<String, Object>>();
        this.rowStrings = new ArrayList<Integer>();
        this.rowUrls = new ArrayList<Integer>();
    }
    
    public static Bundle buildParamBundle(final UEDeviceType ueDeviceType, final boolean b, final boolean b2) {
        final Bundle bundle = new Bundle();
        bundle.putString("device_type", ueDeviceType.getDeviceTypeName());
        bundle.putBoolean("block_is_party_supported", b);
        bundle.putBoolean("block_is_xup_supported", b2);
        return bundle;
    }
    
    private void removeRow(int index) {
        index = this.rowStrings.indexOf(index);
        if (index != -1) {
            this.rowStrings.remove(index);
            this.rowUrls.remove(index);
        }
    }
    
    private void updateMenuItems(final UEDeviceType ueDeviceType, final boolean b, final boolean b2) {
        this.rowStrings.clear();
        this.rowUrls.clear();
        if (ueDeviceType == UEDeviceType.Kora) {
            this.rowStrings.addAll(Utils.typedArrayToArrayList(this.getContext(), 2131230722));
            this.rowUrls.addAll(Utils.typedArrayToArrayList(this.getContext(), 2131230725));
        }
        else if (ueDeviceType == UEDeviceType.Maximus) {
            this.rowStrings.addAll(Utils.typedArrayToArrayList(this.getContext(), 2131230723));
            this.rowUrls.addAll(Utils.typedArrayToArrayList(this.getContext(), 2131230724));
        }
        else if (ueDeviceType == UEDeviceType.Titus) {
            this.rowStrings.addAll(Utils.typedArrayToArrayList(this.getContext(), 2131230726));
            this.rowUrls.addAll(Utils.typedArrayToArrayList(this.getContext(), 2131230727));
        }
        else if (ueDeviceType == UEDeviceType.Caribe) {
            this.rowStrings.addAll(Utils.typedArrayToArrayList(this.getContext(), 2131230728));
            this.rowUrls.addAll(Utils.typedArrayToArrayList(this.getContext(), 2131230729));
        }
        if (!b) {
            this.removeRow(2131165292);
        }
        int n;
        if (b2) {
            n = 2131165294;
        }
        else {
            n = 2131165297;
        }
        this.removeRow(n);
        this.mAdapterData.clear();
        for (final int intValue : this.rowStrings) {
            final HashMap<String, Object> e = new HashMap<String, Object>();
            e.put("text", this.getString(intValue));
            this.mAdapterData.add(e);
        }
        this.mListAdapter.notifyDataSetChanged();
    }
    
    void beginUpdate() {
        ((SafeTask<Void, Progress, Result>)new UpdateTask()).executeOnThreadPoolExecutor(new Void[0]);
    }
    
    @Override
    public String getTitle() {
        return App.getInstance().getString(2131165291);
    }
    
    @OnItemClick({ 16908298 })
    void itemClicked(final View view, final int index) {
        final Bundle bundle = new Bundle();
        bundle.putString("title", this.getString(this.rowStrings.get(index)).toUpperCase());
        bundle.putString("html_path", this.getString(this.rowUrls.get(index)));
        if (this.rowUrls.get(index) == 2131165310 || this.rowUrls.get(index) == 2131165326 || this.rowUrls.get(index) == 2131165330) {
            this.getNavigator().gotoFragment(GestureTipFragment.class, bundle);
        }
        else {
            this.getNavigator().gotoFragment(HelpTipFragment.class, bundle);
        }
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(2130968651, viewGroup, false);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        this.beginUpdate();
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        this.mListAdapter = (BaseAdapter)new SimpleAdapter((Context)this.getActivity(), (List)this.mAdapterData, 2130968652, new String[] { "text" }, new int[] { 16908308 });
        this.mListView.setAdapter((ListAdapter)this.mListAdapter);
        this.updateMenuItems(UEDeviceType.getDeviceTypeByName(this.getArguments().getString("device_type", Utils.getAppDefaultDeviceType().getDeviceTypeName())), this.getArguments().getBoolean("block_is_party_supported", true), this.getArguments().getBoolean("block_is_xup_supported", true));
    }
    
    public class UpdateTask extends SafeTask<Void, Void, Object[]>
    {
        @Override
        public String getTag() {
            return "HowToMenuFragment";
        }
        
        @Override
        public void onSuccess(final Object[] array) {
            super.onSuccess(array);
            if (HowToMenuFragment.this.getView() != null) {
                HowToMenuFragment.this.updateMenuItems((UEDeviceType)array[0], (boolean)array[1], (boolean)array[2]);
            }
        }
        
        @Override
        public Object[] work(final Void... array) throws Exception {
            final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
            Object[] array2;
            if (connectedDevice != null && connectedDevice.getConnectionType() == UEConnectionType.SPP) {
                array2 = new Object[] { UEColourHelper.getDeviceTypeByColour(connectedDevice.getDeviceColor()), connectedDevice.isBlockPartySupported(), connectedDevice.isBroadcastModeSupported() };
            }
            else {
                final DeviceInfo lastSeenDevice = UserPreferences.getInstance().getLastSeenDevice();
                if (lastSeenDevice != null) {
                    array2 = new Object[] { UEColourHelper.getDeviceTypeByColour(lastSeenDevice.color), lastSeenDevice.isBlockPartySupported, lastSeenDevice.isXupSupported };
                }
                else {
                    array2 = new Object[] { Utils.getAppDefaultDeviceType(), true, true };
                }
            }
            return array2;
        }
    }
}
