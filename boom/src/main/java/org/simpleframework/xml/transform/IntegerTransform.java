// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

class IntegerTransform implements Transform<Integer>
{
    @Override
    public Integer read(final String s) {
        return Integer.valueOf(s);
    }
    
    @Override
    public String write(final Integer n) {
        return n.toString();
    }
}
