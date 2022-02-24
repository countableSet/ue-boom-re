// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.support.v4.util.LruCache;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.annotation.Nullable;
import android.content.res.Resources$Theme;
import android.support.v4.content.ContextCompat;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.graphics.ColorFilter;
import android.util.AttributeSet;
import android.content.res.XmlResourceParser;
import android.content.res.Resources;
import android.util.Log;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.os.Build$VERSION;
import android.graphics.PorterDuffColorFilter;
import android.support.v4.graphics.ColorUtils;
import android.support.annotation.DrawableRes;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.appcompat.R;
import android.util.TypedValue;
import android.content.res.ColorStateList;
import android.util.SparseArray;
import android.support.v4.util.ArrayMap;
import android.graphics.drawable.Drawable$ConstantState;
import java.lang.ref.WeakReference;
import android.support.v4.util.LongSparseArray;
import android.content.Context;
import java.util.WeakHashMap;
import android.graphics.PorterDuff$Mode;

public final class AppCompatDrawableManager
{
    private static final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY;
    private static final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED;
    private static final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL;
    private static final ColorFilterLruCache COLOR_FILTER_CACHE;
    private static final boolean DEBUG = false;
    private static final PorterDuff$Mode DEFAULT_MODE;
    private static AppCompatDrawableManager INSTANCE;
    private static final String PLATFORM_VD_CLAZZ = "android.graphics.drawable.VectorDrawable";
    private static final String SKIP_DRAWABLE_TAG = "appcompat_skip_skip";
    private static final String TAG = "AppCompatDrawableManager";
    private static final int[] TINT_CHECKABLE_BUTTON_LIST;
    private static final int[] TINT_COLOR_CONTROL_NORMAL;
    private static final int[] TINT_COLOR_CONTROL_STATE_LIST;
    private final Object mDelegateDrawableCacheLock;
    private final WeakHashMap<Context, LongSparseArray<WeakReference<Drawable$ConstantState>>> mDelegateDrawableCaches;
    private ArrayMap<String, InflateDelegate> mDelegates;
    private boolean mHasCheckedVectorDrawableSetup;
    private SparseArray<String> mKnownDrawableIdTags;
    private WeakHashMap<Context, SparseArray<ColorStateList>> mTintLists;
    private TypedValue mTypedValue;
    
    static {
        DEFAULT_MODE = PorterDuff$Mode.SRC_IN;
        COLOR_FILTER_CACHE = new ColorFilterLruCache(6);
        COLORFILTER_TINT_COLOR_CONTROL_NORMAL = new int[] { R.drawable.abc_textfield_search_default_mtrl_alpha, R.drawable.abc_textfield_default_mtrl_alpha, R.drawable.abc_ab_share_pack_mtrl_alpha };
        TINT_COLOR_CONTROL_NORMAL = new int[] { R.drawable.abc_ic_ab_back_mtrl_am_alpha, R.drawable.abc_ic_go_search_api_mtrl_alpha, R.drawable.abc_ic_search_api_mtrl_alpha, R.drawable.abc_ic_commit_search_api_mtrl_alpha, R.drawable.abc_ic_clear_mtrl_alpha, R.drawable.abc_ic_menu_share_mtrl_alpha, R.drawable.abc_ic_menu_copy_mtrl_am_alpha, R.drawable.abc_ic_menu_cut_mtrl_alpha, R.drawable.abc_ic_menu_selectall_mtrl_alpha, R.drawable.abc_ic_menu_paste_mtrl_am_alpha, R.drawable.abc_ic_menu_moreoverflow_mtrl_alpha, R.drawable.abc_ic_voice_search_api_mtrl_alpha };
        COLORFILTER_COLOR_CONTROL_ACTIVATED = new int[] { R.drawable.abc_textfield_activated_mtrl_alpha, R.drawable.abc_textfield_search_activated_mtrl_alpha, R.drawable.abc_cab_background_top_mtrl_alpha, R.drawable.abc_text_cursor_material };
        COLORFILTER_COLOR_BACKGROUND_MULTIPLY = new int[] { R.drawable.abc_popup_background_mtrl_mult, R.drawable.abc_cab_background_internal_bg, R.drawable.abc_menu_hardkey_panel_mtrl_mult };
        TINT_COLOR_CONTROL_STATE_LIST = new int[] { R.drawable.abc_edit_text_material, R.drawable.abc_tab_indicator_material, R.drawable.abc_textfield_search_material, R.drawable.abc_spinner_mtrl_am_alpha, R.drawable.abc_spinner_textfield_background_material, R.drawable.abc_ratingbar_full_material, R.drawable.abc_switch_track_mtrl_alpha, R.drawable.abc_switch_thumb_material, R.drawable.abc_btn_default_mtrl_shape, R.drawable.abc_btn_borderless_material };
        TINT_CHECKABLE_BUTTON_LIST = new int[] { R.drawable.abc_btn_check_material, R.drawable.abc_btn_radio_material };
    }
    
    public AppCompatDrawableManager() {
        this.mDelegateDrawableCacheLock = new Object();
        this.mDelegateDrawableCaches = new WeakHashMap<Context, LongSparseArray<WeakReference<Drawable$ConstantState>>>(0);
    }
    
    private boolean addCachedDelegateDrawable(@NonNull final Context context, final long n, @NonNull final Drawable drawable) {
        final Drawable$ConstantState constantState = drawable.getConstantState();
        if (constantState == null) {
            return false;
        }
        synchronized (this.mDelegateDrawableCacheLock) {
            LongSparseArray<WeakReference<Drawable$ConstantState>> value;
            if ((value = this.mDelegateDrawableCaches.get(context)) == null) {
                value = new LongSparseArray<WeakReference<Drawable$ConstantState>>();
                this.mDelegateDrawableCaches.put(context, value);
            }
            value.put(n, new WeakReference<Drawable$ConstantState>(constantState));
            // monitorexit(this.mDelegateDrawableCacheLock)
            return true;
        }
        return false;
    }
    
    private void addDelegate(@NonNull final String s, @NonNull final InflateDelegate inflateDelegate) {
        if (this.mDelegates == null) {
            this.mDelegates = new ArrayMap<String, InflateDelegate>();
        }
        this.mDelegates.put(s, inflateDelegate);
    }
    
    private void addTintListToCache(@NonNull final Context context, @DrawableRes final int n, @NonNull final ColorStateList list) {
        if (this.mTintLists == null) {
            this.mTintLists = new WeakHashMap<Context, SparseArray<ColorStateList>>();
        }
        SparseArray value;
        if ((value = this.mTintLists.get(context)) == null) {
            value = new SparseArray();
            this.mTintLists.put(context, (SparseArray<ColorStateList>)value);
        }
        value.append(n, (Object)list);
    }
    
    private static boolean arrayContains(final int[] array, final int n) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (array[i] == n) {
                return true;
            }
        }
        return false;
    }
    
    private ColorStateList createButtonColorStateList(final Context context, int themeAttrColor) {
        final int[][] array = new int[4][];
        final int[] array2 = new int[4];
        themeAttrColor = ThemeUtils.getThemeAttrColor(context, themeAttrColor);
        final int themeAttrColor2 = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlHighlight);
        array[0] = ThemeUtils.DISABLED_STATE_SET;
        array2[0] = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorButtonNormal);
        int n = 0 + 1;
        array[n] = ThemeUtils.PRESSED_STATE_SET;
        array2[n] = ColorUtils.compositeColors(themeAttrColor2, themeAttrColor);
        ++n;
        array[n] = ThemeUtils.FOCUSED_STATE_SET;
        array2[n] = ColorUtils.compositeColors(themeAttrColor2, themeAttrColor);
        final int n2 = n + 1;
        array[n2] = ThemeUtils.EMPTY_STATE_SET;
        array2[n2] = themeAttrColor;
        return new ColorStateList(array, array2);
    }
    
    private ColorStateList createCheckableButtonColorStateList(final Context context) {
        final int[][] array = new int[3][];
        final int[] array2 = new int[3];
        array[0] = ThemeUtils.DISABLED_STATE_SET;
        array2[0] = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorControlNormal);
        int n = 0 + 1;
        array[n] = ThemeUtils.CHECKED_STATE_SET;
        array2[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
        ++n;
        array[n] = ThemeUtils.EMPTY_STATE_SET;
        array2[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal);
        return new ColorStateList(array, array2);
    }
    
    private ColorStateList createColoredButtonColorStateList(final Context context) {
        return this.createButtonColorStateList(context, R.attr.colorAccent);
    }
    
    private ColorStateList createDefaultButtonColorStateList(final Context context) {
        return this.createButtonColorStateList(context, R.attr.colorButtonNormal);
    }
    
    private ColorStateList createDefaultColorStateList(final Context context) {
        final int themeAttrColor = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal);
        final int themeAttrColor2 = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
        final int[][] array = new int[7][];
        final int[] array2 = new int[7];
        array[0] = ThemeUtils.DISABLED_STATE_SET;
        array2[0] = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorControlNormal);
        int n = 0 + 1;
        array[n] = ThemeUtils.FOCUSED_STATE_SET;
        array2[n] = themeAttrColor2;
        ++n;
        array[n] = ThemeUtils.ACTIVATED_STATE_SET;
        array2[n] = themeAttrColor2;
        ++n;
        array[n] = ThemeUtils.PRESSED_STATE_SET;
        array2[n] = themeAttrColor2;
        ++n;
        array[n] = ThemeUtils.CHECKED_STATE_SET;
        array2[n] = themeAttrColor2;
        ++n;
        array[n] = ThemeUtils.SELECTED_STATE_SET;
        array2[n] = themeAttrColor2;
        final int n2 = n + 1;
        array[n2] = ThemeUtils.EMPTY_STATE_SET;
        array2[n2] = themeAttrColor;
        return new ColorStateList(array, array2);
    }
    
    private ColorStateList createEditTextColorStateList(final Context context) {
        final int[][] array = new int[3][];
        final int[] array2 = new int[3];
        array[0] = ThemeUtils.DISABLED_STATE_SET;
        array2[0] = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorControlNormal);
        int n = 0 + 1;
        array[n] = ThemeUtils.NOT_PRESSED_OR_FOCUSED_STATE_SET;
        array2[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal);
        ++n;
        array[n] = ThemeUtils.EMPTY_STATE_SET;
        array2[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
        return new ColorStateList(array, array2);
    }
    
    private ColorStateList createSeekbarThumbColorStateList(final Context context) {
        final int[][] array = new int[2][];
        final int[] array2 = new int[2];
        array[0] = ThemeUtils.DISABLED_STATE_SET;
        array2[0] = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorControlActivated);
        final int n = 0 + 1;
        array[n] = ThemeUtils.EMPTY_STATE_SET;
        array2[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
        return new ColorStateList(array, array2);
    }
    
    private ColorStateList createSpinnerColorStateList(final Context context) {
        final int[][] array = new int[3][];
        final int[] array2 = new int[3];
        array[0] = ThemeUtils.DISABLED_STATE_SET;
        array2[0] = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorControlNormal);
        int n = 0 + 1;
        array[n] = ThemeUtils.NOT_PRESSED_OR_FOCUSED_STATE_SET;
        array2[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal);
        ++n;
        array[n] = ThemeUtils.EMPTY_STATE_SET;
        array2[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
        return new ColorStateList(array, array2);
    }
    
    private ColorStateList createSwitchThumbColorStateList(final Context context) {
        final int[][] array = new int[3][];
        final int[] array2 = new int[3];
        final ColorStateList themeAttrColorStateList = ThemeUtils.getThemeAttrColorStateList(context, R.attr.colorSwitchThumbNormal);
        if (themeAttrColorStateList != null && themeAttrColorStateList.isStateful()) {
            array[0] = ThemeUtils.DISABLED_STATE_SET;
            array2[0] = themeAttrColorStateList.getColorForState(array[0], 0);
            int n = 0 + 1;
            array[n] = ThemeUtils.CHECKED_STATE_SET;
            array2[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
            ++n;
            array[n] = ThemeUtils.EMPTY_STATE_SET;
            array2[n] = themeAttrColorStateList.getDefaultColor();
        }
        else {
            array[0] = ThemeUtils.DISABLED_STATE_SET;
            array2[0] = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorSwitchThumbNormal);
            int n2 = 0 + 1;
            array[n2] = ThemeUtils.CHECKED_STATE_SET;
            array2[n2] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
            ++n2;
            array[n2] = ThemeUtils.EMPTY_STATE_SET;
            array2[n2] = ThemeUtils.getThemeAttrColor(context, R.attr.colorSwitchThumbNormal);
        }
        return new ColorStateList(array, array2);
    }
    
    private ColorStateList createSwitchTrackColorStateList(final Context context) {
        final int[][] array = new int[3][];
        final int[] array2 = new int[3];
        array[0] = ThemeUtils.DISABLED_STATE_SET;
        array2[0] = ThemeUtils.getThemeAttrColor(context, 16842800, 0.1f);
        int n = 0 + 1;
        array[n] = ThemeUtils.CHECKED_STATE_SET;
        array2[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated, 0.3f);
        ++n;
        array[n] = ThemeUtils.EMPTY_STATE_SET;
        array2[n] = ThemeUtils.getThemeAttrColor(context, 16842800, 0.3f);
        return new ColorStateList(array, array2);
    }
    
    private static PorterDuffColorFilter createTintFilter(final ColorStateList list, final PorterDuff$Mode porterDuff$Mode, final int[] array) {
        PorterDuffColorFilter porterDuffColorFilter;
        if (list == null || porterDuff$Mode == null) {
            porterDuffColorFilter = null;
        }
        else {
            porterDuffColorFilter = getPorterDuffColorFilter(list.getColorForState(array, 0), porterDuff$Mode);
        }
        return porterDuffColorFilter;
    }
    
    public static AppCompatDrawableManager get() {
        if (AppCompatDrawableManager.INSTANCE == null) {
            installDefaultInflateDelegates(AppCompatDrawableManager.INSTANCE = new AppCompatDrawableManager());
        }
        return AppCompatDrawableManager.INSTANCE;
    }
    
    private Drawable getCachedDelegateDrawable(@NonNull final Context key, final long n) {
        while (true) {
            final Drawable drawable = null;
            Label_0097: {
                final LongSparseArray<WeakReference<Drawable$ConstantState>> longSparseArray;
                Label_0091: {
                    synchronized (this.mDelegateDrawableCacheLock) {
                        longSparseArray = this.mDelegateDrawableCaches.get(key);
                        Drawable drawable2;
                        if (longSparseArray == null) {
                            // monitorexit(this.mDelegateDrawableCacheLock)
                            drawable2 = drawable;
                        }
                        else {
                            final WeakReference<Drawable$ConstantState> weakReference = longSparseArray.get(n);
                            if (weakReference == null) {
                                break Label_0097;
                            }
                            final Drawable$ConstantState drawable$ConstantState = weakReference.get();
                            if (drawable$ConstantState == null) {
                                break Label_0091;
                            }
                            drawable2 = drawable$ConstantState.newDrawable(key.getResources());
                        }
                        // monitorexit(this.mDelegateDrawableCacheLock)
                        return drawable2;
                    }
                }
                longSparseArray.delete(n);
            }
            // monitorexit(o)
            return drawable;
        }
    }
    
    public static PorterDuffColorFilter getPorterDuffColorFilter(final int n, final PorterDuff$Mode porterDuff$Mode) {
        PorterDuffColorFilter value;
        if ((value = AppCompatDrawableManager.COLOR_FILTER_CACHE.get(n, porterDuff$Mode)) == null) {
            value = new PorterDuffColorFilter(n, porterDuff$Mode);
            AppCompatDrawableManager.COLOR_FILTER_CACHE.put(n, porterDuff$Mode, value);
        }
        return value;
    }
    
    private ColorStateList getTintListFromCache(@NonNull final Context key, @DrawableRes final int n) {
        ColorStateList list = null;
        if (this.mTintLists != null) {
            final SparseArray<ColorStateList> sparseArray = this.mTintLists.get(key);
            list = list;
            if (sparseArray != null) {
                list = (ColorStateList)sparseArray.get(n);
            }
        }
        return list;
    }
    
    private static void installDefaultInflateDelegates(@NonNull final AppCompatDrawableManager appCompatDrawableManager) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT < 21) {
            appCompatDrawableManager.addDelegate("vector", (InflateDelegate)new VdcInflateDelegate());
            if (sdk_INT >= 11) {
                appCompatDrawableManager.addDelegate("animated-vector", (InflateDelegate)new AvdcInflateDelegate());
            }
        }
    }
    
    private static boolean isVectorDrawable(@NonNull final Drawable drawable) {
        return drawable instanceof VectorDrawableCompat || "android.graphics.drawable.VectorDrawable".equals(drawable.getClass().getName());
    }
    
    private Drawable loadDrawableFromDelegates(@NonNull final Context context, @DrawableRes final int n) {
        Drawable drawable;
        if (this.mDelegates != null && !this.mDelegates.isEmpty()) {
            if (this.mKnownDrawableIdTags != null) {
                final String anObject = (String)this.mKnownDrawableIdTags.get(n);
                if ("appcompat_skip_skip".equals(anObject) || (anObject != null && this.mDelegates.get(anObject) == null)) {
                    drawable = null;
                    return drawable;
                }
            }
            else {
                this.mKnownDrawableIdTags = (SparseArray<String>)new SparseArray();
            }
            if (this.mTypedValue == null) {
                this.mTypedValue = new TypedValue();
            }
            final TypedValue mTypedValue = this.mTypedValue;
            final Resources resources = context.getResources();
            resources.getValue(n, mTypedValue, true);
            final long n2 = (long)mTypedValue.assetCookie << 32 | (long)mTypedValue.data;
            final Drawable cachedDelegateDrawable = this.getCachedDelegateDrawable(context, n2);
            if ((drawable = cachedDelegateDrawable) == null) {
                Drawable drawable2 = cachedDelegateDrawable;
                Label_0266: {
                    if (mTypedValue.string != null) {
                        drawable2 = cachedDelegateDrawable;
                        if (mTypedValue.string.toString().endsWith(".xml")) {
                            drawable2 = cachedDelegateDrawable;
                            XmlResourceParser xml = null;
                            AttributeSet attributeSet = null;
                            Label_0292: {
                                try {
                                    xml = resources.getXml(n);
                                    drawable2 = cachedDelegateDrawable;
                                    attributeSet = Xml.asAttributeSet((XmlPullParser)xml);
                                    int next;
                                    do {
                                        drawable2 = cachedDelegateDrawable;
                                        next = ((XmlPullParser)xml).next();
                                    } while (next != 2 && next != 1);
                                    if (next != 2) {
                                        drawable2 = cachedDelegateDrawable;
                                        drawable2 = cachedDelegateDrawable;
                                        final XmlPullParserException ex = new XmlPullParserException("No start tag found");
                                        drawable2 = cachedDelegateDrawable;
                                        throw ex;
                                    }
                                    break Label_0292;
                                }
                                catch (Exception ex2) {
                                    Log.e("AppCompatDrawableManager", "Exception while inflating drawable", (Throwable)ex2);
                                }
                                break Label_0266;
                            }
                            final String name = ((XmlPullParser)xml).getName();
                            this.mKnownDrawableIdTags.append(n, (Object)name);
                            final InflateDelegate inflateDelegate = this.mDelegates.get(name);
                            Drawable fromXmlInner = cachedDelegateDrawable;
                            if (inflateDelegate != null) {
                                fromXmlInner = inflateDelegate.createFromXmlInner(context, (XmlPullParser)xml, attributeSet, context.getTheme());
                            }
                            if ((drawable2 = fromXmlInner) != null) {
                                fromXmlInner.setChangingConfigurations(mTypedValue.changingConfigurations);
                                final boolean addCachedDelegateDrawable = this.addCachedDelegateDrawable(context, n2, fromXmlInner);
                                drawable2 = fromXmlInner;
                                if (addCachedDelegateDrawable) {
                                    drawable2 = fromXmlInner;
                                }
                            }
                        }
                    }
                }
                if ((drawable = drawable2) == null) {
                    this.mKnownDrawableIdTags.append(n, (Object)"appcompat_skip_skip");
                    drawable = drawable2;
                }
            }
        }
        else {
            drawable = null;
        }
        return drawable;
    }
    
    private void removeDelegate(@NonNull final String s, @NonNull final InflateDelegate inflateDelegate) {
        if (this.mDelegates != null && this.mDelegates.get(s) == inflateDelegate) {
            this.mDelegates.remove(s);
        }
    }
    
    private static void setPorterDuffColorFilter(final Drawable drawable, final int n, final PorterDuff$Mode porterDuff$Mode) {
        Drawable mutate = drawable;
        if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
            mutate = drawable.mutate();
        }
        PorterDuff$Mode default_MODE;
        if ((default_MODE = porterDuff$Mode) == null) {
            default_MODE = AppCompatDrawableManager.DEFAULT_MODE;
        }
        mutate.setColorFilter((ColorFilter)getPorterDuffColorFilter(n, default_MODE));
    }
    
    private Drawable tintDrawable(@NonNull final Context context, @DrawableRes final int n, final boolean b, @NonNull final Drawable drawable) {
        final ColorStateList tintList = this.getTintList(context, n);
        Drawable drawable2;
        if (tintList != null) {
            Drawable mutate = drawable;
            if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
                mutate = drawable.mutate();
            }
            final Drawable wrap = DrawableCompat.wrap(mutate);
            DrawableCompat.setTintList(wrap, tintList);
            final PorterDuff$Mode tintMode = this.getTintMode(n);
            drawable2 = wrap;
            if (tintMode != null) {
                DrawableCompat.setTintMode(wrap, tintMode);
                drawable2 = wrap;
            }
        }
        else {
            if (n == R.drawable.abc_cab_background_top_material) {
                final Object o = new LayerDrawable(new Drawable[] { this.getDrawable(context, R.drawable.abc_cab_background_internal_bg), this.getDrawable(context, R.drawable.abc_cab_background_top_mtrl_alpha) });
                return (Drawable)o;
            }
            if (n == R.drawable.abc_seekbar_track_material) {
                final LayerDrawable layerDrawable = (LayerDrawable)drawable;
                setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908288), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal), AppCompatDrawableManager.DEFAULT_MODE);
                setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal), AppCompatDrawableManager.DEFAULT_MODE);
                setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), AppCompatDrawableManager.DEFAULT_MODE);
                drawable2 = drawable;
            }
            else if (n == R.drawable.abc_ratingbar_indicator_material || n == R.drawable.abc_ratingbar_small_material) {
                final LayerDrawable layerDrawable2 = (LayerDrawable)drawable;
                setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(16908288), ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorControlNormal), AppCompatDrawableManager.DEFAULT_MODE);
                setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), AppCompatDrawableManager.DEFAULT_MODE);
                setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), AppCompatDrawableManager.DEFAULT_MODE);
                drawable2 = drawable;
            }
            else {
                drawable2 = drawable;
                if (!tintDrawableUsingColorFilter(context, n, drawable)) {
                    drawable2 = drawable;
                    if (b) {
                        drawable2 = null;
                    }
                }
            }
        }
        final Object o = drawable2;
        return (Drawable)o;
    }
    
    public static void tintDrawable(final Drawable drawable, final TintInfo tintInfo, final int[] array) {
        if (DrawableUtils.canSafelyMutateDrawable(drawable) && drawable.mutate() != drawable) {
            Log.d("AppCompatDrawableManager", "Mutated drawable is not the same instance as the input.");
        }
        else {
            if (tintInfo.mHasTintList || tintInfo.mHasTintMode) {
                ColorStateList mTintList;
                if (tintInfo.mHasTintList) {
                    mTintList = tintInfo.mTintList;
                }
                else {
                    mTintList = null;
                }
                PorterDuff$Mode porterDuff$Mode;
                if (tintInfo.mHasTintMode) {
                    porterDuff$Mode = tintInfo.mTintMode;
                }
                else {
                    porterDuff$Mode = AppCompatDrawableManager.DEFAULT_MODE;
                }
                drawable.setColorFilter((ColorFilter)createTintFilter(mTintList, porterDuff$Mode, array));
            }
            else {
                drawable.clearColorFilter();
            }
            if (Build$VERSION.SDK_INT <= 23) {
                drawable.invalidateSelf();
            }
        }
    }
    
    private static boolean tintDrawableUsingColorFilter(@NonNull final Context context, @DrawableRes final int n, @NonNull final Drawable drawable) {
        final PorterDuff$Mode default_MODE = AppCompatDrawableManager.DEFAULT_MODE;
        int n2 = 0;
        int n3 = 0;
        int round = -1;
        PorterDuff$Mode multiply;
        if (arrayContains(AppCompatDrawableManager.COLORFILTER_TINT_COLOR_CONTROL_NORMAL, n)) {
            n3 = R.attr.colorControlNormal;
            n2 = 1;
            multiply = default_MODE;
        }
        else if (arrayContains(AppCompatDrawableManager.COLORFILTER_COLOR_CONTROL_ACTIVATED, n)) {
            n3 = R.attr.colorControlActivated;
            n2 = 1;
            multiply = default_MODE;
        }
        else if (arrayContains(AppCompatDrawableManager.COLORFILTER_COLOR_BACKGROUND_MULTIPLY, n)) {
            n3 = 16842801;
            n2 = 1;
            multiply = PorterDuff$Mode.MULTIPLY;
        }
        else {
            multiply = default_MODE;
            if (n == R.drawable.abc_list_divider_mtrl_alpha) {
                n3 = 16842800;
                n2 = 1;
                round = Math.round(40.8f);
                multiply = default_MODE;
            }
        }
        boolean b;
        if (n2 != 0) {
            Drawable mutate = drawable;
            if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
                mutate = drawable.mutate();
            }
            mutate.setColorFilter((ColorFilter)getPorterDuffColorFilter(ThemeUtils.getThemeAttrColor(context, n3), multiply));
            if (round != -1) {
                mutate.setAlpha(round);
            }
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    public Drawable getDrawable(@NonNull final Context context, @DrawableRes final int n) {
        return this.getDrawable(context, n, false);
    }
    
    public Drawable getDrawable(@NonNull final Context context, @DrawableRes final int n, final boolean b) {
        Drawable drawable;
        if ((drawable = this.loadDrawableFromDelegates(context, n)) == null) {
            drawable = ContextCompat.getDrawable(context, n);
        }
        Drawable tintDrawable;
        if ((tintDrawable = drawable) != null) {
            tintDrawable = this.tintDrawable(context, n, b, drawable);
        }
        if (tintDrawable != null) {
            DrawableUtils.fixDrawable(tintDrawable);
        }
        return tintDrawable;
    }
    
    public final ColorStateList getTintList(@NonNull final Context context, @DrawableRes final int n) {
        ColorStateList tintListFromCache;
        ColorStateList list = tintListFromCache = this.getTintListFromCache(context, n);
        if (list == null) {
            if (n == R.drawable.abc_edit_text_material) {
                list = this.createEditTextColorStateList(context);
            }
            else if (n == R.drawable.abc_switch_track_mtrl_alpha) {
                list = this.createSwitchTrackColorStateList(context);
            }
            else if (n == R.drawable.abc_switch_thumb_material) {
                list = this.createSwitchThumbColorStateList(context);
            }
            else if (n == R.drawable.abc_btn_default_mtrl_shape || n == R.drawable.abc_btn_borderless_material) {
                list = this.createDefaultButtonColorStateList(context);
            }
            else if (n == R.drawable.abc_btn_colored_material) {
                list = this.createColoredButtonColorStateList(context);
            }
            else if (n == R.drawable.abc_spinner_mtrl_am_alpha || n == R.drawable.abc_spinner_textfield_background_material) {
                list = this.createSpinnerColorStateList(context);
            }
            else if (arrayContains(AppCompatDrawableManager.TINT_COLOR_CONTROL_NORMAL, n)) {
                list = ThemeUtils.getThemeAttrColorStateList(context, R.attr.colorControlNormal);
            }
            else if (arrayContains(AppCompatDrawableManager.TINT_COLOR_CONTROL_STATE_LIST, n)) {
                list = this.createDefaultColorStateList(context);
            }
            else if (arrayContains(AppCompatDrawableManager.TINT_CHECKABLE_BUTTON_LIST, n)) {
                list = this.createCheckableButtonColorStateList(context);
            }
            else if (n == R.drawable.abc_seekbar_thumb_material) {
                list = this.createSeekbarThumbColorStateList(context);
            }
            if ((tintListFromCache = list) != null) {
                this.addTintListToCache(context, n, list);
                tintListFromCache = list;
            }
        }
        return tintListFromCache;
    }
    
    final PorterDuff$Mode getTintMode(final int n) {
        PorterDuff$Mode multiply = null;
        if (n == R.drawable.abc_switch_thumb_material) {
            multiply = PorterDuff$Mode.MULTIPLY;
        }
        return multiply;
    }
    
    public final Drawable onDrawableLoadedFromResources(@NonNull final Context context, @NonNull final TintResources tintResources, @DrawableRes final int n) {
        Drawable drawable;
        if ((drawable = this.loadDrawableFromDelegates(context, n)) == null) {
            drawable = tintResources.superGetDrawable(n);
        }
        Drawable tintDrawable;
        if (drawable != null) {
            tintDrawable = this.tintDrawable(context, n, false, drawable);
        }
        else {
            tintDrawable = null;
        }
        return tintDrawable;
    }
    
    private static class AvdcInflateDelegate implements InflateDelegate
    {
        @Override
        public Drawable createFromXmlInner(@NonNull final Context context, @NonNull final XmlPullParser xmlPullParser, @NonNull final AttributeSet set, @Nullable final Resources$Theme resources$Theme) {
            try {
                return AnimatedVectorDrawableCompat.createFromXmlInner(context, context.getResources(), xmlPullParser, set, resources$Theme);
            }
            catch (Exception ex) {
                Log.e("AvdcInflateDelegate", "Exception while inflating <animated-vector>", (Throwable)ex);
                return null;
            }
        }
    }
    
    private static class ColorFilterLruCache extends LruCache<Integer, PorterDuffColorFilter>
    {
        public ColorFilterLruCache(final int n) {
            super(n);
        }
        
        private static int generateCacheKey(final int n, final PorterDuff$Mode porterDuff$Mode) {
            return (n + 31) * 31 + porterDuff$Mode.hashCode();
        }
        
        PorterDuffColorFilter get(final int n, final PorterDuff$Mode porterDuff$Mode) {
            return this.get(generateCacheKey(n, porterDuff$Mode));
        }
        
        PorterDuffColorFilter put(final int n, final PorterDuff$Mode porterDuff$Mode, final PorterDuffColorFilter porterDuffColorFilter) {
            return this.put(generateCacheKey(n, porterDuff$Mode), porterDuffColorFilter);
        }
    }
    
    private interface InflateDelegate
    {
        Drawable createFromXmlInner(@NonNull final Context p0, @NonNull final XmlPullParser p1, @NonNull final AttributeSet p2, @Nullable final Resources$Theme p3);
    }
    
    private static class VdcInflateDelegate implements InflateDelegate
    {
        @Override
        public Drawable createFromXmlInner(@NonNull final Context context, @NonNull final XmlPullParser xmlPullParser, @NonNull final AttributeSet set, @Nullable final Resources$Theme resources$Theme) {
            try {
                return VectorDrawableCompat.createFromXmlInner(context.getResources(), xmlPullParser, set, resources$Theme);
            }
            catch (Exception ex) {
                Log.e("VdcInflateDelegate", "Exception while inflating <vector>", (Throwable)ex);
                return null;
            }
        }
    }
}
