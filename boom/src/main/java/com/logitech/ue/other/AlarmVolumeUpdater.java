// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import com.logitech.ue.App;
import com.logitech.ue.centurion.UEDeviceManager;

public class AlarmVolumeUpdater extends ValueUpdater<Integer>
{
    @Override
    protected void updateValue(final Integer n) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().setAlarmVolume(n, App.getInstance().getBluetoothMacAddress());
    }
}
