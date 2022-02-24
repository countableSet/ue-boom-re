// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.device.devicedata.UEAlarmInfo;

public class GetDeviceAlarmInfoViaBleTask extends GetDeviceDataTask<UEAlarmInfo>
{
    private static final String TAG;
    
    static {
        TAG = GetDeviceAlarmInfoViaBleTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return GetDeviceAlarmInfoViaBleTask.TAG;
    }
    
    @Override
    public UEAlarmInfo work(final Void... array) throws Exception {
        return UEDeviceManager.getInstance().getConnectedDevice().getAlarmInfo();
    }
}
