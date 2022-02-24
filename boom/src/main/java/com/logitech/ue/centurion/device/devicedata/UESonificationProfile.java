// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;
import java.util.HashMap;

public enum UESonificationProfile
{
    CONGA(1), 
    ENSEMBLE(3), 
    GUITAR(2), 
    NONE(0), 
    TABLA(5), 
    UNKNOWN(-1);
    
    private static final HashMap<UEDeviceType, UESonificationProfile> deviceToProfileMap;
    private static final SparseArray<UESonificationProfile> profileMap;
    final int mCode;
    
    static {
        profileMap = new SparseArray(5);
        deviceToProfileMap = new HashMap<UEDeviceType, UESonificationProfile>(5);
        UESonificationProfile.profileMap.put(UESonificationProfile.NONE.getCode(), (Object)UESonificationProfile.NONE);
        UESonificationProfile.profileMap.put(UESonificationProfile.CONGA.getCode(), (Object)UESonificationProfile.CONGA);
        UESonificationProfile.profileMap.put(UESonificationProfile.GUITAR.getCode(), (Object)UESonificationProfile.GUITAR);
        UESonificationProfile.profileMap.put(UESonificationProfile.ENSEMBLE.getCode(), (Object)UESonificationProfile.ENSEMBLE);
        UESonificationProfile.profileMap.put(UESonificationProfile.TABLA.getCode(), (Object)UESonificationProfile.TABLA);
        UESonificationProfile.deviceToProfileMap.put(UEDeviceType.Kora, UESonificationProfile.CONGA);
        UESonificationProfile.deviceToProfileMap.put(UEDeviceType.Maximus, UESonificationProfile.CONGA);
        UESonificationProfile.deviceToProfileMap.put(UEDeviceType.Titus, UESonificationProfile.ENSEMBLE);
        UESonificationProfile.deviceToProfileMap.put(UEDeviceType.Caribe, UESonificationProfile.TABLA);
    }
    
    private UESonificationProfile(final int mCode) {
        this.mCode = mCode;
    }
    
    public static int getCode(final UESonificationProfile ueSonificationProfile) {
        return ueSonificationProfile.mCode;
    }
    
    public static UESonificationProfile getProfile(final int n) {
        return (UESonificationProfile)UESonificationProfile.profileMap.get(n);
    }
    
    public static UESonificationProfile getProfile(final UEDeviceType key) {
        return UESonificationProfile.deviceToProfileMap.get(key);
    }
    
    public int getCode() {
        return this.mCode;
    }
}
