// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class GetDeviceBatteryLevelViaBleTask extends GetDeviceDataTask<Byte>
{
    private static final String TAG;
    
    static {
        TAG = GetDeviceBatteryLevelViaBleTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return GetDeviceBatteryLevelViaBleTask.TAG;
    }
    
    @Override
    public Byte work(final Void... array) throws Exception {
        return (byte)UEDeviceManager.getInstance().getConnectedDevice().getBatteryLevel();
    }
}
