// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.utils.MAC;

public class RemoveReceiverTask extends SetDeviceDataTask
{
    private static final String TAG;
    protected MAC mAddress;
    
    static {
        TAG = RemoveReceiverTask.class.getSimpleName();
    }
    
    public RemoveReceiverTask(final MAC mAddress) {
        this.mAddress = mAddress;
    }
    
    @Override
    public String getTag() {
        return RemoveReceiverTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().removeReceiverFromBroadcast(this.mAddress);
        return null;
    }
}
