// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Arrays;
import java.lang.reflect.Array;

public class SortedList<T>
{
    private static final int CAPACITY_GROWTH = 10;
    private static final int DELETION = 2;
    private static final int INSERTION = 1;
    public static final int INVALID_POSITION = -1;
    private static final int LOOKUP = 4;
    private static final int MIN_CAPACITY = 10;
    private BatchedCallback mBatchedCallback;
    private Callback mCallback;
    T[] mData;
    private int mMergedSize;
    private T[] mOldData;
    private int mOldDataSize;
    private int mOldDataStart;
    private int mSize;
    private final Class<T> mTClass;
    
    public SortedList(final Class<T> clazz, final Callback<T> callback) {
        this(clazz, callback, 10);
    }
    
    public SortedList(final Class<T> clazz, final Callback<T> mCallback, final int length) {
        this.mTClass = clazz;
        this.mData = (T[])Array.newInstance(clazz, length);
        this.mCallback = mCallback;
        this.mSize = 0;
    }
    
    private int add(final T t, final boolean b) {
        final int index = this.findIndexOf(t, this.mData, 0, this.mSize, 1);
        int n;
        if (index == -1) {
            n = 0;
        }
        else if ((n = index) < this.mSize) {
            final T t2 = this.mData[index];
            n = index;
            if (this.mCallback.areItemsTheSame(t2, t)) {
                if (this.mCallback.areContentsTheSame(t2, t)) {
                    this.mData[index] = t;
                    n = index;
                    return n;
                }
                this.mData[index] = t;
                this.mCallback.onChanged(index, 1);
                n = index;
                return n;
            }
        }
        this.addToData(n, t);
        if (b) {
            this.mCallback.onInserted(n, 1);
        }
        return n;
    }
    
    private void addAllInternal(final T[] array) {
        boolean b;
        if (!(this.mCallback instanceof BatchedCallback)) {
            b = true;
        }
        else {
            b = false;
        }
        if (b) {
            this.beginBatchedUpdates();
        }
        this.mOldData = this.mData;
        this.mOldDataStart = 0;
        this.mOldDataSize = this.mSize;
        Arrays.sort(array, this.mCallback);
        final int deduplicate = this.deduplicate(array);
        if (this.mSize == 0) {
            this.mData = array;
            this.mSize = deduplicate;
            this.mMergedSize = deduplicate;
            this.mCallback.onInserted(0, deduplicate);
        }
        else {
            this.merge(array, deduplicate);
        }
        this.mOldData = null;
        if (b) {
            this.endBatchedUpdates();
        }
    }
    
    private void addToData(final int i, final T t) {
        if (i > this.mSize) {
            throw new IndexOutOfBoundsException("cannot add item to " + i + " because size is " + this.mSize);
        }
        if (this.mSize == this.mData.length) {
            final Object[] mData = (Object[])Array.newInstance(this.mTClass, this.mData.length + 10);
            System.arraycopy(this.mData, 0, mData, 0, i);
            mData[i] = t;
            System.arraycopy(this.mData, i, mData, i + 1, this.mSize - i);
            this.mData = (T[])mData;
        }
        else {
            System.arraycopy(this.mData, i, this.mData, i + 1, this.mSize - i);
            this.mData[i] = t;
        }
        ++this.mSize;
    }
    
    private int deduplicate(final T[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Input array must be non-empty");
        }
        int n = 0;
        int n2 = 1;
        for (int i = 1; i < array.length; ++i) {
            final T t = array[i];
            final int compare = this.mCallback.compare(array[n], t);
            if (compare > 0) {
                throw new IllegalArgumentException("Input must be sorted in ascending order.");
            }
            if (compare == 0) {
                final int sameItem = this.findSameItem(t, array, n, n2);
                if (sameItem != -1) {
                    array[sameItem] = t;
                }
                else {
                    if (n2 != i) {
                        array[n2] = t;
                    }
                    ++n2;
                }
            }
            else {
                if (n2 != i) {
                    array[n2] = t;
                }
                n = n2;
                ++n2;
            }
        }
        return n2;
    }
    
    private int findIndexOf(final T t, final T[] array, int i, int linearEqualitySearch, final int n) {
        while (i < linearEqualitySearch) {
            final int n2 = (i + linearEqualitySearch) / 2;
            final T t2 = array[n2];
            final int compare = this.mCallback.compare(t2, t);
            if (compare < 0) {
                i = n2 + 1;
            }
            else {
                if (compare == 0) {
                    if (this.mCallback.areItemsTheSame(t2, t)) {
                        i = n2;
                    }
                    else {
                        linearEqualitySearch = this.linearEqualitySearch(t, n2, i, linearEqualitySearch);
                        if (n == 1) {
                            i = n2;
                            if (linearEqualitySearch != -1) {
                                i = linearEqualitySearch;
                            }
                        }
                        else {
                            i = linearEqualitySearch;
                        }
                    }
                    return i;
                }
                linearEqualitySearch = n2;
            }
        }
        if (n != 1) {
            i = -1;
        }
        return i;
    }
    
    private int findSameItem(final T t, final T[] array, int i, final int n) {
        while (i < n) {
            if (this.mCallback.areItemsTheSame(array[i], t)) {
                return i;
            }
            ++i;
        }
        i = -1;
        return i;
    }
    
    private int linearEqualitySearch(final T t, int i, final int n, final int n2) {
        for (int j = i - 1; j >= n; --j) {
            final T t2 = this.mData[j];
            if (this.mCallback.compare(t2, t) != 0) {
                break;
            }
            if (this.mCallback.areItemsTheSame(t2, t)) {
                i = j;
                return i;
            }
        }
        ++i;
        while (i < n2) {
            final T t3 = this.mData[i];
            if (this.mCallback.compare(t3, t) != 0) {
                break;
            }
            if (this.mCallback.areItemsTheSame(t3, t)) {
                return i;
            }
            ++i;
        }
        i = -1;
        return i;
    }
    
    private void merge(final T[] array, int n) {
        this.mData = (T[])Array.newInstance(this.mTClass, this.mSize + n + 10);
        this.mMergedSize = 0;
        int n2 = 0;
        while (this.mOldDataStart < this.mOldDataSize || n2 < n) {
            if (this.mOldDataStart == this.mOldDataSize) {
                n -= n2;
                System.arraycopy(array, n2, this.mData, this.mMergedSize, n);
                this.mMergedSize += n;
                this.mSize += n;
                this.mCallback.onInserted(this.mMergedSize - n, n);
                break;
            }
            if (n2 == n) {
                n = this.mOldDataSize - this.mOldDataStart;
                System.arraycopy(this.mOldData, this.mOldDataStart, this.mData, this.mMergedSize, n);
                this.mMergedSize += n;
                break;
            }
            final T t = this.mOldData[this.mOldDataStart];
            final T t2 = array[n2];
            final int compare = this.mCallback.compare(t, t2);
            if (compare > 0) {
                this.mData[this.mMergedSize++] = t2;
                ++this.mSize;
                ++n2;
                this.mCallback.onInserted(this.mMergedSize - 1, 1);
            }
            else if (compare == 0 && this.mCallback.areItemsTheSame(t, t2)) {
                this.mData[this.mMergedSize++] = t2;
                final int n3 = n2 + 1;
                ++this.mOldDataStart;
                n2 = n3;
                if (this.mCallback.areContentsTheSame(t, t2)) {
                    continue;
                }
                this.mCallback.onChanged(this.mMergedSize - 1, 1);
                n2 = n3;
            }
            else {
                this.mData[this.mMergedSize++] = t;
                ++this.mOldDataStart;
            }
        }
    }
    
    private boolean remove(final T t, final boolean b) {
        final boolean b2 = false;
        final int index = this.findIndexOf(t, this.mData, 0, this.mSize, 2);
        boolean b3;
        if (index == -1) {
            b3 = b2;
        }
        else {
            this.removeItemAtIndex(index, b);
            b3 = true;
        }
        return b3;
    }
    
    private void removeItemAtIndex(final int n, final boolean b) {
        System.arraycopy(this.mData, n + 1, this.mData, n, this.mSize - n - 1);
        --this.mSize;
        this.mData[this.mSize] = null;
        if (b) {
            this.mCallback.onRemoved(n, 1);
        }
    }
    
    private void throwIfMerging() {
        if (this.mOldData != null) {
            throw new IllegalStateException("Cannot call this method from within addAll");
        }
    }
    
    public int add(final T t) {
        this.throwIfMerging();
        return this.add(t, true);
    }
    
    public void addAll(final Collection<T> collection) {
        this.addAll(collection.toArray((T[])Array.newInstance(this.mTClass, collection.size())), true);
    }
    
    public void addAll(final T... array) {
        this.addAll(array, false);
    }
    
    public void addAll(final T[] array, final boolean b) {
        this.throwIfMerging();
        if (array.length != 0) {
            if (b) {
                this.addAllInternal(array);
            }
            else {
                final Object[] array2 = (Object[])Array.newInstance(this.mTClass, array.length);
                System.arraycopy(array, 0, array2, 0, array.length);
                this.addAllInternal((T[])array2);
            }
        }
    }
    
    public void beginBatchedUpdates() {
        this.throwIfMerging();
        if (!(this.mCallback instanceof BatchedCallback)) {
            if (this.mBatchedCallback == null) {
                this.mBatchedCallback = new BatchedCallback(this.mCallback);
            }
            this.mCallback = (Callback)this.mBatchedCallback;
        }
    }
    
    public void clear() {
        this.throwIfMerging();
        if (this.mSize != 0) {
            final int mSize = this.mSize;
            Arrays.fill(this.mData, 0, mSize, null);
            this.mSize = 0;
            this.mCallback.onRemoved(0, mSize);
        }
    }
    
    public void endBatchedUpdates() {
        this.throwIfMerging();
        if (this.mCallback instanceof BatchedCallback) {
            ((BatchedCallback)this.mCallback).dispatchLastEvent();
        }
        if (this.mCallback == this.mBatchedCallback) {
            this.mCallback = (Callback)this.mBatchedCallback.mWrappedCallback;
        }
    }
    
    public T get(final int i) throws IndexOutOfBoundsException {
        if (i >= this.mSize || i < 0) {
            throw new IndexOutOfBoundsException("Asked to get item at " + i + " but size is " + this.mSize);
        }
        T t;
        if (this.mOldData != null && i >= this.mMergedSize) {
            t = this.mOldData[i - this.mMergedSize + this.mOldDataStart];
        }
        else {
            t = this.mData[i];
        }
        return t;
    }
    
    public int indexOf(final T t) {
        int n;
        if (this.mOldData != null) {
            n = this.findIndexOf(t, this.mData, 0, this.mMergedSize, 4);
            if (n == -1) {
                final int index = this.findIndexOf(t, this.mOldData, this.mOldDataStart, this.mOldDataSize, 4);
                if (index != -1) {
                    n = index - this.mOldDataStart + this.mMergedSize;
                }
                else {
                    n = -1;
                }
            }
        }
        else {
            n = this.findIndexOf(t, this.mData, 0, this.mSize, 4);
        }
        return n;
    }
    
    public void recalculatePositionOfItemAt(final int n) {
        this.throwIfMerging();
        final T value = this.get(n);
        this.removeItemAtIndex(n, false);
        final int add = this.add(value, false);
        if (n != add) {
            this.mCallback.onMoved(n, add);
        }
    }
    
    public boolean remove(final T t) {
        this.throwIfMerging();
        return this.remove(t, true);
    }
    
    public T removeItemAt(final int n) {
        this.throwIfMerging();
        final T value = this.get(n);
        this.removeItemAtIndex(n, true);
        return value;
    }
    
    public int size() {
        return this.mSize;
    }
    
    public void updateItemAt(final int n, final T t) {
        this.throwIfMerging();
        final T value = this.get(n);
        boolean b;
        if (value == t || !this.mCallback.areContentsTheSame(value, t)) {
            b = true;
        }
        else {
            b = false;
        }
        if (value != t && this.mCallback.compare(value, t) == 0) {
            this.mData[n] = t;
            if (b) {
                this.mCallback.onChanged(n, 1);
            }
        }
        else {
            if (b) {
                this.mCallback.onChanged(n, 1);
            }
            this.removeItemAtIndex(n, false);
            final int add = this.add(t, false);
            if (n != add) {
                this.mCallback.onMoved(n, add);
            }
        }
    }
    
    public static class BatchedCallback<T2> extends Callback<T2>
    {
        static final int TYPE_ADD = 1;
        static final int TYPE_CHANGE = 3;
        static final int TYPE_MOVE = 4;
        static final int TYPE_NONE = 0;
        static final int TYPE_REMOVE = 2;
        int mLastEventCount;
        int mLastEventPosition;
        int mLastEventType;
        private final Callback<T2> mWrappedCallback;
        
        public BatchedCallback(final Callback<T2> mWrappedCallback) {
            this.mLastEventType = 0;
            this.mLastEventPosition = -1;
            this.mLastEventCount = -1;
            this.mWrappedCallback = mWrappedCallback;
        }
        
        @Override
        public boolean areContentsTheSame(final T2 t2, final T2 t3) {
            return this.mWrappedCallback.areContentsTheSame(t2, t3);
        }
        
        @Override
        public boolean areItemsTheSame(final T2 t2, final T2 t3) {
            return this.mWrappedCallback.areItemsTheSame(t2, t3);
        }
        
        @Override
        public int compare(final T2 t2, final T2 t3) {
            return this.mWrappedCallback.compare(t2, t3);
        }
        
        public void dispatchLastEvent() {
            if (this.mLastEventType != 0) {
                switch (this.mLastEventType) {
                    case 1: {
                        this.mWrappedCallback.onInserted(this.mLastEventPosition, this.mLastEventCount);
                        break;
                    }
                    case 2: {
                        this.mWrappedCallback.onRemoved(this.mLastEventPosition, this.mLastEventCount);
                        break;
                    }
                    case 3: {
                        this.mWrappedCallback.onChanged(this.mLastEventPosition, this.mLastEventCount);
                        break;
                    }
                }
                this.mLastEventType = 0;
            }
        }
        
        @Override
        public void onChanged(final int n, final int mLastEventCount) {
            if (this.mLastEventType == 3 && n <= this.mLastEventPosition + this.mLastEventCount && n + mLastEventCount >= this.mLastEventPosition) {
                final int mLastEventPosition = this.mLastEventPosition;
                final int mLastEventCount2 = this.mLastEventCount;
                this.mLastEventPosition = Math.min(n, this.mLastEventPosition);
                this.mLastEventCount = Math.max(mLastEventPosition + mLastEventCount2, n + mLastEventCount) - this.mLastEventPosition;
            }
            else {
                this.dispatchLastEvent();
                this.mLastEventPosition = n;
                this.mLastEventCount = mLastEventCount;
                this.mLastEventType = 3;
            }
        }
        
        @Override
        public void onInserted(final int n, final int mLastEventCount) {
            if (this.mLastEventType == 1 && n >= this.mLastEventPosition && n <= this.mLastEventPosition + this.mLastEventCount) {
                this.mLastEventCount += mLastEventCount;
                this.mLastEventPosition = Math.min(n, this.mLastEventPosition);
            }
            else {
                this.dispatchLastEvent();
                this.mLastEventPosition = n;
                this.mLastEventCount = mLastEventCount;
                this.mLastEventType = 1;
            }
        }
        
        @Override
        public void onMoved(final int n, final int n2) {
            this.dispatchLastEvent();
            this.mWrappedCallback.onMoved(n, n2);
        }
        
        @Override
        public void onRemoved(final int mLastEventPosition, final int mLastEventCount) {
            if (this.mLastEventType == 2 && this.mLastEventPosition == mLastEventPosition) {
                this.mLastEventCount += mLastEventCount;
            }
            else {
                this.dispatchLastEvent();
                this.mLastEventPosition = mLastEventPosition;
                this.mLastEventCount = mLastEventCount;
                this.mLastEventType = 2;
            }
        }
    }
    
    public abstract static class Callback<T2> implements Comparator<T2>
    {
        public abstract boolean areContentsTheSame(final T2 p0, final T2 p1);
        
        public abstract boolean areItemsTheSame(final T2 p0, final T2 p1);
        
        @Override
        public abstract int compare(final T2 p0, final T2 p1);
        
        public abstract void onChanged(final int p0, final int p1);
        
        public abstract void onInserted(final int p0, final int p1);
        
        public abstract void onMoved(final int p0, final int p1);
        
        public abstract void onRemoved(final int p0, final int p1);
    }
}
