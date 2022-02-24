// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.strategy;

class Loader
{
    private static ClassLoader getCallerClassLoader() throws Exception {
        return Loader.class.getClassLoader();
    }
    
    private static ClassLoader getClassLoader() throws Exception {
        return Thread.currentThread().getContextClassLoader();
    }
    
    public Class load(final String name) throws Exception {
        ClassLoader classLoader;
        if ((classLoader = getClassLoader()) == null) {
            classLoader = getCallerClassLoader();
        }
        return classLoader.loadClass(name);
    }
}
