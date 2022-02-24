// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.notification.notificator;

import java.lang.reflect.Constructor;
import com.logitech.ue.centurion.device.command.UEDeviceCommand;
import com.logitech.ue.centurion.notification.UENotification;

public abstract class UEGenericNotificator<T extends UENotification> implements IUENotificator<T>
{
    protected Class<T> mNotificationClass;
    protected UEDeviceCommand.UECommand mNotificationCommand;
    protected Constructor<T> mNotificationConstructor;
    
    public UEGenericNotificator(final UEDeviceCommand.UECommand mNotificationCommand, final Class<T> mNotificationClass) {
        this.mNotificationCommand = mNotificationCommand;
        this.mNotificationClass = mNotificationClass;
        try {
            this.mNotificationConstructor = this.mNotificationClass.getConstructor(byte[].class);
        }
        catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public boolean processNotification(final byte[] array) {
        boolean b = false;
        if (array[1] != this.mNotificationCommand.getMostSignificantByte()) {
            return b;
        }
        b = b;
        if (array[2] != this.mNotificationCommand.getLeastSignificantByte()) {
            return b;
        }
        try {
            this.onNotificationReceived(this.mNotificationConstructor.newInstance(array));
            b = true;
            return b;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
    }
}
