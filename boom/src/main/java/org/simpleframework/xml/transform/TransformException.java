// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import org.simpleframework.xml.core.PersistenceException;

public class TransformException extends PersistenceException
{
    public TransformException(final String format, final Object... args) {
        super(String.format(format, args), new Object[0]);
    }
    
    public TransformException(final Throwable t, final String format, final Object... args) {
        super(String.format(format, args), new Object[] { t });
    }
}
