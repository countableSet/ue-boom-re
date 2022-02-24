// 
// Decompiled by Procyon v0.5.36
// 

package com.caverock.androidsvg;

import android.util.Log;
import java.util.Locale;
import java.util.ArrayList;
import org.xml.sax.SAXException;
import java.util.Iterator;
import java.util.List;

public class CSSParser
{
    private static final String CLASS = "class";
    private static final String ID = "id";
    private static final String TAG = "AndroidSVG CSSParser";
    private boolean inMediaRule;
    private MediaType rendererMediaType;
    
    public CSSParser(final MediaType rendererMediaType) {
        this.rendererMediaType = null;
        this.inMediaRule = false;
        this.rendererMediaType = rendererMediaType;
    }
    
    private static int getChildPosition(final List<SVG.SvgContainer> list, int n, final SVG.SvgElementBase svgElementBase) {
        if (n < 0) {
            n = -1;
        }
        else if (list.get(n) != svgElementBase.parent) {
            n = -1;
        }
        else {
            int n2 = 0;
            final Iterator<SVG.SvgObject> iterator = svgElementBase.parent.getChildren().iterator();
            while (iterator.hasNext()) {
                n = n2;
                if (iterator.next() == svgElementBase) {
                    return n;
                }
                ++n2;
            }
            n = -1;
        }
        return n;
    }
    
    public static boolean mediaMatches(final String s, final MediaType mediaType) throws SAXException {
        final CSSTextScanner cssTextScanner = new CSSTextScanner(s);
        ((SVGParser.TextScanner)cssTextScanner).skipWhitespace();
        final List<MediaType> mediaList = parseMediaList(cssTextScanner);
        if (!((SVGParser.TextScanner)cssTextScanner).empty()) {
            throw new SAXException("Invalid @media type list");
        }
        return mediaMatches(mediaList, mediaType);
    }
    
    private static boolean mediaMatches(final List<MediaType> list, final MediaType mediaType) {
        for (final MediaType mediaType2 : list) {
            if (mediaType2 == MediaType.all || mediaType2 == mediaType) {
                return true;
            }
        }
        return false;
    }
    
    private void parseAtRule(final Ruleset ruleset, final CSSTextScanner cssTextScanner) throws SAXException {
        final String nextIdentifier = cssTextScanner.nextIdentifier();
        ((SVGParser.TextScanner)cssTextScanner).skipWhitespace();
        if (nextIdentifier == null) {
            throw new SAXException("Invalid '@' rule in <style> element");
        }
        if (!this.inMediaRule && nextIdentifier.equals("media")) {
            final List<MediaType> mediaList = parseMediaList(cssTextScanner);
            if (!((SVGParser.TextScanner)cssTextScanner).consume('{')) {
                throw new SAXException("Invalid @media rule: missing rule set");
            }
            ((SVGParser.TextScanner)cssTextScanner).skipWhitespace();
            if (mediaMatches(mediaList, this.rendererMediaType)) {
                this.inMediaRule = true;
                ruleset.addAll(this.parseRuleset(cssTextScanner));
                this.inMediaRule = false;
            }
            else {
                this.parseRuleset(cssTextScanner);
            }
            if (!((SVGParser.TextScanner)cssTextScanner).consume('}')) {
                throw new SAXException("Invalid @media rule: expected '}' at end of rule set");
            }
        }
        else {
            warn("Ignoring @%s rule", nextIdentifier);
            this.skipAtRule(cssTextScanner);
        }
        ((SVGParser.TextScanner)cssTextScanner).skipWhitespace();
    }
    
    protected static List<String> parseClassAttribute(final String str) throws SAXException {
        final CSSTextScanner cssTextScanner = new CSSTextScanner(str);
        List<String> list = null;
        while (!((SVGParser.TextScanner)cssTextScanner).empty()) {
            final String nextIdentifier = cssTextScanner.nextIdentifier();
            if (nextIdentifier == null) {
                throw new SAXException("Invalid value for \"class\" attribute: " + str);
            }
            List<String> list2;
            if ((list2 = list) == null) {
                list2 = new ArrayList<String>();
            }
            list2.add(nextIdentifier);
            ((SVGParser.TextScanner)cssTextScanner).skipWhitespace();
            list = list2;
        }
        return list;
    }
    
    private SVG.Style parseDeclarations(final CSSTextScanner cssTextScanner) throws SAXException {
        final SVG.Style style = new SVG.Style();
        do {
            final String nextIdentifier = cssTextScanner.nextIdentifier();
            ((SVGParser.TextScanner)cssTextScanner).skipWhitespace();
            if (((SVGParser.TextScanner)cssTextScanner).consume(':')) {
                ((SVGParser.TextScanner)cssTextScanner).skipWhitespace();
                final String nextPropertyValue = cssTextScanner.nextPropertyValue();
                if (nextPropertyValue != null) {
                    ((SVGParser.TextScanner)cssTextScanner).skipWhitespace();
                    if (((SVGParser.TextScanner)cssTextScanner).consume('!')) {
                        ((SVGParser.TextScanner)cssTextScanner).skipWhitespace();
                        if (!((SVGParser.TextScanner)cssTextScanner).consume("important")) {
                            throw new SAXException("Malformed rule set in <style> element: found unexpected '!'");
                        }
                        ((SVGParser.TextScanner)cssTextScanner).skipWhitespace();
                    }
                    ((SVGParser.TextScanner)cssTextScanner).consume(';');
                    SVGParser.processStyleProperty(style, nextIdentifier, nextPropertyValue);
                    ((SVGParser.TextScanner)cssTextScanner).skipWhitespace();
                    if (((SVGParser.TextScanner)cssTextScanner).consume('}')) {
                        return style;
                    }
                    continue;
                }
            }
            throw new SAXException("Malformed rule set in <style> element");
        } while (!((SVGParser.TextScanner)cssTextScanner).empty());
        throw new SAXException("Malformed rule set in <style> element");
    }
    
    private static List<MediaType> parseMediaList(final CSSTextScanner cssTextScanner) throws SAXException {
        final ArrayList<MediaType> list = new ArrayList<MediaType>();
        while (!((SVGParser.TextScanner)cssTextScanner).empty()) {
            final String nextToken = ((SVGParser.TextScanner)cssTextScanner).nextToken(',');
            try {
                list.add(MediaType.valueOf(nextToken));
                if (!((SVGParser.TextScanner)cssTextScanner).skipCommaWhitespace()) {
                    break;
                }
                continue;
            }
            catch (IllegalArgumentException ex) {
                throw new SAXException("Invalid @media type list");
            }
        }
        return list;
    }
    
    private boolean parseRule(final Ruleset ruleset, final CSSTextScanner cssTextScanner) throws SAXException {
        final List<Selector> selectorGroup = this.parseSelectorGroup(cssTextScanner);
        boolean b;
        if (selectorGroup != null && !selectorGroup.isEmpty()) {
            if (!((SVGParser.TextScanner)cssTextScanner).consume('{')) {
                throw new SAXException("Malformed rule block in <style> element: missing '{'");
            }
            ((SVGParser.TextScanner)cssTextScanner).skipWhitespace();
            final SVG.Style declarations = this.parseDeclarations(cssTextScanner);
            ((SVGParser.TextScanner)cssTextScanner).skipWhitespace();
            final Iterator<Selector> iterator = selectorGroup.iterator();
            while (iterator.hasNext()) {
                ruleset.add(new Rule((Selector)iterator.next(), declarations));
            }
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    private Ruleset parseRuleset(final CSSTextScanner cssTextScanner) throws SAXException {
        final Ruleset ruleset = new Ruleset();
        while (!((SVGParser.TextScanner)cssTextScanner).empty()) {
            if (!((SVGParser.TextScanner)cssTextScanner).consume("<!--") && !((SVGParser.TextScanner)cssTextScanner).consume("-->")) {
                if (((SVGParser.TextScanner)cssTextScanner).consume('@')) {
                    this.parseAtRule(ruleset, cssTextScanner);
                }
                else {
                    if (this.parseRule(ruleset, cssTextScanner)) {
                        continue;
                    }
                    break;
                }
            }
        }
        return ruleset;
    }
    
    private List<Selector> parseSelectorGroup(final CSSTextScanner cssTextScanner) throws SAXException {
        List<Selector> list;
        if (((SVGParser.TextScanner)cssTextScanner).empty()) {
            list = null;
        }
        else {
            final ArrayList<Selector> list2 = new ArrayList<Selector>(1);
            Selector selector;
            for (selector = new Selector(); !((SVGParser.TextScanner)cssTextScanner).empty() && cssTextScanner.nextSimpleSelector(selector); selector = new Selector()) {
                if (((SVGParser.TextScanner)cssTextScanner).skipCommaWhitespace()) {
                    list2.add(selector);
                }
            }
            list = list2;
            if (!selector.isEmpty()) {
                list2.add(selector);
                list = list2;
            }
        }
        return list;
    }
    
    private static boolean ruleMatch(final Selector selector, final int n, final List<SVG.SvgContainer> list, int i, final SVG.SvgElementBase svgElementBase) {
        boolean b = false;
        final SimpleSelector value = selector.get(n);
        if (selectorMatch(value, list, i, svgElementBase)) {
            if (value.combinator == Combinator.DESCENDANT) {
                if (n == 0) {
                    b = true;
                }
                else {
                    while (i >= 0) {
                        if (ruleMatchOnAncestors(selector, n - 1, list, i)) {
                            b = true;
                            break;
                        }
                        --i;
                    }
                }
            }
            else if (value.combinator == Combinator.CHILD) {
                b = ruleMatchOnAncestors(selector, n - 1, list, i);
            }
            else {
                final int childPosition = getChildPosition(list, i, svgElementBase);
                if (childPosition > 0) {
                    b = ruleMatch(selector, n - 1, list, i, (SVG.SvgElementBase)svgElementBase.parent.getChildren().get(childPosition - 1));
                }
            }
        }
        return b;
    }
    
    protected static boolean ruleMatch(final Selector selector, final SVG.SvgElementBase svgElementBase) {
        final ArrayList<SVG.SvgContainer> list = new ArrayList<SVG.SvgContainer>();
        for (SVG.SvgContainer svgContainer = svgElementBase.parent; svgContainer != null; svgContainer = ((SVG.SvgObject)svgContainer).parent) {
            list.add(0, svgContainer);
        }
        final int n = list.size() - 1;
        boolean b;
        if (selector.size() == 1) {
            b = selectorMatch(selector.get(0), list, n, svgElementBase);
        }
        else {
            b = ruleMatch(selector, selector.size() - 1, list, n, svgElementBase);
        }
        return b;
    }
    
    private static boolean ruleMatchOnAncestors(final Selector selector, final int n, final List<SVG.SvgContainer> list, int i) {
        boolean b = false;
        final SimpleSelector value = selector.get(n);
        final SVG.SvgElementBase svgElementBase = (SVG.SvgElementBase)list.get(i);
        if (selectorMatch(value, list, i, svgElementBase)) {
            if (value.combinator == Combinator.DESCENDANT) {
                if (n == 0) {
                    b = true;
                }
                else {
                    while (i > 0) {
                        if (ruleMatchOnAncestors(selector, n - 1, list, --i)) {
                            b = true;
                            break;
                        }
                    }
                }
            }
            else if (value.combinator == Combinator.CHILD) {
                b = ruleMatchOnAncestors(selector, n - 1, list, i - 1);
            }
            else {
                final int childPosition = getChildPosition(list, i, svgElementBase);
                if (childPosition > 0) {
                    b = ruleMatch(selector, n - 1, list, i, (SVG.SvgElementBase)svgElementBase.parent.getChildren().get(childPosition - 1));
                }
            }
        }
        return b;
    }
    
    private static boolean selectorMatch(final SimpleSelector simpleSelector, final List<SVG.SvgContainer> list, final int n, final SVG.SvgElementBase svgElementBase) {
        final boolean b = false;
        Label_0064: {
            if (simpleSelector.tag == null) {
                break Label_0064;
            }
            boolean b2;
            if (simpleSelector.tag.equalsIgnoreCase("G")) {
                if (svgElementBase instanceof SVG.Group) {
                    break Label_0064;
                }
                b2 = b;
            }
            else {
                b2 = b;
                if (simpleSelector.tag.equals(svgElementBase.getClass().getSimpleName().toLowerCase(Locale.US))) {
                    break Label_0064;
                }
            }
            return b2;
        }
        if (simpleSelector.attribs != null) {
            for (final Attrib attrib : simpleSelector.attribs) {
                if (attrib.name == "id") {
                    if (!attrib.value.equals(svgElementBase.id)) {
                        return b;
                    }
                    continue;
                }
                else {
                    boolean b2 = b;
                    if (attrib.name != "class") {
                        return b2;
                    }
                    b2 = b;
                    if (svgElementBase.classNames == null) {
                        return b2;
                    }
                    if (!svgElementBase.classNames.contains(attrib.value)) {
                        b2 = b;
                        return b2;
                    }
                    continue;
                }
            }
        }
        if (simpleSelector.pseudos != null) {
            final Iterator<String> iterator2 = simpleSelector.pseudos.iterator();
            while (iterator2.hasNext()) {
                boolean b2 = b;
                if (!iterator2.next().equals("first-child")) {
                    return b2;
                }
                if (getChildPosition(list, n, svgElementBase) != 0) {
                    b2 = b;
                    return b2;
                }
            }
        }
        return true;
    }
    
    private void skipAtRule(final CSSTextScanner cssTextScanner) {
        int n = 0;
        while (!((SVGParser.TextScanner)cssTextScanner).empty()) {
            final int intValue = ((SVGParser.TextScanner)cssTextScanner).nextChar();
            if (intValue == 59 && n == 0) {
                break;
            }
            if (intValue == 123) {
                ++n;
            }
            else {
                if (intValue == 125 && n > 0 && --n == 0) {
                    break;
                }
                continue;
            }
        }
    }
    
    private static void warn(final String format, final Object... args) {
        Log.w("AndroidSVG CSSParser", String.format(format, args));
    }
    
    public Ruleset parse(final String s) throws SAXException {
        final CSSTextScanner cssTextScanner = new CSSTextScanner(s);
        ((SVGParser.TextScanner)cssTextScanner).skipWhitespace();
        return this.parseRuleset(cssTextScanner);
    }
    
    public static class Attrib
    {
        public String name;
        public AttribOp operation;
        public String value;
        
        public Attrib(final String name, final AttribOp operation, final String value) {
            this.name = null;
            this.value = null;
            this.name = name;
            this.operation = operation;
            this.value = value;
        }
    }
    
    private enum AttribOp
    {
        DASHMATCH("DASHMATCH", 3), 
        EQUALS("EQUALS", 1), 
        EXISTS("EXISTS", 0), 
        INCLUDES("INCLUDES", 2);
        
        private AttribOp(final String name, final int ordinal) {
        }
    }
    
    private static class CSSTextScanner extends TextScanner
    {
        public CSSTextScanner(final String s) {
            super(s.replaceAll("(?s)/\\*.*?\\*/", ""));
        }
        
        private String nextAttribValue() {
            String s;
            if (((SVGParser.TextScanner)this).empty()) {
                s = null;
            }
            else if ((s = ((SVGParser.TextScanner)this).nextQuotedString()) == null) {
                s = this.nextIdentifier();
            }
            return s;
        }
        
        private int scanForIdentifier() {
            int n;
            if (((SVGParser.TextScanner)this).empty()) {
                n = this.position;
            }
            else {
                final int position = this.position;
                n = this.position;
                int n2;
                if ((n2 = this.input.charAt(this.position)) == 45) {
                    n2 = ((SVGParser.TextScanner)this).advanceChar();
                }
                if ((n2 >= 65 && n2 <= 90) || (n2 >= 97 && n2 <= 122) || n2 == 95) {
                    for (int n3 = ((SVGParser.TextScanner)this).advanceChar(); (n3 >= 65 && n3 <= 90) || (n3 >= 97 && n3 <= 122) || (n3 >= 48 && n3 <= 57) || n3 == 45 || n3 == 95; n3 = ((SVGParser.TextScanner)this).advanceChar()) {}
                    n = this.position;
                }
                this.position = position;
            }
            return n;
        }
        
        public String nextIdentifier() {
            final int scanForIdentifier = this.scanForIdentifier();
            String substring;
            if (scanForIdentifier == this.position) {
                substring = null;
            }
            else {
                substring = this.input.substring(this.position, scanForIdentifier);
                this.position = scanForIdentifier;
            }
            return substring;
        }
        
        public String nextPropertyValue() {
            String substring = null;
            if (!((SVGParser.TextScanner)this).empty()) {
                final int position = this.position;
                int position2 = this.position;
                for (int n = this.input.charAt(this.position); n != -1 && n != 59 && n != 125 && n != 33 && !((SVGParser.TextScanner)this).isEOL(n); n = ((SVGParser.TextScanner)this).advanceChar()) {
                    if (!((SVGParser.TextScanner)this).isWhitespace(n)) {
                        position2 = this.position + 1;
                    }
                }
                if (this.position > position) {
                    substring = this.input.substring(position, position2);
                }
                else {
                    this.position = position;
                }
            }
            return substring;
        }
        
        public boolean nextSimpleSelector(final Selector selector) throws SAXException {
            boolean b = false;
            if (!((SVGParser.TextScanner)this).empty()) {
                final int position = this.position;
                final Combinator combinator = null;
                Object o = null;
                Enum<Combinator> enum1 = combinator;
                if (!selector.isEmpty()) {
                    if (((SVGParser.TextScanner)this).consume('>')) {
                        enum1 = Combinator.CHILD;
                        ((SVGParser.TextScanner)this).skipWhitespace();
                    }
                    else {
                        enum1 = combinator;
                        if (((SVGParser.TextScanner)this).consume('+')) {
                            enum1 = Combinator.FOLLOWS;
                            ((SVGParser.TextScanner)this).skipWhitespace();
                        }
                    }
                }
                if (((SVGParser.TextScanner)this).consume('*')) {
                    o = new SimpleSelector((Combinator)enum1, null);
                }
                else {
                    final String nextIdentifier = this.nextIdentifier();
                    if (nextIdentifier != null) {
                        o = new SimpleSelector((Combinator)enum1, nextIdentifier);
                        selector.addedElement();
                    }
                }
                while (!((SVGParser.TextScanner)this).empty()) {
                    if (((SVGParser.TextScanner)this).consume('.')) {
                        SimpleSelector simpleSelector;
                        if ((simpleSelector = (SimpleSelector)o) == null) {
                            simpleSelector = new SimpleSelector((Combinator)enum1, null);
                        }
                        final String nextIdentifier2 = this.nextIdentifier();
                        if (nextIdentifier2 == null) {
                            throw new SAXException("Invalid \".class\" selector in <style> element");
                        }
                        simpleSelector.addAttrib("class", AttribOp.EQUALS, nextIdentifier2);
                        selector.addedAttributeOrPseudo();
                        o = simpleSelector;
                    }
                    else {
                        SimpleSelector simpleSelector2 = (SimpleSelector)o;
                        if (((SVGParser.TextScanner)this).consume('#')) {
                            if ((simpleSelector2 = (SimpleSelector)o) == null) {
                                simpleSelector2 = new SimpleSelector((Combinator)enum1, null);
                            }
                            final String nextIdentifier3 = this.nextIdentifier();
                            if (nextIdentifier3 == null) {
                                throw new SAXException("Invalid \"#id\" selector in <style> element");
                            }
                            simpleSelector2.addAttrib("id", AttribOp.EQUALS, nextIdentifier3);
                            selector.addedIdAttribute();
                        }
                        if ((o = simpleSelector2) == null) {
                            break;
                        }
                        if (((SVGParser.TextScanner)this).consume('[')) {
                            ((SVGParser.TextScanner)this).skipWhitespace();
                            final String nextIdentifier4 = this.nextIdentifier();
                            String nextAttribValue = null;
                            if (nextIdentifier4 == null) {
                                throw new SAXException("Invalid attribute selector in <style> element");
                            }
                            ((SVGParser.TextScanner)this).skipWhitespace();
                            Enum<AttribOp> enum2 = null;
                            if (((SVGParser.TextScanner)this).consume('=')) {
                                enum2 = AttribOp.EQUALS;
                            }
                            else if (((SVGParser.TextScanner)this).consume("~=")) {
                                enum2 = AttribOp.INCLUDES;
                            }
                            else if (((SVGParser.TextScanner)this).consume("|=")) {
                                enum2 = AttribOp.DASHMATCH;
                            }
                            if (enum2 != null) {
                                ((SVGParser.TextScanner)this).skipWhitespace();
                                nextAttribValue = this.nextAttribValue();
                                if (nextAttribValue == null) {
                                    throw new SAXException("Invalid attribute selector in <style> element");
                                }
                                ((SVGParser.TextScanner)this).skipWhitespace();
                            }
                            if (!((SVGParser.TextScanner)this).consume(']')) {
                                throw new SAXException("Invalid attribute selector in <style> element");
                            }
                            Enum<AttribOp> exists;
                            if ((exists = enum2) == null) {
                                exists = AttribOp.EXISTS;
                            }
                            simpleSelector2.addAttrib(nextIdentifier4, (AttribOp)exists, nextAttribValue);
                            selector.addedAttributeOrPseudo();
                            o = simpleSelector2;
                        }
                        else {
                            o = simpleSelector2;
                            if (!((SVGParser.TextScanner)this).consume(':')) {
                                break;
                            }
                            final int position2 = this.position;
                            o = simpleSelector2;
                            if (this.nextIdentifier() != null) {
                                if (((SVGParser.TextScanner)this).consume('(')) {
                                    ((SVGParser.TextScanner)this).skipWhitespace();
                                    if (this.nextIdentifier() != null) {
                                        ((SVGParser.TextScanner)this).skipWhitespace();
                                        if (!((SVGParser.TextScanner)this).consume(')')) {
                                            this.position = position2 - 1;
                                            o = simpleSelector2;
                                            break;
                                        }
                                    }
                                }
                                simpleSelector2.addPseudo(this.input.substring(position2, this.position));
                                selector.addedAttributeOrPseudo();
                                o = simpleSelector2;
                                break;
                            }
                            break;
                        }
                    }
                }
                if (o != null) {
                    selector.add((SimpleSelector)o);
                    b = true;
                }
                else {
                    this.position = position;
                }
            }
            return b;
        }
    }
    
    private enum Combinator
    {
        CHILD("CHILD", 1), 
        DESCENDANT("DESCENDANT", 0), 
        FOLLOWS("FOLLOWS", 2);
        
        private Combinator(final String name, final int ordinal) {
        }
    }
    
    public enum MediaType
    {
        all("all", 0), 
        aural("aural", 1), 
        braille("braille", 2), 
        embossed("embossed", 3), 
        handheld("handheld", 4), 
        print("print", 5), 
        projection("projection", 6), 
        screen("screen", 7), 
        tty("tty", 8), 
        tv("tv", 9);
        
        private MediaType(final String name, final int ordinal) {
        }
    }
    
    public static class Rule
    {
        public Selector selector;
        public SVG.Style style;
        
        public Rule(final Selector selector, final SVG.Style style) {
            this.selector = null;
            this.style = null;
            this.selector = selector;
            this.style = style;
        }
        
        @Override
        public String toString() {
            return this.selector + " {}";
        }
    }
    
    public static class Ruleset
    {
        private List<Rule> rules;
        
        public Ruleset() {
            this.rules = null;
        }
        
        public void add(final Rule rule) {
            if (this.rules == null) {
                this.rules = new ArrayList<Rule>();
            }
            for (int i = 0; i < this.rules.size(); ++i) {
                if (this.rules.get(i).selector.specificity > rule.selector.specificity) {
                    this.rules.add(i, rule);
                    return;
                }
            }
            this.rules.add(rule);
        }
        
        public void addAll(final Ruleset ruleset) {
            if (ruleset.rules != null) {
                if (this.rules == null) {
                    this.rules = new ArrayList<Rule>(ruleset.rules.size());
                }
                final Iterator<Rule> iterator = ruleset.rules.iterator();
                while (iterator.hasNext()) {
                    this.rules.add(iterator.next());
                }
            }
        }
        
        public List<Rule> getRules() {
            return this.rules;
        }
        
        public boolean isEmpty() {
            return this.rules == null || this.rules.isEmpty();
        }
        
        @Override
        public String toString() {
            String string;
            if (this.rules == null) {
                string = "";
            }
            else {
                final StringBuilder sb = new StringBuilder();
                final Iterator<Rule> iterator = this.rules.iterator();
                while (iterator.hasNext()) {
                    sb.append(iterator.next().toString()).append('\n');
                }
                string = sb.toString();
            }
            return string;
        }
    }
    
    public static class Selector
    {
        public List<SimpleSelector> selector;
        public int specificity;
        
        public Selector() {
            this.selector = null;
            this.specificity = 0;
        }
        
        public void add(final SimpleSelector simpleSelector) {
            if (this.selector == null) {
                this.selector = new ArrayList<SimpleSelector>();
            }
            this.selector.add(simpleSelector);
        }
        
        public void addedAttributeOrPseudo() {
            this.specificity += 100;
        }
        
        public void addedElement() {
            ++this.specificity;
        }
        
        public void addedIdAttribute() {
            this.specificity += 10000;
        }
        
        public SimpleSelector get(final int n) {
            return this.selector.get(n);
        }
        
        public boolean isEmpty() {
            return this.selector == null || this.selector.isEmpty();
        }
        
        public int size() {
            int size;
            if (this.selector == null) {
                size = 0;
            }
            else {
                size = this.selector.size();
            }
            return size;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            final Iterator<SimpleSelector> iterator = this.selector.iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next()).append(' ');
            }
            return sb.append('(').append(this.specificity).append(')').toString();
        }
    }
    
    private static class SimpleSelector
    {
        public List<Attrib> attribs;
        public Combinator combinator;
        public List<String> pseudos;
        public String tag;
        
        static /* synthetic */ int[] $SWITCH_TABLE$com$caverock$androidsvg$CSSParser$AttribOp() {
            int[] $switch_TABLE$com$caverock$androidsvg$CSSParser$AttribOp = SimpleSelector.$SWITCH_TABLE$com$caverock$androidsvg$CSSParser$AttribOp;
            if ($switch_TABLE$com$caverock$androidsvg$CSSParser$AttribOp == null) {
                $switch_TABLE$com$caverock$androidsvg$CSSParser$AttribOp = new int[AttribOp.values().length];
                while (true) {
                    try {
                        $switch_TABLE$com$caverock$androidsvg$CSSParser$AttribOp[AttribOp.DASHMATCH.ordinal()] = 4;
                        try {
                            $switch_TABLE$com$caverock$androidsvg$CSSParser$AttribOp[AttribOp.EQUALS.ordinal()] = 2;
                            try {
                                $switch_TABLE$com$caverock$androidsvg$CSSParser$AttribOp[AttribOp.EXISTS.ordinal()] = 1;
                                try {
                                    $switch_TABLE$com$caverock$androidsvg$CSSParser$AttribOp[AttribOp.INCLUDES.ordinal()] = 3;
                                    SimpleSelector.$SWITCH_TABLE$com$caverock$androidsvg$CSSParser$AttribOp = $switch_TABLE$com$caverock$androidsvg$CSSParser$AttribOp;
                                }
                                catch (NoSuchFieldError noSuchFieldError) {}
                            }
                            catch (NoSuchFieldError noSuchFieldError2) {}
                        }
                        catch (NoSuchFieldError noSuchFieldError3) {}
                    }
                    catch (NoSuchFieldError noSuchFieldError4) {
                        continue;
                    }
                    break;
                }
            }
            return $switch_TABLE$com$caverock$androidsvg$CSSParser$AttribOp;
        }
        
        public SimpleSelector(Combinator descendant, final String tag) {
            this.combinator = null;
            this.tag = null;
            this.attribs = null;
            this.pseudos = null;
            if (descendant == null) {
                descendant = Combinator.DESCENDANT;
            }
            this.combinator = descendant;
            this.tag = tag;
        }
        
        public void addAttrib(final String s, final AttribOp attribOp, final String s2) {
            if (this.attribs == null) {
                this.attribs = new ArrayList<Attrib>();
            }
            this.attribs.add(new Attrib(s, attribOp, s2));
        }
        
        public void addPseudo(final String s) {
            if (this.pseudos == null) {
                this.pseudos = new ArrayList<String>();
            }
            this.pseudos.add(s);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            if (this.combinator == Combinator.CHILD) {
                sb.append("> ");
            }
            else if (this.combinator == Combinator.FOLLOWS) {
                sb.append("+ ");
            }
            String tag;
            if (this.tag == null) {
                tag = "*";
            }
            else {
                tag = this.tag;
            }
            sb.append(tag);
            if (this.attribs != null) {
                for (final Attrib attrib : this.attribs) {
                    sb.append('[').append(attrib.name);
                    switch ($SWITCH_TABLE$com$caverock$androidsvg$CSSParser$AttribOp()[attrib.operation.ordinal()]) {
                        case 2: {
                            sb.append('=').append(attrib.value);
                            break;
                        }
                        case 3: {
                            sb.append("~=").append(attrib.value);
                            break;
                        }
                        case 4: {
                            sb.append("|=").append(attrib.value);
                            break;
                        }
                    }
                    sb.append(']');
                }
            }
            if (this.pseudos != null) {
                final Iterator<String> iterator2 = this.pseudos.iterator();
                while (iterator2.hasNext()) {
                    sb.append(':').append(iterator2.next());
                }
            }
            return sb.toString();
        }
    }
}
