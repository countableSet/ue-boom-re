// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import java.io.Serializable;

class ClassTransform implements Transform<Class>
{
    private static final String BOOLEAN = "boolean";
    private static final String BYTE = "byte";
    private static final String CHARACTER = "char";
    private static final String DOUBLE = "double";
    private static final String FLOAT = "float";
    private static final String INTEGER = "int";
    private static final String LONG = "long";
    private static final String SHORT = "short";
    private static final String VOID = "void";
    
    private ClassLoader getCallerClassLoader() {
        return this.getClass().getClassLoader();
    }
    
    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
    
    private Class readPrimitive(final String s) throws Exception {
        Serializable s2;
        if (s.equals("byte")) {
            s2 = Byte.TYPE;
        }
        else if (s.equals("short")) {
            s2 = Short.TYPE;
        }
        else if (s.equals("int")) {
            s2 = Integer.TYPE;
        }
        else if (s.equals("long")) {
            s2 = Long.TYPE;
        }
        else if (s.equals("char")) {
            s2 = Character.TYPE;
        }
        else if (s.equals("float")) {
            s2 = Float.TYPE;
        }
        else if (s.equals("double")) {
            s2 = Double.TYPE;
        }
        else if (s.equals("boolean")) {
            s2 = Boolean.TYPE;
        }
        else if (s.equals("void")) {
            s2 = Void.TYPE;
        }
        else {
            s2 = null;
        }
        return (Class)s2;
    }
    
    @Override
    public Class read(final String name) throws Exception {
        Class<?> clazz;
        if ((clazz = (Class<?>)this.readPrimitive(name)) == null) {
            ClassLoader classLoader;
            if ((classLoader = getClassLoader()) == null) {
                classLoader = this.getCallerClassLoader();
            }
            clazz = classLoader.loadClass(name);
        }
        return clazz;
    }
    
    @Override
    public String write(final Class clazz) throws Exception {
        return clazz.getName();
    }
}
