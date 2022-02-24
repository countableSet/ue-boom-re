// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

public class RootException extends PersistenceException
{
    public RootException(final String s, final Object... array) {
        super(s, array);
    }
    
    public RootException(final Throwable t, final String s, final Object... array) {
        super(t, s, array);
    }
}
