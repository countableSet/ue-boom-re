// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Iterator;
import java.util.LinkedHashMap;

class ContactMap extends LinkedHashMap<Object, Contact> implements Iterable<Contact>
{
    @Override
    public Iterator<Contact> iterator() {
        return ((LinkedHashMap<K, Contact>)this).values().iterator();
    }
}
