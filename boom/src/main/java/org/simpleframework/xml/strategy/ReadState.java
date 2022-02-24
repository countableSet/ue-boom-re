// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.strategy;

import org.simpleframework.xml.util.WeakCache;

class ReadState extends WeakCache<ReadGraph>
{
    private final Contract contract;
    private final Loader loader;
    
    public ReadState(final Contract contract) {
        this.loader = new Loader();
        this.contract = contract;
    }
    
    private ReadGraph create(final Object o) throws Exception {
        ReadGraph readGraph;
        if ((readGraph = this.fetch(o)) == null) {
            readGraph = new ReadGraph(this.contract, this.loader);
            this.cache(o, readGraph);
        }
        return readGraph;
    }
    
    public ReadGraph find(final Object o) throws Exception {
        final ReadGraph readGraph = this.fetch(o);
        ReadGraph create;
        if (readGraph != null) {
            create = readGraph;
        }
        else {
            create = this.create(o);
        }
        return create;
    }
}
