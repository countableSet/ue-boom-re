// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import java.util.Iterator;
import java.util.HashSet;
import java.io.Writer;
import java.util.Set;

class NodeWriter
{
    private final Set active;
    private final OutputStack stack;
    private final boolean verbose;
    private final Formatter writer;
    
    public NodeWriter(final Writer writer) {
        this(writer, new Format());
    }
    
    public NodeWriter(final Writer writer, final Format format) {
        this(writer, format, false);
    }
    
    private NodeWriter(final Writer writer, final Format format, final boolean verbose) {
        this.writer = new Formatter(writer, format);
        this.active = new HashSet();
        this.stack = new OutputStack(this.active);
        this.verbose = verbose;
    }
    
    private void writeAttributes(final OutputNode outputNode) throws Exception {
        final NodeMap<OutputNode> attributes = outputNode.getAttributes();
        for (final String s : attributes) {
            final OutputNode outputNode2 = attributes.get(s);
            this.writer.writeAttribute(s, outputNode2.getValue(), outputNode2.getPrefix(this.verbose));
        }
        this.active.remove(outputNode);
    }
    
    private void writeComment(final OutputNode outputNode) throws Exception {
        final String comment = outputNode.getComment();
        if (comment != null) {
            this.writer.writeComment(comment);
        }
    }
    
    private void writeEnd(final OutputNode outputNode) throws Exception {
        final String name = outputNode.getName();
        final String prefix = outputNode.getPrefix(this.verbose);
        if (outputNode.getValue() != null) {
            this.writeValue(outputNode);
        }
        if (name != null) {
            this.writer.writeEnd(name, prefix);
            this.writer.flush();
        }
    }
    
    private void writeName(final OutputNode outputNode) throws Exception {
        final String prefix = outputNode.getPrefix(this.verbose);
        final String name = outputNode.getName();
        if (name != null) {
            this.writer.writeStart(name, prefix);
        }
    }
    
    private void writeNamespaces(final OutputNode outputNode) throws Exception {
        final NamespaceMap namespaces = outputNode.getNamespaces();
        for (final String s : namespaces) {
            this.writer.writeNamespace(s, namespaces.getPrefix(s));
        }
    }
    
    private OutputNode writeStart(final OutputNode outputNode, final String s) throws Exception {
        final OutputElement outputElement = new OutputElement(outputNode, this, s);
        if (s == null) {
            throw new NodeException("Can not have a null name");
        }
        return this.stack.push(outputElement);
    }
    
    private void writeStart(final OutputNode outputNode) throws Exception {
        this.writeComment(outputNode);
        this.writeName(outputNode);
        this.writeAttributes(outputNode);
        this.writeNamespaces(outputNode);
    }
    
    private void writeValue(final OutputNode outputNode) throws Exception {
        Mode mode = outputNode.getMode();
        final String value = outputNode.getValue();
        if (value != null) {
            for (final OutputNode outputNode2 : this.stack) {
                if (mode != Mode.INHERIT) {
                    break;
                }
                mode = outputNode2.getMode();
            }
            this.writer.writeText(value, mode);
        }
        outputNode.setValue(null);
    }
    
    public void commit(final OutputNode o) throws Exception {
        if (this.stack.contains(o)) {
            final OutputNode top = this.stack.top();
            if (!this.isCommitted(top)) {
                this.writeStart(top);
            }
            while (this.stack.top() != o) {
                this.writeEnd(this.stack.pop());
            }
            this.writeEnd(o);
            this.stack.pop();
        }
    }
    
    public boolean isCommitted(final OutputNode outputNode) {
        return !this.active.contains(outputNode);
    }
    
    public boolean isRoot(final OutputNode outputNode) {
        return this.stack.bottom() == outputNode;
    }
    
    public void remove(final OutputNode outputNode) throws Exception {
        if (this.stack.top() != outputNode) {
            throw new NodeException("Cannot remove node");
        }
        this.stack.pop();
    }
    
    public OutputNode writeElement(OutputNode o, final String s) throws Exception {
        if (this.stack.isEmpty()) {
            o = this.writeStart(o, s);
        }
        else if (this.stack.contains(o)) {
            final OutputNode top = this.stack.top();
            if (!this.isCommitted(top)) {
                this.writeStart(top);
            }
            while (this.stack.top() != o) {
                this.writeEnd(this.stack.pop());
            }
            if (!this.stack.isEmpty()) {
                this.writeValue(o);
            }
            o = this.writeStart(o, s);
        }
        else {
            o = null;
        }
        return o;
    }
    
    public OutputNode writeRoot() throws Exception {
        final OutputDocument outputDocument = new OutputDocument(this, this.stack);
        if (this.stack.isEmpty()) {
            this.writer.writeProlog();
        }
        return outputDocument;
    }
}
