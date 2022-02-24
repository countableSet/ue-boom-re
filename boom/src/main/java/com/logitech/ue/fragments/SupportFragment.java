// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.exceptions.UEException;
import android.content.Intent;
import android.net.Uri;
import com.logitech.ue.centurion.UEDeviceManager;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;
import android.widget.ListAdapter;
import java.util.List;
import android.content.Context;
import android.widget.SimpleAdapter;
import java.util.HashMap;
import java.util.ArrayList;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.logitech.ue.App;
import android.widget.ListView;

public class SupportFragment extends NavigatableFragment
{
    final String ATTRIBUTE_NAME_TEXT;
    ListView mListView;
    int[] rowStrings;
    
    public SupportFragment() {
        this.ATTRIBUTE_NAME_TEXT = "text";
        this.rowStrings = new int[] { 2131165422, 2131165421, 2131165424, 2131165423 };
    }
    
    @Override
    public String getTitle() {
        return App.getInstance().getString(2131165420);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return (View)(this.mListView = (ListView)layoutInflater.inflate(2130968651, viewGroup, false));
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < this.rowStrings.length; ++i) {
            final HashMap<String, String> e = new HashMap<String, String>();
            e.put("text", this.getString(this.rowStrings[i]));
            list.add(e);
        }
        this.mListView.setAdapter((ListAdapter)new SimpleAdapter((Context)this.getActivity(), (List)list, 2130968652, new String[] { "text" }, new int[] { 16908308 }));
        this.mListView.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, View view, final int n, final long n2) {
                final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
                Object deviceType;
                view = (View)(deviceType = null);
                while (true) {
                    if (connectedDevice == null) {
                        break Label_0036;
                    }
                    deviceType = view;
                    try {
                        if (connectedDevice.getDeviceConnectionStatus().isBtClassicConnectedState()) {
                            deviceType = connectedDevice.getDeviceType();
                        }
                        switch (SupportFragment.this.rowStrings[n]) {
                            case 2131165422: {
                                String s = null;
                                if (deviceType != null) {
                                    switch (deviceType) {
                                        default: {
                                            s = SupportFragment.this.getString(2131165489);
                                            break;
                                        }
                                        case Titus: {
                                            s = SupportFragment.this.getString(2131165487);
                                            break;
                                        }
                                        case Kora: {
                                            s = SupportFragment.this.getString(2131165486);
                                            break;
                                        }
                                        case Caribe: {
                                            s = SupportFragment.this.getString(2131165488);
                                            break;
                                        }
                                        case Maximus: {
                                            s = SupportFragment.this.getString(2131165485);
                                            break;
                                        }
                                    }
                                }
                                else {
                                    s = SupportFragment.this.getString(2131165489);
                                }
                                SupportFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(s)));
                                break;
                            }
                            case 2131165421: {
                                SupportFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(SupportFragment.this.getString(2131165479))));
                                break;
                            }
                            case 2131165424: {
                                String s2 = null;
                                if (deviceType != null) {
                                    switch (deviceType) {
                                        default: {
                                            s2 = SupportFragment.this.getString(2131165476);
                                            break;
                                        }
                                        case Titus: {
                                            s2 = SupportFragment.this.getString(2131165474);
                                            break;
                                        }
                                        case Kora: {
                                            s2 = SupportFragment.this.getString(2131165473);
                                            break;
                                        }
                                        case Caribe: {
                                            s2 = SupportFragment.this.getString(2131165475);
                                            break;
                                        }
                                        case Maximus: {
                                            s2 = SupportFragment.this.getString(2131165472);
                                            break;
                                        }
                                    }
                                }
                                else {
                                    s2 = SupportFragment.this.getString(2131165476);
                                }
                                SupportFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(s2)));
                                break;
                            }
                            case 2131165423: {
                                SupportFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(SupportFragment.this.getString(2131165493))));
                                break;
                            }
                        }
                    }
                    catch (UEException ex) {
                        deviceType = view;
                        continue;
                    }
                    break;
                }
            }
        });
    }
}
