// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.List;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

public class ip extends kw
{
    private static final String t;
    public ArrayList<iq> a;
    final long b;
    final int c;
    final int d;
    final iw e;
    final Map<String, String> f;
    long g;
    int h;
    int i;
    String j;
    String k;
    boolean l;
    public it m;
    private final int u;
    private final int v;
    
    static {
        t = ip.class.getName();
    }
    
    public ip(final String k, final long b, final String s, final long n, final int n2, final int d, final iw e, final Map<String, String> f, final int h, final int i, final String j) {
        this.u = 1000;
        this.v = 30000;
        this.a(s);
        super.n = n;
        this.a_();
        this.k = k;
        this.b = b;
        super.s = n2;
        this.c = n2;
        this.d = d;
        this.e = e;
        this.f = f;
        this.h = h;
        this.i = i;
        this.j = j;
        this.g = 30000L;
        this.a = new ArrayList<iq>();
    }
    
    @Override
    public final void a_() {
        super.a_();
        if (super.p != 1) {
            this.g *= 3L;
        }
    }
    
    public final void c() {
        synchronized (this) {
            this.m.c();
        }
    }
    
    public final void d() {
        final Iterator<iq> iterator = this.a.iterator();
        while (iterator.hasNext()) {
            iterator.next().l = this;
        }
    }
    
    public static final class a implements lg<ip>
    {
        lf<iq> a;
        
        public a() {
            this.a = new lf<iq>(new iq.a());
        }
    }
}
