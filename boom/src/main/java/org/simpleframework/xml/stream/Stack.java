// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import java.util.ArrayList;

class Stack<T> extends ArrayList<T>
{
    public Stack(final int initialCapacity) {
        super(initialCapacity);
    }
    
    public T bottom() {
        T value;
        if (this.size() <= 0) {
            value = null;
        }
        else {
            value = this.get(0);
        }
        return value;
    }
    
    public T pop() {
        final int size = this.size();
        T remove;
        if (size <= 0) {
            remove = null;
        }
        else {
            remove = this.remove(size - 1);
        }
        return remove;
    }
    
    public T push(final T e) {
        this.add(e);
        return e;
    }
    
    public T top() {
        final int size = this.size();
        T value;
        if (size <= 0) {
            value = null;
        }
        else {
            value = this.get(size - 1);
        }
        return value;
    }
}
