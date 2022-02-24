// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.util.LimitedCache;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.util.Cache;

class ExpressionBuilder
{
    private final Cache<Expression> cache;
    private final Format format;
    private final Class type;
    
    public ExpressionBuilder(final Detail detail, final Support support) {
        this.cache = new LimitedCache<Expression>();
        this.format = support.getFormat();
        this.type = detail.getType();
    }
    
    private Expression create(final String s) throws Exception {
        final PathParser pathParser = new PathParser(s, new ClassType(this.type), this.format);
        if (this.cache != null) {
            this.cache.cache(s, pathParser);
        }
        return pathParser;
    }
    
    public Expression build(final String s) throws Exception {
        Expression create;
        if ((create = this.cache.fetch(s)) == null) {
            create = this.create(s);
        }
        return create;
    }
}
