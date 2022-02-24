// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import java.io.Writer;
import java.io.Reader;
import java.io.InputStream;

public final class NodeBuilder
{
    private static Provider PROVIDER;
    
    static {
        NodeBuilder.PROVIDER = ProviderFactory.getInstance();
    }
    
    public static InputNode read(final InputStream inputStream) throws Exception {
        return read(NodeBuilder.PROVIDER.provide(inputStream));
    }
    
    public static InputNode read(final Reader reader) throws Exception {
        return read(NodeBuilder.PROVIDER.provide(reader));
    }
    
    private static InputNode read(final EventReader eventReader) throws Exception {
        return new NodeReader(eventReader).readRoot();
    }
    
    public static OutputNode write(final Writer writer) throws Exception {
        return write(writer, new Format());
    }
    
    public static OutputNode write(final Writer writer, final Format format) throws Exception {
        return new NodeWriter(writer, format).writeRoot();
    }
}
