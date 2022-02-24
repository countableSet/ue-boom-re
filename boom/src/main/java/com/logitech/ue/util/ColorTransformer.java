// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.util;

import org.simpleframework.xml.transform.Transform;

public class ColorTransformer implements Transform<Integer>
{
    @Override
    public Integer read(final String s) throws Exception {
        return Utils.stringToColor(s);
    }
    
    @Override
    public String write(final Integer n) throws Exception {
        return Utils.colorToString(n);
    }
}
