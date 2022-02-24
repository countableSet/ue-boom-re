// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class GetDeviceSerialNumberTask extends GetDeviceDataTask<String>
{
    private static final String TAG;
    
    static {
        TAG = GetDeviceSerialNumberTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return GetDeviceSerialNumberTask.TAG;
    }
    
    @Override
    public String work(final Void... array) throws Exception {
        return UEDeviceManager.getInstance().getConnectedDevice().getSerialNumber();
    }
}
