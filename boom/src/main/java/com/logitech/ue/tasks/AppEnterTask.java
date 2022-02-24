// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.device.devicedata.UELanguage;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.FlurryTracker;
import com.logitech.ue.UserPreferences;
import com.logitech.ue.centurion.device.devicedata.UESonificationProfile;
import com.logitech.ue.App;
import com.logitech.ue.centurion.UEDeviceManager;

public class AppEnterTask extends SafeTask<Void, Void, Void>
{
    private static final String TAG;
    
    static {
        TAG = AppEnterTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return AppEnterTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        if (!this.isCancelled()) {
            final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
            if (connectedDevice != null && connectedDevice.getDeviceConnectionStatus().isBtClassicConnectedState()) {
                final UELanguage language = connectedDevice.getLanguage();
                if (!this.isCancelled()) {
                    final UESonificationProfile sonificationProfile = connectedDevice.getSonificationProfile();
                    if (!this.isCancelled()) {
                        final boolean stickyTWSOrPartyUpFlag = connectedDevice.getStickyTWSOrPartyUpFlag();
                        if (!this.isCancelled()) {
                            final String firmwareVersion = connectedDevice.getFirmwareVersion();
                            if (!this.isCancelled()) {
                                FlurryTracker.logAppActivated(App.getInstance().getLanguageTechnicalName(language), sonificationProfile != UESonificationProfile.NONE, stickyTWSOrPartyUpFlag, UserPreferences.getInstance().isNotificationsEnable(), firmwareVersion, "5.0.166");
                            }
                        }
                    }
                }
            }
            else {
                FlurryTracker.logAppActivated("", null, null, UserPreferences.getInstance().isNotificationsEnable(), "", "5.0.166");
            }
        }
        return null;
    }
}
