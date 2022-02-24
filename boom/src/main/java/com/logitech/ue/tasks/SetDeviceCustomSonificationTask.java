// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.device.devicedata.UESoundProfile;
import android.util.Log;
import com.logitech.ue.centurion.UEDeviceManager;

public class SetDeviceCustomSonificationTask extends SetDeviceDataTask
{
    public static final int PROGRESS_BAR_DELAY = 700;
    private static final String TAG;
    protected boolean mOnCustomSonification;
    
    static {
        TAG = SetDeviceCustomSonificationTask.class.getSimpleName();
    }
    
    public SetDeviceCustomSonificationTask(final boolean mOnCustomSonification) {
        this.mOnCustomSonification = mOnCustomSonification;
    }
    
    @Override
    public String getTag() {
        return SetDeviceCustomSonificationTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        connectedDevice.setCustomState(this.mOnCustomSonification);
        if (this.mOnCustomSonification) {
            Log.d(SetDeviceCustomSonificationTask.TAG, "Call emit alert");
            connectedDevice.emitSound(UESoundProfile.POWER_ON);
        }
        return null;
    }
}
