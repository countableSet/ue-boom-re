// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.utils.MAC;

public class GetConnectedDeviceNameTask extends GetDeviceDataTask<String>
{
    private static final String TAG;
    final MAC mDeviceMACAddress;
    
    static {
        TAG = GetConnectedDeviceNameTask.class.getSimpleName();
    }
    
    public GetConnectedDeviceNameTask(final MAC mDeviceMACAddress) {
        this.mDeviceMACAddress = mDeviceMACAddress;
    }
    
    @Override
    public String getTag() {
        return GetConnectedDeviceNameTask.TAG;
    }
    
    @Override
    public String work(final Void... array) throws Exception {
        return UEDeviceManager.getInstance().getConnectedDevice().getConnectedDeviceNameRequest(this.mDeviceMACAddress);
    }
}
