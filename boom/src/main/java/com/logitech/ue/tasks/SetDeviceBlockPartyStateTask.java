// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class SetDeviceBlockPartyStateTask extends SetDeviceDataTask
{
    private static final String TAG;
    protected boolean mOn;
    
    static {
        TAG = SetDeviceBlockPartyStateTask.class.getSimpleName();
    }
    
    public SetDeviceBlockPartyStateTask(final boolean mOn) {
        this.mOn = mOn;
    }
    
    @Override
    public String getTag() {
        return SetDeviceBlockPartyStateTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().setBlockPartyState(this.mOn);
        return null;
    }
}
