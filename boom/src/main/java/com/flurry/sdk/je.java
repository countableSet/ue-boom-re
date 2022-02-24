// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.widget.Toast;
import java.util.Arrays;

public class je extends ky implements lq.a
{
    private static final String a;
    private String f;
    private boolean g;
    
    static {
        a = je.class.getSimpleName();
    }
    
    public je() {
        this((byte)0);
    }
    
    private je(final byte b) {
        super("Analytics", je.class.getSimpleName());
        this.e = "AnalyticsData_";
        final lp a = lp.a();
        this.g = (boolean)a.a("UseHttps");
        a.a("UseHttps", (lq.a)this);
        km.a(4, je.a, "initSettings, UseHttps = " + this.g);
        final String str = (String)a.a("ReportUrl");
        a.a("ReportUrl", (lq.a)this);
        this.b(str);
        km.a(4, je.a, "initSettings, ReportUrl = " + str);
        this.b();
    }
    
    private void b(final String f) {
        if (f != null && !f.endsWith(".do")) {
            km.a(5, je.a, "overriding analytics agent report URL without an endpoint, are you sure?");
        }
        this.f = f;
    }
    
    @Override
    public final void a(String str, final Object o) {
        switch (str) {
            default: {
                km.a(6, je.a, "onSettingUpdate internal error!");
                break;
            }
            case "UseHttps": {
                this.g = (boolean)o;
                km.a(4, je.a, "onSettingUpdate, UseHttps = " + this.g);
                break;
            }
            case "ReportUrl": {
                str = (String)o;
                this.b(str);
                km.a(4, je.a, "onSettingUpdate, ReportUrl = " + str);
                break;
            }
        }
    }
    
    protected final void a(final String s, final String s2, final int n) {
        jy.a().b(new ma() {
            @Override
            public final void a() {
                if (n == 200) {
                    hr.a();
                    final jh b = hr.b();
                    if (b != null) {
                        b.j = true;
                    }
                }
            }
        });
        super.a(s, s2, n);
    }
    
    protected final void a(final byte[] array, final String str, final String s) {
        String f;
        if (this.f != null) {
            f = this.f;
        }
        else if (this.g) {
            f = "https://data.flurry.com/aap.do";
        }
        else {
            f = "http://data.flurry.com/aap.do";
        }
        km.a(4, je.a, "FlurryDataSender: start upload data " + Arrays.toString(array) + " with id = " + str + " to " + f);
        final ks ks = new ks<Object, Object>();
        ks.f = f;
        ks.w = 100000;
        ks.g = ku.a.c;
        ks.a("Content-Type", "application/octet-stream");
        ks.c = (lg<RequestObjectType>)new lc();
        ks.b = (RequestObjectType)(Object)array;
        ks.a = (ks.a<RequestObjectType, ResponseObjectType>)new ks.a<byte[], Void>() {};
        ((kl<ks>)jw.a()).a(this, ks);
    }
}
