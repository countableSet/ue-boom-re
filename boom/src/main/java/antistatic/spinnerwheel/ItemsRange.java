// 
// Decompiled by Procyon v0.5.36
// 

package antistatic.spinnerwheel;

public class ItemsRange
{
    private int count;
    private int first;
    
    public ItemsRange() {
        this(0, 0);
    }
    
    public ItemsRange(final int first, final int count) {
        this.first = first;
        this.count = count;
    }
    
    public boolean contains(final int n) {
        return n >= this.getFirst() && n <= this.getLast();
    }
    
    public int getCount() {
        return this.count;
    }
    
    public int getFirst() {
        return this.first;
    }
    
    public int getLast() {
        return this.getFirst() + this.getCount() - 1;
    }
}
