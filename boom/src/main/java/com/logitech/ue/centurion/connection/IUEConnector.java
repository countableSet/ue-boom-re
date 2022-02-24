// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.connection;

public interface IUEConnector
{
    void connectToDevice() throws Exception;
    
    void disconnectFromDevice();
    
    UEConnectionType getConnectionType();
}
