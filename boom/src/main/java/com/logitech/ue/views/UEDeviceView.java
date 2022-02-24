// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.views;

import android.view.View$MeasureSpec;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.content.res.TypedArray;
import com.logitech.ue.R;
import butterknife.ButterKnife;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.content.ContextCompat;
import android.graphics.drawable.BitmapDrawable;
import com.logitech.ue.other.SVGRenderCacher;
import android.graphics.Rect;
import android.view.View;
import com.logitech.ue.Utils;
import com.caverock.androidsvg.SVG;
import com.logitech.ue.content.ContentLoadListener;
import com.logitech.ue.content.ContentManager;
import android.graphics.drawable.Drawable;
import com.logitech.ue.centurion.device.devicedata.UEDeviceDimension;
import com.logitech.ue.UEColourHelper;
import com.logitech.ue.centurion.device.devicedata.UEDeviceType;
import android.graphics.Point;
import android.util.AttributeSet;
import android.content.Context;
import com.logitech.ue.centurion.device.devicedata.UEColour;
import android.widget.ImageView;
import android.animation.Animator;
import android.util.SparseIntArray;
import android.widget.FrameLayout;

public class UEDeviceView extends FrameLayout
{
    public static final String DOT_MODE_THEME_NAME = "UEPartyUpCircle";
    public static final int FILL_TIME = 600;
    public static final String LARGE_MODE_THEME_NAME = "UEPartyUp";
    public static final String SMALL_MODE_THEME_NAME = "UEPartyUpDrawer";
    public static final String TAG;
    private static final SparseIntArray mDotResourceMap;
    private static final SparseIntArray mLargeResourceMap;
    private static final SparseIntArray mSmallRecourseMap;
    protected Animator mAnimator;
    protected ImageView mBackImageView;
    protected int mColor;
    protected ImageView mFrontImageView;
    protected CircleSpinner mLoadingSpinner;
    protected float mMaxHeight;
    protected float mMaxWidth;
    private DisplayMode mMode;
    
    static {
        TAG = UEDeviceView.class.getSimpleName();
        (mLargeResourceMap = new SparseIntArray()).append(UEColour.KORA_WHITE.getCode(), 2130837654);
        UEDeviceView.mLargeResourceMap.append(UEColour.KORA_PINK.getCode(), 2130837657);
        UEDeviceView.mLargeResourceMap.append(UEColour.KORA_RED.getCode(), 2130837660);
        UEDeviceView.mLargeResourceMap.append(UEColour.KORA_BLUE.getCode(), 2130837663);
        UEDeviceView.mLargeResourceMap.append(UEColour.KORA_BLACK.getCode(), 2130837666);
        UEDeviceView.mLargeResourceMap.append(UEColour.KORA_MOSS.getCode(), 2130837669);
        UEDeviceView.mLargeResourceMap.append(UEColour.KORA_BLUE_STEEL.getCode(), 2130837672);
        UEDeviceView.mLargeResourceMap.append(UEColour.KORA_SUPER_HERO.getCode(), 2130837675);
        UEDeviceView.mLargeResourceMap.append(UEColour.KORA_AQUA.getCode(), 2130837678);
        UEDeviceView.mLargeResourceMap.append(UEColour.KORA_CITRUS.getCode(), 2130837681);
        UEDeviceView.mLargeResourceMap.append(UEColour.KORA_ORCHID.getCode(), 2130837684);
        UEDeviceView.mLargeResourceMap.append(UEColour.TITUS_BLACK_BLACK_BLUE.getCode(), 2130837737);
        UEDeviceView.mLargeResourceMap.append(UEColour.TITUS_BLUE_BLUE_RED.getCode(), 2130837740);
        UEDeviceView.mLargeResourceMap.append(UEColour.TITUS_RED_RED_WHITE.getCode(), 2130837743);
        UEDeviceView.mLargeResourceMap.append(UEColour.TITUS_PURPLE_PURPLE_BLUE.getCode(), 2130837746);
        UEDeviceView.mLargeResourceMap.append(UEColour.CARIBE_SOLID_CHARCOAL_BLACK.getCode(), 2130837587);
        UEDeviceView.mLargeResourceMap.append(UEColour.CARIBE_SOLID_BLUE_RED.getCode(), 2130837588);
        UEDeviceView.mLargeResourceMap.append(UEColour.CARIBE_SOLID_VIOLET_AQUA.getCode(), 2130837589);
        UEDeviceView.mLargeResourceMap.append(UEColour.CARIBE_SOLID_MEMPHIS_GREEN_GREY.getCode(), 2130837590);
        UEDeviceView.mLargeResourceMap.append(UEColour.CARIBE_ORANGE_PEACH.getCode(), 2130837591);
        UEDeviceView.mLargeResourceMap.append(UEColour.CARIBE_PINK_TEAL.getCode(), 2130837592);
        UEDeviceView.mLargeResourceMap.append(UEColour.MAXIMUS_BLACK_BLACK_WHITE.getCode(), 2130837687);
        UEDeviceView.mLargeResourceMap.append(UEColour.MAXIMUS_WHITE_WHITE_BLACK.getCode(), 2130837690);
        UEDeviceView.mLargeResourceMap.append(UEColour.MAXIMUS_TEAL_GREEN_YELLOW.getCode(), 2130837693);
        UEDeviceView.mLargeResourceMap.append(UEColour.MAXIMUS_VIOLET_ORANGE_YELLOW.getCode(), 2130837696);
        UEDeviceView.mLargeResourceMap.append(UEColour.MAXIMUS_RED_PINK_BLUE.getCode(), 2130837699);
        UEDeviceView.mLargeResourceMap.append(UEColour.MAXIMUS_BLUE_TEAL_RED.getCode(), 2130837702);
        UEDeviceView.mLargeResourceMap.append(UEColour.MAXIMUS_PURPLE_BLACK_WHITE.getCode(), 2130837705);
        (mSmallRecourseMap = new SparseIntArray()).append(UEColour.KORA_WHITE.getCode(), 2130837656);
        UEDeviceView.mSmallRecourseMap.append(UEColour.KORA_PINK.getCode(), 2130837659);
        UEDeviceView.mSmallRecourseMap.append(UEColour.KORA_RED.getCode(), 2130837662);
        UEDeviceView.mSmallRecourseMap.append(UEColour.KORA_BLUE.getCode(), 2130837665);
        UEDeviceView.mSmallRecourseMap.append(UEColour.KORA_BLACK.getCode(), 2130837668);
        UEDeviceView.mSmallRecourseMap.append(UEColour.KORA_MOSS.getCode(), 2130837671);
        UEDeviceView.mSmallRecourseMap.append(UEColour.KORA_BLUE_STEEL.getCode(), 2130837674);
        UEDeviceView.mSmallRecourseMap.append(UEColour.KORA_SUPER_HERO.getCode(), 2130837677);
        UEDeviceView.mSmallRecourseMap.append(UEColour.KORA_AQUA.getCode(), 2130837680);
        UEDeviceView.mSmallRecourseMap.append(UEColour.KORA_CITRUS.getCode(), 2130837683);
        UEDeviceView.mSmallRecourseMap.append(UEColour.KORA_ORCHID.getCode(), 2130837686);
        UEDeviceView.mSmallRecourseMap.append(UEColour.TITUS_BLACK_BLACK_BLUE.getCode(), 2130837739);
        UEDeviceView.mSmallRecourseMap.append(UEColour.TITUS_BLUE_BLUE_RED.getCode(), 2130837742);
        UEDeviceView.mSmallRecourseMap.append(UEColour.TITUS_RED_RED_WHITE.getCode(), 2130837745);
        UEDeviceView.mSmallRecourseMap.append(UEColour.TITUS_PURPLE_PURPLE_BLUE.getCode(), 2130837748);
        UEDeviceView.mSmallRecourseMap.append(UEColour.MAXIMUS_BLACK_BLACK_WHITE.getCode(), 2130837689);
        UEDeviceView.mSmallRecourseMap.append(UEColour.MAXIMUS_WHITE_WHITE_BLACK.getCode(), 2130837692);
        UEDeviceView.mSmallRecourseMap.append(UEColour.MAXIMUS_TEAL_GREEN_YELLOW.getCode(), 2130837695);
        UEDeviceView.mSmallRecourseMap.append(UEColour.MAXIMUS_VIOLET_ORANGE_YELLOW.getCode(), 2130837698);
        UEDeviceView.mSmallRecourseMap.append(UEColour.MAXIMUS_RED_PINK_BLUE.getCode(), 2130837701);
        UEDeviceView.mSmallRecourseMap.append(UEColour.MAXIMUS_BLUE_TEAL_RED.getCode(), 2130837704);
        UEDeviceView.mSmallRecourseMap.append(UEColour.MAXIMUS_PURPLE_BLACK_WHITE.getCode(), 2130837707);
        (mDotResourceMap = new SparseIntArray()).append(UEColour.KORA_WHITE.getCode(), 2130837655);
        UEDeviceView.mDotResourceMap.append(UEColour.KORA_PINK.getCode(), 2130837658);
        UEDeviceView.mDotResourceMap.append(UEColour.KORA_RED.getCode(), 2130837661);
        UEDeviceView.mDotResourceMap.append(UEColour.KORA_BLUE.getCode(), 2130837664);
        UEDeviceView.mDotResourceMap.append(UEColour.KORA_BLACK.getCode(), 2130837667);
        UEDeviceView.mDotResourceMap.append(UEColour.KORA_MOSS.getCode(), 2130837670);
        UEDeviceView.mDotResourceMap.append(UEColour.KORA_BLUE_STEEL.getCode(), 2130837673);
        UEDeviceView.mDotResourceMap.append(UEColour.KORA_SUPER_HERO.getCode(), 2130837676);
        UEDeviceView.mDotResourceMap.append(UEColour.KORA_AQUA.getCode(), 2130837679);
        UEDeviceView.mDotResourceMap.append(UEColour.KORA_CITRUS.getCode(), 2130837682);
        UEDeviceView.mDotResourceMap.append(UEColour.KORA_ORCHID.getCode(), 2130837685);
        UEDeviceView.mDotResourceMap.append(UEColour.TITUS_BLACK_BLACK_BLUE.getCode(), 2130837738);
        UEDeviceView.mDotResourceMap.append(UEColour.TITUS_BLUE_BLUE_RED.getCode(), 2130837741);
        UEDeviceView.mDotResourceMap.append(UEColour.TITUS_RED_RED_WHITE.getCode(), 2130837744);
        UEDeviceView.mDotResourceMap.append(UEColour.TITUS_PURPLE_PURPLE_BLUE.getCode(), 2130837747);
        UEDeviceView.mDotResourceMap.append(UEColour.MAXIMUS_BLACK_BLACK_WHITE.getCode(), 2130837688);
        UEDeviceView.mDotResourceMap.append(UEColour.MAXIMUS_WHITE_WHITE_BLACK.getCode(), 2130837691);
        UEDeviceView.mDotResourceMap.append(UEColour.MAXIMUS_TEAL_GREEN_YELLOW.getCode(), 2130837694);
        UEDeviceView.mDotResourceMap.append(UEColour.MAXIMUS_VIOLET_ORANGE_YELLOW.getCode(), 2130837697);
        UEDeviceView.mDotResourceMap.append(UEColour.MAXIMUS_RED_PINK_BLUE.getCode(), 2130837700);
        UEDeviceView.mDotResourceMap.append(UEColour.MAXIMUS_BLUE_TEAL_RED.getCode(), 2130837703);
        UEDeviceView.mDotResourceMap.append(UEColour.MAXIMUS_PURPLE_BLACK_WHITE.getCode(), 2130837706);
    }
    
    public UEDeviceView(final Context context) {
        this(context, null);
    }
    
    public UEDeviceView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public UEDeviceView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mColor = UEColour.UNKNOWN_COLOUR.getCode();
        this.mMaxHeight = 0.0f;
        this.mMaxWidth = 0.0f;
        this.mMode = DisplayMode.MODE_NORMAL;
        this.init(context, set);
    }
    
    public static Point calculateDevicesSize(int round, final float n, final float n2, final Context context) {
        final UEDeviceDimension deviceSize = getDeviceSize(UEDeviceType.Titus, context);
        UEDeviceDimension ueDeviceDimension;
        if (round != UEColour.UNKNOWN_COLOUR.getCode() && round != UEColour.NO_SPEAKER.getCode()) {
            ueDeviceDimension = getDeviceSize(UEColourHelper.getDeviceTypeByColour(round), context);
        }
        else {
            ueDeviceDimension = getDeviceSize(UEDeviceType.Unknown, context);
        }
        final float n3 = ueDeviceDimension.getWidth() / (float)ueDeviceDimension.getHeight();
        int n4 = Math.round(ueDeviceDimension.getHeight() / (float)deviceSize.getHeight() * n2);
        if ((round = Math.round(n4 * n3)) > n) {
            round = (int)n;
            n4 = Math.round(round / n3);
        }
        return new Point(round, n4);
    }
    
    public static int getDeviceDefaultResourceID(final UEDeviceType ueDeviceType, final DisplayMode displayMode) {
        int n = 0;
        switch (displayMode) {
            default: {
                n = 2130837731;
                break;
            }
            case MODE_DOT: {
                n = 2130837733;
                break;
            }
            case MODE_SMALL: {
                n = 2130837732;
                break;
            }
            case MODE_NORMAL: {
                n = 2130837731;
                if (ueDeviceType == UEDeviceType.Caribe) {
                    n = 2130837736;
                    break;
                }
                if (ueDeviceType == UEDeviceType.RedRocks) {
                    n = 2130837735;
                    break;
                }
                break;
            }
            case MODE_CUSTOM: {
                n = 2130837731;
                if (ueDeviceType == UEDeviceType.Caribe) {
                    n = 2130837736;
                    break;
                }
                if (ueDeviceType == UEDeviceType.RedRocks) {
                    n = 2130837735;
                    break;
                }
                break;
            }
        }
        return n;
    }
    
    public static Point getDeviceDimensions(final DisplayMode displayMode, final int n, final Context context) {
        final Point point = new Point();
        Point point2 = null;
        switch (displayMode) {
            default: {
                point2 = point;
                break;
            }
            case MODE_DOT: {
                point.x = context.getResources().getDimensionPixelSize(2131361876);
                point.y = context.getResources().getDimensionPixelSize(2131361875);
                point2 = point;
                break;
            }
            case MODE_NORMAL: {
                point2 = calculateDevicesSize(n, (float)context.getResources().getDimensionPixelSize(2131361903), (float)context.getResources().getDimensionPixelSize(2131361902), context);
                break;
            }
            case MODE_LARGE: {
                point2 = calculateDevicesSize(n, (float)context.getResources().getDimensionPixelSize(2131361896), (float)context.getResources().getDimensionPixelSize(2131361895), context);
                break;
            }
            case MODE_HUGE: {
                point2 = calculateDevicesSize(n, (float)context.getResources().getDimensionPixelSize(2131361887), (float)context.getResources().getDimensionPixelSize(2131361886), context);
                break;
            }
            case MODE_SMALL: {
                point2 = calculateDevicesSize(n, (float)context.getResources().getDimensionPixelSize(2131361913), (float)context.getResources().getDimensionPixelSize(2131361912), context);
                break;
            }
        }
        return point2;
    }
    
    private Drawable getDeviceFrontImage(final int n, final DisplayMode displayMode) {
        Drawable drawable = null;
        switch (displayMode) {
            default: {
                if (n == UEColour.UNKNOWN_COLOUR.getCode() || n == UEColour.NO_SPEAKER.getCode()) {
                    drawable = this.getDrawable(getDeviceDefaultResourceID(UEColourHelper.getDeviceTypeByColour(n), displayMode));
                    break;
                }
                if (getResourceID(n, this.mMode) != 0) {
                    drawable = this.getDrawable(getResourceID(n, this.mMode));
                    break;
                }
                final SVG svg = ContentManager.getInstance().getSVG(String.format("%02x", n & 0xFF), "UEPartyUp", new ContentLoadListener<SVG>() {
                    @Override
                    public void onLoadSuccess(final SVG svg, final String s, final String s2) {
                        if (Utils.isViewAttached((View)UEDeviceView.this)) {
                            UEDeviceView.this.post((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    UEDeviceView.this.refreshApperance();
                                }
                            });
                        }
                    }
                });
                if (svg == null) {
                    drawable = this.getDrawable(getDeviceDefaultResourceID(UEColourHelper.getDeviceTypeByColour(n), displayMode));
                    break;
                }
                if ((drawable = SVGRenderCacher.getDrawable(svg, new Rect(0, 0, this.getWidth(), this.getHeight()), (SVGRenderCacher.OnSVGReady)new SVGRenderCacher.OnSVGReady() {
                    @Override
                    public void onReady(final Drawable drawable) {
                        UEDeviceView.this.refreshApperance();
                    }
                })) == null) {
                    drawable = this.getDrawable(getDeviceDefaultResourceID(UEColourHelper.getDeviceTypeByColour(n), displayMode));
                    break;
                }
                break;
            }
            case MODE_DOT: {
                if (n == UEColour.UNKNOWN_COLOUR.getCode() || n == UEColour.NO_SPEAKER.getCode()) {
                    drawable = this.getDrawable(getDeviceDefaultResourceID(UEColourHelper.getDeviceTypeByColour(n), displayMode));
                    break;
                }
                if (getResourceID(n, this.mMode) != 0) {
                    drawable = this.getDrawable(getResourceID(n, this.mMode));
                    break;
                }
                final SVG svg2 = ContentManager.getInstance().getSVG(String.format("%02x", n & 0xFF), "UEPartyUpCircle", new ContentLoadListener<SVG>() {
                    @Override
                    public void onLoadSuccess(final SVG svg, final String s, final String s2) {
                        if (Utils.isViewAttached((View)UEDeviceView.this)) {
                            UEDeviceView.this.post((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    UEDeviceView.this.refreshApperance();
                                }
                            });
                        }
                    }
                });
                if (svg2 == null) {
                    drawable = this.getDrawable(getDeviceDefaultResourceID(UEColourHelper.getDeviceTypeByColour(n), displayMode));
                    break;
                }
                if ((drawable = SVGRenderCacher.getDrawable(svg2, new Rect(0, 0, this.getWidth(), this.getHeight()), (SVGRenderCacher.OnSVGReady)new SVGRenderCacher.OnSVGReady() {
                    @Override
                    public void onReady(final Drawable drawable) {
                        UEDeviceView.this.refreshApperance();
                    }
                })) == null) {
                    drawable = this.getDrawable(getDeviceDefaultResourceID(UEColourHelper.getDeviceTypeByColour(n), displayMode));
                    break;
                }
                break;
            }
            case MODE_SMALL: {
                if (n == UEColour.UNKNOWN_COLOUR.getCode() || n == UEColour.NO_SPEAKER.getCode()) {
                    drawable = this.getDrawable(getDeviceDefaultResourceID(UEColourHelper.getDeviceTypeByColour(n), displayMode));
                    break;
                }
                if (getResourceID(n, this.mMode) != 0) {
                    drawable = this.getDrawable(getResourceID(n, this.mMode));
                    break;
                }
                final SVG svg3 = ContentManager.getInstance().getSVG(String.format("%02x", n & 0xFF), "UEPartyUpDrawer", new ContentLoadListener<SVG>() {
                    @Override
                    public void onLoadSuccess(final SVG svg, final String s, final String s2) {
                        if (Utils.isViewAttached((View)UEDeviceView.this)) {
                            UEDeviceView.this.post((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    UEDeviceView.this.refreshApperance();
                                }
                            });
                        }
                    }
                });
                if (svg3 == null) {
                    drawable = this.getDrawable(getDeviceDefaultResourceID(UEColourHelper.getDeviceTypeByColour(n), displayMode));
                    break;
                }
                if ((drawable = SVGRenderCacher.getDrawable(svg3, new Rect(0, 0, this.getWidth(), this.getHeight()), (SVGRenderCacher.OnSVGReady)new SVGRenderCacher.OnSVGReady() {
                    @Override
                    public void onReady(final Drawable drawable) {
                        UEDeviceView.this.refreshApperance();
                    }
                })) == null) {
                    drawable = this.getDrawable(getDeviceDefaultResourceID(UEColourHelper.getDeviceTypeByColour(n), displayMode));
                    break;
                }
                break;
            }
        }
        return drawable;
    }
    
    public static Drawable getDeviceFrontImage(final Context context, final int n, final DisplayMode displayMode) {
        Object o = null;
        switch (displayMode) {
            default: {
                if (n == UEColour.UNKNOWN_COLOUR.getCode() || n == UEColour.NO_SPEAKER.getCode()) {
                    o = getDrawable(context, getDeviceDefaultResourceID(UEColourHelper.getDeviceTypeByColour(n), displayMode));
                    break;
                }
                if (getResourceID(n, displayMode) != 0) {
                    o = getDrawable(context, getResourceID(n, displayMode));
                    break;
                }
                final SVG svg = ContentManager.getInstance().getSVG(String.format("%02x", n & 0xFF), "UEPartyUp", null);
                if (svg != null) {
                    o = new BitmapDrawable(context.getResources(), Utils.drawSVGToBitmap(svg));
                    break;
                }
                o = getDrawable(context, getDeviceDefaultResourceID(UEColourHelper.getDeviceTypeByColour(n), displayMode));
                break;
            }
            case MODE_DOT: {
                if (n == UEColour.UNKNOWN_COLOUR.getCode() || n == UEColour.NO_SPEAKER.getCode()) {
                    o = getDrawable(context, getDeviceDefaultResourceID(UEColourHelper.getDeviceTypeByColour(n), displayMode));
                    break;
                }
                if (getResourceID(n, displayMode) != 0) {
                    o = getDrawable(context, getResourceID(n, displayMode));
                    break;
                }
                final SVG svg2 = ContentManager.getInstance().getSVG(String.format("%02x", n & 0xFF), "UEPartyUpCircle", null);
                if (svg2 != null) {
                    o = new BitmapDrawable(context.getResources(), Utils.drawSVGToBitmap(svg2));
                    break;
                }
                o = getDrawable(context, getDeviceDefaultResourceID(UEColourHelper.getDeviceTypeByColour(n), displayMode));
                break;
            }
            case MODE_SMALL: {
                if (n == UEColour.UNKNOWN_COLOUR.getCode() || n == UEColour.NO_SPEAKER.getCode()) {
                    o = getDrawable(context, getDeviceDefaultResourceID(UEColourHelper.getDeviceTypeByColour(n), displayMode));
                    break;
                }
                if (getResourceID(n, displayMode) != 0) {
                    o = getDrawable(context, getResourceID(n, displayMode));
                    break;
                }
                final SVG svg3 = ContentManager.getInstance().getSVG(String.format("%02x", n & 0xFF), "UEPartyUpDrawer", null);
                if (svg3 != null) {
                    o = new BitmapDrawable(context.getResources(), Utils.drawSVGToBitmap(svg3));
                    break;
                }
                o = getDrawable(context, getDeviceDefaultResourceID(UEColourHelper.getDeviceTypeByColour(n), displayMode));
                break;
            }
        }
        return (Drawable)o;
    }
    
    public static Point getDeviceMaxDimensions(final DisplayMode displayMode, final Context context) {
        final Point point = new Point();
        switch (displayMode) {
            case MODE_DOT: {
                point.x = context.getResources().getDimensionPixelSize(2131361876);
                point.y = context.getResources().getDimensionPixelSize(2131361875);
                break;
            }
            case MODE_NORMAL: {
                point.x = context.getResources().getDimensionPixelSize(2131361903);
                point.y = context.getResources().getDimensionPixelSize(2131361902);
                break;
            }
            case MODE_LARGE: {
                point.x = context.getResources().getDimensionPixelSize(2131361896);
                point.y = context.getResources().getDimensionPixelSize(2131361895);
                break;
            }
            case MODE_HUGE: {
                point.x = context.getResources().getDimensionPixelSize(2131361887);
                point.y = context.getResources().getDimensionPixelSize(2131361886);
                break;
            }
            case MODE_SMALL: {
                point.x = context.getResources().getDimensionPixelSize(2131361913);
                point.y = context.getResources().getDimensionPixelSize(2131361912);
                break;
            }
        }
        return point;
    }
    
    public static UEDeviceDimension getDeviceSize(final UEDeviceType ueDeviceType, final Context context) {
        UEDeviceDimension ueDeviceDimension = null;
        switch (ueDeviceType) {
            default: {
                if ("orpheum".equals("orpheum")) {
                    ueDeviceDimension = new UEDeviceDimension(ueDeviceType, context.getResources().getInteger(2131492871), context.getResources().getInteger(2131492870));
                    break;
                }
                if ("orpheum".equals("shoreline")) {
                    ueDeviceDimension = new UEDeviceDimension(ueDeviceType, context.getResources().getInteger(2131492876), context.getResources().getInteger(2131492875));
                    break;
                }
                if ("orpheum".equals("voldemort")) {
                    ueDeviceDimension = new UEDeviceDimension(ueDeviceType, context.getResources().getInteger(2131492869), context.getResources().getInteger(2131492868));
                    break;
                }
                ueDeviceDimension = new UEDeviceDimension(ueDeviceType, context.getResources().getInteger(2131492871), context.getResources().getInteger(2131492870));
                break;
            }
            case Titus: {
                ueDeviceDimension = new UEDeviceDimension(ueDeviceType, context.getResources().getInteger(2131492876), context.getResources().getInteger(2131492875));
                break;
            }
            case Maximus:
            case Kora: {
                ueDeviceDimension = new UEDeviceDimension(ueDeviceType, context.getResources().getInteger(2131492871), context.getResources().getInteger(2131492870));
                break;
            }
            case Caribe: {
                ueDeviceDimension = new UEDeviceDimension(ueDeviceType, context.getResources().getInteger(2131492869), context.getResources().getInteger(2131492868));
                break;
            }
            case RedRocks: {
                ueDeviceDimension = new UEDeviceDimension(ueDeviceType, context.getResources().getInteger(2131492873), context.getResources().getInteger(2131492872));
                break;
            }
        }
        return ueDeviceDimension;
    }
    
    private Drawable getDrawable(final int n) {
        return getDrawable(this.getContext(), n);
    }
    
    private static Drawable getDrawable(final Context context, final int n) {
        return ContextCompat.getDrawable(context, n);
    }
    
    public static int getResourceID(int n, final DisplayMode displayMode) {
        if (displayMode == DisplayMode.MODE_SMALL) {
            n = UEDeviceView.mSmallRecourseMap.get(n, 0);
        }
        else if (displayMode == DisplayMode.MODE_DOT) {
            n = UEDeviceView.mDotResourceMap.get(n, 0);
        }
        else {
            n = UEDeviceView.mLargeResourceMap.get(n, 0);
        }
        return n;
    }
    
    private void init(final Context context, final AttributeSet set) {
        ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(2130968674, (ViewGroup)this, true);
        this.mFrontImageView = ButterKnife.findById((View)this, 2131624244);
        this.mBackImageView = ButterKnife.findById((View)this, 2131624243);
        this.mLoadingSpinner = ButterKnife.findById((View)this, 2131624245);
        final TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(set, R.styleable.UEDeviceView, 0, 0);
        this.setMode(DisplayMode.values()[obtainStyledAttributes.getInt(0, 1)]);
        this.setDeviceColor(obtainStyledAttributes.getInt(3, UEColour.NO_SPEAKER.getCode()));
        obtainStyledAttributes.recycle();
        if (!this.isInEditMode()) {
            this.setBackgroundImage(this.getDeviceBackgroundImage());
        }
    }
    
    private void setDeviceMaxDimensions() {
        switch (this.mMode) {
            default: {
                final TypedArray obtainStyledAttributes = this.getContext().getTheme().obtainStyledAttributes((AttributeSet)null, R.styleable.UEDeviceView, 0, 0);
                this.mMaxWidth = obtainStyledAttributes.getDimension(2, (float)this.getResources().getDimensionPixelSize(2131361903));
                this.mMaxHeight = obtainStyledAttributes.getDimension(1, (float)this.getResources().getDimensionPixelSize(2131361902));
                obtainStyledAttributes.recycle();
                break;
            }
            case MODE_DOT: {
                this.mMaxWidth = (float)this.getResources().getDimensionPixelSize(2131361876);
                this.mMaxHeight = (float)this.getResources().getDimensionPixelSize(2131361875);
                break;
            }
            case MODE_NORMAL: {
                this.mMaxWidth = (float)this.getResources().getDimensionPixelSize(2131361903);
                this.mMaxHeight = (float)this.getResources().getDimensionPixelSize(2131361902);
                break;
            }
            case MODE_LARGE: {
                this.mMaxWidth = (float)this.getResources().getDimensionPixelSize(2131361896);
                this.mMaxHeight = (float)this.getResources().getDimensionPixelSize(2131361895);
                break;
            }
            case MODE_HUGE: {
                this.mMaxWidth = (float)this.getResources().getDimensionPixelSize(2131361887);
                this.mMaxHeight = (float)this.getResources().getDimensionPixelSize(2131361886);
                break;
            }
            case MODE_SMALL: {
                this.mMaxWidth = (float)this.getResources().getDimensionPixelSize(2131361913);
                this.mMaxHeight = (float)this.getResources().getDimensionPixelSize(2131361912);
                break;
            }
            case MODE_CUSTOM: {
                final TypedArray obtainStyledAttributes2 = this.getContext().getTheme().obtainStyledAttributes((AttributeSet)null, R.styleable.UEDeviceView, 0, 0);
                this.mMaxWidth = obtainStyledAttributes2.getDimension(2, (float)this.getResources().getDimensionPixelSize(2131361866));
                this.mMaxHeight = obtainStyledAttributes2.getDimension(1, (float)this.getResources().getDimensionPixelSize(2131361865));
                obtainStyledAttributes2.recycle();
                break;
            }
        }
    }
    
    public void fillInDevice(final int n) {
        this.setFrontImage(this.getDeviceFrontImage(n));
        this.stopAnimator();
        final AnimatorSet mAnimator = new AnimatorSet();
        mAnimator.play((Animator)ObjectAnimator.ofFloat((Object)this.mFrontImageView, View.ALPHA, new float[] { 1.0f }).setDuration((long)Math.round((1.0f - this.mFrontImageView.getAlpha()) * 600.0f))).with((Animator)ObjectAnimator.ofFloat((Object)this.mBackImageView, View.ALPHA, new float[] { 0.0f }).setDuration((long)Math.round(this.mBackImageView.getAlpha() * 600.0f)));
        (this.mAnimator = (Animator)mAnimator).start();
    }
    
    public void fillOutDevice() {
        this.stopAnimator();
        final AnimatorSet mAnimator = new AnimatorSet();
        mAnimator.play((Animator)ObjectAnimator.ofFloat((Object)this.mFrontImageView, View.ALPHA, new float[] { 0.0f }).setDuration((long)Math.round(this.mFrontImageView.getAlpha() * 600.0f))).with((Animator)ObjectAnimator.ofFloat((Object)this.mBackImageView, View.ALPHA, new float[] { 1.0f }).setDuration((long)Math.round((1.0f - this.mBackImageView.getAlpha()) * 600.0f)));
        (this.mAnimator = (Animator)mAnimator).start();
    }
    
    protected Drawable getDeviceBackgroundImage() {
        return this.getDeviceBackgroundImage(this.mColor, this.mMode);
    }
    
    public Drawable getDeviceBackgroundImage(final int n, final DisplayMode displayMode) {
        return this.getDrawable(getDeviceDefaultResourceID(UEColourHelper.getDeviceTypeByColour(n), displayMode));
    }
    
    public int getDeviceColor() {
        return this.mColor;
    }
    
    protected Drawable getDeviceFrontImage(final int n) {
        Drawable deviceFrontImage;
        if (this.isInEditMode()) {
            deviceFrontImage = null;
        }
        else {
            deviceFrontImage = this.getDeviceFrontImage(n, this.mMode);
        }
        return deviceFrontImage;
    }
    
    public float getMaxHeight() {
        return this.mMaxHeight;
    }
    
    public float getMaxWidth() {
        return this.mMaxWidth;
    }
    
    public DisplayMode getMode() {
        return this.mMode;
    }
    
    protected void onMeasure(int n, final int n2) {
        super.onMeasure(n, n2);
        Point point;
        if (this.mMode == DisplayMode.MODE_DOT) {
            point = getDeviceMaxDimensions(this.mMode, this.getContext());
        }
        else {
            point = calculateDevicesSize(this.mColor, this.mMaxWidth, this.mMaxHeight, this.getContext());
        }
        this.mFrontImageView.measure(View$MeasureSpec.makeMeasureSpec(point.x, 1073741824), View$MeasureSpec.makeMeasureSpec(point.y, 1073741824));
        this.mBackImageView.measure(View$MeasureSpec.makeMeasureSpec(point.x, 1073741824), View$MeasureSpec.makeMeasureSpec(point.y, 1073741824));
        if (this.mMode == DisplayMode.MODE_DOT) {
            n = this.getResources().getDimensionPixelSize(2131361916);
        }
        else {
            n = this.getResources().getDimensionPixelSize(2131361915);
        }
        this.mLoadingSpinner.measure(View$MeasureSpec.makeMeasureSpec(n, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(n, Integer.MIN_VALUE));
        this.setMeasuredDimension(point.x, point.y);
    }
    
    public void refreshApperance() {
        this.setBackgroundImage(this.getDeviceBackgroundImage());
        this.setFrontImage(this.getDeviceFrontImage(this.mColor));
    }
    
    public void setBackgroundImage(final Drawable imageDrawable) {
        this.mBackImageView.setImageDrawable(imageDrawable);
    }
    
    public void setDeviceColor(final int n) {
        this.setDeviceColor(n, false);
    }
    
    public void setDeviceColor(final int mColor, final boolean b) {
        if (b) {
            if (mColor != this.mColor) {
                this.updateImageWithAnimation(mColor);
            }
        }
        else {
            this.updateImageNoAnimation(mColor);
        }
        this.mColor = mColor;
        this.setBackgroundImage(this.getDeviceBackgroundImage());
        this.requestLayout();
    }
    
    public void setFrontImage(final Drawable imageDrawable) {
        this.mFrontImageView.setImageDrawable(imageDrawable);
    }
    
    public void setIsLoading(final boolean b) {
        if (b) {
            this.mLoadingSpinner.setVisibility(0);
            if (this.mColor != UEColour.NO_SPEAKER.getCode()) {
                this.mFrontImageView.setAlpha(0.5f);
                this.mBackImageView.setAlpha(0.0f);
            }
            else {
                this.mFrontImageView.setAlpha(0.0f);
                this.mBackImageView.setAlpha(0.5f);
            }
        }
        else {
            this.mLoadingSpinner.setVisibility(4);
            if (this.mColor != UEColour.NO_SPEAKER.getCode()) {
                this.mFrontImageView.setAlpha(1.0f);
                this.mBackImageView.setAlpha(0.0f);
            }
            else {
                this.mFrontImageView.setAlpha(0.0f);
                this.mBackImageView.setAlpha(1.0f);
            }
        }
    }
    
    public void setMaxHeight(final float mMaxHeight) {
        this.mMaxHeight = mMaxHeight;
    }
    
    public void setMaxWidth(final float mMaxWidth) {
        this.mMaxWidth = mMaxWidth;
    }
    
    public void setMode(final DisplayMode mMode) {
        this.mMode = mMode;
        this.setDeviceMaxDimensions();
        this.refreshApperance();
        this.requestLayout();
        if (this.getParent() != null) {
            this.getParent().requestLayout();
        }
    }
    
    protected void stopAnimator() {
        if (this.mAnimator != null && this.mAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
    }
    
    public void updateImageNoAnimation(final int n) {
        if (n != UEColour.NO_SPEAKER.getCode()) {
            this.setFrontImage(this.getDeviceFrontImage(n));
            this.mFrontImageView.setAlpha(1.0f);
            this.mBackImageView.setAlpha(0.0f);
        }
        else {
            this.mFrontImageView.setAlpha(0.0f);
            this.mBackImageView.setAlpha(1.0f);
        }
    }
    
    public void updateImageWithAnimation(final int n) {
        if (n >= 0 && n != 255) {
            this.fillInDevice(n);
        }
        else {
            this.fillOutDevice();
        }
    }
    
    public enum DisplayMode
    {
        MODE_CUSTOM, 
        MODE_DOT, 
        MODE_HUGE, 
        MODE_LARGE, 
        MODE_NORMAL, 
        MODE_SMALL;
    }
}
