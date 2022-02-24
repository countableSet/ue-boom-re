// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import java.util.concurrent.atomic.AtomicLong;

class AtomicLongTransform implements Transform<AtomicLong>
{
    @Override
    public AtomicLong read(final String s) {
        return new AtomicLong(Long.valueOf(s));
    }
    
    @Override
    public String write(final AtomicLong atomicLong) {
        return atomicLong.toString();
    }
}
