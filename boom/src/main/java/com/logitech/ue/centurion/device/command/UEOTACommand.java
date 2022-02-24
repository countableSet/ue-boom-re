// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.command;

public enum UEOTACommand
{
    ERASE_SQIF(1), 
    NOP(0), 
    OTA_CANCEL(6), 
    READ_DEVICE_NAME(4), 
    RUN_DFU(5), 
    VALIDATE_SQIF(3), 
    WRITE_SQIF(2);
    
    final int command;
    
    private UEOTACommand(final int command) {
        this.command = command;
    }
    
    public static byte[] NOP() {
        return new byte[] { (byte)UEOTACommand.NOP.getCode(), 0, 0 };
    }
    
    public static byte[] cancelOTA() {
        return cancelOTA((byte)0, (byte)0);
    }
    
    public static byte[] cancelOTA(final byte b, final byte b2) {
        return new byte[] { (byte)UEOTACommand.OTA_CANCEL.getCode(), b, b2 };
    }
    
    public static byte[] eraseSqif() {
        return eraseSqif((byte)1, (byte)0, (byte)0);
    }
    
    public static byte[] eraseSqif(final byte b, final byte b2, final byte b3) {
        return new byte[] { (byte)UEOTACommand.ERASE_SQIF.getCode(), b, b2, b3 };
    }
    
    public static byte[] runDfu() {
        return runDfu((byte)1, (byte)0, (byte)0);
    }
    
    public static byte[] runDfu(final byte b, final byte b2, final byte b3) {
        return new byte[] { (byte)UEOTACommand.RUN_DFU.getCode(), b, b2, b3 };
    }
    
    public static byte[] validateSqif() {
        return new byte[] { (byte)UEOTACommand.VALIDATE_SQIF.getCode(), 0, 0 };
    }
    
    public static byte[] writeSqif() {
        return writeSqif((byte)0, (byte)4);
    }
    
    public static byte[] writeSqif(final byte b, final byte b2) {
        final byte[] array = new byte[1027];
        array[0] = (byte)UEOTACommand.WRITE_SQIF.getCode();
        array[1] = b;
        array[2] = b2;
        return array;
    }
    
    public int getCode() {
        return this.command;
    }
}
