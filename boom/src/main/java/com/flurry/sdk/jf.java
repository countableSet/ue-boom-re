// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.Iterator;
import java.util.List;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.Closeable;
import java.util.Map;
import java.io.IOException;
import android.text.TextUtils;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;

public class jf
{
    private static final String b;
    byte[] a;
    
    static {
        b = jf.class.getSimpleName();
    }
    
    private jf() {
    }
    
    public jf(final jg jg) throws IOException {
        while (true) {
        Label_0476_Outer:
            while (true) {
            Label_1129:
                while (true) {
                    Label_1123: {
                    Label_0566:
                        while (true) {
                            try {
                                ByteArrayOutputStream out;
                                Closeable closeable;
                                DataOutputStream dataOutputStream;
                                Object o = null;
                                boolean v;
                                Object o2;
                                int b = 0;
                                int i;
                                Label_0176_Outer:Label_0335_Outer:Label_0357_Outer:
                                while (true) {
                                    out = new ByteArrayOutputStream();
                                    dataOutputStream = (DataOutputStream)(closeable = new DataOutputStream(out));
                                    Label_1059: {
                                        while (true) {
                                        Label_0379:
                                            while (true) {
                                            Label_0357:
                                                while (true) {
                                                Label_0892:
                                                    while (true) {
                                                        Label_0867: {
                                                            while (true) {
                                                            Label_0198:
                                                                while (true) {
                                                                Label_0176:
                                                                    while (true) {
                                                                    Label_0584:
                                                                        while (true) {
                                                                            Label_0573: {
                                                                                try {
                                                                                    dataOutputStream.writeShort(8);
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeUTF(jg.a);
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeLong(jg.b);
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeLong(jg.c);
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeLong(jg.d);
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeBoolean(true);
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeByte(-1);
                                                                                    closeable = dataOutputStream;
                                                                                    if (!TextUtils.isEmpty((CharSequence)jg.f)) {
                                                                                        closeable = dataOutputStream;
                                                                                        dataOutputStream.writeBoolean(true);
                                                                                        closeable = dataOutputStream;
                                                                                        dataOutputStream.writeUTF(jg.f);
                                                                                    }
                                                                                    else {
                                                                                        closeable = dataOutputStream;
                                                                                        dataOutputStream.writeBoolean(false);
                                                                                    }
                                                                                    closeable = dataOutputStream;
                                                                                    if (TextUtils.isEmpty((CharSequence)jg.g)) {
                                                                                        break Label_0573;
                                                                                    }
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeBoolean(true);
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeUTF(jg.g);
                                                                                    closeable = dataOutputStream;
                                                                                    o = jg.h;
                                                                                    if (o != null) {
                                                                                        break Label_0584;
                                                                                    }
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeShort(0);
                                                                                    closeable = dataOutputStream;
                                                                                    o = jg.e;
                                                                                    if (o != null) {
                                                                                        break Label_0176;
                                                                                    }
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeShort(0);
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeUTF(jg.i);
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeUTF(jg.j);
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeByte(jg.k);
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeByte(jg.l);
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeUTF(jg.m);
                                                                                    closeable = dataOutputStream;
                                                                                    if (jg.n != null) {
                                                                                        break Label_0198;
                                                                                    }
                                                                                    o = dataOutputStream;
                                                                                    v = false;
                                                                                    closeable = dataOutputStream;
                                                                                    ((DataOutputStream)o).writeBoolean(v);
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeInt(jg.o);
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeByte(-1);
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeByte(-1);
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeByte(jg.p);
                                                                                    closeable = dataOutputStream;
                                                                                    if (jg.q != null) {
                                                                                        break Label_0867;
                                                                                    }
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeBoolean(false);
                                                                                    closeable = dataOutputStream;
                                                                                    o = jg.r;
                                                                                    if (o != null) {
                                                                                        break Label_0892;
                                                                                    }
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeShort(0);
                                                                                    closeable = dataOutputStream;
                                                                                    o = jg.s;
                                                                                    if (o != null) {
                                                                                        break Label_0357;
                                                                                    }
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeShort(0);
                                                                                    closeable = dataOutputStream;
                                                                                    dataOutputStream.writeBoolean(jg.t);
                                                                                    closeable = dataOutputStream;
                                                                                    o = jg.v;
                                                                                    if (o == null) {
                                                                                        break Label_1123;
                                                                                    }
                                                                                    closeable = dataOutputStream;
                                                                                    o2 = ((List<Object>)o).iterator();
                                                                                    b = 0;
                                                                                    i = 0;
                                                                                    closeable = dataOutputStream;
                                                                                    if (!((Iterator)o2).hasNext()) {
                                                                                        break Label_0566;
                                                                                    }
                                                                                    closeable = dataOutputStream;
                                                                                    i += ((Iterator<ja>)o2).next().a().length;
                                                                                    if (i > 160000) {
                                                                                        closeable = dataOutputStream;
                                                                                        km.a(5, jf.b, "Error Log size exceeded. No more event details logged.");
                                                                                        closeable = dataOutputStream;
                                                                                        dataOutputStream.writeInt(jg.u);
                                                                                        closeable = dataOutputStream;
                                                                                        dataOutputStream.writeShort(b);
                                                                                        for (i = 0; i < b; ++i) {
                                                                                            closeable = dataOutputStream;
                                                                                            dataOutputStream.write(((List<ja>)o).get(i).a());
                                                                                        }
                                                                                        break Label_1059;
                                                                                    }
                                                                                    break Label_0379;
                                                                                }
                                                                                catch (IOException ex) {
                                                                                    closeable = dataOutputStream;
                                                                                    km.a(6, jf.b, "", (Throwable)jg);
                                                                                    closeable = dataOutputStream;
                                                                                    throw jg;
                                                                                }
                                                                                break Label_0566;
                                                                            }
                                                                            closeable = dataOutputStream;
                                                                            dataOutputStream.writeBoolean(false);
                                                                            continue Label_0176_Outer;
                                                                        }
                                                                        dataOutputStream.writeShort(((Map)o).size());
                                                                        o = ((Map<K, String>)o).entrySet().iterator();
                                                                        while (true) {
                                                                            closeable = dataOutputStream;
                                                                            if (!((Iterator)o).hasNext()) {
                                                                                continue Label_0176;
                                                                            }
                                                                            o2 = ((Iterator<Map.Entry<String, String>>)o).next();
                                                                            dataOutputStream.writeUTF(((Map.Entry<String, String>)o2).getKey());
                                                                            dataOutputStream.writeUTF(((Map.Entry<String, String>)o2).getValue());
                                                                        }
                                                                        break;
                                                                    }
                                                                    dataOutputStream.writeShort(((Map)o).size());
                                                                    o2 = ((Map<K, String>)o).entrySet().iterator();
                                                                    while (true) {
                                                                        closeable = dataOutputStream;
                                                                        if (!((Iterator)o2).hasNext()) {
                                                                            continue Label_0198;
                                                                        }
                                                                        o = ((Iterator<Map.Entry<String, String>>)o2).next();
                                                                        dataOutputStream.writeUTF(((Map.Entry<String, String>)o).getKey());
                                                                        dataOutputStream.writeUTF(((Map.Entry<String, String>)o).getValue());
                                                                        dataOutputStream.writeByte(0);
                                                                    }
                                                                    break;
                                                                }
                                                                b = jp.b();
                                                                dataOutputStream.writeBoolean(true);
                                                                dataOutputStream.writeDouble(ly.a(jg.n.getLatitude(), b));
                                                                dataOutputStream.writeDouble(ly.a(jg.n.getLongitude(), b));
                                                                closeable = dataOutputStream;
                                                                dataOutputStream.writeFloat(jg.n.getAccuracy());
                                                                if (b != -1) {
                                                                    v = true;
                                                                    o = dataOutputStream;
                                                                    continue Label_0335_Outer;
                                                                }
                                                                break;
                                                            }
                                                            break Label_1129;
                                                        }
                                                        dataOutputStream.writeBoolean(true);
                                                        closeable = dataOutputStream;
                                                        dataOutputStream.writeLong(jg.q);
                                                        continue Label_0357_Outer;
                                                    }
                                                    dataOutputStream.writeShort(((Map)o).size());
                                                    o2 = ((Map<K, String>)o).entrySet().iterator();
                                                    while (true) {
                                                        closeable = dataOutputStream;
                                                        if (!((Iterator)o2).hasNext()) {
                                                            continue Label_0357;
                                                        }
                                                        o = ((Iterator<Map.Entry<String, String>>)o2).next();
                                                        dataOutputStream.writeUTF(((Map.Entry<String, String>)o).getKey());
                                                        dataOutputStream.writeInt(((Map.Entry<String, jb>)o).getValue().a);
                                                    }
                                                    break;
                                                }
                                                dataOutputStream.writeShort(((List)o).size());
                                                o = ((List<ja>)o).iterator();
                                                while (true) {
                                                    closeable = dataOutputStream;
                                                    if (!((Iterator)o).hasNext()) {
                                                        continue Label_0379;
                                                    }
                                                    dataOutputStream.write(((Iterator<jc>)o).next().b());
                                                }
                                                break;
                                            }
                                            ++b;
                                            continue Label_0476_Outer;
                                        }
                                        ly.a(closeable);
                                        throw jg;
                                    }
                                    dataOutputStream.writeInt(-1);
                                    dataOutputStream.writeShort(0);
                                    dataOutputStream.writeShort(0);
                                    dataOutputStream.writeShort(0);
                                    this.a = out.toByteArray();
                                    ly.a(dataOutputStream);
                                    return;
                                }
                            }
                            catch (IOException ex2) {}
                            finally {
                                final Closeable closeable = null;
                                continue Label_0566;
                            }
                            break;
                        }
                        continue;
                    }
                    int b = 0;
                    continue;
                }
                continue Label_0476_Outer;
            }
        }
    }
    
    public jf(final byte[] a) {
        this.a = a;
    }
    
    public static final class a implements lg<jf>
    {
    }
}
