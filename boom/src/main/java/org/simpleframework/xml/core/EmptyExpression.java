// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Iterator;
import java.util.LinkedList;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.Style;
import java.util.List;

class EmptyExpression implements Expression
{
    private final List<String> list;
    private final Style style;
    
    public EmptyExpression(final Format format) {
        this.list = new LinkedList<String>();
        this.style = format.getStyle();
    }
    
    @Override
    public String getAttribute(final String s) {
        return this.style.getAttribute(s);
    }
    
    @Override
    public String getElement(final String s) {
        return this.style.getElement(s);
    }
    
    @Override
    public String getFirst() {
        return null;
    }
    
    @Override
    public int getIndex() {
        return 0;
    }
    
    @Override
    public String getLast() {
        return null;
    }
    
    @Override
    public String getPath() {
        return "";
    }
    
    @Override
    public Expression getPath(final int n) {
        return null;
    }
    
    @Override
    public Expression getPath(final int n, final int n2) {
        return null;
    }
    
    @Override
    public String getPrefix() {
        return null;
    }
    
    @Override
    public boolean isAttribute() {
        return false;
    }
    
    @Override
    public boolean isEmpty() {
        return true;
    }
    
    @Override
    public boolean isPath() {
        return false;
    }
    
    @Override
    public Iterator<String> iterator() {
        return this.list.iterator();
    }
}
