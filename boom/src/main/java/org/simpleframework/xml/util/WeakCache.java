// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.util;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.Iterator;

public class WeakCache<T> implements Cache<T>
{
    private SegmentList list;
    
    public WeakCache() {
        this(10);
    }
    
    public WeakCache(final int n) {
        this.list = new SegmentList(n);
    }
    
    private Segment map(final Object o) {
        return this.list.get(o);
    }
    
    @Override
    public void cache(final Object o, final T t) {
        this.map(o).cache(o, t);
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.map(o).contains(o);
    }
    
    @Override
    public T fetch(final Object o) {
        return this.map(o).fetch(o);
    }
    
    @Override
    public boolean isEmpty() {
        final Iterator<Segment> iterator = this.list.iterator();
        while (iterator.hasNext()) {
            if (!((Segment)iterator.next()).isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public T take(final Object o) {
        return this.map(o).take(o);
    }
    
    private class Segment extends WeakHashMap<Object, T>
    {
        public void cache(final Object key, final T value) {
            synchronized (this) {
                this.put(key, value);
            }
        }
        
        public boolean contains(final Object key) {
            synchronized (this) {
                return this.containsKey(key);
            }
        }
        
        public T fetch(Object value) {
            synchronized (this) {
                value = ((WeakHashMap<K, Object>)this).get(value);
                return (T)value;
            }
        }
        
        public T take(Object remove) {
            synchronized (this) {
                remove = ((WeakHashMap<K, Object>)this).remove(remove);
                return (T)remove;
            }
        }
    }
    
    private class SegmentList implements Iterable<Segment>
    {
        private List<Segment> list;
        private int size;
        
        public SegmentList(final int size) {
            this.list = new ArrayList<Segment>();
            this.create(this.size = size);
        }
        
        private void create(int i) {
            while (i > 0) {
                this.list.add(new Segment());
                --i;
            }
        }
        
        private int segment(final Object o) {
            return Math.abs(o.hashCode() % this.size);
        }
        
        public Segment get(final Object o) {
            final int segment = this.segment(o);
            Segment segment2;
            if (segment < this.size) {
                segment2 = this.list.get(segment);
            }
            else {
                segment2 = null;
            }
            return segment2;
        }
        
        @Override
        public Iterator<Segment> iterator() {
            return this.list.iterator();
        }
    }
}
