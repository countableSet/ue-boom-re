// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.notification.notificator;

import android.annotation.TargetApi;
import android.util.Log;
import android.os.Build$VERSION;
import android.content.Intent;
import com.logitech.ue.centurion.notification.UENotification;
import android.content.Context;
import com.logitech.ue.centurion.device.command.UEDeviceCommand;
import com.logitech.ue.centurion.notification.UEVoiceNotification;

public class UEVoiceNotificator implements IUENotificator<UEVoiceNotification>
{
    private static final String TAG;
    final int COMMAND_INTERVAL;
    private UEDeviceCommand.UECommand command;
    private Context mContext;
    private long mLastReceivedTime;
    
    static {
        TAG = UEVoiceNotificator.class.getSimpleName();
    }
    
    public UEVoiceNotificator(final UEDeviceCommand.UECommand command, final Context mContext) {
        this.COMMAND_INTERVAL = 1000;
        this.mLastReceivedTime = -1L;
        this.command = command;
        this.mContext = mContext;
    }
    
    @Override
    public void onNotificationReceived(final UEVoiceNotification ueVoiceNotification) {
        final Intent intent = new Intent("com.logitech.ue.centurion.ACTION_VOICE_REQUEST_NOTIFICATION");
        intent.putExtra("status", ueVoiceNotification.getState());
        intent.putExtra("notification", ueVoiceNotification.getCueState());
        this.mContext.sendOrderedBroadcast(intent, (String)null);
    }
    
    @TargetApi(19)
    @Override
    public boolean processNotification(final byte[] array) {
        boolean b2;
        final boolean b = b2 = false;
        if (array[1] == this.command.getMostSignificantByte()) {
            b2 = b;
            if (array[2] == this.command.getLeastSignificantByte()) {
                if (Build$VERSION.SDK_INT < 21) {
                    Log.w(UEVoiceNotificator.TAG, "Voice not supported on Pre-Lollipop.");
                    b2 = b;
                }
                else if (this.mLastReceivedTime > 0L && System.currentTimeMillis() - this.mLastReceivedTime < 1000L) {
                    Log.w(UEVoiceNotificator.TAG, "Voice command frequency is too high. Ignore!");
                    b2 = b;
                }
                else {
                    this.mLastReceivedTime = System.currentTimeMillis();
                    this.onNotificationReceived(new UEVoiceNotification(array));
                    b2 = true;
                }
            }
        }
        return b2;
    }
}
