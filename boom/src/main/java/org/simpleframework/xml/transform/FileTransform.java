// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import java.io.File;

class FileTransform implements Transform<File>
{
    @Override
    public File read(final String pathname) {
        return new File(pathname);
    }
    
    @Override
    public String write(final File file) {
        return file.getPath();
    }
}
