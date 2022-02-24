// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.exceptions.UEUnrecognisedCommandException;
import android.util.Log;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.devicedata.DeviceFirmwareSerialInfo;

public class GetDeviceFirmwareAndSerialTask extends GetDeviceDataTask<DeviceFirmwareSerialInfo>
{
    private static final String TAG;
    
    static {
        TAG = GetDeviceFirmwareAndSerialTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return GetDeviceFirmwareAndSerialTask.TAG;
    }
    
    @Override
    public DeviceFirmwareSerialInfo work(Void... array) throws Exception {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        array = (Void[])(Object)new DeviceFirmwareSerialInfo();
        ((DeviceFirmwareSerialInfo)(Object)array).setFirmwareVersion(connectedDevice.getFirmwareVersion());
        try {
            ((DeviceFirmwareSerialInfo)(Object)array).setSerialNumber(connectedDevice.getSerialNumber());
            return (DeviceFirmwareSerialInfo)(Object)array;
        }
        catch (UEUnrecognisedCommandException ex) {
            Log.d(GetDeviceFirmwareAndSerialTask.TAG, "Serial not supported");
            ((DeviceFirmwareSerialInfo)(Object)array).setSerialNumber("");
            return (DeviceFirmwareSerialInfo)(Object)array;
        }
    }
}
