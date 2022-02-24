// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.NamespaceMap;
import org.simpleframework.xml.Version;
import java.util.Iterator;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.Position;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.strategy.Type;

class Composite implements Converter
{
    private final Context context;
    private final Criteria criteria;
    private final ObjectFactory factory;
    private final Primitive primitive;
    private final Revision revision;
    private final Type type;
    
    public Composite(final Context context, final Type type) {
        this(context, type, null);
    }
    
    public Composite(final Context context, final Type type, final Class clazz) {
        this.factory = new ObjectFactory(context, type, clazz);
        this.primitive = new Primitive(context, type);
        this.criteria = new Collector();
        this.revision = new Revision();
        this.context = context;
        this.type = type;
    }
    
    private boolean isOverridden(final OutputNode outputNode, final Object o, final Type type) throws Exception {
        return this.factory.setOverride(type, o, outputNode);
    }
    
    private Object read(final InputNode inputNode, final Instance instance, final Class clazz) throws Exception {
        final Schema schema = this.context.getSchema(clazz);
        final Caller caller = schema.getCaller();
        final Object read = this.read(schema, instance).read(inputNode);
        caller.validate(read);
        caller.commit(read);
        instance.setInstance(read);
        return this.readResolve(inputNode, read, caller);
    }
    
    private Builder read(final Schema schema, final Instance instance) throws Exception {
        Object o;
        if (schema.getInstantiator().isDefault()) {
            o = new Builder(this, this.criteria, schema, instance);
        }
        else {
            o = new Injector(this, this.criteria, schema, instance);
        }
        return (Builder)o;
    }
    
    private void read(final InputNode inputNode, final Object o, final Schema schema) throws Exception {
        final Section section = schema.getSection();
        this.readVersion(inputNode, o, schema);
        this.readSection(inputNode, o, section);
    }
    
    private void readAttribute(final InputNode inputNode, final Object o, final Section section, final LabelMap labelMap) throws Exception {
        final String attribute = section.getAttribute(inputNode.getName());
        final Label label = labelMap.getLabel(attribute);
        if (label == null) {
            final Position position = inputNode.getPosition();
            final Class type = this.context.getType(this.type, o);
            if (labelMap.isStrict(this.context) && this.revision.isEqual()) {
                throw new AttributeException("Attribute '%s' does not have a match in %s at %s", new Object[] { attribute, type, position });
            }
        }
        else {
            this.readInstance(inputNode, o, label);
        }
    }
    
    private void readAttributes(final InputNode inputNode, final Object o, final Section section) throws Exception {
        final NodeMap<InputNode> attributes = inputNode.getAttributes();
        final LabelMap attributes2 = section.getAttributes();
        final Iterator iterator = attributes.iterator();
        while (iterator.hasNext()) {
            final InputNode attribute = inputNode.getAttribute(iterator.next());
            if (attribute != null) {
                this.readAttribute(attribute, o, section, attributes2);
            }
        }
        this.validate(inputNode, attributes2, o);
    }
    
    private void readElement(final InputNode inputNode, final Object o, final Section section, final LabelMap labelMap) throws Exception {
        final String path = section.getPath(inputNode.getName());
        Label label;
        if ((label = labelMap.getLabel(path)) == null) {
            label = this.criteria.resolve(path);
        }
        if (label == null) {
            final Position position = inputNode.getPosition();
            final Class type = this.context.getType(this.type, o);
            if (labelMap.isStrict(this.context) && this.revision.isEqual()) {
                throw new ElementException("Element '%s' does not have a match in %s at %s", new Object[] { path, type, position });
            }
            inputNode.skip();
        }
        else {
            this.readUnion(inputNode, o, labelMap, label);
        }
    }
    
    private void readElements(final InputNode inputNode, final Object o, final Section section) throws Exception {
        final LabelMap elements = section.getElements();
        for (InputNode inputNode2 = inputNode.getNext(); inputNode2 != null; inputNode2 = inputNode.getNext()) {
            final Section section2 = section.getSection(inputNode2.getName());
            if (section2 != null) {
                this.readSection(inputNode2, o, section2);
            }
            else {
                this.readElement(inputNode2, o, section, elements);
            }
        }
        this.validate(inputNode, elements, o);
    }
    
    private Object readInstance(final InputNode inputNode, final Object o, final Label label) throws Exception {
        final Object variable = this.readVariable(inputNode, o, label);
        if (variable == null) {
            final Position position = inputNode.getPosition();
            final Class type = this.context.getType(this.type, o);
            if (label.isRequired() && this.revision.isEqual()) {
                throw new ValueRequiredException("Empty value for %s in %s at %s", new Object[] { label, type, position });
            }
        }
        else if (variable != label.getEmpty(this.context)) {
            this.criteria.set(label, variable);
        }
        return variable;
    }
    
    private Object readPrimitive(final InputNode inputNode, final Instance instance) throws Exception {
        final Class type = instance.getType();
        final Object read = this.primitive.read(inputNode, type);
        if (type != null) {
            instance.setInstance(read);
        }
        return read;
    }
    
    private Object readResolve(final InputNode inputNode, final Object o, final Caller caller) throws Exception {
        Object resolve;
        if (o != null) {
            final Position position = inputNode.getPosition();
            resolve = caller.resolve(o);
            final Class type = this.type.getType();
            final Class<?> class1 = resolve.getClass();
            if (!type.isAssignableFrom(class1)) {
                throw new ElementException("Type %s does not match %s at %s", new Object[] { class1, type, position });
            }
        }
        else {
            resolve = o;
        }
        return resolve;
    }
    
    private void readSection(final InputNode inputNode, final Object o, final Section section) throws Exception {
        this.readText(inputNode, o, section);
        this.readAttributes(inputNode, o, section);
        this.readElements(inputNode, o, section);
    }
    
    private void readText(final InputNode inputNode, final Object o, final Section section) throws Exception {
        final Label text = section.getText();
        if (text != null) {
            this.readInstance(inputNode, o, text);
        }
    }
    
    private void readUnion(final InputNode inputNode, Object instance, final LabelMap labelMap, final Label label) throws Exception {
        instance = this.readInstance(inputNode, instance, label);
        final String[] paths = label.getPaths();
        for (int length = paths.length, i = 0; i < length; ++i) {
            labelMap.getLabel(paths[i]);
        }
        if (label.isInline()) {
            this.criteria.set(label, instance);
        }
    }
    
    private Object readVariable(final InputNode inputNode, Object value, final Label label) throws Exception {
        final Converter converter = label.getConverter(this.context);
        if (!label.isCollection()) {
            return converter.read(inputNode);
        }
        final Variable value2 = this.criteria.get(label);
        final Contact contact = label.getContact();
        Object o;
        if (value2 != null) {
            o = converter.read(inputNode, value2.getValue());
        }
        else {
            if (value == null) {
                return converter.read(inputNode);
            }
            value = contact.get(value);
            if (value == null) {
                return converter.read(inputNode);
            }
            o = converter.read(inputNode, value);
        }
        return o;
        o = converter.read(inputNode);
        return o;
    }
    
    private void readVersion(final InputNode inputNode, final Object o, final Label label) throws Exception {
        final Object instance = this.readInstance(inputNode, o, label);
        final Class type = this.type.getType();
        if (instance != null) {
            final double revision = this.context.getVersion(type).revision();
            if (!instance.equals(this.revision)) {
                this.revision.compare(revision, instance);
            }
        }
    }
    
    private void readVersion(InputNode inputNode, final Object o, final Schema schema) throws Exception {
        final Label version = schema.getVersion();
        final Class type = this.type.getType();
        if (version != null) {
            inputNode = inputNode.getAttributes().remove(version.getName());
            if (inputNode != null) {
                this.readVersion(inputNode, o, version);
            }
            else {
                final Version version2 = this.context.getVersion(type);
                final Double value = this.revision.getDefault();
                final double revision = version2.revision();
                this.criteria.set(version, value);
                this.revision.compare(revision, value);
            }
        }
    }
    
    private void validate(final InputNode inputNode, final Label label) throws Exception {
        final Converter converter = label.getConverter(this.context);
        final Position position = inputNode.getPosition();
        final Class type = this.type.getType();
        if (!converter.validate(inputNode)) {
            throw new PersistenceException("Invalid value for %s in %s at %s", new Object[] { label, type, position });
        }
        this.criteria.set(label, null);
    }
    
    private void validate(final InputNode inputNode, final LabelMap labelMap) throws Exception {
        final Position position = inputNode.getPosition();
        for (final Label label : labelMap) {
            final Class type = this.type.getType();
            if (label.isRequired() && this.revision.isEqual()) {
                throw new ValueRequiredException("Unable to satisfy %s for %s at %s", new Object[] { label, type, position });
            }
        }
    }
    
    private void validate(final InputNode inputNode, final LabelMap labelMap, final Object o) throws Exception {
        final Class type = this.context.getType(this.type, o);
        final Position position = inputNode.getPosition();
        for (final Label label : labelMap) {
            if (label.isRequired() && this.revision.isEqual()) {
                throw new ValueRequiredException("Unable to satisfy %s for %s at %s", new Object[] { label, type, position });
            }
            final Object empty = label.getEmpty(this.context);
            if (empty == null) {
                continue;
            }
            this.criteria.set(label, empty);
        }
    }
    
    private boolean validate(final InputNode inputNode, final Class clazz) throws Exception {
        final Schema schema = this.context.getSchema(clazz);
        final Section section = schema.getSection();
        this.validateText(inputNode, schema);
        this.validateSection(inputNode, section);
        return inputNode.isElement();
    }
    
    private void validateAttribute(final InputNode inputNode, final Section section, final LabelMap labelMap) throws Exception {
        final Position position = inputNode.getPosition();
        final String attribute = section.getAttribute(inputNode.getName());
        final Label label = labelMap.getLabel(attribute);
        if (label == null) {
            final Class type = this.type.getType();
            if (labelMap.isStrict(this.context) && this.revision.isEqual()) {
                throw new AttributeException("Attribute '%s' does not exist for %s at %s", new Object[] { attribute, type, position });
            }
        }
        else {
            this.validate(inputNode, label);
        }
    }
    
    private void validateAttributes(final InputNode inputNode, final Section section) throws Exception {
        final NodeMap<InputNode> attributes = inputNode.getAttributes();
        final LabelMap attributes2 = section.getAttributes();
        final Iterator iterator = attributes.iterator();
        while (iterator.hasNext()) {
            final InputNode attribute = inputNode.getAttribute(iterator.next());
            if (attribute != null) {
                this.validateAttribute(attribute, section, attributes2);
            }
        }
        this.validate(inputNode, attributes2);
    }
    
    private void validateElement(final InputNode inputNode, final Section section, final LabelMap labelMap) throws Exception {
        final String path = section.getPath(inputNode.getName());
        Label label;
        if ((label = labelMap.getLabel(path)) == null) {
            label = this.criteria.resolve(path);
        }
        if (label == null) {
            final Position position = inputNode.getPosition();
            final Class type = this.type.getType();
            if (labelMap.isStrict(this.context) && this.revision.isEqual()) {
                throw new ElementException("Element '%s' does not exist for %s at %s", new Object[] { path, type, position });
            }
            inputNode.skip();
        }
        else {
            this.validateUnion(inputNode, labelMap, label);
        }
    }
    
    private void validateElements(final InputNode inputNode, final Section section) throws Exception {
        final LabelMap elements = section.getElements();
        for (InputNode inputNode2 = inputNode.getNext(); inputNode2 != null; inputNode2 = inputNode.getNext()) {
            final Section section2 = section.getSection(inputNode2.getName());
            if (section2 != null) {
                this.validateSection(inputNode2, section2);
            }
            else {
                this.validateElement(inputNode2, section, elements);
            }
        }
        this.validate(inputNode, elements);
    }
    
    private void validateSection(final InputNode inputNode, final Section section) throws Exception {
        this.validateAttributes(inputNode, section);
        this.validateElements(inputNode, section);
    }
    
    private void validateText(final InputNode inputNode, final Schema schema) throws Exception {
        final Label text = schema.getText();
        if (text != null) {
            this.validate(inputNode, text);
        }
    }
    
    private void validateUnion(final InputNode inputNode, final LabelMap labelMap, final Label label) throws Exception {
        final String[] paths = label.getPaths();
        for (int length = paths.length, i = 0; i < length; ++i) {
            labelMap.getLabel(paths[i]);
        }
        if (label.isInline()) {
            this.criteria.set(label, null);
        }
        this.validate(inputNode, label);
    }
    
    private void write(final OutputNode outputNode, final Object o, final Schema schema) throws Exception {
        final Section section = schema.getSection();
        this.writeVersion(outputNode, o, schema);
        this.writeSection(outputNode, o, section);
    }
    
    private void writeAttribute(final OutputNode outputNode, final Object o, final Label label) throws Exception {
        if (o != null) {
            label.getDecorator().decorate(outputNode.setAttribute(label.getName(), this.factory.getText(o)));
        }
    }
    
    private void writeAttributes(final OutputNode outputNode, final Object o, final Section section) throws Exception {
        for (final Label label : section.getAttributes()) {
            final Object value = label.getContact().get(o);
            final Class type = this.context.getType(this.type, o);
            Object empty;
            if ((empty = value) == null) {
                empty = label.getEmpty(this.context);
            }
            if (empty == null && label.isRequired()) {
                throw new AttributeException("Value for %s is null in %s", new Object[] { label, type });
            }
            this.writeAttribute(outputNode, empty, label);
        }
    }
    
    private void writeElement(final OutputNode outputNode, final Object o, final Converter converter) throws Exception {
        converter.write(outputNode, o);
    }
    
    private void writeElement(OutputNode child, final Object o, final Label label) throws Exception {
        if (o != null) {
            final Class<?> class1 = o.getClass();
            final Label label2 = label.getLabel(class1);
            final String name = label2.getName();
            final Type type = label.getType(class1);
            child = child.getChild(name);
            if (!label2.isInline()) {
                this.writeNamespaces(child, type, label2);
            }
            if (label2.isInline() || !this.isOverridden(child, o, type)) {
                final Converter converter = label2.getConverter(this.context);
                child.setData(label2.isData());
                this.writeElement(child, o, converter);
            }
        }
    }
    
    private void writeElements(final OutputNode outputNode, final Object o, final Section section) throws Exception {
        for (final String s : section) {
            final Section section2 = section.getSection(s);
            if (section2 != null) {
                this.writeSection(outputNode.getChild(s), o, section2);
            }
            else {
                final Label element = section.getElement(section.getPath(s));
                final Class type = this.context.getType(this.type, o);
                if (this.criteria.get(element) != null) {
                    continue;
                }
                if (element == null) {
                    throw new ElementException("Element '%s' not defined in %s", new Object[] { s, type });
                }
                this.writeUnion(outputNode, o, section, element);
            }
        }
    }
    
    private void writeNamespaces(final OutputNode outputNode, final Type type, final Label label) throws Exception {
        label.getDecorator().decorate(outputNode, this.context.getDecorator(type.getType()));
    }
    
    private Object writeReplace(final Object o) throws Exception {
        Object replace = o;
        if (o != null) {
            replace = this.context.getCaller(o.getClass()).replace(o);
        }
        return replace;
    }
    
    private void writeSection(final OutputNode outputNode, final Object o, final Section section) throws Exception {
        final NamespaceMap namespaces = outputNode.getNamespaces();
        final String prefix = section.getPrefix();
        if (prefix != null) {
            final String reference = namespaces.getReference(prefix);
            if (reference == null) {
                throw new ElementException("Namespace prefix '%s' in %s is not in scope", new Object[] { prefix, this.type });
            }
            outputNode.setReference(reference);
        }
        this.writeAttributes(outputNode, o, section);
        this.writeElements(outputNode, o, section);
        this.writeText(outputNode, o, section);
    }
    
    private void writeText(final OutputNode outputNode, final Object o, final Label label) throws Exception {
        if (o != null && !label.isTextList()) {
            final String text = this.factory.getText(o);
            outputNode.setData(label.isData());
            outputNode.setValue(text);
        }
    }
    
    private void writeText(final OutputNode outputNode, Object empty, final Section section) throws Exception {
        final Label text = section.getText();
        if (text != null) {
            final Object value = text.getContact().get(empty);
            final Class type = this.context.getType(this.type, empty);
            if ((empty = value) == null) {
                empty = text.getEmpty(this.context);
            }
            if (empty == null && text.isRequired()) {
                throw new TextException("Value for %s is null in %s", new Object[] { text, type });
            }
            this.writeText(outputNode, empty, text);
        }
    }
    
    private void writeUnion(final OutputNode outputNode, Object writeReplace, final Section section, final Label label) throws Exception {
        final Object value = label.getContact().get(writeReplace);
        final Class type = this.context.getType(this.type, writeReplace);
        if (value == null && label.isRequired()) {
            throw new ElementException("Value for %s is null in %s", new Object[] { label, type });
        }
        writeReplace = this.writeReplace(value);
        if (writeReplace != null) {
            this.writeElement(outputNode, writeReplace, label);
        }
        this.criteria.set(label, writeReplace);
    }
    
    private void writeVersion(final OutputNode outputNode, final Object o, final Schema schema) throws Exception {
        final Version revision = schema.getRevision();
        final Label version = schema.getVersion();
        if (revision != null) {
            final double default1 = this.revision.getDefault();
            final Double value = revision.revision();
            if (this.revision.compare(value, default1)) {
                if (version.isRequired()) {
                    this.writeAttribute(outputNode, value, version);
                }
            }
            else {
                this.writeAttribute(outputNode, value, version);
            }
        }
    }
    
    @Override
    public Object read(final InputNode inputNode) throws Exception {
        final Instance instance = this.factory.getInstance(inputNode);
        final Class type = instance.getType();
        Object o;
        if (instance.isReference()) {
            o = instance.getInstance();
        }
        else if (this.context.isPrimitive(type)) {
            o = this.readPrimitive(inputNode, instance);
        }
        else {
            o = this.read(inputNode, instance, type);
        }
        return o;
    }
    
    @Override
    public Object read(final InputNode inputNode, final Object o) throws Exception {
        final Schema schema = this.context.getSchema(o.getClass());
        final Caller caller = schema.getCaller();
        this.read(inputNode, o, schema);
        this.criteria.commit(o);
        caller.validate(o);
        caller.commit(o);
        return this.readResolve(inputNode, o, caller);
    }
    
    @Override
    public boolean validate(final InputNode inputNode) throws Exception {
        final Instance instance = this.factory.getInstance(inputNode);
        boolean validate;
        if (!instance.isReference()) {
            instance.setInstance(null);
            validate = this.validate(inputNode, instance.getType());
        }
        else {
            validate = true;
        }
        return validate;
    }
    
    @Override
    public void write(final OutputNode outputNode, final Object o) throws Exception {
        final Schema schema = this.context.getSchema(o.getClass());
        final Caller caller = schema.getCaller();
        try {
            if (schema.isPrimitive()) {
                this.primitive.write(outputNode, o);
            }
            else {
                caller.persist(o);
                this.write(outputNode, o, schema);
            }
        }
        finally {
            caller.complete(o);
        }
    }
    
    private static class Builder
    {
        protected final Composite composite;
        protected final Criteria criteria;
        protected final Schema schema;
        protected final Instance value;
        
        public Builder(final Composite composite, final Criteria criteria, final Schema schema, final Instance value) {
            this.composite = composite;
            this.criteria = criteria;
            this.schema = schema;
            this.value = value;
        }
        
        public Object read(final InputNode inputNode) throws Exception {
            final Object instance = this.value.getInstance();
            final Section section = this.schema.getSection();
            this.value.setInstance(instance);
            this.composite.readVersion(inputNode, instance, this.schema);
            this.composite.readText(inputNode, instance, section);
            this.composite.readAttributes(inputNode, instance, section);
            this.composite.readElements(inputNode, instance, section);
            this.criteria.commit(instance);
            return instance;
        }
    }
    
    private class Injector extends Builder
    {
        private Injector(final Composite composite, final Criteria criteria, final Schema schema, final Instance instance) {
            super(composite, criteria, schema, instance);
        }
        
        private Object readInject(final InputNode inputNode) throws Exception {
            final Object instance = this.schema.getInstantiator().getInstance(this.criteria);
            this.value.setInstance(instance);
            this.criteria.commit(instance);
            return instance;
        }
        
        @Override
        public Object read(final InputNode inputNode) throws Exception {
            final Section section = this.schema.getSection();
            this.composite.readVersion(inputNode, null, this.schema);
            this.composite.readText(inputNode, null, section);
            this.composite.readAttributes(inputNode, null, section);
            this.composite.readElements(inputNode, null, section);
            return this.readInject(inputNode);
        }
    }
}
