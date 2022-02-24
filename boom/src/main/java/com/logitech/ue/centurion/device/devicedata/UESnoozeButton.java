// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UESnoozeButton
{
    BT(0), 
    VOL_DOWN(2), 
    VOL_UP(1);
    
    private static final SparseArray<UESnoozeButton> buttonMap;
    final int code;
    
    static {
        (buttonMap = new SparseArray(3)).put(0, (Object)UESnoozeButton.BT);
        UESnoozeButton.buttonMap.put(1, (Object)UESnoozeButton.VOL_UP);
        UESnoozeButton.buttonMap.put(2, (Object)UESnoozeButton.VOL_DOWN);
    }
    
    private UESnoozeButton(final int code) {
        this.code = code;
    }
    
    public static UESnoozeButton getButtonName(final byte b) {
        return (UESnoozeButton)UESnoozeButton.buttonMap.get((int)b);
    }
    
    public int getCode() {
        return this.code;
    }
}
