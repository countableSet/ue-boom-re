// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class BeginBroadcastVolumeSyncTask extends SetDeviceDataTask
{
    private static final String TAG;
    
    static {
        TAG = BeginBroadcastVolumeSyncTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return BeginBroadcastVolumeSyncTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().syncBroadcastVolume();
        return null;
    }
}
