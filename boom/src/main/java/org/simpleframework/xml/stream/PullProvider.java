// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import java.io.Reader;
import org.xmlpull.v1.XmlPullParser;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParserFactory;

class PullProvider implements Provider
{
    private final XmlPullParserFactory factory;
    
    public PullProvider() throws Exception {
        (this.factory = XmlPullParserFactory.newInstance()).setNamespaceAware(true);
    }
    
    @Override
    public EventReader provide(final InputStream inputStream) throws Exception {
        final XmlPullParser pullParser = this.factory.newPullParser();
        if (inputStream != null) {
            pullParser.setInput(inputStream, (String)null);
        }
        return new PullReader(pullParser);
    }
    
    @Override
    public EventReader provide(final Reader input) throws Exception {
        final XmlPullParser pullParser = this.factory.newPullParser();
        if (input != null) {
            pullParser.setInput(input);
        }
        return new PullReader(pullParser);
    }
}
