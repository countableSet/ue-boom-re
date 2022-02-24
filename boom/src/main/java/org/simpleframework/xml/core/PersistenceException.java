// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

public class PersistenceException extends Exception
{
    public PersistenceException(final String format, final Object... args) {
        super(String.format(format, args));
    }
    
    public PersistenceException(final Throwable cause, final String format, final Object... args) {
        super(String.format(format, args), cause);
    }
}
