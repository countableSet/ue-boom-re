// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

class DoubleTransform implements Transform<Double>
{
    @Override
    public Double read(final String s) {
        return Double.valueOf(s);
    }
    
    @Override
    public String write(final Double n) {
        return n.toString();
    }
}
