// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class GetDeviceNameTask extends GetDeviceDataTask<String>
{
    private static final String TAG;
    
    static {
        TAG = GetDeviceNameTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return GetDeviceNameTask.TAG;
    }
    
    @Override
    public String work(final Void... array) throws Exception {
        return UEDeviceManager.getInstance().getConnectedDevice().getBluetoothName();
    }
}
