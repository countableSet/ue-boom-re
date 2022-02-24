// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.command;

import com.logitech.ue.centurion.interfaces.IUEDeviceCommand;

public class UEGenericDeviceCommand implements IUEDeviceCommand
{
    public static final int COMMAND_LENGTH = 2;
    private static final long serialVersionUID = 0L;
    protected final short mCommand;
    protected final String mCommandName;
    protected final short mReturnCommand;
    protected final byte[] mValue;
    
    protected UEGenericDeviceCommand(final short n, final byte[] mValue, final short n2, String mCommandName) {
        this.mCommand = n;
        this.mValue = mValue;
        this.mReturnCommand = n2;
        if (mCommandName == null) {
            mCommandName = "";
        }
        this.mCommandName = mCommandName;
    }
    
    public static UEGenericDeviceCommand newCommand(final short n, final byte[] array, final short n2, final String s) {
        return new UEGenericDeviceCommand(n, array, n2, s);
    }
    
    @Override
    public byte[] buildCommandData() {
        int n;
        if (this.mValue != null) {
            n = this.mValue.length + 2;
        }
        else {
            n = 2;
        }
        final byte[] array = new byte[n + 1];
        array[0] = (byte)n;
        array[1] = (byte)(this.mCommand >> 8 & 0xFF);
        array[2] = (byte)(this.mCommand & 0xFF);
        if (this.mValue != null) {
            System.arraycopy(this.mValue, 0, array, 3, this.mValue.length);
        }
        return array;
    }
    
    @Override
    public int getCommandHex() {
        return this.mCommand;
    }
    
    @Override
    public String getCommandName() {
        return this.mCommandName;
    }
    
    @Override
    public int getReturnCommand() {
        return this.mReturnCommand;
    }
}
