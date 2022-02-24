// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import org.simpleframework.xml.util.ConcurrentCache;
import org.simpleframework.xml.util.Cache;

class Builder implements Style
{
    private final Cache<String> attributes;
    private final Cache<String> elements;
    private final Style style;
    
    public Builder(final Style style) {
        this.attributes = new ConcurrentCache<String>();
        this.elements = new ConcurrentCache<String>();
        this.style = style;
    }
    
    @Override
    public String getAttribute(String s) {
        final String s2 = this.attributes.fetch(s);
        if (s2 != null) {
            s = s2;
        }
        else {
            final String attribute = this.style.getAttribute(s);
            if (attribute != null) {
                this.attributes.cache(s, attribute);
            }
            s = attribute;
        }
        return s;
    }
    
    @Override
    public String getElement(String s) {
        final String s2 = this.elements.fetch(s);
        if (s2 != null) {
            s = s2;
        }
        else {
            final String element = this.style.getElement(s);
            if (element != null) {
                this.elements.cache(s, element);
            }
            s = element;
        }
        return s;
    }
    
    public void setAttribute(final String s, final String s2) {
        this.attributes.cache(s, s2);
    }
    
    public void setElement(final String s, final String s2) {
        this.elements.cache(s, s2);
    }
}
