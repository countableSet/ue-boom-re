// 
// Decompiled by Procyon v0.5.36
// 

package com.caverock.androidsvg;

import java.util.Collections;
import java.io.InputStream;
import android.graphics.Matrix;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import android.util.Log;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import java.util.HashSet;
import java.util.HashMap;
import org.xml.sax.ext.DefaultHandler2;

public class SVGParser extends DefaultHandler2
{
    private static final String CURRENTCOLOR = "currentColor";
    private static final String FEATURE_STRING_PREFIX = "http://www.w3.org/TR/SVG11/feature#";
    private static final String NONE = "none";
    private static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";
    private static final String TAG = "SVGParser";
    private static final String TAG_A = "a";
    private static final String TAG_CIRCLE = "circle";
    private static final String TAG_CLIPPATH = "clipPath";
    private static final String TAG_DEFS = "defs";
    private static final String TAG_DESC = "desc";
    private static final String TAG_ELLIPSE = "ellipse";
    private static final String TAG_G = "g";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_LINE = "line";
    private static final String TAG_LINEARGRADIENT = "linearGradient";
    private static final String TAG_MARKER = "marker";
    private static final String TAG_MASK = "mask";
    private static final String TAG_PATH = "path";
    private static final String TAG_PATTERN = "pattern";
    private static final String TAG_POLYGON = "polygon";
    private static final String TAG_POLYLINE = "polyline";
    private static final String TAG_RADIALGRADIENT = "radialGradient";
    private static final String TAG_RECT = "rect";
    private static final String TAG_SOLIDCOLOR = "solidColor";
    private static final String TAG_STOP = "stop";
    private static final String TAG_STYLE = "style";
    private static final String TAG_SVG = "svg";
    private static final String TAG_SWITCH = "switch";
    private static final String TAG_SYMBOL = "symbol";
    private static final String TAG_TEXT = "text";
    private static final String TAG_TEXTPATH = "textPath";
    private static final String TAG_TITLE = "title";
    private static final String TAG_TREF = "tref";
    private static final String TAG_TSPAN = "tspan";
    private static final String TAG_USE = "use";
    private static final String TAG_VIEW = "view";
    private static final String VALID_DISPLAY_VALUES = "|inline|block|list-item|run-in|compact|marker|table|inline-table|table-row-group|table-header-group|table-footer-group|table-row|table-column-group|table-column|table-cell|table-caption|none|";
    private static final String VALID_VISIBILITY_VALUES = "|visible|hidden|collapse|";
    private static final String XLINK_NAMESPACE = "http://www.w3.org/1999/xlink";
    private static HashMap<String, PreserveAspectRatio.Alignment> aspectRatioKeywords;
    private static HashMap<String, Integer> colourKeywords;
    private static HashMap<String, SVG.Length> fontSizeKeywords;
    private static HashMap<String, SVG.Style.FontStyle> fontStyleKeywords;
    private static HashMap<String, Integer> fontWeightKeywords;
    protected static HashSet<String> supportedFeatures;
    private SVG.SvgContainer currentElement;
    private int ignoreDepth;
    private boolean ignoring;
    private boolean inMetadataElement;
    private boolean inStyleElement;
    private StringBuilder metadataElementContents;
    private String metadataTag;
    private StringBuilder styleElementContents;
    private HashSet<String> supportedFormats;
    private SVG svgDocument;
    
    static /* synthetic */ int[] $SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr() {
        int[] $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr = SVGParser.$SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr;
        if ($switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr == null) {
            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr = new int[SVGAttr.values().length];
            while (true) {
                try {
                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.CLASS.ordinal()] = 1;
                    try {
                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.UNSUPPORTED.ordinal()] = 92;
                        try {
                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.clip.ordinal()] = 2;
                            try {
                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.clipPathUnits.ordinal()] = 4;
                                try {
                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.clip_path.ordinal()] = 3;
                                    try {
                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.clip_rule.ordinal()] = 5;
                                        try {
                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.color.ordinal()] = 6;
                                            try {
                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.cx.ordinal()] = 7;
                                                try {
                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.cy.ordinal()] = 8;
                                                    try {
                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.d.ordinal()] = 14;
                                                        try {
                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.direction.ordinal()] = 9;
                                                            try {
                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.display.ordinal()] = 15;
                                                                try {
                                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.dx.ordinal()] = 10;
                                                                    try {
                                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.dy.ordinal()] = 11;
                                                                        try {
                                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.fill.ordinal()] = 16;
                                                                            try {
                                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.fill_opacity.ordinal()] = 18;
                                                                                try {
                                                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.fill_rule.ordinal()] = 17;
                                                                                    try {
                                                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.font.ordinal()] = 19;
                                                                                        try {
                                                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.font_family.ordinal()] = 20;
                                                                                            try {
                                                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.font_size.ordinal()] = 21;
                                                                                                try {
                                                                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.font_style.ordinal()] = 23;
                                                                                                    try {
                                                                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.font_weight.ordinal()] = 22;
                                                                                                        try {
                                                                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.fx.ordinal()] = 12;
                                                                                                            try {
                                                                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.fy.ordinal()] = 13;
                                                                                                                try {
                                                                                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.gradientTransform.ordinal()] = 24;
                                                                                                                    try {
                                                                                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.gradientUnits.ordinal()] = 25;
                                                                                                                        try {
                                                                                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.height.ordinal()] = 26;
                                                                                                                            try {
                                                                                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.href.ordinal()] = 27;
                                                                                                                                try {
                                                                                                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.id.ordinal()] = 28;
                                                                                                                                    try {
                                                                                                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.marker.ordinal()] = 29;
                                                                                                                                        try {
                                                                                                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.markerHeight.ordinal()] = 33;
                                                                                                                                            try {
                                                                                                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.markerUnits.ordinal()] = 34;
                                                                                                                                                try {
                                                                                                                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.markerWidth.ordinal()] = 35;
                                                                                                                                                    try {
                                                                                                                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.marker_end.ordinal()] = 32;
                                                                                                                                                        try {
                                                                                                                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.marker_mid.ordinal()] = 31;
                                                                                                                                                            try {
                                                                                                                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.marker_start.ordinal()] = 30;
                                                                                                                                                                try {
                                                                                                                                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.mask.ordinal()] = 36;
                                                                                                                                                                    try {
                                                                                                                                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.maskContentUnits.ordinal()] = 37;
                                                                                                                                                                        try {
                                                                                                                                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.maskUnits.ordinal()] = 38;
                                                                                                                                                                            try {
                                                                                                                                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.media.ordinal()] = 39;
                                                                                                                                                                                try {
                                                                                                                                                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.offset.ordinal()] = 40;
                                                                                                                                                                                    try {
                                                                                                                                                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.opacity.ordinal()] = 41;
                                                                                                                                                                                        try {
                                                                                                                                                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.orient.ordinal()] = 42;
                                                                                                                                                                                            try {
                                                                                                                                                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.overflow.ordinal()] = 43;
                                                                                                                                                                                                try {
                                                                                                                                                                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.pathLength.ordinal()] = 44;
                                                                                                                                                                                                    try {
                                                                                                                                                                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.patternContentUnits.ordinal()] = 45;
                                                                                                                                                                                                        try {
                                                                                                                                                                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.patternTransform.ordinal()] = 46;
                                                                                                                                                                                                            try {
                                                                                                                                                                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.patternUnits.ordinal()] = 47;
                                                                                                                                                                                                                try {
                                                                                                                                                                                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.points.ordinal()] = 48;
                                                                                                                                                                                                                    try {
                                                                                                                                                                                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.preserveAspectRatio.ordinal()] = 49;
                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.r.ordinal()] = 50;
                                                                                                                                                                                                                            try {
                                                                                                                                                                                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.refX.ordinal()] = 51;
                                                                                                                                                                                                                                try {
                                                                                                                                                                                                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.refY.ordinal()] = 52;
                                                                                                                                                                                                                                    try {
                                                                                                                                                                                                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.requiredExtensions.ordinal()] = 54;
                                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.requiredFeatures.ordinal()] = 53;
                                                                                                                                                                                                                                            try {
                                                                                                                                                                                                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.requiredFonts.ordinal()] = 56;
                                                                                                                                                                                                                                                try {
                                                                                                                                                                                                                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.requiredFormats.ordinal()] = 55;
                                                                                                                                                                                                                                                    try {
                                                                                                                                                                                                                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.rx.ordinal()] = 57;
                                                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.ry.ordinal()] = 58;
                                                                                                                                                                                                                                                            try {
                                                                                                                                                                                                                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.solid_color.ordinal()] = 59;
                                                                                                                                                                                                                                                                try {
                                                                                                                                                                                                                                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.solid_opacity.ordinal()] = 60;
                                                                                                                                                                                                                                                                    try {
                                                                                                                                                                                                                                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.spreadMethod.ordinal()] = 61;
                                                                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.startOffset.ordinal()] = 62;
                                                                                                                                                                                                                                                                            try {
                                                                                                                                                                                                                                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.stop_color.ordinal()] = 63;
                                                                                                                                                                                                                                                                                try {
                                                                                                                                                                                                                                                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.stop_opacity.ordinal()] = 64;
                                                                                                                                                                                                                                                                                    try {
                                                                                                                                                                                                                                                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.stroke.ordinal()] = 65;
                                                                                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                                                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.stroke_dasharray.ordinal()] = 66;
                                                                                                                                                                                                                                                                                            try {
                                                                                                                                                                                                                                                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.stroke_dashoffset.ordinal()] = 67;
                                                                                                                                                                                                                                                                                                try {
                                                                                                                                                                                                                                                                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.stroke_linecap.ordinal()] = 68;
                                                                                                                                                                                                                                                                                                    try {
                                                                                                                                                                                                                                                                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.stroke_linejoin.ordinal()] = 69;
                                                                                                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                                                                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.stroke_miterlimit.ordinal()] = 70;
                                                                                                                                                                                                                                                                                                            try {
                                                                                                                                                                                                                                                                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.stroke_opacity.ordinal()] = 71;
                                                                                                                                                                                                                                                                                                                try {
                                                                                                                                                                                                                                                                                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.stroke_width.ordinal()] = 72;
                                                                                                                                                                                                                                                                                                                    try {
                                                                                                                                                                                                                                                                                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.style.ordinal()] = 73;
                                                                                                                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                                                                                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.systemLanguage.ordinal()] = 74;
                                                                                                                                                                                                                                                                                                                            try {
                                                                                                                                                                                                                                                                                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.text_anchor.ordinal()] = 75;
                                                                                                                                                                                                                                                                                                                                try {
                                                                                                                                                                                                                                                                                                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.text_decoration.ordinal()] = 76;
                                                                                                                                                                                                                                                                                                                                    try {
                                                                                                                                                                                                                                                                                                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.transform.ordinal()] = 77;
                                                                                                                                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                                                                                                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.type.ordinal()] = 78;
                                                                                                                                                                                                                                                                                                                                            try {
                                                                                                                                                                                                                                                                                                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.vector_effect.ordinal()] = 79;
                                                                                                                                                                                                                                                                                                                                                try {
                                                                                                                                                                                                                                                                                                                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.version.ordinal()] = 80;
                                                                                                                                                                                                                                                                                                                                                    try {
                                                                                                                                                                                                                                                                                                                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.viewBox.ordinal()] = 81;
                                                                                                                                                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                                                                                                                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.viewport_fill.ordinal()] = 89;
                                                                                                                                                                                                                                                                                                                                                            try {
                                                                                                                                                                                                                                                                                                                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.viewport_fill_opacity.ordinal()] = 90;
                                                                                                                                                                                                                                                                                                                                                                try {
                                                                                                                                                                                                                                                                                                                                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.visibility.ordinal()] = 91;
                                                                                                                                                                                                                                                                                                                                                                    try {
                                                                                                                                                                                                                                                                                                                                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.width.ordinal()] = 82;
                                                                                                                                                                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                                                                                                                                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.x.ordinal()] = 83;
                                                                                                                                                                                                                                                                                                                                                                            try {
                                                                                                                                                                                                                                                                                                                                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.x1.ordinal()] = 85;
                                                                                                                                                                                                                                                                                                                                                                                try {
                                                                                                                                                                                                                                                                                                                                                                                    $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.x2.ordinal()] = 87;
                                                                                                                                                                                                                                                                                                                                                                                    try {
                                                                                                                                                                                                                                                                                                                                                                                        $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.y.ordinal()] = 84;
                                                                                                                                                                                                                                                                                                                                                                                        try {
                                                                                                                                                                                                                                                                                                                                                                                            $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.y1.ordinal()] = 86;
                                                                                                                                                                                                                                                                                                                                                                                            try {
                                                                                                                                                                                                                                                                                                                                                                                                $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr[SVGAttr.y2.ordinal()] = 88;
                                                                                                                                                                                                                                                                                                                                                                                                SVGParser.$SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr = $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr;
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
                                                                                                                                                                                                                                                                                                                                                        catch (NoSuchFieldError noSuchFieldError10) {}
                                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                                    catch (NoSuchFieldError noSuchFieldError11) {}
                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                                catch (NoSuchFieldError noSuchFieldError12) {}
                                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                                            catch (NoSuchFieldError noSuchFieldError13) {}
                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                        catch (NoSuchFieldError noSuchFieldError14) {}
                                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                                    catch (NoSuchFieldError noSuchFieldError15) {}
                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                catch (NoSuchFieldError noSuchFieldError16) {}
                                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                                            catch (NoSuchFieldError noSuchFieldError17) {}
                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                        catch (NoSuchFieldError noSuchFieldError18) {}
                                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                                    catch (NoSuchFieldError noSuchFieldError19) {}
                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                catch (NoSuchFieldError noSuchFieldError20) {}
                                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                                            catch (NoSuchFieldError noSuchFieldError21) {}
                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                        catch (NoSuchFieldError noSuchFieldError22) {}
                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                    catch (NoSuchFieldError noSuchFieldError23) {}
                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                catch (NoSuchFieldError noSuchFieldError24) {}
                                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                                            catch (NoSuchFieldError noSuchFieldError25) {}
                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                        catch (NoSuchFieldError noSuchFieldError26) {}
                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                    catch (NoSuchFieldError noSuchFieldError27) {}
                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                catch (NoSuchFieldError noSuchFieldError28) {}
                                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                                            catch (NoSuchFieldError noSuchFieldError29) {}
                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                        catch (NoSuchFieldError noSuchFieldError30) {}
                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                    catch (NoSuchFieldError noSuchFieldError31) {}
                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                catch (NoSuchFieldError noSuchFieldError32) {}
                                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                                            catch (NoSuchFieldError noSuchFieldError33) {}
                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                        catch (NoSuchFieldError noSuchFieldError34) {}
                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                    catch (NoSuchFieldError noSuchFieldError35) {}
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                catch (NoSuchFieldError noSuchFieldError36) {}
                                                                                                                                                                                                                                            }
                                                                                                                                                                                                                                            catch (NoSuchFieldError noSuchFieldError37) {}
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                        catch (NoSuchFieldError noSuchFieldError38) {}
                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                    catch (NoSuchFieldError noSuchFieldError39) {}
                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                catch (NoSuchFieldError noSuchFieldError40) {}
                                                                                                                                                                                                                            }
                                                                                                                                                                                                                            catch (NoSuchFieldError noSuchFieldError41) {}
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        catch (NoSuchFieldError noSuchFieldError42) {}
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    catch (NoSuchFieldError noSuchFieldError43) {}
                                                                                                                                                                                                                }
                                                                                                                                                                                                                catch (NoSuchFieldError noSuchFieldError44) {}
                                                                                                                                                                                                            }
                                                                                                                                                                                                            catch (NoSuchFieldError noSuchFieldError45) {}
                                                                                                                                                                                                        }
                                                                                                                                                                                                        catch (NoSuchFieldError noSuchFieldError46) {}
                                                                                                                                                                                                    }
                                                                                                                                                                                                    catch (NoSuchFieldError noSuchFieldError47) {}
                                                                                                                                                                                                }
                                                                                                                                                                                                catch (NoSuchFieldError noSuchFieldError48) {}
                                                                                                                                                                                            }
                                                                                                                                                                                            catch (NoSuchFieldError noSuchFieldError49) {}
                                                                                                                                                                                        }
                                                                                                                                                                                        catch (NoSuchFieldError noSuchFieldError50) {}
                                                                                                                                                                                    }
                                                                                                                                                                                    catch (NoSuchFieldError noSuchFieldError51) {}
                                                                                                                                                                                }
                                                                                                                                                                                catch (NoSuchFieldError noSuchFieldError52) {}
                                                                                                                                                                            }
                                                                                                                                                                            catch (NoSuchFieldError noSuchFieldError53) {}
                                                                                                                                                                        }
                                                                                                                                                                        catch (NoSuchFieldError noSuchFieldError54) {}
                                                                                                                                                                    }
                                                                                                                                                                    catch (NoSuchFieldError noSuchFieldError55) {}
                                                                                                                                                                }
                                                                                                                                                                catch (NoSuchFieldError noSuchFieldError56) {}
                                                                                                                                                            }
                                                                                                                                                            catch (NoSuchFieldError noSuchFieldError57) {}
                                                                                                                                                        }
                                                                                                                                                        catch (NoSuchFieldError noSuchFieldError58) {}
                                                                                                                                                    }
                                                                                                                                                    catch (NoSuchFieldError noSuchFieldError59) {}
                                                                                                                                                }
                                                                                                                                                catch (NoSuchFieldError noSuchFieldError60) {}
                                                                                                                                            }
                                                                                                                                            catch (NoSuchFieldError noSuchFieldError61) {}
                                                                                                                                        }
                                                                                                                                        catch (NoSuchFieldError noSuchFieldError62) {}
                                                                                                                                    }
                                                                                                                                    catch (NoSuchFieldError noSuchFieldError63) {}
                                                                                                                                }
                                                                                                                                catch (NoSuchFieldError noSuchFieldError64) {}
                                                                                                                            }
                                                                                                                            catch (NoSuchFieldError noSuchFieldError65) {}
                                                                                                                        }
                                                                                                                        catch (NoSuchFieldError noSuchFieldError66) {}
                                                                                                                    }
                                                                                                                    catch (NoSuchFieldError noSuchFieldError67) {}
                                                                                                                }
                                                                                                                catch (NoSuchFieldError noSuchFieldError68) {}
                                                                                                            }
                                                                                                            catch (NoSuchFieldError noSuchFieldError69) {}
                                                                                                        }
                                                                                                        catch (NoSuchFieldError noSuchFieldError70) {}
                                                                                                    }
                                                                                                    catch (NoSuchFieldError noSuchFieldError71) {}
                                                                                                }
                                                                                                catch (NoSuchFieldError noSuchFieldError72) {}
                                                                                            }
                                                                                            catch (NoSuchFieldError noSuchFieldError73) {}
                                                                                        }
                                                                                        catch (NoSuchFieldError noSuchFieldError74) {}
                                                                                    }
                                                                                    catch (NoSuchFieldError noSuchFieldError75) {}
                                                                                }
                                                                                catch (NoSuchFieldError noSuchFieldError76) {}
                                                                            }
                                                                            catch (NoSuchFieldError noSuchFieldError77) {}
                                                                        }
                                                                        catch (NoSuchFieldError noSuchFieldError78) {}
                                                                    }
                                                                    catch (NoSuchFieldError noSuchFieldError79) {}
                                                                }
                                                                catch (NoSuchFieldError noSuchFieldError80) {}
                                                            }
                                                            catch (NoSuchFieldError noSuchFieldError81) {}
                                                        }
                                                        catch (NoSuchFieldError noSuchFieldError82) {}
                                                    }
                                                    catch (NoSuchFieldError noSuchFieldError83) {}
                                                }
                                                catch (NoSuchFieldError noSuchFieldError84) {}
                                            }
                                            catch (NoSuchFieldError noSuchFieldError85) {}
                                        }
                                        catch (NoSuchFieldError noSuchFieldError86) {}
                                    }
                                    catch (NoSuchFieldError noSuchFieldError87) {}
                                }
                                catch (NoSuchFieldError noSuchFieldError88) {}
                            }
                            catch (NoSuchFieldError noSuchFieldError89) {}
                        }
                        catch (NoSuchFieldError noSuchFieldError90) {}
                    }
                    catch (NoSuchFieldError noSuchFieldError91) {}
                }
                catch (NoSuchFieldError noSuchFieldError92) {
                    continue;
                }
                break;
            }
        }
        return $switch_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr;
    }
    
    static {
        SVGParser.colourKeywords = new HashMap<String, Integer>();
        SVGParser.fontSizeKeywords = new HashMap<String, SVG.Length>(9);
        SVGParser.fontWeightKeywords = new HashMap<String, Integer>(13);
        SVGParser.fontStyleKeywords = new HashMap<String, SVG.Style.FontStyle>(3);
        SVGParser.aspectRatioKeywords = new HashMap<String, PreserveAspectRatio.Alignment>();
        SVGParser.supportedFeatures = new HashSet<String>();
        SVGParser.colourKeywords.put("aliceblue", 15792383);
        SVGParser.colourKeywords.put("antiquewhite", 16444375);
        SVGParser.colourKeywords.put("aqua", 65535);
        SVGParser.colourKeywords.put("aquamarine", 8388564);
        SVGParser.colourKeywords.put("azure", 15794175);
        SVGParser.colourKeywords.put("beige", 16119260);
        SVGParser.colourKeywords.put("bisque", 16770244);
        SVGParser.colourKeywords.put("black", 0);
        SVGParser.colourKeywords.put("blanchedalmond", 16772045);
        SVGParser.colourKeywords.put("blue", 255);
        SVGParser.colourKeywords.put("blueviolet", 9055202);
        SVGParser.colourKeywords.put("brown", 10824234);
        SVGParser.colourKeywords.put("burlywood", 14596231);
        SVGParser.colourKeywords.put("cadetblue", 6266528);
        SVGParser.colourKeywords.put("chartreuse", 8388352);
        SVGParser.colourKeywords.put("chocolate", 13789470);
        SVGParser.colourKeywords.put("coral", 16744272);
        SVGParser.colourKeywords.put("cornflowerblue", 6591981);
        SVGParser.colourKeywords.put("cornsilk", 16775388);
        SVGParser.colourKeywords.put("crimson", 14423100);
        SVGParser.colourKeywords.put("cyan", 65535);
        SVGParser.colourKeywords.put("darkblue", 139);
        SVGParser.colourKeywords.put("darkcyan", 35723);
        SVGParser.colourKeywords.put("darkgoldenrod", 12092939);
        SVGParser.colourKeywords.put("darkgray", 11119017);
        SVGParser.colourKeywords.put("darkgreen", 25600);
        SVGParser.colourKeywords.put("darkgrey", 11119017);
        SVGParser.colourKeywords.put("darkkhaki", 12433259);
        SVGParser.colourKeywords.put("darkmagenta", 9109643);
        SVGParser.colourKeywords.put("darkolivegreen", 5597999);
        SVGParser.colourKeywords.put("darkorange", 16747520);
        SVGParser.colourKeywords.put("darkorchid", 10040012);
        SVGParser.colourKeywords.put("darkred", 9109504);
        SVGParser.colourKeywords.put("darksalmon", 15308410);
        SVGParser.colourKeywords.put("darkseagreen", 9419919);
        SVGParser.colourKeywords.put("darkslateblue", 4734347);
        SVGParser.colourKeywords.put("darkslategray", 3100495);
        SVGParser.colourKeywords.put("darkslategrey", 3100495);
        SVGParser.colourKeywords.put("darkturquoise", 52945);
        SVGParser.colourKeywords.put("darkviolet", 9699539);
        SVGParser.colourKeywords.put("deeppink", 16716947);
        SVGParser.colourKeywords.put("deepskyblue", 49151);
        SVGParser.colourKeywords.put("dimgray", 6908265);
        SVGParser.colourKeywords.put("dimgrey", 6908265);
        SVGParser.colourKeywords.put("dodgerblue", 2003199);
        SVGParser.colourKeywords.put("firebrick", 11674146);
        SVGParser.colourKeywords.put("floralwhite", 16775920);
        SVGParser.colourKeywords.put("forestgreen", 2263842);
        SVGParser.colourKeywords.put("fuchsia", 16711935);
        SVGParser.colourKeywords.put("gainsboro", 14474460);
        SVGParser.colourKeywords.put("ghostwhite", 16316671);
        SVGParser.colourKeywords.put("gold", 16766720);
        SVGParser.colourKeywords.put("goldenrod", 14329120);
        SVGParser.colourKeywords.put("gray", 8421504);
        SVGParser.colourKeywords.put("green", 32768);
        SVGParser.colourKeywords.put("greenyellow", 11403055);
        SVGParser.colourKeywords.put("grey", 8421504);
        SVGParser.colourKeywords.put("honeydew", 15794160);
        SVGParser.colourKeywords.put("hotpink", 16738740);
        SVGParser.colourKeywords.put("indianred", 13458524);
        SVGParser.colourKeywords.put("indigo", 4915330);
        SVGParser.colourKeywords.put("ivory", 16777200);
        SVGParser.colourKeywords.put("khaki", 15787660);
        SVGParser.colourKeywords.put("lavender", 15132410);
        SVGParser.colourKeywords.put("lavenderblush", 16773365);
        SVGParser.colourKeywords.put("lawngreen", 8190976);
        SVGParser.colourKeywords.put("lemonchiffon", 16775885);
        SVGParser.colourKeywords.put("lightblue", 11393254);
        SVGParser.colourKeywords.put("lightcoral", 15761536);
        SVGParser.colourKeywords.put("lightcyan", 14745599);
        SVGParser.colourKeywords.put("lightgoldenrodyellow", 16448210);
        SVGParser.colourKeywords.put("lightgray", 13882323);
        SVGParser.colourKeywords.put("lightgreen", 9498256);
        SVGParser.colourKeywords.put("lightgrey", 13882323);
        SVGParser.colourKeywords.put("lightpink", 16758465);
        SVGParser.colourKeywords.put("lightsalmon", 16752762);
        SVGParser.colourKeywords.put("lightseagreen", 2142890);
        SVGParser.colourKeywords.put("lightskyblue", 8900346);
        SVGParser.colourKeywords.put("lightslategray", 7833753);
        SVGParser.colourKeywords.put("lightslategrey", 7833753);
        SVGParser.colourKeywords.put("lightsteelblue", 11584734);
        SVGParser.colourKeywords.put("lightyellow", 16777184);
        SVGParser.colourKeywords.put("lime", 65280);
        SVGParser.colourKeywords.put("limegreen", 3329330);
        SVGParser.colourKeywords.put("linen", 16445670);
        SVGParser.colourKeywords.put("magenta", 16711935);
        SVGParser.colourKeywords.put("maroon", 8388608);
        SVGParser.colourKeywords.put("mediumaquamarine", 6737322);
        SVGParser.colourKeywords.put("mediumblue", 205);
        SVGParser.colourKeywords.put("mediumorchid", 12211667);
        SVGParser.colourKeywords.put("mediumpurple", 9662683);
        SVGParser.colourKeywords.put("mediumseagreen", 3978097);
        SVGParser.colourKeywords.put("mediumslateblue", 8087790);
        SVGParser.colourKeywords.put("mediumspringgreen", 64154);
        SVGParser.colourKeywords.put("mediumturquoise", 4772300);
        SVGParser.colourKeywords.put("mediumvioletred", 13047173);
        SVGParser.colourKeywords.put("midnightblue", 1644912);
        SVGParser.colourKeywords.put("mintcream", 16121850);
        SVGParser.colourKeywords.put("mistyrose", 16770273);
        SVGParser.colourKeywords.put("moccasin", 16770229);
        SVGParser.colourKeywords.put("navajowhite", 16768685);
        SVGParser.colourKeywords.put("navy", 128);
        SVGParser.colourKeywords.put("oldlace", 16643558);
        SVGParser.colourKeywords.put("olive", 8421376);
        SVGParser.colourKeywords.put("olivedrab", 7048739);
        SVGParser.colourKeywords.put("orange", 16753920);
        SVGParser.colourKeywords.put("orangered", 16729344);
        SVGParser.colourKeywords.put("orchid", 14315734);
        SVGParser.colourKeywords.put("palegoldenrod", 15657130);
        SVGParser.colourKeywords.put("palegreen", 10025880);
        SVGParser.colourKeywords.put("paleturquoise", 11529966);
        SVGParser.colourKeywords.put("palevioletred", 14381203);
        SVGParser.colourKeywords.put("papayawhip", 16773077);
        SVGParser.colourKeywords.put("peachpuff", 16767673);
        SVGParser.colourKeywords.put("peru", 13468991);
        SVGParser.colourKeywords.put("pink", 16761035);
        SVGParser.colourKeywords.put("plum", 14524637);
        SVGParser.colourKeywords.put("powderblue", 11591910);
        SVGParser.colourKeywords.put("purple", 8388736);
        SVGParser.colourKeywords.put("red", 16711680);
        SVGParser.colourKeywords.put("rosybrown", 12357519);
        SVGParser.colourKeywords.put("royalblue", 4286945);
        SVGParser.colourKeywords.put("saddlebrown", 9127187);
        SVGParser.colourKeywords.put("salmon", 16416882);
        SVGParser.colourKeywords.put("sandybrown", 16032864);
        SVGParser.colourKeywords.put("seagreen", 3050327);
        SVGParser.colourKeywords.put("seashell", 16774638);
        SVGParser.colourKeywords.put("sienna", 10506797);
        SVGParser.colourKeywords.put("silver", 12632256);
        SVGParser.colourKeywords.put("skyblue", 8900331);
        SVGParser.colourKeywords.put("slateblue", 6970061);
        SVGParser.colourKeywords.put("slategray", 7372944);
        SVGParser.colourKeywords.put("slategrey", 7372944);
        SVGParser.colourKeywords.put("snow", 16775930);
        SVGParser.colourKeywords.put("springgreen", 65407);
        SVGParser.colourKeywords.put("steelblue", 4620980);
        SVGParser.colourKeywords.put("tan", 13808780);
        SVGParser.colourKeywords.put("teal", 32896);
        SVGParser.colourKeywords.put("thistle", 14204888);
        SVGParser.colourKeywords.put("tomato", 16737095);
        SVGParser.colourKeywords.put("turquoise", 4251856);
        SVGParser.colourKeywords.put("violet", 15631086);
        SVGParser.colourKeywords.put("wheat", 16113331);
        SVGParser.colourKeywords.put("white", 16777215);
        SVGParser.colourKeywords.put("whitesmoke", 16119285);
        SVGParser.colourKeywords.put("yellow", 16776960);
        SVGParser.colourKeywords.put("yellowgreen", 10145074);
        SVGParser.fontSizeKeywords.put("xx-small", new SVG.Length(0.694f, SVG.Unit.pt));
        SVGParser.fontSizeKeywords.put("x-small", new SVG.Length(0.833f, SVG.Unit.pt));
        SVGParser.fontSizeKeywords.put("small", new SVG.Length(10.0f, SVG.Unit.pt));
        SVGParser.fontSizeKeywords.put("medium", new SVG.Length(12.0f, SVG.Unit.pt));
        SVGParser.fontSizeKeywords.put("large", new SVG.Length(14.4f, SVG.Unit.pt));
        SVGParser.fontSizeKeywords.put("x-large", new SVG.Length(17.3f, SVG.Unit.pt));
        SVGParser.fontSizeKeywords.put("xx-large", new SVG.Length(20.7f, SVG.Unit.pt));
        SVGParser.fontSizeKeywords.put("smaller", new SVG.Length(83.33f, SVG.Unit.percent));
        SVGParser.fontSizeKeywords.put("larger", new SVG.Length(120.0f, SVG.Unit.percent));
        SVGParser.fontWeightKeywords.put("normal", 400);
        SVGParser.fontWeightKeywords.put("bold", 700);
        SVGParser.fontWeightKeywords.put("bolder", 1);
        SVGParser.fontWeightKeywords.put("lighter", -1);
        SVGParser.fontWeightKeywords.put("100", 100);
        SVGParser.fontWeightKeywords.put("200", 200);
        SVGParser.fontWeightKeywords.put("300", 300);
        SVGParser.fontWeightKeywords.put("400", 400);
        SVGParser.fontWeightKeywords.put("500", 500);
        SVGParser.fontWeightKeywords.put("600", 600);
        SVGParser.fontWeightKeywords.put("700", 700);
        SVGParser.fontWeightKeywords.put("800", 800);
        SVGParser.fontWeightKeywords.put("900", 900);
        SVGParser.fontStyleKeywords.put("normal", SVG.Style.FontStyle.Normal);
        SVGParser.fontStyleKeywords.put("italic", SVG.Style.FontStyle.Italic);
        SVGParser.fontStyleKeywords.put("oblique", SVG.Style.FontStyle.Oblique);
        SVGParser.aspectRatioKeywords.put("none", PreserveAspectRatio.Alignment.None);
        SVGParser.aspectRatioKeywords.put("xMinYMin", PreserveAspectRatio.Alignment.XMinYMin);
        SVGParser.aspectRatioKeywords.put("xMidYMin", PreserveAspectRatio.Alignment.XMidYMin);
        SVGParser.aspectRatioKeywords.put("xMaxYMin", PreserveAspectRatio.Alignment.XMaxYMin);
        SVGParser.aspectRatioKeywords.put("xMinYMid", PreserveAspectRatio.Alignment.XMinYMid);
        SVGParser.aspectRatioKeywords.put("xMidYMid", PreserveAspectRatio.Alignment.XMidYMid);
        SVGParser.aspectRatioKeywords.put("xMaxYMid", PreserveAspectRatio.Alignment.XMaxYMid);
        SVGParser.aspectRatioKeywords.put("xMinYMax", PreserveAspectRatio.Alignment.XMinYMax);
        SVGParser.aspectRatioKeywords.put("xMidYMax", PreserveAspectRatio.Alignment.XMidYMax);
        SVGParser.aspectRatioKeywords.put("xMaxYMax", PreserveAspectRatio.Alignment.XMaxYMax);
        SVGParser.supportedFeatures.add("Structure");
        SVGParser.supportedFeatures.add("BasicStructure");
        SVGParser.supportedFeatures.add("ConditionalProcessing");
        SVGParser.supportedFeatures.add("Image");
        SVGParser.supportedFeatures.add("Style");
        SVGParser.supportedFeatures.add("ViewportAttribute");
        SVGParser.supportedFeatures.add("Shape");
        SVGParser.supportedFeatures.add("BasicText");
        SVGParser.supportedFeatures.add("PaintAttribute");
        SVGParser.supportedFeatures.add("BasicPaintAttribute");
        SVGParser.supportedFeatures.add("OpacityAttribute");
        SVGParser.supportedFeatures.add("BasicGraphicsAttribute");
        SVGParser.supportedFeatures.add("Marker");
        SVGParser.supportedFeatures.add("Gradient");
        SVGParser.supportedFeatures.add("Pattern");
        SVGParser.supportedFeatures.add("Clip");
        SVGParser.supportedFeatures.add("BasicClip");
        SVGParser.supportedFeatures.add("Mask");
        SVGParser.supportedFeatures.add("View");
    }
    
    public SVGParser() {
        this.svgDocument = null;
        this.currentElement = null;
        this.ignoring = false;
        this.inMetadataElement = false;
        this.metadataTag = null;
        this.metadataElementContents = null;
        this.inStyleElement = false;
        this.styleElementContents = null;
        this.supportedFormats = null;
    }
    
    private void circle(final Attributes attributes) throws SAXException {
        this.debug("<circle>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.Circle circle = new SVG.Circle();
        circle.document = this.svgDocument;
        circle.parent = this.currentElement;
        this.parseAttributesCore(circle, attributes);
        this.parseAttributesStyle(circle, attributes);
        this.parseAttributesTransform(circle, attributes);
        this.parseAttributesConditional(circle, attributes);
        this.parseAttributesCircle(circle, attributes);
        this.currentElement.addChild(circle);
    }
    
    private void clipPath(final Attributes attributes) throws SAXException {
        this.debug("<clipPath>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.ClipPath currentElement = new SVG.ClipPath();
        currentElement.document = this.svgDocument;
        currentElement.parent = this.currentElement;
        this.parseAttributesCore(currentElement, attributes);
        this.parseAttributesStyle(currentElement, attributes);
        this.parseAttributesTransform(currentElement, attributes);
        this.parseAttributesConditional(currentElement, attributes);
        this.parseAttributesClipPath(currentElement, attributes);
        this.currentElement.addChild(currentElement);
        this.currentElement = currentElement;
    }
    
    private void debug(final String s, final Object... array) {
    }
    
    private void defs(final Attributes attributes) throws SAXException {
        this.debug("<defs>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.Defs currentElement = new SVG.Defs();
        currentElement.document = this.svgDocument;
        currentElement.parent = this.currentElement;
        this.parseAttributesCore(currentElement, attributes);
        this.parseAttributesStyle(currentElement, attributes);
        this.parseAttributesTransform(currentElement, attributes);
        this.currentElement.addChild(currentElement);
        this.currentElement = currentElement;
    }
    
    private void dumpNode(final SVG.SvgObject obj, String string) {
        Log.d("SVGParser", String.valueOf(string) + obj);
        if (obj instanceof SVG.SvgConditionalContainer) {
            string = String.valueOf(string) + "  ";
            final Iterator<SVG.SvgObject> iterator = ((SVG.SvgConditionalContainer)obj).children.iterator();
            while (iterator.hasNext()) {
                this.dumpNode(iterator.next(), string);
            }
        }
    }
    
    private void ellipse(final Attributes attributes) throws SAXException {
        this.debug("<ellipse>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.Ellipse ellipse = new SVG.Ellipse();
        ellipse.document = this.svgDocument;
        ellipse.parent = this.currentElement;
        this.parseAttributesCore(ellipse, attributes);
        this.parseAttributesStyle(ellipse, attributes);
        this.parseAttributesTransform(ellipse, attributes);
        this.parseAttributesConditional(ellipse, attributes);
        this.parseAttributesEllipse(ellipse, attributes);
        this.currentElement.addChild(ellipse);
    }
    
    private void g(final Attributes attributes) throws SAXException {
        this.debug("<g>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.Group currentElement = new SVG.Group();
        currentElement.document = this.svgDocument;
        currentElement.parent = this.currentElement;
        this.parseAttributesCore(currentElement, attributes);
        this.parseAttributesStyle(currentElement, attributes);
        this.parseAttributesTransform(currentElement, attributes);
        this.parseAttributesConditional(currentElement, attributes);
        this.currentElement.addChild(currentElement);
        this.currentElement = currentElement;
    }
    
    private void image(final Attributes attributes) throws SAXException {
        this.debug("<image>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.Image currentElement = new SVG.Image();
        currentElement.document = this.svgDocument;
        currentElement.parent = this.currentElement;
        this.parseAttributesCore(currentElement, attributes);
        this.parseAttributesStyle(currentElement, attributes);
        this.parseAttributesTransform(currentElement, attributes);
        this.parseAttributesConditional(currentElement, attributes);
        this.parseAttributesImage(currentElement, attributes);
        this.currentElement.addChild(currentElement);
        this.currentElement = currentElement;
    }
    
    private void line(final Attributes attributes) throws SAXException {
        this.debug("<line>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.Line line = new SVG.Line();
        line.document = this.svgDocument;
        line.parent = this.currentElement;
        this.parseAttributesCore(line, attributes);
        this.parseAttributesStyle(line, attributes);
        this.parseAttributesTransform(line, attributes);
        this.parseAttributesConditional(line, attributes);
        this.parseAttributesLine(line, attributes);
        this.currentElement.addChild(line);
    }
    
    private void linearGradient(final Attributes attributes) throws SAXException {
        this.debug("<linearGradiant>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.SvgLinearGradient currentElement = new SVG.SvgLinearGradient();
        currentElement.document = this.svgDocument;
        currentElement.parent = this.currentElement;
        this.parseAttributesCore(currentElement, attributes);
        this.parseAttributesStyle(currentElement, attributes);
        this.parseAttributesGradient(currentElement, attributes);
        this.parseAttributesLinearGradient(currentElement, attributes);
        this.currentElement.addChild(currentElement);
        this.currentElement = currentElement;
    }
    
    private void marker(final Attributes attributes) throws SAXException {
        this.debug("<marker>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.Marker currentElement = new SVG.Marker();
        currentElement.document = this.svgDocument;
        currentElement.parent = this.currentElement;
        this.parseAttributesCore(currentElement, attributes);
        this.parseAttributesStyle(currentElement, attributes);
        this.parseAttributesConditional(currentElement, attributes);
        this.parseAttributesViewBox(currentElement, attributes);
        this.parseAttributesMarker(currentElement, attributes);
        this.currentElement.addChild(currentElement);
        this.currentElement = currentElement;
    }
    
    private void mask(final Attributes attributes) throws SAXException {
        this.debug("<mask>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.Mask currentElement = new SVG.Mask();
        currentElement.document = this.svgDocument;
        currentElement.parent = this.currentElement;
        this.parseAttributesCore(currentElement, attributes);
        this.parseAttributesStyle(currentElement, attributes);
        this.parseAttributesConditional(currentElement, attributes);
        this.parseAttributesMask(currentElement, attributes);
        this.currentElement.addChild(currentElement);
        this.currentElement = currentElement;
    }
    
    private void parseAttributesCircle(final SVG.Circle circle, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 7: {
                    circle.cx = parseLength(trim);
                    break;
                }
                case 8: {
                    circle.cy = parseLength(trim);
                    break;
                }
                case 50: {
                    circle.r = parseLength(trim);
                    if (circle.r.isNegative()) {
                        throw new SAXException("Invalid <circle> element. r cannot be negative");
                    }
                    break;
                }
            }
        }
    }
    
    private void parseAttributesClipPath(final SVG.ClipPath clipPath, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 4: {
                    if ("objectBoundingBox".equals(trim)) {
                        clipPath.clipPathUnitsAreUser = false;
                        break;
                    }
                    if ("userSpaceOnUse".equals(trim)) {
                        clipPath.clipPathUnitsAreUser = true;
                        break;
                    }
                    throw new SAXException("Invalid value for attribute clipPathUnits");
                }
            }
        }
    }
    
    private void parseAttributesConditional(final SVG.SvgConditional svgConditional, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 53: {
                    svgConditional.setRequiredFeatures(parseRequiredFeatures(trim));
                    break;
                }
                case 54: {
                    svgConditional.setRequiredExtensions(trim);
                    break;
                }
                case 74: {
                    svgConditional.setSystemLanguage(parseSystemLanguage(trim));
                    break;
                }
                case 55: {
                    svgConditional.setRequiredFormats(parseRequiredFormats(trim));
                    break;
                }
                case 56: {
                    final List<String> fontFamily = parseFontFamily(trim);
                    HashSet<String> requiredFonts;
                    if (fontFamily != null) {
                        requiredFonts = new HashSet<String>(fontFamily);
                    }
                    else {
                        requiredFonts = new HashSet<String>(0);
                    }
                    svgConditional.setRequiredFonts(requiredFonts);
                    break;
                }
            }
        }
    }
    
    private void parseAttributesCore(final SVG.SvgElementBase svgElementBase, final Attributes attributes) throws SAXException {
        int i = 0;
        while (i < attributes.getLength()) {
            final String qName = attributes.getQName(i);
            if (qName.equals("id") || qName.equals("xml:id")) {
                svgElementBase.id = attributes.getValue(i).trim();
                break;
            }
            if (qName.equals("xml:space")) {
                final String trim = attributes.getValue(i).trim();
                if ("default".equals(trim)) {
                    svgElementBase.spacePreserve = Boolean.FALSE;
                    break;
                }
                if ("preserve".equals(trim)) {
                    svgElementBase.spacePreserve = Boolean.TRUE;
                    break;
                }
                throw new SAXException("Invalid value for \"xml:space\" attribute: " + trim);
            }
            else {
                ++i;
            }
        }
    }
    
    private void parseAttributesEllipse(final SVG.Ellipse ellipse, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 7: {
                    ellipse.cx = parseLength(trim);
                    break;
                }
                case 8: {
                    ellipse.cy = parseLength(trim);
                    break;
                }
                case 57: {
                    ellipse.rx = parseLength(trim);
                    if (ellipse.rx.isNegative()) {
                        throw new SAXException("Invalid <ellipse> element. rx cannot be negative");
                    }
                    break;
                }
                case 58: {
                    ellipse.ry = parseLength(trim);
                    if (ellipse.ry.isNegative()) {
                        throw new SAXException("Invalid <ellipse> element. ry cannot be negative");
                    }
                    break;
                }
            }
        }
    }
    
    private void parseAttributesGradient(final SVG.GradientElement gradientElement, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 25: {
                    if ("objectBoundingBox".equals(trim)) {
                        gradientElement.gradientUnitsAreUser = false;
                        break;
                    }
                    if ("userSpaceOnUse".equals(trim)) {
                        gradientElement.gradientUnitsAreUser = true;
                        break;
                    }
                    throw new SAXException("Invalid value for attribute gradientUnits");
                }
                case 24: {
                    gradientElement.gradientTransform = this.parseTransformList(trim);
                    break;
                }
                case 61: {
                    try {
                        gradientElement.spreadMethod = SVG.GradientSpread.valueOf(trim);
                        break;
                    }
                    catch (IllegalArgumentException ex) {
                        throw new SAXException("Invalid spreadMethod attribute. \"" + trim + "\" is not a valid value.");
                    }
                }
                case 27: {
                    if ("http://www.w3.org/1999/xlink".equals(attributes.getURI(i))) {
                        gradientElement.href = trim;
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    private void parseAttributesImage(final SVG.Image image, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 83: {
                    image.x = parseLength(trim);
                    break;
                }
                case 84: {
                    image.y = parseLength(trim);
                    break;
                }
                case 82: {
                    image.width = parseLength(trim);
                    if (image.width.isNegative()) {
                        throw new SAXException("Invalid <use> element. width cannot be negative");
                    }
                    break;
                }
                case 26: {
                    image.height = parseLength(trim);
                    if (image.height.isNegative()) {
                        throw new SAXException("Invalid <use> element. height cannot be negative");
                    }
                    break;
                }
                case 27: {
                    if ("http://www.w3.org/1999/xlink".equals(attributes.getURI(i))) {
                        image.href = trim;
                        break;
                    }
                    break;
                }
                case 49: {
                    parsePreserveAspectRatio(image, trim);
                    break;
                }
            }
        }
    }
    
    private void parseAttributesLine(final SVG.Line line, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 85: {
                    line.x1 = parseLength(trim);
                    break;
                }
                case 86: {
                    line.y1 = parseLength(trim);
                    break;
                }
                case 87: {
                    line.x2 = parseLength(trim);
                    break;
                }
                case 88: {
                    line.y2 = parseLength(trim);
                    break;
                }
            }
        }
    }
    
    private void parseAttributesLinearGradient(final SVG.SvgLinearGradient svgLinearGradient, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 85: {
                    svgLinearGradient.x1 = parseLength(trim);
                    break;
                }
                case 86: {
                    svgLinearGradient.y1 = parseLength(trim);
                    break;
                }
                case 87: {
                    svgLinearGradient.x2 = parseLength(trim);
                    break;
                }
                case 88: {
                    svgLinearGradient.y2 = parseLength(trim);
                    break;
                }
            }
        }
    }
    
    private void parseAttributesMarker(final SVG.Marker marker, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 51: {
                    marker.refX = parseLength(trim);
                    break;
                }
                case 52: {
                    marker.refY = parseLength(trim);
                    break;
                }
                case 35: {
                    marker.markerWidth = parseLength(trim);
                    if (marker.markerWidth.isNegative()) {
                        throw new SAXException("Invalid <marker> element. markerWidth cannot be negative");
                    }
                    break;
                }
                case 33: {
                    marker.markerHeight = parseLength(trim);
                    if (marker.markerHeight.isNegative()) {
                        throw new SAXException("Invalid <marker> element. markerHeight cannot be negative");
                    }
                    break;
                }
                case 34: {
                    if ("strokeWidth".equals(trim)) {
                        marker.markerUnitsAreUser = false;
                        break;
                    }
                    if ("userSpaceOnUse".equals(trim)) {
                        marker.markerUnitsAreUser = true;
                        break;
                    }
                    throw new SAXException("Invalid value for attribute markerUnits");
                }
                case 42: {
                    if ("auto".equals(trim)) {
                        marker.orient = Float.NaN;
                        break;
                    }
                    marker.orient = parseFloat(trim);
                    break;
                }
            }
        }
    }
    
    private void parseAttributesMask(final SVG.Mask mask, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 38: {
                    if ("objectBoundingBox".equals(trim)) {
                        mask.maskUnitsAreUser = false;
                        break;
                    }
                    if ("userSpaceOnUse".equals(trim)) {
                        mask.maskUnitsAreUser = true;
                        break;
                    }
                    throw new SAXException("Invalid value for attribute maskUnits");
                }
                case 37: {
                    if ("objectBoundingBox".equals(trim)) {
                        mask.maskContentUnitsAreUser = false;
                        break;
                    }
                    if ("userSpaceOnUse".equals(trim)) {
                        mask.maskContentUnitsAreUser = true;
                        break;
                    }
                    throw new SAXException("Invalid value for attribute maskContentUnits");
                }
                case 83: {
                    mask.x = parseLength(trim);
                    break;
                }
                case 84: {
                    mask.y = parseLength(trim);
                    break;
                }
                case 82: {
                    mask.width = parseLength(trim);
                    if (mask.width.isNegative()) {
                        throw new SAXException("Invalid <mask> element. width cannot be negative");
                    }
                    break;
                }
                case 26: {
                    mask.height = parseLength(trim);
                    if (mask.height.isNegative()) {
                        throw new SAXException("Invalid <mask> element. height cannot be negative");
                    }
                    break;
                }
            }
        }
    }
    
    private void parseAttributesPath(final SVG.Path path, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 14: {
                    path.d = parsePath(trim);
                    break;
                }
                case 44: {
                    path.pathLength = parseFloat(trim);
                    if (path.pathLength < 0.0f) {
                        throw new SAXException("Invalid <path> element. pathLength cannot be negative");
                    }
                    break;
                }
            }
        }
    }
    
    private void parseAttributesPattern(final SVG.Pattern pattern, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 47: {
                    if ("objectBoundingBox".equals(trim)) {
                        pattern.patternUnitsAreUser = false;
                        break;
                    }
                    if ("userSpaceOnUse".equals(trim)) {
                        pattern.patternUnitsAreUser = true;
                        break;
                    }
                    throw new SAXException("Invalid value for attribute patternUnits");
                }
                case 45: {
                    if ("objectBoundingBox".equals(trim)) {
                        pattern.patternContentUnitsAreUser = false;
                        break;
                    }
                    if ("userSpaceOnUse".equals(trim)) {
                        pattern.patternContentUnitsAreUser = true;
                        break;
                    }
                    throw new SAXException("Invalid value for attribute patternContentUnits");
                }
                case 46: {
                    pattern.patternTransform = this.parseTransformList(trim);
                    break;
                }
                case 83: {
                    pattern.x = parseLength(trim);
                    break;
                }
                case 84: {
                    pattern.y = parseLength(trim);
                    break;
                }
                case 82: {
                    pattern.width = parseLength(trim);
                    if (pattern.width.isNegative()) {
                        throw new SAXException("Invalid <pattern> element. width cannot be negative");
                    }
                    break;
                }
                case 26: {
                    pattern.height = parseLength(trim);
                    if (pattern.height.isNegative()) {
                        throw new SAXException("Invalid <pattern> element. height cannot be negative");
                    }
                    break;
                }
                case 27: {
                    if ("http://www.w3.org/1999/xlink".equals(attributes.getURI(i))) {
                        pattern.href = trim;
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    private void parseAttributesPolyLine(final SVG.PolyLine polyLine, final Attributes attributes, final String s) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            if (SVGAttr.fromString(attributes.getLocalName(i)) == SVGAttr.points) {
                final TextScanner textScanner = new TextScanner(attributes.getValue(i));
                final ArrayList<Float> list = new ArrayList<Float>();
                textScanner.skipWhitespace();
                while (!textScanner.empty()) {
                    final Float nextFloat = textScanner.nextFloat();
                    if (nextFloat == null) {
                        throw new SAXException("Invalid <" + s + "> points attribute. Non-coordinate content found in list.");
                    }
                    textScanner.skipCommaWhitespace();
                    final Float nextFloat2 = textScanner.nextFloat();
                    if (nextFloat2 == null) {
                        throw new SAXException("Invalid <" + s + "> points attribute. There should be an even number of coordinates.");
                    }
                    textScanner.skipCommaWhitespace();
                    list.add(nextFloat);
                    list.add(nextFloat2);
                }
                polyLine.points = new float[list.size()];
                int n = 0;
                final Iterator<Object> iterator = list.iterator();
                while (iterator.hasNext()) {
                    polyLine.points[n] = iterator.next();
                    ++n;
                }
            }
        }
    }
    
    private void parseAttributesRadialGradient(final SVG.SvgRadialGradient svgRadialGradient, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 7: {
                    svgRadialGradient.cx = parseLength(trim);
                    break;
                }
                case 8: {
                    svgRadialGradient.cy = parseLength(trim);
                    break;
                }
                case 50: {
                    svgRadialGradient.r = parseLength(trim);
                    if (svgRadialGradient.r.isNegative()) {
                        throw new SAXException("Invalid <radialGradient> element. r cannot be negative");
                    }
                    break;
                }
                case 12: {
                    svgRadialGradient.fx = parseLength(trim);
                    break;
                }
                case 13: {
                    svgRadialGradient.fy = parseLength(trim);
                    break;
                }
            }
        }
    }
    
    private void parseAttributesRect(final SVG.Rect rect, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 83: {
                    rect.x = parseLength(trim);
                    break;
                }
                case 84: {
                    rect.y = parseLength(trim);
                    break;
                }
                case 82: {
                    rect.width = parseLength(trim);
                    if (rect.width.isNegative()) {
                        throw new SAXException("Invalid <rect> element. width cannot be negative");
                    }
                    break;
                }
                case 26: {
                    rect.height = parseLength(trim);
                    if (rect.height.isNegative()) {
                        throw new SAXException("Invalid <rect> element. height cannot be negative");
                    }
                    break;
                }
                case 57: {
                    rect.rx = parseLength(trim);
                    if (rect.rx.isNegative()) {
                        throw new SAXException("Invalid <rect> element. rx cannot be negative");
                    }
                    break;
                }
                case 58: {
                    rect.ry = parseLength(trim);
                    if (rect.ry.isNegative()) {
                        throw new SAXException("Invalid <rect> element. ry cannot be negative");
                    }
                    break;
                }
            }
        }
    }
    
    private void parseAttributesSVG(final SVG.Svg svg, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 83: {
                    svg.x = parseLength(trim);
                    break;
                }
                case 84: {
                    svg.y = parseLength(trim);
                    break;
                }
                case 82: {
                    svg.width = parseLength(trim);
                    if (svg.width.isNegative()) {
                        throw new SAXException("Invalid <svg> element. width cannot be negative");
                    }
                    break;
                }
                case 26: {
                    svg.height = parseLength(trim);
                    if (svg.height.isNegative()) {
                        throw new SAXException("Invalid <svg> element. height cannot be negative");
                    }
                    break;
                }
                case 80: {
                    svg.version = trim;
                    break;
                }
            }
        }
    }
    
    private void parseAttributesStop(final SVG.Stop stop, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 40: {
                    stop.offset = this.parseGradiantOffset(trim);
                    break;
                }
            }
        }
    }
    
    private void parseAttributesStyle(final SVG.SvgElementBase svgElementBase, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            if (trim.length() != 0) {
                switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                    default: {
                        if (svgElementBase.baseStyle == null) {
                            svgElementBase.baseStyle = new SVG.Style();
                        }
                        processStyleProperty(svgElementBase.baseStyle, attributes.getLocalName(i), attributes.getValue(i).trim());
                        break;
                    }
                    case 73: {
                        parseStyle(svgElementBase, trim);
                        break;
                    }
                    case 1: {
                        svgElementBase.classNames = CSSParser.parseClassAttribute(trim);
                        break;
                    }
                }
            }
        }
    }
    
    private void parseAttributesTRef(final SVG.TRef tRef, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 27: {
                    if ("http://www.w3.org/1999/xlink".equals(attributes.getURI(i))) {
                        tRef.href = trim;
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    private void parseAttributesTextPath(final SVG.TextPath textPath, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 27: {
                    if ("http://www.w3.org/1999/xlink".equals(attributes.getURI(i))) {
                        textPath.href = trim;
                        break;
                    }
                    break;
                }
                case 62: {
                    textPath.startOffset = parseLength(trim);
                    break;
                }
            }
        }
    }
    
    private void parseAttributesTextPosition(final SVG.TextPositionedContainer textPositionedContainer, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 83: {
                    textPositionedContainer.x = parseLengthList(trim);
                    break;
                }
                case 84: {
                    textPositionedContainer.y = parseLengthList(trim);
                    break;
                }
                case 10: {
                    textPositionedContainer.dx = parseLengthList(trim);
                    break;
                }
                case 11: {
                    textPositionedContainer.dy = parseLengthList(trim);
                    break;
                }
            }
        }
    }
    
    private void parseAttributesTransform(final SVG.HasTransform hasTransform, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            if (SVGAttr.fromString(attributes.getLocalName(i)) == SVGAttr.transform) {
                hasTransform.setTransform(this.parseTransformList(attributes.getValue(i)));
            }
        }
    }
    
    private void parseAttributesUse(final SVG.Use use, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 83: {
                    use.x = parseLength(trim);
                    break;
                }
                case 84: {
                    use.y = parseLength(trim);
                    break;
                }
                case 82: {
                    use.width = parseLength(trim);
                    if (use.width.isNegative()) {
                        throw new SAXException("Invalid <use> element. width cannot be negative");
                    }
                    break;
                }
                case 26: {
                    use.height = parseLength(trim);
                    if (use.height.isNegative()) {
                        throw new SAXException("Invalid <use> element. height cannot be negative");
                    }
                    break;
                }
                case 27: {
                    if ("http://www.w3.org/1999/xlink".equals(attributes.getURI(i))) {
                        use.href = trim;
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    private void parseAttributesViewBox(final SVG.SvgViewBoxContainer svgViewBoxContainer, final Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 81: {
                    svgViewBoxContainer.viewBox = parseViewBox(trim);
                    break;
                }
                case 49: {
                    parsePreserveAspectRatio(svgViewBoxContainer, trim);
                    break;
                }
            }
        }
    }
    
    private void parseCSSStyleSheet(final String s) throws SAXException {
        this.svgDocument.addCSSRules(new CSSParser(CSSParser.MediaType.screen).parse(s));
    }
    
    private static SVG.CSSClipRect parseClip(final String s) throws SAXException {
        SVG.CSSClipRect cssClipRect;
        if ("auto".equals(s)) {
            cssClipRect = null;
        }
        else {
            if (!s.toLowerCase(Locale.US).startsWith("rect(")) {
                throw new SAXException("Invalid clip attribute shape. Only rect() is supported.");
            }
            final TextScanner textScanner = new TextScanner(s.substring(5));
            textScanner.skipWhitespace();
            final SVG.Length lengthOrAuto = parseLengthOrAuto(textScanner);
            textScanner.skipCommaWhitespace();
            final SVG.Length lengthOrAuto2 = parseLengthOrAuto(textScanner);
            textScanner.skipCommaWhitespace();
            final SVG.Length lengthOrAuto3 = parseLengthOrAuto(textScanner);
            textScanner.skipCommaWhitespace();
            final SVG.Length lengthOrAuto4 = parseLengthOrAuto(textScanner);
            textScanner.skipWhitespace();
            if (!textScanner.consume(')')) {
                throw new SAXException("Bad rect() clip definition: " + s);
            }
            cssClipRect = new SVG.CSSClipRect(lengthOrAuto, lengthOrAuto2, lengthOrAuto3, lengthOrAuto4);
        }
        return cssClipRect;
    }
    
    private static SVG.Colour parseColour(String colourKeyword) throws SAXException {
        if (((String)colourKeyword).charAt(0) != '#') {
            if (!((String)colourKeyword).toLowerCase(Locale.US).startsWith("rgb(")) {
                colourKeyword = parseColourKeyword((String)colourKeyword);
                return (SVG.Colour)colourKeyword;
            }
            final TextScanner textScanner = new TextScanner(((String)colourKeyword).substring(4));
            textScanner.skipWhitespace();
            final int colourComponent = parseColourComponent(textScanner);
            textScanner.skipCommaWhitespace();
            final int colourComponent2 = parseColourComponent(textScanner);
            textScanner.skipCommaWhitespace();
            final int colourComponent3 = parseColourComponent(textScanner);
            textScanner.skipWhitespace();
            if (!textScanner.consume(')')) {
                throw new SAXException("Bad rgb() colour value: " + (String)colourKeyword);
            }
            colourKeyword = new SVG.Colour(colourComponent << 16 | colourComponent2 << 8 | colourComponent3);
            return (SVG.Colour)colourKeyword;
        }
        try {
            if (((String)colourKeyword).length() == 7) {
                colourKeyword = new SVG.Colour(Integer.parseInt(((String)colourKeyword).substring(1), 16));
            }
            else {
                if (((String)colourKeyword).length() != 4) {
                    throw new SAXException("Bad hex colour value: " + (String)colourKeyword);
                }
                final int int1 = Integer.parseInt(((String)colourKeyword).substring(1), 16);
                final int n = int1 & 0xF00;
                final int n2 = int1 & 0xF0;
                final int n3 = int1 & 0xF;
                colourKeyword = new SVG.Colour(n << 16 | n << 12 | n2 << 8 | n2 << 4 | n3 << 4 | n3);
            }
            return (SVG.Colour)colourKeyword;
        }
        catch (NumberFormatException ex) {
            throw new SAXException("Bad colour value: " + (String)colourKeyword);
        }
        throw new SAXException("Bad hex colour value: " + (String)colourKeyword);
    }
    
    private static int parseColourComponent(final TextScanner textScanner) throws SAXException {
        float floatValue;
        final float n = floatValue = textScanner.nextFloat();
        if (textScanner.consume('%')) {
            floatValue = 256.0f * n / 100.0f;
        }
        int n2;
        if (floatValue < 0.0f) {
            n2 = 0;
        }
        else if (floatValue > 255.0f) {
            n2 = 255;
        }
        else {
            n2 = (int)floatValue;
        }
        return n2;
    }
    
    private static SVG.Colour parseColourKeyword(final String str) throws SAXException {
        final Integer n = SVGParser.colourKeywords.get(str.toLowerCase(Locale.US));
        if (n == null) {
            throw new SAXException("Invalid colour keyword: " + str);
        }
        return new SVG.Colour(n);
    }
    
    private static SVG.SvgPaint parseColourSpecifer(final String s) throws SAXException {
        SVG.SvgPaint svgPaint;
        if (s.equals("none")) {
            svgPaint = null;
        }
        else if (s.equals("currentColor")) {
            svgPaint = SVG.CurrentColor.getInstance();
        }
        else {
            svgPaint = parseColour(s);
        }
        return svgPaint;
    }
    
    private static SVG.Style.FillRule parseFillRule(final String str) throws SAXException {
        SVG.Style.FillRule fillRule;
        if ("nonzero".equals(str)) {
            fillRule = SVG.Style.FillRule.NonZero;
        }
        else {
            if (!"evenodd".equals(str)) {
                throw new SAXException("Invalid fill-rule property: " + str);
            }
            fillRule = SVG.Style.FillRule.EvenOdd;
        }
        return fillRule;
    }
    
    private static float parseFloat(final String s) throws SAXException {
        if (s.length() == 0) {
            throw new SAXException("Invalid float value (empty string)");
        }
        try {
            return Float.parseFloat(s);
        }
        catch (NumberFormatException e) {
            throw new SAXException("Invalid float value: " + s, e);
        }
    }
    
    private static void parseFont(final SVG.Style style, final String str) throws SAXException {
        Integer n = null;
        final SVG.Style.FontStyle fontStyle = null;
        String s = null;
        if ("|caption|icon|menu|message-box|small-caption|status-bar|".indexOf(String.valueOf('|') + str + '|') == -1) {
            final TextScanner textScanner = new TextScanner(str);
            Enum<SVG.Style.FontStyle> normal = fontStyle;
            String nextToken;
            Integer n2;
            while (true) {
                nextToken = textScanner.nextToken('/');
                textScanner.skipWhitespace();
                if (nextToken == null) {
                    throw new SAXException("Invalid font style attribute: missing font size and family");
                }
                if (n != null && normal != null) {
                    n2 = n;
                    break;
                }
                if (nextToken.equals("normal")) {
                    continue;
                }
                Integer n3;
                if ((n3 = n) == null) {
                    n3 = SVGParser.fontWeightKeywords.get(nextToken);
                    if ((n = n3) != null) {
                        continue;
                    }
                }
                SVG.Style.FontStyle fontStyle2;
                if ((fontStyle2 = (SVG.Style.FontStyle)normal) == null) {
                    final SVG.Style.FontStyle fontStyle3 = (SVG.Style.FontStyle)(normal = SVGParser.fontStyleKeywords.get(nextToken));
                    n = n3;
                    if (fontStyle3 != null) {
                        continue;
                    }
                    fontStyle2 = fontStyle3;
                }
                normal = fontStyle2;
                n2 = n3;
                if (s != null) {
                    break;
                }
                normal = fontStyle2;
                n2 = n3;
                if (!nextToken.equals("small-caps")) {
                    break;
                }
                s = nextToken;
                normal = fontStyle2;
                n = n3;
            }
            final SVG.Length fontSize = parseFontSize(nextToken);
            if (textScanner.consume('/')) {
                textScanner.skipWhitespace();
                final String nextToken2 = textScanner.nextToken();
                if (nextToken2 == null) {
                    throw new SAXException("Invalid font style attribute: missing line-height");
                }
                parseLength(nextToken2);
                textScanner.skipWhitespace();
            }
            style.fontFamily = parseFontFamily(textScanner.restOfText());
            style.fontSize = fontSize;
            int intValue;
            if (n2 == null) {
                intValue = 400;
            }
            else {
                intValue = n2;
            }
            style.fontWeight = intValue;
            if (normal == null) {
                normal = SVG.Style.FontStyle.Normal;
            }
            style.fontStyle = (SVG.Style.FontStyle)normal;
            style.specifiedFlags |= 0x1E000L;
        }
    }
    
    private static List<String> parseFontFamily(final String s) throws SAXException {
        final List<String> list = null;
        final TextScanner textScanner = new TextScanner(s);
        List<String> list2 = list;
        List<String> list3;
        do {
            String s2;
            if ((s2 = textScanner.nextQuotedString()) == null) {
                s2 = textScanner.nextToken(',');
            }
            if (s2 == null) {
                return list2;
            }
            if ((list3 = list2) == null) {
                list3 = new ArrayList<String>();
            }
            list3.add(s2);
            textScanner.skipCommaWhitespace();
            list2 = list3;
        } while (!textScanner.empty());
        list2 = list3;
        return list2;
    }
    
    private static SVG.Length parseFontSize(final String key) throws SAXException {
        SVG.Length length;
        if ((length = SVGParser.fontSizeKeywords.get(key)) == null) {
            length = parseLength(key);
        }
        return length;
    }
    
    private static SVG.Style.FontStyle parseFontStyle(final String s) throws SAXException {
        final SVG.Style.FontStyle fontStyle = SVGParser.fontStyleKeywords.get(s);
        if (fontStyle == null) {
            throw new SAXException("Invalid font-style property: " + s);
        }
        return fontStyle;
    }
    
    private static Integer parseFontWeight(final String s) throws SAXException {
        final Integer n = SVGParser.fontWeightKeywords.get(s);
        if (n == null) {
            throw new SAXException("Invalid font-weight property: " + s);
        }
        return n;
    }
    
    private static String parseFunctionalIRI(String trim, final String str) throws SAXException {
        if (trim.equals("none")) {
            trim = null;
        }
        else {
            if (!trim.startsWith("url(") || !trim.endsWith(")")) {
                throw new SAXException("Bad " + str + " attribute. Expected \"none\" or \"url()\" format");
            }
            trim = trim.substring(4, trim.length() - 1).trim();
        }
        return trim;
    }
    
    private Float parseGradiantOffset(final String str) throws SAXException {
        if (str.length() == 0) {
            throw new SAXException("Invalid offset value in <stop> (empty string)");
        }
        final int length = str.length();
        boolean b = false;
        int endIndex = length;
        if (str.charAt(str.length() - 1) == '%') {
            endIndex = length - 1;
            b = true;
        }
        try {
            float float1;
            final float n = float1 = Float.parseFloat(str.substring(0, endIndex));
            if (b) {
                float1 = n / 100.0f;
            }
            float f;
            if (float1 < 0.0f) {
                f = 0.0f;
            }
            else {
                f = float1;
                if (float1 > 100.0f) {
                    f = 100.0f;
                }
            }
            return f;
        }
        catch (NumberFormatException e) {
            throw new SAXException("Invalid offset value in <stop>: " + str, e);
        }
    }
    
    protected static SVG.Length parseLength(final String s) throws SAXException {
        if (s.length() == 0) {
            throw new SAXException("Invalid length value (empty string)");
        }
        final int length = s.length();
        final SVG.Unit px = SVG.Unit.px;
        final char char1 = s.charAt(length - 1);
        Label_0075: {
            if (char1 != '%') {
                break Label_0075;
            }
            int n = length - 1;
            Enum<SVG.Unit> enum1 = SVG.Unit.percent;
            try {
                Label_0051: {
                    return new SVG.Length(Float.parseFloat(s.substring(0, n)), (SVG.Unit)enum1);
                }
                while (true) {
                    n = length;
                    enum1 = px;
                    Block_4: {
                        Block_6: {
                            break Block_6;
                            n = length;
                            enum1 = px;
                            break Block_4;
                        }
                        n = length - 2;
                        final String substring = s.substring(n);
                        try {
                            enum1 = SVG.Unit.valueOf(substring.toLowerCase(Locale.US));
                        }
                        catch (IllegalArgumentException ex) {
                            throw new SAXException("Invalid length unit specifier: " + s);
                        }
                    }
                    n = length;
                    enum1 = px;
                    continue;
                }
            }
            // iftrue(Label_0051:, !Character.isLetter(s.charAt(length - 2)))
            // iftrue(Label_0051:, length <= 2)
            // iftrue(Label_0051:, !Character.isLetter(char1))
            catch (NumberFormatException e) {
                throw new SAXException("Invalid length value: " + s, e);
            }
        }
    }
    
    private static List<SVG.Length> parseLengthList(final String s) throws SAXException {
        if (s.length() == 0) {
            throw new SAXException("Invalid length list (empty string)");
        }
        final ArrayList<SVG.Length> list = new ArrayList<SVG.Length>(1);
        final TextScanner textScanner = new TextScanner(s);
        textScanner.skipWhitespace();
        while (!textScanner.empty()) {
            final Float nextFloat = textScanner.nextFloat();
            if (nextFloat == null) {
                throw new SAXException("Invalid length list value: " + textScanner.ahead());
            }
            Enum<SVG.Unit> enum1;
            if ((enum1 = textScanner.nextUnit()) == null) {
                enum1 = SVG.Unit.px;
            }
            list.add(new SVG.Length(nextFloat, (SVG.Unit)enum1));
            textScanner.skipCommaWhitespace();
        }
        return list;
    }
    
    private static SVG.Length parseLengthOrAuto(final TextScanner textScanner) {
        SVG.Length nextLength;
        if (textScanner.consume("auto")) {
            nextLength = new SVG.Length(0.0f);
        }
        else {
            nextLength = textScanner.nextLength();
        }
        return nextLength;
    }
    
    private static float parseOpacity(final String s) throws SAXException {
        final float float1 = parseFloat(s);
        float n;
        if (float1 < 0.0f) {
            n = 0.0f;
        }
        else {
            n = float1;
            if (float1 > 1.0f) {
                n = 1.0f;
            }
        }
        return n;
    }
    
    private static Boolean parseOverflow(final String str) throws SAXException {
        Boolean b;
        if ("visible".equals(str) || "auto".equals(str)) {
            b = Boolean.TRUE;
        }
        else {
            if (!"hidden".equals(str) && !"scroll".equals(str)) {
                throw new SAXException("Invalid toverflow property: " + str);
            }
            b = Boolean.FALSE;
        }
        return b;
    }
    
    private static SVG.SvgPaint parsePaintSpecifier(final String s, final String str) throws SAXException {
        SVG.SvgPaint colourSpecifer2;
        if (s.startsWith("url(")) {
            final int index = s.indexOf(")");
            if (index == -1) {
                throw new SAXException("Bad " + str + " attribute. Unterminated url() reference");
            }
            final String trim = s.substring(4, index).trim();
            final SVG.SvgPaint svgPaint = null;
            final String trim2 = s.substring(index + 1).trim();
            SVG.SvgPaint colourSpecifer = svgPaint;
            if (trim2.length() > 0) {
                colourSpecifer = parseColourSpecifer(trim2);
            }
            colourSpecifer2 = new SVG.PaintReference(trim, colourSpecifer);
        }
        else {
            colourSpecifer2 = parseColourSpecifer(s);
        }
        return colourSpecifer2;
    }
    
    private static SVG.PathDefinition parsePath(final String s) throws SAXException {
        final TextScanner textScanner = new TextScanner(s);
        final float n = 0.0f;
        final float n2 = 0.0f;
        final float n3 = 0.0f;
        final float n4 = 0.0f;
        final float n5 = 0.0f;
        final float n6 = 0.0f;
        final SVG.PathDefinition pathDefinition = new SVG.PathDefinition();
        if (!textScanner.empty()) {
            final int intValue = textScanner.nextChar();
            float n7 = n;
            float n8 = n2;
            float n9 = n5;
            float n10 = n6;
            float n11 = n3;
            float n12 = n4;
            int intValue2;
            if ((intValue2 = intValue) != 77) {
                if (intValue != 109) {
                    return pathDefinition;
                }
                intValue2 = intValue;
                n12 = n4;
                n11 = n3;
                n10 = n6;
                n9 = n5;
                n8 = n2;
                n7 = n;
            }
            Block_23: {
                Block_21: {
                    Block_19: {
                        Block_17: {
                            Block_15: {
                                Block_13: {
                                    Block_11: {
                                        Block_5: {
                                            Label_0378: {
                                            Label_0296:
                                                while (true) {
                                                    textScanner.skipWhitespace();
                                                    float n14 = 0.0f;
                                                    float n13 = 0.0f;
                                                    float n16 = 0.0f;
                                                    float n15 = 0.0f;
                                                    float floatValue = 0.0f;
                                                    float floatValue2 = 0.0f;
                                                    switch (intValue2) {
                                                        default: {
                                                            break Label_0296;
                                                        }
                                                        case 65:
                                                        case 97: {
                                                            final Float nextFloat = textScanner.nextFloat();
                                                            final Float checkedNextFloat = textScanner.checkedNextFloat(nextFloat);
                                                            final Float checkedNextFloat2 = textScanner.checkedNextFloat(checkedNextFloat);
                                                            final Boolean checkedNextFlag = textScanner.checkedNextFlag(checkedNextFloat2);
                                                            final Boolean checkedNextFlag2 = textScanner.checkedNextFlag(checkedNextFlag);
                                                            final Float checkedNextFloat3 = textScanner.checkedNextFloat(checkedNextFlag2);
                                                            final Float checkedNextFloat4 = textScanner.checkedNextFloat(checkedNextFloat3);
                                                            if (checkedNextFloat4 == null || nextFloat < 0.0f || checkedNextFloat < 0.0f) {
                                                                break Label_0378;
                                                            }
                                                            Float value = checkedNextFloat3;
                                                            Float value2 = checkedNextFloat4;
                                                            if (intValue2 == 97) {
                                                                value = checkedNextFloat3 + n7;
                                                                value2 = checkedNextFloat4 + n8;
                                                            }
                                                            pathDefinition.arcTo(nextFloat, checkedNextFloat, checkedNextFloat2, checkedNextFlag, checkedNextFlag2, value, value2);
                                                            n13 = (n14 = value);
                                                            n15 = (n16 = value2);
                                                            floatValue = n11;
                                                            floatValue2 = n12;
                                                            break;
                                                        }
                                                        case 77:
                                                        case 109: {
                                                            final Float nextFloat2 = textScanner.nextFloat();
                                                            final Float checkedNextFloat5 = textScanner.checkedNextFloat(nextFloat2);
                                                            if (checkedNextFloat5 == null) {
                                                                break Block_5;
                                                            }
                                                            Float value3 = nextFloat2;
                                                            Float value4 = checkedNextFloat5;
                                                            if (intValue2 == 109) {
                                                                value3 = nextFloat2;
                                                                value4 = checkedNextFloat5;
                                                                if (!pathDefinition.isEmpty()) {
                                                                    value3 = nextFloat2 + n7;
                                                                    value4 = checkedNextFloat5 + n8;
                                                                }
                                                            }
                                                            pathDefinition.moveTo(value3, value4);
                                                            n13 = (n14 = (floatValue = value3));
                                                            n15 = (n16 = (floatValue2 = value4));
                                                            if (intValue2 == 109) {
                                                                intValue2 = 108;
                                                                break;
                                                            }
                                                            intValue2 = 76;
                                                            break;
                                                        }
                                                        case 76:
                                                        case 108: {
                                                            final Float nextFloat3 = textScanner.nextFloat();
                                                            final Float checkedNextFloat6 = textScanner.checkedNextFloat(nextFloat3);
                                                            if (checkedNextFloat6 == null) {
                                                                break Block_11;
                                                            }
                                                            Float value5 = nextFloat3;
                                                            Float value6 = checkedNextFloat6;
                                                            if (intValue2 == 108) {
                                                                value5 = nextFloat3 + n7;
                                                                value6 = checkedNextFloat6 + n8;
                                                            }
                                                            pathDefinition.lineTo(value5, value6);
                                                            n13 = (n14 = value5);
                                                            n15 = (n16 = value6);
                                                            floatValue = n11;
                                                            floatValue2 = n12;
                                                            break;
                                                        }
                                                        case 67:
                                                        case 99: {
                                                            final Float nextFloat4 = textScanner.nextFloat();
                                                            final Float checkedNextFloat7 = textScanner.checkedNextFloat(nextFloat4);
                                                            final Float checkedNextFloat8 = textScanner.checkedNextFloat(checkedNextFloat7);
                                                            final Float checkedNextFloat9 = textScanner.checkedNextFloat(checkedNextFloat8);
                                                            final Float checkedNextFloat10 = textScanner.checkedNextFloat(checkedNextFloat9);
                                                            final Float checkedNextFloat11 = textScanner.checkedNextFloat(checkedNextFloat10);
                                                            if (checkedNextFloat11 == null) {
                                                                break Block_13;
                                                            }
                                                            Float value7 = checkedNextFloat10;
                                                            Float value8 = nextFloat4;
                                                            Float value9 = checkedNextFloat8;
                                                            Float value10 = checkedNextFloat11;
                                                            Float value11 = checkedNextFloat7;
                                                            Float value12 = checkedNextFloat9;
                                                            if (intValue2 == 99) {
                                                                value7 = checkedNextFloat10 + n7;
                                                                value10 = checkedNextFloat11 + n8;
                                                                value8 = nextFloat4 + n7;
                                                                value11 = checkedNextFloat7 + n8;
                                                                value9 = checkedNextFloat8 + n7;
                                                                value12 = checkedNextFloat9 + n8;
                                                            }
                                                            pathDefinition.cubicTo(value8, value11, value9, value12, value7, value10);
                                                            n13 = value9;
                                                            n15 = value12;
                                                            n14 = value7;
                                                            n16 = value10;
                                                            floatValue = n11;
                                                            floatValue2 = n12;
                                                            break;
                                                        }
                                                        case 83:
                                                        case 115: {
                                                            final Float nextFloat5 = textScanner.nextFloat();
                                                            final Float checkedNextFloat12 = textScanner.checkedNextFloat(nextFloat5);
                                                            final Float checkedNextFloat13 = textScanner.checkedNextFloat(checkedNextFloat12);
                                                            final Float checkedNextFloat14 = textScanner.checkedNextFloat(checkedNextFloat13);
                                                            if (checkedNextFloat14 == null) {
                                                                break Block_15;
                                                            }
                                                            Float value13 = checkedNextFloat13;
                                                            Float value14 = nextFloat5;
                                                            Float value15 = checkedNextFloat14;
                                                            Float value16 = checkedNextFloat12;
                                                            if (intValue2 == 115) {
                                                                value13 = checkedNextFloat13 + n7;
                                                                value15 = checkedNextFloat14 + n8;
                                                                value14 = nextFloat5 + n7;
                                                                value16 = checkedNextFloat12 + n8;
                                                            }
                                                            pathDefinition.cubicTo(2.0f * n7 - n9, 2.0f * n8 - n10, value14, value16, value13, value15);
                                                            n13 = value14;
                                                            n15 = value16;
                                                            n14 = value13;
                                                            n16 = value15;
                                                            floatValue = n11;
                                                            floatValue2 = n12;
                                                            break;
                                                        }
                                                        case 90:
                                                        case 122: {
                                                            pathDefinition.close();
                                                            n13 = n11;
                                                            n14 = n11;
                                                            n15 = n12;
                                                            n16 = n12;
                                                            floatValue = n11;
                                                            floatValue2 = n12;
                                                            break;
                                                        }
                                                        case 72:
                                                        case 104: {
                                                            final Float nextFloat6 = textScanner.nextFloat();
                                                            if (nextFloat6 == null) {
                                                                break Block_17;
                                                            }
                                                            Float value17 = nextFloat6;
                                                            if (intValue2 == 104) {
                                                                value17 = nextFloat6 + n7;
                                                            }
                                                            pathDefinition.lineTo(value17, n8);
                                                            n13 = (n14 = value17);
                                                            n16 = n8;
                                                            n15 = n10;
                                                            floatValue = n11;
                                                            floatValue2 = n12;
                                                            break;
                                                        }
                                                        case 86:
                                                        case 118: {
                                                            final Float nextFloat7 = textScanner.nextFloat();
                                                            if (nextFloat7 == null) {
                                                                break Block_19;
                                                            }
                                                            Float value18 = nextFloat7;
                                                            if (intValue2 == 118) {
                                                                value18 = nextFloat7 + n8;
                                                            }
                                                            pathDefinition.lineTo(n7, value18);
                                                            n15 = (n16 = value18);
                                                            n14 = n7;
                                                            n13 = n9;
                                                            floatValue = n11;
                                                            floatValue2 = n12;
                                                            break;
                                                        }
                                                        case 81:
                                                        case 113: {
                                                            final Float nextFloat8 = textScanner.nextFloat();
                                                            final Float checkedNextFloat15 = textScanner.checkedNextFloat(nextFloat8);
                                                            final Float checkedNextFloat16 = textScanner.checkedNextFloat(checkedNextFloat15);
                                                            final Float checkedNextFloat17 = textScanner.checkedNextFloat(checkedNextFloat16);
                                                            if (checkedNextFloat17 == null) {
                                                                break Block_21;
                                                            }
                                                            Float value19 = checkedNextFloat16;
                                                            Float value20 = nextFloat8;
                                                            Float value21 = checkedNextFloat17;
                                                            Float value22 = checkedNextFloat15;
                                                            if (intValue2 == 113) {
                                                                value19 = checkedNextFloat16 + n7;
                                                                value21 = checkedNextFloat17 + n8;
                                                                value20 = nextFloat8 + n7;
                                                                value22 = checkedNextFloat15 + n8;
                                                            }
                                                            pathDefinition.quadTo(value20, value22, value19, value21);
                                                            n13 = value20;
                                                            n15 = value22;
                                                            n14 = value19;
                                                            n16 = value21;
                                                            floatValue = n11;
                                                            floatValue2 = n12;
                                                            break;
                                                        }
                                                        case 84:
                                                        case 116: {
                                                            final Float value23 = 2.0f * n7 - n9;
                                                            final Float value24 = 2.0f * n8 - n10;
                                                            final Float nextFloat9 = textScanner.nextFloat();
                                                            final Float checkedNextFloat18 = textScanner.checkedNextFloat(nextFloat9);
                                                            if (checkedNextFloat18 == null) {
                                                                break Block_23;
                                                            }
                                                            Float value25 = nextFloat9;
                                                            Float value26 = checkedNextFloat18;
                                                            if (intValue2 == 116) {
                                                                value25 = nextFloat9 + n7;
                                                                value26 = checkedNextFloat18 + n8;
                                                            }
                                                            pathDefinition.quadTo(value23, value24, value25, value26);
                                                            n13 = value23;
                                                            n15 = value24;
                                                            n14 = value25;
                                                            n16 = value26;
                                                            floatValue = n11;
                                                            floatValue2 = n12;
                                                            break;
                                                        }
                                                    }
                                                    textScanner.skipCommaWhitespace();
                                                    if (textScanner.empty()) {
                                                        break;
                                                    }
                                                    n7 = n14;
                                                    n8 = n16;
                                                    n9 = n13;
                                                    n10 = n15;
                                                    n11 = floatValue;
                                                    n12 = floatValue2;
                                                    if (!textScanner.hasLetter()) {
                                                        continue;
                                                    }
                                                    intValue2 = textScanner.nextChar();
                                                    n7 = n14;
                                                    n8 = n16;
                                                    n9 = n13;
                                                    n10 = n15;
                                                    n11 = floatValue;
                                                    n12 = floatValue2;
                                                }
                                                return pathDefinition;
                                            }
                                            Log.e("SVGParser", "Bad path coords for " + (char)intValue2 + " path segment");
                                            return pathDefinition;
                                        }
                                        Log.e("SVGParser", "Bad path coords for " + (char)intValue2 + " path segment");
                                        return pathDefinition;
                                    }
                                    Log.e("SVGParser", "Bad path coords for " + (char)intValue2 + " path segment");
                                    return pathDefinition;
                                }
                                Log.e("SVGParser", "Bad path coords for " + (char)intValue2 + " path segment");
                                return pathDefinition;
                            }
                            Log.e("SVGParser", "Bad path coords for " + (char)intValue2 + " path segment");
                            return pathDefinition;
                        }
                        Log.e("SVGParser", "Bad path coords for " + (char)intValue2 + " path segment");
                        return pathDefinition;
                    }
                    Log.e("SVGParser", "Bad path coords for " + (char)intValue2 + " path segment");
                    return pathDefinition;
                }
                Log.e("SVGParser", "Bad path coords for " + (char)intValue2 + " path segment");
                return pathDefinition;
            }
            Log.e("SVGParser", "Bad path coords for " + (char)intValue2 + " path segment");
        }
        return pathDefinition;
    }
    
    private static void parsePreserveAspectRatio(final SVG.SvgPreserveAspectRatioContainer svgPreserveAspectRatioContainer, final String str) throws SAXException {
        final TextScanner textScanner = new TextScanner(str);
        textScanner.skipWhitespace();
        final PreserveAspectRatio.Scale scale = null;
        String key;
        if ("defer".equals(key = textScanner.nextToken())) {
            textScanner.skipWhitespace();
            key = textScanner.nextToken();
        }
        final PreserveAspectRatio.Alignment alignment = SVGParser.aspectRatioKeywords.get(key);
        textScanner.skipWhitespace();
        PreserveAspectRatio.Scale scale2 = scale;
        if (!textScanner.empty()) {
            final String nextToken = textScanner.nextToken();
            if (nextToken.equals("meet")) {
                scale2 = PreserveAspectRatio.Scale.Meet;
            }
            else {
                if (!nextToken.equals("slice")) {
                    throw new SAXException("Invalid preserveAspectRatio definition: " + str);
                }
                scale2 = PreserveAspectRatio.Scale.Slice;
            }
        }
        svgPreserveAspectRatioContainer.preserveAspectRatio = new PreserveAspectRatio(alignment, scale2);
    }
    
    private static Set<String> parseRequiredFeatures(String nextToken) throws SAXException {
        final TextScanner textScanner = new TextScanner(nextToken);
        final HashSet<String> set = new HashSet<String>();
        while (!textScanner.empty()) {
            nextToken = textScanner.nextToken();
            if (nextToken.startsWith("http://www.w3.org/TR/SVG11/feature#")) {
                set.add(nextToken.substring("http://www.w3.org/TR/SVG11/feature#".length()));
            }
            else {
                set.add("UNSUPPORTED");
            }
            textScanner.skipWhitespace();
        }
        return set;
    }
    
    private static Set<String> parseRequiredFormats(final String s) throws SAXException {
        final TextScanner textScanner = new TextScanner(s);
        final HashSet<String> set = new HashSet<String>();
        while (!textScanner.empty()) {
            set.add(textScanner.nextToken());
            textScanner.skipWhitespace();
        }
        return set;
    }
    
    private static SVG.Length[] parseStrokeDashArray(final String str) throws SAXException {
        final SVG.Length[] array = null;
        final TextScanner textScanner = new TextScanner(str);
        textScanner.skipWhitespace();
        SVG.Length[] array2;
        if (textScanner.empty()) {
            array2 = array;
        }
        else {
            final SVG.Length nextLength = textScanner.nextLength();
            array2 = array;
            if (nextLength != null) {
                if (nextLength.isNegative()) {
                    throw new SAXException("Invalid stroke-dasharray. Dash segemnts cannot be negative: " + str);
                }
                float floatValue = nextLength.floatValue();
                final ArrayList<SVG.Length> list = new ArrayList<SVG.Length>();
                list.add(nextLength);
                while (!textScanner.empty()) {
                    textScanner.skipCommaWhitespace();
                    final SVG.Length nextLength2 = textScanner.nextLength();
                    if (nextLength2 == null) {
                        throw new SAXException("Invalid stroke-dasharray. Non-Length content found: " + str);
                    }
                    if (nextLength2.isNegative()) {
                        throw new SAXException("Invalid stroke-dasharray. Dash segemnts cannot be negative: " + str);
                    }
                    list.add(nextLength2);
                    floatValue += nextLength2.floatValue();
                }
                array2 = array;
                if (floatValue != 0.0f) {
                    array2 = list.toArray(new SVG.Length[list.size()]);
                }
            }
        }
        return array2;
    }
    
    private static SVG.Style.LineCaps parseStrokeLineCap(final String s) throws SAXException {
        SVG.Style.LineCaps lineCaps;
        if ("butt".equals(s)) {
            lineCaps = SVG.Style.LineCaps.Butt;
        }
        else if ("round".equals(s)) {
            lineCaps = SVG.Style.LineCaps.Round;
        }
        else {
            if (!"square".equals(s)) {
                throw new SAXException("Invalid stroke-linecap property: " + s);
            }
            lineCaps = SVG.Style.LineCaps.Square;
        }
        return lineCaps;
    }
    
    private static SVG.Style.LineJoin parseStrokeLineJoin(final String s) throws SAXException {
        SVG.Style.LineJoin lineJoin;
        if ("miter".equals(s)) {
            lineJoin = SVG.Style.LineJoin.Miter;
        }
        else if ("round".equals(s)) {
            lineJoin = SVG.Style.LineJoin.Round;
        }
        else {
            if (!"bevel".equals(s)) {
                throw new SAXException("Invalid stroke-linejoin property: " + s);
            }
            lineJoin = SVG.Style.LineJoin.Bevel;
        }
        return lineJoin;
    }
    
    private static void parseStyle(final SVG.SvgElementBase svgElementBase, String nextToken) throws SAXException {
        final TextScanner textScanner = new TextScanner(nextToken.replaceAll("/\\*.*?\\*/", ""));
        while (true) {
            nextToken = textScanner.nextToken(':');
            textScanner.skipWhitespace();
            if (!textScanner.consume(':')) {
                break;
            }
            textScanner.skipWhitespace();
            final String nextToken2 = textScanner.nextToken(';');
            if (nextToken2 == null) {
                break;
            }
            textScanner.skipWhitespace();
            if (!textScanner.empty() && !textScanner.consume(';')) {
                continue;
            }
            if (svgElementBase.style == null) {
                svgElementBase.style = new SVG.Style();
            }
            processStyleProperty(svgElementBase.style, nextToken, nextToken2);
            textScanner.skipWhitespace();
        }
    }
    
    private static Set<String> parseSystemLanguage(String substring) throws SAXException {
        final TextScanner textScanner = new TextScanner(substring);
        final HashSet<String> set = new HashSet<String>();
        while (!textScanner.empty()) {
            final String nextToken = textScanner.nextToken();
            final int index = nextToken.indexOf(45);
            substring = nextToken;
            if (index != -1) {
                substring = nextToken.substring(0, index);
            }
            set.add(new Locale(substring, "", "").getLanguage());
            textScanner.skipWhitespace();
        }
        return set;
    }
    
    private static SVG.Style.TextAnchor parseTextAnchor(final String s) throws SAXException {
        SVG.Style.TextAnchor textAnchor;
        if ("start".equals(s)) {
            textAnchor = SVG.Style.TextAnchor.Start;
        }
        else if ("middle".equals(s)) {
            textAnchor = SVG.Style.TextAnchor.Middle;
        }
        else {
            if (!"end".equals(s)) {
                throw new SAXException("Invalid text-anchor property: " + s);
            }
            textAnchor = SVG.Style.TextAnchor.End;
        }
        return textAnchor;
    }
    
    private static SVG.Style.TextDecoration parseTextDecoration(final String s) throws SAXException {
        SVG.Style.TextDecoration textDecoration;
        if ("none".equals(s)) {
            textDecoration = SVG.Style.TextDecoration.None;
        }
        else if ("underline".equals(s)) {
            textDecoration = SVG.Style.TextDecoration.Underline;
        }
        else if ("overline".equals(s)) {
            textDecoration = SVG.Style.TextDecoration.Overline;
        }
        else if ("line-through".equals(s)) {
            textDecoration = SVG.Style.TextDecoration.LineThrough;
        }
        else {
            if (!"blink".equals(s)) {
                throw new SAXException("Invalid text-decoration property: " + s);
            }
            textDecoration = SVG.Style.TextDecoration.Blink;
        }
        return textDecoration;
    }
    
    private static SVG.Style.TextDirection parseTextDirection(final String str) throws SAXException {
        SVG.Style.TextDirection textDirection;
        if ("ltr".equals(str)) {
            textDirection = SVG.Style.TextDirection.LTR;
        }
        else {
            if (!"rtl".equals(str)) {
                throw new SAXException("Invalid direction property: " + str);
            }
            textDirection = SVG.Style.TextDirection.RTL;
        }
        return textDirection;
    }
    
    private Matrix parseTransformList(final String s) throws SAXException {
        final Matrix matrix = new Matrix();
        final TextScanner textScanner = new TextScanner(s);
        textScanner.skipWhitespace();
        while (!textScanner.empty()) {
            final String nextFunction = textScanner.nextFunction();
            if (nextFunction == null) {
                throw new SAXException("Bad transform function encountered in transform list: " + s);
            }
            if (nextFunction.equals("matrix")) {
                textScanner.skipWhitespace();
                final Float nextFloat = textScanner.nextFloat();
                textScanner.skipCommaWhitespace();
                final Float nextFloat2 = textScanner.nextFloat();
                textScanner.skipCommaWhitespace();
                final Float nextFloat3 = textScanner.nextFloat();
                textScanner.skipCommaWhitespace();
                final Float nextFloat4 = textScanner.nextFloat();
                textScanner.skipCommaWhitespace();
                final Float nextFloat5 = textScanner.nextFloat();
                textScanner.skipCommaWhitespace();
                final Float nextFloat6 = textScanner.nextFloat();
                textScanner.skipWhitespace();
                if (nextFloat6 == null || !textScanner.consume(')')) {
                    throw new SAXException("Invalid transform list: " + s);
                }
                final Matrix matrix2 = new Matrix();
                matrix2.setValues(new float[] { nextFloat, nextFloat3, nextFloat5, nextFloat2, nextFloat4, nextFloat6, 0.0f, 0.0f, 1.0f });
                matrix.preConcat(matrix2);
            }
            else if (nextFunction.equals("translate")) {
                textScanner.skipWhitespace();
                final Float nextFloat7 = textScanner.nextFloat();
                final Float possibleNextFloat = textScanner.possibleNextFloat();
                textScanner.skipWhitespace();
                if (nextFloat7 == null || !textScanner.consume(')')) {
                    throw new SAXException("Invalid transform list: " + s);
                }
                if (possibleNextFloat == null) {
                    matrix.preTranslate((float)nextFloat7, 0.0f);
                }
                else {
                    matrix.preTranslate((float)nextFloat7, (float)possibleNextFloat);
                }
            }
            else if (nextFunction.equals("scale")) {
                textScanner.skipWhitespace();
                final Float nextFloat8 = textScanner.nextFloat();
                final Float possibleNextFloat2 = textScanner.possibleNextFloat();
                textScanner.skipWhitespace();
                if (nextFloat8 == null || !textScanner.consume(')')) {
                    throw new SAXException("Invalid transform list: " + s);
                }
                if (possibleNextFloat2 == null) {
                    matrix.preScale((float)nextFloat8, (float)nextFloat8);
                }
                else {
                    matrix.preScale((float)nextFloat8, (float)possibleNextFloat2);
                }
            }
            else if (nextFunction.equals("rotate")) {
                textScanner.skipWhitespace();
                final Float nextFloat9 = textScanner.nextFloat();
                final Float possibleNextFloat3 = textScanner.possibleNextFloat();
                final Float possibleNextFloat4 = textScanner.possibleNextFloat();
                textScanner.skipWhitespace();
                if (nextFloat9 == null || !textScanner.consume(')')) {
                    throw new SAXException("Invalid transform list: " + s);
                }
                if (possibleNextFloat3 == null) {
                    matrix.preRotate((float)nextFloat9);
                }
                else {
                    if (possibleNextFloat4 == null) {
                        throw new SAXException("Invalid transform list: " + s);
                    }
                    matrix.preRotate((float)nextFloat9, (float)possibleNextFloat3, (float)possibleNextFloat4);
                }
            }
            else if (nextFunction.equals("skewX")) {
                textScanner.skipWhitespace();
                final Float nextFloat10 = textScanner.nextFloat();
                textScanner.skipWhitespace();
                if (nextFloat10 == null || !textScanner.consume(')')) {
                    throw new SAXException("Invalid transform list: " + s);
                }
                matrix.preSkew((float)Math.tan(Math.toRadians(nextFloat10)), 0.0f);
            }
            else if (nextFunction.equals("skewY")) {
                textScanner.skipWhitespace();
                final Float nextFloat11 = textScanner.nextFloat();
                textScanner.skipWhitespace();
                if (nextFloat11 == null || !textScanner.consume(')')) {
                    throw new SAXException("Invalid transform list: " + s);
                }
                matrix.preSkew(0.0f, (float)Math.tan(Math.toRadians(nextFloat11)));
            }
            else if (nextFunction != null) {
                throw new SAXException("Invalid transform list fn: " + nextFunction + ")");
            }
            if (textScanner.empty()) {
                break;
            }
            textScanner.skipCommaWhitespace();
        }
        return matrix;
    }
    
    private static SVG.Style.VectorEffect parseVectorEffect(final String str) throws SAXException {
        SVG.Style.VectorEffect vectorEffect;
        if ("none".equals(str)) {
            vectorEffect = SVG.Style.VectorEffect.None;
        }
        else {
            if (!"non-scaling-stroke".equals(str)) {
                throw new SAXException("Invalid vector-effect property: " + str);
            }
            vectorEffect = SVG.Style.VectorEffect.NonScalingStroke;
        }
        return vectorEffect;
    }
    
    private static SVG.Box parseViewBox(final String s) throws SAXException {
        final TextScanner textScanner = new TextScanner(s);
        textScanner.skipWhitespace();
        final Float nextFloat = textScanner.nextFloat();
        textScanner.skipCommaWhitespace();
        final Float nextFloat2 = textScanner.nextFloat();
        textScanner.skipCommaWhitespace();
        final Float nextFloat3 = textScanner.nextFloat();
        textScanner.skipCommaWhitespace();
        final Float nextFloat4 = textScanner.nextFloat();
        if (nextFloat == null || nextFloat2 == null || nextFloat3 == null || nextFloat4 == null) {
            throw new SAXException("Invalid viewBox definition - should have four numbers");
        }
        if (nextFloat3 < 0.0f) {
            throw new SAXException("Invalid viewBox. width cannot be negative");
        }
        if (nextFloat4 < 0.0f) {
            throw new SAXException("Invalid viewBox. height cannot be negative");
        }
        return new SVG.Box(nextFloat, nextFloat2, nextFloat3, nextFloat4);
    }
    
    private void path(final Attributes attributes) throws SAXException {
        this.debug("<path>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.Path path = new SVG.Path();
        path.document = this.svgDocument;
        path.parent = this.currentElement;
        this.parseAttributesCore(path, attributes);
        this.parseAttributesStyle(path, attributes);
        this.parseAttributesTransform(path, attributes);
        this.parseAttributesConditional(path, attributes);
        this.parseAttributesPath(path, attributes);
        this.currentElement.addChild(path);
    }
    
    private void pattern(final Attributes attributes) throws SAXException {
        this.debug("<pattern>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.Pattern currentElement = new SVG.Pattern();
        currentElement.document = this.svgDocument;
        currentElement.parent = this.currentElement;
        this.parseAttributesCore(currentElement, attributes);
        this.parseAttributesStyle(currentElement, attributes);
        this.parseAttributesConditional(currentElement, attributes);
        this.parseAttributesViewBox(currentElement, attributes);
        this.parseAttributesPattern(currentElement, attributes);
        this.currentElement.addChild(currentElement);
        this.currentElement = currentElement;
    }
    
    private void polygon(final Attributes attributes) throws SAXException {
        this.debug("<polygon>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.Polygon polygon = new SVG.Polygon();
        polygon.document = this.svgDocument;
        polygon.parent = this.currentElement;
        this.parseAttributesCore(polygon, attributes);
        this.parseAttributesStyle(polygon, attributes);
        this.parseAttributesTransform(polygon, attributes);
        this.parseAttributesConditional(polygon, attributes);
        this.parseAttributesPolyLine(polygon, attributes, "polygon");
        this.currentElement.addChild(polygon);
    }
    
    private void polyline(final Attributes attributes) throws SAXException {
        this.debug("<polyline>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.PolyLine polyLine = new SVG.PolyLine();
        polyLine.document = this.svgDocument;
        polyLine.parent = this.currentElement;
        this.parseAttributesCore(polyLine, attributes);
        this.parseAttributesStyle(polyLine, attributes);
        this.parseAttributesTransform(polyLine, attributes);
        this.parseAttributesConditional(polyLine, attributes);
        this.parseAttributesPolyLine(polyLine, attributes, "polyline");
        this.currentElement.addChild(polyLine);
    }
    
    protected static void processStyleProperty(final SVG.Style style, final String s, final String str) throws SAXException {
        if (str.length() != 0 && !str.equals("inherit")) {
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(s).ordinal()]) {
                case 2: {
                    style.clip = parseClip(str);
                    style.specifiedFlags |= 0x100000L;
                    break;
                }
                case 16: {
                    style.fill = parsePaintSpecifier(str, "fill");
                    style.specifiedFlags |= 0x1L;
                    break;
                }
                case 17: {
                    style.fillRule = parseFillRule(str);
                    style.specifiedFlags |= 0x2L;
                    break;
                }
                case 18: {
                    style.fillOpacity = parseOpacity(str);
                    style.specifiedFlags |= 0x4L;
                    break;
                }
                case 65: {
                    style.stroke = parsePaintSpecifier(str, "stroke");
                    style.specifiedFlags |= 0x8L;
                    break;
                }
                case 71: {
                    style.strokeOpacity = parseOpacity(str);
                    style.specifiedFlags |= 0x10L;
                    break;
                }
                case 72: {
                    style.strokeWidth = parseLength(str);
                    style.specifiedFlags |= 0x20L;
                    break;
                }
                case 68: {
                    style.strokeLineCap = parseStrokeLineCap(str);
                    style.specifiedFlags |= 0x40L;
                    break;
                }
                case 69: {
                    style.strokeLineJoin = parseStrokeLineJoin(str);
                    style.specifiedFlags |= 0x80L;
                    break;
                }
                case 70: {
                    style.strokeMiterLimit = parseFloat(str);
                    style.specifiedFlags |= 0x100L;
                    break;
                }
                case 66: {
                    if ("none".equals(str)) {
                        style.strokeDashArray = null;
                    }
                    else {
                        style.strokeDashArray = parseStrokeDashArray(str);
                    }
                    style.specifiedFlags |= 0x200L;
                    break;
                }
                case 67: {
                    style.strokeDashOffset = parseLength(str);
                    style.specifiedFlags |= 0x400L;
                    break;
                }
                case 41: {
                    style.opacity = parseOpacity(str);
                    style.specifiedFlags |= 0x800L;
                    break;
                }
                case 6: {
                    style.color = parseColour(str);
                    style.specifiedFlags |= 0x1000L;
                    break;
                }
                case 19: {
                    parseFont(style, str);
                    break;
                }
                case 20: {
                    style.fontFamily = parseFontFamily(str);
                    style.specifiedFlags |= 0x2000L;
                    break;
                }
                case 21: {
                    style.fontSize = parseFontSize(str);
                    style.specifiedFlags |= 0x4000L;
                    break;
                }
                case 22: {
                    style.fontWeight = parseFontWeight(str);
                    style.specifiedFlags |= 0x8000L;
                    break;
                }
                case 23: {
                    style.fontStyle = parseFontStyle(str);
                    style.specifiedFlags |= 0x10000L;
                    break;
                }
                case 76: {
                    style.textDecoration = parseTextDecoration(str);
                    style.specifiedFlags |= 0x20000L;
                    break;
                }
                case 9: {
                    style.direction = parseTextDirection(str);
                    style.specifiedFlags |= 0x1000000000L;
                    break;
                }
                case 75: {
                    style.textAnchor = parseTextAnchor(str);
                    style.specifiedFlags |= 0x40000L;
                    break;
                }
                case 43: {
                    style.overflow = parseOverflow(str);
                    style.specifiedFlags |= 0x80000L;
                    break;
                }
                case 29: {
                    style.markerStart = parseFunctionalIRI(str, s);
                    style.markerMid = style.markerStart;
                    style.markerEnd = style.markerStart;
                    style.specifiedFlags |= 0xE00000L;
                    break;
                }
                case 30: {
                    style.markerStart = parseFunctionalIRI(str, s);
                    style.specifiedFlags |= 0x200000L;
                    break;
                }
                case 31: {
                    style.markerMid = parseFunctionalIRI(str, s);
                    style.specifiedFlags |= 0x400000L;
                    break;
                }
                case 32: {
                    style.markerEnd = parseFunctionalIRI(str, s);
                    style.specifiedFlags |= 0x800000L;
                    break;
                }
                case 15: {
                    if (str.indexOf(124) >= 0 || "|inline|block|list-item|run-in|compact|marker|table|inline-table|table-row-group|table-header-group|table-footer-group|table-row|table-column-group|table-column|table-cell|table-caption|none|".indexOf(String.valueOf('|') + str + '|') == -1) {
                        throw new SAXException("Invalid value for \"display\" attribute: " + str);
                    }
                    style.display = !str.equals("none");
                    style.specifiedFlags |= 0x1000000L;
                    break;
                }
                case 91: {
                    if (str.indexOf(124) >= 0 || "|visible|hidden|collapse|".indexOf(String.valueOf('|') + str + '|') == -1) {
                        throw new SAXException("Invalid value for \"visibility\" attribute: " + str);
                    }
                    style.visibility = str.equals("visible");
                    style.specifiedFlags |= 0x2000000L;
                    break;
                }
                case 63: {
                    if (str.equals("currentColor")) {
                        style.stopColor = SVG.CurrentColor.getInstance();
                    }
                    else {
                        style.stopColor = parseColour(str);
                    }
                    style.specifiedFlags |= 0x4000000L;
                    break;
                }
                case 64: {
                    style.stopOpacity = parseOpacity(str);
                    style.specifiedFlags |= 0x8000000L;
                    break;
                }
                case 3: {
                    style.clipPath = parseFunctionalIRI(str, s);
                    style.specifiedFlags |= 0x10000000L;
                    break;
                }
                case 5: {
                    style.clipRule = parseFillRule(str);
                    style.specifiedFlags |= 0x20000000L;
                    break;
                }
                case 36: {
                    style.mask = parseFunctionalIRI(str, s);
                    style.specifiedFlags |= 0x40000000L;
                    break;
                }
                case 59: {
                    if (str.equals("currentColor")) {
                        style.solidColor = SVG.CurrentColor.getInstance();
                    }
                    else {
                        style.solidColor = parseColour(str);
                    }
                    style.specifiedFlags |= 0x80000000L;
                    break;
                }
                case 60: {
                    style.solidOpacity = parseOpacity(str);
                    style.specifiedFlags |= 0x100000000L;
                    break;
                }
                case 89: {
                    if (str.equals("currentColor")) {
                        style.viewportFill = SVG.CurrentColor.getInstance();
                    }
                    else {
                        style.viewportFill = parseColour(str);
                    }
                    style.specifiedFlags |= 0x200000000L;
                    break;
                }
                case 90: {
                    style.viewportFillOpacity = parseOpacity(str);
                    style.specifiedFlags |= 0x400000000L;
                    break;
                }
                case 79: {
                    style.vectorEffect = parseVectorEffect(str);
                    style.specifiedFlags |= 0x800000000L;
                    break;
                }
            }
        }
    }
    
    private void radialGradient(final Attributes attributes) throws SAXException {
        this.debug("<radialGradient>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.SvgRadialGradient currentElement = new SVG.SvgRadialGradient();
        currentElement.document = this.svgDocument;
        currentElement.parent = this.currentElement;
        this.parseAttributesCore(currentElement, attributes);
        this.parseAttributesStyle(currentElement, attributes);
        this.parseAttributesGradient(currentElement, attributes);
        this.parseAttributesRadialGradient(currentElement, attributes);
        this.currentElement.addChild(currentElement);
        this.currentElement = currentElement;
    }
    
    private void rect(final Attributes attributes) throws SAXException {
        this.debug("<rect>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.Rect rect = new SVG.Rect();
        rect.document = this.svgDocument;
        rect.parent = this.currentElement;
        this.parseAttributesCore(rect, attributes);
        this.parseAttributesStyle(rect, attributes);
        this.parseAttributesTransform(rect, attributes);
        this.parseAttributesConditional(rect, attributes);
        this.parseAttributesRect(rect, attributes);
        this.currentElement.addChild(rect);
    }
    
    private void solidColor(final Attributes attributes) throws SAXException {
        this.debug("<solidColor>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.SolidColor currentElement = new SVG.SolidColor();
        currentElement.document = this.svgDocument;
        currentElement.parent = this.currentElement;
        this.parseAttributesCore(currentElement, attributes);
        this.parseAttributesStyle(currentElement, attributes);
        this.currentElement.addChild(currentElement);
        this.currentElement = currentElement;
    }
    
    private void stop(final Attributes attributes) throws SAXException {
        this.debug("<stop>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        if (!(this.currentElement instanceof SVG.GradientElement)) {
            throw new SAXException("Invalid document. <stop> elements are only valid inside <linearGradiant> or <radialGradient> elements.");
        }
        final SVG.Stop currentElement = new SVG.Stop();
        currentElement.document = this.svgDocument;
        currentElement.parent = this.currentElement;
        this.parseAttributesCore(currentElement, attributes);
        this.parseAttributesStyle(currentElement, attributes);
        this.parseAttributesStop(currentElement, attributes);
        this.currentElement.addChild(currentElement);
        this.currentElement = currentElement;
    }
    
    private void style(final Attributes attributes) throws SAXException {
        this.debug("<style>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        boolean equals = true;
        String s = "all";
        for (int i = 0; i < attributes.getLength(); ++i) {
            final String trim = attributes.getValue(i).trim();
            switch ($SWITCH_TABLE$com$caverock$androidsvg$SVGParser$SVGAttr()[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 78: {
                    equals = trim.equals("text/css");
                    break;
                }
                case 39: {
                    s = trim;
                    break;
                }
            }
        }
        if (equals && CSSParser.mediaMatches(s, CSSParser.MediaType.screen)) {
            this.inStyleElement = true;
        }
        else {
            this.ignoring = true;
            this.ignoreDepth = 1;
        }
    }
    
    private void svg(final Attributes attributes) throws SAXException {
        this.debug("<svg>", new Object[0]);
        final SVG.Svg svg = new SVG.Svg();
        svg.document = this.svgDocument;
        svg.parent = this.currentElement;
        this.parseAttributesCore(svg, attributes);
        this.parseAttributesStyle(svg, attributes);
        this.parseAttributesConditional(svg, attributes);
        this.parseAttributesViewBox(svg, attributes);
        this.parseAttributesSVG(svg, attributes);
        if (this.currentElement == null) {
            this.svgDocument.setRootElement(svg);
        }
        else {
            this.currentElement.addChild(svg);
        }
        this.currentElement = svg;
    }
    
    private void symbol(final Attributes attributes) throws SAXException {
        this.debug("<symbol>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.Symbol currentElement = new SVG.Symbol();
        currentElement.document = this.svgDocument;
        currentElement.parent = this.currentElement;
        this.parseAttributesCore(currentElement, attributes);
        this.parseAttributesStyle(currentElement, attributes);
        this.parseAttributesConditional(currentElement, attributes);
        this.parseAttributesViewBox(currentElement, attributes);
        this.currentElement.addChild(currentElement);
        this.currentElement = currentElement;
    }
    
    private void text(final Attributes attributes) throws SAXException {
        this.debug("<text>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.Text currentElement = new SVG.Text();
        currentElement.document = this.svgDocument;
        currentElement.parent = this.currentElement;
        this.parseAttributesCore(currentElement, attributes);
        this.parseAttributesStyle(currentElement, attributes);
        this.parseAttributesTransform(currentElement, attributes);
        this.parseAttributesConditional(currentElement, attributes);
        this.parseAttributesTextPosition(currentElement, attributes);
        this.currentElement.addChild(currentElement);
        this.currentElement = currentElement;
    }
    
    private void textPath(final Attributes attributes) throws SAXException {
        this.debug("<textPath>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.TextPath currentElement = new SVG.TextPath();
        currentElement.document = this.svgDocument;
        currentElement.parent = this.currentElement;
        this.parseAttributesCore(currentElement, attributes);
        this.parseAttributesStyle(currentElement, attributes);
        this.parseAttributesConditional(currentElement, attributes);
        this.parseAttributesTextPath(currentElement, attributes);
        this.currentElement.addChild(currentElement);
        this.currentElement = currentElement;
        if (currentElement.parent instanceof SVG.TextRoot) {
            currentElement.setTextRoot((SVG.TextRoot)currentElement.parent);
        }
        else {
            currentElement.setTextRoot(((SVG.TextChild)currentElement.parent).getTextRoot());
        }
    }
    
    private void tref(final Attributes attributes) throws SAXException {
        this.debug("<tref>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        if (!(this.currentElement instanceof SVG.TextContainer)) {
            throw new SAXException("Invalid document. <tref> elements are only valid inside <text> or <tspan> elements.");
        }
        final SVG.TRef tRef = new SVG.TRef();
        tRef.document = this.svgDocument;
        tRef.parent = this.currentElement;
        this.parseAttributesCore(tRef, attributes);
        this.parseAttributesStyle(tRef, attributes);
        this.parseAttributesConditional(tRef, attributes);
        this.parseAttributesTRef(tRef, attributes);
        this.currentElement.addChild(tRef);
        if (tRef.parent instanceof SVG.TextRoot) {
            tRef.setTextRoot((SVG.TextRoot)tRef.parent);
        }
        else {
            tRef.setTextRoot(((SVG.TextChild)tRef.parent).getTextRoot());
        }
    }
    
    private void tspan(final Attributes attributes) throws SAXException {
        this.debug("<tspan>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        if (!(this.currentElement instanceof SVG.TextContainer)) {
            throw new SAXException("Invalid document. <tspan> elements are only valid inside <text> or other <tspan> elements.");
        }
        final SVG.TSpan currentElement = new SVG.TSpan();
        currentElement.document = this.svgDocument;
        currentElement.parent = this.currentElement;
        this.parseAttributesCore(currentElement, attributes);
        this.parseAttributesStyle(currentElement, attributes);
        this.parseAttributesConditional(currentElement, attributes);
        this.parseAttributesTextPosition(currentElement, attributes);
        this.currentElement.addChild(currentElement);
        this.currentElement = currentElement;
        if (currentElement.parent instanceof SVG.TextRoot) {
            currentElement.setTextRoot((SVG.TextRoot)currentElement.parent);
        }
        else {
            currentElement.setTextRoot(((SVG.TextChild)currentElement.parent).getTextRoot());
        }
    }
    
    private void use(final Attributes attributes) throws SAXException {
        this.debug("<use>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.Use currentElement = new SVG.Use();
        currentElement.document = this.svgDocument;
        currentElement.parent = this.currentElement;
        this.parseAttributesCore(currentElement, attributes);
        this.parseAttributesStyle(currentElement, attributes);
        this.parseAttributesTransform(currentElement, attributes);
        this.parseAttributesConditional(currentElement, attributes);
        this.parseAttributesUse(currentElement, attributes);
        this.currentElement.addChild(currentElement);
        this.currentElement = currentElement;
    }
    
    private void view(final Attributes attributes) throws SAXException {
        this.debug("<view>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.View currentElement = new SVG.View();
        currentElement.document = this.svgDocument;
        currentElement.parent = this.currentElement;
        this.parseAttributesCore(currentElement, attributes);
        this.parseAttributesConditional(currentElement, attributes);
        this.parseAttributesViewBox(currentElement, attributes);
        this.currentElement.addChild(currentElement);
        this.currentElement = currentElement;
    }
    
    private void zwitch(final Attributes attributes) throws SAXException {
        this.debug("<switch>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        final SVG.Switch currentElement = new SVG.Switch();
        currentElement.document = this.svgDocument;
        currentElement.parent = this.currentElement;
        this.parseAttributesCore(currentElement, attributes);
        this.parseAttributesStyle(currentElement, attributes);
        this.parseAttributesTransform(currentElement, attributes);
        this.parseAttributesConditional(currentElement, attributes);
        this.currentElement.addChild(currentElement);
        this.currentElement = currentElement;
    }
    
    @Override
    public void characters(final char[] array, final int n, final int n2) throws SAXException {
        if (!this.ignoring) {
            if (this.inMetadataElement) {
                if (this.metadataElementContents == null) {
                    this.metadataElementContents = new StringBuilder(n2);
                }
                this.metadataElementContents.append(array, n, n2);
            }
            else if (this.inStyleElement) {
                if (this.styleElementContents == null) {
                    this.styleElementContents = new StringBuilder(n2);
                }
                this.styleElementContents.append(array, n, n2);
            }
            else if (this.currentElement instanceof SVG.TextContainer) {
                final SVG.SvgConditionalContainer svgConditionalContainer = (SVG.SvgConditionalContainer)this.currentElement;
                final int size = svgConditionalContainer.children.size();
                SVG.SvgObject svgObject;
                if (size == 0) {
                    svgObject = null;
                }
                else {
                    svgObject = svgConditionalContainer.children.get(size - 1);
                }
                if (svgObject instanceof SVG.TextSequence) {
                    final SVG.TextSequence textSequence = (SVG.TextSequence)svgObject;
                    textSequence.text = String.valueOf(textSequence.text) + new String(array, n, n2);
                }
                else {
                    ((SVG.SvgConditionalContainer)this.currentElement).addChild(new SVG.TextSequence(new String(array, n, n2)));
                }
            }
        }
    }
    
    @Override
    public void comment(final char[] str, final int offset, final int n) throws SAXException {
        if (!this.ignoring && this.inStyleElement) {
            if (this.styleElementContents == null) {
                this.styleElementContents = new StringBuilder(n);
            }
            this.styleElementContents.append(str, offset, n);
        }
    }
    
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }
    
    @Override
    public void endElement(final String anObject, final String localName, final String qName) throws SAXException {
        super.endElement(anObject, localName, qName);
        if (this.ignoring && --this.ignoreDepth == 0) {
            this.ignoring = false;
        }
        else if ("http://www.w3.org/2000/svg".equals(anObject) || "".equals(anObject)) {
            if (localName.equals("title") || localName.equals("desc")) {
                this.inMetadataElement = false;
                if (this.metadataTag.equals("title")) {
                    this.svgDocument.setTitle(this.metadataElementContents.toString());
                }
                else if (this.metadataTag.equals("desc")) {
                    this.svgDocument.setDesc(this.metadataElementContents.toString());
                }
                this.metadataElementContents.setLength(0);
            }
            else if (localName.equals("style") && this.styleElementContents != null) {
                this.inStyleElement = false;
                this.parseCSSStyleSheet(this.styleElementContents.toString());
                this.styleElementContents.setLength(0);
            }
            else if (localName.equals("svg") || localName.equals("defs") || localName.equals("g") || localName.equals("use") || localName.equals("image") || localName.equals("text") || localName.equals("tspan") || localName.equals("switch") || localName.equals("symbol") || localName.equals("marker") || localName.equals("linearGradient") || localName.equals("radialGradient") || localName.equals("stop") || localName.equals("clipPath") || localName.equals("textPath") || localName.equals("pattern") || localName.equals("view") || localName.equals("mask") || localName.equals("solidColor")) {
                this.currentElement = ((SVG.SvgObject)this.currentElement).parent;
            }
        }
    }
    
    protected SVG parse(final InputStream p0) throws SVGParseException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/io/InputStream.markSupported:()Z
        //     4: ifne            250
        //     7: new             Ljava/io/BufferedInputStream;
        //    10: dup            
        //    11: aload_1        
        //    12: invokespecial   java/io/BufferedInputStream.<init>:(Ljava/io/InputStream;)V
        //    15: astore_1       
        //    16: aload_1        
        //    17: iconst_3       
        //    18: invokevirtual   java/io/InputStream.mark:(I)V
        //    21: aload_1        
        //    22: invokevirtual   java/io/InputStream.read:()I
        //    25: istore_2       
        //    26: aload_1        
        //    27: invokevirtual   java/io/InputStream.read:()I
        //    30: istore_3       
        //    31: aload_1        
        //    32: invokevirtual   java/io/InputStream.reset:()V
        //    35: iload_2        
        //    36: iload_3        
        //    37: bipush          8
        //    39: ishl           
        //    40: iadd           
        //    41: ldc_w           35615
        //    44: if_icmpne       247
        //    47: new             Ljava/util/zip/GZIPInputStream;
        //    50: astore          4
        //    52: aload           4
        //    54: aload_1        
        //    55: invokespecial   java/util/zip/GZIPInputStream.<init>:(Ljava/io/InputStream;)V
        //    58: aload           4
        //    60: astore_1       
        //    61: invokestatic    javax/xml/parsers/SAXParserFactory.newInstance:()Ljavax/xml/parsers/SAXParserFactory;
        //    64: astore          4
        //    66: aload           4
        //    68: invokevirtual   javax/xml/parsers/SAXParserFactory.newSAXParser:()Ljavax/xml/parsers/SAXParser;
        //    71: invokevirtual   javax/xml/parsers/SAXParser.getXMLReader:()Lorg/xml/sax/XMLReader;
        //    74: astore          5
        //    76: aload           5
        //    78: aload_0        
        //    79: invokeinterface org/xml/sax/XMLReader.setContentHandler:(Lorg/xml/sax/ContentHandler;)V
        //    84: aload           5
        //    86: ldc_w           "http://xml.org/sax/properties/lexical-handler"
        //    89: aload_0        
        //    90: invokeinterface org/xml/sax/XMLReader.setProperty:(Ljava/lang/String;Ljava/lang/Object;)V
        //    95: new             Lorg/xml/sax/InputSource;
        //    98: astore          4
        //   100: aload           4
        //   102: aload_1        
        //   103: invokespecial   org/xml/sax/InputSource.<init>:(Ljava/io/InputStream;)V
        //   106: aload           5
        //   108: aload           4
        //   110: invokeinterface org/xml/sax/XMLReader.parse:(Lorg/xml/sax/InputSource;)V
        //   115: aload_1        
        //   116: invokevirtual   java/io/InputStream.close:()V
        //   119: aload_0        
        //   120: getfield        com/caverock/androidsvg/SVGParser.svgDocument:Lcom/caverock/androidsvg/SVG;
        //   123: areturn        
        //   124: astore          4
        //   126: goto            61
        //   129: astore          4
        //   131: new             Lcom/caverock/androidsvg/SVGParseException;
        //   134: astore          5
        //   136: aload           5
        //   138: ldc_w           "File error"
        //   141: aload           4
        //   143: invokespecial   com/caverock/androidsvg/SVGParseException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   146: aload           5
        //   148: athrow         
        //   149: astore          4
        //   151: aload_1        
        //   152: invokevirtual   java/io/InputStream.close:()V
        //   155: aload           4
        //   157: athrow         
        //   158: astore          5
        //   160: new             Lcom/caverock/androidsvg/SVGParseException;
        //   163: astore          4
        //   165: aload           4
        //   167: ldc_w           "XML Parser problem"
        //   170: aload           5
        //   172: invokespecial   com/caverock/androidsvg/SVGParseException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   175: aload           4
        //   177: athrow         
        //   178: astore          6
        //   180: new             Lcom/caverock/androidsvg/SVGParseException;
        //   183: astore          4
        //   185: new             Ljava/lang/StringBuilder;
        //   188: astore          5
        //   190: aload           5
        //   192: ldc_w           "SVG parse error: "
        //   195: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   198: aload           4
        //   200: aload           5
        //   202: aload           6
        //   204: invokevirtual   org/xml/sax/SAXException.getMessage:()Ljava/lang/String;
        //   207: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   210: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   213: aload           6
        //   215: invokespecial   com/caverock/androidsvg/SVGParseException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   218: aload           4
        //   220: athrow         
        //   221: astore_1       
        //   222: ldc             "SVGParser"
        //   224: ldc_w           "Exception thrown closing input stream"
        //   227: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   230: pop            
        //   231: goto            155
        //   234: astore_1       
        //   235: ldc             "SVGParser"
        //   237: ldc_w           "Exception thrown closing input stream"
        //   240: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   243: pop            
        //   244: goto            119
        //   247: goto            61
        //   250: goto            16
        //    Exceptions:
        //  throws com.caverock.androidsvg.SVGParseException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                            
        //  -----  -----  -----  -----  ------------------------------------------------
        //  16     35     124    129    Ljava/io/IOException;
        //  47     58     124    129    Ljava/io/IOException;
        //  66     115    129    149    Ljava/io/IOException;
        //  66     115    158    178    Ljavax/xml/parsers/ParserConfigurationException;
        //  66     115    178    221    Lorg/xml/sax/SAXException;
        //  66     115    149    158    Any
        //  115    119    234    247    Ljava/io/IOException;
        //  131    149    149    158    Any
        //  151    155    221    234    Ljava/io/IOException;
        //  160    178    149    158    Any
        //  180    221    149    158    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 116 out of bounds for length 116
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:458)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    protected void setSupportedFormats(final String[] elements) {
        Collections.addAll(this.supportedFormats = new HashSet<String>(elements.length), elements);
    }
    
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        this.svgDocument = new SVG();
    }
    
    @Override
    public void startElement(final String anObject, final String s, final String qName, final Attributes attributes) throws SAXException {
        super.startElement(anObject, s, qName, attributes);
        if (this.ignoring) {
            ++this.ignoreDepth;
        }
        else if ("http://www.w3.org/2000/svg".equals(anObject) || "".equals(anObject)) {
            if (s.equals("svg")) {
                this.svg(attributes);
            }
            else if (s.equals("g")) {
                this.g(attributes);
            }
            else if (s.equals("defs")) {
                this.defs(attributes);
            }
            else if (s.equals("use")) {
                this.use(attributes);
            }
            else if (s.equals("path")) {
                this.path(attributes);
            }
            else if (s.equals("rect")) {
                this.rect(attributes);
            }
            else if (s.equals("circle")) {
                this.circle(attributes);
            }
            else if (s.equals("ellipse")) {
                this.ellipse(attributes);
            }
            else if (s.equals("line")) {
                this.line(attributes);
            }
            else if (s.equals("polyline")) {
                this.polyline(attributes);
            }
            else if (s.equals("polygon")) {
                this.polygon(attributes);
            }
            else if (s.equals("text")) {
                this.text(attributes);
            }
            else if (s.equals("tspan")) {
                this.tspan(attributes);
            }
            else if (s.equals("tref")) {
                this.tref(attributes);
            }
            else if (s.equals("switch")) {
                this.zwitch(attributes);
            }
            else if (s.equals("symbol")) {
                this.symbol(attributes);
            }
            else if (s.equals("marker")) {
                this.marker(attributes);
            }
            else if (s.equals("linearGradient")) {
                this.linearGradient(attributes);
            }
            else if (s.equals("radialGradient")) {
                this.radialGradient(attributes);
            }
            else if (s.equals("stop")) {
                this.stop(attributes);
            }
            else if (s.equals("a")) {
                this.g(attributes);
            }
            else if (s.equals("title") || s.equals("desc")) {
                this.inMetadataElement = true;
                this.metadataTag = s;
            }
            else if (s.equals("clipPath")) {
                this.clipPath(attributes);
            }
            else if (s.equals("textPath")) {
                this.textPath(attributes);
            }
            else if (s.equals("pattern")) {
                this.pattern(attributes);
            }
            else if (s.equals("image")) {
                this.image(attributes);
            }
            else if (s.equals("view")) {
                this.view(attributes);
            }
            else if (s.equals("mask")) {
                this.mask(attributes);
            }
            else if (s.equals("style")) {
                this.style(attributes);
            }
            else if (s.equals("solidColor")) {
                this.solidColor(attributes);
            }
            else {
                this.ignoring = true;
                this.ignoreDepth = 1;
            }
        }
    }
    
    private enum SVGAttr
    {
        CLASS("CLASS", 0), 
        UNSUPPORTED("UNSUPPORTED", 91), 
        clip("clip", 1), 
        clipPathUnits("clipPathUnits", 3), 
        clip_path("clip_path", 2), 
        clip_rule("clip_rule", 4), 
        color("color", 5), 
        cx("cx", 6), 
        cy("cy", 7), 
        d("d", 13), 
        direction("direction", 8), 
        display("display", 14), 
        dx("dx", 9), 
        dy("dy", 10), 
        fill("fill", 15), 
        fill_opacity("fill_opacity", 17), 
        fill_rule("fill_rule", 16), 
        font("font", 18), 
        font_family("font_family", 19), 
        font_size("font_size", 20), 
        font_style("font_style", 22), 
        font_weight("font_weight", 21), 
        fx("fx", 11), 
        fy("fy", 12), 
        gradientTransform("gradientTransform", 23), 
        gradientUnits("gradientUnits", 24), 
        height("height", 25), 
        href("href", 26), 
        id("id", 27), 
        marker("marker", 28), 
        markerHeight("markerHeight", 32), 
        markerUnits("markerUnits", 33), 
        markerWidth("markerWidth", 34), 
        marker_end("marker_end", 31), 
        marker_mid("marker_mid", 30), 
        marker_start("marker_start", 29), 
        mask("mask", 35), 
        maskContentUnits("maskContentUnits", 36), 
        maskUnits("maskUnits", 37), 
        media("media", 38), 
        offset("offset", 39), 
        opacity("opacity", 40), 
        orient("orient", 41), 
        overflow("overflow", 42), 
        pathLength("pathLength", 43), 
        patternContentUnits("patternContentUnits", 44), 
        patternTransform("patternTransform", 45), 
        patternUnits("patternUnits", 46), 
        points("points", 47), 
        preserveAspectRatio("preserveAspectRatio", 48), 
        r("r", 49), 
        refX("refX", 50), 
        refY("refY", 51), 
        requiredExtensions("requiredExtensions", 53), 
        requiredFeatures("requiredFeatures", 52), 
        requiredFonts("requiredFonts", 55), 
        requiredFormats("requiredFormats", 54), 
        rx("rx", 56), 
        ry("ry", 57), 
        solid_color("solid_color", 58), 
        solid_opacity("solid_opacity", 59), 
        spreadMethod("spreadMethod", 60), 
        startOffset("startOffset", 61), 
        stop_color("stop_color", 62), 
        stop_opacity("stop_opacity", 63), 
        stroke("stroke", 64), 
        stroke_dasharray("stroke_dasharray", 65), 
        stroke_dashoffset("stroke_dashoffset", 66), 
        stroke_linecap("stroke_linecap", 67), 
        stroke_linejoin("stroke_linejoin", 68), 
        stroke_miterlimit("stroke_miterlimit", 69), 
        stroke_opacity("stroke_opacity", 70), 
        stroke_width("stroke_width", 71), 
        style("style", 72), 
        systemLanguage("systemLanguage", 73), 
        text_anchor("text_anchor", 74), 
        text_decoration("text_decoration", 75), 
        transform("transform", 76), 
        type("type", 77), 
        vector_effect("vector_effect", 78), 
        version("version", 79), 
        viewBox("viewBox", 80), 
        viewport_fill("viewport_fill", 88), 
        viewport_fill_opacity("viewport_fill_opacity", 89), 
        visibility("visibility", 90), 
        width("width", 81), 
        x("x", 82), 
        x1("x1", 84), 
        x2("x2", 86), 
        y("y", 83), 
        y1("y1", 85), 
        y2("y2", 87);
        
        private SVGAttr(final String name, final int ordinal) {
        }
        
        public static SVGAttr fromString(final String s) {
            SVGAttr svgAttr;
            if (s.equals("class")) {
                svgAttr = SVGAttr.CLASS;
            }
            else if (s.indexOf(95) != -1) {
                svgAttr = SVGAttr.UNSUPPORTED;
            }
            else {
                try {
                    svgAttr = valueOf(s.replace('-', '_'));
                }
                catch (IllegalArgumentException ex) {
                    svgAttr = SVGAttr.UNSUPPORTED;
                }
            }
            return svgAttr;
        }
    }
    
    protected static class TextScanner
    {
        protected String input;
        protected int position;
        
        public TextScanner(final String s) {
            this.position = 0;
            this.input = s.trim();
        }
        
        private int scanForFloat() {
            int position = 0;
            if (this.empty()) {
                position = this.position;
            }
            else {
                int position2 = this.position;
                final int position3 = this.position;
                final char char1 = this.input.charAt(this.position);
                int advanceChar;
                if (char1 == '-' || (advanceChar = char1) == 43) {
                    advanceChar = this.advanceChar();
                }
                int codePoint = advanceChar;
                if (Character.isDigit(advanceChar)) {
                    position2 = this.position + 1;
                    for (codePoint = this.advanceChar(); Character.isDigit(codePoint); codePoint = this.advanceChar()) {
                        position2 = this.position + 1;
                    }
                }
                int n;
                if ((n = codePoint) == 46) {
                    position2 = this.position + 1;
                    int codePoint2;
                    for (codePoint2 = this.advanceChar(); Character.isDigit(codePoint2); codePoint2 = this.advanceChar()) {
                        position2 = this.position + 1;
                    }
                    n = codePoint2;
                }
                Label_0189: {
                    if (n != 101) {
                        position = position2;
                        if (n != 69) {
                            break Label_0189;
                        }
                    }
                    final int advanceChar2 = this.advanceChar();
                    int advanceChar3;
                    if (advanceChar2 == 45 || (advanceChar3 = advanceChar2) == 43) {
                        advanceChar3 = this.advanceChar();
                    }
                    position = position2;
                    if (Character.isDigit(advanceChar3)) {
                        position = this.position + 1;
                        for (int codePoint3 = this.advanceChar(); Character.isDigit(codePoint3); codePoint3 = this.advanceChar()) {
                            position = this.position + 1;
                        }
                    }
                }
                this.position = position3;
            }
            return position;
        }
        
        private int scanForInteger() {
            int n;
            if (this.empty()) {
                n = this.position;
            }
            else {
                n = this.position;
                final int position = this.position;
                final char char1 = this.input.charAt(this.position);
                int advanceChar;
                if (char1 == '-' || (advanceChar = char1) == 43) {
                    advanceChar = this.advanceChar();
                }
                if (Character.isDigit(advanceChar)) {
                    n = this.position + 1;
                    for (int codePoint = this.advanceChar(); Character.isDigit(codePoint); codePoint = this.advanceChar()) {
                        n = this.position + 1;
                    }
                }
                this.position = position;
            }
            return n;
        }
        
        protected int advanceChar() {
            int char1 = -1;
            if (this.position != this.input.length()) {
                ++this.position;
                if (this.position < this.input.length()) {
                    char1 = this.input.charAt(this.position);
                }
            }
            return char1;
        }
        
        public String ahead() {
            final int position = this.position;
            while (!this.empty() && !this.isWhitespace(this.input.charAt(this.position))) {
                ++this.position;
            }
            final String substring = this.input.substring(position, this.position);
            this.position = position;
            return substring;
        }
        
        public Boolean checkedNextFlag(final Object o) {
            Boolean nextFlag;
            if (o == null) {
                nextFlag = null;
            }
            else {
                this.skipCommaWhitespace();
                nextFlag = this.nextFlag();
            }
            return nextFlag;
        }
        
        public Float checkedNextFloat(final Object o) {
            Float nextFloat;
            if (o == null) {
                nextFloat = null;
            }
            else {
                this.skipCommaWhitespace();
                nextFloat = this.nextFloat();
            }
            return nextFloat;
        }
        
        public boolean consume(final char c) {
            final boolean b = this.position < this.input.length() && this.input.charAt(this.position) == c;
            if (b) {
                ++this.position;
            }
            return b;
        }
        
        public boolean consume(final String anObject) {
            final int length = anObject.length();
            final boolean b = this.position <= this.input.length() - length && this.input.substring(this.position, this.position + length).equals(anObject);
            if (b) {
                this.position += length;
            }
            return b;
        }
        
        public boolean empty() {
            return this.position == this.input.length();
        }
        
        public boolean hasLetter() {
            final boolean b = false;
            boolean b2;
            if (this.position == this.input.length()) {
                b2 = b;
            }
            else {
                final char char1 = this.input.charAt(this.position);
                if (char1 < 'a' || char1 > 'z') {
                    b2 = b;
                    if (char1 < 'A') {
                        return b2;
                    }
                    b2 = b;
                    if (char1 > 'Z') {
                        return b2;
                    }
                }
                b2 = true;
            }
            return b2;
        }
        
        protected boolean isEOL(final int n) {
            return n == 10 || n == 13;
        }
        
        protected boolean isWhitespace(final int n) {
            return n == 32 || n == 10 || n == 13 || n == 9;
        }
        
        public Integer nextChar() {
            Integer value;
            if (this.position == this.input.length()) {
                value = null;
            }
            else {
                value = (int)this.input.charAt(this.position++);
            }
            return value;
        }
        
        public Boolean nextFlag() {
            Boolean value = null;
            if (this.position != this.input.length()) {
                final char char1 = this.input.charAt(this.position);
                if (char1 == '0' || char1 == '1') {
                    ++this.position;
                    value = (char1 == '1');
                }
            }
            return value;
        }
        
        public Float nextFloat() {
            final int scanForFloat = this.scanForFloat();
            Float value;
            if (scanForFloat == this.position) {
                value = null;
            }
            else {
                value = Float.parseFloat(this.input.substring(this.position, scanForFloat));
                this.position = scanForFloat;
            }
            return value;
        }
        
        public String nextFunction() {
            String substring = null;
            if (!this.empty()) {
                final int position = this.position;
                int n;
                for (n = this.input.charAt(this.position); (n >= 97 && n <= 122) || (n >= 65 && n <= 90); n = this.advanceChar()) {}
                final int position2 = this.position;
                while (this.isWhitespace(n)) {
                    n = this.advanceChar();
                }
                if (n == 40) {
                    ++this.position;
                    substring = this.input.substring(position, position2);
                }
                else {
                    this.position = position;
                }
            }
            return substring;
        }
        
        public Integer nextInteger() {
            final int scanForInteger = this.scanForInteger();
            Integer value;
            if (scanForInteger == this.position) {
                value = null;
            }
            else {
                value = Integer.parseInt(this.input.substring(this.position, scanForInteger));
                this.position = scanForInteger;
            }
            return value;
        }
        
        public SVG.Length nextLength() {
            final Float nextFloat = this.nextFloat();
            Cloneable cloneable;
            if (nextFloat == null) {
                cloneable = null;
            }
            else {
                final SVG.Unit nextUnit = this.nextUnit();
                if (nextUnit == null) {
                    cloneable = new SVG.Length(nextFloat, SVG.Unit.px);
                }
                else {
                    cloneable = new SVG.Length(nextFloat, nextUnit);
                }
            }
            return (SVG.Length)cloneable;
        }
        
        public String nextQuotedString() {
            final String s = null;
            String substring;
            if (this.empty()) {
                substring = s;
            }
            else {
                final int position = this.position;
                final char char1 = this.input.charAt(this.position);
                if (char1 != '\'') {
                    substring = s;
                    if (char1 != '\"') {
                        return substring;
                    }
                }
                int n;
                for (n = this.advanceChar(); n != -1 && n != char1; n = this.advanceChar()) {}
                if (n == -1) {
                    this.position = position;
                    substring = s;
                }
                else {
                    ++this.position;
                    substring = this.input.substring(position + 1, this.position - 1);
                }
            }
            return substring;
        }
        
        public String nextToken() {
            return this.nextToken(' ');
        }
        
        public String nextToken(final char c) {
            final String s = null;
            String substring;
            if (this.empty()) {
                substring = s;
            }
            else {
                final char char1 = this.input.charAt(this.position);
                substring = s;
                if (!this.isWhitespace(char1)) {
                    substring = s;
                    if (char1 != c) {
                        final int position = this.position;
                        for (int n = this.advanceChar(); n != -1 && n != c && !this.isWhitespace(n); n = this.advanceChar()) {}
                        substring = this.input.substring(position, this.position);
                    }
                }
            }
            return substring;
        }
        
        public SVG.Unit nextUnit() {
            Enum<SVG.Unit> enum1;
            if (this.empty()) {
                enum1 = null;
            }
            else if (this.input.charAt(this.position) == '%') {
                ++this.position;
                enum1 = SVG.Unit.percent;
            }
            else if (this.position > this.input.length() - 2) {
                enum1 = null;
            }
            else {
                try {
                    enum1 = SVG.Unit.valueOf(this.input.substring(this.position, this.position + 2).toLowerCase(Locale.US));
                    this.position += 2;
                }
                catch (IllegalArgumentException ex) {
                    enum1 = null;
                }
            }
            return (SVG.Unit)enum1;
        }
        
        public Float possibleNextFloat() {
            final int position = this.position;
            this.skipCommaWhitespace();
            Float nextFloat = this.nextFloat();
            if (nextFloat == null) {
                this.position = position;
                nextFloat = null;
            }
            return nextFloat;
        }
        
        public String restOfText() {
            String substring;
            if (this.empty()) {
                substring = null;
            }
            else {
                final int position = this.position;
                this.position = this.input.length();
                substring = this.input.substring(position);
            }
            return substring;
        }
        
        public boolean skipCommaWhitespace() {
            boolean b = false;
            this.skipWhitespace();
            if (this.position != this.input.length() && this.input.charAt(this.position) == ',') {
                ++this.position;
                this.skipWhitespace();
                b = true;
            }
            return b;
        }
        
        public void skipWhitespace() {
            while (this.position < this.input.length() && this.isWhitespace(this.input.charAt(this.position))) {
                ++this.position;
            }
        }
    }
}
