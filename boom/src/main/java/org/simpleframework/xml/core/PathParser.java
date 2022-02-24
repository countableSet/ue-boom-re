// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Iterator;
import java.util.ArrayList;
import org.simpleframework.xml.util.ConcurrentCache;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Style;
import java.util.List;
import org.simpleframework.xml.util.Cache;

class PathParser implements Expression
{
    protected boolean attribute;
    protected Cache<String> attributes;
    protected StringBuilder builder;
    protected String cache;
    protected int count;
    protected char[] data;
    protected Cache<String> elements;
    protected List<Integer> indexes;
    protected String location;
    protected List<String> names;
    protected int off;
    protected String path;
    protected List<String> prefixes;
    protected int start;
    protected Style style;
    protected Type type;
    
    public PathParser(final String path, final Type type, final Format format) throws Exception {
        this.attributes = new ConcurrentCache<String>();
        this.elements = new ConcurrentCache<String>();
        this.indexes = new ArrayList<Integer>();
        this.prefixes = new ArrayList<String>();
        this.names = new ArrayList<String>();
        this.builder = new StringBuilder();
        this.style = format.getStyle();
        this.type = type;
        this.parse(this.path = path);
    }
    
    private void align() throws Exception {
        if (this.names.size() > this.indexes.size()) {
            this.indexes.add(1);
        }
    }
    
    private void attribute() throws Exception {
        final int off = this.off + 1;
        this.off = off;
        while (this.off < this.count) {
            final char c = this.data[this.off++];
            if (!this.isValid(c)) {
                throw new PathException("Illegal character '%s' in attribute for '%s' in %s", new Object[] { c, this.path, this.type });
            }
        }
        if (this.off <= off) {
            throw new PathException("Attribute reference in '%s' for %s is empty", new Object[] { this.path, this.type });
        }
        this.attribute = true;
        this.attribute(off, this.off - off);
    }
    
    private void attribute(final int offset, final int count) {
        final String s = new String(this.data, offset, count);
        if (count > 0) {
            this.attribute(s);
        }
    }
    
    private void attribute(String attribute) {
        attribute = this.style.getAttribute(attribute);
        this.prefixes.add(null);
        this.names.add(attribute);
    }
    
    private void build() {
        for (int size = this.names.size(), i = 0; i < size; ++i) {
            final String str = this.prefixes.get(i);
            final String s = this.names.get(i);
            final int intValue = this.indexes.get(i);
            if (i > 0) {
                this.builder.append('/');
            }
            if (this.attribute && i == size - 1) {
                this.builder.append('@');
                this.builder.append(s);
            }
            else {
                if (str != null) {
                    this.builder.append(str);
                    this.builder.append(':');
                }
                this.builder.append(s);
                this.builder.append('[');
                this.builder.append(intValue);
                this.builder.append(']');
            }
        }
        this.location = this.builder.toString();
    }
    
    private void element() throws Exception {
        final int off = this.off;
        int n = 0;
        while (this.off < this.count) {
            final char c = this.data[this.off++];
            if (!this.isValid(c)) {
                if (c == '@') {
                    --this.off;
                    break;
                }
                if (c == '[') {
                    this.index();
                    break;
                }
                if (c != '/') {
                    throw new PathException("Illegal character '%s' in element for '%s' in %s", new Object[] { c, this.path, this.type });
                }
                break;
            }
            else {
                ++n;
            }
        }
        this.element(off, n);
    }
    
    private void element(final int offset, final int count) {
        final String s = new String(this.data, offset, count);
        if (count > 0) {
            this.element(s);
        }
    }
    
    private void element(String element) {
        final int index = element.indexOf(58);
        String substring = null;
        String substring2 = element;
        if (index > 0) {
            substring = element.substring(0, index);
            substring2 = element.substring(index + 1);
        }
        element = this.style.getElement(substring2);
        this.prefixes.add(substring);
        this.names.add(element);
    }
    
    private void index() throws Exception {
        int i = 0;
        int n = 0;
        if (this.data[this.off - 1] == '[') {
            while (true) {
                i = n;
                if (this.off >= this.count) {
                    break;
                }
                final char c = this.data[this.off++];
                if (!this.isDigit(c)) {
                    i = n;
                    break;
                }
                n = n * 10 + c - 48;
            }
        }
        if (this.data[this.off++ - 1] != ']') {
            throw new PathException("Invalid index for path '%s' in %s", new Object[] { this.path, this.type });
        }
        this.indexes.add(i);
    }
    
    private boolean isDigit(final char ch) {
        return Character.isDigit(ch);
    }
    
    private boolean isEmpty(final String s) {
        return s == null || s.length() == 0;
    }
    
    private boolean isLetter(final char ch) {
        return Character.isLetterOrDigit(ch);
    }
    
    private boolean isSpecial(final char c) {
        return c == '_' || c == '-' || c == ':';
    }
    
    private boolean isValid(final char c) {
        return this.isLetter(c) || this.isSpecial(c);
    }
    
    private void parse(final String s) throws Exception {
        if (s != null) {
            this.count = s.length();
            this.data = new char[this.count];
            s.getChars(0, this.count, this.data, 0);
        }
        this.path();
    }
    
    private void path() throws Exception {
        if (this.data[this.off] == '/') {
            throw new PathException("Path '%s' in %s references document root", new Object[] { this.path, this.type });
        }
        if (this.data[this.off] == '.') {
            this.skip();
        }
        while (this.off < this.count) {
            if (this.attribute) {
                throw new PathException("Path '%s' in %s references an invalid attribute", new Object[] { this.path, this.type });
            }
            this.segment();
        }
        this.truncate();
        this.build();
    }
    
    private void segment() throws Exception {
        final char c = this.data[this.off];
        if (c == '/') {
            throw new PathException("Invalid path expression '%s' in %s", new Object[] { this.path, this.type });
        }
        if (c == '@') {
            this.attribute();
        }
        else {
            this.element();
        }
        this.align();
    }
    
    private void skip() throws Exception {
        if (this.data.length > 1) {
            if (this.data[this.off + 1] != '/') {
                throw new PathException("Path '%s' in %s has an illegal syntax", new Object[] { this.path, this.type });
            }
            ++this.off;
        }
        final int n = this.off + 1;
        this.off = n;
        this.start = n;
    }
    
    private void truncate() throws Exception {
        if (this.off - 1 >= this.data.length) {
            --this.off;
        }
        else if (this.data[this.off - 1] == '/') {
            --this.off;
        }
    }
    
    @Override
    public String getAttribute(final String s) {
        String attribute;
        if (!this.isEmpty(this.location)) {
            if ((attribute = this.attributes.fetch(s)) == null) {
                final String attributePath = this.getAttributePath(this.location, s);
                if ((attribute = attributePath) != null) {
                    this.attributes.cache(s, attributePath);
                    attribute = attributePath;
                }
            }
        }
        else {
            attribute = this.style.getAttribute(s);
        }
        return attribute;
    }
    
    protected String getAttributePath(String string, String attribute) {
        attribute = this.style.getAttribute(attribute);
        if (this.isEmpty(string)) {
            string = attribute;
        }
        else {
            string = string + "/@" + attribute;
        }
        return string;
    }
    
    @Override
    public String getElement(final String s) {
        String element;
        if (!this.isEmpty(this.location)) {
            if ((element = this.elements.fetch(s)) == null) {
                final String elementPath = this.getElementPath(this.location, s);
                if ((element = elementPath) != null) {
                    this.elements.cache(s, elementPath);
                    element = elementPath;
                }
            }
        }
        else {
            element = this.style.getElement(s);
        }
        return element;
    }
    
    protected String getElementPath(String string, String element) {
        element = this.style.getElement(element);
        if (!this.isEmpty(element)) {
            if (this.isEmpty(string)) {
                string = element;
            }
            else {
                string = string + "/" + element + "[1]";
            }
        }
        return string;
    }
    
    @Override
    public String getFirst() {
        return this.names.get(0);
    }
    
    @Override
    public int getIndex() {
        return this.indexes.get(0);
    }
    
    @Override
    public String getLast() {
        return this.names.get(this.names.size() - 1);
    }
    
    @Override
    public String getPath() {
        return this.location;
    }
    
    @Override
    public Expression getPath(final int n) {
        return this.getPath(n, 0);
    }
    
    @Override
    public Expression getPath(final int n, final int n2) {
        final int n3 = this.names.size() - 1;
        PathSection pathSection;
        if (n3 - n2 >= n) {
            pathSection = new PathSection(n, n3 - n2);
        }
        else {
            pathSection = new PathSection(n, n);
        }
        return pathSection;
    }
    
    @Override
    public String getPrefix() {
        return this.prefixes.get(0);
    }
    
    @Override
    public boolean isAttribute() {
        return this.attribute;
    }
    
    @Override
    public boolean isEmpty() {
        return this.isEmpty(this.location);
    }
    
    @Override
    public boolean isPath() {
        boolean b = true;
        if (this.names.size() <= 1) {
            b = false;
        }
        return b;
    }
    
    @Override
    public Iterator<String> iterator() {
        return this.names.iterator();
    }
    
    @Override
    public String toString() {
        final int off = this.off;
        final int start = this.start;
        if (this.cache == null) {
            this.cache = new String(this.data, this.start, off - start);
        }
        return this.cache;
    }
    
    private class PathSection implements Expression
    {
        private int begin;
        private List<String> cache;
        private int end;
        private String path;
        private String section;
        
        public PathSection(final int begin, final int end) {
            this.cache = new ArrayList<String>();
            this.begin = begin;
            this.end = end;
        }
        
        private String getCanonicalPath() {
            int index = 0;
            int i;
            for (i = 0; i < this.begin; ++i) {
                index = PathParser.this.location.indexOf(47, index + 1);
            }
            final int n = index;
            int j = i;
            int endIndex = n;
            while (j <= this.end) {
                if ((endIndex = PathParser.this.location.indexOf(47, endIndex + 1)) == -1) {
                    endIndex = PathParser.this.location.length();
                }
                ++j;
            }
            return PathParser.this.location.substring(index + 1, endIndex);
        }
        
        private String getFragment() {
            int start = PathParser.this.start;
            int offset = 0;
            int n = 0;
            int n2;
            while (true) {
                n2 = start;
                if (n > this.end) {
                    break;
                }
                if (start >= PathParser.this.count) {
                    n2 = start + 1;
                    break;
                }
                final char[] data = PathParser.this.data;
                final int n3 = start + 1;
                int n4 = n;
                if (data[start] == '/') {
                    n4 = ++n;
                    if (n == this.begin) {
                        offset = n3;
                        start = n3;
                        continue;
                    }
                }
                start = n3;
                n = n4;
            }
            return new String(PathParser.this.data, offset, n2 - 1 - offset);
        }
        
        @Override
        public String getAttribute(final String s) {
            final String path = this.getPath();
            String attributePath = s;
            if (path != null) {
                attributePath = PathParser.this.getAttributePath(path, s);
            }
            return attributePath;
        }
        
        @Override
        public String getElement(final String s) {
            final String path = this.getPath();
            String elementPath = s;
            if (path != null) {
                elementPath = PathParser.this.getElementPath(path, s);
            }
            return elementPath;
        }
        
        @Override
        public String getFirst() {
            return PathParser.this.names.get(this.begin);
        }
        
        @Override
        public int getIndex() {
            return PathParser.this.indexes.get(this.begin);
        }
        
        @Override
        public String getLast() {
            return PathParser.this.names.get(this.end);
        }
        
        @Override
        public String getPath() {
            if (this.section == null) {
                this.section = this.getCanonicalPath();
            }
            return this.section;
        }
        
        @Override
        public Expression getPath(final int n) {
            return this.getPath(n, 0);
        }
        
        @Override
        public Expression getPath(final int n, final int n2) {
            return new PathSection(this.begin + n, this.end - n2);
        }
        
        @Override
        public String getPrefix() {
            return PathParser.this.prefixes.get(this.begin);
        }
        
        @Override
        public boolean isAttribute() {
            boolean b = false;
            if (PathParser.this.attribute) {
                b = b;
                if (this.end >= PathParser.this.names.size() - 1) {
                    b = true;
                }
            }
            return b;
        }
        
        @Override
        public boolean isEmpty() {
            return this.begin == this.end;
        }
        
        @Override
        public boolean isPath() {
            boolean b = true;
            if (this.end - this.begin < 1) {
                b = false;
            }
            return b;
        }
        
        @Override
        public Iterator<String> iterator() {
            if (this.cache.isEmpty()) {
                for (int i = this.begin; i <= this.end; ++i) {
                    final String s = PathParser.this.names.get(i);
                    if (s != null) {
                        this.cache.add(s);
                    }
                }
            }
            return this.cache.iterator();
        }
        
        @Override
        public String toString() {
            if (this.path == null) {
                this.path = this.getFragment();
            }
            return this.path;
        }
    }
}
