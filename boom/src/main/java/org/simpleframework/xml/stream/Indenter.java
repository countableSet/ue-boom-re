// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

class Indenter
{
    private Cache cache;
    private int count;
    private int indent;
    private int index;
    
    public Indenter() {
        this(new Format());
    }
    
    public Indenter(final Format format) {
        this(format, 16);
    }
    
    private Indenter(final Format format, final int n) {
        this.indent = format.getIndent();
        this.cache = new Cache(n);
    }
    
    private String create() {
        final char[] value = new char[this.count + 1];
        String s;
        if (this.count > 0) {
            value[0] = 10;
            for (int i = 1; i <= this.count; ++i) {
                value[i] = 32;
            }
            s = new String(value);
        }
        else {
            s = "\n";
        }
        return s;
    }
    
    private String indent(final int n) {
        if (this.indent <= 0) {
            return "";
        }
        String s;
        if ((s = this.cache.get(n)) == null) {
            s = this.create();
            this.cache.set(n, s);
        }
        if (this.cache.size() <= 0) {
            return "";
        }
        return s;
        s = "";
        return s;
    }
    
    public String pop() {
        final int index = this.index - 1;
        this.index = index;
        final String indent = this.indent(index);
        if (this.indent > 0) {
            this.count -= this.indent;
        }
        return indent;
    }
    
    public String push() {
        final String indent = this.indent(this.index++);
        if (this.indent > 0) {
            this.count += this.indent;
        }
        return indent;
    }
    
    public String top() {
        return this.indent(this.index);
    }
    
    private static class Cache
    {
        private int count;
        private String[] list;
        
        public Cache(final int n) {
            this.list = new String[n];
        }
        
        private void resize(int i) {
            final String[] list = new String[i];
            for (i = 0; i < this.list.length; ++i) {
                list[i] = this.list[i];
            }
            this.list = list;
        }
        
        public String get(final int n) {
            String s;
            if (n < this.list.length) {
                s = this.list[n];
            }
            else {
                s = null;
            }
            return s;
        }
        
        public void set(final int count, final String s) {
            if (count >= this.list.length) {
                this.resize(count * 2);
            }
            if (count > this.count) {
                this.count = count;
            }
            this.list[count] = s;
        }
        
        public int size() {
            return this.count;
        }
    }
}
