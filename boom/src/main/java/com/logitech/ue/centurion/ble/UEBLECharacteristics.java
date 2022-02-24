// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.ble;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class UEBLECharacteristics
{
    public static final String CHAR_ALARM_GET = "CHAR_ALARM_GET";
    public static final String CHAR_ALARM_SET = "CHAR_ALARM_SET";
    public static final String CHAR_BATTERY_LEVEL = "CHAR_BATTERY_LEVEL";
    public static final String CHAR_DEVICE_NAME = "CHAR_DEVICE_NAME";
    public static final String CHAR_FW_VERSION = "CHAR_FW_VERSION";
    public static final String CHAR_POWER_ON = "CHAR_POWER_ON";
    public static final String CHAR_SN = "CHAR_SN";
    public static final UUID UUID_CHAR_ALARM_GET;
    public static final UUID UUID_CHAR_ALARM_SET;
    public static final UUID UUID_CHAR_BATTERY_LEVEL;
    public static final UUID UUID_CHAR_DEVICE_NAME;
    public static final UUID UUID_CHAR_FW_VERSION;
    public static final UUID UUID_CHAR_POWER_ON;
    public static final UUID UUID_CHAR_SN;
    private static final Map<String, UUID> mUuidMap;
    
    static {
        UUID_CHAR_POWER_ON = UUID.fromString("c6d6dc0d-07f5-47ef-9b59-630622b01fd3");
        UUID_CHAR_ALARM_GET = UUID.fromString("16e005bb-3862-43c7-8f5c-6f654a4ffdd2");
        UUID_CHAR_ALARM_SET = UUID.fromString("16e005bb-3862-43c7-8f5c-6f654a4ffdd2");
        UUID_CHAR_BATTERY_LEVEL = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");
        UUID_CHAR_DEVICE_NAME = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb");
        UUID_CHAR_FW_VERSION = UUID.fromString("00002a28-0000-1000-8000-00805f9b34fb");
        UUID_CHAR_SN = UUID.fromString("00002a25-0000-1000-8000-00805f9b34fb");
        (mUuidMap = new HashMap<String, UUID>()).put("CHAR_POWER_ON", UEBLECharacteristics.UUID_CHAR_POWER_ON);
        UEBLECharacteristics.mUuidMap.put("CHAR_ALARM_GET", UEBLECharacteristics.UUID_CHAR_ALARM_GET);
        UEBLECharacteristics.mUuidMap.put("CHAR_ALARM_SET", UEBLECharacteristics.UUID_CHAR_ALARM_SET);
        UEBLECharacteristics.mUuidMap.put("CHAR_BATTERY_LEVEL", UEBLECharacteristics.UUID_CHAR_BATTERY_LEVEL);
        UEBLECharacteristics.mUuidMap.put("CHAR_DEVICE_NAME", UEBLECharacteristics.UUID_CHAR_DEVICE_NAME);
        UEBLECharacteristics.mUuidMap.put("CHAR_FW_VERSION", UEBLECharacteristics.UUID_CHAR_FW_VERSION);
        UEBLECharacteristics.mUuidMap.put("CHAR_SN", UEBLECharacteristics.UUID_CHAR_SN);
    }
    
    private UEBLECharacteristics() {
    }
    
    public static UUID getUUID(final String s) {
        return UEBLECharacteristics.mUuidMap.get(s);
    }
}
