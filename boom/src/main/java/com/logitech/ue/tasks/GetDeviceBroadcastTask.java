// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastConfiguration;

public class GetDeviceBroadcastTask extends GetDeviceDataTask<UEBroadcastConfiguration>
{
    private static final String TAG;
    
    static {
        TAG = GetDeviceBroadcastTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return GetDeviceBroadcastTask.TAG;
    }
    
    @Override
    public UEBroadcastConfiguration work(final Void... array) throws Exception {
        return UEDeviceManager.getInstance().getConnectedDevice().getBroadcastMode();
    }
}
