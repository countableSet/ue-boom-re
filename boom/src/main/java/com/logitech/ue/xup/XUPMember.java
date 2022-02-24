// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.xup;

import android.os.SystemClock;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastReceiverInfo;
import com.logitech.ue.centurion.utils.MAC;

public class XUPMember
{
    public MAC Address;
    public int batteryLevel;
    public XUPReceiverConnectionState connectionState;
    public int deviceColor;
    public boolean isFetchingName;
    public boolean isFoundByBLE;
    public boolean isNameFresh;
    public boolean isVolumeSynced;
    public long lastTimeSeen;
    public String name;
    public int nameRevision;
    public XUPReceiverStatus status;
    private long statusIgnoreTime;
    
    public XUPMember(final UEBroadcastReceiverInfo ueBroadcastReceiverInfo) {
        this(null, ueBroadcastReceiverInfo.mDeviceColor, ueBroadcastReceiverInfo.mAddress, !ueBroadcastReceiverInfo.mNonzeroVolumeOffset, ueBroadcastReceiverInfo.mBatteryLevel, XUPReceiverStatus.getStatus(ueBroadcastReceiverInfo.mStatus), XUPReceiverConnectionState.getConnectionState(ueBroadcastReceiverInfo.mStatus), ueBroadcastReceiverInfo.mNameRevision);
    }
    
    public XUPMember(final String name, final int deviceColor, final MAC address, final boolean isVolumeSynced, final int batteryLevel, final XUPReceiverStatus status, final XUPReceiverConnectionState connectionState, final int nameRevision) {
        this.status = XUPReceiverStatus.UNKNOWN;
        this.connectionState = XUPReceiverConnectionState.DISCONNECTED;
        this.name = name;
        this.deviceColor = deviceColor;
        this.Address = address;
        this.isVolumeSynced = isVolumeSynced;
        this.batteryLevel = batteryLevel;
        this.status = status;
        this.connectionState = connectionState;
        this.nameRevision = nameRevision;
    }
    
    public long getStatusIgnoreTime() {
        return this.statusIgnoreTime;
    }
    
    public boolean isUpdateIgnored() {
        return SystemClock.elapsedRealtime() - this.statusIgnoreTime < 20000L;
    }
    
    public void setStatusIgnoreTime(final long statusIgnoreTime) {
        this.statusIgnoreTime = statusIgnoreTime;
    }
    
    public boolean shouldUpdateName() {
        return !this.isFetchingName && (this.name == null || !this.isNameFresh);
    }
    
    public void update(final UEBroadcastReceiverInfo ueBroadcastReceiverInfo) {
        final boolean b = true;
        this.deviceColor = ueBroadcastReceiverInfo.mDeviceColor;
        this.Address = ueBroadcastReceiverInfo.mAddress;
        this.isVolumeSynced = !ueBroadcastReceiverInfo.mNonzeroVolumeOffset;
        this.batteryLevel = ueBroadcastReceiverInfo.mBatteryLevel;
        if (this.status == XUPReceiverStatus.SECURED || this.status == XUPReceiverStatus.POWER_OFF) {
            this.status = XUPReceiverStatus.getStatus(ueBroadcastReceiverInfo.mStatus);
        }
        this.connectionState = XUPReceiverConnectionState.getConnectionState(ueBroadcastReceiverInfo.mStatus);
        this.isNameFresh = (this.isNameFresh && this.nameRevision == ueBroadcastReceiverInfo.mNameRevision && b);
        this.nameRevision = ueBroadcastReceiverInfo.mNameRevision;
    }
}
