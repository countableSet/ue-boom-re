// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

public final class lu
{
    long a;
    boolean b;
    boolean c;
    ma d;
    
    public lu() {
        this.a = 1000L;
        this.b = true;
        this.c = false;
        this.d = new ma() {
            @Override
            public final void a() {
                ki.a().a(new ls());
                if (lu.this.b && lu.this.c) {
                    jy.a().a(lu.this.d, lu.this.a);
                }
            }
        };
    }
    
    public final void a() {
        synchronized (this) {
            if (!this.c) {
                jy.a().a(this.d, this.a);
                this.c = true;
            }
        }
    }
    
    public final void b() {
        synchronized (this) {
            if (this.c) {
                final jy a = jy.a();
                final ma d = this.d;
                if (d != null) {
                    a.c.removeCallbacks((Runnable)d);
                }
                this.c = false;
            }
        }
    }
}
