// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UEBroadcastState
{
    DISABLE(0), 
    ENABLE_BROADCASTING_ONLY(2), 
    ENABLE_SCANNING_AND_BROADCASTING(3), 
    ENABLE_SCANNING_ONLY(1), 
    UNKNOWN(-1);
    
    private static final SparseArray<UEBroadcastState> map;
    private final int mCode;
    
    static {
        (map = new SparseArray()).put(UEBroadcastState.DISABLE.getCode(), (Object)UEBroadcastState.DISABLE);
        UEBroadcastState.map.put(UEBroadcastState.ENABLE_SCANNING_ONLY.getCode(), (Object)UEBroadcastState.ENABLE_SCANNING_ONLY);
        UEBroadcastState.map.put(UEBroadcastState.ENABLE_BROADCASTING_ONLY.getCode(), (Object)UEBroadcastState.ENABLE_BROADCASTING_ONLY);
        UEBroadcastState.map.put(UEBroadcastState.ENABLE_SCANNING_AND_BROADCASTING.getCode(), (Object)UEBroadcastState.ENABLE_SCANNING_AND_BROADCASTING);
    }
    
    private UEBroadcastState(final int mCode) {
        this.mCode = mCode;
    }
    
    public static UEBroadcastState getState(final int n) {
        return (UEBroadcastState)UEBroadcastState.map.get(n, (Object)UEBroadcastState.UNKNOWN);
    }
    
    public int getCode() {
        return this.mCode;
    }
}
