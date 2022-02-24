// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.activities;

import android.content.Intent;
import com.logitech.ue.services.UEAlarmService;
import android.app.Activity;
import com.logitech.ue.App;
import android.util.Log;
import com.flurry.android.FlurryAgent;
import android.os.Bundle;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.content.Context;
import java.util.Iterator;
import android.os.AsyncTask$Status;
import android.os.AsyncTask;
import java.util.ArrayList;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity
{
    private static int mNumberOfInstance;
    private final ArrayList<AsyncTask<?, ?, ?>> mTaskList;
    
    static {
        BaseActivity.mNumberOfInstance = 0;
    }
    
    public BaseActivity() {
        this.mTaskList = new ArrayList<AsyncTask<?, ?, ?>>();
    }
    
    public void abortAllTasks() {
        for (final AsyncTask<?, ?, ?> asyncTask : this.mTaskList) {
            if (asyncTask != null && asyncTask.getStatus() != AsyncTask$Status.FINISHED) {
                asyncTask.cancel(true);
            }
        }
        this.mTaskList.clear();
    }
    
    protected void attachBaseContext(final Context context) {
        super.attachBaseContext((Context)CalligraphyContextWrapper.wrap(context));
    }
    
    public String getTag() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        FlurryAgent.init((Context)this, "39XRGF98QFB523KT9CJD");
    }
    
    @Override
    protected void onDestroy() {
        Log.i(this.getTag(), "Destroy");
        this.abortAllTasks();
        super.onDestroy();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(this.getTag(), "Pause");
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(this.getTag(), "Resume");
    }
    
    @Override
    protected void onStart() {
        Log.i(this.getTag(), "Start");
        App.getInstance().claimCurrentActivity(this);
        ++BaseActivity.mNumberOfInstance;
        if (BaseActivity.mNumberOfInstance == 1) {
            App.getInstance().onAppResume(this);
        }
        super.onStart();
        FlurryAgent.onStartSession((Context)this);
        if (!this.getClass().equals(AlarmPopupActivity.class)) {
            if (UEAlarmService.mAlarmState == UEAlarmService.AlarmState.SNOOZE) {
                final Intent intent = new Intent((Context)this, (Class)AlarmPopupActivity.class);
                intent.setAction("ALARM_SNOOZING");
                intent.addFlags(268435456);
                intent.addFlags(32768);
                this.startActivity(intent);
                this.finish();
            }
            else if (UEAlarmService.mAlarmState == UEAlarmService.AlarmState.FIRE) {
                final Intent intent2 = new Intent((Context)this, (Class)AlarmPopupActivity.class);
                intent2.setAction("ALARM_FIRED");
                intent2.addFlags(268435456);
                intent2.addFlags(32768);
                this.startActivity(intent2);
                this.finish();
            }
        }
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(this.getTag(), "Stop");
        --BaseActivity.mNumberOfInstance;
        if (BaseActivity.mNumberOfInstance == 0) {
            App.getInstance().onAppPause(this);
        }
        App.getInstance().releaseCurrentActivity(this);
        FlurryAgent.onEndSession((Context)this);
    }
    
    public void registerTask(final AsyncTask<?, ?, ?> e) {
        if (e != null) {
            this.mTaskList.add(e);
        }
        else {
            Log.e(this.getTag(), "An abandoned task is trying to register itself.");
        }
    }
    
    public void unregisterTask(final AsyncTask<?, ?, ?> o) {
        this.mTaskList.remove(o);
    }
}
