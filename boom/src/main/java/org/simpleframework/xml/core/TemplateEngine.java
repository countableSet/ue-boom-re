// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.filter.Filter;

class TemplateEngine
{
    private Filter filter;
    private Template name;
    private int off;
    private Template source;
    private Template text;
    
    public TemplateEngine(final Filter filter) {
        this.source = new Template();
        this.name = new Template();
        this.text = new Template();
        this.filter = filter;
    }
    
    private void name() {
        while (this.off < this.source.count) {
            final char c = this.source.buf[this.off++];
            if (c == '}') {
                this.replace();
                break;
            }
            this.name.append(c);
        }
        if (this.name.length() > 0) {
            this.text.append("${");
            this.text.append(this.name);
        }
    }
    
    private void parse() {
        while (this.off < this.source.count) {
            final char c = this.source.buf[this.off++];
            if (c == '$' && this.off < this.source.count) {
                if (this.source.buf[this.off++] == '{') {
                    this.name();
                    continue;
                }
                --this.off;
            }
            this.text.append(c);
        }
    }
    
    private void replace() {
        if (this.name.length() > 0) {
            this.replace(this.name);
        }
        this.name.clear();
    }
    
    private void replace(final String s) {
        final String replace = this.filter.replace(s);
        if (replace == null) {
            this.text.append("${");
            this.text.append(s);
            this.text.append("}");
        }
        else {
            this.text.append(replace);
        }
    }
    
    private void replace(final Template template) {
        this.replace(template.toString());
    }
    
    public void clear() {
        this.name.clear();
        this.text.clear();
        this.source.clear();
        this.off = 0;
    }
    
    public String process(String string) {
        if (((String)string).indexOf(36) >= 0) {
            try {
                this.source.append((String)string);
                this.parse();
                string = this.text.toString();
            }
            finally {
                this.clear();
            }
        }
        return (String)string;
    }
}
