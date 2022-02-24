// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.activities;

import android.text.format.DateFormat;
import android.content.IntentFilter;
import java.util.Calendar;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.exceptions.UEException;
import android.view.View$OnTouchListener;
import android.view.GestureDetector$OnGestureListener;
import com.logitech.ue.centurion.UEDeviceManager;
import android.view.MotionEvent;
import android.view.GestureDetector$SimpleOnGestureListener;
import android.view.View$OnClickListener;
import com.logitech.ue.FlurryTracker;
import com.logitech.ue.devicedata.AlarmSettings;
import com.logitech.ue.centurion.device.devicedata.UEAlarmNotificationFlags;
import com.logitech.ue.centurion.notification.UEAlarmNotification;
import android.content.Context;
import android.util.Log;
import android.os.IBinder;
import android.content.ComponentName;
import android.os.Handler;
import android.view.View;
import android.view.GestureDetector;
import android.content.Intent;
import com.logitech.ue.centurion.device.devicedata.UEAlarmStatus;
import android.widget.TextView;
import android.content.ServiceConnection;
import com.logitech.ue.services.UEAlarmService;
import android.widget.ImageView;
import android.content.BroadcastReceiver;

public class AlarmPopupActivity extends BaseActivity
{
    public static final String PARAM_BACKUP_ALARM = "isBackupAlarm";
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    final BroadcastReceiver mAlarmChangeReceiver;
    private ImageView mAlarmLogo;
    private UEAlarmService mAlarmService;
    private final ServiceConnection mAlarmServiceConn;
    private TextView mAlarmStateLabel;
    private UEAlarmStatus mAlarmStatus;
    private TextView mAmPmLabel;
    private Intent mCurrentIntent;
    private ImageView mDoubleArrow;
    private GestureDetector mGestureDetector;
    private TextView mHoursLabel;
    private boolean mIsAlarmBackup;
    private TextView mMinutesLabel;
    private View mRoot;
    private Handler mUpdateTimeHandler;
    
    public AlarmPopupActivity() {
        this.mAlarmService = null;
        this.mCurrentIntent = null;
        this.mAlarmServiceConn = (ServiceConnection)new ServiceConnection() {
            public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
                Log.d(AlarmPopupActivity.this.getTag(), "Connecting to UEAlarmService at " + System.currentTimeMillis());
                AlarmPopupActivity.this.mAlarmService = ((UEAlarmService.AlarmServiceBinder)binder).getService();
                if (AlarmPopupActivity.this.mAlarmService != null) {
                    Log.d(AlarmPopupActivity.this.getTag(), "UEAlarmService bound");
                    AlarmPopupActivity.this.initialiseUI();
                }
                else {
                    Log.e(AlarmPopupActivity.this.getTag(), "Oops, UEAlarmService not bound!");
                }
            }
            
            public void onServiceDisconnected(final ComponentName componentName) {
                Log.w(AlarmPopupActivity.this.getTag(), "UEAlarmService disconnected at " + System.currentTimeMillis());
            }
        };
        this.mAlarmChangeReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                Log.d(AlarmPopupActivity.this.getTag(), "Broadcast received: " + intent.getAction());
                if ("com.logitech.ue.centurion.ALARM_NOTIFICATION".equals(intent.getAction())) {
                    final UEAlarmNotification ueAlarmNotification = (UEAlarmNotification)intent.getParcelableExtra("notification");
                    if (!AlarmPopupActivity.this.mIsAlarmBackup) {
                        if (ueAlarmNotification.getAlarmNotificationFlag() == UEAlarmNotificationFlags.OFF) {
                            Log.d(AlarmPopupActivity.this.getTag(), "Alarm turned OFF on speaker");
                            AlarmPopupActivity.this.finish();
                        }
                        else if (ueAlarmNotification.getAlarmNotificationFlag() == UEAlarmNotificationFlags.SNOOZE) {
                            Log.d(AlarmPopupActivity.this.getTag(), "Alarm SNOOZED on speaker");
                            if (AlarmPopupActivity.this.mAlarmStatus != UEAlarmStatus.SNOOZING) {
                                AlarmPopupActivity.this.snoozeAlarm(true);
                                FlurryTracker.logAlarmSnooze(AlarmSettings.getMusicType().name());
                            }
                        }
                        else if (ueAlarmNotification.getAlarmNotificationFlag() == UEAlarmNotificationFlags.FIRE) {
                            Log.d(AlarmPopupActivity.this.getTag(), "Alarm FIRED");
                            AlarmPopupActivity.this.setPopupUI(UEAlarmStatus.FIRE);
                        }
                    }
                }
            }
        };
    }
    
    private void bindAlarmService(Intent mCurrentIntent) {
        this.mCurrentIntent = mCurrentIntent;
        mCurrentIntent = new Intent((Context)this, (Class)UEAlarmService.class);
        mCurrentIntent.setAction("ALARM_FIRED");
        this.bindService(mCurrentIntent, this.mAlarmServiceConn, 1);
    }
    
    private void handleIntent(final Intent intent) {
        final String action = intent.getAction();
        Log.d(this.getTag(), "Intent action: " + action);
        if ("ALARM_FIRED".equals(action)) {
            this.mIsAlarmBackup = intent.getBooleanExtra("isBackupAlarm", false);
            if (this.mIsAlarmBackup) {
                Log.w(this.getTag(), "Backup alarm fire");
                this.mAlarmStateLabel.setVisibility(8);
                this.mRoot.setOnClickListener((View$OnClickListener)null);
            }
            else {
                this.mAlarmStateLabel.setVisibility(0);
                this.setPopupUI(UEAlarmStatus.FIRE);
            }
        }
        else if ("ALARM_SNOOZING".equals(intent.getAction())) {
            this.setPopupUI(UEAlarmStatus.SNOOZING);
        }
        else {
            Log.e(this.getTag(), "Unrecognisable action: " + action);
        }
    }
    
    private void initialiseUI() {
        this.mRoot = this.getWindow().getDecorView().findViewById(16908290);
        this.mAlarmLogo = (ImageView)this.findViewById(2131624038);
        this.mAlarmStateLabel = (TextView)this.findViewById(2131624039);
        this.mDoubleArrow = (ImageView)this.findViewById(2131624041);
        this.mHoursLabel = (TextView)this.findViewById(2131624034);
        this.mMinutesLabel = (TextView)this.findViewById(2131624036);
        this.mAmPmLabel = (TextView)this.findViewById(2131624037);
        this.mGestureDetector = new GestureDetector((Context)this, (GestureDetector$OnGestureListener)new GestureDetector$SimpleOnGestureListener() {
            public boolean onDown(final MotionEvent motionEvent) {
                return true;
            }
            
            public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float a, final float n) {
                try {
                    if (Math.abs(motionEvent.getY() - motionEvent2.getY()) <= 250.0f && motionEvent2.getX() - motionEvent.getX() > 120.0f && Math.abs(a) > 200.0f) {
                        new Handler().postDelayed((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                FlurryTracker.logAlarmDismiss(AlarmSettings.getMusicType().name());
                                AlarmPopupActivity.this.stopAlarm(!AlarmPopupActivity.this.mIsAlarmBackup);
                            }
                        }, 200L);
                    }
                    return false;
                }
                catch (Exception ex) {
                    return false;
                }
            }
            
            public boolean onSingleTapUp(final MotionEvent motionEvent) {
                if (UEAlarmService.mAlarmState == UEAlarmService.AlarmState.FIRE) {
                    if (UEDeviceManager.getInstance().getConnectedDevice() == null) {
                        AlarmPopupActivity.this.stopAlarm(false);
                    }
                    else {
                        FlurryTracker.logAlarmSnooze(AlarmSettings.getMusicType().name());
                        AlarmPopupActivity.this.snoozeAlarm(false);
                    }
                }
                return super.onSingleTapUp(motionEvent);
            }
        });
        this.mRoot.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                return AlarmPopupActivity.this.mGestureDetector.onTouchEvent(motionEvent);
            }
        });
        if (this.mCurrentIntent != null) {
            this.handleIntent(this.mCurrentIntent);
        }
        else {
            Log.e(this.getTag(), "CurrentIntent is NULL");
        }
        this.updateTime();
    }
    
    private void snoozeAlarm(final boolean b) {
        this.mAlarmService.snoozeAlarm(b);
        this.mAlarmService.stopPlayback(1);
        this.setPopupUI(UEAlarmStatus.SNOOZING);
    }
    
    private void stopAlarm(final boolean b) {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        while (true) {
            if (connectedDevice == null || !b) {
                break Label_0019;
            }
            try {
                connectedDevice.stopAlarm();
                this.mAlarmService.stopPlayback();
                this.mAlarmService.finishAlarm();
                this.finish();
            }
            catch (UEException ex) {
                ex.printStackTrace();
                continue;
            }
            break;
        }
    }
    
    @Override
    public void onBackPressed() {
        Log.d(this.getTag(), "Ignore BACK on alarm console");
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mUpdateTimeHandler = new Handler();
        this.getWindow().addFlags(6815872);
        this.setContentView(2130968604);
        this.bindAlarmService(this.getIntent());
    }
    
    @Override
    protected void onDestroy() {
        while (true) {
            try {
                this.unbindService(this.mAlarmServiceConn);
                super.onDestroy();
            }
            catch (Exception ex) {
                Log.w(this.getTag(), "AlarmService was not bound");
                continue;
            }
            break;
        }
    }
    
    @Override
    protected void onNewIntent(final Intent intent) {
        this.bindAlarmService(intent);
    }
    
    @Override
    protected void onPause() {
        this.mUpdateTimeHandler.removeCallbacksAndMessages((Object)null);
        LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.mAlarmChangeReceiver);
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        this.updateTime();
        final Calendar instance = Calendar.getInstance();
        Log.e(this.getTag(), "Refresh time after " + (60 - instance.get(13)) * 1000);
        this.mUpdateTimeHandler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                AlarmPopupActivity.this.updateTime();
                AlarmPopupActivity.this.mUpdateTimeHandler.postDelayed((Runnable)this, 60000L);
            }
        }, (long)((60 - instance.get(13)) * 1000));
        LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.mAlarmChangeReceiver, new IntentFilter("com.logitech.ue.centurion.ALARM_NOTIFICATION"));
    }
    
    public void setPopupUI(final UEAlarmStatus ueAlarmStatus) {
        this.updateTime();
        this.mAlarmStatus = ueAlarmStatus;
        if (UEAlarmStatus.FIRE.equals(ueAlarmStatus) || UEAlarmStatus.STREAMING.equals(ueAlarmStatus) || UEAlarmStatus.STREAMING_AFTER_SNOOZE.equals(ueAlarmStatus)) {
            this.mAlarmLogo.setImageResource(2130837612);
            this.mAlarmStateLabel.setText(2131165427);
            this.mRoot.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    FlurryTracker.logAlarmSnooze(AlarmSettings.getMusicType().name());
                    AlarmPopupActivity.this.snoozeAlarm(false);
                }
            });
        }
        else if (UEAlarmStatus.SNOOZING.equals(ueAlarmStatus) || UEAlarmStatus.SNOOZING_AFTER_SNOOZE.equals(ueAlarmStatus)) {
            this.mAlarmLogo.setImageResource(2130837613);
            this.mAlarmStateLabel.setText(2131165403);
            this.mRoot.setOnClickListener((View$OnClickListener)null);
        }
    }
    
    public void updateTime() {
        if (this.mRoot != null) {
            final Calendar instance = Calendar.getInstance();
            if (DateFormat.is24HourFormat((Context)this)) {
                this.mAmPmLabel.setVisibility(4);
                this.mHoursLabel.setText((CharSequence)String.format("%02d", instance.get(11)));
            }
            else {
                this.mAmPmLabel.setVisibility(0);
                int value;
                if ((value = instance.get(10)) == 0) {
                    value = 12;
                }
                this.mHoursLabel.setText((CharSequence)String.format("%02d", value));
                final TextView mAmPmLabel = this.mAmPmLabel;
                String text;
                if (instance.get(9) == 0) {
                    text = "AM";
                }
                else {
                    text = "PM";
                }
                mAmPmLabel.setText((CharSequence)text);
            }
            this.mMinutesLabel.setText((CharSequence)String.format("%02d", instance.get(12)));
        }
    }
}
