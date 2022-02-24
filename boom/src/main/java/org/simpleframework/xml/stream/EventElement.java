// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import java.util.ArrayList;

abstract class EventElement extends ArrayList<Attribute> implements EventNode
{
    @Override
    public int getLine() {
        return -1;
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
        return true;
    }
    
    @Override
    public boolean isText() {
        return false;
    }
}
