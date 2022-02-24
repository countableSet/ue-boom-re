// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

public class HyphenStyle implements Style
{
    private final Builder builder;
    private final Style style;
    
    public HyphenStyle() {
        this.style = new HyphenBuilder();
        this.builder = new Builder(this.style);
    }
    
    @Override
    public String getAttribute(final String s) {
        return this.builder.getAttribute(s);
    }
    
    @Override
    public String getElement(final String s) {
        return this.builder.getElement(s);
    }
    
    public void setAttribute(final String s, final String s2) {
        this.builder.setAttribute(s, s2);
    }
    
    public void setElement(final String s, final String s2) {
        this.builder.setElement(s, s2);
    }
}
