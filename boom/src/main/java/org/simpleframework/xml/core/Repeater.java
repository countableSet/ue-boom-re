// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.InputNode;

interface Repeater extends Converter
{
    Object read(final InputNode p0, final Object p1) throws Exception;
}
