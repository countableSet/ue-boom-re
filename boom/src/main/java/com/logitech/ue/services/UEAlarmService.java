// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.services;

import com.logitech.ue.tasks.SafeTask;
import android.os.Binder;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import com.logitech.ue.tasks.SetVolumeTask;
import java.sql.Time;
import com.logitech.ue.centurion.exceptions.UEOperationTimeOutException;
import com.logitech.ue.centurion.exceptions.UEOperationException;
import com.logitech.ue.centurion.exceptions.UEConnectionException;
import com.logitech.ue.centurion.utils.MAC;
import java.util.concurrent.ExecutionException;
import android.os.AsyncTask;
import com.logitech.ue.tasks.InitManagerTask;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.logitech.ue.activities.AlarmPopupActivity;
import java.io.IOException;
import android.media.MediaPlayer$OnPreparedListener;
import com.logitech.ue.Utils;
import android.media.MediaPlayer$OnCompletionListener;
import com.logitech.ue.UserPreferences;
import android.database.Cursor;
import android.provider.MediaStore$Audio$Media;
import java.util.ArrayDeque;
import com.logitech.ue.other.MusicSelection;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.os.Build$VERSION;
import android.app.Notification$Builder;
import java.util.Date;
import android.text.format.DateFormat;
import com.logitech.ue.App;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.device.devicedata.UEAVRCP;
import com.logitech.ue.other.AlarmMusicType;
import com.logitech.ue.devicedata.AlarmSettings;
import com.logitech.ue.centurion.exceptions.UEException;
import com.logitech.ue.centurion.device.devicedata.UEAlarmNotificationFlags;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.notification.UEAlarmNotification;
import android.text.TextUtils;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;
import android.util.Log;
import android.content.Intent;
import android.os.CountDownTimer;
import java.util.Queue;
import android.media.MediaPlayer;
import android.content.Context;
import android.os.IBinder;
import android.media.AudioManager;
import android.content.BroadcastReceiver;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;

public class UEAlarmService extends Service
{
    public static final String ALARM_ACTION_ALARM = "ALARM";
    public static final String ALARM_ACTION_ALARM_OFF = "ALARM_OFF";
    public static final String ALARM_ACTION_ALARM_RESETUP = "ALARM_RESETUP";
    public static final String ALARM_ACTION_ALL_OFF = "ALL_OFF";
    public static final String ALARM_ACTION_PHONE_REBOOTED = "PHONE_REBOOTED";
    private static final String ALARM_BACKUP_ACTION = "com.logitech.ue.services.UEAlarmService.ALARM_BACKUP_ACTION";
    public static final String ALARM_FIRED = "ALARM_FIRED";
    private static final String ALARM_MONOPOLY_MODE_ACTION = "com.logitech.ue.services.UEAlarmService.ALARM_MONOPOLY_MODE_ACTION";
    public static final String ALARM_RUNNING = "ALARM_SNOOZING";
    private static final int BACKUP_DELAY = 30000;
    private static final int COUNTDOWN_INTERVAL = 4000;
    private static final int MONOPOLY_MODE_DELAY = -15000;
    private static final int NOTIFICATION_ID = 1;
    private static final int PLAYBACK_TIME = 3600000;
    public static final int STOP_FOR_SNOOZE = 1;
    public static final int STOP_FOR_STOP = 2;
    private static final String TAG;
    public static AlarmState mAlarmState;
    private PendingIntent mAlarmBackupPendingIntent;
    private AlarmManager mAlarmManager;
    private PendingIntent mAlarmMonopolyModePendingIntent;
    final BroadcastReceiver mAlarmTimerReceiver;
    private AudioManager mAudioManager;
    final BroadcastReceiver mBTConnectionReceiver;
    final BroadcastReceiver mBTSessionReceiver;
    private String mBannerMessage;
    private final IBinder mBinder;
    private final Context mContext;
    private MediaPlayer mMediaplayer;
    private Queue<String> mMusicQueue;
    PendingIntent mPIntent;
    private long mPlaybackTimeLeft;
    private CountDownTimer mPlaybackTimer;
    private int mSavedSpeakerVolume;
    private int mSavedStreamVolume;
    private long mScheduledTime;
    
    static {
        TAG = UEAlarmService.class.getSimpleName();
        UEAlarmService.mAlarmState = AlarmState.OFF;
    }
    
    public UEAlarmService() {
        this.mBinder = (IBinder)new AlarmServiceBinder();
        this.mContext = (Context)this;
        this.mSavedStreamVolume = -1;
        this.mSavedSpeakerVolume = -1;
        this.mBTConnectionReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent obj) {
                Log.d(UEAlarmService.TAG, "Receive broadcast. Intent: " + obj);
                if ("com.logitech.ue.centurion.CONNECTION_CHANGED".equals(obj.getAction())) {
                    Log.d(UEAlarmService.TAG, "Device connection state changed to " + UEDeviceStatus.getStatus(obj.getIntExtra("status", UEDeviceStatus.UNKNOWN.getValue())));
                }
            }
        };
        this.mBTSessionReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent obj) {
                Log.d(UEAlarmService.TAG, "Receive broadcast. Intent: " + obj);
                if (TextUtils.equals((CharSequence)"com.logitech.ue.centurion.ALARM_NOTIFICATION", (CharSequence)obj.getAction())) {
                    final UEAlarmNotification ueAlarmNotification = (UEAlarmNotification)obj.getParcelableExtra("notification");
                    Log.d(UEAlarmService.TAG, "Receive alarm notification " + ueAlarmNotification.getAlarmNotificationFlag());
                    final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
                    if (connectedDevice == null || !connectedDevice.getDeviceConnectionStatus().isBtClassicConnectedState()) {
                        Log.wtf(UEAlarmService.TAG, "Device can be NULL if DeviceManager sends alarm change event");
                    }
                    else {
                        if (ueAlarmNotification.getAlarmNotificationFlag() == UEAlarmNotificationFlags.OFF) {
                            if (UEAlarmService.mAlarmState != AlarmState.FIRE) {
                                Log.d(UEAlarmService.TAG, "Ignore Alarm turned off, because we are not in FIRE state");
                                return;
                            }
                            Log.d(UEAlarmService.TAG, "Alarm turned off");
                            while (true) {
                                try {
                                    connectedDevice.ackAlarm();
                                    UEAlarmService.this.stopPlayback();
                                    UEAlarmService.this.finishAlarm();
                                    return;
                                }
                                catch (UEException ex) {
                                    ex.printStackTrace();
                                    continue;
                                }
                                break;
                            }
                        }
                        if (ueAlarmNotification.getAlarmNotificationFlag() == UEAlarmNotificationFlags.FIRE) {
                            if (UEAlarmService.this.mAlarmManager != null && UEAlarmService.this.mAlarmBackupPendingIntent != null) {
                                UEAlarmService.this.mAlarmManager.cancel(UEAlarmService.this.mAlarmBackupPendingIntent);
                                Log.d(UEAlarmService.TAG, "Phone backup alarm cancelled");
                            }
                            UEAlarmService.mAlarmState = AlarmState.FIRE;
                            while (true) {
                                try {
                                    if (AlarmSettings.getMusicType().equals(AlarmMusicType.LAST_PLAY)) {
                                        Log.d(UEAlarmService.TAG, "Ask speaker to send AVRCP play command");
                                        connectedDevice.ackAlarm();
                                        UEAlarmService.this.setStreamingVolume(connectedDevice);
                                        connectedDevice.sendAVRCPCommand(UEAVRCP.PLAY);
                                        UEAlarmService.this.startPlaybackTimer();
                                    }
                                    else {
                                        connectedDevice.ackAlarm();
                                        UEAlarmService.this.playLocalMusic(connectedDevice);
                                    }
                                    UEAlarmService.this.launchAlarmPopupConsole(false);
                                    return;
                                }
                                catch (UEException ex2) {
                                    ex2.printStackTrace();
                                    continue;
                                }
                                continue;
                            }
                        }
                        if (ueAlarmNotification.getAlarmNotificationFlag() == UEAlarmNotificationFlags.SNOOZE) {
                            while (true) {
                                try {
                                    connectedDevice.ackAlarm();
                                    UEAlarmService.this.stopPlayback();
                                    return;
                                }
                                catch (UEException ex3) {
                                    ex3.printStackTrace();
                                    continue;
                                }
                                break;
                            }
                        }
                        Log.e(UEAlarmService.TAG, "Unrecognised alarm status");
                    }
                }
            }
        };
        this.mAlarmTimerReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                Log.d(UEAlarmService.TAG, "Broadcast received: " + intent.getAction());
                if (intent.getAction().equals("com.logitech.ue.services.UEAlarmService.ALARM_BACKUP_ACTION")) {
                    Log.w(UEAlarmService.TAG, "Device not connected yet. Launching phone backup alarm.");
                    UEAlarmService.this.playBackMusic(2131099747);
                    UEAlarmService.this.launchAlarmPopupConsole(true);
                }
                else if (intent.getAction().equals("com.logitech.ue.services.UEAlarmService.ALARM_MONOPOLY_MODE_ACTION")) {
                    Log.d(UEAlarmService.TAG, "Change alarm state to PRE FIRE");
                    UEAlarmService.mAlarmState = AlarmState.PRE_FIRE;
                }
            }
        };
        this.mMediaplayer = new MediaPlayer();
    }
    
    @SuppressLint({ "NewApi" })
    private void displayBannerNotification() {
        final Intent launchIntentForPackage = this.getPackageManager().getLaunchIntentForPackage(App.getInstance().getPackageName());
        launchIntentForPackage.addFlags(67108864);
        this.mPIntent = PendingIntent.getActivity(this.mContext, 0, launchIntentForPackage, 268435456);
        this.mBannerMessage = (Object)this.getResources().getText(2131165220) + " " + DateFormat.getTimeFormat(this.mContext).format(AlarmSettings.getTime());
        final Notification$Builder setContentIntent = new Notification$Builder(this.mContext).setContentTitle((CharSequence)(this.getString(2131165477) + " " + this.getString(2131165209))).setContentText((CharSequence)this.mBannerMessage).setSmallIcon(17301550).setContentIntent(this.mPIntent);
        Notification notification;
        if (Build$VERSION.SDK_INT >= 16) {
            notification = setContentIntent.build();
        }
        else {
            notification = setContentIntent.getNotification();
        }
        this.startForeground(1, notification);
        Log.d(UEAlarmService.TAG, UEAlarmService.TAG + " runs at foreground");
    }
    
    private ArrayDeque<String> getFileListBySelection(final MusicSelection musicSelection) {
        Log.d(UEAlarmService.TAG, "Generate audio queue by MusicSelection");
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
        final Cursor query = this.getContentResolver().query(MediaStore$Audio$Media.EXTERNAL_CONTENT_URI, new String[] { "_data" }, s3, (String[])null, (String)null);
        while (query.moveToNext()) {
            arrayDeque.add(query.getString(0));
        }
        query.close();
        return arrayDeque;
    }
    
    private void playBackMusic(final int n) {
        if (!this.mAudioManager.isSpeakerphoneOn()) {
            this.mAudioManager.setSpeakerphoneOn(true);
        }
        if (this.mMediaplayer != null) {
            this.mMediaplayer.release();
            Log.w(UEAlarmService.TAG, "Media Player released from previous usage!");
        }
        this.mMediaplayer = MediaPlayer.create(this.mContext, n);
        Log.d(UEAlarmService.TAG, "Media Player is idle");
        this.mMediaplayer.setAudioStreamType(3);
        this.mSavedStreamVolume = this.mAudioManager.getStreamVolume(3);
        this.mAudioManager.setStreamVolume(3, UserPreferences.getInstance().getAlarmVolume(), 0);
        this.mMediaplayer.start();
        Log.d(UEAlarmService.TAG, "Media Player start playback");
        this.mMediaplayer.setOnCompletionListener((MediaPlayer$OnCompletionListener)new MediaPlayer$OnCompletionListener() {
            public void onCompletion(final MediaPlayer mediaPlayer) {
                UEAlarmService.this.mAudioManager.setStreamVolume(3, UEAlarmService.this.mSavedStreamVolume, 0);
            }
        });
    }
    
    private void playLocalMusic(final UEGenericDevice ueGenericDevice) {
        if (!this.mAudioManager.isBluetoothA2dpOn()) {
            Log.e(UEAlarmService.TAG, "A2DP routing is NOT on.");
        }
        if (this.mMediaplayer != null && this.mMediaplayer.isPlaying()) {
            Log.w(UEAlarmService.TAG, "Assuming alarm is running. Ignore incoming playback request");
        }
        else if (Utils.isReadExternalStoragePermissionGranted()) {
            AlarmSettings.setMusicType(AlarmSettings.getMusicType());
            AlarmSettings.setMusicSelection(AlarmSettings.getMusicSelection());
            this.setMusicPlayerQueue(this.getFileListBySelection(AlarmSettings.getMusicSelection()));
            this.playMusic(AlarmSettings.getMusicType(), ueGenericDevice);
        }
        else {
            this.playBackMusic(2131099747);
        }
    }
    
    private void playMusic(final AlarmMusicType alarmMusicType, final UEGenericDevice ueGenericDevice) {
        if (alarmMusicType == AlarmMusicType.SINGLE_SOUND || alarmMusicType == AlarmMusicType.MULTI_SOUND) {
            this.playMusicMediaPlayer(ueGenericDevice);
        }
    }
    
    private void playMusicMediaPlayer(final UEGenericDevice streamingVolume) {
        Log.d(UEAlarmService.TAG, "playMusicMediaPlayer");
        this.setStreamingVolume(streamingVolume);
        this.mMediaplayer.setOnCompletionListener((MediaPlayer$OnCompletionListener)new MediaPlayer$OnCompletionListener() {
            public void onCompletion(final MediaPlayer mediaPlayer) {
                UEAlarmService.this.mAudioManager.setStreamVolume(3, UEAlarmService.this.mSavedStreamVolume, 0);
            }
        });
        this.startPlaybackTimer();
        this.playNextSong();
    }
    
    private void playNextSong() {
        if (this.mMusicQueue == null || this.mMusicQueue.size() == 0) {
            Log.d(UEAlarmService.TAG, "Music queue is empty");
        }
        else {
            try {
                final String s = this.mMusicQueue.poll();
                Log.d(UEAlarmService.TAG, "Music play next song: " + s);
                this.mMusicQueue.add(s);
                this.mMediaplayer.reset();
                this.mMediaplayer.setDataSource(s);
                this.mMediaplayer.prepareAsync();
                this.mMediaplayer.setOnPreparedListener((MediaPlayer$OnPreparedListener)new MediaPlayer$OnPreparedListener() {
                    public void onPrepared(final MediaPlayer mediaPlayer) {
                        UEAlarmService.this.mMediaplayer.start();
                    }
                });
                this.mMediaplayer.setOnCompletionListener((MediaPlayer$OnCompletionListener)new MediaPlayer$OnCompletionListener() {
                    public void onCompletion(final MediaPlayer mediaPlayer) {
                        UEAlarmService.this.playNextSong();
                    }
                });
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void resumeAlarmAfterPhoneReboot() {
        Log.d(UEAlarmService.TAG, "Recovering alarm setting");
        this.displayBannerNotification();
    }
    
    private void setStreamingVolume(final UEGenericDevice p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ifnull          46
        //     4: aload_0        
        //     5: aload_1        
        //     6: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.getVolume:()I
        //     9: putfield        com/logitech/ue/services/UEAlarmService.mSavedSpeakerVolume:I
        //    12: getstatic       com/logitech/ue/services/UEAlarmService.TAG:Ljava/lang/String;
        //    15: astore_2       
        //    16: new             Ljava/lang/StringBuilder;
        //    19: astore_3       
        //    20: aload_3        
        //    21: invokespecial   java/lang/StringBuilder.<init>:()V
        //    24: aload_2        
        //    25: aload_3        
        //    26: ldc_w           "Save current speaker volume "
        //    29: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    32: aload_0        
        //    33: getfield        com/logitech/ue/services/UEAlarmService.mSavedSpeakerVolume:I
        //    36: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    39: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    42: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    45: pop            
        //    46: aload_0        
        //    47: aload_0        
        //    48: getfield        com/logitech/ue/services/UEAlarmService.mAudioManager:Landroid/media/AudioManager;
        //    51: iconst_3       
        //    52: invokevirtual   android/media/AudioManager.getStreamVolume:(I)I
        //    55: putfield        com/logitech/ue/services/UEAlarmService.mSavedStreamVolume:I
        //    58: getstatic       com/logitech/ue/services/UEAlarmService.TAG:Ljava/lang/String;
        //    61: new             Ljava/lang/StringBuilder;
        //    64: dup            
        //    65: invokespecial   java/lang/StringBuilder.<init>:()V
        //    68: ldc_w           "Save current phone volume "
        //    71: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    74: aload_0        
        //    75: getfield        com/logitech/ue/services/UEAlarmService.mSavedStreamVolume:I
        //    78: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    81: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    84: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    87: pop            
        //    88: aload_1        
        //    89: ifnull          132
        //    92: aload_1        
        //    93: invokestatic    com/logitech/ue/devicedata/AlarmSettings.getVolume:()I
        //    96: invokevirtual   com/logitech/ue/centurion/device/UEGenericDevice.setVolume:(I)V
        //    99: getstatic       com/logitech/ue/services/UEAlarmService.TAG:Ljava/lang/String;
        //   102: astore_1       
        //   103: new             Ljava/lang/StringBuilder;
        //   106: astore_2       
        //   107: aload_2        
        //   108: invokespecial   java/lang/StringBuilder.<init>:()V
        //   111: aload_1        
        //   112: aload_2        
        //   113: ldc_w           "Set speaker alarm volume to "
        //   116: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   119: invokestatic    com/logitech/ue/devicedata/AlarmSettings.getVolume:()I
        //   122: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   125: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   128: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   131: pop            
        //   132: aload_0        
        //   133: getfield        com/logitech/ue/services/UEAlarmService.mAudioManager:Landroid/media/AudioManager;
        //   136: iconst_3       
        //   137: invokestatic    com/logitech/ue/devicedata/AlarmSettings.getVolume:()I
        //   140: iconst_0       
        //   141: invokevirtual   android/media/AudioManager.setStreamVolume:(III)V
        //   144: getstatic       com/logitech/ue/services/UEAlarmService.TAG:Ljava/lang/String;
        //   147: new             Ljava/lang/StringBuilder;
        //   150: dup            
        //   151: invokespecial   java/lang/StringBuilder.<init>:()V
        //   154: ldc_w           "Set phone alarm volume to "
        //   157: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   160: invokestatic    com/logitech/ue/devicedata/AlarmSettings.getVolume:()I
        //   163: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   166: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   169: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   172: pop            
        //   173: return         
        //   174: astore_2       
        //   175: aload_2        
        //   176: invokevirtual   com/logitech/ue/centurion/exceptions/UEException.printStackTrace:()V
        //   179: goto            46
        //   182: astore_1       
        //   183: aload_1        
        //   184: invokevirtual   com/logitech/ue/centurion/exceptions/UEException.printStackTrace:()V
        //   187: goto            132
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                              
        //  -----  -----  -----  -----  --------------------------------------------------
        //  4      46     174    182    Lcom/logitech/ue/centurion/exceptions/UEException;
        //  92     132    182    190    Lcom/logitech/ue/centurion/exceptions/UEException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0132:
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
    
    private void startPlaybackTimer() {
        if (this.mPlaybackTimer != null) {
            Log.e(UEAlarmService.TAG, "Check logic. mPlaybackTimer should be NULL here.");
            this.mPlaybackTimer = null;
        }
        Log.d(UEAlarmService.TAG, "Playback time left: " + this.mPlaybackTimeLeft / 1000L + "s");
        this.mPlaybackTimer = new CountDownTimer(this.mPlaybackTimeLeft, 4000L) {
            public void onFinish() {
                UEAlarmService.this.stopPlayback();
                final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
                if (connectedDevice == null) {
                    return;
                }
                try {
                    connectedDevice.stopAlarm();
                }
                catch (UEException ex) {
                    ex.printStackTrace();
                }
            }
            
            public void onTick(final long n) {
                UEAlarmService.this.mPlaybackTimeLeft -= 4000L;
                Log.d(UEAlarmService.TAG, "Time left: " + UEAlarmService.this.mPlaybackTimeLeft / 1000L + "s");
            }
        }.start();
    }
    
    private void stopPlaybackTimer() {
        if (this.mPlaybackTimer != null) {
            this.mPlaybackTimer.cancel();
            this.mPlaybackTimer = null;
        }
        Log.d(UEAlarmService.TAG, "Playback time left: " + this.mPlaybackTimeLeft / 1000L + "s");
    }
    
    public void finishAlarm() {
        this.stopPlaybackTimer();
        UEAlarmService.mAlarmState = AlarmState.OFF;
        if (AlarmSettings.isRepeat()) {
            Log.d(UEAlarmService.TAG, "Update banner displaying for next alarm");
            this.setupAlarm(false);
        }
        else {
            AlarmSettings.setOn(false);
            this.stopForeground(true);
            this.stopSelf();
        }
    }
    
    public void launchAlarmPopupConsole(final boolean b) {
        if (App.getInstance().getCurrentActivity() == null || !App.getInstance().getCurrentActivity().getClass().equals(AlarmPopupActivity.class)) {
            final Intent intent = new Intent((Context)this, (Class)AlarmPopupActivity.class);
            intent.setAction("ALARM_FIRED");
            intent.addFlags(32768);
            intent.addFlags(268435456);
            intent.putExtra("isBackupAlarm", b);
            this.startActivity(intent);
        }
    }
    
    public IBinder onBind(final Intent intent) {
        Log.d(UEAlarmService.TAG, "onBind receives. Action: " + intent.getAction());
        return this.mBinder;
    }
    
    public void onCreate() {
        super.onCreate();
        Log.d(UEAlarmService.TAG, "Create");
        this.mAudioManager = (AudioManager)this.getSystemService("audio");
        this.mSavedStreamVolume = this.mAudioManager.getStreamVolume(3);
        this.mAlarmManager = (AlarmManager)this.getSystemService("alarm");
        LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.mBTConnectionReceiver, new IntentFilter("com.logitech.ue.centurion.CONNECTION_CHANGED"));
        LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.mBTSessionReceiver, new IntentFilter("com.logitech.ue.centurion.ALARM_NOTIFICATION"));
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.logitech.ue.services.UEAlarmService.ALARM_BACKUP_ACTION");
        intentFilter.addAction("com.logitech.ue.services.UEAlarmService.ALARM_MONOPOLY_MODE_ACTION");
        this.getApplicationContext().registerReceiver(this.mAlarmTimerReceiver, intentFilter);
        this.mAlarmBackupPendingIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.logitech.ue.services.UEAlarmService.ALARM_BACKUP_ACTION"), 268435456);
        this.mAlarmMonopolyModePendingIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.logitech.ue.services.UEAlarmService.ALARM_MONOPOLY_MODE_ACTION"), 268435456);
    }
    
    public void onDestroy() {
        Log.d(UEAlarmService.TAG, "Destroy");
        LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.mBTConnectionReceiver);
        LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.mBTSessionReceiver);
        this.getApplicationContext().unregisterReceiver(this.mAlarmTimerReceiver);
        while (true) {
            if (this.mMediaplayer == null) {
                break Label_0079;
            }
            if (this.mMediaplayer.isPlaying()) {
                this.mMediaplayer.stop();
            }
            try {
                this.mMediaplayer.release();
                this.mMediaplayer = null;
                UEAlarmService.mAlarmState = AlarmState.OFF;
                Log.d(UEAlarmService.TAG, UEAlarmService.TAG + " destroyed");
                super.onDestroy();
            }
            catch (Exception ex) {
                this.mMediaplayer = null;
                continue;
            }
            finally {
                this.mMediaplayer = null;
            }
            break;
        }
    }
    
    public int onStartCommand(final Intent intent, final int n, final int n2) {
        Label_0035: {
            if (UEDeviceManager.getInstance().isReady()) {
                break Label_0035;
            }
            try {
                final InitManagerTask initManagerTask = new InitManagerTask();
                initManagerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]).get();
                if (intent == null) {
                    Log.w(UEAlarmService.TAG, "Alarm service re-launched with NULL intent");
                    if (AlarmSettings.isOn()) {
                        this.setupAlarm(false);
                    }
                    Label_0060: {
                        return 3;
                    }
                }
                goto Label_0072;
            }
            catch (InterruptedException ex) {}
            catch (ExecutionException initManagerTask) {
                goto Label_0064;
            }
        }
        try {
            this.mMediaplayer.stop();
            this.mMediaplayer.release();
            AlarmSettings.setOn(false);
            this.stopSelf();
            return 3;
            this.setupAlarm();
            return 3;
            // iftrue(Label_0060:, !"PHONE_REBOOTED".equals((Object)intent))
            // iftrue(Label_0226:, !AlarmSettings.isOn())
            // iftrue(Label_0060:, !AlarmSettings.isOn())
            // iftrue(Label_0204:, !"ALARM_RESETUP".equals((Object)intent))
            while (true) {
            Block_14:
                while (true) {
                    this.setupAlarm(false);
                    return 3;
                    Label_0204: {
                        break Block_14;
                    }
                    Log.w(UEAlarmService.TAG, "Alarm service re-launched intent");
                    continue;
                }
                this.resumeAlarmAfterPhoneReboot();
                return 3;
                continue;
            }
            Label_0226: {
                Log.d(UEAlarmService.TAG, "No alarm running. Stop alarm service");
            }
            this.stopSelf();
            return 3;
        }
        catch (Exception ex2) {}
    }
    
    public void setAlarmToDevice(final UEGenericDevice ueGenericDevice) throws UEConnectionException, UEOperationException, UEOperationTimeOutException {
        if (ueGenericDevice != null && ueGenericDevice.getDeviceConnectionStatus().isBtClassicConnectedState()) {
            final MAC bluetoothMacAddress = App.getInstance().getBluetoothMacAddress();
            if (AlarmSettings.isOn()) {
                ueGenericDevice.setAlarm(AlarmSettings.getTimerInMilliseconds(), bluetoothMacAddress);
                ueGenericDevice.setSnoozeTimeAlarm(10, bluetoothMacAddress);
                ueGenericDevice.setAlarmVolume(AlarmSettings.getVolume(), bluetoothMacAddress);
                ueGenericDevice.setAlarmBackupTone(2, bluetoothMacAddress);
                if (AlarmSettings.isRepeat()) {
                    Log.d(UEAlarmService.TAG, "Alarm daily repeat set");
                    ueGenericDevice.setRepeatAlarm(true, bluetoothMacAddress);
                }
                else {
                    ueGenericDevice.setRepeatAlarm(false, bluetoothMacAddress);
                }
                this.displayBannerNotification();
                Log.d(UEAlarmService.TAG, "Alarm set at " + AlarmSettings.getTimerInMilliseconds() / 1000L + " seconds away");
            }
            else {
                ueGenericDevice.clearAlarm(bluetoothMacAddress);
            }
        }
        else {
            Log.e(UEAlarmService.TAG, "Device is not reachable via BT classic");
        }
    }
    
    public void setMusicPlayerQueue(final Queue<String> mMusicQueue) {
        this.mMusicQueue = mMusicQueue;
        this.mMediaplayer.reset();
    }
    
    public void setupAlarm() {
        this.setupAlarm(true);
    }
    
    public void setupAlarm(final boolean b) {
        final Time time = AlarmSettings.getTime();
        this.mMusicQueue = this.getFileListBySelection(AlarmSettings.getMusicSelection());
        final long timerInMilliseconds = AlarmSettings.getTimerInMilliseconds();
        this.mScheduledTime = System.currentTimeMillis() + timerInMilliseconds;
        if (time == null) {
            Log.e(UEAlarmService.TAG, "alarmTime is NULL");
            this.stopSelf();
        }
        else if (timerInMilliseconds <= 0L) {
            Log.e(UEAlarmService.TAG, "timer is less than 0");
            this.stopSelf();
        }
        else {
            UEAlarmService.mAlarmState = AlarmState.WAITING;
            this.mPlaybackTimeLeft = 3600000L;
        Label_0099:
            while (true) {
                if (!b) {
                    break Label_0099;
                }
            Label_0157_Outer:
                while (true) {
                    final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
                    while (true) {
                        Label_0184: {
                            while (true) {
                                try {
                                    this.setAlarmToDevice(connectedDevice);
                                    if (Build$VERSION.SDK_INT >= 19) {
                                        break Label_0184;
                                    }
                                    this.mAlarmManager.set(0, this.mScheduledTime + 30000L, this.mAlarmBackupPendingIntent);
                                    final AlarmManager mAlarmManager = this.mAlarmManager;
                                    if (this.mScheduledTime - 15000L < 0L) {
                                        final long n = 0L;
                                        mAlarmManager.set(0, n, this.mAlarmMonopolyModePendingIntent);
                                        this.displayBannerNotification();
                                        break;
                                    }
                                }
                                catch (UEException ex) {
                                    ex.printStackTrace();
                                    continue Label_0099;
                                }
                                final long n = this.mScheduledTime - 15000L;
                                continue Label_0157_Outer;
                            }
                        }
                        this.mAlarmManager.setExact(0, this.mScheduledTime + 30000L, this.mAlarmBackupPendingIntent);
                        final AlarmManager mAlarmManager2 = this.mAlarmManager;
                        long n2;
                        if (this.mScheduledTime - 15000L < 0L) {
                            n2 = 0L;
                        }
                        else {
                            n2 = this.mScheduledTime - 15000L;
                        }
                        mAlarmManager2.setExact(0, n2, this.mAlarmMonopolyModePendingIntent);
                        continue;
                    }
                }
                break;
            }
        }
    }
    
    public void snoozeAlarm(final boolean b) {
        UEAlarmService.mAlarmState = AlarmState.SNOOZE;
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        this.stopPlayback(1, connectedDevice);
        if (b || connectedDevice == null) {
            return;
        }
        try {
            connectedDevice.snoozeAlarm();
        }
        catch (UEException ex) {
            ex.printStackTrace();
        }
    }
    
    public void stopPlayback() {
        this.stopPlayback(0, UEDeviceManager.getInstance().getConnectedDevice());
    }
    
    public void stopPlayback(final int n) {
        this.stopPlayback(n, UEDeviceManager.getInstance().getConnectedDevice());
    }
    
    public void stopPlayback(final int n, UEGenericDevice setContentIntent) {
        this.stopPlaybackTimer();
        if (this.mMediaplayer != null && this.mMediaplayer.isPlaying()) {
            this.mMediaplayer.stop();
            Log.d(UEAlarmService.TAG, "Mediaplayer stopped");
        }
        while (true) {
            if (setContentIntent == null || !AlarmSettings.getMusicType().equals(AlarmMusicType.LAST_PLAY)) {
                break Label_0071;
            }
            try {
                setContentIntent.sendAVRCPCommand(UEAVRCP.STOP);
                Log.d(UEAlarmService.TAG, "AVRCP STOP command sent");
                if (setContentIntent != null && this.mSavedSpeakerVolume != -1) {
                    ((SafeTask<Void, Progress, Result>)new SetVolumeTask(this.mSavedSpeakerVolume) {
                        @Override
                        public Void work(final Void... array) throws Exception {
                            Log.d(UEAlarmService.TAG, "Waiting for streaming completely finish");
                            Thread.sleep(1000L);
                            UEAlarmService.this.mAudioManager.setStreamVolume(3, UEAlarmService.this.mSavedSpeakerVolume, 0);
                            return super.work(new Void[0]);
                        }
                    }).executeOnThreadPoolExecutor(new Void[0]);
                    Log.d(UEAlarmService.TAG, "Restore speaker volume back to " + this.mSavedSpeakerVolume);
                }
                if (n == 1) {
                    Log.d(UEAlarmService.TAG, "Update banner displaying Snoozing");
                    setContentIntent = (UEGenericDevice)new NotificationCompat.Builder(this.mContext).setContentTitle(this.getString(2131165477) + " " + this.getString(2131165209)).setContentText(this.getString(2131165221)).setSmallIcon(17301550).setContentIntent(this.mPIntent);
                    ((NotificationManager)this.getSystemService("notification")).notify(1, ((NotificationCompat.Builder)setContentIntent).build());
                }
            }
            catch (UEException ex) {
                ex.printStackTrace();
                continue;
            }
            break;
        }
    }
    
    public boolean stopService(final Intent intent) {
        Log.w(UEAlarmService.TAG, "UEAlarmService stopped");
        UEAlarmService.mAlarmState = AlarmState.OFF;
        return super.stopService(intent);
    }
    
    public class AlarmServiceBinder extends Binder
    {
        public UEAlarmService getService() {
            return UEAlarmService.this;
        }
    }
    
    public enum AlarmState
    {
        FIRE, 
        OFF, 
        PRE_FIRE, 
        SNOOZE, 
        WAITING;
    }
}
