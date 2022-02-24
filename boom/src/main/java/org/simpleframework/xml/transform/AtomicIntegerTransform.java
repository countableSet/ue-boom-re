// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import java.util.concurrent.atomic.AtomicInteger;

class AtomicIntegerTransform implements Transform<AtomicInteger>
{
    @Override
    public AtomicInteger read(final String s) {
        return new AtomicInteger(Integer.valueOf(s));
    }
    
    @Override
    public String write(final AtomicInteger atomicInteger) {
        return atomicInteger.toString();
    }
}
