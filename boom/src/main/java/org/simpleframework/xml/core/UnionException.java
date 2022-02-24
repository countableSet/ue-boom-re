// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

public class UnionException extends PersistenceException
{
    public UnionException(final String format, final Object... args) {
        super(String.format(format, args), new Object[0]);
    }
}
