// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import java.util.ArrayList;
import javax.xml.stream.events.Characters;
import javax.xml.stream.Location;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.Iterator;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.XMLEventReader;

class StreamReader implements EventReader
{
    private EventNode peek;
    private XMLEventReader reader;
    
    public StreamReader(final XMLEventReader reader) {
        this.reader = reader;
    }
    
    private Entry attribute(final Attribute attribute) {
        return new Entry(attribute);
    }
    
    private Start build(final Start start) {
        final Iterator<Attribute> attributes = start.getAttributes();
        while (attributes.hasNext()) {
            final Entry attribute = this.attribute(attributes.next());
            if (!attribute.isReserved()) {
                ((ArrayList<Entry>)start).add(attribute);
            }
        }
        return start;
    }
    
    private End end() {
        return new End();
    }
    
    private EventNode read() throws Exception {
        final XMLEvent nextEvent = this.reader.nextEvent();
        EventNode eventNode;
        if (!nextEvent.isEndDocument()) {
            if (nextEvent.isStartElement()) {
                eventNode = this.start(nextEvent);
            }
            else if (nextEvent.isCharacters()) {
                eventNode = this.text(nextEvent);
            }
            else if (nextEvent.isEndElement()) {
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
    
    private Start start(final XMLEvent xmlEvent) {
        Start build;
        final Start start = build = new Start(xmlEvent);
        if (start.isEmpty()) {
            build = this.build(start);
        }
        return build;
    }
    
    private Text text(final XMLEvent xmlEvent) {
        return new Text(xmlEvent);
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
        private final javax.xml.stream.events.Attribute entry;
        
        public Entry(final javax.xml.stream.events.Attribute entry) {
            this.entry = entry;
        }
        
        @Override
        public String getName() {
            return this.entry.getName().getLocalPart();
        }
        
        @Override
        public String getPrefix() {
            return this.entry.getName().getPrefix();
        }
        
        @Override
        public String getReference() {
            return this.entry.getName().getNamespaceURI();
        }
        
        @Override
        public Object getSource() {
            return this.entry;
        }
        
        @Override
        public String getValue() {
            return this.entry.getValue();
        }
        
        @Override
        public boolean isReserved() {
            return false;
        }
    }
    
    private static class Start extends EventElement
    {
        private final StartElement element;
        private final Location location;
        
        public Start(final XMLEvent xmlEvent) {
            this.element = xmlEvent.asStartElement();
            this.location = xmlEvent.getLocation();
        }
        
        public Iterator<Attribute> getAttributes() {
            return this.element.getAttributes();
        }
        
        @Override
        public int getLine() {
            return this.location.getLineNumber();
        }
        
        @Override
        public String getName() {
            return this.element.getName().getLocalPart();
        }
        
        @Override
        public String getPrefix() {
            return this.element.getName().getPrefix();
        }
        
        @Override
        public String getReference() {
            return this.element.getName().getNamespaceURI();
        }
        
        @Override
        public Object getSource() {
            return this.element;
        }
    }
    
    private static class Text extends EventToken
    {
        private final Characters text;
        
        public Text(final XMLEvent xmlEvent) {
            this.text = xmlEvent.asCharacters();
        }
        
        @Override
        public Object getSource() {
            return this.text;
        }
        
        @Override
        public String getValue() {
            return this.text.getData();
        }
        
        @Override
        public boolean isText() {
            return true;
        }
    }
}
