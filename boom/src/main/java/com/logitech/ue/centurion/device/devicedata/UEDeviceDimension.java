// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

public class UEDeviceDimension
{
    int height;
    UEDeviceType type;
    int width;
    
    public UEDeviceDimension(final UEDeviceType type, final int width, final int height) {
        this.type = UEDeviceType.Unknown;
        this.width = 0;
        this.height = 0;
        this.type = type;
        this.width = width;
        this.height = height;
    }
    
    public UEDeviceType getDeviceType() {
        return this.type;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
}
