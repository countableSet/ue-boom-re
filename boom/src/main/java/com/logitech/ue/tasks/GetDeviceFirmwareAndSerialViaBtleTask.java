// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.devicedata.DeviceFirmwareSerialInfo;

public class GetDeviceFirmwareAndSerialViaBtleTask extends GetDeviceDataTask<DeviceFirmwareSerialInfo>
{
    private final String TAG;
    private DeviceFirmwareSerialInfo mInfo;
    
    public GetDeviceFirmwareAndSerialViaBtleTask() {
        this.TAG = this.getClass().getSimpleName();
        this.mInfo = new DeviceFirmwareSerialInfo();
    }
    
    @Override
    public String getTag() {
        return this.TAG;
    }
    
    @Override
    public DeviceFirmwareSerialInfo work(final Void... array) throws Exception {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        this.mInfo.setFirmwareVersion(connectedDevice.getFirmwareVersion());
        this.mInfo.setSerialNumber(connectedDevice.getSerialNumber());
        return this.mInfo;
    }
}
