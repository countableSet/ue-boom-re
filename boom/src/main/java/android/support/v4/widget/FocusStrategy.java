// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.widget;

import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.graphics.Rect;

class FocusStrategy
{
    private static boolean beamBeats(final int n, @NonNull final Rect rect, @NonNull final Rect rect2, @NonNull final Rect rect3) {
        final boolean b = true;
        final boolean beamsOverlap = beamsOverlap(n, rect, rect2);
        boolean b2;
        if (beamsOverlap(n, rect, rect3) || !beamsOverlap) {
            b2 = false;
        }
        else {
            b2 = b;
            if (isToDirectionOf(n, rect, rect3)) {
                b2 = b;
                if (n != 17) {
                    b2 = b;
                    if (n != 66) {
                        b2 = b;
                        if (majorAxisDistance(n, rect, rect2) >= majorAxisDistanceToFarEdge(n, rect, rect3)) {
                            b2 = false;
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    private static boolean beamsOverlap(final int n, @NonNull final Rect rect, @NonNull final Rect rect2) {
        boolean b = true;
        switch (n) {
            default: {
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            case 17:
            case 66: {
                if (rect2.bottom >= rect.top && rect2.top <= rect.bottom) {
                    break;
                }
                b = false;
                break;
            }
            case 33:
            case 130: {
                if (rect2.right < rect.left || rect2.left > rect.right) {
                    b = false;
                    break;
                }
                break;
            }
        }
        return b;
    }
    
    public static <L, T> T findNextFocusInAbsoluteDirection(@NonNull final L l, @NonNull final CollectionAdapter<L, T> collectionAdapter, @NonNull final BoundsAdapter<T> boundsAdapter, @Nullable final T t, @NonNull final Rect rect, final int n) {
        final Rect rect2 = new Rect(rect);
        switch (n) {
            default: {
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            case 17: {
                rect2.offset(rect.width() + 1, 0);
                break;
            }
            case 66: {
                rect2.offset(-(rect.width() + 1), 0);
                break;
            }
            case 33: {
                rect2.offset(0, rect.height() + 1);
                break;
            }
            case 130: {
                rect2.offset(0, -(rect.height() + 1));
                break;
            }
        }
        T t2 = null;
        final int size = collectionAdapter.size(l);
        final Rect rect3 = new Rect();
        for (int i = 0; i < size; ++i) {
            final T value = collectionAdapter.get(l, i);
            if (value != t) {
                boundsAdapter.obtainBounds(value, rect3);
                if (isBetterCandidate(n, rect, rect3, rect2)) {
                    rect2.set(rect3);
                    t2 = value;
                }
            }
        }
        return t2;
    }
    
    public static <L, T> T findNextFocusInRelativeDirection(@NonNull final L l, @NonNull final CollectionAdapter<L, T> collectionAdapter, @NonNull final BoundsAdapter<T> boundsAdapter, @Nullable final T t, final int n, final boolean b, final boolean b2) {
        final int size = collectionAdapter.size(l);
        final ArrayList list = new ArrayList<T>(size);
        for (int i = 0; i < size; ++i) {
            list.add(collectionAdapter.get(l, i));
        }
        Collections.sort((List<T>)list, new SequentialComparator<Object>(b, (BoundsAdapter<? super T>)boundsAdapter));
        T t2 = null;
        switch (n) {
            default: {
                throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD}.");
            }
            case 2: {
                t2 = getNextFocusable(t, (ArrayList<T>)list, b2);
                break;
            }
            case 1: {
                t2 = getPreviousFocusable(t, (ArrayList<T>)list, b2);
                break;
            }
        }
        return t2;
    }
    
    private static <T> T getNextFocusable(final T o, final ArrayList<T> list, final boolean b) {
        final int size = list.size();
        int lastIndex;
        if (o == null) {
            lastIndex = -1;
        }
        else {
            lastIndex = list.lastIndexOf(o);
        }
        T t;
        if (++lastIndex < size) {
            t = list.get(lastIndex);
        }
        else if (b && size > 0) {
            t = list.get(0);
        }
        else {
            t = null;
        }
        return t;
    }
    
    private static <T> T getPreviousFocusable(final T o, final ArrayList<T> list, final boolean b) {
        final int size = list.size();
        int index;
        if (o == null) {
            index = size;
        }
        else {
            index = list.indexOf(o);
        }
        T t;
        if (--index >= 0) {
            t = list.get(index);
        }
        else if (b && size > 0) {
            t = list.get(size - 1);
        }
        else {
            t = null;
        }
        return t;
    }
    
    private static int getWeightedDistanceFor(final int n, final int n2) {
        return n * 13 * n + n2 * n2;
    }
    
    private static boolean isBetterCandidate(final int n, @NonNull final Rect rect, @NonNull final Rect rect2, @NonNull final Rect rect3) {
        final boolean b = true;
        boolean b2;
        if (!isCandidate(rect, rect2, n)) {
            b2 = false;
        }
        else {
            b2 = b;
            if (isCandidate(rect, rect3, n)) {
                b2 = b;
                if (!beamBeats(n, rect, rect2, rect3)) {
                    if (beamBeats(n, rect, rect3, rect2)) {
                        b2 = false;
                    }
                    else {
                        b2 = b;
                        if (getWeightedDistanceFor(majorAxisDistance(n, rect, rect2), minorAxisDistance(n, rect, rect2)) >= getWeightedDistanceFor(majorAxisDistance(n, rect, rect3), minorAxisDistance(n, rect, rect3))) {
                            b2 = false;
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    private static boolean isCandidate(@NonNull final Rect rect, @NonNull final Rect rect2, final int n) {
        boolean b = true;
        switch (n) {
            default: {
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            case 17: {
                if ((rect.right > rect2.right || rect.left >= rect2.right) && rect.left > rect2.left) {
                    break;
                }
                b = false;
                break;
            }
            case 66: {
                if ((rect.left >= rect2.left && rect.right > rect2.left) || rect.right >= rect2.right) {
                    b = false;
                    break;
                }
                break;
            }
            case 33: {
                if ((rect.bottom <= rect2.bottom && rect.top < rect2.bottom) || rect.top <= rect2.top) {
                    b = false;
                    break;
                }
                break;
            }
            case 130: {
                if ((rect.top >= rect2.top && rect.bottom > rect2.top) || rect.bottom >= rect2.bottom) {
                    b = false;
                    break;
                }
                break;
            }
        }
        return b;
    }
    
    private static boolean isToDirectionOf(final int n, @NonNull final Rect rect, @NonNull final Rect rect2) {
        boolean b = true;
        switch (n) {
            default: {
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            case 17: {
                if (rect.left >= rect2.right) {
                    break;
                }
                b = false;
                break;
            }
            case 66: {
                if (rect.right > rect2.left) {
                    b = false;
                    break;
                }
                break;
            }
            case 33: {
                if (rect.top < rect2.bottom) {
                    b = false;
                    break;
                }
                break;
            }
            case 130: {
                if (rect.bottom > rect2.top) {
                    b = false;
                    break;
                }
                break;
            }
        }
        return b;
    }
    
    private static int majorAxisDistance(final int n, @NonNull final Rect rect, @NonNull final Rect rect2) {
        return Math.max(0, majorAxisDistanceRaw(n, rect, rect2));
    }
    
    private static int majorAxisDistanceRaw(int n, @NonNull final Rect rect, @NonNull final Rect rect2) {
        switch (n) {
            default: {
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            case 17: {
                n = rect.left - rect2.right;
                break;
            }
            case 66: {
                n = rect2.left - rect.right;
                break;
            }
            case 33: {
                n = rect.top - rect2.bottom;
                break;
            }
            case 130: {
                n = rect2.top - rect.bottom;
                break;
            }
        }
        return n;
    }
    
    private static int majorAxisDistanceToFarEdge(final int n, @NonNull final Rect rect, @NonNull final Rect rect2) {
        return Math.max(1, majorAxisDistanceToFarEdgeRaw(n, rect, rect2));
    }
    
    private static int majorAxisDistanceToFarEdgeRaw(int n, @NonNull final Rect rect, @NonNull final Rect rect2) {
        switch (n) {
            default: {
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            case 17: {
                n = rect.left - rect2.left;
                break;
            }
            case 66: {
                n = rect2.right - rect.right;
                break;
            }
            case 33: {
                n = rect.top - rect2.top;
                break;
            }
            case 130: {
                n = rect2.bottom - rect.bottom;
                break;
            }
        }
        return n;
    }
    
    private static int minorAxisDistance(int n, @NonNull final Rect rect, @NonNull final Rect rect2) {
        switch (n) {
            default: {
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            case 17:
            case 66: {
                n = Math.abs(rect.top + rect.height() / 2 - (rect2.top + rect2.height() / 2));
                break;
            }
            case 33:
            case 130: {
                n = Math.abs(rect.left + rect.width() / 2 - (rect2.left + rect2.width() / 2));
                break;
            }
        }
        return n;
    }
    
    public interface BoundsAdapter<T>
    {
        void obtainBounds(final T p0, final Rect p1);
    }
    
    public interface CollectionAdapter<T, V>
    {
        V get(final T p0, final int p1);
        
        int size(final T p0);
    }
    
    private static class SequentialComparator<T> implements Comparator<T>
    {
        private final BoundsAdapter<T> mAdapter;
        private final boolean mIsLayoutRtl;
        private final Rect mTemp1;
        private final Rect mTemp2;
        
        SequentialComparator(final boolean mIsLayoutRtl, final BoundsAdapter<T> mAdapter) {
            this.mTemp1 = new Rect();
            this.mTemp2 = new Rect();
            this.mIsLayoutRtl = mIsLayoutRtl;
            this.mAdapter = mAdapter;
        }
        
        @Override
        public int compare(final T t, final T t2) {
            final int n = 1;
            int n2 = 1;
            final int n3 = -1;
            final Rect mTemp1 = this.mTemp1;
            final Rect mTemp2 = this.mTemp2;
            this.mAdapter.obtainBounds(t, mTemp1);
            this.mAdapter.obtainBounds(t2, mTemp2);
            if (mTemp1.top < mTemp2.top) {
                n2 = n3;
            }
            else if (mTemp1.top > mTemp2.top) {
                n2 = 1;
            }
            else if (mTemp1.left < mTemp2.left) {
                if (!this.mIsLayoutRtl) {
                    n2 = -1;
                }
            }
            else if (mTemp1.left > mTemp2.left) {
                n2 = n3;
                if (!this.mIsLayoutRtl) {
                    n2 = 1;
                }
            }
            else {
                n2 = n3;
                if (mTemp1.bottom >= mTemp2.bottom) {
                    if (mTemp1.bottom > mTemp2.bottom) {
                        n2 = 1;
                    }
                    else if (mTemp1.right < mTemp2.right) {
                        if (this.mIsLayoutRtl) {
                            n2 = n;
                        }
                        else {
                            n2 = -1;
                        }
                    }
                    else if (mTemp1.right > mTemp2.right) {
                        n2 = n3;
                        if (!this.mIsLayoutRtl) {
                            n2 = 1;
                        }
                    }
                    else {
                        n2 = 0;
                    }
                }
            }
            return n2;
        }
    }
}
