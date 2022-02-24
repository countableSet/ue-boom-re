// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class BeginDeviceDiscoveryTask extends SetDeviceDataTask
{
    private static final String TAG;
    
    static {
        TAG = BeginDeviceDiscoveryTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return BeginDeviceDiscoveryTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().beginDeviceDiscoveryMode();
        return null;
    }
}
