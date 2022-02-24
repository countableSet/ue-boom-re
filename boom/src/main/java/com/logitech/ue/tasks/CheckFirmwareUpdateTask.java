// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.firmware.UpdateInstruction;
import com.logitech.ue.firmware.UpdateRequestError;
import com.logitech.ue.firmware.OnCheckUpdateInstructionListener;
import com.logitech.ue.firmware.FirmwareManager;
import com.logitech.ue.firmware.UpdateInstructionParams;
import java.util.Locale;
import android.os.Build$VERSION;
import com.logitech.ue.UEColourHelper;
import com.logitech.ue.centurion.UEDeviceManager;

public class CheckFirmwareUpdateTask extends SafeTask<Void, Void, Void>
{
    private static final String TAG;
    
    static {
        TAG = CheckFirmwareUpdateTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return CheckFirmwareUpdateTask.TAG;
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        if (connectedDevice.getDeviceConnectionStatus().isBtClassicConnectedState() && connectedDevice.isOTASupported()) {
            final UpdateInstructionParams updateInstructionParams = new UpdateInstructionParams("orpheum", "5.0.166", UEColourHelper.getDeviceTypeByColour(connectedDevice.getDeviceColor()).getDeviceTypeName().toLowerCase(), connectedDevice.getHardwareRevision(), connectedDevice.getFirmwareVersion(), "Android", Build$VERSION.RELEASE, Locale.getDefault().getLanguage());
            FirmwareManager.getInstance().beginCheckUpdateInstruction(updateInstructionParams, new OnCheckUpdateInstructionListener() {
                @Override
                public void onError(final Exception ex) {
                }
                
                @Override
                public void onFail(final UpdateRequestError updateRequestError) {
                }
                
                @Override
                public void onSuccess(final UpdateInstruction updateInstruction) {
                    FirmwareManager.getInstance().beginUpdatePreparation(updateInstructionParams, updateInstruction);
                }
            });
        }
        return null;
    }
}
