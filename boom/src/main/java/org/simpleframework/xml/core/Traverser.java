// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Style;

class Traverser
{
    private final Context context;
    private final Style style;
    
    public Traverser(final Context context) {
        this.style = context.getStyle();
        this.context = context;
    }
    
    private Composite getComposite(final Class clazz) throws Exception {
        final Type type = this.getType(clazz);
        if (clazz == null) {
            throw new RootException("Can not instantiate null class", new Object[0]);
        }
        return new Composite(this.context, type);
    }
    
    private Decorator getDecorator(final Class clazz) throws Exception {
        return this.context.getDecorator(clazz);
    }
    
    private Type getType(final Class clazz) {
        return new ClassType(clazz);
    }
    
    private Object read(final InputNode inputNode, final Class clazz, final Object o) throws Exception {
        if (this.getName(clazz) == null) {
            throw new RootException("Root annotation required for %s", new Object[] { clazz });
        }
        return o;
    }
    
    protected String getName(final Class clazz) throws Exception {
        return this.style.getElement(this.context.getName(clazz));
    }
    
    public Object read(final InputNode inputNode, final Class clazz) throws Exception {
        final Object read = this.getComposite(clazz).read(inputNode);
        Object read2;
        if (read != null) {
            read2 = this.read(inputNode, read.getClass(), read);
        }
        else {
            read2 = null;
        }
        return read2;
    }
    
    public Object read(final InputNode inputNode, final Object o) throws Exception {
        final Class<?> class1 = o.getClass();
        return this.read(inputNode, class1, this.getComposite(class1).read(inputNode, o));
    }
    
    public boolean validate(final InputNode inputNode, final Class clazz) throws Exception {
        final Composite composite = this.getComposite(clazz);
        if (this.getName(clazz) == null) {
            throw new RootException("Root annotation required for %s", new Object[] { clazz });
        }
        return composite.validate(inputNode);
    }
    
    public void write(final OutputNode outputNode, final Object o) throws Exception {
        this.write(outputNode, o, o.getClass());
    }
    
    public void write(final OutputNode outputNode, final Object o, final Class clazz) throws Exception {
        final Class<?> class1 = o.getClass();
        final String name = this.getName(class1);
        if (name == null) {
            throw new RootException("Root annotation required for %s", new Object[] { class1 });
        }
        this.write(outputNode, o, clazz, name);
    }
    
    public void write(OutputNode child, final Object o, final Class clazz, final String s) throws Exception {
        child = child.getChild(s);
        final Type type = this.getType(clazz);
        if (o != null) {
            final Class<?> class1 = o.getClass();
            final Decorator decorator = this.getDecorator(class1);
            if (decorator != null) {
                decorator.decorate(child);
            }
            if (!this.context.setOverride(type, o, child)) {
                this.getComposite(class1).write(child, o);
            }
        }
        child.commit();
    }
}
