// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.io.FilterOutputStream;
import java.util.HashMap;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.Closeable;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;

public final class it
{
    private static final String e;
    public long a;
    int b;
    public String c;
    Map<Long, ip> d;
    private long f;
    private long g;
    private ix h;
    private boolean i;
    private int j;
    private AtomicInteger k;
    
    static {
        e = iu.class.getName();
    }
    
    public it(final String c, final boolean i, final long a, final long g, final ix h, final Map<Long, ip> d) {
        this.c = c;
        this.i = i;
        this.a = a;
        this.g = g;
        this.h = h;
        this.f = System.currentTimeMillis();
        this.d = d;
        if (d != null) {
            final Iterator<Long> iterator = d.keySet().iterator();
            while (iterator.hasNext()) {
                d.get(iterator.next()).m = this;
            }
            this.j = d.size();
        }
        else {
            this.j = 0;
        }
        this.k = new AtomicInteger(0);
    }
    
    public final List<ip> a() {
        List<ip> emptyList;
        if (this.d != null) {
            emptyList = new ArrayList<ip>(this.d.values());
        }
        else {
            emptyList = Collections.emptyList();
        }
        return emptyList;
    }
    
    public final boolean b() {
        synchronized (this) {
            return this.k.intValue() >= this.j;
        }
    }
    
    public final void c() {
        synchronized (this) {
            this.k.incrementAndGet();
        }
    }
    
    public final byte[] d() throws IOException {
        while (true) {
            try {
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                OutputStream outputStream = new DataOutputStream(out);
                Label_0373: {
                    try {
                        ((DataOutputStream)outputStream).writeShort(this.h.e);
                        ((DataOutputStream)outputStream).writeLong(this.a);
                        ((DataOutputStream)outputStream).writeLong(this.g);
                        ((DataOutputStream)outputStream).writeBoolean(this.i);
                        if (this.i) {
                            ((DataOutputStream)outputStream).writeShort(this.b);
                            ((DataOutputStream)outputStream).writeUTF(this.c);
                        }
                        ((DataOutputStream)outputStream).writeShort(this.d.size());
                        if (this.d != null) {
                            for (final Map.Entry<Long, ip> entry : this.d.entrySet()) {
                                final ip ip = entry.getValue();
                                ((DataOutputStream)outputStream).writeLong(entry.getKey());
                                ((DataOutputStream)outputStream).writeUTF(ip.r);
                                ((DataOutputStream)outputStream).writeShort(ip.a.size());
                                for (final iq iq : ip.a) {
                                    ((DataOutputStream)outputStream).writeShort(iq.a);
                                    ((DataOutputStream)outputStream).writeLong(iq.b);
                                    ((DataOutputStream)outputStream).writeLong(iq.c);
                                    ((DataOutputStream)outputStream).writeBoolean(iq.d);
                                    ((DataOutputStream)outputStream).writeShort(iq.e);
                                    ((DataOutputStream)outputStream).writeShort(iq.f.e);
                                    if ((iq.e < 200 || iq.e >= 400) && iq.g != null) {
                                        final byte[] bytes = iq.g.getBytes();
                                        ((DataOutputStream)outputStream).writeShort(bytes.length);
                                        ((FilterOutputStream)outputStream).write(bytes);
                                    }
                                    ((DataOutputStream)outputStream).writeShort(iq.h);
                                    ((DataOutputStream)outputStream).writeInt((int)iq.k);
                                }
                            }
                        }
                        break Label_0373;
                    }
                    catch (IOException out) {
                        OutputStream outputStream2 = outputStream;
                        try {
                            km.a(6, it.e, "Error when generating report", (Throwable)out);
                            throw out;
                        }
                        finally {
                            outputStream = outputStream2;
                            outputStream2 = out;
                        }
                        ly.a(outputStream);
                        throw outputStream2;
                        final byte[] byteArray = out.toByteArray();
                        ly.a(outputStream);
                        return byteArray;
                    }
                }
            }
            catch (IOException out) {
                continue;
            }
            break;
        }
    }
    
    public static final class a implements lg<it>
    {
        lf<ip> a;
        
        public a() {
            this.a = new lf<ip>(new ip.a());
        }
    }
}
