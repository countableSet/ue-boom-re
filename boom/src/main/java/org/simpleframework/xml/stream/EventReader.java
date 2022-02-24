// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

interface EventReader
{
    EventNode next() throws Exception;
    
    EventNode peek() throws Exception;
}
