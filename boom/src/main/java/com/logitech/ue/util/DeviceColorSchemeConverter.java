// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.util;

import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.InputNode;
import com.logitech.ue.manifest.DeviceColorScheme;
import org.simpleframework.xml.convert.Converter;

public class DeviceColorSchemeConverter implements Converter<DeviceColorScheme>
{
    @Override
    public DeviceColorScheme read(final InputNode inputNode) throws Exception {
        final DeviceColorScheme deviceColorScheme = new DeviceColorScheme();
        deviceColorScheme.name = inputNode.getAttribute("name").getValue();
        if (inputNode.getAttribute("strapColor") != null) {
            deviceColorScheme.strapColor = Utils.stringToColor(inputNode.getAttribute("strapColor").getValue());
        }
        if (inputNode.getAttribute("buttonColor") != null) {
            deviceColorScheme.buttonColor = Utils.stringToColor(inputNode.getAttribute("buttonColor").getValue());
        }
        if (inputNode.getAttribute("accentColor") != null) {
            deviceColorScheme.accentColor = Utils.stringToColor(inputNode.getAttribute("accentColor").getValue());
        }
        if (inputNode.getAttribute("fabricColor") != null) {
            deviceColorScheme.fabricColor = Utils.stringToColor(inputNode.getAttribute("fabricColor").getValue());
        }
        return deviceColorScheme;
    }
    
    @Override
    public void write(final OutputNode outputNode, final DeviceColorScheme deviceColorScheme) throws Exception {
        outputNode.setAttribute("name", deviceColorScheme.name);
        outputNode.setAttribute("strapColor", Integer.toString(deviceColorScheme.strapColor));
        outputNode.setAttribute("buttonColor", Integer.toString(deviceColorScheme.buttonColor));
        outputNode.setAttribute("accentColor", Integer.toString(deviceColorScheme.accentColor));
        if (deviceColorScheme.fabricColor != null) {
            outputNode.setAttribute("fabricColor", Integer.toString(deviceColorScheme.fabricColor));
        }
    }
}
