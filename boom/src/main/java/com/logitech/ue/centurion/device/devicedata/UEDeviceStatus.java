// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UEDeviceStatus
{
    CONNECTED_OFF(102), 
    CONNECTING(1), 
    DISCONNECTED(0), 
    DISCONNECTING(3), 
    DOUBLEUP_CONNECTED(101), 
    PLAYING(10), 
    SINGLE_CONNECTED(2), 
    STANDBY(100), 
    UNKNOWN(-1);
    
    static final SparseArray<UEDeviceStatus> statusMap;
    final int mStatusValue;
    
    static {
        (statusMap = new SparseArray()).put(0, (Object)UEDeviceStatus.DISCONNECTED);
        UEDeviceStatus.statusMap.put(1, (Object)UEDeviceStatus.CONNECTING);
        UEDeviceStatus.statusMap.put(2, (Object)UEDeviceStatus.SINGLE_CONNECTED);
        UEDeviceStatus.statusMap.put(3, (Object)UEDeviceStatus.DISCONNECTING);
        UEDeviceStatus.statusMap.put(10, (Object)UEDeviceStatus.PLAYING);
        UEDeviceStatus.statusMap.put(100, (Object)UEDeviceStatus.STANDBY);
        UEDeviceStatus.statusMap.put(101, (Object)UEDeviceStatus.DOUBLEUP_CONNECTED);
        UEDeviceStatus.statusMap.put(102, (Object)UEDeviceStatus.CONNECTED_OFF);
        UEDeviceStatus.statusMap.put(-1, (Object)UEDeviceStatus.UNKNOWN);
    }
    
    private UEDeviceStatus(final int mStatusValue) {
        this.mStatusValue = mStatusValue;
    }
    
    public static UEDeviceStatus getStatus(final int n) {
        return (UEDeviceStatus)UEDeviceStatus.statusMap.get(n);
    }
    
    public static int getValue(final UEDeviceStatus ueDeviceStatus) {
        return ueDeviceStatus.mStatusValue;
    }
    
    public int getValue() {
        return this.mStatusValue;
    }
    
    public boolean isBtClassicConnectedState() {
        return this == UEDeviceStatus.SINGLE_CONNECTED || this == UEDeviceStatus.PLAYING || this == UEDeviceStatus.DOUBLEUP_CONNECTED;
    }
}
