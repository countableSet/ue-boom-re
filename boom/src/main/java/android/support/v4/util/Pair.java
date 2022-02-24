// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.util;

public class Pair<F, S>
{
    public final F first;
    public final S second;
    
    public Pair(final F first, final S second) {
        this.first = first;
        this.second = second;
    }
    
    public static <A, B> Pair<A, B> create(final A a, final B b) {
        return new Pair<A, B>(a, b);
    }
    
    private static boolean objectsEqual(final Object o, final Object obj) {
        return o == obj || (o != null && o.equals(obj));
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        boolean b2;
        if (!(o instanceof Pair)) {
            b2 = b;
        }
        else {
            final Pair pair = (Pair)o;
            b2 = b;
            if (objectsEqual(pair.first, this.first)) {
                b2 = b;
                if (objectsEqual(pair.second, this.second)) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.first == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.first.hashCode();
        }
        if (this.second != null) {
            hashCode = this.second.hashCode();
        }
        return hashCode2 ^ hashCode;
    }
}
