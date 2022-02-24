// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.text.TextUtils;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Build$VERSION;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import android.content.Context;

public class ju implements a
{
    private static ju a;
    private static final String b;
    private String c;
    private String d;
    
    static {
        b = ju.class.getSimpleName();
    }
    
    private ju() {
        final lp a = lp.a();
        this.c = (String)a.a("VersionName");
        a.a("VersionName", (lq.a)this);
        km.a(4, ju.b, "initSettings, VersionName = " + this.c);
    }
    
    public static ju a() {
        synchronized (ju.class) {
            if (ju.a == null) {
                ju.a = new ju();
            }
            return ju.a;
        }
    }
    
    public static String a(final Context context) {
        String versionName = null;
        final PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return versionName;
        }
        try {
            versionName = packageManager.getPackageInfo(context.getPackageName(), 0).versionName;
            return versionName;
        }
        catch (PackageManager$NameNotFoundException ex) {
            versionName = "unknown";
            return versionName;
        }
    }
    
    public static String b() {
        return Build$VERSION.RELEASE;
    }
    
    public static String c() {
        return Build.DEVICE;
    }
    
    public static String d() {
        return Build.ID;
    }
    
    public static String e() {
        return Build.MANUFACTURER;
    }
    
    public static String f() {
        return Build.MODEL;
    }
    
    private static String h() {
        while (true) {
            try {
                final Context a = jy.a().a;
                final PackageInfo packageInfo = a.getPackageManager().getPackageInfo(a.getPackageName(), 0);
                String s;
                if (packageInfo.versionName != null) {
                    s = packageInfo.versionName;
                }
                else {
                    if (packageInfo.versionCode == 0) {
                        return "Unknown";
                    }
                    s = Integer.toString(packageInfo.versionCode);
                }
                return s;
            }
            catch (Throwable t) {
                km.a(6, ju.b, "", t);
            }
            return "Unknown";
        }
    }
    
    @Override
    public final void a(final String s, final Object o) {
        if (s.equals("VersionName")) {
            this.c = (String)o;
            km.a(4, ju.b, "onSettingUpdate, VersionName = " + this.c);
        }
        else {
            km.a(6, ju.b, "onSettingUpdate internal error!");
        }
    }
    
    public final String g() {
        synchronized (this) {
            String s;
            if (!TextUtils.isEmpty((CharSequence)this.c)) {
                s = this.c;
            }
            else if (!TextUtils.isEmpty((CharSequence)this.d)) {
                s = this.d;
            }
            else {
                this.d = h();
                s = this.d;
            }
            return s;
        }
    }
}
