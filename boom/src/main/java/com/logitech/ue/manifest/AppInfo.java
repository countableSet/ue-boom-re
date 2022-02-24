// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.manifest;

import org.simpleframework.xml.ElementList;
import java.util.ArrayList;
import org.simpleframework.xml.Attribute;

public class AppInfo
{
    @Attribute
    public String name;
    @ElementList(inline = true)
    public ArrayList<SpeakerScale> speakerScaleMap;
    
    public AppInfo() {
        this.speakerScaleMap = new ArrayList<SpeakerScale>();
    }
}
