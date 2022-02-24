// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import java.util.LinkedHashMap;

class LabelMap extends LinkedHashMap<String, Label> implements Iterable<Label>
{
    private final Policy policy;
    
    public LabelMap() {
        this((Policy)null);
    }
    
    public LabelMap(final Policy policy) {
        this.policy = policy;
    }
    
    private String[] getArray(final Set<String> set) {
        return set.toArray(new String[0]);
    }
    
    public String[] getKeys() throws Exception {
        final HashSet<String> set = new HashSet<String>();
        for (final Label label : this) {
            if (label != null) {
                final String path = label.getPath();
                final String name = label.getName();
                set.add(path);
                set.add(name);
            }
        }
        return this.getArray(set);
    }
    
    public Label getLabel(final String key) {
        return ((HashMap<K, Label>)this).remove(key);
    }
    
    public LabelMap getLabels() throws Exception {
        final LabelMap labelMap = new LabelMap(this.policy);
        for (final Label value : this) {
            if (value != null) {
                labelMap.put(value.getPath(), value);
            }
        }
        return labelMap;
    }
    
    public String[] getPaths() throws Exception {
        final HashSet<String> set = new HashSet<String>();
        for (final Label label : this) {
            if (label != null) {
                set.add(label.getPath());
            }
        }
        return this.getArray(set);
    }
    
    public boolean isStrict(final Context context) {
        boolean strict;
        if (this.policy == null) {
            strict = context.isStrict();
        }
        else {
            strict = (context.isStrict() && this.policy.isStrict());
        }
        return strict;
    }
    
    @Override
    public Iterator<Label> iterator() {
        return ((LinkedHashMap<K, Label>)this).values().iterator();
    }
}
