// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;
import java.util.Arrays;
import java.util.concurrent.CancellationException;
import com.logitech.ue.centurion.exceptions.UEOperationException;
import com.logitech.ue.centurion.connection.UEDeviceConnector;
import com.logitech.ue.centurion.device.devicedata.UEOTAStatus;
import android.util.Log;
import com.logitech.ue.centurion.device.UEGenericDevice;

public class WriteCustomSoundsToDeviceTask extends SafeTask<byte[], Void, Void>
{
    public static final byte PARTITION_MASK_3 = 8;
    public static final String TAG;
    public static final int WRITE_BUFFER_SIZE = 1024;
    private int partitionMask;
    
    static {
        TAG = WriteCustomSoundsToDeviceTask.class.getSimpleName();
    }
    
    private void startFlashing(final UEGenericDevice ueGenericDevice, final byte[] array, final byte b) throws Exception {
        Log.d(WriteCustomSoundsToDeviceTask.TAG, "*** Current OTA status is " + ueGenericDevice.getOTAStatus().name());
        Log.d(WriteCustomSoundsToDeviceTask.TAG, "Attempting to enter OTA mode");
        ueGenericDevice.setOTAStatus(UEOTAStatus.START);
        ((UEDeviceConnector)ueGenericDevice.getConnector()).switchMode(UEDeviceConnector.Mode.OTA);
        int i = 0;
        while (i == 0) {
            Log.d(WriteCustomSoundsToDeviceTask.TAG, "Waiting 1 second for OTA switch");
            Thread.sleep(1000L);
            try {
                ueGenericDevice.NOP();
                i = 1;
            }
            catch (UEOperationException ex) {
                Log.w(WriteCustomSoundsToDeviceTask.TAG, "Still NOT in OTA mode yet");
            }
        }
        Log.d(WriteCustomSoundsToDeviceTask.TAG, String.format("Erase partition %d", b));
        ueGenericDevice.erasePartition(b);
        Log.d(WriteCustomSoundsToDeviceTask.TAG, "Partition erase successful");
        final byte[] a = new byte[1024];
        for (int j = 0; j < array.length; j += 1024) {
            if (this.isCancelled()) {
                throw new CancellationException();
            }
            Arrays.fill(a, (byte)0);
            int n;
            if (array.length - j < 1024) {
                n = array.length - j;
            }
            else {
                n = 1024;
            }
            System.arraycopy(array, j, a, 0, n);
            Log.d(WriteCustomSoundsToDeviceTask.TAG, String.format("Sending bytes with offset %d of %d", j, array.length));
            ueGenericDevice.writeSQIF(a);
        }
        Log.d(WriteCustomSoundsToDeviceTask.TAG, "Finish data write");
        ueGenericDevice.validateSQIF();
        Log.d(WriteCustomSoundsToDeviceTask.TAG, "Waiting 1 sec between last data written and cancel OTA");
        Thread.sleep(1000L);
        Log.d(WriteCustomSoundsToDeviceTask.TAG, "Leaving OTA mode");
        ueGenericDevice.cancelOTA();
        ((UEDeviceConnector)ueGenericDevice.getConnector()).switchMode(UEDeviceConnector.Mode.Centurion);
        Log.d(WriteCustomSoundsToDeviceTask.TAG, "Mounting partition " + b);
        ueGenericDevice.mountPartition(b, 1);
        final int n2 = this.partitionMask | 0x8;
        Log.d(WriteCustomSoundsToDeviceTask.TAG, String.format("Setting partition state to 0x%04X", n2));
        ueGenericDevice.setPartitionState(n2);
    }
    
    @Override
    public String getTag() {
        return WriteCustomSoundsToDeviceTask.TAG;
    }
    
    public void startOTAFlashing(final UEGenericDevice ueGenericDevice, final byte[] array) throws Exception {
        this.partitionMask = ueGenericDevice.getPartitionState();
        this.startFlashing(ueGenericDevice, array, (byte)3);
    }
    
    @Override
    public Void work(final byte[]... array) throws Exception {
        this.startOTAFlashing(UEDeviceManager.getInstance().getConnectedDevice(), array[0]);
        return null;
    }
}
