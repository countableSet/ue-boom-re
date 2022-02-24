// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UEBroadcastAudioMode
{
    CENTER(3), 
    LEFT(1), 
    RIGHT(2), 
    UNKNOWN(0);
    
    private static final SparseArray<UEBroadcastAudioMode> map;
    private final int mCode;
    
    static {
        (map = new SparseArray()).put(UEBroadcastAudioMode.LEFT.getCode(), (Object)UEBroadcastAudioMode.LEFT);
        UEBroadcastAudioMode.map.put(UEBroadcastAudioMode.RIGHT.getCode(), (Object)UEBroadcastAudioMode.RIGHT);
        UEBroadcastAudioMode.map.put(UEBroadcastAudioMode.CENTER.getCode(), (Object)UEBroadcastAudioMode.CENTER);
    }
    
    private UEBroadcastAudioMode(final int mCode) {
        this.mCode = mCode;
    }
    
    public static UEBroadcastAudioMode getAudioMode(final int n) {
        return (UEBroadcastAudioMode)UEBroadcastAudioMode.map.get(n, (Object)UEBroadcastAudioMode.UNKNOWN);
    }
    
    public int getCode() {
        return this.mCode;
    }
}
