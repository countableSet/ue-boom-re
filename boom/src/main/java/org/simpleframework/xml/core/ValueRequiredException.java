// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

public class ValueRequiredException extends PersistenceException
{
    public ValueRequiredException(final String s, final Object... array) {
        super(s, array);
    }
    
    public ValueRequiredException(final Throwable t, final String s, final Object... array) {
        super(t, s, array);
    }
}
