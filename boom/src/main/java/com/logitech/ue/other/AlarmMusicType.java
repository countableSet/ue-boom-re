// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import android.util.SparseArray;

public enum AlarmMusicType
{
    LAST_PLAY(1), 
    MULTI_SOUND(3), 
    NO_MUSIC(0), 
    SINGLE_SOUND(2);
    
    private static final SparseArray<AlarmMusicType> typeMap;
    final int typeCode;
    
    static {
        (typeMap = new SparseArray(5)).put(0, (Object)AlarmMusicType.NO_MUSIC);
        AlarmMusicType.typeMap.put(1, (Object)AlarmMusicType.LAST_PLAY);
        AlarmMusicType.typeMap.put(2, (Object)AlarmMusicType.SINGLE_SOUND);
        AlarmMusicType.typeMap.put(3, (Object)AlarmMusicType.MULTI_SOUND);
    }
    
    private AlarmMusicType(final int typeCode) {
        this.typeCode = typeCode;
    }
    
    public static AlarmMusicType getAlarmByCode(final int n) {
        return (AlarmMusicType)AlarmMusicType.typeMap.get(n);
    }
    
    public static int getCode(final AlarmMusicType alarmMusicType) {
        return alarmMusicType.typeCode;
    }
    
    public int getCode() {
        return getCode(this);
    }
}
