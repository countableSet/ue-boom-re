// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class GetPhoneBluetoothAddressTask extends GetDeviceDataTask<byte[]>
{
    private static final String TAG;
    
    static {
        TAG = GetPhoneBluetoothAddressTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return GetPhoneBluetoothAddressTask.TAG;
    }
    
    @Override
    public byte[] work(final Void... array) throws Exception {
        return UEDeviceManager.getInstance().getConnectedDevice().getPhoneBluetoothAddress();
    }
}
