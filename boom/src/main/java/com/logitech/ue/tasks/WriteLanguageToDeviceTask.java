// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.UEDeviceManager;
import java.io.InputStream;
import java.io.IOException;
import com.logitech.ue.App;
import com.logitech.ue.centurion.connection.UEDeviceConnector;
import com.logitech.ue.centurion.device.devicedata.UEOTAStatus;
import android.util.Log;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.device.devicedata.UELanguage;

public class WriteLanguageToDeviceTask extends SafeTask<UELanguage, Void, Void>
{
    public static final byte PARTITION_MASK_1 = 2;
    public static final byte PARTITION_MASK_2 = 4;
    public static final byte PARTITION_MASK_LANGUAGES = 6;
    public static final String TAG;
    private int[] languageFiles;
    private int partitionMask;
    
    static {
        TAG = WriteLanguageToDeviceTask.class.getSimpleName();
    }
    
    public WriteLanguageToDeviceTask() {
        this.languageFiles = new int[] { 2131099752, 2131099753, 2131099759, 2131099754, 2131099757, 2131099758, 2131099755, 2131099756, 2131099751 };
    }
    
    private void startLanguageDownload(final UEGenericDevice ueGenericDevice, final UELanguage ueLanguage, final byte b) throws Exception {
        Log.d(WriteLanguageToDeviceTask.TAG, "Attempting to enter OTA mode");
        ueGenericDevice.setOTAStatus(UEOTAStatus.START);
        ((UEDeviceConnector)ueGenericDevice.getConnector()).switchMode(UEDeviceConnector.Mode.OTA);
        Log.d(WriteLanguageToDeviceTask.TAG, "Waiting 2 sec for OTA switch");
        Thread.sleep(2000L);
        Log.d(WriteLanguageToDeviceTask.TAG, String.format("Erase partition %d", b));
        ueGenericDevice.erasePartition(b);
        Log.d(WriteLanguageToDeviceTask.TAG, "SQIF erase successful");
        final int i = this.languageFiles[ueLanguage.getCode()];
        Log.d(WriteLanguageToDeviceTask.TAG, "Language resource " + i);
        final InputStream openRawResource = App.getInstance().getResources().openRawResource(i);
        final byte[] b2 = new byte[1024];
        int j = 0;
        while (true) {
            Label_0147: {
                if (openRawResource != null) {
                    break Label_0147;
                }
            Label_0277:
                while (true) {
                    break Label_0277;
                    while (true) {
                        while (true) {
                            Label_0492: {
                                try {
                                    final int read = openRawResource.read(b2);
                                    if (read > 0) {
                                        Log.d(WriteLanguageToDeviceTask.TAG, "Writing " + read + " bytes with offset " + j + " of language data");
                                        ueGenericDevice.writeSQIF(b2);
                                        j += read;
                                        break;
                                    }
                                    if (read == -1) {
                                        Log.d(WriteLanguageToDeviceTask.TAG, "Language write complete");
                                    }
                                    else {
                                        Log.d(WriteLanguageToDeviceTask.TAG, "Unexpected count " + read);
                                    }
                                    Log.d(WriteLanguageToDeviceTask.TAG, "Finish data write");
                                    ueGenericDevice.validateSQIF();
                                    Log.d(WriteLanguageToDeviceTask.TAG, "Waiting 1 sec between last data written and cancel OTA");
                                    Thread.sleep(1000L);
                                    Log.d(WriteLanguageToDeviceTask.TAG, "Leaving OTA mode");
                                    ueGenericDevice.cancelOTA();
                                    ((UEDeviceConnector)ueGenericDevice.getConnector()).switchMode(UEDeviceConnector.Mode.Centurion);
                                    Log.d(WriteLanguageToDeviceTask.TAG, "Mounting language partition " + b);
                                    ueGenericDevice.mountPartition(b, 1);
                                    if (b == 1) {
                                        final int n = (this.partitionMask & 0xFFFFFFF9) | 0x2;
                                        Log.d(WriteLanguageToDeviceTask.TAG, "Selecting language " + ueLanguage);
                                        ueGenericDevice.setLanguage(ueLanguage);
                                        Log.d(WriteLanguageToDeviceTask.TAG, String.format("Setting partition state to 0x%04X", n));
                                        ueGenericDevice.setPartitionState(n);
                                        return;
                                    }
                                    break Label_0492;
                                }
                                catch (IOException ex) {
                                    ex.printStackTrace();
                                    continue Label_0277;
                                }
                                continue Label_0277;
                            }
                            final int n = (this.partitionMask & 0xFFFFFFF9) | 0x4;
                            continue;
                        }
                    }
                    break;
                }
            }
            if (!this.isCancelled()) {
                continue;
            }
            break;
        }
        Log.d(WriteLanguageToDeviceTask.TAG, "Leaving OTA mode");
        ueGenericDevice.cancelOTA();
        ((UEDeviceConnector)ueGenericDevice.getConnector()).switchMode(UEDeviceConnector.Mode.Centurion);
    }
    
    public void downloadLanguage(final UEGenericDevice ueGenericDevice, final UELanguage ueLanguage) throws Exception {
        this.partitionMask = ueGenericDevice.getPartitionState();
        if (this.partitionMask != 0) {
            Log.d(WriteLanguageToDeviceTask.TAG, "Current partition mask " + this.partitionMask);
            final int n = this.partitionMask & 0x6;
            byte b2;
            if (n == 6) {
                Log.w(WriteLanguageToDeviceTask.TAG, "Both language partitions occupied, attempting to unmount");
                ueGenericDevice.setPartitionState(this.partitionMask & 0xFFFFFFF9);
                final byte b = 1;
                Log.d(WriteLanguageToDeviceTask.TAG, "Partitions unmounted, initiating download");
                b2 = b;
            }
            else if (n == 2) {
                Log.d(WriteLanguageToDeviceTask.TAG, "Initiating download to second language partition");
                b2 = 2;
            }
            else {
                Log.d(WriteLanguageToDeviceTask.TAG, "Initiating download to first language partition");
                b2 = 1;
            }
            this.startLanguageDownload(ueGenericDevice, ueLanguage, b2);
        }
    }
    
    @Override
    public String getTag() {
        return WriteLanguageToDeviceTask.TAG;
    }
    
    @Override
    public Void work(final UELanguage... array) throws Exception {
        this.downloadLanguage(UEDeviceManager.getInstance().getConnectedDevice(), array[0]);
        return null;
    }
}
