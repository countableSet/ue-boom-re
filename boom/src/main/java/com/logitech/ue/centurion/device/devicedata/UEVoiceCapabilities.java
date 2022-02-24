// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UEVoiceCapabilities
{
    AVS(1), 
    GV_SIRI(2), 
    GV_SIRI_AND_AVS(3), 
    OFF(0), 
    UNKNOWN(-1);
    
    private static final SparseArray<UEVoiceCapabilities> map;
    final int mCode;
    
    static {
        (map = new SparseArray()).put(UEVoiceCapabilities.OFF.getCode(), (Object)UEVoiceCapabilities.OFF);
        UEVoiceCapabilities.map.put(UEVoiceCapabilities.GV_SIRI.getCode(), (Object)UEVoiceCapabilities.GV_SIRI);
        UEVoiceCapabilities.map.put(UEVoiceCapabilities.AVS.getCode(), (Object)UEVoiceCapabilities.AVS);
        UEVoiceCapabilities.map.put(UEVoiceCapabilities.GV_SIRI_AND_AVS.getCode(), (Object)UEVoiceCapabilities.GV_SIRI_AND_AVS);
    }
    
    private UEVoiceCapabilities(final int mCode) {
        this.mCode = mCode;
    }
    
    public static UEVoiceCapabilities getVoiceCapabilities(final int n) {
        return (UEVoiceCapabilities)UEVoiceCapabilities.map.get(n, (Object)UEVoiceCapabilities.UNKNOWN);
    }
    
    public int getCode() {
        return this.mCode;
    }
}
