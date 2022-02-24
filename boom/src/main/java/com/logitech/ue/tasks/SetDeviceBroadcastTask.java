// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.Utils;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastState;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastAudioOptions;

public class SetDeviceBroadcastTask extends SetDeviceDataTask
{
    private static final String TAG;
    public UEBroadcastAudioOptions mAudioOptions;
    public UEBroadcastState mState;
    
    static {
        TAG = SetDeviceBroadcastTask.class.getSimpleName();
    }
    
    public SetDeviceBroadcastTask(final UEBroadcastState mState, final UEBroadcastAudioOptions mAudioOptions) {
        this.mState = mState;
        this.mAudioOptions = mAudioOptions;
    }
    
    @Override
    public String getTag() {
        return SetDeviceBroadcastTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().setBroadcastMode(this.mState, this.mAudioOptions);
        if (Utils.isCoarseLocationPermissionGranted()) {}
        return null;
    }
}
