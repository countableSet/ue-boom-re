// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.views;

import java.util.Random;
import android.graphics.RectF;
import android.content.res.TypedArray;
import com.logitech.ue.R;
import butterknife.ButterKnife;
import android.util.AttributeSet;
import android.content.Context;
import android.util.SparseArray;
import android.graphics.PointF;
import android.view.View;
import butterknife.BindDimen;
import android.widget.FrameLayout;

public class SpiderFrameLayout extends FrameLayout
{
    public static final int DOUBLE_MODE_LEFT_SPOT = 0;
    public static final int DOUBLE_MODE_RIGHT_SPOT = 1;
    public static final int DOUBLE_UP_SPOT_COUNT = 2;
    public static final int INVALID_SPOT_INDEX = -1;
    public static final int MASTER_DEFAULT_SPOT = 0;
    public static final int MEGA_UP_SPOT_COUNT = 50;
    public static final int MONSTER_UP_SPOT_COUNT = 200;
    public static final int SOLO_SPOT_COUNT = 1;
    private static final String TAG;
    DisplayMode mDisplayMode;
    @BindDimen(2131361873)
    float mDotSpeakerHeight;
    @BindDimen(2131361874)
    float mDotSpeakerWidth;
    @BindDimen(2131361867)
    float mDoubleUpDeviceMargin;
    View mMasterView;
    @BindDimen(2131361902)
    float mNormalSpeakerHeight;
    @BindDimen(2131361903)
    float mNormalSpeakerWidth;
    PointF[] mPositionsCache;
    SparseArray<View> mSpotReserveSet;
    
    static {
        TAG = SpiderFrameLayout.class.getSimpleName();
    }
    
    public SpiderFrameLayout(final Context context) {
        this(context, null);
    }
    
    public SpiderFrameLayout(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public SpiderFrameLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mDisplayMode = DisplayMode.Solo;
        this.mSpotReserveSet = (SparseArray<View>)new SparseArray();
        this.mPositionsCache = null;
        this.initAttributes(context, set);
        ButterKnife.bind(this, (View)this);
    }
    
    private void initAttributes(final Context context, final AttributeSet set) {
        if (set != null) {
            final TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(set, R.styleable.SpiderFrameLayout, 0, 0);
            this.setDisplayMode(DisplayMode.values()[obtainStyledAttributes.getInt(0, 0)]);
            obtainStyledAttributes.recycle();
        }
    }
    
    PointF[] calculateMegaUPSpotsPositions() {
        return this.calculateMegaUPSpotsPositions(this.getNumberOfSpots(), new RectF(this.mDotSpeakerWidth / 2.0f, this.mDotSpeakerHeight + this.mDotSpeakerHeight / 2.0f, this.getWidth() - this.mDotSpeakerWidth, this.getHeight() - this.mDotSpeakerHeight * 2.0f), new RectF((this.getWidth() - this.mNormalSpeakerWidth - this.mDotSpeakerWidth) / 2.0f, (this.getHeight() - this.mNormalSpeakerHeight - this.mDotSpeakerHeight) / 2.0f, (this.getWidth() + this.mNormalSpeakerWidth + this.mDotSpeakerWidth) / 2.0f, (this.getHeight() + this.mNormalSpeakerHeight + this.mDotSpeakerHeight) / 2.0f), this.mDotSpeakerWidth * 2.0f);
    }
    
    PointF[] calculateMegaUPSpotsPositions(final int n, final RectF rectF, final RectF rectF2, final float n2) {
        final PointF[] array = new PointF[n];
        final Random random = new Random();
        array[0] = new PointF(rectF.centerX(), rectF.centerY());
        final float n3 = n2 * n2 / 4.0f;
        for (int i = 1; i < n; ++i) {
            int j = 0;
            int n4 = 0;
        Label_0059:
            while (j == 0) {
                array[i] = new PointF(rectF.left + random.nextFloat() * rectF.width(), rectF.top + random.nextFloat() * rectF.height());
                int n5;
                if (!rectF2.contains(array[i].x, array[i].y)) {
                    n5 = 1;
                }
                else {
                    n5 = 0;
                }
                j = n5;
                if (n5 != 0) {
                    if (n4 < 10) {
                        int n6 = 1;
                        while (true) {
                            j = n5;
                            if (n6 >= i) {
                                break;
                            }
                            if ((array[n6].x - array[i].x) * (array[n6].x - array[i].x) + (array[n6].y - array[i].y) * (array[n6].y - array[i].y) < n2 * n2) {
                                j = 0;
                                break;
                            }
                            ++n6;
                        }
                        ++n4;
                    }
                    else {
                        int n7 = 1;
                        while (true) {
                            j = n5;
                            if (n7 >= i) {
                                continue Label_0059;
                            }
                            if ((array[n7].x - array[i].x) * (array[n7].x - array[i].x) + (array[n7].y - array[i].y) * (array[n7].y - array[i].y) < n3) {
                                break;
                            }
                            ++n7;
                        }
                        j = 0;
                    }
                }
            }
        }
        return array;
    }
    
    PointF[] calculateMonsterUPSpotsPositions() {
        return this.calculateMonsterUPSpotsPositions(this.getNumberOfSpots(), new RectF(this.mDotSpeakerWidth / 2.0f, this.mDotSpeakerHeight + this.mDotSpeakerHeight / 2.0f, this.getWidth() - this.mDotSpeakerWidth, this.getHeight() - this.mDotSpeakerHeight * 2.0f), new RectF((this.getWidth() - this.mNormalSpeakerWidth - this.mDotSpeakerWidth) / 2.0f, (this.getHeight() - this.mNormalSpeakerHeight - this.mDotSpeakerHeight) / 2.0f, (this.getWidth() + this.mNormalSpeakerWidth + this.mDotSpeakerWidth) / 2.0f, (this.getHeight() + this.mNormalSpeakerHeight + this.mDotSpeakerHeight) / 2.0f));
    }
    
    PointF[] calculateMonsterUPSpotsPositions(final int n, final RectF rectF, final RectF rectF2) {
        final PointF[] array = new PointF[n];
        final Random random = new Random();
        if (this.mPositionsCache.length == 50) {
            System.arraycopy(this.mPositionsCache, 0, array, 0, 50);
        }
        else {
            System.arraycopy(this.calculateMegaUPSpotsPositions(50, rectF, rectF2, 2.0f * this.mDotSpeakerWidth), 0, array, 0, 50);
        }
        array[0] = new PointF(rectF.centerX(), rectF.centerY());
        for (int i = 50; i < n; ++i) {
            int j = 0;
            while (j == 0) {
                array[i] = new PointF(rectF.left + random.nextFloat() * rectF.width(), rectF.top + random.nextFloat() * rectF.height());
                if (!rectF2.contains(array[i].x, array[i].y)) {
                    j = 1;
                }
                else {
                    j = 0;
                }
            }
        }
        return array;
    }
    
    public int[] getAllFreeSpots() {
        final int[] array = new int[this.getNumberOfSpots() - this.mSpotReserveSet.size()];
        int n = 0;
        int n2;
        for (int i = 0; i < this.getNumberOfSpots(); ++i, n = n2) {
            n2 = n;
            if (this.mSpotReserveSet.get(i) == null) {
                array[n] = i;
                n2 = n + 1;
            }
        }
        return array;
    }
    
    public int getClosestFreeSpot(final float n, final float n2) {
        return this.getClosestSpot(n, n2, this.getAllFreeSpots());
    }
    
    public int getClosestFreeSpot(final float n, final float n2, final int... array) {
        int n3 = -1;
        PointF pointF = null;
        PointF spotPosition;
        int n5;
        for (int i = 0; i < array.length; ++i, pointF = spotPosition, n3 = n5) {
            final int n4 = array[i];
            spotPosition = pointF;
            n5 = n3;
            if (this.mSpotReserveSet.get(n4) == null) {
                if (n3 == -1) {
                    n5 = n4;
                    spotPosition = this.getSpotPosition(n4);
                }
                else {
                    final PointF spotPosition2 = this.getSpotPosition(n4);
                    spotPosition = pointF;
                    n5 = n3;
                    if (Math.pow(n - spotPosition2.x, 2.0) + Math.pow(n2 - spotPosition2.y, 2.0) < Math.pow(n - pointF.x, 2.0) + Math.pow(n2 - pointF.y, 2.0)) {
                        n5 = n4;
                        spotPosition = spotPosition2;
                    }
                }
            }
        }
        return n3;
    }
    
    public int getClosestSpot(final float n, final float n2) {
        final int[] array = new int[this.getNumberOfSpots()];
        for (int i = 0; i < this.getNumberOfSpots(); ++i) {
            array[i] = i;
        }
        return this.getClosestSpot(n, n2, array);
    }
    
    public int getClosestSpot(final float n, final float n2, final int... array) {
        int n3;
        if (array.length == 0) {
            n3 = -1;
        }
        else {
            int n4 = array[0];
            final PointF spotPosition = this.getSpotPosition(n4);
            float n5 = (float)(Math.pow(spotPosition.x - n, 2.0) + Math.pow(spotPosition.y - n2, 2.0));
            int n6 = 1;
            while (true) {
                n3 = n4;
                if (n6 >= array.length) {
                    break;
                }
                final PointF spotPosition2 = this.getSpotPosition(array[n6]);
                final float n7 = (float)(Math.pow(spotPosition2.x - n, 2.0) + Math.pow(spotPosition2.y - n2, 2.0));
                float n8 = n5;
                if (n7 < n5) {
                    n4 = array[n6];
                    n8 = n7;
                }
                ++n6;
                n5 = n8;
            }
        }
        return n3;
    }
    
    public DisplayMode getDisplayMode() {
        return this.mDisplayMode;
    }
    
    public int getMasterSpot() {
        int n;
        if (this.mDisplayMode == DisplayMode.DoubleUP) {
            n = -1;
        }
        else {
            n = 0;
        }
        return n;
    }
    
    public int getNumberOfSpots() {
        int n = 0;
        switch (this.mDisplayMode) {
            default: {
                n = 0;
                break;
            }
            case Solo: {
                n = 1;
                break;
            }
            case DoubleUP: {
                n = 2;
                break;
            }
            case MegaUP: {
                n = 50;
                break;
            }
            case MonsterUP: {
                n = 200;
                break;
            }
        }
        return n;
    }
    
    public View getSpiderManView() {
        return this.mMasterView;
    }
    
    public int getSpot(final View view) {
        for (int i = 0; i < this.mSpotReserveSet.size(); ++i) {
            final int key = this.mSpotReserveSet.keyAt(i);
            if (view == this.mSpotReserveSet.get(key)) {
                return key;
            }
        }
        return -1;
    }
    
    public PointF getSpotPosition(final int n) {
        PointF pointF = new PointF();
        final float x = (float)(this.getWidth() / 2);
        final float y = (float)(this.getHeight() / 2);
        switch (this.mDisplayMode) {
            case Solo: {
                pointF.x = x;
                pointF.y = y;
                break;
            }
            case DoubleUP: {
                if (n == 0) {
                    pointF.x = x - this.mDoubleUpDeviceMargin;
                    pointF.y = (float)(this.getHeight() / 2);
                    break;
                }
                pointF.x = this.mDoubleUpDeviceMargin + y;
                pointF.y = (float)(this.getHeight() / 2);
                break;
            }
            case MegaUP: {
                pointF = new PointF(this.mPositionsCache[n].x, this.mPositionsCache[n].y);
                break;
            }
            case MonsterUP: {
                pointF = new PointF(this.mPositionsCache[n].x, this.mPositionsCache[n].y);
                break;
            }
        }
        return pointF;
    }
    
    public PointF getSpotPosition(final int n, final int n2, final int n3) {
        PointF spotPosition = new PointF();
        switch (this.mDisplayMode) {
            default: {
                spotPosition = this.getSpotPosition(n);
                spotPosition.x -= n2 / 2;
                spotPosition.y -= n3 / 2;
                break;
            }
            case Solo: {
                final float n4 = (float)(this.getWidth() / 2);
                final float n5 = (float)(this.getHeight() / 2);
                final float n6 = (float)this.getResources().getDimensionPixelSize(2131361903);
                final float n7 = (this.getHeight() + (float)this.getResources().getDimensionPixelSize(2131361902)) / 2.0f;
                spotPosition.x = n4 - n2 / 2;
                spotPosition.y = n5 - n3 / 2;
                break;
            }
            case DoubleUP: {
                final float n8 = (float)(this.getWidth() / 2);
                final float n9 = (float)(this.getHeight() / 2);
                final float n10 = (float)this.getResources().getDimensionPixelSize(2131361903);
                final float n11 = (this.getHeight() + (float)this.getResources().getDimensionPixelSize(2131361902)) / 2.0f;
                if (n == 0) {
                    spotPosition.x = n8 - this.mDoubleUpDeviceMargin - n2 / 2;
                    spotPosition.y = n9 - n3 / 2;
                    break;
                }
                spotPosition.x = this.mDoubleUpDeviceMargin + n8 - n2 / 2;
                spotPosition.y = n9 - n3 / 2;
                break;
            }
        }
        return spotPosition;
    }
    
    public PointF getSpotPosition(final int n, final View view) {
        return this.getSpotPosition(n, view.getWidth(), view.getHeight());
    }
    
    public View getViewBySpot(final int n) {
        return (View)this.mSpotReserveSet.get(n);
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (this.mDisplayMode == DisplayMode.MegaUP) {
            this.mPositionsCache = this.calculateMegaUPSpotsPositions();
        }
        else if (this.mDisplayMode == DisplayMode.MonsterUP) {
            this.mPositionsCache = this.calculateMonsterUPSpotsPositions();
        }
    }
    
    public void releaseSpot(final int n) {
        this.mSpotReserveSet.remove(n);
    }
    
    public void releaseSpot(final View view) {
        final int spot = this.getSpot(view);
        if (spot != -1) {
            this.mSpotReserveSet.remove(spot);
        }
    }
    
    public void removeView(final View view) {
        final int spot = this.getSpot(view);
        if (spot != -1) {
            this.releaseSpot(spot);
        }
        super.removeView(view);
    }
    
    public void reserveSpot(final int n, final View view) {
        this.mSpotReserveSet.put(n, (Object)view);
    }
    
    public void setDisplayMode(final DisplayMode mDisplayMode) {
        if (this.mDisplayMode != mDisplayMode) {
            this.mDisplayMode = mDisplayMode;
            this.mSpotReserveSet.clear();
            if (this.mDisplayMode == DisplayMode.MegaUP) {
                this.mPositionsCache = this.calculateMegaUPSpotsPositions();
            }
            else if (this.mDisplayMode == DisplayMode.MonsterUP) {
                this.mPositionsCache = this.calculateMonsterUPSpotsPositions();
            }
        }
    }
    
    public void setMasterView(final View mMasterView) {
        this.mMasterView = mMasterView;
        this.invalidate();
    }
    
    public enum DisplayMode
    {
        DoubleUP, 
        MegaUP, 
        MonsterUP, 
        Solo;
    }
}
