// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.LinkedHashMap;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.strategy.Type;

class CompositeUnion implements Converter
{
    private final Context context;
    private final LabelMap elements;
    private final Group group;
    private final Expression path;
    private final Type type;
    
    public CompositeUnion(final Context context, final Group group, final Expression path, final Type type) throws Exception {
        this.elements = group.getElements();
        this.context = context;
        this.group = group;
        this.type = type;
        this.path = path;
    }
    
    private void write(final OutputNode outputNode, final Object o, final Label label) throws Exception {
        label.getConverter(this.context).write(outputNode, o);
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
        final Class<?> class1 = o.getClass();
        final Label label = this.group.getLabel(class1);
        if (label == null) {
            throw new UnionException("Value of %s not declared in %s with annotation %s", new Object[] { class1, this.type, this.group });
        }
        this.write(outputNode, o, label);
    }
}
