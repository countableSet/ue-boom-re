// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Iterator;
import org.simpleframework.xml.stream.OutputNode;
import java.util.Collection;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.strategy.Type;

class TextList implements Repeater
{
    private final CollectionFactory factory;
    private final Primitive primitive;
    private final Type type;
    
    public TextList(final Context context, final Type type, final Label label) {
        this.type = new ClassType(String.class);
        this.factory = new CollectionFactory(context, type);
        this.primitive = new Primitive(context, this.type);
    }
    
    @Override
    public Object read(final InputNode inputNode) throws Exception {
        final Instance instance = this.factory.getInstance(inputNode);
        final Object instance2 = instance.getInstance();
        Object o;
        if (instance.isReference()) {
            o = instance.getInstance();
        }
        else {
            o = this.read(inputNode, instance2);
        }
        return o;
    }
    
    @Override
    public Object read(final InputNode inputNode, final Object o) throws Exception {
        final Collection collection = (Collection)o;
        final Object read = this.primitive.read(inputNode);
        if (read != null) {
            collection.add(read);
        }
        return o;
    }
    
    @Override
    public boolean validate(final InputNode inputNode) throws Exception {
        return true;
    }
    
    @Override
    public void write(OutputNode parent, final Object o) throws Exception {
        final Collection collection = (Collection)o;
        parent = parent.getParent();
        final Iterator<Object> iterator = collection.iterator();
        while (iterator.hasNext()) {
            this.primitive.write(parent, iterator.next());
        }
    }
}
