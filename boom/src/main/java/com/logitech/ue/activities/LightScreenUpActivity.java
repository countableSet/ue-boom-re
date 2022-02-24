// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.activities;

import android.view.MotionEvent;
import android.os.Handler;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.app.Activity;

public class LightScreenUpActivity extends Activity
{
    public static final String ACTION_TURN_SCREEN_OFF = "ACTION_TURN_SCREEN_OFF";
    private static final String TAG;
    private Activity mContext;
    private BroadcastReceiver receiver;
    
    static {
        TAG = LightScreenUpActivity.class.getSimpleName();
    }
    
    public LightScreenUpActivity() {
        this.receiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if ("ACTION_TURN_SCREEN_OFF".equals(intent.getAction())) {
                    Log.d(LightScreenUpActivity.TAG, "Turn off screen lighting");
                    LightScreenUpActivity.this.mContext.finish();
                }
            }
        };
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        ((LightScreenUpActivity)(this.mContext = this)).registerReceiver(this.receiver, new IntentFilter("ACTION_TURN_SCREEN_OFF"));
        this.getWindow().addFlags(6815872);
        Log.d(LightScreenUpActivity.TAG, "*** Light up phone screen");
        new Handler().postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                Log.d(LightScreenUpActivity.TAG, "*** Finishing lighting activity");
                LightScreenUpActivity.this.mContext.finish();
            }
        }, 15000L);
    }
    
    protected void onDestroy() {
        while (true) {
            try {
                this.unregisterReceiver(this.receiver);
                Log.d(LightScreenUpActivity.TAG, "*** Lighting activity finished");
                super.onDestroy();
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        Log.d(LightScreenUpActivity.TAG, "*** User touched screen. Finishing lighting activity");
        this.mContext.finish();
        return true;
    }
}
