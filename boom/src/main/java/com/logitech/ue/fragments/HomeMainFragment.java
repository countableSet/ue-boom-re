// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import com.logitech.ue.tasks.SafeTask;
import com.logitech.ue.centurion.device.devicedata.UEAudioRouting;
import com.logitech.ue.centurion.device.devicedata.UEHardwareInfo;
import com.logitech.ue.centurion.spp.exceptions.UESPPConnectionException;
import com.logitech.ue.centurion.exceptions.UEException;
import com.logitech.ue.tasks.AttachableTask;
import com.logitech.ue.UEColourHelper;
import butterknife.OnClick;
import android.net.Uri;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.view.animation.LinearInterpolator;
import android.util.Property;
import android.animation.ObjectAnimator;
import com.logitech.ue.Utils;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import butterknife.ButterKnife;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.util.TypedValue;
import com.logitech.ue.tasks.AdjustDeviceVolumeTask;
import com.logitech.ue.centurion.device.devicedata.UEBlockPartyState;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.tasks.SetVolumeTask;
import com.logitech.ue.tasks.GetDeviceVolumeTask;
import com.logitech.ue.centurion.exceptions.UEUnrecognisedCommandException;
import com.logitech.ue.tasks.GetDeviceBlockPartyStatueTask;
import java.util.Locale;
import com.logitech.ue.utils.AnimationUtils;
import java.util.concurrent.TimeUnit;
import com.logitech.ue.other.DeviceInfo;
import com.logitech.ue.centurion.device.devicedata.UEColour;
import com.logitech.ue.tasks.PowerOnDeviceTask;
import com.logitech.ue.tasks.TurnOffDeviceTask;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastAudioOptions;
import com.logitech.ue.tasks.SetDeviceBroadcastTask;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastState;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastConfiguration;
import com.logitech.ue.tasks.GetDeviceBroadcastTask;
import com.logitech.ue.FlurryTracker;
import android.content.DialogInterface$OnDismissListener;
import com.logitech.ue.tasks.GetIsBroadcastSupportedTask;
import com.logitech.ue.UserPreferences;
import com.logitech.ue.activities.MainActivity;
import com.logitech.ue.tasks.GetDeviceBatteryLevelViaBleTask;
import com.logitech.ue.centurion.device.devicedata.UEChargingInfo;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStreamingStatus;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.notification.UEDeviceRestreamingStatusNotification;
import android.content.Intent;
import android.content.Context;
import com.logitech.ue.App;
import android.util.Log;
import com.logitech.ue.views.VolumeController;
import android.animation.AnimatorSet;
import android.os.Handler;
import com.logitech.ue.views.FadeImageButton;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;
import com.logitech.ue.tasks.GetDeviceChargingInfoTask;
import android.content.BroadcastReceiver;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import android.widget.TextView;
import android.widget.ImageView;
import butterknife.Bind;
import android.view.View;
import com.logitech.ue.interfaces.IPage;
import android.support.v4.app.Fragment;

public class HomeMainFragment extends Fragment implements IPage
{
    private static final int BATTERY_DEVICE_ACCESSING_INTERVAL = 120000;
    private static final int BATTERY_INITIAL_DELAY = 2000;
    private static final int BATTERY_PULLING_INTERVAL = 5000;
    public static final int INVALID_BATTERY_LEVEL = 0;
    public static final int POWER_ON_TIMEOUT = 17000;
    public static final int WAITING_FOR_POWER_OFF_RECONNECT_INTERVAL = 17000;
    private final String TAG;
    @Bind({ 2131624170 })
    View mBatteryChargeIndicator;
    @Bind({ 2131624171 })
    ImageView mBatteryChargeIndicatorImage;
    @Bind({ 2131624172 })
    TextView mBatteryChargeIndicatorText;
    private ScheduledThreadPoolExecutor mBatteryPullingExecutor;
    BroadcastReceiver mBroadcastReceiver;
    private int mCachedBatteryLevel;
    @Bind({ 2131624167 })
    View mConnectingIndicator;
    @Bind({ 2131624168 })
    ImageView mConnectingIndicatorImage;
    @Bind({ 2131624169 })
    TextView mConnectingIndicatorText;
    protected GetDeviceChargingInfoTask mGetDeviceChargingInfoTask;
    private Boolean mIsBLEOn;
    private long mLastBatteryCheckTime;
    private UEDeviceStatus mLastStatusOfDevice;
    @Bind({ 2131624173 })
    FadeImageButton mPowerButton;
    private Handler mPowerOnEventHandler;
    private boolean mPowerOnNotClicked;
    private Handler mReconnectEventHandler;
    private UEDeviceStatus mState;
    private AnimatorSet mVolumeAnimator;
    @Bind({ 2131624166 })
    VolumeController mVolumeController;
    private boolean mWaitingForReconnect;
    private VolumeController.OnControlButtonsClickListener onVolumeClickListener;
    
    public HomeMainFragment() {
        this.TAG = this.getClass().getSimpleName();
        this.mState = UEDeviceStatus.DISCONNECTED;
        this.mIsBLEOn = false;
        this.mPowerOnEventHandler = new Handler();
        this.mLastBatteryCheckTime = -1L;
        this.mCachedBatteryLevel = 0;
        this.mWaitingForReconnect = false;
        this.mLastStatusOfDevice = null;
        this.mReconnectEventHandler = new Handler();
        this.mPowerOnNotClicked = true;
        this.onVolumeClickListener = new VolumeController.OnControlButtonsClickListener() {
            @Override
            public void onMinusButtonClicked() {
                Log.d(HomeMainFragment.this.TAG, "Master minus clicked");
                if (App.getDeviceConnectionState().isBtClassicConnectedState()) {
                    HomeMainFragment.this.changeDeviceVolume(false);
                }
            }
            
            @Override
            public void onMinusButtonHold() {
            }
            
            @Override
            public void onPlusButtonClicked() {
                Log.d(HomeMainFragment.this.TAG, "Master plus clicked");
                if (App.getDeviceConnectionState().isBtClassicConnectedState()) {
                    HomeMainFragment.this.changeDeviceVolume(true);
                }
            }
            
            @Override
            public void onPlusButtonHold() {
            }
        };
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                Log.d(HomeMainFragment.this.TAG, "Receive broadcast " + intent.getAction());
                if (HomeMainFragment.this.getView() != null) {
                    if (intent.getAction().equals("com.logitech.ue.fragments.STATE_CHANGED")) {
                        switch (IntroFragment.IntroState.values()[intent.getIntExtra("intro_state", 0)]) {
                            case beginning: {
                                HomeMainFragment.this.switchState(UEDeviceStatus.STANDBY, false);
                                break;
                            }
                            case finishing: {
                                if (intent.getExtras().containsKey("primary_color")) {
                                    HomeMainFragment.this.switchState(UEDeviceStatus.SINGLE_CONNECTED, false);
                                    break;
                                }
                                HomeMainFragment.this.switchState(UEDeviceStatus.DISCONNECTED, false);
                                break;
                            }
                            case finished: {
                                HomeMainFragment.this.beginUpdateState(true);
                                break;
                            }
                        }
                    }
                    else if (intent.getAction().equals("com.logitech.ue.fragments.PARTY_MODE_STATE_CHANGED")) {
                        HomeMainFragment.this.beginUpdateState(true);
                    }
                    else if (intent.getAction().equals("com.logitech.ue.centurion.ACTION_BROADCAST_STATUS_NOTIFICATION")) {
                        HomeMainFragment.this.processBroadcastStatusChanged();
                    }
                    else if (App.getDeviceConnectionState() != UEDeviceStatus.STANDBY) {
                        if (intent.getAction().equals("com.logitech.ue.centurion.CONNECTION_CHANGED")) {
                            final UEDeviceStatus status = UEDeviceStatus.getStatus(intent.getExtras().getInt("status"));
                            if (status.isBtClassicConnectedState() || status == UEDeviceStatus.CONNECTED_OFF) {
                                HomeMainFragment.this.setWaitingForReconnect(false);
                                HomeMainFragment.this.mPowerOnNotClicked = true;
                                HomeMainFragment.this.mPowerOnEventHandler.removeCallbacksAndMessages((Object)null);
                            }
                            HomeMainFragment.this.beginUpdateState(true);
                        }
                        else if (intent.getAction().equals("com.logitech.ue.centurion.RESTREAMING_STATUS_CHANGE_NOTIFICATION")) {
                            final UEDeviceStreamingStatus devicesStreamingStatus = ((UEDeviceRestreamingStatusNotification)intent.getExtras().getParcelable("notification")).getDevicesStreamingStatus();
                            if (UEDeviceManager.getInstance().getConnectedDevice() != null && UEDeviceManager.getInstance().getConnectedDevice().getDeviceConnectionStatus().isBtClassicConnectedState()) {
                                if (devicesStreamingStatus.isDevicePairedStatus()) {
                                    HomeMainFragment.this.switchState(UEDeviceStatus.DOUBLEUP_CONNECTED, true);
                                }
                                else {
                                    HomeMainFragment.this.switchState(UEDeviceStatus.SINGLE_CONNECTED, true);
                                }
                            }
                            else {
                                HomeMainFragment.this.switchState(UEDeviceStatus.DISCONNECTED, true);
                            }
                        }
                    }
                }
            }
        };
    }
    
    private void beginUpdateState(final boolean b) {
        if (App.getDeviceConnectionState() == UEDeviceStatus.STANDBY) {
            Log.d(this.TAG, "Don't update due to STANDBY state");
        }
        else {
            Log.d(this.TAG, "Update");
            ((SafeTask<Void, Progress, Result>)new UpdateStateTask(b)).executeOnThreadPoolExecutor(new Void[0]);
        }
    }
    
    private void beginUpdateToConnectedHome(final boolean b) {
        Log.d(this.TAG, "Update to connected Home");
        ((SafeTask<Void, Progress, Result>)new UpdateToConnectedHomeTask(b)).executeOnThreadPoolExecutor(new Void[0]);
    }
    
    private void beginUpdateToDoubleUpHome(final boolean b) {
        Log.d(this.TAG, "Update to Double Up Home");
        ((SafeTask<Void, Progress, Result>)new UpdateToDoubleUpTask(b)).executeOnThreadPoolExecutor(new Void[0]);
    }
    
    private void cancelBatteryTimer() {
        Log.d(this.TAG, "Cancel Battery timer");
        if (this.mBatteryPullingExecutor != null) {
            this.mBatteryPullingExecutor.shutdown();
            this.mBatteryPullingExecutor.purge();
            this.mBatteryPullingExecutor = null;
        }
    }
    
    private void cancelWaitForReconnect() {
        this.mReconnectEventHandler.removeCallbacksAndMessages((Object)null);
        this.setWaitingForReconnect(false);
    }
    
    private void fetchMasterBatteryLevel() {
        if (this.mCachedBatteryLevel <= 0 || System.currentTimeMillis() - this.mLastBatteryCheckTime >= 120000L) {
            if (App.getDeviceConnectionState().isBtClassicConnectedState()) {
                Log.d(this.TAG, "Pulling master battery level");
                ((SafeTask<Void, Progress, Result>)(this.mGetDeviceChargingInfoTask = new GetDeviceChargingInfoTask() {
                    @Override
                    public void onError(final Exception ex) {
                        super.onError(ex);
                        Log.e(HomeMainFragment.this.TAG, "Failed fetching master battery level", (Throwable)ex);
                    }
                    
                    public void onSuccess(final UEChargingInfo ueChargingInfo) {
                        super.onSuccess((T)ueChargingInfo);
                        if (HomeMainFragment.this.getView() != null) {
                            HomeMainFragment.this.mCachedBatteryLevel = ueChargingInfo.getCharge();
                            HomeMainFragment.this.mLastBatteryCheckTime = System.currentTimeMillis();
                            HomeMainFragment.this.updateBatteryIndicatorView();
                        }
                    }
                })).executeOnThreadPoolExecutor(new Void[0]);
            }
            else if (App.getDeviceConnectionState() == UEDeviceStatus.CONNECTED_OFF) {
                Log.d(this.TAG, "Pulling master battery level via BLE");
                ((SafeTask<Void, Progress, Result>)new GetDeviceBatteryLevelViaBleTask() {
                    public void onSuccess(final Byte b) {
                        super.onSuccess((T)b);
                        if (HomeMainFragment.this.getView() != null) {
                            if (b == -1) {
                                Log.e(HomeMainFragment.this.TAG, "Fail to get battery level via BLE");
                                HomeMainFragment.this.mBatteryChargeIndicator.setVisibility(4);
                            }
                            else {
                                HomeMainFragment.this.mCachedBatteryLevel = b;
                                Log.d(HomeMainFragment.this.TAG, "Got battery level via BLE. Level: " + HomeMainFragment.this.mCachedBatteryLevel);
                                HomeMainFragment.this.updateBatteryIndicatorView();
                            }
                        }
                    }
                }).executeOnThreadPoolExecutor(new Void[0]);
            }
        }
    }
    
    private int getXUPConnectionCount() {
        final DoubleXUPFragment doubleXUPFragment = (DoubleXUPFragment)((MainActivity)this.getActivity()).getTab(1);
        int n;
        if (doubleXUPFragment != null) {
            final Fragment fragmentById = doubleXUPFragment.getChildFragmentManager().findFragmentById(2131624045);
            if (fragmentById != null && fragmentById instanceof XUpFragment) {
                if (XUpFragment.sWasSelected) {
                    n = ((XUpFragment)fragmentById).getConnectedDevicesCount();
                }
                else {
                    n = UserPreferences.getInstance().getXUPLastSessionConnectionCount();
                }
            }
            else {
                n = 0;
            }
        }
        else {
            n = 0;
        }
        return n;
    }
    
    private void hideConnectingIndicator() {
        this.mConnectingIndicator.setVisibility(4);
        this.mConnectingIndicatorText.setVisibility(4);
    }
    
    private void powerOffSpeaker() {
        ((SafeTask<Void, Progress, Result>)new GetIsBroadcastSupportedTask() {
            @Override
            public void onError(final Exception ex) {
                super.onError(ex);
                Log.e(HomeMainFragment.this.TAG, "Failed to power off speaker", (Throwable)ex);
                App.getInstance().showMessageDialog(HomeMainFragment.this.getString(2131165373), null);
                FlurryTracker.logError(HomeMainFragment.this.TAG, ex.getMessage());
            }
            
            public void onSuccess(final Boolean b) {
                super.onSuccess((T)b);
                if (b) {
                    ((SafeTask<Void, Progress, Result>)new GetDeviceBroadcastTask() {
                        @Override
                        public void onError(final Exception ex) {
                            super.onError(ex);
                            Log.e(HomeMainFragment.this.TAG, "Failed to power off speaker", (Throwable)ex);
                            App.getInstance().showMessageDialog(HomeMainFragment.this.getString(2131165373), null);
                            FlurryTracker.logError(HomeMainFragment.this.TAG, ex.getMessage());
                        }
                        
                        public void onSuccess(final UEBroadcastConfiguration ueBroadcastConfiguration) {
                            super.onSuccess((T)ueBroadcastConfiguration);
                            if ((ueBroadcastConfiguration.getBroadcastState() == UEBroadcastState.ENABLE_SCANNING_AND_BROADCASTING || ueBroadcastConfiguration.getBroadcastState() == UEBroadcastState.ENABLE_BROADCASTING_ONLY) && HomeMainFragment.this.getXUPConnectionCount() != 0) {
                                Log.d(HomeMainFragment.this.TAG, "Can't power off speaker, because it is in broadcasting mode. Show warning dialog");
                                App.getInstance().showAlertDialog(HomeMainFragment.this.getString(2131165466), HomeMainFragment.this.getString(2131165465), HomeMainFragment.this.getString(2131165358).toUpperCase(), HomeMainFragment.this.getString(2131165470).toUpperCase(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                                    public void onClick(final DialogInterface dialogInterface, final int n) {
                                        if (n == -1) {
                                            HomeMainFragment.this.waitForSpeakerReconnect(17000);
                                            ((MainActivity)HomeMainFragment.this.getActivity()).setWaitingForReconnect(true);
                                            ((SafeTask<Void, Progress, Result>)new SetDeviceBroadcastTask(UEBroadcastState.DISABLE, ueBroadcastConfiguration.getAudioSetting()) {
                                                @Override
                                                public void onError(final Exception ex) {
                                                    super.onError(ex);
                                                    Log.e(HomeMainFragment.this.TAG, "Failed to power off speaker", (Throwable)ex);
                                                    App.getInstance().showMessageDialog(HomeMainFragment.this.getString(2131165373), null);
                                                    FlurryTracker.logError(HomeMainFragment.this.TAG, ex.getMessage());
                                                }
                                                
                                                public void onSuccess(final Void void1) {
                                                    super.onSuccess((T)void1);
                                                    FlurryTracker.logXUPEndSession();
                                                    new Handler().postDelayed((Runnable)new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            ((SafeTask<Void, Progress, Result>)new TurnOffDeviceTask() {
                                                                public void onSuccess(final Void void1) {
                                                                    super.onSuccess((T)void1);
                                                                    FlurryTracker.logRemotePowerOff();
                                                                }
                                                            }).executeOnThreadPoolExecutor(new Void[0]);
                                                        }
                                                    }, 2000L);
                                                }
                                            }).executeOnThreadPoolExecutor(new Void[0]);
                                        }
                                    }
                                });
                            }
                            else {
                                Log.d(HomeMainFragment.this.TAG, "Power off speaker");
                                HomeMainFragment.this.waitForSpeakerReconnect(17000);
                                ((MainActivity)HomeMainFragment.this.getActivity()).setWaitingForReconnect(true);
                                ((SafeTask<Void, Progress, Result>)new SetDeviceBroadcastTask(UEBroadcastState.DISABLE, ueBroadcastConfiguration.getAudioSetting()) {
                                    @Override
                                    public void onError(final Exception ex) {
                                        super.onError(ex);
                                        App.getInstance().showMessageDialog(HomeMainFragment.this.getString(2131165373), null);
                                        FlurryTracker.logError(HomeMainFragment.this.TAG, ex.getMessage());
                                        ex.printStackTrace();
                                    }
                                    
                                    public void onSuccess(final Void void1) {
                                        super.onSuccess((T)void1);
                                        FlurryTracker.logXUPEndSession();
                                        ((SafeTask<Void, Progress, Result>)new TurnOffDeviceTask() {
                                            public void onSuccess(final Void void1) {
                                                super.onSuccess((T)void1);
                                                FlurryTracker.logRemotePowerOff();
                                            }
                                        }).executeOnThreadPoolExecutor(new Void[0]);
                                    }
                                }).executeOnThreadPoolExecutor(new Void[0]);
                            }
                        }
                    }).executeOnThreadPoolExecutor(new Void[0]);
                }
                else {
                    Log.d(HomeMainFragment.this.TAG, "Power off speaker");
                    HomeMainFragment.this.waitForSpeakerReconnect(17000);
                    ((MainActivity)HomeMainFragment.this.getActivity()).setWaitingForReconnect(true);
                    ((SafeTask<Void, Progress, Result>)new TurnOffDeviceTask() {
                        public void onSuccess(final Void void1) {
                            super.onSuccess((T)void1);
                            FlurryTracker.logRemotePowerOff();
                        }
                    }).executeOnThreadPoolExecutor(new Void[0]);
                }
            }
        }).executeOnThreadPoolExecutor(new Void[0]);
    }
    
    private void powerOnSpeaker() {
        Log.d(this.TAG, "Power on speaker");
        FlurryTracker.logRemotePowerOn();
        ((SafeTask<Void, Progress, Result>)new PowerOnDeviceTask()).executeOnThreadPoolExecutor(new Void[0]);
        this.mPowerButton.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                int color = 0;
                Log.d(HomeMainFragment.this.TAG, "Show power on spinner");
                HomeMainFragment.this.mPowerButton.setVisibility(0);
                final DeviceInfo lastSeenDevice = UserPreferences.getInstance().getLastSeenDevice();
                final HomeMainFragment this$0 = HomeMainFragment.this;
                if (lastSeenDevice != null) {
                    color = lastSeenDevice.color;
                }
                this$0.updateColor(color);
                HomeMainFragment.this.mPowerOnEventHandler.postDelayed((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (App.getDeviceConnectionState() == UEDeviceStatus.CONNECTED_OFF) {
                            Log.w(HomeMainFragment.this.TAG, "Speaker didn't reconnect. Restore connected off UI");
                            App.getInstance().showMessageDialog(HomeMainFragment.this.getString(2131165374), null);
                            HomeMainFragment.this.updateColor(UEColour.UNKNOWN_COLOUR.getCode());
                        }
                    }
                }, 17000L);
            }
        }, 600L);
    }
    
    private void processBroadcastStatusChanged() {
        this.updateConnectingIndicator();
    }
    
    private void setWaitingForReconnect(final boolean mWaitingForReconnect) {
        this.mWaitingForReconnect = mWaitingForReconnect;
        if (this.getActivity() != null) {
            ((MainActivity)this.getActivity()).setWaitingForReconnect(this.mWaitingForReconnect);
            if (!mWaitingForReconnect) {
                ((MainActivity)this.getActivity()).updateUI(this.mLastStatusOfDevice);
            }
        }
    }
    
    private void showConnectedHome(final int i, final boolean b) {
        final String tag = this.TAG;
        String s;
        if (b) {
            s = "On";
        }
        else {
            s = "Off";
        }
        Log.d(tag, String.format("Show connected Home state. Main speaker color %02X. Animation %s", i, s));
        this.updateColor(i);
        this.mVolumeController.setEnabled(true);
        this.mPowerButton.setVisibility(0);
        this.mPowerButton.setSelected(true);
        this.mBatteryChargeIndicator.setVisibility(0);
        if (this.mCachedBatteryLevel > 0) {
            this.updateBatteryIndicatorView();
        }
        else {
            Log.d(this.TAG, "Battery level is not cached. Don't show battery level");
            this.mBatteryChargeIndicator.setVisibility(4);
        }
    }
    
    private void showConnectedOffHome(final int n, final boolean b) {
        final String tag = this.TAG;
        String s;
        if (b) {
            s = "On";
        }
        else {
            s = "Off";
        }
        Log.d(tag, String.format("Show Connected Off Home state. Animation %s", s));
        this.updateColor(n);
        this.mVolumeController.setEnabled(false);
        this.mPowerButton.setSelected(false);
        this.hideConnectingIndicator();
        if (this.mCachedBatteryLevel > 0) {
            this.updateBatteryIndicatorView();
        }
        else {
            Log.d(this.TAG, "Battery level is not cached. Don't show battery level");
            this.mBatteryChargeIndicator.setVisibility(8);
        }
        if (UserPreferences.getInstance().getBleSetting()) {
            Log.d(this.TAG, "Show power on button");
            this.mPowerButton.setVisibility(0);
        }
        else {
            Log.d(this.TAG, "Don't show power on button");
            this.mPowerButton.setVisibility(8);
        }
    }
    
    private void showConnectingIndicator(final int i) {
        this.mConnectingIndicator.setVisibility(0);
        this.mConnectingIndicatorText.setVisibility(0);
        this.mConnectingIndicatorText.setText((CharSequence)String.format("%2d-UP", i));
    }
    
    private void showDisconnectedHome(final boolean b) {
        final String tag = this.TAG;
        String s;
        if (b) {
            s = "On";
        }
        else {
            s = "Off";
        }
        Log.d(tag, String.format("Show Disconnected Home state. Animation %s", s));
        this.mVolumeController.setEnabled(false);
        this.mPowerButton.setVisibility(8);
        this.mBatteryChargeIndicator.setVisibility(8);
        this.hideConnectingIndicator();
        if (((MainActivity)this.getActivity()).getCurrentTabIndex() == 0 && !this.mWaitingForReconnect) {
            this.getActivity().setTitle((CharSequence)this.getString(2131165287));
        }
    }
    
    private void showPartyHome(final boolean b, final int n) {
        Log.d(this.TAG, "Show Block Party Home state ");
        this.showConnectedHome(n, b);
    }
    
    private void startBatteryPullingTimer(final int n) {
        Log.d(this.TAG, "Start battery pulling timer");
        if (this.mBatteryPullingExecutor == null) {
            this.mBatteryPullingExecutor = new ScheduledThreadPoolExecutor(4);
        }
        this.mBatteryPullingExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                HomeMainFragment.this.fetchMasterBatteryLevel();
            }
        }, 2000L, n, TimeUnit.MILLISECONDS);
    }
    
    private void updateBatteryIndicatorView() {
        if (this.mCachedBatteryLevel > 0) {
            Log.d(this.TAG, "Update master battery indicator view. Level = " + this.mCachedBatteryLevel);
            if (this.mBatteryChargeIndicator.getVisibility() != 0) {
                this.mBatteryChargeIndicator.setAlpha(0.0f);
                this.mBatteryChargeIndicator.setVisibility(0);
                this.mBatteryChargeIndicator.animate().alpha(1.0f).setDuration((long)AnimationUtils.getAndroidShortAnimationTime((Context)this.getActivity())).start();
            }
            this.mBatteryChargeIndicatorText.setText((CharSequence)String.format(Locale.getDefault(), "%3d%%", this.mCachedBatteryLevel));
        }
        else {
            Log.e(this.TAG, "Error on battery level. Turning off master battery indicator");
        }
    }
    
    private void updateConnectingIndicator() {
        if (this.getActivity() != null) {
            final DoubleXUPFragment doubleXUPFragment = (DoubleXUPFragment)((MainActivity)this.getActivity()).getTab(1);
            if (doubleXUPFragment != null) {
                final Fragment fragmentById = doubleXUPFragment.getChildFragmentManager().findFragmentById(2131624045);
                if (fragmentById != null && fragmentById instanceof XUpFragment) {
                    this.showConnectingIndicator(this.getXUPConnectionCount() + 1);
                }
                else if (App.getDeviceConnectionState() == UEDeviceStatus.DOUBLEUP_CONNECTED) {
                    this.showConnectingIndicator(2);
                }
                else {
                    this.showConnectingIndicator(1);
                }
            }
            else {
                this.showConnectingIndicator(1);
            }
        }
    }
    
    private void waitForSpeakerReconnect(final int n) {
        Log.d(this.TAG, "Wait for speaker reconnect");
        this.setWaitingForReconnect(true);
        this.mReconnectEventHandler.removeCallbacksAndMessages((Object)null);
        new Handler().postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                HomeMainFragment.this.mReconnectEventHandler.postDelayed((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        HomeMainFragment.this.setWaitingForReconnect(false);
                        HomeMainFragment.this.beginUpdateState(true);
                    }
                }, (long)n);
            }
        }, 100L);
    }
    
    protected void changeDeviceVolume(final boolean b) {
        ((SafeTask<Void, Progress, Result>)new GetDeviceBlockPartyStatueTask() {
            @Override
            public void onError(final Exception ex) {
                super.onError(ex);
                if (ex instanceof UEUnrecognisedCommandException) {
                    Log.d(HomeMainFragment.this.TAG, "Device is in Block Party mode, so use adjust volume and don't play animation");
                    ((SafeTask<Void, Progress, Result>)new GetDeviceVolumeTask() {
                        @Override
                        public void onError(final Exception ex) {
                            super.onError(ex);
                            App.getInstance().showMessageDialog(HomeMainFragment.this.getString(2131165257), null);
                        }
                        
                        public void onSuccess(final Integer n) {
                            super.onSuccess((T)n);
                            final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
                            if (connectedDevice != null) {
                                int n2;
                                if (b) {
                                    n2 = n + 1;
                                }
                                else {
                                    n2 = n - 1;
                                }
                                HomeMainFragment.this.playVolumeAnimation(n, n2, connectedDevice.isVolume32Supported());
                                ((SafeTask<Void, Progress, Result>)new SetVolumeTask(n2)).executeOnThreadPoolExecutor(new Void[0]);
                            }
                        }
                    }).executeOnThreadPoolExecutor(new Void[0]);
                }
            }
            
            public void onSuccess(final UEBlockPartyState ueBlockPartyState) {
                super.onSuccess((T)ueBlockPartyState);
                if (ueBlockPartyState.isOnOrEntering()) {
                    Log.d(HomeMainFragment.this.TAG, "Device is in Block Party mode, so use adjust volume and don't play animation");
                    ((SafeTask<Void, Progress, Result>)new AdjustDeviceVolumeTask(true, b)).executeOnThreadPoolExecutor(new Void[0]);
                }
                else {
                    ((SafeTask<Void, Progress, Result>)new GetDeviceVolumeTask() {
                        @Override
                        public void onError(final Exception ex) {
                            super.onError(ex);
                            App.getInstance().showMessageDialog(HomeMainFragment.this.getString(2131165257), null);
                        }
                        
                        public void onSuccess(final Integer n) {
                            super.onSuccess((T)n);
                            final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
                            if (connectedDevice != null) {
                                int n2;
                                if (b) {
                                    n2 = n + 1;
                                }
                                else {
                                    n2 = n - 1;
                                }
                                HomeMainFragment.this.playVolumeAnimation(n, n2, connectedDevice.isVolume32Supported());
                                ((SafeTask<Void, Progress, Result>)new SetVolumeTask(n2)).executeOnThreadPoolExecutor(new Void[0]);
                            }
                        }
                    }).executeOnThreadPoolExecutor(new Void[0]);
                }
            }
        }).executeOnThreadPoolExecutor(new Void[0]);
    }
    
    @Override
    public int getAccentColor() {
        final TypedValue typedValue = new TypedValue();
        this.getActivity().getTheme().resolveAttribute(17170446, typedValue, true);
        return typedValue.data;
    }
    
    @Override
    public String getTitle() {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        try {
            return connectedDevice.getBluetoothName();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return App.getInstance().getString(2131165287);
        }
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(2130968635, viewGroup, false);
    }
    
    @Override
    public void onDeselected() {
        Log.d(this.TAG, "Is deselected as Tab");
    }
    
    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }
    
    @Override
    public void onSelected() {
        Log.d(this.TAG, "Is selected as Tab");
        this.updateConnectingIndicator();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.logitech.ue.centurion.CONNECTION_CHANGED");
        intentFilter.addAction("com.logitech.ue.centurion.RESTREAMING_STATUS_CHANGE_NOTIFICATION");
        intentFilter.addAction("com.logitech.ue.fragments.STATE_CHANGED");
        intentFilter.addAction("com.logitech.ue.fragments.PARTY_MODE_STATE_CHANGED");
        intentFilter.addAction("com.logitech.ue.centurion.ACTION_BROADCAST_STATUS_NOTIFICATION");
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(this.mBroadcastReceiver, intentFilter);
        this.beginUpdateState(false);
    }
    
    @Override
    public void onStop() {
        this.cancelBatteryTimer();
        LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(this.mBroadcastReceiver);
        super.onStop();
    }
    
    @Override
    public void onTransition(final float n) {
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        final DeviceInfo lastSeenDevice = UserPreferences.getInstance().getLastSeenDevice();
        int color;
        if (lastSeenDevice != null) {
            color = lastSeenDevice.color;
        }
        else {
            color = 0;
        }
        this.showConnectedOffHome(color, false);
        this.mVolumeController.setOnControlButtonsClickListener(this.onVolumeClickListener);
        this.mPowerOnNotClicked = true;
    }
    
    protected void playVolumeAnimation(int n, int level, final boolean b) {
        int n2;
        if (level < 0) {
            n2 = 0;
        }
        else if (b) {
            if ((n2 = level) > 31) {
                n2 = 31;
            }
        }
        else if ((n2 = level) > 15) {
            n2 = 15;
        }
        if (b) {
            final int round = Math.round(n2 / 31.0f * 10000.0f);
            level = Math.round(n / 31.0f * 10000.0f);
            n = round;
        }
        else {
            final int round2 = Math.round(n2 / 15.0f * 10000.0f);
            level = Math.round(n / 15.0f * 10000.0f);
            n = round2;
        }
        if (this.mVolumeAnimator == null) {
            this.mVolumeController.setLevel(level);
            level = Utils.getAndroidLongAnimationTime(this.getContext());
            this.mVolumeAnimator = null;
        }
        else {
            this.mVolumeAnimator.cancel();
            level = 0;
        }
        this.mVolumeController.setLevelAlpha(1.0f);
        final ObjectAnimator ofInt = ObjectAnimator.ofInt((Object)this.mVolumeController, (Property)VolumeController.LEVEL, new int[] { n });
        ofInt.setInterpolator((TimeInterpolator)new LinearInterpolator());
        ofInt.setDuration((long)Utils.getAndroidShortAnimationTime(this.getContext()));
        ofInt.setStartDelay((long)level);
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.mVolumeController, (Property)VolumeController.LEVEL_ALPHA, new float[] { 0.0f });
        ofFloat.setStartDelay((long)Utils.getAndroidLongAnimationTime(this.getContext()));
        ofFloat.setDuration((long)Utils.getAndroidLongAnimationTime(this.getContext()));
        (this.mVolumeAnimator = new AnimatorSet()).playSequentially(new Animator[] { (Animator)ofInt, (Animator)ofFloat });
        this.mVolumeAnimator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            boolean isCanceled;
            
            public void onAnimationCancel(final Animator animator) {
                super.onAnimationCancel(animator);
                this.isCanceled = true;
            }
            
            public void onAnimationEnd(final Animator animator) {
                super.onAnimationEnd(animator);
                if (this.isCanceled) {
                    HomeMainFragment.this.mVolumeAnimator = null;
                }
            }
        });
        this.mVolumeAnimator.start();
    }
    
    @OnClick({ 2131624173 })
    public void powerClick(final View view) {
        Log.d(this.TAG, "Power button clicked");
        if (this.mState.isBtClassicConnectedState()) {
            if (!this.mPowerButton.isActivated()) {
                Log.d(this.TAG, "Power off is not supported. Show \"Enable Remote Power Off\" dialog");
                App.getInstance().showAlertDialog(this.getString(2131165259), 2130837749, 2131165285, 2131165341, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        if (n == -1) {
                            HomeMainFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(HomeMainFragment.this.getString(2131165490))));
                        }
                    }
                });
            }
            else if (!this.mWaitingForReconnect) {
                this.mPowerOnNotClicked = true;
                this.powerOffSpeaker();
            }
        }
        else if (this.mState == UEDeviceStatus.CONNECTED_OFF) {
            if (!this.mWaitingForReconnect) {
                this.powerOnSpeaker();
                this.mPowerOnNotClicked = false;
            }
        }
        else if (this.mState == UEDeviceStatus.DISCONNECTED && !this.mWaitingForReconnect && this.mPowerOnNotClicked) {
            App.getInstance().showMessageDialog(this.getString(2131165360), null);
        }
    }
    
    public void switchState(final UEDeviceStatus mState, final boolean b) {
        Log.i(this.TAG, "Switch to " + mState.name());
        this.mState = mState;
        switch (mState) {
            case DISCONNECTED: {
                this.cancelBatteryTimer();
                this.showDisconnectedHome(b);
                break;
            }
            case SINGLE_CONNECTED: {
                this.beginUpdateToConnectedHome(b);
                this.startBatteryPullingTimer(5000);
                break;
            }
            case DOUBLEUP_CONNECTED: {
                this.beginUpdateToDoubleUpHome(b);
                this.startBatteryPullingTimer(5000);
                break;
            }
            case CONNECTED_OFF: {
                if (this.mIsBLEOn) {
                    final DeviceInfo lastSeenDevice = UserPreferences.getInstance().getLastSeenDevice();
                    int color;
                    if (lastSeenDevice != null) {
                        color = lastSeenDevice.color;
                    }
                    else {
                        color = 0;
                    }
                    this.showConnectedOffHome(color, b);
                    this.startBatteryPullingTimer(5000);
                    break;
                }
                break;
            }
        }
    }
    
    public void updateColor(int deviceSpineColor) {
        Log.d(this.TAG, String.format("Update device color to %02X.", deviceSpineColor));
        int deviceButtonsColor;
        if (UEColourHelper.isValidColour(deviceSpineColor)) {
            deviceButtonsColor = UEColourHelper.getDeviceButtonsColor(deviceSpineColor);
            deviceSpineColor = UEColourHelper.getDeviceSpineColor(deviceSpineColor);
        }
        else {
            deviceButtonsColor = -1;
            deviceSpineColor = -16777216;
        }
        this.mVolumeController.setPrimaryColor(deviceButtonsColor);
        this.mVolumeController.setSecondaryColor(deviceSpineColor);
        if (Utils.isColorBright(deviceSpineColor)) {
            this.mBatteryChargeIndicatorText.setTextColor(-16777216);
            this.mBatteryChargeIndicatorImage.setColorFilter(-16777216);
            this.mConnectingIndicatorText.setTextColor(-16777216);
            this.mConnectingIndicatorImage.setColorFilter(-16777216);
            this.mPowerButton.setImageResource(2130837717);
        }
        else {
            this.mBatteryChargeIndicatorText.setTextColor(-1);
            this.mBatteryChargeIndicatorImage.setColorFilter(-1);
            this.mConnectingIndicatorText.setTextColor(-1);
            this.mConnectingIndicatorImage.setColorFilter(-1);
            this.mPowerButton.setImageResource(2130837718);
        }
    }
    
    public class UpdateStateTask extends AttachableTask<Object[]>
    {
        boolean mUpdateWithAnimation;
        
        public UpdateStateTask(final boolean mUpdateWithAnimation) {
            this.mUpdateWithAnimation = false;
            this.mUpdateWithAnimation = mUpdateWithAnimation;
        }
        
        @Override
        public String getTag() {
            return UpdateToDoubleUpTask.class.getSimpleName();
        }
        
        @Override
        public void onError(final Exception ex) {
            super.onError(ex);
            FlurryTracker.logError(HomeMainFragment.this.TAG, ex.getMessage());
            App.getInstance().gotoNuclearHome(ex);
        }
        
        public void onSuccess(final Object[] array) {
            super.onSuccess((T)array);
            if (HomeMainFragment.this.getView() != null) {
                final UEDeviceStatus ueDeviceStatus = (UEDeviceStatus)array[0];
                final String string = HomeMainFragment.this.getString(2131165287);
                String title;
                if (ueDeviceStatus.isBtClassicConnectedState()) {
                    final String s = (String)array[1];
                    if (((UEDeviceStreamingStatus)array[2]).isDevicePairedStatus()) {
                        HomeMainFragment.this.switchState(UEDeviceStatus.DOUBLEUP_CONNECTED, this.mUpdateWithAnimation);
                        title = s;
                    }
                    else {
                        HomeMainFragment.this.switchState(UEDeviceStatus.SINGLE_CONNECTED, this.mUpdateWithAnimation);
                        title = s;
                    }
                }
                else if (App.getDeviceConnectionState() == UEDeviceStatus.CONNECTED_OFF) {
                    title = (String)array[1];
                    HomeMainFragment.this.switchState(UEDeviceStatus.CONNECTED_OFF, this.mUpdateWithAnimation);
                }
                else {
                    HomeMainFragment.this.switchState(UEDeviceStatus.DISCONNECTED, this.mUpdateWithAnimation);
                    title = string;
                }
                if (((MainActivity)HomeMainFragment.this.getActivity()).getCurrentTabIndex() == 0 && !HomeMainFragment.this.mWaitingForReconnect) {
                    HomeMainFragment.this.getActivity().setTitle((CharSequence)title);
                }
            }
        }
        
        @Override
        public Object[] work(Void... array) throws Exception {
            final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
            array = (Void[])new Object[3];
            try {
                array[0] = (Void)App.getDeviceConnectionState();
                if (App.getDeviceConnectionState().isBtClassicConnectedState()) {
                    array[1] = (Void)connectedDevice.getBluetoothName();
                    array[2] = (Void)connectedDevice.getDeviceStreamingStatus();
                }
                else if (App.getDeviceConnectionState() == UEDeviceStatus.CONNECTED_OFF) {
                    array[1] = (Void)connectedDevice.getBluetoothName();
                }
                return array;
            }
            catch (UEException ex) {
                App.getInstance().gotoDisconnectedHome();
                return array;
            }
            return array;
        }
    }
    
    public class UpdateToConnectedHomeTask extends AttachableTask<Object[]>
    {
        boolean mUpdateWithAnimation;
        
        public UpdateToConnectedHomeTask(final boolean mUpdateWithAnimation) {
            this.mUpdateWithAnimation = false;
            this.mUpdateWithAnimation = mUpdateWithAnimation;
        }
        
        @Override
        public String getTag() {
            return UpdateToConnectedHomeTask.class.getSimpleName();
        }
        
        @Override
        public void onError(final Exception ex) {
            super.onError(ex);
            FlurryTracker.logError(HomeMainFragment.this.TAG, ex.getMessage());
            App.getInstance().gotoNuclearHome(ex);
        }
        
        public void onSuccess(final Object[] array) {
            super.onSuccess((T)array);
            if (HomeMainFragment.this.getView() != null) {
                final int intValue = (int)array[0];
                final UEBlockPartyState ueBlockPartyState = (UEBlockPartyState)array[1];
                HomeMainFragment.this.mIsBLEOn = (Boolean)array[2];
                if (ueBlockPartyState != null) {
                    if (ueBlockPartyState.isOnOrEntering()) {
                        HomeMainFragment.this.showPartyHome(this.mUpdateWithAnimation, intValue);
                    }
                    else {
                        HomeMainFragment.this.showConnectedHome(intValue, this.mUpdateWithAnimation);
                    }
                }
                else {
                    HomeMainFragment.this.showConnectedHome(intValue, this.mUpdateWithAnimation);
                }
                if (UEDeviceManager.getInstance().isBleSupported()) {
                    if (HomeMainFragment.this.mIsBLEOn != null) {
                        HomeMainFragment.this.mPowerButton.setActivated(true);
                        if (HomeMainFragment.this.mIsBLEOn) {
                            Log.d(HomeMainFragment.this.TAG, "Show power on/off");
                        }
                        else {
                            Log.d(HomeMainFragment.this.TAG, "Don't show power on/off, because it is disabled");
                            HomeMainFragment.this.mPowerButton.setVisibility(8);
                        }
                    }
                    else {
                        Log.d(HomeMainFragment.this.TAG, "Show fade out power on/off");
                        HomeMainFragment.this.mPowerButton.setActivated(false);
                    }
                }
                else {
                    Log.d(HomeMainFragment.this.TAG, "Show fade out power on/off, because of old android");
                    HomeMainFragment.this.mPowerButton.setActivated(false);
                }
                if (HomeMainFragment.this.mCachedBatteryLevel > 0) {
                    HomeMainFragment.this.updateBatteryIndicatorView();
                }
                else {
                    Log.d(HomeMainFragment.this.TAG, "Battery level is not cached. Don't show battery level");
                    HomeMainFragment.this.mBatteryChargeIndicator.setVisibility(8);
                }
                HomeMainFragment.this.updateConnectingIndicator();
            }
        }
        
        @Override
        public Object[] work(final Void... array) throws Exception {
            final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
            if (connectedDevice != null) {
                final Object[] array2 = { connectedDevice.getDeviceColor(), null, null };
                if (connectedDevice.isBlockPartySupported()) {
                    array2[1] = connectedDevice.getBlockPartyState();
                }
                if (connectedDevice.isBLESupported()) {
                    array2[2] = connectedDevice.getBLEState();
                }
                return array2;
            }
            throw new UESPPConnectionException("Device is NULL");
        }
    }
    
    public class UpdateToDoubleUpTask extends AttachableTask<Object[]>
    {
        boolean mUpdateWithAnimation;
        
        public UpdateToDoubleUpTask(final boolean mUpdateWithAnimation) {
            this.mUpdateWithAnimation = false;
            this.mUpdateWithAnimation = mUpdateWithAnimation;
        }
        
        @Override
        public String getTag() {
            return UpdateToDoubleUpTask.class.getSimpleName();
        }
        
        @Override
        public void onError(final Exception ex) {
            super.onError(ex);
            FlurryTracker.logError(HomeMainFragment.this.TAG, ex.getMessage());
            App.getInstance().gotoNuclearHome(ex);
        }
        
        public void onSuccess(final Object[] array) {
            super.onSuccess((T)array);
            if (HomeMainFragment.this.getView() != null) {
                final UEHardwareInfo ueHardwareInfo = (UEHardwareInfo)array[0];
                final UEAudioRouting ueAudioRouting = (UEAudioRouting)array[1];
                final Boolean b = (Boolean)array[2];
                if (HomeMainFragment.this.mCachedBatteryLevel > 0) {
                    HomeMainFragment.this.updateBatteryIndicatorView();
                }
                else {
                    Log.d(HomeMainFragment.this.TAG, "Battery level is not cached. Don't show battery level");
                    HomeMainFragment.this.mBatteryChargeIndicator.setVisibility(8);
                }
                if (UEDeviceManager.getInstance().isBleSupported()) {
                    if (b != null) {
                        if (b) {
                            Log.d(HomeMainFragment.this.TAG, "Show power button");
                            HomeMainFragment.this.mPowerButton.setVisibility(0);
                            HomeMainFragment.this.mPowerButton.setSelected(true);
                        }
                        else {
                            Log.d(HomeMainFragment.this.TAG, "Don't show power button, because it is disabled");
                            HomeMainFragment.this.mPowerButton.setVisibility(8);
                        }
                    }
                    else {
                        Log.d(HomeMainFragment.this.TAG, "Show fade out power button");
                        HomeMainFragment.this.mPowerButton.setVisibility(0);
                        HomeMainFragment.this.mPowerButton.setSelected(true);
                        HomeMainFragment.this.mPowerButton.setClickable(false);
                    }
                }
                else {
                    Log.d(HomeMainFragment.this.TAG, "Show fade out power button, because of old android");
                    HomeMainFragment.this.mPowerButton.setVisibility(0);
                    HomeMainFragment.this.mPowerButton.setSelected(true);
                    HomeMainFragment.this.mPowerButton.setClickable(false);
                }
                HomeMainFragment.this.showConnectingIndicator(2);
            }
        }
        
        @Override
        public Object[] work(final Void... array) throws Exception {
            final Object[] array2 = new Object[3];
            final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
            array2[0] = connectedDevice.getHardwareInfo();
            array2[1] = connectedDevice.getAudioRouting();
            array2[2] = connectedDevice.isBLESupported();
            if (connectedDevice.isBLESupported()) {
                array2[2] = connectedDevice.getBLEState();
            }
            return array2;
        }
    }
}
