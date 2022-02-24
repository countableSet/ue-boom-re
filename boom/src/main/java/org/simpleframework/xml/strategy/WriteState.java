// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.strategy;

import org.simpleframework.xml.util.WeakCache;

class WriteState extends WeakCache<WriteGraph>
{
    private Contract contract;
    
    public WriteState(final Contract contract) {
        this.contract = contract;
    }
    
    public WriteGraph find(final Object o) {
        WriteGraph writeGraph;
        if ((writeGraph = this.fetch(o)) == null) {
            writeGraph = new WriteGraph(this.contract);
            this.cache(o, writeGraph);
        }
        return writeGraph;
    }
}
