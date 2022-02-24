// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.device.devicedata.UELanguage;

public class SetLanguageTask extends SetDeviceDataTask
{
    private static final String TAG;
    protected UELanguage mDeviceLanguage;
    protected boolean mWithAnnouncement;
    
    static {
        TAG = SetLanguageTask.class.getSimpleName();
    }
    
    public SetLanguageTask(final UELanguage ueLanguage) {
        this(ueLanguage, true);
    }
    
    public SetLanguageTask(final UELanguage mDeviceLanguage, final boolean mWithAnnouncement) {
        this.mDeviceLanguage = mDeviceLanguage;
        this.mWithAnnouncement = mWithAnnouncement;
    }
    
    @Override
    public String getTag() {
        return SetLanguageTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().setLanguage(this.mDeviceLanguage);
        if (this.mWithAnnouncement) {
            UEDeviceManager.getInstance().getConnectedDevice().announceBatteryLevel();
        }
        return null;
    }
}
