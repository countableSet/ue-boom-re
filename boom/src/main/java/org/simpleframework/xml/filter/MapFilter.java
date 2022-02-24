// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.filter;

import java.util.Map;

public class MapFilter implements Filter
{
    private Filter filter;
    private Map map;
    
    public MapFilter(final Map map) {
        this(map, null);
    }
    
    public MapFilter(final Map map, final Filter filter) {
        this.filter = filter;
        this.map = map;
    }
    
    @Override
    public String replace(String s) {
        Object value = null;
        if (this.map != null) {
            value = this.map.get(s);
        }
        if (value != null) {
            s = value.toString();
        }
        else if (this.filter != null) {
            s = this.filter.replace(s);
        }
        else {
            s = null;
        }
        return s;
    }
}
