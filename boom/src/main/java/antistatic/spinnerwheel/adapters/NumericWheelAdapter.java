// 
// Decompiled by Procyon v0.5.36
// 

package antistatic.spinnerwheel.adapters;

import android.content.Context;

public class NumericWheelAdapter extends AbstractWheelTextAdapter
{
    public static final int DEFAULT_MAX_VALUE = 9;
    private static final int DEFAULT_MIN_VALUE = 0;
    private String format;
    private int maxValue;
    private int minValue;
    
    public NumericWheelAdapter(final Context context) {
        this(context, 0, 9);
    }
    
    public NumericWheelAdapter(final Context context, final int n, final int n2) {
        this(context, n, n2, null);
    }
    
    public NumericWheelAdapter(final Context context, final int minValue, final int maxValue, final String format) {
        super(context);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.format = format;
    }
    
    public CharSequence getItemText(int n) {
        String s;
        if (n >= 0 && n < this.getItemsCount()) {
            n += this.minValue;
            if (this.format != null) {
                s = String.format(this.format, n);
            }
            else {
                s = Integer.toString(n);
            }
        }
        else {
            s = null;
        }
        return s;
    }
    
    @Override
    public int getItemsCount() {
        return this.maxValue - this.minValue + 1;
    }
    
    public void setMaxValue(final int maxValue) {
        this.maxValue = maxValue;
        this.notifyDataInvalidatedEvent();
    }
    
    public void setMinValue(final int minValue) {
        this.minValue = minValue;
        this.notifyDataInvalidatedEvent();
    }
}
