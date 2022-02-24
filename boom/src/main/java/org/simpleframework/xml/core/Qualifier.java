// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Namespace;

class Qualifier implements Decorator
{
    private NamespaceDecorator decorator;
    
    public Qualifier(final Contact contact) {
        this.decorator = new NamespaceDecorator();
        this.scan(contact);
    }
    
    private void namespace(final Contact contact) {
        final Namespace namespace = contact.getAnnotation(Namespace.class);
        if (namespace != null) {
            this.decorator.set(namespace);
            this.decorator.add(namespace);
        }
    }
    
    private void scan(final Contact contact) {
        this.namespace(contact);
        this.scope(contact);
    }
    
    private void scope(final Contact contact) {
        final NamespaceList list = contact.getAnnotation(NamespaceList.class);
        if (list != null) {
            final Namespace[] value = list.value();
            for (int length = value.length, i = 0; i < length; ++i) {
                this.decorator.add(value[i]);
            }
        }
    }
    
    @Override
    public void decorate(final OutputNode outputNode) {
        this.decorator.decorate(outputNode);
    }
    
    @Override
    public void decorate(final OutputNode outputNode, final Decorator decorator) {
        this.decorator.decorate(outputNode, decorator);
    }
}
