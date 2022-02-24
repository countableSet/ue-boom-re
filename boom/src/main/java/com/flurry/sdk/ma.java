// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.io.PrintWriter;
import java.io.PrintStream;

public abstract class ma implements Runnable
{
    private static final String a;
    PrintStream u;
    PrintWriter v;
    
    static {
        a = ma.class.getSimpleName();
    }
    
    public abstract void a();
    
    @Override
    public final void run() {
        try {
            this.a();
        }
        catch (Throwable t) {
            if (this.u != null) {
                t.printStackTrace(this.u);
            }
            else if (this.v != null) {
                t.printStackTrace(this.v);
            }
            else {
                t.printStackTrace();
            }
            km.a(6, ma.a, "", t);
        }
    }
}
