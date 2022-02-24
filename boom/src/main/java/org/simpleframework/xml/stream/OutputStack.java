// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;

class OutputStack extends ArrayList<OutputNode>
{
    private final Set active;
    
    public OutputStack(final Set active) {
        this.active = active;
    }
    
    public OutputNode bottom() {
        OutputNode outputNode;
        if (this.size() <= 0) {
            outputNode = null;
        }
        else {
            outputNode = this.get(0);
        }
        return outputNode;
    }
    
    @Override
    public Iterator<OutputNode> iterator() {
        return new Sequence();
    }
    
    public OutputNode pop() {
        final int size = this.size();
        OutputNode purge;
        if (size <= 0) {
            purge = null;
        }
        else {
            purge = this.purge(size - 1);
        }
        return purge;
    }
    
    public OutputNode purge(final int index) {
        final OutputNode outputNode = this.remove(index);
        if (outputNode != null) {
            this.active.remove(outputNode);
        }
        return outputNode;
    }
    
    public OutputNode push(final OutputNode e) {
        this.active.add(e);
        this.add(e);
        return e;
    }
    
    public OutputNode top() {
        final int size = this.size();
        OutputNode outputNode;
        if (size <= 0) {
            outputNode = null;
        }
        else {
            outputNode = this.get(size - 1);
        }
        return outputNode;
    }
    
    private class Sequence implements Iterator<OutputNode>
    {
        private int cursor;
        
        public Sequence() {
            this.cursor = OutputStack.this.size();
        }
        
        @Override
        public boolean hasNext() {
            return this.cursor > 0;
        }
        
        @Override
        public OutputNode next() {
            OutputNode outputNode;
            if (this.hasNext()) {
                final OutputStack this$0 = OutputStack.this;
                final int n = this.cursor - 1;
                this.cursor = n;
                outputNode = this$0.get(n);
            }
            else {
                outputNode = null;
            }
            return outputNode;
        }
        
        @Override
        public void remove() {
            OutputStack.this.purge(this.cursor);
        }
    }
}
