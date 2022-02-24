// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

public class MethodException extends PersistenceException
{
    public MethodException(final String s, final Object... array) {
        super(s, array);
    }
    
    public MethodException(final Throwable t, final String s, final Object... array) {
        super(t, s, array);
    }
}
