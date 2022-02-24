// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.other.DeviceInfo;
import com.logitech.ue.centurion.UEDiscoveryManager;
import android.util.Log;
import com.logitech.ue.App;
import com.logitech.ue.Utils;
import com.logitech.ue.UserPreferences;
import com.logitech.ue.centurion.UEDeviceManager;

public class CheckForDeviceTask extends SafeTask<Void, Void, Void>
{
    private static final String TAG;
    
    static {
        TAG = CheckForDeviceTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return CheckForDeviceTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        if (UEDeviceManager.getInstance().isReady() && UEDeviceManager.getInstance().getConnectedDevice() == null) {
            UEDeviceManager.getInstance().checkForDevice();
            final DeviceInfo lastSeenDevice = UserPreferences.getInstance().getLastSeenDevice();
            if (Utils.isCoarseLocationPermissionGranted() && UEDeviceManager.getInstance().getConnectedDevice() == null && lastSeenDevice != null && App.getInstance().isAppActive()) {
                Log.d(CheckForDeviceTask.TAG, "Nothing is connected. Start BLE searching.");
                UEDiscoveryManager.getInstance().startBLESearch(lastSeenDevice.address.toString());
            }
        }
        return null;
    }
}
