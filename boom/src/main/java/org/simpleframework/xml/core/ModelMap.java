// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Iterator;
import java.util.LinkedHashMap;

class ModelMap extends LinkedHashMap<String, ModelList> implements Iterable<ModelList>
{
    private final Detail detail;
    
    public ModelMap(final Detail detail) {
        this.detail = detail;
    }
    
    public ModelMap getModels() throws Exception {
        final ModelMap modelMap = new ModelMap(this.detail);
        for (final String key : ((LinkedHashMap<String, V>)this).keySet()) {
            final ModelList list = ((LinkedHashMap<K, ModelList>)this).get(key);
            ModelList build;
            if ((build = list) != null) {
                build = list.build();
            }
            if (modelMap.containsKey(key)) {
                throw new PathException("Path with name '%s' is a duplicate in %s ", new Object[] { key, this.detail });
            }
            modelMap.put(key, build);
        }
        return modelMap;
    }
    
    @Override
    public Iterator<ModelList> iterator() {
        return ((LinkedHashMap<K, ModelList>)this).values().iterator();
    }
    
    public Model lookup(final String key, final int n) {
        final ModelList list = ((LinkedHashMap<K, ModelList>)this).get(key);
        Model lookup;
        if (list != null) {
            lookup = list.lookup(n);
        }
        else {
            lookup = null;
        }
        return lookup;
    }
    
    public void register(final String s, final Model model) {
        ModelList value;
        if ((value = ((LinkedHashMap<K, ModelList>)this).get(s)) == null) {
            value = new ModelList();
            this.put(s, value);
        }
        value.register(model);
    }
}
