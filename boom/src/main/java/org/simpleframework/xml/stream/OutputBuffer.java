// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import java.io.IOException;
import java.io.Writer;

class OutputBuffer
{
    private StringBuilder text;
    
    public OutputBuffer() {
        this.text = new StringBuilder();
    }
    
    public void append(final char c) {
        this.text.append(c);
    }
    
    public void append(final String str) {
        this.text.append(str);
    }
    
    public void append(final String s, final int start, final int end) {
        this.text.append(s, start, end);
    }
    
    public void append(final char[] str) {
        this.text.append(str, 0, str.length);
    }
    
    public void append(final char[] str, final int offset, final int len) {
        this.text.append(str, offset, len);
    }
    
    public void clear() {
        this.text.setLength(0);
    }
    
    public void write(final Writer writer) throws IOException {
        writer.append((CharSequence)this.text);
    }
}
