// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

public enum jt
{
    a("DeviceId", 0, 0), 
    b("AndroidAdvertisingId", 1, 13);
    
    public final int c;
    public final boolean d;
    
    private jt(final int ordinal, final boolean c) {
        this.c = (c ? 1 : 0);
        this.d = true;
    }
}
