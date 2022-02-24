// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

public class Format
{
    private final int indent;
    private final String prolog;
    private final Style style;
    private final Verbosity verbosity;
    
    public Format() {
        this(3);
    }
    
    public Format(final int n) {
        this(n, null, new IdentityStyle());
    }
    
    public Format(final int n, final String s) {
        this(n, s, new IdentityStyle());
    }
    
    public Format(final int n, final String s, final Style style) {
        this(n, s, style, Verbosity.HIGH);
    }
    
    public Format(final int indent, final String prolog, final Style style, final Verbosity verbosity) {
        this.verbosity = verbosity;
        this.prolog = prolog;
        this.indent = indent;
        this.style = style;
    }
    
    public Format(final int n, final Style style) {
        this(n, null, style);
    }
    
    public Format(final int n, final Style style, final Verbosity verbosity) {
        this(n, null, style, verbosity);
    }
    
    public Format(final int n, final Verbosity verbosity) {
        this(n, new IdentityStyle(), verbosity);
    }
    
    public Format(final String s) {
        this(3, s);
    }
    
    public Format(final Style style) {
        this(3, style);
    }
    
    public Format(final Style style, final Verbosity verbosity) {
        this(3, style, verbosity);
    }
    
    public Format(final Verbosity verbosity) {
        this(3, verbosity);
    }
    
    public int getIndent() {
        return this.indent;
    }
    
    public String getProlog() {
        return this.prolog;
    }
    
    public Style getStyle() {
        return this.style;
    }
    
    public Verbosity getVerbosity() {
        return this.verbosity;
    }
}
