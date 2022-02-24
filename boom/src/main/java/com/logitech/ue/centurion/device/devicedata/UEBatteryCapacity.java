// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

public class UEBatteryCapacity
{
    private float currentCapacity;
    private float maxCapacity;
    
    public UEBatteryCapacity() {
    }
    
    public UEBatteryCapacity(final float currentCapacity, final float maxCapacity) {
        this.setCurrentCapacity(currentCapacity);
        this.setMaxCapacity(maxCapacity);
    }
    
    public float getCurrentCapacity() {
        return this.currentCapacity;
    }
    
    public float getMaxCapacity() {
        return this.maxCapacity;
    }
    
    public void setCurrentCapacity(final float currentCapacity) {
        this.currentCapacity = currentCapacity;
    }
    
    public void setMaxCapacity(final float maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("[Current capacity: %.2fmAh]\n", this.currentCapacity * 0.1));
        sb.append(String.format("[Max capacity: %.2fmAh]", this.maxCapacity * 0.1));
        return sb.toString();
    }
}
