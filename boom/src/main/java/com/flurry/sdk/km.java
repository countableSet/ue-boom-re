// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.text.TextUtils;
import android.util.Log;

public final class km
{
    private static boolean a;
    private static int b;
    private static boolean c;
    
    static {
        km.a = false;
        km.b = 5;
        km.c = false;
    }
    
    public static void a() {
        km.a = true;
    }
    
    public static void a(final int b) {
        km.b = b;
    }
    
    public static void a(final int n, final String s, final String s2) {
        c(n, s, s2);
    }
    
    public static void a(final int n, final String s, final String str, final Throwable t) {
        c(n, s, str + '\n' + Log.getStackTraceString(t));
    }
    
    public static void a(final String s, final String s2) {
        b(3, s, s2);
    }
    
    public static void a(final String s, final String str, final Throwable t) {
        b(6, s, str + '\n' + Log.getStackTraceString(t));
    }
    
    public static void b() {
        km.a = false;
    }
    
    private static void b(final int n, final String s, final String s2) {
        if (!km.a && km.b <= n) {
            d(n, s, s2);
        }
    }
    
    public static void b(final String s, final String s2) {
        b(6, s, s2);
    }
    
    public static int c() {
        return km.b;
    }
    
    private static void c(final int n, final String s, final String s2) {
        if (km.c) {
            d(n, s, s2);
        }
    }
    
    public static void c(final String s, final String s2) {
        b(4, s, s2);
    }
    
    private static void d(final int n, String s, final String s2) {
        if (!km.c) {
            s = "FlurryAgent";
        }
        int length;
        if (TextUtils.isEmpty((CharSequence)s2)) {
            length = 0;
        }
        else {
            length = s2.length();
        }
        int endIndex;
        for (int i = 0; i < length; i = endIndex) {
            if (4000 > length - i) {
                endIndex = length;
            }
            else {
                endIndex = i + 4000;
            }
            if (Log.println(n, s, s2.substring(i, endIndex)) <= 0) {
                break;
            }
        }
    }
    
    public static void d(final String s, final String s2) {
        b(2, s, s2);
    }
    
    public static boolean d() {
        return km.c;
    }
    
    public static void e(final String s, final String s2) {
        b(5, s, s2);
    }
}
