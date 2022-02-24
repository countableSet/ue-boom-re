// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

public class ElementException extends PersistenceException
{
    public ElementException(final String s, final Object... array) {
        super(s, array);
    }
    
    public ElementException(final Throwable t, final String s, final Object... array) {
        super(t, s, array);
    }
}
