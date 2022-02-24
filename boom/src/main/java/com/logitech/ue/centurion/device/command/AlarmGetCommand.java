// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.command;

public enum AlarmGetCommand
{
    ALARM_STATE(1), 
    BACKUP_TONE_NUMBER(8), 
    NOTIFICATION(64), 
    REPEAT_DAY(7), 
    SNOOZE_TIME(4), 
    VOLUME16(6), 
    VOLUME32(10);
    
    final int commandCode;
    
    private AlarmGetCommand(final int commandCode) {
        this.commandCode = commandCode;
    }
    
    public int getCode() {
        return this.commandCode;
    }
}
