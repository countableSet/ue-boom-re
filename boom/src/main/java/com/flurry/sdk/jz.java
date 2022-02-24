// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.Locale;

public class jz
{
    private static final String a;
    
    static {
        a = jz.class.getSimpleName();
    }
    
    public static int a() {
        final int intValue = (int)lp.a().a("AgentVersion");
        km.a(4, jz.a, "getAgentVersion() = " + intValue);
        return intValue;
    }
    
    public static String b() {
        String s;
        if (c().length() > 0) {
            s = ".";
        }
        else {
            s = "";
        }
        return String.format(Locale.getDefault(), "Flurry_Android_%d_%d.%d.%d%s%s", a(), (int)lp.a().a("ReleaseMajorVersion"), (int)lp.a().a("ReleaseMinorVersion"), (int)lp.a().a("ReleasePatchVersion"), s, c());
    }
    
    private static String c() {
        return (String)lp.a().a("ReleaseBetaVersion");
    }
}
