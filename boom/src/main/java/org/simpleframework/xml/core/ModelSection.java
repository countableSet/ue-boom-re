// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Iterator;

class ModelSection implements Section
{
    private LabelMap attributes;
    private LabelMap elements;
    private Model model;
    private ModelMap models;
    
    public ModelSection(final Model model) {
        this.model = model;
    }
    
    @Override
    public String getAttribute(String attribute) throws Exception {
        final Expression expression = this.model.getExpression();
        if (expression != null) {
            attribute = expression.getAttribute(attribute);
        }
        return attribute;
    }
    
    @Override
    public LabelMap getAttributes() throws Exception {
        if (this.attributes == null) {
            this.attributes = this.model.getAttributes();
        }
        return this.attributes;
    }
    
    @Override
    public Label getElement(final String s) throws Exception {
        return this.getElements().getLabel(s);
    }
    
    @Override
    public LabelMap getElements() throws Exception {
        if (this.elements == null) {
            this.elements = this.model.getElements();
        }
        return this.elements;
    }
    
    public ModelMap getModels() throws Exception {
        if (this.models == null) {
            this.models = this.model.getModels();
        }
        return this.models;
    }
    
    @Override
    public String getName() {
        return this.model.getName();
    }
    
    @Override
    public String getPath(String element) throws Exception {
        final Expression expression = this.model.getExpression();
        if (expression != null) {
            element = expression.getElement(element);
        }
        return element;
    }
    
    @Override
    public String getPrefix() {
        return this.model.getPrefix();
    }
    
    @Override
    public Section getSection(final String key) throws Exception {
        final ModelList list = ((LinkedHashMap<K, ModelList>)this.getModels()).get(key);
        if (list == null) {
            return null;
        }
        final Model take = list.take();
        if (take == null) {
            return null;
        }
        return new ModelSection(take);
        modelSection = null;
        return modelSection;
    }
    
    @Override
    public Label getText() throws Exception {
        return this.model.getText();
    }
    
    @Override
    public boolean isSection(final String key) throws Exception {
        return this.getModels().get(key) != null;
    }
    
    @Override
    public Iterator<String> iterator() {
        final ArrayList<String> list = new ArrayList<String>();
        final Iterator<String> iterator = this.model.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return (Iterator<String>)list.iterator();
    }
}
