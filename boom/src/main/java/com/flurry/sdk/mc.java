// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.webkit.MimeTypeMap;
import android.net.Uri;
import java.net.URISyntaxException;
import java.net.URI;
import java.util.Locale;
import android.text.TextUtils;
import java.util.regex.Pattern;

public final class mc
{
    private static final Pattern a;
    
    static {
        a = Pattern.compile("/");
    }
    
    public static String a(final String str) {
        String s;
        if (TextUtils.isEmpty((CharSequence)str)) {
            s = str;
        }
        else {
            final URI i = i(str);
            s = str;
            if (i != null) {
                final String scheme = i.getScheme();
                if (TextUtils.isEmpty((CharSequence)scheme)) {
                    s = "http" + str;
                }
                else {
                    final String lowerCase = scheme.toLowerCase(Locale.getDefault());
                    s = str;
                    if (!scheme.equals(lowerCase)) {
                        final int index = str.indexOf(scheme);
                        s = str;
                        if (index >= 0) {
                            s = lowerCase + str.substring(scheme.length() + index);
                        }
                    }
                }
            }
        }
        return s;
    }
    
    public static String a(final String s, String input) {
        String string = s;
        if (!TextUtils.isEmpty((CharSequence)s)) {
            if (TextUtils.isEmpty((CharSequence)input)) {
                string = s;
            }
            else {
                final URI i = i(s);
                string = s;
                if (i != null) {
                    final URI normalize = i.normalize();
                    final URI j = i(input);
                    string = s;
                    if (j != null) {
                        final URI normalize2 = j.normalize();
                        string = s;
                        if (!normalize.isOpaque()) {
                            string = s;
                            if (!normalize2.isOpaque()) {
                                input = normalize.getScheme();
                                final String scheme = normalize2.getScheme();
                                if (scheme == null) {
                                    string = s;
                                    if (input != null) {
                                        return string;
                                    }
                                }
                                if (scheme != null) {
                                    string = s;
                                    if (!scheme.equals(input)) {
                                        return string;
                                    }
                                }
                                input = normalize.getAuthority();
                                final String authority = normalize2.getAuthority();
                                if (authority == null) {
                                    string = s;
                                    if (input != null) {
                                        return string;
                                    }
                                }
                                if (authority != null) {
                                    string = s;
                                    if (!authority.equals(input)) {
                                        return string;
                                    }
                                }
                                final String path = normalize.getPath();
                                input = normalize2.getPath();
                                String[] split;
                                String[] split2;
                                int length;
                                int length2;
                                int n;
                                for (split = mc.a.split(path, -1), split2 = mc.a.split(input, -1), length = split.length, length2 = split2.length, n = 0; n < length2 && n < length && split[n].equals(split2[n]); ++n) {}
                                input = normalize.getQuery();
                                final String fragment;
                                String s2 = fragment = normalize.getFragment();
                                final StringBuilder sb = new StringBuilder();
                                String s3;
                                if (n == length2 && n == length) {
                                    final String query = normalize2.getQuery();
                                    final String fragment2 = normalize2.getFragment();
                                    final boolean equals = TextUtils.equals((CharSequence)input, (CharSequence)query);
                                    final boolean equals2 = TextUtils.equals((CharSequence)fragment, (CharSequence)fragment2);
                                    if (equals && equals2) {
                                        s2 = null;
                                        s3 = null;
                                    }
                                    else {
                                        String s4;
                                        if (equals && !TextUtils.isEmpty((CharSequence)fragment)) {
                                            s4 = null;
                                        }
                                        else {
                                            s4 = input;
                                        }
                                        if (TextUtils.isEmpty((CharSequence)s4) && TextUtils.isEmpty((CharSequence)fragment)) {
                                            sb.append(split[length - 1]);
                                        }
                                        else {
                                            input = s4;
                                        }
                                        s3 = input;
                                    }
                                }
                                else {
                                    int n2 = n;
                                    int k;
                                    while (true) {
                                        k = n;
                                        if (n2 >= length2 - 1) {
                                            break;
                                        }
                                        sb.append("..");
                                        sb.append("/");
                                        ++n2;
                                    }
                                    while (k < length - 1) {
                                        sb.append(split[k]);
                                        sb.append("/");
                                        ++k;
                                    }
                                    if (k < length) {
                                        sb.append(split[k]);
                                    }
                                    s3 = input;
                                    s2 = fragment;
                                    if (sb.length() == 0) {
                                        sb.append(".");
                                        sb.append("/");
                                        s3 = input;
                                        s2 = fragment;
                                    }
                                }
                                final URI a = a(null, null, sb.toString(), s3, s2);
                                string = s;
                                if (a != null) {
                                    string = a.toString();
                                }
                            }
                        }
                    }
                }
            }
        }
        return string;
    }
    
    private static URI a(final String scheme, final String authority, final String path, final String query, final String fragment) {
        try {
            return new URI(scheme, authority, path, query, fragment);
        }
        catch (URISyntaxException ex) {
            return null;
        }
    }
    
    public static String b(final String s) {
        String string;
        if (TextUtils.isEmpty((CharSequence)s)) {
            string = s;
        }
        else {
            final URI i = i(s);
            string = s;
            if (i != null) {
                final URI normalize = i.normalize();
                string = s;
                if (!normalize.isOpaque()) {
                    final URI a = a(normalize.getScheme(), normalize.getAuthority(), "/", null, null);
                    string = s;
                    if (a != null) {
                        string = a.toString();
                    }
                }
            }
        }
        return string;
    }
    
    public static String b(final String s, final String str) {
        String string = s;
        if (TextUtils.isEmpty((CharSequence)s)) {
            return string;
        }
        try {
            final URI uri = new URI(s);
            string = s;
            if (!uri.isAbsolute()) {
                string = s;
                if (!TextUtils.isEmpty((CharSequence)str)) {
                    final URI uri2 = new URI(str);
                    string = uri2.getScheme() + "://" + uri2.getHost() + s;
                }
            }
            return string;
        }
        catch (Exception ex) {
            string = s;
            return string;
        }
    }
    
    public static String c(final String s) {
        String string;
        if (TextUtils.isEmpty((CharSequence)s)) {
            string = s;
        }
        else {
            final URI i = i(s);
            string = s;
            if (i != null) {
                final URI normalize = i.normalize();
                string = s;
                if (!normalize.isOpaque()) {
                    final URI resolve = normalize.resolve("./");
                    string = s;
                    if (resolve != null) {
                        string = resolve.toString();
                    }
                }
            }
        }
        return string;
    }
    
    public static boolean d(final String s) {
        boolean b2;
        final boolean b = b2 = false;
        if (!TextUtils.isEmpty((CharSequence)s)) {
            final Uri parse = Uri.parse(s);
            b2 = b;
            if (parse.getScheme() != null) {
                b2 = b;
                if (parse.getScheme().equals("market")) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    public static boolean e(final String s) {
        boolean b2;
        final boolean b = b2 = false;
        if (!TextUtils.isEmpty((CharSequence)s)) {
            final Uri parse = Uri.parse(s);
            b2 = b;
            if (parse.getScheme() != null) {
                if (!parse.getScheme().equals("http")) {
                    b2 = b;
                    if (!parse.getScheme().equals("https")) {
                        return b2;
                    }
                }
                b2 = true;
            }
        }
        return b2;
    }
    
    public static boolean f(final String s) {
        boolean b2;
        final boolean b = b2 = false;
        if (!TextUtils.isEmpty((CharSequence)s)) {
            final Uri parse = Uri.parse(s);
            b2 = b;
            if (parse.getHost() != null) {
                b2 = b;
                if (parse.getHost().equals("play.google.com")) {
                    b2 = b;
                    if (parse.getScheme() != null) {
                        b2 = b;
                        if (parse.getScheme().startsWith("http")) {
                            b2 = true;
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    public static boolean g(String mimeTypeFromExtension) {
        boolean b2;
        final boolean b = b2 = false;
        if (!TextUtils.isEmpty((CharSequence)mimeTypeFromExtension)) {
            mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(mimeTypeFromExtension));
            b2 = b;
            if (mimeTypeFromExtension != null) {
                b2 = b;
                if (mimeTypeFromExtension.startsWith("video/")) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    public static boolean h(final String s) {
        final boolean b = false;
        boolean b2;
        if (TextUtils.isEmpty((CharSequence)s)) {
            b2 = b;
        }
        else {
            final URI i = i(s);
            b2 = b;
            if (i != null) {
                if (i.getScheme() != null && !"http".equalsIgnoreCase(i.getScheme())) {
                    b2 = b;
                    if (!"https".equalsIgnoreCase(i.getScheme())) {
                        return b2;
                    }
                }
                b2 = true;
            }
        }
        return b2;
    }
    
    private static URI i(final String str) {
        try {
            return new URI(str);
        }
        catch (URISyntaxException ex) {
            return null;
        }
    }
}
