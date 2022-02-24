// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.notification.notificator;

import android.support.v4.content.LocalBroadcastManager;
import android.os.Parcelable;
import android.content.Intent;
import android.util.Log;
import com.logitech.ue.centurion.device.command.UEDeviceCommand;
import android.content.Context;
import com.logitech.ue.centurion.notification.UENotification;

public class UEBroadcastNotificator<T extends UENotification> extends UEGenericNotificator<T>
{
    private static final String TAG = "UEBroadcastNotificator";
    String mAction;
    Context mContext;
    String mNotificationExtraKey;
    
    public UEBroadcastNotificator(final UEDeviceCommand.UECommand ueCommand, final Class<T> clazz, final String mAction, final String mNotificationExtraKey, final Context mContext) {
        super(ueCommand, clazz);
        this.mAction = mAction;
        this.mNotificationExtraKey = mNotificationExtraKey;
        this.mContext = mContext;
    }
    
    @Override
    public void onNotificationReceived(final T t) {
        Log.d("UEBroadcastNotificator", "On notification received. Action:" + this.mAction);
        final Intent intent = new Intent(this.mAction);
        intent.putExtra(this.mNotificationExtraKey, (Parcelable)t);
        LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent);
    }
}
