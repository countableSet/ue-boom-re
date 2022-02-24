// 
// Decompiled by Procyon v0.5.36
// 

package com.caverock.androidsvg;

import android.graphics.Matrix;
import org.xml.sax.SAXException;
import android.graphics.Picture;
import android.graphics.Canvas;
import android.util.Log;
import java.util.HashSet;
import java.util.Set;
import android.graphics.RectF;
import java.io.ByteArrayInputStream;
import android.content.res.Resources;
import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import android.content.res.AssetManager;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class SVG
{
    private static final int DEFAULT_PICTURE_HEIGHT = 512;
    private static final int DEFAULT_PICTURE_WIDTH = 512;
    private static final List<SvgObject> EMPTY_CHILD_LIST;
    protected static final long SPECIFIED_ALL = -1L;
    protected static final long SPECIFIED_CLIP = 1048576L;
    protected static final long SPECIFIED_CLIP_PATH = 268435456L;
    protected static final long SPECIFIED_CLIP_RULE = 536870912L;
    protected static final long SPECIFIED_COLOR = 4096L;
    protected static final long SPECIFIED_DIRECTION = 68719476736L;
    protected static final long SPECIFIED_DISPLAY = 16777216L;
    protected static final long SPECIFIED_FILL = 1L;
    protected static final long SPECIFIED_FILL_OPACITY = 4L;
    protected static final long SPECIFIED_FILL_RULE = 2L;
    protected static final long SPECIFIED_FONT_FAMILY = 8192L;
    protected static final long SPECIFIED_FONT_SIZE = 16384L;
    protected static final long SPECIFIED_FONT_STYLE = 65536L;
    protected static final long SPECIFIED_FONT_WEIGHT = 32768L;
    protected static final long SPECIFIED_MARKER_END = 8388608L;
    protected static final long SPECIFIED_MARKER_MID = 4194304L;
    protected static final long SPECIFIED_MARKER_START = 2097152L;
    protected static final long SPECIFIED_MASK = 1073741824L;
    protected static final long SPECIFIED_NON_INHERITING = 68133849088L;
    protected static final long SPECIFIED_OPACITY = 2048L;
    protected static final long SPECIFIED_OVERFLOW = 524288L;
    protected static final long SPECIFIED_SOLID_COLOR = 2147483648L;
    protected static final long SPECIFIED_SOLID_OPACITY = 4294967296L;
    protected static final long SPECIFIED_STOP_COLOR = 67108864L;
    protected static final long SPECIFIED_STOP_OPACITY = 134217728L;
    protected static final long SPECIFIED_STROKE = 8L;
    protected static final long SPECIFIED_STROKE_DASHARRAY = 512L;
    protected static final long SPECIFIED_STROKE_DASHOFFSET = 1024L;
    protected static final long SPECIFIED_STROKE_LINECAP = 64L;
    protected static final long SPECIFIED_STROKE_LINEJOIN = 128L;
    protected static final long SPECIFIED_STROKE_MITERLIMIT = 256L;
    protected static final long SPECIFIED_STROKE_OPACITY = 16L;
    protected static final long SPECIFIED_STROKE_WIDTH = 32L;
    protected static final long SPECIFIED_TEXT_ANCHOR = 262144L;
    protected static final long SPECIFIED_TEXT_DECORATION = 131072L;
    protected static final long SPECIFIED_VECTOR_EFFECT = 34359738368L;
    protected static final long SPECIFIED_VIEWPORT_FILL = 8589934592L;
    protected static final long SPECIFIED_VIEWPORT_FILL_OPACITY = 17179869184L;
    protected static final long SPECIFIED_VISIBILITY = 33554432L;
    private static final double SQRT2 = 1.414213562373095;
    protected static final String SUPPORTED_SVG_VERSION = "1.2";
    private static final String TAG = "AndroidSVG";
    private static final String VERSION = "1.2.1";
    private CSSParser.Ruleset cssRules;
    private String desc;
    private SVGExternalFileResolver fileResolver;
    private float renderDPI;
    private Svg rootElement;
    private String title;
    
    static {
        EMPTY_CHILD_LIST = new ArrayList<SvgObject>(0);
    }
    
    protected SVG() {
        this.rootElement = null;
        this.title = "";
        this.desc = "";
        this.fileResolver = null;
        this.renderDPI = 96.0f;
        this.cssRules = new CSSParser.Ruleset();
    }
    
    private Box getDocumentDimensions(float floatValue) {
        final Length width = this.rootElement.width;
        final Length height = this.rootElement.height;
        Box box;
        if (width == null || width.isZero() || width.unit == Unit.percent || width.unit == Unit.em || width.unit == Unit.ex) {
            box = new Box(-1.0f, -1.0f, -1.0f, -1.0f);
        }
        else {
            final float floatValue2 = width.floatValue(floatValue);
            if (height != null) {
                if (height.isZero() || height.unit == Unit.percent || height.unit == Unit.em || height.unit == Unit.ex) {
                    box = new Box(-1.0f, -1.0f, -1.0f, -1.0f);
                    return box;
                }
                floatValue = height.floatValue(floatValue);
            }
            else if (this.rootElement.viewBox != null) {
                floatValue = this.rootElement.viewBox.height * floatValue2 / this.rootElement.viewBox.width;
            }
            else {
                floatValue = floatValue2;
            }
            box = new Box(0.0f, 0.0f, floatValue2, floatValue);
        }
        return box;
    }
    
    private SvgElementBase getElementById(final SvgContainer svgContainer, final String s) {
        final SvgElementBase svgElementBase = (SvgElementBase)svgContainer;
        SvgElementBase elementById;
        if (s.equals(svgElementBase.id)) {
            elementById = svgElementBase;
        }
        else {
            for (final SvgObject svgObject : svgContainer.getChildren()) {
                if (svgObject instanceof SvgElementBase) {
                    elementById = (SvgElementBase)svgObject;
                    if (s.equals(elementById.id)) {
                        return elementById;
                    }
                    if (!(svgObject instanceof SvgContainer)) {
                        continue;
                    }
                    elementById = this.getElementById((SvgContainer)svgObject, s);
                    if (elementById != null) {
                        return elementById;
                    }
                    continue;
                }
            }
            elementById = null;
        }
        return elementById;
    }
    
    private List<SvgObject> getElementsByTagName(final SvgContainer svgContainer, final Class clazz) {
        final ArrayList<SvgContainer> list = (ArrayList<SvgContainer>)new ArrayList<SvgObject>();
        if (svgContainer.getClass() == clazz) {
            list.add((SvgObject)svgContainer);
        }
        for (final SvgObject svgObject : svgContainer.getChildren()) {
            if (svgObject.getClass() == clazz) {
                list.add(svgObject);
            }
            if (svgObject instanceof SvgContainer) {
                this.getElementsByTagName((SvgContainer)svgObject, clazz);
            }
        }
        return (List<SvgObject>)list;
    }
    
    public static SVG getFromAsset(final AssetManager assetManager, final String s) throws SVGParseException, IOException {
        final SVGParser svgParser = new SVGParser();
        final InputStream open = assetManager.open(s);
        final SVG parse = svgParser.parse(open);
        open.close();
        return parse;
    }
    
    public static SVG getFromInputStream(final InputStream inputStream) throws SVGParseException {
        return new SVGParser().parse(inputStream);
    }
    
    public static SVG getFromResource(final Context context, final int n) throws SVGParseException {
        return getFromResource(context.getResources(), n);
    }
    
    public static SVG getFromResource(final Resources resources, final int n) throws SVGParseException {
        return new SVGParser().parse(resources.openRawResource(n));
    }
    
    public static SVG getFromString(final String s) throws SVGParseException {
        return new SVGParser().parse(new ByteArrayInputStream(s.getBytes()));
    }
    
    public static String getVersion() {
        return "1.2.1";
    }
    
    protected void addCSSRules(final CSSParser.Ruleset ruleset) {
        this.cssRules.addAll(ruleset);
    }
    
    protected List<CSSParser.Rule> getCSSRules() {
        return this.cssRules.getRules();
    }
    
    public float getDocumentAspectRatio() {
        final float n = -1.0f;
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }
        final Length width = this.rootElement.width;
        final Length height = this.rootElement.height;
        float n2;
        if (width != null && height != null && width.unit != Unit.percent && height.unit != Unit.percent) {
            n2 = n;
            if (!width.isZero()) {
                if (height.isZero()) {
                    n2 = n;
                }
                else {
                    n2 = width.floatValue(this.renderDPI) / height.floatValue(this.renderDPI);
                }
            }
        }
        else {
            n2 = n;
            if (this.rootElement.viewBox != null) {
                n2 = n;
                if (this.rootElement.viewBox.width != 0.0f) {
                    n2 = n;
                    if (this.rootElement.viewBox.height != 0.0f) {
                        n2 = this.rootElement.viewBox.width / this.rootElement.viewBox.height;
                    }
                }
            }
        }
        return n2;
    }
    
    public String getDocumentDescription() {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }
        return this.desc;
    }
    
    public float getDocumentHeight() {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }
        return this.getDocumentDimensions(this.renderDPI).height;
    }
    
    public PreserveAspectRatio getDocumentPreserveAspectRatio() {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }
        PreserveAspectRatio preserveAspectRatio;
        if (this.rootElement.preserveAspectRatio == null) {
            preserveAspectRatio = null;
        }
        else {
            preserveAspectRatio = this.rootElement.preserveAspectRatio;
        }
        return preserveAspectRatio;
    }
    
    public String getDocumentSVGVersion() {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }
        return this.rootElement.version;
    }
    
    public String getDocumentTitle() {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }
        return this.title;
    }
    
    public RectF getDocumentViewBox() {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }
        RectF rectF;
        if (this.rootElement.viewBox == null) {
            rectF = null;
        }
        else {
            rectF = this.rootElement.viewBox.toRectF();
        }
        return rectF;
    }
    
    public float getDocumentWidth() {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }
        return this.getDocumentDimensions(this.renderDPI).width;
    }
    
    protected SvgObject getElementById(final String s) {
        SvgObject svgObject;
        if (s.equals(this.rootElement.id)) {
            svgObject = this.rootElement;
        }
        else {
            svgObject = this.getElementById((SvgContainer)this.rootElement, s);
        }
        return svgObject;
    }
    
    protected List<SvgObject> getElementsByTagName(final Class clazz) {
        return this.getElementsByTagName((SvgContainer)this.rootElement, clazz);
    }
    
    protected SVGExternalFileResolver getFileResolver() {
        return this.fileResolver;
    }
    
    public float getRenderDPI() {
        return this.renderDPI;
    }
    
    protected Svg getRootElement() {
        return this.rootElement;
    }
    
    public Set<String> getViewList() {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }
        final List<SvgObject> elementsByTagName = this.getElementsByTagName(View.class);
        final HashSet set = new HashSet<String>(elementsByTagName.size());
        for (final View view : elementsByTagName) {
            if (view.id != null) {
                set.add(view.id);
            }
            else {
                Log.w("AndroidSVG", "getViewList(): found a <view> without an id attribute");
            }
        }
        return (Set<String>)set;
    }
    
    protected boolean hasCSSRules() {
        return !this.cssRules.isEmpty();
    }
    
    public void registerExternalFileResolver(final SVGExternalFileResolver fileResolver) {
        this.fileResolver = fileResolver;
    }
    
    public void renderToCanvas(final Canvas canvas) {
        this.renderToCanvas(canvas, null);
    }
    
    public void renderToCanvas(final Canvas canvas, final RectF rectF) {
        Box fromLimits;
        if (rectF != null) {
            fromLimits = Box.fromLimits(rectF.left, rectF.top, rectF.right, rectF.bottom);
        }
        else {
            fromLimits = new Box(0.0f, 0.0f, (float)canvas.getWidth(), (float)canvas.getHeight());
        }
        new SVGAndroidRenderer(canvas, fromLimits, this.renderDPI).renderDocument(this, null, null, true);
    }
    
    public Picture renderToPicture() {
        final Length width = this.rootElement.width;
        Picture picture;
        if (width != null) {
            final float floatValue = width.floatValue(this.renderDPI);
            final Box viewBox = this.rootElement.viewBox;
            float floatValue2;
            if (viewBox != null) {
                floatValue2 = viewBox.height * floatValue / viewBox.width;
            }
            else {
                final Length height = this.rootElement.height;
                if (height != null) {
                    floatValue2 = height.floatValue(this.renderDPI);
                }
                else {
                    floatValue2 = floatValue;
                }
            }
            picture = this.renderToPicture((int)Math.ceil(floatValue), (int)Math.ceil(floatValue2));
        }
        else {
            picture = this.renderToPicture(512, 512);
        }
        return picture;
    }
    
    public Picture renderToPicture(final int n, final int n2) {
        final Picture picture = new Picture();
        new SVGAndroidRenderer(picture.beginRecording(n, n2), new Box(0.0f, 0.0f, (float)n, (float)n2), this.renderDPI).renderDocument(this, null, null, false);
        picture.endRecording();
        return picture;
    }
    
    public void renderViewToCanvas(final String s, final Canvas canvas) {
        this.renderViewToCanvas(s, canvas, null);
    }
    
    public void renderViewToCanvas(final String s, final Canvas canvas, final RectF rectF) {
        final SvgObject elementById = this.getElementById(s);
        if (elementById != null && elementById instanceof View) {
            final View view = (View)elementById;
            if (view.viewBox == null) {
                Log.w("AndroidSVG", "View element is missing a viewBox attribute.");
            }
            else {
                Box fromLimits;
                if (rectF != null) {
                    fromLimits = Box.fromLimits(rectF.left, rectF.top, rectF.right, rectF.bottom);
                }
                else {
                    fromLimits = new Box(0.0f, 0.0f, (float)canvas.getWidth(), (float)canvas.getHeight());
                }
                new SVGAndroidRenderer(canvas, fromLimits, this.renderDPI).renderDocument(this, view.viewBox, view.preserveAspectRatio, true);
            }
        }
    }
    
    public Picture renderViewToPicture(final String s, final int n, final int n2) {
        final Picture picture = null;
        final SvgObject elementById = this.getElementById(s);
        Picture picture2;
        if (elementById == null) {
            picture2 = picture;
        }
        else {
            picture2 = picture;
            if (elementById instanceof View) {
                final View view = (View)elementById;
                if (view.viewBox == null) {
                    Log.w("AndroidSVG", "View element is missing a viewBox attribute.");
                    picture2 = picture;
                }
                else {
                    picture2 = new Picture();
                    new SVGAndroidRenderer(picture2.beginRecording(n, n2), new Box(0.0f, 0.0f, (float)n, (float)n2), this.renderDPI).renderDocument(this, view.viewBox, view.preserveAspectRatio, false);
                    picture2.endRecording();
                }
            }
        }
        return picture2;
    }
    
    protected SvgObject resolveIRI(final String s) {
        final SvgObject svgObject = null;
        SvgObject elementById;
        if (s == null) {
            elementById = svgObject;
        }
        else {
            elementById = svgObject;
            if (s.length() > 1) {
                elementById = svgObject;
                if (s.startsWith("#")) {
                    elementById = this.getElementById(s.substring(1));
                }
            }
        }
        return elementById;
    }
    
    protected void setDesc(final String desc) {
        this.desc = desc;
    }
    
    public void setDocumentHeight(final float n) {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }
        this.rootElement.height = new Length(n);
    }
    
    public void setDocumentHeight(final String s) throws SVGParseException {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }
        try {
            this.rootElement.height = SVGParser.parseLength(s);
        }
        catch (SAXException ex) {
            throw new SVGParseException(ex.getMessage());
        }
    }
    
    public void setDocumentPreserveAspectRatio(final PreserveAspectRatio preserveAspectRatio) {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }
        this.rootElement.preserveAspectRatio = preserveAspectRatio;
    }
    
    public void setDocumentViewBox(final float n, final float n2, final float n3, final float n4) {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }
        this.rootElement.viewBox = new Box(n, n2, n3, n4);
    }
    
    public void setDocumentWidth(final float n) {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }
        this.rootElement.width = new Length(n);
    }
    
    public void setDocumentWidth(final String s) throws SVGParseException {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }
        try {
            this.rootElement.width = SVGParser.parseLength(s);
        }
        catch (SAXException ex) {
            throw new SVGParseException(ex.getMessage());
        }
    }
    
    public void setRenderDPI(final float renderDPI) {
        this.renderDPI = renderDPI;
    }
    
    protected void setRootElement(final Svg rootElement) {
        this.rootElement = rootElement;
    }
    
    protected void setTitle(final String title) {
        this.title = title;
    }
    
    protected static class Box implements Cloneable
    {
        public float height;
        public float minX;
        public float minY;
        public float width;
        
        public Box(final float minX, final float minY, final float width, final float height) {
            this.minX = minX;
            this.minY = minY;
            this.width = width;
            this.height = height;
        }
        
        public static Box fromLimits(final float n, final float n2, final float n3, final float n4) {
            return new Box(n, n2, n3 - n, n4 - n2);
        }
        
        public float maxX() {
            return this.minX + this.width;
        }
        
        public float maxY() {
            return this.minY + this.height;
        }
        
        public RectF toRectF() {
            return new RectF(this.minX, this.minY, this.maxX(), this.maxY());
        }
        
        @Override
        public String toString() {
            return "[" + this.minX + " " + this.minY + " " + this.width + " " + this.height + "]";
        }
        
        public void union(final Box box) {
            if (box.minX < this.minX) {
                this.minX = box.minX;
            }
            if (box.minY < this.minY) {
                this.minY = box.minY;
            }
            if (box.maxX() > this.maxX()) {
                this.width = box.maxX() - this.minX;
            }
            if (box.maxY() > this.maxY()) {
                this.height = box.maxY() - this.minY;
            }
        }
    }
    
    protected static class CSSClipRect
    {
        public Length bottom;
        public Length left;
        public Length right;
        public Length top;
        
        public CSSClipRect(final Length top, final Length right, final Length bottom, final Length left) {
            this.top = top;
            this.right = right;
            this.bottom = bottom;
            this.left = left;
        }
    }
    
    protected static class Circle extends GraphicsElement
    {
        public Length cx;
        public Length cy;
        public Length r;
    }
    
    protected static class ClipPath extends Group implements NotDirectlyRendered
    {
        public Boolean clipPathUnitsAreUser;
    }
    
    protected static class Colour extends SvgPaint
    {
        public static final Colour BLACK;
        public int colour;
        
        static {
            BLACK = new Colour(0);
        }
        
        public Colour(final int colour) {
            this.colour = colour;
        }
        
        @Override
        public String toString() {
            return String.format("#%06x", this.colour);
        }
    }
    
    protected static class CurrentColor extends SvgPaint
    {
        private static CurrentColor instance;
        
        static {
            CurrentColor.instance = new CurrentColor();
        }
        
        private CurrentColor() {
        }
        
        public static CurrentColor getInstance() {
            return CurrentColor.instance;
        }
    }
    
    protected static class Defs extends Group implements NotDirectlyRendered
    {
    }
    
    protected static class Ellipse extends GraphicsElement
    {
        public Length cx;
        public Length cy;
        public Length rx;
        public Length ry;
    }
    
    protected static class GradientElement extends SvgElementBase implements SvgContainer
    {
        public List<SvgObject> children;
        public Matrix gradientTransform;
        public Boolean gradientUnitsAreUser;
        public String href;
        public GradientSpread spreadMethod;
        
        protected GradientElement() {
            this.children = new ArrayList<SvgObject>();
        }
        
        @Override
        public void addChild(final SvgObject obj) throws SAXException {
            if (obj instanceof Stop) {
                this.children.add(obj);
                return;
            }
            throw new SAXException("Gradient elements cannot contain " + obj + " elements.");
        }
        
        @Override
        public List<SvgObject> getChildren() {
            return this.children;
        }
    }
    
    protected enum GradientSpread
    {
        pad("pad", 0), 
        reflect("reflect", 1), 
        repeat("repeat", 2);
        
        private GradientSpread(final String name, final int ordinal) {
        }
    }
    
    protected abstract static class GraphicsElement extends SvgConditionalElement implements HasTransform
    {
        public Matrix transform;
        
        @Override
        public void setTransform(final Matrix transform) {
            this.transform = transform;
        }
    }
    
    protected static class Group extends SvgConditionalContainer implements HasTransform
    {
        public Matrix transform;
        
        @Override
        public void setTransform(final Matrix transform) {
            this.transform = transform;
        }
    }
    
    protected interface HasTransform
    {
        void setTransform(final Matrix p0);
    }
    
    protected static class Image extends SvgPreserveAspectRatioContainer implements HasTransform
    {
        public Length height;
        public String href;
        public Matrix transform;
        public Length width;
        public Length x;
        public Length y;
        
        @Override
        public void setTransform(final Matrix transform) {
            this.transform = transform;
        }
    }
    
    protected static class Length implements Cloneable
    {
        Unit unit;
        float value;
        
        static /* synthetic */ int[] $SWITCH_TABLE$com$caverock$androidsvg$SVG$Unit() {
            int[] $switch_TABLE$com$caverock$androidsvg$SVG$Unit = Length.$SWITCH_TABLE$com$caverock$androidsvg$SVG$Unit;
            if ($switch_TABLE$com$caverock$androidsvg$SVG$Unit == null) {
                $switch_TABLE$com$caverock$androidsvg$SVG$Unit = new int[Unit.values().length];
                while (true) {
                    try {
                        $switch_TABLE$com$caverock$androidsvg$SVG$Unit[Unit.cm.ordinal()] = 5;
                        try {
                            $switch_TABLE$com$caverock$androidsvg$SVG$Unit[Unit.em.ordinal()] = 2;
                            try {
                                $switch_TABLE$com$caverock$androidsvg$SVG$Unit[Unit.ex.ordinal()] = 3;
                                try {
                                    $switch_TABLE$com$caverock$androidsvg$SVG$Unit[Unit.in.ordinal()] = 4;
                                    try {
                                        $switch_TABLE$com$caverock$androidsvg$SVG$Unit[Unit.mm.ordinal()] = 6;
                                        try {
                                            $switch_TABLE$com$caverock$androidsvg$SVG$Unit[Unit.pc.ordinal()] = 8;
                                            try {
                                                $switch_TABLE$com$caverock$androidsvg$SVG$Unit[Unit.percent.ordinal()] = 9;
                                                try {
                                                    $switch_TABLE$com$caverock$androidsvg$SVG$Unit[Unit.pt.ordinal()] = 7;
                                                    try {
                                                        $switch_TABLE$com$caverock$androidsvg$SVG$Unit[Unit.px.ordinal()] = 1;
                                                        Length.$SWITCH_TABLE$com$caverock$androidsvg$SVG$Unit = $switch_TABLE$com$caverock$androidsvg$SVG$Unit;
                                                    }
                                                    catch (NoSuchFieldError noSuchFieldError) {}
                                                }
                                                catch (NoSuchFieldError noSuchFieldError2) {}
                                            }
                                            catch (NoSuchFieldError noSuchFieldError3) {}
                                        }
                                        catch (NoSuchFieldError noSuchFieldError4) {}
                                    }
                                    catch (NoSuchFieldError noSuchFieldError5) {}
                                }
                                catch (NoSuchFieldError noSuchFieldError6) {}
                            }
                            catch (NoSuchFieldError noSuchFieldError7) {}
                        }
                        catch (NoSuchFieldError noSuchFieldError8) {}
                    }
                    catch (NoSuchFieldError noSuchFieldError9) {
                        continue;
                    }
                    break;
                }
            }
            return $switch_TABLE$com$caverock$androidsvg$SVG$Unit;
        }
        
        public Length(final float value) {
            this.value = 0.0f;
            this.unit = Unit.px;
            this.value = value;
            this.unit = Unit.px;
        }
        
        public Length(final float value, final Unit unit) {
            this.value = 0.0f;
            this.unit = Unit.px;
            this.value = value;
            this.unit = unit;
        }
        
        public float floatValue() {
            return this.value;
        }
        
        public float floatValue(float n) {
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVG$Unit()[this.unit.ordinal()]) {
                default: {
                    n = this.value;
                    break;
                }
                case 1: {
                    n = this.value;
                    break;
                }
                case 4: {
                    n *= this.value;
                    break;
                }
                case 5: {
                    n = this.value * n / 2.54f;
                    break;
                }
                case 6: {
                    n = this.value * n / 25.4f;
                    break;
                }
                case 7: {
                    n = this.value * n / 72.0f;
                    break;
                }
                case 8: {
                    n = this.value * n / 6.0f;
                    break;
                }
            }
            return n;
        }
        
        public float floatValue(final SVGAndroidRenderer svgAndroidRenderer) {
            float n;
            if (this.unit == Unit.percent) {
                final Box currentViewPortInUserUnits = svgAndroidRenderer.getCurrentViewPortInUserUnits();
                if (currentViewPortInUserUnits == null) {
                    n = this.value;
                }
                else {
                    final float width = currentViewPortInUserUnits.width;
                    final float height = currentViewPortInUserUnits.height;
                    if (width == height) {
                        n = this.value * width / 100.0f;
                    }
                    else {
                        n = this.value * (float)(Math.sqrt(width * width + height * height) / 1.414213562373095) / 100.0f;
                    }
                }
            }
            else {
                n = this.floatValueX(svgAndroidRenderer);
            }
            return n;
        }
        
        public float floatValue(final SVGAndroidRenderer svgAndroidRenderer, float floatValueX) {
            if (this.unit == Unit.percent) {
                floatValueX = this.value * floatValueX / 100.0f;
            }
            else {
                floatValueX = this.floatValueX(svgAndroidRenderer);
            }
            return floatValueX;
        }
        
        public float floatValueX(final SVGAndroidRenderer svgAndroidRenderer) {
            float n = 0.0f;
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVG$Unit()[this.unit.ordinal()]) {
                default: {
                    n = this.value;
                    break;
                }
                case 1: {
                    n = this.value;
                    break;
                }
                case 2: {
                    n = this.value * svgAndroidRenderer.getCurrentFontSize();
                    break;
                }
                case 3: {
                    n = this.value * svgAndroidRenderer.getCurrentFontXHeight();
                    break;
                }
                case 4: {
                    n = this.value * svgAndroidRenderer.getDPI();
                    break;
                }
                case 5: {
                    n = this.value * svgAndroidRenderer.getDPI() / 2.54f;
                    break;
                }
                case 6: {
                    n = this.value * svgAndroidRenderer.getDPI() / 25.4f;
                    break;
                }
                case 7: {
                    n = this.value * svgAndroidRenderer.getDPI() / 72.0f;
                    break;
                }
                case 8: {
                    n = this.value * svgAndroidRenderer.getDPI() / 6.0f;
                    break;
                }
                case 9: {
                    final Box currentViewPortInUserUnits = svgAndroidRenderer.getCurrentViewPortInUserUnits();
                    if (currentViewPortInUserUnits == null) {
                        n = this.value;
                        break;
                    }
                    n = this.value * currentViewPortInUserUnits.width / 100.0f;
                    break;
                }
            }
            return n;
        }
        
        public float floatValueY(final SVGAndroidRenderer svgAndroidRenderer) {
            float n;
            if (this.unit == Unit.percent) {
                final Box currentViewPortInUserUnits = svgAndroidRenderer.getCurrentViewPortInUserUnits();
                if (currentViewPortInUserUnits == null) {
                    n = this.value;
                }
                else {
                    n = this.value * currentViewPortInUserUnits.height / 100.0f;
                }
            }
            else {
                n = this.floatValueX(svgAndroidRenderer);
            }
            return n;
        }
        
        public boolean isNegative() {
            return this.value < 0.0f;
        }
        
        public boolean isZero() {
            return this.value == 0.0f;
        }
        
        @Override
        public String toString() {
            return String.valueOf(String.valueOf(this.value)) + this.unit;
        }
    }
    
    protected static class Line extends GraphicsElement
    {
        public Length x1;
        public Length x2;
        public Length y1;
        public Length y2;
    }
    
    protected static class Marker extends SvgViewBoxContainer implements NotDirectlyRendered
    {
        public Length markerHeight;
        public boolean markerUnitsAreUser;
        public Length markerWidth;
        public Float orient;
        public Length refX;
        public Length refY;
    }
    
    protected static class Mask extends SvgConditionalContainer implements NotDirectlyRendered
    {
        public Length height;
        public Boolean maskContentUnitsAreUser;
        public Boolean maskUnitsAreUser;
        public Length width;
        public Length x;
        public Length y;
    }
    
    protected interface NotDirectlyRendered
    {
    }
    
    protected static class PaintReference extends SvgPaint
    {
        public SvgPaint fallback;
        public String href;
        
        public PaintReference(final String href, final SvgPaint fallback) {
            this.href = href;
            this.fallback = fallback;
        }
        
        @Override
        public String toString() {
            return String.valueOf(this.href) + " " + this.fallback;
        }
    }
    
    protected static class Path extends GraphicsElement
    {
        public PathDefinition d;
        public Float pathLength;
    }
    
    protected static class PathDefinition implements PathInterface
    {
        private static final byte ARCTO = 4;
        private static final byte CLOSE = 8;
        private static final byte CUBICTO = 2;
        private static final byte LINETO = 1;
        private static final byte MOVETO = 0;
        private static final byte QUADTO = 3;
        private List<Byte> commands;
        private List<Float> coords;
        
        public PathDefinition() {
            this.commands = null;
            this.coords = null;
            this.commands = new ArrayList<Byte>();
            this.coords = new ArrayList<Float>();
        }
        
        @Override
        public void arcTo(final float f, final float f2, final float f3, final boolean b, final boolean b2, final float f4, final float f5) {
            boolean b3 = false;
            int n;
            if (b) {
                n = 2;
            }
            else {
                n = 0;
            }
            if (b2) {
                b3 = true;
            }
            this.commands.add((byte)(n | 0x4 | (b3 ? 1 : 0)));
            this.coords.add(f);
            this.coords.add(f2);
            this.coords.add(f3);
            this.coords.add(f4);
            this.coords.add(f5);
        }
        
        @Override
        public void close() {
            this.commands.add((Byte)8);
        }
        
        @Override
        public void cubicTo(final float f, final float f2, final float f3, final float f4, final float f5, final float f6) {
            this.commands.add((Byte)2);
            this.coords.add(f);
            this.coords.add(f2);
            this.coords.add(f3);
            this.coords.add(f4);
            this.coords.add(f5);
            this.coords.add(f6);
        }
        
        public void enumeratePath(final PathInterface pathInterface) {
            final Iterator<Float> iterator = this.coords.iterator();
            for (final byte byteValue : this.commands) {
                switch (byteValue) {
                    default: {
                        pathInterface.arcTo(iterator.next(), iterator.next(), iterator.next(), (byteValue & 0x2) != 0x0, (byteValue & 0x1) != 0x0, iterator.next(), iterator.next());
                        continue;
                    }
                    case 0: {
                        pathInterface.moveTo(iterator.next(), iterator.next());
                        continue;
                    }
                    case 1: {
                        pathInterface.lineTo(iterator.next(), iterator.next());
                        continue;
                    }
                    case 2: {
                        pathInterface.cubicTo(iterator.next(), iterator.next(), iterator.next(), iterator.next(), iterator.next(), iterator.next());
                        continue;
                    }
                    case 3: {
                        pathInterface.quadTo(iterator.next(), iterator.next(), iterator.next(), iterator.next());
                        continue;
                    }
                    case 8: {
                        pathInterface.close();
                        continue;
                    }
                }
            }
        }
        
        public boolean isEmpty() {
            return this.commands.isEmpty();
        }
        
        @Override
        public void lineTo(final float f, final float f2) {
            this.commands.add((Byte)1);
            this.coords.add(f);
            this.coords.add(f2);
        }
        
        @Override
        public void moveTo(final float f, final float f2) {
            this.commands.add((Byte)0);
            this.coords.add(f);
            this.coords.add(f2);
        }
        
        @Override
        public void quadTo(final float f, final float f2, final float f3, final float f4) {
            this.commands.add((Byte)3);
            this.coords.add(f);
            this.coords.add(f2);
            this.coords.add(f3);
            this.coords.add(f4);
        }
    }
    
    protected interface PathInterface
    {
        void arcTo(final float p0, final float p1, final float p2, final boolean p3, final boolean p4, final float p5, final float p6);
        
        void close();
        
        void cubicTo(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5);
        
        void lineTo(final float p0, final float p1);
        
        void moveTo(final float p0, final float p1);
        
        void quadTo(final float p0, final float p1, final float p2, final float p3);
    }
    
    protected static class Pattern extends SvgViewBoxContainer implements NotDirectlyRendered
    {
        public Length height;
        public String href;
        public Boolean patternContentUnitsAreUser;
        public Matrix patternTransform;
        public Boolean patternUnitsAreUser;
        public Length width;
        public Length x;
        public Length y;
    }
    
    protected static class PolyLine extends GraphicsElement
    {
        public float[] points;
    }
    
    protected static class Polygon extends PolyLine
    {
    }
    
    protected static class Rect extends GraphicsElement
    {
        public Length height;
        public Length rx;
        public Length ry;
        public Length width;
        public Length x;
        public Length y;
    }
    
    protected static class SolidColor extends SvgElementBase implements SvgContainer
    {
        public Length solidColor;
        public Length solidOpacity;
        
        @Override
        public void addChild(final SvgObject svgObject) throws SAXException {
        }
        
        @Override
        public List<SvgObject> getChildren() {
            return SVG.EMPTY_CHILD_LIST;
        }
    }
    
    protected static class Stop extends SvgElementBase implements SvgContainer
    {
        public Float offset;
        
        @Override
        public void addChild(final SvgObject svgObject) throws SAXException {
        }
        
        @Override
        public List<SvgObject> getChildren() {
            return SVG.EMPTY_CHILD_LIST;
        }
    }
    
    protected static class Style implements Cloneable
    {
        public static final int FONT_WEIGHT_BOLD = 700;
        public static final int FONT_WEIGHT_BOLDER = 1;
        public static final int FONT_WEIGHT_LIGHTER = -1;
        public static final int FONT_WEIGHT_NORMAL = 400;
        public CSSClipRect clip;
        public String clipPath;
        public FillRule clipRule;
        public Colour color;
        public TextDirection direction;
        public Boolean display;
        public SvgPaint fill;
        public Float fillOpacity;
        public FillRule fillRule;
        public List<String> fontFamily;
        public Length fontSize;
        public FontStyle fontStyle;
        public Integer fontWeight;
        public String markerEnd;
        public String markerMid;
        public String markerStart;
        public String mask;
        public Float opacity;
        public Boolean overflow;
        public SvgPaint solidColor;
        public Float solidOpacity;
        public long specifiedFlags;
        public SvgPaint stopColor;
        public Float stopOpacity;
        public SvgPaint stroke;
        public Length[] strokeDashArray;
        public Length strokeDashOffset;
        public LineCaps strokeLineCap;
        public LineJoin strokeLineJoin;
        public Float strokeMiterLimit;
        public Float strokeOpacity;
        public Length strokeWidth;
        public TextAnchor textAnchor;
        public TextDecoration textDecoration;
        public VectorEffect vectorEffect;
        public SvgPaint viewportFill;
        public Float viewportFillOpacity;
        public Boolean visibility;
        
        protected Style() {
            this.specifiedFlags = 0L;
        }
        
        public static Style getDefaultStyle() {
            final Style style = new Style();
            style.specifiedFlags = -1L;
            style.fill = Colour.BLACK;
            style.fillRule = FillRule.NonZero;
            style.fillOpacity = 1.0f;
            style.stroke = null;
            style.strokeOpacity = 1.0f;
            style.strokeWidth = new Length(1.0f);
            style.strokeLineCap = LineCaps.Butt;
            style.strokeLineJoin = LineJoin.Miter;
            style.strokeMiterLimit = 4.0f;
            style.strokeDashArray = null;
            style.strokeDashOffset = new Length(0.0f);
            style.opacity = 1.0f;
            style.color = Colour.BLACK;
            style.fontFamily = null;
            style.fontSize = new Length(12.0f, Unit.pt);
            style.fontWeight = 400;
            style.fontStyle = FontStyle.Normal;
            style.textDecoration = TextDecoration.None;
            style.direction = TextDirection.LTR;
            style.textAnchor = TextAnchor.Start;
            style.overflow = true;
            style.clip = null;
            style.markerStart = null;
            style.markerMid = null;
            style.markerEnd = null;
            style.display = Boolean.TRUE;
            style.visibility = Boolean.TRUE;
            style.stopColor = Colour.BLACK;
            style.stopOpacity = 1.0f;
            style.clipPath = null;
            style.clipRule = FillRule.NonZero;
            style.mask = null;
            style.solidColor = null;
            style.solidOpacity = 1.0f;
            style.viewportFill = null;
            style.viewportFillOpacity = 1.0f;
            style.vectorEffect = VectorEffect.None;
            return style;
        }
        
        @Override
        protected Object clone() {
            try {
                final Style style = (Style)super.clone();
                if (this.strokeDashArray != null) {
                    style.strokeDashArray = this.strokeDashArray.clone();
                }
                return style;
            }
            catch (CloneNotSupportedException ex) {
                throw new InternalError(ex.toString());
            }
        }
        
        public void resetNonInheritingProperties() {
            this.resetNonInheritingProperties(false);
        }
        
        public void resetNonInheritingProperties(final boolean b) {
            this.display = Boolean.TRUE;
            Boolean overflow;
            if (b) {
                overflow = Boolean.TRUE;
            }
            else {
                overflow = Boolean.FALSE;
            }
            this.overflow = overflow;
            this.clip = null;
            this.clipPath = null;
            this.opacity = 1.0f;
            this.stopColor = Colour.BLACK;
            this.stopOpacity = 1.0f;
            this.mask = null;
            this.solidColor = null;
            this.solidOpacity = 1.0f;
            this.viewportFill = null;
            this.viewportFillOpacity = 1.0f;
            this.vectorEffect = VectorEffect.None;
        }
        
        public enum FillRule
        {
            EvenOdd("EvenOdd", 1), 
            NonZero("NonZero", 0);
            
            private FillRule(final String name, final int ordinal) {
            }
        }
        
        public enum FontStyle
        {
            Italic("Italic", 1), 
            Normal("Normal", 0), 
            Oblique("Oblique", 2);
            
            private FontStyle(final String name, final int ordinal) {
            }
        }
        
        public enum LineCaps
        {
            Butt("Butt", 0), 
            Round("Round", 1), 
            Square("Square", 2);
            
            private LineCaps(final String name, final int ordinal) {
            }
        }
        
        public enum LineJoin
        {
            Bevel("Bevel", 2), 
            Miter("Miter", 0), 
            Round("Round", 1);
            
            private LineJoin(final String name, final int ordinal) {
            }
        }
        
        public enum TextAnchor
        {
            End("End", 2), 
            Middle("Middle", 1), 
            Start("Start", 0);
            
            private TextAnchor(final String name, final int ordinal) {
            }
        }
        
        public enum TextDecoration
        {
            Blink("Blink", 4), 
            LineThrough("LineThrough", 3), 
            None("None", 0), 
            Overline("Overline", 2), 
            Underline("Underline", 1);
            
            private TextDecoration(final String name, final int ordinal) {
            }
        }
        
        public enum TextDirection
        {
            LTR("LTR", 0), 
            RTL("RTL", 1);
            
            private TextDirection(final String name, final int ordinal) {
            }
        }
        
        public enum VectorEffect
        {
            NonScalingStroke("NonScalingStroke", 1), 
            None("None", 0);
            
            private VectorEffect(final String name, final int ordinal) {
            }
        }
    }
    
    protected static class Svg extends SvgViewBoxContainer
    {
        public Length height;
        public String version;
        public Length width;
        public Length x;
        public Length y;
    }
    
    protected interface SvgConditional
    {
        String getRequiredExtensions();
        
        Set<String> getRequiredFeatures();
        
        Set<String> getRequiredFonts();
        
        Set<String> getRequiredFormats();
        
        Set<String> getSystemLanguage();
        
        void setRequiredExtensions(final String p0);
        
        void setRequiredFeatures(final Set<String> p0);
        
        void setRequiredFonts(final Set<String> p0);
        
        void setRequiredFormats(final Set<String> p0);
        
        void setSystemLanguage(final Set<String> p0);
    }
    
    protected static class SvgConditionalContainer extends SvgElement implements SvgContainer, SvgConditional
    {
        public List<SvgObject> children;
        public String requiredExtensions;
        public Set<String> requiredFeatures;
        public Set<String> requiredFonts;
        public Set<String> requiredFormats;
        public Set<String> systemLanguage;
        
        protected SvgConditionalContainer() {
            this.children = new ArrayList<SvgObject>();
            this.requiredFeatures = null;
            this.requiredExtensions = null;
            this.systemLanguage = null;
            this.requiredFormats = null;
            this.requiredFonts = null;
        }
        
        @Override
        public void addChild(final SvgObject svgObject) throws SAXException {
            this.children.add(svgObject);
        }
        
        @Override
        public List<SvgObject> getChildren() {
            return this.children;
        }
        
        @Override
        public String getRequiredExtensions() {
            return this.requiredExtensions;
        }
        
        @Override
        public Set<String> getRequiredFeatures() {
            return this.requiredFeatures;
        }
        
        @Override
        public Set<String> getRequiredFonts() {
            return this.requiredFonts;
        }
        
        @Override
        public Set<String> getRequiredFormats() {
            return this.requiredFormats;
        }
        
        @Override
        public Set<String> getSystemLanguage() {
            return null;
        }
        
        @Override
        public void setRequiredExtensions(final String requiredExtensions) {
            this.requiredExtensions = requiredExtensions;
        }
        
        @Override
        public void setRequiredFeatures(final Set<String> requiredFeatures) {
            this.requiredFeatures = requiredFeatures;
        }
        
        @Override
        public void setRequiredFonts(final Set<String> requiredFonts) {
            this.requiredFonts = requiredFonts;
        }
        
        @Override
        public void setRequiredFormats(final Set<String> requiredFormats) {
            this.requiredFormats = requiredFormats;
        }
        
        @Override
        public void setSystemLanguage(final Set<String> systemLanguage) {
            this.systemLanguage = systemLanguage;
        }
    }
    
    protected static class SvgConditionalElement extends SvgElement implements SvgConditional
    {
        public String requiredExtensions;
        public Set<String> requiredFeatures;
        public Set<String> requiredFonts;
        public Set<String> requiredFormats;
        public Set<String> systemLanguage;
        
        protected SvgConditionalElement() {
            this.requiredFeatures = null;
            this.requiredExtensions = null;
            this.systemLanguage = null;
            this.requiredFormats = null;
            this.requiredFonts = null;
        }
        
        @Override
        public String getRequiredExtensions() {
            return this.requiredExtensions;
        }
        
        @Override
        public Set<String> getRequiredFeatures() {
            return this.requiredFeatures;
        }
        
        @Override
        public Set<String> getRequiredFonts() {
            return this.requiredFonts;
        }
        
        @Override
        public Set<String> getRequiredFormats() {
            return this.requiredFormats;
        }
        
        @Override
        public Set<String> getSystemLanguage() {
            return this.systemLanguage;
        }
        
        @Override
        public void setRequiredExtensions(final String requiredExtensions) {
            this.requiredExtensions = requiredExtensions;
        }
        
        @Override
        public void setRequiredFeatures(final Set<String> requiredFeatures) {
            this.requiredFeatures = requiredFeatures;
        }
        
        @Override
        public void setRequiredFonts(final Set<String> requiredFonts) {
            this.requiredFonts = requiredFonts;
        }
        
        @Override
        public void setRequiredFormats(final Set<String> requiredFormats) {
            this.requiredFormats = requiredFormats;
        }
        
        @Override
        public void setSystemLanguage(final Set<String> systemLanguage) {
            this.systemLanguage = systemLanguage;
        }
    }
    
    protected interface SvgContainer
    {
        void addChild(final SvgObject p0) throws SAXException;
        
        List<SvgObject> getChildren();
    }
    
    protected static class SvgElement extends SvgElementBase
    {
        public Box boundingBox;
        
        protected SvgElement() {
            this.boundingBox = null;
        }
    }
    
    protected static class SvgElementBase extends SvgObject
    {
        public Style baseStyle;
        public List<String> classNames;
        public String id;
        public Boolean spacePreserve;
        public Style style;
        
        protected SvgElementBase() {
            this.id = null;
            this.spacePreserve = null;
            this.baseStyle = null;
            this.style = null;
            this.classNames = null;
        }
    }
    
    protected static class SvgLinearGradient extends GradientElement
    {
        public Length x1;
        public Length x2;
        public Length y1;
        public Length y2;
    }
    
    protected static class SvgObject
    {
        public SVG document;
        public SvgContainer parent;
        
        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }
    
    protected abstract static class SvgPaint implements Cloneable
    {
    }
    
    protected static class SvgPreserveAspectRatioContainer extends SvgConditionalContainer
    {
        public PreserveAspectRatio preserveAspectRatio;
        
        protected SvgPreserveAspectRatioContainer() {
            this.preserveAspectRatio = null;
        }
    }
    
    protected static class SvgRadialGradient extends GradientElement
    {
        public Length cx;
        public Length cy;
        public Length fx;
        public Length fy;
        public Length r;
    }
    
    protected static class SvgViewBoxContainer extends SvgPreserveAspectRatioContainer
    {
        public Box viewBox;
    }
    
    protected static class Switch extends Group
    {
    }
    
    protected static class Symbol extends SvgViewBoxContainer implements NotDirectlyRendered
    {
    }
    
    protected static class TRef extends TextContainer implements TextChild
    {
        public String href;
        private TextRoot textRoot;
        
        @Override
        public TextRoot getTextRoot() {
            return this.textRoot;
        }
        
        @Override
        public void setTextRoot(final TextRoot textRoot) {
            this.textRoot = textRoot;
        }
    }
    
    protected static class TSpan extends TextPositionedContainer implements TextChild
    {
        private TextRoot textRoot;
        
        @Override
        public TextRoot getTextRoot() {
            return this.textRoot;
        }
        
        @Override
        public void setTextRoot(final TextRoot textRoot) {
            this.textRoot = textRoot;
        }
    }
    
    protected static class Text extends TextPositionedContainer implements TextRoot, HasTransform
    {
        public Matrix transform;
        
        @Override
        public void setTransform(final Matrix transform) {
            this.transform = transform;
        }
    }
    
    protected interface TextChild
    {
        TextRoot getTextRoot();
        
        void setTextRoot(final TextRoot p0);
    }
    
    protected static class TextContainer extends SvgConditionalContainer
    {
        @Override
        public void addChild(final SvgObject obj) throws SAXException {
            if (obj instanceof TextChild) {
                this.children.add(obj);
                return;
            }
            throw new SAXException("Text content elements cannot contain " + obj + " elements.");
        }
    }
    
    protected static class TextPath extends TextContainer implements TextChild
    {
        public String href;
        public Length startOffset;
        private TextRoot textRoot;
        
        @Override
        public TextRoot getTextRoot() {
            return this.textRoot;
        }
        
        @Override
        public void setTextRoot(final TextRoot textRoot) {
            this.textRoot = textRoot;
        }
    }
    
    protected static class TextPositionedContainer extends TextContainer
    {
        public List<Length> dx;
        public List<Length> dy;
        public List<Length> x;
        public List<Length> y;
    }
    
    protected interface TextRoot
    {
    }
    
    protected static class TextSequence extends SvgObject implements TextChild
    {
        public String text;
        private TextRoot textRoot;
        
        public TextSequence(final String text) {
            this.text = text;
        }
        
        @Override
        public TextRoot getTextRoot() {
            return this.textRoot;
        }
        
        @Override
        public void setTextRoot(final TextRoot textRoot) {
            this.textRoot = textRoot;
        }
        
        @Override
        public String toString() {
            return String.valueOf(this.getClass().getSimpleName()) + " '" + this.text + "'";
        }
    }
    
    protected enum Unit
    {
        cm("cm", 4), 
        em("em", 1), 
        ex("ex", 2), 
        in("in", 3), 
        mm("mm", 5), 
        pc("pc", 7), 
        percent("percent", 8), 
        pt("pt", 6), 
        px("px", 0);
        
        private Unit(final String name, final int ordinal) {
        }
    }
    
    protected static class Use extends Group
    {
        public Length height;
        public String href;
        public Length width;
        public Length x;
        public Length y;
    }
    
    protected static class View extends SvgViewBoxContainer implements NotDirectlyRendered
    {
    }
}
