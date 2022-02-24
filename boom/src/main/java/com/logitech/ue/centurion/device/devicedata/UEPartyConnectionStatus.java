// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UEPartyConnectionStatus
{
    CONNECTED(1), 
    LINK_LOSS(2), 
    NOT_CONNECTED(0), 
    UNKNOWN(-1);
    
    private static final SparseArray<UEPartyConnectionStatus> map;
    private final int mCode;
    
    static {
        (map = new SparseArray()).put(UEPartyConnectionStatus.NOT_CONNECTED.getCode(), (Object)UEPartyConnectionStatus.NOT_CONNECTED);
        UEPartyConnectionStatus.map.put(UEPartyConnectionStatus.CONNECTED.getCode(), (Object)UEPartyConnectionStatus.CONNECTED);
        UEPartyConnectionStatus.map.put(UEPartyConnectionStatus.LINK_LOSS.getCode(), (Object)UEPartyConnectionStatus.LINK_LOSS);
    }
    
    private UEPartyConnectionStatus(final int mCode) {
        this.mCode = mCode;
    }
    
    public static UEPartyConnectionStatus getPartyConnectionStatus(final int n) {
        return (UEPartyConnectionStatus)UEPartyConnectionStatus.map.get(n, (Object)UEPartyConnectionStatus.UNKNOWN);
    }
    
    public int getCode() {
        return this.mCode;
    }
}
