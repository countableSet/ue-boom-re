// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStream;

public class iq
{
    private static final String m;
    public int a;
    public long b;
    public long c;
    public boolean d;
    public int e;
    public ir f;
    public String g;
    public int h;
    public long i;
    public boolean j;
    public long k;
    public ip l;
    
    static {
        m = iq.class.getName();
    }
    
    public iq(final ip l, final long b, final long c, final int a) {
        this.k = 0L;
        this.l = l;
        this.b = b;
        this.c = c;
        this.a = a;
        this.e = 0;
        this.f = ir.d;
    }
    
    public final void a() {
        this.l.a.add(this);
        if (this.d) {
            this.l.l = true;
        }
    }
    
    public static final class a implements lg<iq>
    {
    }
}
