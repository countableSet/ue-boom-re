// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.content.Context;
import java.lang.ref.WeakReference;

public final class ll extends kg
{
    public WeakReference<Context> a;
    public lk b;
    public int c;
    public long d;
    
    public ll() {
        super("com.flurry.android.sdk.FlurrySessionEvent");
    }
    
    public enum a
    {
        public static final int a;
        public static final int b;
        public static final int c;
        public static final int d;
        public static final int e;
        private static final /* synthetic */ int[] f;
        
        static {
            a = 1;
            b = 2;
            c = 3;
            d = 4;
            e = 5;
            f = new int[] { ll.a.a, ll.a.b, ll.a.c, ll.a.d, ll.a.e };
        }
        
        public static int[] a() {
            return ll.a.f.clone();
        }
    }
}
