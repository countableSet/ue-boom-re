// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

class Template
{
    protected char[] buf;
    protected String cache;
    protected int count;
    
    public Template() {
        this(16);
    }
    
    public Template(final int n) {
        this.buf = new char[n];
    }
    
    public void append(final char c) {
        this.ensureCapacity(this.count + 1);
        this.buf[this.count++] = c;
    }
    
    public void append(final String s) {
        this.ensureCapacity(this.count + s.length());
        s.getChars(0, s.length(), this.buf, this.count);
        this.count += s.length();
    }
    
    public void append(final String s, final int srcBegin, final int srcEnd) {
        this.ensureCapacity(this.count + srcEnd);
        s.getChars(srcBegin, srcEnd, this.buf, this.count);
        this.count += srcEnd;
    }
    
    public void append(final Template template) {
        this.append(template.buf, 0, template.count);
    }
    
    public void append(final Template template, final int n, final int n2) {
        this.append(template.buf, n, n2);
    }
    
    public void append(final char[] array, final int n, final int n2) {
        this.ensureCapacity(this.count + n2);
        System.arraycopy(array, n, this.buf, this.count, n2);
        this.count += n2;
    }
    
    public void clear() {
        this.cache = null;
        this.count = 0;
    }
    
    protected void ensureCapacity(final int a) {
        if (this.buf.length < a) {
            final char[] buf = new char[Math.max(a, this.buf.length * 2)];
            System.arraycopy(this.buf, 0, buf, 0, this.count);
            this.buf = buf;
        }
    }
    
    public int length() {
        return this.count;
    }
    
    @Override
    public String toString() {
        return new String(this.buf, 0, this.count);
    }
}
