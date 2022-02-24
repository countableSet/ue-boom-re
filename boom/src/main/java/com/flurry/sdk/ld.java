// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ld<ObjectType> implements lg<ObjectType>
{
    protected final lg<ObjectType> a;
    
    public ld(final lg<ObjectType> a) {
        this.a = a;
    }
    
    @Override
    public ObjectType a(final InputStream inputStream) throws IOException {
        ObjectType a;
        if (this.a != null && inputStream != null) {
            a = this.a.a(inputStream);
        }
        else {
            a = null;
        }
        return a;
    }
    
    @Override
    public void a(final OutputStream outputStream, final ObjectType objectType) throws IOException {
        if (this.a != null && outputStream != null && objectType != null) {
            this.a.a(outputStream, objectType);
        }
    }
}
