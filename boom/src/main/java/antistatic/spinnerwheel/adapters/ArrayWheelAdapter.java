// 
// Decompiled by Procyon v0.5.36
// 

package antistatic.spinnerwheel.adapters;

import android.content.Context;

public class ArrayWheelAdapter<T> extends AbstractWheelTextAdapter
{
    private T[] items;
    
    public ArrayWheelAdapter(final Context context, final T[] items) {
        super(context);
        this.items = items;
    }
    
    public CharSequence getItemText(final int n) {
        CharSequence string;
        if (n >= 0 && n < this.items.length) {
            final T t = this.items[n];
            if (t instanceof CharSequence) {
                string = (CharSequence)t;
            }
            else {
                string = t.toString();
            }
        }
        else {
            string = null;
        }
        return string;
    }
    
    @Override
    public int getItemsCount() {
        return this.items.length;
    }
}
