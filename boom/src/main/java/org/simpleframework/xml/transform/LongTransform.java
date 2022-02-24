// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

class LongTransform implements Transform<Long>
{
    @Override
    public Long read(final String s) {
        return Long.valueOf(s);
    }
    
    @Override
    public String write(final Long n) {
        return n.toString();
    }
}
