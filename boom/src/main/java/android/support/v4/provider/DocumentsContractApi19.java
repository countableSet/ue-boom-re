// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.provider;

import android.database.Cursor;
import android.content.ContentResolver;
import android.util.Log;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.net.Uri;
import android.content.Context;

class DocumentsContractApi19
{
    private static final String TAG = "DocumentFile";
    
    public static boolean canRead(final Context context, final Uri uri) {
        boolean b = false;
        if (context.checkCallingOrSelfUriPermission(uri, 1) == 0 && !TextUtils.isEmpty((CharSequence)getRawType(context, uri))) {
            b = true;
        }
        return b;
    }
    
    public static boolean canWrite(final Context context, final Uri uri) {
        final boolean b = false;
        boolean b2;
        if (context.checkCallingOrSelfUriPermission(uri, 2) != 0) {
            b2 = b;
        }
        else {
            final String rawType = getRawType(context, uri);
            final int queryForInt = queryForInt(context, uri, "flags", 0);
            b2 = b;
            if (!TextUtils.isEmpty((CharSequence)rawType)) {
                if ((queryForInt & 0x4) != 0x0) {
                    b2 = true;
                }
                else if ("vnd.android.document/directory".equals(rawType) && (queryForInt & 0x8) != 0x0) {
                    b2 = true;
                }
                else {
                    b2 = b;
                    if (!TextUtils.isEmpty((CharSequence)rawType)) {
                        b2 = b;
                        if ((queryForInt & 0x2) != 0x0) {
                            b2 = true;
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    private static void closeQuietly(final AutoCloseable autoCloseable) {
        if (autoCloseable == null) {
            return;
        }
        try {
            autoCloseable.close();
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex2) {}
    }
    
    public static boolean delete(final Context context, final Uri uri) {
        return DocumentsContract.deleteDocument(context.getContentResolver(), uri);
    }
    
    public static boolean exists(Context query, final Uri uri) {
        final ContentResolver contentResolver = query.getContentResolver();
        Object o = null;
        query = null;
        try {
            return ((Cursor)(o = (query = (Context)contentResolver.query(uri, new String[] { "document_id" }, (String)null, (String[])null, (String)null)))).getCount() > 0;
        }
        catch (Exception obj) {
            o = query;
            o = query;
            final StringBuilder sb = new StringBuilder();
            o = query;
            Log.w("DocumentFile", sb.append("Failed query: ").append(obj).toString());
            closeQuietly((AutoCloseable)query);
            return false;
        }
        finally {
            closeQuietly((AutoCloseable)o);
        }
    }
    
    public static String getName(final Context context, final Uri uri) {
        return queryForString(context, uri, "_display_name", null);
    }
    
    private static String getRawType(final Context context, final Uri uri) {
        return queryForString(context, uri, "mime_type", null);
    }
    
    public static String getType(final Context context, final Uri uri) {
        String rawType;
        if ("vnd.android.document/directory".equals(rawType = getRawType(context, uri))) {
            rawType = null;
        }
        return rawType;
    }
    
    public static boolean isDirectory(final Context context, final Uri uri) {
        return "vnd.android.document/directory".equals(getRawType(context, uri));
    }
    
    public static boolean isDocumentUri(final Context context, final Uri uri) {
        return DocumentsContract.isDocumentUri(context, uri);
    }
    
    public static boolean isFile(final Context context, final Uri uri) {
        final String rawType = getRawType(context, uri);
        return !"vnd.android.document/directory".equals(rawType) && !TextUtils.isEmpty((CharSequence)rawType);
    }
    
    public static long lastModified(final Context context, final Uri uri) {
        return queryForLong(context, uri, "last_modified", 0L);
    }
    
    public static long length(final Context context, final Uri uri) {
        return queryForLong(context, uri, "_size", 0L);
    }
    
    private static int queryForInt(final Context context, final Uri uri, final String s, final int n) {
        return (int)queryForLong(context, uri, s, n);
    }
    
    private static long queryForLong(Context query, final Uri uri, final String s, long long1) {
        final ContentResolver contentResolver = query.getContentResolver();
        Object o = null;
        query = null;
        try {
            final Object o2 = o = (query = (Context)contentResolver.query(uri, new String[] { s }, (String)null, (String[])null, (String)null));
            Label_0081: {
                if (!((Cursor)o2).moveToFirst()) {
                    break Label_0081;
                }
                query = (Context)o2;
                o = o2;
                if (((Cursor)o2).isNull(0)) {
                    break Label_0081;
                }
                query = (Context)o2;
                o = o2;
                long1 = ((Cursor)o2).getLong(0);
                return long1;
            }
            closeQuietly((AutoCloseable)o2);
            return long1;
        }
        catch (Exception obj) {
            o = query;
            o = query;
            final StringBuilder sb = new StringBuilder();
            o = query;
            Log.w("DocumentFile", sb.append("Failed query: ").append(obj).toString());
            closeQuietly((AutoCloseable)query);
            return long1;
        }
        finally {
            closeQuietly((AutoCloseable)o);
        }
    }
    
    private static String queryForString(Context string, final Uri uri, String s, final String s2) {
        final ContentResolver contentResolver = string.getContentResolver();
        string = null;
        Object query = null;
        try {
            final Context context = string = (Context)(query = contentResolver.query(uri, new String[] { s }, (String)null, (String[])null, (String)null));
            Label_0079: {
                if (!((Cursor)context).moveToFirst()) {
                    break Label_0079;
                }
                query = context;
                string = context;
                if (((Cursor)context).isNull(0)) {
                    break Label_0079;
                }
                query = context;
                string = context;
                s = (String)(string = (Context)((Cursor)context).getString(0));
                return (String)string;
            }
            closeQuietly((AutoCloseable)context);
            string = (Context)s2;
            return (String)string;
        }
        catch (Exception obj) {
            string = (Context)query;
            string = (Context)query;
            final StringBuilder sb = new StringBuilder();
            string = (Context)query;
            Log.w("DocumentFile", sb.append("Failed query: ").append(obj).toString());
            closeQuietly((AutoCloseable)query);
            string = (Context)s2;
            return (String)string;
        }
        finally {
            closeQuietly((AutoCloseable)string);
        }
    }
}
