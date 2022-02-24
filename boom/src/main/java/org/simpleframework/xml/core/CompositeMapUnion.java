// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.LinkedHashMap;
import org.simpleframework.xml.stream.InputNode;
import java.util.Iterator;
import java.util.Map;
import java.util.Collections;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Style;

class CompositeMapUnion implements Repeater
{
    private final Context context;
    private final LabelMap elements;
    private final Group group;
    private final Expression path;
    private final Style style;
    private final Type type;
    
    public CompositeMapUnion(final Context context, final Group group, final Expression path, final Type type) throws Exception {
        this.elements = group.getElements();
        this.style = context.getStyle();
        this.context = context;
        this.group = group;
        this.type = type;
        this.path = path;
    }
    
    private void write(final OutputNode outputNode, final Object key, final Object value, final Label label) throws Exception {
        final Converter converter = label.getConverter(this.context);
        final Map<Object, Object> singletonMap = Collections.singletonMap(key, value);
        if (!label.isInline()) {
            final String element = this.style.getElement(label.getName());
            if (!outputNode.isCommitted()) {
                outputNode.setName(element);
            }
        }
        converter.write(outputNode, singletonMap);
    }
    
    private void write(final OutputNode outputNode, final Map map) throws Exception {
        for (final Object next : map.keySet()) {
            final V value = map.get(next);
            if (value != null) {
                final Class<?> class1 = value.getClass();
                final Label label = this.group.getLabel(class1);
                if (label == null) {
                    throw new UnionException("Value of %s not declared in %s with annotation %s", new Object[] { class1, this.type, this.group });
                }
                this.write(outputNode, next, value, label);
            }
        }
    }
    
    @Override
    public Object read(final InputNode inputNode) throws Exception {
        return ((LinkedHashMap<K, Label>)this.elements).get(this.path.getElement(inputNode.getName())).getConverter(this.context).read(inputNode);
    }
    
    @Override
    public Object read(final InputNode inputNode, final Object o) throws Exception {
        return ((LinkedHashMap<K, Label>)this.elements).get(this.path.getElement(inputNode.getName())).getConverter(this.context).read(inputNode, o);
    }
    
    @Override
    public boolean validate(final InputNode inputNode) throws Exception {
        return ((LinkedHashMap<K, Label>)this.elements).get(this.path.getElement(inputNode.getName())).getConverter(this.context).validate(inputNode);
    }
    
    @Override
    public void write(final OutputNode outputNode, final Object o) throws Exception {
        final Map map = (Map)o;
        if (this.group.isInline()) {
            if (!map.isEmpty()) {
                this.write(outputNode, map);
            }
            else if (!outputNode.isCommitted()) {
                outputNode.remove();
            }
        }
        else {
            this.write(outputNode, map);
        }
    }
}
