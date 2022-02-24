// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.transform.Transform;
import org.simpleframework.xml.transform.Matcher;

class EmptyMatcher implements Matcher
{
    @Override
    public Transform match(final Class clazz) throws Exception {
        return null;
    }
}
