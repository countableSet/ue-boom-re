// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import java.util.Iterator;
import java.util.LinkedHashMap;

class PrefixResolver extends LinkedHashMap<String, String> implements NamespaceMap
{
    private final OutputNode source;
    
    public PrefixResolver(final OutputNode source) {
        this.source = source;
    }
    
    private String resolvePrefix(String prefix) {
        final NamespaceMap namespaces = this.source.getNamespaces();
        if (namespaces == null) {
            return null;
        }
        prefix = namespaces.getPrefix(prefix);
        if (this.containsValue(prefix)) {
            return null;
        }
        return prefix;
        prefix = null;
        return prefix;
    }
    
    private String resolveReference(String reference) {
        final NamespaceMap namespaces = this.source.getNamespaces();
        if (namespaces != null) {
            reference = namespaces.getReference(reference);
        }
        else {
            reference = null;
        }
        return reference;
    }
    
    @Override
    public String getPrefix() {
        return this.source.getPrefix();
    }
    
    @Override
    public String getPrefix(String resolvePrefix) {
        if (this.size() <= 0) {
            return this.resolvePrefix(resolvePrefix);
        }
        final String s = ((LinkedHashMap<K, String>)this).get(resolvePrefix);
        if (s == null) {
            return this.resolvePrefix(resolvePrefix);
        }
        resolvePrefix = s;
        return resolvePrefix;
        resolvePrefix = this.resolvePrefix(resolvePrefix);
        return resolvePrefix;
    }
    
    @Override
    public String getReference(String resolveReference) {
        if (this.containsValue(resolveReference)) {
            for (final String key : this) {
                final String s = ((LinkedHashMap<K, String>)this).get(key);
                if (s != null && s.equals(resolveReference)) {
                    resolveReference = key;
                    return resolveReference;
                }
            }
            return this.resolveReference(resolveReference);
        }
        return this.resolveReference(resolveReference);
        resolveReference = this.resolveReference(resolveReference);
        return resolveReference;
    }
    
    @Override
    public Iterator<String> iterator() {
        return ((LinkedHashMap<String, V>)this).keySet().iterator();
    }
    
    @Override
    public String setReference(final String s) {
        return this.setReference(s, "");
    }
    
    @Override
    public String setReference(String key, final String value) {
        if (this.resolvePrefix(key) != null) {
            key = null;
        }
        else {
            key = this.put(key, value);
        }
        return key;
    }
}
