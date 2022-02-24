// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.simpleframework.xml.Version;
import org.simpleframework.xml.Order;

class PrimitiveScanner implements Scanner
{
    private final Detail detail;
    private final Section section;
    
    public PrimitiveScanner(final Detail detail) {
        this.section = new EmptySection(this);
        this.detail = detail;
    }
    
    @Override
    public Caller getCaller(final Context context) {
        return new Caller(this, context);
    }
    
    @Override
    public Function getCommit() {
        return null;
    }
    
    @Override
    public Function getComplete() {
        return null;
    }
    
    @Override
    public Decorator getDecorator() {
        return null;
    }
    
    @Override
    public Instantiator getInstantiator() {
        return null;
    }
    
    @Override
    public String getName() {
        return null;
    }
    
    @Override
    public Order getOrder() {
        return null;
    }
    
    @Override
    public ParameterMap getParameters() {
        return new ParameterMap();
    }
    
    @Override
    public Function getPersist() {
        return null;
    }
    
    @Override
    public Function getReplace() {
        return null;
    }
    
    @Override
    public Function getResolve() {
        return null;
    }
    
    @Override
    public Version getRevision() {
        return null;
    }
    
    @Override
    public Section getSection() {
        return this.section;
    }
    
    @Override
    public Signature getSignature() {
        return null;
    }
    
    @Override
    public List<Signature> getSignatures() {
        return new LinkedList<Signature>();
    }
    
    @Override
    public Label getText() {
        return null;
    }
    
    @Override
    public Class getType() {
        return this.detail.getType();
    }
    
    @Override
    public Function getValidate() {
        return null;
    }
    
    @Override
    public Label getVersion() {
        return null;
    }
    
    @Override
    public boolean isEmpty() {
        return true;
    }
    
    @Override
    public boolean isPrimitive() {
        return true;
    }
    
    @Override
    public boolean isStrict() {
        return true;
    }
    
    private static class EmptySection implements Section
    {
        private final List<String> list;
        private final Scanner scanner;
        
        public EmptySection(final Scanner scanner) {
            this.list = new LinkedList<String>();
            this.scanner = scanner;
        }
        
        @Override
        public String getAttribute(final String s) {
            return null;
        }
        
        @Override
        public LabelMap getAttributes() {
            return new LabelMap(this.scanner);
        }
        
        @Override
        public Label getElement(final String s) {
            return null;
        }
        
        @Override
        public LabelMap getElements() {
            return new LabelMap(this.scanner);
        }
        
        @Override
        public String getName() {
            return null;
        }
        
        @Override
        public String getPath(final String s) {
            return null;
        }
        
        @Override
        public String getPrefix() {
            return null;
        }
        
        @Override
        public Section getSection(final String s) {
            return null;
        }
        
        @Override
        public Label getText() {
            return null;
        }
        
        @Override
        public boolean isSection(final String s) {
            return false;
        }
        
        @Override
        public Iterator<String> iterator() {
            return this.list.iterator();
        }
    }
}
