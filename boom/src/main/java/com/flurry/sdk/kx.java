// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public abstract class kx<ReportInfo extends kw>
{
    private static final String a;
    public static long b;
    public boolean c;
    public long d;
    private final int e;
    private final kf<List<ReportInfo>> f;
    private final List<ReportInfo> g;
    private int h;
    private final Runnable i;
    private final kh<jq> j;
    
    static {
        a = kx.class.getSimpleName();
        kx.b = 10000L;
    }
    
    public kx() {
        this.e = Integer.MAX_VALUE;
        this.g = new ArrayList<ReportInfo>();
        this.i = new ma() {
            @Override
            public final void a() {
                kx.this.b();
            }
        };
        this.j = new kh<jq>() {};
        ki.a().a("com.flurry.android.sdk.NetworkStateEvent", this.j);
        this.f = this.a();
        this.d = kx.b;
        this.h = -1;
        jy.a().b(new ma() {
            @Override
            public final void a() {
                kx.this.b(kx.this.g);
                kx.this.b();
            }
        });
    }
    
    private void b() {
        while (true) {
            Label_0038: {
                synchronized (this) {
                    if (!this.c) {
                        if (this.h < 0) {
                            break Label_0038;
                        }
                        km.a(3, kx.a, "Transmit is in progress");
                    }
                    return;
                }
            }
            this.f();
            if (this.g.isEmpty()) {
                this.d = kx.b;
                this.h = -1;
                return;
            }
            this.h = 0;
            jy.a().b(new ma() {
                @Override
                public final void a() {
                    kx.this.d();
                }
            });
        }
    }
    
    private void d() {
        synchronized (this) {
            ly.b();
        Label_0065:
            while (true) {
                Label_0085: {
                    if (jr.a().b) {
                        while (this.h < this.g.size()) {
                            final kw kw = this.g.get(this.h++);
                            if (!kw.o) {
                                break Label_0065;
                            }
                        }
                        break Label_0085;
                    }
                    km.a(3, kx.a, "Network is not available, aborting transmission");
                    break Label_0085;
                    kw kw = null;
                    if (kw == null) {
                        this.e();
                    }
                    else {
                        this.a((ReportInfo)kw);
                    }
                    return;
                }
                final kw kw = null;
                continue Label_0065;
            }
        }
    }
    
    private void e() {
        while (true) {
            while (true) {
                Label_0081: {
                    synchronized (this) {
                        this.f();
                        this.a(this.g);
                        if (this.c) {
                            km.a(3, kx.a, "Reporter paused");
                            this.d = kx.b;
                        }
                        else {
                            if (!this.g.isEmpty()) {
                                break Label_0081;
                            }
                            km.a(3, kx.a, "All reports sent successfully");
                            this.d = kx.b;
                        }
                        this.h = -1;
                        return;
                    }
                }
                this.d <<= 1;
                km.a(3, kx.a, "One or more reports failed to send, backing off: " + this.d + "ms");
                jy.a().a(this.i, this.d);
                continue;
            }
        }
    }
    
    private void f() {
        while (true) {
            while (true) {
                final Iterator<ReportInfo> iterator;
                kw kw = null;
                Label_0097: {
                    synchronized (this) {
                        iterator = this.g.iterator();
                        while (iterator.hasNext()) {
                            kw = iterator.next();
                            if (!kw.o) {
                                break Label_0097;
                            }
                            km.a(3, kx.a, "Url transmitted - " + kw.q + " Attempts: " + kw.p);
                            iterator.remove();
                        }
                        break;
                    }
                }
                if (kw.p > kw.a()) {
                    km.a(3, kx.a, "Exceeded max no of attempts - " + kw.q + " Attempts: " + kw.p);
                    iterator.remove();
                    continue;
                }
                if (System.currentTimeMillis() > kw.n && kw.p > 0) {
                    km.a(3, kx.a, "Expired: Time expired - " + kw.q + " Attempts: " + kw.p);
                    iterator.remove();
                    continue;
                }
                continue;
            }
        }
    }
    // monitorexit(this)
    
    public abstract kf<List<ReportInfo>> a();
    
    public abstract void a(final ReportInfo p0);
    
    public void a(final List<ReportInfo> c) {
        synchronized (this) {
            ly.b();
            this.f.a(new ArrayList<ReportInfo>((Collection<? extends ReportInfo>)c));
        }
    }
    
    public final void b(final ReportInfo reportInfo) {
        // monitorenter(this)
        if (reportInfo != null) {
            try {
                this.g.add(reportInfo);
                jy.a().b(new ma() {
                    @Override
                    public final void a() {
                        kx.this.b();
                    }
                });
            }
            finally {
            }
            // monitorexit(this)
        }
    }
    // monitorexit(this)
    
    public void b(final List<ReportInfo> list) {
        synchronized (this) {
            ly.b();
            final List<ReportInfo> list2 = this.f.a();
            if (list2 != null) {
                list.addAll((Collection<? extends ReportInfo>)list2);
            }
        }
    }
    
    public final void c() {
        this.c = false;
        jy.a().b(new ma() {
            @Override
            public final void a() {
                kx.this.b();
            }
        });
    }
    
    public final void c(final ReportInfo reportInfo) {
        synchronized (this) {
            reportInfo.o = true;
            jy.a().b(new ma() {
                @Override
                public final void a() {
                    kx.this.d();
                }
            });
        }
    }
    
    public final void d(final ReportInfo reportInfo) {
        synchronized (this) {
            reportInfo.a_();
            jy.a().b(new ma() {
                @Override
                public final void a() {
                    kx.this.d();
                }
            });
        }
    }
}
