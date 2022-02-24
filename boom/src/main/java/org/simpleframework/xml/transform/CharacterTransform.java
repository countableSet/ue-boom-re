// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

class CharacterTransform implements Transform<Character>
{
    @Override
    public Character read(final String s) throws Exception {
        if (s.length() != 1) {
            throw new InvalidFormatException("Cannot convert '%s' to a character", new Object[] { s });
        }
        return s.charAt(0);
    }
    
    @Override
    public String write(final Character c) throws Exception {
        return c.toString();
    }
}
