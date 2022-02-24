// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.device.devicedata.UEBroadcastConfiguration;
import com.logitech.ue.centurion.device.UEGenericDevice;
import android.util.Log;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastState;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.FlurryTracker;

public class AutostopBroadcastTask extends SetDeviceDataTask
{
    private static final String TAG;
    
    static {
        TAG = AutostopBroadcastTask.class.getSimpleName();
    }
    
    public AutostopBroadcastTask() {
        super(false);
    }
    
    @Override
    public String getTag() {
        return AutostopBroadcastTask.TAG;
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
            if (broadcastMode.getBroadcastState() == UEBroadcastState.ENABLE_SCANNING_ONLY) {
                Log.d(AutostopBroadcastTask.TAG, "Device is scanning. Auto stop scanning");
                connectedDevice.setBroadcastMode(UEBroadcastState.DISABLE, broadcastMode.getAudioSetting());
            }
            else if (broadcastMode.getBroadcastState() == UEBroadcastState.ENABLE_SCANNING_AND_BROADCASTING) {
                Log.d(AutostopBroadcastTask.TAG, "Broadcast is going on. Do nothing.");
            }
        }
        else {
            Log.d(AutostopBroadcastTask.TAG, "Device doesn't support X-UP. Nothing to stop");
        }
        return null;
    }
}
