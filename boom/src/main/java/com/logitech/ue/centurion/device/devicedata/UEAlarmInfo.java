// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import java.sql.Time;
import com.logitech.ue.centurion.utils.UEUtils;

public class UEAlarmInfo
{
    private static final String TAG;
    private String mAlarmHost;
    private UEAlarmStatus mAlarmStatus;
    private long mAlarmTimer;
    
    static {
        TAG = UEAlarmInfo.class.getSimpleName();
    }
    
    public UEAlarmInfo() {
        this.mAlarmStatus = UEAlarmStatus.OFF;
        this.mAlarmTimer = 0L;
        this.mAlarmHost = "00:00";
    }
    
    public static UEAlarmInfo buildFromBLEMessage(final byte[] array) {
        final UEAlarmInfo ueAlarmInfo = new UEAlarmInfo();
        ueAlarmInfo.mAlarmTimer = UEUtils.getUnsignedIntToLong(UEUtils.byteArrayToInt(array));
        ueAlarmInfo.mAlarmHost = String.format("%02X:%02X", array[4], array[5]);
        return ueAlarmInfo;
    }
    
    public static UEAlarmInfo buildFromCenturionMessage(final byte[] array) {
        final UEAlarmInfo ueAlarmInfo = new UEAlarmInfo();
        ueAlarmInfo.mAlarmStatus = UEAlarmStatus.getStatus(array[3]);
        ueAlarmInfo.mAlarmTimer = UEUtils.getUnsignedIntToLong(UEUtils.byteArrayToInt(array, 5));
        ueAlarmInfo.mAlarmHost = String.format("%02X:%02X", array[9], array[10]);
        return ueAlarmInfo;
    }
    
    public String getAlarmHostAddress() {
        return this.mAlarmHost;
    }
    
    public UEAlarmStatus getAlarmState() {
        return this.mAlarmStatus;
    }
    
    public Time getAlarmTime() {
        return new Time(System.currentTimeMillis() + this.mAlarmTimer);
    }
    
    public long getLastTimer() {
        return this.mAlarmTimer;
    }
}
