// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.device.devicedata.UEChargingInfo;

public class GetDeviceChargingInfoTask extends GetDeviceDataTask<UEChargingInfo>
{
    private static final String TAG;
    
    static {
        TAG = GetDeviceChargingInfoTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return GetDeviceChargingInfoTask.TAG;
    }
    
    @Override
    public UEChargingInfo work(final Void... array) throws Exception {
        return UEDeviceManager.getInstance().getConnectedDevice().getChargingInfo();
    }
}
