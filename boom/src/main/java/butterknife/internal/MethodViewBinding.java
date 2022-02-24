// 
// Decompiled by Procyon v0.5.36
// 

package butterknife.internal;

import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

final class MethodViewBinding implements ViewBinding
{
    private final String name;
    private final List<Parameter> parameters;
    private final boolean required;
    
    MethodViewBinding(final String name, final List<Parameter> c, final boolean required) {
        this.name = name;
        this.parameters = Collections.unmodifiableList((List<? extends Parameter>)new ArrayList<Parameter>(c));
        this.required = required;
    }
    
    @Override
    public String getDescription() {
        return "method '" + this.name + "'";
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<Parameter> getParameters() {
        return this.parameters;
    }
    
    public boolean isRequired() {
        return this.required;
    }
}
