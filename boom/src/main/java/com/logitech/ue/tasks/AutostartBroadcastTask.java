// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.device.devicedata.UEBroadcastConfiguration;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.Utils;
import android.util.Log;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastState;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.FlurryTracker;

public class AutostartBroadcastTask extends SetDeviceDataTask
{
    private static final String TAG;
    
    static {
        TAG = AutostartBroadcastTask.class.getSimpleName();
    }
    
    public AutostartBroadcastTask() {
        super(false);
    }
    
    @Override
    public String getTag() {
        return AutostartBroadcastTask.TAG;
    }
    
    @Override
    public void onError(final Exception ex) {
        super.onError(ex);
        FlurryTracker.logXUPError("Start fail");
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        if (connectedDevice.isBroadcastModeSupported()) {
            final UEBroadcastConfiguration broadcastMode = connectedDevice.getBroadcastMode();
            if (broadcastMode.getBroadcastState() == UEBroadcastState.DISABLE) {
                Log.d(AutostartBroadcastTask.TAG, "Device is not scanning. Auto start scanning");
                connectedDevice.setBroadcastMode(UEBroadcastState.ENABLE_SCANNING_ONLY, broadcastMode.getAudioSetting());
            }
            else if (broadcastMode.getBroadcastState() == UEBroadcastState.ENABLE_BROADCASTING_ONLY) {
                Log.d(AutostartBroadcastTask.TAG, "Device is not scanning. Auto start broadcast and scanning");
                connectedDevice.setBroadcastMode(UEBroadcastState.ENABLE_SCANNING_AND_BROADCASTING, broadcastMode.getAudioSetting());
            }
            else {
                Log.d(AutostartBroadcastTask.TAG, "Device doesn't support X-UP. Nothing to start");
            }
            if (Utils.isCoarseLocationPermissionGranted()) {}
        }
        return null;
    }
}
