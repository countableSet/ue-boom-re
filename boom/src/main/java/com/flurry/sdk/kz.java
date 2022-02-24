// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.UUID;

public final class kz
{
    String a;
    byte[] b;
    
    private kz() {
        this.a = null;
        this.b = null;
    }
    
    public kz(final byte[] b) {
        this.a = null;
        this.b = null;
        this.a = UUID.randomUUID().toString();
        this.b = b;
    }
    
    public static String a(final String str) {
        return ".yflurrydatasenderblock." + str;
    }
    
    public static final class a implements lg<kz>
    {
    }
}
