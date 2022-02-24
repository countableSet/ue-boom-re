// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.ecomm;

import android.content.Context;
import com.logitech.ue.ecomm.model.Notification;

public class NotificationPopupBuilder
{
    NotificationPopup popup;
    
    public NotificationPopupBuilder(final Notification notification, final Context context) {
        this.popup = new NotificationPopup(context, notification);
    }
    
    public NotificationPopup build() {
        return this.popup;
    }
    
    public NotificationPopupBuilder setNotificationPopupListener(final NotificationPopup.NotificationPopupListener notificationPopupListener) {
        this.popup.setNotificationPopupListener(notificationPopupListener);
        return this;
    }
}
