// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import java.text.SimpleDateFormat;
import java.util.Date;

enum DateType
{
    FULL("yyyy-MM-dd HH:mm:ss.S z"), 
    LONG("yyyy-MM-dd HH:mm:ss z"), 
    NORMAL("yyyy-MM-dd z"), 
    SHORT("yyyy-MM-dd");
    
    private DateFormat format;
    
    private DateType(final String s) {
        this.format = new DateFormat(s);
    }
    
    public static Date getDate(final String s) throws Exception {
        return getType(s).getFormat().getDate(s);
    }
    
    private DateFormat getFormat() {
        return this.format;
    }
    
    public static String getText(final Date date) throws Exception {
        return DateType.FULL.getFormat().getText(date);
    }
    
    public static DateType getType(final String s) {
        final int length = s.length();
        DateType dateType;
        if (length > 23) {
            dateType = DateType.FULL;
        }
        else if (length > 20) {
            dateType = DateType.LONG;
        }
        else if (length > 11) {
            dateType = DateType.NORMAL;
        }
        else {
            dateType = DateType.SHORT;
        }
        return dateType;
    }
    
    private static class DateFormat
    {
        private SimpleDateFormat format;
        
        public DateFormat(final String pattern) {
            this.format = new SimpleDateFormat(pattern);
        }
        
        public Date getDate(final String source) throws Exception {
            synchronized (this) {
                return this.format.parse(source);
            }
        }
        
        public String getText(final Date date) throws Exception {
            synchronized (this) {
                return this.format.format(date);
            }
        }
    }
}
