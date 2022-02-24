// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import java.util.regex.Pattern;
import java.util.Locale;

class LocaleTransform implements Transform<Locale>
{
    private final Pattern pattern;
    
    public LocaleTransform() {
        this.pattern = Pattern.compile("_");
    }
    
    private Locale read(final String[] array) throws Exception {
        final String[] array2 = { "", "", "" };
        for (int i = 0; i < array2.length; ++i) {
            if (i < array.length) {
                array2[i] = array[i];
            }
        }
        return new Locale(array2[0], array2[1], array2[2]);
    }
    
    @Override
    public Locale read(final String input) throws Exception {
        final String[] split = this.pattern.split(input);
        if (split.length < 1) {
            throw new InvalidFormatException("Invalid locale %s", new Object[] { input });
        }
        return this.read(split);
    }
    
    @Override
    public String write(final Locale locale) {
        return locale.toString();
    }
}
