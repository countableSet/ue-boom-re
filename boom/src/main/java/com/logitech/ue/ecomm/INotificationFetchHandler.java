// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.ecomm;

import com.logitech.ue.ecomm.model.Notification;

public interface INotificationFetchHandler
{
    void onNoNotificationReceived(final Exception p0);
    
    void onReceiveNotification(final Notification p0);
}
