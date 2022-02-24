// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UEAVRCP
{
    FAST_FORWARD_PRESS(6), 
    FAST_FORWARD_RELEASE(7), 
    NEXT_GROUP(10), 
    PAUSE(2), 
    PLAY(1), 
    PLAY_PAUSE(3), 
    PREVIOUS_GROUP(11), 
    REWIND_PRESS(8), 
    REWIND_RELEASE(9), 
    SKIP_BACKWARD(5), 
    SKIP_FORWARD(4), 
    STOP(0), 
    UNKNOWN(-1);
    
    private static final SparseArray<UEAVRCP> avrcpMap;
    private final int mCode;
    
    static {
        (avrcpMap = new SparseArray()).put(UEAVRCP.STOP.getCode(), (Object)UEAVRCP.STOP);
        UEAVRCP.avrcpMap.put(UEAVRCP.PLAY.getCode(), (Object)UEAVRCP.PLAY);
        UEAVRCP.avrcpMap.put(UEAVRCP.PAUSE.getCode(), (Object)UEAVRCP.PAUSE);
        UEAVRCP.avrcpMap.put(UEAVRCP.PLAY_PAUSE.getCode(), (Object)UEAVRCP.PLAY_PAUSE);
        UEAVRCP.avrcpMap.put(UEAVRCP.SKIP_FORWARD.getCode(), (Object)UEAVRCP.SKIP_FORWARD);
        UEAVRCP.avrcpMap.put(UEAVRCP.SKIP_BACKWARD.getCode(), (Object)UEAVRCP.SKIP_BACKWARD);
        UEAVRCP.avrcpMap.put(UEAVRCP.FAST_FORWARD_PRESS.getCode(), (Object)UEAVRCP.FAST_FORWARD_PRESS);
        UEAVRCP.avrcpMap.put(UEAVRCP.FAST_FORWARD_RELEASE.getCode(), (Object)UEAVRCP.FAST_FORWARD_RELEASE);
        UEAVRCP.avrcpMap.put(UEAVRCP.REWIND_PRESS.getCode(), (Object)UEAVRCP.REWIND_PRESS);
        UEAVRCP.avrcpMap.put(UEAVRCP.REWIND_RELEASE.getCode(), (Object)UEAVRCP.REWIND_RELEASE);
        UEAVRCP.avrcpMap.put(UEAVRCP.NEXT_GROUP.getCode(), (Object)UEAVRCP.NEXT_GROUP);
        UEAVRCP.avrcpMap.put(UEAVRCP.PREVIOUS_GROUP.getCode(), (Object)UEAVRCP.PREVIOUS_GROUP);
    }
    
    private UEAVRCP(final int mCode) {
        this.mCode = mCode;
    }
    
    public static UEAVRCP getAVRCP(final int n) {
        return (UEAVRCP)UEAVRCP.avrcpMap.get(n, (Object)UEAVRCP.UNKNOWN);
    }
    
    public int getCode() {
        return this.mCode;
    }
}
