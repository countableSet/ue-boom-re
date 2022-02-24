// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.devicedata;

public class DeviceFirmwareSerialInfo
{
    private String mFirmwareVersion;
    private String mSerialNumber;
    
    public DeviceFirmwareSerialInfo() {
        this.mFirmwareVersion = null;
        this.mSerialNumber = null;
    }
    
    public DeviceFirmwareSerialInfo(final String mFirmwareVersion, final String mSerialNumber) {
        this.mFirmwareVersion = null;
        this.mSerialNumber = null;
        this.mFirmwareVersion = mFirmwareVersion;
        this.mSerialNumber = mSerialNumber;
    }
    
    public String getFirmwareVersion() {
        return this.mFirmwareVersion;
    }
    
    public String getSerialNumber() {
        return this.mSerialNumber;
    }
    
    public void setFirmwareVersion(final String mFirmwareVersion) {
        this.mFirmwareVersion = mFirmwareVersion;
    }
    
    public void setSerialNumber(final String mSerialNumber) {
        this.mSerialNumber = mSerialNumber;
    }
}
