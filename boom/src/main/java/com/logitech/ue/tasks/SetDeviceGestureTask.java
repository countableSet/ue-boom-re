// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class SetDeviceGestureTask extends SetDeviceDataTask
{
    private static final String TAG;
    protected Boolean mIsOn;
    
    static {
        TAG = SetDeviceGestureTask.class.getSimpleName();
    }
    
    public SetDeviceGestureTask(final Boolean mIsOn) {
        this.mIsOn = mIsOn;
    }
    
    @Override
    public String getTag() {
        return SetDeviceGestureTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().setGestureState(this.mIsOn);
        return null;
    }
}
