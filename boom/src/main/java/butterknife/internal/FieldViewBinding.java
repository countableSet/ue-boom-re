// 
// Decompiled by Procyon v0.5.36
// 

package butterknife.internal;

final class FieldViewBinding implements ViewBinding
{
    private final String name;
    private final boolean required;
    private final String type;
    
    FieldViewBinding(final String name, final String type, final boolean required) {
        this.name = name;
        this.type = type;
        this.required = required;
    }
    
    @Override
    public String getDescription() {
        return "field '" + this.name + "'";
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
    
    public boolean requiresCast() {
        return !"android.view.View".equals(this.type);
    }
}
