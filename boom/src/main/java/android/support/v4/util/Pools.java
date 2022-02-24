// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.util;

public final class Pools
{
    private Pools() {
    }
    
    public interface Pool<T>
    {
        T acquire();
        
        boolean release(final T p0);
    }
    
    public static class SimplePool<T> implements Pool<T>
    {
        private final Object[] mPool;
        private int mPoolSize;
        
        public SimplePool(final int n) {
            if (n <= 0) {
                throw new IllegalArgumentException("The max pool size must be > 0");
            }
            this.mPool = new Object[n];
        }
        
        private boolean isInPool(final T t) {
            for (int i = 0; i < this.mPoolSize; ++i) {
                if (this.mPool[i] == t) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public T acquire() {
            Object o;
            if (this.mPoolSize > 0) {
                final int n = this.mPoolSize - 1;
                o = this.mPool[n];
                this.mPool[n] = null;
                --this.mPoolSize;
            }
            else {
                o = null;
            }
            return (T)o;
        }
        
        @Override
        public boolean release(final T t) {
            if (this.isInPool(t)) {
                throw new IllegalStateException("Already in the pool!");
            }
            boolean b;
            if (this.mPoolSize < this.mPool.length) {
                this.mPool[this.mPoolSize] = t;
                ++this.mPoolSize;
                b = true;
            }
            else {
                b = false;
            }
            return b;
        }
    }
    
    public static class SynchronizedPool<T> extends SimplePool<T>
    {
        private final Object mLock;
        
        public SynchronizedPool(final int n) {
            super(n);
            this.mLock = new Object();
        }
        
        @Override
        public T acquire() {
            synchronized (this.mLock) {
                return super.acquire();
            }
        }
        
        @Override
        public boolean release(final T t) {
            synchronized (this.mLock) {
                return super.release(t);
            }
        }
    }
}
