// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.exceptions;

public class ReconnectionException extends Exception
{
    public static final String MESSAGE_RECONNECTION_FAILED = "Device fail to reconnect in time";
    private static final long serialVersionUID = -8157838223473545850L;
    
    public ReconnectionException() {
        super("Device fail to reconnect in time");
    }
}
