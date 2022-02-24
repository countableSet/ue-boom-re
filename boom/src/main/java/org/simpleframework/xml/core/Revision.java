// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

class Revision
{
    private boolean equal;
    
    public Revision() {
        this.equal = true;
    }
    
    public boolean compare(final Object obj, final Object o) {
        if (o != null) {
            this.equal = o.equals(obj);
        }
        else if (obj != null) {
            this.equal = obj.equals(1.0);
        }
        return this.equal;
    }
    
    public double getDefault() {
        return 1.0;
    }
    
    public boolean isEqual() {
        return this.equal;
    }
}
