// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UEOTAStatus
{
    END(2), 
    ERROR(3), 
    NONE(0), 
    START(1), 
    UNKNOWN(-1);
    
    private static final SparseArray<UEOTAStatus> statusMap;
    private final int mCode;
    
    static {
        (statusMap = new SparseArray()).put(UEOTAStatus.NONE.getCode(), (Object)UEOTAStatus.NONE);
        UEOTAStatus.statusMap.put(UEOTAStatus.START.getCode(), (Object)UEOTAStatus.START);
        UEOTAStatus.statusMap.put(UEOTAStatus.END.getCode(), (Object)UEOTAStatus.END);
        UEOTAStatus.statusMap.put(UEOTAStatus.ERROR.getCode(), (Object)UEOTAStatus.ERROR);
    }
    
    private UEOTAStatus(final int mCode) {
        this.mCode = mCode;
    }
    
    public static UEOTAStatus getStatus(final int n) {
        return (UEOTAStatus)UEOTAStatus.statusMap.get(n, (Object)UEOTAStatus.UNKNOWN);
    }
    
    public int getCode() {
        return this.mCode;
    }
}
