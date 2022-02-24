// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.io.OutputStream;
import java.io.InputStream;

public final class ks<RequestObjectType, ResponseObjectType> extends ku
{
    public a<RequestObjectType, ResponseObjectType> a;
    public RequestObjectType b;
    public lg<RequestObjectType> c;
    public lg<ResponseObjectType> d;
    private ResponseObjectType x;
    
    static /* synthetic */ void d(final ks ks) {
        if (ks.a != null && !ks.b()) {
            ks.a.a(ks, ks.x);
        }
    }
    
    @Override
    public final void a() {
        super.k = new c() {
            @Override
            public final void a(final ku ku) {
                ks.d(ks.this);
            }
            
            @Override
            public final void a(final ku ku, final InputStream inputStream) throws Exception {
                if (ku.d() && ks.this.d != null) {
                    ks.this.x = (ResponseObjectType)ks.this.d.a(inputStream);
                }
            }
            
            @Override
            public final void a(final OutputStream outputStream) throws Exception {
                if (ks.this.b != null && ks.this.c != null) {
                    ks.this.c.a(outputStream, ks.this.b);
                }
            }
        };
        super.a();
    }
    
    public interface a<RequestObjectType, ResponseObjectType>
    {
        void a(final ks<RequestObjectType, ResponseObjectType> p0, final ResponseObjectType p1);
    }
}
