// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device;

import android.support.annotation.NonNull;
import com.logitech.ue.centurion.connection.UEConnectionType;
import com.logitech.ue.centurion.connection.IUEConnector;
import com.logitech.ue.centurion.utils.MAC;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;

public class UEDevice
{
    private static final String TAG;
    private static volatile UEDeviceStatus mDeviceConnectionStatus;
    protected final MAC mAddress;
    private final IUEConnector mConnector;
    protected boolean mEnableCache;
    
    static {
        TAG = UEDevice.class.getSimpleName();
        UEDevice.mDeviceConnectionStatus = UEDeviceStatus.UNKNOWN;
    }
    
    public UEDevice(final MAC mAddress, final IUEConnector mConnector) {
        this.mEnableCache = true;
        this.mAddress = mAddress;
        this.mConnector = mConnector;
    }
    
    public void dropFirstLevelCache() {
        this.dropSecondLevelCache();
    }
    
    public void dropSecondLevelCache() {
        this.dropThirdLevelCache();
    }
    
    public void dropThirdLevelCache() {
    }
    
    public MAC getAddress() {
        return this.mAddress;
    }
    
    public UEConnectionType getConnectionType() {
        return this.getConnector().getConnectionType();
    }
    
    @NonNull
    public IUEConnector getConnector() {
        return this.mConnector;
    }
    
    public UEDeviceStatus getDeviceConnectionStatus() {
        return UEDevice.mDeviceConnectionStatus;
    }
    
    public boolean isEnableCache() {
        return this.mEnableCache;
    }
    
    public void setDeviceConnectionStatus(final UEDeviceStatus mDeviceConnectionStatus) {
        UEDevice.mDeviceConnectionStatus = mDeviceConnectionStatus;
    }
    
    public void setEnableCache(final boolean mEnableCache) {
        if (!(this.mEnableCache = mEnableCache)) {
            this.dropFirstLevelCache();
        }
    }
}
