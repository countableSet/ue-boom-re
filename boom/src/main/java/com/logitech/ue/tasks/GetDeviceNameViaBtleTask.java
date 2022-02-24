// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class GetDeviceNameViaBtleTask extends GetDeviceDataTask<String>
{
    private final String TAG;
    
    public GetDeviceNameViaBtleTask() {
        this.TAG = this.getClass().getSimpleName();
    }
    
    @Override
    public String getTag() {
        return this.TAG;
    }
    
    @Override
    public String work(final Void... array) throws Exception {
        return UEDeviceManager.getInstance().getConnectedDevice().getBluetoothName();
    }
}
