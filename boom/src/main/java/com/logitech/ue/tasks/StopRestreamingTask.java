// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class StopRestreamingTask extends SetDeviceDataTask
{
    private static final String TAG;
    
    static {
        TAG = StopRestreamingTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return StopRestreamingTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().stopRestreamingMode();
        return null;
    }
}
