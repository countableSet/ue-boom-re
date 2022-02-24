// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

class OutputAttribute implements OutputNode
{
    private String name;
    private String reference;
    private NamespaceMap scope;
    private OutputNode source;
    private String value;
    
    public OutputAttribute(final OutputNode source, final String name, final String value) {
        this.scope = source.getNamespaces();
        this.source = source;
        this.value = value;
        this.name = name;
    }
    
    @Override
    public void commit() {
    }
    
    @Override
    public NodeMap<OutputNode> getAttributes() {
        return new OutputNodeMap(this);
    }
    
    @Override
    public OutputNode getChild(final String s) {
        return null;
    }
    
    @Override
    public String getComment() {
        return null;
    }
    
    @Override
    public Mode getMode() {
        return Mode.INHERIT;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public NamespaceMap getNamespaces() {
        return this.scope;
    }
    
    @Override
    public OutputNode getParent() {
        return this.source;
    }
    
    @Override
    public String getPrefix() {
        return this.scope.getPrefix(this.reference);
    }
    
    @Override
    public String getPrefix(final boolean b) {
        return this.scope.getPrefix(this.reference);
    }
    
    @Override
    public String getReference() {
        return this.reference;
    }
    
    @Override
    public String getValue() {
        return this.value;
    }
    
    @Override
    public boolean isCommitted() {
        return true;
    }
    
    @Override
    public boolean isRoot() {
        return false;
    }
    
    @Override
    public void remove() {
    }
    
    @Override
    public OutputNode setAttribute(final String s, final String s2) {
        return null;
    }
    
    @Override
    public void setComment(final String s) {
    }
    
    @Override
    public void setData(final boolean b) {
    }
    
    @Override
    public void setMode(final Mode mode) {
    }
    
    @Override
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public void setReference(final String reference) {
        this.reference = reference;
    }
    
    @Override
    public void setValue(final String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return String.format("attribute %s='%s'", this.name, this.value);
    }
}
