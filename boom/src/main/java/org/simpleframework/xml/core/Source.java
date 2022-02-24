// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.Version;
import org.simpleframework.xml.stream.Style;
import org.simpleframework.xml.stream.NodeMap;
import java.util.Map;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.filter.Filter;

class Source implements Context
{
    private TemplateEngine engine;
    private Filter filter;
    private Session session;
    private Strategy strategy;
    private Support support;
    
    public Source(final Strategy strategy, final Support support, final Session session) {
        this.filter = new TemplateFilter(this, support);
        this.engine = new TemplateEngine(this.filter);
        this.strategy = strategy;
        this.support = support;
        this.session = session;
    }
    
    private Scanner getScanner(final Class clazz) throws Exception {
        return this.support.getScanner(clazz);
    }
    
    @Override
    public Object getAttribute(final Object o) {
        return this.session.get(o);
    }
    
    @Override
    public Caller getCaller(final Class clazz) throws Exception {
        return this.getScanner(clazz).getCaller(this);
    }
    
    @Override
    public Decorator getDecorator(final Class clazz) throws Exception {
        return this.getScanner(clazz).getDecorator();
    }
    
    @Override
    public Instance getInstance(final Class clazz) {
        return this.support.getInstance(clazz);
    }
    
    @Override
    public Instance getInstance(final Value value) {
        return this.support.getInstance(value);
    }
    
    @Override
    public String getName(final Class clazz) throws Exception {
        return this.support.getName(clazz);
    }
    
    @Override
    public Value getOverride(final Type type, final InputNode inputNode) throws Exception {
        final NodeMap<InputNode> attributes = inputNode.getAttributes();
        if (attributes == null) {
            throw new PersistenceException("No attributes for %s", new Object[] { inputNode });
        }
        return this.strategy.read(type, attributes, this.session);
    }
    
    @Override
    public String getProperty(final String s) {
        return this.engine.process(s);
    }
    
    @Override
    public Schema getSchema(final Class clazz) throws Exception {
        final Scanner scanner = this.getScanner(clazz);
        if (scanner == null) {
            throw new PersistenceException("Invalid schema class %s", new Object[] { clazz });
        }
        return new ClassSchema(scanner, this);
    }
    
    @Override
    public Session getSession() {
        return this.session;
    }
    
    @Override
    public Style getStyle() {
        return this.support.getStyle();
    }
    
    @Override
    public Support getSupport() {
        return this.support;
    }
    
    @Override
    public Class getType(final Type type, final Object o) {
        Class<?> clazz;
        if (o != null) {
            clazz = o.getClass();
        }
        else {
            clazz = (Class<?>)type.getType();
        }
        return clazz;
    }
    
    @Override
    public Version getVersion(final Class clazz) throws Exception {
        return this.getScanner(clazz).getRevision();
    }
    
    @Override
    public boolean isFloat(final Class clazz) throws Exception {
        final Support support = this.support;
        return Support.isFloat(clazz);
    }
    
    @Override
    public boolean isFloat(final Type type) throws Exception {
        return this.isFloat(type.getType());
    }
    
    @Override
    public boolean isPrimitive(final Class clazz) throws Exception {
        return this.support.isPrimitive(clazz);
    }
    
    @Override
    public boolean isPrimitive(final Type type) throws Exception {
        return this.isPrimitive(type.getType());
    }
    
    @Override
    public boolean isStrict() {
        return this.session.isStrict();
    }
    
    @Override
    public boolean setOverride(final Type type, final Object o, final OutputNode outputNode) throws Exception {
        final NodeMap<OutputNode> attributes = outputNode.getAttributes();
        if (attributes == null) {
            throw new PersistenceException("No attributes for %s", new Object[] { outputNode });
        }
        return this.strategy.write(type, o, attributes, this.session);
    }
}
