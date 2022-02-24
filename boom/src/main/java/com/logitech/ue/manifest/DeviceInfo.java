// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.manifest;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Attribute;
import java.util.ArrayList;

public class DeviceInfo
{
    @Attribute
    @ElementList(name = "themes", required = false)
    public ArrayList<DeviceColorScheme> deviceColorSchemeMap;
    @Attribute
    public String displayName;
    @Attribute
    public String hexCode;
    @Attribute
    public String type;
    @Attribute
    public String version;
    
    public DeviceInfo() {
        this.deviceColorSchemeMap = new ArrayList<DeviceColorScheme>();
    }
}
