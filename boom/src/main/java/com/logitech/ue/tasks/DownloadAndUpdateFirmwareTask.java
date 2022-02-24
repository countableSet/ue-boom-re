// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import java.util.Iterator;
import com.logitech.ue.firmware.UpdateStepInfo;
import com.logitech.ue.firmware.FirmwareManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import android.util.Log;
import android.content.Context;
import com.logitech.ue.firmware.UpdateInstruction;

public class DownloadAndUpdateFirmwareTask extends UpdateFirmwareTask
{
    public static final int PROGRESS_STAGE_FIRMWARE_DOWNLOAD = 100;
    private static final String TAG;
    
    static {
        TAG = DownloadAndUpdateFirmwareTask.class.getSimpleName();
    }
    
    public DownloadAndUpdateFirmwareTask(final UpdateInstruction updateInstruction, final String s, final Context context) {
        super(updateInstruction, s, context);
    }
    
    protected byte[] downloadDFU(final String str) throws IOException {
        Log.d(DownloadAndUpdateFirmwareTask.TAG, "Start firmware download. URL:" + str);
        final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(str).openConnection();
        httpURLConnection.setInstanceFollowRedirects(true);
        final InputStream inputStream = httpURLConnection.getInputStream();
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final byte[] array = new byte[16384];
        final int contentLength = httpURLConnection.getContentLength();
        Log.d(DownloadAndUpdateFirmwareTask.TAG, String.format("Firmware download size %d KB. URL%s:", contentLength / 1024, str));
        while (!this.isCancelled()) {
            final int read = inputStream.read(array, 0, 16384);
            if (read == -1) {
                break;
            }
            Log.d(DownloadAndUpdateFirmwareTask.TAG, String.format("Firmware download load %d of %d KB", read / 1024, contentLength / 1024));
            byteArrayOutputStream.write(array, 0, read);
            this.increaseProgress(1.0f * read / contentLength * 100.0f);
        }
        Log.d(DownloadAndUpdateFirmwareTask.TAG, "Firmware download completed. URL:" + str);
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        inputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
    
    @Override
    protected int estimatedProgress(final UpdateInstruction updateInstruction) {
        int estimatedProgress = super.estimatedProgress(updateInstruction);
        final Iterator<UpdateStepInfo> iterator = updateInstruction.updateStepInfoList.iterator();
        while (iterator.hasNext()) {
            if (!FirmwareManager.getInstance().isUpdateStepReady(iterator.next())) {
                estimatedProgress += 100;
            }
        }
        return estimatedProgress;
    }
    
    @Override
    protected byte[] prepareDFUData(final UpdateStepInfo updateStepInfo) throws IOException {
        final byte[] firmware = FirmwareManager.getInstance().getFirmware(updateStepInfo);
        byte[] array;
        if (firmware == null) {
            Log.d(DownloadAndUpdateFirmwareTask.TAG, String.format("Firmware %s is not ready. Download firmware URL:%s", updateStepInfo.firmwareVersion, updateStepInfo.firmwareDownloadURL));
            final byte[] downloadDFU = this.downloadDFU(updateStepInfo.firmwareDownloadURL);
            FirmwareManager.getInstance().saveFirmwareInCache(updateStepInfo.firmwareDownloadURL, downloadDFU);
            array = downloadDFU;
        }
        else {
            Log.d(DownloadAndUpdateFirmwareTask.TAG, String.format("Firmware %s is ready.", updateStepInfo.firmwareVersion));
            array = firmware;
        }
        return array;
    }
}
