// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import java.util.List;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.animation.Animation;
import com.logitech.ue.other.ViewExpandAnimation;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import com.logitech.ue.other.OnGlobalLayoutSelfRemovingListener;
import android.widget.ProgressBar;
import android.widget.ImageView;
import com.logitech.ue.views.FadeButton;
import android.widget.RelativeLayout;
import com.logitech.ue.centurion.exceptions.UEErrorResultException;
import com.logitech.ue.centurion.device.devicedata.UEAckResponse;
import java.util.Collection;
import java.util.ArrayList;
import com.logitech.ue.tasks.SafeTask;
import android.os.AsyncTask$Status;
import com.logitech.ue.centurion.device.devicedata.UEAVRCP;
import com.logitech.ue.tasks.GetPlaybackMetadataTask;
import com.logitech.ue.utils.AnimationUtils;
import com.logitech.ue.centurion.device.devicedata.UEPartyConnectionStatus;
import butterknife.OnTouch;
import com.logitech.ue.tasks.AdjustDeviceVolumeTask;
import android.view.MotionEvent;
import android.annotation.TargetApi;
import butterknife.ButterKnife;
import butterknife.OnClick;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import android.animation.AnimatorSet$Builder;
import com.logitech.ue.centurion.exceptions.UEException;
import com.logitech.ue.other.BlockPartyUtils;
import com.logitech.ue.centurion.device.devicedata.UEBlockPartyState;
import java.util.Iterator;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import com.logitech.ue.UEColourHelper;
import com.logitech.ue.centurion.device.devicedata.UEDeviceType;
import com.logitech.ue.tasks.GetDeviceNameTask;
import com.logitech.ue.centurion.device.UEGenericDevice;
import android.os.AsyncTask;
import com.logitech.ue.centurion.UEDeviceManager;
import android.os.Handler;
import com.logitech.ue.tasks.SetDeviceBroadcastTask;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastAudioOptions;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastState;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastConfiguration;
import com.logitech.ue.activities.MainActivity;
import com.logitech.ue.tasks.StopRestreamingTask;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStreamingStatus;
import android.content.DialogInterface$OnDismissListener;
import com.logitech.ue.tasks.GetDeviceStreamingStatusTask;
import com.logitech.ue.centurion.exceptions.UEUnrecognisedCommandException;
import com.logitech.ue.tasks.GetDeviceBroadcastTask;
import android.view.ViewGroup$LayoutParams;
import android.view.View$OnClickListener;
import com.logitech.ue.Utils;
import android.content.res.Resources$Theme;
import android.util.TypedValue;
import com.logitech.ue.centurion.device.devicedata.UEPlaybackMetadataType;
import com.logitech.ue.App;
import com.logitech.ue.centurion.notification.UETrackLengthInfoNotification;
import com.logitech.ue.FlurryTracker;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;
import android.util.Log;
import android.content.Intent;
import android.content.Context;
import java.util.HashSet;
import android.widget.LinearLayout;
import java.util.LinkedHashMap;
import java.util.Set;
import android.widget.TextView;
import java.util.LinkedList;
import android.animation.Animator;
import java.util.HashMap;
import android.view.View;
import butterknife.Bind;
import android.widget.Button;
import com.logitech.ue.centurion.utils.MAC;
import android.content.BroadcastReceiver;
import com.logitech.ue.centurion.device.devicedata.UEBlockPartyInfo;
import com.logitech.ue.tasks.GetConnectedDeviceNameTask;
import com.logitech.ue.interfaces.IPage;
import android.support.v4.app.Fragment;

public class BlockPartyFragment extends Fragment implements IPage
{
    public static final String ACTION_PARTY_MODE_STATE_CHANGED = "com.logitech.ue.fragments.PARTY_MODE_STATE_CHANGED";
    private static final int MAX_SELF_UPDATES = 7;
    public static final String PARAM_SOURCE_KEY = "source";
    public static final String PARAM_STATE_KEY = "state";
    public static final String PLAYBACK_PARAM_TRACK_ELAPSED = "TRACK_ELAPSED";
    public static final String PLAYBACK_PARAM_TRACK_LENGTH = "TRACK_LENGTH";
    private static final int PROGRESS_UPDATE_MS = 1000;
    public static final int SHORT_ANIMATION_TIME = 300;
    public static final String TAG;
    private GetConnectedDeviceNameTask mBeginConnectedDeviceNameTask;
    private UEBlockPartyInfo mBlockPartyInfo;
    final BroadcastReceiver mBroadcastReceiver;
    private MAC mCurrentStreamingDeviceAddress;
    @Bind({ 2131624182 })
    Button mEndPartyButton;
    private boolean mExpanded;
    @Bind({ 2131624189 })
    View mHorizontalLine;
    private final HashMap<MAC, String> mMACAddressNameMap;
    private Animator mMainAnimator;
    private LinkedList<PartyMemberCellHolder> mMemberCellHolderList;
    private int mPartyAccentColor;
    @Bind({ 2131624186 })
    View mPartyIntroContainer;
    @Bind({ 2131624185 })
    TextView mPartyJoinHintLabel;
    @Bind({ 2131624177 })
    View mPartyMainContainer;
    @Bind({ 2131624187 })
    TextView mPartyQuoteLabel;
    @Bind({ 2131624191 })
    TextView mPartyStartHintLabel;
    private State mPartyState;
    private final Set<MAC> mSessionGuestsSet;
    private final LinkedHashMap<MAC, HashMap<String, Object>> mSessionMembersInfoMap;
    private int mSessionPausePlayCount;
    private long mSessionStartTime;
    private int mSessionStreamSwitchCount;
    @Bind({ 2131624188 })
    View mStartPartyButton;
    private boolean mStreamingFile;
    private int mUpdatesCount;
    @Bind({ 2131624190 })
    View mVerticalLine;
    @Bind({ 2131624166 })
    LinearLayout mVolumeControl;
    @Bind({ 2131624184 })
    View mVolumeMinusButton;
    @Bind({ 2131624183 })
    View mVolumePlusButton;
    
    static {
        TAG = BlockPartyFragment.class.getSimpleName();
    }
    
    public BlockPartyFragment() {
        this.mSessionGuestsSet = new HashSet<MAC>();
        this.mSessionStartTime = 0L;
        this.mSessionPausePlayCount = 0;
        this.mSessionStreamSwitchCount = 0;
        this.mPartyState = State.Enable;
        this.mMemberCellHolderList = new LinkedList<PartyMemberCellHolder>();
        this.mMainAnimator = null;
        this.mBlockPartyInfo = null;
        this.mCurrentStreamingDeviceAddress = null;
        this.mMACAddressNameMap = new HashMap<MAC, String>();
        this.mSessionMembersInfoMap = new LinkedHashMap<MAC, HashMap<String, Object>>();
        this.mBeginConnectedDeviceNameTask = null;
        this.mExpanded = false;
        this.mUpdatesCount = 0;
        this.mStreamingFile = false;
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getAction().equals("com.logitech.ue.centurion.CONNECTION_CHANGED")) {
                    Log.d(BlockPartyFragment.TAG, "Device connection status changed");
                    if (UEDeviceStatus.getStatus(intent.getIntExtra("status", UEDeviceStatus.getValue(UEDeviceStatus.DISCONNECTED))).isBtClassicConnectedState()) {
                        BlockPartyFragment.this.beginAsyncUpdate(true);
                        BlockPartyFragment.this.beginAsyncUpdateHintLabel();
                    }
                    else {
                        if (BlockPartyFragment.this.mPartyState == State.Normal) {
                            FlurryTracker.logBlockPartyHostLeft();
                        }
                        BlockPartyFragment.this.showDisabledParty();
                        BlockPartyFragment.this.clearBlockPartySessionData();
                    }
                }
                else if (intent.getAction().equals("com.logitech.ue.centurion.BLOCK_PARTY_CHANGE_STATE_NOTIFICATION")) {
                    Log.d(BlockPartyFragment.TAG, "Party state changed");
                    if (BlockPartyFragment.this.mMainAnimator == null) {
                        BlockPartyFragment.this.beginAsyncUpdate(true);
                    }
                    else {
                        BlockPartyFragment.this.beginPartyInfoUpdate();
                    }
                }
                else if (intent.getAction().equals("com.logitech.ue.centurion.TRACK_LENGTH_INFO_NOTIFICATION")) {
                    BlockPartyFragment.this.mUpdatesCount = 0;
                    BlockPartyFragment.this.mStreamingFile = true;
                    final UETrackLengthInfoNotification ueTrackLengthInfoNotification = (UETrackLengthInfoNotification)intent.getExtras().getParcelable("notification");
                    if (BlockPartyFragment.this.mCurrentStreamingDeviceAddress != null && ueTrackLengthInfoNotification != null) {
                        if (!BlockPartyFragment.this.mSessionMembersInfoMap.containsKey(BlockPartyFragment.this.mCurrentStreamingDeviceAddress)) {
                            BlockPartyFragment.this.mSessionMembersInfoMap.put(BlockPartyFragment.this.mCurrentStreamingDeviceAddress, new HashMap());
                        }
                        final HashMap hashMap = BlockPartyFragment.this.mSessionMembersInfoMap.get(App.getInstance().getBluetoothMacAddress());
                        if ((hashMap != null && (!hashMap.containsKey(UEPlaybackMetadataType.TITLE.toString()) || !hashMap.containsKey(UEPlaybackMetadataType.ARTIST.toString()) || !hashMap.containsKey(UEPlaybackMetadataType.ALBUM.toString()))) || ueTrackLengthInfoNotification.getTrackLength() == 0 || ueTrackLengthInfoNotification.getTrackElapsed() == 0) {
                            BlockPartyFragment.this.beginAsyncUpdate(true);
                        }
                        else {
                            BlockPartyFragment.this.mSessionMembersInfoMap.get(BlockPartyFragment.this.mCurrentStreamingDeviceAddress).put("TRACK_LENGTH", ueTrackLengthInfoNotification.getTrackLength());
                            BlockPartyFragment.this.mSessionMembersInfoMap.get(BlockPartyFragment.this.mCurrentStreamingDeviceAddress).put("TRACK_ELAPSED", ueTrackLengthInfoNotification.getTrackElapsed());
                            BlockPartyFragment.this.updateMembers();
                        }
                    }
                }
                else if (intent.getAction().equals("com.logitech.ue.fragments.PARTY_MODE_STATE_CHANGED")) {
                    final String string = intent.getExtras().getString("source");
                    if (string != null && BlockPartyFragment.class.getSimpleName().compareTo(string) != 0) {
                        BlockPartyFragment.this.beginAsyncUpdate(false);
                    }
                }
            }
        };
    }
    
    private boolean guestIsStreaming(final MAC mac) {
        return this.mCurrentStreamingDeviceAddress != null && this.mCurrentStreamingDeviceAddress.equals(mac);
    }
    
    private boolean hostIsStreaming() {
        return this.guestIsStreaming(App.getInstance().getBluetoothMacAddress());
    }
    
    private void initColors() {
        final TypedValue typedValue = new TypedValue();
        final Resources$Theme theme = this.getActivity().getTheme();
        theme.resolveAttribute(2130771971, typedValue, true);
        this.mPartyAccentColor = typedValue.data;
        theme.resolveAttribute(16842800, typedValue, true);
    }
    
    private void initPartyUI() {
        for (int i = 0; i < this.mMemberCellHolderList.size(); ++i) {
            final PartyMemberCellHolder partyMemberCellHolder = this.mMemberCellHolderList.get(i);
            if (partyMemberCellHolder.mAnimator != null) {
                Utils.stopAnimator(partyMemberCellHolder.mAnimator);
                partyMemberCellHolder.mAnimator = null;
                if (partyMemberCellHolder.mRoot.getMeasuredHeight() < this.getResources().getDimensionPixelSize(2131361909)) {
                    final ViewGroup$LayoutParams layoutParams = partyMemberCellHolder.mRoot.getLayoutParams();
                    layoutParams.height = this.getResources().getDimensionPixelSize(2131361909);
                    partyMemberCellHolder.mRoot.setLayoutParams(layoutParams);
                }
            }
            partyMemberCellHolder.mKickMemberButton.setOnClickListener((View$OnClickListener)null);
            partyMemberCellHolder.mPlayButton.setOnClickListener((View$OnClickListener)null);
            if (i == 0) {
                this.initHostCell(partyMemberCellHolder);
            }
            else if (this.mBlockPartyInfo != null && this.mBlockPartyInfo.getGuestCount() > i - 1) {
                this.updateGuestCell(partyMemberCellHolder, this.mBlockPartyInfo.getGuestInfo(i - 1), i - 1);
            }
            else {
                this.updateGuestCell(partyMemberCellHolder, null, i - 1);
            }
        }
    }
    
    private void tryToStart() {
        if (this.mPartyState == State.Enable) {
            Log.d(BlockPartyFragment.TAG, "Check X-UP state before Block Party start");
            ((SafeTask<Void, Progress, Result>)new GetDeviceBroadcastTask() {
                @Override
                public void onError(final Exception ex) {
                    super.onError(ex);
                    if (ex instanceof UEUnrecognisedCommandException) {
                        Log.d(BlockPartyFragment.TAG, "Device doesn't support X-UP. Check Double Up state");
                        ((SafeTask<Void, Progress, Result>)new GetDeviceStreamingStatusTask() {
                            @Override
                            public void onError(final Exception ex) {
                                super.onError(ex);
                                Log.e(BlockPartyFragment.TAG, "Failed to check if DoubleUp is supported. Can't start Block Party");
                                App.getInstance().showMessageDialog(BlockPartyFragment.this.getString(2131165228), null);
                            }
                            
                            public void onSuccess(final UEDeviceStreamingStatus ueDeviceStreamingStatus) {
                                super.onSuccess((T)ueDeviceStreamingStatus);
                                if (ueDeviceStreamingStatus.isDevicePairedStatus()) {
                                    Log.d(BlockPartyFragment.TAG, "Device is in Double-UP. Show shutdown DoubleUP dialog");
                                    App.getInstance().showAlertDialog(BlockPartyFragment.this.getString(2131165236), 2131165234, 2131165260, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                                        public void onClick(final DialogInterface dialogInterface, final int n) {
                                            if (n == -1) {
                                                ((SafeTask<Void, Progress, Result>)new StopRestreamingTask() {
                                                    @Override
                                                    public void onError(final Exception ex) {
                                                        super.onError(ex);
                                                        App.getInstance().showMessageDialog(BlockPartyFragment.this.getString(2131165256), null);
                                                    }
                                                    
                                                    public void onSuccess(final Void void1) {
                                                        super.onSuccess((T)void1);
                                                        BlockPartyFragment.this.startBlockParty();
                                                    }
                                                }).executeOnThreadPoolExecutor(new Void[0]);
                                            }
                                            else {
                                                ((MainActivity)BlockPartyFragment.this.getActivity()).changeTab(1, true);
                                            }
                                        }
                                    });
                                }
                                else {
                                    BlockPartyFragment.this.startBlockParty();
                                }
                            }
                        }).executeOnThreadPoolExecutor(new Void[0]);
                    }
                    else {
                        Log.e(BlockPartyFragment.TAG, "Failed to check if X-UP is supported. Can't start Block Party");
                        App.getInstance().showMessageDialog(BlockPartyFragment.this.getString(2131165228), null);
                    }
                }
                
                public void onSuccess(final UEBroadcastConfiguration ueBroadcastConfiguration) {
                    super.onSuccess((T)ueBroadcastConfiguration);
                    if (ueBroadcastConfiguration.getBroadcastState() != UEBroadcastState.DISABLE) {
                        Log.d(BlockPartyFragment.TAG, "Device in X-UP. Show shutdown X-UP dialog");
                        App.getInstance().showAlertDialog(BlockPartyFragment.this.getString(2131165237), 2131165234, 2131165261, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                            public void onClick(final DialogInterface dialogInterface, final int n) {
                                if (n == -1) {
                                    ((SafeTask<Void, Progress, Result>)new SetDeviceBroadcastTask(UEBroadcastState.DISABLE, UEBroadcastAudioOptions.MULTIPLE) {
                                        @Override
                                        public void onError(final Exception ex) {
                                            super.onError(ex);
                                            App.getInstance().showMessageDialog(BlockPartyFragment.this.getString(2131165469), null);
                                            FlurryTracker.logXUPError("End fail");
                                        }
                                        
                                        public void onSuccess(final Void void1) {
                                            super.onSuccess((T)void1);
                                            FlurryTracker.logXUPEndSession();
                                            new Handler().postDelayed((Runnable)new Runnable() {
                                                @Override
                                                public void run() {
                                                    BlockPartyFragment.this.startBlockParty();
                                                }
                                            }, 2000L);
                                        }
                                    }).executeOnThreadPoolExecutor(new Void[0]);
                                }
                                else {
                                    ((MainActivity)BlockPartyFragment.this.getActivity()).changeTab(1, true);
                                }
                            }
                        });
                    }
                    else {
                        BlockPartyFragment.this.startBlockParty();
                    }
                }
            }).executeOnThreadPoolExecutor(new Void[0]);
        }
    }
    
    public void beginAsyncUpdate(final boolean b) {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        if (connectedDevice != null && connectedDevice.getDeviceConnectionStatus().isBtClassicConnectedState()) {
            Log.d(BlockPartyFragment.TAG, "Begin update party info task");
            new AsyncUpdateDataTask(b) {
                @Override
                public void onError(final Exception ex) {
                    super.onError(ex);
                    BlockPartyFragment.this.showDisabledParty();
                }
                
                @Override
                public void onSuccess(final Object o) {
                    super.onSuccess(o);
                    BlockPartyFragment.this.updateUI(BlockPartyFragment.this.mBlockPartyInfo);
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
        }
        else {
            Log.d(BlockPartyFragment.TAG, "Device not found drop party info");
            this.mBlockPartyInfo = null;
            this.showDisabledParty();
        }
    }
    
    public void beginAsyncUpdateHintLabel() {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        if (connectedDevice != null && connectedDevice.getDeviceConnectionStatus().isBtClassicConnectedState()) {
            Log.d(BlockPartyFragment.TAG, "Begin update hint text");
            ((SafeTask<Void, Progress, Result>)new GetDeviceNameTask() {
                @Override
                public void onError(final Exception ex) {
                    super.onError(ex);
                    if (BlockPartyFragment.this.getActivity() != null) {
                        BlockPartyFragment.this.mPartyJoinHintLabel.setText((CharSequence)BlockPartyFragment.this.getString(2131165368, UEColourHelper.getDeviceSpecificName(UEDeviceType.Maximus, BlockPartyFragment.this.getContext())));
                    }
                }
                
                public void onSuccess(final String s) {
                    super.onSuccess((T)s);
                    if (BlockPartyFragment.this.getActivity() != null) {
                        BlockPartyFragment.this.mPartyJoinHintLabel.setText((CharSequence)BlockPartyFragment.this.getString(2131165368, s));
                    }
                }
            }).executeOnThreadPoolExecutor(new Void[0]);
        }
        else {
            Log.d(BlockPartyFragment.TAG, "Can't update hint text. Use default speaker name");
            this.mPartyJoinHintLabel.setText((CharSequence)this.getString(2131165368, UEColourHelper.getDeviceSpecificName(UEDeviceType.Kora, this.getContext())));
        }
    }
    
    public void beginPartyInfoUpdate() {
        Log.d(BlockPartyFragment.TAG, "Begin update party info task without UI update");
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        if (connectedDevice != null && connectedDevice.getDeviceConnectionStatus().isBtClassicConnectedState()) {
            new AsyncUpdateDataTask(false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
        }
        else {
            this.mBlockPartyInfo = null;
        }
    }
    
    public Animator buildStepOneEndAnimation() {
        final AnimatorSet set = new AnimatorSet();
        final ObjectAnimator setDuration = ObjectAnimator.ofFloat((Object)this.mEndPartyButton, View.ALPHA, new float[] { 0.0f }).setDuration(300L);
        final ObjectAnimator setDuration2 = ObjectAnimator.ofFloat((Object)this.mPartyJoinHintLabel, View.ALPHA, new float[] { 0.0f }).setDuration(300L);
        final ObjectAnimator setDuration3 = ObjectAnimator.ofFloat((Object)this.mVolumeControl, View.ALPHA, new float[] { 0.0f }).setDuration(300L);
        final AnimatorSet set2 = new AnimatorSet();
        set2.playTogether(new Animator[] { (Animator)setDuration2, (Animator)setDuration3, (Animator)setDuration });
        final PartyMemberCellHolder partyMemberCellHolder = this.mMemberCellHolderList.get(0);
        final PartyMemberCellHolder partyMemberCellHolder2 = this.mMemberCellHolderList.get(1);
        final ObjectAnimator setDuration4 = ObjectAnimator.ofFloat((Object)partyMemberCellHolder2.mRoot, View.TRANSLATION_Y, new float[] { partyMemberCellHolder.mRoot.getY() - partyMemberCellHolder2.mRoot.getY() }).setDuration(300L);
        final ObjectAnimator setDuration5 = ObjectAnimator.ofFloat((Object)partyMemberCellHolder2.mMemberInfoContainer, View.ALPHA, new float[] { 0.0f }).setDuration(300L);
        final PartyMemberCellHolder partyMemberCellHolder3 = this.mMemberCellHolderList.get(2);
        final ObjectAnimator setDuration6 = ObjectAnimator.ofFloat((Object)partyMemberCellHolder3.mRoot, View.TRANSLATION_Y, new float[] { partyMemberCellHolder2.mRoot.getY() - partyMemberCellHolder3.mRoot.getY() }).setDuration(300L);
        setDuration6.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                super.onAnimationEnd(animator);
                partyMemberCellHolder3.mRoot.setVisibility(4);
            }
        });
        final ObjectAnimator setDuration7 = ObjectAnimator.ofFloat((Object)partyMemberCellHolder3.mMemberInfoContainer, View.ALPHA, new float[] { 0.0f }).setDuration(300L);
        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                Log.d(BlockPartyFragment.TAG, "End END animation step ONE");
                super.onAnimationEnd(animator);
                partyMemberCellHolder2.mRoot.setVisibility(4);
                partyMemberCellHolder2.mRoot.setTranslationY(0.0f);
                partyMemberCellHolder2.mMemberInfoContainer.setAlpha(1.0f);
                partyMemberCellHolder3.mRoot.setVisibility(4);
                partyMemberCellHolder3.mRoot.setTranslationY(0.0f);
                partyMemberCellHolder3.mMemberInfoContainer.setAlpha(1.0f);
                BlockPartyFragment.this.mEndPartyButton.setVisibility(4);
                BlockPartyFragment.this.mPartyJoinHintLabel.setVisibility(4);
                BlockPartyFragment.this.mEndPartyButton.setAlpha(1.0f);
                BlockPartyFragment.this.mPartyJoinHintLabel.setAlpha(1.0f);
            }
            
            public void onAnimationStart(final Animator animator) {
                Log.d(BlockPartyFragment.TAG, "Start END animation step ONE");
                super.onAnimationStart(animator);
                BlockPartyFragment.this.mVolumeControl.setEnabled(false);
            }
        });
        set.playSequentially(new Animator[] { (Animator)set2, (Animator)setDuration7, (Animator)setDuration6, (Animator)setDuration5, (Animator)setDuration4 });
        return (Animator)set;
    }
    
    public Animator buildStepOneStartAnimation() {
        final AnimatorSet set = new AnimatorSet();
        set.play((Animator)ObjectAnimator.ofFloat((Object)this.mStartPartyButton, View.ROTATION, new float[] { 720.0f }).setDuration(1200L)).before((Animator)ObjectAnimator.ofFloat((Object)this.mVerticalLine, View.SCALE_Y, new float[] { 0.0f }).setDuration(300L)).before((Animator)ObjectAnimator.ofFloat((Object)this.mHorizontalLine, View.SCALE_X, new float[] { this.mMemberCellHolderList.getFirst().mHostIndicator.getWidth() / (float)this.mHorizontalLine.getWidth() }).setDuration(300L));
        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                Log.d(BlockPartyFragment.TAG, "End START animation step ONE");
                super.onAnimationEnd(animator);
                BlockPartyFragment.this.mStartPartyButton.setRotation(0.0f);
                BlockPartyFragment.this.mVerticalLine.setScaleY(1.0f);
                BlockPartyFragment.this.mHorizontalLine.setScaleX(1.0f);
            }
            
            public void onAnimationStart(final Animator animator) {
                Log.d(BlockPartyFragment.TAG, "Start START animation step ONE");
                super.onAnimationStart(animator);
                BlockPartyFragment.this.mPartyStartHintLabel.setVisibility(4);
                BlockPartyFragment.this.mPartyQuoteLabel.setVisibility(4);
            }
        });
        return (Animator)set;
    }
    
    public Animator buildStepThreeEndAnimation() {
        final PartyMemberCellHolder partyMemberCellHolder = this.mMemberCellHolderList.getFirst();
        final AnimatorSet set = new AnimatorSet();
        final ObjectAnimator setDuration = ObjectAnimator.ofFloat((Object)this.mVerticalLine, View.SCALE_Y, new float[] { 0.0f, 1.0f }).setDuration(300L);
        final ObjectAnimator setDuration2 = ObjectAnimator.ofFloat((Object)this.mHorizontalLine, View.SCALE_X, new float[] { partyMemberCellHolder.mHostIndicator.getWidth() / (float)this.mHorizontalLine.getWidth(), 1.0f }).setDuration(300L);
        final ObjectAnimator setDuration3 = ObjectAnimator.ofFloat((Object)this.mStartPartyButton, View.ROTATION, new float[] { 720.0f, 0.0f }).setDuration(1200L);
        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                Log.d(BlockPartyFragment.TAG, "End END animation step THREE");
                super.onAnimationEnd(animator);
                BlockPartyFragment.this.mPartyStartHintLabel.setVisibility(0);
                BlockPartyFragment.this.mPartyQuoteLabel.setVisibility(0);
            }
            
            public void onAnimationStart(final Animator animator) {
                Log.d(BlockPartyFragment.TAG, "Start END animation step THREE");
                super.onAnimationStart(animator);
                BlockPartyFragment.this.mEndPartyButton.setVisibility(0);
                BlockPartyFragment.this.mPartyJoinHintLabel.setVisibility(0);
                final Iterator iterator = BlockPartyFragment.this.mMemberCellHolderList.iterator();
                while (iterator.hasNext()) {
                    iterator.next().mRoot.setVisibility(0);
                }
                BlockPartyFragment.this.mPartyMainContainer.setVisibility(4);
                BlockPartyFragment.this.mPartyIntroContainer.setVisibility(0);
                BlockPartyFragment.this.mPartyStartHintLabel.setVisibility(4);
                BlockPartyFragment.this.mPartyQuoteLabel.setVisibility(4);
            }
        });
        set.play((Animator)setDuration).with((Animator)setDuration2).before((Animator)setDuration3);
        return (Animator)set;
    }
    
    public Animator buildStepThreeStartAnimation() {
        final AnimatorSet set = new AnimatorSet();
        final PartyMemberCellHolder partyMemberCellHolder = this.mMemberCellHolderList.get(0);
        final PartyMemberCellHolder partyMemberCellHolder2 = this.mMemberCellHolderList.get(1);
        final float y = partyMemberCellHolder2.mRoot.getY();
        final ObjectAnimator setDuration = ObjectAnimator.ofFloat((Object)partyMemberCellHolder2.mRoot, View.Y, new float[] { partyMemberCellHolder.mRoot.getY(), partyMemberCellHolder2.mRoot.getY() }).setDuration(300L);
        final ObjectAnimator setDuration2 = ObjectAnimator.ofFloat((Object)partyMemberCellHolder2.mMemberInfoContainer, View.ALPHA, new float[] { 0.0f, 1.0f }).setDuration(300L);
        final PartyMemberCellHolder partyMemberCellHolder3 = this.mMemberCellHolderList.get(2);
        final ObjectAnimator setDuration3 = ObjectAnimator.ofFloat((Object)partyMemberCellHolder3.mRoot, View.Y, new float[] { y, partyMemberCellHolder3.mRoot.getY() }).setDuration(300L);
        setDuration3.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationStart(final Animator animator) {
                partyMemberCellHolder3.mRoot.setVisibility(0);
            }
        });
        final ObjectAnimator setDuration4 = ObjectAnimator.ofFloat((Object)partyMemberCellHolder3.mMemberInfoContainer, View.ALPHA, new float[] { 0.0f, 1.0f }).setDuration(300L);
        final ObjectAnimator setDuration5 = ObjectAnimator.ofFloat((Object)this.mEndPartyButton, View.ALPHA, new float[] { 0.0f, 1.0f }).setDuration(300L);
        final ObjectAnimator setDuration6 = ObjectAnimator.ofFloat((Object)this.mPartyJoinHintLabel, View.ALPHA, new float[] { 0.0f, 1.0f }).setDuration(300L);
        final AnimatorSet set2 = new AnimatorSet();
        set2.playTogether(new Animator[] { (Animator)setDuration6, (Animator)setDuration5 });
        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                Log.d(BlockPartyFragment.TAG, "End START animation step THREE");
                super.onAnimationEnd(animator);
            }
            
            public void onAnimationStart(final Animator animator) {
                Log.d(BlockPartyFragment.TAG, "Start START animation step THREE");
                super.onAnimationStart(animator);
                partyMemberCellHolder2.mRoot.setVisibility(0);
                partyMemberCellHolder2.mMemberInfoContainer.setAlpha(0.0f);
                partyMemberCellHolder3.mMemberInfoContainer.setAlpha(0.0f);
                BlockPartyFragment.this.mEndPartyButton.setVisibility(0);
                BlockPartyFragment.this.mPartyJoinHintLabel.setVisibility(0);
                BlockPartyFragment.this.mEndPartyButton.setAlpha(0.0f);
                BlockPartyFragment.this.mPartyJoinHintLabel.setAlpha(0.0f);
            }
        });
        set.playSequentially(new Animator[] { (Animator)setDuration, (Animator)setDuration2, (Animator)setDuration3, (Animator)setDuration4, (Animator)set2 });
        return (Animator)set;
    }
    
    public Animator buildStepTwoEndAnimation() {
        final AnimatorSet set = new AnimatorSet();
        final PartyMemberCellHolder partyMemberCellHolder = this.mMemberCellHolderList.get(0);
        final int[] array = new int[2];
        partyMemberCellHolder.mRoot.getLocationInWindow(array);
        final int[] array2 = new int[2];
        this.mHorizontalLine.getLocationInWindow(array2);
        final float y = partyMemberCellHolder.mArtworkContainer.getY();
        final ObjectAnimator setDuration = ObjectAnimator.ofFloat((Object)partyMemberCellHolder.mMemberInfoContainer, View.ALPHA, new float[] { 0.0f }).setDuration(300L);
        final ObjectAnimator setDuration2 = ObjectAnimator.ofFloat((Object)partyMemberCellHolder.mRoot, View.TRANSLATION_X, new float[] { (float)(array2[0] - array[0] + (this.mHorizontalLine.getWidth() - partyMemberCellHolder.mHostIndicator.getWidth()) / 2) }).setDuration(300L);
        final ObjectAnimator setDuration3 = ObjectAnimator.ofFloat((Object)partyMemberCellHolder.mRoot, View.TRANSLATION_Y, new float[] { (float)(array2[1] - array[1]) }).setDuration(300L);
        final ObjectAnimator setDuration4 = ObjectAnimator.ofFloat((Object)partyMemberCellHolder.mArtworkContainer, View.Y, new float[] { (float)(-partyMemberCellHolder.mArtworkContainer.getHeight()) }).setDuration(300L);
        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                Log.d(BlockPartyFragment.TAG, "End END animation step TWO");
                super.onAnimationEnd(animator);
                partyMemberCellHolder.mRoot.setVisibility(4);
                partyMemberCellHolder.mRoot.setTranslationY(0.0f);
                partyMemberCellHolder.mRoot.setTranslationX(0.0f);
                partyMemberCellHolder.mMemberInfoContainer.setAlpha(1.0f);
                partyMemberCellHolder.mArtworkContainer.setY(y);
            }
            
            public void onAnimationStart(final Animator animator) {
                Log.d(BlockPartyFragment.TAG, "Start END animation step TWO");
                super.onAnimationStart(animator);
            }
        });
        set.playSequentially(new Animator[] { (Animator)setDuration, (Animator)setDuration2, (Animator)setDuration3, (Animator)setDuration4 });
        return (Animator)set;
    }
    
    public Animator buildStepTwoStartAnimation() {
        final AnimatorSet set = new AnimatorSet();
        final PartyMemberCellHolder partyMemberCellHolder = this.mMemberCellHolderList.get(0);
        final ObjectAnimator setDuration = ObjectAnimator.ofFloat((Object)partyMemberCellHolder.mArtworkContainer, View.Y, new float[] { (float)(-partyMemberCellHolder.mArtworkContainer.getHeight()), partyMemberCellHolder.mArtworkContainer.getY() }).setDuration(300L);
        final ObjectAnimator setDuration2 = ObjectAnimator.ofFloat((Object)partyMemberCellHolder.mRoot, View.TRANSLATION_Y, new float[] { 0.0f }).setDuration(300L);
        final ObjectAnimator setDuration3 = ObjectAnimator.ofFloat((Object)partyMemberCellHolder.mRoot, View.TRANSLATION_X, new float[] { 0.0f }).setDuration(300L);
        final ObjectAnimator setDuration4 = ObjectAnimator.ofFloat((Object)partyMemberCellHolder.mMemberInfoContainer, View.ALPHA, new float[] { 1.0f }).setDuration(300L);
        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                Log.d(BlockPartyFragment.TAG, "End START animation step TWO");
                super.onAnimationEnd(animator);
            }
            
            public void onAnimationStart(final Animator animator) {
                Log.d(BlockPartyFragment.TAG, "Start START animation step TWO");
                super.onAnimationStart(animator);
                final Iterator iterator = BlockPartyFragment.this.mMemberCellHolderList.iterator();
                while (iterator.hasNext()) {
                    iterator.next().mRoot.setVisibility(4);
                }
                partyMemberCellHolder.mArtworkContainer.setVisibility(0);
                partyMemberCellHolder.mMemberInfoContainer.setVisibility(0);
                partyMemberCellHolder.mMemberInfoContainer.setAlpha(0.0f);
                partyMemberCellHolder.mRoot.setVisibility(0);
                BlockPartyFragment.this.mPartyIntroContainer.setVisibility(4);
                BlockPartyFragment.this.mPartyStartHintLabel.setVisibility(0);
                BlockPartyFragment.this.mPartyQuoteLabel.setVisibility(0);
                BlockPartyFragment.this.mPartyMainContainer.setVisibility(0);
                BlockPartyFragment.this.mEndPartyButton.setVisibility(4);
                BlockPartyFragment.this.mPartyJoinHintLabel.setVisibility(4);
                final int[] array = new int[2];
                partyMemberCellHolder.mRoot.getLocationInWindow(array);
                final int[] array2 = new int[2];
                BlockPartyFragment.this.mHorizontalLine.getLocationInWindow(array2);
                partyMemberCellHolder.mRoot.setTranslationX((float)(array2[0] - array[0] + (BlockPartyFragment.this.mHorizontalLine.getWidth() - partyMemberCellHolder.mHostIndicator.getWidth()) / 2));
                partyMemberCellHolder.mRoot.setTranslationY((float)(array2[1] - array[1]));
            }
        });
        set.playSequentially(new Animator[] { (Animator)setDuration, (Animator)setDuration2, (Animator)setDuration3, (Animator)setDuration4 });
        return (Animator)set;
    }
    
    public void clearBlockPartySessionData() {
        this.mMACAddressNameMap.clear();
        this.mSessionMembersInfoMap.clear();
        this.mSessionGuestsSet.clear();
        this.mSessionStartTime = 0L;
        this.mSessionPausePlayCount = 0;
        this.mSessionStreamSwitchCount = 0;
    }
    
    public void endBlockParty() {
        Log.d(BlockPartyFragment.TAG, "End party");
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        if (!App.getDeviceConnectionState().isBtClassicConnectedState() || connectedDevice == null) {
            return;
        }
        int i = 0;
        try {
            while (i < this.mBlockPartyInfo.getGuestCount()) {
                connectedDevice.kickMemberFromParty(this.mBlockPartyInfo.getGuestInfo(i).Address);
                ++i;
            }
            connectedDevice.setBlockPartyState(false);
            FlurryTracker.logBlockPartyEnd();
            if (this.mSessionStartTime != 0L) {
                FlurryTracker.logBlockPartySessionDuration((System.currentTimeMillis() - this.mSessionStartTime) % 1000L);
                FlurryTracker.logBlockPartyPlayPausePerSession(this.mSessionPausePlayCount);
                FlurryTracker.logBlockPartyUniqueMembers(this.mSessionGuestsSet.size());
                FlurryTracker.logBlockPartyDJSwitch(this.mSessionStreamSwitchCount);
            }
            this.playEndPartyAnimation();
            this.clearBlockPartySessionData();
            BlockPartyUtils.broadcastBlackPartyStateChanged(UEBlockPartyState.OFF, BlockPartyFragment.class.getSimpleName(), this.getContext());
        }
        catch (UEException ex) {
            App.getInstance().showMessageDialog(this.getResources().getString(2131165229), null);
        }
    }
    
    @Override
    public int getAccentColor() {
        return this.mPartyAccentColor;
    }
    
    public State getState() {
        return this.mPartyState;
    }
    
    @Override
    public String getTitle() {
        return App.getInstance().getString(2131165230);
    }
    
    public void initHostCell(final PartyMemberCellHolder partyMemberCellHolder) {
        if (this.hostIsStreaming()) {
            this.showStreamingCell(partyMemberCellHolder, null);
        }
        else {
            this.showPausedCell(partyMemberCellHolder);
        }
        partyMemberCellHolder.mKickMemberButton.setVisibility(8);
    }
    
    public boolean isAnimationRunning() {
        return this.mMainAnimator != null && this.mMainAnimator.isRunning();
    }
    
    public void kickGuest(final int n) {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        if (connectedDevice == null || !connectedDevice.getDeviceConnectionStatus().isBtClassicConnectedState() || this.mBlockPartyInfo == null || this.mBlockPartyInfo.getGuestCount() < n) {
            Log.d(BlockPartyFragment.TAG, "Can't kick guest \u2116" + n);
        }
        else {
            Log.d(BlockPartyFragment.TAG, "Kick guest \u2116" + n);
            FlurryTracker.logBlockPartyMemberRemove();
            final UEBlockPartyInfo.UEPartyMemberInfo guestInfo = this.mBlockPartyInfo.getGuestInfo(n);
            final PartyMemberCellHolder partyMemberCellHolder = this.mMemberCellHolderList.get(n + 1);
            partyMemberCellHolder.mRoot.setPivotY(0.0f);
            final AnimatorSet mMainAnimator = new AnimatorSet();
            final AnimatorSet$Builder play = mMainAnimator.play((Animator)ObjectAnimator.ofFloat((Object)partyMemberCellHolder.mRoot, View.SCALE_Y, new float[] { 0.0f }).setDuration(300L));
            for (int i = n + 2; i < this.mMemberCellHolderList.size(); ++i) {
                play.with((Animator)ObjectAnimator.ofFloat((Object)this.mMemberCellHolderList.get(i).mRoot, View.TRANSLATION_Y, new float[] { (float)(-this.mMemberCellHolderList.get(i).mRoot.getHeight()) }).setDuration(300L));
            }
            this.mBlockPartyInfo.getGuests().remove(n);
            final AsyncTask<Object, Object, Object> asyncTask = new AsyncTask<Object, Object, Object>() {
                protected Object doInBackground(final Object[] array) {
                    try {
                        connectedDevice.kickMemberFromParty(guestInfo.Address);
                        return null;
                    }
                    catch (UEException ex) {
                        return ex;
                    }
                }
                
                protected void onPostExecute(final Object o) {
                    if (o instanceof UEException) {
                        Log.e(BlockPartyFragment.TAG, "Failed to kick a guest from party", (Throwable)o);
                        App.getInstance().showMessageDialog(BlockPartyFragment.this.getString(2131165273), null);
                        BlockPartyFragment.this.beginAsyncUpdate(false);
                    }
                }
            };
            (this.mMainAnimator = (Animator)mMainAnimator).addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    super.onAnimationEnd(animator);
                    Log.d(BlockPartyFragment.TAG, "End KICK animation for guest \u2116" + n);
                    BlockPartyFragment.this.mMainAnimator = null;
                    partyMemberCellHolder.mRoot.setPivotY((float)(partyMemberCellHolder.mRoot.getHeight() / 2));
                    partyMemberCellHolder.mRoot.setScaleY(1.0f);
                    for (int i = n + 2; i < BlockPartyFragment.this.mMemberCellHolderList.size(); ++i) {
                        ((PartyMemberCellHolder)BlockPartyFragment.this.mMemberCellHolderList.get(i)).mRoot.setTranslationY(0.0f);
                    }
                    BlockPartyFragment.this.updateUI(BlockPartyFragment.this.mBlockPartyInfo);
                }
                
                public void onAnimationStart(final Animator animator) {
                    super.onAnimationStart(animator);
                    Log.d(BlockPartyFragment.TAG, "Start KICK animation for guest \u2116" + n);
                }
            });
            this.mMainAnimator.start();
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[0]);
        }
    }
    
    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        Log.d(BlockPartyFragment.TAG, "OnAttach");
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.logitech.ue.centurion.CONNECTION_CHANGED");
        intentFilter.addAction("com.logitech.ue.centurion.BLOCK_PARTY_CHANGE_STATE_NOTIFICATION");
        intentFilter.addAction("com.logitech.ue.centurion.TRACK_LENGTH_INFO_NOTIFICATION");
        intentFilter.addAction("com.logitech.ue.fragments.PARTY_MODE_STATE_CHANGED");
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(this.mBroadcastReceiver, intentFilter);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(2130968638, viewGroup, false);
    }
    
    @Override
    public void onDeselected() {
        Log.d(BlockPartyFragment.TAG, "Is deselected as Tab");
    }
    
    @Override
    public void onDetach() {
        Log.d(BlockPartyFragment.TAG, "OnDetach");
        LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(this.mBroadcastReceiver);
        super.onDetach();
    }
    
    @OnClick({ 2131624182 })
    public void onEndClicked(final View view) {
        if (!this.isAnimationRunning() && this.mPartyState == State.Normal) {
            this.endBlockParty();
        }
    }
    
    @Override
    public void onSelected() {
        Log.d(BlockPartyFragment.TAG, "Is selected as Tab");
    }
    
    @Override
    public void onStart() {
        super.onStart();
        Log.d(BlockPartyFragment.TAG, "onStart");
        this.beginAsyncUpdate(true);
        this.beginAsyncUpdateHintLabel();
    }
    
    @OnClick({ 2131624188 })
    public void onStartClicked(final View view) {
        if (!this.isAnimationRunning()) {
            if (!App.getDeviceConnectionState().isBtClassicConnectedState()) {
                Log.d(BlockPartyFragment.TAG, "Start clicked. But device is disconnected. Show dialog");
                App.getInstance().showMessageDialog(this.getString(2131165407), null);
            }
            else if (this.mPartyState == State.Disabled) {
                Log.d(BlockPartyFragment.TAG, "Start clicked. But party is locked. Show dialog");
                this.playUnlockBlockPartyAnimation();
            }
            else {
                this.tryToStart();
            }
        }
    }
    
    @Override
    public void onStop() {
        Log.d(BlockPartyFragment.TAG, "onStop");
        this.stopAnimation();
        super.onStop();
    }
    
    @Override
    public void onTransition(final float n) {
    }
    
    @TargetApi(15)
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        this.initColors();
        this.mMemberCellHolderList.clear();
        this.mMemberCellHolderList.add(new PartyMemberCellHolder(view.findViewById(2131624181)));
        this.mMemberCellHolderList.add(new PartyMemberCellHolder(view.findViewById(2131624180)));
        this.mMemberCellHolderList.add(new PartyMemberCellHolder(view.findViewById(2131624179)));
        for (int i = 1; i < this.mMemberCellHolderList.size(); ++i) {
            this.mMemberCellHolderList.get(i).mHostIndicator.setVisibility(4);
        }
        this.mVolumeControl.setEnabled(false);
    }
    
    @OnTouch({ 2131624184 })
    public boolean onVolumeMinusButtonTouch(final View view, final MotionEvent motionEvent) {
        final Handler handler = new Handler();
        if (this.mVolumeControl.isEnabled()) {
            switch (motionEvent.getAction()) {
                case 0: {
                    ((SafeTask<Void, Progress, Result>)new AdjustDeviceVolumeTask(true, false)).executeOnThreadPoolExecutor(new Void[0]);
                    handler.postDelayed((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            if (BlockPartyFragment.this.mVolumeMinusButton.isPressed()) {
                                ((SafeTask<Void, Progress, Result>)new AdjustDeviceVolumeTask(true, false)).executeOnThreadPoolExecutor(new Void[0]);
                                handler.postDelayed((Runnable)this, 200L);
                            }
                        }
                    }, 400L);
                    break;
                }
                case 1:
                case 3: {
                    handler.removeCallbacksAndMessages((Object)null);
                    break;
                }
            }
        }
        return this.mVolumeMinusButton.onTouchEvent(motionEvent);
    }
    
    @OnTouch({ 2131624183 })
    public boolean onVolumePlusButtonTouch(final View view, final MotionEvent motionEvent) {
        final Handler handler = new Handler();
        if (this.mVolumeControl.isEnabled()) {
            switch (motionEvent.getAction()) {
                case 0: {
                    ((SafeTask<Void, Progress, Result>)new AdjustDeviceVolumeTask(true, true)).executeOnThreadPoolExecutor(new Void[0]);
                    handler.postDelayed((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            if (BlockPartyFragment.this.mVolumePlusButton.isPressed()) {
                                ((SafeTask<Void, Progress, Result>)new AdjustDeviceVolumeTask(true, true)).executeOnThreadPoolExecutor(new Void[0]);
                                handler.postDelayed((Runnable)this, 200L);
                            }
                        }
                    }, 400L);
                    break;
                }
                case 1:
                case 3: {
                    handler.removeCallbacksAndMessages((Object)null);
                    break;
                }
            }
        }
        return this.mVolumePlusButton.onTouchEvent(motionEvent);
    }
    
    public void playEndPartyAnimation() {
        if (this.mMainAnimator != null) {
            Log.d(BlockPartyFragment.TAG, "Can't play END animation, because other animation is running");
        }
        else {
            Log.d(BlockPartyFragment.TAG, "Play END animation");
            this.mMainAnimator = (Animator)new AnimatorSet();
            ((AnimatorSet)this.mMainAnimator).playSequentially(new Animator[] { this.buildStepOneEndAnimation(), this.buildStepTwoEndAnimation(), this.buildStepThreeEndAnimation() });
            this.mMainAnimator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    Log.d(BlockPartyFragment.TAG, "End END animation");
                    super.onAnimationEnd(animator);
                    BlockPartyFragment.this.mMainAnimator = null;
                    BlockPartyFragment.this.beginAsyncUpdate(false);
                }
                
                public void onAnimationStart(final Animator animator) {
                    Log.d(BlockPartyFragment.TAG, "Start END animation");
                    super.onAnimationStart(animator);
                }
            });
            this.mMainAnimator.start();
        }
    }
    
    public void playStartPartyAnimation() {
        if (this.mMainAnimator != null) {
            Log.d(BlockPartyFragment.TAG, "Can't play START animation, because other animation is running");
        }
        else {
            Log.d(BlockPartyFragment.TAG, "Play START animation");
            this.mStartPartyButton.setActivated(true);
            this.mPartyIntroContainer.setVisibility(0);
            this.mPartyMainContainer.setVisibility(4);
            this.mPartyQuoteLabel.setVisibility(4);
            this.mPartyStartHintLabel.setVisibility(4);
            this.mMainAnimator = (Animator)new AnimatorSet();
            ((AnimatorSet)this.mMainAnimator).playSequentially(new Animator[] { this.buildStepOneStartAnimation(), this.buildStepTwoStartAnimation(), this.buildStepThreeStartAnimation() });
            this.mMainAnimator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    super.onAnimationEnd(animator);
                    Log.d(BlockPartyFragment.TAG, "End START animation");
                    BlockPartyFragment.this.mMainAnimator = null;
                    BlockPartyFragment.this.setState(State.Normal);
                    new Handler().postDelayed((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            BlockPartyFragment.this.beginAsyncUpdate(false);
                        }
                    }, 200L);
                }
                
                public void onAnimationStart(final Animator animator) {
                    super.onAnimationStart(animator);
                    Log.d(BlockPartyFragment.TAG, "Start START animation");
                }
            });
            this.mMainAnimator.start();
        }
    }
    
    public void playUnlockBlockPartyAnimation() {
        if (this.mMainAnimator != null) {
            Log.d(BlockPartyFragment.TAG, "Can't play UNLOCK animation, because other animation is running");
        }
        else {
            Log.d(BlockPartyFragment.TAG, "Play UNLOCK animation");
            (this.mMainAnimator = (Animator)ObjectAnimator.ofFloat((Object)this.mStartPartyButton, View.ROTATION, new float[] { 720.0f }).setDuration(1200L)).addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    super.onAnimationEnd(animator);
                    Log.d(BlockPartyFragment.TAG, "End UNLOCK animation");
                    BlockPartyFragment.this.mMainAnimator = null;
                    BlockPartyFragment.this.mStartPartyButton.setRotation(0.0f);
                    Log.d(BlockPartyFragment.TAG, "Show update dialog");
                    App.getInstance().showAlertDialog(BlockPartyFragment.this.getResources().getString(2131165231), 2131165340, 2131165440, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            if (n == -1) {
                                Log.d(BlockPartyFragment.TAG, "Update clicked. Go to More Screen for update");
                                FlurryTracker.logOTAUpdate();
                                ((MainActivity)BlockPartyFragment.this.getActivity()).beginOTAUpdate();
                            }
                            else {
                                Log.d(BlockPartyFragment.TAG, "Cancel clicked. Go to Home Screen Main Fragment");
                                FlurryTracker.logOTACancel();
                                ((MainActivity)BlockPartyFragment.this.getActivity()).changeTab(0, true);
                            }
                        }
                    });
                }
                
                public void onAnimationStart(final Animator animator) {
                    super.onAnimationStart(animator);
                    Log.d(BlockPartyFragment.TAG, "Start UNLOCK animation");
                }
            });
            this.mMainAnimator.start();
        }
    }
    
    public void setState(final State mPartyState) {
        this.mPartyState = mPartyState;
    }
    
    public void showDisabledParty() {
        Log.d(BlockPartyFragment.TAG, "Show disabled party");
        this.stopAnimation();
        this.setState(State.Disabled);
        this.mPartyIntroContainer.setVisibility(0);
        this.mPartyMainContainer.setVisibility(4);
        this.mPartyQuoteLabel.setVisibility(0);
        this.mPartyStartHintLabel.setVisibility(4);
        this.mStartPartyButton.setActivated(false);
        this.mVolumeControl.setEnabled(false);
        this.mVolumeControl.setAlpha(0.0f);
    }
    
    public void showEnableParty() {
        Log.d(BlockPartyFragment.TAG, "Show enable party");
        this.stopAnimation();
        this.setState(State.Enable);
        this.mPartyIntroContainer.setVisibility(0);
        this.mPartyMainContainer.setVisibility(4);
        this.mPartyQuoteLabel.setVisibility(0);
        this.mPartyStartHintLabel.setVisibility(0);
        this.mStartPartyButton.setActivated(true);
        this.mVolumeControl.setEnabled(false);
        this.mVolumeControl.setAlpha(0.0f);
    }
    
    public void showNormalParty() {
        Log.d(BlockPartyFragment.TAG, "Show normal party");
        this.stopAnimation();
        this.setState(State.Normal);
        this.mPartyIntroContainer.setVisibility(4);
        this.mPartyMainContainer.setVisibility(0);
        if (this.mBlockPartyInfo.getGuestCount() > 1 || (this.mBlockPartyInfo.getGuestCount() == 1 && this.mBlockPartyInfo.getGuestInfo(0).mConnectionStatus != UEPartyConnectionStatus.NOT_CONNECTED)) {
            this.mVolumeControl.setEnabled(true);
            this.mVolumeControl.animate().alpha(1.0f).setDuration(800L);
            this.mPartyJoinHintLabel.animate().alpha(0.0f).setDuration((long)AnimationUtils.getAndroidLongAnimationTime(this.getContext())).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    super.onAnimationEnd(animator);
                    BlockPartyFragment.this.mPartyJoinHintLabel.setVisibility(4);
                }
            });
        }
        this.updateMembers();
    }
    
    public void showPausedCell(final PartyMemberCellHolder partyMemberCellHolder) {
        switch (partyMemberCellHolder.mState) {
            default: {
                partyMemberCellHolder.showConnectedCell();
                break;
            }
            case Lost: {
                partyMemberCellHolder.showLostCell();
                break;
            }
        }
    }
    
    public void showStreamingCell(final PartyMemberCellHolder partyMemberCellHolder, final HashMap<String, Object> hashMap) {
        partyMemberCellHolder.mPlayIconImage.setImageResource(2130837712);
        switch (partyMemberCellHolder.mState) {
            default: {
                partyMemberCellHolder.showStreamingFileCell(hashMap);
                break;
            }
            case Lost: {
                partyMemberCellHolder.showLostCell();
                break;
            }
            case StreamingFile: {
                partyMemberCellHolder.showStreamingFileCell(hashMap);
                break;
            }
            case StreamingRadio: {
                partyMemberCellHolder.showStreamingRadioCell(hashMap);
                break;
            }
        }
    }
    
    public void startBlockParty() {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        if (!App.getDeviceConnectionState().isBtClassicConnectedState() || connectedDevice == null) {
            return;
        }
        try {
            connectedDevice.setBlockPartyState(true);
            FlurryTracker.logBlockPartyStart();
            this.mSessionStartTime = System.currentTimeMillis();
            this.playStartPartyAnimation();
            this.clearBlockPartySessionData();
            connectedDevice.setEnableNotificationsMask(connectedDevice.getEnableNotificationsMask() | 0x1);
            this.mBlockPartyInfo = new UEBlockPartyInfo();
            this.initPartyUI();
            BlockPartyUtils.broadcastBlackPartyStateChanged(UEBlockPartyState.ON, BlockPartyFragment.class.getSimpleName(), this.getContext());
        }
        catch (UEUnrecognisedCommandException ex) {
            Log.e(BlockPartyFragment.TAG, "Party not supported", (Throwable)ex);
            App.getInstance().showMessageDialog(this.getResources().getString(2131165232), null);
        }
        catch (UEException ex2) {
            Log.e(BlockPartyFragment.TAG, "Party  state failed", (Throwable)ex2);
            App.getInstance().showMessageDialog(this.getResources().getString(2131165228), null);
            FlurryTracker.logError(BlockPartyFragment.TAG, ex2.getMessage());
        }
    }
    
    public void stopAnimation() {
        if (this.mMainAnimator != null) {
            Log.d(BlockPartyFragment.TAG, "Stop animation");
            Utils.stopAnimator(this.mMainAnimator);
            this.mMainAnimator = null;
        }
    }
    
    public void updateDeviceMetadata(final MAC mac) {
        ((SafeTask<Void, Progress, Result>)new GetPlaybackMetadataTask(mac) {
            public void onSuccess(final HashMap<UEPlaybackMetadataType, Object> hashMap) {
                super.onSuccess((T)hashMap);
                if (!BlockPartyFragment.this.mSessionMembersInfoMap.containsKey(mac)) {
                    BlockPartyFragment.this.mSessionMembersInfoMap.put(mac, new HashMap());
                }
                final HashMap<String, Object> value = BlockPartyFragment.this.mSessionMembersInfoMap.get(mac);
                value.put(UEPlaybackMetadataType.TITLE.toString(), hashMap.get(UEPlaybackMetadataType.TITLE));
                value.put(UEPlaybackMetadataType.ARTIST.toString(), hashMap.get(UEPlaybackMetadataType.ARTIST));
                value.put(UEPlaybackMetadataType.ALBUM.toString(), hashMap.get(UEPlaybackMetadataType.ALBUM));
                BlockPartyFragment.this.mSessionMembersInfoMap.put(mac, value);
                BlockPartyFragment.this.updateMembers();
            }
        }).executeOnThreadPoolExecutor(new Void[0]);
    }
    
    public void updateGuestCell(final PartyMemberCellHolder partyMemberCellHolder, final UEBlockPartyInfo.UEPartyMemberInfo uePartyMemberInfo, final int i) {
        if (uePartyMemberInfo != null) {
            switch (uePartyMemberInfo.mConnectionStatus) {
                case NOT_CONNECTED: {
                    partyMemberCellHolder.showOpenCell();
                    partyMemberCellHolder.mPlayButton.setOnClickListener((View$OnClickListener)null);
                    partyMemberCellHolder.mSkipButton.setOnClickListener((View$OnClickListener)null);
                    partyMemberCellHolder.mKickMemberButton.setOnClickListener((View$OnClickListener)null);
                    break;
                }
                case LINK_LOSS: {
                    partyMemberCellHolder.showLostCell();
                    partyMemberCellHolder.mPlayButton.setOnClickListener((View$OnClickListener)null);
                    partyMemberCellHolder.mSkipButton.setOnClickListener((View$OnClickListener)null);
                    break;
                }
                case CONNECTED: {
                    partyMemberCellHolder.mPlayButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                        public void onClick(final View view) {
                            final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
                            Label_0187: {
                                if (connectedDevice == null || !connectedDevice.getDeviceConnectionStatus().isBtClassicConnectedState() || uePartyMemberInfo.mConnectionStatus != UEPartyConnectionStatus.CONNECTED) {
                                    break Label_0187;
                                }
                                try {
                                    UEAVRCP ueavrcp;
                                    if (BlockPartyFragment.this.mCurrentStreamingDeviceAddress != null && BlockPartyFragment.this.mCurrentStreamingDeviceAddress.equals(uePartyMemberInfo.Address)) {
                                        ueavrcp = UEAVRCP.PAUSE;
                                        FlurryTracker.logBlockPartyPlaybackPause();
                                    }
                                    else {
                                        ueavrcp = UEAVRCP.PLAY;
                                        FlurryTracker.logBlockPartyPlaybackPlay();
                                    }
                                    BlockPartyFragment.this.mSessionPausePlayCount++;
                                    Log.d(BlockPartyFragment.TAG, "Play clicked. Send  " + ueavrcp.name() + " to guest \u2116" + i);
                                    connectedDevice.sendAVRCPCommand(ueavrcp, uePartyMemberInfo.Address);
                                    return;
                                }
                                catch (UEException ex) {
                                    Log.e(BlockPartyFragment.TAG, "Failed to send PAUSE/PLAY AVRCP command to guest " + uePartyMemberInfo.Address, (Throwable)ex);
                                    return;
                                }
                            }
                            Log.w(BlockPartyFragment.TAG, "Play clicked. Can't send play to guest \u2116" + i);
                        }
                    });
                    partyMemberCellHolder.mKickMemberButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                        public void onClick(final View view) {
                            partyMemberCellHolder.mState = PartyMemberState.Open;
                            if (!BlockPartyFragment.this.isAnimationRunning()) {
                                BlockPartyFragment.this.kickGuest(i);
                            }
                        }
                    });
                    partyMemberCellHolder.mSkipButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                        public void onClick(final View view) {
                            final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
                            Label_0145: {
                                if (connectedDevice == null || !connectedDevice.getDeviceConnectionStatus().isBtClassicConnectedState()) {
                                    break Label_0145;
                                }
                                if (uePartyMemberInfo.mConnectionStatus != UEPartyConnectionStatus.CONNECTED) {
                                    if (uePartyMemberInfo.mConnectionStatus != UEPartyConnectionStatus.LINK_LOSS) {
                                        break Label_0145;
                                    }
                                }
                                try {
                                    Log.d(BlockPartyFragment.TAG, "Skip clicked. Skip track for guest \u2116" + i);
                                    FlurryTracker.logBlockPartyPlaybackSkip();
                                    connectedDevice.sendAVRCPCommand(UEAVRCP.SKIP_FORWARD, uePartyMemberInfo.Address);
                                    return;
                                }
                                catch (UEException ex) {
                                    Log.e(BlockPartyFragment.TAG, "Failed to send SKIP_FORWARD AVRCP command to guest " + uePartyMemberInfo.Address, (Throwable)ex);
                                    FlurryTracker.logError(BlockPartyFragment.TAG, ex.getMessage());
                                    return;
                                }
                            }
                            Log.w(BlockPartyFragment.TAG, "Skip clicked. Can't skip track for guest \u2116" + i);
                        }
                    });
                    if (this.mMACAddressNameMap.containsKey(uePartyMemberInfo.Address)) {
                        partyMemberCellHolder.mMemberNameLabel.setText((CharSequence)this.mMACAddressNameMap.get(uePartyMemberInfo.Address));
                    }
                    else {
                        if (this.mBeginConnectedDeviceNameTask == null || this.mBeginConnectedDeviceNameTask.getStatus() == AsyncTask$Status.FINISHED) {
                            ((SafeTask<Void, Progress, Result>)(this.mBeginConnectedDeviceNameTask = new GetConnectedDeviceNameTask(uePartyMemberInfo.Address) {
                                public void onSuccess(final String s) {
                                    super.onSuccess((T)s);
                                    Log.d(BlockPartyFragment.TAG, "Received device name. Device address: " + uePartyMemberInfo.Address + " Name: " + s);
                                    BlockPartyFragment.this.mMACAddressNameMap.put(uePartyMemberInfo.Address, s);
                                    BlockPartyFragment.this.updateMembers();
                                }
                            })).executeOnThreadPoolExecutor(new Void[0]);
                        }
                        partyMemberCellHolder.mMemberNameLabel.setText((CharSequence)this.getString(2131165246, Integer.toString(i)));
                    }
                    HashMap<String, Object> value;
                    if ((value = this.mSessionMembersInfoMap.get(uePartyMemberInfo.Address)) == null) {
                        value = new HashMap<String, Object>();
                        this.mSessionMembersInfoMap.put(uePartyMemberInfo.Address, value);
                    }
                    if (this.guestIsStreaming(uePartyMemberInfo.Address)) {
                        this.showStreamingCell(partyMemberCellHolder, value);
                        break;
                    }
                    this.showPausedCell(partyMemberCellHolder);
                    break;
                }
            }
        }
        else {
            partyMemberCellHolder.showOpenCell();
        }
    }
    
    public void updateHostCell(final PartyMemberCellHolder partyMemberCellHolder) {
        partyMemberCellHolder.mMemberNameLabel.setText((CharSequence)App.getInstance().getBluetoothName());
        HashMap<String, Object> value;
        if ((value = this.mSessionMembersInfoMap.get(App.getInstance().getBluetoothMacAddress())) == null) {
            value = new HashMap<String, Object>();
            this.mSessionMembersInfoMap.put(App.getInstance().getBluetoothMacAddress(), value);
        }
        final MAC bluetoothMacAddress = App.getInstance().getBluetoothMacAddress();
        partyMemberCellHolder.mSkipButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
                Label_0059: {
                    if (connectedDevice == null || !connectedDevice.getDeviceConnectionStatus().isBtClassicConnectedState()) {
                        break Label_0059;
                    }
                    try {
                        Log.d(BlockPartyFragment.TAG, "Skip clicked. Skip track for host");
                        FlurryTracker.logBlockPartyPlaybackSkip();
                        connectedDevice.sendAVRCPCommand(UEAVRCP.SKIP_FORWARD, bluetoothMacAddress);
                        return;
                    }
                    catch (UEException ex) {
                        Log.e(BlockPartyFragment.TAG, "Failed to send SKIP_FORWARD AVRCP command to host", (Throwable)ex);
                        return;
                    }
                }
                Log.w(BlockPartyFragment.TAG, "Skip clicked. Can't skip track for host");
            }
        });
        partyMemberCellHolder.mPlayButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
                Label_0144: {
                    if (connectedDevice == null || !connectedDevice.getDeviceConnectionStatus().isBtClassicConnectedState()) {
                        break Label_0144;
                    }
                    try {
                        UEAVRCP ueavrcp;
                        if (BlockPartyFragment.this.mCurrentStreamingDeviceAddress != null && App.getInstance().getBluetoothMacAddress().equals(BlockPartyFragment.this.mCurrentStreamingDeviceAddress)) {
                            ueavrcp = UEAVRCP.PAUSE;
                            FlurryTracker.logBlockPartyPlaybackPause();
                        }
                        else {
                            ueavrcp = UEAVRCP.PLAY;
                            FlurryTracker.logBlockPartyPlaybackPlay();
                        }
                        BlockPartyFragment.this.mSessionPausePlayCount++;
                        Log.d(BlockPartyFragment.TAG, "Play clicked. Send  " + ueavrcp.name() + " to host");
                        connectedDevice.sendAVRCPCommand(ueavrcp, bluetoothMacAddress);
                        return;
                    }
                    catch (UEException ex) {
                        Log.e(BlockPartyFragment.TAG, "Failed to send AVRCP command to host", (Throwable)ex);
                        ex.printStackTrace();
                        return;
                    }
                }
                Log.w(BlockPartyFragment.TAG, "Play clicked. Can't send play to host");
            }
        });
        if (this.hostIsStreaming()) {
            this.showStreamingCell(partyMemberCellHolder, value);
        }
        else {
            this.showPausedCell(partyMemberCellHolder);
        }
        partyMemberCellHolder.mKickMemberButton.setVisibility(8);
    }
    
    public void updateMembers() {
        Log.d(BlockPartyFragment.TAG, "Update members");
        for (int i = 0; i < this.mMemberCellHolderList.size(); ++i) {
            final PartyMemberCellHolder partyMemberCellHolder = this.mMemberCellHolderList.get(i);
            if (partyMemberCellHolder.mAnimator != null) {
                Utils.stopAnimator(partyMemberCellHolder.mAnimator);
                partyMemberCellHolder.mAnimator = null;
                if (partyMemberCellHolder.mRoot.getMeasuredHeight() < this.getResources().getDimensionPixelSize(2131361909)) {
                    final ViewGroup$LayoutParams layoutParams = partyMemberCellHolder.mRoot.getLayoutParams();
                    layoutParams.height = this.getResources().getDimensionPixelSize(2131361909);
                    partyMemberCellHolder.mRoot.setLayoutParams(layoutParams);
                }
            }
            if (i == 0) {
                this.updateHostCell(this.mMemberCellHolderList.get(i));
            }
            else if (this.mBlockPartyInfo != null && this.mBlockPartyInfo.getGuestCount() > i - 1) {
                this.updateGuestCell(this.mMemberCellHolderList.get(i), this.mBlockPartyInfo.getGuestInfo(i - 1), i - 1);
            }
            else {
                this.updateGuestCell(this.mMemberCellHolderList.get(i), null, i - 1);
            }
        }
    }
    
    public void updateUI(final UEBlockPartyInfo ueBlockPartyInfo) {
        if (this.getView() == null || this.mMainAnimator != null) {
            Log.e(BlockPartyFragment.TAG, "Can't update while animation is running or view doesn't exist");
        }
        else if (ueBlockPartyInfo == null) {
            Log.d(BlockPartyFragment.TAG, "Update IU. No Party info. Show connect device dialog");
            this.setState(State.Enable);
            this.showEnableParty();
        }
        else {
            Log.d(BlockPartyFragment.TAG, "Update IU. Party info is existing");
            this.setState(State.Normal);
            this.showNormalParty();
        }
    }
    
    public class AsyncUpdateDataTask extends SafeTask<Void, Void, Object>
    {
        private boolean mUpdateMetadata;
        
        public AsyncUpdateDataTask(final boolean mUpdateMetadata) {
            this.mUpdateMetadata = mUpdateMetadata;
        }
        
        @Override
        public String getTag() {
            return BlockPartyFragment.TAG + ".AsyncUpdateDataTask";
        }
        
        @Override
        public void onSuccess(Object o) {
            final int n = 0;
            super.onSuccess(o);
            if (o instanceof Object[]) {
                final Object[] array = (Object[])o;
                BlockPartyFragment.this.mBlockPartyInfo = (UEBlockPartyInfo)array[0];
                if (array[1] != null && BlockPartyFragment.this.mCurrentStreamingDeviceAddress != null && !BlockPartyFragment.this.mCurrentStreamingDeviceAddress.equals(array[1])) {
                    BlockPartyFragment.this.mSessionStreamSwitchCount++;
                }
                BlockPartyFragment.this.mCurrentStreamingDeviceAddress = (MAC)array[1];
                final Set keySet = BlockPartyFragment.this.mSessionMembersInfoMap.keySet();
                for (final MAC mac : (MAC[])keySet.toArray(new MAC[keySet.size()])) {
                    final int n2 = 1;
                    Label_0154: {
                        if (!mac.equals(App.getInstance().getBluetoothMacAddress())) {
                            int n3 = 0;
                            while (true) {
                                Label_0303: {
                                    int n4;
                                    while (true) {
                                        n4 = n2;
                                        if (n3 >= BlockPartyFragment.this.mBlockPartyInfo.getGuestCount()) {
                                            break;
                                        }
                                        if (mac.equals(BlockPartyFragment.this.mBlockPartyInfo.getGuestInfo(n3).Address)) {
                                            if (BlockPartyFragment.this.mBlockPartyInfo.getGuestInfo(n3).mConnectionStatus != UEPartyConnectionStatus.CONNECTED && BlockPartyFragment.this.mBlockPartyInfo.getGuestInfo(n3).mConnectionStatus != UEPartyConnectionStatus.LINK_LOSS) {
                                                n4 = 1;
                                                break;
                                            }
                                            break Label_0303;
                                        }
                                        else {
                                            ++n3;
                                        }
                                    }
                                    if (n4 != 0) {
                                        Log.d(BlockPartyFragment.TAG, "Remove " + mac + " from party cache info");
                                        BlockPartyFragment.this.mSessionMembersInfoMap.remove(mac);
                                    }
                                    break Label_0154;
                                }
                                int n4 = 0;
                                continue;
                            }
                        }
                    }
                }
                for (int j = 0; j < BlockPartyFragment.this.mBlockPartyInfo.getGuestCount(); ++j) {
                    if (!BlockPartyFragment.this.mSessionGuestsSet.contains(BlockPartyFragment.this.mBlockPartyInfo.getGuestInfo(j).Address)) {
                        FlurryTracker.logBlockPartyMemberJoin();
                        BlockPartyFragment.this.mSessionGuestsSet.add(BlockPartyFragment.this.mBlockPartyInfo.getGuestInfo(j).Address);
                    }
                    if (!BlockPartyFragment.this.mSessionMembersInfoMap.containsKey(BlockPartyFragment.this.mBlockPartyInfo.getGuestInfo(j).Address)) {
                        Log.d(BlockPartyFragment.TAG, "Add " + BlockPartyFragment.this.mBlockPartyInfo.getGuestInfo(j).Address + " to party cache info");
                        BlockPartyFragment.this.mSessionMembersInfoMap.put(BlockPartyFragment.this.mBlockPartyInfo.getGuestInfo(j).Address, new HashMap());
                    }
                }
                o = new ArrayList();
                final MAC[] array3 = (MAC[])keySet.toArray(new MAC[keySet.size()]);
                for (int length2 = array3.length, k = n; k < length2; ++k) {
                    final MAC mac2 = array3[k];
                    if (!mac2.equals(App.getInstance().getBluetoothMacAddress())) {
                        for (int l = 0; l < BlockPartyFragment.this.mBlockPartyInfo.getGuestCount(); ++l) {
                            if (mac2.equals(BlockPartyFragment.this.mBlockPartyInfo.getGuestInfo(l).Address)) {
                                ((List<UEBlockPartyInfo.UEPartyMemberInfo>)o).add(BlockPartyFragment.this.mBlockPartyInfo.getGuestInfo(l));
                                break;
                            }
                        }
                    }
                }
                Log.d(BlockPartyFragment.TAG, "Final Block Party info: " + BlockPartyFragment.this.mBlockPartyInfo.toString());
                BlockPartyFragment.this.mBlockPartyInfo.getGuests().clear();
                BlockPartyFragment.this.mBlockPartyInfo.getGuests().addAll((Collection<? extends UEBlockPartyInfo.UEPartyMemberInfo>)o);
            }
            else {
                BlockPartyFragment.this.mBlockPartyInfo = null;
                BlockPartyFragment.this.mCurrentStreamingDeviceAddress = null;
            }
        }
        
        @Override
        public Object work(Void... array) throws Exception {
            Log.d(BlockPartyFragment.TAG, "Running update async task");
            final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
            if (connectedDevice != null && connectedDevice.getDeviceConnectionStatus().isBtClassicConnectedState()) {
                array = (Void[])new Object[2];
                if (!connectedDevice.getBlockPartyState().isOnOrEntering()) {
                    array = null;
                }
                else {
                    final UEBlockPartyInfo blockPartyInfo = connectedDevice.getBlockPartyInfo();
                    array[0] = (Void)blockPartyInfo;
                    while (true) {
                        try {
                            array[1] = (Void)connectedDevice.getPartyCurrentStreamingDeviceAddress();
                            if (this.mUpdateMetadata) {
                                BlockPartyFragment.this.updateDeviceMetadata(App.getInstance().getBluetoothMacAddress());
                                for (int i = 0; i < blockPartyInfo.getGuestCount(); ++i) {
                                    if (blockPartyInfo.getGuestInfo(i).mConnectionStatus == UEPartyConnectionStatus.CONNECTED) {
                                        BlockPartyFragment.this.updateDeviceMetadata(blockPartyInfo.getGuestInfo(i).Address);
                                    }
                                }
                            }
                        }
                        catch (UEErrorResultException ex) {
                            if (ex.getResponseCode() != UEAckResponse.VALUE_NOT_AVAILABLE) {
                                throw ex;
                            }
                            array[1] = null;
                            continue;
                        }
                        break;
                    }
                    Log.d(BlockPartyFragment.TAG, "Success to update");
                }
            }
            else {
                Log.d(BlockPartyFragment.TAG, "Failed to update. No device is connected");
                array = null;
            }
            return array;
        }
    }
    
    public class PartyMemberCellHolder
    {
        public Animator mAnimator;
        @Bind({ 2131624225 })
        public RelativeLayout mArtworkContainer;
        public Handler mHandler;
        @Bind({ 2131624226 })
        public View mHostIndicator;
        @Bind({ 2131624231 })
        public View mKickMemberButton;
        @Bind({ 2131624229 })
        public LinearLayout mMemberInfoContainer;
        @Bind({ 2131624230 })
        public TextView mMemberNameLabel;
        @Bind({ 2131624227 })
        public FadeButton mPlayButton;
        @Bind({ 2131624228 })
        public ImageView mPlayIconImage;
        @Bind({ 2131624232 })
        public View mPlaybackPanel;
        public int mPlaybackPanelHeight;
        @Bind({ 2131624235 })
        public ProgressBar mPlaybackProgressBar;
        public View mRoot;
        @Bind({ 2131624237 })
        public View mSkipButton;
        public PartyMemberState mState;
        @Bind({ 2131624236 })
        public TextView mTrackArtistAlbumLabel;
        @Bind({ 2131624233 })
        public TextView mTrackElapsed;
        @Bind({ 2131624234 })
        public TextView mTrackLength;
        @Bind({ 2131624238 })
        public TextView mTrackTitleLabel;
        
        public PartyMemberCellHolder(final View mRoot) {
            this.mState = PartyMemberState.Open;
            this.mRoot = mRoot;
            this.mHandler = new Handler();
            ButterKnife.bind(this, mRoot);
            if (this.mPlaybackPanel.getHeight() == 0) {
                this.mPlaybackPanel.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new OnGlobalLayoutSelfRemovingListener(this.mPlaybackPanel) {
                    @Override
                    public void onHandleGlobalLayout() {
                        PartyMemberCellHolder.this.mPlaybackPanelHeight = PartyMemberCellHolder.this.mPlaybackPanel.getHeight();
                    }
                });
            }
            else {
                this.mPlaybackPanelHeight = this.mPlaybackPanel.getHeight();
            }
        }
        
        public void collapsePlaybackPanel() {
            if (BlockPartyFragment.this.mExpanded) {
                this.mPlaybackPanel.startAnimation((Animation)new ViewExpandAnimation(this.mPlaybackPanel, 300, 1));
                BlockPartyFragment.this.mExpanded = false;
            }
        }
        
        public void expandPlaybackPanel() {
            if (!BlockPartyFragment.this.mExpanded) {
                final ViewExpandAnimation viewExpandAnimation = new ViewExpandAnimation(this.mPlaybackPanel, 300, 0);
                viewExpandAnimation.setHeight(this.mPlaybackPanelHeight);
                this.mPlaybackPanel.startAnimation((Animation)viewExpandAnimation);
                BlockPartyFragment.this.mExpanded = true;
            }
        }
        
        public void fadeInAllElements() {
            this.mPlayButton.setAlpha(1.0f);
            this.mMemberNameLabel.setAlpha(1.0f);
            this.mPlaybackPanel.setAlpha(1.0f);
            this.mPlaybackProgressBar.setAlpha(1.0f);
            this.mTrackLength.setAlpha(1.0f);
            this.mTrackElapsed.setAlpha(1.0f);
            this.mTrackTitleLabel.setAlpha(1.0f);
            this.mTrackArtistAlbumLabel.setAlpha(1.0f);
            this.mKickMemberButton.setAlpha(1.0f);
            this.mSkipButton.setAlpha(1.0f);
        }
        
        public void showConnectedCell() {
            this.mHandler.removeCallbacksAndMessages((Object)null);
            this.fadeInAllElements();
            this.mPlayIconImage.setImageResource(2130837714);
            this.mMemberNameLabel.setVisibility(0);
            if (this.mState == PartyMemberState.StreamingFile) {
                this.collapsePlaybackPanel();
            }
            else {
                this.mPlaybackPanel.setVisibility(8);
                this.mTrackElapsed.setVisibility(8);
                this.mTrackLength.setVisibility(8);
                this.mPlaybackProgressBar.setVisibility(8);
                this.mPlaybackProgressBar.setProgress(0);
            }
            this.mKickMemberButton.setVisibility(0);
            this.mSkipButton.setVisibility(8);
            this.mState = PartyMemberState.Connected;
        }
        
        public void showLostCell() {
            this.mHandler.removeCallbacksAndMessages((Object)null);
            this.fadeInAllElements();
            this.mPlayIconImage.setImageResource(2130837715);
            this.mMemberNameLabel.setVisibility(0);
            this.mPlaybackPanel.setVisibility(8);
            this.mTrackElapsed.setVisibility(8);
            this.mTrackLength.setVisibility(8);
            this.mPlaybackProgressBar.setVisibility(8);
            this.mPlaybackProgressBar.setProgress(0);
            this.mTrackArtistAlbumLabel.setText((CharSequence)"");
            this.mTrackArtistAlbumLabel.setVisibility(8);
            this.mTrackTitleLabel.setText((CharSequence)"");
            this.mTrackTitleLabel.setVisibility(8);
            this.mKickMemberButton.setVisibility(0);
            this.mSkipButton.setVisibility(0);
            this.mState = PartyMemberState.Lost;
            if (BlockPartyFragment.this.mStreamingFile) {
                this.mState = PartyMemberState.Lost;
            }
        }
        
        public void showOpenCell() {
            this.mHandler.removeCallbacksAndMessages((Object)null);
            this.fadeInAllElements();
            this.mPlayIconImage.setImageResource(2130837640);
            this.mMemberNameLabel.setText((CharSequence)BlockPartyFragment.this.getResources().getString(2131165357));
            this.mMemberNameLabel.setVisibility(0);
            this.mPlaybackPanel.setVisibility(8);
            this.mTrackElapsed.setVisibility(8);
            this.mTrackLength.setVisibility(8);
            this.mPlaybackProgressBar.setVisibility(8);
            this.mPlaybackProgressBar.setProgress(0);
            this.mTrackArtistAlbumLabel.setText((CharSequence)"");
            this.mTrackArtistAlbumLabel.setVisibility(8);
            this.mTrackTitleLabel.setText((CharSequence)"");
            this.mTrackTitleLabel.setVisibility(8);
            this.mKickMemberButton.setVisibility(8);
            this.mSkipButton.setVisibility(8);
            this.mState = PartyMemberState.Open;
        }
        
        @SuppressLint({ "SetTextI18n" })
        public void showStreamingFileCell(final HashMap<String, Object> hashMap) {
            this.mState = PartyMemberState.StreamingFile;
            this.mHandler.removeCallbacksAndMessages((Object)null);
            this.fadeInAllElements();
            this.mPlayIconImage.setImageResource(2130837712);
            this.mMemberNameLabel.setVisibility(0);
            this.mPlaybackPanel.setVisibility(0);
            this.mTrackElapsed.setVisibility(0);
            this.mTrackLength.setVisibility(0);
            this.mPlaybackProgressBar.setVisibility(0);
            this.expandPlaybackPanel();
            this.mTrackArtistAlbumLabel.setVisibility(0);
            this.mTrackTitleLabel.setVisibility(0);
            if (hashMap.containsKey(UEPlaybackMetadataType.TITLE.toString())) {
                final String string = hashMap.get(UEPlaybackMetadataType.TITLE.toString()).toString();
                final TextView mTrackTitleLabel = this.mTrackTitleLabel;
                String string2 = string;
                if (string.isEmpty()) {
                    string2 = BlockPartyFragment.this.getString(2131165437);
                }
                mTrackTitleLabel.setText((CharSequence)string2);
            }
            String str = BlockPartyFragment.this.getString(2131165436);
            if (hashMap.containsKey(UEPlaybackMetadataType.ARTIST.toString())) {
                str = str;
                if (!TextUtils.isEmpty((CharSequence)hashMap.get(UEPlaybackMetadataType.ARTIST.toString()).toString())) {
                    str = hashMap.get(UEPlaybackMetadataType.ARTIST.toString()).toString();
                }
            }
            String str2 = BlockPartyFragment.this.getString(2131165435);
            if (hashMap.containsKey(UEPlaybackMetadataType.ALBUM.toString())) {
                str2 = str2;
                if (!TextUtils.isEmpty((CharSequence)hashMap.get(UEPlaybackMetadataType.ALBUM.toString()).toString())) {
                    str2 = hashMap.get(UEPlaybackMetadataType.ALBUM.toString()).toString();
                }
            }
            this.mTrackArtistAlbumLabel.setText((CharSequence)(str + ", " + str2));
            if (hashMap.containsKey("TRACK_LENGTH") && hashMap.containsKey("TRACK_ELAPSED")) {
                this.mState = PartyMemberState.StreamingFile;
                this.mPlaybackProgressBar.setVisibility(0);
                this.updateTrackProgressTimePanel(hashMap.get("TRACK_LENGTH"), hashMap.get("TRACK_ELAPSED"));
                this.mHandler.postDelayed((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        BlockPartyFragment.this.mUpdatesCount++;
                        if (BlockPartyFragment.this.mUpdatesCount > 7) {
                            BlockPartyFragment.this.mStreamingFile = false;
                            PartyMemberCellHolder.this.showStreamingRadioCell(hashMap);
                        }
                        else {
                            final int intValue = hashMap.get("TRACK_LENGTH");
                            final int i = hashMap.get("TRACK_ELAPSED") + 1000;
                            if (i > intValue || intValue <= 0) {
                                PartyMemberCellHolder.this.showStreamingRadioCell(hashMap);
                            }
                            else {
                                PartyMemberCellHolder.this.updateTrackProgressTimePanel(intValue, i);
                                hashMap.put("TRACK_ELAPSED", i);
                                PartyMemberCellHolder.this.mHandler.postDelayed((Runnable)this, 1000L);
                            }
                        }
                    }
                }, 1000L);
                this.mKickMemberButton.setVisibility(0);
                this.mSkipButton.setVisibility(0);
            }
            else {
                BlockPartyFragment.this.mStreamingFile = false;
                this.showStreamingRadioCell(hashMap);
            }
        }
        
        public void showStreamingRadioCell(final HashMap<String, Object> hashMap) {
            if (BlockPartyFragment.this.mStreamingFile) {
                BlockPartyFragment.this.mStreamingFile = false;
                this.showStreamingFileCell(hashMap);
            }
            else {
                BlockPartyFragment.this.mStreamingFile = false;
                this.mHandler.removeCallbacksAndMessages((Object)null);
                this.fadeInAllElements();
                this.mPlayIconImage.setImageResource(2130837712);
                this.mMemberNameLabel.setVisibility(0);
                if (this.mState == PartyMemberState.StreamingFile) {
                    this.collapsePlaybackPanel();
                }
                else {
                    this.mPlaybackPanel.setVisibility(8);
                    this.mTrackElapsed.setVisibility(8);
                    this.mTrackLength.setVisibility(8);
                    this.mPlaybackProgressBar.setVisibility(8);
                    this.mPlaybackProgressBar.setProgress(0);
                }
                this.mTrackArtistAlbumLabel.setText((CharSequence)"");
                this.mTrackArtistAlbumLabel.setVisibility(8);
                this.mTrackTitleLabel.setText((CharSequence)"");
                this.mTrackTitleLabel.setVisibility(8);
                this.mSkipButton.setVisibility(8);
                this.mState = PartyMemberState.StreamingRadio;
            }
        }
        
        public void updateTrackProgressTimePanel(int progress, int n) {
            if (this.mState == PartyMemberState.StreamingFile) {
                int n2;
                if ((n2 = n) < 0) {
                    n2 = 0;
                }
                final int i = progress / 60000;
                n = progress % 60000 / 1000;
                this.mTrackLength.setText((CharSequence)String.format(Locale.getDefault(), "%01d:%02d", i, n));
                if (this.mTrackLength.getVisibility() != 0) {
                    this.mTrackLength.setVisibility(0);
                }
                final int j = n2 / 60000;
                n = n2 % 60000 / 1000;
                this.mTrackElapsed.setText((CharSequence)String.format(Locale.getDefault(), "%01d:%02d", j, n));
                if (this.mTrackElapsed.getVisibility() != 0) {
                    this.mTrackElapsed.setVisibility(0);
                }
                progress = (int)(100.0f * n2 / progress);
                this.mPlaybackProgressBar.setProgress(progress);
                if (this.mPlaybackProgressBar.getVisibility() != 0) {
                    this.mPlaybackProgressBar.setVisibility(0);
                }
                this.mSkipButton.setVisibility(0);
                this.mSkipButton.setEnabled(true);
                if (this.mTrackTitleLabel.getVisibility() != 0) {
                    this.mTrackTitleLabel.setVisibility(0);
                }
                if (this.mTrackArtistAlbumLabel.getVisibility() != 0) {
                    this.mTrackArtistAlbumLabel.setVisibility(0);
                }
            }
        }
    }
    
    enum PartyMemberState
    {
        Connected, 
        Lost, 
        Open, 
        StreamingFile, 
        StreamingRadio;
    }
    
    public enum State
    {
        Disabled, 
        Enable, 
        Normal, 
        Xup;
    }
}
