// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.xup;

import com.logitech.ue.centurion.device.devicedata.UEBroadcastReceiverStatus;

public enum XUPReceiverConnectionState
{
    CONNECTED, 
    CONNECTING, 
    DISCONNECTED, 
    DISCONNECTING;
    
    public static XUPReceiverConnectionState getConnectionState(final UEBroadcastReceiverStatus ueBroadcastReceiverStatus) {
        XUPReceiverConnectionState xupReceiverConnectionState;
        if (ueBroadcastReceiverStatus == UEBroadcastReceiverStatus.PLAYING_THIS_BROADCAST) {
            xupReceiverConnectionState = XUPReceiverConnectionState.CONNECTED;
        }
        else {
            xupReceiverConnectionState = XUPReceiverConnectionState.DISCONNECTED;
        }
        return xupReceiverConnectionState;
    }
}
