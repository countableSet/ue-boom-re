// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import com.logitech.ue.tasks.GetDeviceBalanceTask;
import com.logitech.ue.tasks.SafeTask;
import android.support.v7.widget.DefaultItemAnimator;
import com.logitech.ue.activities.XUPTricksActivity;
import com.logitech.ue.activities.XUPTutorialActivity;
import android.view.View$OnClickListener;
import android.support.v4.content.ContextCompat;
import com.logitech.ue.activities.MainActivity;
import java.util.Iterator;
import android.animation.AnimatorSet$Builder;
import com.logitech.ue.tasks.BeginBroadcastVolumeSyncTask;
import android.animation.TimeInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar$OnSeekBarChangeListener;
import butterknife.ButterKnife;
import com.logitech.ue.other.BlockPartyUtils;
import com.logitech.ue.tasks.SetDeviceBlockPartyStateTask;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import com.logitech.ue.UserPreferences;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import butterknife.OnClick;
import java.util.Locale;
import android.util.TypedValue;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import android.os.AsyncTask$Status;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastAudioMode;
import android.view.View$DragShadowBuilder;
import android.content.ClipData;
import com.logitech.ue.other.DeviceDragShadowBuilder;
import android.content.DialogInterface$OnCancelListener;
import android.content.DialogInterface$OnDismissListener;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastState;
import android.view.DragEvent;
import android.view.View$OnDragListener;
import com.logitech.ue.tasks.GetDeviceColorTask;
import com.logitech.ue.tasks.GetDeviceNameTask;
import android.view.View$OnTouchListener;
import android.view.MotionEvent;
import com.logitech.ue.LongPressGestureDetector;
import com.logitech.ue.tasks.SetDeviceBroadcastTask;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastConfiguration;
import com.logitech.ue.tasks.GetDeviceBroadcastTask;
import java.util.Random;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastReceiverStatus;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastReceiverInfo;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastAdvertisementInfo;
import com.logitech.ue.centurion.UEDeviceManager;
import android.bluetooth.BluetoothDevice;
import com.logitech.ue.UEColourHelper;
import com.logitech.ue.other.MD5Helper;
import com.logitech.ue.xup.XUPReceiverConnectionState;
import com.logitech.ue.xup.XUPReceiverStatus;
import android.graphics.Point;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.graphics.PointF;
import com.logitech.ue.Utils;
import android.animation.Animator;
import com.logitech.ue.utils.AnimationUtils;
import com.logitech.ue.other.OnGlobalLayoutSelfRemovingListener;
import android.os.Bundle;
import com.logitech.ue.tasks.AutostopBroadcastTask;
import com.logitech.ue.tasks.AutostartBroadcastTask;
import com.logitech.ue.centurion.device.devicedata.UEBlockPartyState;
import com.logitech.ue.centurion.notification.UEReceiverFixedAttributesNotification;
import com.logitech.ue.centurion.notification.UEReceiverRemovedNotification;
import com.logitech.ue.centurion.notification.UEReceiverAddedNotification;
import com.logitech.ue.centurion.notification.UEBroadcastStatusNotification;
import com.logitech.ue.centurion.device.devicedata.UEColour;
import com.logitech.ue.App;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;
import android.util.Log;
import android.content.Intent;
import android.content.Context;
import com.logitech.ue.other.NameCacher;
import android.os.SystemClock;
import com.logitech.ue.FlurryTracker;
import com.logitech.ue.centurion.device.command.UEDeviceCommand;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.Button;
import com.logitech.ue.other.BLENameFetcher;
import com.logitech.ue.xup.XUPMember;
import java.util.HashMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindDimen;
import com.logitech.ue.views.UEDeviceView;
import android.os.Handler;
import com.logitech.ue.views.SpiderFrameLayout;
import java.util.ArrayList;
import android.content.BroadcastReceiver;
import com.logitech.ue.BroadcastModeHelper;
import com.logitech.ue.other.BalanceUpdater;
import android.widget.SeekBar;
import android.view.View;
import butterknife.Bind;
import android.widget.TextView;
import com.logitech.ue.centurion.utils.MAC;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastAudioOptions;
import com.logitech.ue.interfaces.IPage;
import android.support.v4.app.Fragment;

public class XUpFragment extends Fragment implements IPage
{
    private static final int MEMBER_DISCONNECTION_TIMEOUT = 30000;
    private static final int MEMBER_RECHECK_TIME = 30000;
    private static final int SECURED_OR_OFF_RECEIVER_DIALOG_TIMEOUT = 60000;
    private static final String TAG;
    private static final int TRANSITION_TIME = 200;
    private static final int VIBRATION_TIME = 100;
    private static final int X_UP_SCANNING_HEARTBEAT_TIMER = 120000;
    public static boolean sWasSelected;
    volatile UEBroadcastAudioOptions mAudioOptions;
    MAC mAwaitingDeviceAddress;
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
    BroadcastModeHelper.ExecutionListener mBroadcastHelperListener;
    BroadcastModeHelper mBroadcastModeHelper;
    BroadcastReceiver mBroadcastReceiver;
    ArrayList<MAC> mConnectedMemberList;
    @Bind({ 2131624117 })
    SpiderFrameLayout mContainer;
    Handler mDialogTimeoutHandler;
    @Bind({ 2131624107 })
    TextView mDoubleUpButton;
    @Bind({ 2131624115 })
    View mDoubleUpPanel;
    UEDeviceView mDraggedView;
    DeviceDrawerAdapter mDrawerAdapter;
    @BindDimen(2131361924)
    float mDrawerHeight;
    ArrayList<MAC> mDrawerMemberList;
    @Bind({ 2131624198 })
    RecyclerView mDrawerRecyclerView;
    UEDeviceView mDroppedView;
    Handler mHearthBeatTimer;
    @Bind({ 2131624114 })
    TextView mHostNameCloud;
    boolean mIsVolumeSynced;
    boolean mIsXUPEnding;
    LinearLayoutManager mLayoutManager;
    @Bind({ 2131624118 })
    UEDeviceView mMainDeviceView;
    HashMap<MAC, XUPMember> mMemberInfoMap;
    BLENameFetcher mNameFetcher;
    BLENameFetcher.OnSuccessListener mNameFetcherListener;
    @Bind({ 2131624196 })
    TextView mSpeakersFoundLabel;
    @Bind({ 2131624120 })
    Button mStartButton;
    State mState;
    @Bind({ 2131624108 })
    TextView mStereoButton;
    @Bind({ 2131624116 })
    View mSwapSpeakers;
    @Bind({ 2131624197 })
    View mTutorialCloud;
    UpdateBalanceTask mUpdateBalanceTask;
    @Bind({ 2131624193 })
    View mVolumeSyncButton;
    @Bind({ 2131624194 })
    ImageView mVolumeSyncIcon;
    @Bind({ 2131624195 })
    TextView mVolumeSyncText;
    Handler recheckHandler;
    Vibrator vibrator;
    
    static {
        TAG = XUpFragment.class.getSimpleName();
        XUpFragment.sWasSelected = false;
    }
    
    public XUpFragment() {
        this.mState = State.Normal;
        this.mMemberInfoMap = new HashMap<MAC, XUPMember>();
        this.mConnectedMemberList = new ArrayList<MAC>();
        this.mDrawerMemberList = new ArrayList<MAC>();
        this.mBalanceUpdater = new BalanceUpdater();
        this.recheckHandler = new Handler();
        this.mHearthBeatTimer = new Handler();
        this.mBroadcastModeHelper = new BroadcastModeHelper();
        this.mNameFetcher = new BLENameFetcher();
        this.mAudioOptions = UEBroadcastAudioOptions.MULTIPLE;
        this.mIsVolumeSynced = false;
        this.mDialogTimeoutHandler = new Handler();
        this.mIsXUPEnding = false;
        this.mBroadcastHelperListener = new BroadcastModeHelper.ExecutionListener() {
            @Override
            public void onRequestSendFail(final Exception ex, final UEDeviceCommand.UECommand ueCommand, final MAC key, final Object[] array) {
                final XUPMember xupMember = XUpFragment.this.mMemberInfoMap.get(key);
                switch (ueCommand) {
                    case AddReceiverToBroadcast: {
                        if (xupMember != null) {
                            xupMember.setStatusIgnoreTime(0L);
                            XUpFragment.this.removeConnectedMember(xupMember.Address);
                            XUpFragment.this.addMemberToDrawer(key);
                            XUpFragment.this.updateTitle();
                            XUpFragment.this.updateDoubleUpControls();
                            XUpFragment.this.updateAboveDrawerLabel();
                            XUpFragment.this.recheckAndTransitContainerState();
                        }
                        FlurryTracker.logXUPError("Add receiver centurion fail");
                        break;
                    }
                    case RemoveReceiverFromBroadcast: {
                        if (xupMember != null) {
                            xupMember.setStatusIgnoreTime(0L);
                            XUpFragment.this.addConnectedMember(xupMember);
                            XUpFragment.this.removeDeviceFromDrawer(key);
                            XUpFragment.this.updateTitle();
                            XUpFragment.this.updateDoubleUpControls();
                            XUpFragment.this.updateAboveDrawerLabel();
                            XUpFragment.this.recheckAndTransitContainerState();
                        }
                        FlurryTracker.logXUPError("Remove receiver centurion fail");
                        break;
                    }
                    case QueryReceiverFixedAttributes: {
                        if (xupMember != null) {
                            xupMember.isFetchingName = false;
                        }
                        FlurryTracker.logXUPError("Get fixed attributes centurion fail");
                        break;
                    }
                }
            }
            
            @Override
            public void onRequestSendSuccess(final UEDeviceCommand.UECommand ueCommand, final MAC key, final Object[] array) {
                final XUPMember xupMember = XUpFragment.this.mMemberInfoMap.get(key);
                switch (ueCommand) {
                    case AddReceiverToBroadcast: {
                        if (xupMember != null) {
                            xupMember.setStatusIgnoreTime(SystemClock.elapsedRealtime());
                            break;
                        }
                        break;
                    }
                    case RemoveReceiverFromBroadcast: {
                        if (xupMember != null) {
                            xupMember.setStatusIgnoreTime(SystemClock.elapsedRealtime());
                            break;
                        }
                        break;
                    }
                }
            }
            
            @Override
            public void onRequestTimeout(final UEDeviceCommand.UECommand ueCommand, final MAC key, final Object[] array) {
                final XUPMember xupMember = XUpFragment.this.mMemberInfoMap.get(key);
                switch (ueCommand) {
                    case AddReceiverToBroadcast: {
                        if (xupMember != null) {
                            xupMember.setStatusIgnoreTime(0L);
                            XUpFragment.this.removeConnectedMember(xupMember.Address);
                            XUpFragment.this.addMemberToDrawer(key);
                            XUpFragment.this.updateAboveDrawerLabel();
                        }
                        FlurryTracker.logXUPError("Add receiver timeout");
                        break;
                    }
                    case RemoveReceiverFromBroadcast: {
                        FlurryTracker.logXUPError("Remove receiver timeout");
                        break;
                    }
                    case QueryReceiverFixedAttributes: {
                        if (xupMember != null) {
                            xupMember.isFetchingName = false;
                        }
                        FlurryTracker.logXUPError("Get fixed attributes timeout");
                        break;
                    }
                }
            }
        };
        this.mNameFetcherListener = new BLENameFetcher.OnSuccessListener() {
            @Override
            public void onError(final MAC key, final int n, final Exception ex) {
                final XUPMember xupMember = XUpFragment.this.mMemberInfoMap.get(key);
                if (xupMember != null) {
                    xupMember.isFetchingName = false;
                }
                FlurryTracker.logXUPError("Get name via BLE fail");
            }
            
            @Override
            public void onSuccess(final MAC key, final int n, final String name) {
                boolean isNameFresh = false;
                final XUPMember xupMember = XUpFragment.this.mMemberInfoMap.get(key);
                if (xupMember != null) {
                    xupMember.isFetchingName = false;
                    xupMember.name = name;
                    if (xupMember.nameRevision == n) {
                        isNameFresh = true;
                    }
                    xupMember.isNameFresh = isNameFresh;
                    NameCacher.saveDeviceName(xupMember.Address, xupMember.name, xupMember.nameRevision);
                }
            }
        };
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                Log.d(XUpFragment.TAG, "Receive broadcast " + intent.getAction());
                if (XUpFragment.this.getView() != null) {
                    if (intent.getAction().equals("com.logitech.ue.centurion.CONNECTION_CHANGED")) {
                        if (UEDeviceStatus.getStatus(intent.getExtras().getInt("status")).isBtClassicConnectedState()) {
                            if (App.getDeviceConnectionState().isBtClassicConnectedState()) {
                                XUpFragment.this.beginMasterColorUpdate();
                            }
                        }
                        else {
                            XUpFragment.this.mBroadcastModeHelper.stop();
                            XUpFragment.this.mMainDeviceView.setDeviceColor(UEColour.NO_SPEAKER.getCode());
                        }
                        XUpFragment.this.updateUI();
                    }
                    else if (intent.getAction().equals("com.logitech.ue.centurion.ACTION_BROADCAST_STATUS_NOTIFICATION")) {
                        XUpFragment.this.processBroadcastStatusNotification((UEBroadcastStatusNotification)intent.getParcelableExtra("notification"));
                    }
                    else if (intent.getAction().equals("com.logitech.ue.centurion.ACTION_RECEIVER_ADDED_NOTIFICATION")) {
                        XUpFragment.this.processAddReceiverNotification((UEReceiverAddedNotification)intent.getParcelableExtra("notification"));
                    }
                    else if (intent.getAction().equals("com.logitech.ue.centurion.ACTION_RECEIVER_REMOVED_NOTIFICATION")) {
                        XUpFragment.this.processRemoveReceiverNotification((UEReceiverRemovedNotification)intent.getParcelableExtra("notification"));
                    }
                    else if (intent.getAction().equals("com.logitech.ue.centurion.ACTION_RECEIVER_FIXED_ATTRIBUTES_NOTIFICATION")) {
                        XUpFragment.this.processFixedAttributeNotification((UEReceiverFixedAttributesNotification)intent.getParcelableExtra("notification"));
                    }
                    else if (!intent.getAction().equals("com.logitech.ue.bluetooth.le.ACTION_BLE_PACKAGE_RECEIVED") && intent.getAction().equals("com.logitech.ue.fragments.PARTY_MODE_STATE_CHANGED")) {
                        XUpFragment.this.processBlockPartyStatusChanged(UEBlockPartyState.getState(intent.getExtras().getInt("state")), intent.getExtras().getString("source"));
                    }
                }
            }
        };
    }
    
    private void autoStartXUP() {
        ((SafeTask<Void, Progress, Result>)new AutostartBroadcastTask() {
            public void onSuccess(final Void void1) {
                super.onSuccess((T)void1);
            }
        }).executeOnThreadPoolExecutor(new Void[0]);
        this.startXUPHeartBeatTimer();
    }
    
    private void autoStopXUP() {
        ((SafeTask<Void, Progress, Result>)new AutostopBroadcastTask() {
            public void onSuccess(final Void void1) {
                super.onSuccess((T)void1);
            }
        }).executeOnThreadPoolExecutor(new Void[0]);
        this.stopXUPHeartBeatTimer();
    }
    
    private UEDeviceView.DisplayMode getMasterDisplayMode(final SpiderFrameLayout.DisplayMode displayMode) {
        UEDeviceView.DisplayMode displayMode2 = null;
        switch (displayMode) {
            default: {
                displayMode2 = UEDeviceView.DisplayMode.MODE_NORMAL;
                break;
            }
            case Solo: {
                displayMode2 = UEDeviceView.DisplayMode.MODE_NORMAL;
                break;
            }
            case DoubleUP: {
                displayMode2 = UEDeviceView.DisplayMode.MODE_NORMAL;
                break;
            }
            case MegaUP: {
                displayMode2 = UEDeviceView.DisplayMode.MODE_NORMAL;
                break;
            }
            case MonsterUP: {
                displayMode2 = UEDeviceView.DisplayMode.MODE_NORMAL;
                break;
            }
        }
        return displayMode2;
    }
    
    private UEDeviceView.DisplayMode getSlaveDisplayMode(final SpiderFrameLayout.DisplayMode displayMode) {
        UEDeviceView.DisplayMode displayMode2 = null;
        switch (displayMode) {
            default: {
                displayMode2 = UEDeviceView.DisplayMode.MODE_DOT;
                break;
            }
            case DoubleUP: {
                displayMode2 = UEDeviceView.DisplayMode.MODE_NORMAL;
                break;
            }
            case MegaUP: {
                displayMode2 = UEDeviceView.DisplayMode.MODE_DOT;
                break;
            }
            case MonsterUP: {
                displayMode2 = UEDeviceView.DisplayMode.MODE_DOT;
                break;
            }
        }
        return displayMode2;
    }
    
    private State getState() {
        return this.mState;
    }
    
    private UEDeviceView getViewByMember(final MAC mac) {
        for (int i = 0; i < this.mContainer.getChildCount(); ++i) {
            if (mac.equals(this.mContainer.getChildAt(i).getTag())) {
                return (UEDeviceView)this.mContainer.getChildAt(i);
            }
        }
        return null;
    }
    
    private void hideDoubleUpControls() {
        Log.d(XUpFragment.TAG, "Hide DoubleUP controls");
        this.mDoubleUpPanel.setVisibility(8);
        this.mBalancePanel.setVisibility(8);
        this.mSwapSpeakers.setVisibility(8);
        this.mBalanceSeekBar.setProgress(this.mBalanceSeekBar.getMax() / 2);
    }
    
    public static XUpFragment newInstance() {
        final XUpFragment xUpFragment = new XUpFragment();
        xUpFragment.setArguments(new Bundle());
        return xUpFragment;
    }
    
    private void playContainerTransition(final SpiderFrameLayout.DisplayMode displayMode) {
        this.mContainer.setDisplayMode(displayMode);
        this.mContainer.forceLayout();
        this.mContainer.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new OnGlobalLayoutSelfRemovingListener(this.mContainer) {
            @Override
            public void onHandleGlobalLayout() {
                if (displayMode == XUpFragment.this.recheckContainerState() && XUpFragment.this.getView() != null) {
                    for (int i = 0; i < XUpFragment.this.mContainer.getChildCount(); ++i) {
                        final UEDeviceView ueDeviceView = (UEDeviceView)XUpFragment.this.mContainer.getChildAt(i);
                        final Animator animator = AnimationUtils.getAnimator((View)ueDeviceView);
                        final Animator animator2 = (Animator)ueDeviceView.getTag(2131623944);
                        if (displayMode == SpiderFrameLayout.DisplayMode.MegaUP || displayMode == SpiderFrameLayout.DisplayMode.MonsterUP) {
                            if (animator2 == null && (animator == null || !animator.isRunning())) {
                                XUpFragment.this.startPulseAnimation((View)ueDeviceView);
                            }
                        }
                        else {
                            XUpFragment.this.stopPulseAnimation((View)ueDeviceView);
                        }
                    }
                    Log.d(XUpFragment.TAG, "Play container Transition animation. DisplayMode: " + displayMode.name());
                    final UEDeviceView.DisplayMode access$600 = XUpFragment.this.getMasterDisplayMode(displayMode);
                    int n;
                    if ((n = XUpFragment.this.mContainer.getMasterSpot()) == -1 && (n = XUpFragment.this.mContainer.getSpot((View)XUpFragment.this.mMainDeviceView)) == -1) {
                        if (XUpFragment.this.mAudioOptions != UEBroadcastAudioOptions.MULTIPLE) {
                            if (XUpFragment.this.mAudioOptions == UEBroadcastAudioOptions.STEREO_LEFT) {
                                n = 0;
                            }
                            else {
                                n = 1;
                            }
                        }
                        else {
                            n = XUpFragment.this.mContainer.getClosestSpot(Utils.getViewCenterX((View)XUpFragment.this.mMainDeviceView), Utils.getViewCenterY((View)XUpFragment.this.mMainDeviceView));
                        }
                    }
                    XUpFragment.this.mContainer.reserveSpot(n, (View)XUpFragment.this.mMainDeviceView);
                    final PointF spotPosition = XUpFragment.this.mContainer.getSpotPosition(n, (View)XUpFragment.this.mMainDeviceView);
                    XUpFragment.this.playSpeakerMoveAndRescaleAnimation(XUpFragment.this.mMainDeviceView, access$600, spotPosition.x, spotPosition.y);
                    final UEDeviceView.DisplayMode access$601 = XUpFragment.this.getSlaveDisplayMode(displayMode);
                    for (int j = 0; j < XUpFragment.this.mContainer.getChildCount(); ++j) {
                        final UEDeviceView ueDeviceView2 = (UEDeviceView)XUpFragment.this.mContainer.getChildAt(j);
                        if (ueDeviceView2 != XUpFragment.this.mMainDeviceView && ueDeviceView2 != XUpFragment.this.mDraggedView && ueDeviceView2 != XUpFragment.this.mDroppedView) {
                            int n2;
                            if ((n2 = XUpFragment.this.mContainer.getSpot((View)ueDeviceView2)) == -1) {
                                n2 = XUpFragment.this.mContainer.getClosestFreeSpot(Utils.getViewCenterX((View)ueDeviceView2), Utils.getViewCenterY((View)ueDeviceView2));
                            }
                            XUpFragment.this.mContainer.reserveSpot(n2, (View)ueDeviceView2);
                            final PointF spotPosition2 = XUpFragment.this.mContainer.getSpotPosition(n2, (View)ueDeviceView2);
                            XUpFragment.this.playSpeakerMoveAndRescaleAnimation(ueDeviceView2, access$601, spotPosition2.x, spotPosition2.y);
                        }
                    }
                }
            }
        });
    }
    
    private void playDropAnimation(final UEDeviceView ueDeviceView, final float n, final float n2) {
        Log.d(XUpFragment.TAG, "Play drop device animation. Container display mode is " + this.mContainer.getDisplayMode().name());
        final UEDeviceView.DisplayMode slaveDisplayMode = this.getSlaveDisplayMode(this.mContainer.getDisplayMode());
        final int closestFreeSpot = this.mContainer.getClosestFreeSpot(n, n2);
        this.mContainer.reserveSpot(closestFreeSpot, (View)ueDeviceView);
        final PointF spotPosition = this.mContainer.getSpotPosition(closestFreeSpot, (View)ueDeviceView);
        this.playSpeakerMoveAndRescaleAnimation(ueDeviceView, slaveDisplayMode, spotPosition.x, spotPosition.y);
    }
    
    private void playDropAnimation(final UEDeviceView ueDeviceView, final int n) {
        Log.d(XUpFragment.TAG, "Play drop device animation. Container display mode is " + this.mContainer.getDisplayMode().name());
        final UEDeviceView.DisplayMode slaveDisplayMode = this.getSlaveDisplayMode(this.mContainer.getDisplayMode());
        this.mContainer.reserveSpot(n, (View)ueDeviceView);
        final PointF spotPosition = this.mContainer.getSpotPosition(n, (View)ueDeviceView);
        this.playSpeakerMoveAndRescaleAnimation(ueDeviceView, slaveDisplayMode, spotPosition.x, spotPosition.y);
    }
    
    private void playFreeUpSpot(final int n) {
        final UEDeviceView ueDeviceView = (UEDeviceView)this.mContainer.getViewBySpot(n);
        if (ueDeviceView != null) {
            final PointF spotPosition = this.mContainer.getSpotPosition(n);
            final int closestFreeSpot = this.mContainer.getClosestFreeSpot(spotPosition.x, spotPosition.y);
            this.playFreeUpSpot(closestFreeSpot);
            this.mContainer.releaseSpot(n);
            final PointF spotPosition2 = this.mContainer.getSpotPosition(closestFreeSpot, (View)ueDeviceView);
            this.mContainer.reserveSpot(closestFreeSpot, (View)ueDeviceView);
            this.playSpeakerMoveAndRescaleAnimation(ueDeviceView, ueDeviceView.getMode(), spotPosition2.x, spotPosition2.y);
        }
    }
    
    private boolean playSpeakerMoveAndRescaleAnimation(final UEDeviceView ueDeviceView, final UEDeviceView.DisplayMode displayMode, final float n, final float n2) {
        final Point deviceDimensions = UEDeviceView.getDeviceDimensions(displayMode, ueDeviceView.getDeviceColor(), this.getContext());
        final float n3 = 1.0f * deviceDimensions.x / ueDeviceView.getWidth();
        final float n4 = 1.0f * deviceDimensions.y / ueDeviceView.getHeight();
        boolean b;
        if (!Utils.equalsFloat(ueDeviceView.getX(), n) || !Utils.equalsFloat(ueDeviceView.getY(), n2) || !Utils.equalsFloat(ueDeviceView.getScaleX(), n3) || !Utils.equalsFloat(ueDeviceView.getScaleY(), n4) || ueDeviceView.getMode() != displayMode) {
            Log.d(XUpFragment.TAG, String.format("Play move and scale animation for view %s. OLD dm:%s W:%.2f H:%.2f X:%.2f Y:%.2f Sx:%.2f Sy:%.2f NEW dm:%s W:%d H:%d X:%.2f Y:%.2f Sx:%.2f Sy:%.2f", ueDeviceView.getTag(), ueDeviceView.getMode(), ueDeviceView.getMaxWidth(), ueDeviceView.getMaxHeight(), ueDeviceView.getX(), ueDeviceView.getY(), ueDeviceView.getScaleX(), ueDeviceView.getScaleY(), displayMode.name(), deviceDimensions.x, deviceDimensions.y, n, n2, n3, n4));
            final Animator animator = AnimationUtils.getAnimator((View)ueDeviceView);
            if (animator != null && animator instanceof AnimatorSet) {
                final AnimatorSet set = (AnimatorSet)animator;
                final Object[] animatorValue = AnimationUtils.getAnimatorValue((View)ueDeviceView);
                if (animatorValue instanceof Float[]) {
                    final Float[] array = (Float[])animatorValue;
                    if (Utils.equalsFloat(array[0], array[0]) && Utils.equalsFloat(array[1], array[1]) && Utils.equalsFloat(array[2], array[2]) && Utils.equalsFloat(array[3], array[3])) {
                        b = false;
                        return b;
                    }
                    set.cancel();
                }
            }
            this.stopPulseAnimation((View)ueDeviceView);
            final AnimatorSet set2 = new AnimatorSet();
            set2.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)ueDeviceView, View.X, new float[] { n }), (Animator)ObjectAnimator.ofFloat((Object)ueDeviceView, View.Y, new float[] { n2 }), (Animator)ObjectAnimator.ofFloat((Object)ueDeviceView, View.SCALE_X, new float[] { n3 }), (Animator)ObjectAnimator.ofFloat((Object)ueDeviceView, View.SCALE_Y, new float[] { n4 }) });
            set2.setDuration(200L);
            set2.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                boolean isCanceled = false;
                
                public void onAnimationCancel(final Animator animator) {
                    super.onAnimationCancel(animator);
                    this.isCanceled = true;
                }
                
                public void onAnimationEnd(final Animator animator) {
                    super.onAnimationEnd(animator);
                    AnimationUtils.detachAnimatorFromView((View)ueDeviceView, animator);
                    if (!this.isCanceled) {
                        XUpFragment.this.resetViewScaleAndAdjustBounds(ueDeviceView, displayMode);
                    }
                }
            });
            AnimationUtils.attacheAnimatorToView((View)ueDeviceView, (Animator)set2, new Float[] { n, n2, n3, n4 });
            set2.start();
            b = true;
        }
        else {
            Log.d(XUpFragment.TAG, String.format("Skip play move and scale animation for view %s", ueDeviceView.getTag()));
            b = false;
        }
        return b;
    }
    
    private void processAddReceiverNotification(final UEReceiverAddedNotification ueReceiverAddedNotification) {
        Log.d(XUpFragment.TAG, String.format("ACK Member add to broadcast. Address: %s ExecutionStatus: %s", ueReceiverAddedNotification.getAddress(), ueReceiverAddedNotification.getExecutionStatus()));
        final XUPMember xupMember = this.mMemberInfoMap.get(ueReceiverAddedNotification.getAddress());
        if (xupMember != null) {
            xupMember.setStatusIgnoreTime(0L);
            if (ueReceiverAddedNotification.getExecutionStatus() == UEReceiverAddedNotification.ExecutionStatus.DOUBLE_PRESS_REQUIRED) {
                xupMember.status = XUPReceiverStatus.SECURED;
                this.showSecuredDeviceDialog(xupMember.Address);
            }
            else if (ueReceiverAddedNotification.getExecutionStatus() == UEReceiverAddedNotification.ExecutionStatus.POWER_ON_REQUIRED) {
                xupMember.status = XUPReceiverStatus.POWER_OFF;
                this.showPowerOffDeviceDialog(xupMember.Address);
            }
            else if (ueReceiverAddedNotification.getExecutionStatus() == UEReceiverAddedNotification.ExecutionStatus.DELIVERED) {
                xupMember.connectionState = XUPReceiverConnectionState.CONNECTED;
                this.updateUI();
                FlurryTracker.logXUPReceiverJoin(com.logitech.ue.util.Utils.byteArrayToHexString(MD5Helper.md5(xupMember.Address)), xupMember.deviceColor, UEColourHelper.getDeviceTypeByColour(xupMember.deviceColor).name(), true);
            }
            else {
                xupMember.connectionState = XUPReceiverConnectionState.DISCONNECTED;
                this.removeConnectedMember(xupMember.Address);
                this.recheckAndTransitContainerState();
                this.updateUI();
                FlurryTracker.logXUPError("Add receiver delivery fail");
            }
        }
    }
    
    private void processBLEReceiverPackage(final BluetoothDevice bluetoothDevice, final byte[] array) {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        if (connectedDevice != null && !bluetoothDevice.getAddress().equals(connectedDevice.getAddress().toString()) && UEBroadcastAdvertisementInfo.isValidatePackage(array)) {
            final UEBroadcastAdvertisementInfo ueBroadcastAdvertisementInfo = new UEBroadcastAdvertisementInfo(array);
            Log.d(XUpFragment.TAG, "Process BLE package. Address: " + bluetoothDevice.getAddress());
            this.processReceiversInfo(true, ueBroadcastAdvertisementInfo.buildBroadcastReceiverInfo(new MAC(bluetoothDevice.getAddress()), connectedDevice.getAddress()));
        }
    }
    
    private void processBlockPartyStatusChanged(final UEBlockPartyState ueBlockPartyState, final String s) {
        Log.d(XUpFragment.TAG, String.format("Process Block Party status changed. Status:%s Source: %s", ueBlockPartyState, s));
        if (ueBlockPartyState == UEBlockPartyState.OFF) {
            this.setState(State.Normal);
        }
        else {
            this.setState(State.Blocked);
        }
        this.updateUI();
        this.clearXUPData();
    }
    
    private void processBroadcastStatusNotification(final UEBroadcastStatusNotification ueBroadcastStatusNotification) {
        Log.d(XUpFragment.TAG, String.format("Broadcast status notification. Receivers count: %d", ueBroadcastStatusNotification.getReceiversList().size()));
        this.processReceiversInfo(false, (UEBroadcastReceiverInfo[])ueBroadcastStatusNotification.getReceiversList().toArray(new UEBroadcastReceiverInfo[ueBroadcastStatusNotification.getReceiversList().size()]));
    }
    
    private void processFixedAttributeNotification(final UEReceiverFixedAttributesNotification ueReceiverFixedAttributesNotification) {
        Log.d(XUpFragment.TAG, String.format("ACK Got fixed attributes. Address: %s Delivered: %b", ueReceiverFixedAttributesNotification.getAddress(), ueReceiverFixedAttributesNotification.isCommandDelivered()));
        final XUPMember xupMember = this.mMemberInfoMap.get(ueReceiverFixedAttributesNotification.getAddress());
        if (xupMember != null) {
            if (ueReceiverFixedAttributesNotification.isCommandDelivered()) {
                xupMember.name = ueReceiverFixedAttributesNotification.getDeviceName();
                xupMember.isNameFresh = true;
                NameCacher.saveDeviceName(xupMember.Address, xupMember.name, xupMember.nameRevision);
            }
            else {
                FlurryTracker.logXUPError("Get fixed attributes delivery fail");
            }
            xupMember.isFetchingName = false;
        }
    }
    
    private void processReceiversInfo(final boolean b, final UEBroadcastReceiverInfo... array) {
        Log.d(XUpFragment.TAG, "Processing receivers info. Count: " + array.length);
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        for (final UEBroadcastReceiverInfo ueBroadcastReceiverInfo : array) {
            if (ueBroadcastReceiverInfo.mStatus != UEBroadcastReceiverStatus.DOESNT_SUPPORT_XUP && ueBroadcastReceiverInfo.mStatus != UEBroadcastReceiverStatus.UNKNOWN) {
                XUPMember value;
                if (this.mMemberInfoMap.containsKey(ueBroadcastReceiverInfo.mAddress)) {
                    value = this.mMemberInfoMap.get(ueBroadcastReceiverInfo.mAddress);
                    Log.d(XUpFragment.TAG, String.format("Update member. Name: %s Color: 0x%02X X-UP Status: %s Address: %s", value.name, ueBroadcastReceiverInfo.mDeviceColor, ueBroadcastReceiverInfo.mStatus, ueBroadcastReceiverInfo.mAddress));
                    value.update(ueBroadcastReceiverInfo);
                    value.lastTimeSeen = elapsedRealtime;
                }
                else {
                    value = new XUPMember(ueBroadcastReceiverInfo);
                    final NameCacher.NameProvision checkDeviceNameProvision = NameCacher.checkDeviceNameProvision(ueBroadcastReceiverInfo.mAddress);
                    if (checkDeviceNameProvision != null) {
                        value.name = checkDeviceNameProvision.name;
                        value.isNameFresh = (checkDeviceNameProvision.revision == value.nameRevision);
                    }
                    Log.d(XUpFragment.TAG, String.format("Add new member. Name: %s Color: 0x%02X X-UP Status: %s  Address: %s", value.name, ueBroadcastReceiverInfo.mDeviceColor, ueBroadcastReceiverInfo.mStatus, ueBroadcastReceiverInfo.mAddress));
                    this.mMemberInfoMap.put(ueBroadcastReceiverInfo.mAddress, value);
                    value.lastTimeSeen = elapsedRealtime;
                    FlurryTracker.logXUPReceiverDiscovery(com.logitech.ue.util.Utils.byteArrayToHexString(MD5Helper.md5(value.Address)));
                }
                if (b) {
                    value.isFoundByBLE = true;
                }
            }
            else {
                Log.d(XUpFragment.TAG, String.format("Receiver is doesn't support X-UP. Address: %s", ueBroadcastReceiverInfo.mAddress));
            }
        }
        if (this.getState() != State.Transitioning) {
            this.recheckAllMember();
            this.updateUI();
        }
    }
    
    private void processRemoveReceiverNotification(final UEReceiverRemovedNotification ueReceiverRemovedNotification) {
        Log.d(XUpFragment.TAG, String.format("ACK Member remove from broadcast. Address: %s Delivered: %b", ueReceiverRemovedNotification.getAddress(), ueReceiverRemovedNotification.isCommandDelivered()));
        final XUPMember xupMember = this.mMemberInfoMap.get(ueReceiverRemovedNotification.getAddress());
        if (xupMember != null) {
            xupMember.setStatusIgnoreTime(0L);
            if (ueReceiverRemovedNotification.isCommandDelivered()) {
                xupMember.connectionState = XUPReceiverConnectionState.DISCONNECTED;
                FlurryTracker.logXUPReceiverLeft(com.logitech.ue.util.Utils.byteArrayToHexString(MD5Helper.md5(xupMember.Address)));
            }
            else {
                xupMember.connectionState = XUPReceiverConnectionState.CONNECTED;
                this.removeDeviceFromDrawer(xupMember.Address);
                this.addConnectedMember(xupMember);
                this.recheckAndTransitContainerState();
                this.updateUI();
                FlurryTracker.logXUPError("Remove receiver delivery fail");
            }
        }
    }
    
    private void resetViewScaleAndAdjustBounds(final UEDeviceView ueDeviceView, final UEDeviceView.DisplayMode mode) {
        if (ueDeviceView.getMode() != mode || !Utils.equalsFloat(ueDeviceView.getScaleX(), 1.0f) || !Utils.equalsFloat(ueDeviceView.getScaleY(), 1.0f)) {
            final float viewCenterX = Utils.getViewCenterX((View)ueDeviceView);
            final float viewCenterY = Utils.getViewCenterY((View)ueDeviceView);
            ueDeviceView.setMode(mode);
            ueDeviceView.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new OnGlobalLayoutSelfRemovingListener(ueDeviceView) {
                @Override
                public void onHandleGlobalLayout() {
                    if (ueDeviceView.getMode() == mode) {
                        Utils.setViewCenterX((View)ueDeviceView, viewCenterX);
                        Utils.setViewCenterY((View)ueDeviceView, viewCenterY);
                        ueDeviceView.setScaleX(1.0f);
                        ueDeviceView.setScaleY(1.0f);
                    }
                }
            });
        }
    }
    
    private void setState(final State mState) {
        if (mState != this.mState) {
            Log.d(XUpFragment.TAG, String.format("Change state from %s to %s", this.mState, mState));
            this.mState = mState;
        }
    }
    
    private void showDoubleUpControls() {
        Log.d(XUpFragment.TAG, "Show DoubleUP controls");
        this.mDoubleUpPanel.setVisibility(0);
        if (this.mAudioOptions != UEBroadcastAudioOptions.MULTIPLE) {
            this.mStereoButton.setSelected(true);
            this.mDoubleUpButton.setSelected(false);
            this.mBalancePanel.setVisibility(0);
            this.mSwapSpeakers.setVisibility(0);
        }
        else {
            this.mStereoButton.setSelected(false);
            this.mDoubleUpButton.setSelected(true);
            this.mBalancePanel.setVisibility(8);
            this.mSwapSpeakers.setVisibility(8);
            this.mBalanceSeekBar.setProgress(this.mBalanceSeekBar.getMax() / 2);
        }
    }
    
    private void startPulseAnimation(final View view) {
        final Random random = new Random();
        final int androidLongAnimationTime = AnimationUtils.getAndroidLongAnimationTime(this.getContext());
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)view, View.SCALE_X, new float[] { 1.0f, 1.1f, 1.0f, 1.1f, 1.0f });
        ofFloat.setRepeatCount(-1);
        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)view, View.SCALE_Y, new float[] { 1.0f, 1.1f, 1.0f, 1.1f, 1.0f });
        ofFloat2.setRepeatCount(-1);
        final AnimatorSet set = new AnimatorSet();
        set.setStartDelay((long)random.nextInt(androidLongAnimationTime / 2));
        set.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ofFloat2 });
        set.setDuration((long)(androidLongAnimationTime * 2));
        set.start();
        view.setTag(2131623944, (Object)set);
    }
    
    private void startXUPHeartBeatTimer() {
        this.stopXUPHeartBeatTimer();
        this.mHearthBeatTimer.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                ((SafeTask<Void, Progress, Result>)new GetDeviceBroadcastTask() {
                    public void onSuccess(final UEBroadcastConfiguration ueBroadcastConfiguration) {
                        super.onSuccess((T)ueBroadcastConfiguration);
                        ((SafeTask<Void, Progress, Result>)new SetDeviceBroadcastTask(ueBroadcastConfiguration.getBroadcastState(), ueBroadcastConfiguration.getAudioSetting())).executeOnThreadPoolExecutor(new Void[0]);
                    }
                }).executeOnThreadPoolExecutor(new Void[0]);
                XUpFragment.this.mHearthBeatTimer.postDelayed((Runnable)this, 120000L);
            }
        }, 120000L);
    }
    
    private void stopPulseAnimation(final View view) {
        final Animator animator = (Animator)view.getTag(2131623944);
        if (animator != null) {
            animator.cancel();
            view.setTag(2131623944, (Object)null);
        }
    }
    
    private void stopXUPHeartBeatTimer() {
        this.mHearthBeatTimer.removeCallbacksAndMessages((Object)null);
    }
    
    private void updateAboveDrawerLabel() {
        if (this.getState() != State.Blocked) {
            if (this.mContainer.getChildCount() == 1 && this.mDrawerMemberList.size() == 0) {
                Log.d(XUpFragment.TAG, "Show Tutorial cloud and hide found speakers label");
                this.mTutorialCloud.setVisibility(0);
                this.mSpeakersFoundLabel.setVisibility(8);
            }
            else {
                Log.d(XUpFragment.TAG, "Hide Tutorial cloud and show found speakers label");
                this.mTutorialCloud.setVisibility(8);
                this.mSpeakersFoundLabel.setVisibility(0);
                this.mSpeakersFoundLabel.setText((CharSequence)this.getString(2131165412, this.mDrawerMemberList.size()));
            }
        }
        else {
            Log.d(XUpFragment.TAG, "Hide Tutorial cloud and found speakers label");
            this.mTutorialCloud.setVisibility(8);
            this.mSpeakersFoundLabel.setVisibility(8);
        }
    }
    
    private void updateDoubleUpControls() {
        if (this.isInDoubleUpState() || this.getState() == State.Blocked) {
            if (this.isInPerfectMarriageState()) {
                this.showDoubleUpControls();
            }
            else {
                this.hideDoubleUpControls();
            }
        }
        else {
            this.hideDoubleUpControls();
        }
    }
    
    public UEDeviceView addConnectedMember(final XUPMember xupMember) {
        UEDeviceView viewByMember = this.getViewByMember(xupMember.Address);
        if (viewByMember == null) {
            if (this.mDraggedView == null || this.mDraggedView.getTag() == null || !this.mDraggedView.getTag().equals(xupMember.Address)) {
                Log.d(XUpFragment.TAG, "Add device view. Address: " + xupMember.Address);
                viewByMember = new UEDeviceView(this.getContext());
                viewByMember.setDeviceColor(xupMember.deviceColor);
                viewByMember.setMode(this.getSlaveDisplayMode(this.mContainer.getDisplayMode()));
                viewByMember.setTag((Object)xupMember.Address);
                viewByMember.setOnTouchListener((View$OnTouchListener)new LongPressGestureDetector() {
                    @Override
                    public void onLongPress(final View view, final MotionEvent motionEvent) {
                        XUpFragment.this.beginMemberDragFromContainer(xupMember, (UEDeviceView)view);
                    }
                });
                this.mContainer.addView((View)viewByMember);
                viewByMember.setX((float)(this.mContainer.getWidth() / 2));
                viewByMember.setY((float)(this.mContainer.getHeight() / 2));
            }
            else {
                Log.d(XUpFragment.TAG, "Device view is dragged, not add it to container. Address: " + xupMember.Address);
            }
        }
        else {
            Log.d(XUpFragment.TAG, "Device view is already was added. Address: " + xupMember.Address);
        }
        if (this.mConnectedMemberList.indexOf(xupMember.Address) == -1) {
            this.mConnectedMemberList.add(xupMember.Address);
        }
        return viewByMember;
    }
    
    public void addMemberToDrawer(final MAC mac) {
        if (this.mDrawerMemberList.indexOf(mac) == -1) {
            this.mDrawerMemberList.add(mac);
            ((RecyclerView.Adapter)this.mDrawerAdapter).notifyItemInserted(this.mDrawerMemberList.size());
        }
    }
    
    public void beginAudioSettingUpdate() {
        ((SafeTask<Void, Progress, Result>)new GetDeviceBroadcastTask() {
            public void onSuccess(final UEBroadcastConfiguration ueBroadcastConfiguration) {
                super.onSuccess((T)ueBroadcastConfiguration);
                XUpFragment.this.mAudioOptions = ueBroadcastConfiguration.getAudioSetting();
            }
        }).executeOnThreadPoolExecutor(new Void[0]);
    }
    
    public void beginHostNameUpdate() {
        ((SafeTask<Void, Progress, Result>)new GetDeviceNameTask() {
            public void onSuccess(final String text) {
                super.onSuccess((T)text);
                XUpFragment.this.mHostNameCloud.setText((CharSequence)text);
            }
        }).executeOnThreadPoolExecutor(new Void[0]);
    }
    
    public void beginMasterColorUpdate() {
        ((SafeTask<Void, Progress, Result>)new GetDeviceColorTask() {
            @Override
            public void onError(final Exception ex) {
                super.onError(ex);
                XUpFragment.this.mMainDeviceView.setDeviceColor(UEColour.NO_SPEAKER.getCode());
                XUpFragment.this.mContainer.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new OnGlobalLayoutSelfRemovingListener(XUpFragment.this.mContainer) {
                    @Override
                    public void onHandleGlobalLayout() {
                        final PointF spotPosition = XUpFragment.this.mContainer.getSpotPosition(XUpFragment.this.mContainer.getSpot((View)XUpFragment.this.mMainDeviceView), (View)XUpFragment.this.mMainDeviceView);
                        XUpFragment.this.mMainDeviceView.setX(spotPosition.x);
                        XUpFragment.this.mMainDeviceView.setY(spotPosition.y);
                    }
                });
            }
            
            public void onSuccess(final Integer n) {
                super.onSuccess((T)n);
                XUpFragment.this.mMainDeviceView.setDeviceColor(n);
                XUpFragment.this.mContainer.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new OnGlobalLayoutSelfRemovingListener(XUpFragment.this.mContainer) {
                    @Override
                    public void onHandleGlobalLayout() {
                        final PointF spotPosition = XUpFragment.this.mContainer.getSpotPosition(XUpFragment.this.mContainer.getSpot((View)XUpFragment.this.mMainDeviceView), (View)XUpFragment.this.mMainDeviceView);
                        XUpFragment.this.mMainDeviceView.setX(spotPosition.x);
                        XUpFragment.this.mMainDeviceView.setY(spotPosition.y);
                    }
                });
            }
        }).executeOnThreadPoolExecutor(new Void[0]);
    }
    
    public void beginMasterDrag() {
        Log.d(XUpFragment.TAG, "Begin master drag");
        this.doShortVibration();
        this.mContainer.setOnDragListener((View$OnDragListener)new View$OnDragListener() {
            final /* synthetic */ Point val$deviceRenderSize = UEDeviceView.getDeviceDimensions(UEDeviceView.DisplayMode.MODE_SMALL, XUpFragment.this.mMainDeviceView.getDeviceColor(), this.getContext());
            
            public boolean onDrag(final View view, final DragEvent dragEvent) {
                int n = 0;
                switch (dragEvent.getAction()) {
                    case 1: {
                        Log.d(XUpFragment.TAG, "Drag Master. Container: STARTED.");
                        XUpFragment.this.mIsXUPEnding = false;
                        XUpFragment.this.mMainDeviceView.setVisibility(8);
                        break;
                    }
                    case 2: {
                        Utils.setViewCenterX((View)XUpFragment.this.mMainDeviceView, dragEvent.getX());
                        Utils.setViewCenterY((View)XUpFragment.this.mMainDeviceView, dragEvent.getY());
                        if (XUpFragment.this.mContainer.getDisplayMode() != SpiderFrameLayout.DisplayMode.DoubleUP) {
                            break;
                        }
                        final int closestSpot = XUpFragment.this.mContainer.getClosestSpot(dragEvent.getX(), dragEvent.getY());
                        if (XUpFragment.this.mContainer.getViewBySpot(closestSpot) != XUpFragment.this.mMainDeviceView) {
                            final UEDeviceView ueDeviceView = (UEDeviceView)XUpFragment.this.mContainer.getChildAt(1);
                            if (closestSpot == 0) {
                                n = 1;
                            }
                            final PointF spotPosition = XUpFragment.this.mContainer.getSpotPosition(n, (View)ueDeviceView);
                            XUpFragment.this.playSpeakerMoveAndRescaleAnimation(ueDeviceView, XUpFragment.this.getSlaveDisplayMode(XUpFragment.this.mContainer.getDisplayMode()), spotPosition.x, spotPosition.y);
                            XUpFragment.this.mContainer.reserveSpot(n, (View)ueDeviceView);
                            XUpFragment.this.mContainer.reserveSpot(closestSpot, (View)XUpFragment.this.mMainDeviceView);
                            break;
                        }
                        break;
                    }
                    case 6: {
                        Log.d(XUpFragment.TAG, String.format("Drag Master. Container: EXIT. X:%.2f Y:%.2f", dragEvent.getX(), dragEvent.getY()));
                        break;
                    }
                    case 3: {
                        Log.d(XUpFragment.TAG, String.format("Drag Master. Container: DROP. X:%.2f Y:%.2f", dragEvent.getX(), dragEvent.getY()));
                        if (XUpFragment.this.mContainer.getDisplayMode() != SpiderFrameLayout.DisplayMode.DoubleUP) {
                            break;
                        }
                        final int closestSpot2 = XUpFragment.this.mContainer.getClosestSpot(dragEvent.getX(), dragEvent.getY());
                        if ((XUpFragment.this.mAudioOptions == UEBroadcastAudioOptions.REVERSE_STEREO_LEFT && closestSpot2 == 0) || (XUpFragment.this.mAudioOptions == UEBroadcastAudioOptions.STEREO_LEFT && closestSpot2 == 1)) {
                            final XUpFragment this$0 = XUpFragment.this;
                            UEBroadcastAudioOptions mAudioOptions;
                            if (XUpFragment.this.mContainer.getSpot((View)XUpFragment.this.mMainDeviceView) == 0) {
                                mAudioOptions = UEBroadcastAudioOptions.STEREO_LEFT;
                            }
                            else {
                                mAudioOptions = UEBroadcastAudioOptions.REVERSE_STEREO_LEFT;
                            }
                            this$0.mAudioOptions = mAudioOptions;
                            ((SafeTask<Void, Progress, Result>)new SetDeviceBroadcastTask(UEBroadcastState.ENABLE_SCANNING_AND_BROADCASTING, XUpFragment.this.mAudioOptions)).executeOnThreadPoolExecutor(new Void[0]);
                            break;
                        }
                        break;
                    }
                    case 4: {
                        Log.d(XUpFragment.TAG, String.format("Drag Master. Container: ENDED. X:%.2f Y:%.2f", dragEvent.getX(), dragEvent.getY()));
                        if (!XUpFragment.this.mIsXUPEnding) {
                            new Handler().post((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    XUpFragment.this.mMainDeviceView.setVisibility(0);
                                    XUpFragment.this.mMainDeviceView.setScaleX(View$OnDragListener.this.val$deviceRenderSize.x / (float)XUpFragment.this.mMainDeviceView.getWidth());
                                    XUpFragment.this.mMainDeviceView.setScaleY(View$OnDragListener.this.val$deviceRenderSize.y / (float)XUpFragment.this.mMainDeviceView.getHeight());
                                    final PointF spotPosition = XUpFragment.this.mContainer.getSpotPosition(XUpFragment.this.mContainer.getSpot((View)XUpFragment.this.mMainDeviceView), (View)XUpFragment.this.mMainDeviceView);
                                    XUpFragment.this.playSpeakerMoveAndRescaleAnimation(XUpFragment.this.mMainDeviceView, XUpFragment.this.getMasterDisplayMode(XUpFragment.this.mContainer.getDisplayMode()), spotPosition.x, spotPosition.y);
                                }
                            });
                            break;
                        }
                        new Handler().post((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                final PointF spotPosition = XUpFragment.this.mContainer.getSpotPosition(XUpFragment.this.mContainer.getSpot((View)XUpFragment.this.mMainDeviceView), (View)XUpFragment.this.mMainDeviceView);
                                XUpFragment.this.mMainDeviceView.setX(spotPosition.x);
                                XUpFragment.this.mMainDeviceView.setY(spotPosition.y);
                            }
                        });
                        break;
                    }
                }
                return true;
            }
        });
        this.mDrawerRecyclerView.setOnDragListener((View$OnDragListener)new View$OnDragListener() {
            public boolean onDrag(final View view, final DragEvent dragEvent) {
                switch (dragEvent.getAction()) {
                    case 5: {
                        Log.d(XUpFragment.TAG, String.format("Drag Master. Drawer: ENTERED. X:%.2f Y:%.2f", dragEvent.getX(), dragEvent.getY()));
                        break;
                    }
                    case 6: {
                        Log.d(XUpFragment.TAG, String.format("Drag Master. Drawer: EXITED. X:%.2f Y:%.2f", dragEvent.getX(), dragEvent.getY()));
                        break;
                    }
                    case 3: {
                        Log.d(XUpFragment.TAG, String.format("Drag Master. Drawer: DROP. X:%.2f Y:%.2f", dragEvent.getX(), dragEvent.getY()));
                        XUpFragment.this.mIsXUPEnding = true;
                        XUpFragment.this.setState(State.Transitioning);
                        App.getInstance().showAlertDialog(XUpFragment.this.getString(2131165466), XUpFragment.this.getString(2131165465), XUpFragment.this.getString(2131165358).toUpperCase(), XUpFragment.this.getString(2131165470).toUpperCase(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                            public void onClick(final DialogInterface dialogInterface, final int n) {
                                if (n == -1) {
                                    ((SafeTask<Void, Progress, Result>)new SetDeviceBroadcastTask(UEBroadcastState.ENABLE_SCANNING_ONLY, UEBroadcastAudioOptions.MULTIPLE) {
                                        @Override
                                        public void onError(final Exception ex) {
                                            super.onError(ex);
                                            App.getInstance().showMessageDialog(XUpFragment.this.getString(2131165469), null);
                                            FlurryTracker.logXUPError("End fail");
                                        }
                                        
                                        public void onSuccess(final Void void1) {
                                            super.onSuccess((T)void1);
                                            XUpFragment.this.playEndXUPAnimation();
                                            FlurryTracker.logXUPEndSession();
                                        }
                                    }).executeOnThreadPoolExecutor(new Void[0]);
                                }
                                else {
                                    XUpFragment.this.playCancelEndXUPAnimation();
                                }
                            }
                        }, null, (DialogInterface$OnCancelListener)new DialogInterface$OnCancelListener() {
                            public void onCancel(final DialogInterface dialogInterface) {
                                XUpFragment.this.playCancelEndXUPAnimation();
                            }
                        });
                        break;
                    }
                    case 4: {
                        Log.d(XUpFragment.TAG, String.format("Drag Master. Drawer: ENDED. X:%.2f Y:%.2f", dragEvent.getX(), dragEvent.getY()));
                        break;
                    }
                }
                return true;
            }
        });
        this.mMainDeviceView.startDrag((ClipData)null, (View$DragShadowBuilder)new DeviceDragShadowBuilder(this.getContext(), this.mHostNameCloud.getText().toString(), this.mMainDeviceView.getDeviceColor()), (Object)this.mHostNameCloud.getText(), 0);
    }
    
    public void beginMemberDrag(final XUPMember xupMember, final View view, final boolean b) {
        this.doShortVibration();
        this.mContainer.setOnDragListener((View$OnDragListener)new View$OnDragListener() {
            public boolean onDrag(final View view, final DragEvent dragEvent) {
                switch (dragEvent.getAction()) {
                    case 1: {
                        Log.d(XUpFragment.TAG, "Drag of member. Container: STARTED.");
                        if (b) {
                            view.setVisibility(4);
                            (XUpFragment.this.mDraggedView = new UEDeviceView((Context)XUpFragment.this.getActivity())).setMode(UEDeviceView.DisplayMode.MODE_SMALL);
                            XUpFragment.this.mDraggedView.setDeviceColor(xupMember.deviceColor);
                            XUpFragment.this.mDraggedView.setTag((Object)xupMember.Address);
                            XUpFragment.this.mDraggedView.setOnTouchListener((View$OnTouchListener)new LongPressGestureDetector() {
                                @Override
                                public void onLongPress(final View view, final MotionEvent motionEvent) {
                                    XUpFragment.this.beginMemberDragFromContainer(xupMember, (UEDeviceView)view);
                                }
                            });
                            break;
                        }
                        (XUpFragment.this.mDraggedView = (UEDeviceView)view).setVisibility(4);
                        XUpFragment.this.mDraggedView.setMode(UEDeviceView.DisplayMode.MODE_SMALL);
                        XUpFragment.this.mContainer.bringChildToFront((View)XUpFragment.this.mDraggedView);
                        XUpFragment.this.mContainer.releaseSpot((View)XUpFragment.this.mDraggedView);
                        XUpFragment.this.mContainer.removeView((View)XUpFragment.this.mDraggedView);
                        XUpFragment.this.recheckAndTransitContainerState();
                        XUpFragment.this.updateDoubleUpControls();
                        break;
                    }
                    case 5: {
                        Log.d(XUpFragment.TAG, String.format("Drag of member. Container: ENTERED. X:%.2f Y:%.2f", dragEvent.getX(), dragEvent.getY()));
                        break;
                    }
                    case 6: {
                        Log.d(XUpFragment.TAG, String.format("Drag of member. Container: EXITED. X:%.2f Y:%.2f", dragEvent.getX(), dragEvent.getY()));
                        break;
                    }
                    case 3: {
                        Log.d(XUpFragment.TAG, String.format("Drag of member. Container: DROP. X:%.2f Y:%.2f", dragEvent.getX(), dragEvent.getY()));
                        if (b) {
                            int n;
                            if (XUpFragment.this.mConnectedMemberList.size() == 0) {
                                n = 1;
                            }
                            else {
                                n = 0;
                            }
                            if (n != 0) {
                                if (Utils.areSpeakersInPerfectDoubleUpState(XUpFragment.this.mMainDeviceView.getDeviceColor(), xupMember.deviceColor) && XUpFragment.this.mAudioOptions != UEBroadcastAudioOptions.MULTIPLE) {
                                    ((SafeTask<Void, Progress, Result>)new SetDeviceBroadcastTask(UEBroadcastState.ENABLE_SCANNING_AND_BROADCASTING, XUpFragment.this.mAudioOptions)).executeOnThreadPoolExecutor(new Void[0]);
                                }
                                else {
                                    ((SafeTask<Void, Progress, Result>)new SetDeviceBroadcastTask(UEBroadcastState.ENABLE_SCANNING_AND_BROADCASTING, UEBroadcastAudioOptions.MULTIPLE)).executeOnThreadPoolExecutor(new Void[0]);
                                }
                                final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
                                if (connectedDevice != null) {
                                    FlurryTracker.logXUPStartSession(com.logitech.ue.util.Utils.byteArrayToHexString(MD5Helper.md5(connectedDevice.getAddress())), XUpFragment.this.mMainDeviceView.getDeviceColor(), UEColourHelper.getDeviceTypeByColour(XUpFragment.this.mMainDeviceView.getDeviceColor()).getDeviceTypeName());
                                }
                            }
                            else if (XUpFragment.this.mConnectedMemberList.size() == 1) {
                                ((SafeTask<Void, Progress, Result>)new SetDeviceBroadcastTask(UEBroadcastState.ENABLE_SCANNING_AND_BROADCASTING, UEBroadcastAudioOptions.MULTIPLE)).executeOnThreadPoolExecutor(new Void[0]);
                                XUpFragment.this.mAudioOptions = UEBroadcastAudioOptions.MULTIPLE;
                            }
                            xupMember.setStatusIgnoreTime(SystemClock.elapsedRealtime());
                            xupMember.connectionState = XUPReceiverConnectionState.CONNECTING;
                            new Handler().postDelayed((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    XUpFragment.this.mBroadcastModeHelper.addReceiverToBroadcast(xupMember.Address, UEBroadcastAudioMode.RIGHT);
                                }
                            }, 2000L);
                        }
                        if (XUpFragment.this.mContainer.indexOfChild((View)XUpFragment.this.mDraggedView) == -1) {
                            XUpFragment.this.mContainer.addView((View)XUpFragment.this.mDraggedView, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-2, -2));
                        }
                        XUpFragment.this.mDraggedView.setVisibility(0);
                        final int[] array = new int[2];
                        XUpFragment.this.mContainer.getLocationInWindow(array);
                        final float n2 = dragEvent.getX() - array[0];
                        final float n3 = dragEvent.getY() - array[1];
                        Utils.setViewCenterX((View)XUpFragment.this.mDraggedView, n2);
                        Utils.setViewCenterY((View)XUpFragment.this.mDraggedView, n3);
                        (XUpFragment.this.mDroppedView = XUpFragment.this.mDraggedView).requestLayout();
                        XUpFragment.this.mDroppedView.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new OnGlobalLayoutSelfRemovingListener(XUpFragment.this.mDroppedView) {
                            @Override
                            public void onHandleGlobalLayout() {
                                int n = 0;
                                final SpiderFrameLayout.DisplayMode recheckContainerState = XUpFragment.this.recheckContainerState();
                                XUpFragment.this.mContainer.setDisplayMode(recheckContainerState);
                                if (recheckContainerState == SpiderFrameLayout.DisplayMode.MegaUP || recheckContainerState == SpiderFrameLayout.DisplayMode.MonsterUP) {
                                    XUpFragment.this.mContainer.reserveSpot(0, (View)XUpFragment.this.mMainDeviceView);
                                }
                                else {
                                    XUpFragment.this.updateDoubleUpControls();
                                }
                                if (recheckContainerState == SpiderFrameLayout.DisplayMode.DoubleUP && XUpFragment.this.mAudioOptions != UEBroadcastAudioOptions.MULTIPLE) {
                                    final XUpFragment this$0 = XUpFragment.this;
                                    final UEDeviceView mDroppedView = XUpFragment.this.mDroppedView;
                                    if (XUpFragment.this.mAudioOptions == UEBroadcastAudioOptions.STEREO_LEFT) {
                                        n = 1;
                                    }
                                    this$0.playDropAnimation(mDroppedView, n);
                                }
                                else {
                                    XUpFragment.this.playDropAnimation(XUpFragment.this.mDroppedView, n2, n3);
                                }
                                XUpFragment.this.playContainerTransition(recheckContainerState);
                                XUpFragment.this.mDroppedView.postDelayed((Runnable)new Runnable() {
                                    @Override
                                    public void run() {
                                        if (b) {
                                            XUpFragment.this.mDroppedView.setIsLoading(true);
                                        }
                                        XUpFragment.this.mDroppedView = null;
                                    }
                                }, 200L);
                            }
                        });
                        if (b) {
                            XUpFragment.this.addConnectedMember(xupMember);
                            XUpFragment.this.removeDeviceFromDrawer(xupMember.Address);
                        }
                        XUpFragment.this.mDraggedView = null;
                        break;
                    }
                    case 4: {
                        Log.d(XUpFragment.TAG, String.format("Drag of member. Container: ENDED. X:%.2f Y:%.2f", dragEvent.getX(), dragEvent.getY()));
                        if (b) {
                            new Handler().post((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    view.setVisibility(0);
                                }
                            });
                        }
                        else if (XUpFragment.this.mDraggedView != null) {
                            new Handler().post((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    XUpFragment.this.disconnectMember(xupMember);
                                    if (XUpFragment.this.mConnectedMemberList.size() == 0) {
                                        ((SafeTask<Void, Progress, Result>)new SetDeviceBroadcastTask(UEBroadcastState.ENABLE_SCANNING_ONLY, UEBroadcastAudioOptions.MULTIPLE)).executeOnThreadPoolExecutor(new Void[0]);
                                        FlurryTracker.logXUPEndSession();
                                    }
                                }
                            });
                        }
                        XUpFragment.this.mDraggedView = null;
                        break;
                    }
                }
                return true;
            }
        });
        this.mDrawerRecyclerView.setOnDragListener((View$OnDragListener)new View$OnDragListener() {
            public boolean onDrag(final View view, final DragEvent dragEvent) {
                switch (dragEvent.getAction()) {
                    case 5: {
                        Log.d(XUpFragment.TAG, String.format("Drag of member. Drawer: ENTERED. X:%.2f Y:%.2f", dragEvent.getX(), dragEvent.getY()));
                        break;
                    }
                    case 6: {
                        Log.d(XUpFragment.TAG, String.format("Drag of member. Drawer: EXITED. X:%.2f Y:%.2f", dragEvent.getX(), dragEvent.getY()));
                        break;
                    }
                    case 3: {
                        Log.d(XUpFragment.TAG, String.format("Drag of member. Drawer: DROP. X:%.2f Y:%.2f", dragEvent.getX(), dragEvent.getY()));
                        break;
                    }
                    case 4: {
                        Log.d(XUpFragment.TAG, String.format("Drag of member. Drawer: ENDED. X:%.2f Y:%.2f", dragEvent.getX(), dragEvent.getY()));
                        break;
                    }
                }
                return true;
            }
        });
        final Context context = this.getContext();
        String s;
        if (xupMember.name != null) {
            s = xupMember.name;
        }
        else {
            s = UEColourHelper.getDeviceSpecificName(xupMember.deviceColor, this.getContext());
        }
        view.startDrag((ClipData)null, (View$DragShadowBuilder)new DeviceDragShadowBuilder(context, s, xupMember.deviceColor), (Object)null, 0);
    }
    
    public void beginMemberDragFromContainer(final XUPMember xupMember, final UEDeviceView ueDeviceView) {
        Log.d(XUpFragment.TAG, "Begin drag from container");
        this.beginMemberDrag(xupMember, (View)ueDeviceView, false);
    }
    
    public void beginMemberDragFromDrawer(final XUPMember xupMember, final View view) {
        Log.d(XUpFragment.TAG, "Begin drag from Drawer");
        this.beginMemberDrag(xupMember, view, true);
    }
    
    public void beginUpdate() {
        Log.d(XUpFragment.TAG, "Begin Update");
        ((SafeTask<Void, Progress, Result>)new Update()).executeOnThreadPoolExecutor(new Void[0]);
        this.updateTitle();
        this.updateVolumeSync();
        this.updateHostName();
        this.updateDoubleUpControls();
        this.updateAboveDrawerLabel();
        this.recheckAllMember();
        this.updateUI();
    }
    
    public void beginUpdateBalance() {
        if (this.mUpdateBalanceTask == null || this.mUpdateBalanceTask.getStatus() == AsyncTask$Status.FINISHED) {
            ((SafeTask<Void, Progress, Result>)(this.mUpdateBalanceTask = new UpdateBalanceTask())).executeOnThreadPoolExecutor(new Void[0]);
        }
    }
    
    public void checkMemberName(final XUPMember xupMember) {
        if (xupMember.shouldUpdateName()) {
            Log.d(XUpFragment.TAG, String.format("Update member name. Name: %s Color: 0x%02X Status: %s State: %s Address: %s", xupMember.name, xupMember.deviceColor, xupMember.status, xupMember.connectionState, xupMember.Address));
            xupMember.isFetchingName = true;
            if (UEDeviceManager.getInstance().isBleSupported()) {
                this.mNameFetcher.fetchName(xupMember.Address, xupMember.nameRevision, this.mNameFetcherListener);
            }
            else {
                this.mBroadcastModeHelper.getReceiverFixedAttributes(xupMember.Address);
            }
        }
    }
    
    void clearXUPData() {
        this.mMemberInfoMap.clear();
        this.mConnectedMemberList.clear();
        this.mDrawerMemberList.clear();
    }
    
    protected void disconnectMember(final XUPMember xupMember) {
        Log.d(XUpFragment.TAG, String.format("Disconnect member. Name: %s Address: %s", xupMember.name, xupMember.Address));
        this.mBroadcastModeHelper.removeReceiverFromBroadcast(xupMember.Address);
        xupMember.setStatusIgnoreTime(SystemClock.elapsedRealtime());
        xupMember.connectionState = XUPReceiverConnectionState.DISCONNECTING;
        final FragmentActivity activity = this.getActivity();
        final Resources resources = this.getResources();
        String s;
        if (xupMember.name != null) {
            s = xupMember.name;
        }
        else {
            s = UEColourHelper.getDeviceSpecificName(UEColourHelper.getDeviceTypeByColour(xupMember.deviceColor), this.getContext());
        }
        Toast.makeText((Context)activity, (CharSequence)resources.getString(2131165245, new Object[] { s }), 0).show();
        this.removeConnectedMember(xupMember.Address);
        this.addMemberToDrawer(xupMember.Address);
        this.updateUI();
    }
    
    public void doShortVibration() {
        if (this.vibrator != null) {
            this.vibrator.vibrate(100L);
        }
    }
    
    @Override
    public int getAccentColor() {
        final TypedValue typedValue = new TypedValue();
        this.getActivity().getTheme().resolveAttribute(17170446, typedValue, true);
        return typedValue.data;
    }
    
    public int getConnectedDevicesCount() {
        return this.mConnectedMemberList.size();
    }
    
    @Override
    public String getTitle() {
        String s;
        if (this.getConnectedDevicesCount() >= 0) {
            s = String.format(Locale.getDefault(), "%d-UP", this.getConnectedDevicesCount() + 1);
        }
        else {
            s = App.getInstance().getString(2131165458);
        }
        return s;
    }
    
    public boolean isInDoubleUpState() {
        return this.mContainer.getChildCount() == 2;
    }
    
    public boolean isInPerfectMarriageState() {
        return this.isInDoubleUpState() && UEColourHelper.getDeviceTypeByColour(this.mMainDeviceView.getDeviceColor()) == UEColourHelper.getDeviceTypeByColour(((UEDeviceView)this.mContainer.getChildAt(1)).getDeviceColor());
    }
    
    public boolean isInXUPState() {
        return this.mContainer.getChildCount() > 2;
    }
    
    public boolean isShowingTutorial() {
        return this.getChildFragmentManager().findFragmentByTag(XUPTutorialDialogFragment.TAG) != null;
    }
    
    public boolean isSoloState() {
        boolean b = true;
        if (this.mContainer.getChildCount() != 1) {
            b = false;
        }
        return b;
    }
    
    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        Log.d(XUpFragment.TAG, "Attach");
        this.vibrator = (Vibrator)context.getSystemService("vibrator");
        this.mNameFetcher.start(this.getContext());
        this.mBroadcastModeHelper.start(this.getContext());
    }
    
    @OnClick({ 2131624098 })
    public void onBalanceCenterClicked(final View view) {
        Log.d(XUpFragment.TAG, "On balance center clicked");
        this.mBalanceSeekBar.setProgress(this.mBalanceSeekBar.getMax() / 2);
        this.setDeviceBalance(this.mBalanceSeekBar.getMax() / 2);
    }
    
    @OnClick({ 2131624097 })
    public void onBalanceLeftClicked(final View view) {
        Log.d(XUpFragment.TAG, "On balance left clicked");
        this.mBalanceSeekBar.setProgress(0);
        this.setDeviceBalance(0);
        FlurryTracker.logBalanceChange();
    }
    
    @OnClick({ 2131624099 })
    public void onBalanceRightClicked(final View view) {
        Log.d(XUpFragment.TAG, "On balance right clicked");
        this.mBalanceSeekBar.setProgress(this.mBalanceSeekBar.getMax());
        this.setDeviceBalance(this.mBalanceSeekBar.getMax());
        FlurryTracker.logBalanceChange();
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mBroadcastModeHelper.setListener(this.mBroadcastHelperListener);
        this.recheckHandler.postDelayed((Runnable)new RecheckTimerRunnable(), 30000L);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(2130968640, viewGroup, false);
    }
    
    @Override
    public void onDeselected() {
        Log.d(XUpFragment.TAG, "Is deselected as Tab");
        this.autoStopXUP();
        this.stopRechekTimer();
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    
    @Override
    public void onDetach() {
        Log.d(XUpFragment.TAG, "Detach");
        this.mNameFetcher.stop();
        this.mBroadcastModeHelper.stop();
        super.onDetach();
    }
    
    @OnClick({ 2131624107 })
    public void onDoubleUpClicked(final View view) {
        Log.d(XUpFragment.TAG, "DoubleUP clicked");
        this.mDoubleUpButton.setSelected(true);
        this.mStereoButton.setSelected(false);
        ((SafeTask<Void, Progress, Result>)new SetDeviceBroadcastTask(UEBroadcastState.ENABLE_SCANNING_AND_BROADCASTING, UEBroadcastAudioOptions.MULTIPLE)).executeOnThreadPoolExecutor(new Void[0]);
        FlurryTracker.logStereoChange(false);
        this.mBalanceSeekBar.setProgress(this.mBalanceSeekBar.getMax() / 2);
        this.mBalanceUpdater.update((Byte)0);
        this.mAudioOptions = UEBroadcastAudioOptions.MULTIPLE;
        Toast.makeText((Context)this.getActivity(), 2131165248, 0).show();
        this.updateUI();
    }
    
    @Override
    public void onPause() {
        this.autoStopXUP();
        this.startRechekTimer();
        super.onPause();
    }
    
    @Override
    public void onSelected() {
        Log.d(XUpFragment.TAG, "Is selected as Tab");
        if (UserPreferences.getInstance().isXUPOnBoardingWasSeen()) {
            this.recheckAllMember();
            this.updateUI();
        }
        else if (!UserPreferences.getInstance().isXUPOnBoardingWasSeen()) {
            this.showTutorial();
        }
        if (this.mState != State.Blocked) {
            this.autoStartXUP();
        }
        XUpFragment.sWasSelected = true;
        this.startRechekTimer();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.logitech.ue.centurion.CONNECTION_CHANGED");
        intentFilter.addAction("com.logitech.ue.centurion.ACTION_BROADCAST_STATUS_NOTIFICATION");
        intentFilter.addAction("com.logitech.ue.centurion.ACTION_RECEIVER_ADDED_NOTIFICATION");
        intentFilter.addAction("com.logitech.ue.centurion.ACTION_RECEIVER_REMOVED_NOTIFICATION");
        intentFilter.addAction("com.logitech.ue.centurion.ACTION_RECEIVER_FIXED_ATTRIBUTES_NOTIFICATION");
        intentFilter.addAction("com.logitech.ue.fragments.PARTY_MODE_STATE_CHANGED");
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(this.mBroadcastReceiver, intentFilter);
        this.beginUpdate();
    }
    
    @OnClick({ 2131624120 })
    public void onStartClicked(final View view) {
        Log.d(XUpFragment.TAG, "On Start clicked");
        App.getInstance().showAlertDialog(this.getString(2131165417), this.getString(2131165416), 2131165358, 2131165470, -2, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                if (n == -1) {
                    Log.d(XUpFragment.TAG, "User agreed to shutdown Block Party");
                    ((SafeTask<Void, Progress, Result>)new SetDeviceBlockPartyStateTask(false) {
                        public void onSuccess(final Void void1) {
                            super.onSuccess((T)void1);
                            ((SafeTask<Void, Progress, Result>)new SetDeviceBroadcastTask(UEBroadcastState.ENABLE_SCANNING_ONLY, XUpFragment.this.mAudioOptions)).executeOnThreadPoolExecutor(new Void[0]);
                            BlockPartyUtils.broadcastBlackPartyStateChanged(UEBlockPartyState.OFF, XUpFragment.class.getSimpleName(), XUpFragment.this.getContext());
                            XUpFragment.this.setState(State.Normal);
                        }
                    }).executeOnThreadPoolExecutor(new Void[0]);
                }
                else {
                    Log.d(XUpFragment.TAG, "User disagreed to shutdown Block Party");
                }
            }
        });
    }
    
    @OnClick({ 2131624108 })
    public void onStereoClicked(final View view) {
        Log.d(XUpFragment.TAG, "Stereo clicked");
        this.mDoubleUpButton.setSelected(false);
        this.mStereoButton.setSelected(true);
        UEBroadcastAudioOptions mAudioOptions;
        if (this.mContainer.getSpot((View)this.mMainDeviceView) == 0) {
            mAudioOptions = UEBroadcastAudioOptions.STEREO_LEFT;
        }
        else {
            mAudioOptions = UEBroadcastAudioOptions.REVERSE_STEREO_LEFT;
        }
        this.mAudioOptions = mAudioOptions;
        ((SafeTask<Void, Progress, Result>)new SetDeviceBroadcastTask(UEBroadcastState.ENABLE_SCANNING_AND_BROADCASTING, this.mAudioOptions)).executeOnThreadPoolExecutor(new Void[0]);
        FlurryTracker.logStereoChange(true);
        Toast.makeText((Context)this.getActivity(), 2131165419, 0).show();
        this.updateUI();
    }
    
    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(this.mBroadcastReceiver);
        super.onStop();
    }
    
    @OnClick({ 2131624116 })
    public void onSwapSpeakersClick(final View view) {
        boolean b = false;
        Log.d(XUpFragment.TAG, "DoubleUP swap clicked");
        UEBroadcastAudioOptions mAudioOptions;
        if (this.mContainer.getSpot((View)this.mMainDeviceView) == 0) {
            mAudioOptions = UEBroadcastAudioOptions.REVERSE_STEREO_LEFT;
        }
        else {
            mAudioOptions = UEBroadcastAudioOptions.STEREO_LEFT;
        }
        this.mAudioOptions = mAudioOptions;
        ((SafeTask<Void, Progress, Result>)new SetDeviceBroadcastTask(UEBroadcastState.ENABLE_SCANNING_AND_BROADCASTING, this.mAudioOptions) {
            public void onSuccess(final Void void1) {
                super.onSuccess((T)void1);
                XUpFragment.this.setDeviceBalance(XUpFragment.this.mBalanceSeekBar.getProgress());
            }
        }).executeOnThreadPoolExecutor(new Void[0]);
        this.mBalanceSeekBar.setProgress(this.mBalanceSeekBar.getMax() - this.mBalanceSeekBar.getProgress());
        final UEDeviceView mMainDeviceView = this.mMainDeviceView;
        final UEDeviceView ueDeviceView = (UEDeviceView)this.mContainer.getChildAt(1);
        if (this.mContainer.getSpot((View)this.mMainDeviceView) == 0) {
            b = true;
        }
        this.swapSpeakers(mMainDeviceView, ueDeviceView, b, true);
    }
    
    @Override
    public void onTransition(final float n) {
        if (this.getView() != null) {
            this.mTutorialCloud.setTranslationY(this.mDrawerHeight * 1.5f * n);
            this.mSpeakersFoundLabel.setTranslationY(this.mDrawerHeight * 1.5f * n);
            this.mDrawerRecyclerView.setTranslationY(this.mDrawerHeight * 1.5f * n);
        }
        else {
            Log.e(XUpFragment.TAG, "Tutorial or drawer has not been initialised yet!");
        }
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        this.mContainer.setMasterView((View)this.mMainDeviceView);
        this.mDrawerRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new LinearLayoutManager(this.getActivity(), 0, false) {
            @Override
            public void onLayoutChildren(final Recycler recycler, final RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
                    final float n = (float)(((RecyclerView.LayoutManager)this).getDecoratedRight(((RecyclerView.LayoutManager)this).getChildAt(((RecyclerView.LayoutManager)this).getChildCount() - 1)) - ((RecyclerView.LayoutManager)this).getDecoratedLeft(((RecyclerView.LayoutManager)this).getChildAt(0)));
                    if (n < ((RecyclerView.LayoutManager)this).getWidth()) {
                        int round = Math.round((((RecyclerView.LayoutManager)this).getWidth() - n) / 2.0f);
                        for (int i = 0; i < ((RecyclerView.LayoutManager)this).getChildCount(); ++i) {
                            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
                            ((RecyclerView.LayoutManager)this).layoutDecorated(child, round, ((RecyclerView.LayoutManager)this).getDecoratedTop(child), round + child.getMeasuredWidth(), child.getMeasuredHeight() + ((RecyclerView.LayoutManager)this).getDecoratedTop(child));
                            round += child.getMeasuredWidth() + ((RecyclerView.LayoutManager)this).getPaddingLeft();
                        }
                    }
                }
            }
        };
        this.mDrawerRecyclerView.setLayoutManager((RecyclerView.LayoutManager)this.mLayoutManager);
        this.mDrawerRecyclerView.setItemAnimator((RecyclerView.ItemAnimator)new DrawableChangeAnimator());
        this.mDrawerAdapter = new DeviceDrawerAdapter();
        this.mDrawerRecyclerView.setAdapter((RecyclerView.Adapter)this.mDrawerAdapter);
        this.mBalanceSeekBar.setOnSeekBarChangeListener((SeekBar$OnSeekBarChangeListener)new SeekBar$OnSeekBarChangeListener() {
            public void onProgressChanged(final SeekBar seekBar, final int n, final boolean b) {
                XUpFragment.this.updateBalanceSeekBar(n, b);
            }
            
            public void onStartTrackingTouch(final SeekBar seekBar) {
            }
            
            public void onStopTrackingTouch(final SeekBar seekBar) {
                FlurryTracker.logBalanceChange();
            }
        });
        this.mContainer.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new OnGlobalLayoutSelfRemovingListener(this.mContainer) {
            @Override
            public void onHandleGlobalLayout() {
                final PointF spotPosition = XUpFragment.this.mContainer.getSpotPosition(0, (View)XUpFragment.this.mMainDeviceView);
                XUpFragment.this.mContainer.reserveSpot(0, (View)XUpFragment.this.mMainDeviceView);
                XUpFragment.this.mMainDeviceView.setX(spotPosition.x);
                XUpFragment.this.mMainDeviceView.setY(spotPosition.y);
                ((SafeTask<Void, Progress, Result>)new GetDeviceNameTask() {
                    public void onSuccess(final String text) {
                        super.onSuccess((T)text);
                        XUpFragment.this.mHostNameCloud.setVisibility(0);
                        XUpFragment.this.mHostNameCloud.setText((CharSequence)text);
                    }
                }).executeOnThreadPoolExecutor(new Void[0]);
            }
        });
        this.mMainDeviceView.setOnTouchListener((View$OnTouchListener)new LongPressGestureDetector() {
            @Override
            public void onLongPress(final View view, final MotionEvent motionEvent) {
                if (XUpFragment.this.mConnectedMemberList.size() > 0) {
                    XUpFragment.this.beginMasterDrag();
                }
            }
        });
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.mHostNameCloud, View.TRANSLATION_Y, new float[] { TypedValue.applyDimension(1, 8.0f, this.getResources().getDisplayMetrics()) });
        ofFloat.setRepeatCount(-1);
        ofFloat.setRepeatMode(2);
        ofFloat.setInterpolator((TimeInterpolator)new LinearInterpolator());
        ofFloat.setDuration((long)AnimationUtils.getAndroidLongAnimationTime(this.getResources()));
        ofFloat.start();
        AnimationUtils.attacheAnimatorToView((View)this.mHostNameCloud, (Animator)ofFloat);
    }
    
    @OnClick({ 2131624193 })
    public void onVolumeSyncClicked(final View view) {
        Log.d(XUpFragment.TAG, "Volume sync clicked");
        ((SafeTask<Void, Progress, Result>)new BeginBroadcastVolumeSyncTask() {
            @Override
            public void onError(final Exception ex) {
                super.onError(ex);
                FlurryTracker.logXUPError("Volume sync fail");
            }
        }).executeOnThreadPoolExecutor(new Void[0]);
        if (!AnimationUtils.isRunningAnimation(this.mVolumeSyncButton)) {
            this.mIsVolumeSynced = true;
            this.mVolumeSyncIcon.setImageResource(2130837615);
            this.mVolumeSyncText.setText(2131165426);
            this.mVolumeSyncText.setTextColor(this.getResources().getColor(2131558420));
            final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.mVolumeSyncButton, View.ALPHA, new float[] { 0.0f });
            ofFloat.setDuration((long)AnimationUtils.getAndroidMediumAnimationTime(this.getContext()));
            ofFloat.setStartDelay(2000L);
            ofFloat.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    super.onAnimationEnd(animator);
                    XUpFragment.this.mVolumeSyncButton.setAlpha(1.0f);
                    XUpFragment.this.mVolumeSyncButton.setVisibility(8);
                    AnimationUtils.detachAnimatorFromView(XUpFragment.this.mVolumeSyncButton, (Animator)ofFloat);
                }
            });
            ofFloat.start();
            AnimationUtils.attacheAnimatorToView(this.mVolumeSyncButton, (Animator)ofFloat);
            FlurryTracker.logXUPVolumeSync();
        }
    }
    
    public void playCancelEndXUPAnimation() {
        this.setState(State.Transitioning);
        this.mMainDeviceView.setVisibility(0);
        this.mMainDeviceView.setAlpha(0.0f);
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.mMainDeviceView, View.ALPHA, new float[] { 1.0f });
        ofFloat.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                super.onAnimationEnd(animator);
                XUpFragment.this.setState(State.Normal);
            }
        });
        ofFloat.setDuration((long)AnimationUtils.getAndroidShortAnimationTime(this.getContext()));
        ofFloat.start();
    }
    
    public void playEndXUPAnimation() {
        this.setState(State.Transitioning);
        this.mMainDeviceView.setVisibility(0);
        this.mMainDeviceView.setAlpha(0.0f);
        final AnimatorSet set = new AnimatorSet();
        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                super.onAnimationEnd(animator);
                XUpFragment.this.setState(State.Normal);
                while (!XUpFragment.this.mConnectedMemberList.isEmpty()) {
                    final MAC mac = XUpFragment.this.mConnectedMemberList.get(0);
                    XUpFragment.this.removeConnectedMember(mac);
                    XUpFragment.this.addMemberToDrawer(mac);
                }
                XUpFragment.this.playContainerTransition(SpiderFrameLayout.DisplayMode.Solo);
                XUpFragment.this.updateUI();
            }
        });
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this.mMainDeviceView, View.ALPHA, new float[] { 1.0f });
        ofFloat.setDuration((long)AnimationUtils.getAndroidShortAnimationTime(this.getContext()));
        final AnimatorSet$Builder play = set.play((Animator)ofFloat);
        for (int i = 0; i < this.mContainer.getChildCount(); ++i) {
            final View child = this.mContainer.getChildAt(i);
            AnimationUtils.stopAnimation(child);
            if (child != this.mMainDeviceView) {
                final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)child, View.ALPHA, new float[] { 0.0f });
                ofFloat2.setDuration((long)AnimationUtils.getAndroidShortAnimationTime(this.getContext()));
                play.with((Animator)ofFloat2);
            }
        }
        set.start();
    }
    
    void recheckAllMember() {
        Log.d(XUpFragment.TAG, "Recheck all members. Member count: " + this.mMemberInfoMap.keySet().size());
        final ArrayList<MAC> list = new ArrayList<MAC>();
        final ArrayList<XUPMember> list2 = new ArrayList<XUPMember>();
        for (final MAC key : this.mMemberInfoMap.keySet()) {
            final XUPMember xupMember = this.mMemberInfoMap.get(key);
            final long elapsedRealtime = SystemClock.elapsedRealtime();
            if (elapsedRealtime - xupMember.lastTimeSeen > 30000L) {
                list.add(key);
                Log.d(XUpFragment.TAG, String.format("Remove member. Name: %s Color: 0x%02X Status: %s State: %s Address: %s", xupMember.name, xupMember.deviceColor, xupMember.status, xupMember.connectionState, xupMember.Address));
            }
            else if (xupMember.isUpdateIgnored()) {
                Log.d(XUpFragment.TAG, String.format("Ignore member. Ignore time left: %d ms. Name: %s Color: 0x%02X Status: %s State: %s  Address: %s", 20000L - (elapsedRealtime - xupMember.getStatusIgnoreTime()), xupMember.name, xupMember.deviceColor, xupMember.status, xupMember.connectionState, xupMember.Address));
            }
            else {
                list2.add(this.mMemberInfoMap.get(key));
            }
        }
        for (final MAC mac : list) {
            final XUPMember xupMember2 = this.mMemberInfoMap.get(mac);
            this.removeDeviceFromDrawer(mac);
            this.removeConnectedMember(xupMember2.Address);
            this.mMemberInfoMap.remove(mac);
        }
        this.recheckMembers((XUPMember[])list2.toArray(new XUPMember[list2.size()]));
    }
    
    public void recheckAndTransitContainerState() {
        this.playContainerTransition(this.recheckContainerState());
    }
    
    public SpiderFrameLayout.DisplayMode recheckContainerState() {
        final int childCount = this.mContainer.getChildCount();
        SpiderFrameLayout.DisplayMode displayMode;
        if (childCount == 1) {
            displayMode = SpiderFrameLayout.DisplayMode.Solo;
        }
        else if (childCount == 2) {
            displayMode = SpiderFrameLayout.DisplayMode.DoubleUP;
        }
        else if (childCount > 2 && childCount <= 50) {
            displayMode = SpiderFrameLayout.DisplayMode.MegaUP;
        }
        else {
            displayMode = SpiderFrameLayout.DisplayMode.MonsterUP;
        }
        return displayMode;
    }
    
    public void recheckMembers(final XUPMember... array) {
        final ArrayList<XUPMember> list = new ArrayList<XUPMember>();
        this.mIsVolumeSynced = true;
        for (final XUPMember e : array) {
            Log.d(XUpFragment.TAG, String.format("Recheck member. Name: %s Color: 0x%02X Status: %s State: %s Address: %s", e.name, e.deviceColor, e.status, e.connectionState, e.Address));
            if (e.connectionState == XUPReceiverConnectionState.CONNECTED) {
                if (this.mConnectedMemberList.indexOf(e.Address) == -1) {
                    list.add(e);
                }
                this.removeDeviceFromDrawer(e.Address);
                UEDeviceView ueDeviceView;
                if ((ueDeviceView = this.getViewByMember(e.Address)) == null) {
                    ueDeviceView = this.addConnectedMember(e);
                }
                if (ueDeviceView != null) {
                    ueDeviceView.setIsLoading(false);
                }
                this.mIsVolumeSynced &= e.isVolumeSynced;
                if (this.mAwaitingDeviceAddress != null && this.mAwaitingDeviceAddress.equals(e.Address)) {
                    App.getInstance().dismissMessageDialog();
                    this.mAwaitingDeviceAddress = null;
                    this.mDialogTimeoutHandler.removeCallbacksAndMessages((Object)null);
                }
            }
            else {
                if (this.mConnectedMemberList.indexOf(e.Address) != -1) {
                    FlurryTracker.logXUPReceiverLeft(com.logitech.ue.util.Utils.byteArrayToHexString(MD5Helper.md5(e.Address)));
                }
                this.removeConnectedMember(e.Address);
                this.addMemberToDrawer(e.Address);
            }
            this.checkMemberName(e);
        }
        for (final XUPMember xupMember : list) {
            Log.d(XUpFragment.TAG, String.format("Add member to X-UP. Name: %s Color: 0x%02X Status: %s State: %s Address: %s", xupMember.name, xupMember.deviceColor, xupMember.status, xupMember.connectionState, xupMember.Address));
            this.addConnectedMember(xupMember);
            FlurryTracker.logXUPReceiverJoin(com.logitech.ue.util.Utils.byteArrayToHexString(MD5Helper.md5(xupMember.Address)), xupMember.deviceColor, UEColourHelper.getDeviceTypeByColour(xupMember.deviceColor).name(), false);
        }
        this.recheckAndTransitContainerState();
        this.updateUI();
        if (XUpFragment.sWasSelected) {
            UserPreferences.getInstance().setXUPLastSessionConnectionCount(this.mConnectedMemberList.size());
        }
    }
    
    public void removeConnectedMember(final MAC mac) {
        if (mac != null) {
            if (this.mConnectedMemberList.indexOf(mac) != -1) {
                this.mConnectedMemberList.remove(mac);
            }
            this.mContainer.removeView((View)this.getViewByMember(mac));
        }
    }
    
    public void removeDeviceFromDrawer(final MAC mac) {
        final int index = this.mDrawerMemberList.indexOf(mac);
        if (index != -1) {
            this.mDrawerMemberList.remove(mac);
            ((RecyclerView.Adapter)this.mDrawerAdapter).notifyItemRemoved(index + 1);
        }
    }
    
    public void setDeviceBalance(final int n) {
        this.mBalanceUpdater.update((byte)(Math.round(255.0f * (1.0f * n / this.mBalanceSeekBar.getMax())) - 128));
    }
    
    public void showPowerOffDeviceDialog(final MAC mAwaitingDeviceAddress) {
        this.mAwaitingDeviceAddress = mAwaitingDeviceAddress;
        App.getInstance().showMessageDialog(this.getResources().getString(2131165462), this.getResources().getString(2131165461), null);
        this.mDialogTimeoutHandler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                App.getInstance().dismissMessageDialog();
                XUpFragment.this.removeConnectedMember(XUpFragment.this.mAwaitingDeviceAddress);
                XUpFragment.this.mAwaitingDeviceAddress = null;
            }
        }, 60000L);
    }
    
    public void showSecuredDeviceDialog(final MAC mAwaitingDeviceAddress) {
        this.mAwaitingDeviceAddress = mAwaitingDeviceAddress;
        App.getInstance().showMessageDialog(this.getResources().getString(2131165464), this.getResources().getString(2131165463), null);
        this.mDialogTimeoutHandler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                App.getInstance().dismissMessageDialog();
                XUpFragment.this.removeConnectedMember(XUpFragment.this.mAwaitingDeviceAddress);
                XUpFragment.this.mAwaitingDeviceAddress = null;
            }
        }, 60000L);
    }
    
    public void showTutorial() {
    }
    
    public void startRechekTimer() {
        this.recheckHandler.postDelayed((Runnable)new RecheckTimerRunnable(), 30000L);
    }
    
    public void stopRechekTimer() {
        this.recheckHandler.removeCallbacksAndMessages((Object)null);
    }
    
    public void swapSpeakers(final UEDeviceView ueDeviceView, final UEDeviceView ueDeviceView2, final boolean b, final boolean b2) {
        int n = 0;
        if (this.mContainer.getDisplayMode() != SpiderFrameLayout.DisplayMode.DoubleUP) {
            Log.e(XUpFragment.TAG, "Container is not in DOUBLE display mode speakers. Don't swap");
        }
        else {
            Log.d(XUpFragment.TAG, "Swap speakers");
        }
        int n2;
        if (b) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        if (!b) {
            n = 1;
        }
        final PointF spotPosition = this.mContainer.getSpotPosition(n2, (View)this.mMainDeviceView);
        final PointF spotPosition2 = this.mContainer.getSpotPosition(n, (View)ueDeviceView2);
        this.mContainer.reserveSpot(n2, (View)ueDeviceView);
        this.mContainer.reserveSpot(n, (View)ueDeviceView2);
        if (b2) {
            this.playSpeakerMoveAndRescaleAnimation(ueDeviceView, UEDeviceView.DisplayMode.MODE_NORMAL, spotPosition.x, spotPosition.y);
            this.playSpeakerMoveAndRescaleAnimation(ueDeviceView2, UEDeviceView.DisplayMode.MODE_NORMAL, spotPosition2.x, spotPosition2.y);
        }
        else {
            ueDeviceView.setX(spotPosition.x);
            ueDeviceView.setY(spotPosition.y);
            ueDeviceView2.setX(spotPosition2.x);
            ueDeviceView2.setY(spotPosition2.y);
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
            Log.d(XUpFragment.TAG, "Update balance seek bar to value " + deviceBalance);
            final int n = this.mBalanceSeekBar.getMax() / 2;
            if (Math.abs(deviceBalance - n) <= 0.05f * this.mBalanceSeekBar.getMax()) {
                deviceBalance = n;
                this.mBalanceSeekBar.setProgress(deviceBalance);
            }
            this.setDeviceBalance(deviceBalance);
        }
    }
    
    public void updateHostName() {
        if (this.isSoloState() && this.getState() != State.Blocked) {
            Log.d(XUpFragment.TAG, "Show host name cloud");
            this.mHostNameCloud.setVisibility(0);
        }
        else {
            if (!this.isSoloState()) {
                Log.d(XUpFragment.TAG, "Hide host name cloud. X-UP is not in solo state");
            }
            else {
                Log.d(XUpFragment.TAG, "Hide host name cloud. State is blocked");
            }
            this.mHostNameCloud.setVisibility(8);
        }
    }
    
    void updateTitle() {
        if (this.getActivity() != null) {
            Log.d(XUpFragment.TAG, "Update title");
            ((MainActivity)this.getActivity()).onTitleChanged(this.getParentFragment(), this.getTitle());
        }
        else {
            Log.w(XUpFragment.TAG, "Can't update title because activity is null");
        }
    }
    
    public void updateUI() {
        if (this.getView() == null) {
            Log.w(XUpFragment.TAG, "Don't update UI if view doesn't exist");
        }
        else {
            Log.d(XUpFragment.TAG, "Update UI");
            this.updateTitle();
            this.updateVolumeSync();
            this.updateHostName();
            this.updateDoubleUpControls();
            this.updateAboveDrawerLabel();
            if (this.mState != State.Blocked) {
                this.mDrawerRecyclerView.setVisibility(0);
                this.mStartButton.setVisibility(8);
            }
            else {
                this.mDrawerRecyclerView.setVisibility(8);
                this.mStartButton.setVisibility(0);
            }
        }
    }
    
    public void updateVolumeSync() {
        if (this.isInXUPState() || this.getState() == State.Blocked) {
            if (!this.mIsVolumeSynced) {
                if (!AnimationUtils.isRunningAnimation(this.mVolumeSyncButton)) {
                    Log.d(XUpFragment.TAG, "Show volume sync");
                    this.mVolumeSyncButton.setVisibility(0);
                    this.mVolumeSyncText.setText(this.getResources().getText(2131165453));
                    this.mVolumeSyncText.setTextColor(ContextCompat.getColor(this.getContext(), 17170443));
                    this.mVolumeSyncIcon.setImageResource(2130837653);
                }
            }
            else {
                Log.d(XUpFragment.TAG, "Hide volume sync");
                this.mVolumeSyncButton.setVisibility(8);
            }
        }
        else {
            Log.d(XUpFragment.TAG, "Hide volume sync because we are not in X-UP");
            this.mVolumeSyncButton.setVisibility(8);
        }
    }
    
    public class DeviceDrawerAdapter extends Adapter<ViewHolder>
    {
        public static final int ITEM_TYPE_DEVICE = 0;
        public static final int ITEM_TYPE_DUMMY_DEVICE = 1;
        
        MAC getDeviceAddress(int index) {
            MAC mac;
            if (--index < XUpFragment.this.mDrawerMemberList.size()) {
                mac = XUpFragment.this.mDrawerMemberList.get(index);
            }
            else {
                mac = new MAC();
            }
            return mac;
        }
        
        @Override
        public int getItemCount() {
            return XUpFragment.this.mDrawerMemberList.size() + 1;
        }
        
        @Override
        public long getItemId(final int n) {
            return this.getDeviceAddress(n).hashCode();
        }
        
        @Override
        public int getItemViewType(int n) {
            if (n == 0) {
                n = 1;
            }
            else {
                n = 0;
            }
            return n;
        }
        
        XUPMember getMember(final int n) {
            XUPMember xupMember;
            if (n == 0) {
                xupMember = null;
            }
            else {
                xupMember = XUpFragment.this.mMemberInfoMap.get(this.getDeviceAddress(n));
            }
            return xupMember;
        }
        
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            viewHolder.update(this.getMember(n));
        }
        
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            ViewHolder viewHolder;
            if (n == 0) {
                viewHolder = new DeviceHolder(View.inflate(viewGroup.getContext(), 2130968620, (ViewGroup)null));
            }
            else {
                viewHolder = new DummyDeviceHolder(View.inflate(viewGroup.getContext(), 2130968621, (ViewGroup)null));
            }
            return viewHolder;
        }
        
        public class DeviceHolder extends ViewHolder
        {
            UEDeviceView mDeviceView;
            XUPMember mMember;
            
            public DeviceHolder(final View view) {
                super(view);
                this.mDeviceView = (UEDeviceView)view.findViewById(2131624106);
                this.itemView.setOnTouchListener((View$OnTouchListener)new LongPressGestureDetector() {
                    @Override
                    public void onLongPress(final View view, final MotionEvent motionEvent) {
                        if (XUpFragment.this.mDrawerRecyclerView.getScrollState() == 0) {
                            XUpFragment.this.beginMemberDragFromDrawer(DeviceHolder.this.mMember, DeviceHolder.this.itemView);
                            XUpFragment.this.updateHostName();
                        }
                    }
                });
                this.mDeviceView.setEnabled(false);
            }
            
            @Override
            public void update(final XUPMember mMember) {
                this.mMember = mMember;
                this.itemView.setVisibility(0);
                this.mDeviceView.setDeviceColor(mMember.deviceColor);
            }
        }
        
        public class DummyDeviceHolder extends ViewHolder
        {
            public DummyDeviceHolder(final View view) {
                super(view);
                view.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        XUpFragment.this.showTutorial();
                        App.getInstance().showAlertDialog(XUpFragment.this.getString(2131165283), null, 2131165371, 2131165290, -1, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                            public void onClick(final DialogInterface dialogInterface, final int n) {
                                if (n == -1) {
                                    XUpFragment.this.startActivity(new Intent((Context)XUpFragment.this.getActivity(), (Class)XUPTutorialActivity.class));
                                }
                                else {
                                    XUpFragment.this.startActivity(new Intent((Context)XUpFragment.this.getActivity(), (Class)XUPTricksActivity.class));
                                }
                            }
                        });
                        FlurryTracker.logXUPMissingSpeakersTaped();
                    }
                });
            }
            
            @Override
            public void update(final XUPMember xupMember) {
            }
        }
        
        public abstract class ViewHolder extends RecyclerView.ViewHolder
        {
            public ViewHolder(final View view) {
                super(view);
            }
            
            public abstract void update(final XUPMember p0);
        }
    }
    
    private class DrawableChangeAnimator extends DefaultItemAnimator
    {
        @Override
        public boolean animateAdd(final RecyclerView.ViewHolder viewHolder) {
            viewHolder.itemView.setTranslationX((float)XUpFragment.this.mDrawerRecyclerView.getWidth());
            viewHolder.itemView.animate().translationX(0.0f).setDuration((long)AnimationUtils.getAndroidLongAnimationTime(XUpFragment.this.getContext())).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    super.onAnimationEnd(animator);
                    ((RecyclerView.ItemAnimator)DrawableChangeAnimator.this).dispatchAnimationFinished(viewHolder);
                }
            });
            return true;
        }
    }
    
    private class RecheckTimerRunnable implements Runnable
    {
        @Override
        public void run() {
            if (XUpFragment.this.getState() != State.Transitioning && XUpFragment.this.getView() != null) {
                XUpFragment.this.recheckAllMember();
                XUpFragment.this.updateUI();
            }
            XUpFragment.this.recheckHandler.postDelayed((Runnable)this, 30000L);
        }
    }
    
    public enum State
    {
        Blocked, 
        Normal, 
        Transitioning;
    }
    
    class Update extends SafeTask<Void, Void, Object[]>
    {
        @Override
        public String getTag() {
            return XUpFragment.TAG;
        }
        
        @Override
        public void onSuccess(final Object[] array) {
            super.onSuccess(array);
            if (!((UEBlockPartyState)array[0]).isOnOrEntering()) {
                XUpFragment.this.setState(State.Normal);
                XUpFragment.this.beginMasterColorUpdate();
                XUpFragment.this.beginUpdateBalance();
                XUpFragment.this.beginHostNameUpdate();
                XUpFragment.this.beginAudioSettingUpdate();
            }
            else {
                XUpFragment.this.setState(State.Blocked);
            }
            XUpFragment.this.updateUI();
        }
        
        @Override
        public Object[] work(final Void... array) throws Exception {
            return new Object[] { UEDeviceManager.getInstance().getConnectedDevice().getBlockPartyState() };
        }
    }
    
    class UpdateBalanceTask extends GetDeviceBalanceTask
    {
        @Override
        public void onError(final Exception ex) {
            super.onError(ex);
            XUpFragment.this.updateBalanceSeekBar(0, false);
        }
        
        public void onSuccess(final Byte b) {
            super.onSuccess((T)b);
            Byte value = b;
            if (XUpFragment.this.mContainer.getSpot((View)XUpFragment.this.mMainDeviceView) == 1) {
                byte b2;
                if (b == -128) {
                    b2 = (byte)(-(b + 1));
                }
                else {
                    b2 = -b;
                }
                value = b2;
            }
            XUpFragment.this.mBalanceSeekBar.setProgress(Math.round((1.0f - (value + 128) / 255.0f) * XUpFragment.this.mBalanceSeekBar.getMax()));
        }
    }
}
