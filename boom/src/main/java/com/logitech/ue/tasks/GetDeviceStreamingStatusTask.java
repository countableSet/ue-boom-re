// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStreamingStatus;

public class GetDeviceStreamingStatusTask extends GetDeviceDataTask<UEDeviceStreamingStatus>
{
    private static final String TAG;
    
    static {
        TAG = GetDeviceStreamingStatusTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return GetDeviceStreamingStatusTask.TAG;
    }
    
    @Override
    public UEDeviceStreamingStatus work(final Void... array) throws Exception {
        return UEDeviceManager.getInstance().getConnectedDevice().getDeviceStreamingStatus();
    }
}
