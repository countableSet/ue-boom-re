// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UEBroadcastAudioOptions
{
    MULTIPLE(0), 
    REVERSE_STEREO_LEFT(144), 
    REVERSE_STEREO_RIGHT(160), 
    STEREO_LEFT(80), 
    STEREO_RIGHT(96);
    
    private static final SparseArray<UEBroadcastAudioOptions> map;
    private final int mCode;
    
    static {
        (map = new SparseArray()).put(UEBroadcastAudioOptions.MULTIPLE.getCode(), (Object)UEBroadcastAudioOptions.MULTIPLE);
        UEBroadcastAudioOptions.map.put(UEBroadcastAudioOptions.STEREO_LEFT.getCode(), (Object)UEBroadcastAudioOptions.STEREO_LEFT);
        UEBroadcastAudioOptions.map.put(UEBroadcastAudioOptions.STEREO_RIGHT.getCode(), (Object)UEBroadcastAudioOptions.STEREO_RIGHT);
        UEBroadcastAudioOptions.map.put(UEBroadcastAudioOptions.REVERSE_STEREO_LEFT.getCode(), (Object)UEBroadcastAudioOptions.REVERSE_STEREO_LEFT);
        UEBroadcastAudioOptions.map.put(UEBroadcastAudioOptions.REVERSE_STEREO_RIGHT.getCode(), (Object)UEBroadcastAudioOptions.REVERSE_STEREO_RIGHT);
    }
    
    private UEBroadcastAudioOptions(final int mCode) {
        this.mCode = mCode;
    }
    
    public static UEBroadcastAudioOptions getAudioOptions(final int n) {
        return (UEBroadcastAudioOptions)UEBroadcastAudioOptions.map.get(n, (Object)UEBroadcastAudioOptions.MULTIPLE);
    }
    
    public int getCode() {
        return this.mCode;
    }
}
