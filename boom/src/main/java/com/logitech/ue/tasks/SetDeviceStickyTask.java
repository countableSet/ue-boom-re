// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class SetDeviceStickyTask extends SetDeviceDataTask
{
    private static final String TAG;
    protected boolean mIsStickyOn;
    
    static {
        TAG = SetDeviceStickyTask.class.getSimpleName();
    }
    
    public SetDeviceStickyTask(final boolean mIsStickyOn) {
        this.mIsStickyOn = mIsStickyOn;
    }
    
    @Override
    public String getTag() {
        return SetDeviceStickyTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().setStickyTWSOrPartyUpFlag(this.mIsStickyOn);
        return null;
    }
}
