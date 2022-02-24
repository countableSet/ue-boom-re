// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UEAlarmStatus
{
    BACKUP(3), 
    BACKUP_AFTER_SNOOZE(131), 
    FIRE(1), 
    FIRE_AFTER_SNOOZE(129), 
    OFF(0), 
    RECOVERY(16), 
    REMINDER(5), 
    SNOOZING(4), 
    SNOOZING_AFTER_SNOOZE(132), 
    STREAMING(2), 
    STREAMING_AFTER_SNOOZE(130), 
    UNKNOWN(-1);
    
    private static final SparseArray<UEAlarmStatus> statusMap;
    final int statusCode;
    
    static {
        (statusMap = new SparseArray(11)).put(0, (Object)UEAlarmStatus.OFF);
        UEAlarmStatus.statusMap.put(1, (Object)UEAlarmStatus.FIRE);
        UEAlarmStatus.statusMap.put(129, (Object)UEAlarmStatus.FIRE_AFTER_SNOOZE);
        UEAlarmStatus.statusMap.put(2, (Object)UEAlarmStatus.STREAMING);
        UEAlarmStatus.statusMap.put(130, (Object)UEAlarmStatus.STREAMING_AFTER_SNOOZE);
        UEAlarmStatus.statusMap.put(3, (Object)UEAlarmStatus.BACKUP);
        UEAlarmStatus.statusMap.put(131, (Object)UEAlarmStatus.BACKUP_AFTER_SNOOZE);
        UEAlarmStatus.statusMap.put(4, (Object)UEAlarmStatus.SNOOZING);
        UEAlarmStatus.statusMap.put(132, (Object)UEAlarmStatus.SNOOZING_AFTER_SNOOZE);
        UEAlarmStatus.statusMap.put(5, (Object)UEAlarmStatus.REMINDER);
        UEAlarmStatus.statusMap.put(16, (Object)UEAlarmStatus.RECOVERY);
    }
    
    private UEAlarmStatus(final int statusCode) {
        this.statusCode = statusCode;
    }
    
    public static UEAlarmStatus getStatus(final byte b) {
        return (UEAlarmStatus)UEAlarmStatus.statusMap.get((int)b, (Object)UEAlarmStatus.UNKNOWN);
    }
    
    public static UEAlarmStatus getStatus(final int n) {
        return (UEAlarmStatus)UEAlarmStatus.statusMap.get(n, (Object)UEAlarmStatus.UNKNOWN);
    }
    
    public int getCode() {
        return this.statusCode;
    }
}
