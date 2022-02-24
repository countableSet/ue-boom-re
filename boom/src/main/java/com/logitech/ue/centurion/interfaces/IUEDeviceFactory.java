// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.interfaces;

import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.connection.UEDeviceConnector;
import com.logitech.ue.centurion.utils.MAC;

public interface IUEDeviceFactory
{
    UEGenericDevice buildDevice(final String p0, final MAC p1, final UEDeviceConnector p2);
    
    boolean isDeviceIDValid(final String p0);
}
