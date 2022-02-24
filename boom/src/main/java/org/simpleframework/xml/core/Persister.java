// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.Reader;
import org.simpleframework.xml.stream.NodeBuilder;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.strategy.TreeStrategy;
import org.simpleframework.xml.filter.Filter;
import org.simpleframework.xml.filter.PlatformFilter;
import java.util.Map;
import java.util.HashMap;
import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.Serializer;

public class Persister implements Serializer
{
    private final Format format;
    private final SessionManager manager;
    private final Strategy strategy;
    private final Support support;
    
    public Persister() {
        this(new HashMap());
    }
    
    public Persister(final Map map) {
        this(new PlatformFilter(map));
    }
    
    public Persister(final Map map, final Format format) {
        this(new PlatformFilter(map));
    }
    
    public Persister(final Filter filter) {
        this(new TreeStrategy(), filter);
    }
    
    public Persister(final Filter filter, final Format format) {
        this(new TreeStrategy(), filter, format);
    }
    
    public Persister(final Filter filter, final Matcher matcher) {
        this(new TreeStrategy(), filter, matcher);
    }
    
    public Persister(final Filter filter, final Matcher matcher, final Format format) {
        this(new TreeStrategy(), filter, matcher, format);
    }
    
    public Persister(final Strategy strategy) {
        this(strategy, new HashMap());
    }
    
    public Persister(final Strategy strategy, final Map map) {
        this(strategy, new PlatformFilter(map));
    }
    
    public Persister(final Strategy strategy, final Map map, final Format format) {
        this(strategy, new PlatformFilter(map), format);
    }
    
    public Persister(final Strategy strategy, final Filter filter) {
        this(strategy, filter, new Format());
    }
    
    public Persister(final Strategy strategy, final Filter filter, final Format format) {
        this(strategy, filter, new EmptyMatcher(), format);
    }
    
    public Persister(final Strategy strategy, final Filter filter, final Matcher matcher) {
        this(strategy, filter, matcher, new Format());
    }
    
    public Persister(final Strategy strategy, final Filter filter, final Matcher matcher, final Format format) {
        this.support = new Support(filter, matcher, format);
        this.manager = new SessionManager();
        this.strategy = strategy;
        this.format = format;
    }
    
    public Persister(final Strategy strategy, final Format format) {
        this(strategy, new HashMap(), format);
    }
    
    public Persister(final Strategy strategy, final Matcher matcher) {
        this(strategy, new PlatformFilter(), matcher);
    }
    
    public Persister(final Strategy strategy, final Matcher matcher, final Format format) {
        this(strategy, new PlatformFilter(), matcher, format);
    }
    
    public Persister(final Format format) {
        this(new TreeStrategy(), format);
    }
    
    public Persister(final Matcher matcher) {
        this(new TreeStrategy(), matcher);
    }
    
    public Persister(final Matcher matcher, final Format format) {
        this(new TreeStrategy(), matcher, format);
    }
    
    private <T> T read(final Class<? extends T> clazz, final InputNode inputNode, final Context context) throws Exception {
        return (T)new Traverser(context).read(inputNode, clazz);
    }
    
    private <T> T read(final Class<? extends T> clazz, final InputNode inputNode, final Session session) throws Exception {
        return this.read(clazz, inputNode, (Context)new Source(this.strategy, this.support, session));
    }
    
    private <T> T read(final T t, final InputNode inputNode, final Context context) throws Exception {
        return (T)new Traverser(context).read(inputNode, t);
    }
    
    private <T> T read(final T t, final InputNode inputNode, final Session session) throws Exception {
        return this.read(t, inputNode, new Source(this.strategy, this.support, session));
    }
    
    private boolean validate(final Class clazz, final InputNode inputNode, final Context context) throws Exception {
        return new Traverser(context).validate(inputNode, clazz);
    }
    
    private boolean validate(final Class clazz, final InputNode inputNode, final Session session) throws Exception {
        return this.validate(clazz, inputNode, new Source(this.strategy, this.support, session));
    }
    
    private void write(final Object o, final OutputNode outputNode, final Context context) throws Exception {
        new Traverser(context).write(outputNode, o);
    }
    
    private void write(final Object o, final OutputNode outputNode, final Session session) throws Exception {
        this.write(o, outputNode, new Source(this.strategy, this.support, session));
    }
    
    @Override
    public <T> T read(final Class<? extends T> clazz, final File file) throws Exception {
        return this.read(clazz, file, true);
    }
    
    @Override
    public <T> T read(final Class<? extends T> clazz, File file, final boolean b) throws Exception {
        file = (File)new FileInputStream(file);
        try {
            return this.read(clazz, (InputStream)file, b);
        }
        finally {
            ((InputStream)file).close();
        }
    }
    
    @Override
    public <T> T read(final Class<? extends T> clazz, final InputStream inputStream) throws Exception {
        return this.read(clazz, inputStream, true);
    }
    
    @Override
    public <T> T read(final Class<? extends T> clazz, final InputStream inputStream, final boolean b) throws Exception {
        return this.read(clazz, NodeBuilder.read(inputStream), b);
    }
    
    @Override
    public <T> T read(final Class<? extends T> clazz, final Reader reader) throws Exception {
        return this.read(clazz, reader, true);
    }
    
    @Override
    public <T> T read(final Class<? extends T> clazz, final Reader reader, final boolean b) throws Exception {
        return this.read(clazz, NodeBuilder.read(reader), b);
    }
    
    @Override
    public <T> T read(final Class<? extends T> clazz, final String s) throws Exception {
        return this.read(clazz, s, true);
    }
    
    @Override
    public <T> T read(final Class<? extends T> clazz, final String s, final boolean b) throws Exception {
        return this.read(clazz, (Reader)new StringReader(s), b);
    }
    
    @Override
    public <T> T read(final Class<? extends T> clazz, final InputNode inputNode) throws Exception {
        return this.read(clazz, inputNode, true);
    }
    
    @Override
    public <T> T read(final Class<? extends T> clazz, final InputNode inputNode, final boolean b) throws Exception {
        final Session open = this.manager.open(b);
        try {
            return (T)this.read((Class<?>)clazz, inputNode, open);
        }
        finally {
            this.manager.close();
        }
    }
    
    @Override
    public <T> T read(final T t, final File file) throws Exception {
        return this.read(t, file, true);
    }
    
    @Override
    public <T> T read(final T t, File file, final boolean b) throws Exception {
        file = (File)new FileInputStream(file);
        try {
            return this.read(t, (InputStream)file, b);
        }
        finally {
            ((InputStream)file).close();
        }
    }
    
    @Override
    public <T> T read(final T t, final InputStream inputStream) throws Exception {
        return this.read(t, inputStream, true);
    }
    
    @Override
    public <T> T read(final T t, final InputStream inputStream, final boolean b) throws Exception {
        return this.read(t, NodeBuilder.read(inputStream), b);
    }
    
    @Override
    public <T> T read(final T t, final Reader reader) throws Exception {
        return this.read(t, reader, true);
    }
    
    @Override
    public <T> T read(final T t, final Reader reader, final boolean b) throws Exception {
        return this.read(t, NodeBuilder.read(reader), b);
    }
    
    @Override
    public <T> T read(final T t, final String s) throws Exception {
        return this.read(t, s, true);
    }
    
    @Override
    public <T> T read(final T t, final String s, final boolean b) throws Exception {
        return this.read(t, new StringReader(s), b);
    }
    
    @Override
    public <T> T read(final T t, final InputNode inputNode) throws Exception {
        return this.read(t, inputNode, true);
    }
    
    @Override
    public <T> T read(final T t, final InputNode inputNode, final boolean b) throws Exception {
        final Session open = this.manager.open(b);
        try {
            return this.read(t, inputNode, open);
        }
        finally {
            this.manager.close();
        }
    }
    
    @Override
    public boolean validate(final Class clazz, final File file) throws Exception {
        return this.validate(clazz, file, true);
    }
    
    @Override
    public boolean validate(final Class clazz, File file, final boolean b) throws Exception {
        file = (File)new FileInputStream(file);
        try {
            return this.validate(clazz, (InputStream)file, b);
        }
        finally {
            ((InputStream)file).close();
        }
    }
    
    @Override
    public boolean validate(final Class clazz, final InputStream inputStream) throws Exception {
        return this.validate(clazz, inputStream, true);
    }
    
    @Override
    public boolean validate(final Class clazz, final InputStream inputStream, final boolean b) throws Exception {
        return this.validate(clazz, NodeBuilder.read(inputStream), b);
    }
    
    @Override
    public boolean validate(final Class clazz, final Reader reader) throws Exception {
        return this.validate(clazz, reader, true);
    }
    
    @Override
    public boolean validate(final Class clazz, final Reader reader, final boolean b) throws Exception {
        return this.validate(clazz, NodeBuilder.read(reader), b);
    }
    
    @Override
    public boolean validate(final Class clazz, final String s) throws Exception {
        return this.validate(clazz, s, true);
    }
    
    @Override
    public boolean validate(final Class clazz, final String s, final boolean b) throws Exception {
        return this.validate(clazz, new StringReader(s), b);
    }
    
    @Override
    public boolean validate(final Class clazz, final InputNode inputNode) throws Exception {
        return this.validate(clazz, inputNode, true);
    }
    
    @Override
    public boolean validate(final Class clazz, final InputNode inputNode, final boolean b) throws Exception {
        final Session open = this.manager.open(b);
        try {
            return this.validate(clazz, inputNode, open);
        }
        finally {
            this.manager.close();
        }
    }
    
    @Override
    public void write(final Object o, File file) throws Exception {
        file = (File)new FileOutputStream(file);
        try {
            this.write(o, (OutputStream)file);
        }
        finally {
            ((OutputStream)file).close();
        }
    }
    
    @Override
    public void write(final Object o, final OutputStream outputStream) throws Exception {
        this.write(o, outputStream, "utf-8");
    }
    
    public void write(final Object o, final OutputStream out, final String charsetName) throws Exception {
        this.write(o, new OutputStreamWriter(out, charsetName));
    }
    
    @Override
    public void write(final Object o, final Writer writer) throws Exception {
        this.write(o, NodeBuilder.write(writer, this.format));
    }
    
    @Override
    public void write(final Object o, final OutputNode outputNode) throws Exception {
        final Session open = this.manager.open();
        try {
            this.write(o, outputNode, open);
        }
        finally {
            this.manager.close();
        }
    }
}
