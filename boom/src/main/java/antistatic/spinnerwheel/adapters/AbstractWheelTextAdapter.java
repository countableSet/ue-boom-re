// 
// Decompiled by Procyon v0.5.36
// 

package antistatic.spinnerwheel.adapters;

import android.view.ViewGroup;
import android.util.Log;
import android.widget.TextView;
import android.view.View;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.content.Context;

public abstract class AbstractWheelTextAdapter extends AbstractWheelAdapter
{
    public static final int DEFAULT_TEXT_COLOR = -15724528;
    public static final int DEFAULT_TEXT_GRAVITY = 17;
    public static final int DEFAULT_TEXT_SIZE = 24;
    public static final int LABEL_COLOR = -9437072;
    protected static final int NO_RESOURCE = 0;
    public static final int TEXT_VIEW_ITEM_RESOURCE = -1;
    protected Context context;
    protected int emptyItemResourceId;
    protected LayoutInflater inflater;
    protected int itemResourceId;
    protected int itemTextResourceId;
    private int textColor;
    private int textGravity;
    private int textSize;
    private Typeface textTypeface;
    
    protected AbstractWheelTextAdapter(final Context context) {
        this(context, -1);
    }
    
    protected AbstractWheelTextAdapter(final Context context, final int n) {
        this(context, n, 0);
    }
    
    protected AbstractWheelTextAdapter(final Context context, final int itemResourceId, final int itemTextResourceId) {
        this.textColor = -15724528;
        this.textSize = 24;
        this.textGravity = 17;
        this.context = context;
        this.itemResourceId = itemResourceId;
        this.itemTextResourceId = itemTextResourceId;
        this.inflater = (LayoutInflater)context.getSystemService("layout_inflater");
    }
    
    private TextView getTextView(final View view, final int n) {
        TextView textView = null;
        Label_0020: {
            if (n != 0) {
                break Label_0020;
            }
            try {
                if (view instanceof TextView) {
                    textView = (TextView)view;
                }
                else if (n != 0) {
                    textView = (TextView)view.findViewById(n);
                }
                return textView;
            }
            catch (ClassCastException cause) {
                Log.e("AbstractWheelAdapter", "You must supply a resource ID for a TextView");
                throw new IllegalStateException("AbstractWheelAdapter requires the resource ID to be a TextView", cause);
            }
        }
    }
    
    private View getView(final int n, final ViewGroup viewGroup) {
        Object inflate = null;
        switch (n) {
            default: {
                inflate = this.inflater.inflate(n, viewGroup, false);
                break;
            }
            case 0: {
                inflate = null;
                break;
            }
            case -1: {
                inflate = new TextView(this.context);
                break;
            }
        }
        return (View)inflate;
    }
    
    protected void configureTextView(final TextView textView) {
        if (this.itemResourceId == -1) {
            textView.setTextColor(this.textColor);
            textView.setGravity(this.textGravity);
            textView.setTextSize((float)this.textSize);
            textView.setLines(1);
        }
        if (this.textTypeface != null) {
            textView.setTypeface(this.textTypeface);
        }
        else {
            textView.setTypeface(Typeface.SANS_SERIF, 1);
        }
    }
    
    @Override
    public View getEmptyItem(final View view, final ViewGroup viewGroup) {
        View view2 = view;
        if (view == null) {
            view2 = this.getView(this.emptyItemResourceId, viewGroup);
        }
        if (view2 instanceof TextView) {
            this.configureTextView((TextView)view2);
        }
        return view2;
    }
    
    public int getEmptyItemResource() {
        return this.emptyItemResourceId;
    }
    
    @Override
    public View getItem(final int n, View view, final ViewGroup viewGroup) {
        if (n >= 0 && n < this.getItemsCount()) {
            View view2;
            if ((view2 = view) == null) {
                view2 = this.getView(this.itemResourceId, viewGroup);
            }
            final TextView textView = this.getTextView(view2, this.itemTextResourceId);
            if (textView != null) {
                CharSequence itemText;
                if ((itemText = this.getItemText(n)) == null) {
                    itemText = "";
                }
                textView.setText(itemText);
                this.configureTextView(textView);
            }
            view = view2;
        }
        else {
            view = null;
        }
        return view;
    }
    
    public int getItemResource() {
        return this.itemResourceId;
    }
    
    protected abstract CharSequence getItemText(final int p0);
    
    public int getItemTextResource() {
        return this.itemTextResourceId;
    }
    
    public int getTextColor() {
        return this.textColor;
    }
    
    public int getTextGravity() {
        return this.textGravity;
    }
    
    public int getTextSize() {
        return this.textSize;
    }
    
    public void setEmptyItemResource(final int emptyItemResourceId) {
        this.emptyItemResourceId = emptyItemResourceId;
    }
    
    public void setItemResource(final int itemResourceId) {
        this.itemResourceId = itemResourceId;
    }
    
    public void setItemTextResource(final int itemTextResourceId) {
        this.itemTextResourceId = itemTextResourceId;
    }
    
    public void setTextColor(final int textColor) {
        this.textColor = textColor;
    }
    
    public void setTextGravity(final int textGravity) {
        this.textGravity = textGravity;
    }
    
    public void setTextSize(final int textSize) {
        this.textSize = textSize;
    }
    
    public void setTextTypeface(final Typeface textTypeface) {
        this.textTypeface = textTypeface;
    }
}
