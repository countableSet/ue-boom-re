// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.Position;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.strategy.Type;
import java.lang.annotation.Annotation;

class Variable implements Label
{
    private final Label label;
    private final Object value;
    
    public Variable(final Label label, final Object value) {
        this.label = label;
        this.value = value;
    }
    
    @Override
    public Annotation getAnnotation() {
        return this.label.getAnnotation();
    }
    
    @Override
    public Contact getContact() {
        return this.label.getContact();
    }
    
    @Override
    public Converter getConverter(final Context context) throws Exception {
        Converter converter = this.label.getConverter(context);
        if (!(converter instanceof Adapter)) {
            converter = new Adapter(converter, this.label, this.value);
        }
        return converter;
    }
    
    @Override
    public Decorator getDecorator() throws Exception {
        return this.label.getDecorator();
    }
    
    @Override
    public Type getDependent() throws Exception {
        return this.label.getDependent();
    }
    
    @Override
    public Object getEmpty(final Context context) throws Exception {
        return this.label.getEmpty(context);
    }
    
    @Override
    public String getEntry() throws Exception {
        return this.label.getEntry();
    }
    
    @Override
    public Expression getExpression() throws Exception {
        return this.label.getExpression();
    }
    
    @Override
    public Object getKey() throws Exception {
        return this.label.getKey();
    }
    
    @Override
    public Label getLabel(final Class clazz) {
        return this;
    }
    
    @Override
    public String getName() throws Exception {
        return this.label.getName();
    }
    
    @Override
    public String[] getNames() throws Exception {
        return this.label.getNames();
    }
    
    @Override
    public String getOverride() {
        return this.label.getOverride();
    }
    
    @Override
    public String getPath() throws Exception {
        return this.label.getPath();
    }
    
    @Override
    public String[] getPaths() throws Exception {
        return this.label.getPaths();
    }
    
    @Override
    public Class getType() {
        return this.label.getType();
    }
    
    @Override
    public Type getType(final Class clazz) throws Exception {
        return this.label.getType(clazz);
    }
    
    public Object getValue() {
        return this.value;
    }
    
    @Override
    public boolean isAttribute() {
        return this.label.isAttribute();
    }
    
    @Override
    public boolean isCollection() {
        return this.label.isCollection();
    }
    
    @Override
    public boolean isData() {
        return this.label.isData();
    }
    
    @Override
    public boolean isInline() {
        return this.label.isInline();
    }
    
    @Override
    public boolean isRequired() {
        return this.label.isRequired();
    }
    
    @Override
    public boolean isText() {
        return this.label.isText();
    }
    
    @Override
    public boolean isTextList() {
        return this.label.isTextList();
    }
    
    @Override
    public boolean isUnion() {
        return this.label.isUnion();
    }
    
    @Override
    public String toString() {
        return this.label.toString();
    }
    
    private static class Adapter implements Repeater
    {
        private final Label label;
        private final Converter reader;
        private final Object value;
        
        public Adapter(final Converter reader, final Label label, final Object value) {
            this.reader = reader;
            this.value = value;
            this.label = label;
        }
        
        @Override
        public Object read(final InputNode inputNode) throws Exception {
            return this.read(inputNode, this.value);
        }
        
        @Override
        public Object read(final InputNode inputNode, final Object o) throws Exception {
            final Position position = inputNode.getPosition();
            final String name = inputNode.getName();
            if (this.reader instanceof Repeater) {
                return ((Repeater)this.reader).read(inputNode, o);
            }
            throw new PersistenceException("Element '%s' is already used with %s at %s", new Object[] { name, this.label, position });
        }
        
        @Override
        public boolean validate(final InputNode inputNode) throws Exception {
            final Position position = inputNode.getPosition();
            final String name = inputNode.getName();
            if (this.reader instanceof Repeater) {
                return ((Repeater)this.reader).validate(inputNode);
            }
            throw new PersistenceException("Element '%s' declared twice at %s", new Object[] { name, position });
        }
        
        @Override
        public void write(final OutputNode outputNode, final Object o) throws Exception {
            this.write(outputNode, o);
        }
    }
}
