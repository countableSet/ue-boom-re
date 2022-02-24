// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.annotation.TargetApi;
import android.os.Bundle;
import java.util.Arrays;
import android.app.Activity;
import java.util.TimeZone;
import java.io.IOException;
import android.os.SystemClock;
import com.flurry.android.FlurryEventRecordStatus;
import android.content.SharedPreferences$Editor;
import java.util.Collections;
import java.util.Collection;
import android.content.SharedPreferences;
import android.text.TextUtils;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;
import android.content.Context;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

public class jh implements a
{
    static final String a;
    static int b;
    static int c;
    static int d;
    static int e;
    static int f;
    private final List<jc> A;
    private boolean B;
    private int C;
    private final List<ja> D;
    private int E;
    private int F;
    private final hs G;
    WeakReference<lk> g;
    File h;
    kf<List<jf>> i;
    public boolean j;
    boolean k;
    String l;
    byte m;
    Long n;
    boolean o;
    final kh<jm> p;
    private final AtomicInteger q;
    private final AtomicInteger r;
    private final kh<ll> s;
    private long t;
    private String u;
    private int v;
    private final List<jf> w;
    private final Map<String, List<String>> x;
    private final Map<String, String> y;
    private final Map<String, jb> z;
    
    static {
        a = jh.class.getSimpleName();
        jh.b = 100;
        jh.c = 10;
        jh.d = 1000;
        jh.e = 160000;
        jh.f = 50;
    }
    
    public jh() {
        this.q = new AtomicInteger(0);
        this.r = new AtomicInteger(0);
        this.s = new kh<ll>() {};
        this.v = -1;
        this.w = new ArrayList<jf>();
        this.x = new HashMap<String, List<String>>();
        this.y = new HashMap<String, String>();
        this.z = new HashMap<String, jb>();
        this.A = new ArrayList<jc>();
        this.B = true;
        this.C = 0;
        this.D = new ArrayList<ja>();
        this.E = 0;
        this.F = 0;
        this.o = true;
        this.G = new hs();
        this.p = new kh<jm>() {};
        ki.a().a("com.flurry.android.sdk.FlurrySessionEvent", this.s);
    }
    
    private void a(final boolean p0, final long p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: monitorenter   
        //     2: aload_0        
        //     3: getfield        com/flurry/sdk/jh.o:Z
        //     6: ifne            21
        //     9: iconst_3       
        //    10: getstatic       com/flurry/sdk/jh.a:Ljava/lang/String;
        //    13: ldc             "Analytics disabled, not sending agent report."
        //    15: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //    18: aload_0        
        //    19: monitorexit    
        //    20: return         
        //    21: iload_1        
        //    22: ifne            37
        //    25: aload_0        
        //    26: getfield        com/flurry/sdk/jh.w:Ljava/util/List;
        //    29: invokeinterface java/util/List.isEmpty:()Z
        //    34: ifne            18
        //    37: iconst_3       
        //    38: getstatic       com/flurry/sdk/jh.a:Ljava/lang/String;
        //    41: ldc             "generating agent report"
        //    43: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //    46: new             Lcom/flurry/sdk/jd;
        //    49: astore          4
        //    51: aload           4
        //    53: invokestatic    com/flurry/sdk/jy.a:()Lcom/flurry/sdk/jy;
        //    56: getfield        com/flurry/sdk/jy.d:Ljava/lang/String;
        //    59: invokestatic    com/flurry/sdk/ju.a:()Lcom/flurry/sdk/ju;
        //    62: invokevirtual   com/flurry/sdk/ju.g:()Ljava/lang/String;
        //    65: aload_0        
        //    66: getfield        com/flurry/sdk/jh.j:Z
        //    69: invokestatic    com/flurry/sdk/jl.a:()Lcom/flurry/sdk/jl;
        //    72: invokevirtual   com/flurry/sdk/jl.c:()Z
        //    75: aload_0        
        //    76: getfield        com/flurry/sdk/jh.t:J
        //    79: lload_2        
        //    80: aload_0        
        //    81: getfield        com/flurry/sdk/jh.w:Ljava/util/List;
        //    84: invokestatic    com/flurry/sdk/jl.a:()Lcom/flurry/sdk/jl;
        //    87: getfield        com/flurry/sdk/jl.a:Ljava/util/Map;
        //    90: invokestatic    java/util/Collections.unmodifiableMap:(Ljava/util/Map;)Ljava/util/Map;
        //    93: aload_0        
        //    94: getfield        com/flurry/sdk/jh.G:Lcom/flurry/sdk/hs;
        //    97: invokevirtual   com/flurry/sdk/hs.a:()Ljava/util/Map;
        //   100: aload_0        
        //   101: getfield        com/flurry/sdk/jh.x:Ljava/util/Map;
        //   104: invokestatic    com/flurry/sdk/ka.a:()Lcom/flurry/sdk/ka;
        //   107: invokevirtual   com/flurry/sdk/ka.b:()Ljava/util/HashMap;
        //   110: invokestatic    java/lang/System.currentTimeMillis:()J
        //   113: invokespecial   com/flurry/sdk/jd.<init>:(Ljava/lang/String;Ljava/lang/String;ZZJJLjava/util/List;Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;J)V
        //   116: aload           4
        //   118: getfield        com/flurry/sdk/jd.a:[B
        //   121: astore          4
        //   123: aload           4
        //   125: ifnonnull       205
        //   128: getstatic       com/flurry/sdk/jh.a:Ljava/lang/String;
        //   131: ldc_w           "Error generating report"
        //   134: invokestatic    com/flurry/sdk/km.e:(Ljava/lang/String;Ljava/lang/String;)V
        //   137: aload_0        
        //   138: getfield        com/flurry/sdk/jh.w:Ljava/util/List;
        //   141: invokeinterface java/util/List.clear:()V
        //   146: aload_0        
        //   147: getfield        com/flurry/sdk/jh.i:Lcom/flurry/sdk/kf;
        //   150: invokevirtual   com/flurry/sdk/kf.b:()Z
        //   153: pop            
        //   154: goto            18
        //   157: astore          4
        //   159: aload_0        
        //   160: monitorexit    
        //   161: aload           4
        //   163: athrow         
        //   164: astore          4
        //   166: getstatic       com/flurry/sdk/jh.a:Ljava/lang/String;
        //   169: astore          5
        //   171: new             Ljava/lang/StringBuilder;
        //   174: astore          6
        //   176: aload           6
        //   178: ldc_w           "Exception while generating report: "
        //   181: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   184: aload           5
        //   186: aload           6
        //   188: aload           4
        //   190: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   193: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   196: invokestatic    com/flurry/sdk/km.e:(Ljava/lang/String;Ljava/lang/String;)V
        //   199: aconst_null    
        //   200: astore          4
        //   202: goto            123
        //   205: getstatic       com/flurry/sdk/jh.a:Ljava/lang/String;
        //   208: astore          6
        //   210: new             Ljava/lang/StringBuilder;
        //   213: astore          5
        //   215: aload           5
        //   217: ldc_w           "generated report of size "
        //   220: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   223: iconst_3       
        //   224: aload           6
        //   226: aload           5
        //   228: aload           4
        //   230: arraylength    
        //   231: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   234: ldc_w           " with "
        //   237: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   240: aload_0        
        //   241: getfield        com/flurry/sdk/jh.w:Ljava/util/List;
        //   244: invokeinterface java/util/List.size:()I
        //   249: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   252: ldc_w           " reports."
        //   255: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   258: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   261: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //   264: invokestatic    com/flurry/sdk/hr.a:()Lcom/flurry/sdk/hr;
        //   267: getfield        com/flurry/sdk/hr.b:Lcom/flurry/sdk/je;
        //   270: astore          5
        //   272: new             Ljava/lang/StringBuilder;
        //   275: astore          6
        //   277: aload           6
        //   279: invokespecial   java/lang/StringBuilder.<init>:()V
        //   282: aload           6
        //   284: invokestatic    com/flurry/sdk/jz.a:()I
        //   287: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   290: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   293: astore          6
        //   295: aload           5
        //   297: aload           4
        //   299: invokestatic    com/flurry/sdk/jy.a:()Lcom/flurry/sdk/jy;
        //   302: getfield        com/flurry/sdk/jy.d:Ljava/lang/String;
        //   305: aload           6
        //   307: invokevirtual   com/flurry/sdk/je.b:([BLjava/lang/String;Ljava/lang/String;)V
        //   310: goto            137
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  2      18     157    164    Any
        //  25     37     157    164    Any
        //  37     46     157    164    Any
        //  46     123    164    205    Ljava/lang/Exception;
        //  46     123    157    164    Any
        //  128    137    157    164    Any
        //  137    154    157    164    Any
        //  166    199    157    164    Any
        //  205    310    157    164    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0123:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private void b(final long n) {
        synchronized (this) {
            for (final jc jc : this.A) {
                if (jc.b && !jc.c) {
                    jc.a(n);
                }
            }
        }
    }
    // monitorexit(this)
    
    static /* synthetic */ void d(final jh jh) {
        final SharedPreferences sharedPreferences = jy.a().a.getSharedPreferences("FLURRY_SHARED_PREFERENCES", 0);
        jh.j = sharedPreferences.getBoolean("com.flurry.sdk.previous_successful_report", false);
        jk.a();
        jh.t = sharedPreferences.getLong("com.flurry.sdk.initial_run_time", jk.c());
        jh.u = sharedPreferences.getString("com.flurry.sdk.api_key", "");
        if (TextUtils.isEmpty((CharSequence)jh.u) && jh.t > 0L) {
            jh.u = jy.a().d;
        }
        else if (!jh.u.equals(jy.a().d)) {
            jk.a();
            jh.t = jk.c();
        }
    }
    
    private void e() {
        synchronized (this) {
            km.a(4, jh.a, "Loading persistent session report data.");
            final List<jf> list = this.i.a();
            if (list != null) {
                this.w.addAll(list);
            }
            else if (this.h.exists()) {
                km.a(4, jh.a, "Legacy persistent agent data found, converting.");
                final ji a = hu.a(this.h);
                if (a != null) {
                    final boolean a2 = a.a;
                    long t;
                    if ((t = a.b) <= 0L) {
                        jk.a();
                        t = jk.c();
                    }
                    this.j = a2;
                    this.t = t;
                    this.f();
                    final List<Object> unmodifiableList = Collections.unmodifiableList((List<?>)a.c);
                    if (unmodifiableList != null) {
                        this.w.addAll((Collection<? extends jf>)unmodifiableList);
                    }
                }
                this.h.delete();
                this.d();
            }
        }
    }
    
    private void f() {
        final SharedPreferences$Editor edit = jy.a().a.getSharedPreferences("FLURRY_SHARED_PREFERENCES", 0).edit();
        edit.putBoolean("com.flurry.sdk.previous_successful_report", this.j);
        edit.putLong("com.flurry.sdk.initial_run_time", this.t);
        edit.putString("com.flurry.sdk.api_key", jy.a().d);
        edit.apply();
    }
    
    public final FlurryEventRecordStatus a(String a, final String s, final Map<String, String> map) {
        synchronized (this) {
            FlurryEventRecordStatus obj = FlurryEventRecordStatus.kFlurryEventFailed;
            if (map != null && !TextUtils.isEmpty((CharSequence)s)) {
                map.put("\ue8ffsid+Tumblr", s);
                obj = this.a(a, map, false);
                a = jh.a;
                km.a(5, a, "logEvent status for syndication:" + obj);
            }
            return obj;
        }
    }
    
    public final FlurryEventRecordStatus a(String a, Map<String, String> emptyMap, final boolean b) {
        while (true) {
            while (true) {
                Label_0503: {
                    FlurryEventRecordStatus flurryEventRecordStatus = null;
                    Label_0495: {
                        Label_0467: {
                            synchronized (this) {
                                final FlurryEventRecordStatus kFlurryEventRecorded = FlurryEventRecordStatus.kFlurryEventRecorded;
                                if (!this.o) {
                                    flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventAnalyticsDisabled;
                                    km.e(jh.a, "Analytics has been disabled, not logging event.");
                                }
                                else {
                                    final long elapsedRealtime = SystemClock.elapsedRealtime();
                                    jk.a();
                                    final long d = jk.d();
                                    final String b2 = ly.b(a);
                                    if (b2.length() == 0) {
                                        flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventFailed;
                                    }
                                    else {
                                        final jb jb = this.z.get(b2);
                                        if (jb == null) {
                                            if (this.z.size() < jh.b) {
                                                final jb jb2 = new jb();
                                                jb2.a = 1;
                                                this.z.put(b2, jb2);
                                                km.e(jh.a, "Event count started: " + b2);
                                            }
                                            else {
                                                a = jh.a;
                                                km.e(a, "Too many different events. Event not counted: " + b2);
                                                final FlurryEventRecordStatus kFlurryEventUniqueCountExceeded = FlurryEventRecordStatus.kFlurryEventUniqueCountExceeded;
                                            }
                                        }
                                        else {
                                            ++jb.a;
                                            km.e(jh.a, "Event count incremented: " + b2);
                                            final FlurryEventRecordStatus kFlurryEventRecorded2 = FlurryEventRecordStatus.kFlurryEventRecorded;
                                        }
                                        if (!this.k || this.A.size() >= jh.d || this.C >= jh.e) {
                                            break Label_0495;
                                        }
                                        if (emptyMap != null) {
                                            break Label_0503;
                                        }
                                        emptyMap = Collections.emptyMap();
                                        if (emptyMap.size() > jh.c) {
                                            km.e(jh.a, "MaxEventParams exceeded: " + emptyMap.size());
                                            flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventParamsCountExceeded;
                                        }
                                        else {
                                            final jc jc = new jc(this.q.incrementAndGet(), b2, emptyMap, elapsedRealtime - d, b);
                                            if (jc.b().length + this.C > jh.e) {
                                                break Label_0467;
                                            }
                                            this.A.add(jc);
                                            this.C += jc.b().length;
                                            final FlurryEventRecordStatus flurryEventRecordStatus2 = flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventRecorded;
                                            if (this.o) {
                                                flurryEventRecordStatus = flurryEventRecordStatus2;
                                                if (hr.a().a != null) {
                                                    jy.a().b(new Runnable() {
                                                        @Override
                                                        public final void run() {
                                                            hr.a().a.a(b2, emptyMap);
                                                        }
                                                    });
                                                    flurryEventRecordStatus = flurryEventRecordStatus2;
                                                }
                                            }
                                        }
                                    }
                                }
                                return flurryEventRecordStatus;
                            }
                        }
                        this.C = jh.e;
                        this.B = false;
                        km.e(jh.a, "Event Log size exceeded. No more event details logged.");
                        flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventLogCountExceeded;
                        return flurryEventRecordStatus;
                    }
                    this.B = false;
                    return flurryEventRecordStatus;
                }
                continue;
            }
        }
    }
    
    final jf a(final long b, final long c, final long d, int n) {
        synchronized (this) {
            final jg jg = new jg();
            jg.a = ju.a().g();
            jg.b = b;
            jg.c = c;
            jg.d = d;
            jg.e = this.y;
            jk.a();
            final jx h = jk.h();
            while (true) {
            Label_0342_Outer:
                while (true) {
                    Label_0183: {
                        String d2 = null;
                        String l;
                        jf jf;
                        String e = null;
                        Map<String, String> f = null;
                        jx h2;
                        jx h3;
                        Label_0330_Outer:Label_0336_Outer:
                        while (true) {
                            Label_0128: {
                                while (true) {
                                    Label_0100: {
                                        while (true) {
                                            Label_0072: {
                                                if (h != null) {
                                                    d2 = h.d();
                                                    break Label_0072;
                                                }
                                                Label_0324: {
                                                    break Label_0324;
                                                    while (true) {
                                                        jg.m = l;
                                                        jg.n = jp.a().g();
                                                        jg.o = this.F;
                                                        jg.p = this.m;
                                                        jg.q = this.n;
                                                        jg.r = this.z;
                                                        jg.s = this.A;
                                                        jg.t = this.B;
                                                        jg.v = this.D;
                                                        jg.u = this.E;
                                                        try {
                                                            jf = new jf(jg);
                                                            if (jf == null) {
                                                                km.e(jh.a, "New session report wasn't created");
                                                            }
                                                            return jf;
                                                            n = lw.e();
                                                            break Label_0183;
                                                            l = this.l;
                                                            continue Label_0342_Outer;
                                                            e = null;
                                                            break Label_0100;
                                                            f = null;
                                                            break Label_0128;
                                                            d2 = null;
                                                        }
                                                        catch (IOException obj) {
                                                            km.a(5, jh.a, "Error creating analytics session report: " + obj);
                                                            jf = null;
                                                        }
                                                    }
                                                }
                                            }
                                            jg.f = d2;
                                            jk.a();
                                            h2 = jk.h();
                                            if (h2 == null) {
                                                continue Label_0336_Outer;
                                            }
                                            break;
                                        }
                                        e = h2.e();
                                    }
                                    jg.g = e;
                                    jk.a();
                                    h3 = jk.h();
                                    if (h3 == null) {
                                        continue;
                                    }
                                    break;
                                }
                                f = h3.f();
                            }
                            jg.h = f;
                            jo.a();
                            jg.i = jo.b();
                            jo.a();
                            jg.j = TimeZone.getDefault().getID();
                            jg.k = n;
                            if (this.v == -1) {
                                continue Label_0330_Outer;
                            }
                            break;
                        }
                        n = this.v;
                    }
                    jg.l = n;
                    if (this.l == null) {
                        final String l = "";
                        continue Label_0342_Outer;
                    }
                    break;
                }
                continue;
            }
        }
    }
    
    public final void a() {
        synchronized (this) {
            this.v = lw.e();
            if (hr.a().c != null) {
                jy.a().b(new ma() {
                    @Override
                    public final void a() {
                        hr.a().c.c();
                    }
                });
            }
            if (this.o && hr.a().a != null) {
                jy.a().b(new ma() {
                    @Override
                    public final void a() {
                        hr.a().a.b();
                    }
                });
            }
        }
    }
    
    public final void a(final long n) {
        synchronized (this) {
            ki.a().a(this.p);
            jk.a();
            this.b(jk.e());
            jy.a().b(new ma() {
                @Override
                public final void a() {
                    if (jh.this.o && hr.a().a != null) {
                        hr.a().a.c();
                    }
                    if (hr.a().c != null) {
                        jy.a().b(new ma() {
                            @Override
                            public final void a() {
                                hr.a().c.c = true;
                            }
                        });
                    }
                }
            });
            if (jl.a().b()) {
                jy.a().b(new ma() {
                    @Override
                    public final void a() {
                        jh.this.a(false, n);
                    }
                });
            }
            lp.a().b("Gender", (lq.a)this);
            lp.a().b("UserId", (lq.a)this);
            lp.a().b("Age", (lq.a)this);
            lp.a().b("LogEvents", (lq.a)this);
        }
    }
    
    final void a(final Context context) {
        if (context instanceof Activity) {
            final Bundle extras = ((Activity)context).getIntent().getExtras();
            if (extras != null) {
                km.a(3, jh.a, "Launch Options Bundle is present " + extras.toString());
                for (final String str : extras.keySet()) {
                    if (str != null) {
                        final Object value = extras.get(str);
                        String string;
                        if (value != null) {
                            string = value.toString();
                        }
                        else {
                            string = "null";
                        }
                        this.x.put(str, new ArrayList<String>(Arrays.asList(string)));
                        km.a(3, jh.a, "Launch options Key: " + str + ". Its value: " + string);
                    }
                }
            }
        }
    }
    
    @Override
    public final void a(final String s, final Object o) {
        switch (s) {
            default: {
                km.a(6, jh.a, "onSettingUpdate internal error!");
                break;
            }
            case "LogEvents": {
                this.k = (boolean)o;
                km.a(4, jh.a, "onSettingUpdate, LogEvents = " + this.k);
                break;
            }
            case "UserId": {
                this.l = (String)o;
                km.a(4, jh.a, "onSettingUpdate, UserId = " + this.l);
                break;
            }
            case "Gender": {
                this.m = (byte)o;
                km.a(4, jh.a, "onSettingUpdate, Gender = " + this.m);
                break;
            }
            case "Age": {
                this.n = (Long)o;
                km.a(4, jh.a, "onSettingUpdate, Birthdate = " + this.n);
                break;
            }
            case "analyticsEnabled": {
                this.o = (boolean)o;
                km.a(4, jh.a, "onSettingUpdate, AnalyticsEnabled = " + this.o);
                break;
            }
        }
    }
    
    public final void a(String a, String s, final String s2, final Throwable t) {
        // monitorenter(this)
        Label_0125: {
            if (a == null) {
                break Label_0125;
            }
            while (true) {
                Label_0254: {
                    while (true) {
                        int n = 0;
                        Label_0248: {
                            try {
                                if ("uncaught".equals(a)) {
                                    n = 1;
                                }
                                else {
                                    n = 0;
                                }
                                ++this.E;
                                if (this.D.size() < jh.f) {
                                    final ja ja = new ja(this.r.incrementAndGet(), System.currentTimeMillis(), (String)a, s, s2, t);
                                    this.D.add(ja);
                                    a = jh.a;
                                    s = (String)new StringBuilder("Error logged: ");
                                    km.e((String)a, ((StringBuilder)s).append(ja.a).toString());
                                }
                                else {
                                    if (n == 0) {
                                        break Label_0254;
                                    }
                                    n = 0;
                                    if (n < this.D.size()) {
                                        final ja ja2 = this.D.get(n);
                                        if (ja2.a == null || "uncaught".equals(ja2.a)) {
                                            break Label_0248;
                                        }
                                        this.D.set(n, new ja(this.r.incrementAndGet(), System.currentTimeMillis(), (String)a, s, s2, t));
                                    }
                                }
                                return;
                            }
                            finally {
                            }
                            // monitorexit(this)
                        }
                        ++n;
                        continue;
                    }
                }
                km.e(jh.a, "Max errors logged. No more errors logged.");
            }
        }
    }
    
    public final void a(final String anObject, final Map<String, String> map) {
        while (true) {
            while (true) {
                jc jc = null;
                Label_0252: {
                    synchronized (this) {
                        final Iterator<jc> iterator = this.A.iterator();
                        while (iterator.hasNext()) {
                            jc = iterator.next();
                            int n;
                            if (jc.b && jc.d == 0L && jc.a.equals(anObject)) {
                                n = 1;
                            }
                            else {
                                n = 0;
                            }
                            if (n != 0) {
                                final long elapsedRealtime = SystemClock.elapsedRealtime();
                                jk.a();
                                final long d = jk.d();
                                if (map != null && map.size() > 0 && this.C < jh.e) {
                                    final int n2 = this.C - jc.b().length;
                                    final HashMap hashMap = new HashMap<String, String>(jc.a());
                                    jc.a(map);
                                    if (jc.b().length + n2 > jh.e) {
                                        break Label_0252;
                                    }
                                    if (jc.a().size() > jh.c) {
                                        km.e(jh.a, "MaxEventParams exceeded on endEvent: " + jc.a().size());
                                        jc.b((Map<String, String>)hashMap);
                                    }
                                    else {
                                        this.C = n2 + jc.b().length;
                                    }
                                }
                                jc.a(elapsedRealtime - d);
                                break;
                            }
                        }
                        return;
                    }
                }
                final Map<String, String> map2;
                jc.b(map2);
                this.B = false;
                this.C = jh.e;
                km.e(jh.a, "Event Log size exceeded. No more event details logged.");
                continue;
            }
        }
    }
    
    @TargetApi(18)
    final void a(final boolean p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: istore_2       
        //     2: iload_1        
        //     3: ifeq            196
        //     6: aload_0        
        //     7: getfield        com/flurry/sdk/jh.y:Ljava/util/Map;
        //    10: ldc_w           "boot.time"
        //    13: invokestatic    java/lang/System.currentTimeMillis:()J
        //    16: invokestatic    android/os/SystemClock.elapsedRealtime:()J
        //    19: lsub           
        //    20: invokestatic    java/lang/Long.toString:(J)Ljava/lang/String;
        //    23: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    28: pop            
        //    29: new             Landroid/os/StatFs;
        //    32: dup            
        //    33: invokestatic    android/os/Environment.getRootDirectory:()Ljava/io/File;
        //    36: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //    39: invokespecial   android/os/StatFs.<init>:(Ljava/lang/String;)V
        //    42: astore_3       
        //    43: new             Landroid/os/StatFs;
        //    46: dup            
        //    47: invokestatic    android/os/Environment.getExternalStorageDirectory:()Ljava/io/File;
        //    50: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //    53: invokespecial   android/os/StatFs.<init>:(Ljava/lang/String;)V
        //    56: astore          4
        //    58: getstatic       android/os/Build$VERSION.SDK_INT:I
        //    61: bipush          18
        //    63: if_icmplt       523
        //    66: aload_0        
        //    67: getfield        com/flurry/sdk/jh.y:Ljava/util/Map;
        //    70: ldc_w           "disk.size.total.internal"
        //    73: aload_3        
        //    74: invokevirtual   android/os/StatFs.getAvailableBlocksLong:()J
        //    77: invokestatic    java/lang/Long.toString:(J)Ljava/lang/String;
        //    80: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    85: pop            
        //    86: aload_0        
        //    87: getfield        com/flurry/sdk/jh.y:Ljava/util/Map;
        //    90: ldc_w           "disk.size.available.internal"
        //    93: aload_3        
        //    94: invokevirtual   android/os/StatFs.getAvailableBlocksLong:()J
        //    97: invokestatic    java/lang/Long.toString:(J)Ljava/lang/String;
        //   100: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   105: pop            
        //   106: aload_0        
        //   107: getfield        com/flurry/sdk/jh.y:Ljava/util/Map;
        //   110: ldc_w           "disk.size.total.external"
        //   113: aload           4
        //   115: invokevirtual   android/os/StatFs.getAvailableBlocksLong:()J
        //   118: invokestatic    java/lang/Long.toString:(J)Ljava/lang/String;
        //   121: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   126: pop            
        //   127: aload_0        
        //   128: getfield        com/flurry/sdk/jh.y:Ljava/util/Map;
        //   131: ldc_w           "disk.size.available.external"
        //   134: aload           4
        //   136: invokevirtual   android/os/StatFs.getAvailableBlocksLong:()J
        //   139: invokestatic    java/lang/Long.toString:(J)Ljava/lang/String;
        //   142: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   147: pop            
        //   148: aload_0        
        //   149: getfield        com/flurry/sdk/jh.y:Ljava/util/Map;
        //   152: astore          4
        //   154: invokestatic    com/flurry/sdk/js.a:()Lcom/flurry/sdk/js;
        //   157: pop            
        //   158: aload           4
        //   160: ldc_w           "carrier.name"
        //   163: invokestatic    com/flurry/sdk/js.b:()Ljava/lang/String;
        //   166: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   171: pop            
        //   172: aload_0        
        //   173: getfield        com/flurry/sdk/jh.y:Ljava/util/Map;
        //   176: astore          4
        //   178: invokestatic    com/flurry/sdk/js.a:()Lcom/flurry/sdk/js;
        //   181: pop            
        //   182: aload           4
        //   184: ldc_w           "carrier.details"
        //   187: invokestatic    com/flurry/sdk/js.c:()Ljava/lang/String;
        //   190: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   195: pop            
        //   196: invokestatic    com/flurry/sdk/jy.a:()Lcom/flurry/sdk/jy;
        //   199: getfield        com/flurry/sdk/jy.a:Landroid/content/Context;
        //   202: ldc_w           "activity"
        //   205: invokevirtual   android/content/Context.getSystemService:(Ljava/lang/String;)Ljava/lang/Object;
        //   208: checkcast       Landroid/app/ActivityManager;
        //   211: astore          4
        //   213: new             Landroid/app/ActivityManager$MemoryInfo;
        //   216: dup            
        //   217: invokespecial   android/app/ActivityManager$MemoryInfo.<init>:()V
        //   220: astore_3       
        //   221: aload           4
        //   223: aload_3        
        //   224: invokevirtual   android/app/ActivityManager.getMemoryInfo:(Landroid/app/ActivityManager$MemoryInfo;)V
        //   227: aload_0        
        //   228: getfield        com/flurry/sdk/jh.y:Ljava/util/Map;
        //   231: astore          5
        //   233: new             Ljava/lang/StringBuilder;
        //   236: dup            
        //   237: ldc_w           "memory.available"
        //   240: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   243: astore          6
        //   245: iload_1        
        //   246: ifeq            612
        //   249: ldc_w           ".start"
        //   252: astore          4
        //   254: aload           5
        //   256: aload           6
        //   258: aload           4
        //   260: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   263: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   266: aload_3        
        //   267: getfield        android/app/ActivityManager$MemoryInfo.availMem:J
        //   270: invokestatic    java/lang/Long.toString:(J)Ljava/lang/String;
        //   273: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   278: pop            
        //   279: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   282: bipush          16
        //   284: if_icmplt       339
        //   287: aload_0        
        //   288: getfield        com/flurry/sdk/jh.y:Ljava/util/Map;
        //   291: astore          5
        //   293: new             Ljava/lang/StringBuilder;
        //   296: dup            
        //   297: ldc_w           "memory.total"
        //   300: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   303: astore          6
        //   305: iload_1        
        //   306: ifeq            620
        //   309: ldc_w           ".start"
        //   312: astore          4
        //   314: aload           5
        //   316: aload           6
        //   318: aload           4
        //   320: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   323: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   326: aload_3        
        //   327: getfield        android/app/ActivityManager$MemoryInfo.availMem:J
        //   330: invokestatic    java/lang/Long.toString:(J)Ljava/lang/String;
        //   333: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   338: pop            
        //   339: new             Landroid/content/IntentFilter;
        //   342: astore          4
        //   344: aload           4
        //   346: ldc_w           "android.intent.action.BATTERY_CHANGED"
        //   349: invokespecial   android/content/IntentFilter.<init>:(Ljava/lang/String;)V
        //   352: invokestatic    com/flurry/sdk/jy.a:()Lcom/flurry/sdk/jy;
        //   355: getfield        com/flurry/sdk/jy.a:Landroid/content/Context;
        //   358: aconst_null    
        //   359: aload           4
        //   361: invokevirtual   android/content/Context.registerReceiver:(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;)Landroid/content/Intent;
        //   364: astore          4
        //   366: aload           4
        //   368: ifnull          708
        //   371: aload           4
        //   373: ldc_w           "status"
        //   376: iconst_m1      
        //   377: invokevirtual   android/content/Intent.getIntExtra:(Ljava/lang/String;I)I
        //   380: istore          7
        //   382: iload           7
        //   384: iconst_2       
        //   385: if_icmpeq       394
        //   388: iload           7
        //   390: iconst_5       
        //   391: if_icmpne       628
        //   394: iconst_1       
        //   395: istore          8
        //   397: aload           4
        //   399: ldc_w           "level"
        //   402: iconst_m1      
        //   403: invokevirtual   android/content/Intent.getIntExtra:(Ljava/lang/String;I)I
        //   406: istore_2       
        //   407: aload           4
        //   409: ldc_w           "scale"
        //   412: iconst_m1      
        //   413: invokevirtual   android/content/Intent.getIntExtra:(Ljava/lang/String;I)I
        //   416: istore          7
        //   418: iload_2        
        //   419: i2f            
        //   420: iload           7
        //   422: i2f            
        //   423: fdiv           
        //   424: fstore          9
        //   426: aload_0        
        //   427: getfield        com/flurry/sdk/jh.y:Ljava/util/Map;
        //   430: astore_3       
        //   431: new             Ljava/lang/StringBuilder;
        //   434: dup            
        //   435: ldc_w           "battery.charging"
        //   438: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   441: astore          6
        //   443: iload_1        
        //   444: ifeq            676
        //   447: ldc_w           ".start"
        //   450: astore          4
        //   452: aload_3        
        //   453: aload           6
        //   455: aload           4
        //   457: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   460: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   463: iload           8
        //   465: invokestatic    java/lang/Boolean.toString:(Z)Ljava/lang/String;
        //   468: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   473: pop            
        //   474: aload_0        
        //   475: getfield        com/flurry/sdk/jh.y:Ljava/util/Map;
        //   478: astore          6
        //   480: new             Ljava/lang/StringBuilder;
        //   483: dup            
        //   484: ldc_w           "battery.remaining"
        //   487: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   490: astore_3       
        //   491: iload_1        
        //   492: ifeq            684
        //   495: ldc_w           ".start"
        //   498: astore          4
        //   500: aload           6
        //   502: aload_3        
        //   503: aload           4
        //   505: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   508: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   511: fload           9
        //   513: invokestatic    java/lang/Float.toString:(F)Ljava/lang/String;
        //   516: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   521: pop            
        //   522: return         
        //   523: aload_0        
        //   524: getfield        com/flurry/sdk/jh.y:Ljava/util/Map;
        //   527: ldc_w           "disk.size.total.internal"
        //   530: aload_3        
        //   531: invokevirtual   android/os/StatFs.getAvailableBlocks:()I
        //   534: i2l            
        //   535: invokestatic    java/lang/Long.toString:(J)Ljava/lang/String;
        //   538: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   543: pop            
        //   544: aload_0        
        //   545: getfield        com/flurry/sdk/jh.y:Ljava/util/Map;
        //   548: ldc_w           "disk.size.available.internal"
        //   551: aload_3        
        //   552: invokevirtual   android/os/StatFs.getAvailableBlocks:()I
        //   555: i2l            
        //   556: invokestatic    java/lang/Long.toString:(J)Ljava/lang/String;
        //   559: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   564: pop            
        //   565: aload_0        
        //   566: getfield        com/flurry/sdk/jh.y:Ljava/util/Map;
        //   569: ldc_w           "disk.size.total.external"
        //   572: aload           4
        //   574: invokevirtual   android/os/StatFs.getAvailableBlocks:()I
        //   577: i2l            
        //   578: invokestatic    java/lang/Long.toString:(J)Ljava/lang/String;
        //   581: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   586: pop            
        //   587: aload_0        
        //   588: getfield        com/flurry/sdk/jh.y:Ljava/util/Map;
        //   591: ldc_w           "disk.size.available.external"
        //   594: aload           4
        //   596: invokevirtual   android/os/StatFs.getAvailableBlocks:()I
        //   599: i2l            
        //   600: invokestatic    java/lang/Long.toString:(J)Ljava/lang/String;
        //   603: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   608: pop            
        //   609: goto            148
        //   612: ldc_w           ".end"
        //   615: astore          4
        //   617: goto            254
        //   620: ldc_w           ".end"
        //   623: astore          4
        //   625: goto            314
        //   628: iconst_0       
        //   629: istore          8
        //   631: goto            397
        //   634: astore          4
        //   636: iconst_0       
        //   637: istore          8
        //   639: iconst_m1      
        //   640: istore          7
        //   642: iconst_5       
        //   643: getstatic       com/flurry/sdk/jh.a:Ljava/lang/String;
        //   646: new             Ljava/lang/StringBuilder;
        //   649: dup            
        //   650: ldc_w           "Error getting battery status: "
        //   653: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   656: aload           4
        //   658: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   661: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   664: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //   667: iload           7
        //   669: istore_2       
        //   670: iconst_m1      
        //   671: istore          7
        //   673: goto            418
        //   676: ldc_w           ".end"
        //   679: astore          4
        //   681: goto            452
        //   684: ldc_w           ".end"
        //   687: astore          4
        //   689: goto            500
        //   692: astore          4
        //   694: iconst_m1      
        //   695: istore          7
        //   697: goto            642
        //   700: astore          4
        //   702: iload_2        
        //   703: istore          7
        //   705: goto            642
        //   708: iconst_0       
        //   709: istore          8
        //   711: iconst_m1      
        //   712: istore          7
        //   714: goto            418
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  339    366    634    642    Ljava/lang/Exception;
        //  371    382    634    642    Ljava/lang/Exception;
        //  397    407    692    700    Ljava/lang/Exception;
        //  407    418    700    708    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 310 out of bounds for length 310
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:458)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public final void b() {
        synchronized (this) {
            this.a(false);
            jk.a();
            final long c = jk.c();
            jk.a();
            final long e = jk.e();
            jk.a();
            long f = 0L;
            final jx h = jk.h();
            if (h != null) {
                f = h.f;
            }
            jk.a();
            final int e2 = jk.g().e;
            if (this.o && hr.a().a != null) {
                jy.a().b(new ma() {
                    @Override
                    public final void a() {
                        hr.a().a.a(c);
                    }
                });
            }
            jy.a().b(new ma() {
                @Override
                public final void a() {
                    jh.this.f();
                }
            });
            if (jl.a().b()) {
                jy.a().b(new ma() {
                    @Override
                    public final void a() {
                        final jf a = jh.this.a(c, e, f, e2);
                        jh.this.w.clear();
                        jh.this.w.add(a);
                        jh.this.d();
                    }
                });
            }
        }
    }
    
    public final void c() {
        synchronized (this) {
            ++this.F;
        }
    }
    
    public final void d() {
        synchronized (this) {
            km.a(4, jh.a, "Saving persistent agent data.");
            this.i.a(this.w);
        }
    }
}
