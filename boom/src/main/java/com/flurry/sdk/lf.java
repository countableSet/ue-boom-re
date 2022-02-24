// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.ArrayList;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class lf<T> implements lg<List<T>>
{
    lg<T> a;
    
    public lf(final lg<T> a) {
        this.a = a;
    }
    
    @Override
    public final void a(final OutputStream outputStream, final List<T> list) throws IOException {
        int i = 0;
        if (outputStream != null) {
            final DataOutputStream dataOutputStream = new DataOutputStream(outputStream) {
                @Override
                public final void close() {
                }
            };
            int size;
            if (list != null) {
                size = list.size();
            }
            else {
                size = 0;
            }
            dataOutputStream.writeInt(size);
            while (i < size) {
                this.a.a(outputStream, list.get(i));
                ++i;
            }
            dataOutputStream.flush();
        }
    }
    
    public final List<T> b(final InputStream inputStream) throws IOException {
        if (inputStream != null) {
            final int int1 = new DataInputStream(inputStream) {
                @Override
                public final void close() {
                }
            }.readInt();
            final ArrayList list = new ArrayList<T>(int1);
            int n = 0;
            while (true) {
                final List<T> list2 = (List<T>)list;
                if (n >= int1) {
                    return list2;
                }
                final T a = this.a.a(inputStream);
                if (a == null) {
                    break;
                }
                list.add(a);
                ++n;
            }
            throw new IOException("Missing record.");
        }
        return null;
    }
}
