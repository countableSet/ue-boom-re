// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.exceptions.UEUnrecognisedCommandException;
import android.util.Log;
import com.logitech.ue.centurion.UEDeviceManager;

public class AdjustDeviceVolumeTask extends SetDeviceDataTask
{
    private static final String TAG;
    protected boolean mChangeMasterVolume;
    protected boolean mIncreaseVolume;
    
    static {
        TAG = AdjustDeviceVolumeTask.class.getSimpleName();
    }
    
    public AdjustDeviceVolumeTask(final boolean mChangeMasterVolume, final boolean mIncreaseVolume) {
        this.mChangeMasterVolume = mChangeMasterVolume;
        this.mIncreaseVolume = mIncreaseVolume;
    }
    
    @Override
    public String getTag() {
        return AdjustDeviceVolumeTask.TAG;
    }
    
    @Override
    public Void work(Void... connectedDevice) throws Exception {
        int n = 0;
        connectedDevice = (Void[])(Object)UEDeviceManager.getInstance().getConnectedDevice();
        try {
            final boolean b = !this.mChangeMasterVolume;
            if (!this.mIncreaseVolume) {
                n = 1;
            }
            ((UEGenericDevice)(Object)connectedDevice).adjustVolume(b, n);
            return null;
        }
        catch (UEUnrecognisedCommandException ex) {
            Log.d(AdjustDeviceVolumeTask.TAG, "Adjust volume not supported", (Throwable)ex);
            ((UEGenericDevice)(Object)connectedDevice).setVolume(((UEGenericDevice)(Object)connectedDevice).getVolume() + 1);
            return null;
        }
    }
}
