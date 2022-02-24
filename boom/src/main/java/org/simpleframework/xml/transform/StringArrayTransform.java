// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import java.util.regex.Pattern;

class StringArrayTransform implements Transform<String[]>
{
    private final Pattern pattern;
    private final String token;
    
    public StringArrayTransform() {
        this(",");
    }
    
    public StringArrayTransform(final String s) {
        this.pattern = Pattern.compile(s);
        this.token = s;
    }
    
    private String[] read(final String input, String s) {
        final String[] split = this.pattern.split(input);
        for (int i = 0; i < split.length; ++i) {
            s = split[i];
            if (s != null) {
                split[i] = s.trim();
            }
        }
        return split;
    }
    
    private String write(final String[] array, final String str) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            final String str2 = array[i];
            if (str2 != null) {
                if (sb.length() > 0) {
                    sb.append(str);
                    sb.append(' ');
                }
                sb.append(str2);
            }
        }
        return sb.toString();
    }
    
    @Override
    public String[] read(final String s) {
        return this.read(s, this.token);
    }
    
    @Override
    public String write(final String[] array) {
        return this.write(array, this.token);
    }
}
