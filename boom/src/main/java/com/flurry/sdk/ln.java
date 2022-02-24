// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.TimerTask;
import java.util.Timer;

final class ln
{
    private Timer a;
    private a b;
    
    public final void a() {
        synchronized (this) {
            if (this.a != null) {
                this.a.cancel();
                this.a = null;
            }
            this.b = null;
        }
    }
    
    public final void a(final long delay) {
        synchronized (this) {
            int n;
            if (this.a != null) {
                n = 1;
            }
            else {
                n = 0;
            }
            if (n != 0) {
                this.a();
            }
            this.a = new Timer("FlurrySessionTimer");
            this.b = new a();
            this.a.schedule(this.b, delay);
        }
    }
    
    final class a extends TimerTask
    {
        @Override
        public final void run() {
            ln.this.a();
            ki.a().a(new lo());
        }
    }
}
