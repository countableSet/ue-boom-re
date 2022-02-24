// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.device.devicedata.UEAudioRouting;

public class GetAudioRoutingTask extends GetDeviceDataTask<UEAudioRouting>
{
    private static final String TAG;
    
    static {
        TAG = GetAudioRoutingTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return GetAudioRoutingTask.TAG;
    }
    
    @Override
    public UEAudioRouting work(final Void... array) throws Exception {
        return UEDeviceManager.getInstance().getConnectedDevice().getAudioRouting();
    }
}
