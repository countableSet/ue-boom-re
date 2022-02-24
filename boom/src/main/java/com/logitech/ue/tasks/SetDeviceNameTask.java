// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;

public class SetDeviceNameTask extends SetDeviceDataTask
{
    private static final String TAG;
    protected String mDeviceNewName;
    
    static {
        TAG = SetDeviceNameTask.class.getSimpleName();
    }
    
    public SetDeviceNameTask(final String mDeviceNewName) {
        this.mDeviceNewName = mDeviceNewName;
    }
    
    @Override
    public String getTag() {
        return SetDeviceNameTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().setBluetoothName(this.mDeviceNewName);
        return null;
    }
}
