// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class SetDevicePromiscuousModeTask extends SetDeviceDataTask
{
    private static final String TAG;
    protected Boolean mIsOn;
    
    static {
        TAG = SetDevicePromiscuousModeTask.class.getSimpleName();
    }
    
    public SetDevicePromiscuousModeTask(final Boolean mIsOn) {
        this.mIsOn = mIsOn;
    }
    
    @Override
    public String getTag() {
        return SetDevicePromiscuousModeTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().setXUPPromiscuousModel(this.mIsOn);
        return null;
    }
}
