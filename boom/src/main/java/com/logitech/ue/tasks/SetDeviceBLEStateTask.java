// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class SetDeviceBLEStateTask extends SetDeviceDataTask
{
    private static final String TAG;
    protected boolean mBLEState;
    
    static {
        TAG = SetDeviceBLEStateTask.class.getSimpleName();
    }
    
    public SetDeviceBLEStateTask() {
    }
    
    public SetDeviceBLEStateTask(final boolean mbleState) {
        this.mBLEState = mbleState;
    }
    
    @Override
    public String getTag() {
        return SetDeviceBLEStateTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().setBLEState(this.mBLEState);
        return null;
    }
}
