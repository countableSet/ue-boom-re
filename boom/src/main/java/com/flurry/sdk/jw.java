// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.concurrent.BlockingQueue;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public final class jw extends kl<ku>
{
    private static jw a;
    
    static {
        jw.a = null;
    }
    
    protected jw() {
        super(jw.class.getName(), TimeUnit.MILLISECONDS, new PriorityBlockingQueue(11, new kj()));
    }
    
    public static jw a() {
        synchronized (jw.class) {
            if (jw.a == null) {
                jw.a = new jw();
            }
            return jw.a;
        }
    }
}
