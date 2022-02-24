// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.Iterator;
import java.util.LinkedList;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class lq
{
    private Map<String, Object> a;
    private Map<String, List<a>> b;
    
    public lq() {
        this.a = new HashMap<String, Object>();
        this.b = new HashMap<String, List<a>>();
    }
    
    public final Object a(final String s) {
        synchronized (this) {
            return this.a.get(s);
        }
    }
    
    public final void a(final String s, final a a) {
        synchronized (this) {
            if (!TextUtils.isEmpty((CharSequence)s) && a != null) {
                List<a> list;
                if ((list = this.b.get(s)) == null) {
                    list = new LinkedList<a>();
                }
                list.add(a);
                this.b.put(s, list);
            }
        }
    }
    
    public final void a(final String s, final Object o) {
    Label_0068_Outer:
        while (true) {
            while (true) {
            Label_0142:
                while (true) {
                    Label_0136: {
                        synchronized (this) {
                            if (!TextUtils.isEmpty((CharSequence)s)) {
                                final Object value = this.a.get(s);
                                if (o != value && (o == null || !o.equals(value))) {
                                    break Label_0136;
                                }
                                final int n = 1;
                                if (n == 0) {
                                    if (o != null) {
                                        break Label_0142;
                                    }
                                    this.a.remove(s);
                                    if (this.b.get(s) != null) {
                                        final Iterator<a> iterator = this.b.get(s).iterator();
                                        while (iterator.hasNext()) {
                                            iterator.next().a(s, o);
                                        }
                                    }
                                }
                            }
                            return;
                        }
                    }
                    final int n = 0;
                    continue Label_0068_Outer;
                }
                this.a.put(s, o);
                continue;
            }
        }
    }
    
    public final boolean b(final String s, final a a) {
        synchronized (this) {
            boolean b;
            if (TextUtils.isEmpty((CharSequence)s)) {
                b = false;
            }
            else if (a == null) {
                b = false;
            }
            else {
                final List<a> list = this.b.get(s);
                b = (list != null && list.remove(a));
            }
            return b;
        }
    }
    
    public interface a
    {
        void a(final String p0, final Object p1);
    }
}
