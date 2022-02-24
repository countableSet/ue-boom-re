// 
// Decompiled by Procyon v0.5.36
// 

package butterknife.internal;

final class FieldResourceBinding
{
    private final int id;
    private final String method;
    private final String name;
    
    FieldResourceBinding(final int id, final String name, final String method) {
        this.id = id;
        this.name = name;
        this.method = method;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getMethod() {
        return this.method;
    }
    
    public String getName() {
        return this.name;
    }
}
