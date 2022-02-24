// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UEBroadcastReceiverStatus
{
    CONNECTED_NO_STREAMING(2), 
    DOESNT_SUPPORT_XUP(9), 
    LOCAL_HFP(6), 
    NO_STREAMING_NOT_CONNECTED(1), 
    PLAYING_ANOTHER_BROADCAST(7), 
    PLAYING_THIS_BROADCAST(8), 
    POWER_OFF(0), 
    STREAMING_A2DP(3), 
    STREAMING_AUX(4), 
    STREAMING_AUX_NOT_CONNECTED(5), 
    UNKNOWN(-1);
    
    private static final SparseArray<UEBroadcastReceiverStatus> map;
    private final int mCode;
    
    static {
        int i = 0;
        map = new SparseArray();
        for (UEBroadcastReceiverStatus[] values = values(); i < values.length; ++i) {
            final UEBroadcastReceiverStatus ueBroadcastReceiverStatus = values[i];
            if (ueBroadcastReceiverStatus != UEBroadcastReceiverStatus.UNKNOWN) {
                UEBroadcastReceiverStatus.map.put(ueBroadcastReceiverStatus.getCode(), (Object)ueBroadcastReceiverStatus);
            }
        }
    }
    
    private UEBroadcastReceiverStatus(final int mCode) {
        this.mCode = mCode;
    }
    
    public static UEBroadcastReceiverStatus getStatus(final int n) {
        return (UEBroadcastReceiverStatus)UEBroadcastReceiverStatus.map.get(n, (Object)UEBroadcastReceiverStatus.UNKNOWN);
    }
    
    public int getCode() {
        return this.mCode;
    }
}
