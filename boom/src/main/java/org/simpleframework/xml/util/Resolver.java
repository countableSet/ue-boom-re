// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.util;

import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.AbstractSet;

public class Resolver<M extends Match> extends AbstractSet<M>
{
    protected final Cache cache;
    protected final Stack stack;
    
    public Resolver() {
        this.stack = new Stack();
        this.cache = new Cache();
    }
    
    private boolean match(final char[] array, int n, final char[] array2, int n2) {
        final boolean b = true;
        while (n2 < array2.length && n < array.length) {
            int n3 = n;
            int n4 = n2;
            if (array2[n2] == '*') {
                int n5 = n2;
                while (array2[n5] == '*') {
                    n2 = n5 + 1;
                    if ((n5 = n2) >= array2.length) {
                        return b;
                    }
                }
                int i = n;
                n2 = n5;
                if (array2[n5] == '?') {
                    n2 = n5 + 1;
                    final boolean b2 = b;
                    if (n2 >= array2.length) {
                        return b2;
                    }
                    i = n;
                }
                while (i < array.length) {
                    if (array[i] == array2[n2] || array2[n2] == '?') {
                        if (array2[n2 - 1] == '?') {
                            break;
                        }
                        final boolean b2 = b;
                        if (this.match(array, i, array2, n2)) {
                            return b2;
                        }
                    }
                    ++i;
                }
                n3 = i;
                n4 = n2;
                if (array.length == i) {
                    return false;
                }
            }
            n = array[n3];
            n2 = n4 + 1;
            if (n == array2[n4] || array2[n2 - 1] == '?') {
                n = n3 + 1;
                continue;
            }
            return false;
        }
        int n6;
        if (array2.length != (n6 = n2)) {
            while (array2[n6] == '*') {
                n = n6 + 1;
                if ((n6 = n) >= array2.length) {
                    return b;
                }
            }
            return false;
        }
        boolean b2 = b;
        if (array.length != n) {
            b2 = false;
            return b2;
        }
        return b2;
    }
    
    private boolean match(final char[] array, final char[] array2) {
        return this.match(array, 0, array2, 0);
    }
    
    private List<M> resolveAll(final String key, final char[] array) {
        final ArrayList<Match> value = (ArrayList<Match>)new ArrayList<M>();
        for (final Match match : this.stack) {
            if (this.match(array, match.getPattern().toCharArray())) {
                ((HashMap<String, T>)this.cache).put(key, (T)value);
                value.add(match);
            }
        }
        return (List<M>)value;
    }
    
    @Override
    public boolean add(final M m) {
        this.stack.push(m);
        return true;
    }
    
    @Override
    public void clear() {
        this.cache.clear();
        this.stack.clear();
    }
    
    @Override
    public Iterator<M> iterator() {
        return this.stack.sequence();
    }
    
    public boolean remove(final M o) {
        this.cache.clear();
        return this.stack.remove(o);
    }
    
    public M resolve(final String key) {
        List<M> resolveAll;
        if ((resolveAll = ((LinkedHashMap<K, List<M>>)this.cache).get(key)) == null) {
            resolveAll = this.resolveAll(key);
        }
        Match match;
        if (resolveAll.isEmpty()) {
            match = null;
        }
        else {
            match = resolveAll.get(0);
        }
        return (M)match;
    }
    
    public List<M> resolveAll(final String key) {
        final List<M> list = ((LinkedHashMap<K, List<M>>)this.cache).get(key);
        List<M> resolveAll;
        if (list != null) {
            resolveAll = list;
        }
        else {
            final char[] charArray = key.toCharArray();
            if (charArray == null) {
                resolveAll = null;
            }
            else {
                resolveAll = this.resolveAll(key, charArray);
            }
        }
        return resolveAll;
    }
    
    @Override
    public int size() {
        return this.stack.size();
    }
    
    private class Cache extends LimitedCache<List<M>>
    {
        public Cache() {
            super(1024);
        }
    }
    
    private class Stack extends LinkedList<M>
    {
        public void purge(final int index) {
            Resolver.this.cache.clear();
            this.remove(index);
        }
        
        @Override
        public void push(final M e) {
            Resolver.this.cache.clear();
            this.addFirst(e);
        }
        
        public Iterator<M> sequence() {
            return new Sequence();
        }
        
        private class Sequence implements Iterator<M>
        {
            private int cursor;
            
            public Sequence() {
                this.cursor = Stack.this.size();
            }
            
            @Override
            public boolean hasNext() {
                return this.cursor > 0;
            }
            
            @Override
            public M next() {
                Match match;
                if (this.hasNext()) {
                    final Stack this$1 = Stack.this;
                    final int n = this.cursor - 1;
                    this.cursor = n;
                    match = this$1.get(n);
                }
                else {
                    match = null;
                }
                return (M)match;
            }
            
            @Override
            public void remove() {
                Stack.this.purge(this.cursor);
            }
        }
    }
}
