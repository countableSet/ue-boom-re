// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

public class ConstructorException extends PersistenceException
{
    public ConstructorException(final String s, final Object... array) {
        super(s, array);
    }
    
    public ConstructorException(final Throwable t, final String s, final Object... array) {
        super(t, s, array);
    }
}
