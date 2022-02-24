// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

class ShortTransform implements Transform<Short>
{
    @Override
    public Short read(final String s) {
        return Short.valueOf(s);
    }
    
    @Override
    public String write(final Short n) {
        return n.toString();
    }
}
