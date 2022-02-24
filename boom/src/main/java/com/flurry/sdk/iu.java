// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.zip.Checksum;
import java.util.Iterator;
import java.util.zip.CRC32;
import android.os.Build$VERSION;
import android.os.Build;
import java.io.IOException;
import java.util.Map;
import java.util.Collections;
import java.io.Closeable;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class iu
{
    public static final String b;
    private static iu c;
    public String a;
    private kf<List<iv>> d;
    private List<iv> e;
    private boolean f;
    
    static {
        b = iu.class.getName();
        iu.c = null;
    }
    
    private iu() {
    }
    
    public static iu a() {
        synchronized (iu.class) {
            if (iu.c == null) {
                final iu iu = com.flurry.sdk.iu.c = new iu();
                iu.d = new kf<List<iv>>(jy.a().a.getFileStreamPath(".yflurrypulselogging." + Long.toString(ly.i(jy.a().d), 16)), ".yflurrypulselogging.", 1, (lj<Object>)new lj<List<iv>>() {
                    @Override
                    public final lg<List<iv>> a(final int n) {
                        return new lf<iv>(new iv.a());
                    }
                });
                iu.f = (boolean)lp.a().a("UseHttps");
                km.a(4, com.flurry.sdk.iu.b, "initSettings, UseHttps = " + iu.f);
                iu.e = iu.d.a();
                if (iu.e == null) {
                    iu.e = new ArrayList<iv>();
                }
            }
            return iu.c;
        }
    }
    
    private void a(final byte[] array) {
        while (true) {
            Label_0049: {
                synchronized (this) {
                    if (!jr.a().b) {
                        km.a(5, iu.b, "Reports were not sent! No Internet connection!");
                    }
                    else {
                        if (array != null && array.length != 0) {
                            break Label_0049;
                        }
                        km.a(3, iu.b, "No report need be sent");
                    }
                    return;
                }
            }
            String a;
            if (this.a != null) {
                a = this.a;
            }
            else {
                a = "https://data.flurry.com/pcr.do";
            }
            final byte[] array2;
            km.a(4, iu.b, "PulseLoggingManager: start upload data " + Arrays.toString(array2) + " to " + a);
            final ks ks = new ks();
            ks.f = a;
            ks.w = 100000;
            ks.g = ku.a.c;
            ks.j = true;
            ks.a("Content-Type", "application/octet-stream");
            ks.c = (lg<RequestObjectType>)new lc();
            ks.b = (RequestObjectType)(Object)array2;
            ks.a = (ks.a<RequestObjectType, ResponseObjectType>)new ks.a<byte[], Void>() {};
            ((kl<ks>)jw.a()).a(this, ks);
        }
    }
    
    private byte[] d() throws IOException {
    Label_0275_Outer:
        while (true) {
        Label_0275:
            while (true) {
                try {
                    while (true) {
                        final ByteArrayOutputStream out = new ByteArrayOutputStream();
                        DataOutputStream dataOutputStream = new DataOutputStream(out);
                        Object bytes = null;
                        Label_0281: {
                            while (true) {
                                try {
                                    if (this.e == null || this.e.isEmpty()) {
                                        final byte[] array = out.toByteArray();
                                        ly.a(dataOutputStream);
                                        return array;
                                    }
                                    dataOutputStream.writeShort(1);
                                    dataOutputStream.writeShort(1);
                                    dataOutputStream.writeLong(System.currentTimeMillis());
                                    dataOutputStream.writeUTF(jy.a().d);
                                    dataOutputStream.writeUTF(ju.a().g());
                                    dataOutputStream.writeShort(jz.a());
                                    dataOutputStream.writeShort(3);
                                    ju.a();
                                    dataOutputStream.writeUTF(ju.c());
                                    dataOutputStream.writeBoolean(jl.a().c());
                                    bytes = new ArrayList<ia>();
                                    for (final Map.Entry<K, byte[]> entry : Collections.unmodifiableMap((Map<?, ?>)jl.a().a).entrySet()) {
                                        final ia ia = new ia();
                                        ia.a = ((jt)entry.getKey()).c;
                                        if (!((jt)entry.getKey()).d) {
                                            break Label_0281;
                                        }
                                        ia.b = new String(entry.getValue());
                                        ((List<ia>)bytes).add(ia);
                                    }
                                }
                                catch (IOException out) {
                                    Object iterator = dataOutputStream;
                                    try {
                                        km.a(6, iu.b, "Error when generating report", (Throwable)out);
                                        throw out;
                                    }
                                    finally {
                                        dataOutputStream = (DataOutputStream)iterator;
                                        iterator = out;
                                    }
                                    ly.a(dataOutputStream);
                                    throw iterator;
                                    final Map.Entry<K, byte[]> entry;
                                    final ia ia;
                                    ia.b = ly.b(entry.getValue());
                                    continue Label_0275_Outer;
                                }
                                finally {
                                    continue Label_0275;
                                }
                                break;
                            }
                        }
                        dataOutputStream.writeShort(((List)bytes).size());
                        final Iterator<ia> iterator2 = ((List<ia>)bytes).iterator();
                        while (iterator2.hasNext()) {
                            bytes = iterator2.next();
                            dataOutputStream.writeShort(((ia)bytes).a);
                            bytes = ((ia)bytes).b.getBytes();
                            dataOutputStream.writeShort(((ia)bytes).length);
                            dataOutputStream.write((byte[])bytes);
                        }
                        dataOutputStream.writeShort(6);
                        dataOutputStream.writeShort(in.b.h);
                        dataOutputStream.writeUTF(Build.MODEL);
                        dataOutputStream.writeShort(in.c.h);
                        dataOutputStream.writeUTF(Build.BOARD);
                        dataOutputStream.writeShort(in.d.h);
                        dataOutputStream.writeUTF(Build.ID);
                        dataOutputStream.writeShort(in.e.h);
                        dataOutputStream.writeUTF(Build.DEVICE);
                        dataOutputStream.writeShort(in.f.h);
                        dataOutputStream.writeUTF(Build.PRODUCT);
                        dataOutputStream.writeShort(in.g.h);
                        dataOutputStream.writeUTF(Build$VERSION.RELEASE);
                        dataOutputStream.writeShort(this.e.size());
                        final Iterator<iv> iterator3 = this.e.iterator();
                        while (iterator3.hasNext()) {
                            dataOutputStream.write(iterator3.next().a);
                        }
                        final byte[] byteArray = out.toByteArray();
                        bytes = new CRC32();
                        ((Checksum)bytes).update(byteArray);
                        dataOutputStream.writeInt((int)((CRC32)bytes).getValue());
                        final byte[] array = out.toByteArray();
                        ly.a(dataOutputStream);
                        return array;
                    }
                }
                catch (IOException out) {
                    continue Label_0275_Outer;
                }
                finally {
                    final DataOutputStream dataOutputStream = null;
                    continue Label_0275;
                }
                break;
            }
            break;
        }
    }
    
    public final void a(final it it) {
        synchronized (this) {
            try {
                this.e.add(new iv(it.d()));
                km.a(4, iu.b, "Saving persistent Pulse logging data.");
                this.d.a(this.e);
            }
            catch (IOException ex) {
                km.a(6, iu.b, "Error when generating pulse log report in addReport part");
            }
        }
    }
    
    public final void b() {
        synchronized (this) {
            try {
                this.a(this.d());
            }
            catch (IOException ex) {
                km.a(6, iu.b, "Report not send due to exception in generate data");
            }
        }
    }
}
