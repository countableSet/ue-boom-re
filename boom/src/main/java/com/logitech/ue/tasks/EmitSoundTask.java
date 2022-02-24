// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.device.devicedata.UESoundProfile;

public class EmitSoundTask extends SetDeviceDataTask
{
    private static final String TAG;
    private UESoundProfile mProfile;
    
    static {
        TAG = EmitSoundTask.class.getSimpleName();
    }
    
    public EmitSoundTask(final UESoundProfile mProfile) {
        this.mProfile = mProfile;
    }
    
    @Override
    public String getTag() {
        return EmitSoundTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().emitSound(this.mProfile);
        return null;
    }
}
