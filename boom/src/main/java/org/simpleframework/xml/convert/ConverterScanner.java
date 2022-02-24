// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.convert;

import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.Root;
import java.lang.annotation.Annotation;

class ConverterScanner
{
    private final ScannerBuilder builder;
    private final ConverterFactory factory;
    
    public ConverterScanner() {
        this.factory = new ConverterFactory();
        this.builder = new ScannerBuilder();
    }
    
    private <T extends Annotation> T getAnnotation(final Class<?> clazz, final Class<T> clazz2) {
        return this.builder.build(clazz).scan(clazz2);
    }
    
    private Convert getConvert(final Class clazz) throws Exception {
        final Convert convert = this.getAnnotation(clazz, Convert.class);
        if (convert != null && this.getAnnotation(clazz, Root.class) == null) {
            throw new ConvertException("Root annotation required for %s", new Object[] { clazz });
        }
        return convert;
    }
    
    private Convert getConvert(final Type type) throws Exception {
        final Convert convert = type.getAnnotation(Convert.class);
        if (convert != null && type.getAnnotation(Element.class) == null) {
            throw new ConvertException("Element annotation required for %s", new Object[] { type });
        }
        return convert;
    }
    
    private Convert getConvert(final Type type, final Class clazz) throws Exception {
        Convert convert;
        if ((convert = this.getConvert(type)) == null) {
            convert = this.getConvert(clazz);
        }
        return convert;
    }
    
    private Class getType(final Type type, final Object o) {
        Class<?> clazz = (Class<?>)type.getType();
        if (o != null) {
            clazz = o.getClass();
        }
        return clazz;
    }
    
    private Class getType(final Type type, final Value value) {
        Class clazz = type.getType();
        if (value != null) {
            clazz = value.getType();
        }
        return clazz;
    }
    
    public Converter getConverter(final Type type, final Object o) throws Exception {
        final Convert convert = this.getConvert(type, this.getType(type, o));
        Converter instance;
        if (convert != null) {
            instance = this.factory.getInstance(convert);
        }
        else {
            instance = null;
        }
        return instance;
    }
    
    public Converter getConverter(final Type type, final Value value) throws Exception {
        final Convert convert = this.getConvert(type, this.getType(type, value));
        Converter instance;
        if (convert != null) {
            instance = this.factory.getInstance(convert);
        }
        else {
            instance = null;
        }
        return instance;
    }
}
