// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.text.util;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Collections;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import android.webkit.WebView;
import java.util.regex.Matcher;
import java.util.Locale;
import java.util.Iterator;
import android.support.v4.util.PatternsCompat;
import java.util.ArrayList;
import android.text.util.Linkify;
import android.text.style.URLSpan;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.util.Linkify$TransformFilter;
import android.text.util.Linkify$MatchFilter;
import android.support.annotation.Nullable;
import java.util.regex.Pattern;
import android.text.method.MovementMethod;
import android.text.method.LinkMovementMethod;
import android.support.annotation.NonNull;
import android.widget.TextView;
import java.util.Comparator;

public final class LinkifyCompat
{
    private static final Comparator<LinkSpec> COMPARATOR;
    private static final String[] EMPTY_STRING;
    
    static {
        EMPTY_STRING = new String[0];
        COMPARATOR = new Comparator<LinkSpec>() {
            @Override
            public final int compare(final LinkSpec linkSpec, final LinkSpec linkSpec2) {
                int n = -1;
                if (linkSpec.start >= linkSpec2.start) {
                    if (linkSpec.start > linkSpec2.start) {
                        n = 1;
                    }
                    else if (linkSpec.end < linkSpec2.end) {
                        n = 1;
                    }
                    else if (linkSpec.end <= linkSpec2.end) {
                        n = 0;
                    }
                }
                return n;
            }
        };
    }
    
    private LinkifyCompat() {
    }
    
    private static void addLinkMovementMethod(@NonNull final TextView textView) {
        final MovementMethod movementMethod = textView.getMovementMethod();
        if ((movementMethod == null || !(movementMethod instanceof LinkMovementMethod)) && textView.getLinksClickable()) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
    
    public static final void addLinks(@NonNull final TextView textView, @NonNull final Pattern pattern, @Nullable final String s) {
        addLinks(textView, pattern, s, null, null, null);
    }
    
    public static final void addLinks(@NonNull final TextView textView, @NonNull final Pattern pattern, @Nullable final String s, @Nullable final Linkify$MatchFilter linkify$MatchFilter, @Nullable final Linkify$TransformFilter linkify$TransformFilter) {
        addLinks(textView, pattern, s, null, linkify$MatchFilter, linkify$TransformFilter);
    }
    
    public static final void addLinks(@NonNull final TextView textView, @NonNull final Pattern pattern, @Nullable final String s, @Nullable final String[] array, @Nullable final Linkify$MatchFilter linkify$MatchFilter, @Nullable final Linkify$TransformFilter linkify$TransformFilter) {
        final SpannableString value = SpannableString.valueOf(textView.getText());
        if (addLinks((Spannable)value, pattern, s, array, linkify$MatchFilter, linkify$TransformFilter)) {
            textView.setText((CharSequence)value);
            addLinkMovementMethod(textView);
        }
    }
    
    public static final boolean addLinks(@NonNull final Spannable spannable, final int n) {
        boolean b;
        if (n == 0) {
            b = false;
        }
        else {
            final URLSpan[] array = (URLSpan[])spannable.getSpans(0, spannable.length(), (Class)URLSpan.class);
            for (int i = array.length - 1; i >= 0; --i) {
                spannable.removeSpan((Object)array[i]);
            }
            if ((n & 0x4) != 0x0) {
                Linkify.addLinks(spannable, 4);
            }
            final ArrayList<LinkSpec> list = new ArrayList<LinkSpec>();
            if ((n & 0x1) != 0x0) {
                gatherLinks(list, spannable, PatternsCompat.AUTOLINK_WEB_URL, new String[] { "http://", "https://", "rtsp://" }, Linkify.sUrlMatchFilter, null);
            }
            if ((n & 0x2) != 0x0) {
                gatherLinks(list, spannable, PatternsCompat.AUTOLINK_EMAIL_ADDRESS, new String[] { "mailto:" }, null, null);
            }
            if ((n & 0x8) != 0x0) {
                gatherMapLinks(list, spannable);
            }
            pruneOverlaps(list, spannable);
            if (list.size() == 0) {
                b = false;
            }
            else {
                for (final LinkSpec linkSpec : list) {
                    if (linkSpec.frameworkAddedSpan == null) {
                        applyLink(linkSpec.url, linkSpec.start, linkSpec.end, spannable);
                    }
                }
                b = true;
            }
        }
        return b;
    }
    
    public static final boolean addLinks(@NonNull final Spannable spannable, @NonNull final Pattern pattern, @Nullable final String s) {
        return addLinks(spannable, pattern, s, null, null, null);
    }
    
    public static final boolean addLinks(@NonNull final Spannable spannable, @NonNull final Pattern pattern, @Nullable final String s, @Nullable final Linkify$MatchFilter linkify$MatchFilter, @Nullable final Linkify$TransformFilter linkify$TransformFilter) {
        return addLinks(spannable, pattern, s, null, linkify$MatchFilter, linkify$TransformFilter);
    }
    
    public static final boolean addLinks(@NonNull final Spannable input, @NonNull final Pattern pattern, @Nullable final String s, @Nullable final String[] array, @Nullable final Linkify$MatchFilter linkify$MatchFilter, @Nullable final Linkify$TransformFilter linkify$TransformFilter) {
        String s2 = s;
        if (s == null) {
            s2 = "";
        }
        String[] empty_STRING = null;
        Label_0027: {
            if (array != null) {
                empty_STRING = array;
                if (array.length >= 1) {
                    break Label_0027;
                }
            }
            empty_STRING = LinkifyCompat.EMPTY_STRING;
        }
        final String[] array2 = new String[empty_STRING.length + 1];
        array2[0] = s2.toLowerCase(Locale.ROOT);
        for (int i = 0; i < empty_STRING.length; ++i) {
            final String s3 = empty_STRING[i];
            String lowerCase;
            if (s3 == null) {
                lowerCase = "";
            }
            else {
                lowerCase = s3.toLowerCase(Locale.ROOT);
            }
            array2[i + 1] = lowerCase;
        }
        boolean b = false;
        final Matcher matcher = pattern.matcher((CharSequence)input);
        while (matcher.find()) {
            final int start = matcher.start();
            final int end = matcher.end();
            boolean acceptMatch = true;
            if (linkify$MatchFilter != null) {
                acceptMatch = linkify$MatchFilter.acceptMatch((CharSequence)input, start, end);
            }
            if (acceptMatch) {
                applyLink(makeUrl(matcher.group(0), array2, matcher, linkify$TransformFilter), start, end, input);
                b = true;
            }
        }
        return b;
    }
    
    public static final boolean addLinks(@NonNull final TextView textView, final int n) {
        boolean b = false;
        if (n != 0) {
            final CharSequence text = textView.getText();
            if (text instanceof Spannable) {
                if (addLinks((Spannable)text, n)) {
                    addLinkMovementMethod(textView);
                    b = true;
                }
            }
            else {
                final SpannableString value = SpannableString.valueOf(text);
                if (addLinks((Spannable)value, n)) {
                    addLinkMovementMethod(textView);
                    textView.setText((CharSequence)value);
                    b = true;
                }
            }
        }
        return b;
    }
    
    private static void applyLink(final String s, final int n, final int n2, final Spannable spannable) {
        spannable.setSpan((Object)new URLSpan(s), n, n2, 33);
    }
    
    private static void gatherLinks(final ArrayList<LinkSpec> list, final Spannable input, final Pattern pattern, final String[] array, final Linkify$MatchFilter linkify$MatchFilter, final Linkify$TransformFilter linkify$TransformFilter) {
        final Matcher matcher = pattern.matcher((CharSequence)input);
        while (matcher.find()) {
            final int start = matcher.start();
            final int end = matcher.end();
            if (linkify$MatchFilter == null || linkify$MatchFilter.acceptMatch((CharSequence)input, start, end)) {
                final LinkSpec e = new LinkSpec();
                e.url = makeUrl(matcher.group(0), array, matcher, linkify$TransformFilter);
                e.start = start;
                e.end = end;
                list.add(e);
            }
        }
    }
    
    private static final void gatherMapLinks(final ArrayList<LinkSpec> ex, Spannable spannable) {
        spannable = (Spannable)spannable.toString();
        int n = 0;
        while (true) {
            String address;
            LinkSpec linkSpec;
            try {
                while (true) {
                    address = WebView.findAddress((String)spannable);
                    if (address == null) {
                        break;
                    }
                    final int index = ((String)spannable).indexOf(address);
                    if (index < 0) {
                        break;
                    }
                    linkSpec = new LinkSpec();
                    final int beginIndex = index + address.length();
                    linkSpec.start = n + index;
                    linkSpec.end = n + beginIndex;
                    spannable = (Spannable)((String)spannable).substring(beginIndex);
                    n += beginIndex;
                    final String s = address;
                    final String s2 = "UTF-8";
                    final String s3 = URLEncoder.encode(s, s2);
                    final StringBuilder sb = new(java.lang.StringBuilder.class);
                    final StringBuilder sb3;
                    final StringBuilder sb2 = sb3 = sb;
                    new StringBuilder();
                    final LinkSpec linkSpec2 = linkSpec;
                    final StringBuilder sb4 = sb2;
                    final String s4 = "geo:0,0?q=";
                    final StringBuilder sb5 = sb4.append(s4);
                    final String s5 = s3;
                    final StringBuilder sb6 = sb5.append(s5);
                    final String s6 = sb6.toString();
                    linkSpec2.url = s6;
                    final ArrayList<LinkSpec> list = (ArrayList<LinkSpec>)ex;
                    final LinkSpec linkSpec3 = linkSpec;
                    list.add(linkSpec3);
                }
                return;
            }
            catch (UnsupportedOperationException ex) {
                return;
            }
            try {
                final String s = address;
                final String s2 = "UTF-8";
                final String s3 = URLEncoder.encode(s, s2);
                final StringBuilder sb = new(java.lang.StringBuilder.class);
                final StringBuilder sb3;
                final StringBuilder sb2 = sb3 = sb;
                new StringBuilder();
                final LinkSpec linkSpec2 = linkSpec;
                final StringBuilder sb4 = sb2;
                final String s4 = "geo:0,0?q=";
                final StringBuilder sb5 = sb4.append(s4);
                final String s5 = s3;
                final StringBuilder sb6 = sb5.append(s5);
                final String s6 = sb6.toString();
                linkSpec2.url = s6;
                final ArrayList<LinkSpec> list = (ArrayList<LinkSpec>)ex;
                final LinkSpec linkSpec3 = linkSpec;
                list.add(linkSpec3);
                continue;
            }
            catch (UnsupportedEncodingException ex2) {
                continue;
            }
            break;
        }
    }
    
    private static String makeUrl(@NonNull String string, @NonNull final String[] array, final Matcher matcher, @Nullable final Linkify$TransformFilter linkify$TransformFilter) {
        String transformUrl = string;
        if (linkify$TransformFilter != null) {
            transformUrl = linkify$TransformFilter.transformUrl(matcher, string);
        }
        final int n = 0;
        int n2 = 0;
        int n3;
        while (true) {
            n3 = n;
            string = transformUrl;
            if (n2 >= array.length) {
                break;
            }
            if (transformUrl.regionMatches(true, 0, array[n2], 0, array[n2].length())) {
                n3 = 1;
                string = transformUrl;
                if (!transformUrl.regionMatches(false, 0, array[n2], 0, array[n2].length())) {
                    string = array[n2] + transformUrl.substring(array[n2].length());
                    n3 = n3;
                    break;
                }
                break;
            }
            else {
                ++n2;
            }
        }
        String string2 = string;
        if (n3 == 0) {
            string2 = string;
            if (array.length > 0) {
                string2 = array[0] + string;
            }
        }
        return string2;
    }
    
    private static final void pruneOverlaps(final ArrayList<LinkSpec> list, final Spannable spannable) {
        final URLSpan[] array = (URLSpan[])spannable.getSpans(0, spannable.length(), (Class)URLSpan.class);
        for (int i = 0; i < array.length; ++i) {
            final LinkSpec e = new LinkSpec();
            e.frameworkAddedSpan = array[i];
            e.start = spannable.getSpanStart((Object)array[i]);
            e.end = spannable.getSpanEnd((Object)array[i]);
            list.add(e);
        }
        Collections.sort((List<Object>)list, (Comparator<? super Object>)LinkifyCompat.COMPARATOR);
        int size = list.size();
        int j = 0;
        while (j < size - 1) {
            final LinkSpec linkSpec = list.get(j);
            final LinkSpec linkSpec2 = list.get(j + 1);
            int n = -1;
            if (linkSpec.start <= linkSpec2.start && linkSpec.end > linkSpec2.start) {
                if (linkSpec2.end <= linkSpec.end) {
                    n = j + 1;
                }
                else if (linkSpec.end - linkSpec.start > linkSpec2.end - linkSpec2.start) {
                    n = j + 1;
                }
                else if (linkSpec.end - linkSpec.start < linkSpec2.end - linkSpec2.start) {
                    n = j;
                }
                if (n != -1) {
                    final URLSpan frameworkAddedSpan = list.get(n).frameworkAddedSpan;
                    if (frameworkAddedSpan != null) {
                        spannable.removeSpan((Object)frameworkAddedSpan);
                    }
                    list.remove(n);
                    --size;
                    continue;
                }
            }
            ++j;
        }
    }
    
    private static class LinkSpec
    {
        int end;
        URLSpan frameworkAddedSpan;
        int start;
        String url;
        
        LinkSpec() {
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface LinkifyMask {
    }
}
