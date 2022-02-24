// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class GetDeviceColorTask extends GetDeviceDataTask<Integer>
{
    private static final String TAG;
    
    static {
        TAG = GetDeviceColorTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return GetDeviceColorTask.TAG;
    }
    
    @Override
    public Integer work(final Void... array) throws Exception {
        return UEDeviceManager.getInstance().getConnectedDevice().getDeviceColor();
    }
}
