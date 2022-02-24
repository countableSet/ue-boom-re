// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

public enum UEFeature
{
    Balance(4), 
    Band5EQ(16), 
    ExtendedFeatures(Integer.MIN_VALUE), 
    Sticky(1), 
    Volume32Value(8), 
    VolumeSync(2);
    
    private final int value;
    
    private UEFeature(final int value) {
        this.value = value;
    }
    
    public int getCode() {
        return this.value;
    }
}
