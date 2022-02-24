// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import java.net.URL;

class URLTransform implements Transform<URL>
{
    @Override
    public URL read(final String spec) throws Exception {
        return new URL(spec);
    }
    
    @Override
    public String write(final URL url) throws Exception {
        return url.toString();
    }
}
