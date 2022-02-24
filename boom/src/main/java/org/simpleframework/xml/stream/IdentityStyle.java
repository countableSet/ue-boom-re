// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

class IdentityStyle implements Style
{
    @Override
    public String getAttribute(final String s) {
        return s;
    }
    
    @Override
    public String getElement(final String s) {
        return s;
    }
}
