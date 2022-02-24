// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UEEQSetting
{
    BASS_BOOST(3), 
    CUSTOM(4), 
    INTIMATE(2), 
    OUT_LOUD(0), 
    UNKNOWN(-1), 
    VOCAL(1);
    
    private static final SparseArray<UEEQSetting> eqMap;
    private final int settingCode;
    
    static {
        (eqMap = new SparseArray(5)).put(UEEQSetting.OUT_LOUD.getCode(), (Object)UEEQSetting.OUT_LOUD);
        UEEQSetting.eqMap.put(UEEQSetting.VOCAL.getCode(), (Object)UEEQSetting.VOCAL);
        UEEQSetting.eqMap.put(UEEQSetting.INTIMATE.getCode(), (Object)UEEQSetting.INTIMATE);
        UEEQSetting.eqMap.put(UEEQSetting.BASS_BOOST.getCode(), (Object)UEEQSetting.BASS_BOOST);
        UEEQSetting.eqMap.put(UEEQSetting.CUSTOM.getCode(), (Object)UEEQSetting.CUSTOM);
    }
    
    private UEEQSetting(final int settingCode) {
        this.settingCode = settingCode;
    }
    
    public static int getCode(final UEEQSetting ueeqSetting) {
        return ueeqSetting.settingCode;
    }
    
    public static UEEQSetting getEQSetting(final int n) {
        return (UEEQSetting)UEEQSetting.eqMap.get(n);
    }
    
    public int getCode() {
        return getCode(this);
    }
}
