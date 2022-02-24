// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.text;

import java.util.Locale;

public final class BidiFormatter
{
    private static final int DEFAULT_FLAGS = 2;
    private static final BidiFormatter DEFAULT_LTR_INSTANCE;
    private static final BidiFormatter DEFAULT_RTL_INSTANCE;
    private static TextDirectionHeuristicCompat DEFAULT_TEXT_DIRECTION_HEURISTIC;
    private static final int DIR_LTR = -1;
    private static final int DIR_RTL = 1;
    private static final int DIR_UNKNOWN = 0;
    private static final String EMPTY_STRING = "";
    private static final int FLAG_STEREO_RESET = 2;
    private static final char LRE = '\u202a';
    private static final char LRM = '\u200e';
    private static final String LRM_STRING;
    private static final char PDF = '\u202c';
    private static final char RLE = '\u202b';
    private static final char RLM = '\u200f';
    private static final String RLM_STRING;
    private final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat;
    private final int mFlags;
    private final boolean mIsRtlContext;
    
    static {
        BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
        LRM_STRING = Character.toString('\u200e');
        RLM_STRING = Character.toString('\u200f');
        DEFAULT_LTR_INSTANCE = new BidiFormatter(false, 2, BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC);
        DEFAULT_RTL_INSTANCE = new BidiFormatter(true, 2, BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC);
    }
    
    private BidiFormatter(final boolean mIsRtlContext, final int mFlags, final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat) {
        this.mIsRtlContext = mIsRtlContext;
        this.mFlags = mFlags;
        this.mDefaultTextDirectionHeuristicCompat = mDefaultTextDirectionHeuristicCompat;
    }
    
    private static int getEntryDir(final String s) {
        return new DirectionalityEstimator(s, false).getEntryDir();
    }
    
    private static int getExitDir(final String s) {
        return new DirectionalityEstimator(s, false).getExitDir();
    }
    
    public static BidiFormatter getInstance() {
        return new Builder().build();
    }
    
    public static BidiFormatter getInstance(final Locale locale) {
        return new Builder(locale).build();
    }
    
    public static BidiFormatter getInstance(final boolean b) {
        return new Builder(b).build();
    }
    
    private static boolean isRtlLocale(final Locale locale) {
        boolean b = true;
        if (TextUtilsCompat.getLayoutDirectionFromLocale(locale) != 1) {
            b = false;
        }
        return b;
    }
    
    private String markAfter(String s, final TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        final boolean rtl = textDirectionHeuristicCompat.isRtl(s, 0, s.length());
        if (!this.mIsRtlContext && (rtl || getExitDir(s) == 1)) {
            s = BidiFormatter.LRM_STRING;
        }
        else if (this.mIsRtlContext && (!rtl || getExitDir(s) == -1)) {
            s = BidiFormatter.RLM_STRING;
        }
        else {
            s = "";
        }
        return s;
    }
    
    private String markBefore(String s, final TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        final boolean rtl = textDirectionHeuristicCompat.isRtl(s, 0, s.length());
        if (!this.mIsRtlContext && (rtl || getEntryDir(s) == 1)) {
            s = BidiFormatter.LRM_STRING;
        }
        else if (this.mIsRtlContext && (!rtl || getEntryDir(s) == -1)) {
            s = BidiFormatter.RLM_STRING;
        }
        else {
            s = "";
        }
        return s;
    }
    
    public boolean getStereoReset() {
        return (this.mFlags & 0x2) != 0x0;
    }
    
    public boolean isRtl(final String s) {
        return this.mDefaultTextDirectionHeuristicCompat.isRtl(s, 0, s.length());
    }
    
    public boolean isRtlContext() {
        return this.mIsRtlContext;
    }
    
    public String unicodeWrap(final String s) {
        return this.unicodeWrap(s, this.mDefaultTextDirectionHeuristicCompat, true);
    }
    
    public String unicodeWrap(final String s, final TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        return this.unicodeWrap(s, textDirectionHeuristicCompat, true);
    }
    
    public String unicodeWrap(String string, TextDirectionHeuristicCompat textDirectionHeuristicCompat, final boolean b) {
        if (string == null) {
            string = null;
        }
        else {
            final boolean rtl = textDirectionHeuristicCompat.isRtl(string, 0, string.length());
            final StringBuilder sb = new StringBuilder();
            if (this.getStereoReset() && b) {
                if (rtl) {
                    textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.RTL;
                }
                else {
                    textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.LTR;
                }
                sb.append(this.markBefore(string, textDirectionHeuristicCompat));
            }
            if (rtl != this.mIsRtlContext) {
                char c;
                if (rtl) {
                    c = '\u202b';
                }
                else {
                    c = '\u202a';
                }
                sb.append(c);
                sb.append(string);
                sb.append('\u202c');
            }
            else {
                sb.append(string);
            }
            if (b) {
                if (rtl) {
                    textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.RTL;
                }
                else {
                    textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.LTR;
                }
                sb.append(this.markAfter(string, textDirectionHeuristicCompat));
            }
            string = sb.toString();
        }
        return string;
    }
    
    public String unicodeWrap(final String s, final boolean b) {
        return this.unicodeWrap(s, this.mDefaultTextDirectionHeuristicCompat, b);
    }
    
    public static final class Builder
    {
        private int mFlags;
        private boolean mIsRtlContext;
        private TextDirectionHeuristicCompat mTextDirectionHeuristicCompat;
        
        public Builder() {
            this.initialize(isRtlLocale(Locale.getDefault()));
        }
        
        public Builder(final Locale locale) {
            this.initialize(isRtlLocale(locale));
        }
        
        public Builder(final boolean b) {
            this.initialize(b);
        }
        
        private static BidiFormatter getDefaultInstanceFromContext(final boolean b) {
            BidiFormatter bidiFormatter;
            if (b) {
                bidiFormatter = BidiFormatter.DEFAULT_RTL_INSTANCE;
            }
            else {
                bidiFormatter = BidiFormatter.DEFAULT_LTR_INSTANCE;
            }
            return bidiFormatter;
        }
        
        private void initialize(final boolean mIsRtlContext) {
            this.mIsRtlContext = mIsRtlContext;
            this.mTextDirectionHeuristicCompat = BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC;
            this.mFlags = 2;
        }
        
        public BidiFormatter build() {
            BidiFormatter defaultInstanceFromContext;
            if (this.mFlags == 2 && this.mTextDirectionHeuristicCompat == BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC) {
                defaultInstanceFromContext = getDefaultInstanceFromContext(this.mIsRtlContext);
            }
            else {
                defaultInstanceFromContext = new BidiFormatter(this.mIsRtlContext, this.mFlags, this.mTextDirectionHeuristicCompat, null);
            }
            return defaultInstanceFromContext;
        }
        
        public Builder setTextDirectionHeuristic(final TextDirectionHeuristicCompat mTextDirectionHeuristicCompat) {
            this.mTextDirectionHeuristicCompat = mTextDirectionHeuristicCompat;
            return this;
        }
        
        public Builder stereoReset(final boolean b) {
            if (b) {
                this.mFlags |= 0x2;
            }
            else {
                this.mFlags &= 0xFFFFFFFD;
            }
            return this;
        }
    }
    
    private static class DirectionalityEstimator
    {
        private static final byte[] DIR_TYPE_CACHE;
        private static final int DIR_TYPE_CACHE_SIZE = 1792;
        private int charIndex;
        private final boolean isHtml;
        private char lastChar;
        private final int length;
        private final String text;
        
        static {
            DIR_TYPE_CACHE = new byte[1792];
            for (int i = 0; i < 1792; ++i) {
                DirectionalityEstimator.DIR_TYPE_CACHE[i] = Character.getDirectionality(i);
            }
        }
        
        DirectionalityEstimator(final String text, final boolean isHtml) {
            this.text = text;
            this.isHtml = isHtml;
            this.length = text.length();
        }
        
        private static byte getCachedDirectionality(final char ch) {
            byte directionality;
            if (ch < '\u0700') {
                directionality = DirectionalityEstimator.DIR_TYPE_CACHE[ch];
            }
            else {
                directionality = Character.getDirectionality(ch);
            }
            return directionality;
        }
        
        private byte skipEntityBackward() {
            final int charIndex = this.charIndex;
            while (this.charIndex > 0) {
                final String text = this.text;
                final int n = this.charIndex - 1;
                this.charIndex = n;
                this.lastChar = text.charAt(n);
                if (this.lastChar == '&') {
                    return 12;
                }
                if (this.lastChar == ';') {
                    break;
                }
            }
            this.charIndex = charIndex;
            this.lastChar = 59;
            return 13;
        }
        
        private byte skipEntityForward() {
            while (this.charIndex < this.length) {
                final char char1 = this.text.charAt(this.charIndex++);
                this.lastChar = char1;
                if (char1 == ';') {
                    break;
                }
            }
            return 12;
        }
        
        private byte skipTagBackward() {
            final int charIndex = this.charIndex;
            while (this.charIndex > 0) {
                final String text = this.text;
                final int n = this.charIndex - 1;
                this.charIndex = n;
                this.lastChar = text.charAt(n);
                if (this.lastChar == '<') {
                    return 12;
                }
                if (this.lastChar == '>') {
                    break;
                }
                if (this.lastChar != '\"' && this.lastChar != '\'') {
                    continue;
                }
                final char lastChar = this.lastChar;
                while (this.charIndex > 0) {
                    final String text2 = this.text;
                    final int n2 = this.charIndex - 1;
                    this.charIndex = n2;
                    final char char1 = text2.charAt(n2);
                    this.lastChar = char1;
                    if (char1 != lastChar) {
                        continue;
                    }
                    break;
                }
            }
            this.charIndex = charIndex;
            this.lastChar = 62;
            return 13;
        }
        
        private byte skipTagForward() {
            final int charIndex = this.charIndex;
            while (this.charIndex < this.length) {
                this.lastChar = this.text.charAt(this.charIndex++);
                if (this.lastChar == '>') {
                    return 12;
                }
                if (this.lastChar != '\"' && this.lastChar != '\'') {
                    continue;
                }
                final char lastChar = this.lastChar;
                while (this.charIndex < this.length) {
                    final char char1 = this.text.charAt(this.charIndex++);
                    this.lastChar = char1;
                    if (char1 != lastChar) {
                        continue;
                    }
                    break;
                }
            }
            this.charIndex = charIndex;
            this.lastChar = 60;
            return 13;
        }
        
        byte dirTypeBackward() {
            this.lastChar = this.text.charAt(this.charIndex - 1);
            byte b;
            if (Character.isLowSurrogate(this.lastChar)) {
                final int codePointBefore = Character.codePointBefore(this.text, this.charIndex);
                this.charIndex -= Character.charCount(codePointBefore);
                b = Character.getDirectionality(codePointBefore);
            }
            else {
                --this.charIndex;
                b = getCachedDirectionality(this.lastChar);
                if (this.isHtml) {
                    if (this.lastChar == '>') {
                        b = this.skipTagBackward();
                    }
                    else {
                        b = b;
                        if (this.lastChar == ';') {
                            b = this.skipEntityBackward();
                        }
                    }
                }
            }
            return b;
        }
        
        byte dirTypeForward() {
            this.lastChar = this.text.charAt(this.charIndex);
            byte b;
            if (Character.isHighSurrogate(this.lastChar)) {
                final int codePoint = Character.codePointAt(this.text, this.charIndex);
                this.charIndex += Character.charCount(codePoint);
                b = Character.getDirectionality(codePoint);
            }
            else {
                ++this.charIndex;
                b = getCachedDirectionality(this.lastChar);
                if (this.isHtml) {
                    if (this.lastChar == '<') {
                        b = this.skipTagForward();
                    }
                    else {
                        b = b;
                        if (this.lastChar == '&') {
                            b = this.skipEntityForward();
                        }
                    }
                }
            }
            return b;
        }
        
        int getEntryDir() {
            this.charIndex = 0;
            int n = 0;
            int n2 = 0;
            int n3 = 0;
            while (this.charIndex < this.length && n3 == 0) {
                int n4 = 0;
                switch (this.dirTypeForward()) {
                    case 18: {
                        --n;
                        n2 = 0;
                        continue;
                    }
                    case 16:
                    case 17: {
                        ++n;
                        n2 = 1;
                        continue;
                    }
                    case 14:
                    case 15: {
                        ++n;
                        n2 = -1;
                    }
                    case 9: {
                        continue;
                    }
                    default: {
                        n3 = n;
                        continue;
                    }
                    case 0: {
                        if (n == 0) {
                            n4 = -1;
                            break;
                        }
                        n3 = n;
                        continue;
                    }
                    case 1:
                    case 2: {
                        if (n == 0) {
                            n4 = 1;
                            break;
                        }
                        n3 = n;
                        continue;
                    }
                }
                return n4;
            }
            if (n3 == 0) {
                return 0;
            }
            int n4;
            if ((n4 = n2) == 0) {
                while (this.charIndex > 0) {
                    switch (this.dirTypeBackward()) {
                        default: {
                            continue;
                        }
                        case 14:
                        case 15: {
                            if (n3 == n) {
                                n4 = -1;
                                return n4;
                            }
                            --n;
                            continue;
                        }
                        case 16:
                        case 17: {
                            if (n3 == n) {
                                n4 = 1;
                                return n4;
                            }
                            --n;
                            continue;
                        }
                        case 18: {
                            ++n;
                            continue;
                        }
                    }
                }
                n4 = 0;
                return n4;
            }
            return n4;
        }
        
        int getExitDir() {
            final int n = -1;
            this.charIndex = this.length;
            int n2 = 0;
            int n3 = 0;
            while (this.charIndex > 0) {
                int n4 = 0;
                switch (this.dirTypeBackward()) {
                    case 18: {
                        ++n2;
                    }
                    case 9: {
                        continue;
                    }
                    default: {
                        if (n3 == 0) {
                            n3 = n2;
                            continue;
                        }
                        continue;
                    }
                    case 0: {
                        if (n2 == 0) {
                            n4 = n;
                            break;
                        }
                        if (n3 == 0) {
                            n3 = n2;
                            continue;
                        }
                        continue;
                    }
                    case 14:
                    case 15: {
                        n4 = n;
                        if (n3 != n2) {
                            --n2;
                            continue;
                        }
                        break;
                    }
                    case 1:
                    case 2: {
                        if (n2 == 0) {
                            n4 = 1;
                            break;
                        }
                        if (n3 == 0) {
                            n3 = n2;
                            continue;
                        }
                        continue;
                    }
                    case 16:
                    case 17: {
                        if (n3 == n2) {
                            n4 = 1;
                            break;
                        }
                        --n2;
                        continue;
                    }
                }
                return n4;
            }
            return 0;
        }
    }
}
