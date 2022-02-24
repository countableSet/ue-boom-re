// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.notification.notificator;

import com.logitech.ue.centurion.device.command.AlarmGetCommand;
import com.logitech.ue.centurion.device.command.UEDeviceCommand;
import com.logitech.ue.centurion.notification.UENotification;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Parcelable;
import android.content.Intent;
import android.content.Context;
import com.logitech.ue.centurion.notification.UEAlarmNotification;

public class UEAlarmNotificator implements IUENotificator<UEAlarmNotification>
{
    String mAction;
    Context mContext;
    String mNotificationExtraKey;
    
    public UEAlarmNotificator(final String mAction, final String mNotificationExtraKey, final Context mContext) {
        this.mAction = mAction;
        this.mNotificationExtraKey = mNotificationExtraKey;
        this.mContext = mContext;
    }
    
    @Override
    public void onNotificationReceived(final UEAlarmNotification ueAlarmNotification) {
        final Intent intent = new Intent(this.mAction);
        intent.putExtra(this.mNotificationExtraKey, (Parcelable)ueAlarmNotification);
        LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent);
    }
    
    @Override
    public boolean processNotification(final byte[] array) {
        boolean b = true;
        if (array[1] == UEDeviceCommand.UECommand.ReturnAlarm.getMostSignificantByte() && array[2] == UEDeviceCommand.UECommand.ReturnAlarm.getLeastSignificantByte() && array[3] == AlarmGetCommand.NOTIFICATION.getCode()) {
            this.onNotificationReceived(new UEAlarmNotification(array));
        }
        else {
            b = false;
        }
        return b;
    }
}
