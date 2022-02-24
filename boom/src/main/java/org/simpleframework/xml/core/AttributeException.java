// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

public class AttributeException extends PersistenceException
{
    public AttributeException(final String s, final Object... array) {
        super(s, array);
    }
    
    public AttributeException(final Throwable t, final String s, final Object... array) {
        super(t, s, array);
    }
}
