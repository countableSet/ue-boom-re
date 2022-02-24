// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import java.io.BufferedWriter;
import java.io.Writer;

class Formatter
{
    private static final char[] AND;
    private static final char[] CLOSE;
    private static final char[] DOUBLE;
    private static final char[] GREATER;
    private static final char[] LESS;
    private static final char[] NAMESPACE;
    private static final char[] OPEN;
    private static final char[] SINGLE;
    private OutputBuffer buffer;
    private Indenter indenter;
    private Tag last;
    private String prolog;
    private Writer result;
    
    static {
        NAMESPACE = new char[] { 'x', 'm', 'l', 'n', 's' };
        LESS = new char[] { '&', 'l', 't', ';' };
        GREATER = new char[] { '&', 'g', 't', ';' };
        DOUBLE = new char[] { '&', 'q', 'u', 'o', 't', ';' };
        SINGLE = new char[] { '&', 'a', 'p', 'o', 's', ';' };
        AND = new char[] { '&', 'a', 'm', 'p', ';' };
        OPEN = new char[] { '<', '!', '-', '-', ' ' };
        CLOSE = new char[] { ' ', '-', '-', '>' };
    }
    
    public Formatter(final Writer out, final Format format) {
        this.result = new BufferedWriter(out, 1024);
        this.indenter = new Indenter(format);
        this.buffer = new OutputBuffer();
        this.prolog = format.getProlog();
    }
    
    private void append(final char c) throws Exception {
        this.buffer.append(c);
    }
    
    private void append(final String s) throws Exception {
        this.buffer.append(s);
    }
    
    private void append(final char[] array) throws Exception {
        this.buffer.append(array);
    }
    
    private void data(final String s) throws Exception {
        this.write("<![CDATA[");
        this.write(s);
        this.write("]]>");
    }
    
    private void escape(final char c) throws Exception {
        final char[] symbol = this.symbol(c);
        if (symbol != null) {
            this.write(symbol);
        }
        else {
            this.write(c);
        }
    }
    
    private void escape(final String s) throws Exception {
        for (int length = s.length(), i = 0; i < length; ++i) {
            this.escape(s.charAt(i));
        }
    }
    
    private boolean isEmpty(final String s) {
        boolean b = true;
        if (s != null) {
            b = (s.length() == 0 && b);
        }
        return b;
    }
    
    private boolean isText(final char c) {
        boolean b = true;
        switch (c) {
            default: {
                b = (c > ' ' && c <= '~' && c != '\u00f7' && b);
                return b;
            }
            case '\t':
            case '\n':
            case '\r':
            case ' ': {
                return b;
            }
        }
    }
    
    private char[] symbol(final char c) {
        char[] array = null;
        switch (c) {
            default: {
                array = null;
                break;
            }
            case '<': {
                array = Formatter.LESS;
                break;
            }
            case '>': {
                array = Formatter.GREATER;
                break;
            }
            case '\"': {
                array = Formatter.DOUBLE;
                break;
            }
            case '\'': {
                array = Formatter.SINGLE;
                break;
            }
            case '&': {
                array = Formatter.AND;
                break;
            }
        }
        return array;
    }
    
    private String unicode(final char i) {
        return Integer.toString(i);
    }
    
    private void write(final char c) throws Exception {
        this.buffer.write(this.result);
        this.buffer.clear();
        this.result.write(c);
    }
    
    private void write(final String str) throws Exception {
        this.buffer.write(this.result);
        this.buffer.clear();
        this.result.write(str);
    }
    
    private void write(final String str, final String str2) throws Exception {
        this.buffer.write(this.result);
        this.buffer.clear();
        if (!this.isEmpty(str2)) {
            this.result.write(str2);
            this.result.write(58);
        }
        this.result.write(str);
    }
    
    private void write(final char[] cbuf) throws Exception {
        this.buffer.write(this.result);
        this.buffer.clear();
        this.result.write(cbuf);
    }
    
    public void flush() throws Exception {
        this.buffer.write(this.result);
        this.buffer.clear();
        this.result.flush();
    }
    
    public void writeAttribute(final String s, final String s2, final String s3) throws Exception {
        if (this.last != Tag.START) {
            throw new NodeException("Start element required");
        }
        this.write(' ');
        this.write(s, s3);
        this.write('=');
        this.write('\"');
        this.escape(s2);
        this.write('\"');
    }
    
    public void writeComment(final String s) throws Exception {
        final String top = this.indenter.top();
        if (this.last == Tag.START) {
            this.append('>');
        }
        if (top != null) {
            this.append(top);
            this.append(Formatter.OPEN);
            this.append(s);
            this.append(Formatter.CLOSE);
        }
        this.last = Tag.COMMENT;
    }
    
    public void writeEnd(final String s, final String s2) throws Exception {
        final String pop = this.indenter.pop();
        if (this.last == Tag.START) {
            this.write('/');
            this.write('>');
        }
        else {
            if (this.last != Tag.TEXT) {
                this.write(pop);
            }
            if (this.last != Tag.START) {
                this.write('<');
                this.write('/');
                this.write(s, s2);
                this.write('>');
            }
        }
        this.last = Tag.END;
    }
    
    public void writeNamespace(final String s, final String s2) throws Exception {
        if (this.last != Tag.START) {
            throw new NodeException("Start element required");
        }
        this.write(' ');
        this.write(Formatter.NAMESPACE);
        if (!this.isEmpty(s2)) {
            this.write(':');
            this.write(s2);
        }
        this.write('=');
        this.write('\"');
        this.escape(s);
        this.write('\"');
    }
    
    public void writeProlog() throws Exception {
        if (this.prolog != null) {
            this.write(this.prolog);
            this.write("\n");
        }
    }
    
    public void writeStart(final String s, final String s2) throws Exception {
        final String push = this.indenter.push();
        if (this.last == Tag.START) {
            this.append('>');
        }
        this.flush();
        this.append(push);
        this.append('<');
        if (!this.isEmpty(s2)) {
            this.append(s2);
            this.append(':');
        }
        this.append(s);
        this.last = Tag.START;
    }
    
    public void writeText(final String s) throws Exception {
        this.writeText(s, Mode.ESCAPE);
    }
    
    public void writeText(final String s, final Mode mode) throws Exception {
        if (this.last == Tag.START) {
            this.write('>');
        }
        if (mode == Mode.DATA) {
            this.data(s);
        }
        else {
            this.escape(s);
        }
        this.last = Tag.TEXT;
    }
    
    private enum Tag
    {
        COMMENT, 
        END, 
        START, 
        TEXT;
    }
}
