// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Iterator;
import org.simpleframework.xml.stream.NamespaceMap;
import org.simpleframework.xml.stream.OutputNode;
import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.Namespace;

class NamespaceDecorator implements Decorator
{
    private Namespace primary;
    private List<Namespace> scope;
    
    public NamespaceDecorator() {
        this.scope = new ArrayList<Namespace>();
    }
    
    private void namespace(final OutputNode outputNode) {
        if (this.primary != null) {
            outputNode.setReference(this.primary.reference());
        }
    }
    
    private void scope(final OutputNode outputNode) {
        final NamespaceMap namespaces = outputNode.getNamespaces();
        for (final Namespace namespace : this.scope) {
            namespaces.setReference(namespace.reference(), namespace.prefix());
        }
    }
    
    public void add(final Namespace namespace) {
        this.scope.add(namespace);
    }
    
    @Override
    public void decorate(final OutputNode outputNode) {
        this.decorate(outputNode, null);
    }
    
    @Override
    public void decorate(final OutputNode outputNode, final Decorator decorator) {
        if (decorator != null) {
            decorator.decorate(outputNode);
        }
        this.scope(outputNode);
        this.namespace(outputNode);
    }
    
    public void set(final Namespace primary) {
        if (primary != null) {
            this.add(primary);
        }
        this.primary = primary;
    }
}
