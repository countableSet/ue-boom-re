// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.device.devicedata.UEBlockPartyState;

public class GetDeviceBlockPartyStatueTask extends GetDeviceDataTask<UEBlockPartyState>
{
    private static final String TAG;
    
    static {
        TAG = GetDeviceBlockPartyStatueTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return GetDeviceBlockPartyStatueTask.TAG;
    }
    
    @Override
    public UEBlockPartyState work(final Void... array) throws Exception {
        return UEDeviceManager.getInstance().getConnectedDevice().getBlockPartyState();
    }
}
