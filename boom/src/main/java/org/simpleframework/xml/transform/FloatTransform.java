// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

class FloatTransform implements Transform<Float>
{
    @Override
    public Float read(final String s) {
        return Float.valueOf(s);
    }
    
    @Override
    public String write(final Float n) {
        return n.toString();
    }
}
