// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UEAlarmNotificationFlags
{
    FIRE(1), 
    OFF(0), 
    SNOOZE(2), 
    UNKNOWN(-1);
    
    private static final SparseArray<UEAlarmNotificationFlags> statusMap;
    final int statusCode;
    
    static {
        (statusMap = new SparseArray(11)).put(UEAlarmNotificationFlags.OFF.getCode(), (Object)UEAlarmNotificationFlags.OFF);
        UEAlarmNotificationFlags.statusMap.put(UEAlarmNotificationFlags.FIRE.getCode(), (Object)UEAlarmNotificationFlags.FIRE);
        UEAlarmNotificationFlags.statusMap.put(UEAlarmNotificationFlags.SNOOZE.getCode(), (Object)UEAlarmNotificationFlags.SNOOZE);
    }
    
    private UEAlarmNotificationFlags(final int statusCode) {
        this.statusCode = statusCode;
    }
    
    public static UEAlarmNotificationFlags getStatus(final byte b) {
        return (UEAlarmNotificationFlags)UEAlarmNotificationFlags.statusMap.get((int)b);
    }
    
    public static UEAlarmNotificationFlags getStatus(final int n) {
        return (UEAlarmNotificationFlags)UEAlarmNotificationFlags.statusMap.get(n, (Object)UEAlarmNotificationFlags.UNKNOWN);
    }
    
    public int getCode() {
        return this.statusCode;
    }
}
