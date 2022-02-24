// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import com.logitech.ue.tasks.SafeTask;
import com.logitech.ue.tasks.GetDeviceDataTask;
import android.widget.SeekBar$OnSeekBarChangeListener;
import android.widget.SeekBar;
import android.view.View$OnClickListener;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Calendar;
import android.widget.BaseAdapter;
import com.logitech.ue.centurion.device.devicedata.UEChargingInfo;
import java.util.TimerTask;
import com.logitech.ue.centurion.utils.UEUtils;
import com.logitech.ue.tasks.EmitSoundTask;
import com.logitech.ue.centurion.device.devicedata.UESoundProfile;
import java.io.IOException;
import android.media.MediaPlayer$OnCompletionListener;
import android.media.MediaPlayer$OnPreparedListener;
import android.os.SystemClock;
import com.logitech.ue.tasks.SetVolumeTask;
import com.logitech.ue.centurion.device.devicedata.UEAVRCP;
import android.view.KeyEvent;
import android.view.View$OnKeyListener;
import com.logitech.ue.Utils;
import com.logitech.ue.other.AlarmMusicType;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;
import android.widget.ListAdapter;
import java.sql.Time;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.os.Handler;
import android.content.DialogInterface$OnDismissListener;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.Menu;
import android.os.Bundle;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.exceptions.UEException;
import com.logitech.ue.FlurryTracker;
import com.logitech.ue.devicedata.AlarmSettings;
import com.logitech.ue.centurion.device.devicedata.UEAlarmStatus;
import com.logitech.ue.centurion.device.devicedata.UEAlarmInfo;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.App;
import android.database.Cursor;
import android.provider.MediaStore$Audio$Media;
import java.util.ArrayDeque;
import com.logitech.ue.other.MusicSelection;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;
import android.content.Context;
import android.util.Log;
import android.os.IBinder;
import android.content.ComponentName;
import android.media.MediaPlayer;
import com.logitech.ue.other.VolumeUpdater;
import com.logitech.ue.other.AlarmVolumeUpdater;
import android.content.Intent;
import java.util.Queue;
import android.widget.ListView;
import java.util.Timer;
import android.content.BroadcastReceiver;
import com.logitech.ue.tasks.GetDeviceChargingInfoTask;
import android.media.AudioManager;
import android.content.ServiceConnection;
import com.logitech.ue.services.UEAlarmService;
import com.logitech.ue.wrappers.AlarmTimeWrapper;

public class AlarmMainFragment extends NavigatableFragment implements OnAlarmWrapperListener
{
    private static final int BATTERY_UPDATE_INITIAL_DELAY = 0;
    private static final int BATTERY_UPDATE_INTERVAL = 5000;
    public static final String EXTRA_ALARM_CONFLICT_CHECKED = "conflict_checked";
    public static final String EXTRA_BTLE = "btle";
    public static final int MUSIC_STATE_IDLE = 0;
    public static final int MUSIC_STATE_PLAYING_MUSIC = 1;
    public static final String TAG;
    private static boolean mIsToSetAlarmService;
    public AlarmOptionsAdapter mAdapter;
    private UEAlarmService mAlarmService;
    private final ServiceConnection mAlarmServiceConn;
    private AudioManager mAudioManager;
    private GetDeviceChargingInfoTask mBatteryChargingInfoTask;
    final BroadcastReceiver mBroadcastReceiver;
    private Timer mCheckBatteryTimer;
    protected int mDeviceOldVolume;
    private volatile boolean mIsAlarmChangeAllowed;
    public ListView mMenuList;
    private Queue<String> mMusicQueue;
    private Intent mServiceIntent;
    protected int mStreamOldVolume;
    private AlarmVolumeUpdater mVolumeAlarmUpdater;
    private VolumeUpdater mVolumeUpdater;
    private int musicState;
    private MediaPlayer player;
    
    static {
        TAG = AlarmMainFragment.class.getSimpleName();
        AlarmMainFragment.mIsToSetAlarmService = false;
    }
    
    public AlarmMainFragment() {
        this.mIsAlarmChangeAllowed = true;
        this.player = new MediaPlayer();
        this.musicState = 0;
        this.mVolumeUpdater = new VolumeUpdater();
        this.mVolumeAlarmUpdater = new AlarmVolumeUpdater();
        this.mDeviceOldVolume = 0;
        this.mStreamOldVolume = 0;
        this.mAlarmServiceConn = (ServiceConnection)new ServiceConnection() {
            public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
                AlarmMainFragment.this.mAlarmService = ((UEAlarmService.AlarmServiceBinder)binder).getService();
                if (AlarmMainFragment.this.mAlarmService != null) {
                    Log.d(AlarmMainFragment.this.getTag(), "UEAlarmService bound");
                    if (AlarmMainFragment.mIsToSetAlarmService) {
                        Log.d(AlarmMainFragment.this.getTag(), "Update alarm service");
                        AlarmMainFragment.this.mAlarmService.setupAlarm();
                        AlarmMainFragment.mIsToSetAlarmService = false;
                    }
                }
            }
            
            public void onServiceDisconnected(final ComponentName componentName) {
                Log.w(AlarmMainFragment.this.getTag(), "UEAlarmService disconnected");
            }
        };
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getAction().equals("com.logitech.ue.centurion.CONNECTION_CHANGED") && UEDeviceStatus.getStatus(intent.getIntExtra("status", UEDeviceStatus.getValue(UEDeviceStatus.DISCONNECTED))) == UEDeviceStatus.DISCONNECTED) {
                    AlarmMainFragment.this.getNavigator().goBack();
                }
            }
        };
    }
    
    public ArrayDeque<String> getFileListBySelection(final MusicSelection musicSelection) {
        Log.d(AlarmMainFragment.TAG, "Generate audio queue by MusicSelection");
        final ArrayDeque<String> arrayDeque = new ArrayDeque<String>();
        String s = "is_music != 0";
        if (musicSelection.artistKey != null) {
            s = "is_music != 0" + " AND artist_key= '" + musicSelection.artistKey.replace("'", "''") + "'";
        }
        else if (musicSelection.artistName != null) {
            s = "is_music != 0" + " AND artist= '" + musicSelection.artistName.replace("'", "''") + "'";
        }
        String s2;
        if (musicSelection.albumKey != null) {
            s2 = s + " AND album_key='" + musicSelection.albumKey.replace("'", "''") + "'";
        }
        else {
            s2 = s;
            if (musicSelection.albumName != null) {
                s2 = s + " AND album= '" + musicSelection.albumName.replace("'", "''") + "'";
            }
        }
        String s3;
        if (musicSelection.titleKey != null) {
            s3 = s2 + " AND title_key='" + musicSelection.titleKey.replace("'", "''") + "'";
        }
        else {
            s3 = s2;
            if (musicSelection.titleName != null) {
                s3 = s2 + " AND title= '" + musicSelection.titleName.replace("'", "''") + "'";
            }
        }
        final Cursor query = this.getContext().getContentResolver().query(MediaStore$Audio$Media.EXTERNAL_CONTENT_URI, new String[] { "_data" }, s3, (String[])null, (String)null);
        while (query.moveToNext()) {
            arrayDeque.add(query.getString(0));
        }
        query.close();
        return arrayDeque;
    }
    
    public int getMusicState() {
        return this.musicState;
    }
    
    @Override
    public String getTitle() {
        return App.getInstance().getResources().getString(2131165208);
    }
    
    public void initialiseLocalSettings() {
        if (!App.getDeviceConnectionState().isBtClassicConnectedState()) {
            if (App.getDeviceConnectionState() != UEDeviceStatus.CONNECTED_OFF) {
                Log.e(this.getTag(), "Device is NULL");
                return;
            }
            Log.d(this.getTag(), "BLE alarm mode");
            if (UEDeviceManager.getInstance().getConnectedDevice() == null) {
                Log.e(AlarmMainFragment.TAG, "No device connected via either Classic or BLE");
                return;
            }
            ((SafeTask<Void, Progress, Result>)new GetFullAlarmInfoViaBleTask() {
                public void onSuccess(final Object[] array) {
                    super.onSuccess((T)array);
                    final UEAlarmInfo ueAlarmInfo = (UEAlarmInfo)array[0];
                    final boolean booleanValue = (boolean)array[1];
                    if (ueAlarmInfo.getAlarmState() == UEAlarmStatus.RECOVERY) {
                        Log.w(AlarmMainFragment.TAG, "Alarm in RECOVERY mode");
                    }
                    else if (ueAlarmInfo.getLastTimer() > 0L) {
                        AlarmSettings.setOn(true);
                        AlarmSettings.setTime(ueAlarmInfo.getAlarmTime());
                        AlarmSettings.setHostAddress(ueAlarmInfo.getAlarmHostAddress());
                    }
                    else {
                        Log.w(AlarmMainFragment.TAG, "Set alarm to OFF");
                        AlarmSettings.setOn(false);
                        AlarmSettings.setHostAddress(App.getInstance().getBluetoothMacAddressTail());
                    }
                    AlarmSettings.setRepeat(booleanValue);
                }
            }).executeOnThreadPoolExecutor(new Void[0]);
            return;
        }
        while (true) {
            Log.d(this.getTag(), "BT classic connected mode");
            final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
            while (true) {
                UEAlarmInfo alarmInfo = null;
                Label_0108: {
                    try {
                        alarmInfo = connectedDevice.getAlarmInfo();
                        if (alarmInfo != null) {
                            if (alarmInfo.getAlarmState() != UEAlarmStatus.RECOVERY) {
                                break Label_0108;
                            }
                            if (this.mAlarmService == null) {
                                Log.e(this.getTag(), "mAlarmService is NULL. Unable to recover alarm.");
                            }
                            else {
                                this.mAlarmService.setAlarmToDevice(connectedDevice);
                            }
                            AlarmSettings.setRepeat(connectedDevice.getRepeatAlarm());
                        }
                        AlarmSettings.setVolume(connectedDevice.getAlarmVolume());
                        return;
                    }
                    catch (UEException ex) {
                        FlurryTracker.logError(AlarmMainFragment.TAG, ex.getMessage());
                        ex.printStackTrace();
                        return;
                    }
                }
                if (alarmInfo.getLastTimer() > 0L) {
                    AlarmSettings.setOn(true);
                    AlarmSettings.setTime(alarmInfo.getAlarmTime());
                    AlarmSettings.setHostAddress(alarmInfo.getAlarmHostAddress());
                    continue;
                }
                AlarmSettings.setOn(false);
                AlarmSettings.setHostAddress(App.getInstance().getBluetoothMacAddressTail());
                continue;
            }
        }
    }
    
    public boolean isChangeAllowed() {
        return this.mIsAlarmChangeAllowed;
    }
    
    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        this.mAudioManager = (AudioManager)context.getSystemService("audio");
        this.initialiseLocalSettings();
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setHasOptionsMenu(true);
    }
    
    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(2130968625, viewGroup, false);
    }
    
    @Override
    public void onDetach() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        com/logitech/ue/fragments/AlarmMainFragment.mAlarmService:Lcom/logitech/ue/services/UEAlarmService;
        //     4: ifnull          18
        //     7: aload_0        
        //     8: invokevirtual   com/logitech/ue/fragments/AlarmMainFragment.getContext:()Landroid/content/Context;
        //    11: aload_0        
        //    12: getfield        com/logitech/ue/fragments/AlarmMainFragment.mAlarmServiceConn:Landroid/content/ServiceConnection;
        //    15: invokevirtual   android/content/Context.unbindService:(Landroid/content/ServiceConnection;)V
        //    18: aload_0        
        //    19: getfield        com/logitech/ue/fragments/AlarmMainFragment.player:Landroid/media/MediaPlayer;
        //    22: ifnull          54
        //    25: aload_0        
        //    26: getfield        com/logitech/ue/fragments/AlarmMainFragment.player:Landroid/media/MediaPlayer;
        //    29: invokevirtual   android/media/MediaPlayer.isPlaying:()Z
        //    32: ifeq            42
        //    35: aload_0        
        //    36: getfield        com/logitech/ue/fragments/AlarmMainFragment.player:Landroid/media/MediaPlayer;
        //    39: invokevirtual   android/media/MediaPlayer.stop:()V
        //    42: aload_0        
        //    43: getfield        com/logitech/ue/fragments/AlarmMainFragment.player:Landroid/media/MediaPlayer;
        //    46: invokevirtual   android/media/MediaPlayer.release:()V
        //    49: aload_0        
        //    50: aconst_null    
        //    51: putfield        com/logitech/ue/fragments/AlarmMainFragment.player:Landroid/media/MediaPlayer;
        //    54: aload_0        
        //    55: invokespecial   com/logitech/ue/fragments/NavigatableFragment.onDetach:()V
        //    58: return         
        //    59: astore_1       
        //    60: aload_0        
        //    61: invokevirtual   com/logitech/ue/fragments/AlarmMainFragment.getTag:()Ljava/lang/String;
        //    64: ldc_w           "mAlarmServiceConn was not bound"
        //    67: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //    70: pop            
        //    71: goto            18
        //    74: astore_1       
        //    75: aload_0        
        //    76: aconst_null    
        //    77: putfield        com/logitech/ue/fragments/AlarmMainFragment.player:Landroid/media/MediaPlayer;
        //    80: goto            54
        //    83: astore_1       
        //    84: aload_0        
        //    85: aconst_null    
        //    86: putfield        com/logitech/ue/fragments/AlarmMainFragment.player:Landroid/media/MediaPlayer;
        //    89: aload_1        
        //    90: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  7      18     59     74     Ljava/lang/Exception;
        //  42     49     74     83     Ljava/lang/Exception;
        //  42     49     83     91     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0042:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public void onOnValueChanged(final boolean b) {
        this.setAlarmOn(b, true);
        this.mAdapter.notifyDataSetChanged();
    }
    
    @Override
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        boolean onOptionsItemSelected = false;
        switch (menuItem.getItemId()) {
            default: {
                onOptionsItemSelected = super.onOptionsItemSelected(menuItem);
                break;
            }
            case 2130837637: {
                App.getInstance().showMessageDialog(this.getString(2131165213), null);
                onOptionsItemSelected = true;
                break;
            }
        }
        return onOptionsItemSelected;
    }
    
    public void onPreviewClicked(final View view) {
        if (this.getMusicState() == 0) {
            this.playMusic(AlarmSettings.getMusicType(), AlarmSettings.getVolume());
        }
        else if (this.getMusicState() == 1) {
            this.pauseMusic(true);
        }
        this.mAdapter.notifyDataSetChanged();
    }
    
    @Override
    public void onRepeatValueChanged(final boolean b) {
        if (b) {
            if (this.setAlarmOn(true, true)) {
                this.setAlarmRepeat(b);
            }
        }
        else {
            AlarmSettings.setRepeat(b);
        }
        this.mAdapter.notifyDataSetChanged();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        this.mAdapter.notifyDataSetChanged();
        final String bluetoothMacAddressTail = App.getInstance().getBluetoothMacAddressTail();
        final String hostAddress = AlarmSettings.getHostAddress();
        this.mStreamOldVolume = this.mAudioManager.getStreamVolume(3);
        Log.d(this.getTag(), "This phone BT address is " + bluetoothMacAddressTail);
        if (hostAddress == null || hostAddress.equals(bluetoothMacAddressTail)) {
            this.mIsAlarmChangeAllowed = true;
        }
        else {
            Log.w(this.getTag(), "Alarm was set by another device " + hostAddress);
            this.mIsAlarmChangeAllowed = false;
        }
        if (!this.isChangeAllowed() && this.getArguments() != null && !this.getArguments().getBoolean("conflict_checked", false)) {
            new Handler().postDelayed((Runnable)new Runnable() {
                @Override
                public void run() {
                    App.getInstance().showAlertDialog(AlarmMainFragment.this.getString(2131165212), 2131165358, 2131165470, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            if (n == -1) {
                                AlarmMainFragment.this.mIsAlarmChangeAllowed = true;
                            }
                            else {
                                AlarmMainFragment.this.mIsAlarmChangeAllowed = false;
                                AlarmMainFragment.this.getNavigator().goBack();
                            }
                        }
                    });
                }
            }, 100L);
        }
    }
    
    @Override
    public void onStart() {
        super.onStart();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.logitech.ue.centurion.CONNECTION_CHANGED");
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(this.mBroadcastReceiver, intentFilter);
    }
    
    @Override
    public void onStop() {
        super.onStop();
        if (this.musicState == 1) {
            this.pauseMusic(true);
        }
        LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(this.mBroadcastReceiver);
    }
    
    @Override
    public void onTimeValueChanged(final int hour, final int minute) {
        AlarmSettings.setTime(new Time(hour, minute, 0));
        AlarmSettings.setHostAddress(App.getInstance().getBluetoothMacAddressTail());
        this.setAlarmOn(true, true);
        this.mAdapter.notifyDataSetChanged();
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mMenuList = (ListView)view.findViewById(16908298);
        this.mAdapter = new AlarmOptionsAdapter();
        this.mMenuList.setChoiceMode(1);
        this.mMenuList.setAdapter((ListAdapter)this.mAdapter);
        this.mMenuList.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                if (n == 1) {
                    AlarmSettings.setMusicType(AlarmMusicType.LAST_PLAY);
                    AlarmMainFragment.this.mAdapter.notifyDataSetChanged();
                }
                else if (n == 2 && Utils.isReadExternalStoragePermissionGranted()) {
                    AlarmMainFragment.this.getNavigator().gotoFragment(LibraryMusicSelectorFragment.class, null);
                }
            }
        });
        view.setOnKeyListener((View$OnKeyListener)new View$OnKeyListener() {
            public boolean onKey(final View view, final int n, final KeyEvent keyEvent) {
                return n == 4;
            }
        });
    }
    
    public void pauseMusic(final boolean b) {
        this.pauseMusicMediaPlayer();
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        while (true) {
            if (connectedDevice == null) {
                break Label_0082;
            }
            Label_0123: {
                if (!this.mAudioManager.isMusicActive()) {
                    break Label_0123;
                }
                while (true) {
                    try {
                        connectedDevice.sendAVRCPCommand(UEAVRCP.PAUSE);
                        Log.d(AlarmMainFragment.TAG, "AVRCP PAUSE command sent");
                        connectedDevice.sendAVRCPCommand(UEAVRCP.STOP);
                        Log.d(AlarmMainFragment.TAG, "AVRCP STOP command sent");
                        if (b) {
                            ((SafeTask<Void, Progress, Result>)new SetVolumeTask(this.mDeviceOldVolume) {
                                @Override
                                public Void work(final Void... array) throws Exception {
                                    Log.w(AlarmMainFragment.TAG, "Waiting for streaming completely finish");
                                    SystemClock.sleep(1000L);
                                    AlarmMainFragment.this.mAudioManager.setStreamVolume(3, AlarmMainFragment.this.mStreamOldVolume, 0);
                                    return super.work(new Void[0]);
                                }
                            }).executeOnThreadPoolExecutor(new Void[0]);
                        }
                        this.musicState = 0;
                        Log.d(AlarmMainFragment.TAG, "Music paused");
                        return;
                    }
                    catch (UEException ex) {
                        FlurryTracker.logError(AlarmMainFragment.TAG, ex.getMessage());
                        ex.printStackTrace();
                        App.getInstance().gotoNuclearHome(ex);
                        continue;
                    }
                    break;
                }
            }
            Log.w(AlarmMainFragment.TAG, "Tried to pause music when music is NOT active");
            continue;
        }
    }
    
    void pauseMusicMediaPlayer() {
        if (this.player != null && this.player.isPlaying()) {
            Log.d(AlarmMainFragment.TAG, "Pause media player");
            this.player.pause();
            this.musicState = 0;
        }
    }
    
    public void playMusic(final AlarmMusicType ex, final int volume) {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        if (this.mAudioManager.isMusicActive()) {
            Log.d(AlarmMainFragment.TAG, "Pause current music");
            this.pauseMusic(false);
        }
    Label_0061:
        while (true) {
            if (connectedDevice == null) {
                break Label_0061;
            }
            while (true) {
                try {
                    this.mStreamOldVolume = this.mAudioManager.getStreamVolume(3);
                    this.mDeviceOldVolume = connectedDevice.getVolume();
                    connectedDevice.setVolume(volume);
                    if (ex == AlarmMusicType.SINGLE_SOUND || ex == AlarmMusicType.MULTI_SOUND) {
                        if (Utils.isReadExternalStoragePermissionGranted()) {
                            Log.d(AlarmMainFragment.TAG, "Play local music.");
                            this.setMusicPlayerQueue(this.getFileListBySelection(AlarmSettings.getMusicSelection()));
                            this.playMusicMediaPlayer();
                            this.musicState = 1;
                        }
                        return;
                    }
                }
                catch (Exception ex2) {
                    FlurryTracker.logError(AlarmMainFragment.TAG, ex2.getMessage());
                    ex2.printStackTrace();
                    continue Label_0061;
                }
                if (ex != AlarmMusicType.LAST_PLAY) {
                    return;
                }
                Log.d(AlarmMainFragment.TAG, "Play last music played.");
                while (true) {
                    try {
                        Log.d(AlarmMainFragment.TAG, "--> Send AVRCP PLAY command");
                        UEDeviceManager.getInstance().getConnectedDevice().sendAVRCPCommand(UEAVRCP.PLAY);
                        this.musicState = 1;
                    }
                    catch (Exception ex) {
                        FlurryTracker.logError(AlarmMainFragment.TAG, ex.getMessage());
                        ex.printStackTrace();
                        continue;
                    }
                    break;
                }
            }
            break;
        }
    }
    
    void playMusicMediaPlayer() {
        Log.d(AlarmMainFragment.TAG, "Play Music");
        if (this.getMusicState() == 0) {
            this.playNextSong();
        }
        else {
            this.player.start();
        }
    }
    
    void playNextSong() {
        if (this.mMusicQueue == null || this.mMusicQueue.size() == 0) {
            Log.d(AlarmMainFragment.TAG, "Music queue is empty");
        }
        else {
            try {
                final String s = this.mMusicQueue.poll();
                Log.d(AlarmMainFragment.TAG, "Music play next song: " + s);
                this.mMusicQueue.add(s);
                this.player.reset();
                this.player.setDataSource(s);
                this.player.prepareAsync();
                this.player.setOnPreparedListener((MediaPlayer$OnPreparedListener)new MediaPlayer$OnPreparedListener() {
                    public void onPrepared(final MediaPlayer mediaPlayer) {
                        AlarmMainFragment.this.player.start();
                        AlarmMainFragment.this.setMusicState(1);
                    }
                });
                this.player.setOnCompletionListener((MediaPlayer$OnCompletionListener)new MediaPlayer$OnCompletionListener() {
                    public void onCompletion(final MediaPlayer mediaPlayer) {
                        AlarmMainFragment.this.playNextSong();
                    }
                });
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public boolean setAlarmOn(final boolean on, final boolean b) {
        final boolean b2 = false;
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        while (true) {
            Label_0167: {
                if (!on) {
                    break Label_0167;
                }
                while (true) {
                    Label_0142: {
                        try {
                            if (App.getDeviceConnectionState() == UEDeviceStatus.SINGLE_CONNECTED || App.getDeviceConnectionState() == UEDeviceStatus.DOUBLEUP_CONNECTED) {
                                if (AlarmSettings.isOn() == on) {
                                    this.setupAlarmService();
                                }
                                else {
                                    if (connectedDevice.getChargingInfo().getCharge() < 20) {
                                        break Label_0142;
                                    }
                                    AlarmSettings.setOn(true);
                                    this.setupAlarmService();
                                    if (b) {
                                        new EmitSoundTask(UESoundProfile.ALARM_ON).execute((Object[])new Void[0]);
                                    }
                                }
                                AlarmSettings.setOn(on);
                                AlarmSettings.setHostAddress(App.getInstance().getBluetoothMacAddressTail());
                                return true;
                            }
                            break;
                        }
                        catch (UEException ex) {
                            FlurryTracker.logError(AlarmMainFragment.TAG, ex.getMessage());
                            ex.printStackTrace();
                            App.getInstance().gotoNuclearHome(ex);
                            return b2;
                        }
                    }
                    Log.w(this.getTag(), "Battery level is too low. Show battery warning dialog");
                    connectedDevice.announceBatteryLevel();
                    this.showLowBatteryDialog();
                    return b2;
                }
            }
            if (on && App.getDeviceConnectionState() == UEDeviceStatus.CONNECTED_OFF) {
                Log.d(AlarmMainFragment.TAG, "Set alarm via BLE");
                if (AlarmSettings.isOn() == on) {
                    this.setupAlarmService();
                }
                else {
                    AlarmSettings.setOn(true);
                    this.setupAlarmService();
                }
                connectedDevice.setAlarm(AlarmSettings.getTimerInMilliseconds(), App.getInstance().getBluetoothMacAddress());
                connectedDevice.setAlarmVolume(AlarmSettings.getVolume(), App.getInstance().getBluetoothMacAddress());
                connectedDevice.setRepeatAlarm(AlarmSettings.isRepeat(), App.getInstance().getBluetoothMacAddress());
                continue;
            }
            if (!on) {
                Log.d(this.getTag(), "Disable alarm");
                this.stopAlarm(b);
                AlarmSettings.setRepeat(false);
                continue;
            }
            continue;
        }
    }
    
    public void setAlarmRepeat(final boolean repeat) {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        try {
            if (App.getDeviceConnectionState() == UEDeviceStatus.SINGLE_CONNECTED || App.getDeviceConnectionState() == UEDeviceStatus.DOUBLEUP_CONNECTED) {
                connectedDevice.setRepeatAlarm(repeat, App.getInstance().getBluetoothMacAddress());
            }
            else if (App.getDeviceConnectionState() == UEDeviceStatus.CONNECTED_OFF) {
                Log.d(AlarmMainFragment.TAG, "Set repeat via BTLE");
                if (!AlarmSettings.isOn()) {
                    Log.d(AlarmMainFragment.TAG, "Trun ON alarm because user turnes on Daily Repeat");
                    AlarmSettings.setOn(true);
                    this.setupAlarmService();
                }
                connectedDevice.setRepeatAlarm(repeat, App.getInstance().getBluetoothMacAddress());
            }
            AlarmSettings.setRepeat(repeat);
        }
        catch (UEException ex) {
            FlurryTracker.logError(AlarmMainFragment.TAG, ex.getMessage());
            ex.printStackTrace();
            App.getInstance().gotoNuclearHome(ex);
        }
    }
    
    public void setMusicPlayerQueue(final Queue<String> mMusicQueue) {
        this.mMusicQueue = mMusicQueue;
        if (this.player != null) {
            this.player.reset();
        }
        else {
            this.player = new MediaPlayer();
        }
    }
    
    public void setMusicState(final int musicState) {
        this.musicState = musicState;
    }
    
    public void setupAlarmService() {
        UEDeviceManager.getInstance().getConnectedDevice();
        if (!App.getDeviceConnectionState().isBtClassicConnectedState() && App.getDeviceConnectionState() != UEDeviceStatus.CONNECTED_OFF) {
            Log.e(this.getTag(), "Device is NULL and NOT BTLE mode");
        }
        else if (this.mAlarmService == null) {
            (this.mServiceIntent = new Intent(this.getContext(), (Class)UEAlarmService.class)).setAction("ALARM");
            if (!UEUtils.isServiceRunning(this.getContext(), UEAlarmService.class.getName())) {
                Log.d(this.getTag(), "Start new UEAlarmService");
                this.getContext().startService(this.mServiceIntent);
                AlarmMainFragment.mIsToSetAlarmService = false;
            }
            else {
                AlarmMainFragment.mIsToSetAlarmService = true;
            }
            this.getContext().bindService(this.mServiceIntent, this.mAlarmServiceConn, 8);
        }
        else {
            this.mAlarmService.setupAlarm();
        }
    }
    
    public void showLowBatteryDialog() {
        App.getInstance().showMessageDialog(this.getString(2131165227), (DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener() {
            public void onDismiss(final DialogInterface dialogInterface) {
                Log.d(AlarmMainFragment.TAG, "Dismiss charge warning dialog");
                AlarmMainFragment.this.stopBatteryTimer();
            }
        });
        this.startBatteryTimer();
    }
    
    void startBatteryTimer() {
        this.stopBatteryTimer();
        Log.d(AlarmMainFragment.TAG, "Start battery checking timer");
        (this.mCheckBatteryTimer = new Timer()).schedule(new TimerTask() {
            @Override
            public void run() {
                if (AlarmMainFragment.this.mBatteryChargingInfoTask != null) {
                    AlarmMainFragment.this.mBatteryChargingInfoTask.cancel(true);
                }
                AlarmMainFragment.this.mBatteryChargingInfoTask = new GetDeviceChargingInfoTask() {
                    @Override
                    public void onError(final Exception ex) {
                        super.onError(ex);
                        ex.printStackTrace();
                        App.getInstance().gotoNuclearHome(ex);
                    }
                    
                    public void onSuccess(final UEChargingInfo ueChargingInfo) {
                        super.onSuccess((T)ueChargingInfo);
                        if (ueChargingInfo.getCharge() >= 20) {
                            Log.d(AlarmMainFragment.TAG, "Dismiss dialog and enable alarm");
                            App.getInstance().dismissMessageDialog();
                            AlarmMainFragment.this.setAlarmOn(true, true);
                            AlarmMainFragment.this.stopBatteryTimer();
                        }
                        else {
                            Log.d(AlarmMainFragment.TAG, "Timer check battery level is still low and not charging");
                        }
                    }
                };
                AlarmMainFragment.this.mBatteryChargingInfoTask.execute((Object[])new Void[0]);
            }
        }, 0L, 5000L);
    }
    
    public void stopAlarm(final boolean p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: invokevirtual   com/logitech/ue/centurion/UEDeviceManager.getConnectedDevice:()Lcom/logitech/ue/centurion/device/UEGenericDevice;
        //     6: astore_2       
        //     7: aload_2        
        //     8: ifnonnull       23
        //    11: aload_0        
        //    12: invokevirtual   com/logitech/ue/fragments/AlarmMainFragment.getTag:()Ljava/lang/String;
        //    15: ldc_w           "Device is NULL"
        //    18: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //    21: pop            
        //    22: return         
        //    23: aload_2        
        //    24: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.getDeviceConnectionStatus:()Lcom/logitech/ue/centurion/device/devicedata/UEDeviceStatus;
        //    27: invokevirtual   com/logitech/ue/centurion/device/devicedata/UEDeviceStatus.isBtClassicConnectedState:()Z
        //    30: ifeq            117
        //    33: aload_2        
        //    34: invokestatic    com/logitech/ue/App.getInstance:()Lcom/logitech/ue/App;
        //    37: invokevirtual   com/logitech/ue/App.getBluetoothMacAddress:()Lcom/logitech/ue/centurion/utils/MAC;
        //    40: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.clearAlarm:(Lcom/logitech/ue/centurion/utils/MAC;)V
        //    43: aload_2        
        //    44: getstatic       com/logitech/ue/centurion/device/devicedata/UESoundProfile.ALARM_OFF:Lcom/logitech/ue/centurion/device/devicedata/UESoundProfile;
        //    47: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.emitSound:(Lcom/logitech/ue/centurion/device/devicedata/UESoundProfile;)V
        //    50: aload_0        
        //    51: getfield        com/logitech/ue/fragments/AlarmMainFragment.mServiceIntent:Landroid/content/Intent;
        //    54: ifnonnull       75
        //    57: aload_0        
        //    58: new             Landroid/content/Intent;
        //    61: dup            
        //    62: aload_0        
        //    63: invokevirtual   com/logitech/ue/fragments/AlarmMainFragment.getContext:()Landroid/content/Context;
        //    66: ldc_w           Lcom/logitech/ue/services/UEAlarmService;.class
        //    69: invokespecial   android/content/Intent.<init>:(Landroid/content/Context;Ljava/lang/Class;)V
        //    72: putfield        com/logitech/ue/fragments/AlarmMainFragment.mServiceIntent:Landroid/content/Intent;
        //    75: aload_0        
        //    76: getfield        com/logitech/ue/fragments/AlarmMainFragment.mServiceIntent:Landroid/content/Intent;
        //    79: ldc_w           "ALARM_OFF"
        //    82: invokevirtual   android/content/Intent.setAction:(Ljava/lang/String;)Landroid/content/Intent;
        //    85: pop            
        //    86: aload_0        
        //    87: invokevirtual   com/logitech/ue/fragments/AlarmMainFragment.getContext:()Landroid/content/Context;
        //    90: aload_0        
        //    91: getfield        com/logitech/ue/fragments/AlarmMainFragment.mAlarmServiceConn:Landroid/content/ServiceConnection;
        //    94: invokevirtual   android/content/Context.unbindService:(Landroid/content/ServiceConnection;)V
        //    97: aload_0        
        //    98: invokevirtual   com/logitech/ue/fragments/AlarmMainFragment.getContext:()Landroid/content/Context;
        //   101: aload_0        
        //   102: getfield        com/logitech/ue/fragments/AlarmMainFragment.mServiceIntent:Landroid/content/Intent;
        //   105: invokevirtual   android/content/Context.stopService:(Landroid/content/Intent;)Z
        //   108: pop            
        //   109: aload_0        
        //   110: aconst_null    
        //   111: putfield        com/logitech/ue/fragments/AlarmMainFragment.mAlarmService:Lcom/logitech/ue/services/UEAlarmService;
        //   114: goto            22
        //   117: aload_2        
        //   118: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.getDeviceConnectionStatus:()Lcom/logitech/ue/centurion/device/devicedata/UEDeviceStatus;
        //   121: getstatic       com/logitech/ue/centurion/device/devicedata/UEDeviceStatus.CONNECTED_OFF:Lcom/logitech/ue/centurion/device/devicedata/UEDeviceStatus;
        //   124: if_acmpne       50
        //   127: aload_2        
        //   128: invokestatic    com/logitech/ue/App.getInstance:()Lcom/logitech/ue/App;
        //   131: invokevirtual   com/logitech/ue/App.getBluetoothMacAddress:()Lcom/logitech/ue/centurion/utils/MAC;
        //   134: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.clearAlarm:(Lcom/logitech/ue/centurion/utils/MAC;)V
        //   137: goto            50
        //   140: astore_2       
        //   141: getstatic       com/logitech/ue/fragments/AlarmMainFragment.TAG:Ljava/lang/String;
        //   144: aload_2        
        //   145: invokevirtual   com/logitech/ue/centurion/exceptions/UEException.getMessage:()Ljava/lang/String;
        //   148: invokestatic    com/logitech/ue/FlurryTracker.logError:(Ljava/lang/String;Ljava/lang/String;)V
        //   151: aload_2        
        //   152: invokevirtual   com/logitech/ue/centurion/exceptions/UEException.printStackTrace:()V
        //   155: invokestatic    com/logitech/ue/App.getInstance:()Lcom/logitech/ue/App;
        //   158: aload_2        
        //   159: invokevirtual   com/logitech/ue/App.gotoNuclearHome:(Ljava/lang/Exception;)V
        //   162: goto            50
        //   165: astore_2       
        //   166: getstatic       com/logitech/ue/fragments/AlarmMainFragment.TAG:Ljava/lang/String;
        //   169: aload_2        
        //   170: invokevirtual   java/lang/Exception.getMessage:()Ljava/lang/String;
        //   173: invokestatic    com/logitech/ue/FlurryTracker.logError:(Ljava/lang/String;Ljava/lang/String;)V
        //   176: aload_0        
        //   177: invokevirtual   com/logitech/ue/fragments/AlarmMainFragment.getTag:()Ljava/lang/String;
        //   180: ldc_w           "mAlarmServiceConn is not bound"
        //   183: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //   186: pop            
        //   187: goto            97
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                              
        //  -----  -----  -----  -----  --------------------------------------------------
        //  23     50     140    165    Lcom/logitech/ue/centurion/exceptions/UEException;
        //  86     97     165    190    Ljava/lang/Exception;
        //  117    137    140    165    Lcom/logitech/ue/centurion/exceptions/UEException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0097:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    void stopBatteryTimer() {
        if (this.mCheckBatteryTimer != null) {
            Log.d(AlarmMainFragment.TAG, "Kill current check battery timer");
            this.mCheckBatteryTimer.cancel();
            this.mCheckBatteryTimer.purge();
            this.mCheckBatteryTimer = null;
        }
        if (this.mBatteryChargingInfoTask != null) {
            this.mBatteryChargingInfoTask.cancel(true);
        }
        this.mBatteryChargingInfoTask = null;
        this.mCheckBatteryTimer = null;
    }
    
    public class AlarmOptionsAdapter extends BaseAdapter
    {
        public int getCount() {
            return 4;
        }
        
        public Object getItem(final int n) {
            return null;
        }
        
        public long getItemId(final int n) {
            return n;
        }
        
        public int getItemViewType(final int n) {
            return n;
        }
        
        public View getView(int n, View viewById, final ViewGroup viewGroup) {
            View view;
            if (n == 0) {
                if ((view = viewById) == null) {
                    view = AlarmMainFragment.this.getActivity().getLayoutInflater().inflate(2130968611, viewGroup, false);
                    final AlarmTimeWrapper tag = new AlarmTimeWrapper(view);
                    tag.setListener((AlarmTimeWrapper.OnAlarmWrapperListener)AlarmMainFragment.this);
                    view.setTag((Object)tag);
                }
                final AlarmTimeWrapper alarmTimeWrapper = (AlarmTimeWrapper)view.getTag();
                final Time time = AlarmSettings.getTime();
                final Calendar instance = Calendar.getInstance();
                instance.setTimeInMillis(time.getTime());
                alarmTimeWrapper.setTime(instance.get(11), instance.get(12));
                alarmTimeWrapper.setIsOn(AlarmSettings.isOn());
                alarmTimeWrapper.setIsRepeatOn(AlarmSettings.isRepeat());
            }
            else if (n == 1) {
                if ((view = viewById) == null) {
                    view = AlarmMainFragment.this.getActivity().getLayoutInflater().inflate(2130968650, viewGroup, false);
                }
                ((TextView)view.findViewById(16908308)).setText(2131165214);
                final ImageView imageView = (ImageView)view.findViewById(16908294);
                if (AlarmSettings.getMusicType() == AlarmMusicType.LAST_PLAY) {
                    imageView.setImageResource(2130837730);
                    imageView.setVisibility(0);
                }
                else {
                    imageView.setVisibility(4);
                }
            }
            else if (n == 2) {
                if ((view = viewById) == null) {
                    view = AlarmMainFragment.this.getActivity().getLayoutInflater().inflate(2130968609, viewGroup, false);
                }
                final MusicSelection musicSelection = AlarmSettings.getMusicSelection();
                String text;
                if (musicSelection.isEmpty()) {
                    text = AlarmMainFragment.this.getString(2131165359);
                }
                else if (AlarmSettings.getMusicType() == AlarmMusicType.SINGLE_SOUND) {
                    text = musicSelection.titleName;
                }
                else if (AlarmSettings.getMusicType() == AlarmMusicType.MULTI_SOUND) {
                    if (!TextUtils.isEmpty((CharSequence)musicSelection.albumKey)) {
                        text = musicSelection.albumName;
                    }
                    else {
                        text = musicSelection.artistName;
                    }
                }
                else if (!TextUtils.isEmpty((CharSequence)musicSelection.titleKey)) {
                    text = musicSelection.titleName;
                }
                else if (!TextUtils.isEmpty((CharSequence)musicSelection.albumKey)) {
                    text = musicSelection.albumName;
                }
                else {
                    text = musicSelection.artistName;
                }
                ((TextView)view.findViewById(16908309)).setText((CharSequence)text);
                if (AlarmSettings.getMusicType() == AlarmMusicType.SINGLE_SOUND || AlarmSettings.getMusicType() == AlarmMusicType.MULTI_SOUND) {
                    view.findViewById(16908294).setVisibility(0);
                }
                else {
                    view.findViewById(16908294).setVisibility(4);
                }
            }
            else {
                view = viewById;
                if (n == 3) {
                    if ((view = viewById) == null) {
                        view = AlarmMainFragment.this.getActivity().getLayoutInflater().inflate(2130968610, viewGroup, false);
                    }
                    final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
                    viewById = view.findViewById(2131624086);
                    final ImageView imageView2 = (ImageView)viewById.findViewWithTag((Object)"icon");
                    boolean volume32Supported = false;
                    if (connectedDevice == null || !connectedDevice.getDeviceConnectionStatus().isBtClassicConnectedState()) {
                        viewById.setVisibility(8);
                    }
                    else {
                        viewById.setVisibility(0);
                        volume32Supported = connectedDevice.isVolume32Supported();
                        if (AlarmMainFragment.this.getMusicState() == 1) {
                            imageView2.setImageResource(2130837642);
                        }
                        else {
                            imageView2.setImageResource(2130837643);
                        }
                    }
                    viewById.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                        public void onClick(final View view) {
                            AlarmMainFragment.this.onPreviewClicked(view);
                        }
                    });
                    final ImageView imageView3 = (ImageView)view.findViewById(2131624088);
                    int volume = AlarmSettings.getVolume();
                    final SeekBar seekBar = (SeekBar)view.findViewById(2131624087);
                    int n2;
                    if (volume32Supported) {
                        n = 20;
                        n2 = 10;
                        seekBar.setMax(31);
                    }
                    else {
                        volume %= 16;
                        n = 10;
                        n2 = 5;
                        seekBar.setMax(15);
                    }
                    if (volume > n) {
                        imageView3.setImageResource(2130837647);
                    }
                    else if (volume > n2) {
                        imageView3.setImageResource(2130837646);
                    }
                    else if (volume >= 1) {
                        imageView3.setImageResource(2130837645);
                    }
                    else {
                        imageView3.setImageResource(2130837644);
                    }
                    seekBar.setProgress(volume - 1);
                    seekBar.setOnSeekBarChangeListener((SeekBar$OnSeekBarChangeListener)new SeekBar$OnSeekBarChangeListener() {
                        public void onProgressChanged(final SeekBar seekBar, int volume, final boolean b) {
                            if (b) {
                                ++volume;
                                if (AlarmMainFragment.this.musicState == 1) {
                                    AlarmMainFragment.this.mVolumeUpdater.update(volume);
                                }
                                AlarmMainFragment.this.mVolumeAlarmUpdater.update(volume);
                                AlarmSettings.setVolume(volume);
                                if (volume > n) {
                                    imageView3.setImageResource(2130837647);
                                }
                                else if (volume > n2) {
                                    imageView3.setImageResource(2130837646);
                                }
                                else if (volume > 1) {
                                    imageView3.setImageResource(2130837645);
                                }
                                else {
                                    imageView3.setImageResource(2130837644);
                                }
                            }
                        }
                        
                        public void onStartTrackingTouch(final SeekBar seekBar) {
                        }
                        
                        public void onStopTrackingTouch(final SeekBar seekBar) {
                        }
                    });
                }
            }
            return view;
        }
        
        public int getViewTypeCount() {
            return 4;
        }
        
        public boolean hasStableIds() {
            return true;
        }
    }
    
    public class GetFullAlarmInfoViaBleTask extends GetDeviceDataTask<Object[]>
    {
        @Override
        public String getTag() {
            return AlarmMainFragment.TAG;
        }
        
        @Override
        public Object[] work(final Void... array) throws Exception {
            return new Object[] { UEDeviceManager.getInstance().getConnectedDevice().getAlarmInfo(), UEDeviceManager.getInstance().getConnectedDevice().getRepeatAlarm() };
        }
    }
}
