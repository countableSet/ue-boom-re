// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import java.util.TimeZone;

class TimeZoneTransform implements Transform<TimeZone>
{
    @Override
    public TimeZone read(final String id) {
        return TimeZone.getTimeZone(id);
    }
    
    @Override
    public String write(final TimeZone timeZone) {
        return timeZone.getID();
    }
}
