// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.Set;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class kd<K, V>
{
    public final Map<K, List<V>> a;
    private int b;
    
    public kd() {
        this.a = new HashMap<K, List<V>>();
    }
    
    public kd(final Map<K, List<V>> a) {
        this.a = a;
    }
    
    public final List<V> a(final K k) {
        List<V> list;
        if (k == null) {
            list = Collections.emptyList();
        }
        else if ((list = this.a(k, false)) == null) {
            list = Collections.emptyList();
        }
        return list;
    }
    
    public final List<V> a(final K k, final boolean b) {
        List<V> list2;
        final List<V> list = list2 = this.a.get(k);
        if (b && (list2 = list) == null) {
            if (this.b > 0) {
                list2 = new ArrayList<V>(this.b);
            }
            else {
                list2 = new ArrayList<V>();
            }
            this.a.put(k, list2);
        }
        return list2;
    }
    
    public final void a() {
        this.a.clear();
    }
    
    public final void a(final kd<K, V> kd) {
        if (kd != null) {
            for (final Map.Entry<K, List<V>> entry : kd.a.entrySet()) {
                this.a(entry.getKey(), true).addAll((Collection<? extends V>)entry.getValue());
            }
        }
    }
    
    public final void a(final K k, final V v) {
        if (k != null) {
            this.a(k, true).add(v);
        }
    }
    
    public final Collection<Map.Entry<K, V>> b() {
        final ArrayList<AbstractMap.SimpleImmutableEntry<K, Object>> list = (ArrayList<AbstractMap.SimpleImmutableEntry<K, Object>>)new ArrayList<Map.Entry<K, V>>();
        for (final Map.Entry<K, List<V>> entry : this.a.entrySet()) {
            final Iterator<V> iterator2 = entry.getValue().iterator();
            while (iterator2.hasNext()) {
                list.add((AbstractMap.SimpleImmutableEntry<K, Object>)new AbstractMap.SimpleImmutableEntry<Object, Object>(entry.getKey(), iterator2.next()));
            }
        }
        return (Collection<Map.Entry<K, V>>)list;
    }
    
    public final boolean b(final K k) {
        return k != null && this.a.remove(k) != null;
    }
    
    public final boolean b(final K k, final V v) {
        boolean remove = false;
        if (k != null) {
            final List<V> a = this.a(k, false);
            if (a != null) {
                remove = a.remove(v);
                if (a.size() == 0) {
                    this.a.remove(k);
                    remove = remove;
                }
            }
        }
        return remove;
    }
    
    public final Set<K> c() {
        return this.a.keySet();
    }
    
    public final Collection<V> d() {
        final ArrayList<V> list = new ArrayList<V>();
        final Iterator<Map.Entry<K, List<V>>> iterator = this.a.entrySet().iterator();
        while (iterator.hasNext()) {
            list.addAll((Collection<? extends V>)iterator.next().getValue());
        }
        return list;
    }
    
    public final int e() {
        final Iterator<Map.Entry<K, List<V>>> iterator = this.a.entrySet().iterator();
        int n = 0;
        while (iterator.hasNext()) {
            n += iterator.next().getValue().size();
        }
        return n;
    }
}
