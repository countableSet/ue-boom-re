// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Document;

class DocumentReader implements EventReader
{
    private static final String RESERVED = "xml";
    private EventNode peek;
    private NodeExtractor queue;
    private NodeStack stack;
    
    public DocumentReader(final Document document) {
        this.queue = new NodeExtractor(document);
        ((Stack<Document>)(this.stack = new NodeStack())).push(document);
    }
    
    private Entry attribute(final Node node) {
        return new Entry(node);
    }
    
    private Start build(final Start start) {
        final NamedNodeMap attributes = start.getAttributes();
        for (int length = attributes.getLength(), i = 0; i < length; ++i) {
            final Entry attribute = this.attribute(attributes.item(i));
            if (!attribute.isReserved()) {
                ((ArrayList<Entry>)start).add(attribute);
            }
        }
        return start;
    }
    
    private EventNode convert(final Node node) throws Exception {
        EventNode eventNode;
        if (node.getNodeType() == 1) {
            if (node != null) {
                this.stack.push(node);
            }
            eventNode = this.start(node);
        }
        else {
            eventNode = this.text(node);
        }
        return eventNode;
    }
    
    private End end() {
        return new End();
    }
    
    private EventNode read() throws Exception {
        final Node node = this.queue.peek();
        EventNode eventNode;
        if (node == null) {
            eventNode = this.end();
        }
        else {
            eventNode = this.read(node);
        }
        return eventNode;
    }
    
    private EventNode read(final Node node) throws Exception {
        final Node parentNode = node.getParentNode();
        final Node node2 = this.stack.top();
        EventNode eventNode;
        if (parentNode != node2) {
            if (node2 != null) {
                this.stack.pop();
            }
            eventNode = this.end();
        }
        else {
            if (node != null) {
                this.queue.poll();
            }
            eventNode = this.convert(node);
        }
        return eventNode;
    }
    
    private Start start(final Node node) {
        Start build;
        final Start start = build = new Start(node);
        if (start.isEmpty()) {
            build = this.build(start);
        }
        return build;
    }
    
    private Text text(final Node node) {
        return new Text(node);
    }
    
    @Override
    public EventNode next() throws Exception {
        EventNode eventNode = this.peek;
        if (eventNode == null) {
            eventNode = this.read();
        }
        else {
            this.peek = null;
        }
        return eventNode;
    }
    
    @Override
    public EventNode peek() throws Exception {
        if (this.peek == null) {
            this.peek = this.next();
        }
        return this.peek;
    }
    
    private static class End extends EventToken
    {
        @Override
        public boolean isEnd() {
            return true;
        }
    }
    
    private static class Entry extends EventAttribute
    {
        private final Node node;
        
        public Entry(final Node node) {
            this.node = node;
        }
        
        @Override
        public String getName() {
            return this.node.getLocalName();
        }
        
        @Override
        public String getPrefix() {
            return this.node.getPrefix();
        }
        
        @Override
        public String getReference() {
            return this.node.getNamespaceURI();
        }
        
        @Override
        public Object getSource() {
            return this.node;
        }
        
        @Override
        public String getValue() {
            return this.node.getNodeValue();
        }
        
        @Override
        public boolean isReserved() {
            final String prefix = this.getPrefix();
            final String name = this.getName();
            boolean b;
            if (prefix != null) {
                b = prefix.startsWith("xml");
            }
            else {
                b = name.startsWith("xml");
            }
            return b;
        }
    }
    
    private static class Start extends EventElement
    {
        private final Element element;
        
        public Start(final Node node) {
            this.element = (Element)node;
        }
        
        public NamedNodeMap getAttributes() {
            return this.element.getAttributes();
        }
        
        @Override
        public String getName() {
            return this.element.getLocalName();
        }
        
        @Override
        public String getPrefix() {
            return this.element.getPrefix();
        }
        
        @Override
        public String getReference() {
            return this.element.getNamespaceURI();
        }
        
        @Override
        public Object getSource() {
            return this.element;
        }
    }
    
    private static class Text extends EventToken
    {
        private final Node node;
        
        public Text(final Node node) {
            this.node = node;
        }
        
        @Override
        public Object getSource() {
            return this.node;
        }
        
        @Override
        public String getValue() {
            return this.node.getNodeValue();
        }
        
        @Override
        public boolean isText() {
            return true;
        }
    }
}
