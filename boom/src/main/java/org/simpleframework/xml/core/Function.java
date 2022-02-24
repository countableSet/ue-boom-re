// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Map;
import java.lang.reflect.Method;

class Function
{
    private final boolean contextual;
    private final Method method;
    
    public Function(final Method method) {
        this(method, false);
    }
    
    public Function(final Method method, final boolean contextual) {
        this.contextual = contextual;
        this.method = method;
    }
    
    public Object call(final Context context, final Object o) throws Exception {
        Object o2;
        if (o != null) {
            final Map map = context.getSession().getMap();
            if (this.contextual) {
                o2 = this.method.invoke(o, map);
            }
            else {
                o2 = this.method.invoke(o, new Object[0]);
            }
        }
        else {
            o2 = null;
        }
        return o2;
    }
}
