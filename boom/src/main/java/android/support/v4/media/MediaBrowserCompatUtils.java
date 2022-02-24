// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.media;

import android.os.Bundle;

public class MediaBrowserCompatUtils
{
    public static boolean areSameOptions(final Bundle bundle, final Bundle bundle2) {
        boolean b = true;
        if (bundle != bundle2) {
            if (bundle == null) {
                if (bundle2.getInt("android.media.browse.extra.PAGE", -1) != -1 || bundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1) != -1) {
                    b = false;
                }
            }
            else if (bundle2 == null) {
                if (bundle.getInt("android.media.browse.extra.PAGE", -1) != -1 || bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1) != -1) {
                    b = false;
                }
            }
            else if (bundle.getInt("android.media.browse.extra.PAGE", -1) != bundle2.getInt("android.media.browse.extra.PAGE", -1) || bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1) != bundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1)) {
                b = false;
            }
        }
        return b;
    }
    
    public static boolean hasDuplicatedItems(final Bundle bundle, final Bundle bundle2) {
        boolean b = true;
        int int1;
        if (bundle == null) {
            int1 = -1;
        }
        else {
            int1 = bundle.getInt("android.media.browse.extra.PAGE", -1);
        }
        int int2;
        if (bundle2 == null) {
            int2 = -1;
        }
        else {
            int2 = bundle2.getInt("android.media.browse.extra.PAGE", -1);
        }
        int int3;
        if (bundle == null) {
            int3 = -1;
        }
        else {
            int3 = bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1);
        }
        int int4;
        if (bundle2 == null) {
            int4 = -1;
        }
        else {
            int4 = bundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1);
        }
        int n;
        int n2;
        if (int1 == -1 || int3 == -1) {
            n = 0;
            n2 = Integer.MAX_VALUE;
        }
        else {
            final int n3 = int3 * int1;
            n2 = n3 + int3 - 1;
            n = n3;
        }
        int n4;
        int n5;
        if (int2 == -1 || int4 == -1) {
            n4 = 0;
            n5 = Integer.MAX_VALUE;
        }
        else {
            n4 = int4 * int2;
            n5 = n4 + int4 - 1;
        }
        if ((n > n4 || n4 > n2) && (n > n5 || n5 > n2)) {
            b = false;
        }
        return b;
    }
}
