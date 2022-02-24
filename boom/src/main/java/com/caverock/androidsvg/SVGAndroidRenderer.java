// 
// Decompiled by Procyon v0.5.36
// 

package com.caverock.androidsvg;

import android.graphics.Rect;
import android.graphics.Paint$Style;
import android.os.Build$VERSION;
import android.graphics.DashPathEffect;
import android.graphics.PathEffect;
import android.graphics.Paint$Join;
import android.graphics.Paint$Cap;
import android.graphics.PathMeasure;
import java.util.Set;
import java.util.Collection;
import java.util.Locale;
import android.graphics.RadialGradient;
import android.graphics.Paint;
import android.graphics.LinearGradient;
import android.graphics.Shader$TileMode;
import android.graphics.Path$FillType;
import android.util.Log;
import android.graphics.Bitmap$Config;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.util.Iterator;
import android.graphics.RectF;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Path;
import android.graphics.Matrix;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import java.util.Stack;

public class SVGAndroidRenderer
{
    private static final float BEZIER_ARC_FACTOR = 0.5522848f;
    private static final String DEFAULT_FONT_FAMILY = "sans-serif";
    private static final int LUMINANCE_FACTOR_SHIFT = 15;
    private static final int LUMINANCE_TO_ALPHA_BLUE = 2362;
    private static final int LUMINANCE_TO_ALPHA_GREEN = 23442;
    private static final int LUMINANCE_TO_ALPHA_RED = 6963;
    private static final String TAG = "SVGAndroidRenderer";
    private Stack<Bitmap> bitmapStack;
    private Canvas canvas;
    private Stack<Canvas> canvasStack;
    private SVG.Box canvasViewPort;
    private boolean directRenderingMode;
    private SVG document;
    private float dpi;
    private Stack<Matrix> matrixStack;
    private Stack<SVG.SvgContainer> parentStack;
    private RendererState state;
    private Stack<RendererState> stateStack;
    
    static /* synthetic */ int[] $SWITCH_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment() {
        int[] $switch_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment = SVGAndroidRenderer.$SWITCH_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment;
        if ($switch_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment == null) {
            $switch_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment = new int[PreserveAspectRatio.Alignment.values().length];
            while (true) {
                try {
                    $switch_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment[PreserveAspectRatio.Alignment.None.ordinal()] = 1;
                    try {
                        $switch_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment[PreserveAspectRatio.Alignment.XMaxYMax.ordinal()] = 10;
                        try {
                            $switch_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment[PreserveAspectRatio.Alignment.XMaxYMid.ordinal()] = 7;
                            try {
                                $switch_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment[PreserveAspectRatio.Alignment.XMaxYMin.ordinal()] = 4;
                                try {
                                    $switch_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment[PreserveAspectRatio.Alignment.XMidYMax.ordinal()] = 9;
                                    try {
                                        $switch_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment[PreserveAspectRatio.Alignment.XMidYMid.ordinal()] = 6;
                                        try {
                                            $switch_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment[PreserveAspectRatio.Alignment.XMidYMin.ordinal()] = 3;
                                            try {
                                                $switch_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment[PreserveAspectRatio.Alignment.XMinYMax.ordinal()] = 8;
                                                try {
                                                    $switch_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment[PreserveAspectRatio.Alignment.XMinYMid.ordinal()] = 5;
                                                    try {
                                                        $switch_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment[PreserveAspectRatio.Alignment.XMinYMin.ordinal()] = 2;
                                                        SVGAndroidRenderer.$SWITCH_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment = $switch_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment;
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
                    catch (NoSuchFieldError noSuchFieldError9) {}
                }
                catch (NoSuchFieldError noSuchFieldError10) {
                    continue;
                }
                break;
            }
        }
        return $switch_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment;
    }
    
    static /* synthetic */ int[] $SWITCH_TABLE$com$caverock$androidsvg$SVG$Style$FillRule() {
        int[] $switch_TABLE$com$caverock$androidsvg$SVG$Style$FillRule = SVGAndroidRenderer.$SWITCH_TABLE$com$caverock$androidsvg$SVG$Style$FillRule;
        if ($switch_TABLE$com$caverock$androidsvg$SVG$Style$FillRule == null) {
            $switch_TABLE$com$caverock$androidsvg$SVG$Style$FillRule = new int[SVG.Style.FillRule.values().length];
            while (true) {
                try {
                    $switch_TABLE$com$caverock$androidsvg$SVG$Style$FillRule[SVG.Style.FillRule.EvenOdd.ordinal()] = 2;
                    try {
                        $switch_TABLE$com$caverock$androidsvg$SVG$Style$FillRule[SVG.Style.FillRule.NonZero.ordinal()] = 1;
                        SVGAndroidRenderer.$SWITCH_TABLE$com$caverock$androidsvg$SVG$Style$FillRule = $switch_TABLE$com$caverock$androidsvg$SVG$Style$FillRule;
                    }
                    catch (NoSuchFieldError noSuchFieldError) {}
                }
                catch (NoSuchFieldError noSuchFieldError2) {
                    continue;
                }
                break;
            }
        }
        return $switch_TABLE$com$caverock$androidsvg$SVG$Style$FillRule;
    }
    
    static /* synthetic */ int[] $SWITCH_TABLE$com$caverock$androidsvg$SVG$Style$LineCaps() {
        int[] $switch_TABLE$com$caverock$androidsvg$SVG$Style$LineCaps = SVGAndroidRenderer.$SWITCH_TABLE$com$caverock$androidsvg$SVG$Style$LineCaps;
        if ($switch_TABLE$com$caverock$androidsvg$SVG$Style$LineCaps == null) {
            $switch_TABLE$com$caverock$androidsvg$SVG$Style$LineCaps = new int[SVG.Style.LineCaps.values().length];
            while (true) {
                try {
                    $switch_TABLE$com$caverock$androidsvg$SVG$Style$LineCaps[SVG.Style.LineCaps.Butt.ordinal()] = 1;
                    try {
                        $switch_TABLE$com$caverock$androidsvg$SVG$Style$LineCaps[SVG.Style.LineCaps.Round.ordinal()] = 2;
                        try {
                            $switch_TABLE$com$caverock$androidsvg$SVG$Style$LineCaps[SVG.Style.LineCaps.Square.ordinal()] = 3;
                            SVGAndroidRenderer.$SWITCH_TABLE$com$caverock$androidsvg$SVG$Style$LineCaps = $switch_TABLE$com$caverock$androidsvg$SVG$Style$LineCaps;
                        }
                        catch (NoSuchFieldError noSuchFieldError) {}
                    }
                    catch (NoSuchFieldError noSuchFieldError2) {}
                }
                catch (NoSuchFieldError noSuchFieldError3) {
                    continue;
                }
                break;
            }
        }
        return $switch_TABLE$com$caverock$androidsvg$SVG$Style$LineCaps;
    }
    
    static /* synthetic */ int[] $SWITCH_TABLE$com$caverock$androidsvg$SVG$Style$LineJoin() {
        int[] $switch_TABLE$com$caverock$androidsvg$SVG$Style$LineJoin = SVGAndroidRenderer.$SWITCH_TABLE$com$caverock$androidsvg$SVG$Style$LineJoin;
        if ($switch_TABLE$com$caverock$androidsvg$SVG$Style$LineJoin == null) {
            $switch_TABLE$com$caverock$androidsvg$SVG$Style$LineJoin = new int[SVG.Style.LineJoin.values().length];
            while (true) {
                try {
                    $switch_TABLE$com$caverock$androidsvg$SVG$Style$LineJoin[SVG.Style.LineJoin.Bevel.ordinal()] = 3;
                    try {
                        $switch_TABLE$com$caverock$androidsvg$SVG$Style$LineJoin[SVG.Style.LineJoin.Miter.ordinal()] = 1;
                        try {
                            $switch_TABLE$com$caverock$androidsvg$SVG$Style$LineJoin[SVG.Style.LineJoin.Round.ordinal()] = 2;
                            SVGAndroidRenderer.$SWITCH_TABLE$com$caverock$androidsvg$SVG$Style$LineJoin = $switch_TABLE$com$caverock$androidsvg$SVG$Style$LineJoin;
                        }
                        catch (NoSuchFieldError noSuchFieldError) {}
                    }
                    catch (NoSuchFieldError noSuchFieldError2) {}
                }
                catch (NoSuchFieldError noSuchFieldError3) {
                    continue;
                }
                break;
            }
        }
        return $switch_TABLE$com$caverock$androidsvg$SVG$Style$LineJoin;
    }
    
    protected SVGAndroidRenderer(final Canvas canvas, final SVG.Box canvasViewPort, final float dpi) {
        this.canvas = canvas;
        this.dpi = dpi;
        this.canvasViewPort = canvasViewPort;
    }
    
    private void addObjectToClip(final SVG.GraphicsElement graphicsElement, final Path path, final Matrix matrix) {
        this.updateStyleForElement(this.state, graphicsElement);
        if (this.display() && this.visible()) {
            if (graphicsElement.transform != null) {
                matrix.preConcat(graphicsElement.transform);
            }
            Path path2;
            if (graphicsElement instanceof SVG.Rect) {
                path2 = this.makePathAndBoundingBox((SVG.Rect)graphicsElement);
            }
            else if (graphicsElement instanceof SVG.Circle) {
                path2 = this.makePathAndBoundingBox((SVG.Circle)graphicsElement);
            }
            else if (graphicsElement instanceof SVG.Ellipse) {
                path2 = this.makePathAndBoundingBox((SVG.Ellipse)graphicsElement);
            }
            else {
                if (!(graphicsElement instanceof SVG.PolyLine)) {
                    return;
                }
                path2 = this.makePathAndBoundingBox((SVG.PolyLine)graphicsElement);
            }
            this.checkForClipPath(graphicsElement);
            path.setFillType(path2.getFillType());
            path.addPath(path2, matrix);
        }
    }
    
    private void addObjectToClip(final SVG.Path path, final Path path2, final Matrix matrix) {
        this.updateStyleForElement(this.state, path);
        if (this.display() && this.visible()) {
            if (path.transform != null) {
                matrix.preConcat(path.transform);
            }
            final Path path3 = new PathConverter(path.d).getPath();
            if (path.boundingBox == null) {
                path.boundingBox = this.calculatePathBounds(path3);
            }
            this.checkForClipPath(path);
            path2.setFillType(this.getClipRuleFromState());
            path2.addPath(path3, matrix);
        }
    }
    
    private void addObjectToClip(final SVG.SvgObject svgObject, final boolean b, final Path path, final Matrix matrix) {
        if (this.display()) {
            this.clipStatePush();
            if (svgObject instanceof SVG.Use) {
                if (b) {
                    this.addObjectToClip((SVG.Use)svgObject, path, matrix);
                }
                else {
                    error("<use> elements inside a <clipPath> cannot reference another <use>", new Object[0]);
                }
            }
            else if (svgObject instanceof SVG.Path) {
                this.addObjectToClip((SVG.Path)svgObject, path, matrix);
            }
            else if (svgObject instanceof SVG.Text) {
                this.addObjectToClip((SVG.Text)svgObject, path, matrix);
            }
            else if (svgObject instanceof SVG.GraphicsElement) {
                this.addObjectToClip((SVG.GraphicsElement)svgObject, path, matrix);
            }
            else {
                error("Invalid %s element found in clipPath definition", svgObject.getClass().getSimpleName());
            }
            this.clipStatePop();
        }
    }
    
    private void addObjectToClip(final SVG.Text text, final Path path, final Matrix matrix) {
        this.updateStyleForElement(this.state, text);
        if (this.display()) {
            if (text.transform != null) {
                matrix.preConcat(text.transform);
            }
            float floatValueX;
            if (text.x == null || text.x.size() == 0) {
                floatValueX = 0.0f;
            }
            else {
                floatValueX = text.x.get(0).floatValueX(this);
            }
            float floatValueY;
            if (text.y == null || text.y.size() == 0) {
                floatValueY = 0.0f;
            }
            else {
                floatValueY = text.y.get(0).floatValueY(this);
            }
            float floatValueX2;
            if (text.dx == null || text.dx.size() == 0) {
                floatValueX2 = 0.0f;
            }
            else {
                floatValueX2 = text.dx.get(0).floatValueX(this);
            }
            float floatValueY2;
            if (text.dy == null || text.dy.size() == 0) {
                floatValueY2 = 0.0f;
            }
            else {
                floatValueY2 = text.dy.get(0).floatValueY(this);
            }
            float n = floatValueX;
            if (this.state.style.textAnchor != SVG.Style.TextAnchor.Start) {
                final float calculateTextWidth = this.calculateTextWidth(text);
                if (this.state.style.textAnchor == SVG.Style.TextAnchor.Middle) {
                    n = floatValueX - calculateTextWidth / 2.0f;
                }
                else {
                    n = floatValueX - calculateTextWidth;
                }
            }
            if (text.boundingBox == null) {
                final TextBoundsCalculator textBoundsCalculator = new TextBoundsCalculator(n, floatValueY);
                this.enumerateTextSpans(text, (TextProcessor)textBoundsCalculator);
                text.boundingBox = new SVG.Box(textBoundsCalculator.bbox.left, textBoundsCalculator.bbox.top, textBoundsCalculator.bbox.width(), textBoundsCalculator.bbox.height());
            }
            this.checkForClipPath(text);
            final Path path2 = new Path();
            this.enumerateTextSpans(text, (TextProcessor)new PlainTextToPath(n + floatValueX2, floatValueY + floatValueY2, path2));
            path.setFillType(this.getClipRuleFromState());
            path.addPath(path2, matrix);
        }
    }
    
    private void addObjectToClip(final SVG.Use use, final Path path, final Matrix matrix) {
        this.updateStyleForElement(this.state, use);
        if (this.display() && this.visible()) {
            if (use.transform != null) {
                matrix.preConcat(use.transform);
            }
            final SVG.SvgObject resolveIRI = use.document.resolveIRI(use.href);
            if (resolveIRI == null) {
                error("Use reference '%s' not found", use.href);
            }
            else {
                this.checkForClipPath(use);
                this.addObjectToClip(resolveIRI, false, path, matrix);
            }
        }
    }
    
    private static void arcTo(final float n, final float n2, float a, float a2, final float n3, final boolean b, final boolean b2, final float n4, final float n5, final SVG.PathInterface pathInterface) {
        if (n != n4 || n2 != n5) {
            if (a == 0.0f || a2 == 0.0f) {
                pathInterface.lineTo(n4, n5);
            }
            else {
                final float abs = Math.abs(a);
                final float abs2 = Math.abs(a2);
                a = (float)Math.toRadians(n3 % 360.0);
                final double cos = Math.cos(a);
                final double sin = Math.sin(a);
                final double n6 = (n - n4) / 2.0;
                final double n7 = (n2 - n5) / 2.0;
                final double n8 = cos * n6 + sin * n7;
                final double n9 = -sin * n6 + cos * n7;
                double n10 = abs * abs;
                double n11 = abs2 * abs2;
                final double n12 = n8 * n8;
                final double n13 = n9 * n9;
                final double n14 = n12 / n10 + n13 / n11;
                a2 = abs;
                a = abs2;
                if (n14 > 1.0) {
                    a2 = abs * (float)Math.sqrt(n14);
                    a = abs2 * (float)Math.sqrt(n14);
                    n10 = a2 * a2;
                    n11 = a * a;
                }
                int n15;
                if (b == b2) {
                    n15 = -1;
                }
                else {
                    n15 = 1;
                }
                final double n16 = n15;
                double a3;
                if ((a3 = (n10 * n11 - n10 * n13 - n11 * n12) / (n10 * n13 + n11 * n12)) < 0.0) {
                    a3 = 0.0;
                }
                final double n17 = n16 * Math.sqrt(a3);
                final double n18 = n17 * (a2 * n9 / a);
                final double n19 = n17 * -(a * n8 / a2);
                final double n20 = (n + n4) / 2.0;
                final double n21 = (n2 + n5) / 2.0;
                final double n22 = (n8 - n18) / a2;
                final double n23 = (n9 - n19) / a;
                final double n24 = (-n8 - n18) / a2;
                final double n25 = (-n9 - n19) / a;
                final double sqrt = Math.sqrt(n22 * n22 + n23 * n23);
                double n26;
                if (n23 < 0.0) {
                    n26 = -1.0;
                }
                else {
                    n26 = 1.0;
                }
                final double degrees = Math.toDegrees(Math.acos(n22 / sqrt) * n26);
                final double sqrt2 = Math.sqrt((n22 * n22 + n23 * n23) * (n24 * n24 + n25 * n25));
                double n27;
                if (n22 * n25 - n23 * n24 < 0.0) {
                    n27 = -1.0;
                }
                else {
                    n27 = 1.0;
                }
                final double degrees2 = Math.toDegrees(Math.acos((n22 * n24 + n23 * n25) / sqrt2) * n27);
                double n28;
                if (!b2 && degrees2 > 0.0) {
                    n28 = degrees2 - 360.0;
                }
                else {
                    n28 = degrees2;
                    if (b2) {
                        n28 = degrees2;
                        if (degrees2 < 0.0) {
                            n28 = degrees2 + 360.0;
                        }
                    }
                }
                final float[] arcToBeziers = arcToBeziers(degrees % 360.0, n28 % 360.0);
                final Matrix matrix = new Matrix();
                matrix.postScale(a2, a);
                matrix.postRotate(n3);
                matrix.postTranslate((float)(n20 + (cos * n18 - sin * n19)), (float)(n21 + (sin * n18 + cos * n19)));
                matrix.mapPoints(arcToBeziers);
                arcToBeziers[arcToBeziers.length - 2] = n4;
                arcToBeziers[arcToBeziers.length - 1] = n5;
                for (int i = 0; i < arcToBeziers.length; i += 6) {
                    pathInterface.cubicTo(arcToBeziers[i], arcToBeziers[i + 1], arcToBeziers[i + 2], arcToBeziers[i + 3], arcToBeziers[i + 4], arcToBeziers[i + 5]);
                }
            }
        }
    }
    
    private static float[] arcToBeziers(double radians, double n) {
        final int n2 = (int)Math.ceil(Math.abs(n) / 90.0);
        radians = Math.toRadians(radians);
        final float n3 = (float)(Math.toRadians(n) / n2);
        n = 1.3333333333333333 * Math.sin(n3 / 2.0) / (1.0 + Math.cos(n3 / 2.0));
        final float[] array = new float[n2 * 6];
        int i = 0;
        int n4 = 0;
        while (i < n2) {
            final double n5 = radians + i * n3;
            final double cos = Math.cos(n5);
            final double sin = Math.sin(n5);
            final int n6 = n4 + 1;
            array[n4] = (float)(cos - n * sin);
            final int n7 = n6 + 1;
            array[n6] = (float)(n * cos + sin);
            final double n8 = n5 + n3;
            final double cos2 = Math.cos(n8);
            final double sin2 = Math.sin(n8);
            final int n9 = n7 + 1;
            array[n7] = (float)(n * sin2 + cos2);
            final int n10 = n9 + 1;
            array[n9] = (float)(sin2 - n * cos2);
            final int n11 = n10 + 1;
            array[n10] = (float)cos2;
            n4 = n11 + 1;
            array[n11] = (float)sin2;
            ++i;
        }
        return array;
    }
    
    private List<MarkerVector> calculateMarkerPositions(final SVG.Line line) {
        float floatValueX;
        if (line.x1 != null) {
            floatValueX = line.x1.floatValueX(this);
        }
        else {
            floatValueX = 0.0f;
        }
        float floatValueY;
        if (line.y1 != null) {
            floatValueY = line.y1.floatValueY(this);
        }
        else {
            floatValueY = 0.0f;
        }
        float floatValueX2;
        if (line.x2 != null) {
            floatValueX2 = line.x2.floatValueX(this);
        }
        else {
            floatValueX2 = 0.0f;
        }
        float floatValueY2;
        if (line.y2 != null) {
            floatValueY2 = line.y2.floatValueY(this);
        }
        else {
            floatValueY2 = 0.0f;
        }
        final ArrayList<MarkerVector> list = new ArrayList<MarkerVector>(2);
        list.add(new MarkerVector(floatValueX, floatValueY, floatValueX2 - floatValueX, floatValueY2 - floatValueY));
        list.add(new MarkerVector(floatValueX2, floatValueY2, floatValueX2 - floatValueX, floatValueY2 - floatValueY));
        return list;
    }
    
    private List<MarkerVector> calculateMarkerPositions(final SVG.PolyLine polyLine) {
        final int length = polyLine.points.length;
        List<MarkerVector> list;
        if (length < 2) {
            list = null;
        }
        else {
            final ArrayList<MarkerVector> list2 = new ArrayList<MarkerVector>();
            MarkerVector markerVector = new MarkerVector(polyLine.points[0], polyLine.points[1], 0.0f, 0.0f);
            float n = 0.0f;
            float n2 = 0.0f;
            for (int i = 2; i < length; i += 2) {
                n = polyLine.points[i];
                n2 = polyLine.points[i + 1];
                markerVector.add(n, n2);
                list2.add(markerVector);
                markerVector = new MarkerVector(n, n2, n - markerVector.x, n2 - markerVector.y);
            }
            if (polyLine instanceof SVG.Polygon) {
                list = list2;
                if (n != polyLine.points[0]) {
                    list = list2;
                    if (n2 != polyLine.points[1]) {
                        final float n3 = polyLine.points[0];
                        final float n4 = polyLine.points[1];
                        markerVector.add(n3, n4);
                        list2.add(markerVector);
                        final MarkerVector markerVector2 = new MarkerVector(n3, n4, n3 - markerVector.x, n4 - markerVector.y);
                        markerVector2.add(list2.get(0));
                        list2.add(markerVector2);
                        list2.set(0, markerVector2);
                        list = list2;
                    }
                }
            }
            else {
                list2.add(markerVector);
                list = list2;
            }
        }
        return list;
    }
    
    private SVG.Box calculatePathBounds(final Path path) {
        final RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        return new SVG.Box(rectF.left, rectF.top, rectF.width(), rectF.height());
    }
    
    private float calculateTextWidth(final SVG.TextContainer textContainer) {
        final TextWidthCalculator textWidthCalculator = new TextWidthCalculator((TextWidthCalculator)null);
        this.enumerateTextSpans(textContainer, (TextProcessor)textWidthCalculator);
        return textWidthCalculator.x;
    }
    
    private Matrix calculateViewBoxTransform(final SVG.Box box, final SVG.Box box2, final PreserveAspectRatio preserveAspectRatio) {
        final Matrix matrix = new Matrix();
        if (preserveAspectRatio != null && preserveAspectRatio.getAlignment() != null) {
            final float n = box.width / box2.width;
            final float n2 = box.height / box2.height;
            final float n3 = -box2.minX;
            float n4 = -box2.minY;
            if (preserveAspectRatio.equals(PreserveAspectRatio.STRETCH)) {
                matrix.preTranslate(box.minX, box.minY);
                matrix.preScale(n, n2);
                matrix.preTranslate(n3, n4);
            }
            else {
                float n5;
                if (preserveAspectRatio.getScale() == PreserveAspectRatio.Scale.Slice) {
                    n5 = Math.max(n, n2);
                }
                else {
                    n5 = Math.min(n, n2);
                }
                final float n6 = box.width / n5;
                final float n7 = box.height / n5;
                float n8 = n3;
                Label_0208: {
                    switch ($SWITCH_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment()[preserveAspectRatio.getAlignment().ordinal()]) {
                        default: {
                            n8 = n3;
                            break Label_0208;
                        }
                        case 4:
                        case 7:
                        case 10: {
                            n8 = n3 - (box2.width - n6);
                            break Label_0208;
                        }
                        case 3:
                        case 6:
                        case 9: {
                            n8 = n3 - (box2.width - n6) / 2.0f;
                        }
                        case 5:
                        case 8: {
                            switch ($SWITCH_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment()[preserveAspectRatio.getAlignment().ordinal()]) {
                                case 5:
                                case 6:
                                case 7: {
                                    n4 -= (box2.height - n7) / 2.0f;
                                    break;
                                }
                                case 8:
                                case 9:
                                case 10: {
                                    n4 -= box2.height - n7;
                                    break;
                                }
                            }
                            matrix.preTranslate(box.minX, box.minY);
                            matrix.preScale(n5, n5);
                            matrix.preTranslate(n8, n4);
                            break;
                        }
                    }
                }
            }
        }
        return matrix;
    }
    
    private void checkForClipPath(final SVG.SvgElement svgElement) {
        this.checkForClipPath(svgElement, svgElement.boundingBox);
    }
    
    private void checkForClipPath(final SVG.SvgElement svgElement, final SVG.Box box) {
        if (this.state.style.clipPath != null) {
            final SVG.SvgObject resolveIRI = svgElement.document.resolveIRI(this.state.style.clipPath);
            if (resolveIRI == null) {
                error("ClipPath reference '%s' not found", this.state.style.clipPath);
            }
            else {
                final SVG.ClipPath clipPath = (SVG.ClipPath)resolveIRI;
                if (clipPath.children.isEmpty()) {
                    this.canvas.clipRect(0, 0, 0, 0);
                }
                else {
                    boolean b;
                    if (clipPath.clipPathUnitsAreUser != null && !clipPath.clipPathUnitsAreUser) {
                        b = false;
                    }
                    else {
                        b = true;
                    }
                    if (svgElement instanceof SVG.Group && !b) {
                        warn("<clipPath clipPathUnits=\"objectBoundingBox\"> is not supported when referenced from container elements (like %s)", svgElement.getClass().getSimpleName());
                    }
                    else {
                        this.clipStatePush();
                        if (!b) {
                            final Matrix matrix = new Matrix();
                            matrix.preTranslate(box.minX, box.minY);
                            matrix.preScale(box.width, box.height);
                            this.canvas.concat(matrix);
                        }
                        if (clipPath.transform != null) {
                            this.canvas.concat(clipPath.transform);
                        }
                        this.state = this.findInheritFromAncestorState(clipPath);
                        this.checkForClipPath(clipPath);
                        final Path path = new Path();
                        final Iterator<SVG.SvgObject> iterator = clipPath.children.iterator();
                        while (iterator.hasNext()) {
                            this.addObjectToClip(iterator.next(), true, path, new Matrix());
                        }
                        this.canvas.clipPath(path);
                        this.clipStatePop();
                    }
                }
            }
        }
    }
    
    private void checkForGradiantsAndPatterns(final SVG.SvgElement svgElement) {
        if (this.state.style.fill instanceof SVG.PaintReference) {
            this.decodePaintReference(true, svgElement.boundingBox, (SVG.PaintReference)this.state.style.fill);
        }
        if (this.state.style.stroke instanceof SVG.PaintReference) {
            this.decodePaintReference(false, svgElement.boundingBox, (SVG.PaintReference)this.state.style.stroke);
        }
    }
    
    private Bitmap checkForImageDataURL(final String s) {
        final Bitmap bitmap = null;
        Bitmap decodeByteArray;
        if (!s.startsWith("data:")) {
            decodeByteArray = bitmap;
        }
        else {
            decodeByteArray = bitmap;
            if (s.length() >= 14) {
                final int index = s.indexOf(44);
                decodeByteArray = bitmap;
                if (index != -1) {
                    decodeByteArray = bitmap;
                    if (index >= 12) {
                        decodeByteArray = bitmap;
                        if (";base64".equals(s.substring(index - 7, index))) {
                            final byte[] decode = Base64.decode(s.substring(index + 1), 0);
                            decodeByteArray = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                        }
                    }
                }
            }
        }
        return decodeByteArray;
    }
    
    private Typeface checkGenericFont(final String s, final Integer n, final SVG.Style.FontStyle fontStyle) {
        final Typeface typeface = null;
        boolean b;
        if (fontStyle == SVG.Style.FontStyle.Italic) {
            b = true;
        }
        else {
            b = false;
        }
        int n2;
        if (n > 500) {
            if (b) {
                n2 = 3;
            }
            else {
                n2 = 1;
            }
        }
        else if (b) {
            n2 = 2;
        }
        else {
            n2 = 0;
        }
        Typeface typeface2;
        if (s.equals("serif")) {
            typeface2 = Typeface.create(Typeface.SERIF, n2);
        }
        else if (s.equals("sans-serif")) {
            typeface2 = Typeface.create(Typeface.SANS_SERIF, n2);
        }
        else if (s.equals("monospace")) {
            typeface2 = Typeface.create(Typeface.MONOSPACE, n2);
        }
        else if (s.equals("cursive")) {
            typeface2 = Typeface.create(Typeface.SANS_SERIF, n2);
        }
        else {
            typeface2 = typeface;
            if (s.equals("fantasy")) {
                typeface2 = Typeface.create(Typeface.SANS_SERIF, n2);
            }
        }
        return typeface2;
    }
    
    private void checkXMLSpaceAttribute(final SVG.SvgObject svgObject) {
        if (svgObject instanceof SVG.SvgElementBase) {
            final SVG.SvgElementBase svgElementBase = (SVG.SvgElementBase)svgObject;
            if (svgElementBase.spacePreserve != null) {
                this.state.spacePreserve = svgElementBase.spacePreserve;
            }
        }
    }
    
    private int clamp255(final float n) {
        final int n2 = (int)(256.0f * n);
        int n3;
        if (n2 < 0) {
            n3 = 0;
        }
        else if ((n3 = n2) > 255) {
            n3 = 255;
        }
        return n3;
    }
    
    private void clipStatePop() {
        this.canvas.restore();
        this.state = this.stateStack.pop();
    }
    
    private void clipStatePush() {
        this.canvas.save(1);
        this.stateStack.push(this.state);
        this.state = (RendererState)this.state.clone();
    }
    
    private static void debug(final String s, final Object... array) {
    }
    
    private void decodePaintReference(final boolean b, final SVG.Box box, final SVG.PaintReference paintReference) {
        final SVG.SvgObject resolveIRI = this.document.resolveIRI(paintReference.href);
        if (resolveIRI == null) {
            String s;
            if (b) {
                s = "Fill";
            }
            else {
                s = "Stroke";
            }
            error("%s reference '%s' not found", s, paintReference.href);
            if (paintReference.fallback != null) {
                this.setPaintColour(this.state, b, paintReference.fallback);
            }
            else if (b) {
                this.state.hasFill = false;
            }
            else {
                this.state.hasStroke = false;
            }
        }
        else {
            if (resolveIRI instanceof SVG.SvgLinearGradient) {
                this.makeLinearGradiant(b, box, (SVG.SvgLinearGradient)resolveIRI);
            }
            if (resolveIRI instanceof SVG.SvgRadialGradient) {
                this.makeRadialGradiant(b, box, (SVG.SvgRadialGradient)resolveIRI);
            }
            if (resolveIRI instanceof SVG.SolidColor) {
                this.setSolidColor(b, (SVG.SolidColor)resolveIRI);
            }
        }
    }
    
    private boolean display() {
        return this.state.style.display == null || this.state.style.display;
    }
    
    private void doFilledPath(final SVG.SvgElement svgElement, final Path path) {
        Label_0058: {
            if (!(this.state.style.fill instanceof SVG.PaintReference)) {
                break Label_0058;
            }
            final SVG.SvgObject resolveIRI = this.document.resolveIRI(((SVG.PaintReference)this.state.style.fill).href);
            if (!(resolveIRI instanceof SVG.Pattern)) {
                break Label_0058;
            }
            this.fillWithPattern(svgElement, path, (SVG.Pattern)resolveIRI);
            return;
        }
        this.canvas.drawPath(path, this.state.fillPaint);
    }
    
    private void doStroke(final Path path) {
        if (this.state.style.vectorEffect == SVG.Style.VectorEffect.NonScalingStroke) {
            final Matrix matrix = this.canvas.getMatrix();
            final Path path2 = new Path();
            path.transform(matrix, path2);
            this.canvas.setMatrix(new Matrix());
            final Shader shader = this.state.strokePaint.getShader();
            final Matrix localMatrix = new Matrix();
            if (shader != null) {
                shader.getLocalMatrix(localMatrix);
                final Matrix localMatrix2 = new Matrix(localMatrix);
                localMatrix2.postConcat(matrix);
                shader.setLocalMatrix(localMatrix2);
            }
            this.canvas.drawPath(path2, this.state.strokePaint);
            this.canvas.setMatrix(matrix);
            if (shader != null) {
                shader.setLocalMatrix(localMatrix);
            }
        }
        else {
            this.canvas.drawPath(path, this.state.strokePaint);
        }
    }
    
    private void duplicateCanvas() {
        try {
            final Bitmap bitmap = Bitmap.createBitmap(this.canvas.getWidth(), this.canvas.getHeight(), Bitmap$Config.ARGB_8888);
            this.bitmapStack.push(bitmap);
            final Canvas canvas = new Canvas(bitmap);
            canvas.setMatrix(this.canvas.getMatrix());
            this.canvas = canvas;
        }
        catch (OutOfMemoryError outOfMemoryError) {
            error("Not enough memory to create temporary bitmaps for mask processing", new Object[0]);
            throw outOfMemoryError;
        }
    }
    
    private void enumerateTextSpans(final SVG.TextContainer textContainer, final TextProcessor textProcessor) {
        if (this.display()) {
            final Iterator<SVG.SvgObject> iterator = textContainer.children.iterator();
            boolean b = true;
            while (iterator.hasNext()) {
                final SVG.SvgObject svgObject = iterator.next();
                if (svgObject instanceof SVG.TextSequence) {
                    textProcessor.processText(this.textXMLSpaceTransform(((SVG.TextSequence)svgObject).text, b, !iterator.hasNext()));
                }
                else {
                    this.processTextChild(svgObject, textProcessor);
                }
                b = false;
            }
        }
    }
    
    private static void error(final String format, final Object... args) {
        Log.e("SVGAndroidRenderer", String.format(format, args));
    }
    
    private void extractRawText(final SVG.TextContainer textContainer, final StringBuilder sb) {
        final Iterator<SVG.SvgObject> iterator = textContainer.children.iterator();
        boolean b = true;
        while (iterator.hasNext()) {
            final SVG.SvgObject svgObject = iterator.next();
            if (svgObject instanceof SVG.TextContainer) {
                this.extractRawText((SVG.TextContainer)svgObject, sb);
            }
            else if (svgObject instanceof SVG.TextSequence) {
                sb.append(this.textXMLSpaceTransform(((SVG.TextSequence)svgObject).text, b, !iterator.hasNext()));
            }
            b = false;
        }
    }
    
    private void fillInChainedGradientFields(final SVG.GradientElement gradientElement, String s) {
        final SVG.SvgObject resolveIRI = gradientElement.document.resolveIRI(s);
        if (resolveIRI == null) {
            warn("Gradient reference '%s' not found", s);
        }
        else if (!(resolveIRI instanceof SVG.GradientElement)) {
            error("Gradient href attributes must point to other gradient elements", new Object[0]);
        }
        else if (resolveIRI == gradientElement) {
            error("Circular reference in gradient href attribute '%s'", s);
        }
        else {
            s = (String)resolveIRI;
            if (gradientElement.gradientUnitsAreUser == null) {
                gradientElement.gradientUnitsAreUser = ((SVG.GradientElement)s).gradientUnitsAreUser;
            }
            if (gradientElement.gradientTransform == null) {
                gradientElement.gradientTransform = ((SVG.GradientElement)s).gradientTransform;
            }
            if (gradientElement.spreadMethod == null) {
                gradientElement.spreadMethod = ((SVG.GradientElement)s).spreadMethod;
            }
            if (gradientElement.children.isEmpty()) {
                gradientElement.children = ((SVG.GradientElement)s).children;
            }
            while (true) {
                while (true) {
                    try {
                        if (gradientElement instanceof SVG.SvgLinearGradient) {
                            this.fillInChainedGradientFields((SVG.SvgLinearGradient)gradientElement, (SVG.SvgLinearGradient)resolveIRI);
                        }
                        else {
                            this.fillInChainedGradientFields((SVG.SvgRadialGradient)gradientElement, (SVG.SvgRadialGradient)resolveIRI);
                        }
                        if (((SVG.GradientElement)s).href != null) {
                            this.fillInChainedGradientFields(gradientElement, ((SVG.GradientElement)s).href);
                            break;
                        }
                        break;
                    }
                    catch (ClassCastException ex) {
                        continue;
                    }
                    continue;
                }
            }
        }
    }
    
    private void fillInChainedGradientFields(final SVG.SvgLinearGradient svgLinearGradient, final SVG.SvgLinearGradient svgLinearGradient2) {
        if (svgLinearGradient.x1 == null) {
            svgLinearGradient.x1 = svgLinearGradient2.x1;
        }
        if (svgLinearGradient.y1 == null) {
            svgLinearGradient.y1 = svgLinearGradient2.y1;
        }
        if (svgLinearGradient.x2 == null) {
            svgLinearGradient.x2 = svgLinearGradient2.x2;
        }
        if (svgLinearGradient.y2 == null) {
            svgLinearGradient.y2 = svgLinearGradient2.y2;
        }
    }
    
    private void fillInChainedGradientFields(final SVG.SvgRadialGradient svgRadialGradient, final SVG.SvgRadialGradient svgRadialGradient2) {
        if (svgRadialGradient.cx == null) {
            svgRadialGradient.cx = svgRadialGradient2.cx;
        }
        if (svgRadialGradient.cy == null) {
            svgRadialGradient.cy = svgRadialGradient2.cy;
        }
        if (svgRadialGradient.r == null) {
            svgRadialGradient.r = svgRadialGradient2.r;
        }
        if (svgRadialGradient.fx == null) {
            svgRadialGradient.fx = svgRadialGradient2.fx;
        }
        if (svgRadialGradient.fy == null) {
            svgRadialGradient.fy = svgRadialGradient2.fy;
        }
    }
    
    private void fillInChainedPatternFields(final SVG.Pattern pattern, final String s) {
        final SVG.SvgObject resolveIRI = pattern.document.resolveIRI(s);
        if (resolveIRI == null) {
            warn("Pattern reference '%s' not found", s);
        }
        else if (!(resolveIRI instanceof SVG.Pattern)) {
            error("Pattern href attributes must point to other pattern elements", new Object[0]);
        }
        else if (resolveIRI == pattern) {
            error("Circular reference in pattern href attribute '%s'", s);
        }
        else {
            final SVG.Pattern pattern2 = (SVG.Pattern)resolveIRI;
            if (pattern.patternUnitsAreUser == null) {
                pattern.patternUnitsAreUser = pattern2.patternUnitsAreUser;
            }
            if (pattern.patternContentUnitsAreUser == null) {
                pattern.patternContentUnitsAreUser = pattern2.patternContentUnitsAreUser;
            }
            if (pattern.patternTransform == null) {
                pattern.patternTransform = pattern2.patternTransform;
            }
            if (pattern.x == null) {
                pattern.x = pattern2.x;
            }
            if (pattern.y == null) {
                pattern.y = pattern2.y;
            }
            if (pattern.width == null) {
                pattern.width = pattern2.width;
            }
            if (pattern.height == null) {
                pattern.height = pattern2.height;
            }
            if (pattern.children.isEmpty()) {
                pattern.children = pattern2.children;
            }
            if (pattern.viewBox == null) {
                pattern.viewBox = pattern2.viewBox;
            }
            if (pattern.preserveAspectRatio == null) {
                pattern.preserveAspectRatio = pattern2.preserveAspectRatio;
            }
            if (pattern2.href != null) {
                this.fillInChainedPatternFields(pattern, pattern2.href);
            }
        }
    }
    
    private void fillWithPattern(final SVG.SvgElement svgElement, final Path path, final SVG.Pattern pattern) {
        int n;
        if (pattern.patternUnitsAreUser != null && pattern.patternUnitsAreUser) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (pattern.href != null) {
            this.fillInChainedPatternFields(pattern, pattern.href);
        }
        float floatValueX;
        float floatValueY;
        float floatValueX2;
        float floatValueY2;
        if (n != 0) {
            if (pattern.x != null) {
                floatValueX = pattern.x.floatValueX(this);
            }
            else {
                floatValueX = 0.0f;
            }
            if (pattern.y != null) {
                floatValueY = pattern.y.floatValueY(this);
            }
            else {
                floatValueY = 0.0f;
            }
            if (pattern.width != null) {
                floatValueX2 = pattern.width.floatValueX(this);
            }
            else {
                floatValueX2 = 0.0f;
            }
            if (pattern.height != null) {
                floatValueY2 = pattern.height.floatValueY(this);
            }
            else {
                floatValueY2 = 0.0f;
            }
        }
        else {
            float floatValue;
            if (pattern.x != null) {
                floatValue = pattern.x.floatValue(this, 1.0f);
            }
            else {
                floatValue = 0.0f;
            }
            float floatValue2;
            if (pattern.y != null) {
                floatValue2 = pattern.y.floatValue(this, 1.0f);
            }
            else {
                floatValue2 = 0.0f;
            }
            float floatValue3;
            if (pattern.width != null) {
                floatValue3 = pattern.width.floatValue(this, 1.0f);
            }
            else {
                floatValue3 = 0.0f;
            }
            float floatValue4;
            if (pattern.height != null) {
                floatValue4 = pattern.height.floatValue(this, 1.0f);
            }
            else {
                floatValue4 = 0.0f;
            }
            final float n2 = svgElement.boundingBox.minX + svgElement.boundingBox.width * floatValue;
            final float n3 = svgElement.boundingBox.minY + svgElement.boundingBox.height * floatValue2;
            final float n4 = floatValue3 * svgElement.boundingBox.width;
            floatValueY2 = floatValue4 * svgElement.boundingBox.height;
            floatValueX2 = n4;
            floatValueX = n2;
            floatValueY = n3;
        }
        if (floatValueX2 != 0.0f && floatValueY2 != 0.0f) {
            PreserveAspectRatio preserveAspectRatio;
            if (pattern.preserveAspectRatio != null) {
                preserveAspectRatio = pattern.preserveAspectRatio;
            }
            else {
                preserveAspectRatio = PreserveAspectRatio.LETTERBOX;
            }
            this.statePush();
            this.canvas.clipPath(path);
            final RendererState rendererState = new RendererState();
            this.updateStyle(rendererState, SVG.Style.getDefaultStyle());
            rendererState.style.overflow = false;
            this.state = this.findInheritFromAncestorState(pattern, rendererState);
            Cloneable boundingBox;
            final SVG.Box box = (SVG.Box)(boundingBox = svgElement.boundingBox);
            if (pattern.patternTransform != null) {
                this.canvas.concat(pattern.patternTransform);
                final Matrix matrix = new Matrix();
                boundingBox = box;
                if (pattern.patternTransform.invert(matrix)) {
                    final float[] array = { svgElement.boundingBox.minX, svgElement.boundingBox.minY, svgElement.boundingBox.maxX(), svgElement.boundingBox.minY, svgElement.boundingBox.maxX(), svgElement.boundingBox.maxY(), svgElement.boundingBox.minX, svgElement.boundingBox.maxY() };
                    matrix.mapPoints(array);
                    final RectF rectF = new RectF(array[0], array[1], array[0], array[1]);
                    for (int i = 2; i <= 6; i += 2) {
                        if (array[i] < rectF.left) {
                            rectF.left = array[i];
                        }
                        if (array[i] > rectF.right) {
                            rectF.right = array[i];
                        }
                        if (array[i + 1] < rectF.top) {
                            rectF.top = array[i + 1];
                        }
                        if (array[i + 1] > rectF.bottom) {
                            rectF.bottom = array[i + 1];
                        }
                    }
                    boundingBox = new SVG.Box(rectF.left, rectF.top, rectF.right - rectF.left, rectF.bottom - rectF.top);
                }
            }
            final float n5 = (float)Math.floor((((SVG.Box)boundingBox).minX - floatValueX) / floatValueX2);
            final float n6 = (float)Math.floor((((SVG.Box)boundingBox).minY - floatValueY) / floatValueY2);
            final float maxX = ((SVG.Box)boundingBox).maxX();
            final float maxY = ((SVG.Box)boundingBox).maxY();
            final SVG.Box box2 = new SVG.Box(0.0f, 0.0f, floatValueX2, floatValueY2);
            for (float minY = floatValueY + n6 * floatValueY2; minY < maxY; minY += floatValueY2) {
                for (float minX = floatValueX + n5 * floatValueX2; minX < maxX; minX += floatValueX2) {
                    box2.minX = minX;
                    box2.minY = minY;
                    this.statePush();
                    if (!this.state.style.overflow) {
                        this.setClipRect(box2.minX, box2.minY, box2.width, box2.height);
                    }
                    if (pattern.viewBox != null) {
                        this.canvas.concat(this.calculateViewBoxTransform(box2, pattern.viewBox, preserveAspectRatio));
                    }
                    else {
                        int n7;
                        if (pattern.patternContentUnitsAreUser != null && !pattern.patternContentUnitsAreUser) {
                            n7 = 0;
                        }
                        else {
                            n7 = 1;
                        }
                        this.canvas.translate(minX, minY);
                        if (n7 == 0) {
                            this.canvas.scale(svgElement.boundingBox.width, svgElement.boundingBox.height);
                        }
                    }
                    final boolean pushLayer = this.pushLayer();
                    final Iterator<SVG.SvgObject> iterator = pattern.children.iterator();
                    while (iterator.hasNext()) {
                        this.render(iterator.next());
                    }
                    if (pushLayer) {
                        this.popLayer(pattern);
                    }
                    this.statePop();
                }
            }
            this.statePop();
        }
    }
    
    private RendererState findInheritFromAncestorState(final SVG.SvgObject svgObject) {
        final RendererState rendererState = new RendererState();
        this.updateStyle(rendererState, SVG.Style.getDefaultStyle());
        return this.findInheritFromAncestorState(svgObject, rendererState);
    }
    
    private RendererState findInheritFromAncestorState(SVG.SvgObject svgObject, final RendererState rendererState) {
        final ArrayList<SVG.SvgElementBase> list = new ArrayList<SVG.SvgElementBase>();
        while (true) {
            if (svgObject instanceof SVG.SvgElementBase) {
                list.add(0, (SVG.SvgElementBase)svgObject);
            }
            if (svgObject.parent == null) {
                break;
            }
            svgObject = (SVG.SvgObject)svgObject.parent;
        }
        final Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.updateStyleForElement(rendererState, iterator.next());
        }
        rendererState.viewBox = this.document.getRootElement().viewBox;
        if (rendererState.viewBox == null) {
            rendererState.viewBox = this.canvasViewPort;
        }
        rendererState.viewPort = this.canvasViewPort;
        rendererState.directRendering = this.state.directRendering;
        return rendererState;
    }
    
    private SVG.Style.TextAnchor getAnchorPosition() {
        SVG.Style.TextAnchor textAnchor;
        if (this.state.style.direction == SVG.Style.TextDirection.LTR || this.state.style.textAnchor == SVG.Style.TextAnchor.Middle) {
            textAnchor = this.state.style.textAnchor;
        }
        else if (this.state.style.textAnchor == SVG.Style.TextAnchor.Start) {
            textAnchor = SVG.Style.TextAnchor.End;
        }
        else {
            textAnchor = SVG.Style.TextAnchor.Start;
        }
        return textAnchor;
    }
    
    private Path$FillType getClipRuleFromState() {
        Path$FillType path$FillType = null;
        if (this.state.style.clipRule == null) {
            path$FillType = Path$FillType.WINDING;
        }
        else {
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVG$Style$FillRule()[this.state.style.clipRule.ordinal()]) {
                default: {
                    path$FillType = Path$FillType.WINDING;
                    break;
                }
                case 2: {
                    path$FillType = Path$FillType.EVEN_ODD;
                    break;
                }
            }
        }
        return path$FillType;
    }
    
    private Path$FillType getFillTypeFromState() {
        Path$FillType path$FillType = null;
        if (this.state.style.fillRule == null) {
            path$FillType = Path$FillType.WINDING;
        }
        else {
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVG$Style$FillRule()[this.state.style.fillRule.ordinal()]) {
                default: {
                    path$FillType = Path$FillType.WINDING;
                    break;
                }
                case 2: {
                    path$FillType = Path$FillType.EVEN_ODD;
                    break;
                }
            }
        }
        return path$FillType;
    }
    
    private static void info(final String format, final Object... args) {
        Log.i("SVGAndroidRenderer", String.format(format, args));
    }
    
    private boolean isSpecified(final SVG.Style style, final long n) {
        return (style.specifiedFlags & n) != 0x0L;
    }
    
    private void makeLinearGradiant(final boolean b, final SVG.Box box, final SVG.SvgLinearGradient svgLinearGradient) {
        if (svgLinearGradient.href != null) {
            this.fillInChainedGradientFields(svgLinearGradient, svgLinearGradient.href);
        }
        boolean b2;
        if (svgLinearGradient.gradientUnitsAreUser != null && svgLinearGradient.gradientUnitsAreUser) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        Paint paint;
        if (b) {
            paint = this.state.fillPaint;
        }
        else {
            paint = this.state.strokePaint;
        }
        float n;
        float floatValueY2;
        float n3;
        float n4;
        if (b2) {
            final SVG.Box currentViewPortInUserUnits = this.getCurrentViewPortInUserUnits();
            if (svgLinearGradient.x1 != null) {
                n = svgLinearGradient.x1.floatValueX(this);
            }
            else {
                n = 0.0f;
            }
            float floatValueY;
            if (svgLinearGradient.y1 != null) {
                floatValueY = svgLinearGradient.y1.floatValueY(this);
            }
            else {
                floatValueY = 0.0f;
            }
            float n2;
            if (svgLinearGradient.x2 != null) {
                n2 = svgLinearGradient.x2.floatValueX(this);
            }
            else {
                n2 = currentViewPortInUserUnits.width;
            }
            if (svgLinearGradient.y2 != null) {
                floatValueY2 = svgLinearGradient.y2.floatValueY(this);
                n3 = n2;
                n4 = floatValueY;
            }
            else {
                floatValueY2 = 0.0f;
                n4 = floatValueY;
                n3 = n2;
            }
        }
        else {
            if (svgLinearGradient.x1 != null) {
                n = svgLinearGradient.x1.floatValue(this, 1.0f);
            }
            else {
                n = 0.0f;
            }
            float floatValue;
            if (svgLinearGradient.y1 != null) {
                floatValue = svgLinearGradient.y1.floatValue(this, 1.0f);
            }
            else {
                floatValue = 0.0f;
            }
            float floatValue2;
            if (svgLinearGradient.x2 != null) {
                floatValue2 = svgLinearGradient.x2.floatValue(this, 1.0f);
            }
            else {
                floatValue2 = 1.0f;
            }
            float floatValue3;
            if (svgLinearGradient.y2 != null) {
                floatValue3 = svgLinearGradient.y2.floatValue(this, 1.0f);
            }
            else {
                floatValue3 = 0.0f;
            }
            n4 = floatValue;
            n3 = floatValue2;
            floatValueY2 = floatValue3;
        }
        this.statePush();
        this.state = this.findInheritFromAncestorState(svgLinearGradient);
        final Matrix localMatrix = new Matrix();
        if (!b2) {
            localMatrix.preTranslate(box.minX, box.minY);
            localMatrix.preScale(box.width, box.height);
        }
        if (svgLinearGradient.gradientTransform != null) {
            localMatrix.preConcat(svgLinearGradient.gradientTransform);
        }
        final int size = svgLinearGradient.children.size();
        if (size == 0) {
            this.statePop();
            if (b) {
                this.state.hasFill = false;
            }
            else {
                this.state.hasStroke = false;
            }
        }
        else {
            final int[] array = new int[size];
            final float[] array2 = new float[size];
            int n5 = 0;
            float floatValue4 = -1.0f;
            for (final SVG.Stop stop : svgLinearGradient.children) {
                if (n5 == 0 || stop.offset >= floatValue4) {
                    array2[n5] = stop.offset;
                    floatValue4 = stop.offset;
                }
                else {
                    array2[n5] = floatValue4;
                }
                this.statePush();
                this.updateStyleForElement(this.state, stop);
                SVG.SvgPaint black;
                if ((black = this.state.style.stopColor) == null) {
                    black = SVG.Colour.BLACK;
                }
                array[n5] = (this.clamp255(this.state.style.stopOpacity) << 24 | ((SVG.Colour)black).colour);
                ++n5;
                this.statePop();
            }
            if ((n == n3 && n4 == floatValueY2) || size == 1) {
                this.statePop();
                paint.setColor(array[size - 1]);
            }
            else {
                Shader$TileMode shader$TileMode = Shader$TileMode.CLAMP;
                if (svgLinearGradient.spreadMethod != null) {
                    if (svgLinearGradient.spreadMethod == SVG.GradientSpread.reflect) {
                        shader$TileMode = Shader$TileMode.MIRROR;
                    }
                    else {
                        shader$TileMode = shader$TileMode;
                        if (svgLinearGradient.spreadMethod == SVG.GradientSpread.repeat) {
                            shader$TileMode = Shader$TileMode.REPEAT;
                        }
                    }
                }
                this.statePop();
                final LinearGradient shader = new LinearGradient(n, n4, n3, floatValueY2, array, array2, shader$TileMode);
                shader.setLocalMatrix(localMatrix);
                paint.setShader((Shader)shader);
            }
        }
    }
    
    private Path makePathAndBoundingBox(final SVG.Circle circle) {
        float floatValueX;
        if (circle.cx != null) {
            floatValueX = circle.cx.floatValueX(this);
        }
        else {
            floatValueX = 0.0f;
        }
        float floatValueY;
        if (circle.cy != null) {
            floatValueY = circle.cy.floatValueY(this);
        }
        else {
            floatValueY = 0.0f;
        }
        final float floatValue = circle.r.floatValue(this);
        final float n = floatValueX - floatValue;
        final float n2 = floatValueY - floatValue;
        final float n3 = floatValueX + floatValue;
        final float n4 = floatValueY + floatValue;
        if (circle.boundingBox == null) {
            circle.boundingBox = new SVG.Box(n, n2, 2.0f * floatValue, 2.0f * floatValue);
        }
        final float n5 = floatValue * 0.5522848f;
        final Path path = new Path();
        path.moveTo(floatValueX, n2);
        path.cubicTo(floatValueX + n5, n2, n3, floatValueY - n5, n3, floatValueY);
        path.cubicTo(n3, floatValueY + n5, floatValueX + n5, n4, floatValueX, n4);
        path.cubicTo(floatValueX - n5, n4, n, floatValueY + n5, n, floatValueY);
        path.cubicTo(n, floatValueY - n5, floatValueX - n5, n2, floatValueX, n2);
        path.close();
        return path;
    }
    
    private Path makePathAndBoundingBox(final SVG.Ellipse ellipse) {
        float floatValueX;
        if (ellipse.cx != null) {
            floatValueX = ellipse.cx.floatValueX(this);
        }
        else {
            floatValueX = 0.0f;
        }
        float floatValueY;
        if (ellipse.cy != null) {
            floatValueY = ellipse.cy.floatValueY(this);
        }
        else {
            floatValueY = 0.0f;
        }
        final float floatValueX2 = ellipse.rx.floatValueX(this);
        final float floatValueY2 = ellipse.ry.floatValueY(this);
        final float n = floatValueX - floatValueX2;
        final float n2 = floatValueY - floatValueY2;
        final float n3 = floatValueX + floatValueX2;
        final float n4 = floatValueY + floatValueY2;
        if (ellipse.boundingBox == null) {
            ellipse.boundingBox = new SVG.Box(n, n2, 2.0f * floatValueX2, 2.0f * floatValueY2);
        }
        final float n5 = floatValueX2 * 0.5522848f;
        final float n6 = floatValueY2 * 0.5522848f;
        final Path path = new Path();
        path.moveTo(floatValueX, n2);
        path.cubicTo(floatValueX + n5, n2, n3, floatValueY - n6, n3, floatValueY);
        path.cubicTo(n3, floatValueY + n6, floatValueX + n5, n4, floatValueX, n4);
        path.cubicTo(floatValueX - n5, n4, n, floatValueY + n6, n, floatValueY);
        path.cubicTo(n, floatValueY - n6, floatValueX - n5, n2, floatValueX, n2);
        path.close();
        return path;
    }
    
    private Path makePathAndBoundingBox(final SVG.Line line) {
        float floatValueX;
        if (line.x1 == null) {
            floatValueX = 0.0f;
        }
        else {
            floatValueX = line.x1.floatValueX(this);
        }
        float floatValueY;
        if (line.y1 == null) {
            floatValueY = 0.0f;
        }
        else {
            floatValueY = line.y1.floatValueY(this);
        }
        float floatValueX2;
        if (line.x2 == null) {
            floatValueX2 = 0.0f;
        }
        else {
            floatValueX2 = line.x2.floatValueX(this);
        }
        float floatValueY2;
        if (line.y2 == null) {
            floatValueY2 = 0.0f;
        }
        else {
            floatValueY2 = line.y2.floatValueY(this);
        }
        if (line.boundingBox == null) {
            line.boundingBox = new SVG.Box(Math.min(floatValueX, floatValueY), Math.min(floatValueY, floatValueY2), Math.abs(floatValueX2 - floatValueX), Math.abs(floatValueY2 - floatValueY));
        }
        final Path path = new Path();
        path.moveTo(floatValueX, floatValueY);
        path.lineTo(floatValueX2, floatValueY2);
        return path;
    }
    
    private Path makePathAndBoundingBox(final SVG.PolyLine polyLine) {
        final Path path = new Path();
        path.moveTo(polyLine.points[0], polyLine.points[1]);
        for (int i = 2; i < polyLine.points.length; i += 2) {
            path.lineTo(polyLine.points[i], polyLine.points[i + 1]);
        }
        if (polyLine instanceof SVG.Polygon) {
            path.close();
        }
        if (polyLine.boundingBox == null) {
            polyLine.boundingBox = this.calculatePathBounds(path);
        }
        path.setFillType(this.getClipRuleFromState());
        return path;
    }
    
    private Path makePathAndBoundingBox(final SVG.Rect rect) {
        float a;
        float floatValueY;
        if (rect.rx == null && rect.ry == null) {
            a = 0.0f;
            floatValueY = 0.0f;
        }
        else if (rect.rx == null) {
            floatValueY = (a = rect.ry.floatValueY(this));
        }
        else if (rect.ry == null) {
            floatValueY = (a = rect.rx.floatValueX(this));
        }
        else {
            a = rect.rx.floatValueX(this);
            floatValueY = rect.ry.floatValueY(this);
        }
        final float min = Math.min(a, rect.width.floatValueX(this) / 2.0f);
        final float min2 = Math.min(floatValueY, rect.height.floatValueY(this) / 2.0f);
        float floatValueX;
        if (rect.x != null) {
            floatValueX = rect.x.floatValueX(this);
        }
        else {
            floatValueX = 0.0f;
        }
        float floatValueY2;
        if (rect.y != null) {
            floatValueY2 = rect.y.floatValueY(this);
        }
        else {
            floatValueY2 = 0.0f;
        }
        final float floatValueX2 = rect.width.floatValueX(this);
        final float floatValueY3 = rect.height.floatValueY(this);
        if (rect.boundingBox == null) {
            rect.boundingBox = new SVG.Box(floatValueX, floatValueY2, floatValueX2, floatValueY3);
        }
        final float n = floatValueX + floatValueX2;
        final float n2 = floatValueY2 + floatValueY3;
        final Path path = new Path();
        if (min == 0.0f || min2 == 0.0f) {
            path.moveTo(floatValueX, floatValueY2);
            path.lineTo(n, floatValueY2);
            path.lineTo(n, n2);
            path.lineTo(floatValueX, n2);
            path.lineTo(floatValueX, floatValueY2);
        }
        else {
            final float n3 = min * 0.5522848f;
            final float n4 = min2 * 0.5522848f;
            path.moveTo(floatValueX, floatValueY2 + min2);
            path.cubicTo(floatValueX, floatValueY2 + min2 - n4, floatValueX + min - n3, floatValueY2, floatValueX + min, floatValueY2);
            path.lineTo(n - min, floatValueY2);
            path.cubicTo(n - min + n3, floatValueY2, n, floatValueY2 + min2 - n4, n, floatValueY2 + min2);
            path.lineTo(n, n2 - min2);
            path.cubicTo(n, n2 - min2 + n4, n - min + n3, n2, n - min, n2);
            path.lineTo(floatValueX + min, n2);
            path.cubicTo(floatValueX + min - n3, n2, floatValueX, n2 - min2 + n4, floatValueX, n2 - min2);
            path.lineTo(floatValueX, floatValueY2 + min2);
        }
        path.close();
        return path;
    }
    
    private void makeRadialGradiant(final boolean b, final SVG.Box box, final SVG.SvgRadialGradient svgRadialGradient) {
        if (svgRadialGradient.href != null) {
            this.fillInChainedGradientFields(svgRadialGradient, svgRadialGradient.href);
        }
        boolean b2;
        if (svgRadialGradient.gradientUnitsAreUser != null && svgRadialGradient.gradientUnitsAreUser) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        Paint paint;
        if (b) {
            paint = this.state.fillPaint;
        }
        else {
            paint = this.state.strokePaint;
        }
        float n3;
        float n4;
        float n5;
        if (b2) {
            final SVG.Length length = new SVG.Length(50.0f, SVG.Unit.percent);
            float n;
            if (svgRadialGradient.cx != null) {
                n = svgRadialGradient.cx.floatValueX(this);
            }
            else {
                n = length.floatValueX(this);
            }
            float n2;
            if (svgRadialGradient.cy != null) {
                n2 = svgRadialGradient.cy.floatValueY(this);
            }
            else {
                n2 = length.floatValueY(this);
            }
            if (svgRadialGradient.r != null) {
                n3 = svgRadialGradient.r.floatValue(this);
                n4 = n2;
                n5 = n;
            }
            else {
                n3 = length.floatValue(this);
                n5 = n;
                n4 = n2;
            }
        }
        else {
            float floatValue;
            if (svgRadialGradient.cx != null) {
                floatValue = svgRadialGradient.cx.floatValue(this, 1.0f);
            }
            else {
                floatValue = 0.5f;
            }
            float floatValue2;
            if (svgRadialGradient.cy != null) {
                floatValue2 = svgRadialGradient.cy.floatValue(this, 1.0f);
            }
            else {
                floatValue2 = 0.5f;
            }
            float floatValue3;
            if (svgRadialGradient.r != null) {
                floatValue3 = svgRadialGradient.r.floatValue(this, 1.0f);
            }
            else {
                floatValue3 = 0.5f;
            }
            n5 = floatValue;
            n4 = floatValue2;
            n3 = floatValue3;
        }
        this.statePush();
        this.state = this.findInheritFromAncestorState(svgRadialGradient);
        final Matrix localMatrix = new Matrix();
        if (!b2) {
            localMatrix.preTranslate(box.minX, box.minY);
            localMatrix.preScale(box.width, box.height);
        }
        if (svgRadialGradient.gradientTransform != null) {
            localMatrix.preConcat(svgRadialGradient.gradientTransform);
        }
        final int size = svgRadialGradient.children.size();
        if (size == 0) {
            this.statePop();
            if (b) {
                this.state.hasFill = false;
            }
            else {
                this.state.hasStroke = false;
            }
        }
        else {
            final int[] array = new int[size];
            final float[] array2 = new float[size];
            int n6 = 0;
            float floatValue4 = -1.0f;
            for (final SVG.Stop stop : svgRadialGradient.children) {
                if (n6 == 0 || stop.offset >= floatValue4) {
                    array2[n6] = stop.offset;
                    floatValue4 = stop.offset;
                }
                else {
                    array2[n6] = floatValue4;
                }
                this.statePush();
                this.updateStyleForElement(this.state, stop);
                SVG.SvgPaint black;
                if ((black = this.state.style.stopColor) == null) {
                    black = SVG.Colour.BLACK;
                }
                array[n6] = (this.clamp255(this.state.style.stopOpacity) << 24 | ((SVG.Colour)black).colour);
                ++n6;
                this.statePop();
            }
            if (n3 == 0.0f || size == 1) {
                this.statePop();
                paint.setColor(array[size - 1]);
            }
            else {
                Shader$TileMode shader$TileMode = Shader$TileMode.CLAMP;
                if (svgRadialGradient.spreadMethod != null) {
                    if (svgRadialGradient.spreadMethod == SVG.GradientSpread.reflect) {
                        shader$TileMode = Shader$TileMode.MIRROR;
                    }
                    else {
                        shader$TileMode = shader$TileMode;
                        if (svgRadialGradient.spreadMethod == SVG.GradientSpread.repeat) {
                            shader$TileMode = Shader$TileMode.REPEAT;
                        }
                    }
                }
                this.statePop();
                final RadialGradient shader = new RadialGradient(n5, n4, n3, array, array2, shader$TileMode);
                shader.setLocalMatrix(localMatrix);
                paint.setShader((Shader)shader);
            }
        }
    }
    
    private void parentPop() {
        this.parentStack.pop();
        this.matrixStack.pop();
    }
    
    private void parentPush(final SVG.SvgContainer item) {
        this.parentStack.push(item);
        this.matrixStack.push(this.canvas.getMatrix());
    }
    
    private void popLayer(final SVG.SvgElement svgElement) {
        if (this.state.style.mask != null && this.state.directRendering) {
            final SVG.SvgObject resolveIRI = this.document.resolveIRI(this.state.style.mask);
            this.duplicateCanvas();
            this.renderMask((SVG.Mask)resolveIRI, svgElement);
            final Bitmap processMaskBitmaps = this.processMaskBitmaps();
            (this.canvas = this.canvasStack.pop()).save();
            this.canvas.setMatrix(new Matrix());
            this.canvas.drawBitmap(processMaskBitmaps, 0.0f, 0.0f, this.state.fillPaint);
            processMaskBitmaps.recycle();
            this.canvas.restore();
        }
        this.statePop();
    }
    
    private Bitmap processMaskBitmaps() {
        final Bitmap bitmap = this.bitmapStack.pop();
        final Bitmap bitmap2 = this.bitmapStack.pop();
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final int[] array = new int[width];
        final int[] array2 = new int[width];
        for (int i = 0; i < height; ++i) {
            bitmap.getPixels(array, 0, width, 0, i, width, 1);
            bitmap2.getPixels(array2, 0, width, 0, i, width, 1);
            for (int j = 0; j < width; ++j) {
                final int n = array[j];
                final int n2 = n >> 24 & 0xFF;
                if (n2 == 0) {
                    array2[j] = 0;
                }
                else {
                    final int n3 = ((n >> 16 & 0xFF) * 6963 + (n >> 8 & 0xFF) * 23442 + (n & 0xFF) * 2362) * n2 / 8355840;
                    final int n4 = array2[j];
                    array2[j] = ((0xFFFFFF & n4) | (n4 >> 24 & 0xFF) * n3 / 255 << 24);
                }
            }
            bitmap2.setPixels(array2, 0, width, 0, i, width, 1);
        }
        bitmap.recycle();
        return bitmap2;
    }
    
    private void processTextChild(SVG.SvgObject resolveIRI, final TextProcessor textProcessor) {
        if (textProcessor.doTextContainer((SVG.TextContainer)resolveIRI)) {
            if (resolveIRI instanceof SVG.TextPath) {
                this.statePush();
                this.renderTextPath((SVG.TextPath)resolveIRI);
                this.statePop();
            }
            else if (resolveIRI instanceof SVG.TSpan) {
                debug("TSpan render", new Object[0]);
                this.statePush();
                final SVG.TSpan tSpan = (SVG.TSpan)resolveIRI;
                this.updateStyleForElement(this.state, tSpan);
                if (this.display()) {
                    float n = 0.0f;
                    float n2 = 0.0f;
                    float floatValueX = 0.0f;
                    float floatValueY = 0.0f;
                    if (textProcessor instanceof PlainTextDrawer) {
                        if (tSpan.x == null || tSpan.x.size() == 0) {
                            n = ((PlainTextDrawer)textProcessor).x;
                        }
                        else {
                            n = tSpan.x.get(0).floatValueX(this);
                        }
                        if (tSpan.y == null || tSpan.y.size() == 0) {
                            n2 = ((PlainTextDrawer)textProcessor).y;
                        }
                        else {
                            n2 = tSpan.y.get(0).floatValueY(this);
                        }
                        if (tSpan.dx == null || tSpan.dx.size() == 0) {
                            floatValueX = 0.0f;
                        }
                        else {
                            floatValueX = tSpan.dx.get(0).floatValueX(this);
                        }
                        if (tSpan.dy == null || tSpan.dy.size() == 0) {
                            floatValueY = 0.0f;
                        }
                        else {
                            floatValueY = tSpan.dy.get(0).floatValueY(this);
                        }
                    }
                    this.checkForGradiantsAndPatterns((SVG.SvgElement)tSpan.getTextRoot());
                    if (textProcessor instanceof PlainTextDrawer) {
                        ((PlainTextDrawer)textProcessor).x = n + floatValueX;
                        ((PlainTextDrawer)textProcessor).y = n2 + floatValueY;
                    }
                    final boolean pushLayer = this.pushLayer();
                    this.enumerateTextSpans(tSpan, textProcessor);
                    if (pushLayer) {
                        this.popLayer(tSpan);
                    }
                }
                this.statePop();
            }
            else if (resolveIRI instanceof SVG.TRef) {
                this.statePush();
                final SVG.TRef tRef = (SVG.TRef)resolveIRI;
                this.updateStyleForElement(this.state, tRef);
                if (this.display()) {
                    this.checkForGradiantsAndPatterns((SVG.SvgElement)tRef.getTextRoot());
                    resolveIRI = resolveIRI.document.resolveIRI(tRef.href);
                    if (resolveIRI != null && resolveIRI instanceof SVG.TextContainer) {
                        final StringBuilder sb = new StringBuilder();
                        this.extractRawText((SVG.TextContainer)resolveIRI, sb);
                        if (sb.length() > 0) {
                            textProcessor.processText(sb.toString());
                        }
                    }
                    else {
                        error("Tref reference '%s' not found", tRef.href);
                    }
                }
                this.statePop();
            }
        }
    }
    
    private boolean pushLayer() {
        boolean b;
        if (!this.requiresCompositing()) {
            b = false;
        }
        else {
            this.canvas.saveLayerAlpha((RectF)null, this.clamp255(this.state.style.opacity), 4);
            this.stateStack.push(this.state);
            this.state = (RendererState)this.state.clone();
            if (this.state.style.mask != null && this.state.directRendering) {
                final SVG.SvgObject resolveIRI = this.document.resolveIRI(this.state.style.mask);
                if (resolveIRI == null || !(resolveIRI instanceof SVG.Mask)) {
                    error("Mask reference '%s' not found", this.state.style.mask);
                    this.state.style.mask = null;
                    b = true;
                    return b;
                }
                this.canvasStack.push(this.canvas);
                this.duplicateCanvas();
            }
            b = true;
        }
        return b;
    }
    
    private void render(final SVG.Circle circle) {
        debug("Circle render", new Object[0]);
        if (circle.r != null && !circle.r.isZero()) {
            this.updateStyleForElement(this.state, circle);
            if (this.display() && this.visible()) {
                if (circle.transform != null) {
                    this.canvas.concat(circle.transform);
                }
                final Path pathAndBoundingBox = this.makePathAndBoundingBox(circle);
                this.updateParentBoundingBox(circle);
                this.checkForGradiantsAndPatterns(circle);
                this.checkForClipPath(circle);
                final boolean pushLayer = this.pushLayer();
                if (this.state.hasFill) {
                    this.doFilledPath(circle, pathAndBoundingBox);
                }
                if (this.state.hasStroke) {
                    this.doStroke(pathAndBoundingBox);
                }
                if (pushLayer) {
                    this.popLayer(circle);
                }
            }
        }
    }
    
    private void render(final SVG.Ellipse ellipse) {
        debug("Ellipse render", new Object[0]);
        if (ellipse.rx != null && ellipse.ry != null && !ellipse.rx.isZero() && !ellipse.ry.isZero()) {
            this.updateStyleForElement(this.state, ellipse);
            if (this.display() && this.visible()) {
                if (ellipse.transform != null) {
                    this.canvas.concat(ellipse.transform);
                }
                final Path pathAndBoundingBox = this.makePathAndBoundingBox(ellipse);
                this.updateParentBoundingBox(ellipse);
                this.checkForGradiantsAndPatterns(ellipse);
                this.checkForClipPath(ellipse);
                final boolean pushLayer = this.pushLayer();
                if (this.state.hasFill) {
                    this.doFilledPath(ellipse, pathAndBoundingBox);
                }
                if (this.state.hasStroke) {
                    this.doStroke(pathAndBoundingBox);
                }
                if (pushLayer) {
                    this.popLayer(ellipse);
                }
            }
        }
    }
    
    private void render(final SVG.Group group) {
        debug("Group render", new Object[0]);
        this.updateStyleForElement(this.state, group);
        if (this.display()) {
            if (group.transform != null) {
                this.canvas.concat(group.transform);
            }
            this.checkForClipPath(group);
            final boolean pushLayer = this.pushLayer();
            this.renderChildren(group, true);
            if (pushLayer) {
                this.popLayer(group);
            }
            this.updateParentBoundingBox(group);
        }
    }
    
    private void render(final SVG.Image image) {
        debug("Image render", new Object[0]);
        if (image.width != null && !image.width.isZero() && image.height != null && !image.height.isZero() && image.href != null) {
            PreserveAspectRatio preserveAspectRatio;
            if (image.preserveAspectRatio != null) {
                preserveAspectRatio = image.preserveAspectRatio;
            }
            else {
                preserveAspectRatio = PreserveAspectRatio.LETTERBOX;
            }
            Bitmap bitmap;
            if ((bitmap = this.checkForImageDataURL(image.href)) == null) {
                final SVGExternalFileResolver fileResolver = this.document.getFileResolver();
                if (fileResolver == null) {
                    return;
                }
                bitmap = fileResolver.resolveImage(image.href);
            }
            if (bitmap == null) {
                error("Could not locate image '%s'", image.href);
            }
            else {
                this.updateStyleForElement(this.state, image);
                if (this.display() && this.visible()) {
                    if (image.transform != null) {
                        this.canvas.concat(image.transform);
                    }
                    float floatValueX;
                    if (image.x != null) {
                        floatValueX = image.x.floatValueX(this);
                    }
                    else {
                        floatValueX = 0.0f;
                    }
                    float floatValueY;
                    if (image.y != null) {
                        floatValueY = image.y.floatValueY(this);
                    }
                    else {
                        floatValueY = 0.0f;
                    }
                    this.state.viewPort = new SVG.Box(floatValueX, floatValueY, image.width.floatValueX(this), image.height.floatValueX(this));
                    if (!this.state.style.overflow) {
                        this.setClipRect(this.state.viewPort.minX, this.state.viewPort.minY, this.state.viewPort.width, this.state.viewPort.height);
                    }
                    image.boundingBox = new SVG.Box(0.0f, 0.0f, (float)bitmap.getWidth(), (float)bitmap.getHeight());
                    this.canvas.concat(this.calculateViewBoxTransform(this.state.viewPort, image.boundingBox, preserveAspectRatio));
                    this.updateParentBoundingBox(image);
                    this.checkForClipPath(image);
                    final boolean pushLayer = this.pushLayer();
                    this.viewportFill();
                    this.canvas.drawBitmap(bitmap, 0.0f, 0.0f, this.state.fillPaint);
                    if (pushLayer) {
                        this.popLayer(image);
                    }
                }
            }
        }
    }
    
    private void render(final SVG.Line line) {
        debug("Line render", new Object[0]);
        this.updateStyleForElement(this.state, line);
        if (this.display() && this.visible() && this.state.hasStroke) {
            if (line.transform != null) {
                this.canvas.concat(line.transform);
            }
            final Path pathAndBoundingBox = this.makePathAndBoundingBox(line);
            this.updateParentBoundingBox(line);
            this.checkForGradiantsAndPatterns(line);
            this.checkForClipPath(line);
            final boolean pushLayer = this.pushLayer();
            this.doStroke(pathAndBoundingBox);
            this.renderMarkers(line);
            if (pushLayer) {
                this.popLayer(line);
            }
        }
    }
    
    private void render(final SVG.Path path) {
        debug("Path render", new Object[0]);
        this.updateStyleForElement(this.state, path);
        if (this.display() && this.visible() && (this.state.hasStroke || this.state.hasFill)) {
            if (path.transform != null) {
                this.canvas.concat(path.transform);
            }
            final Path path2 = new PathConverter(path.d).getPath();
            if (path.boundingBox == null) {
                path.boundingBox = this.calculatePathBounds(path2);
            }
            this.updateParentBoundingBox(path);
            this.checkForGradiantsAndPatterns(path);
            this.checkForClipPath(path);
            final boolean pushLayer = this.pushLayer();
            if (this.state.hasFill) {
                path2.setFillType(this.getFillTypeFromState());
                this.doFilledPath(path, path2);
            }
            if (this.state.hasStroke) {
                this.doStroke(path2);
            }
            this.renderMarkers(path);
            if (pushLayer) {
                this.popLayer(path);
            }
        }
    }
    
    private void render(final SVG.PolyLine polyLine) {
        debug("PolyLine render", new Object[0]);
        this.updateStyleForElement(this.state, polyLine);
        if (this.display() && this.visible() && (this.state.hasStroke || this.state.hasFill)) {
            if (polyLine.transform != null) {
                this.canvas.concat(polyLine.transform);
            }
            if (polyLine.points.length >= 2) {
                final Path pathAndBoundingBox = this.makePathAndBoundingBox(polyLine);
                this.updateParentBoundingBox(polyLine);
                this.checkForGradiantsAndPatterns(polyLine);
                this.checkForClipPath(polyLine);
                final boolean pushLayer = this.pushLayer();
                if (this.state.hasFill) {
                    this.doFilledPath(polyLine, pathAndBoundingBox);
                }
                if (this.state.hasStroke) {
                    this.doStroke(pathAndBoundingBox);
                }
                this.renderMarkers(polyLine);
                if (pushLayer) {
                    this.popLayer(polyLine);
                }
            }
        }
    }
    
    private void render(final SVG.Polygon polygon) {
        debug("Polygon render", new Object[0]);
        this.updateStyleForElement(this.state, polygon);
        if (this.display() && this.visible() && (this.state.hasStroke || this.state.hasFill)) {
            if (polygon.transform != null) {
                this.canvas.concat(polygon.transform);
            }
            if (polygon.points.length >= 2) {
                final Path pathAndBoundingBox = this.makePathAndBoundingBox(polygon);
                this.updateParentBoundingBox(polygon);
                this.checkForGradiantsAndPatterns(polygon);
                this.checkForClipPath(polygon);
                final boolean pushLayer = this.pushLayer();
                if (this.state.hasFill) {
                    this.doFilledPath(polygon, pathAndBoundingBox);
                }
                if (this.state.hasStroke) {
                    this.doStroke(pathAndBoundingBox);
                }
                this.renderMarkers(polygon);
                if (pushLayer) {
                    this.popLayer(polygon);
                }
            }
        }
    }
    
    private void render(final SVG.Rect rect) {
        debug("Rect render", new Object[0]);
        if (rect.width != null && rect.height != null && !rect.width.isZero() && !rect.height.isZero()) {
            this.updateStyleForElement(this.state, rect);
            if (this.display() && this.visible()) {
                if (rect.transform != null) {
                    this.canvas.concat(rect.transform);
                }
                final Path pathAndBoundingBox = this.makePathAndBoundingBox(rect);
                this.updateParentBoundingBox(rect);
                this.checkForGradiantsAndPatterns(rect);
                this.checkForClipPath(rect);
                final boolean pushLayer = this.pushLayer();
                if (this.state.hasFill) {
                    this.doFilledPath(rect, pathAndBoundingBox);
                }
                if (this.state.hasStroke) {
                    this.doStroke(pathAndBoundingBox);
                }
                if (pushLayer) {
                    this.popLayer(rect);
                }
            }
        }
    }
    
    private void render(final SVG.Svg svg) {
        this.render(svg, svg.width, svg.height);
    }
    
    private void render(final SVG.Svg svg, final SVG.Length length, final SVG.Length length2) {
        this.render(svg, length, length2, svg.viewBox, svg.preserveAspectRatio);
    }
    
    private void render(final SVG.Svg svg, final SVG.Length length, final SVG.Length length2, final SVG.Box box, final PreserveAspectRatio preserveAspectRatio) {
        debug("Svg render", new Object[0]);
        if ((length == null || !length.isZero()) && (length2 == null || !length2.isZero())) {
            PreserveAspectRatio preserveAspectRatio2;
            if ((preserveAspectRatio2 = preserveAspectRatio) == null) {
                if (svg.preserveAspectRatio != null) {
                    preserveAspectRatio2 = svg.preserveAspectRatio;
                }
                else {
                    preserveAspectRatio2 = PreserveAspectRatio.LETTERBOX;
                }
            }
            this.updateStyleForElement(this.state, svg);
            if (this.display()) {
                float floatValueX = 0.0f;
                float floatValueY = 0.0f;
                if (svg.parent != null) {
                    if (svg.x != null) {
                        floatValueX = svg.x.floatValueX(this);
                    }
                    else {
                        floatValueX = 0.0f;
                    }
                    if (svg.y != null) {
                        floatValueY = svg.y.floatValueY(this);
                    }
                    else {
                        floatValueY = 0.0f;
                    }
                }
                final SVG.Box currentViewPortInUserUnits = this.getCurrentViewPortInUserUnits();
                float n;
                if (length != null) {
                    n = length.floatValueX(this);
                }
                else {
                    n = currentViewPortInUserUnits.width;
                }
                float n2;
                if (length2 != null) {
                    n2 = length2.floatValueY(this);
                }
                else {
                    n2 = currentViewPortInUserUnits.height;
                }
                this.state.viewPort = new SVG.Box(floatValueX, floatValueY, n, n2);
                if (!this.state.style.overflow) {
                    this.setClipRect(this.state.viewPort.minX, this.state.viewPort.minY, this.state.viewPort.width, this.state.viewPort.height);
                }
                this.checkForClipPath(svg, this.state.viewPort);
                if (box != null) {
                    this.canvas.concat(this.calculateViewBoxTransform(this.state.viewPort, box, preserveAspectRatio2));
                    this.state.viewBox = svg.viewBox;
                }
                else {
                    this.canvas.translate(floatValueX, floatValueY);
                }
                final boolean pushLayer = this.pushLayer();
                this.viewportFill();
                this.renderChildren(svg, true);
                if (pushLayer) {
                    this.popLayer(svg);
                }
                this.updateParentBoundingBox(svg);
            }
        }
    }
    
    private void render(final SVG.SvgObject svgObject) {
        if (!(svgObject instanceof SVG.NotDirectlyRendered)) {
            this.statePush();
            this.checkXMLSpaceAttribute(svgObject);
            if (svgObject instanceof SVG.Svg) {
                this.render((SVG.Svg)svgObject);
            }
            else if (svgObject instanceof SVG.Use) {
                this.render((SVG.Use)svgObject);
            }
            else if (svgObject instanceof SVG.Switch) {
                this.render((SVG.Switch)svgObject);
            }
            else if (svgObject instanceof SVG.Group) {
                this.render((SVG.Group)svgObject);
            }
            else if (svgObject instanceof SVG.Image) {
                this.render((SVG.Image)svgObject);
            }
            else if (svgObject instanceof SVG.Path) {
                this.render((SVG.Path)svgObject);
            }
            else if (svgObject instanceof SVG.Rect) {
                this.render((SVG.Rect)svgObject);
            }
            else if (svgObject instanceof SVG.Circle) {
                this.render((SVG.Circle)svgObject);
            }
            else if (svgObject instanceof SVG.Ellipse) {
                this.render((SVG.Ellipse)svgObject);
            }
            else if (svgObject instanceof SVG.Line) {
                this.render((SVG.Line)svgObject);
            }
            else if (svgObject instanceof SVG.Polygon) {
                this.render((SVG.Polygon)svgObject);
            }
            else if (svgObject instanceof SVG.PolyLine) {
                this.render((SVG.PolyLine)svgObject);
            }
            else if (svgObject instanceof SVG.Text) {
                this.render((SVG.Text)svgObject);
            }
            this.statePop();
        }
    }
    
    private void render(final SVG.Switch switch1) {
        debug("Switch render", new Object[0]);
        this.updateStyleForElement(this.state, switch1);
        if (this.display()) {
            if (switch1.transform != null) {
                this.canvas.concat(switch1.transform);
            }
            this.checkForClipPath(switch1);
            final boolean pushLayer = this.pushLayer();
            this.renderSwitchChild(switch1);
            if (pushLayer) {
                this.popLayer(switch1);
            }
            this.updateParentBoundingBox(switch1);
        }
    }
    
    private void render(final SVG.Symbol symbol, final SVG.Length length, final SVG.Length length2) {
        debug("Symbol render", new Object[0]);
        if ((length == null || !length.isZero()) && (length2 == null || !length2.isZero())) {
            PreserveAspectRatio preserveAspectRatio;
            if (symbol.preserveAspectRatio != null) {
                preserveAspectRatio = symbol.preserveAspectRatio;
            }
            else {
                preserveAspectRatio = PreserveAspectRatio.LETTERBOX;
            }
            this.updateStyleForElement(this.state, symbol);
            float n;
            if (length != null) {
                n = length.floatValueX(this);
            }
            else {
                n = this.state.viewPort.width;
            }
            float n2;
            if (length2 != null) {
                n2 = length2.floatValueX(this);
            }
            else {
                n2 = this.state.viewPort.height;
            }
            this.state.viewPort = new SVG.Box(0.0f, 0.0f, n, n2);
            if (!this.state.style.overflow) {
                this.setClipRect(this.state.viewPort.minX, this.state.viewPort.minY, this.state.viewPort.width, this.state.viewPort.height);
            }
            if (symbol.viewBox != null) {
                this.canvas.concat(this.calculateViewBoxTransform(this.state.viewPort, symbol.viewBox, preserveAspectRatio));
                this.state.viewBox = symbol.viewBox;
            }
            final boolean pushLayer = this.pushLayer();
            this.renderChildren(symbol, true);
            if (pushLayer) {
                this.popLayer(symbol);
            }
            this.updateParentBoundingBox(symbol);
        }
    }
    
    private void render(final SVG.Text text) {
        debug("Text render", new Object[0]);
        this.updateStyleForElement(this.state, text);
        if (this.display()) {
            if (text.transform != null) {
                this.canvas.concat(text.transform);
            }
            float floatValueX;
            if (text.x == null || text.x.size() == 0) {
                floatValueX = 0.0f;
            }
            else {
                floatValueX = text.x.get(0).floatValueX(this);
            }
            float floatValueY;
            if (text.y == null || text.y.size() == 0) {
                floatValueY = 0.0f;
            }
            else {
                floatValueY = text.y.get(0).floatValueY(this);
            }
            float floatValueX2;
            if (text.dx == null || text.dx.size() == 0) {
                floatValueX2 = 0.0f;
            }
            else {
                floatValueX2 = text.dx.get(0).floatValueX(this);
            }
            float floatValueY2;
            if (text.dy == null || text.dy.size() == 0) {
                floatValueY2 = 0.0f;
            }
            else {
                floatValueY2 = text.dy.get(0).floatValueY(this);
            }
            final SVG.Style.TextAnchor anchorPosition = this.getAnchorPosition();
            float n = floatValueX;
            if (anchorPosition != SVG.Style.TextAnchor.Start) {
                final float calculateTextWidth = this.calculateTextWidth(text);
                if (anchorPosition == SVG.Style.TextAnchor.Middle) {
                    n = floatValueX - calculateTextWidth / 2.0f;
                }
                else {
                    n = floatValueX - calculateTextWidth;
                }
            }
            if (text.boundingBox == null) {
                final TextBoundsCalculator textBoundsCalculator = new TextBoundsCalculator(n, floatValueY);
                this.enumerateTextSpans(text, (TextProcessor)textBoundsCalculator);
                text.boundingBox = new SVG.Box(textBoundsCalculator.bbox.left, textBoundsCalculator.bbox.top, textBoundsCalculator.bbox.width(), textBoundsCalculator.bbox.height());
            }
            this.updateParentBoundingBox(text);
            this.checkForGradiantsAndPatterns(text);
            this.checkForClipPath(text);
            final boolean pushLayer = this.pushLayer();
            this.enumerateTextSpans(text, (TextProcessor)new PlainTextDrawer(n + floatValueX2, floatValueY + floatValueY2));
            if (pushLayer) {
                this.popLayer(text);
            }
        }
    }
    
    private void render(final SVG.Use use) {
        debug("Use render", new Object[0]);
        if ((use.width == null || !use.width.isZero()) && (use.height == null || !use.height.isZero())) {
            this.updateStyleForElement(this.state, use);
            if (this.display()) {
                final SVG.SvgObject resolveIRI = use.document.resolveIRI(use.href);
                if (resolveIRI == null) {
                    error("Use reference '%s' not found", use.href);
                }
                else {
                    if (use.transform != null) {
                        this.canvas.concat(use.transform);
                    }
                    final Matrix matrix = new Matrix();
                    float floatValueX;
                    if (use.x != null) {
                        floatValueX = use.x.floatValueX(this);
                    }
                    else {
                        floatValueX = 0.0f;
                    }
                    float floatValueY;
                    if (use.y != null) {
                        floatValueY = use.y.floatValueY(this);
                    }
                    else {
                        floatValueY = 0.0f;
                    }
                    matrix.preTranslate(floatValueX, floatValueY);
                    this.canvas.concat(matrix);
                    this.checkForClipPath(use);
                    final boolean pushLayer = this.pushLayer();
                    this.parentPush(use);
                    if (resolveIRI instanceof SVG.Svg) {
                        this.statePush();
                        final SVG.Svg svg = (SVG.Svg)resolveIRI;
                        SVG.Length length;
                        if (use.width != null) {
                            length = use.width;
                        }
                        else {
                            length = svg.width;
                        }
                        SVG.Length length2;
                        if (use.height != null) {
                            length2 = use.height;
                        }
                        else {
                            length2 = svg.height;
                        }
                        this.render(svg, length, length2);
                        this.statePop();
                    }
                    else if (resolveIRI instanceof SVG.Symbol) {
                        SVG.Length width;
                        if (use.width != null) {
                            width = use.width;
                        }
                        else {
                            width = new SVG.Length(100.0f, SVG.Unit.percent);
                        }
                        SVG.Length height;
                        if (use.height != null) {
                            height = use.height;
                        }
                        else {
                            height = new SVG.Length(100.0f, SVG.Unit.percent);
                        }
                        this.statePush();
                        this.render((SVG.Symbol)resolveIRI, width, height);
                        this.statePop();
                    }
                    else {
                        this.render(resolveIRI);
                    }
                    this.parentPop();
                    if (pushLayer) {
                        this.popLayer(use);
                    }
                    this.updateParentBoundingBox(use);
                }
            }
        }
    }
    
    private void renderChildren(final SVG.SvgContainer svgContainer, final boolean b) {
        if (b) {
            this.parentPush(svgContainer);
        }
        final Iterator<SVG.SvgObject> iterator = svgContainer.getChildren().iterator();
        while (iterator.hasNext()) {
            this.render(iterator.next());
        }
        if (b) {
            this.parentPop();
        }
    }
    
    private void renderMarker(final SVG.Marker marker, final MarkerVector markerVector) {
        final float n = 0.0f;
        this.statePush();
        float floatValue = n;
        Label_0069: {
            if (marker.orient != null) {
                if (Float.isNaN(marker.orient)) {
                    if (markerVector.dx == 0.0f) {
                        floatValue = n;
                        if (markerVector.dy == 0.0f) {
                            break Label_0069;
                        }
                    }
                    floatValue = (float)Math.toDegrees(Math.atan2(markerVector.dy, markerVector.dx));
                }
                else {
                    floatValue = marker.orient;
                }
            }
        }
        float floatValue2;
        if (marker.markerUnitsAreUser) {
            floatValue2 = 1.0f;
        }
        else {
            floatValue2 = this.state.style.strokeWidth.floatValue(this.dpi);
        }
        this.state = this.findInheritFromAncestorState(marker);
        final Matrix matrix = new Matrix();
        matrix.preTranslate(markerVector.x, markerVector.y);
        matrix.preRotate(floatValue);
        matrix.preScale(floatValue2, floatValue2);
        float floatValueX;
        if (marker.refX != null) {
            floatValueX = marker.refX.floatValueX(this);
        }
        else {
            floatValueX = 0.0f;
        }
        float floatValueY;
        if (marker.refY != null) {
            floatValueY = marker.refY.floatValueY(this);
        }
        else {
            floatValueY = 0.0f;
        }
        float floatValueX2;
        if (marker.markerWidth != null) {
            floatValueX2 = marker.markerWidth.floatValueX(this);
        }
        else {
            floatValueX2 = 3.0f;
        }
        float floatValueY2;
        if (marker.markerHeight != null) {
            floatValueY2 = marker.markerHeight.floatValueY(this);
        }
        else {
            floatValueY2 = 3.0f;
        }
        if (marker.viewBox != null) {
            final float n2 = floatValueX2 / marker.viewBox.width;
            final float n3 = floatValueY2 / marker.viewBox.height;
            PreserveAspectRatio preserveAspectRatio;
            if (marker.preserveAspectRatio != null) {
                preserveAspectRatio = marker.preserveAspectRatio;
            }
            else {
                preserveAspectRatio = PreserveAspectRatio.LETTERBOX;
            }
            float n4 = n2;
            float n5 = n3;
            if (!preserveAspectRatio.equals(PreserveAspectRatio.STRETCH)) {
                float n6;
                if (preserveAspectRatio.getScale() == PreserveAspectRatio.Scale.Slice) {
                    n6 = Math.max(n2, n3);
                }
                else {
                    n6 = Math.min(n2, n3);
                }
                n5 = n6;
                n4 = n6;
            }
            matrix.preTranslate(-floatValueX * n4, -floatValueY * n5);
            this.canvas.concat(matrix);
            final float n7 = marker.viewBox.width * n4;
            final float n8 = marker.viewBox.height * n5;
            final float n9 = 0.0f;
            float n10 = 0.0f;
            float n11 = n9;
            Label_0400: {
                switch ($SWITCH_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment()[preserveAspectRatio.getAlignment().ordinal()]) {
                    default: {
                        n11 = n9;
                        break Label_0400;
                    }
                    case 4:
                    case 7:
                    case 10: {
                        n11 = 0.0f - (floatValueX2 - n7);
                        break Label_0400;
                    }
                    case 3:
                    case 6:
                    case 9: {
                        n11 = 0.0f - (floatValueX2 - n7) / 2.0f;
                    }
                    case 5:
                    case 8: {
                        switch ($SWITCH_TABLE$com$caverock$androidsvg$PreserveAspectRatio$Alignment()[preserveAspectRatio.getAlignment().ordinal()]) {
                            case 5:
                            case 6:
                            case 7: {
                                n10 = 0.0f - (floatValueY2 - n8) / 2.0f;
                                break;
                            }
                            case 8:
                            case 9:
                            case 10: {
                                n10 = 0.0f - (floatValueY2 - n8);
                                break;
                            }
                        }
                        if (!this.state.style.overflow) {
                            this.setClipRect(n11, n10, floatValueX2, floatValueY2);
                        }
                        matrix.reset();
                        matrix.preScale(n4, n5);
                        this.canvas.concat(matrix);
                        break;
                    }
                }
            }
        }
        else {
            matrix.preTranslate(-floatValueX, -floatValueY);
            this.canvas.concat(matrix);
            if (!this.state.style.overflow) {
                this.setClipRect(0.0f, 0.0f, floatValueX2, floatValueY2);
            }
        }
        final boolean pushLayer = this.pushLayer();
        this.renderChildren(marker, false);
        if (pushLayer) {
            this.popLayer(marker);
        }
        this.statePop();
    }
    
    private void renderMarkers(final SVG.GraphicsElement graphicsElement) {
        if (this.state.style.markerStart != null || this.state.style.markerMid != null || this.state.style.markerEnd != null) {
            final SVG.Marker marker = null;
            final SVG.Marker marker2 = null;
            final SVG.Marker marker3 = null;
            SVG.Marker marker4 = marker;
            if (this.state.style.markerStart != null) {
                final SVG.SvgObject resolveIRI = graphicsElement.document.resolveIRI(this.state.style.markerStart);
                if (resolveIRI != null) {
                    marker4 = (SVG.Marker)resolveIRI;
                }
                else {
                    error("Marker reference '%s' not found", this.state.style.markerStart);
                    marker4 = marker;
                }
            }
            SVG.Marker marker5 = marker2;
            if (this.state.style.markerMid != null) {
                final SVG.SvgObject resolveIRI2 = graphicsElement.document.resolveIRI(this.state.style.markerMid);
                if (resolveIRI2 != null) {
                    marker5 = (SVG.Marker)resolveIRI2;
                }
                else {
                    error("Marker reference '%s' not found", this.state.style.markerMid);
                    marker5 = marker2;
                }
            }
            SVG.Marker marker6 = marker3;
            if (this.state.style.markerEnd != null) {
                final SVG.SvgObject resolveIRI3 = graphicsElement.document.resolveIRI(this.state.style.markerEnd);
                if (resolveIRI3 != null) {
                    marker6 = (SVG.Marker)resolveIRI3;
                }
                else {
                    error("Marker reference '%s' not found", this.state.style.markerEnd);
                    marker6 = marker3;
                }
            }
            List<MarkerVector> list;
            if (graphicsElement instanceof SVG.Path) {
                list = new MarkerPositionCalculator(((SVG.Path)graphicsElement).d).getMarkers();
            }
            else if (graphicsElement instanceof SVG.Line) {
                list = this.calculateMarkerPositions((SVG.Line)graphicsElement);
            }
            else {
                list = this.calculateMarkerPositions((SVG.PolyLine)graphicsElement);
            }
            if (list != null) {
                final int size = list.size();
                if (size != 0) {
                    final SVG.Style style = this.state.style;
                    final SVG.Style style2 = this.state.style;
                    this.state.style.markerEnd = null;
                    style2.markerMid = null;
                    style.markerStart = null;
                    if (marker4 != null) {
                        this.renderMarker(marker4, list.get(0));
                    }
                    if (marker5 != null) {
                        for (int i = 1; i < size - 1; ++i) {
                            this.renderMarker(marker5, list.get(i));
                        }
                    }
                    if (marker6 != null) {
                        this.renderMarker(marker6, list.get(size - 1));
                    }
                }
            }
        }
    }
    
    private void renderMask(final SVG.Mask mask, final SVG.SvgElement svgElement) {
        debug("Mask render", new Object[0]);
        int n;
        if (mask.maskUnitsAreUser != null && mask.maskUnitsAreUser) {
            n = 1;
        }
        else {
            n = 0;
        }
        float n2;
        float n3;
        if (n != 0) {
            if (mask.width != null) {
                n2 = mask.width.floatValueX(this);
            }
            else {
                n2 = svgElement.boundingBox.width;
            }
            if (mask.height != null) {
                n3 = mask.height.floatValueY(this);
            }
            else {
                n3 = svgElement.boundingBox.height;
            }
            if (mask.x != null) {
                mask.x.floatValueX(this);
            }
            else {
                final float n4 = (float)(svgElement.boundingBox.minX - 0.1 * svgElement.boundingBox.width);
            }
            if (mask.y != null) {
                mask.y.floatValueY(this);
            }
            else {
                final float n5 = (float)(svgElement.boundingBox.minY - 0.1 * svgElement.boundingBox.height);
            }
        }
        else {
            if (mask.x != null) {
                mask.x.floatValue(this, 1.0f);
            }
            if (mask.y != null) {
                mask.y.floatValue(this, 1.0f);
            }
            float floatValue;
            if (mask.width != null) {
                floatValue = mask.width.floatValue(this, 1.0f);
            }
            else {
                floatValue = 1.2f;
            }
            float floatValue2;
            if (mask.height != null) {
                floatValue2 = mask.height.floatValue(this, 1.0f);
            }
            else {
                floatValue2 = 1.2f;
            }
            final float minX = svgElement.boundingBox.minX;
            final float width = svgElement.boundingBox.width;
            final float minY = svgElement.boundingBox.minY;
            final float height = svgElement.boundingBox.height;
            n2 = floatValue * svgElement.boundingBox.width;
            n3 = floatValue2 * svgElement.boundingBox.height;
        }
        if (n2 != 0.0f && n3 != 0.0f) {
            this.statePush();
            this.state = this.findInheritFromAncestorState(mask);
            this.state.style.opacity = 1.0f;
            int n6;
            if (mask.maskContentUnitsAreUser != null && !mask.maskContentUnitsAreUser) {
                n6 = 0;
            }
            else {
                n6 = 1;
            }
            if (n6 == 0) {
                this.canvas.translate(svgElement.boundingBox.minX, svgElement.boundingBox.minY);
                this.canvas.scale(svgElement.boundingBox.width, svgElement.boundingBox.height);
            }
            this.renderChildren(mask, false);
            this.statePop();
        }
    }
    
    private void renderSwitchChild(final SVG.Switch switch1) {
        final String language = Locale.getDefault().getLanguage();
        final SVGExternalFileResolver fileResolver = this.document.getFileResolver();
    Label_0026:
        for (final SVG.SvgObject svgObject : ((SVG.SvgConditionalContainer)switch1).getChildren()) {
            if (svgObject instanceof SVG.SvgConditional) {
                final SVG.SvgConditional svgConditional = (SVG.SvgConditional)svgObject;
                if (svgConditional.getRequiredExtensions() != null) {
                    continue;
                }
                final Set<String> systemLanguage = svgConditional.getSystemLanguage();
                if (systemLanguage != null && (systemLanguage.isEmpty() || !systemLanguage.contains(language))) {
                    continue;
                }
                final Set<String> requiredFeatures = svgConditional.getRequiredFeatures();
                if (requiredFeatures == null || (!requiredFeatures.isEmpty() && SVGParser.supportedFeatures.containsAll(requiredFeatures))) {
                    final Set<String> requiredFormats = svgConditional.getRequiredFormats();
                    if (requiredFormats != null) {
                        if (requiredFormats.isEmpty() || fileResolver == null) {
                            continue;
                        }
                        final Iterator<String> iterator2 = requiredFormats.iterator();
                        while (iterator2.hasNext()) {
                            if (!fileResolver.isFormatSupported(iterator2.next())) {
                                continue Label_0026;
                            }
                        }
                    }
                    final Set<String> requiredFonts = svgConditional.getRequiredFonts();
                    if (requiredFonts != null) {
                        if (requiredFonts.isEmpty() || fileResolver == null) {
                            continue;
                        }
                        final Iterator<String> iterator3 = requiredFonts.iterator();
                        while (iterator3.hasNext()) {
                            if (fileResolver.resolveFont(iterator3.next(), this.state.style.fontWeight, String.valueOf(this.state.style.fontStyle)) == null) {
                                continue Label_0026;
                            }
                        }
                    }
                    this.render(svgObject);
                    break;
                }
                continue;
            }
        }
    }
    
    private void renderTextPath(final SVG.TextPath textPath) {
        debug("TextPath render", new Object[0]);
        this.updateStyleForElement(this.state, textPath);
        if (this.display() && this.visible()) {
            final SVG.SvgObject resolveIRI = textPath.document.resolveIRI(textPath.href);
            if (resolveIRI == null) {
                error("TextPath reference '%s' not found", textPath.href);
            }
            else {
                final SVG.Path path = (SVG.Path)resolveIRI;
                final Path path2 = new PathConverter(path.d).getPath();
                if (path.transform != null) {
                    path2.transform(path.transform);
                }
                final PathMeasure pathMeasure = new PathMeasure(path2, false);
                float floatValue;
                if (textPath.startOffset != null) {
                    floatValue = textPath.startOffset.floatValue(this, pathMeasure.getLength());
                }
                else {
                    floatValue = 0.0f;
                }
                final SVG.Style.TextAnchor anchorPosition = this.getAnchorPosition();
                float n = floatValue;
                if (anchorPosition != SVG.Style.TextAnchor.Start) {
                    final float calculateTextWidth = this.calculateTextWidth(textPath);
                    if (anchorPosition == SVG.Style.TextAnchor.Middle) {
                        n = floatValue - calculateTextWidth / 2.0f;
                    }
                    else {
                        n = floatValue - calculateTextWidth;
                    }
                }
                this.checkForGradiantsAndPatterns((SVG.SvgElement)textPath.getTextRoot());
                final boolean pushLayer = this.pushLayer();
                this.enumerateTextSpans(textPath, (TextProcessor)new PathTextDrawer(path2, n, 0.0f));
                if (pushLayer) {
                    this.popLayer(textPath);
                }
            }
        }
    }
    
    private boolean requiresCompositing() {
        final boolean b = false;
        if (this.state.style.mask != null && !this.state.directRendering) {
            warn("Masks are not supported when using getPicture()", new Object[0]);
        }
        if (this.state.style.opacity < 1.0f) {
            return true;
        }
        boolean b2 = b;
        if (this.state.style.mask != null) {
            if (this.state.directRendering) {
                return true;
            }
            b2 = b;
        }
        return b2;
        b2 = true;
        return b2;
    }
    
    private void resetState() {
        this.state = new RendererState();
        this.stateStack = new Stack<RendererState>();
        this.updateStyle(this.state, SVG.Style.getDefaultStyle());
        this.state.viewPort = this.canvasViewPort;
        this.state.spacePreserve = false;
        this.state.directRendering = this.directRenderingMode;
        this.stateStack.push((RendererState)this.state.clone());
        this.canvasStack = new Stack<Canvas>();
        this.bitmapStack = new Stack<Bitmap>();
        this.matrixStack = new Stack<Matrix>();
        this.parentStack = new Stack<SVG.SvgContainer>();
    }
    
    private void setClipRect(float n, float n2, float n3, float n4) {
        final float n5 = n;
        final float n6 = n2;
        final float n7 = n + n3;
        final float n8 = n4 += n2;
        n3 = n5;
        n2 = n7;
        n = n6;
        if (this.state.style.clip != null) {
            n3 = n5 + this.state.style.clip.left.floatValueX(this);
            n = n6 + this.state.style.clip.top.floatValueY(this);
            n2 = n7 - this.state.style.clip.right.floatValueX(this);
            n4 = n8 - this.state.style.clip.bottom.floatValueY(this);
        }
        this.canvas.clipRect(n3, n, n2, n4);
    }
    
    private void setPaintColour(final RendererState rendererState, final boolean b, final SVG.SvgPaint svgPaint) {
        Float n;
        if (b) {
            n = rendererState.style.fillOpacity;
        }
        else {
            n = rendererState.style.strokeOpacity;
        }
        final float floatValue = n;
        int n2;
        if (svgPaint instanceof SVG.Colour) {
            n2 = ((SVG.Colour)svgPaint).colour;
        }
        else {
            if (!(svgPaint instanceof SVG.CurrentColor)) {
                return;
            }
            n2 = rendererState.style.color.colour;
        }
        final int n3 = n2 | this.clamp255(floatValue) << 24;
        if (b) {
            rendererState.fillPaint.setColor(n3);
        }
        else {
            rendererState.strokePaint.setColor(n3);
        }
    }
    
    private void setSolidColor(final boolean b, final SVG.SolidColor solidColor) {
        final boolean b2 = true;
        boolean hasFill = true;
        if (b) {
            if (this.isSpecified(solidColor.baseStyle, 2147483648L)) {
                this.state.style.fill = solidColor.baseStyle.solidColor;
                final RendererState state = this.state;
                if (solidColor.baseStyle.solidColor == null) {
                    hasFill = false;
                }
                state.hasFill = hasFill;
            }
            if (this.isSpecified(solidColor.baseStyle, 4294967296L)) {
                this.state.style.fillOpacity = solidColor.baseStyle.solidOpacity;
            }
            if (this.isSpecified(solidColor.baseStyle, 6442450944L)) {
                this.setPaintColour(this.state, b, this.state.style.fill);
            }
        }
        else {
            if (this.isSpecified(solidColor.baseStyle, 2147483648L)) {
                this.state.style.stroke = solidColor.baseStyle.solidColor;
                this.state.hasStroke = (solidColor.baseStyle.solidColor != null && b2);
            }
            if (this.isSpecified(solidColor.baseStyle, 4294967296L)) {
                this.state.style.strokeOpacity = solidColor.baseStyle.solidOpacity;
            }
            if (this.isSpecified(solidColor.baseStyle, 6442450944L)) {
                this.setPaintColour(this.state, b, this.state.style.stroke);
            }
        }
    }
    
    private void statePop() {
        this.canvas.restore();
        this.state = this.stateStack.pop();
    }
    
    private void statePush() {
        this.canvas.save();
        this.stateStack.push(this.state);
        this.state = (RendererState)this.state.clone();
    }
    
    private String textXMLSpaceTransform(String s, final boolean b, final boolean b2) {
        if (this.state.spacePreserve) {
            s = s.replaceAll("[\\n\\t]", " ");
        }
        else {
            final String s2 = s = s.replaceAll("\\n", "").replaceAll("\\t", " ");
            if (b) {
                s = s2.replaceAll("^\\s+", "");
            }
            String replaceAll = s;
            if (b2) {
                replaceAll = s.replaceAll("\\s+$", "");
            }
            s = replaceAll.replaceAll("\\s{2,}", " ");
        }
        return s;
    }
    
    private void updateParentBoundingBox(final SVG.SvgElement svgElement) {
        if (svgElement.parent != null && svgElement.boundingBox != null) {
            final Matrix matrix = new Matrix();
            if (this.matrixStack.peek().invert(matrix)) {
                final float[] array = { svgElement.boundingBox.minX, svgElement.boundingBox.minY, svgElement.boundingBox.maxX(), svgElement.boundingBox.minY, svgElement.boundingBox.maxX(), svgElement.boundingBox.maxY(), svgElement.boundingBox.minX, svgElement.boundingBox.maxY() };
                matrix.preConcat(this.canvas.getMatrix());
                matrix.mapPoints(array);
                final RectF rectF = new RectF(array[0], array[1], array[0], array[1]);
                for (int i = 2; i <= 6; i += 2) {
                    if (array[i] < rectF.left) {
                        rectF.left = array[i];
                    }
                    if (array[i] > rectF.right) {
                        rectF.right = array[i];
                    }
                    if (array[i + 1] < rectF.top) {
                        rectF.top = array[i + 1];
                    }
                    if (array[i + 1] > rectF.bottom) {
                        rectF.bottom = array[i + 1];
                    }
                }
                final SVG.SvgElement svgElement2 = (SVG.SvgElement)this.parentStack.peek();
                if (svgElement2.boundingBox == null) {
                    svgElement2.boundingBox = SVG.Box.fromLimits(rectF.left, rectF.top, rectF.right, rectF.bottom);
                }
                else {
                    svgElement2.boundingBox.union(SVG.Box.fromLimits(rectF.left, rectF.top, rectF.right, rectF.bottom));
                }
            }
        }
    }
    
    private void updateStyle(final RendererState rendererState, final SVG.Style style) {
        if (this.isSpecified(style, 4096L)) {
            rendererState.style.color = style.color;
        }
        if (this.isSpecified(style, 2048L)) {
            rendererState.style.opacity = style.opacity;
        }
        if (this.isSpecified(style, 1L)) {
            rendererState.style.fill = style.fill;
            rendererState.hasFill = (style.fill != null);
        }
        if (this.isSpecified(style, 4L)) {
            rendererState.style.fillOpacity = style.fillOpacity;
        }
        if (this.isSpecified(style, 6149L)) {
            this.setPaintColour(rendererState, true, rendererState.style.fill);
        }
        if (this.isSpecified(style, 2L)) {
            rendererState.style.fillRule = style.fillRule;
        }
        if (this.isSpecified(style, 8L)) {
            rendererState.style.stroke = style.stroke;
            rendererState.hasStroke = (style.stroke != null);
        }
        if (this.isSpecified(style, 16L)) {
            rendererState.style.strokeOpacity = style.strokeOpacity;
        }
        if (this.isSpecified(style, 6168L)) {
            this.setPaintColour(rendererState, false, rendererState.style.stroke);
        }
        if (this.isSpecified(style, 34359738368L)) {
            rendererState.style.vectorEffect = style.vectorEffect;
        }
        if (this.isSpecified(style, 32L)) {
            rendererState.style.strokeWidth = style.strokeWidth;
            rendererState.strokePaint.setStrokeWidth(rendererState.style.strokeWidth.floatValue(this));
        }
        if (this.isSpecified(style, 64L)) {
            rendererState.style.strokeLineCap = style.strokeLineCap;
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVG$Style$LineCaps()[style.strokeLineCap.ordinal()]) {
                case 1: {
                    rendererState.strokePaint.setStrokeCap(Paint$Cap.BUTT);
                    break;
                }
                case 2: {
                    rendererState.strokePaint.setStrokeCap(Paint$Cap.ROUND);
                    break;
                }
                case 3: {
                    rendererState.strokePaint.setStrokeCap(Paint$Cap.SQUARE);
                    break;
                }
            }
        }
        if (this.isSpecified(style, 128L)) {
            rendererState.style.strokeLineJoin = style.strokeLineJoin;
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVG$Style$LineJoin()[style.strokeLineJoin.ordinal()]) {
                case 1: {
                    rendererState.strokePaint.setStrokeJoin(Paint$Join.MITER);
                    break;
                }
                case 2: {
                    rendererState.strokePaint.setStrokeJoin(Paint$Join.ROUND);
                    break;
                }
                case 3: {
                    rendererState.strokePaint.setStrokeJoin(Paint$Join.BEVEL);
                    break;
                }
            }
        }
        if (this.isSpecified(style, 256L)) {
            rendererState.style.strokeMiterLimit = style.strokeMiterLimit;
            rendererState.strokePaint.setStrokeMiter((float)style.strokeMiterLimit);
        }
        if (this.isSpecified(style, 512L)) {
            rendererState.style.strokeDashArray = style.strokeDashArray;
        }
        if (this.isSpecified(style, 1024L)) {
            rendererState.style.strokeDashOffset = style.strokeDashOffset;
        }
        if (this.isSpecified(style, 1536L)) {
            if (rendererState.style.strokeDashArray == null) {
                rendererState.strokePaint.setPathEffect((PathEffect)null);
            }
            else {
                float n = 0.0f;
                final int length = rendererState.style.strokeDashArray.length;
                int n2;
                if (length % 2 == 0) {
                    n2 = length;
                }
                else {
                    n2 = length * 2;
                }
                final float[] array = new float[n2];
                for (int i = 0; i < n2; ++i) {
                    array[i] = rendererState.style.strokeDashArray[i % length].floatValue(this);
                    n += array[i];
                }
                if (n == 0.0f) {
                    rendererState.strokePaint.setPathEffect((PathEffect)null);
                }
                else {
                    float floatValue;
                    final float n3 = floatValue = rendererState.style.strokeDashOffset.floatValue(this);
                    if (n3 < 0.0f) {
                        floatValue = n + n3 % n;
                    }
                    rendererState.strokePaint.setPathEffect((PathEffect)new DashPathEffect(array, floatValue));
                }
            }
        }
        if (this.isSpecified(style, 16384L)) {
            final float currentFontSize = this.getCurrentFontSize();
            rendererState.style.fontSize = style.fontSize;
            rendererState.fillPaint.setTextSize(style.fontSize.floatValue(this, currentFontSize));
            rendererState.strokePaint.setTextSize(style.fontSize.floatValue(this, currentFontSize));
        }
        if (this.isSpecified(style, 8192L)) {
            rendererState.style.fontFamily = style.fontFamily;
        }
        if (this.isSpecified(style, 32768L)) {
            if (style.fontWeight == -1 && rendererState.style.fontWeight > 100) {
                final SVG.Style style2 = rendererState.style;
                style2.fontWeight -= 100;
            }
            else if (style.fontWeight == 1 && rendererState.style.fontWeight < 900) {
                final SVG.Style style3 = rendererState.style;
                style3.fontWeight += 100;
            }
            else {
                rendererState.style.fontWeight = style.fontWeight;
            }
        }
        if (this.isSpecified(style, 65536L)) {
            rendererState.style.fontStyle = style.fontStyle;
        }
        if (this.isSpecified(style, 106496L)) {
            final Typeface typeface = null;
            Typeface typeface2 = null;
            Typeface resolveFont = typeface;
            Label_0765: {
                if (rendererState.style.fontFamily != null) {
                    resolveFont = typeface;
                    if (this.document != null) {
                        final SVGExternalFileResolver fileResolver = this.document.getFileResolver();
                        for (final String s : rendererState.style.fontFamily) {
                            final Typeface checkGenericFont = this.checkGenericFont(s, rendererState.style.fontWeight, rendererState.style.fontStyle);
                            if ((resolveFont = checkGenericFont) == null) {
                                resolveFont = checkGenericFont;
                                if (fileResolver != null) {
                                    resolveFont = fileResolver.resolveFont(s, rendererState.style.fontWeight, String.valueOf(rendererState.style.fontStyle));
                                }
                            }
                            if ((typeface2 = resolveFont) != null) {
                                break Label_0765;
                            }
                        }
                        resolveFont = typeface2;
                    }
                }
            }
            Typeface checkGenericFont2;
            if ((checkGenericFont2 = resolveFont) == null) {
                checkGenericFont2 = this.checkGenericFont("sans-serif", rendererState.style.fontWeight, rendererState.style.fontStyle);
            }
            rendererState.fillPaint.setTypeface(checkGenericFont2);
            rendererState.strokePaint.setTypeface(checkGenericFont2);
        }
        if (this.isSpecified(style, 131072L)) {
            rendererState.style.textDecoration = style.textDecoration;
            rendererState.fillPaint.setStrikeThruText(style.textDecoration == SVG.Style.TextDecoration.LineThrough);
            rendererState.fillPaint.setUnderlineText(style.textDecoration == SVG.Style.TextDecoration.Underline);
            if (Build$VERSION.SDK_INT >= 17) {
                rendererState.strokePaint.setStrikeThruText(style.textDecoration == SVG.Style.TextDecoration.LineThrough);
                rendererState.strokePaint.setUnderlineText(style.textDecoration == SVG.Style.TextDecoration.Underline);
            }
        }
        if (this.isSpecified(style, 68719476736L)) {
            rendererState.style.direction = style.direction;
        }
        if (this.isSpecified(style, 262144L)) {
            rendererState.style.textAnchor = style.textAnchor;
        }
        if (this.isSpecified(style, 524288L)) {
            rendererState.style.overflow = style.overflow;
        }
        if (this.isSpecified(style, 2097152L)) {
            rendererState.style.markerStart = style.markerStart;
        }
        if (this.isSpecified(style, 4194304L)) {
            rendererState.style.markerMid = style.markerMid;
        }
        if (this.isSpecified(style, 8388608L)) {
            rendererState.style.markerEnd = style.markerEnd;
        }
        if (this.isSpecified(style, 16777216L)) {
            rendererState.style.display = style.display;
        }
        if (this.isSpecified(style, 33554432L)) {
            rendererState.style.visibility = style.visibility;
        }
        if (this.isSpecified(style, 1048576L)) {
            rendererState.style.clip = style.clip;
        }
        if (this.isSpecified(style, 268435456L)) {
            rendererState.style.clipPath = style.clipPath;
        }
        if (this.isSpecified(style, 536870912L)) {
            rendererState.style.clipRule = style.clipRule;
        }
        if (this.isSpecified(style, 1073741824L)) {
            rendererState.style.mask = style.mask;
        }
        if (this.isSpecified(style, 67108864L)) {
            rendererState.style.stopColor = style.stopColor;
        }
        if (this.isSpecified(style, 134217728L)) {
            rendererState.style.stopOpacity = style.stopOpacity;
        }
        if (this.isSpecified(style, 8589934592L)) {
            rendererState.style.viewportFill = style.viewportFill;
        }
        if (this.isSpecified(style, 17179869184L)) {
            rendererState.style.viewportFillOpacity = style.viewportFillOpacity;
        }
    }
    
    private void updateStyleForElement(final RendererState rendererState, final SVG.SvgElementBase svgElementBase) {
        rendererState.style.resetNonInheritingProperties(svgElementBase.parent == null);
        if (svgElementBase.baseStyle != null) {
            this.updateStyle(rendererState, svgElementBase.baseStyle);
        }
        if (this.document.hasCSSRules()) {
            for (final CSSParser.Rule rule : this.document.getCSSRules()) {
                if (CSSParser.ruleMatch(rule.selector, svgElementBase)) {
                    this.updateStyle(rendererState, rule.style);
                }
            }
        }
        if (svgElementBase.style != null) {
            this.updateStyle(rendererState, svgElementBase.style);
        }
    }
    
    private void viewportFill() {
        int n;
        if (this.state.style.viewportFill instanceof SVG.Colour) {
            n = ((SVG.Colour)this.state.style.viewportFill).colour;
        }
        else {
            if (!(this.state.style.viewportFill instanceof SVG.CurrentColor)) {
                return;
            }
            n = this.state.style.color.colour;
        }
        int n2 = n;
        if (this.state.style.viewportFillOpacity != null) {
            n2 = (n | this.clamp255(this.state.style.viewportFillOpacity) << 24);
        }
        this.canvas.drawColor(n2);
    }
    
    private boolean visible() {
        return this.state.style.visibility == null || this.state.style.visibility;
    }
    
    private static void warn(final String format, final Object... args) {
        Log.w("SVGAndroidRenderer", String.format(format, args));
    }
    
    protected float getCurrentFontSize() {
        return this.state.fillPaint.getTextSize();
    }
    
    protected float getCurrentFontXHeight() {
        return this.state.fillPaint.getTextSize() / 2.0f;
    }
    
    protected SVG.Box getCurrentViewPortInUserUnits() {
        SVG.Box box;
        if (this.state.viewBox != null) {
            box = this.state.viewBox;
        }
        else {
            box = this.state.viewPort;
        }
        return box;
    }
    
    protected float getDPI() {
        return this.dpi;
    }
    
    protected void renderDocument(final SVG document, final SVG.Box box, final PreserveAspectRatio preserveAspectRatio, final boolean directRenderingMode) {
        this.document = document;
        this.directRenderingMode = directRenderingMode;
        final SVG.Svg rootElement = document.getRootElement();
        if (rootElement == null) {
            warn("Nothing to render. Document is empty.", new Object[0]);
        }
        else {
            this.resetState();
            this.checkXMLSpaceAttribute(rootElement);
            final SVG.Length width = rootElement.width;
            final SVG.Length height = rootElement.height;
            SVG.Box viewBox;
            if (box != null) {
                viewBox = box;
            }
            else {
                viewBox = rootElement.viewBox;
            }
            PreserveAspectRatio preserveAspectRatio2;
            if (preserveAspectRatio != null) {
                preserveAspectRatio2 = preserveAspectRatio;
            }
            else {
                preserveAspectRatio2 = rootElement.preserveAspectRatio;
            }
            this.render(rootElement, width, height, viewBox, preserveAspectRatio2);
        }
    }
    
    private class MarkerPositionCalculator implements PathInterface
    {
        private boolean closepathReAdjustPending;
        private MarkerVector lastPos;
        private List<MarkerVector> markers;
        private boolean normalCubic;
        private boolean startArc;
        private float startX;
        private float startY;
        private int subpathStartIndex;
        
        public MarkerPositionCalculator(final PathDefinition pathDefinition) {
            this.markers = new ArrayList<MarkerVector>();
            this.lastPos = null;
            this.startArc = false;
            this.normalCubic = true;
            this.subpathStartIndex = -1;
            pathDefinition.enumeratePath(this);
            if (this.closepathReAdjustPending) {
                this.lastPos.add((MarkerVector)this.markers.get(this.subpathStartIndex));
                this.markers.set(this.subpathStartIndex, this.lastPos);
                this.closepathReAdjustPending = false;
            }
            if (this.lastPos != null) {
                this.markers.add(this.lastPos);
            }
        }
        
        @Override
        public void arcTo(final float n, final float n2, final float n3, final boolean b, final boolean b2, final float n4, final float n5) {
            this.startArc = true;
            this.normalCubic = false;
            arcTo(this.lastPos.x, this.lastPos.y, n, n2, n3, b, b2, n4, n5, this);
            this.normalCubic = true;
            this.closepathReAdjustPending = false;
        }
        
        @Override
        public void close() {
            this.markers.add(this.lastPos);
            this.lineTo(this.startX, this.startY);
            this.closepathReAdjustPending = true;
        }
        
        @Override
        public void cubicTo(final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
            if (this.normalCubic || this.startArc) {
                this.lastPos.add(n, n2);
                this.markers.add(this.lastPos);
                this.startArc = false;
            }
            this.lastPos = new MarkerVector(n5, n6, n5 - n3, n6 - n4);
            this.closepathReAdjustPending = false;
        }
        
        public List<MarkerVector> getMarkers() {
            return this.markers;
        }
        
        @Override
        public void lineTo(final float n, final float n2) {
            this.lastPos.add(n, n2);
            this.markers.add(this.lastPos);
            this.lastPos = new MarkerVector(n, n2, n - this.lastPos.x, n2 - this.lastPos.y);
            this.closepathReAdjustPending = false;
        }
        
        @Override
        public void moveTo(final float startX, final float startY) {
            if (this.closepathReAdjustPending) {
                this.lastPos.add((MarkerVector)this.markers.get(this.subpathStartIndex));
                this.markers.set(this.subpathStartIndex, this.lastPos);
                this.closepathReAdjustPending = false;
            }
            if (this.lastPos != null) {
                this.markers.add(this.lastPos);
            }
            this.startX = startX;
            this.startY = startY;
            this.lastPos = new MarkerVector(startX, startY, 0.0f, 0.0f);
            this.subpathStartIndex = this.markers.size();
        }
        
        @Override
        public void quadTo(final float n, final float n2, final float n3, final float n4) {
            this.lastPos.add(n, n2);
            this.markers.add(this.lastPos);
            this.lastPos = new MarkerVector(n3, n4, n3 - n, n4 - n2);
            this.closepathReAdjustPending = false;
        }
    }
    
    private class MarkerVector
    {
        public float dx;
        public float dy;
        public float x;
        public float y;
        
        public MarkerVector(final float x, final float y, final float n, final float n2) {
            this.dx = 0.0f;
            this.dy = 0.0f;
            this.x = x;
            this.y = y;
            final double sqrt = Math.sqrt(n * n + n2 * n2);
            if (sqrt != 0.0) {
                this.dx = (float)(n / sqrt);
                this.dy = (float)(n2 / sqrt);
            }
        }
        
        public void add(float n, float n2) {
            n -= this.x;
            n2 -= this.y;
            final double sqrt = Math.sqrt(n * n + n2 * n2);
            if (sqrt != 0.0) {
                this.dx += (float)(n / sqrt);
                this.dy += (float)(n2 / sqrt);
            }
        }
        
        public void add(final MarkerVector markerVector) {
            this.dx += markerVector.dx;
            this.dy += markerVector.dy;
        }
        
        @Override
        public String toString() {
            return "(" + this.x + "," + this.y + " " + this.dx + "," + this.dy + ")";
        }
    }
    
    private class PathConverter implements PathInterface
    {
        float lastX;
        float lastY;
        android.graphics.Path path;
        
        public PathConverter(final PathDefinition pathDefinition) {
            this.path = new android.graphics.Path();
            pathDefinition.enumeratePath(this);
        }
        
        @Override
        public void arcTo(final float n, final float n2, final float n3, final boolean b, final boolean b2, final float lastX, final float lastY) {
            arcTo(this.lastX, this.lastY, n, n2, n3, b, b2, lastX, lastY, this);
            this.lastX = lastX;
            this.lastY = lastY;
        }
        
        @Override
        public void close() {
            this.path.close();
        }
        
        @Override
        public void cubicTo(final float n, final float n2, final float n3, final float n4, final float lastX, final float lastY) {
            this.path.cubicTo(n, n2, n3, n4, lastX, lastY);
            this.lastX = lastX;
            this.lastY = lastY;
        }
        
        public android.graphics.Path getPath() {
            return this.path;
        }
        
        @Override
        public void lineTo(final float lastX, final float lastY) {
            this.path.lineTo(lastX, lastY);
            this.lastX = lastX;
            this.lastY = lastY;
        }
        
        @Override
        public void moveTo(final float lastX, final float lastY) {
            this.path.moveTo(lastX, lastY);
            this.lastX = lastX;
            this.lastY = lastY;
        }
        
        @Override
        public void quadTo(final float n, final float n2, final float lastX, final float lastY) {
            this.path.quadTo(n, n2, lastX, lastY);
            this.lastX = lastX;
            this.lastY = lastY;
        }
    }
    
    private class PathTextDrawer extends PlainTextDrawer
    {
        private Path path;
        
        public PathTextDrawer(final Path path, final float n, final float n2) {
            super(n, n2);
            this.path = path;
        }
        
        @Override
        public void processText(final String s) {
            if (SVGAndroidRenderer.this.visible()) {
                if (SVGAndroidRenderer.this.state.hasFill) {
                    SVGAndroidRenderer.this.canvas.drawTextOnPath(s, this.path, this.x, this.y, SVGAndroidRenderer.this.state.fillPaint);
                }
                if (SVGAndroidRenderer.this.state.hasStroke) {
                    SVGAndroidRenderer.this.canvas.drawTextOnPath(s, this.path, this.x, this.y, SVGAndroidRenderer.this.state.strokePaint);
                }
            }
            this.x += SVGAndroidRenderer.this.state.fillPaint.measureText(s);
        }
    }
    
    private class PlainTextDrawer extends TextProcessor
    {
        public float x;
        public float y;
        
        public PlainTextDrawer(final float x, final float y) {
            super((TextProcessor)null);
            this.x = x;
            this.y = y;
        }
        
        @Override
        public void processText(final String s) {
            debug("TextSequence render", new Object[0]);
            if (SVGAndroidRenderer.this.visible()) {
                if (SVGAndroidRenderer.this.state.hasFill) {
                    SVGAndroidRenderer.this.canvas.drawText(s, this.x, this.y, SVGAndroidRenderer.this.state.fillPaint);
                }
                if (SVGAndroidRenderer.this.state.hasStroke) {
                    SVGAndroidRenderer.this.canvas.drawText(s, this.x, this.y, SVGAndroidRenderer.this.state.strokePaint);
                }
            }
            this.x += SVGAndroidRenderer.this.state.fillPaint.measureText(s);
        }
    }
    
    private class PlainTextToPath extends TextProcessor
    {
        public Path textAsPath;
        public float x;
        public float y;
        
        public PlainTextToPath(final float x, final float y, final Path textAsPath) {
            super((TextProcessor)null);
            this.x = x;
            this.y = y;
            this.textAsPath = textAsPath;
        }
        
        @Override
        public boolean doTextContainer(final SVG.TextContainer textContainer) {
            boolean b = false;
            if (textContainer instanceof SVG.TextPath) {
                warn("Using <textPath> elements in a clip path is not supported.", new Object[0]);
            }
            else {
                b = true;
            }
            return b;
        }
        
        @Override
        public void processText(final String s) {
            if (SVGAndroidRenderer.this.visible()) {
                final Path path = new Path();
                SVGAndroidRenderer.this.state.fillPaint.getTextPath(s, 0, s.length(), this.x, this.y, path);
                this.textAsPath.addPath(path);
            }
            this.x += SVGAndroidRenderer.this.state.fillPaint.measureText(s);
        }
    }
    
    private class RendererState implements Cloneable
    {
        public boolean directRendering;
        public Paint fillPaint;
        public boolean hasFill;
        public boolean hasStroke;
        public boolean spacePreserve;
        public Paint strokePaint;
        public SVG.Style style;
        public SVG.Box viewBox;
        public SVG.Box viewPort;
        
        public RendererState() {
            (this.fillPaint = new Paint()).setFlags(385);
            this.fillPaint.setStyle(Paint$Style.FILL);
            this.fillPaint.setTypeface(Typeface.DEFAULT);
            (this.strokePaint = new Paint()).setFlags(385);
            this.strokePaint.setStyle(Paint$Style.STROKE);
            this.strokePaint.setTypeface(Typeface.DEFAULT);
            this.style = SVG.Style.getDefaultStyle();
        }
        
        @Override
        protected Object clone() {
            try {
                final RendererState rendererState = (RendererState)super.clone();
                rendererState.style = (SVG.Style)this.style.clone();
                rendererState.fillPaint = new Paint(this.fillPaint);
                rendererState.strokePaint = new Paint(this.strokePaint);
                return rendererState;
            }
            catch (CloneNotSupportedException ex) {
                throw new InternalError(ex.toString());
            }
        }
    }
    
    private class TextBoundsCalculator extends TextProcessor
    {
        RectF bbox;
        float x;
        float y;
        
        public TextBoundsCalculator(final float x, final float y) {
            super((TextProcessor)null);
            this.bbox = new RectF();
            this.x = x;
            this.y = y;
        }
        
        @Override
        public boolean doTextContainer(final SVG.TextContainer textContainer) {
            boolean b = false;
            if (textContainer instanceof SVG.TextPath) {
                final SVG.TextPath textPath = (SVG.TextPath)textContainer;
                final SVG.SvgObject resolveIRI = textContainer.document.resolveIRI(textPath.href);
                if (resolveIRI == null) {
                    error("TextPath path reference '%s' not found", new Object[] { textPath.href });
                }
                else {
                    final SVG.Path path = (SVG.Path)resolveIRI;
                    final Path path2 = new PathConverter(path.d).getPath();
                    if (path.transform != null) {
                        path2.transform(path.transform);
                    }
                    final RectF rectF = new RectF();
                    path2.computeBounds(rectF, true);
                    this.bbox.union(rectF);
                }
            }
            else {
                b = true;
            }
            return b;
        }
        
        @Override
        public void processText(final String s) {
            if (SVGAndroidRenderer.this.visible()) {
                final Rect rect = new Rect();
                SVGAndroidRenderer.this.state.fillPaint.getTextBounds(s, 0, s.length(), rect);
                final RectF rectF = new RectF(rect);
                rectF.offset(this.x, this.y);
                this.bbox.union(rectF);
            }
            this.x += SVGAndroidRenderer.this.state.fillPaint.measureText(s);
        }
    }
    
    private abstract class TextProcessor
    {
        public boolean doTextContainer(final SVG.TextContainer textContainer) {
            return true;
        }
        
        public abstract void processText(final String p0);
    }
    
    private class TextWidthCalculator extends TextProcessor
    {
        public float x;
        
        private TextWidthCalculator() {
            super((TextProcessor)null);
            this.x = 0.0f;
        }
        
        @Override
        public void processText(final String s) {
            this.x += SVGAndroidRenderer.this.state.fillPaint.measureText(s);
        }
    }
}
