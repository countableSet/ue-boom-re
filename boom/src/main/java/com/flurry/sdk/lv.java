// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.content.pm.PackageManager$NameNotFoundException;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.content.pm.PackageInfo;
import android.content.Context;

public final class lv
{
    private static final String a;
    
    static {
        a = lv.class.getSimpleName();
    }
    
    public static String a(final Context context) {
        final PackageInfo d = d(context);
        String packageName;
        if (d != null && d.packageName != null) {
            packageName = d.packageName;
        }
        else {
            packageName = "";
        }
        return packageName;
    }
    
    public static String b(final Context context) {
        final PackageInfo d = d(context);
        String versionName;
        if (d != null && d.versionName != null) {
            versionName = d.versionName;
        }
        else {
            versionName = "";
        }
        return versionName;
    }
    
    public static Bundle c(final Context context) {
        final ApplicationInfo e = e(context);
        Bundle bundle;
        if (e != null && e.metaData != null) {
            bundle = e.metaData;
        }
        else {
            bundle = Bundle.EMPTY;
        }
        return bundle;
    }
    
    private static PackageInfo d(final Context context) {
        PackageInfo packageInfo = null;
        if (context == null) {
            return packageInfo;
        }
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo;
        }
        catch (PackageManager$NameNotFoundException ex) {
            km.a(lv.a, "Cannot find package info for package: " + context.getPackageName());
            packageInfo = packageInfo;
            return packageInfo;
        }
    }
    
    private static ApplicationInfo e(final Context context) {
        ApplicationInfo applicationInfo = null;
        if (context == null) {
            return applicationInfo;
        }
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            return applicationInfo;
        }
        catch (PackageManager$NameNotFoundException ex) {
            km.a(lv.a, "Cannot find application info for package: " + context.getPackageName());
            applicationInfo = applicationInfo;
            return applicationInfo;
        }
    }
}
