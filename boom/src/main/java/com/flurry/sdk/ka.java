// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.Map;
import java.util.HashMap;

public class ka
{
    private static ka a;
    private static final String b;
    private static final HashMap<String, Map<String, String>> c;
    
    static {
        b = ka.class.getSimpleName();
        c = new HashMap<String, Map<String, String>>();
    }
    
    public static ka a() {
        synchronized (ka.class) {
            if (ka.a == null) {
                ka.a = new ka();
            }
            return ka.a;
        }
    }
    
    public final void a(final String key, String b, final Map<String, String> map) {
        // monitorenter(this)
        Map<String, String> value = map;
        Label_0019: {
            if (map != null) {
                break Label_0019;
            }
            while (true) {
                Label_0150: {
                    try {
                        value = new HashMap<String, String>();
                        if (value.size() >= 10) {
                            b = ka.b;
                            km.e((String)b, "MaxOriginParams exceeded: " + value.size());
                        }
                        else {
                            value.put("flurryOriginVersion", (String)b);
                            synchronized (ka.c) {
                                if (ka.c.size() >= 10 && !ka.c.containsKey(key)) {
                                    km.e(ka.b, "MaxOrigins exceeded: " + ka.c.size());
                                    return;
                                }
                                break Label_0150;
                            }
                        }
                        return;
                    }
                    finally {
                    }
                    // monitorexit(this)
                }
                final String key2;
                ka.c.put(key2, value);
            }
            // monitorexit(b)
        }
    }
    
    public final HashMap<String, Map<String, String>> b() {
        synchronized (this) {
            synchronized (ka.c) {
                return new HashMap<String, Map<String, String>>(ka.c);
            }
        }
    }
}
