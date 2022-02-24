// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class SetVolumeTask extends SetDeviceDataTask
{
    private static final String TAG;
    protected int mVolumeLevel;
    
    static {
        TAG = SetVolumeTask.class.getSimpleName();
    }
    
    public SetVolumeTask(final int mVolumeLevel) {
        this.mVolumeLevel = mVolumeLevel;
    }
    
    @Override
    public String getTag() {
        return SetVolumeTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().setVolume(this.mVolumeLevel);
        return null;
    }
}
