// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

class InputStack extends Stack<InputNode>
{
    public InputStack() {
        super(6);
    }
    
    public boolean isRelevant(final InputNode o) {
        return this.contains(o) || this.isEmpty();
    }
}
