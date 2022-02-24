// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.App;
import com.logitech.ue.centurion.UEDeviceManager;

public class SetAlarmVolumeTask extends SetDeviceDataTask
{
    private static final String TAG;
    protected int mVolumeLevel;
    
    static {
        TAG = SetAlarmVolumeTask.class.getSimpleName();
    }
    
    public SetAlarmVolumeTask(final int mVolumeLevel) {
        this.mVolumeLevel = mVolumeLevel;
    }
    
    @Override
    public String getTag() {
        return SetAlarmVolumeTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().setAlarmVolume(this.mVolumeLevel, App.getInstance().getBluetoothMacAddress());
        return null;
    }
}
