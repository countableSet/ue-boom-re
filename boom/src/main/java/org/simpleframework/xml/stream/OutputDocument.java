// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

class OutputDocument implements OutputNode
{
    private String comment;
    private Mode mode;
    private String name;
    private String reference;
    private OutputStack stack;
    private OutputNodeMap table;
    private String value;
    private NodeWriter writer;
    
    public OutputDocument(final NodeWriter writer, final OutputStack stack) {
        this.table = new OutputNodeMap(this);
        this.mode = Mode.INHERIT;
        this.writer = writer;
        this.stack = stack;
    }
    
    @Override
    public void commit() throws Exception {
        if (this.stack.isEmpty()) {
            throw new NodeException("No root node");
        }
        this.stack.bottom().commit();
    }
    
    @Override
    public NodeMap<OutputNode> getAttributes() {
        return this.table;
    }
    
    @Override
    public OutputNode getChild(final String s) throws Exception {
        return this.writer.writeElement(this, s);
    }
    
    @Override
    public String getComment() {
        return this.comment;
    }
    
    @Override
    public Mode getMode() {
        return this.mode;
    }
    
    @Override
    public String getName() {
        return null;
    }
    
    @Override
    public NamespaceMap getNamespaces() {
        return null;
    }
    
    @Override
    public OutputNode getParent() {
        return null;
    }
    
    @Override
    public String getPrefix() {
        return null;
    }
    
    @Override
    public String getPrefix(final boolean b) {
        return null;
    }
    
    @Override
    public String getReference() {
        return this.reference;
    }
    
    @Override
    public String getValue() throws Exception {
        return this.value;
    }
    
    @Override
    public boolean isCommitted() {
        return this.stack.isEmpty();
    }
    
    @Override
    public boolean isRoot() {
        return true;
    }
    
    @Override
    public void remove() throws Exception {
        if (this.stack.isEmpty()) {
            throw new NodeException("No root node");
        }
        this.stack.bottom().remove();
    }
    
    @Override
    public OutputNode setAttribute(final String s, final String s2) {
        return this.table.put(s, s2);
    }
    
    @Override
    public void setComment(final String comment) {
        this.comment = comment;
    }
    
    @Override
    public void setData(final boolean b) {
        if (b) {
            this.mode = Mode.DATA;
        }
        else {
            this.mode = Mode.ESCAPE;
        }
    }
    
    @Override
    public void setMode(final Mode mode) {
        this.mode = mode;
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
}
