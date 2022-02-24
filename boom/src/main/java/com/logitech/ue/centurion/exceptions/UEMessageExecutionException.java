// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.exceptions;

import com.logitech.ue.centurion.interfaces.IUEDeviceCommand;

public class UEMessageExecutionException extends UEOperationException
{
    private static final long serialVersionUID = -284951423863460531L;
    protected final IUEDeviceCommand mCommand;
    
    public UEMessageExecutionException(final IUEDeviceCommand mCommand, final String str) {
        super(mCommand.getCommandName() + ": " + str);
        this.mCommand = mCommand;
    }
    
    public IUEDeviceCommand getCommand() {
        return this.mCommand;
    }
}
