// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

class InputAttribute implements InputNode
{
    private String name;
    private InputNode parent;
    private String prefix;
    private String reference;
    private Object source;
    private String value;
    
    public InputAttribute(final InputNode parent, final String name, final String value) {
        this.parent = parent;
        this.value = value;
        this.name = name;
    }
    
    public InputAttribute(final InputNode parent, final Attribute attribute) {
        this.reference = attribute.getReference();
        this.prefix = attribute.getPrefix();
        this.source = attribute.getSource();
        this.value = attribute.getValue();
        this.name = attribute.getName();
        this.parent = parent;
    }
    
    @Override
    public InputNode getAttribute(final String s) {
        return null;
    }
    
    @Override
    public NodeMap<InputNode> getAttributes() {
        return new InputNodeMap(this);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public InputNode getNext() {
        return null;
    }
    
    @Override
    public InputNode getNext(final String s) {
        return null;
    }
    
    @Override
    public InputNode getParent() {
        return this.parent;
    }
    
    @Override
    public Position getPosition() {
        return this.parent.getPosition();
    }
    
    @Override
    public String getPrefix() {
        return this.prefix;
    }
    
    @Override
    public String getReference() {
        return this.reference;
    }
    
    @Override
    public Object getSource() {
        return this.source;
    }
    
    @Override
    public String getValue() {
        return this.value;
    }
    
    @Override
    public boolean isElement() {
        return false;
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }
    
    @Override
    public boolean isRoot() {
        return false;
    }
    
    @Override
    public void skip() {
    }
    
    @Override
    public String toString() {
        return String.format("attribute %s='%s'", this.name, this.value);
    }
}
