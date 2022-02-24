// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.view.ViewGroup;
import android.net.Uri$Builder;
import java.util.List;
import android.content.res.Resources;
import java.io.FileNotFoundException;
import android.view.View;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.res.Resources$NotFoundException;
import android.support.v4.content.ContextCompat;
import android.net.Uri;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import android.util.Log;
import android.content.ComponentName;
import android.text.style.TextAppearanceSpan;
import android.text.SpannableString;
import android.support.v7.appcompat.R;
import android.util.TypedValue;
import android.graphics.drawable.Drawable;
import android.database.Cursor;
import android.content.res.ColorStateList;
import android.app.SearchableInfo;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.Drawable$ConstantState;
import java.util.WeakHashMap;
import android.view.View$OnClickListener;
import android.support.v4.widget.ResourceCursorAdapter;

class SuggestionsAdapter extends ResourceCursorAdapter implements View$OnClickListener
{
    private static final boolean DBG = false;
    static final int INVALID_INDEX = -1;
    private static final String LOG_TAG = "SuggestionsAdapter";
    private static final int QUERY_LIMIT = 50;
    static final int REFINE_ALL = 2;
    static final int REFINE_BY_ENTRY = 1;
    static final int REFINE_NONE = 0;
    private boolean mClosed;
    private final int mCommitIconResId;
    private int mFlagsCol;
    private int mIconName1Col;
    private int mIconName2Col;
    private final WeakHashMap<String, Drawable$ConstantState> mOutsideDrawablesCache;
    private final Context mProviderContext;
    private int mQueryRefinement;
    private final SearchManager mSearchManager;
    private final SearchView mSearchView;
    private final SearchableInfo mSearchable;
    private int mText1Col;
    private int mText2Col;
    private int mText2UrlCol;
    private ColorStateList mUrlColor;
    
    public SuggestionsAdapter(final Context mProviderContext, final SearchView mSearchView, final SearchableInfo mSearchable, final WeakHashMap<String, Drawable$ConstantState> mOutsideDrawablesCache) {
        super(mProviderContext, mSearchView.getSuggestionRowLayout(), null, true);
        this.mClosed = false;
        this.mQueryRefinement = 1;
        this.mText1Col = -1;
        this.mText2Col = -1;
        this.mText2UrlCol = -1;
        this.mIconName1Col = -1;
        this.mIconName2Col = -1;
        this.mFlagsCol = -1;
        this.mSearchManager = (SearchManager)this.mContext.getSystemService("search");
        this.mSearchView = mSearchView;
        this.mSearchable = mSearchable;
        this.mCommitIconResId = mSearchView.getSuggestionCommitIconResId();
        this.mProviderContext = mProviderContext;
        this.mOutsideDrawablesCache = mOutsideDrawablesCache;
    }
    
    private Drawable checkIconCache(final String key) {
        final Drawable$ConstantState drawable$ConstantState = this.mOutsideDrawablesCache.get(key);
        Drawable drawable;
        if (drawable$ConstantState == null) {
            drawable = null;
        }
        else {
            drawable = drawable$ConstantState.newDrawable();
        }
        return drawable;
    }
    
    private CharSequence formatUrl(final CharSequence charSequence) {
        if (this.mUrlColor == null) {
            final TypedValue typedValue = new TypedValue();
            this.mContext.getTheme().resolveAttribute(R.attr.textColorSearchUrl, typedValue, true);
            this.mUrlColor = this.mContext.getResources().getColorStateList(typedValue.resourceId);
        }
        final SpannableString spannableString = new SpannableString(charSequence);
        spannableString.setSpan((Object)new TextAppearanceSpan((String)null, 0, 0, this.mUrlColor, (ColorStateList)null), 0, charSequence.length(), 33);
        return (CharSequence)spannableString;
    }
    
    private Drawable getActivityIcon(final ComponentName componentName) {
        while (true) {
            final PackageManager packageManager = this.mContext.getPackageManager();
            ActivityInfo activityInfo;
            int iconResource;
            try {
                activityInfo = packageManager.getActivityInfo(componentName, 128);
                iconResource = activityInfo.getIconResource();
                if (iconResource == 0) {
                    return null;
                }
            }
            catch (PackageManager$NameNotFoundException ex) {
                Log.w("SuggestionsAdapter", ex.toString());
                return null;
            }
            Drawable drawable;
            if ((drawable = packageManager.getDrawable(componentName.getPackageName(), iconResource, activityInfo.applicationInfo)) == null) {
                Log.w("SuggestionsAdapter", "Invalid icon resource " + iconResource + " for " + componentName.flattenToShortString());
                drawable = null;
                return drawable;
            }
            return drawable;
        }
    }
    
    private Drawable getActivityIconWithCache(final ComponentName componentName) {
        final Drawable drawable = null;
        final String flattenToShortString = componentName.flattenToShortString();
        Drawable drawable2;
        if (this.mOutsideDrawablesCache.containsKey(flattenToShortString)) {
            final Drawable$ConstantState drawable$ConstantState = this.mOutsideDrawablesCache.get(flattenToShortString);
            if (drawable$ConstantState == null) {
                drawable2 = drawable;
            }
            else {
                drawable2 = drawable$ConstantState.newDrawable(this.mProviderContext.getResources());
            }
        }
        else {
            final Drawable activityIcon = this.getActivityIcon(componentName);
            Drawable$ConstantState constantState;
            if (activityIcon == null) {
                constantState = null;
            }
            else {
                constantState = activityIcon.getConstantState();
            }
            this.mOutsideDrawablesCache.put(flattenToShortString, constantState);
            drawable2 = activityIcon;
        }
        return drawable2;
    }
    
    public static String getColumnString(final Cursor cursor, final String s) {
        return getStringOrNull(cursor, cursor.getColumnIndex(s));
    }
    
    private Drawable getDefaultIcon1(final Cursor cursor) {
        Drawable drawable = this.getActivityIconWithCache(this.mSearchable.getSearchActivity());
        if (drawable == null) {
            drawable = this.mContext.getPackageManager().getDefaultActivityIcon();
        }
        return drawable;
    }
    
    private Drawable getDrawable(final Uri p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: aload_1        
        //     4: invokevirtual   android/net/Uri.getScheme:()Ljava/lang/String;
        //     7: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    10: istore_2       
        //    11: iload_2        
        //    12: ifeq            106
        //    15: aload_0        
        //    16: aload_1        
        //    17: invokevirtual   android/support/v7/widget/SuggestionsAdapter.getDrawableFromResourceUri:(Landroid/net/Uri;)Landroid/graphics/drawable/Drawable;
        //    20: astore_3       
        //    21: aload_3        
        //    22: astore_1       
        //    23: aload_1        
        //    24: areturn        
        //    25: astore_3       
        //    26: new             Ljava/io/FileNotFoundException;
        //    29: astore          4
        //    31: new             Ljava/lang/StringBuilder;
        //    34: astore_3       
        //    35: aload_3        
        //    36: invokespecial   java/lang/StringBuilder.<init>:()V
        //    39: aload           4
        //    41: aload_3        
        //    42: ldc_w           "Resource does not exist: "
        //    45: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    48: aload_1        
        //    49: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    52: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    55: invokespecial   java/io/FileNotFoundException.<init>:(Ljava/lang/String;)V
        //    58: aload           4
        //    60: athrow         
        //    61: astore_3       
        //    62: ldc             "SuggestionsAdapter"
        //    64: new             Ljava/lang/StringBuilder;
        //    67: dup            
        //    68: invokespecial   java/lang/StringBuilder.<init>:()V
        //    71: ldc_w           "Icon not found: "
        //    74: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    77: aload_1        
        //    78: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    81: ldc_w           ", "
        //    84: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    87: aload_3        
        //    88: invokevirtual   java/io/FileNotFoundException.getMessage:()Ljava/lang/String;
        //    91: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    94: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    97: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //   100: pop            
        //   101: aconst_null    
        //   102: astore_1       
        //   103: goto            23
        //   106: aload_0        
        //   107: getfield        android/support/v7/widget/SuggestionsAdapter.mProviderContext:Landroid/content/Context;
        //   110: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //   113: aload_1        
        //   114: invokevirtual   android/content/ContentResolver.openInputStream:(Landroid/net/Uri;)Ljava/io/InputStream;
        //   117: astore          4
        //   119: aload           4
        //   121: ifnonnull       159
        //   124: new             Ljava/io/FileNotFoundException;
        //   127: astore          4
        //   129: new             Ljava/lang/StringBuilder;
        //   132: astore_3       
        //   133: aload_3        
        //   134: invokespecial   java/lang/StringBuilder.<init>:()V
        //   137: aload           4
        //   139: aload_3        
        //   140: ldc_w           "Failed to open "
        //   143: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   146: aload_1        
        //   147: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   150: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   153: invokespecial   java/io/FileNotFoundException.<init>:(Ljava/lang/String;)V
        //   156: aload           4
        //   158: athrow         
        //   159: aload           4
        //   161: aconst_null    
        //   162: invokestatic    android/graphics/drawable/Drawable.createFromStream:(Ljava/io/InputStream;Ljava/lang/String;)Landroid/graphics/drawable/Drawable;
        //   165: astore_3       
        //   166: aload           4
        //   168: invokevirtual   java/io/InputStream.close:()V
        //   171: aload_3        
        //   172: astore_1       
        //   173: goto            23
        //   176: astore          4
        //   178: new             Ljava/lang/StringBuilder;
        //   181: astore          5
        //   183: aload           5
        //   185: invokespecial   java/lang/StringBuilder.<init>:()V
        //   188: ldc             "SuggestionsAdapter"
        //   190: aload           5
        //   192: ldc_w           "Error closing icon stream for "
        //   195: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   198: aload_1        
        //   199: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   202: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   205: aload           4
        //   207: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   210: pop            
        //   211: aload_3        
        //   212: astore_1       
        //   213: goto            23
        //   216: astore_3       
        //   217: aload           4
        //   219: invokevirtual   java/io/InputStream.close:()V
        //   222: aload_3        
        //   223: athrow         
        //   224: astore          4
        //   226: new             Ljava/lang/StringBuilder;
        //   229: astore          5
        //   231: aload           5
        //   233: invokespecial   java/lang/StringBuilder.<init>:()V
        //   236: ldc             "SuggestionsAdapter"
        //   238: aload           5
        //   240: ldc_w           "Error closing icon stream for "
        //   243: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   246: aload_1        
        //   247: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   250: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   253: aload           4
        //   255: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   258: pop            
        //   259: goto            222
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                             
        //  -----  -----  -----  -----  -------------------------------------------------
        //  0      11     61     106    Ljava/io/FileNotFoundException;
        //  15     21     25     61     Landroid/content/res/Resources$NotFoundException;
        //  15     21     61     106    Ljava/io/FileNotFoundException;
        //  26     61     61     106    Ljava/io/FileNotFoundException;
        //  106    119    61     106    Ljava/io/FileNotFoundException;
        //  124    159    61     106    Ljava/io/FileNotFoundException;
        //  159    166    216    262    Any
        //  166    171    176    216    Ljava/io/IOException;
        //  166    171    61     106    Ljava/io/FileNotFoundException;
        //  178    211    61     106    Ljava/io/FileNotFoundException;
        //  217    222    224    262    Ljava/io/IOException;
        //  217    222    61     106    Ljava/io/FileNotFoundException;
        //  222    224    61     106    Ljava/io/FileNotFoundException;
        //  226    259    61     106    Ljava/io/FileNotFoundException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 124 out of bounds for length 124
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:458)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3569)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private Drawable getDrawableFromResourceValue(final String str) {
        Drawable drawable;
        if (str == null || str.length() == 0 || "0".equals(str)) {
            drawable = null;
        }
        else {
            try {
                final int int1 = Integer.parseInt(str);
                final String string = "android.resource://" + this.mProviderContext.getPackageName() + "/" + int1;
                if ((drawable = this.checkIconCache(string)) == null) {
                    drawable = ContextCompat.getDrawable(this.mProviderContext, int1);
                    this.storeInIconCache(string, drawable);
                }
            }
            catch (NumberFormatException ex) {
                if ((drawable = this.checkIconCache(str)) == null) {
                    drawable = this.getDrawable(Uri.parse(str));
                    this.storeInIconCache(str, drawable);
                }
            }
            catch (Resources$NotFoundException ex2) {
                Log.w("SuggestionsAdapter", "Icon resource not found: " + str);
                drawable = null;
            }
        }
        return drawable;
    }
    
    private Drawable getIcon1(final Cursor cursor) {
        Drawable drawable;
        if (this.mIconName1Col == -1) {
            drawable = null;
        }
        else if ((drawable = this.getDrawableFromResourceValue(cursor.getString(this.mIconName1Col))) == null) {
            drawable = this.getDefaultIcon1(cursor);
        }
        return drawable;
    }
    
    private Drawable getIcon2(final Cursor cursor) {
        Drawable drawableFromResourceValue;
        if (this.mIconName2Col == -1) {
            drawableFromResourceValue = null;
        }
        else {
            drawableFromResourceValue = this.getDrawableFromResourceValue(cursor.getString(this.mIconName2Col));
        }
        return drawableFromResourceValue;
    }
    
    private static String getStringOrNull(final Cursor cursor, final int n) {
        final String s = null;
        String string;
        if (n == -1) {
            string = s;
        }
        else {
            try {
                string = cursor.getString(n);
            }
            catch (Exception ex) {
                Log.e("SuggestionsAdapter", "unexpected error retrieving valid column from cursor, did the remote process die?", (Throwable)ex);
                string = s;
            }
        }
        return string;
    }
    
    private void setViewDrawable(final ImageView imageView, final Drawable imageDrawable, final int visibility) {
        imageView.setImageDrawable(imageDrawable);
        if (imageDrawable == null) {
            imageView.setVisibility(visibility);
        }
        else {
            imageView.setVisibility(0);
            imageDrawable.setVisible(false, false);
            imageDrawable.setVisible(true, false);
        }
    }
    
    private void setViewText(final TextView textView, final CharSequence text) {
        textView.setText(text);
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(8);
        }
        else {
            textView.setVisibility(0);
        }
    }
    
    private void storeInIconCache(final String key, final Drawable drawable) {
        if (drawable != null) {
            this.mOutsideDrawablesCache.put(key, drawable.getConstantState());
        }
    }
    
    private void updateSpinnerState(final Cursor cursor) {
        Bundle extras;
        if (cursor != null) {
            extras = cursor.getExtras();
        }
        else {
            extras = null;
        }
        if (extras == null || extras.getBoolean("in_progress")) {}
    }
    
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final ChildViewCache childViewCache = (ChildViewCache)view.getTag();
        int int1 = 0;
        if (this.mFlagsCol != -1) {
            int1 = cursor.getInt(this.mFlagsCol);
        }
        if (childViewCache.mText1 != null) {
            this.setViewText(childViewCache.mText1, getStringOrNull(cursor, this.mText1Col));
        }
        if (childViewCache.mText2 != null) {
            final String stringOrNull = getStringOrNull(cursor, this.mText2UrlCol);
            CharSequence charSequence;
            if (stringOrNull != null) {
                charSequence = this.formatUrl(stringOrNull);
            }
            else {
                charSequence = getStringOrNull(cursor, this.mText2Col);
            }
            if (TextUtils.isEmpty(charSequence)) {
                if (childViewCache.mText1 != null) {
                    childViewCache.mText1.setSingleLine(false);
                    childViewCache.mText1.setMaxLines(2);
                }
            }
            else if (childViewCache.mText1 != null) {
                childViewCache.mText1.setSingleLine(true);
                childViewCache.mText1.setMaxLines(1);
            }
            this.setViewText(childViewCache.mText2, charSequence);
        }
        if (childViewCache.mIcon1 != null) {
            this.setViewDrawable(childViewCache.mIcon1, this.getIcon1(cursor), 4);
        }
        if (childViewCache.mIcon2 != null) {
            this.setViewDrawable(childViewCache.mIcon2, this.getIcon2(cursor), 8);
        }
        if (this.mQueryRefinement == 2 || (this.mQueryRefinement == 1 && (int1 & 0x1) != 0x0)) {
            childViewCache.mIconRefine.setVisibility(0);
            childViewCache.mIconRefine.setTag((Object)childViewCache.mText1.getText());
            childViewCache.mIconRefine.setOnClickListener((View$OnClickListener)this);
        }
        else {
            childViewCache.mIconRefine.setVisibility(8);
        }
    }
    
    public void changeCursor(final Cursor cursor) {
        if (this.mClosed) {
            Log.w("SuggestionsAdapter", "Tried to change cursor after adapter was closed.");
            if (cursor != null) {
                cursor.close();
            }
        }
        else {
            try {
                super.changeCursor(cursor);
                if (cursor != null) {
                    this.mText1Col = cursor.getColumnIndex("suggest_text_1");
                    this.mText2Col = cursor.getColumnIndex("suggest_text_2");
                    this.mText2UrlCol = cursor.getColumnIndex("suggest_text_2_url");
                    this.mIconName1Col = cursor.getColumnIndex("suggest_icon_1");
                    this.mIconName2Col = cursor.getColumnIndex("suggest_icon_2");
                    this.mFlagsCol = cursor.getColumnIndex("suggest_flags");
                }
            }
            catch (Exception ex) {
                Log.e("SuggestionsAdapter", "error changing cursor and caching columns", (Throwable)ex);
            }
        }
    }
    
    public void close() {
        this.changeCursor(null);
        this.mClosed = true;
    }
    
    public CharSequence convertToString(final Cursor cursor) {
        CharSequence charSequence;
        if (cursor == null) {
            charSequence = null;
        }
        else if ((charSequence = getColumnString(cursor, "suggest_intent_query")) == null) {
            if (this.mSearchable.shouldRewriteQueryFromData()) {
                charSequence = getColumnString(cursor, "suggest_intent_data");
                if (charSequence != null) {
                    return charSequence;
                }
            }
            if (this.mSearchable.shouldRewriteQueryFromText()) {
                charSequence = getColumnString(cursor, "suggest_text_1");
                if (charSequence != null) {
                    return charSequence;
                }
            }
            charSequence = null;
        }
        return charSequence;
    }
    
    Drawable getDrawableFromResourceUri(final Uri uri) throws FileNotFoundException {
        final String authority = uri.getAuthority();
        if (TextUtils.isEmpty((CharSequence)authority)) {
            throw new FileNotFoundException("No authority: " + uri);
        }
        Resources resourcesForApplication;
        List pathSegments;
        try {
            resourcesForApplication = this.mContext.getPackageManager().getResourcesForApplication(authority);
            pathSegments = uri.getPathSegments();
            if (pathSegments == null) {
                throw new FileNotFoundException("No path: " + uri);
            }
        }
        catch (PackageManager$NameNotFoundException ex) {
            throw new FileNotFoundException("No package found for authority: " + uri);
        }
        final int size = pathSegments.size();
        while (true) {
            Label_0213: {
                if (size != 1) {
                    break Label_0213;
                }
                try {
                    final int n = Integer.parseInt(pathSegments.get(0));
                    if (n == 0) {
                        throw new FileNotFoundException("No resource found for: " + uri);
                    }
                    return resourcesForApplication.getDrawable(n);
                }
                catch (NumberFormatException ex2) {
                    throw new FileNotFoundException("Single path segment is not a resource ID: " + uri);
                }
            }
            if (size == 2) {
                final int n = resourcesForApplication.getIdentifier((String)pathSegments.get(1), (String)pathSegments.get(0), authority);
                continue;
            }
            break;
        }
        throw new FileNotFoundException("More than two path segments: " + uri);
    }
    
    public int getQueryRefinement() {
        return this.mQueryRefinement;
    }
    
    Cursor getSearchManagerSuggestions(final SearchableInfo searchableInfo, final String s, final int i) {
        Cursor query = null;
        if (searchableInfo != null) {
            final String suggestAuthority = searchableInfo.getSuggestAuthority();
            if (suggestAuthority != null) {
                final Uri$Builder fragment = new Uri$Builder().scheme("content").authority(suggestAuthority).query("").fragment("");
                final String suggestPath = searchableInfo.getSuggestPath();
                if (suggestPath != null) {
                    fragment.appendEncodedPath(suggestPath);
                }
                fragment.appendPath("search_suggest_query");
                final String suggestSelection = searchableInfo.getSuggestSelection();
                String[] array = null;
                if (suggestSelection != null) {
                    array = new String[] { s };
                }
                else {
                    fragment.appendPath(s);
                }
                if (i > 0) {
                    fragment.appendQueryParameter("limit", String.valueOf(i));
                }
                query = this.mContext.getContentResolver().query(fragment.build(), (String[])null, suggestSelection, array, (String)null);
            }
        }
        return query;
    }
    
    public View getView(final int n, View view, ViewGroup viewGroup) {
        try {
            view = super.getView(n, view, viewGroup);
            return view;
        }
        catch (RuntimeException ex) {
            Log.w("SuggestionsAdapter", "Search suggestions cursor threw exception.", (Throwable)ex);
            viewGroup = (ViewGroup)(view = this.newView(this.mContext, this.mCursor, viewGroup));
            if (viewGroup != null) {
                ((ChildViewCache)((View)viewGroup).getTag()).mText1.setText((CharSequence)ex.toString());
                view = (View)viewGroup;
                return view;
            }
            return view;
        }
    }
    
    public boolean hasStableIds() {
        return false;
    }
    
    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup viewGroup) {
        final View view = super.newView(context, cursor, viewGroup);
        view.setTag((Object)new ChildViewCache(view));
        ((ImageView)view.findViewById(R.id.edit_query)).setImageResource(this.mCommitIconResId);
        return view;
    }
    
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.updateSpinnerState(this.getCursor());
    }
    
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
        this.updateSpinnerState(this.getCursor());
    }
    
    public void onClick(final View view) {
        final Object tag = view.getTag();
        if (tag instanceof CharSequence) {
            this.mSearchView.onQueryRefine((CharSequence)tag);
        }
    }
    
    public Cursor runQueryOnBackgroundThread(final CharSequence charSequence) {
        final Cursor cursor = null;
        String string;
        if (charSequence == null) {
            string = "";
        }
        else {
            string = charSequence.toString();
        }
        Cursor cursor2 = cursor;
        if (this.mSearchView.getVisibility() == 0) {
            if (this.mSearchView.getWindowVisibility() != 0) {
                cursor2 = cursor;
            }
            else {
                try {
                    final Cursor searchManagerSuggestions = this.getSearchManagerSuggestions(this.mSearchable, string, 50);
                    cursor2 = cursor;
                    if (searchManagerSuggestions != null) {
                        searchManagerSuggestions.getCount();
                        cursor2 = searchManagerSuggestions;
                    }
                }
                catch (RuntimeException ex) {
                    Log.w("SuggestionsAdapter", "Search suggestions query threw an exception.", (Throwable)ex);
                    cursor2 = cursor;
                }
            }
        }
        return cursor2;
    }
    
    public void setQueryRefinement(final int mQueryRefinement) {
        this.mQueryRefinement = mQueryRefinement;
    }
    
    private static final class ChildViewCache
    {
        public final ImageView mIcon1;
        public final ImageView mIcon2;
        public final ImageView mIconRefine;
        public final TextView mText1;
        public final TextView mText2;
        
        public ChildViewCache(final View view) {
            this.mText1 = (TextView)view.findViewById(16908308);
            this.mText2 = (TextView)view.findViewById(16908309);
            this.mIcon1 = (ImageView)view.findViewById(16908295);
            this.mIcon2 = (ImageView)view.findViewById(16908296);
            this.mIconRefine = (ImageView)view.findViewById(R.id.edit_query);
        }
    }
}
