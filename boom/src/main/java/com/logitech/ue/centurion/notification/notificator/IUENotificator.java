// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.notification.notificator;

import com.logitech.ue.centurion.notification.UENotification;

public interface IUENotificator<T extends UENotification>
{
    void onNotificationReceived(final T p0);
    
    boolean processNotification(final byte[] p0);
}
