// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

class OutputElement implements OutputNode
{
    private String comment;
    private Mode mode;
    private String name;
    private OutputNode parent;
    private String reference;
    private NamespaceMap scope;
    private OutputNodeMap table;
    private String value;
    private NodeWriter writer;
    
    public OutputElement(final OutputNode parent, final NodeWriter writer, final String name) {
        this.scope = new PrefixResolver(parent);
        this.table = new OutputNodeMap(this);
        this.mode = Mode.INHERIT;
        this.writer = writer;
        this.parent = parent;
        this.name = name;
    }
    
    @Override
    public void commit() throws Exception {
        this.writer.commit(this);
    }
    
    @Override
    public OutputNodeMap getAttributes() {
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
        return this.name;
    }
    
    @Override
    public NamespaceMap getNamespaces() {
        return this.scope;
    }
    
    @Override
    public OutputNode getParent() {
        return this.parent;
    }
    
    @Override
    public String getPrefix() {
        return this.getPrefix(true);
    }
    
    @Override
    public String getPrefix(final boolean b) {
        String s = this.scope.getPrefix(this.reference);
        if (b && (s = s) == null) {
            s = this.parent.getPrefix();
        }
        return s;
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
        return this.writer.isCommitted(this);
    }
    
    @Override
    public boolean isRoot() {
        return this.writer.isRoot(this);
    }
    
    @Override
    public void remove() throws Exception {
        this.writer.remove(this);
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
    
    @Override
    public String toString() {
        return String.format("element %s", this.name);
    }
}
