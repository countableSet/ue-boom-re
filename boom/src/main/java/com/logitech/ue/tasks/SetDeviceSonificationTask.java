// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.device.devicedata.UESoundProfile;
import android.util.Log;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.device.devicedata.UESonificationProfile;

public class SetDeviceSonificationTask extends SetDeviceDataTask
{
    private static final String TAG;
    protected UESonificationProfile mSonification;
    
    static {
        TAG = SetDeviceSonificationTask.class.getSimpleName();
    }
    
    public SetDeviceSonificationTask(final UESonificationProfile mSonification) {
        this.mSonification = mSonification;
    }
    
    @Override
    public String getTag() {
        return SetDeviceSonificationTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        connectedDevice.setSonificationProfile(this.mSonification);
        if (this.mSonification != UESonificationProfile.NONE) {
            Log.d(SetDeviceSonificationTask.TAG, "Call emit alert");
            connectedDevice.emitSound(UESoundProfile.POWER_ON);
        }
        return null;
    }
}
