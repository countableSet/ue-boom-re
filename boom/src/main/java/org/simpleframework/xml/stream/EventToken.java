// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import java.util.Iterator;

abstract class EventToken implements EventNode
{
    @Override
    public int getLine() {
        return -1;
    }
    
    @Override
    public String getName() {
        return null;
    }
    
    @Override
    public String getPrefix() {
        return null;
    }
    
    @Override
    public String getReference() {
        return null;
    }
    
    @Override
    public Object getSource() {
        return null;
    }
    
    @Override
    public String getValue() {
        return null;
    }
    
    @Override
    public boolean isEnd() {
        return false;
    }
    
    @Override
    public boolean isStart() {
        return false;
    }
    
    @Override
    public boolean isText() {
        return false;
    }
    
    @Override
    public Iterator<Attribute> iterator() {
        return null;
    }
}
