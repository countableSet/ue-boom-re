// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.manifest;

import org.simpleframework.xml.Attribute;
import com.logitech.ue.util.DeviceColorSchemeConverter;
import org.simpleframework.xml.convert.Convert;
import org.simpleframework.xml.Root;

@Root
@Convert(DeviceColorSchemeConverter.class)
public class DeviceColorScheme
{
    @Attribute(required = false)
    public int accentColor;
    @Attribute(required = false)
    public int buttonColor;
    @Attribute(required = false)
    public Integer fabricColor;
    @Attribute
    public String name;
    @Attribute(name = "strapColor", required = false)
    public int strapColor;
    
    public DeviceColorScheme() {
    }
    
    public DeviceColorScheme(final String name, final Integer n, final Integer n2, final Integer n3, final Integer fabricColor) {
        this.name = name;
        this.strapColor = n;
        this.buttonColor = n2;
        this.accentColor = n3;
        this.fabricColor = fabricColor;
    }
}
