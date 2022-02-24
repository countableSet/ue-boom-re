// 
// Decompiled by Procyon v0.5.36
// 

package uk.co.chrisjenx.calligraphy;

import android.util.Log;
import android.content.res.AssetManager;
import java.util.HashMap;
import android.graphics.Typeface;
import java.util.Map;

public final class TypefaceUtils
{
    private static final Map<String, Typeface> sCachedFonts;
    private static final Map<Typeface, CalligraphyTypefaceSpan> sCachedSpans;
    
    static {
        sCachedFonts = new HashMap<String, Typeface>();
        sCachedSpans = new HashMap<Typeface, CalligraphyTypefaceSpan>();
    }
    
    private TypefaceUtils() {
    }
    
    public static CalligraphyTypefaceSpan getSpan(final Typeface typeface) {
        CalligraphyTypefaceSpan calligraphyTypefaceSpan = null;
        if (typeface == null) {
            calligraphyTypefaceSpan = null;
        }
        else {
            synchronized (TypefaceUtils.sCachedSpans) {
                if (!TypefaceUtils.sCachedSpans.containsKey(typeface)) {
                    TypefaceUtils.sCachedSpans.put(typeface, new CalligraphyTypefaceSpan(typeface));
                    // monitorexit(TypefaceUtils.sCachedSpans)
                    return calligraphyTypefaceSpan;
                }
            }
            calligraphyTypefaceSpan = TypefaceUtils.sCachedSpans.get(calligraphyTypefaceSpan);
        }
        // monitorexit(map)
        return calligraphyTypefaceSpan;
    }
    
    public static boolean isLoaded(final Typeface typeface) {
        return typeface != null && TypefaceUtils.sCachedFonts.containsValue(typeface);
    }
    
    public static Typeface load(final AssetManager assetManager, final String str) {
        synchronized (TypefaceUtils.sCachedFonts) {
            while (true) {
                try {
                    if (!TypefaceUtils.sCachedFonts.containsKey(str)) {
                        final Typeface fromAsset = Typeface.createFromAsset(assetManager, str);
                        TypefaceUtils.sCachedFonts.put(str, fromAsset);
                        return fromAsset;
                    }
                }
                catch (Exception ex) {
                    Log.w("Calligraphy", "Can't create asset from " + str + ". Make sure you have passed in the correct path and file name.", (Throwable)ex);
                    TypefaceUtils.sCachedFonts.put(str, null);
                    // monitorexit(TypefaceUtils.sCachedFonts)
                    return null;
                }
                // monitorexit(TypefaceUtils.sCachedFonts)
                return TypefaceUtils.sCachedFonts.get(str);
            }
        }
    }
}
