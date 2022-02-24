// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class GetIsBroadcastSupportedTask extends GetDeviceDataTask<Boolean>
{
    private static final String TAG;
    
    static {
        TAG = GetIsBroadcastSupportedTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return GetIsBroadcastSupportedTask.TAG;
    }
    
    @Override
    public Boolean work(final Void... array) throws Exception {
        return UEDeviceManager.getInstance().getConnectedDevice().isBroadcastModeSupported();
    }
}
