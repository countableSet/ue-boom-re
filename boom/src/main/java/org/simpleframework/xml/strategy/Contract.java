// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.strategy;

class Contract
{
    private String label;
    private String length;
    private String mark;
    private String refer;
    
    public Contract(final String mark, final String refer, final String label, final String length) {
        this.length = length;
        this.label = label;
        this.refer = refer;
        this.mark = mark;
    }
    
    public String getIdentity() {
        return this.mark;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public String getLength() {
        return this.length;
    }
    
    public String getReference() {
        return this.refer;
    }
}
