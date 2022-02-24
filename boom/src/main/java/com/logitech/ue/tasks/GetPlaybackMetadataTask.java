// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.UEDeviceManager;
import com.logitech.ue.centurion.utils.MAC;
import com.logitech.ue.centurion.device.devicedata.UEPlaybackMetadataType;
import java.util.HashMap;

public class GetPlaybackMetadataTask extends GetDeviceDataTask<HashMap<UEPlaybackMetadataType, Object>>
{
    private static final String TAG;
    MAC mDeviceMACAddress;
    
    static {
        TAG = GetPlaybackMetadataTask.class.getSimpleName();
    }
    
    public GetPlaybackMetadataTask(final MAC mDeviceMACAddress) {
        this.mDeviceMACAddress = mDeviceMACAddress;
    }
    
    @Override
    public String getTag() {
        return GetPlaybackMetadataTask.TAG;
    }
    
    @Override
    public HashMap<UEPlaybackMetadataType, Object> work(final Void... array) throws Exception {
        final HashMap<UEPlaybackMetadataType, Object> hashMap = new HashMap<UEPlaybackMetadataType, Object>();
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        hashMap.put(UEPlaybackMetadataType.TITLE, connectedDevice.getPlaybackMetadataRequest(this.mDeviceMACAddress, UEPlaybackMetadataType.TITLE));
        hashMap.put(UEPlaybackMetadataType.ARTIST, connectedDevice.getPlaybackMetadataRequest(this.mDeviceMACAddress, UEPlaybackMetadataType.ARTIST));
        hashMap.put(UEPlaybackMetadataType.ALBUM, connectedDevice.getPlaybackMetadataRequest(this.mDeviceMACAddress, UEPlaybackMetadataType.ALBUM));
        return hashMap;
    }
}
