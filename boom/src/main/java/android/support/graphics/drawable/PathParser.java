// 
// Decompiled by Procyon v0.5.36
// 

package android.support.graphics.drawable;

import android.util.Log;
import android.graphics.Path;
import java.util.ArrayList;

class PathParser
{
    private static final String LOGTAG = "PathParser";
    
    private static void addNode(final ArrayList<PathDataNode> list, final char c, final float[] array) {
        list.add(new PathDataNode(c, array));
    }
    
    public static boolean canMorph(final PathDataNode[] array, final PathDataNode[] array2) {
        boolean b2;
        final boolean b = b2 = false;
        if (array != null) {
            if (array2 == null) {
                b2 = b;
            }
            else {
                b2 = b;
                if (array.length == array2.length) {
                    for (int i = 0; i < array.length; ++i) {
                        b2 = b;
                        if (array[i].type != array2[i].type) {
                            return b2;
                        }
                        b2 = b;
                        if (array[i].params.length != array2[i].params.length) {
                            return b2;
                        }
                    }
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    private static float[] copyOfRange(final float[] array, final int n, int a) {
        if (n > a) {
            throw new IllegalArgumentException();
        }
        final int length = array.length;
        if (n < 0 || n > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        a -= n;
        final int min = Math.min(a, length - n);
        final float[] array2 = new float[a];
        System.arraycopy(array, n, array2, 0, min);
        return array2;
    }
    
    public static PathDataNode[] createNodesFromPathData(final String s) {
        PathDataNode[] array;
        if (s == null) {
            array = null;
        }
        else {
            int n = 0;
            int i = 1;
            final ArrayList<PathDataNode> list = new ArrayList<PathDataNode>();
            while (i < s.length()) {
                i = nextStart(s, i);
                final String trim = s.substring(n, i).trim();
                if (trim.length() > 0) {
                    addNode(list, trim.charAt(0), getFloats(trim));
                }
                n = i;
                ++i;
            }
            if (i - n == 1 && n < s.length()) {
                addNode(list, s.charAt(n), new float[0]);
            }
            array = list.toArray(new PathDataNode[list.size()]);
        }
        return array;
    }
    
    public static Path createPathFromPathData(String str) {
        final Path path = new Path();
        final PathDataNode[] nodesFromPathData = createNodesFromPathData((String)str);
        if (nodesFromPathData == null) {
            return null;
        }
        try {
            PathDataNode.nodesToPath(nodesFromPathData, path);
            str = path;
            return (Path)str;
        }
        catch (RuntimeException cause) {
            throw new RuntimeException("Error in parsing " + (String)str, cause);
        }
        str = null;
        return (Path)str;
    }
    
    public static PathDataNode[] deepCopyNodes(final PathDataNode[] array) {
        PathDataNode[] array2;
        if (array == null) {
            array2 = null;
        }
        else {
            final PathDataNode[] array3 = new PathDataNode[array.length];
            int n = 0;
            while (true) {
                array2 = array3;
                if (n >= array.length) {
                    break;
                }
                array3[n] = new PathDataNode(array[n]);
                ++n;
            }
        }
        return array2;
    }
    
    private static void extract(final String s, final int n, final ExtractFloatResult extractFloatResult) {
        int i = n;
        int n2 = 0;
        extractFloatResult.mEndWithNegOrDot = false;
        int n3 = 0;
        int n4 = 0;
        while (i < s.length()) {
            final boolean b = false;
            int n5 = 0;
            int n6 = 0;
            int n7 = 0;
            switch (s.charAt(i)) {
                default: {
                    n5 = n3;
                    n6 = (b ? 1 : 0);
                    n7 = n2;
                    break;
                }
                case ' ':
                case ',': {
                    n7 = 1;
                    n6 = (b ? 1 : 0);
                    n5 = n3;
                    break;
                }
                case '-': {
                    n7 = n2;
                    n6 = (b ? 1 : 0);
                    n5 = n3;
                    if (i == n) {
                        break;
                    }
                    n7 = n2;
                    n6 = (b ? 1 : 0);
                    n5 = n3;
                    if (n4 == 0) {
                        n7 = 1;
                        extractFloatResult.mEndWithNegOrDot = true;
                        n6 = (b ? 1 : 0);
                        n5 = n3;
                        break;
                    }
                    break;
                }
                case '.': {
                    if (n3 == 0) {
                        n5 = 1;
                        n7 = n2;
                        n6 = (b ? 1 : 0);
                        break;
                    }
                    n7 = 1;
                    extractFloatResult.mEndWithNegOrDot = true;
                    n6 = (b ? 1 : 0);
                    n5 = n3;
                    break;
                }
                case 'E':
                case 'e': {
                    n6 = 1;
                    n7 = n2;
                    n5 = n3;
                    break;
                }
            }
            if (n7 != 0) {
                break;
            }
            ++i;
            n2 = n7;
            n4 = n6;
            n3 = n5;
        }
        extractFloatResult.mEndPosition = i;
    }
    
    private static float[] getFloats(String copyOfRange) {
        boolean b = true;
        boolean b2;
        if (((String)copyOfRange).charAt(0) == 'z') {
            b2 = true;
        }
        else {
            b2 = false;
        }
        if (((String)copyOfRange).charAt(0) != 'Z') {
            b = false;
        }
        if (b2 | b) {
            copyOfRange = new float[0];
        }
        else {
            while (true) {
                while (true) {
                    Label_0189: {
                        try {
                            final float[] array = new float[((String)copyOfRange).length()];
                            int i = 1;
                            final ExtractFloatResult extractFloatResult = new ExtractFloatResult();
                            final int length = ((String)copyOfRange).length();
                            int n = 0;
                            while (i < length) {
                                extract((String)copyOfRange, i, extractFloatResult);
                                final int mEndPosition = extractFloatResult.mEndPosition;
                                if (i >= mEndPosition) {
                                    break Label_0189;
                                }
                                final int n2 = n + 1;
                                array[n] = Float.parseFloat(((String)copyOfRange).substring(i, mEndPosition));
                                n = n2;
                                if (extractFloatResult.mEndWithNegOrDot) {
                                    i = mEndPosition;
                                }
                                else {
                                    i = mEndPosition + 1;
                                }
                            }
                            copyOfRange = copyOfRange(array, 0, n);
                            break;
                        }
                        catch (NumberFormatException cause) {
                            throw new RuntimeException("error in parsing \"" + (String)copyOfRange + "\"", cause);
                        }
                    }
                    continue;
                }
            }
        }
        return (float[])copyOfRange;
    }
    
    private static int nextStart(final String s, int i) {
        while (i < s.length()) {
            final char char1 = s.charAt(i);
            if (((char1 - 'A') * (char1 - 'Z') <= 0 || (char1 - 'a') * (char1 - 'z') <= 0) && char1 != 'e' && char1 != 'E') {
                break;
            }
            ++i;
        }
        return i;
    }
    
    public static void updateNodes(final PathDataNode[] array, final PathDataNode[] array2) {
        for (int i = 0; i < array2.length; ++i) {
            array[i].type = array2[i].type;
            for (int j = 0; j < array2[i].params.length; ++j) {
                array[i].params[j] = array2[i].params[j];
            }
        }
    }
    
    private static class ExtractFloatResult
    {
        int mEndPosition;
        boolean mEndWithNegOrDot;
    }
    
    public static class PathDataNode
    {
        float[] params;
        char type;
        
        private PathDataNode(final char c, final float[] params) {
            this.type = c;
            this.params = params;
        }
        
        private PathDataNode(final PathDataNode pathDataNode) {
            this.type = pathDataNode.type;
            this.params = copyOfRange(pathDataNode.params, 0, pathDataNode.params.length);
        }
        
        private static void addCommand(final Path path, final float[] array, final char c, final char c2, final float[] array2) {
            int n = 2;
            float n2 = array[0];
            float n3 = array[1];
            float n4 = array[2];
            float n5 = array[3];
            final float n6 = array[4];
            final float n7 = array[5];
            switch (c2) {
                case 'Z':
                case 'z': {
                    path.close();
                    n2 = n6;
                    n3 = n7;
                    n4 = n6;
                    n5 = n7;
                    path.moveTo(n2, n3);
                    break;
                }
                case 'L':
                case 'M':
                case 'T':
                case 'l':
                case 'm':
                case 't': {
                    n = 2;
                    break;
                }
                case 'H':
                case 'V':
                case 'h':
                case 'v': {
                    n = 1;
                    break;
                }
                case 'C':
                case 'c': {
                    n = 6;
                    break;
                }
                case 'Q':
                case 'S':
                case 'q':
                case 's': {
                    n = 4;
                    break;
                }
                case 'A':
                case 'a': {
                    n = 7;
                    break;
                }
            }
            final int n8 = 0;
            char c3 = c;
            int i = n8;
            float n9 = n7;
            float n10 = n6;
            float n11 = n5;
            float n12 = n4;
            while (i < array2.length) {
                float n13 = 0.0f;
                float n14 = 0.0f;
                float n15 = 0.0f;
                switch (c2) {
                    default: {
                        n13 = n9;
                        n14 = n11;
                        n15 = n12;
                        break;
                    }
                    case 'm': {
                        n2 += array2[i + 0];
                        n3 += array2[i + 1];
                        if (i > 0) {
                            path.rLineTo(array2[i + 0], array2[i + 1]);
                            n15 = n12;
                            n14 = n11;
                            n13 = n9;
                            break;
                        }
                        path.rMoveTo(array2[i + 0], array2[i + 1]);
                        n10 = n2;
                        n13 = n3;
                        n15 = n12;
                        n14 = n11;
                        break;
                    }
                    case 'M': {
                        n2 = array2[i + 0];
                        n3 = array2[i + 1];
                        if (i > 0) {
                            path.lineTo(array2[i + 0], array2[i + 1]);
                            n15 = n12;
                            n14 = n11;
                            n13 = n9;
                            break;
                        }
                        path.moveTo(array2[i + 0], array2[i + 1]);
                        n10 = n2;
                        n13 = n3;
                        n15 = n12;
                        n14 = n11;
                        break;
                    }
                    case 'l': {
                        path.rLineTo(array2[i + 0], array2[i + 1]);
                        n2 += array2[i + 0];
                        n3 += array2[i + 1];
                        n15 = n12;
                        n14 = n11;
                        n13 = n9;
                        break;
                    }
                    case 'L': {
                        path.lineTo(array2[i + 0], array2[i + 1]);
                        n2 = array2[i + 0];
                        n3 = array2[i + 1];
                        n15 = n12;
                        n14 = n11;
                        n13 = n9;
                        break;
                    }
                    case 'h': {
                        path.rLineTo(array2[i + 0], 0.0f);
                        n2 += array2[i + 0];
                        n15 = n12;
                        n14 = n11;
                        n13 = n9;
                        break;
                    }
                    case 'H': {
                        path.lineTo(array2[i + 0], n3);
                        n2 = array2[i + 0];
                        n15 = n12;
                        n14 = n11;
                        n13 = n9;
                        break;
                    }
                    case 'v': {
                        path.rLineTo(0.0f, array2[i + 0]);
                        n3 += array2[i + 0];
                        n15 = n12;
                        n14 = n11;
                        n13 = n9;
                        break;
                    }
                    case 'V': {
                        path.lineTo(n2, array2[i + 0]);
                        n3 = array2[i + 0];
                        n15 = n12;
                        n14 = n11;
                        n13 = n9;
                        break;
                    }
                    case 'c': {
                        path.rCubicTo(array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3], array2[i + 4], array2[i + 5]);
                        n15 = n2 + array2[i + 2];
                        n14 = n3 + array2[i + 3];
                        n2 += array2[i + 4];
                        n3 += array2[i + 5];
                        n13 = n9;
                        break;
                    }
                    case 'C': {
                        path.cubicTo(array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3], array2[i + 4], array2[i + 5]);
                        n2 = array2[i + 4];
                        n3 = array2[i + 5];
                        n15 = array2[i + 2];
                        n14 = array2[i + 3];
                        n13 = n9;
                        break;
                    }
                    case 's': {
                        float n16 = 0.0f;
                        float n17 = 0.0f;
                        if (c3 == 'c' || c3 == 's' || c3 == 'C' || c3 == 'S') {
                            n16 = n2 - n12;
                            n17 = n3 - n11;
                        }
                        path.rCubicTo(n16, n17, array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3]);
                        n15 = n2 + array2[i + 0];
                        n14 = n3 + array2[i + 1];
                        n2 += array2[i + 2];
                        n3 += array2[i + 3];
                        n13 = n9;
                        break;
                    }
                    case 'S': {
                        float n18 = n2;
                        float n19 = n3;
                        if (c3 == 'c' || c3 == 's' || c3 == 'C' || c3 == 'S') {
                            n18 = 2.0f * n2 - n12;
                            n19 = 2.0f * n3 - n11;
                        }
                        path.cubicTo(n18, n19, array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3]);
                        n15 = array2[i + 0];
                        n14 = array2[i + 1];
                        n2 = array2[i + 2];
                        n3 = array2[i + 3];
                        n13 = n9;
                        break;
                    }
                    case 'q': {
                        path.rQuadTo(array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3]);
                        n15 = n2 + array2[i + 0];
                        n14 = n3 + array2[i + 1];
                        n2 += array2[i + 2];
                        n3 += array2[i + 3];
                        n13 = n9;
                        break;
                    }
                    case 'Q': {
                        path.quadTo(array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3]);
                        n15 = array2[i + 0];
                        n14 = array2[i + 1];
                        n2 = array2[i + 2];
                        n3 = array2[i + 3];
                        n13 = n9;
                        break;
                    }
                    case 't': {
                        float n20 = 0.0f;
                        float n21 = 0.0f;
                        if (c3 == 'q' || c3 == 't' || c3 == 'Q' || c3 == 'T') {
                            n20 = n2 - n12;
                            n21 = n3 - n11;
                        }
                        path.rQuadTo(n20, n21, array2[i + 0], array2[i + 1]);
                        final float n22 = n2 + n20;
                        n14 = n3 + n21;
                        n2 += array2[i + 0];
                        n3 += array2[i + 1];
                        n15 = n22;
                        n13 = n9;
                        break;
                    }
                    case 'T': {
                        float n23 = n2;
                        float n24 = n3;
                        if (c3 == 'q' || c3 == 't' || c3 == 'Q' || c3 == 'T') {
                            n23 = 2.0f * n2 - n12;
                            n24 = 2.0f * n3 - n11;
                        }
                        path.quadTo(n23, n24, array2[i + 0], array2[i + 1]);
                        final float n25 = n24;
                        final float n26 = array2[i + 0];
                        n3 = array2[i + 1];
                        n15 = n23;
                        n14 = n25;
                        n13 = n9;
                        n2 = n26;
                        break;
                    }
                    case 'a': {
                        drawArc(path, n2, n3, array2[i + 5] + n2, array2[i + 6] + n3, array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3] != 0.0f, array2[i + 4] != 0.0f);
                        n2 += array2[i + 5];
                        n3 += array2[i + 6];
                        n15 = n2;
                        n14 = n3;
                        n13 = n9;
                        break;
                    }
                    case 'A': {
                        drawArc(path, n2, n3, array2[i + 5], array2[i + 6], array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3] != 0.0f, array2[i + 4] != 0.0f);
                        n2 = array2[i + 5];
                        n3 = array2[i + 6];
                        n15 = n2;
                        n14 = n3;
                        n13 = n9;
                        break;
                    }
                }
                c3 = c2;
                i += n;
                n12 = n15;
                n11 = n14;
                n9 = n13;
            }
            array[0] = n2;
            array[1] = n3;
            array[2] = n12;
            array[3] = n11;
            array[4] = n10;
            array[5] = n9;
        }
        
        private static void arcToBezier(final Path path, final double n, final double n2, final double n3, final double n4, double n5, double n6, double cos, double n7, double n8) {
            final int n9 = (int)Math.ceil(Math.abs(4.0 * n8 / 3.141592653589793));
            final double cos2 = Math.cos(cos);
            final double sin = Math.sin(cos);
            cos = Math.cos(n7);
            final double sin2 = Math.sin(n7);
            final double n10 = -n3 * cos2 * sin2 - n4 * sin * cos;
            final double n11 = -n3 * sin * sin2 + n4 * cos2 * cos;
            final double n12 = n8 / n9;
            int i = 0;
            cos = n5;
            n8 = n7;
            n5 = n11;
            n7 = n10;
            while (i < n9) {
                final double n13 = n8 + n12;
                final double sin3 = Math.sin(n13);
                final double cos3 = Math.cos(n13);
                final double n14 = n3 * cos2 * cos3 + n - n4 * sin * sin3;
                final double n15 = n3 * sin * cos3 + n2 + n4 * cos2 * sin3;
                final double n16 = -n3 * cos2 * sin3 - n4 * sin * cos3;
                final double n17 = -n3 * sin * sin3 + n4 * cos2 * cos3;
                final double tan = Math.tan((n13 - n8) / 2.0);
                n8 = Math.sin(n13 - n8) * (Math.sqrt(4.0 + 3.0 * tan * tan) - 1.0) / 3.0;
                path.cubicTo((float)(cos + n8 * n7), (float)(n6 + n8 * n5), (float)(n14 - n8 * n16), (float)(n15 - n8 * n17), (float)n14, (float)n15);
                n8 = n13;
                cos = n14;
                n6 = n15;
                n7 = n16;
                n5 = n17;
                ++i;
            }
        }
        
        private static void drawArc(final Path path, final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final float n7, final boolean b, final boolean b2) {
            final double radians = Math.toRadians(n7);
            final double cos = Math.cos(radians);
            final double sin = Math.sin(radians);
            final double n8 = (n * cos + n2 * sin) / n5;
            final double n9 = (-n * sin + n2 * cos) / n6;
            final double n10 = (n3 * cos + n4 * sin) / n5;
            final double n11 = (-n3 * sin + n4 * cos) / n6;
            final double n12 = n8 - n10;
            final double n13 = n9 - n11;
            final double n14 = (n8 + n10) / 2.0;
            final double n15 = (n9 + n11) / 2.0;
            final double n16 = n12 * n12 + n13 * n13;
            if (n16 == 0.0) {
                Log.w("PathParser", " Points are coincident");
            }
            else {
                final double a = 1.0 / n16 - 0.25;
                if (a < 0.0) {
                    Log.w("PathParser", "Points are too far apart " + n16);
                    final float n17 = (float)(Math.sqrt(n16) / 1.99999);
                    drawArc(path, n, n2, n3, n4, n5 * n17, n6 * n17, n7, b, b2);
                }
                else {
                    final double sqrt = Math.sqrt(a);
                    final double n18 = sqrt * n12;
                    final double n19 = sqrt * n13;
                    double n20;
                    double n21;
                    if (b == b2) {
                        n20 = n14 - n19;
                        n21 = n15 + n18;
                    }
                    else {
                        n20 = n14 + n19;
                        n21 = n15 - n18;
                    }
                    final double atan2 = Math.atan2(n9 - n21, n8 - n20);
                    final double n22 = Math.atan2(n11 - n21, n10 - n20) - atan2;
                    final boolean b3 = n22 >= 0.0;
                    double n23 = n22;
                    if (b2 != b3) {
                        if (n22 > 0.0) {
                            n23 = n22 - 6.283185307179586;
                        }
                        else {
                            n23 = n22 + 6.283185307179586;
                        }
                    }
                    final double n24 = n20 * n5;
                    final double n25 = n21 * n6;
                    arcToBezier(path, n24 * cos - n25 * sin, n24 * sin + n25 * cos, n5, n6, n, n2, radians, atan2, n23);
                }
            }
        }
        
        public static void nodesToPath(final PathDataNode[] array, final Path path) {
            final float[] array2 = new float[6];
            final char c = 'm';
            int i = 0;
            char c2 = c;
            while (i < array.length) {
                addCommand(path, array2, c2, array[i].type, array[i].params);
                final char type = array[i].type;
                ++i;
                c2 = type;
            }
        }
        
        public void interpolatePathDataNode(final PathDataNode pathDataNode, final PathDataNode pathDataNode2, final float n) {
            for (int i = 0; i < pathDataNode.params.length; ++i) {
                this.params[i] = pathDataNode.params[i] * (1.0f - n) + pathDataNode2.params[i] * n;
            }
        }
    }
}
