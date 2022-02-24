// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Map;
import java.util.Collection;
import org.simpleframework.xml.transform.Transform;
import org.simpleframework.xml.stream.Style;
import java.util.List;
import java.lang.annotation.Annotation;
import org.simpleframework.xml.strategy.Value;
import java.io.Serializable;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.filter.PlatformFilter;
import org.simpleframework.xml.transform.Transformer;
import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.filter.Filter;

class Support implements Filter
{
    private final DetailExtractor defaults;
    private final DetailExtractor details;
    private final Filter filter;
    private final Format format;
    private final InstanceFactory instances;
    private final LabelExtractor labels;
    private final Matcher matcher;
    private final ScannerFactory scanners;
    private final Transformer transform;
    
    public Support() {
        this(new PlatformFilter());
    }
    
    public Support(final Filter filter) {
        this(filter, new EmptyMatcher());
    }
    
    public Support(final Filter filter, final Matcher matcher) {
        this(filter, matcher, new Format());
    }
    
    public Support(final Filter filter, final Matcher matcher, final Format format) {
        this.defaults = new DetailExtractor(this, DefaultType.FIELD);
        this.transform = new Transformer(matcher);
        this.scanners = new ScannerFactory(this);
        this.details = new DetailExtractor(this);
        this.labels = new LabelExtractor(format);
        this.instances = new InstanceFactory();
        this.matcher = matcher;
        this.filter = filter;
        this.format = format;
    }
    
    private String getClassName(final Class clazz) throws Exception {
        Class componentType = clazz;
        if (clazz.isArray()) {
            componentType = clazz.getComponentType();
        }
        String s = componentType.getSimpleName();
        if (!componentType.isPrimitive()) {
            s = Reflector.getName(s);
        }
        return s;
    }
    
    public static Class getPrimitive(final Class clazz) {
        Serializable s;
        if (clazz == Double.TYPE) {
            s = Double.class;
        }
        else if (clazz == Float.TYPE) {
            s = Float.class;
        }
        else if (clazz == Integer.TYPE) {
            s = Integer.class;
        }
        else if (clazz == Long.TYPE) {
            s = Long.class;
        }
        else if (clazz == Boolean.TYPE) {
            s = Boolean.class;
        }
        else if (clazz == Character.TYPE) {
            s = Character.class;
        }
        else if (clazz == Short.TYPE) {
            s = Short.class;
        }
        else if ((s = clazz) == Byte.TYPE) {
            s = Byte.class;
        }
        return (Class)s;
    }
    
    public static boolean isAssignable(Class primitive, final Class clazz) {
        Class primitive2 = primitive;
        if (primitive.isPrimitive()) {
            primitive2 = getPrimitive(primitive);
        }
        primitive = clazz;
        if (clazz.isPrimitive()) {
            primitive = getPrimitive(clazz);
        }
        return primitive.isAssignableFrom(primitive2);
    }
    
    public static boolean isFloat(final Class clazz) throws Exception {
        final boolean b = true;
        boolean b2;
        if (clazz == Double.class) {
            b2 = b;
        }
        else {
            b2 = b;
            if (clazz != Float.class) {
                b2 = b;
                if (clazz != Float.TYPE) {
                    b2 = b;
                    if (clazz != Double.TYPE) {
                        b2 = false;
                    }
                }
            }
        }
        return b2;
    }
    
    public Detail getDetail(final Class clazz) {
        return this.getDetail(clazz, null);
    }
    
    public Detail getDetail(final Class clazz, final DefaultType defaultType) {
        Detail detail;
        if (defaultType != null) {
            detail = this.defaults.getDetail(clazz);
        }
        else {
            detail = this.details.getDetail(clazz);
        }
        return detail;
    }
    
    public ContactList getFields(final Class clazz) throws Exception {
        return this.getFields(clazz, null);
    }
    
    public ContactList getFields(final Class clazz, final DefaultType defaultType) throws Exception {
        ContactList list;
        if (defaultType != null) {
            list = this.defaults.getFields(clazz);
        }
        else {
            list = this.details.getFields(clazz);
        }
        return list;
    }
    
    public Format getFormat() {
        return this.format;
    }
    
    public Instance getInstance(final Class clazz) {
        return this.instances.getInstance(clazz);
    }
    
    public Instance getInstance(final Value value) {
        return this.instances.getInstance(value);
    }
    
    public Label getLabel(final Contact contact, final Annotation annotation) throws Exception {
        return this.labels.getLabel(contact, annotation);
    }
    
    public List<Label> getLabels(final Contact contact, final Annotation annotation) throws Exception {
        return this.labels.getList(contact, annotation);
    }
    
    public ContactList getMethods(final Class clazz) throws Exception {
        return this.getMethods(clazz, null);
    }
    
    public ContactList getMethods(final Class clazz, final DefaultType defaultType) throws Exception {
        ContactList list;
        if (defaultType != null) {
            list = this.defaults.getMethods(clazz);
        }
        else {
            list = this.details.getMethods(clazz);
        }
        return list;
    }
    
    public String getName(final Class clazz) throws Exception {
        final String name = this.getScanner(clazz).getName();
        String className;
        if (name != null) {
            className = name;
        }
        else {
            className = this.getClassName(clazz);
        }
        return className;
    }
    
    public Scanner getScanner(final Class clazz) throws Exception {
        return this.scanners.getInstance(clazz);
    }
    
    public Style getStyle() {
        return this.format.getStyle();
    }
    
    public Transform getTransform(final Class clazz) throws Exception {
        return this.matcher.match(clazz);
    }
    
    public boolean isContainer(final Class clazz) {
        boolean array = true;
        if (!Collection.class.isAssignableFrom(clazz) && !Map.class.isAssignableFrom(clazz)) {
            array = clazz.isArray();
        }
        return array;
    }
    
    public boolean isPrimitive(final Class clazz) throws Exception {
        final boolean b = true;
        boolean valid;
        if (clazz == String.class) {
            valid = b;
        }
        else {
            valid = b;
            if (clazz != Float.class) {
                valid = b;
                if (clazz != Double.class) {
                    valid = b;
                    if (clazz != Long.class) {
                        valid = b;
                        if (clazz != Integer.class) {
                            valid = b;
                            if (clazz != Boolean.class) {
                                valid = b;
                                if (!clazz.isEnum()) {
                                    valid = b;
                                    if (!clazz.isPrimitive()) {
                                        valid = this.transform.valid(clazz);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return valid;
    }
    
    public Object read(final String s, final Class clazz) throws Exception {
        return this.transform.read(s, clazz);
    }
    
    @Override
    public String replace(final String s) {
        return this.filter.replace(s);
    }
    
    public boolean valid(final Class clazz) throws Exception {
        return this.transform.valid(clazz);
    }
    
    public String write(final Object o, final Class clazz) throws Exception {
        return this.transform.write(o, clazz);
    }
}
