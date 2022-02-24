// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class GetDeviceBalanceTask extends GetDeviceDataTask<Byte>
{
    private static final String TAG;
    
    static {
        TAG = GetDeviceBalanceTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return GetDeviceBalanceTask.TAG;
    }
    
    @Override
    public Byte work(final Void... array) throws Exception {
        return UEDeviceManager.getInstance().getConnectedDevice().getTWSBalance();
    }
}
