// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion;

import java.util.Iterator;
import android.util.Log;
import com.logitech.ue.centurion.device.UESPPDevice;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.connection.UEDeviceConnector;
import com.logitech.ue.centurion.utils.MAC;
import java.util.HashMap;
import com.logitech.ue.centurion.interfaces.IUEDeviceFactory;

public class UESimpleDeviceFactory implements IUEDeviceFactory
{
    protected HashMap<String, Class> mDeviceMap;
    
    public UESimpleDeviceFactory(final HashMap<String, Class> mDeviceMap) {
        this.mDeviceMap = mDeviceMap;
    }
    
    @Override
    public UEGenericDevice buildDevice(String s, final MAC mac, final UEDeviceConnector ueDeviceConnector) {
        final Iterator<String> iterator = this.mDeviceMap.keySet().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                return null;
            }
            final String key = iterator.next();
            if (!s.matches(key)) {
                continue;
            }
            try {
                s = (String)((Class<UESPPDevice>)this.mDeviceMap.get(key)).getConstructor(MAC.class, UEDeviceConnector.class).newInstance(mac, ueDeviceConnector);
                return (UEGenericDevice)s;
            }
            catch (Exception ex) {
                Log.e("UESimpleDeviceFactory", "Fail to create class " + this.mDeviceMap.get(key).getSimpleName());
                continue;
            }
            s = null;
            return (UEGenericDevice)s;
        }
    }
    
    @Override
    public boolean isDeviceIDValid(final String s) {
        final Iterator<String> iterator = this.mDeviceMap.keySet().iterator();
        while (iterator.hasNext()) {
            if (s.matches(iterator.next())) {
                return true;
            }
        }
        return false;
    }
}
