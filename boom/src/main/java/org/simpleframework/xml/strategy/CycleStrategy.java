// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.strategy;

import java.util.Map;
import org.simpleframework.xml.stream.NodeMap;

public class CycleStrategy implements Strategy
{
    private final Contract contract;
    private final ReadState read;
    private final WriteState write;
    
    public CycleStrategy() {
        this("id", "reference");
    }
    
    public CycleStrategy(final String s, final String s2) {
        this(s, s2, "class");
    }
    
    public CycleStrategy(final String s, final String s2, final String s3) {
        this(s, s2, s3, "length");
    }
    
    public CycleStrategy(final String s, final String s2, final String s3, final String s4) {
        this.contract = new Contract(s, s2, s3, s4);
        this.write = new WriteState(this.contract);
        this.read = new ReadState(this.contract);
    }
    
    @Override
    public Value read(final Type type, final NodeMap nodeMap, final Map map) throws Exception {
        final ReadGraph find = this.read.find(map);
        Value read;
        if (find != null) {
            read = find.read(type, nodeMap);
        }
        else {
            read = null;
        }
        return read;
    }
    
    @Override
    public boolean write(final Type type, final Object o, final NodeMap nodeMap, final Map map) {
        final WriteGraph find = this.write.find(map);
        return find != null && find.write(type, o, nodeMap);
    }
}
