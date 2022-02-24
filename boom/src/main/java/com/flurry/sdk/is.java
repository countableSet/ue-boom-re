// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.text.TextUtils;
import android.widget.Toast;
import android.content.SharedPreferences$Editor;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class is
{
    public static int a;
    public static int b;
    public static AtomicInteger c;
    static kf<List<it>> d;
    private static is f;
    private static Map<Integer, it> g;
    private String e;
    private final AtomicInteger h;
    private long i;
    private kh<jq> j;
    
    private is() {
        this.e = is.class.getSimpleName();
        this.j = new kh<jq>() {};
        is.g = new HashMap<Integer, it>();
        this.h = new AtomicInteger(0);
        is.c = new AtomicInteger(0);
        if (is.b == 0) {
            is.b = 600000;
        }
        if (is.a == 0) {
            is.a = 15;
        }
        this.i = jy.a().a.getSharedPreferences("FLURRY_SHARED_PREFERENCES", 0).getLong("timeToSendNextPulseReport", 0L);
        if (is.d == null) {
            l();
        }
        ki.a().a("com.flurry.android.sdk.NetworkStateEvent", this.j);
    }
    
    public static is a() {
        synchronized (is.class) {
            if (is.f == null) {
                is.f = new is();
            }
            return is.f;
        }
    }
    
    public static void a(final int a) {
        is.a = a;
    }
    
    public static List<it> b() {
        return new ArrayList<it>(is.g.values());
    }
    
    public static void b(final int b) {
        is.b = b;
    }
    
    private void c(final int n) {
        synchronized (this) {
            km.a(3, this.e, "Removing report " + n + " from PulseCallbackManager");
            is.g.remove(n);
        }
    }
    
    private void c(final iq iq) {
        iq.d = true;
        iq.a();
        is.c.incrementAndGet();
        iq.l.c();
        km.a(3, this.e, iq.l.m.c + " report to " + iq.l.k + " finalized.");
        this.c();
        this.f();
    }
    
    public static List<it> d() {
        if (is.d == null) {
            l();
        }
        return is.d.a();
    }
    
    private void f() {
        if (g() || this.h()) {
            km.a(3, this.e, "Threshold reached. Sending callback logging reports");
            this.i();
        }
    }
    
    private static boolean g() {
        return is.c.intValue() >= is.a;
    }
    
    private boolean h() {
        return System.currentTimeMillis() > this.i;
    }
    
    private void i() {
        for (final it it : b()) {
            final Iterator<ip> iterator2 = it.a().iterator();
            int n = 0;
            while (iterator2.hasNext()) {
                final Iterator<iq> iterator3 = iterator2.next().a.iterator();
                int n2 = n;
                while (true) {
                    n = n2;
                    if (!iterator3.hasNext()) {
                        break;
                    }
                    final iq iq = iterator3.next();
                    if (iq.j) {
                        iterator3.remove();
                    }
                    else {
                        if (iq.f.equals(ir.d)) {
                            continue;
                        }
                        iq.j = true;
                        n2 = 1;
                    }
                }
            }
            if (n != 0) {
                iu.a().a(it);
            }
        }
        iu.a().b();
        this.i = System.currentTimeMillis() + is.b;
        this.j();
        for (final it it2 : b()) {
            if (it2.b()) {
                this.c(it2.b);
            }
            else {
                for (final ip ip : it2.a()) {
                    if (ip.l) {
                        it2.d.remove(ip.b);
                    }
                    else {
                        final Iterator<iq> iterator6 = ip.a.iterator();
                        while (iterator6.hasNext()) {
                            if (iterator6.next().j) {
                                iterator6.remove();
                            }
                        }
                    }
                }
            }
        }
        is.c = new AtomicInteger(0);
        this.c();
    }
    
    private void j() {
        final SharedPreferences$Editor edit = jy.a().a.getSharedPreferences("FLURRY_SHARED_PREFERENCES", 0).edit();
        edit.putLong("timeToSendNextPulseReport", this.i);
        edit.apply();
    }
    
    private int k() {
        synchronized (this) {
            return this.h.incrementAndGet();
        }
    }
    
    private static void l() {
        is.d = new kf<List<it>>(jy.a().a.getFileStreamPath(".yflurryanongoingpulsecallbackreporter"), ".yflurryanongoingpulsecallbackreporter", 2, new lj<List<it>>() {
            @Override
            public final lg<List<it>> a(final int n) {
                return new lf<it>(new it.a());
            }
        });
    }
    
    public final void a(final iq iq) {
        synchronized (this) {
            km.a(3, this.e, iq.l.m.c + " report sent successfully to " + iq.l.k);
            iq.f = ir.a;
            iq.g = "";
            this.c(iq);
            if (km.c() <= 3 && km.d()) {
                jy.a().a(new Runnable() {
                    @Override
                    public final void run() {
                        Toast.makeText(jy.a().a, (CharSequence)("PulseCallbackReportInfo HTTP Response Code: " + iq.e + " for url: " + iq.l.r), 1).show();
                    }
                });
            }
        }
    }
    
    public final void a(final it it) {
        // monitorenter(this)
        Label_0020: {
            if (it != null) {
                break Label_0020;
            }
            try {
                km.a(3, this.e, "Must add valid PulseCallbackAsyncReportInfo");
                Label_0017: {
                    return;
                }
                // iftrue(Label_0017:, !iterator.hasNext())
                // iftrue(Label_0109:, this.i != 0L)
            Block_3_Outer:
                while (true) {
                    Iterator<ip> iterator = null;
                    Block_5: {
                        while (true) {
                            while (true) {
                                final int k = this.k();
                                it.b = k;
                                is.g.put(k, it);
                                iterator = it.a().iterator();
                                break Block_5;
                                this.i = System.currentTimeMillis() + is.b;
                                jy.a().b(new Runnable() {
                                    @Override
                                    public final void run() {
                                        is.this.j();
                                    }
                                });
                                continue Block_3_Outer;
                            }
                            km.a(3, this.e, "Adding and sending " + it.c + " report to PulseCallbackManager.");
                            continue;
                        }
                    }
                    hr.a().c.b(iterator.next());
                    continue;
                }
            }
            // iftrue(Label_0017:, it.a().size() == 0)
            finally {
            }
            // monitorexit(this)
        }
    }
    
    public final boolean a(final iq iq, final String s) {
        boolean b = true;
        synchronized (this) {
            ++iq.h;
            iq.i = System.currentTimeMillis();
            boolean b2;
            if (iq.h > iq.l.d) {
                b2 = true;
            }
            else {
                b2 = false;
            }
            if (b2 || TextUtils.isEmpty((CharSequence)s)) {
                km.a(3, this.e, "Maximum number of redirects attempted. Aborting: " + iq.l.m.c + " report to " + iq.l.k);
                iq.f = ir.c;
                iq.g = "";
                this.c(iq);
                b = false;
            }
            else {
                km.a(3, this.e, "Report to " + iq.l.k + " redirecting to url: " + s);
                iq.l.r = s;
                this.c();
            }
            return b;
        }
    }
    
    public final void b(final iq iq) {
        synchronized (this) {
            km.a(3, this.e, "Maximum number of attempts reached. Aborting: " + iq.l.m.c);
            iq.f = ir.b;
            iq.i = System.currentTimeMillis();
            iq.g = "";
            this.c(iq);
        }
    }
    
    public final void b(final it it) {
        // monitorenter(this)
        Label_0020: {
            if (it != null) {
                break Label_0020;
            }
        Block_6_Outer:
            while (true) {
                try {
                    km.a(3, this.e, "Must add valid PulseCallbackAsyncReportInfo");
                    return;
                    // iftrue(Label_0173:, !iterator.hasNext())
                    // iftrue(Label_0121:, !g())
                    // iftrue(Label_0059:, this.i != 0L)
                    while (true) {
                    Block_7_Outer:
                        while (true) {
                            this.i = System.currentTimeMillis() + is.b;
                            jy.a().b(new Runnable() {
                                @Override
                                public final void run() {
                                    is.this.j();
                                }
                            });
                            while (true) {
                                Label_0059: {
                                    break Label_0059;
                                    km.a(3, this.e, "Max Callback Attempts threshold reached. Sending callback logging reports");
                                    this.i();
                                    break Block_7_Outer;
                                }
                                final int k = this.k();
                                it.b = k;
                                is.g.put(k, it);
                                final Iterator<ip> iterator = it.a().iterator();
                                Label_0096: {
                                    final Iterator<iq> iterator2 = iterator.next().a.iterator();
                                }
                                break Block_7_Outer;
                                final Iterator<iq> iterator2;
                                iterator2.next();
                                is.c.incrementAndGet();
                                continue Block_6_Outer;
                            }
                            continue Block_7_Outer;
                        }
                        continue;
                    }
                }
                // iftrue(Label_0096:, !iterator2.hasNext())
                finally {
                }
                // monitorexit(this)
                Label_0173: {
                    if (this.h()) {
                        km.a(3, this.e, "Time threshold reached. Sending callback logging reports");
                        this.i();
                    }
                }
                final it it2;
                km.a(3, this.e, "Restoring " + it2.c + " report to PulseCallbackManager. Number of stored completed callbacks: " + is.c.get());
            }
        }
    }
    
    public final boolean b(final iq iq, String e) {
        while (true) {
            boolean b = false;
            Label_0200: {
                synchronized (this) {
                    iq.f = ir.c;
                    iq.i = System.currentTimeMillis();
                    String g = e;
                    if (e == null) {
                        g = "";
                    }
                    iq.g = g;
                    final ip l = iq.l;
                    int n;
                    if (l.p >= l.c) {
                        n = 1;
                    }
                    else {
                        n = 0;
                    }
                    if (n != 0) {
                        km.a(3, this.e, "Maximum number of attempts reached. Aborting: " + iq.l.m.c + " report to " + iq.l.k);
                        this.c(iq);
                    }
                    else {
                        if (mc.h(iq.l.r)) {
                            break Label_0200;
                        }
                        e = this.e;
                        km.a(3, e, "Url: " + iq.l.r + " is invalid.");
                        this.c(iq);
                    }
                    return b;
                }
            }
            final iq iq2;
            km.a(3, this.e, "Retrying callback to " + iq2.l.m.c + " in: " + iq2.l.g / 1000L + " seconds.");
            iq2.a();
            is.c.incrementAndGet();
            this.c();
            this.f();
            b = true;
            return b;
        }
    }
    
    public final void c() {
        jy.a().b(new Runnable() {
            @Override
            public final void run() {
                is.a();
                final List<it> b = is.b();
                if (is.d == null) {
                    l();
                }
                is.d.a(b);
            }
        });
    }
}
