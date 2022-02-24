// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.command;

public enum AlarmSetCommand
{
    ACK_ALARM(5), 
    BACKUP_TONE(8), 
    CLEAR(0), 
    SET_ALARM(1), 
    SET_ALARM_VOLUME(6), 
    SET_ALARM_VOLUME_32(10), 
    SET_REPEAT_DAY(7), 
    SET_SNOOZE_TIME(4), 
    SET_TIMEOUT_AFTER_NO_STREAMING(9), 
    SNOOZE(2), 
    STOP_CURRENT_ALARM(3);
    
    final int commandCode;
    
    private AlarmSetCommand(final int commandCode) {
        this.commandCode = commandCode;
    }
    
    public int getCode() {
        return this.commandCode;
    }
}
