// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.xup;

import com.logitech.ue.centurion.device.devicedata.UEBroadcastReceiverStatus;

public enum XUPReceiverStatus
{
    NOT_SUPPORTED, 
    POWER_OFF, 
    READY, 
    SECURED, 
    UNKNOWN;
    
    public static XUPReceiverStatus getStatus(final UEBroadcastReceiverStatus ueBroadcastReceiverStatus) {
        XUPReceiverStatus xupReceiverStatus;
        if (ueBroadcastReceiverStatus == UEBroadcastReceiverStatus.POWER_OFF) {
            xupReceiverStatus = XUPReceiverStatus.POWER_OFF;
        }
        else if (ueBroadcastReceiverStatus == UEBroadcastReceiverStatus.DOESNT_SUPPORT_XUP) {
            xupReceiverStatus = XUPReceiverStatus.NOT_SUPPORTED;
        }
        else if (ueBroadcastReceiverStatus == UEBroadcastReceiverStatus.UNKNOWN) {
            xupReceiverStatus = XUPReceiverStatus.UNKNOWN;
        }
        else {
            xupReceiverStatus = XUPReceiverStatus.READY;
        }
        return xupReceiverStatus;
    }
}
