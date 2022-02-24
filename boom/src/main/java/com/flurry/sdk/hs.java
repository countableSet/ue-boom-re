// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.List;
import java.util.Map;
import android.content.Context;
import java.io.File;

public class hs
{
    private static final String b;
    boolean a;
    private final ht c;
    private final File d;
    private String e;
    
    static {
        b = hs.class.getSimpleName();
    }
    
    public hs() {
        this(jy.a().a);
    }
    
    public hs(final Context context) {
        this.c = new ht();
        this.d = context.getFileStreamPath(".flurryinstallreceiver.");
        km.a(3, hs.b, "Referrer file name if it exists:  " + this.d);
    }
    
    private void b(final String e) {
        if (e != null) {
            this.e = e;
        }
    }
    
    private void c() {
        if (!this.a) {
            this.a = true;
            km.a(4, hs.b, "Loading referrer info from file: " + this.d.getAbsolutePath());
            final String c = lx.c(this.d);
            km.a(hs.b, "Referrer file contents: " + c);
            this.b(c);
        }
    }
    
    public final Map<String, List<String>> a() {
        synchronized (this) {
            this.c();
            return ht.a(this.e);
        }
    }
    
    public final void a(final String s) {
        synchronized (this) {
            this.a = true;
            this.b(s);
            lx.a(this.d, this.e);
        }
    }
    
    public final String b() {
        synchronized (this) {
            this.c();
            return this.e;
        }
    }
}
