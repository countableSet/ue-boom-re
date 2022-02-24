// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.Text;
import java.lang.reflect.Constructor;

class TextParameter extends TemplateParameter
{
    private final Contact contact;
    private final Expression expression;
    private final int index;
    private final Object key;
    private final Label label;
    private final String name;
    private final String path;
    private final Class type;
    
    public TextParameter(final Constructor constructor, final Text text, final Format format, final int index) throws Exception {
        this.contact = new Contact(text, constructor, index);
        this.label = new TextLabel(this.contact, text, format);
        this.expression = this.label.getExpression();
        this.path = this.label.getPath();
        this.type = this.label.getType();
        this.name = this.label.getName();
        this.key = this.label.getKey();
        this.index = index;
    }
    
    @Override
    public Annotation getAnnotation() {
        return this.contact.getAnnotation();
    }
    
    @Override
    public Expression getExpression() {
        return this.expression;
    }
    
    @Override
    public int getIndex() {
        return this.index;
    }
    
    @Override
    public Object getKey() {
        return this.key;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public String getName(final Context context) {
        return this.getName();
    }
    
    @Override
    public String getPath() {
        return this.path;
    }
    
    public String getPath(final Context context) {
        return this.getPath();
    }
    
    @Override
    public Class getType() {
        return this.type;
    }
    
    @Override
    public boolean isPrimitive() {
        return this.type.isPrimitive();
    }
    
    @Override
    public boolean isRequired() {
        return this.label.isRequired();
    }
    
    @Override
    public boolean isText() {
        return true;
    }
    
    @Override
    public String toString() {
        return this.contact.toString();
    }
    
    private static class Contact extends ParameterContact<Text>
    {
        public Contact(final Text text, final Constructor constructor, final int n) {
            super(text, constructor, n);
        }
        
        @Override
        public String getName() {
            return "";
        }
    }
}
