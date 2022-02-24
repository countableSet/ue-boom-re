// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.devicedata;

import java.util.ArrayList;
import com.logitech.ue.centurion.device.devicedata.UESonificationProfile;
import com.logitech.ue.centurion.device.devicedata.UELanguage;
import com.logitech.ue.centurion.device.devicedata.UEEQSetting;

public class DeviceSettingsInfo
{
    public UEEQSetting EQSettings;
    public Boolean bleState;
    public int color;
    public Boolean customSonification;
    public Boolean isGestureEnabled;
    public Boolean isXUPSupported;
    public UELanguage language;
    public String name;
    public byte[] partitionInfo;
    public UESonificationProfile sonificationProfile;
    public ArrayList<Integer> supportedLanguages;
    public Boolean twsLockFlag;
    
    public DeviceSettingsInfo() {
        this.customSonification = null;
        this.twsLockFlag = null;
        this.bleState = null;
        this.partitionInfo = null;
        this.isGestureEnabled = null;
        this.isXUPSupported = null;
    }
}
