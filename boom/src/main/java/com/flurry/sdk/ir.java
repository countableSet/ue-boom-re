// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

public enum ir
{
    a(1), 
    b(2), 
    c(3), 
    d(4);
    
    int e;
    
    private ir(final int e) {
        this.e = e;
    }
    
    public static ir a(final int n) {
        ir ir = null;
        switch (n) {
            default: {
                ir = null;
                break;
            }
            case 1: {
                ir = com.flurry.sdk.ir.a;
                break;
            }
            case 2: {
                ir = com.flurry.sdk.ir.b;
                break;
            }
            case 3: {
                ir = com.flurry.sdk.ir.c;
                break;
            }
            case 4: {
                ir = com.flurry.sdk.ir.d;
                break;
            }
        }
        return ir;
    }
}
