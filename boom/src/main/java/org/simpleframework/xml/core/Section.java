// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

interface Section extends Iterable<String>
{
    String getAttribute(final String p0) throws Exception;
    
    LabelMap getAttributes() throws Exception;
    
    Label getElement(final String p0) throws Exception;
    
    LabelMap getElements() throws Exception;
    
    String getName();
    
    String getPath(final String p0) throws Exception;
    
    String getPrefix();
    
    Section getSection(final String p0) throws Exception;
    
    Label getText() throws Exception;
    
    boolean isSection(final String p0) throws Exception;
}
