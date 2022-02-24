// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

abstract class Splitter
{
    protected StringBuilder builder;
    protected int count;
    protected int off;
    protected char[] text;
    
    public Splitter(final String s) {
        this.builder = new StringBuilder();
        this.text = s.toCharArray();
        this.count = this.text.length;
    }
    
    private boolean acronym() {
        boolean b = true;
        int off = this.off;
        int n = 0;
        while (off < this.count && this.isUpper(this.text[off])) {
            ++n;
            ++off;
        }
        if (n > 1) {
            int off2;
            if ((off2 = off) < this.count) {
                off2 = off;
                if (this.isUpper(this.text[off - 1])) {
                    off2 = off - 1;
                }
            }
            this.commit(this.text, this.off, off2 - this.off);
            this.off = off2;
        }
        if (n <= 1) {
            b = false;
        }
        return b;
    }
    
    private boolean isDigit(final char ch) {
        return Character.isDigit(ch);
    }
    
    private boolean isLetter(final char ch) {
        return Character.isLetter(ch);
    }
    
    private boolean isSpecial(final char ch) {
        return !Character.isLetterOrDigit(ch);
    }
    
    private boolean isUpper(final char ch) {
        return Character.isUpperCase(ch);
    }
    
    private boolean number() {
        int off = this.off;
        int n = 0;
        while (off < this.count && this.isDigit(this.text[off])) {
            ++n;
            ++off;
        }
        if (n > 0) {
            this.commit(this.text, this.off, off - this.off);
        }
        this.off = off;
        return n > 0;
    }
    
    private void token() {
        int i;
        for (i = this.off; i < this.count; ++i) {
            final char c = this.text[i];
            if (!this.isLetter(c) || (i > this.off && this.isUpper(c))) {
                break;
            }
        }
        if (i > this.off) {
            this.parse(this.text, this.off, i - this.off);
            this.commit(this.text, this.off, i - this.off);
        }
        this.off = i;
    }
    
    protected abstract void commit(final char[] p0, final int p1, final int p2);
    
    protected abstract void parse(final char[] p0, final int p1, final int p2);
    
    public String process() {
        while (this.off < this.count) {
            while (this.off < this.count && this.isSpecial(this.text[this.off])) {
                ++this.off;
            }
            if (!this.acronym()) {
                this.token();
                this.number();
            }
        }
        return this.builder.toString();
    }
    
    protected char toLower(final char ch) {
        return Character.toLowerCase(ch);
    }
    
    protected char toUpper(final char ch) {
        return Character.toUpperCase(ch);
    }
}
