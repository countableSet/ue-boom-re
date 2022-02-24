// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import java.io.Reader;
import java.io.InputStream;

interface Provider
{
    EventReader provide(final InputStream p0) throws Exception;
    
    EventReader provide(final Reader p0) throws Exception;
}
