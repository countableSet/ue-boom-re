// 
// Decompiled by Procyon v0.5.36
// 

package butterknife.internal;

final class FieldCollectionViewBinding implements ViewBinding
{
    private final Kind kind;
    private final String name;
    private final boolean required;
    private final String type;
    
    FieldCollectionViewBinding(final String name, final String type, final Kind kind, final boolean required) {
        this.name = name;
        this.type = type;
        this.kind = kind;
        this.required = required;
    }
    
    @Override
    public String getDescription() {
        return "field '" + this.name + "'";
    }
    
    public Kind getKind() {
        return this.kind;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getType() {
        return this.type;
    }
    
    public boolean isRequired() {
        return this.required;
    }
    
    enum Kind
    {
        ARRAY, 
        LIST;
    }
}
