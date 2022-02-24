// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import java.util.Iterator;
import java.util.HashMap;

public enum UEDeviceType
{
    Caribe("Caribe"), 
    Kora("Kora"), 
    Maximus("Maximus"), 
    RedRocks("RedRocks"), 
    Titus("Titus"), 
    Unknown("Unknown");
    
    private static final HashMap<UEDeviceType, String> deviceIDPatternMap;
    private static final HashMap<String, UEDeviceType> deviceTypeMap;
    final String deviceType;
    
    static {
        (deviceTypeMap = new HashMap<String, UEDeviceType>(16)).put("Titus", UEDeviceType.Titus);
        UEDeviceType.deviceTypeMap.put("Kora", UEDeviceType.Kora);
        UEDeviceType.deviceTypeMap.put("Caribe", UEDeviceType.Caribe);
        UEDeviceType.deviceTypeMap.put("Maximus", UEDeviceType.Maximus);
        UEDeviceType.deviceTypeMap.put("RedRocks", UEDeviceType.RedRocks);
        UEDeviceType.deviceTypeMap.put("Unknown", UEDeviceType.Unknown);
        (deviceIDPatternMap = new HashMap<UEDeviceType, String>(16)).put(UEDeviceType.Titus, "MEGABOOM");
        UEDeviceType.deviceIDPatternMap.put(UEDeviceType.Kora, "UE Boom\\s?");
        UEDeviceType.deviceIDPatternMap.put(UEDeviceType.Caribe, "((.)*ROLL(.)*)|(Caribe-2)");
        UEDeviceType.deviceIDPatternMap.put(UEDeviceType.Maximus, "MAXIMUS");
        UEDeviceType.deviceIDPatternMap.put(UEDeviceType.RedRocks, "RedRock");
    }
    
    private UEDeviceType(final String deviceType) {
        this.deviceType = deviceType;
    }
    
    public static UEDeviceType getDeviceTypeByID(final String s) {
        for (final UEDeviceType key : UEDeviceType.deviceIDPatternMap.keySet()) {
            if (s.matches(UEDeviceType.deviceIDPatternMap.get(key))) {
                return key;
            }
        }
        return UEDeviceType.Unknown;
    }
    
    public static UEDeviceType getDeviceTypeByName(final String key) {
        return UEDeviceType.deviceTypeMap.get(key);
    }
    
    public String getDeviceIDPattern() {
        return UEDeviceType.deviceIDPatternMap.get(this);
    }
    
    public String getDeviceTypeName() {
        return this.deviceType;
    }
}
