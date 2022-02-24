// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;

public interface lg<ObjectType>
{
    ObjectType a(final InputStream p0) throws IOException;
    
    void a(final OutputStream p0, final ObjectType p1) throws IOException;
}
