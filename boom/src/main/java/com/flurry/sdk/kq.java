// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

public class kq<ObjectType>
{
    private static final String a;
    private static final byte[] b;
    private String c;
    private lg<ObjectType> d;
    
    static {
        a = kq.class.getSimpleName();
        b = new byte[] { 113, -92, -8, 125, 121, 107, -65, -61, -74, -114, -32, 0, -57, -87, -35, -56, -6, -52, 51, 126, -104, 49, 79, -52, 118, -84, 99, -52, -14, -126, -27, -64 };
    }
    
    public kq(final String c, final lg<ObjectType> d) {
        this.c = c;
        this.d = d;
    }
    
    public static int a(final byte[] input) {
        int b;
        if (input == null) {
            b = 0;
        }
        else {
            final ke ke = new ke();
            ke.update(input);
            b = ke.b();
        }
        return b;
    }
    
    private static void c(final byte[] array) {
        if (array != null) {
            final int length = array.length;
            final int length2 = kq.b.length;
            for (int i = 0; i < length; ++i) {
                array[i] = (byte)(array[i] ^ kq.b[i % length2] ^ i * 31 % 251);
            }
        }
    }
    
    private static void d(final byte[] array) {
        c(array);
    }
    
    public final byte[] a(final ObjectType objectType) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.d.a(byteArrayOutputStream, objectType);
        final byte[] byteArray = byteArrayOutputStream.toByteArray();
        km.a(3, kq.a, "Encoding " + this.c + ": " + new String(byteArray));
        final le<byte[]> le = new le<byte[]>(new lc());
        final ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        le.a(byteArrayOutputStream2, byteArray);
        final byte[] byteArray2 = byteArrayOutputStream2.toByteArray();
        c(byteArray2);
        return byteArray2;
    }
    
    public final ObjectType b(byte[] buf) throws IOException {
        if (buf == null) {
            throw new IOException("Decoding: " + this.c + ": Nothing to decode");
        }
        d(buf);
        buf = new le<byte[]>(new lc()).a(new ByteArrayInputStream(buf));
        km.a(3, kq.a, "Decoding: " + this.c + ": " + new String(buf));
        return this.d.a(new ByteArrayInputStream(buf));
    }
}
