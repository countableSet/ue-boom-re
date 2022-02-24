// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class GetDeviceSlaveBatteryLevelTask extends GetDeviceDataTask<Byte>
{
    private static final String TAG;
    
    static {
        TAG = GetDeviceSlaveBatteryLevelTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return GetDeviceSlaveBatteryLevelTask.TAG;
    }
    
    @Override
    public Byte work(final Void... array) throws Exception {
        return UEDeviceManager.getInstance().getConnectedDevice().getSlaveBatteryLevel();
    }
}
