// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.io.IOException;
import java.io.Closeable;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;

public final class ja
{
    public String a;
    private int b;
    private long c;
    private String d;
    private String e;
    private Throwable f;
    
    public ja(final int b, final long c, final String a, final String d, final String e, final Throwable f) {
        this.b = b;
        this.c = c;
        this.a = a;
        this.d = d;
        this.e = e;
        this.f = f;
    }
    
    public final byte[] a() {
        while (true) {
            try {
                byte[] out = (Object)new ByteArrayOutputStream();
                Object byteArray;
                final DataOutputStream dataOutputStream = (DataOutputStream)(byteArray = new DataOutputStream((OutputStream)(Object)out));
                Label_0354: {
                    try {
                        dataOutputStream.writeShort(this.b);
                        byteArray = dataOutputStream;
                        dataOutputStream.writeLong(this.c);
                        byteArray = dataOutputStream;
                        dataOutputStream.writeUTF(this.a);
                        byteArray = dataOutputStream;
                        dataOutputStream.writeUTF(this.d);
                        byteArray = dataOutputStream;
                        dataOutputStream.writeUTF(this.e);
                        byteArray = dataOutputStream;
                        if (this.f != null) {
                            byteArray = dataOutputStream;
                            if ("uncaught".equals(this.a)) {
                                byteArray = dataOutputStream;
                                dataOutputStream.writeByte(3);
                            }
                            else {
                                byteArray = dataOutputStream;
                                dataOutputStream.writeByte(2);
                            }
                            byteArray = dataOutputStream;
                            dataOutputStream.writeByte(2);
                            byteArray = dataOutputStream;
                            byteArray = dataOutputStream;
                            final Object bytes = new StringBuilder("");
                            byteArray = dataOutputStream;
                            final String property = System.getProperty("line.separator");
                            byteArray = dataOutputStream;
                            final StackTraceElement[] stackTrace = this.f.getStackTrace();
                            byteArray = dataOutputStream;
                            for (int length = stackTrace.length, i = 0; i < length; ++i) {
                                byteArray = dataOutputStream;
                                ((StringBuilder)bytes).append(stackTrace[i]);
                                byteArray = dataOutputStream;
                                ((StringBuilder)bytes).append(property);
                            }
                            break Label_0354;
                        }
                        break Label_0354;
                    }
                    catch (IOException ex) {
                        byteArray = dataOutputStream;
                        out = new byte[0];
                        ly.a(dataOutputStream);
                        byteArray = out;
                        return (byte[])byteArray;
                        // iftrue(Label_0303:, n >= length2)
                        while (true) {
                            while (true) {
                                Label_0267: {
                                    Object bytes;
                                    final String property;
                                    while (true) {
                                        byteArray = dataOutputStream;
                                        dataOutputStream.flush();
                                        byteArray = dataOutputStream;
                                        out = (byte[])(byteArray = ((ByteArrayOutputStream)(Object)out).toByteArray());
                                        ly.a(dataOutputStream);
                                        return (byte[])byteArray;
                                        byteArray = dataOutputStream;
                                        final StackTraceElement[] stackTrace2;
                                        int n = 0;
                                        ((StringBuilder)bytes).append(stackTrace2[n]);
                                        byteArray = dataOutputStream;
                                        ((StringBuilder)bytes).append(property);
                                        ++n;
                                        break Label_0267;
                                        Label_0303: {
                                            byteArray = dataOutputStream;
                                        }
                                        bytes = ((StringBuilder)bytes).toString().getBytes();
                                        byteArray = dataOutputStream;
                                        dataOutputStream.writeInt(((StringBuilder)bytes).length);
                                        byteArray = dataOutputStream;
                                        dataOutputStream.write((byte[])bytes);
                                        continue;
                                        byteArray = dataOutputStream;
                                        dataOutputStream.writeByte(1);
                                        byteArray = dataOutputStream;
                                        dataOutputStream.writeByte(0);
                                        continue;
                                    }
                                    byteArray = dataOutputStream;
                                    ((StringBuilder)bytes).append(property);
                                    byteArray = dataOutputStream;
                                    ((StringBuilder)bytes).append("Caused by: ");
                                    byteArray = dataOutputStream;
                                    final StackTraceElement[] stackTrace2 = this.f.getCause().getStackTrace();
                                    byteArray = dataOutputStream;
                                    final int length2 = stackTrace2.length;
                                    int n = 0;
                                }
                                continue;
                            }
                            byteArray = dataOutputStream;
                            continue;
                        }
                    }
                    // iftrue(Label_0303:, this.f.getCause() == null)
                }
                ly.a((Closeable)byteArray);
                throw dataOutputStream;
            }
            catch (IOException ex2) {}
            finally {
                final Object byteArray = null;
                continue;
            }
            break;
        }
    }
}
