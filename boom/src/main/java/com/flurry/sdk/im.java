// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.Collection;
import android.location.Location;
import java.util.Collections;
import android.os.Build$VERSION;
import android.os.Build;
import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;
import java.util.HashMap;
import android.text.TextUtils;
import java.util.Iterator;
import java.util.Map;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class im implements a
{
    private static final String a;
    private final Runnable b;
    private final kh<jm> c;
    private final kh<jn> d;
    private final kh<jq> e;
    private final kq<hy> f;
    private final kq<hz> g;
    private kf<ik> h;
    private kf<List<it>> i;
    private final il j;
    private final kd<String, ic> k;
    private final List<it> l;
    private boolean m;
    private String n;
    private boolean o;
    private boolean p;
    private long q;
    private long r;
    private boolean s;
    private hz t;
    private boolean u;
    
    static {
        a = im.class.getSimpleName();
    }
    
    public im() {
        this.b = new ma() {
            @Override
            public final void a() {
                im.this.f();
            }
        };
        this.c = new kh<jm>() {};
        this.d = new kh<jn>() {};
        this.e = new kh<jq>() {};
        this.f = new kq<hy>("proton config request", new iy());
        this.g = new kq<hz>("proton config response", new iz());
        this.j = new il();
        this.k = new kd<String, ic>();
        this.l = new ArrayList<it>();
        this.o = true;
        this.q = 10000L;
        final lp a = lp.a();
        this.m = (boolean)a.a("ProtonEnabled");
        a.a("ProtonEnabled", (lq.a)this);
        km.a(4, im.a, "initSettings, protonEnabled = " + this.m);
        this.n = (String)a.a("ProtonConfigUrl");
        a.a("ProtonConfigUrl", (lq.a)this);
        km.a(4, im.a, "initSettings, protonConfigUrl = " + this.n);
        this.o = (boolean)a.a("analyticsEnabled");
        a.a("analyticsEnabled", (lq.a)this);
        km.a(4, im.a, "initSettings, AnalyticsEnabled = " + this.o);
        ki.a().a("com.flurry.android.sdk.IdProviderFinishedEvent", this.c);
        ki.a().a("com.flurry.android.sdk.IdProviderUpdatedAdvertisingId", this.d);
        ki.a().a("com.flurry.android.sdk.NetworkStateEvent", this.e);
        final Context a2 = jy.a().a;
        this.h = new kf<ik>(a2.getFileStreamPath(".yflurryprotonconfig." + Long.toString(ly.i(jy.a().d), 16)), ".yflurryprotonconfig.", 1, (lj<Object>)new lj<ik>() {
            @Override
            public final lg<ik> a(final int n) {
                return new ik.a();
            }
        });
        this.i = new kf<List<it>>(a2.getFileStreamPath(".yflurryprotonreport." + Long.toString(ly.i(jy.a().d), 16)), ".yflurryprotonreport.", 1, (lj<Object>)new lj<List<it>>() {
            @Override
            public final lg<List<it>> a(final int n) {
                return new lf<it>(new it.a());
            }
        });
        jy.a().b(new ma() {
            @Override
            public final void a() {
                im.this.i();
            }
        });
        jy.a().b(new ma() {
            @Override
            public final void a() {
                im.this.k();
            }
        });
    }
    
    private void a(final long a, final boolean b, final byte[] c) {
        // monitorenter(this)
        if (c != null) {
            try {
                km.a(4, im.a, "Saving proton config response");
                final ik ik = new ik();
                ik.a = a;
                ik.b = b;
                ik.c = c;
                this.h.a(ik);
            }
            finally {
            }
            // monitorexit(this)
        }
    }
    // monitorexit(this)
    
    private void b(final long n) {
        synchronized (this) {
            final Iterator<it> iterator = this.l.iterator();
            while (iterator.hasNext()) {
                if (n == iterator.next().a) {
                    iterator.remove();
                }
            }
        }
    }
    // monitorexit(this)
    
    private void b(final String str, Map<String, String> it) {
        while (true) {
            Label_0077: {
                synchronized (this) {
                    km.a(3, im.a, "Event triggered: " + str);
                    if (!this.o) {
                        km.e(im.a, "Analytics and pulse have been disabled.");
                    }
                    else {
                        if (this.t != null) {
                            break Label_0077;
                        }
                        km.a(3, im.a, "Config response is empty. No events to fire.");
                    }
                    return;
                }
            }
            ly.b();
            final String str2;
            if (TextUtils.isEmpty((CharSequence)str2)) {
                return;
            }
            final List<ic> a = this.k.a(str2);
            if (a == null) {
                km.a(3, im.a, "No events to fire. Returning.");
                return;
            }
            if (a.size() == 0) {
                km.a(3, im.a, "No events to fire. Returning.");
                return;
            }
            final long currentTimeMillis = System.currentTimeMillis();
            final boolean b = it != null;
            ix ix = null;
            switch (str2) {
                default: {
                    ix = com.flurry.sdk.ix.d;
                    break;
                }
                case "flurry.session_start": {
                    ix = com.flurry.sdk.ix.b;
                    break;
                }
                case "flurry.session_end": {
                    ix = com.flurry.sdk.ix.c;
                    break;
                }
                case "flurry.app_install": {
                    ix = com.flurry.sdk.ix.a;
                    break;
                }
            }
            final HashMap<Long, ip> hashMap = new HashMap<Long, ip>();
        Label_0490_Outer:
            for (final ic ic : a) {
                boolean b2 = false;
                Label_0524: {
                    if (ic instanceof id) {
                        km.a(4, im.a, "Event contains triggers.");
                        final String[] d = ((id)ic).d;
                        if (d == null) {
                            km.a(4, im.a, "Template does not contain trigger values. Firing.");
                            b2 = true;
                        }
                        else if (d.length == 0) {
                            km.a(4, im.a, "Template does not contain trigger values. Firing.");
                            b2 = true;
                        }
                        else if (it == null) {
                            km.a(4, im.a, "Publisher has not passed in params list. Not firing.");
                            continue Label_0490_Outer;
                        }
                        final String anObject = ((Map<String, String>)it).get(((id)ic).c);
                        if (anObject == null) {
                            km.a(4, im.a, "Publisher params has no value associated with proton key. Not firing.");
                            continue Label_0490_Outer;
                        }
                        final int length = d.length;
                        int i = 0;
                        while (true) {
                            while (i < length) {
                                if (d[i].equals(anObject)) {
                                    b2 = true;
                                    if (!b2) {
                                        km.a(4, im.a, "Publisher params list does not match proton param values. Not firing.");
                                        continue Label_0490_Outer;
                                    }
                                    km.a(4, im.a, "Publisher params match proton values. Firing.");
                                    break Label_0524;
                                }
                                else {
                                    ++i;
                                }
                            }
                            continue;
                        }
                    }
                }
                final hw b3 = ic.b;
                if (b3 == null) {
                    km.a(3, im.a, "Template is empty. Not firing current event.");
                }
                else {
                    km.a(3, im.a, "Creating callback report for partner: " + b3.b);
                    final HashMap<String, String> hashMap2 = new HashMap<String, String>();
                    hashMap2.put("event_name", str2);
                    hashMap2.put("event_time_millis", String.valueOf(currentTimeMillis));
                    final String a2 = this.j.a(b3.e, hashMap2);
                    String a3 = null;
                    if (b3.f != null) {
                        a3 = this.j.a(b3.f, hashMap2);
                    }
                    hashMap.put(b3.a, new ip(b3.b, b3.a, a2, System.currentTimeMillis() + 259200000L, this.t.e.b, b3.g, b3.d, b3.j, b3.i, b3.h, a3));
                }
            }
            if (hashMap.size() == 0) {
                return;
            }
            jk.a();
            final long c = jk.c();
            jk.a();
            it = new it(str2, b, c, jk.f(), ix, hashMap);
            if ("flurry.session_end".equals(str2)) {
                km.a(3, im.a, "Storing Pulse callbacks for event: " + str2);
                this.l.add(it);
                return;
            }
            km.a(3, im.a, "Firing Pulse callbacks for event: " + str2);
            is.a().a(it);
        }
    }
    
    private static boolean b(final hz hz) {
        boolean b = false;
        if (hz != null) {
            final hx e = hz.e;
        Label_0187:
            while (true) {
                if (e != null && e.a != null) {
                Label_0285:
                    for (int i = 0; i < e.a.size(); ++i) {
                        final hw hw = e.a.get(i);
                        if (hw != null) {
                            Label_0174: {
                                if (!hw.b.equals("") && hw.a != -1L && !hw.e.equals("")) {
                                    final List<ic> c = hw.c;
                                Label_0169:
                                    while (true) {
                                        if (c != null) {
                                            for (final ic ic : c) {
                                                if (ic.a.equals("")) {
                                                    km.a(3, im.a, "An event is missing a name");
                                                    final int n = 0;
                                                    break Label_0169;
                                                }
                                                if (ic instanceof id && ((id)ic).c.equals("")) {
                                                    km.a(3, im.a, "An event trigger is missing a param name");
                                                    final int n = 0;
                                                    break Label_0169;
                                                }
                                            }
                                        }
                                        Label_0279: {
                                            break Label_0279;
                                            final int n;
                                            if (n == 0) {
                                                break Label_0174;
                                            }
                                            continue Label_0285;
                                        }
                                        final int n = 1;
                                        continue Label_0169;
                                    }
                                }
                            }
                            km.a(3, im.a, "A callback template is missing required values");
                            final int n2 = 0;
                            break Label_0187;
                        }
                    }
                }
                Label_0291: {
                    break Label_0291;
                    final int n2;
                    if (n2 == 0 || (hz.e != null && hz.e.e != null && hz.e.e.equals(""))) {
                        km.a(3, im.a, "Config response is missing required values.");
                        return b;
                    }
                    b = true;
                    return b;
                }
                final int n2 = 1;
                continue Label_0187;
            }
        }
        return b;
    }
    
    private void e() {
        synchronized (this) {
            if (this.m) {
                ly.b();
                final SharedPreferences sharedPreferences = jy.a().a.getSharedPreferences("FLURRY_SHARED_PREFERENCES", 0);
                if (sharedPreferences.getBoolean("com.flurry.android.flurryAppInstall", true)) {
                    this.b("flurry.app_install", null);
                    final SharedPreferences$Editor edit = sharedPreferences.edit();
                    edit.putBoolean("com.flurry.android.flurryAppInstall", false);
                    edit.apply();
                }
            }
        }
    }
    
    private void f() {
        boolean m;
        long currentTimeMillis;
        byte[] g;
        ks ks;
        String n;
        String string;
        Label_0078_Outer:Label_0131_Outer:
        while (true) {
            while (true) {
            Label_0385:
                while (true) {
                Label_0281:
                    while (true) {
                        Label_0276: {
                            synchronized (this) {
                                m = this.m;
                                if (m) {
                                    ly.b();
                                    if (this.p && jl.a().b()) {
                                        currentTimeMillis = System.currentTimeMillis();
                                        if (jl.a().c()) {
                                            break Label_0276;
                                        }
                                        m = true;
                                        if (this.t != null) {
                                            if (this.s == m) {
                                                break Label_0281;
                                            }
                                            km.a(3, im.a, "Limit ad tracking value has changed, purging");
                                            this.t = null;
                                        }
                                        jw.a().a(this);
                                        km.a(3, im.a, "Requesting proton config");
                                        g = this.g();
                                        if (g != null) {
                                            ks = new ks();
                                            if (!TextUtils.isEmpty((CharSequence)this.n)) {
                                                break Label_0385;
                                            }
                                            n = "https://proton.flurry.com/sdk/v1/config";
                                            ks.f = n;
                                            ks.w = 5000;
                                            ks.g = ku.a.c;
                                            string = Integer.toString(kq.a(g));
                                            ks.a("Content-Type", "application/x-flurry;version=2");
                                            ks.a("Accept", "application/x-flurry;version=2");
                                            ks.a("FM-Checksum", string);
                                            ks.c = (lg<RequestObjectType>)new lc();
                                            ks.d = (lg<ResponseObjectType>)new lc();
                                            ks.b = (RequestObjectType)(Object)g;
                                            ks.a = (ks.a<RequestObjectType, ResponseObjectType>)new ks.a<byte[], byte[]>() {};
                                            ((kl<ks>)jw.a()).a(this, ks);
                                        }
                                    }
                                }
                                return;
                            }
                        }
                        m = false;
                        continue Label_0078_Outer;
                    }
                    if (System.currentTimeMillis() < this.r + this.t.b * 1000L) {
                        km.a(3, im.a, "Cached Proton config valid, no need to refresh");
                        if (!this.u) {
                            this.u = true;
                            this.b("flurry.session_start", null);
                        }
                        return;
                    }
                    else {
                        if (System.currentTimeMillis() >= this.r + this.t.c * 1000L) {
                            km.a(3, im.a, "Cached Proton config expired, purging");
                            this.t = null;
                            this.k.a();
                            continue Label_0131_Outer;
                        }
                        continue Label_0131_Outer;
                    }
                    break;
                }
                n = this.n;
                continue;
            }
        }
    }
    
    private byte[] g() {
        hy hy = null;
        Label_0389: {
            byte[] a = null;
        Label_0319_Outer:
            while (true) {
                while (true) {
                    ia ia = null;
                Label_0368:
                    while (true) {
                        Label_0363: {
                            try {
                                hy = new hy();
                                hy.a = jy.a().d;
                                hy.b = lv.a(jy.a().a);
                                hy.c = lv.b(jy.a().a);
                                hy.d = jz.a();
                                hy.e = 3;
                                ju.a();
                                hy.f = ju.b();
                                if (!jl.a().c()) {
                                    final boolean g = true;
                                    hy.g = g;
                                    hy.h = new ib();
                                    hy.h.a = new hv();
                                    hy.h.a.a = Build.MODEL;
                                    hy.h.a.b = Build.BRAND;
                                    hy.h.a.c = Build.ID;
                                    hy.h.a.d = Build.DEVICE;
                                    hy.h.a.e = Build.PRODUCT;
                                    hy.h.a.f = Build$VERSION.RELEASE;
                                    hy.i = new ArrayList<ia>();
                                    for (final Map.Entry<Object, Object> entry : Collections.unmodifiableMap((Map<?, ?>)jl.a().a).entrySet()) {
                                        ia = new ia();
                                        ia.a = entry.getKey().c;
                                        if (!entry.getKey().d) {
                                            break Label_0368;
                                        }
                                        ia.b = new String(entry.getValue());
                                        hy.i.add(ia);
                                    }
                                    break Label_0389;
                                }
                                break Label_0363;
                            }
                            catch (Exception obj) {
                                km.a(5, im.a, "Proton config request failed with exception: " + obj);
                                a = null;
                            }
                            break;
                        }
                        final boolean g = false;
                        continue Label_0319_Outer;
                    }
                    Map.Entry<Object, Object> entry = null;
                    ia.b = ly.b(entry.getValue());
                    continue;
                }
            }
            return a;
        }
        final Location g2 = jp.a().g();
        if (g2 != null) {
            final int d = jp.d();
            hy.j = new if();
            hy.j.a = new ie();
            hy.j.a.a = ly.a(g2.getLatitude(), d);
            hy.j.a.b = ly.a(g2.getLongitude(), d);
            hy.j.a.c = (float)ly.a(g2.getAccuracy(), d);
        }
        final String a2 = (String)lp.a().a("UserId");
        if (!a2.equals("")) {
            hy.k = new ii();
            hy.k.a = a2;
        }
        return this.f.a(hy);
    }
    
    private void h() {
        if (this.t != null) {
            km.a(5, im.a, "Processing config response");
            is.a(this.t.e.c);
            is.b(this.t.e.d * 1000);
            final iu a = iu.a();
            final String e = this.t.e.e;
            if (e != null && !e.endsWith(".do")) {
                km.a(5, iu.b, "overriding analytics agent report URL without an endpoint, are you sure?");
            }
            a.a = e;
            if (this.m) {
                lp.a().a("analyticsEnabled", this.t.f.b);
            }
            this.k.a();
            final hx e2 = this.t.e;
            if (e2 != null) {
                final List<hw> a2 = e2.a;
                if (a2 != null) {
                    for (final hw b : a2) {
                        if (b != null) {
                            final List<ic> c = b.c;
                            if (c == null) {
                                continue;
                            }
                            for (final ic ic : c) {
                                if (ic != null && !TextUtils.isEmpty((CharSequence)ic.a)) {
                                    ic.b = b;
                                    this.k.a(ic.a, ic);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void i() {
        synchronized (this) {
            final ik ik = this.h.a();
            Label_0089: {
                if (ik == null) {
                    break Label_0089;
                }
                try {
                    final hz hz = this.g.b(ik.c);
                    hz t = hz;
                    if (!b(hz)) {
                        t = null;
                    }
                    if (t != null) {
                        km.a(4, im.a, "Loaded saved proton config response");
                        this.q = 10000L;
                        this.r = ik.a;
                        this.s = ik.b;
                        this.t = t;
                        this.h();
                    }
                    this.p = true;
                    jy.a().b(new ma() {
                        @Override
                        public final void a() {
                            im.this.f();
                        }
                    });
                }
                catch (Exception obj) {
                    km.a(5, im.a, "Failed to decode saved proton config response: " + obj);
                    this.h.b();
                    final hz hz = null;
                }
            }
        }
    }
    
    private void j() {
        while (true) {
            synchronized (this) {
                if (!this.o) {
                    km.e(im.a, "Analytics disabled, not sending pulse reports.");
                    return;
                }
                km.a(4, im.a, "Sending " + this.l.size() + " queued reports.");
                for (final it it : this.l) {
                    km.a(3, im.a, "Firing Pulse callbacks for event: " + it.c);
                    is.a().a(it);
                }
            }
            this.m();
        }
    }
    
    private void k() {
        synchronized (this) {
            km.a(4, im.a, "Loading queued report data.");
            final List<it> list = this.i.a();
            if (list != null) {
                this.l.addAll(list);
            }
        }
    }
    
    private void l() {
        synchronized (this) {
            km.a(4, im.a, "Saving queued report data.");
            this.i.a(this.l);
        }
    }
    
    private void m() {
        synchronized (this) {
            this.l.clear();
            this.i.b();
        }
    }
    
    public final void a() {
        synchronized (this) {
            if (this.m) {
                ly.b();
                jk.a();
                io.a = jk.c();
                this.u = false;
                this.f();
            }
        }
    }
    
    public final void a(final long n) {
        synchronized (this) {
            if (this.m) {
                ly.b();
                this.b(n);
                this.b("flurry.session_end", null);
                jy.a().b(new ma() {
                    @Override
                    public final void a() {
                        im.this.l();
                    }
                });
            }
        }
    }
    
    @Override
    public final void a(final String s, final Object o) {
        switch (s) {
            default: {
                km.a(6, im.a, "onSettingUpdate internal error!");
                break;
            }
            case "ProtonEnabled": {
                this.m = (boolean)o;
                km.a(4, im.a, "onSettingUpdate, protonEnabled = " + this.m);
                break;
            }
            case "ProtonConfigUrl": {
                this.n = (String)o;
                km.a(4, im.a, "onSettingUpdate, protonConfigUrl = " + this.n);
                break;
            }
            case "analyticsEnabled": {
                this.o = (boolean)o;
                km.a(4, im.a, "onSettingUpdate, AnalyticsEnabled = " + this.o);
                break;
            }
        }
    }
    
    public final void a(final String s, final Map<String, String> map) {
        synchronized (this) {
            if (this.m) {
                ly.b();
                this.b(s, map);
            }
        }
    }
    
    public final void b() {
        synchronized (this) {
            if (this.m) {
                ly.b();
                jk.a();
                this.b(jk.c());
                this.j();
            }
        }
    }
    
    public final void c() {
        synchronized (this) {
            if (this.m) {
                ly.b();
                this.j();
            }
        }
    }
}
