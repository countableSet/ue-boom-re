// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

class BooleanTransform implements Transform<Boolean>
{
    @Override
    public Boolean read(final String s) {
        return Boolean.valueOf(s);
    }
    
    @Override
    public String write(final Boolean b) {
        return b.toString();
    }
}
