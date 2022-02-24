// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.exceptions;

import com.logitech.ue.centurion.interfaces.IUEDeviceCommand;

public class UEOTAException extends UEOperationException
{
    private static final long serialVersionUID = -284951423863460531L;
    protected final IUEDeviceCommand mCommand;
    
    public UEOTAException(final IUEDeviceCommand mCommand, final int i) {
        super(mCommand.getCommandName() + ". Bad status: " + i);
        this.mCommand = mCommand;
    }
    
    public IUEDeviceCommand getCommand() {
        return this.mCommand;
    }
}
