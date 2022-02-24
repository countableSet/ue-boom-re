// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UEDeviceStreamingStatus
{
    A2DP_CONNECTED(16), 
    A2DP_DISCONNECTED(17), 
    A2DP_STREAMING(32), 
    A2DP_STREAMING_STOPPED(33), 
    PAIRING_IN_DISCOVERY_MODE(2), 
    PAIRING_IN_INQUIRY_MODE(1), 
    TWS_CONNECTED(51), 
    TWS_DISCONNECTED(52), 
    TWS_LINK_LOSS(53), 
    TWS_MASTER_MODE(65), 
    TWS_PAIRED(50), 
    TWS_PAIRING_FAILED(54), 
    TWS_REQUEST_IGNORED(67), 
    TWS_SLAVE_DISCONNECTED(66), 
    TWS_SLAVE_MODE(64), 
    TWS_STOPPED(55), 
    TWS_STREAMING(56), 
    TWS_STREAMING_STOPPED(57), 
    UNDEFINED(0), 
    UNKNOWN(-1);
    
    static final SparseArray<UEDeviceStreamingStatus> statusMap;
    private final int mCode;
    
    static {
        (statusMap = new SparseArray()).put(UEDeviceStreamingStatus.UNDEFINED.getCode(), (Object)UEDeviceStreamingStatus.UNDEFINED);
        UEDeviceStreamingStatus.statusMap.put(UEDeviceStreamingStatus.PAIRING_IN_INQUIRY_MODE.getCode(), (Object)UEDeviceStreamingStatus.PAIRING_IN_INQUIRY_MODE);
        UEDeviceStreamingStatus.statusMap.put(UEDeviceStreamingStatus.PAIRING_IN_DISCOVERY_MODE.getCode(), (Object)UEDeviceStreamingStatus.PAIRING_IN_DISCOVERY_MODE);
        UEDeviceStreamingStatus.statusMap.put(UEDeviceStreamingStatus.A2DP_CONNECTED.getCode(), (Object)UEDeviceStreamingStatus.A2DP_CONNECTED);
        UEDeviceStreamingStatus.statusMap.put(UEDeviceStreamingStatus.A2DP_DISCONNECTED.getCode(), (Object)UEDeviceStreamingStatus.A2DP_DISCONNECTED);
        UEDeviceStreamingStatus.statusMap.put(UEDeviceStreamingStatus.A2DP_STREAMING.getCode(), (Object)UEDeviceStreamingStatus.A2DP_STREAMING);
        UEDeviceStreamingStatus.statusMap.put(UEDeviceStreamingStatus.A2DP_STREAMING_STOPPED.getCode(), (Object)UEDeviceStreamingStatus.A2DP_STREAMING_STOPPED);
        UEDeviceStreamingStatus.statusMap.put(UEDeviceStreamingStatus.TWS_PAIRED.getCode(), (Object)UEDeviceStreamingStatus.TWS_PAIRED);
        UEDeviceStreamingStatus.statusMap.put(UEDeviceStreamingStatus.TWS_CONNECTED.getCode(), (Object)UEDeviceStreamingStatus.TWS_CONNECTED);
        UEDeviceStreamingStatus.statusMap.put(UEDeviceStreamingStatus.TWS_DISCONNECTED.getCode(), (Object)UEDeviceStreamingStatus.TWS_DISCONNECTED);
        UEDeviceStreamingStatus.statusMap.put(UEDeviceStreamingStatus.TWS_LINK_LOSS.getCode(), (Object)UEDeviceStreamingStatus.TWS_LINK_LOSS);
        UEDeviceStreamingStatus.statusMap.put(UEDeviceStreamingStatus.TWS_PAIRING_FAILED.getCode(), (Object)UEDeviceStreamingStatus.TWS_PAIRING_FAILED);
        UEDeviceStreamingStatus.statusMap.put(UEDeviceStreamingStatus.TWS_STOPPED.getCode(), (Object)UEDeviceStreamingStatus.TWS_STOPPED);
        UEDeviceStreamingStatus.statusMap.put(UEDeviceStreamingStatus.TWS_STREAMING.getCode(), (Object)UEDeviceStreamingStatus.TWS_STREAMING);
        UEDeviceStreamingStatus.statusMap.put(UEDeviceStreamingStatus.TWS_STREAMING_STOPPED.getCode(), (Object)UEDeviceStreamingStatus.TWS_STREAMING_STOPPED);
        UEDeviceStreamingStatus.statusMap.put(UEDeviceStreamingStatus.TWS_SLAVE_MODE.getCode(), (Object)UEDeviceStreamingStatus.TWS_SLAVE_MODE);
        UEDeviceStreamingStatus.statusMap.put(UEDeviceStreamingStatus.TWS_MASTER_MODE.getCode(), (Object)UEDeviceStreamingStatus.TWS_MASTER_MODE);
        UEDeviceStreamingStatus.statusMap.put(UEDeviceStreamingStatus.TWS_SLAVE_DISCONNECTED.getCode(), (Object)UEDeviceStreamingStatus.TWS_SLAVE_DISCONNECTED);
        UEDeviceStreamingStatus.statusMap.put(UEDeviceStreamingStatus.TWS_REQUEST_IGNORED.getCode(), (Object)UEDeviceStreamingStatus.TWS_REQUEST_IGNORED);
    }
    
    private UEDeviceStreamingStatus(final int mCode) {
        this.mCode = mCode;
    }
    
    public static UEDeviceStreamingStatus getStatus(final int n) {
        return (UEDeviceStreamingStatus)UEDeviceStreamingStatus.statusMap.get(n, (Object)UEDeviceStreamingStatus.UNKNOWN);
    }
    
    public int getCode() {
        return this.mCode;
    }
    
    public boolean isDevicePairedStatus() {
        return this == UEDeviceStreamingStatus.TWS_CONNECTED || this == UEDeviceStreamingStatus.TWS_STREAMING || this == UEDeviceStreamingStatus.TWS_STREAMING_STOPPED || this == UEDeviceStreamingStatus.TWS_PAIRED;
    }
}
