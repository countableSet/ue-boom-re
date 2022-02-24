// 
// Decompiled by Procyon v0.5.36
// 

package butterknife;

import java.util.RandomAccess;
import java.util.AbstractList;

final class ImmutableList<T> extends AbstractList<T> implements RandomAccess
{
    private final T[] views;
    
    ImmutableList(final T[] views) {
        this.views = views;
    }
    
    @Override
    public boolean contains(final Object o) {
        final boolean b = false;
        final T[] views = this.views;
        final int length = views.length;
        int n = 0;
        boolean b2;
        while (true) {
            b2 = b;
            if (n >= length) {
                break;
            }
            if (views[n] == o) {
                b2 = true;
                break;
            }
            ++n;
        }
        return b2;
    }
    
    @Override
    public T get(final int n) {
        return this.views[n];
    }
    
    @Override
    public int size() {
        return this.views.length;
    }
}
