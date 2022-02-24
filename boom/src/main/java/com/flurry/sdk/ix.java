// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

public enum ix
{
    a(1), 
    b(2), 
    c(3), 
    d(4);
    
    final int e;
    
    private ix(final int e) {
        this.e = e;
    }
    
    public static ix a(final int n) {
        ix ix = null;
        switch (n) {
            default: {
                ix = null;
                break;
            }
            case 1: {
                ix = com.flurry.sdk.ix.a;
                break;
            }
            case 2: {
                ix = com.flurry.sdk.ix.b;
                break;
            }
            case 3: {
                ix = com.flurry.sdk.ix.c;
                break;
            }
            case 4: {
                ix = com.flurry.sdk.ix.d;
                break;
            }
        }
        return ix;
    }
}
