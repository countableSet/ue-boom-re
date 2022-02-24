// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import com.logitech.ue.centurion.UEDeviceManager;
import android.util.Log;

public class VolumeUpdater extends ValueUpdater<Integer>
{
    public static final String TAG;
    
    static {
        TAG = VolumeUpdater.class.getSimpleName();
    }
    
    @Override
    protected void updateValue(final Integer obj) throws Exception {
        Log.d(VolumeUpdater.TAG, "Update volume to " + obj);
        UEDeviceManager.getInstance().getConnectedDevice().setVolume(obj);
    }
}
