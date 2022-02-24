// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

public final class lt
{
    private static long a;
    private static lt b;
    private final lu c;
    
    static {
        lt.a = 100L;
        lt.b = null;
    }
    
    public lt() {
        this.c = new lu();
        this.c.a = lt.a;
        this.c.b = true;
    }
    
    public static lt a() {
        synchronized (lt.class) {
            if (lt.b == null) {
                lt.b = new lt();
            }
            return lt.b;
        }
    }
    
    public final void a(final kh<ls> kh) {
        synchronized (this) {
            ki.a().a("com.flurry.android.sdk.TickEvent", kh);
            if (ki.a().a("com.flurry.android.sdk.TickEvent") > 0) {
                this.c.a();
            }
        }
    }
    
    public final void b(final kh<ls> kh) {
        synchronized (this) {
            ki.a().b("com.flurry.android.sdk.TickEvent", kh);
            if (ki.a().a("com.flurry.android.sdk.TickEvent") == 0) {
                this.c.b();
            }
        }
    }
}
