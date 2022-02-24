// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.firmware.UpdateInstruction;
import com.logitech.ue.other.AlarmMusicType;
import com.logitech.ue.centurion.device.devicedata.UEEQSetting;
import com.logitech.ue.FlurryTracker;
import android.content.Context;
import com.logitech.ue.other.EQHelper;
import com.logitech.ue.App;
import com.logitech.ue.firmware.UpdateInstructionParams;
import java.util.Locale;
import android.os.Build$VERSION;
import com.logitech.ue.UEColourHelper;
import com.logitech.ue.firmware.FirmwareManager;
import com.logitech.ue.centurion.device.devicedata.UEAudioRouting;
import com.logitech.ue.devicedata.AlarmSettings;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.UEDeviceManager;

public class AppExitTask extends SafeTask<Void, Void, Void>
{
    private static final String TAG;
    
    static {
        TAG = AppExitTask.class.getSimpleName();
    }
    
    @Override
    public String getTag() {
        return AppExitTask.TAG;
    }
    
    @Override
    protected void onPostExecute(final Object o) {
        super.onPostExecute(o);
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        if (connectedDevice != null) {
            connectedDevice.dropSecondLevelCache();
        }
    }
    
    @Override
    public Void work(final Void... array) throws Exception {
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        if (connectedDevice != null && connectedDevice.getDeviceConnectionStatus().isBtClassicConnectedState()) {
            final boolean bleState = connectedDevice.getBLEState();
            if (!this.isCancelled()) {
                final UEEQSetting eqSetting = connectedDevice.getEQSetting();
                if (!this.isCancelled()) {
                    final AlarmMusicType musicType = AlarmSettings.getMusicType();
                    if (!this.isCancelled()) {
                        final boolean on = AlarmSettings.isOn();
                        if (!this.isCancelled()) {
                            final boolean repeat = AlarmSettings.isRepeat();
                            if (!this.isCancelled()) {
                                final boolean b = connectedDevice.getAudioRouting() != UEAudioRouting.Double;
                                if (!this.isCancelled()) {
                                    final UpdateInstruction updateInstructionFromCache = FirmwareManager.getInstance().getUpdateInstructionFromCache(new UpdateInstructionParams("orpheum", "5.0.166", UEColourHelper.getDeviceTypeByColour(connectedDevice.getDeviceColor()).getDeviceTypeName().toLowerCase(), connectedDevice.getHardwareRevision(), connectedDevice.getFirmwareVersion(), "Android", Build$VERSION.RELEASE, Locale.getDefault().getLanguage()));
                                    String s;
                                    if (updateInstructionFromCache != null && updateInstructionFromCache.isUpdateAvailable) {
                                        s = "Update Now";
                                    }
                                    else {
                                        s = "Up-To-Date";
                                    }
                                    FlurryTracker.logAppDeactivated(bleState, EQHelper.getEQTechnicalName((Context)App.getInstance(), eqSetting), musicType.name(), on, repeat, b, s);
                                }
                            }
                        }
                    }
                }
            }
        }
        else {
            FlurryTracker.logAppDeactivated(null, null, null, AlarmSettings.isOn(), AlarmSettings.isRepeat(), null, null);
        }
        return null;
    }
}
