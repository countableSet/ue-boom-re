// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStream;

public final class li<T> implements lg<T>
{
    private final String a;
    private final int b;
    private final lj<T> c;
    
    public li(final String a, final int b, final lj<T> c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    @Override
    public final T a(final InputStream inputStream) throws IOException {
        T a;
        if (inputStream == null || this.c == null) {
            a = null;
        }
        else {
            final DataInputStream dataInputStream = new DataInputStream(inputStream) {
                @Override
                public final void close() {
                }
            };
            final String utf = dataInputStream.readUTF();
            if (!this.a.equals(utf)) {
                throw new IOException("Signature: " + utf + " is invalid");
            }
            a = this.c.a(dataInputStream.readInt()).a(dataInputStream);
        }
        return a;
    }
    
    @Override
    public final void a(final OutputStream outputStream, final T t) throws IOException {
        if (outputStream != null && this.c != null) {
            final DataOutputStream dataOutputStream = new DataOutputStream(outputStream) {
                @Override
                public final void close() {
                }
            };
            dataOutputStream.writeUTF(this.a);
            dataOutputStream.writeInt(this.b);
            this.c.a(this.b).a(dataOutputStream, t);
            dataOutputStream.flush();
        }
    }
}
