// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.Locale;

public final class jo
{
    private static jo a;
    
    private jo() {
    }
    
    public static jo a() {
        synchronized (jo.class) {
            if (jo.a == null) {
                jo.a = new jo();
            }
            return jo.a;
        }
    }
    
    public static String b() {
        return Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
    }
}
