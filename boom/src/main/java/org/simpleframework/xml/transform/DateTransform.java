// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import java.util.Date;

class DateTransform<T extends Date> implements Transform<T>
{
    private final DateFactory<T> factory;
    
    public DateTransform(final Class<T> clazz) throws Exception {
        this.factory = new DateFactory<T>(clazz);
    }
    
    @Override
    public T read(final String s) throws Exception {
        synchronized (this) {
            return this.factory.getInstance(DateType.getDate(s).getTime());
        }
    }
    
    @Override
    public String write(final T t) throws Exception {
        synchronized (this) {
            return DateType.getText(t);
        }
    }
}
