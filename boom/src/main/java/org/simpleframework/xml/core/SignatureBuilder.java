// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Constructor;

class SignatureBuilder
{
    private final Constructor factory;
    private final ParameterTable table;
    
    public SignatureBuilder(final Constructor factory) {
        this.table = new ParameterTable();
        this.factory = factory;
    }
    
    private List<Signature> build(final ParameterTable parameterTable) throws Exception {
        List<Signature> list;
        if (this.table.isEmpty()) {
            list = this.create();
        }
        else {
            this.build(parameterTable, 0);
            list = this.create(parameterTable);
        }
        return list;
    }
    
    private void build(final ParameterTable parameterTable, final int n) {
        this.build(parameterTable, new ParameterList(), n);
    }
    
    private void build(final ParameterTable parameterTable, final ParameterList list, final int n) {
        final ParameterList value = this.table.get(n);
        final int size = value.size();
        if (this.table.width() - 1 > n) {
            for (int i = 0; i < size; ++i) {
                final ParameterList list2 = new ParameterList(list);
                if (list != null) {
                    list2.add(value.get(i));
                    this.build(parameterTable, list2, n + 1);
                }
            }
        }
        else {
            this.populate(parameterTable, list, n);
        }
    }
    
    private List<Signature> create() throws Exception {
        final ArrayList<Signature> list = new ArrayList<Signature>();
        final Signature signature = new Signature(this.factory);
        if (this.isValid()) {
            list.add(signature);
        }
        return list;
    }
    
    private List<Signature> create(final ParameterTable parameterTable) throws Exception {
        final ArrayList<Signature> list = new ArrayList<Signature>();
        final int access$100 = parameterTable.height();
        final int access$101 = parameterTable.width();
        for (int i = 0; i < access$100; ++i) {
            final Signature signature = new Signature(this.factory);
            for (int j = 0; j < access$101; ++j) {
                final Parameter value = parameterTable.get(j, i);
                final String path = value.getPath();
                if (signature.contains(value.getKey())) {
                    throw new ConstructorException("Parameter '%s' is a duplicate in %s", new Object[] { path, this.factory });
                }
                signature.add(value);
            }
            list.add(signature);
        }
        return list;
    }
    
    private void populate(final ParameterTable parameterTable, final ParameterList list, final int n) {
        final ParameterList value = this.table.get(n);
        final int size = list.size();
        for (int size2 = value.size(), i = 0; i < size2; ++i) {
            for (int j = 0; j < size; ++j) {
                parameterTable.get(j).add(list.get(j));
            }
            parameterTable.get(n).add(value.get(i));
        }
    }
    
    public List<Signature> build() throws Exception {
        return this.build(new ParameterTable());
    }
    
    public void insert(final Parameter parameter, final int n) {
        this.table.insert(parameter, n);
    }
    
    public boolean isValid() {
        return this.factory.getParameterTypes().length == this.table.width();
    }
    
    private static class ParameterList extends ArrayList<Parameter>
    {
        public ParameterList() {
        }
        
        public ParameterList(final ParameterList c) {
            super(c);
        }
    }
    
    private static class ParameterTable extends ArrayList<ParameterList>
    {
        public ParameterTable() {
        }
        
        private int height() {
            int size = 0;
            if (this.width() > 0) {
                size = this.get(0).size();
            }
            return size;
        }
        
        private int width() {
            return this.size();
        }
        
        public Parameter get(final int n, final int index) {
            return this.get(n).get(index);
        }
        
        @Override
        public ParameterList get(final int index) {
            for (int i = this.size(); i <= index; ++i) {
                this.add(new ParameterList());
            }
            return super.get(index);
        }
        
        public void insert(final Parameter e, final int n) {
            final ParameterList value = this.get(n);
            if (value != null) {
                value.add(e);
            }
        }
    }
}
