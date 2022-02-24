// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.Order;
import org.simpleframework.xml.stream.Format;

class ModelAssembler
{
    private final ExpressionBuilder builder;
    private final Detail detail;
    private final Format format;
    
    public ModelAssembler(final ExpressionBuilder builder, final Detail detail, final Support support) throws Exception {
        this.format = support.getFormat();
        this.builder = builder;
        this.detail = detail;
    }
    
    private void assembleAttributes(final Model model, final Order order) throws Exception {
        for (final String s : order.attributes()) {
            final Expression build = this.builder.build(s);
            if (!build.isAttribute() && build.isPath()) {
                throw new PathException("Ordered attribute '%s' references an element in %s", new Object[] { build, this.detail });
            }
            if (!build.isPath()) {
                model.registerAttribute(this.format.getStyle().getAttribute(s));
            }
            else {
                this.registerAttributes(model, build);
            }
        }
    }
    
    private void assembleElements(final Model model, final Order order) throws Exception {
        final String[] elements = order.elements();
        for (int length = elements.length, i = 0; i < length; ++i) {
            final Expression build = this.builder.build(elements[i]);
            if (build.isAttribute()) {
                throw new PathException("Ordered element '%s' references an attribute in %s", new Object[] { build, this.detail });
            }
            this.registerElements(model, build);
        }
    }
    
    private void registerAttribute(final Model model, final Expression expression) throws Exception {
        final String first = expression.getFirst();
        if (first != null) {
            model.registerAttribute(first);
        }
    }
    
    private void registerAttributes(Model register, Expression path) throws Exception {
        final String prefix = path.getPrefix();
        final String first = path.getFirst();
        final int index = path.getIndex();
        if (path.isPath()) {
            register = register.register(first, prefix, index);
            path = path.getPath(1);
            if (register == null) {
                throw new PathException("Element '%s' does not exist in %s", new Object[] { first, this.detail });
            }
            this.registerAttributes(register, path);
        }
        else {
            this.registerAttribute(register, path);
        }
    }
    
    private void registerElement(final Model model, final Expression expression) throws Exception {
        final String prefix = expression.getPrefix();
        final String first = expression.getFirst();
        final int index = expression.getIndex();
        if (index > 1 && model.lookup(first, index - 1) == null) {
            throw new PathException("Ordered element '%s' in path '%s' is out of sequence for %s", new Object[] { first, expression, this.detail });
        }
        model.register(first, prefix, index);
    }
    
    private void registerElements(final Model model, final Expression expression) throws Exception {
        final String prefix = expression.getPrefix();
        final String first = expression.getFirst();
        final int index = expression.getIndex();
        if (first != null) {
            final Model register = model.register(first, prefix, index);
            final Expression path = expression.getPath(1);
            if (expression.isPath()) {
                this.registerElements(register, path);
            }
        }
        this.registerElement(model, expression);
    }
    
    public void assemble(final Model model, final Order order) throws Exception {
        this.assembleElements(model, order);
        this.assembleAttributes(model, order);
    }
}
