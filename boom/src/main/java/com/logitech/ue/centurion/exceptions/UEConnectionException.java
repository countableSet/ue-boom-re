// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.exceptions;

public class UEConnectionException extends UEException
{
    public static final String MESSAGE_CONNECTION_FAILED = "Connector is not connected. Call open connection first";
    public static final String MESSAGE_CONNECTOR_ALREADY_CONNECTED = "Connector is already connected";
    public static final String MESSAGE_CONNECTOR_IS_NOT_CONNECTED = "Connector is not connected. Call open connection first";
    public static final String MESSAGE_STREAM_WRITE_FAILED = "Failed to write to the stream. Stream connection lost.";
    public static final String MESSAGE_WRONG_CONNECTOR_MODE = "Connector is not in necessary mode to process this message";
    private static final long serialVersionUID = -8157838223473545850L;
    
    public UEConnectionException(final String s) {
        super(s);
    }
}
