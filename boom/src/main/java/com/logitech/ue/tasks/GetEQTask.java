// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.device.devicedata.UEEQSetting;

public class GetEQTask extends GetDeviceDataTask<UEEQSetting>
{
    private static final String TAG;
    
    static {
        TAG = GetEQTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return GetEQTask.TAG;
    }
    
    @Override
    public UEEQSetting work(final Void... array) throws Exception {
        return UEDeviceManager.getInstance().getConnectedDevice().getEQSetting();
    }
}
