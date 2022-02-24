// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

public class UEFeaturesInfo
{
    public final int masterMask;
    public final int slaveMask;
    public final boolean slavePresent;
    
    public UEFeaturesInfo(final int masterMask, final int slaveMask, final boolean slavePresent) {
        this.masterMask = masterMask;
        this.slaveMask = slaveMask;
        this.slavePresent = slavePresent;
    }
    
    public boolean doesMasterAndSlaveSupportFeature(final UEFeature ueFeature) {
        return this.doesMasterSupportFeature(ueFeature) && this.doesSlaveSupportFeature(ueFeature);
    }
    
    public boolean doesMasterSupportFeature(final UEFeature ueFeature) {
        return (this.masterMask & ueFeature.getCode()) != 0x0;
    }
    
    public boolean doesSlavePresent() {
        return this.slavePresent;
    }
    
    public boolean doesSlaveSupportFeature(final UEFeature ueFeature) {
        return this.slavePresent && (this.slaveMask & ueFeature.getCode()) != 0x0;
    }
}
