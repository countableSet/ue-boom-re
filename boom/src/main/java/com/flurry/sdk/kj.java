// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.Comparator;

public class kj implements Comparator<Runnable>
{
    private static final String a;
    
    static {
        a = kj.class.getSimpleName();
    }
    
    private static int a(final Runnable runnable) {
        int n = Integer.MAX_VALUE;
        if (runnable != null) {
            if (runnable instanceof kk) {
                final mb mb = (mb)((kk)runnable).a();
                if (mb != null) {
                    n = mb.w;
                }
                else {
                    n = Integer.MAX_VALUE;
                }
            }
            else if (runnable instanceof mb) {
                n = ((mb)runnable).w;
            }
            else {
                km.a(6, kj.a, "Unknown runnable class: " + runnable.getClass().getName());
                n = n;
            }
        }
        return n;
    }
}
