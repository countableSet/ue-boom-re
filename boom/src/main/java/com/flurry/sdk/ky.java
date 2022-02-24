// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class ky
{
    private kh<jq> a;
    public final String b;
    public Set<String> c;
    public la d;
    public String e;
    
    public ky(final String s, final String b) {
        this.c = new HashSet<String>();
        this.e = "defaultDataKey_";
        this.a = new kh<jq>() {};
        this.b = b;
        ki.a().a("com.flurry.android.sdk.NetworkStateEvent", this.a);
        jy.a().b(new ma() {
            @Override
            public final void a() {
                ky.this.d = new la(s);
            }
        });
    }
    
    public final void a(final String s) {
        jy.a().b(new ma() {
            @Override
            public final void a() {
                if (!ky.this.c.remove(s)) {
                    km.a(6, ky.this.b, "Internal error. Block with id = " + s + " was not in progress state");
                }
            }
        });
    }
    
    public void a(final String s, final String s2, final int n) {
        jy.a().b(new ma() {
            @Override
            public final void a() {
                if (!ky.this.d.a(s, s2)) {
                    km.a(6, ky.this.b, "Internal error. Block wasn't deleted with id = " + s);
                }
                if (!ky.this.c.remove(s)) {
                    km.a(6, ky.this.b, "Internal error. Block with id = " + s + " was not in progress state");
                }
            }
        });
    }
    
    public abstract void a(final byte[] p0, final String p1, final String p2);
    
    public final void b() {
        jy.a().b(new ma() {
            @Override
            public final void a() {
                final ky b = ky.this;
                if (!jr.a().b) {
                    km.a(5, b.b, "Reports were not sent! No Internet connection!");
                }
                else {
                    final ArrayList list = new ArrayList<String>(b.d.c.keySet());
                    if (list.isEmpty()) {
                        km.a(4, b.b, "No more reports to send.");
                    }
                    else {
                        for (final String s : list) {
                            if (!b.c()) {
                                break;
                            }
                            final List<String> a = b.d.a(s);
                            km.a(4, b.b, "Number of not sent blocks = " + a.size());
                            for (final String str : a) {
                                if (!b.c.contains(str)) {
                                    if (!b.c()) {
                                        break;
                                    }
                                    final kz kz = new kf<kz>(jy.a().a.getFileStreamPath(com.flurry.sdk.kz.a(str)), ".yflurrydatasenderblock.", 1, new lj<kz>() {
                                        @Override
                                        public final lg<kz> a(final int n) {
                                            return new kz.a();
                                        }
                                    }).a();
                                    if (kz == null) {
                                        km.a(6, b.b, "Internal ERROR! Cannot read!");
                                        b.d.a(str, s);
                                    }
                                    else {
                                        final byte[] b2 = kz.b;
                                        if (b2 == null || b2.length == 0) {
                                            km.a(6, b.b, "Internal ERROR! Report is empty!");
                                            b.d.a(str, s);
                                        }
                                        else {
                                            km.a(5, b.b, "Reading block info " + str);
                                            b.c.add(str);
                                            b.a(b2, str, s);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
    
    public final void b(final byte[] array, final String s, final String s2) {
        if (array == null || array.length == 0) {
            km.a(6, this.b, "Report that has to be sent is EMPTY or NULL");
        }
        else {
            jy.a().b(new ma() {
                @Override
                public final void a() {
                    final ky d = ky.this;
                    final byte[] a = array;
                    final String string = d.e + s + "_" + s2;
                    final kz kz = new kz(a);
                    final String a2 = kz.a;
                    new kf<kz>(jy.a().a.getFileStreamPath(com.flurry.sdk.kz.a(a2)), ".yflurrydatasenderblock.", 1, (lj<Object>)new lj<kz>() {
                        @Override
                        public final lg<kz> a(final int n) {
                            return new kz.a();
                        }
                    }).a(kz);
                    km.a(5, d.b, "Saving Block File " + a2 + " at " + jy.a().a.getFileStreamPath(com.flurry.sdk.kz.a(a2)));
                    d.d.a(kz, string);
                }
            });
            this.b();
        }
    }
    
    final boolean c() {
        return this.c.size() <= 5;
    }
    
    public interface a
    {
    }
}
