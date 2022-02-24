// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import android.support.annotation.NonNull;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStreamingStatus;
import com.logitech.ue.centurion.device.UEGenericDevice;
import android.os.SystemClock;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.device.devicedata.UEHardwareInfo;

public class GetDeviceHardwareInfoTask extends GetDeviceDataTask<UEHardwareInfo>
{
    public static final int DEFAULT_ATTEMPTS_COUNT = 7;
    private static final String TAG;
    private int mAttempts;
    
    static {
        TAG = GetDeviceHardwareInfoTask.class.getSimpleName();
    }
    
    public GetDeviceHardwareInfoTask() {
        this(7);
    }
    
    public GetDeviceHardwareInfoTask(final int n) {
        this(n, true);
    }
    
    public GetDeviceHardwareInfoTask(final int mAttempts, final boolean b) {
        super(b);
        this.mAttempts = 7;
        if (mAttempts <= 0) {
            throw new IllegalArgumentException("Attempts count can't be 0 or less");
        }
        this.mAttempts = mAttempts;
    }
    
    public GetDeviceHardwareInfoTask(final boolean b) {
        this(7, b);
    }
    
    @Override
    public String getTag() {
        return GetDeviceHardwareInfoTask.TAG;
    }
    
    @NonNull
    @Override
    public UEHardwareInfo work(final Void... array) throws Exception {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        final UEDeviceStreamingStatus deviceStreamingStatus = connectedDevice.getDeviceStreamingStatus();
        while (this.mAttempts > 1) {
            final UEHardwareInfo hardwareInfo = connectedDevice.getHardwareInfo();
            Label_0060: {
                if (hardwareInfo.getSecondaryDeviceColour() != 0) {
                    UEHardwareInfo hardwareInfo2 = hardwareInfo;
                    if (deviceStreamingStatus.isDevicePairedStatus()) {
                        if (hardwareInfo.getSecondaryDeviceColour() == 255) {
                            break Label_0060;
                        }
                        hardwareInfo2 = hardwareInfo;
                    }
                    return hardwareInfo2;
                }
            }
            SystemClock.sleep(500L);
            --this.mAttempts;
        }
        return connectedDevice.getHardwareInfo();
    }
}
