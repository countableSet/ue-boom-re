// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml;

import org.simpleframework.xml.stream.OutputNode;
import java.io.Writer;
import java.io.OutputStream;
import org.simpleframework.xml.stream.InputNode;
import java.io.Reader;
import java.io.InputStream;
import java.io.File;

public interface Serializer
{
     <T> T read(final Class<? extends T> p0, final File p1) throws Exception;
    
     <T> T read(final Class<? extends T> p0, final File p1, final boolean p2) throws Exception;
    
     <T> T read(final Class<? extends T> p0, final InputStream p1) throws Exception;
    
     <T> T read(final Class<? extends T> p0, final InputStream p1, final boolean p2) throws Exception;
    
     <T> T read(final Class<? extends T> p0, final Reader p1) throws Exception;
    
     <T> T read(final Class<? extends T> p0, final Reader p1, final boolean p2) throws Exception;
    
     <T> T read(final Class<? extends T> p0, final String p1) throws Exception;
    
     <T> T read(final Class<? extends T> p0, final String p1, final boolean p2) throws Exception;
    
     <T> T read(final Class<? extends T> p0, final InputNode p1) throws Exception;
    
     <T> T read(final Class<? extends T> p0, final InputNode p1, final boolean p2) throws Exception;
    
     <T> T read(final T p0, final File p1) throws Exception;
    
     <T> T read(final T p0, final File p1, final boolean p2) throws Exception;
    
     <T> T read(final T p0, final InputStream p1) throws Exception;
    
     <T> T read(final T p0, final InputStream p1, final boolean p2) throws Exception;
    
     <T> T read(final T p0, final Reader p1) throws Exception;
    
     <T> T read(final T p0, final Reader p1, final boolean p2) throws Exception;
    
     <T> T read(final T p0, final String p1) throws Exception;
    
     <T> T read(final T p0, final String p1, final boolean p2) throws Exception;
    
     <T> T read(final T p0, final InputNode p1) throws Exception;
    
     <T> T read(final T p0, final InputNode p1, final boolean p2) throws Exception;
    
    boolean validate(final Class p0, final File p1) throws Exception;
    
    boolean validate(final Class p0, final File p1, final boolean p2) throws Exception;
    
    boolean validate(final Class p0, final InputStream p1) throws Exception;
    
    boolean validate(final Class p0, final InputStream p1, final boolean p2) throws Exception;
    
    boolean validate(final Class p0, final Reader p1) throws Exception;
    
    boolean validate(final Class p0, final Reader p1, final boolean p2) throws Exception;
    
    boolean validate(final Class p0, final String p1) throws Exception;
    
    boolean validate(final Class p0, final String p1, final boolean p2) throws Exception;
    
    boolean validate(final Class p0, final InputNode p1) throws Exception;
    
    boolean validate(final Class p0, final InputNode p1, final boolean p2) throws Exception;
    
    void write(final Object p0, final File p1) throws Exception;
    
    void write(final Object p0, final OutputStream p1) throws Exception;
    
    void write(final Object p0, final Writer p1) throws Exception;
    
    void write(final Object p0, final OutputNode p1) throws Exception;
}
