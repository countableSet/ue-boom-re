// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastAudioMode;
import com.logitech.ue.centurion.utils.MAC;

public class AddReceiverTask extends SetDeviceDataTask
{
    private static final String TAG;
    protected MAC mAddress;
    protected UEBroadcastAudioMode mAudioMode;
    
    static {
        TAG = AddReceiverTask.class.getSimpleName();
    }
    
    public AddReceiverTask(final MAC mAddress, final UEBroadcastAudioMode mAudioMode) {
        this.mAddress = mAddress;
        this.mAudioMode = mAudioMode;
    }
    
    @Override
    public String getTag() {
        return AddReceiverTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().addReceiverToBroadcast(this.mAddress, this.mAudioMode);
        return null;
    }
}
