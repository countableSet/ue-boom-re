// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.exceptions;

import android.support.annotation.NonNull;
import com.logitech.ue.centurion.interfaces.IUEDeviceCommand;

public class UEUnrecognisedCommandException extends UEMessageExecutionException
{
    private static final long serialVersionUID = -4742307958053018203L;
    
    public UEUnrecognisedCommandException(@NonNull final IUEDeviceCommand iueDeviceCommand) {
        super(iueDeviceCommand, "Command is not supported " + iueDeviceCommand.getCommandName());
    }
}
