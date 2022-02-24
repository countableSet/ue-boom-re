// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.App;
import com.logitech.ue.centurion.UEDeviceManager;

public class PowerOnDeviceTask extends SetDeviceDataTask
{
    private static final String TAG;
    
    static {
        TAG = PowerOnDeviceTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return PowerOnDeviceTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().setPowerOn(App.getInstance().getBluetoothMacAddress());
        return null;
    }
}
