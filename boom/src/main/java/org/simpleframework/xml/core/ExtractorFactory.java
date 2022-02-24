// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Element;
import java.lang.reflect.Constructor;
import org.simpleframework.xml.ElementMapUnion;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.ElementUnion;
import java.lang.annotation.Annotation;
import org.simpleframework.xml.stream.Format;

class ExtractorFactory
{
    private final Contact contact;
    private final Format format;
    private final Annotation label;
    
    public ExtractorFactory(final Contact contact, final Annotation label, final Format format) {
        this.contact = contact;
        this.format = format;
        this.label = label;
    }
    
    private ExtractorBuilder getBuilder(final Annotation annotation) throws Exception {
        ExtractorBuilder extractorBuilder;
        if (annotation instanceof ElementUnion) {
            extractorBuilder = new ExtractorBuilder(ElementUnion.class, ElementExtractor.class);
        }
        else if (annotation instanceof ElementListUnion) {
            extractorBuilder = new ExtractorBuilder(ElementListUnion.class, ElementListExtractor.class);
        }
        else {
            if (!(annotation instanceof ElementMapUnion)) {
                throw new PersistenceException("Annotation %s is not a union", new Object[] { annotation });
            }
            extractorBuilder = new ExtractorBuilder(ElementMapUnion.class, ElementMapExtractor.class);
        }
        return extractorBuilder;
    }
    
    private Object getInstance(final Annotation annotation) throws Exception {
        final Constructor access$000 = this.getBuilder(annotation).getConstructor();
        if (!access$000.isAccessible()) {
            access$000.setAccessible(true);
        }
        return access$000.newInstance(this.contact, annotation, this.format);
    }
    
    public Extractor getInstance() throws Exception {
        return (Extractor)this.getInstance(this.label);
    }
    
    private static class ElementExtractor implements Extractor<Element>
    {
        private final Contact contact;
        private final Format format;
        private final ElementUnion union;
        
        public ElementExtractor(final Contact contact, final ElementUnion union, final Format format) throws Exception {
            this.contact = contact;
            this.format = format;
            this.union = union;
        }
        
        @Override
        public Element[] getAnnotations() {
            return this.union.value();
        }
        
        @Override
        public Label getLabel(final Element element) {
            return new ElementLabel(this.contact, element, this.format);
        }
        
        @Override
        public Class getType(final Element element) {
            Class clazz;
            if ((clazz = element.type()) == Void.TYPE) {
                clazz = this.contact.getType();
            }
            return clazz;
        }
    }
    
    private static class ElementListExtractor implements Extractor<ElementList>
    {
        private final Contact contact;
        private final Format format;
        private final ElementListUnion union;
        
        public ElementListExtractor(final Contact contact, final ElementListUnion union, final Format format) throws Exception {
            this.contact = contact;
            this.format = format;
            this.union = union;
        }
        
        @Override
        public ElementList[] getAnnotations() {
            return this.union.value();
        }
        
        @Override
        public Label getLabel(final ElementList list) {
            return new ElementListLabel(this.contact, list, this.format);
        }
        
        @Override
        public Class getType(final ElementList list) {
            return list.type();
        }
    }
    
    private static class ElementMapExtractor implements Extractor<ElementMap>
    {
        private final Contact contact;
        private final Format format;
        private final ElementMapUnion union;
        
        public ElementMapExtractor(final Contact contact, final ElementMapUnion union, final Format format) throws Exception {
            this.contact = contact;
            this.format = format;
            this.union = union;
        }
        
        @Override
        public ElementMap[] getAnnotations() {
            return this.union.value();
        }
        
        @Override
        public Label getLabel(final ElementMap elementMap) {
            return new ElementMapLabel(this.contact, elementMap, this.format);
        }
        
        @Override
        public Class getType(final ElementMap elementMap) {
            return elementMap.valueType();
        }
    }
    
    private static class ExtractorBuilder
    {
        private final Class label;
        private final Class type;
        
        public ExtractorBuilder(final Class label, final Class type) {
            this.label = label;
            this.type = type;
        }
        
        private Constructor getConstructor() throws Exception {
            return this.type.getConstructor(Contact.class, this.label, Format.class);
        }
    }
}
