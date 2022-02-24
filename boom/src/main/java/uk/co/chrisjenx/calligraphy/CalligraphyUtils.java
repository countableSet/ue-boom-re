// 
// Decompiled by Procyon v0.5.36
// 

package uk.co.chrisjenx.calligraphy;

import android.content.res.TypedArray;
import android.content.res.Resources$NotFoundException;
import android.content.res.Resources$Theme;
import android.util.TypedValue;
import android.util.AttributeSet;
import android.text.SpannableString;
import android.text.Spannable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView$BufferType;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.widget.TextView;
import android.content.Context;

public final class CalligraphyUtils
{
    private static Boolean sToolbarCheck;
    
    static {
        CalligraphyUtils.sToolbarCheck = null;
    }
    
    private CalligraphyUtils() {
    }
    
    static void applyFontToTextView(final Context context, final TextView textView, final CalligraphyConfig calligraphyConfig) {
        applyFontToTextView(context, textView, calligraphyConfig, false);
    }
    
    public static void applyFontToTextView(final Context context, final TextView textView, final CalligraphyConfig calligraphyConfig, final String s) {
        applyFontToTextView(context, textView, calligraphyConfig, s, false);
    }
    
    static void applyFontToTextView(final Context context, final TextView textView, final CalligraphyConfig calligraphyConfig, final String s, final boolean b) {
        if (context != null && textView != null && calligraphyConfig != null && (TextUtils.isEmpty((CharSequence)s) || !applyFontToTextView(context, textView, s, b))) {
            applyFontToTextView(context, textView, calligraphyConfig, b);
        }
    }
    
    static void applyFontToTextView(final Context context, final TextView textView, final CalligraphyConfig calligraphyConfig, final boolean b) {
        if (context != null && textView != null && calligraphyConfig != null && calligraphyConfig.isFontSet()) {
            applyFontToTextView(context, textView, calligraphyConfig.getFontPath(), b);
        }
    }
    
    public static boolean applyFontToTextView(final Context context, final TextView textView, final String s) {
        return applyFontToTextView(context, textView, s, false);
    }
    
    static boolean applyFontToTextView(final Context context, final TextView textView, final String s, final boolean b) {
        return textView != null && context != null && applyFontToTextView(textView, TypefaceUtils.load(context.getAssets(), s), b);
    }
    
    public static boolean applyFontToTextView(final TextView textView, final Typeface typeface) {
        return applyFontToTextView(textView, typeface, false);
    }
    
    public static boolean applyFontToTextView(final TextView textView, final Typeface typeface, final boolean b) {
        boolean b2;
        if (textView == null || typeface == null) {
            b2 = false;
        }
        else {
            textView.setPaintFlags(textView.getPaintFlags() | 0x80 | 0x1);
            textView.setTypeface(typeface);
            if (b) {
                textView.setText(applyTypefaceSpan(textView.getText(), typeface), TextView$BufferType.SPANNABLE);
                textView.addTextChangedListener((TextWatcher)new TextWatcher() {
                    public void afterTextChanged(final Editable editable) {
                        CalligraphyUtils.applyTypefaceSpan((CharSequence)editable, typeface);
                    }
                    
                    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                    
                    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                });
            }
            b2 = true;
        }
        return b2;
    }
    
    public static CharSequence applyTypefaceSpan(final CharSequence charSequence, final Typeface typeface) {
        Object o = charSequence;
        if (charSequence != null) {
            o = charSequence;
            if (charSequence.length() > 0) {
                o = charSequence;
                if (!(charSequence instanceof Spannable)) {
                    o = new SpannableString(charSequence);
                }
                ((Spannable)o).setSpan((Object)TypefaceUtils.getSpan(typeface), 0, ((CharSequence)o).length(), 33);
            }
        }
        return (CharSequence)o;
    }
    
    static boolean canCheckForV7Toolbar() {
        Label_0018: {
            if (CalligraphyUtils.sToolbarCheck != null) {
                break Label_0018;
            }
            try {
                Class.forName("android.support.v7.widget.Toolbar");
                CalligraphyUtils.sToolbarCheck = Boolean.TRUE;
                return CalligraphyUtils.sToolbarCheck;
            }
            catch (ClassNotFoundException ex) {
                CalligraphyUtils.sToolbarCheck = Boolean.FALSE;
                return CalligraphyUtils.sToolbarCheck;
            }
        }
    }
    
    static String pullFontPathFromStyle(final Context context, AttributeSet obtainStyledAttributes, final int n) {
        String s = null;
        if (n == -1 || obtainStyledAttributes == null) {
            s = null;
        }
        else {
            obtainStyledAttributes = (AttributeSet)context.obtainStyledAttributes(obtainStyledAttributes, new int[] { n });
            Label_0056: {
                if (obtainStyledAttributes == null) {
                    break Label_0056;
                }
                try {
                    if (TextUtils.isEmpty((CharSequence)((TypedArray)obtainStyledAttributes).getString(0))) {
                        ((TypedArray)obtainStyledAttributes).recycle();
                    }
                }
                catch (Exception ex) {
                    ((TypedArray)obtainStyledAttributes).recycle();
                }
                finally {
                    ((TypedArray)obtainStyledAttributes).recycle();
                }
            }
        }
        return s;
    }
    
    static String pullFontPathFromTextAppearance(Context obtainStyledAttributes, AttributeSet obtainStyledAttributes2, final int n) {
        String s2;
        final String s = s2 = null;
        if (n != -1) {
            if (obtainStyledAttributes2 == null) {
                s2 = s;
            }
            else {
                int resourceId = -1;
                obtainStyledAttributes2 = (AttributeSet)((Context)obtainStyledAttributes).obtainStyledAttributes(obtainStyledAttributes2, new int[] { 16842804 });
                Label_0053: {
                    if (obtainStyledAttributes2 == null) {
                        break Label_0053;
                    }
                    try {
                        resourceId = ((TypedArray)obtainStyledAttributes2).getResourceId(0, -1);
                        ((TypedArray)obtainStyledAttributes2).recycle();
                        obtainStyledAttributes = (Exception)((Context)obtainStyledAttributes).obtainStyledAttributes(resourceId, new int[] { n });
                        s2 = s;
                        if (obtainStyledAttributes != null) {
                            final Exception ex = obtainStyledAttributes;
                            final int n2 = 0;
                            s2 = ((TypedArray)ex).getString(n2);
                            return s2;
                        }
                        return s2;
                    }
                    catch (Exception obtainStyledAttributes) {
                        ((TypedArray)obtainStyledAttributes2).recycle();
                        s2 = s;
                        return s2;
                    }
                    finally {
                        final Exception ex2;
                        obtainStyledAttributes = ex2;
                        ((TypedArray)obtainStyledAttributes2).recycle();
                    }
                }
                try {
                    final Exception ex = obtainStyledAttributes;
                    final int n2 = 0;
                    s2 = ((TypedArray)ex).getString(n2);
                }
                catch (Exception ex3) {
                    ((TypedArray)obtainStyledAttributes).recycle();
                    s2 = s;
                }
                finally {
                    ((TypedArray)obtainStyledAttributes).recycle();
                }
            }
        }
        return s2;
    }
    
    static String pullFontPathFromTheme(Context obtainStyledAttributes, final int n, final int n2) {
        String s = null;
        if (n != -1) {
            if (n2 == -1) {
                s = s;
            }
            else {
                final Resources$Theme theme = obtainStyledAttributes.getTheme();
                final TypedValue typedValue = new TypedValue();
                theme.resolveAttribute(n, typedValue, true);
                obtainStyledAttributes = (Context)theme.obtainStyledAttributes(typedValue.resourceId, new int[] { n2 });
                try {
                    ((TypedArray)obtainStyledAttributes).getString(0);
                }
                catch (Exception ex) {
                    ((TypedArray)obtainStyledAttributes).recycle();
                }
                finally {
                    ((TypedArray)obtainStyledAttributes).recycle();
                }
            }
        }
        return s;
    }
    
    static String pullFontPathFromTheme(Context obtainStyledAttributes, int resourceId, final int n, final int n2) {
        String s2;
        final String s = s2 = null;
        if (resourceId != -1) {
            if (n2 == -1) {
                s2 = s;
            }
            else {
                final Resources$Theme theme = ((Context)obtainStyledAttributes).getTheme();
                final TypedValue typedValue = new TypedValue();
                theme.resolveAttribute(resourceId, typedValue, true);
                Object obtainStyledAttributes2 = theme.obtainStyledAttributes(typedValue.resourceId, new int[] { n });
                try {
                    resourceId = ((TypedArray)obtainStyledAttributes2).getResourceId(0, -1);
                    ((TypedArray)obtainStyledAttributes2).recycle();
                    obtainStyledAttributes2 = s;
                    if (resourceId == -1) {
                        return s2;
                    }
                    obtainStyledAttributes = (Exception)((Context)obtainStyledAttributes).obtainStyledAttributes(resourceId, new int[] { n2 });
                    obtainStyledAttributes2 = s;
                    if (obtainStyledAttributes != null) {
                        final Exception ex = obtainStyledAttributes;
                        final int n3 = 0;
                        ((TypedArray)ex).getString(n3);
                        return s2;
                    }
                    return s2;
                }
                catch (Exception obtainStyledAttributes) {
                    ((TypedArray)obtainStyledAttributes2).recycle();
                    return s2;
                }
                finally {
                    final Exception ex2;
                    obtainStyledAttributes = ex2;
                    ((TypedArray)obtainStyledAttributes2).recycle();
                }
                try {
                    final Exception ex = obtainStyledAttributes;
                    final int n3 = 0;
                    ((TypedArray)ex).getString(n3);
                }
                catch (Exception ex3) {
                    ((TypedArray)obtainStyledAttributes).recycle();
                }
                finally {
                    ((TypedArray)obtainStyledAttributes).recycle();
                }
            }
        }
        return s2;
    }
    
    static String pullFontPathFromView(final Context context, final AttributeSet set, int attributeResourceValue) {
        String s2;
        final String s = s2 = null;
        if (attributeResourceValue != -1) {
            if (set == null) {
                s2 = s;
            }
            else {
                String resourceEntryName = null;
                Label_0062: {
                    try {
                        resourceEntryName = context.getResources().getResourceEntryName(attributeResourceValue);
                        attributeResourceValue = set.getAttributeResourceValue((String)null, resourceEntryName, -1);
                        if (attributeResourceValue <= 0) {
                            break Label_0062;
                        }
                        s2 = context.getString(attributeResourceValue);
                    }
                    catch (Resources$NotFoundException ex) {
                        s2 = s;
                    }
                    return s2;
                }
                s2 = set.getAttributeValue((String)null, resourceEntryName);
            }
        }
        return s2;
    }
}
