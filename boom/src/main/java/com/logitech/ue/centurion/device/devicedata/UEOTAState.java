// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

public enum UEOTAState
{
    STATUS_CMD_COMPLETED_OK(0), 
    STATUS_CMD_FAILED(2), 
    STATUS_CMD_IN_PROGRESS(1), 
    STATUS_CMD_OTHER_ERROR(4), 
    STATUS_CMD_TIMEOUT(3);
    
    final int mCode;
    
    private UEOTAState(final int mCode) {
        this.mCode = mCode;
    }
    
    public int getCode() {
        return this.mCode;
    }
}
