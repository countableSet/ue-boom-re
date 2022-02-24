// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.command;

public class UEOTADeviceCommand extends UEGenericDeviceCommand
{
    public static final int COMMAND_LENGTH = 3;
    
    protected UEOTADeviceCommand(final UEOTACommand ueotaCommand, final byte[] array) {
        super((short)ueotaCommand.getCode(), array, (short)0, ueotaCommand.name());
    }
    
    protected UEOTADeviceCommand(final short n, final byte[] array, final short n2, final String s) {
        super(n, array, n2, s);
    }
    
    public static UEOTADeviceCommand newCommand(final UEOTACommand ueotaCommand, final byte[] array) {
        return new UEOTADeviceCommand(ueotaCommand, array);
    }
    
    @Override
    public byte[] buildCommandData() {
        int n;
        if (this.mValue != null) {
            n = this.mValue.length + 3;
        }
        else {
            n = 3;
        }
        final byte[] array = new byte[n];
        array[0] = (byte)(this.mCommand & 0xFF);
        byte b;
        if (this.mValue != null) {
            b = (byte)(this.mValue.length & 0xFF);
        }
        else {
            b = 0;
        }
        array[1] = b;
        byte b2;
        if (this.mValue != null) {
            b2 = (byte)(this.mValue.length >> 8 & 0xFF);
        }
        else {
            b2 = 0;
        }
        array[2] = b2;
        if (this.mValue != null) {
            System.arraycopy(this.mValue, 0, array, 3, this.mValue.length);
        }
        return array;
    }
    
    @Override
    public int getReturnCommand() {
        return this.getCommandHex();
    }
}
