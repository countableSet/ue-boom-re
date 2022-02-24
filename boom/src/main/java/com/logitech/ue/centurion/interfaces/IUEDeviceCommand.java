// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.interfaces;

import java.io.Serializable;

public interface IUEDeviceCommand extends Serializable
{
    byte[] buildCommandData();
    
    int getCommandHex();
    
    String getCommandName();
    
    int getReturnCommand();
}
