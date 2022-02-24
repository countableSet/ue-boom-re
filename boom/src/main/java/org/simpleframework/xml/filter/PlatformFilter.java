// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.filter;

import java.util.Map;

public class PlatformFilter extends StackFilter
{
    public PlatformFilter() {
        this(null);
    }
    
    public PlatformFilter(final Map map) {
        this.push(new EnvironmentFilter());
        this.push(new SystemFilter());
        this.push(new MapFilter(map));
    }
}
