// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UEAudioRouting
{
    Double(119), 
    MasterLeft(101), 
    MasterRight(86), 
    UNKNOWN(-1);
    
    private static final SparseArray<UEAudioRouting> routingMap;
    final int modeCode;
    
    static {
        (routingMap = new SparseArray(3)).put(101, (Object)UEAudioRouting.MasterLeft);
        UEAudioRouting.routingMap.put(86, (Object)UEAudioRouting.MasterRight);
        UEAudioRouting.routingMap.put(119, (Object)UEAudioRouting.Double);
    }
    
    private UEAudioRouting(final int modeCode) {
        this.modeCode = modeCode;
    }
    
    public static UEAudioRouting getRouting(final int n) {
        return (UEAudioRouting)UEAudioRouting.routingMap.get(n, (Object)UEAudioRouting.UNKNOWN);
    }
    
    public int getCode() {
        return this.modeCode;
    }
}
