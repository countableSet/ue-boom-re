// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class GetDeviceCustomEQTask extends GetDeviceDataTask<byte[]>
{
    private static final String TAG;
    
    static {
        TAG = GetDeviceCustomEQTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return GetDeviceCustomEQTask.TAG;
    }
    
    @Override
    public byte[] work(final Void... array) throws Exception {
        return UEDeviceManager.getInstance().getConnectedDevice().getCustomEQ();
    }
}
