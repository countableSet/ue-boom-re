// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

class ByteTransform implements Transform<Byte>
{
    @Override
    public Byte read(final String s) {
        return Byte.valueOf(s);
    }
    
    @Override
    public String write(final Byte b) {
        return b.toString();
    }
}
