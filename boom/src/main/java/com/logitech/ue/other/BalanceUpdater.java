// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import com.logitech.ue.centurion.UEDeviceManager;

public class BalanceUpdater extends ValueUpdater<Byte>
{
    @Override
    protected void updateValue(final Byte b) throws Exception {
        UEDeviceManager.getInstance().getConnectedDevice().setTWSBalance(b);
    }
}
