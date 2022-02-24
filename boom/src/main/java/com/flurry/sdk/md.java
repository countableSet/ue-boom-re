// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.content.Context;

public class md implements kp, a, UncaughtExceptionHandler
{
    private static final String a;
    private boolean b;
    
    static {
        a = md.class.getSimpleName();
    }
    
    @Override
    public final void a(final Context context) {
        final lp a = lp.a();
        this.b = (boolean)a.a("CaptureUncaughtExceptions");
        a.a("CaptureUncaughtExceptions", (lq.a)this);
        km.a(4, md.a, "initSettings, CrashReportingEnabled = " + this.b);
        final me a2 = me.a();
        synchronized (a2.b) {
            a2.b.put(this, null);
        }
    }
    
    @Override
    public final void a(final String s, final Object o) {
        if (s.equals("CaptureUncaughtExceptions")) {
            this.b = (boolean)o;
            km.a(4, md.a, "onSettingUpdate, CrashReportingEnabled = " + this.b);
        }
        else {
            km.a(6, md.a, "onSettingUpdate internal error!");
        }
    }
    
    @Override
    public void uncaughtException(final Thread thread, final Throwable t) {
        t.printStackTrace();
        if (this.b) {
            String s = "";
            final StackTraceElement[] stackTrace = t.getStackTrace();
            if (stackTrace != null && stackTrace.length > 0) {
                final StringBuilder sb = new StringBuilder();
                if (t.getMessage() != null) {
                    sb.append(" (").append(t.getMessage()).append(")\n");
                }
                s = sb.toString();
            }
            else if (t.getMessage() != null) {
                s = t.getMessage();
            }
            hr.a();
            hr.a("uncaught", s, t);
        }
        lm.a().d();
        jp.a().f();
    }
}
