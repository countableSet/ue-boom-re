// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Iterator;

class TreeModel implements Model
{
    private LabelMap attributes;
    private Detail detail;
    private LabelMap elements;
    private Expression expression;
    private int index;
    private Label list;
    private ModelMap models;
    private String name;
    private OrderList order;
    private Policy policy;
    private String prefix;
    private Label text;
    
    public TreeModel(final Policy policy, final Detail detail) {
        this(policy, detail, null, null, 1);
    }
    
    public TreeModel(final Policy policy, final Detail detail, final String name, final String prefix, final int index) {
        this.attributes = new LabelMap(policy);
        this.elements = new LabelMap(policy);
        this.models = new ModelMap(detail);
        this.order = new OrderList();
        this.detail = detail;
        this.policy = policy;
        this.prefix = prefix;
        this.index = index;
        this.name = name;
    }
    
    private Model create(final String e, final String s, final int n) throws Exception {
        final TreeModel treeModel = new TreeModel(this.policy, this.detail, e, s, n);
        if (e != null) {
            this.models.register(e, treeModel);
            this.order.add(e);
        }
        return treeModel;
    }
    
    private void validateAttributes(final Class clazz) throws Exception {
        for (final String key : ((LinkedHashMap<String, V>)this.attributes).keySet()) {
            if (this.attributes.get(key) == null) {
                throw new AttributeException("Ordered attribute '%s' does not exist in %s", new Object[] { key, clazz });
            }
            if (this.expression == null) {
                continue;
            }
            this.expression.getAttribute(key);
        }
    }
    
    private void validateElements(final Class clazz) throws Exception {
        for (final String s : ((LinkedHashMap<String, V>)this.elements).keySet()) {
            final ModelList list = ((LinkedHashMap<K, ModelList>)this.models).get(s);
            final Label label = ((LinkedHashMap<K, Label>)this.elements).get(s);
            if (list == null && label == null) {
                throw new ElementException("Ordered element '%s' does not exist in %s", new Object[] { s, clazz });
            }
            if (list != null && label != null && !list.isEmpty()) {
                throw new ElementException("Element '%s' is also a path name in %s", new Object[] { s, clazz });
            }
            if (this.expression == null) {
                continue;
            }
            this.expression.getElement(s);
        }
    }
    
    private void validateExpression(final Label label) throws Exception {
        final Expression expression = label.getExpression();
        if (this.expression != null) {
            final String path = this.expression.getPath();
            final String path2 = expression.getPath();
            if (!path.equals(path2)) {
                throw new PathException("Path '%s' does not match '%s' in %s", new Object[] { path, path2, this.detail });
            }
        }
        else {
            this.expression = expression;
        }
    }
    
    private void validateExpressions(final Class clazz) throws Exception {
        for (final Label label : this.elements) {
            if (label != null) {
                this.validateExpression(label);
            }
        }
        for (final Label label2 : this.attributes) {
            if (label2 != null) {
                this.validateExpression(label2);
            }
        }
        if (this.text != null) {
            this.validateExpression(this.text);
        }
    }
    
    private void validateModels(final Class clazz) throws Exception {
        for (final ModelList list : this.models) {
            int n = 1;
            for (final Model model : list) {
                if (model != null) {
                    final String name = model.getName();
                    final int index = model.getIndex();
                    if (index != n) {
                        throw new ElementException("Path section '%s[%s]' is out of sequence in %s", new Object[] { name, index, clazz });
                    }
                    model.validate(clazz);
                    ++n;
                }
            }
        }
    }
    
    private void validateText(final Class clazz) throws Exception {
        if (this.text != null) {
            if (!this.elements.isEmpty()) {
                throw new TextException("Text annotation %s used with elements in %s", new Object[] { this.text, clazz });
            }
            if (this.isComposite()) {
                throw new TextException("Text annotation %s can not be used with paths in %s", new Object[] { this.text, clazz });
            }
        }
    }
    
    @Override
    public LabelMap getAttributes() throws Exception {
        return this.attributes.getLabels();
    }
    
    @Override
    public LabelMap getElements() throws Exception {
        return this.elements.getLabels();
    }
    
    @Override
    public Expression getExpression() {
        return this.expression;
    }
    
    @Override
    public int getIndex() {
        return this.index;
    }
    
    @Override
    public ModelMap getModels() throws Exception {
        return this.models.getModels();
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getPrefix() {
        return this.prefix;
    }
    
    @Override
    public Label getText() {
        Label label;
        if (this.list != null) {
            label = this.list;
        }
        else {
            label = this.text;
        }
        return label;
    }
    
    @Override
    public boolean isAttribute(final String key) {
        return this.attributes.containsKey(key);
    }
    
    @Override
    public boolean isComposite() {
        boolean b = true;
        final Iterator<ModelList> iterator = this.models.iterator();
        while (iterator.hasNext()) {
            for (final Model model : iterator.next()) {
                if (model != null && !model.isEmpty()) {
                    return b;
                }
            }
        }
        if (this.models.isEmpty()) {
            b = false;
            return b;
        }
        return b;
    }
    
    @Override
    public boolean isElement(final String key) {
        return this.elements.containsKey(key);
    }
    
    @Override
    public boolean isEmpty() {
        final boolean b = false;
        boolean b2;
        if (this.text != null) {
            b2 = b;
        }
        else {
            b2 = b;
            if (this.elements.isEmpty()) {
                b2 = b;
                if (this.attributes.isEmpty()) {
                    b2 = b;
                    if (!this.isComposite()) {
                        b2 = true;
                    }
                }
            }
        }
        return b2;
    }
    
    @Override
    public boolean isModel(final String key) {
        return this.models.containsKey(key);
    }
    
    @Override
    public Iterator<String> iterator() {
        final ArrayList<String> list = new ArrayList<String>();
        final Iterator<String> iterator = this.order.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return (Iterator<String>)list.iterator();
    }
    
    @Override
    public Model lookup(final String s, final int n) {
        return this.models.lookup(s, n);
    }
    
    @Override
    public Model lookup(Expression path) {
        Model model2;
        final Model model = model2 = this.lookup(path.getFirst(), path.getIndex());
        if (path.isPath()) {
            path = path.getPath(1, 0);
            if ((model2 = model) != null) {
                model2 = model.lookup(path);
            }
        }
        return model2;
    }
    
    @Override
    public Model register(final String s, final String s2, final int n) throws Exception {
        Model model;
        if ((model = this.models.lookup(s, n)) == null) {
            model = this.create(s, s2, n);
        }
        return model;
    }
    
    @Override
    public void register(final Label label) throws Exception {
        if (label.isAttribute()) {
            this.registerAttribute(label);
        }
        else if (label.isText()) {
            this.registerText(label);
        }
        else {
            this.registerElement(label);
        }
    }
    
    @Override
    public void registerAttribute(final String key) throws Exception {
        this.attributes.put(key, null);
    }
    
    @Override
    public void registerAttribute(final Label value) throws Exception {
        final String name = value.getName();
        if (this.attributes.get(name) != null) {
            throw new AttributeException("Duplicate annotation of name '%s' on %s", new Object[] { name, value });
        }
        this.attributes.put(name, value);
    }
    
    @Override
    public void registerElement(final String key) throws Exception {
        if (!this.order.contains(key)) {
            this.order.add(key);
        }
        this.elements.put(key, null);
    }
    
    @Override
    public void registerElement(final Label label) throws Exception {
        final String name = label.getName();
        if (this.elements.get(name) != null) {
            throw new ElementException("Duplicate annotation of name '%s' on %s", new Object[] { name, label });
        }
        if (!this.order.contains(name)) {
            this.order.add(name);
        }
        if (label.isTextList()) {
            this.list = label;
        }
        this.elements.put(name, label);
    }
    
    @Override
    public void registerText(final Label text) throws Exception {
        if (this.text != null) {
            throw new TextException("Duplicate text annotation on %s", new Object[] { text });
        }
        this.text = text;
    }
    
    @Override
    public String toString() {
        return String.format("model '%s[%s]'", this.name, this.index);
    }
    
    @Override
    public void validate(final Class clazz) throws Exception {
        this.validateExpressions(clazz);
        this.validateAttributes(clazz);
        this.validateElements(clazz);
        this.validateModels(clazz);
        this.validateText(clazz);
    }
    
    private static class OrderList extends ArrayList<String>
    {
        public OrderList() {
        }
    }
}
