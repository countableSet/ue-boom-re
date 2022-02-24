// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import com.logitech.ue.tasks.SafeTask;
import com.logitech.ue.tasks.SetLanguageTask;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer$OnErrorListener;
import android.media.MediaPlayer$OnCompletionListener;
import com.logitech.ue.other.LanguagePreviewHelper;
import java.util.Iterator;
import android.widget.ListAdapter;
import android.graphics.Typeface;
import android.widget.TextView;
import java.util.Collection;
import java.util.Collections;
import android.os.AsyncTask$Status;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import android.widget.AdapterView;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.net.Uri;
import android.content.DialogInterface$OnClickListener;
import com.logitech.ue.centurion.exceptions.UEException;
import android.util.Log;
import android.os.Handler;
import android.content.DialogInterface;
import android.content.DialogInterface$OnDismissListener;
import com.logitech.ue.UEColourHelper;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.App;
import com.logitech.ue.centurion.device.devicedata.UEDeviceType;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;
import android.content.Intent;
import android.content.Context;
import com.logitech.ue.tasks.WriteLanguageToDeviceTask;
import java.util.ArrayList;
import android.media.MediaPlayer;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.device.devicedata.UELanguage;
import android.content.BroadcastReceiver;
import android.widget.AdapterView$OnItemClickListener;

public class LanguageSelectorFragment extends NavigatableFragment implements AdapterView$OnItemClickListener
{
    public static final int LANGUAGE_HIDE_DIALOG_DELAY = 3000;
    private static final int LANGUAGE_OTA_REBOOT_WAITING = 10000;
    public static final String PARAM_DEVICE_LANGUAGE = "language";
    public static final String PARAM_DEVICE_PARTITION_INFO_LANGUAGES = "partition_info";
    public static final String PARAM_DEVICE_SUPPORTED_LANGUAGES = "supported_languages";
    private static final String TAG;
    BroadcastReceiver mBroadcastReceiver;
    private UELanguage mCurrentLanguage;
    UEGenericDevice mDevice;
    private UELanguage[] mLanguages;
    private ListView mList;
    private ArrayAdapter<String> mListAdapter;
    private MediaPlayer mMediaPlayer;
    private UELanguage mNewLanguage;
    private byte[] mPartitionInfo;
    private ArrayList<Integer> mSupportedLanguageIndex;
    private String[] mValues;
    private WriteLanguageToDeviceTask mWriteLanguageToDeviceTask;
    
    static {
        TAG = LanguageSelectorFragment.class.getSimpleName();
    }
    
    public LanguageSelectorFragment() {
        this.mPartitionInfo = null;
        this.mWriteLanguageToDeviceTask = null;
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getAction().equals("com.logitech.ue.centurion.CONNECTION_CHANGED")) {
                    UEDeviceStatus ueDeviceStatus;
                    if (intent.getExtras() != null) {
                        ueDeviceStatus = UEDeviceStatus.getStatus(intent.getExtras().getInt("status"));
                    }
                    else {
                        ueDeviceStatus = UEDeviceStatus.DISCONNECTED;
                    }
                    if (LanguageSelectorFragment.this.mWriteLanguageToDeviceTask == null && !ueDeviceStatus.isBtClassicConnectedState() && LanguageSelectorFragment.this.getActivity() != null) {
                        LanguageSelectorFragment.this.getActivity().finish();
                    }
                }
            }
        };
    }
    
    private boolean shouldWriteNewLanguage(final UELanguage ueLanguage, final ArrayList<Integer> list, final UEDeviceType ueDeviceType) {
        return ueDeviceType == UEDeviceType.Kora && list != null && !list.contains(ueLanguage.getCode());
    }
    
    @Override
    public String getTitle() {
        return App.getInstance().getString(2131165339);
    }
    
    public void onAttach(final Context context) {
        super.onAttach(context);
        this.mDevice = UEDeviceManager.getInstance().getConnectedDevice();
        (this.mMediaPlayer = new MediaPlayer()).setAudioStreamType(3);
    }
    
    @Override
    public boolean onBack() {
        boolean b = true;
        if (this.mNewLanguage == null) {
            return b;
        }
        while (true) {
            Label_0130: {
                try {
                    if (!this.shouldWriteNewLanguage(this.mNewLanguage, this.mSupportedLanguageIndex, UEColourHelper.getDeviceTypeByColour(this.mDevice.getDeviceColor()))) {
                        break Label_0130;
                    }
                    if (this.mDevice.isPartitionValidForLanguage(this.mPartitionInfo)) {
                        ((SafeTask<UELanguage, Progress, Result>)(this.mWriteLanguageToDeviceTask = new WriteLanguageToDeviceTask() {
                            @Override
                            public void onError(final Exception ex) {
                                super.onError(ex);
                                App.getInstance().dismissProgressDialog();
                                App.getInstance().showMessageDialog(LanguageSelectorFragment.this.getString(2131165456), (DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener() {
                                    public void onDismiss(final DialogInterface dialogInterface) {
                                        if (LanguageSelectorFragment.this.getNavigator() != null && LanguageSelectorFragment.this.getNavigator() == App.getInstance().getCurrentActivity()) {
                                            LanguageSelectorFragment.this.getNavigator().goBack();
                                        }
                                    }
                                });
                            }
                            
                            @Override
                            protected void onPostExecute(final Object o) {
                                super.onPostExecute(o);
                                if (LanguageSelectorFragment.this.getActivity() != null) {
                                    LanguageSelectorFragment.this.getActivity().getWindow().clearFlags(128);
                                }
                            }
                            
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                LanguageSelectorFragment.this.getActivity().getWindow().addFlags(128);
                                App.getInstance().showProgressDialog(LanguageSelectorFragment.this.getResources().getString(2131165457));
                            }
                            
                            @Override
                            public void onSuccess(final Void void1) {
                                super.onSuccess(void1);
                                final Handler handler = new Handler();
                                Log.i(LanguageSelectorFragment$5.TAG, "Writing success. Make sure device has rebooted");
                                handler.postDelayed((Runnable)new Runnable() {
                                    @Override
                                    public void run() {
                                        LanguageSelectorFragment.this.mWriteLanguageToDeviceTask = null;
                                        App.getInstance().dismissProgressDialog();
                                        if (LanguageSelectorFragment.this.getNavigator() != null && LanguageSelectorFragment.this.getNavigator() == App.getInstance().getCurrentActivity()) {
                                            LanguageSelectorFragment.this.getNavigator().goBack();
                                        }
                                    }
                                }, 10000L);
                            }
                        })).executeOnThreadPoolExecutor(this.mNewLanguage);
                        b = false;
                        return b;
                    }
                }
                catch (UEException ex) {
                    ex.printStackTrace();
                    b = false;
                    return b;
                }
                App.getInstance().showAlertDialog(this.getResources().getString(2131165278), 2130837749, 2131165285, 2131165341, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        if (n == -1) {
                            LanguageSelectorFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(LanguageSelectorFragment.this.getString(2131165490))));
                        }
                    }
                });
                b = false;
                return b;
            }
            this.setNewLanguageToDevice(this.mNewLanguage, false);
            b = b;
            return b;
        }
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return (View)(this.mList = (ListView)layoutInflater.inflate(2130968651, viewGroup, false));
    }
    
    public void onDetach() {
        this.mMediaPlayer.release();
        super.onDetach();
    }
    
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
        while (true) {
            this.mNewLanguage = this.mLanguages[n];
            Log.d(LanguageSelectorFragment.TAG, "Language row " + n + " clicked. New value " + this.mNewLanguage.name());
            while (true) {
                try {
                    if (this.shouldWriteNewLanguage(this.mNewLanguage, this.mSupportedLanguageIndex, this.mDevice.getDeviceType())) {
                        try {
                            this.previewBatteryLevelForLanguage(this.mNewLanguage, UEDeviceManager.getInstance().getConnectedDevice().getChargingInfo().getCharge());
                            this.mList.setSelection(n);
                            this.mListAdapter.notifyDataSetChanged();
                            return;
                        }
                        catch (UEException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                catch (UEException ex2) {
                    ex2.printStackTrace();
                    continue;
                }
                this.setNewLanguageToDevice(this.mNewLanguage, true);
                continue;
            }
        }
    }
    
    public void onResume() {
        super.onResume();
        if (!this.mDevice.getDeviceConnectionStatus().isBtClassicConnectedState()) {
            this.mNewLanguage = null;
            this.getNavigator().goBack();
        }
    }
    
    @Override
    public void onStart() {
        super.onStart();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.logitech.ue.centurion.CONNECTION_CHANGED");
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(this.mBroadcastReceiver, intentFilter);
    }
    
    public void onStop() {
        App.getInstance().dismissProgressDialog();
        if (this.mWriteLanguageToDeviceTask != null && this.mWriteLanguageToDeviceTask.getStatus() != AsyncTask$Status.FINISHED) {
            this.mWriteLanguageToDeviceTask.cancel(true);
            App.getInstance().dismissProgressDialog();
            App.getInstance().showMessageDialog(this.getString(2131165456), (DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener() {
                public void onDismiss(final DialogInterface dialogInterface) {
                    if (LanguageSelectorFragment.this.getNavigator() != null && LanguageSelectorFragment.this.getNavigator() == App.getInstance().getCurrentActivity()) {
                        LanguageSelectorFragment.this.getNavigator().goBack();
                    }
                }
            });
        }
        this.mWriteLanguageToDeviceTask = null;
        LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(this.mBroadcastReceiver);
        this.getActivity().getWindow().clearFlags(128);
        super.onStop();
    }
    
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mCurrentLanguage = UELanguage.getLanguage(this.getArguments().getInt("language"));
        this.mSupportedLanguageIndex = (ArrayList<Integer>)this.getArguments().getIntegerArrayList("supported_languages");
        this.mPartitionInfo = this.getArguments().getByteArray("partition_info");
        int selection = -1;
        final ArrayList<Object> list = new ArrayList<Object>();
        if (this.mPartitionInfo != null) {
            Collections.addAll(list, UELanguage.values());
        }
        else if (this.mSupportedLanguageIndex != null && !this.mSupportedLanguageIndex.isEmpty()) {
            final Iterator<Integer> iterator = this.mSupportedLanguageIndex.iterator();
            while (iterator.hasNext()) {
                list.add(UELanguage.getLanguage(iterator.next()));
            }
        }
        else {
            Collections.addAll(list, UELanguage.values());
        }
        this.mValues = new String[list.size()];
        this.mLanguages = new UELanguage[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            this.mLanguages[i] = list.get(i);
            this.mValues[i] = App.getInstance().getLanguageName(list.get(i));
            if (this.mCurrentLanguage.equals(list.get(i))) {
                selection = i;
            }
        }
        this.mListAdapter = new ArrayAdapter<String>(this.getActivity(), 2130968649, 16908308, this.mValues) {
            public View getView(final int n, View view, final ViewGroup viewGroup) {
                view = super.getView(n, view, viewGroup);
                if (LanguageSelectorFragment.this.mList.getCheckedItemPosition() == n) {
                    view.findViewById(16908294).setVisibility(0);
                    ((TextView)view.findViewById(16908308)).setTypeface((Typeface)null, 1);
                }
                else {
                    view.findViewById(16908294).setVisibility(4);
                    ((TextView)view.findViewById(16908308)).setTypeface((Typeface)null, 0);
                }
                return view;
            }
        };
        this.mList.setChoiceMode(1);
        this.mList.setAdapter((ListAdapter)this.mListAdapter);
        this.mList.setOnItemClickListener((AdapterView$OnItemClickListener)this);
        this.mList.setItemChecked(selection, true);
        this.mList.setSelection(selection);
    }
    
    public void previewBatteryLevelForLanguage(final UELanguage ueLanguage, int fileWithLanguageAndLevel) {
        fileWithLanguageAndLevel = LanguagePreviewHelper.fileWithLanguageAndLevel(ueLanguage, fileWithLanguageAndLevel);
        final AssetFileDescriptor openRawResourceFd = this.getResources().openRawResourceFd(fileWithLanguageAndLevel);
        if (openRawResourceFd == null) {
            return;
        }
        while (true) {
            try {
                this.mMediaPlayer.reset();
                this.mMediaPlayer.setDataSource(openRawResourceFd.getFileDescriptor(), openRawResourceFd.getStartOffset(), openRawResourceFd.getLength());
                this.mMediaPlayer.prepare();
                this.mMediaPlayer.start();
                Log.d(LanguageSelectorFragment.TAG, "Playing new language " + ueLanguage.name());
                openRawResourceFd.close();
                this.mMediaPlayer.setOnCompletionListener((MediaPlayer$OnCompletionListener)new MediaPlayer$OnCompletionListener() {
                    public void onCompletion(final MediaPlayer mediaPlayer) {
                        Log.d(LanguageSelectorFragment.TAG, ueLanguage.name() + " playback ended.");
                    }
                });
                this.mMediaPlayer.setOnErrorListener((MediaPlayer$OnErrorListener)new MediaPlayer$OnErrorListener() {
                    public boolean onError(final MediaPlayer mediaPlayer, final int n, final int n2) {
                        Log.e(LanguageSelectorFragment.TAG, "Error on playing " + ueLanguage.name());
                        return false;
                    }
                });
            }
            catch (Exception ex) {
                Log.e(LanguageSelectorFragment.TAG, ex.getMessage(), (Throwable)ex);
                continue;
            }
            break;
        }
    }
    
    public void setNewLanguageToDevice(final UELanguage ueLanguage, final boolean b) {
        ((SafeTask<Void, Progress, Result>)new SetLanguageTask(ueLanguage, b)).executeOnThreadPoolExecutor(new Void[0]);
    }
}
