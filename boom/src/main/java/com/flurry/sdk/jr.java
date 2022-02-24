// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.content.Context;
import android.content.BroadcastReceiver;

public final class jr extends BroadcastReceiver
{
    private static jr c;
    boolean a;
    public boolean b;
    private boolean d;
    
    private jr() {
        boolean d = false;
        this.d = false;
        final Context a = jy.a().a;
        if (a.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == 0) {
            d = true;
        }
        this.d = d;
        this.b = this.a(a);
        if (this.d) {
            this.c();
        }
    }
    
    public static jr a() {
        synchronized (jr.class) {
            if (jr.c == null) {
                jr.c = new jr();
            }
            return jr.c;
        }
    }
    
    private boolean a(final Context context) {
        boolean b2;
        final boolean b = b2 = true;
        if (this.d) {
            if (context == null) {
                b2 = b;
            }
            else {
                final NetworkInfo activeNetworkInfo = d().getActiveNetworkInfo();
                if (activeNetworkInfo != null) {
                    b2 = b;
                    if (activeNetworkInfo.isConnected()) {
                        return b2;
                    }
                }
                b2 = false;
            }
        }
        return b2;
    }
    
    private void c() {
        synchronized (this) {
            if (!this.a) {
                final Context a = jy.a().a;
                this.b = this.a(a);
                a.registerReceiver((BroadcastReceiver)this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
                this.a = true;
            }
        }
    }
    
    private static ConnectivityManager d() {
        return (ConnectivityManager)jy.a().a.getSystemService("connectivity");
    }
    
    public final a b() {
        a a = null;
        if (!this.d) {
            a = jr.a.a;
        }
        else {
            final NetworkInfo activeNetworkInfo = d().getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                a = jr.a.a;
            }
            else {
                switch (activeNetworkInfo.getType()) {
                    default: {
                        if (activeNetworkInfo.isConnected()) {
                            a = jr.a.b;
                            break;
                        }
                        a = jr.a.a;
                        break;
                    }
                    case 1: {
                        a = jr.a.c;
                        break;
                    }
                    case 0:
                    case 2:
                    case 3:
                    case 4:
                    case 5: {
                        a = jr.a.d;
                        break;
                    }
                    case 8: {
                        a = jr.a.a;
                        break;
                    }
                }
            }
        }
        return a;
    }
    
    public final void onReceive(final Context context, final Intent intent) {
        final boolean a = this.a(context);
        if (this.b != a) {
            this.b = a;
            final jq jq = new jq();
            jq.a = a;
            jq.b = this.b();
            ki.a().a(jq);
        }
    }
    
    public enum a
    {
        a(0), 
        b(1), 
        c(2), 
        d(3);
        
        public int e;
        
        private a(final int e) {
            this.e = e;
        }
    }
}
