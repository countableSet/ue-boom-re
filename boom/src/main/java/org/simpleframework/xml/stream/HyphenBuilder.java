// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

class HyphenBuilder implements Style
{
    @Override
    public String getAttribute(final String s) {
        String process = null;
        if (s != null) {
            process = new Parser(s).process();
        }
        return process;
    }
    
    @Override
    public String getElement(final String s) {
        String process = null;
        if (s != null) {
            process = new Parser(s).process();
        }
        return process;
    }
    
    private class Parser extends Splitter
    {
        private Parser(final String s) {
            super(s);
        }
        
        @Override
        protected void commit(final char[] str, final int offset, final int len) {
            this.builder.append(str, offset, len);
            if (offset + len < this.count) {
                this.builder.append('-');
            }
        }
        
        @Override
        protected void parse(final char[] array, final int n, final int n2) {
            array[n] = this.toLower(array[n]);
        }
    }
}
