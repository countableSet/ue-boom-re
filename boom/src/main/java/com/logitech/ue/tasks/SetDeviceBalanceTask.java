// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class SetDeviceBalanceTask extends SetDeviceDataTask
{
    private static final String TAG;
    protected byte mBalance;
    
    static {
        TAG = SetDeviceBalanceTask.class.getSimpleName();
    }
    
    public SetDeviceBalanceTask(final byte b) {
        this.mBalance = b;
    }
    
    @Override
    public String getTag() {
        return SetDeviceBalanceTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().setTWSBalance(this.mBalance);
        return null;
    }
}
