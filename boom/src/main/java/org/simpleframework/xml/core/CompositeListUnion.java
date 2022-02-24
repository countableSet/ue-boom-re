// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.util.Collections;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Style;

class CompositeListUnion implements Repeater
{
    private final Context context;
    private final LabelMap elements;
    private final Group group;
    private final Expression path;
    private final Style style;
    private final Type type;
    
    public CompositeListUnion(final Context context, final Group group, final Expression path, final Type type) throws Exception {
        this.elements = group.getElements();
        this.style = context.getStyle();
        this.context = context;
        this.group = group;
        this.type = type;
        this.path = path;
    }
    
    private Object readElement(final InputNode inputNode) throws Exception {
        return ((LinkedHashMap<K, Label>)this.elements).get(this.path.getElement(inputNode.getName())).getConverter(this.context).read(inputNode);
    }
    
    private Object readElement(final InputNode inputNode, final Object o) throws Exception {
        return ((LinkedHashMap<K, Label>)this.elements).get(this.path.getElement(inputNode.getName())).getConverter(this.context).read(inputNode, o);
    }
    
    private Object readText(final InputNode inputNode) throws Exception {
        return this.group.getText().getConverter(this.context).read(inputNode);
    }
    
    private Object readText(final InputNode inputNode, final Object o) throws Exception {
        return this.group.getText().getConverter(this.context).read(inputNode.getParent(), o);
    }
    
    private void write(final OutputNode outputNode, final Object o, final Label label) throws Exception {
        final Converter converter = label.getConverter(this.context);
        final Set<Object> singleton = Collections.singleton(o);
        if (!label.isInline()) {
            final String element = this.style.getElement(label.getName());
            if (!outputNode.isCommitted()) {
                outputNode.setName(element);
            }
        }
        converter.write(outputNode, singleton);
    }
    
    private void write(final OutputNode outputNode, final Collection collection) throws Exception {
        for (final Object next : collection) {
            if (next != null) {
                final Class<?> class1 = next.getClass();
                final Label label = this.group.getLabel(class1);
                if (label == null) {
                    throw new UnionException("Entry of %s not declared in %s with annotation %s", new Object[] { class1, this.type, this.group });
                }
                this.write(outputNode, next, label);
            }
        }
    }
    
    @Override
    public Object read(final InputNode inputNode) throws Exception {
        Object o;
        if (this.group.getText() == null) {
            o = this.readElement(inputNode);
        }
        else {
            o = this.readText(inputNode);
        }
        return o;
    }
    
    @Override
    public Object read(final InputNode inputNode, final Object o) throws Exception {
        Object o2 = this.readElement(inputNode, o);
        if (this.group.getText() != null) {
            o2 = this.readText(inputNode, o);
        }
        return o2;
    }
    
    @Override
    public boolean validate(final InputNode inputNode) throws Exception {
        return ((LinkedHashMap<K, Label>)this.elements).get(this.path.getElement(inputNode.getName())).getConverter(this.context).validate(inputNode);
    }
    
    @Override
    public void write(final OutputNode outputNode, final Object o) throws Exception {
        final Collection collection = (Collection)o;
        if (this.group.isInline()) {
            if (!collection.isEmpty()) {
                this.write(outputNode, collection);
            }
            else if (!outputNode.isCommitted()) {
                outputNode.remove();
            }
        }
        else {
            this.write(outputNode, collection);
        }
    }
}
