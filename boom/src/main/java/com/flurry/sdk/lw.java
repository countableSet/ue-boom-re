// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.util.DisplayMetrics;
import android.annotation.SuppressLint;
import java.lang.reflect.Method;
import android.view.Display;
import android.os.Build$VERSION;
import android.view.WindowManager;
import android.graphics.Point;
import android.app.KeyguardManager;

public final class lw
{
    public static int a(final int n) {
        return Math.round(n / d().density);
    }
    
    public static boolean a() {
        return ((KeyguardManager)jy.a().a.getSystemService("keyguard")).inKeyguardRestrictedInputMode();
    }
    
    public static int b(final int n) {
        return Math.round(d().density * n);
    }
    
    @SuppressLint({ "NewApi" })
    public static Point b() {
        final Display defaultDisplay = ((WindowManager)jy.a().a.getSystemService("window")).getDefaultDisplay();
        final Point point = new Point();
        if (Build$VERSION.SDK_INT >= 17) {
            defaultDisplay.getRealSize(point);
        }
        else if (Build$VERSION.SDK_INT >= 14) {
            try {
                final Method method = Display.class.getMethod("getRawHeight", (Class<?>[])new Class[0]);
                point.x = (int)Display.class.getMethod("getRawWidth", (Class<?>[])new Class[0]).invoke(defaultDisplay, new Object[0]);
                point.y = (int)method.invoke(defaultDisplay, new Object[0]);
            }
            catch (Throwable t) {
                defaultDisplay.getSize(point);
            }
        }
        else if (Build$VERSION.SDK_INT >= 13) {
            defaultDisplay.getSize(point);
        }
        else {
            point.x = defaultDisplay.getWidth();
            point.y = defaultDisplay.getHeight();
        }
        return point;
    }
    
    public static DisplayMetrics c() {
        final Display defaultDisplay = ((WindowManager)jy.a().a.getSystemService("window")).getDefaultDisplay();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return displayMetrics;
    }
    
    @SuppressLint({ "NewApi" })
    public static DisplayMetrics d() {
        final Display defaultDisplay = ((WindowManager)jy.a().a.getSystemService("window")).getDefaultDisplay();
        DisplayMetrics c;
        if (Build$VERSION.SDK_INT >= 17) {
            c = new DisplayMetrics();
            defaultDisplay.getRealMetrics(c);
        }
        else {
            if (Build$VERSION.SDK_INT >= 14) {
                try {
                    c = new DisplayMetrics();
                    Display.class.getMethod("getRealMetrics", (Class<?>[])new Class[0]).invoke(defaultDisplay, c);
                    return c;
                }
                catch (Exception ex) {}
            }
            c = c();
        }
        return c;
    }
    
    public static int e() {
        final Point b = b();
        int n;
        if (b.x == b.y) {
            n = 3;
        }
        else if (b.x < b.y) {
            n = 1;
        }
        else {
            n = 2;
        }
        return n;
    }
}
