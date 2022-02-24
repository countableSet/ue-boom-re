// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue;

import com.logitech.ue.services.UEVoiceService;
import com.logitech.ue.services.UEAlarmService;
import com.logitech.ue.devicedata.AlarmSettings;
import android.util.Log;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class OnUpgradeReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, final Intent intent) {
        Log.d(this.getClass().getSimpleName(), "Receive event " + intent.getAction());
        if ("android.intent.action.MY_PACKAGE_REPLACED".equals(intent.getAction()) || "android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            if (App.getInstance() != null && AlarmSettings.isOn()) {
                if ("android.intent.action.PACKAGE_REPLACED".equals(intent.getAction())) {
                    Log.d(this.getClass().getSimpleName(), "App was upgraded. Relaunch the alarm.");
                }
                else {
                    Log.d(this.getClass().getSimpleName(), "Phone was rebooted. Relaunch the alarm.");
                }
                final Intent intent2 = new Intent((Context)App.getInstance(), (Class)UEAlarmService.class);
                intent2.setAction("ALARM_RESETUP");
                App.getInstance().startService(intent2);
            }
            if (App.getInstance() != null) {
                App.getInstance().startService(new Intent((Context)App.getInstance(), (Class)UEVoiceService.class));
            }
        }
    }
}
