// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Namespace;
import java.util.List;
import java.lang.reflect.Constructor;
import java.lang.annotation.Annotation;
import org.simpleframework.xml.DefaultType;

interface Detail
{
    DefaultType getAccess();
    
    Annotation[] getAnnotations();
    
    Constructor[] getConstructors();
    
    List<FieldDetail> getFields();
    
    List<MethodDetail> getMethods();
    
    String getName();
    
    Namespace getNamespace();
    
    NamespaceList getNamespaceList();
    
    Order getOrder();
    
    DefaultType getOverride();
    
    Root getRoot();
    
    Class getSuper();
    
    Class getType();
    
    boolean isInstantiable();
    
    boolean isPrimitive();
    
    boolean isRequired();
    
    boolean isStrict();
}
