// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

class InputElement implements InputNode
{
    private final InputNodeMap map;
    private final EventNode node;
    private final InputNode parent;
    private final NodeReader reader;
    
    public InputElement(final InputNode parent, final NodeReader reader, final EventNode node) {
        this.map = new InputNodeMap(this, node);
        this.reader = reader;
        this.parent = parent;
        this.node = node;
    }
    
    @Override
    public InputNode getAttribute(final String s) {
        return this.map.get(s);
    }
    
    @Override
    public NodeMap<InputNode> getAttributes() {
        return this.map;
    }
    
    @Override
    public String getName() {
        return this.node.getName();
    }
    
    @Override
    public InputNode getNext() throws Exception {
        return this.reader.readElement(this);
    }
    
    @Override
    public InputNode getNext(final String s) throws Exception {
        return this.reader.readElement(this, s);
    }
    
    @Override
    public InputNode getParent() {
        return this.parent;
    }
    
    @Override
    public Position getPosition() {
        return new InputPosition(this.node);
    }
    
    @Override
    public String getPrefix() {
        return this.node.getPrefix();
    }
    
    @Override
    public String getReference() {
        return this.node.getReference();
    }
    
    @Override
    public Object getSource() {
        return this.node.getSource();
    }
    
    @Override
    public String getValue() throws Exception {
        return this.reader.readValue(this);
    }
    
    @Override
    public boolean isElement() {
        return true;
    }
    
    @Override
    public boolean isEmpty() throws Exception {
        return this.map.isEmpty() && this.reader.isEmpty(this);
    }
    
    @Override
    public boolean isRoot() {
        return this.reader.isRoot(this);
    }
    
    @Override
    public void skip() throws Exception {
        this.reader.skipElement(this);
    }
    
    @Override
    public String toString() {
        return String.format("element %s", this.getName());
    }
}
