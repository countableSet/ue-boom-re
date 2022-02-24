// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

public class InvalidFormatException extends TransformException
{
    public InvalidFormatException(final String format, final Object... args) {
        super(String.format(format, args), new Object[0]);
    }
    
    public InvalidFormatException(final Throwable t, final String format, final Object... args) {
        super(String.format(format, args), new Object[] { t });
    }
}
