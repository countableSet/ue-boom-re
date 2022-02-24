// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.exceptions;

import com.logitech.ue.centurion.interfaces.IUEDeviceCommand;
import com.logitech.ue.centurion.device.devicedata.UEAckResponse;

public class UEErrorResultException extends UEMessageExecutionException
{
    private static final long serialVersionUID = -184138025386325165L;
    private final UEAckResponse mResponseCode;
    
    public UEErrorResultException(final IUEDeviceCommand iueDeviceCommand, final UEAckResponse mResponseCode) {
        super(iueDeviceCommand, String.format("Command failed with error code %s(0x%02X)", mResponseCode.name(), mResponseCode.getValue()));
        this.mResponseCode = mResponseCode;
    }
    
    public UEAckResponse getResponseCode() {
        return this.mResponseCode;
    }
}
