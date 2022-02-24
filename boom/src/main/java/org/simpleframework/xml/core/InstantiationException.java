// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

public class InstantiationException extends PersistenceException
{
    public InstantiationException(final String s, final Object... array) {
        super(s, array);
    }
    
    public InstantiationException(final Throwable t, final String s, final Object... array) {
        super(t, s, array);
    }
}
