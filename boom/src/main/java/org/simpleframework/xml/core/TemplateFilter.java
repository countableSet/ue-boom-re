// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.filter.Filter;

class TemplateFilter implements Filter
{
    private Context context;
    private Filter filter;
    
    public TemplateFilter(final Context context, final Filter filter) {
        this.context = context;
        this.filter = filter;
    }
    
    @Override
    public String replace(String s) {
        final Object attribute = this.context.getAttribute(s);
        if (attribute != null) {
            s = attribute.toString();
        }
        else {
            s = this.filter.replace(s);
        }
        return s;
    }
}
