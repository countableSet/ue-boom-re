// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.strategy;

import org.simpleframework.xml.core.PersistenceException;

public class CycleException extends PersistenceException
{
    public CycleException(final String s, final Object... array) {
        super(s, array);
    }
    
    public CycleException(final Throwable t, final String s, final Object... array) {
        super(t, s, array);
    }
}
