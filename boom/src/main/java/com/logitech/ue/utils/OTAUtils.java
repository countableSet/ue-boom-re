// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.utils;

import java.util.Arrays;
import com.logitech.ue.centurion.exceptions.UEConnectionException;
import com.logitech.ue.centurion.exceptions.UEOperationException;
import android.os.SystemClock;
import com.logitech.ue.centurion.connection.UEDeviceConnector;
import com.logitech.ue.centurion.device.devicedata.UEOTAStatus;
import android.util.Log;
import com.logitech.ue.centurion.device.UEGenericDevice;

public final class OTAUtils
{
    public static final int WRITE_BUFFER_SIZE = 1024;
    
    public static void enterOTAMode(final UEGenericDevice ueGenericDevice, final String s) throws UEOperationException, UEConnectionException {
        Log.d(s, "Attempting to enter OTA mode");
        ueGenericDevice.setOTAStatus(UEOTAStatus.START);
        ((UEDeviceConnector)ueGenericDevice.getConnector()).switchMode(UEDeviceConnector.Mode.OTA);
        Log.d(s, "Waiting 1 sec for OTA switch");
        SystemClock.sleep(1000L);
    }
    
    public static void erasePartition(final UEGenericDevice ueGenericDevice, final int i, final String s) throws UEOperationException, UEConnectionException {
        Log.d(s, String.format("Erase partition %d", i));
        ueGenericDevice.erasePartition((byte)i);
    }
    
    public static void flashDFU(final UEGenericDevice ueGenericDevice, final byte[] a, final String s) throws UEOperationException, UEConnectionException {
        Log.d(s, "Writing dfu size " + Arrays.toString(a) + " bytes");
        int i = 0;
        final byte[] a2 = new byte[1024];
        while (i < a.length) {
            Arrays.fill(a2, (byte)0);
            int n;
            if (a.length - i < 1024) {
                n = a.length - i;
            }
            else {
                n = 1024;
            }
            System.arraycopy(a, i, a2, 0, n);
            Log.d(s, String.format("Sending bytes with offset %d of %d", i, a.length));
            ueGenericDevice.writeSQIF(a2);
            i += 1024;
        }
    }
}
