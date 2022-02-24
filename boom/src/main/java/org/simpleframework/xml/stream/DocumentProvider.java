// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import java.io.Reader;
import java.io.InputStream;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilderFactory;

class DocumentProvider implements Provider
{
    private final DocumentBuilderFactory factory;
    
    public DocumentProvider() {
        (this.factory = DocumentBuilderFactory.newInstance()).setNamespaceAware(true);
    }
    
    private EventReader provide(final InputSource inputSource) throws Exception {
        return new DocumentReader(this.factory.newDocumentBuilder().parse(inputSource));
    }
    
    @Override
    public EventReader provide(final InputStream byteStream) throws Exception {
        return this.provide(new InputSource(byteStream));
    }
    
    @Override
    public EventReader provide(final Reader characterStream) throws Exception {
        return this.provide(new InputSource(characterStream));
    }
}
