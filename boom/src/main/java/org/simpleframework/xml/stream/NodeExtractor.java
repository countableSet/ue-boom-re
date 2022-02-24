// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.util.LinkedList;

class NodeExtractor extends LinkedList<org.w3c.dom.Node>
{
    public NodeExtractor(final Document document) {
        this.extract(document);
    }
    
    private void extract(final Document document) {
        final Element documentElement = document.getDocumentElement();
        if (documentElement != null) {
            ((LinkedList<Element>)this).offer(documentElement);
            this.extract(documentElement);
        }
    }
    
    private void extract(final org.w3c.dom.Node node) {
        final NodeList childNodes = node.getChildNodes();
        for (int length = childNodes.getLength(), i = 0; i < length; ++i) {
            final org.w3c.dom.Node item = childNodes.item(i);
            if (item.getNodeType() != 8) {
                this.offer(item);
                this.extract(item);
            }
        }
    }
}
