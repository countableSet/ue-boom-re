// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.content.res.TypedArray;
import java.util.Arrays;
import com.logitech.ue.tasks.SafeTask;
import com.logitech.ue.other.EQHelper;
import android.support.annotation.NonNull;
import com.logitech.ue.FlurryTracker;
import com.logitech.ue.tasks.SetDeviceEQTask;
import android.support.v4.view.PagerAdapter;
import butterknife.ButterKnife;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.os.Bundle;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.connection.UEConnectionType;
import com.logitech.ue.centurion.UEDeviceManager;
import android.net.Uri;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import com.logitech.ue.App;
import android.graphics.drawable.Drawable;
import android.os.Build$VERSION;
import android.graphics.PorterDuff$Mode;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.view.View;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;
import android.util.Log;
import android.content.Intent;
import android.content.Context;
import com.logitech.ue.Utils;
import android.os.Handler;
import android.widget.SeekBar$OnSeekBarChangeListener;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.support.v4.view.ViewPager;
import com.logitech.ue.other.EQUpdater;
import android.content.BroadcastReceiver;
import butterknife.Bind;
import android.widget.SeekBar;
import com.logitech.ue.centurion.device.devicedata.UEEQSetting;

public class EQMainFragment extends NavigatableFragment
{
    private static final byte[] BASSBOOST_PRESET_EQ_VALUES;
    private static final int EQ_MAX = 10;
    private static final int EQ_MIDDLE = 5;
    private static final byte[] INTIMATE_PRESET_EQ_VALUES;
    private static final byte[] STANDARD_PRESET_EQ_VALUES;
    private static final String TAG;
    private static final byte[] VOCAL_PRESET_EQ_VALUES;
    private static final UEEQSetting[] mEQSettings;
    private static final int mEQViewPagerCustomItem;
    private final long UPDATE_DELAY_MS;
    private boolean isBassBoostSupported;
    private boolean isCustomEQSupported;
    @Bind({ 2131624127 })
    SeekBar mBassLeftSeekBar;
    @Bind({ 2131624129 })
    SeekBar mBassRightSeekBar;
    BroadcastReceiver mBroadcastReceiver;
    byte[] mCurrentCustomEQValue;
    UEEQSetting mCurrentEQValue;
    EQUpdater mCustomEQUpdater;
    @Bind({ 2131624122 })
    ViewPager mEQViewPager;
    private TextView mEqLevelTextView;
    private int mEqSeekBarsHalfWidth;
    private int mEqSeekBarsHeight;
    @Bind({ 2131624131 })
    SeekBar mMidSeekBar;
    private PopupWindow mPopupWindow;
    private SeekBar$OnSeekBarChangeListener mSeekBarChangeListener;
    @Bind({ 2131624133 })
    SeekBar mTrebleLeftSeekBar;
    @Bind({ 2131624135 })
    SeekBar mTrebleRightSeekBar;
    public Handler mUpdateHandler;
    
    static {
        TAG = EQMainFragment.class.getSimpleName();
        mEQSettings = new UEEQSetting[] { UEEQSetting.CUSTOM, UEEQSetting.OUT_LOUD, UEEQSetting.BASS_BOOST, UEEQSetting.INTIMATE, UEEQSetting.VOCAL };
        mEQViewPagerCustomItem = Utils.indexOf(EQMainFragment.mEQSettings, UEEQSetting.CUSTOM);
        if ("orpheum".equals("orpheum")) {
            STANDARD_PRESET_EQ_VALUES = new byte[] { 0, 0, 0, 0, 0 };
            BASSBOOST_PRESET_EQ_VALUES = new byte[] { 0, -3, -3, -3, -3 };
            INTIMATE_PRESET_EQ_VALUES = new byte[] { 0, -3, -3, 0, 0 };
            VOCAL_PRESET_EQ_VALUES = new byte[] { -4, -3, 0, 0, 0 };
        }
        else if ("orpheum".equals("shoreline")) {
            STANDARD_PRESET_EQ_VALUES = new byte[] { 0, 0, 0, 0, 0 };
            BASSBOOST_PRESET_EQ_VALUES = new byte[] { 0, -2, -3, -3, -3 };
            INTIMATE_PRESET_EQ_VALUES = new byte[] { 0, -2, -4, -2, 0 };
            VOCAL_PRESET_EQ_VALUES = new byte[] { -5, -5, 0, 0, 0 };
        }
        else {
            STANDARD_PRESET_EQ_VALUES = new byte[] { 0, 0, 0, 0, 0 };
            BASSBOOST_PRESET_EQ_VALUES = new byte[] { 3, 2, 1, 0, -1 };
            INTIMATE_PRESET_EQ_VALUES = new byte[] { -1, -2, 1, 0, -1 };
            VOCAL_PRESET_EQ_VALUES = new byte[] { -1, -2, 1, 2, -1 };
        }
    }
    
    public EQMainFragment() {
        this.UPDATE_DELAY_MS = 500L;
        this.isBassBoostSupported = false;
        this.isCustomEQSupported = false;
        this.mCurrentCustomEQValue = new byte[] { 0, 0, 0, 0, 0 };
        this.mCustomEQUpdater = new EQUpdater();
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent obj) {
                Log.d(EQMainFragment.TAG, "Broadcast received " + obj);
                if (obj.getAction().equals("com.logitech.ue.centurion.CONNECTION_CHANGED")) {
                    final UEDeviceStatus status = UEDeviceStatus.getStatus(obj.getIntExtra("status", UEDeviceStatus.getValue(UEDeviceStatus.DISCONNECTED)));
                    if (status == UEDeviceStatus.DISCONNECTED || status == UEDeviceStatus.DISCONNECTING) {
                        EQMainFragment.this.getNavigator().goBack();
                    }
                }
            }
        };
        this.mSeekBarChangeListener = (SeekBar$OnSeekBarChangeListener)new SeekBar$OnSeekBarChangeListener() {
            private boolean isManualChange = false;
            
            public void onProgressChanged(final SeekBar seekBar, final int i, final boolean b) {
                if (this.isManualChange) {
                    Log.d(EQMainFragment.TAG, "Seek bar value changed. Value: " + i);
                    final int[] array = new int[2];
                    seekBar.getLocationInWindow(array);
                    EQMainFragment.this.mEqLevelTextView.setText((CharSequence)String.format("%+1d", i - 5));
                    EQMainFragment.this.mPopupWindow.dismiss();
                    EQMainFragment.this.mPopupWindow.showAtLocation((View)seekBar, 51, array[0] - EQMainFragment.this.mEqSeekBarsHalfWidth / 2, array[1] - EQMainFragment.this.mEqSeekBarsHeight / 11 * (i + 2) - EQMainFragment.this.getResources().getDimensionPixelSize(2131361878));
                    EQMainFragment.this.mCurrentCustomEQValue = EQMainFragment.this.getCustomEQValues();
                    if (EQMainFragment.mEQSettings[EQMainFragment.this.mEQViewPager.getCurrentItem()] != UEEQSetting.CUSTOM) {
                        Log.d(EQMainFragment.TAG, "Change tab to CUSTOM");
                        EQMainFragment.this.mEQViewPager.setCurrentItem(EQMainFragment.mEQViewPagerCustomItem);
                        EQMainFragment.this.mCurrentEQValue = UEEQSetting.CUSTOM;
                    }
                    else {
                        EQMainFragment.this.updateCustomEQ(EQMainFragment.this.mCurrentCustomEQValue);
                    }
                }
            }
            
            public void onStartTrackingTouch(final SeekBar seekBar) {
                this.isManualChange = true;
            }
            
            public void onStopTrackingTouch(final SeekBar seekBar) {
                this.isManualChange = false;
                EQMainFragment.this.mPopupWindow.dismiss();
            }
        };
    }
    
    public static byte[] convertDeviceToUIEQValues(final byte[] array) {
        final byte[] array2 = new byte[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = (byte)Math.round(array[i] / 25.6f);
        }
        return array2;
    }
    
    public static byte[] convertUIToDeviceEQValues(final byte[] array) {
        final byte[] array2 = new byte[array.length];
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == 5) {
                array2[i] = 127;
            }
            else {
                array2[i] = (byte)Math.round(array[i] * 25.6f);
            }
        }
        return array2;
    }
    
    private void createProgressPopUp() {
        final View inflate = this.getActivity().getLayoutInflater().inflate(2130968623, (ViewGroup)null);
        this.mPopupWindow = new PopupWindow(inflate, -2, -2);
        this.mEqLevelTextView = (TextView)inflate.findViewById(2131624110);
        final Drawable drawable = ContextCompat.getDrawable((Context)this.getActivity(), 2130837595);
        drawable.setColorFilter(ContextCompat.getColor((Context)this.getActivity(), 2131558420), PorterDuff$Mode.SRC_ATOP);
        if (Build$VERSION.SDK_INT < 16) {
            this.mEqLevelTextView.setBackgroundDrawable(drawable);
        }
        else {
            this.mEqLevelTextView.setBackground(drawable);
        }
    }
    
    private void showUpdateViaUSBDialog() {
        Log.d(EQMainFragment.TAG, "Show update via USB dialog");
        App.getInstance().showAlertDialog(this.getResources().getString(2131165258), 2130837749, 2131165285, 2131165341, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                if (n == -1) {
                    EQMainFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(EQMainFragment.this.getString(2131165490))));
                }
            }
        });
    }
    
    public void beginUpdate() {
        Log.d(EQMainFragment.TAG, "Begin update");
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        if (connectedDevice != null && connectedDevice.getConnectionType() == UEConnectionType.SPP) {
            ((SafeTask<Void, Progress, Result>)new UpdateTask()).executeOnThreadPoolExecutor(new Void[0]);
        }
        else {
            Log.d(EQMainFragment.TAG, "Device not connected. Go back");
            this.getNavigator().goBack();
        }
    }
    
    public byte[] getCustomEQValues() {
        return new byte[] { (byte)(this.mBassLeftSeekBar.getProgress() - 5), (byte)(this.mBassRightSeekBar.getProgress() - 5), (byte)(this.mMidSeekBar.getProgress() - 5), (byte)(this.mTrebleLeftSeekBar.getProgress() - 5), (byte)(this.mTrebleRightSeekBar.getProgress() - 5) };
    }
    
    public byte[] getEQValues(final UEEQSetting ueeqSetting) {
        byte[] array = null;
        switch (ueeqSetting) {
            default: {
                array = EQMainFragment.STANDARD_PRESET_EQ_VALUES;
                break;
            }
            case VOCAL: {
                array = EQMainFragment.VOCAL_PRESET_EQ_VALUES;
                break;
            }
            case INTIMATE: {
                array = EQMainFragment.INTIMATE_PRESET_EQ_VALUES;
                break;
            }
            case BASS_BOOST: {
                array = EQMainFragment.BASSBOOST_PRESET_EQ_VALUES;
                break;
            }
            case CUSTOM: {
                array = this.mCurrentCustomEQValue;
                break;
            }
        }
        return array;
    }
    
    @Override
    public int getStatusBarColor() {
        Object o = this.getContext();
        if (o == null) {
            return -7829368;
        }
        o = ((Context)o).obtainStyledAttributes(new int[] { 2130771978 });
        try {
            return ((TypedArray)o).getColor(0, 0);
        }
        catch (Exception ex) {
            final int color = -7829368;
            ((TypedArray)o).recycle();
            return color;
        }
        finally {
            ((TypedArray)o).recycle();
        }
        return -7829368;
    }
    
    @Override
    public String getTitle() {
        return App.getInstance().getString(2131165272);
    }
    
    @Override
    public int getToolbarBackgroundColor() {
        Object o = this.getActivity();
        if (o == null) {
            return -16777216;
        }
        o = ((Context)o).obtainStyledAttributes(new int[] { 2130771977 });
        try {
            return ((TypedArray)o).getColor(0, 0);
        }
        catch (Exception ex) {
            final int color = -16777216;
            ((TypedArray)o).recycle();
            return color;
        }
        finally {
            ((TypedArray)o).recycle();
        }
        return -16777216;
    }
    
    @Override
    public int getToolbarTextColor() {
        Object o = this.getContext();
        if (o == null) {
            return -7829368;
        }
        o = ((Context)o).obtainStyledAttributes(new int[] { 2130771978 });
        try {
            return ((TypedArray)o).getColor(0, 0);
        }
        catch (Exception ex) {
            final int color = -7829368;
            ((TypedArray)o).recycle();
            return color;
        }
        finally {
            ((TypedArray)o).recycle();
        }
        return -7829368;
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setHasOptionsMenu(true);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(2130968627, viewGroup, false);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.logitech.ue.centurion.CONNECTION_CHANGED");
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(this.mBroadcastReceiver, intentFilter);
        this.beginUpdate();
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
        this.mBassLeftSeekBar.setOnSeekBarChangeListener(this.mSeekBarChangeListener);
        this.mBassRightSeekBar.setOnSeekBarChangeListener(this.mSeekBarChangeListener);
        this.mMidSeekBar.setOnSeekBarChangeListener(this.mSeekBarChangeListener);
        this.mTrebleLeftSeekBar.setOnSeekBarChangeListener(this.mSeekBarChangeListener);
        this.mTrebleRightSeekBar.setOnSeekBarChangeListener(this.mSeekBarChangeListener);
        this.mEQViewPager.setAdapter(new EQNamesPagerAdapter((Context)this.getActivity(), EQMainFragment.mEQSettings));
        this.mEQViewPager.setOffscreenPageLimit(EQMainFragment.mEQSettings.length);
        this.mUpdateHandler = new Handler();
        this.mEQViewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener)new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(final int n) {
            }
            
            @Override
            public void onPageScrolled(final int n, final float n2, final int n3) {
            }
            
            @Override
            public void onPageSelected(final int i) {
                final int color = ContextCompat.getColor((Context)EQMainFragment.this.getActivity(), 2131558421);
                for (int j = 0; j < EQMainFragment.mEQSettings.length; ++j) {
                    final TextView textView = (TextView)EQMainFragment.this.mEQViewPager.findViewWithTag((Object)j);
                    if (textView != null) {
                        if (textView.getTag().equals(i)) {
                            textView.setTextColor(-1);
                        }
                        else {
                            textView.setTextColor(color);
                        }
                    }
                }
                if (EQMainFragment.mEQSettings[i] == UEEQSetting.CUSTOM && !EQMainFragment.this.isCustomEQSupported) {
                    EQMainFragment.this.showUpdateViaUSBDialog();
                }
                else if (EQMainFragment.mEQSettings[i] == UEEQSetting.BASS_BOOST && !EQMainFragment.this.isBassBoostSupported) {
                    EQMainFragment.this.showUpdateViaUSBDialog();
                }
                else {
                    EQMainFragment.this.mCurrentEQValue = EQMainFragment.mEQSettings[i];
                    EQMainFragment.this.updateEQBars(EQMainFragment.this.getEQValues(EQMainFragment.this.mCurrentEQValue));
                    EQMainFragment.this.updateEQ(EQMainFragment.this.mCurrentEQValue);
                }
                EQMainFragment.this.setEnableBands(EQMainFragment.mEQSettings[i] == UEEQSetting.CUSTOM);
            }
        });
        final TextView textView = (TextView)this.mEQViewPager.findViewWithTag((Object)0);
        if (textView != null) {
            textView.setTextColor(-1);
        }
        this.createProgressPopUp();
        this.mTrebleRightSeekBar.post((Runnable)new Runnable() {
            @Override
            public void run() {
                EQMainFragment.this.mEqSeekBarsHeight = EQMainFragment.this.mTrebleRightSeekBar.getWidth();
                EQMainFragment.this.mEqSeekBarsHalfWidth = EQMainFragment.this.mTrebleRightSeekBar.getHeight() / 2;
            }
        });
    }
    
    public void setEnableBands(final boolean b) {
        Log.d(EQMainFragment.TAG, "Set enable bands. Enable: " + b);
        this.mBassLeftSeekBar.setEnabled(b);
        this.mBassRightSeekBar.setEnabled(b);
        this.mMidSeekBar.setEnabled(b);
        this.mTrebleLeftSeekBar.setEnabled(b);
        this.mTrebleRightSeekBar.setEnabled(b);
    }
    
    public void updateCustomEQ(final byte[] array) {
        this.mUpdateHandler.removeCallbacksAndMessages((Object)null);
        this.mUpdateHandler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                Log.d(EQMainFragment.TAG, String.format("Update 5 Bands. Values: %d %d %d %d %d", array[0], array[1], array[2], array[3], array[4]));
                EQMainFragment.this.mCustomEQUpdater.update(EQMainFragment.convertUIToDeviceEQValues(array));
            }
        }, 500L);
    }
    
    public void updateEQ(final UEEQSetting ueeqSetting) {
        Log.d(EQMainFragment.TAG, String.format("Update EQ on device. Value: %s", ueeqSetting));
        if (this.mCurrentEQValue != UEEQSetting.CUSTOM) {
            this.mCustomEQUpdater.stop();
            ((SafeTask<Void, Progress, Result>)new SetDeviceEQTask(ueeqSetting)).executeOnThreadPoolExecutor(new Void[0]);
        }
        else if (this.isCustomEQSupported) {
            this.updateCustomEQ(this.getEQValues(ueeqSetting));
        }
        else {
            ((SafeTask<Void, Progress, Result>)new SetDeviceEQTask(ueeqSetting)).executeOnThreadPoolExecutor(new Void[0]);
        }
        FlurryTracker.logEQChange(ueeqSetting.name());
    }
    
    public void updateEQBars(final byte[] array) {
        Log.d(EQMainFragment.TAG, String.format("Update EQ bars. Value: %d %d %d %d %d", array[0], array[1], array[2], array[3], array[4]));
        this.mBassLeftSeekBar.setProgress(array[0] + 5);
        this.mBassRightSeekBar.setProgress(array[1] + 5);
        this.mMidSeekBar.setProgress(array[2] + 5);
        this.mTrebleLeftSeekBar.setProgress(array[3] + 5);
        this.mTrebleRightSeekBar.setProgress(array[4] + 5);
        this.mEQViewPager.setCurrentItem(Utils.indexOf(EQMainFragment.mEQSettings, this.mCurrentEQValue));
    }
    
    public static class EQNamesPagerAdapter extends PagerAdapter
    {
        private Context mContext;
        private UEEQSetting[] mEQSettings;
        
        public EQNamesPagerAdapter(final Context mContext, @NonNull final UEEQSetting[] meqSettings) {
            this.mContext = mContext;
            this.mEQSettings = meqSettings;
        }
        
        @Override
        public void destroyItem(final ViewGroup viewGroup, final int n, final Object o) {
            viewGroup.removeView((View)o);
        }
        
        @Override
        public int getCount() {
            return this.mEQSettings.length;
        }
        
        @Override
        public Object instantiateItem(final ViewGroup viewGroup, final int i) {
            final ViewGroup viewGroup2 = (ViewGroup)LayoutInflater.from(this.mContext).inflate(2130968622, viewGroup, false);
            final TextView textView = (TextView)viewGroup2.findViewById(2131624109);
            textView.setText((CharSequence)EQHelper.getEQName(this.mContext, this.mEQSettings[i]));
            textView.setTag((Object)i);
            viewGroup.addView((View)viewGroup2);
            return viewGroup2;
        }
        
        @Override
        public boolean isViewFromObject(final View view, final Object o) {
            return view == o;
        }
    }
    
    public class UpdateTask extends SafeTask<Void, Void, Object[]>
    {
        @Override
        public String getTag() {
            return EQMainFragment.TAG;
        }
        
        @Override
        public void onError(final Exception ex) {
            super.onError(ex);
            App.getInstance().processNuclearException(ex);
        }
        
        @Override
        public void onSuccess(final Object[] array) {
            super.onSuccess(array);
            EQMainFragment.this.mCurrentEQValue = (UEEQSetting)array[0];
            EQMainFragment.this.isBassBoostSupported = (boolean)array[1];
            EQMainFragment.this.isCustomEQSupported = (boolean)array[2];
            if (EQMainFragment.this.isCustomEQSupported) {
                Log.d(EQMainFragment.TAG, "Update UI. Custom EQ is supported");
                EQMainFragment.this.mCurrentCustomEQValue = EQMainFragment.convertDeviceToUIEQValues(Arrays.copyOf((byte[])array[3], 5));
            }
            else {
                Log.d(EQMainFragment.TAG, "Update UI. Custom EQ is not supported");
                Arrays.fill(EQMainFragment.this.mCurrentCustomEQValue, (byte)0);
            }
            EQMainFragment.this.updateEQBars(EQMainFragment.this.mCurrentCustomEQValue);
        }
        
        @Override
        public Object[] work(final Void... array) throws Exception {
            final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
            final UEEQSetting eqSetting = connectedDevice.getEQSetting();
            Object[] array2;
            if (connectedDevice.isBassBoostSupported()) {
                if (connectedDevice.is5BandEQSupported()) {
                    array2 = new Object[] { eqSetting, true, true, connectedDevice.getCustomEQ() };
                }
                else {
                    array2 = new Object[] { eqSetting, true, false, null };
                }
            }
            else {
                array2 = new Object[] { eqSetting, false, false, null };
            }
            return array2;
        }
    }
}
