// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.Version;

class Structure
{
    private final Instantiator factory;
    private final Model model;
    private final boolean primitive;
    private final Label text;
    private final Label version;
    
    public Structure(final Instantiator factory, final Model model, final Label version, final Label text, final boolean primitive) {
        this.primitive = primitive;
        this.factory = factory;
        this.version = version;
        this.model = model;
        this.text = text;
    }
    
    public Instantiator getInstantiator() {
        return this.factory;
    }
    
    public Version getRevision() {
        Version version;
        if (this.version != null) {
            version = this.version.getContact().getAnnotation(Version.class);
        }
        else {
            version = null;
        }
        return version;
    }
    
    public Section getSection() {
        return new ModelSection(this.model);
    }
    
    public Label getText() {
        return this.text;
    }
    
    public Label getVersion() {
        return this.version;
    }
    
    public boolean isPrimitive() {
        return this.primitive;
    }
}
