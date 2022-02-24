// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Iterator;
import java.util.ArrayList;

class ModelList extends ArrayList<Model>
{
    public ModelList() {
    }
    
    public ModelList build() {
        final ModelList list = new ModelList();
        final Iterator<Model> iterator = this.iterator();
        while (iterator.hasNext()) {
            list.register(iterator.next());
        }
        return list;
    }
    
    @Override
    public boolean isEmpty() {
        for (final Model model : this) {
            if (model != null && !model.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public Model lookup(final int n) {
        Model model;
        if (n <= this.size()) {
            model = this.get(n - 1);
        }
        else {
            model = null;
        }
        return model;
    }
    
    public void register(final Model element) {
        final int index = element.getIndex();
        final int size = this.size();
        for (int i = 0; i < index; ++i) {
            if (i >= size) {
                this.add(null);
            }
            if (i == index - 1) {
                this.set(index - 1, element);
            }
        }
    }
    
    public Model take() {
        while (!this.isEmpty()) {
            final Model model = this.remove(0);
            if (!model.isEmpty()) {
                return model;
            }
        }
        return null;
    }
}
