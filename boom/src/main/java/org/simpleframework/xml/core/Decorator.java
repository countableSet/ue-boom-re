// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.OutputNode;

interface Decorator
{
    void decorate(final OutputNode p0);
    
    void decorate(final OutputNode p0, final Decorator p1);
}
