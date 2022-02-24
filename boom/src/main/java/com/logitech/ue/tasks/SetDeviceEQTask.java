// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.device.devicedata.UEEQSetting;

public class SetDeviceEQTask extends SetDeviceDataTask
{
    private static final String TAG;
    protected UEEQSetting mEQSetting;
    
    static {
        TAG = SetDeviceEQTask.class.getSimpleName();
    }
    
    public SetDeviceEQTask(final UEEQSetting meqSetting) {
        this.mEQSetting = meqSetting;
    }
    
    @Override
    public String getTag() {
        return SetDeviceEQTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().setEQSetting(this.mEQSetting);
        return null;
    }
}
