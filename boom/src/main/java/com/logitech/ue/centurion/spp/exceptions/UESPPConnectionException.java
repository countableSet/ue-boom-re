// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.spp.exceptions;

import com.logitech.ue.centurion.exceptions.UEConnectionException;

public class UESPPConnectionException extends UEConnectionException
{
    public static final String MESSAGE_STREAM_WRITE_FAILED = "Failed to write to the stream. Stream connection lost.";
    private static final long serialVersionUID = -8157838223473545850L;
    
    public UESPPConnectionException(final String s) {
        super(s);
    }
}
