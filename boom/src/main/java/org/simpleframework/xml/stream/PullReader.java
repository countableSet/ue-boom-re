// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;

class PullReader implements EventReader
{
    private XmlPullParser parser;
    private EventNode peek;
    
    public PullReader(final XmlPullParser parser) {
        this.parser = parser;
    }
    
    private Entry attribute(final int n) throws Exception {
        return new Entry(this.parser, n);
    }
    
    private Start build(final Start start) throws Exception {
        for (int attributeCount = this.parser.getAttributeCount(), i = 0; i < attributeCount; ++i) {
            final Entry attribute = this.attribute(i);
            if (!attribute.isReserved()) {
                ((ArrayList<Entry>)start).add(attribute);
            }
        }
        return start;
    }
    
    private End end() throws Exception {
        return new End();
    }
    
    private EventNode read() throws Exception {
        final int next = this.parser.next();
        EventNode eventNode;
        if (next != 1) {
            if (next == 2) {
                eventNode = this.start();
            }
            else if (next == 4) {
                eventNode = this.text();
            }
            else if (next == 3) {
                eventNode = this.end();
            }
            else {
                eventNode = this.read();
            }
        }
        else {
            eventNode = null;
        }
        return eventNode;
    }
    
    private Start start() throws Exception {
        Start build;
        final Start start = build = new Start(this.parser);
        if (start.isEmpty()) {
            build = this.build(start);
        }
        return build;
    }
    
    private Text text() throws Exception {
        return new Text(this.parser);
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
        private final String name;
        private final String prefix;
        private final String reference;
        private final XmlPullParser source;
        private final String value;
        
        public Entry(final XmlPullParser source, final int n) {
            this.reference = source.getAttributeNamespace(n);
            this.prefix = source.getAttributePrefix(n);
            this.value = source.getAttributeValue(n);
            this.name = source.getAttributeName(n);
            this.source = source;
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
        public String getReference() {
            return this.reference;
        }
        
        @Override
        public Object getSource() {
            return this.source;
        }
        
        @Override
        public String getValue() {
            return this.value;
        }
        
        @Override
        public boolean isReserved() {
            return false;
        }
    }
    
    private static class Start extends EventElement
    {
        private final int line;
        private final String name;
        private final String prefix;
        private final String reference;
        private final XmlPullParser source;
        
        public Start(final XmlPullParser source) {
            this.reference = source.getNamespace();
            this.line = source.getLineNumber();
            this.prefix = source.getPrefix();
            this.name = source.getName();
            this.source = source;
        }
        
        @Override
        public int getLine() {
            return this.line;
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
        public String getReference() {
            return this.reference;
        }
        
        @Override
        public Object getSource() {
            return this.source;
        }
    }
    
    private static class Text extends EventToken
    {
        private final XmlPullParser source;
        private final String text;
        
        public Text(final XmlPullParser source) {
            this.text = source.getText();
            this.source = source;
        }
        
        @Override
        public Object getSource() {
            return this.source;
        }
        
        @Override
        public String getValue() {
            return this.text;
        }
        
        @Override
        public boolean isText() {
            return true;
        }
    }
}
