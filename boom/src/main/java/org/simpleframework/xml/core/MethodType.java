// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

enum MethodType
{
    GET(3), 
    IS(2), 
    NONE(0), 
    SET(3);
    
    private int prefix;
    
    private MethodType(final int prefix) {
        this.prefix = prefix;
    }
    
    public int getPrefix() {
        return this.prefix;
    }
}
