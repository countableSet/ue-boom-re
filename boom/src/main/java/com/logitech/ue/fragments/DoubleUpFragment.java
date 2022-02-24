// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import com.logitech.ue.tasks.SafeTask;
import com.logitech.ue.tasks.GetDeviceBalanceTask;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.tasks.AttachableTask;
import com.logitech.ue.activities.MainActivity;
import com.logitech.ue.centurion.device.devicedata.UEDeviceType;
import com.logitech.ue.UEColourHelper;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.graphics.PointF;
import com.logitech.ue.utils.AnimationUtils;
import com.logitech.ue.other.OnGlobalLayoutSelfRemovingListener;
import com.logitech.ue.centurion.device.devicedata.UEColour;
import com.logitech.ue.tasks.GetAudioRoutingTask;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.view.animation.BounceInterpolator;
import android.animation.ObjectAnimator;
import android.widget.SeekBar$OnSeekBarChangeListener;
import butterknife.ButterKnife;
import com.logitech.ue.other.BlockPartyUtils;
import com.logitech.ue.tasks.SetDeviceBlockPartyStateTask;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import com.logitech.ue.centurion.device.devicedata.UEBlockPartyState;
import com.logitech.ue.tasks.BeginDeviceDiscoveryTask;
import com.logitech.ue.centurion.exceptions.UEUnrecognisedCommandException;
import com.logitech.ue.tasks.GetDeviceBlockPartyStatueTask;
import android.widget.Toast;
import com.logitech.ue.tasks.StopRestreamingTask;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.logitech.ue.FlurryTracker;
import android.util.TypedValue;
import android.os.AsyncTask$Status;
import com.logitech.ue.App;
import butterknife.OnClick;
import com.logitech.ue.tasks.SetDeviceAudioRoutingTask;
import com.logitech.ue.centurion.device.devicedata.UEAudioRouting;
import com.logitech.ue.Utils;
import android.os.Bundle;
import com.logitech.ue.centurion.device.devicedata.UEHardwareInfo;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStreamingStatus;
import com.logitech.ue.tasks.GetDeviceHardwareInfoTask;
import com.logitech.ue.centurion.notification.UEDeviceRestreamingStatusNotification;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;
import android.util.Log;
import android.content.Intent;
import android.content.Context;
import android.os.Vibrator;
import com.logitech.ue.views.FadeButton;
import android.widget.Button;
import com.logitech.ue.views.UEDeviceView;
import com.logitech.ue.views.SpiderFrameLayout;
import android.content.BroadcastReceiver;
import com.logitech.ue.other.BalanceUpdater;
import android.widget.SeekBar;
import android.view.View;
import butterknife.Bind;
import android.widget.TextView;
import com.logitech.ue.interfaces.IPage;
import android.support.v4.app.Fragment;

public class DoubleUpFragment extends Fragment implements IPage
{
    public static final int LEFT_SPOT_INDEX = 0;
    public static final int RIGHT_SPOT_INDEX = 1;
    private static final String TAG;
    @Bind({ 2131624098 })
    TextView mBalanceLabel;
    @Bind({ 2131624097 })
    TextView mBalanceLeftLabel;
    @Bind({ 2131624113 })
    View mBalancePanel;
    @Bind({ 2131624099 })
    TextView mBalanceRightLabel;
    @Bind({ 2131624096 })
    SeekBar mBalanceSeekBar;
    BalanceUpdater mBalanceUpdater;
    BroadcastReceiver mBroadcastReceiver;
    @Bind({ 2131624117 })
    SpiderFrameLayout mContainer;
    @Bind({ 2131624107 })
    TextView mDoubleUpButton;
    @Bind({ 2131624115 })
    View mDoubleUpPanel;
    FullUpdateTask mFullUpdateTask;
    @Bind({ 2131624112 })
    TextView mHintLabel;
    boolean mIsStereoON;
    @Bind({ 2131624118 })
    UEDeviceView mMainDeviceView;
    @Bind({ 2131624111 })
    TextView mPowerUpLabel;
    @Bind({ 2131624119 })
    UEDeviceView mSecondaryDeviceView;
    @Bind({ 2131624120 })
    Button mStartStopButton;
    State mState;
    @Bind({ 2131624108 })
    TextView mStereoButton;
    @Bind({ 2131624116 })
    FadeButton mSwapSpeakers;
    UpdateBalanceTask mUpdateBalanceTask;
    Vibrator vibrator;
    
    static {
        TAG = DoubleUpFragment.class.getSimpleName();
    }
    
    public DoubleUpFragment() {
        this.mState = null;
        this.mIsStereoON = true;
        this.mBalanceUpdater = new BalanceUpdater();
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                Log.d(DoubleUpFragment.TAG, "Receive broadcast " + intent.getAction());
                if (DoubleUpFragment.this.getView() != null) {
                    if (intent.getAction().equals("com.logitech.ue.centurion.CONNECTION_CHANGED")) {
                        UEDeviceStatus.getStatus(intent.getExtras().getInt("status"));
                        DoubleUpFragment.this.beginFullUpdate();
                    }
                    else if (intent.getAction().equals("com.logitech.ue.centurion.RESTREAMING_STATUS_CHANGE_NOTIFICATION")) {
                        final UEDeviceStreamingStatus devicesStreamingStatus = ((UEDeviceRestreamingStatusNotification)intent.getExtras().getParcelable("notification")).getDevicesStreamingStatus();
                        Log.d(DoubleUpFragment.TAG, "Receive status broadcast. Status: " + devicesStreamingStatus.toString());
                        ((SafeTask<Void, Progress, Result>)new GetDeviceHardwareInfoTask() {
                            public void onSuccess(final UEHardwareInfo ueHardwareInfo) {
                                super.onSuccess((T)ueHardwareInfo);
                                if (DoubleUpFragment.this.getView() != null) {
                                    DoubleUpFragment.this.processDeviceStatus(devicesStreamingStatus, ueHardwareInfo.getPrimaryDeviceColour(), ueHardwareInfo.getSecondaryDeviceColour());
                                    DoubleUpFragment.this.beginUpdateBalance();
                                }
                            }
                        }).executeOnThreadPoolExecutor(new Void[0]);
                    }
                }
            }
        };
    }
    
    public static DoubleUpFragment newInstance() {
        final DoubleUpFragment doubleUpFragment = new DoubleUpFragment();
        doubleUpFragment.setArguments(new Bundle());
        return doubleUpFragment;
    }
    
    private void updateDoubleUpControls(final State state, final int n, final int n2) {
        if (state == State.DoubleUp) {
            if (Utils.areSpeakersInPerfectDoubleUpState(n, n2)) {
                Log.d(DoubleUpFragment.TAG, "Show Double Up controls");
                this.mDoubleUpPanel.setVisibility(0);
                if (this.mIsStereoON) {
                    this.mStereoButton.setSelected(true);
                    this.mDoubleUpButton.setSelected(false);
                    this.mBalancePanel.setVisibility(0);
                    this.mSwapSpeakers.setVisibility(0);
                    this.mSwapSpeakers.setClickableDisableState(false);
                }
                else {
                    this.mStereoButton.setSelected(false);
                    this.mDoubleUpButton.setSelected(true);
                    this.mBalancePanel.setVisibility(8);
                    this.mSwapSpeakers.setVisibility(8);
                    this.mBalanceSeekBar.setProgress(this.mBalanceSeekBar.getMax() / 2);
                }
            }
            else {
                Log.d(DoubleUpFragment.TAG, "Hide Double Up controls");
                this.mDoubleUpPanel.setVisibility(8);
                this.mSwapSpeakers.setVisibility(8);
                this.mBalancePanel.setVisibility(8);
                this.mBalanceSeekBar.setProgress(this.mBalanceSeekBar.getMax() / 2);
            }
        }
        else {
            Log.d(DoubleUpFragment.TAG, "Hide Double Up controls");
            this.mDoubleUpPanel.setVisibility(8);
            this.mSwapSpeakers.setVisibility(8);
            this.mBalancePanel.setVisibility(8);
            this.mBalanceSeekBar.setProgress(this.mBalanceSeekBar.getMax() / 2);
        }
    }
    
    @OnClick({ 2131624116 })
    public void OnSwapSpeakersClick(final View view) {
        if (!this.mSwapSpeakers.isClickableDisabled()) {
            Log.d(DoubleUpFragment.TAG, "Double up swap clicked");
            UEAudioRouting ueAudioRouting;
            if (this.mContainer.getSpot((View)this.mMainDeviceView) == 0) {
                ueAudioRouting = UEAudioRouting.MasterRight;
            }
            else {
                ueAudioRouting = UEAudioRouting.MasterLeft;
            }
            ((SafeTask<Void, Progress, Result>)new SetDeviceAudioRoutingTask(ueAudioRouting)).executeOnThreadPoolExecutor(new Void[0]);
            this.mBalanceSeekBar.setProgress(this.mBalanceSeekBar.getMax() - this.mBalanceSeekBar.getProgress());
            this.swapSpeakers((View)this.mMainDeviceView, (View)this.mSecondaryDeviceView, this.mContainer.getSpot((View)this.mMainDeviceView) == 0, true);
        }
    }
    
    public void beginFullUpdate() {
        if (this.getView() == null) {
            Log.w(DoubleUpFragment.TAG, "Don't update UI if view doesn't exist");
        }
        else {
            Log.d(DoubleUpFragment.TAG, "Begin full UI update");
            if (App.getDeviceConnectionState().isBtClassicConnectedState()) {
                if (this.mFullUpdateTask == null || this.mFullUpdateTask.getStatus() == AsyncTask$Status.FINISHED) {
                    ((SafeTask<Void, Progress, Result>)(this.mFullUpdateTask = new FullUpdateTask())).executeOnThreadPoolExecutor(new Void[0]);
                }
            }
            else {
                this.showDisabled();
            }
        }
    }
    
    public void beginUpdateBalance() {
        if (this.mUpdateBalanceTask == null || this.mUpdateBalanceTask.getStatus() == AsyncTask$Status.FINISHED) {
            ((SafeTask<Void, Progress, Result>)(this.mUpdateBalanceTask = new UpdateBalanceTask())).executeOnThreadPoolExecutor(new Void[0]);
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
        return App.getInstance().getString(2131165255);
    }
    
    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        Log.d(DoubleUpFragment.TAG, "Attach");
        this.vibrator = (Vibrator)context.getSystemService("vibrator");
    }
    
    @OnClick({ 2131624098 })
    public void onBalanceCenterClicked(final View view) {
        this.mBalanceSeekBar.setProgress(this.mBalanceSeekBar.getMax() / 2);
        this.setDeviceBalance(this.mBalanceSeekBar.getMax() / 2);
    }
    
    @OnClick({ 2131624097 })
    public void onBalanceLeftClicked(final View view) {
        this.mBalanceSeekBar.setProgress(0);
        this.setDeviceBalance(0);
        FlurryTracker.logBalanceChange();
    }
    
    @OnClick({ 2131624099 })
    public void onBalanceRightClicked(final View view) {
        this.mBalanceSeekBar.setProgress(this.mBalanceSeekBar.getMax());
        this.setDeviceBalance(this.mBalanceSeekBar.getMax());
        FlurryTracker.logBalanceChange();
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(2130968626, viewGroup, false);
    }
    
    @Override
    public void onDeselected() {
        Log.d(DoubleUpFragment.TAG, "Is deselected as Tab");
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(DoubleUpFragment.TAG, "Detach");
    }
    
    @OnClick({ 2131624107 })
    public void onDoubleUpClicked(final View view) {
        Log.d(DoubleUpFragment.TAG, "Double Up clicked");
        this.mDoubleUpButton.setSelected(true);
        this.mStereoButton.setSelected(false);
        ((SafeTask<Void, Progress, Result>)new SetDeviceAudioRoutingTask(UEAudioRouting.Double)).executeOnThreadPoolExecutor(new Void[0]);
        this.mBalanceSeekBar.setProgress(this.mBalanceSeekBar.getMax() / 2);
        this.mBalanceUpdater.update((Byte)0);
        FlurryTracker.logStereoChange(this.mIsStereoON = false);
        this.updateDoubleUpControls(State.DoubleUp, this.mMainDeviceView.getDeviceColor(), this.mSecondaryDeviceView.getDeviceColor());
    }
    
    @Override
    public void onSelected() {
        Log.d(DoubleUpFragment.TAG, "Is selected as Tab");
    }
    
    @Override
    public void onStart() {
        super.onStart();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.logitech.ue.centurion.CONNECTION_CHANGED");
        intentFilter.addAction("com.logitech.ue.centurion.RESTREAMING_STATUS_CHANGE_NOTIFICATION");
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(this.mBroadcastReceiver, intentFilter);
        this.beginFullUpdate();
    }
    
    @OnClick({ 2131624120 })
    public void onStartScanClick(final View view) {
        if (this.mState == State.DoubleUp) {
            Log.d(DoubleUpFragment.TAG, "Double up unlink clicked");
            this.showSolo(this.mMainDeviceView.getDeviceColor());
            ((SafeTask<Void, Progress, Result>)new StopRestreamingTask()).executeOnThreadPoolExecutor(new Void[0]);
            Toast.makeText((Context)this.getActivity(), (CharSequence)"End Double Up", 0).show();
        }
        else {
            Log.d(DoubleUpFragment.TAG, "Double up start scan clicked");
            ((SafeTask<Void, Progress, Result>)new GetDeviceBlockPartyStatueTask() {
                @Override
                public void onError(final Exception ex) {
                    super.onError(ex);
                    if (ex instanceof UEUnrecognisedCommandException) {
                        ((SafeTask<Void, Progress, Result>)new BeginDeviceDiscoveryTask()).executeOnThreadPoolExecutor(new Void[0]);
                    }
                }
                
                public void onSuccess(final UEBlockPartyState ueBlockPartyState) {
                    super.onSuccess((T)ueBlockPartyState);
                    if (DoubleUpFragment.this.getView() != null) {
                        if (ueBlockPartyState.isOnOrEntering()) {
                            Log.d(DoubleUpFragment.TAG, "Speaker in Block Party. Show \"End Block Party\" dialog");
                            App.getInstance().showAlertDialog(DoubleUpFragment.this.getString(2131165235), 2131165234, 2131165261, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                                public void onClick(final DialogInterface dialogInterface, final int n) {
                                    if (n == -1) {
                                        Log.d(DoubleUpFragment.TAG, "End Block Party and go to Double Up activity");
                                        ((SafeTask<Void, Progress, Result>)new SetDeviceBlockPartyStateTask(false) {
                                            @Override
                                            public void onError(final Exception ex) {
                                                super.onError(ex);
                                                Log.e(DoubleUpFragment.TAG, "Stop party mode failed", (Throwable)ex);
                                                App.getInstance().gotoNuclearHome(ex);
                                            }
                                            
                                            public void onSuccess(final Void void1) {
                                                super.onSuccess((T)void1);
                                                if (DoubleUpFragment.this.getView() != null) {
                                                    ((SafeTask<Void, Progress, Result>)new BeginDeviceDiscoveryTask()).executeOnThreadPoolExecutor(new Void[0]);
                                                    DoubleUpFragment.this.showScanning(DoubleUpFragment.this.mMainDeviceView.getDeviceColor());
                                                    BlockPartyUtils.broadcastBlackPartyStateChanged(UEBlockPartyState.OFF, DoubleUpFragment.class.getSimpleName(), DoubleUpFragment.this.getContext());
                                                }
                                            }
                                        }).executeOnThreadPoolExecutor(new Void[0]);
                                    }
                                }
                            });
                        }
                        else {
                            ((SafeTask<Void, Progress, Result>)new BeginDeviceDiscoveryTask()).executeOnThreadPoolExecutor(new Void[0]);
                            DoubleUpFragment.this.showScanning(DoubleUpFragment.this.mMainDeviceView.getDeviceColor());
                        }
                    }
                }
            }).executeOnThreadPoolExecutor(new Void[0]);
        }
    }
    
    @OnClick({ 2131624108 })
    public void onStereoClicked(final View view) {
        Log.d(DoubleUpFragment.TAG, "Stereo clicked");
        this.mDoubleUpButton.setSelected(false);
        this.mStereoButton.setSelected(true);
        UEAudioRouting ueAudioRouting;
        if (this.mContainer.getViewBySpot(0) == this.mMainDeviceView) {
            ueAudioRouting = UEAudioRouting.MasterLeft;
        }
        else {
            ueAudioRouting = UEAudioRouting.MasterRight;
        }
        ((SafeTask<Void, Progress, Result>)new SetDeviceAudioRoutingTask(ueAudioRouting)).executeOnThreadPoolExecutor(new Void[0]);
        FlurryTracker.logStereoChange(this.mIsStereoON = true);
        this.updateDoubleUpControls(State.DoubleUp, this.mMainDeviceView.getDeviceColor(), this.mSecondaryDeviceView.getDeviceColor());
    }
    
    @Override
    public void onStop() {
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
        this.mContainer.setDisplayMode(SpiderFrameLayout.DisplayMode.DoubleUP);
        this.mContainer.setMasterView((View)this.mMainDeviceView);
        this.mContainer.reserveSpot(0, (View)this.mMainDeviceView);
        this.mContainer.reserveSpot(1, (View)this.mSecondaryDeviceView);
        this.mBalanceSeekBar.setOnSeekBarChangeListener((SeekBar$OnSeekBarChangeListener)new SeekBar$OnSeekBarChangeListener() {
            public void onProgressChanged(final SeekBar seekBar, final int n, final boolean b) {
                DoubleUpFragment.this.updateBalanceSeekBar(n, b);
            }
            
            public void onStartTrackingTouch(final SeekBar seekBar) {
            }
            
            public void onStopTrackingTouch(final SeekBar seekBar) {
                FlurryTracker.logBalanceChange();
            }
        });
        this.showDisabled();
    }
    
    void playJumpAnimation(final View view, final float n, final int n2, final int n3) {
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)view, View.Y, new float[] { view.getY() - n });
        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)view, View.Y, new float[] { view.getY() });
        ofFloat.setDuration((long)Math.round(n2 / 3.0f));
        ofFloat2.setDuration((long)Math.round(n2 * 2 / 3.0f));
        ofFloat2.setInterpolator((TimeInterpolator)new BounceInterpolator());
        final AnimatorSet set = new AnimatorSet();
        set.playSequentially(new Animator[] { (Animator)ofFloat, (Animator)ofFloat2 });
        set.setStartDelay((long)n3);
        set.start();
    }
    
    void processDeviceStatus(final UEDeviceStreamingStatus ueDeviceStreamingStatus, final int n, final int n2) {
        if (this.mState == null) {
            this.processInitialDeviceStatus(ueDeviceStreamingStatus, n, n2);
        }
        else {
            this.processNormalDeviceStatus(ueDeviceStreamingStatus, n, n2);
        }
    }
    
    void processInitialDeviceStatus(final UEDeviceStreamingStatus ueDeviceStreamingStatus, final int i, final int j) {
        Log.d(DoubleUpFragment.TAG, String.format("Process initial status %s. Main color: %02X. Secondary color: %02X", ueDeviceStreamingStatus.name(), i, j));
        switch (ueDeviceStreamingStatus) {
            default: {
                this.showSolo(i);
                break;
            }
            case TWS_CONNECTED:
            case TWS_STREAMING:
            case TWS_STREAMING_STOPPED: {
                ((SafeTask<Void, Progress, Result>)new GetAudioRoutingTask() {
                    public void onSuccess(final UEAudioRouting ueAudioRouting) {
                        super.onSuccess((T)ueAudioRouting);
                        if (DoubleUpFragment.this.getView() != null) {
                            DoubleUpFragment.this.beginUpdateBalance();
                            if (ueAudioRouting == UEAudioRouting.MasterLeft) {
                                DoubleUpFragment.this.mIsStereoON = true;
                                if (DoubleUpFragment.this.mContainer.getSpot((View)DoubleUpFragment.this.mMainDeviceView) != 0) {
                                    DoubleUpFragment.this.swapSpeakers((View)DoubleUpFragment.this.mMainDeviceView, (View)DoubleUpFragment.this.mSecondaryDeviceView, false, false);
                                }
                            }
                            else if (ueAudioRouting == UEAudioRouting.MasterRight) {
                                DoubleUpFragment.this.mIsStereoON = true;
                                if (DoubleUpFragment.this.mContainer.getSpot((View)DoubleUpFragment.this.mMainDeviceView) != 1) {
                                    DoubleUpFragment.this.swapSpeakers((View)DoubleUpFragment.this.mMainDeviceView, (View)DoubleUpFragment.this.mSecondaryDeviceView, true, false);
                                }
                            }
                            else {
                                DoubleUpFragment.this.mIsStereoON = false;
                            }
                            DoubleUpFragment.this.showDoubleUp(i, j);
                        }
                    }
                }).executeOnThreadPoolExecutor(new Void[0]);
                break;
            }
            case PAIRING_IN_INQUIRY_MODE:
            case PAIRING_IN_DISCOVERY_MODE: {
                this.showScanning(i);
                break;
            }
        }
    }
    
    void processNormalDeviceStatus(final UEDeviceStreamingStatus ueDeviceStreamingStatus, final int i, final int j) {
        Log.d(DoubleUpFragment.TAG, String.format("Process status %s. Main color: %02X. Secondary color: %02X", ueDeviceStreamingStatus.name(), i, j));
        switch (ueDeviceStreamingStatus) {
            case TWS_CONNECTED:
            case TWS_STREAMING:
            case TWS_STREAMING_STOPPED: {
                ((SafeTask<Void, Progress, Result>)new GetAudioRoutingTask() {
                    public void onSuccess(final UEAudioRouting ueAudioRouting) {
                        super.onSuccess((T)ueAudioRouting);
                        if (DoubleUpFragment.this.getView() != null) {
                            if (Utils.areSpeakersInPerfectDoubleUpState(i, j)) {
                                DoubleUpFragment.this.beginUpdateBalance();
                                if (ueAudioRouting == UEAudioRouting.MasterLeft) {
                                    DoubleUpFragment.this.mIsStereoON = true;
                                    if (DoubleUpFragment.this.mContainer.getSpot((View)DoubleUpFragment.this.mMainDeviceView) != 0) {
                                        DoubleUpFragment.this.swapSpeakers((View)DoubleUpFragment.this.mMainDeviceView, (View)DoubleUpFragment.this.mSecondaryDeviceView, false, true);
                                    }
                                }
                                else if (ueAudioRouting == UEAudioRouting.MasterRight) {
                                    DoubleUpFragment.this.mIsStereoON = true;
                                    if (DoubleUpFragment.this.mContainer.getSpot((View)DoubleUpFragment.this.mMainDeviceView) != 1) {
                                        DoubleUpFragment.this.swapSpeakers((View)DoubleUpFragment.this.mMainDeviceView, (View)DoubleUpFragment.this.mSecondaryDeviceView, true, true);
                                    }
                                }
                                else {
                                    DoubleUpFragment.this.mIsStereoON = false;
                                }
                            }
                            DoubleUpFragment.this.showDoubleUp(i, j);
                        }
                    }
                }).executeOnThreadPoolExecutor(new Void[0]);
                break;
            }
            case TWS_PAIRING_FAILED: {
                if (this.mState == State.Scanning) {
                    Toast.makeText((Context)this.getActivity(), (CharSequence)this.getString(2131165380), 1).show();
                }
                this.showSolo(i);
                break;
            }
            case TWS_LINK_LOSS: {
                if (this.mState == State.DoubleUp) {
                    Toast.makeText((Context)this.getActivity(), (CharSequence)this.getString(2131165408), 1).show();
                }
                this.showSolo(i);
                break;
            }
            case PAIRING_IN_DISCOVERY_MODE: {
                this.showScanning(i);
                break;
            }
            case A2DP_STREAMING:
            case A2DP_STREAMING_STOPPED:
            case A2DP_CONNECTED:
            case TWS_STOPPED:
            case TWS_DISCONNECTED: {
                this.showSolo(i);
                break;
            }
        }
    }
    
    public void setDeviceBalance(int n) {
        byte b;
        n = (b = (byte)(Math.round(255.0f * (1.0f * n / this.mBalanceSeekBar.getMax())) - 128));
        if (this.mContainer.getSpot((View)this.mMainDeviceView) == 0) {
            if (n == -128) {
                n = (b = (byte)(-(n + 1)));
            }
            else {
                n = (b = (byte)(-n));
            }
        }
        this.mBalanceUpdater.update(b);
    }
    
    void showDisabled() {
        Log.d(DoubleUpFragment.TAG, "Show Disable state");
        this.mState = State.Disabled;
        this.mStartStopButton.setVisibility(8);
        this.mSecondaryDeviceView.setIsLoading(false);
        this.mHintLabel.setText(this.getText(2131165253));
        this.mMainDeviceView.setDeviceColor(UEColour.NO_SPEAKER.getCode(), true);
        this.mSecondaryDeviceView.setDeviceColor(UEColour.NO_SPEAKER.getCode(), true);
        this.mContainer.reserveSpot(0, (View)this.mMainDeviceView);
        this.mContainer.reserveSpot(1, (View)this.mSecondaryDeviceView);
        this.mContainer.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new OnGlobalLayoutSelfRemovingListener(this.mContainer) {
            @Override
            public void onHandleGlobalLayout() {
                final PointF spotPosition = DoubleUpFragment.this.mContainer.getSpotPosition(0, (View)DoubleUpFragment.this.mMainDeviceView);
                DoubleUpFragment.this.mMainDeviceView.animate().x(spotPosition.x).y(spotPosition.y).setDuration((long)AnimationUtils.getAndroidShortAnimationTime(DoubleUpFragment.this.getContext()));
                final PointF spotPosition2 = DoubleUpFragment.this.mContainer.getSpotPosition(1, (View)DoubleUpFragment.this.mSecondaryDeviceView);
                DoubleUpFragment.this.mSecondaryDeviceView.animate().x(spotPosition2.x).y(spotPosition2.y).setDuration((long)AnimationUtils.getAndroidShortAnimationTime(DoubleUpFragment.this.getContext()));
            }
        });
        this.updateDoubleUpControls(State.Disabled, this.mMainDeviceView.getDeviceColor(), UEColour.NO_SPEAKER.getCode());
    }
    
    void showDoubleUp(final int i, final int j) {
        Log.d(DoubleUpFragment.TAG, String.format("Show Double Up state. Main color: %02X. Secondary color:%02X", i, j));
        final State mState = this.mState;
        this.mState = State.DoubleUp;
        this.mSecondaryDeviceView.setVisibility(0);
        this.mStartStopButton.setVisibility(0);
        this.mPowerUpLabel.setVisibility(8);
        this.mHintLabel.setVisibility(8);
        this.mStartStopButton.setText(2131165438);
        this.mSecondaryDeviceView.setIsLoading(false);
        this.mMainDeviceView.setDeviceColor(i, true);
        this.mSecondaryDeviceView.setDeviceColor(j, true);
        this.mContainer.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new OnGlobalLayoutSelfRemovingListener(this.mContainer) {
            @Override
            public void onHandleGlobalLayout() {
                final PointF spotPosition = DoubleUpFragment.this.mContainer.getSpotPosition(DoubleUpFragment.this.mContainer.getSpot((View)DoubleUpFragment.this.mMainDeviceView), (View)DoubleUpFragment.this.mMainDeviceView);
                final float x = spotPosition.x;
                final UEDeviceType deviceTypeByColour = UEColourHelper.getDeviceTypeByColour(j);
                float n = 0.0f;
                Label_0082: {
                    if (deviceTypeByColour != UEDeviceType.Caribe) {
                        n = x;
                        if (deviceTypeByColour != UEDeviceType.RedRocks) {
                            break Label_0082;
                        }
                    }
                    n = x - DoubleUpFragment.this.mMainDeviceView.getWidth() / 4;
                }
                DoubleUpFragment.this.mMainDeviceView.animate().x(n).y(spotPosition.y).setDuration((long)AnimationUtils.getAndroidShortAnimationTime(DoubleUpFragment.this.getContext()));
                final PointF spotPosition2 = DoubleUpFragment.this.mContainer.getSpotPosition(DoubleUpFragment.this.mContainer.getSpot((View)DoubleUpFragment.this.mSecondaryDeviceView), (View)DoubleUpFragment.this.mSecondaryDeviceView);
                final float x2 = spotPosition2.x;
                float n2 = 0.0f;
                Label_0193: {
                    if (deviceTypeByColour != UEDeviceType.Caribe) {
                        n2 = x2;
                        if (deviceTypeByColour != UEDeviceType.RedRocks) {
                            break Label_0193;
                        }
                    }
                    n2 = x2 + DoubleUpFragment.this.mMainDeviceView.getWidth() / 2;
                }
                DoubleUpFragment.this.mSecondaryDeviceView.animate().x(n2).y(spotPosition2.y).setDuration((long)AnimationUtils.getAndroidShortAnimationTime(DoubleUpFragment.this.getContext()));
            }
        });
        this.updateDoubleUpControls(State.DoubleUp, i, j);
        this.beginUpdateBalance();
        this.updateTitle();
    }
    
    void showScanning(final int i) {
        Log.d(DoubleUpFragment.TAG, String.format("Show Scanning state. Main color: %02X", i));
        final State mState = this.mState;
        this.mState = State.Scanning;
        this.mSecondaryDeviceView.setVisibility(0);
        this.mStartStopButton.setVisibility(8);
        this.mPowerUpLabel.setVisibility(0);
        this.mHintLabel.setVisibility(0);
        this.mSecondaryDeviceView.setIsLoading(true);
        this.mHintLabel.setText(2131165252);
        this.mMainDeviceView.setDeviceColor(i, true);
        this.mSecondaryDeviceView.setDeviceColor(UEColour.NO_SPEAKER.getCode(), true);
        this.mContainer.reserveSpot(0, (View)this.mMainDeviceView);
        this.mContainer.reserveSpot(1, (View)this.mSecondaryDeviceView);
        this.mContainer.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new OnGlobalLayoutSelfRemovingListener(this.mContainer) {
            @Override
            public void onHandleGlobalLayout() {
                final PointF spotPosition = DoubleUpFragment.this.mContainer.getSpotPosition(0, (View)DoubleUpFragment.this.mMainDeviceView);
                DoubleUpFragment.this.mMainDeviceView.animate().x(spotPosition.x).y(spotPosition.y).setDuration((long)AnimationUtils.getAndroidShortAnimationTime(DoubleUpFragment.this.getContext()));
                final PointF spotPosition2 = DoubleUpFragment.this.mContainer.getSpotPosition(1, (View)DoubleUpFragment.this.mSecondaryDeviceView);
                DoubleUpFragment.this.mSecondaryDeviceView.animate().x(spotPosition2.x).y(spotPosition2.y).setDuration((long)AnimationUtils.getAndroidShortAnimationTime(DoubleUpFragment.this.getContext()));
            }
        });
        this.updateDoubleUpControls(State.Scanning, this.mMainDeviceView.getDeviceColor(), UEColour.NO_SPEAKER.getCode());
        this.updateTitle();
    }
    
    void showSolo(final int i) {
        Log.d(DoubleUpFragment.TAG, String.format("Show Solo state. Main color: %02X", i));
        final State mState = this.mState;
        this.mState = State.Solo;
        this.mStartStopButton.setVisibility(0);
        this.mPowerUpLabel.setVisibility(8);
        this.mHintLabel.setVisibility(0);
        this.mSecondaryDeviceView.setIsLoading(false);
        this.mStartStopButton.setText(2131165413);
        this.mHintLabel.setText(2131165253);
        this.mMainDeviceView.setDeviceColor(i, true);
        this.mSecondaryDeviceView.setDeviceColor(UEColour.NO_SPEAKER.getCode(), true);
        this.mContainer.reserveSpot(0, (View)this.mMainDeviceView);
        this.mContainer.reserveSpot(1, (View)this.mSecondaryDeviceView);
        this.mContainer.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new OnGlobalLayoutSelfRemovingListener(this.mContainer) {
            @Override
            public void onHandleGlobalLayout() {
                final PointF spotPosition = DoubleUpFragment.this.mContainer.getSpotPosition(0, (View)DoubleUpFragment.this.mMainDeviceView);
                DoubleUpFragment.this.mMainDeviceView.animate().x(spotPosition.x).y(spotPosition.y).setDuration((long)AnimationUtils.getAndroidShortAnimationTime(DoubleUpFragment.this.getContext()));
                final PointF spotPosition2 = DoubleUpFragment.this.mContainer.getSpotPosition(1, (View)DoubleUpFragment.this.mSecondaryDeviceView);
                DoubleUpFragment.this.mSecondaryDeviceView.animate().x(spotPosition2.x).y(spotPosition2.y).setDuration((long)AnimationUtils.getAndroidShortAnimationTime(DoubleUpFragment.this.getContext()));
            }
        });
        this.updateDoubleUpControls(State.Solo, this.mMainDeviceView.getDeviceColor(), UEColour.NO_SPEAKER.getCode());
        this.updateTitle();
    }
    
    public void swapSpeakers(final View view, final View view2, final boolean b, final boolean b2) {
        int n;
        if (b) {
            n = 1;
        }
        else {
            n = 0;
        }
        int n2;
        if (b) {
            n2 = 0;
        }
        else {
            n2 = 1;
        }
        this.mContainer.releaseSpot(0);
        this.mContainer.releaseSpot(1);
        this.mContainer.reserveSpot(n, view);
        this.mContainer.reserveSpot(n2, view2);
        final PointF spotPosition = this.mContainer.getSpotPosition(n, view);
        final PointF spotPosition2 = this.mContainer.getSpotPosition(n2, view2);
        if (b2) {
            view.animate().x(spotPosition.x).y(spotPosition.y).setDuration((long)AnimationUtils.getAndroidShortAnimationTime(this.getResources()));
            view2.animate().x(spotPosition2.x).y(spotPosition2.y).setDuration((long)AnimationUtils.getAndroidShortAnimationTime(this.getResources()));
        }
        else {
            view.setX(spotPosition.x);
            view.setY(spotPosition.y);
            view2.setX(spotPosition2.x);
            view2.setY(spotPosition2.y);
        }
    }
    
    public void updateBalanceSeekBar(int deviceBalance, final boolean b) {
        final float alpha = deviceBalance * 1.0f / this.mBalanceSeekBar.getMax();
        if (deviceBalance > this.mBalanceSeekBar.getMax() / 2) {
            this.mBalanceLeftLabel.setAlpha(0.5f);
            this.mBalanceLabel.setAlpha(1.0f - alpha + 0.5f);
            this.mBalanceRightLabel.setAlpha(alpha);
        }
        else {
            this.mBalanceLeftLabel.setAlpha(1.0f - alpha);
            this.mBalanceLabel.setAlpha(0.5f + alpha);
            this.mBalanceRightLabel.setAlpha(0.5f);
        }
        if (b) {
            Log.d(DoubleUpFragment.TAG, "Update balance seek bar to value " + deviceBalance);
            final int n = this.mBalanceSeekBar.getMax() / 2;
            if (Math.abs(deviceBalance - n) <= 0.05f * this.mBalanceSeekBar.getMax()) {
                deviceBalance = n;
                this.mBalanceSeekBar.setProgress(deviceBalance);
            }
            this.setDeviceBalance(deviceBalance);
        }
    }
    
    void updateTitle() {
        if (this.getActivity() != null) {
            Log.d(DoubleUpFragment.TAG, "Update title");
            ((MainActivity)this.getActivity()).onTitleChanged(this.getParentFragment(), this.getTitle());
        }
        else {
            Log.w(DoubleUpFragment.TAG, "Can't update title because activity is null title");
        }
    }
    
    class FullUpdateTask extends AttachableTask<Object[]>
    {
        @Override
        public String getTag() {
            return this.getClass().getSimpleName();
        }
        
        @Override
        public void onError(final Exception ex) {
            super.onError(ex);
            if (DoubleUpFragment.this.getView() != null) {
                App.getInstance().gotoNuclearHome(ex);
                DoubleUpFragment.this.showDisabled();
            }
        }
        
        public void onSuccess(final Object[] array) {
            super.onSuccess((T)array);
            if (DoubleUpFragment.this.getView() != null) {
                final UEDeviceStreamingStatus ueDeviceStreamingStatus = (UEDeviceStreamingStatus)array[0];
                final UEHardwareInfo ueHardwareInfo = (UEHardwareInfo)array[1];
                DoubleUpFragment.this.processDeviceStatus(ueDeviceStreamingStatus, ueHardwareInfo.getPrimaryDeviceColour(), ueHardwareInfo.getSecondaryDeviceColour());
            }
        }
        
        @Override
        public Object[] work(final Void... array) throws Exception {
            final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
            return new Object[] { connectedDevice.getDeviceStreamingStatus(), connectedDevice.getHardwareInfo() };
        }
    }
    
    public enum State
    {
        Disabled, 
        DoubleUp, 
        Scanning, 
        Solo;
    }
    
    class UpdateBalanceTask extends GetDeviceBalanceTask
    {
        @Override
        public void onError(final Exception ex) {
            super.onError(ex);
            if (DoubleUpFragment.this.getView() != null) {
                DoubleUpFragment.this.updateBalanceSeekBar(0, false);
            }
        }
        
        public void onSuccess(final Byte b) {
            super.onSuccess((T)b);
            if (DoubleUpFragment.this.getView() != null) {
                Byte value = b;
                if (DoubleUpFragment.this.mContainer.getSpot((View)DoubleUpFragment.this.mMainDeviceView) == 1) {
                    byte b2;
                    if (b == -128) {
                        b2 = (byte)(-(b + 1));
                    }
                    else {
                        b2 = -b;
                    }
                    value = b2;
                }
                DoubleUpFragment.this.mBalanceSeekBar.setProgress(Math.round((1.0f - (value + 128) / 255.0f) * DoubleUpFragment.this.mBalanceSeekBar.getMax()));
            }
        }
    }
}
