// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

public enum iw
{
    a("GET", 0), 
    b("PUT", 1), 
    c("POST", 2);
    
    String d;
    int e;
    
    private iw(final String d, final int e) {
        this.d = d;
        this.e = e;
    }
    
    public static iw a(final int n) {
        iw iw = null;
        switch (n) {
            default: {
                iw = null;
                break;
            }
            case 0: {
                iw = com.flurry.sdk.iw.a;
                break;
            }
            case 1: {
                iw = com.flurry.sdk.iw.b;
                break;
            }
            case 2: {
                iw = com.flurry.sdk.iw.c;
                break;
            }
        }
        return iw;
    }
}
