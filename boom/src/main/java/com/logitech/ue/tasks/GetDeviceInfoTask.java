// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.UEDeviceManager;
import java.util.concurrent.CancellationException;
import com.logitech.ue.other.DeviceInfo;

public class GetDeviceInfoTask extends GetDeviceDataTask<DeviceInfo>
{
    private static final String TAG = "GetDeviceInfoTask";
    
    public GetDeviceInfoTask(final boolean b) {
        super(b);
    }
    
    private void checkCancellation() {
        if (this.isCancelled()) {
            throw new CancellationException();
        }
    }
    
    @Override
    public String getTag() {
        return "GetDeviceInfoTask";
    }
    
    @Override
    public DeviceInfo work(final Void... array) throws Exception {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        final DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.color = connectedDevice.getDeviceColor();
        this.checkCancellation();
        deviceInfo.name = connectedDevice.getBluetoothName();
        this.checkCancellation();
        deviceInfo.address = connectedDevice.getAddress();
        this.checkCancellation();
        deviceInfo.isBlockPartySupported = connectedDevice.isBlockPartySupported();
        this.checkCancellation();
        deviceInfo.isXupSupported = connectedDevice.isBroadcastModeSupported();
        return deviceInfo;
    }
}
