// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.convert;

public class ConvertException extends Exception
{
    public ConvertException(final String format, final Object... args) {
        super(String.format(format, args));
    }
}
