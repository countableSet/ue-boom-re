// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.database;

import android.text.TextUtils;

public final class DatabaseUtilsCompat
{
    private DatabaseUtilsCompat() {
    }
    
    public static String[] appendSelectionArgs(String[] array, final String[] array2) {
        if (array == null || array.length == 0) {
            array = array2;
        }
        else {
            final String[] array3 = new String[array.length + array2.length];
            System.arraycopy(array, 0, array3, 0, array.length);
            System.arraycopy(array2, 0, array3, array.length, array2.length);
            array = array3;
        }
        return array;
    }
    
    public static String concatenateWhere(final String str, String string) {
        if (!TextUtils.isEmpty((CharSequence)str)) {
            if (TextUtils.isEmpty((CharSequence)string)) {
                string = str;
            }
            else {
                string = "(" + str + ") AND (" + string + ")";
            }
        }
        return string;
    }
}
