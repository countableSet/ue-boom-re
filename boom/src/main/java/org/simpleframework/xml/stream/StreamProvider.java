// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import java.io.Reader;
import java.io.InputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;

class StreamProvider implements Provider
{
    private final XMLInputFactory factory;
    
    public StreamProvider() {
        this.factory = XMLInputFactory.newInstance();
    }
    
    private EventReader provide(final XMLEventReader xmlEventReader) throws Exception {
        return new StreamReader(xmlEventReader);
    }
    
    @Override
    public EventReader provide(final InputStream inputStream) throws Exception {
        return this.provide(this.factory.createXMLEventReader(inputStream));
    }
    
    @Override
    public EventReader provide(final Reader reader) throws Exception {
        return this.provide(this.factory.createXMLEventReader(reader));
    }
}
