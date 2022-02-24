// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.filter;

public class EnvironmentFilter implements Filter
{
    private Filter filter;
    
    public EnvironmentFilter() {
        this(null);
    }
    
    public EnvironmentFilter(final Filter filter) {
        this.filter = filter;
    }
    
    @Override
    public String replace(String replace) {
        final String getenv = System.getenv(replace);
        if (getenv != null) {
            replace = getenv;
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
