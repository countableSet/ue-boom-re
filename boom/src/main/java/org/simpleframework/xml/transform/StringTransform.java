// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

class StringTransform implements Transform<String>
{
    @Override
    public String read(final String s) {
        return s;
    }
    
    @Override
    public String write(final String s) {
        return s;
    }
}
