// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UESoundProfile
{
    ALARM_OFF(24767), 
    ALARM_ON(24689), 
    BATTERY_LOW(24811), 
    CONNECTED(24774), 
    POWER_ON(24768);
    
    private static final SparseArray<UESoundProfile> profileMap;
    final int mCode;
    
    static {
        (profileMap = new SparseArray()).put(0, (Object)UESoundProfile.POWER_ON);
        UESoundProfile.profileMap.put(1, (Object)UESoundProfile.CONNECTED);
        UESoundProfile.profileMap.put(2, (Object)UESoundProfile.ALARM_ON);
        UESoundProfile.profileMap.put(3, (Object)UESoundProfile.ALARM_OFF);
        UESoundProfile.profileMap.put(4, (Object)UESoundProfile.BATTERY_LOW);
    }
    
    private UESoundProfile(final int mCode) {
        this.mCode = mCode;
    }
    
    public static UESoundProfile getProfile(final int n) {
        return (UESoundProfile)UESoundProfile.profileMap.get(n);
    }
    
    public static UESoundProfile getProfile(final String anotherString) {
        for (final UESoundProfile ueSoundProfile : values()) {
            if (ueSoundProfile.toString().equalsIgnoreCase(anotherString)) {
                return ueSoundProfile;
            }
        }
        return null;
    }
    
    public int getCode() {
        return this.mCode;
    }
}
