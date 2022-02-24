// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class GetDeviceVolumeTask extends GetDeviceDataTask<Integer>
{
    private static final String TAG;
    
    static {
        TAG = GetDeviceVolumeTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return GetDeviceVolumeTask.TAG;
    }
    
    @Override
    public Integer work(final Void... array) throws Exception {
        return UEDeviceManager.getInstance().getConnectedDevice().getVolume();
    }
}
