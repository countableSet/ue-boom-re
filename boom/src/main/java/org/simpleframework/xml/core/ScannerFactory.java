// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.util.ConcurrentCache;
import org.simpleframework.xml.util.Cache;

class ScannerFactory
{
    private final Cache<Scanner> cache;
    private final Support support;
    
    public ScannerFactory(final Support support) {
        this.cache = new ConcurrentCache<Scanner>();
        this.support = support;
    }
    
    public Scanner getInstance(final Class clazz) throws Exception {
        Scanner scanner;
        if ((scanner = this.cache.fetch(clazz)) == null) {
            final Detail detail = this.support.getDetail(clazz);
            if (this.support.isPrimitive(clazz)) {
                scanner = new PrimitiveScanner(detail);
            }
            else {
                final ObjectScanner objectScanner = (ObjectScanner)(scanner = new ObjectScanner(detail, this.support));
                if (objectScanner.isPrimitive()) {
                    scanner = objectScanner;
                    if (!this.support.isContainer(clazz)) {
                        scanner = new DefaultScanner(detail, this.support);
                    }
                }
            }
            this.cache.cache(clazz, scanner);
        }
        return scanner;
    }
}
