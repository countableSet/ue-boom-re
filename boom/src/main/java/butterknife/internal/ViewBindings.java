// 
// Decompiled by Procyon v0.5.36
// 

package butterknife.internal;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;

final class ViewBindings
{
    private final Set<FieldViewBinding> fieldBindings;
    private final int id;
    private final LinkedHashMap<ListenerClass, Map<ListenerMethod, Set<MethodViewBinding>>> methodBindings;
    
    ViewBindings(final int id) {
        this.fieldBindings = new LinkedHashSet<FieldViewBinding>();
        this.methodBindings = new LinkedHashMap<ListenerClass, Map<ListenerMethod, Set<MethodViewBinding>>>();
        this.id = id;
    }
    
    public void addFieldBinding(final FieldViewBinding fieldViewBinding) {
        this.fieldBindings.add(fieldViewBinding);
    }
    
    public void addMethodBinding(final ListenerClass listenerClass, final ListenerMethod listenerMethod, final MethodViewBinding methodViewBinding) {
        final Map<ListenerMethod, Set<MethodViewBinding>> map = this.methodBindings.get(listenerClass);
        final Set<MethodViewBinding> set = null;
        Set<MethodViewBinding> set2;
        LinkedHashMap<ListenerMethod, Set<MethodViewBinding>> linkedHashMap;
        if (map == null) {
            final LinkedHashMap<ListenerMethod, Set<MethodViewBinding>> value = new LinkedHashMap<ListenerMethod, Set<MethodViewBinding>>();
            this.methodBindings.put(listenerClass, value);
            set2 = set;
            linkedHashMap = value;
        }
        else {
            set2 = map.get(listenerMethod);
            linkedHashMap = (LinkedHashMap<ListenerMethod, Set<MethodViewBinding>>)map;
        }
        Set<MethodViewBinding> set3 = set2;
        if (set2 == null) {
            set3 = new LinkedHashSet<MethodViewBinding>();
            linkedHashMap.put(listenerMethod, set3);
        }
        set3.add(methodViewBinding);
    }
    
    public Collection<FieldViewBinding> getFieldBindings() {
        return this.fieldBindings;
    }
    
    public int getId() {
        return this.id;
    }
    
    public Map<ListenerClass, Map<ListenerMethod, Set<MethodViewBinding>>> getMethodBindings() {
        return this.methodBindings;
    }
    
    public List<ViewBinding> getRequiredBindings() {
        final ArrayList<FieldViewBinding> list = (ArrayList<FieldViewBinding>)new ArrayList<ViewBinding>();
        for (final FieldViewBinding fieldViewBinding : this.fieldBindings) {
            if (fieldViewBinding.isRequired()) {
                list.add(fieldViewBinding);
            }
        }
        final Iterator<Map<ListenerMethod, Set<MethodViewBinding>>> iterator2 = this.methodBindings.values().iterator();
        while (iterator2.hasNext()) {
            final Iterator<Set<MethodViewBinding>> iterator3 = iterator2.next().values().iterator();
            while (iterator3.hasNext()) {
                for (final MethodViewBinding methodViewBinding : iterator3.next()) {
                    if (methodViewBinding.isRequired()) {
                        list.add((FieldViewBinding)methodViewBinding);
                    }
                }
            }
        }
        return (List<ViewBinding>)list;
    }
    
    public boolean hasMethodBinding(final ListenerClass key, final ListenerMethod listenerMethod) {
        final Map<ListenerMethod, Set<MethodViewBinding>> map = this.methodBindings.get(key);
        return map != null && map.containsKey(listenerMethod);
    }
}
