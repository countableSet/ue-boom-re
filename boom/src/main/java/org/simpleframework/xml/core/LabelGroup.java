// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Arrays;
import java.util.List;

class LabelGroup
{
    private final List<Label> list;
    private final int size;
    
    public LabelGroup(final List<Label> list) {
        this.size = list.size();
        this.list = list;
    }
    
    public LabelGroup(final Label label) {
        this(Arrays.asList(label));
    }
    
    public List<Label> getList() {
        return this.list;
    }
    
    public Label getPrimary() {
        Label label;
        if (this.size > 0) {
            label = this.list.get(0);
        }
        else {
            label = null;
        }
        return label;
    }
}
