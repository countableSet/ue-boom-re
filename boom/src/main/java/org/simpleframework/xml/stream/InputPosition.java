// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

class InputPosition implements Position
{
    private EventNode source;
    
    public InputPosition(final EventNode source) {
        this.source = source;
    }
    
    @Override
    public int getLine() {
        return this.source.getLine();
    }
    
    @Override
    public String toString() {
        return String.format("line %s", this.getLine());
    }
}
