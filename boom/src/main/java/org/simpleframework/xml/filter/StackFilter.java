// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.filter;

import java.util.Stack;

public class StackFilter implements Filter
{
    private Stack<Filter> stack;
    
    public StackFilter() {
        this.stack = new Stack<Filter>();
    }
    
    public void push(final Filter item) {
        this.stack.push(item);
    }
    
    @Override
    public String replace(String s) {
        int size = this.stack.size();
        while (--size >= 0) {
            final String replace = this.stack.get(size).replace(s);
            if (replace != null) {
                s = replace;
                return s;
            }
        }
        s = null;
        return s;
    }
}
