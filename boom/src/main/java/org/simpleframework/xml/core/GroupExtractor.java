// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.Text;
import java.util.LinkedHashMap;
import java.util.Iterator;
import org.simpleframework.xml.stream.Format;
import java.lang.annotation.Annotation;

class GroupExtractor implements Group
{
    private final LabelMap elements;
    private final ExtractorFactory factory;
    private final Annotation label;
    private final Registry registry;
    
    public GroupExtractor(final Contact contact, final Annotation label, final Format format) throws Exception {
        this.factory = new ExtractorFactory(contact, label, format);
        this.elements = new LabelMap();
        this.registry = new Registry(this.elements);
        this.label = label;
        this.extract();
    }
    
    private void extract() throws Exception {
        final Extractor instance = this.factory.getInstance();
        if (instance != null) {
            this.extract(instance);
        }
    }
    
    private void extract(final Extractor extractor) throws Exception {
        final Annotation[] annotations = extractor.getAnnotations();
        for (int length = annotations.length, i = 0; i < length; ++i) {
            this.extract(extractor, annotations[i]);
        }
    }
    
    private void extract(final Extractor extractor, final Annotation annotation) throws Exception {
        final Label label = extractor.getLabel(annotation);
        final Class type = extractor.getType(annotation);
        if (this.registry != null) {
            this.registry.register(type, label);
        }
    }
    
    @Override
    public LabelMap getElements() throws Exception {
        return this.elements.getLabels();
    }
    
    @Override
    public Label getLabel(final Class clazz) {
        return this.registry.resolve(clazz);
    }
    
    public String[] getNames() throws Exception {
        return this.elements.getKeys();
    }
    
    public String[] getPaths() throws Exception {
        return this.elements.getPaths();
    }
    
    @Override
    public Label getText() {
        return this.registry.resolveText();
    }
    
    public boolean isDeclared(final Class key) {
        return this.registry.containsKey(key);
    }
    
    @Override
    public boolean isInline() {
        boolean b = false;
        final Iterator<Label> iterator = this.registry.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().isInline()) {
                return b;
            }
        }
        if (!this.registry.isEmpty()) {
            b = true;
            return b;
        }
        return b;
    }
    
    @Override
    public boolean isTextList() {
        return this.registry.isText();
    }
    
    public boolean isValid(final Class clazz) {
        return this.registry.resolve(clazz) != null;
    }
    
    @Override
    public String toString() {
        return this.label.toString();
    }
    
    private static class Registry extends LinkedHashMap<Class, Label> implements Iterable<Label>
    {
        private LabelMap elements;
        private Label text;
        
        public Registry(final LabelMap elements) {
            this.elements = elements;
        }
        
        private void registerElement(final Class clazz, final Label label) throws Exception {
            final String name = label.getName();
            if (!this.elements.containsKey(name)) {
                this.elements.put(name, label);
            }
            if (!this.containsKey(clazz)) {
                this.put(clazz, label);
            }
        }
        
        private void registerText(final Label label) throws Exception {
            final Text text = label.getContact().getAnnotation(Text.class);
            if (text != null) {
                this.text = new TextListLabel(label, text);
            }
        }
        
        private Label resolveElement(Class superclass) {
            while (superclass != null) {
                final Label label = ((LinkedHashMap<K, Label>)this).get(superclass);
                if (label != null) {
                    return label;
                }
                superclass = superclass.getSuperclass();
            }
            return null;
        }
        
        private Label resolveText(final Class clazz) {
            Label text;
            if (this.text != null && clazz == String.class) {
                text = this.text;
            }
            else {
                text = null;
            }
            return text;
        }
        
        public boolean isText() {
            return this.text != null;
        }
        
        @Override
        public Iterator<Label> iterator() {
            return ((LinkedHashMap<K, Label>)this).values().iterator();
        }
        
        public void register(final Class clazz, final Label label) throws Exception {
            final CacheLabel cacheLabel = new CacheLabel(label);
            this.registerElement(clazz, cacheLabel);
            this.registerText(cacheLabel);
        }
        
        public Label resolve(final Class clazz) {
            Label label;
            if ((label = this.resolveText(clazz)) == null) {
                label = this.resolveElement(clazz);
            }
            return label;
        }
        
        public Label resolveText() {
            return this.resolveText(String.class);
        }
    }
}
