// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.telephony.TelephonyManager;

public class js
{
    private static js a;
    private static final String b;
    
    static {
        b = js.class.getSimpleName();
    }
    
    private js() {
    }
    
    public static js a() {
        synchronized (js.class) {
            if (js.a == null) {
                js.a = new js();
            }
            return js.a;
        }
    }
    
    public static String b() {
        final TelephonyManager telephonyManager = (TelephonyManager)jy.a().a.getSystemService("phone");
        String networkOperatorName;
        if (telephonyManager == null) {
            networkOperatorName = null;
        }
        else {
            networkOperatorName = telephonyManager.getNetworkOperatorName();
        }
        return networkOperatorName;
    }
    
    public static String c() {
        final TelephonyManager telephonyManager = (TelephonyManager)jy.a().a.getSystemService("phone");
        String networkOperator;
        if (telephonyManager == null) {
            networkOperator = null;
        }
        else {
            networkOperator = telephonyManager.getNetworkOperator();
        }
        return networkOperator;
    }
}
