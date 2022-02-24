// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.devicedata;

import java.util.Calendar;
import java.sql.Time;
import com.logitech.ue.other.AlarmMusicType;
import com.logitech.ue.other.MusicSelection;
import com.logitech.ue.UserPreferences;

public class AlarmSettings
{
    public static final int ALARM_DEFAULT_VOLUME = 10;
    private static final long DAY_IN_MS = 86400000L;
    private static final long MINIMUM_THRESHOLD = 35000L;
    
    private AlarmSettings() {
    }
    
    public static String getHostAddress() {
        return UserPreferences.getInstance().getHostAddress();
    }
    
    public static MusicSelection getMusicSelection() {
        return UserPreferences.getInstance().getAlarmMusicSelection();
    }
    
    public static AlarmMusicType getMusicType() {
        return UserPreferences.getInstance().getAlarmMusicType();
    }
    
    public static Time getTime() {
        return UserPreferences.getInstance().getAlarmLastTime();
    }
    
    public static long getTimerInMilliseconds() {
        final Calendar instance = Calendar.getInstance();
        instance.set(11, getTime().getHours());
        instance.set(12, getTime().getMinutes());
        instance.set(13, 0);
        final Calendar instance2 = Calendar.getInstance();
        final long n = instance.getTimeInMillis() - instance2.getTimeInMillis();
        long n2;
        if (instance.get(11) == instance2.get(11) && instance.get(12) == instance2.get(12)) {
            n2 = n + 86400000L;
        }
        else {
            n2 = n;
            if (instance.get(11) == instance2.get(11)) {
                n2 = n;
                if (instance.get(12) == instance2.get(12) + 1) {
                    if (n >= 35000L) {
                        n2 = n;
                    }
                    else {
                        n2 = 35000L;
                    }
                }
            }
        }
        long n3 = n2;
        if (n2 <= 0L) {
            n3 = n2 + 86400000L;
        }
        return n3;
    }
    
    public static int getVolume() {
        int alarmVolume;
        if (UserPreferences.getInstance().getAlarmVolume() > 0) {
            alarmVolume = UserPreferences.getInstance().getAlarmVolume();
        }
        else {
            alarmVolume = 10;
        }
        return alarmVolume;
    }
    
    public static boolean isOn() {
        return UserPreferences.getInstance().getIsAlarmOn();
    }
    
    public static boolean isRepeat() {
        return UserPreferences.getInstance().getIsAlarmRepeat();
    }
    
    public static void setHostAddress(final String hostAddress) {
        UserPreferences.getInstance().setHostAddress(hostAddress);
    }
    
    public static void setMusicSelection(final MusicSelection alarmMusicSelection) {
        UserPreferences.getInstance().setAlarmMusicSelection(alarmMusicSelection);
    }
    
    public static void setMusicType(final AlarmMusicType alarmMusicType) {
        UserPreferences.getInstance().setAlarmMusicType(alarmMusicType);
    }
    
    public static void setOn(final boolean isAlarmOn) {
        UserPreferences.getInstance().setIsAlarmOn(isAlarmOn);
    }
    
    public static void setRepeat(final boolean isAlarmRepeat) {
        UserPreferences.getInstance().setIsAlarmRepeat(isAlarmRepeat);
    }
    
    public static void setTime(final Time alarmLastTime) {
        UserPreferences.getInstance().setAlarmLastTime(alarmLastTime);
    }
    
    public static void setVolume(final int alarmVolume) {
        UserPreferences.getInstance().setAlarmVolume(alarmVolume);
    }
}
