// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UEVoiceState
{
    AVS(0), 
    GV_SIRI(1), 
    OFF(3), 
    UNKNOWN(-1);
    
    private static final SparseArray<UEVoiceState> map;
    final int mCode;
    
    static {
        (map = new SparseArray()).put(UEVoiceState.OFF.getCode(), (Object)UEVoiceState.OFF);
        UEVoiceState.map.put(UEVoiceState.GV_SIRI.getCode(), (Object)UEVoiceState.GV_SIRI);
        UEVoiceState.map.put(UEVoiceState.AVS.getCode(), (Object)UEVoiceState.AVS);
    }
    
    private UEVoiceState(final int mCode) {
        this.mCode = mCode;
    }
    
    public static UEVoiceState getVoiceSate(final int n) {
        return (UEVoiceState)UEVoiceState.map.get(n, (Object)UEVoiceState.UNKNOWN);
    }
    
    public int getCode() {
        return this.mCode;
    }
}
