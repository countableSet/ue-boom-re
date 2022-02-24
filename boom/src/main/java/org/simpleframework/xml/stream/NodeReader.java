// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

class NodeReader
{
    private final EventReader reader;
    private final InputStack stack;
    private final StringBuilder text;
    
    public NodeReader(final EventReader reader) {
        this.text = new StringBuilder();
        this.stack = new InputStack();
        this.reader = reader;
    }
    
    private void fillText(final InputNode inputNode) throws Exception {
        final EventNode peek = this.reader.peek();
        if (peek.isText()) {
            this.text.append(peek.getValue());
        }
    }
    
    private boolean isName(final EventNode eventNode, final String anObject) {
        final String name = eventNode.getName();
        return name != null && name.equals(anObject);
    }
    
    private String readBuffer(final InputNode inputNode) throws Exception {
        String string;
        if (this.text.length() > 0) {
            string = this.text.toString();
            this.text.setLength(0);
        }
        else {
            string = null;
        }
        return string;
    }
    
    private InputNode readStart(InputNode inputNode, final EventNode eventNode) throws Exception {
        inputNode = new InputElement(inputNode, this, eventNode);
        if (this.text.length() > 0) {
            this.text.setLength(0);
        }
        if (eventNode.isStart()) {
            inputNode = this.stack.push(inputNode);
        }
        return inputNode;
    }
    
    private String readText(final InputNode inputNode) throws Exception {
        for (EventNode eventNode = this.reader.peek(); this.stack.top() == inputNode && eventNode.isText(); eventNode = this.reader.peek()) {
            this.fillText(inputNode);
            this.reader.next();
        }
        return this.readBuffer(inputNode);
    }
    
    public boolean isEmpty(final InputNode inputNode) throws Exception {
        return this.stack.top() == inputNode && this.reader.peek().isEnd();
    }
    
    public boolean isRoot(final InputNode inputNode) {
        return this.stack.bottom() == inputNode;
    }
    
    public InputNode readElement(final InputNode inputNode) throws Exception {
        final InputNode inputNode2 = null;
        InputNode start;
        if (!this.stack.isRelevant(inputNode)) {
            start = inputNode2;
        }
        else {
            EventNode eventNode = this.reader.next();
            while (true) {
                start = inputNode2;
                if (eventNode == null) {
                    return start;
                }
                if (eventNode.isEnd()) {
                    start = inputNode2;
                    if (this.stack.pop() == inputNode) {
                        return start;
                    }
                }
                else if (eventNode.isStart()) {
                    break;
                }
                eventNode = this.reader.next();
            }
            start = this.readStart(inputNode, eventNode);
        }
        return start;
    }
    
    public InputNode readElement(final InputNode inputNode, final String s) throws Exception {
        final InputNode inputNode2 = null;
        InputNode element;
        if (!this.stack.isRelevant(inputNode)) {
            element = inputNode2;
        }
        else {
            EventNode eventNode = this.reader.peek();
            while (true) {
                element = inputNode2;
                if (eventNode == null) {
                    return element;
                }
                if (eventNode.isText()) {
                    this.fillText(inputNode);
                }
                else if (eventNode.isEnd()) {
                    element = inputNode2;
                    if (this.stack.top() == inputNode) {
                        return element;
                    }
                    this.stack.pop();
                }
                else if (eventNode.isStart()) {
                    break;
                }
                this.reader.next();
                eventNode = this.reader.peek();
            }
            element = inputNode2;
            if (this.isName(eventNode, s)) {
                element = this.readElement(inputNode);
            }
        }
        return element;
    }
    
    public InputNode readRoot() throws Exception {
        InputNode element = null;
        if (this.stack.isEmpty() && (element = this.readElement(null)) == null) {
            throw new NodeException("Document has no root element");
        }
        return element;
    }
    
    public String readValue(final InputNode inputNode) throws Exception {
        String text = null;
        if (this.stack.isRelevant(inputNode)) {
            if (this.text.length() <= 0 && this.reader.peek().isEnd()) {
                if (this.stack.top() == inputNode) {
                    return text;
                }
                this.stack.pop();
                this.reader.next();
            }
            text = this.readText(inputNode);
        }
        return text;
    }
    
    public void skipElement(final InputNode inputNode) throws Exception {
        while (this.readElement(inputNode) != null) {}
    }
}
