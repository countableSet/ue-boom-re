// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class TurnOffDeviceTask extends SetDeviceDataTask
{
    private static final String TAG;
    
    static {
        TAG = TurnOffDeviceTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return TurnOffDeviceTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().remoteOff();
        return null;
    }
}
