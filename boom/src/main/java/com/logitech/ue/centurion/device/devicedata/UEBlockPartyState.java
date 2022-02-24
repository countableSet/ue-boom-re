// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UEBlockPartyState
{
    ENTERING(2), 
    EXITING(3), 
    OFF(0), 
    ON(1), 
    UNKNOWN(-1);
    
    private static final SparseArray<UEBlockPartyState> map;
    final int mCode;
    
    static {
        (map = new SparseArray()).put(UEBlockPartyState.OFF.getCode(), (Object)UEBlockPartyState.OFF);
        UEBlockPartyState.map.put(UEBlockPartyState.ON.getCode(), (Object)UEBlockPartyState.ON);
        UEBlockPartyState.map.put(UEBlockPartyState.ENTERING.getCode(), (Object)UEBlockPartyState.ENTERING);
        UEBlockPartyState.map.put(UEBlockPartyState.EXITING.getCode(), (Object)UEBlockPartyState.EXITING);
    }
    
    private UEBlockPartyState(final int mCode) {
        this.mCode = mCode;
    }
    
    public static UEBlockPartyState getState(final int n) {
        return (UEBlockPartyState)UEBlockPartyState.map.get(n, (Object)UEBlockPartyState.UNKNOWN);
    }
    
    public int getCode() {
        return this.mCode;
    }
    
    public boolean isOnOrEntering() {
        return this == UEBlockPartyState.ON || this == UEBlockPartyState.ENTERING;
    }
}
