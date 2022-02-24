// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

class EnumTransform implements Transform<Enum>
{
    private final Class type;
    
    public EnumTransform(final Class type) {
        this.type = type;
    }
    
    @Override
    public Enum read(final String name) throws Exception {
        return Enum.valueOf((Class<Enum>)this.type, name);
    }
    
    @Override
    public String write(final Enum enum1) throws Exception {
        return enum1.name();
    }
}
