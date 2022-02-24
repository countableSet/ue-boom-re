// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import java.util.Date;
import java.util.GregorianCalendar;

class GregorianCalendarTransform implements Transform<GregorianCalendar>
{
    private final DateTransform transform;
    
    public GregorianCalendarTransform() throws Exception {
        this(Date.class);
    }
    
    public GregorianCalendarTransform(final Class clazz) throws Exception {
        this.transform = new DateTransform(clazz);
    }
    
    private GregorianCalendar read(final Date time) throws Exception {
        final GregorianCalendar gregorianCalendar = new GregorianCalendar();
        if (time != null) {
            gregorianCalendar.setTime(time);
        }
        return gregorianCalendar;
    }
    
    @Override
    public GregorianCalendar read(final String s) throws Exception {
        return this.read(this.transform.read(s));
    }
    
    @Override
    public String write(final GregorianCalendar gregorianCalendar) throws Exception {
        return this.transform.write(gregorianCalendar.getTime());
    }
}
