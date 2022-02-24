// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.device.devicedata.UEAudioRouting;

public class SetDeviceAudioRoutingTask extends SetDeviceDataTask
{
    private static final String TAG;
    protected UEAudioRouting mAudioRouting;
    
    static {
        TAG = SetDeviceAudioRoutingTask.class.getSimpleName();
    }
    
    public SetDeviceAudioRoutingTask(final UEAudioRouting mAudioRouting) {
        this.mAudioRouting = mAudioRouting;
    }
    
    @Override
    public String getTag() {
        return SetDeviceAudioRoutingTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().setAudioRouting(this.mAudioRouting);
        return null;
    }
}
