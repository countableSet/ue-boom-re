// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.filter;

public class SystemFilter implements Filter
{
    private Filter filter;
    
    public SystemFilter() {
        this(null);
    }
    
    public SystemFilter(final Filter filter) {
        this.filter = filter;
    }
    
    @Override
    public String replace(String replace) {
        final String property = System.getProperty(replace);
        if (property != null) {
            replace = property;
        }
        else if (this.filter != null) {
            replace = this.filter.replace(replace);
        }
        else {
            replace = null;
        }
        return replace;
    }
}
