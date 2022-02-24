// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import com.logitech.ue.centurion.UEDeviceManager;
import android.util.Log;
import com.logitech.ue.centurion.utils.UEUtils;

public class EQUpdater extends ValueUpdater<byte[]>
{
    public static final String TAG;
    
    static {
        TAG = EQUpdater.class.getSimpleName();
    }
    
    @Override
    protected void updateValue(final byte[] customEQ) throws Exception {
        Log.d(EQUpdater.TAG, "Update eq to " + UEUtils.byteArrayToFancyHexString(customEQ));
        UEDeviceManager.getInstance().getConnectedDevice().setCustomEQ(customEQ);
    }
}
