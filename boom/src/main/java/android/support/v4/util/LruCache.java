// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.util;

import java.util.Map;
import java.util.LinkedHashMap;

public class LruCache<K, V>
{
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private final LinkedHashMap<K, V> map;
    private int maxSize;
    private int missCount;
    private int putCount;
    private int size;
    
    public LruCache(final int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap<K, V>(0, 0.75f, true);
    }
    
    private int safeSizeOf(final K obj, final V obj2) {
        final int size = this.sizeOf(obj, obj2);
        if (size < 0) {
            throw new IllegalStateException("Negative size: " + obj + "=" + obj2);
        }
        return size;
    }
    
    protected V create(final K k) {
        return null;
    }
    
    public final int createCount() {
        synchronized (this) {
            return this.createCount;
        }
    }
    
    protected void entryRemoved(final boolean b, final K k, final V v, final V v2) {
    }
    
    public final void evictAll() {
        this.trimToSize(-1);
    }
    
    public final int evictionCount() {
        synchronized (this) {
            return this.evictionCount;
        }
    }
    
    public final V get(final K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        while (true) {
            V value = null;
            V v = null;
            Label_0077: {
                synchronized (this) {
                    value = this.map.get(key);
                    if (value != null) {
                        ++this.hitCount;
                        // monitorexit(this)
                        v = value;
                    }
                    else {
                        ++this.missCount;
                        // monitorexit(this)
                        value = this.create(key);
                        if (value != null) {
                            break Label_0077;
                        }
                        v = null;
                    }
                    return v;
                }
            }
            synchronized (this) {
                ++this.createCount;
                final K k;
                final V put = this.map.put(k, value);
                if (put != null) {
                    this.map.put(k, put);
                }
                else {
                    this.size += this.safeSizeOf(k, value);
                }
                // monitorexit(this)
                if (put != null) {
                    this.entryRemoved(false, k, value, put);
                    return v;
                }
            }
            this.trimToSize(this.maxSize);
            v = value;
            return v;
        }
    }
    
    public final int hitCount() {
        synchronized (this) {
            return this.hitCount;
        }
    }
    
    public final int maxSize() {
        synchronized (this) {
            return this.maxSize;
        }
    }
    
    public final int missCount() {
        synchronized (this) {
            return this.missCount;
        }
    }
    
    public final V put(final K key, final V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }
        synchronized (this) {
            ++this.putCount;
            this.size += this.safeSizeOf(key, value);
            final V put = this.map.put(key, value);
            if (put != null) {
                this.size -= this.safeSizeOf(key, put);
            }
            // monitorexit(this)
            if (put != null) {
                this.entryRemoved(false, key, put, value);
            }
            this.trimToSize(this.maxSize);
            return put;
        }
    }
    
    public final int putCount() {
        synchronized (this) {
            return this.putCount;
        }
    }
    
    public final V remove(final K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            final V remove = this.map.remove(key);
            if (remove != null) {
                this.size -= this.safeSizeOf(key, remove);
            }
            // monitorexit(this)
            if (remove != null) {
                this.entryRemoved(false, key, remove, null);
            }
            return remove;
        }
    }
    
    public void resize(final int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        synchronized (this) {
            this.maxSize = maxSize;
            // monitorexit(this)
            this.trimToSize(maxSize);
        }
    }
    
    public final int size() {
        synchronized (this) {
            return this.size;
        }
    }
    
    protected int sizeOf(final K k, final V v) {
        return 1;
    }
    
    public final Map<K, V> snapshot() {
        synchronized (this) {
            return new LinkedHashMap<K, V>((Map<? extends K, ? extends V>)this.map);
        }
    }
    
    @Override
    public final String toString() {
        int i = 0;
        synchronized (this) {
            final int n = this.hitCount + this.missCount;
            if (n != 0) {
                i = this.hitCount * 100 / n;
            }
            return String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", this.maxSize, this.hitCount, this.missCount, i);
        }
    }
    
    public void trimToSize(final int n) {
        while (true) {
            synchronized (this) {
                if (this.size < 0 || (this.map.isEmpty() && this.size != 0)) {
                    throw new IllegalStateException(this.getClass().getName() + ".sizeOf() is reporting inconsistent results!");
                }
            }
            if (this.size <= n || this.map.isEmpty()) {
                break;
            }
            final Map.Entry<K, V> entry = this.map.entrySet().iterator().next();
            final K key = entry.getKey();
            final V value = entry.getValue();
            this.map.remove(key);
            this.size -= this.safeSizeOf(key, value);
            ++this.evictionCount;
            // monitorexit(this)
            this.entryRemoved(true, key, value, null);
        }
    }
    // monitorexit(this)
}
