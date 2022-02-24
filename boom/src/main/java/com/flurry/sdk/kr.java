// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.regex.Matcher;
import android.text.TextUtils;
import java.util.regex.Pattern;

public class kr
{
    private final Pattern a;
    
    public kr() {
        this.a = Pattern.compile(".*?(%\\{\\w+\\}).*?");
    }
    
    public static boolean a(final String str, final String s) {
        return !TextUtils.isEmpty((CharSequence)s) && s.equals("%{" + str + "}");
    }
    
    public final String a(String group) {
        final Matcher matcher = this.a.matcher(group);
        if (matcher.matches()) {
            group = matcher.group(1);
        }
        else {
            group = null;
        }
        return group;
    }
}
