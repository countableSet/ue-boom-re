// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

class CamelCaseBuilder implements Style
{
    protected final boolean attribute;
    protected final boolean element;
    
    public CamelCaseBuilder(final boolean element, final boolean attribute) {
        this.attribute = attribute;
        this.element = element;
    }
    
    @Override
    public String getAttribute(final String s) {
        String process = null;
        if (s != null) {
            process = new Attribute(s).process();
        }
        return process;
    }
    
    @Override
    public String getElement(final String s) {
        String process = null;
        if (s != null) {
            process = new Element(s).process();
        }
        return process;
    }
    
    private class Attribute extends Splitter
    {
        private boolean capital;
        
        private Attribute(final String s) {
            super(s);
        }
        
        @Override
        protected void commit(final char[] str, final int offset, final int len) {
            this.builder.append(str, offset, len);
        }
        
        @Override
        protected void parse(final char[] array, final int n, final int n2) {
            if (CamelCaseBuilder.this.attribute || this.capital) {
                array[n] = this.toUpper(array[n]);
            }
            this.capital = true;
        }
    }
    
    private class Element extends Attribute
    {
        private boolean capital;
        
        private Element(final String s) {
            super(s);
        }
        
        @Override
        protected void parse(final char[] array, final int n, final int n2) {
            if (CamelCaseBuilder.this.element || this.capital) {
                array[n] = this.toUpper(array[n]);
            }
            this.capital = true;
        }
    }
}
