// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue;

import android.util.Log;
import com.logitech.ue.centurion.device.devicedata.UEPartition;
import java.util.Arrays;
import android.support.annotation.NonNull;
import com.logitech.ue.centurion.exceptions.UEConnectionException;
import com.logitech.ue.centurion.exceptions.UEOperationException;
import com.logitech.ue.centurion.connection.IUEConnector;
import com.logitech.ue.centurion.connection.UEDeviceConnector;
import com.logitech.ue.centurion.utils.MAC;
import com.logitech.ue.centurion.device.devicedata.UEDeviceType;
import com.logitech.ue.centurion.device.UESPPDevice;

public class UEKoraDevice extends UESPPDevice
{
    @Deprecated
    private static final byte[] KORA_ADK30_PARTITION_DESCRIPTOR;
    public static final String TAG;
    private UEDeviceType mUEDeviceType;
    
    static {
        TAG = UEKoraDevice.class.getSimpleName();
        KORA_ADK30_PARTITION_DESCRIPTOR = new byte[] { 24, 0, 0, 0, 5, 7, -8, 0, 3, 2, 108, 0, 1, 2, 108, 0, 1, 2, 108, 0, 2, 0, -96, 0, 4 };
    }
    
    public UEKoraDevice(final MAC mac, final UEDeviceConnector ueDeviceConnector) {
        super(mac, ueDeviceConnector);
    }
    
    @Override
    public void dropFirstLevelCache() {
        super.dropFirstLevelCache();
    }
    
    @Override
    public void dropSecondLevelCache() {
        super.dropSecondLevelCache();
    }
    
    @Override
    public void dropThirdLevelCache() {
        super.dropThirdLevelCache();
    }
    
    @NonNull
    @Override
    public UEDeviceType getDeviceType() throws UEOperationException, UEConnectionException {
        if (this.mUEDeviceType == null) {
            this.mUEDeviceType = super.getDeviceType();
        }
        return this.mUEDeviceType;
    }
    
    @Override
    public boolean isPartitionValidForLanguage(@NonNull final byte[] array) {
        boolean b = true;
        final UEPartition uePartition = new UEPartition(Arrays.copyOfRange(array, 9, 13));
        final UEPartition uePartition2 = new UEPartition(Arrays.copyOfRange(array, 13, 17));
        if (1 == uePartition.getType() && 1 == uePartition2.getType()) {
            Log.i(UEKoraDevice.TAG, "Both partition 1 and 2 are of correct type for language downloading");
        }
        else {
            Log.e(UEKoraDevice.TAG, "Partition type are " + uePartition.getType() + " and " + uePartition2.getType() + ", not correct for dynamic language downloading.");
            b = false;
        }
        return b;
    }
}
