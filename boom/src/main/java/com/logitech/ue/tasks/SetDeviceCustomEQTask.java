// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class SetDeviceCustomEQTask extends SetDeviceDataTask
{
    private static final String TAG;
    protected byte[] mCustomEQ;
    
    static {
        TAG = SetDeviceCustomEQTask.class.getSimpleName();
    }
    
    public SetDeviceCustomEQTask(final byte[] mCustomEQ) {
        this.mCustomEQ = mCustomEQ;
    }
    
    @Override
    public String getTag() {
        return SetDeviceCustomEQTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().setCustomEQ(this.mCustomEQ);
        return null;
    }
}
