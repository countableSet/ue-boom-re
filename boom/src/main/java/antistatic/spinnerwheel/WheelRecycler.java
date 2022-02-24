// 
// Decompiled by Procyon v0.5.36
// 

package antistatic.spinnerwheel;

import android.widget.LinearLayout;
import java.util.LinkedList;
import android.view.View;
import java.util.List;

public class WheelRecycler
{
    private static final String LOG_TAG;
    private List<View> emptyItems;
    private List<View> items;
    private AbstractWheel wheel;
    
    static {
        LOG_TAG = WheelRecycler.class.getName();
    }
    
    public WheelRecycler(final AbstractWheel wheel) {
        this.wheel = wheel;
    }
    
    private List<View> addView(final View view, final List<View> list) {
        List<View> list2 = list;
        if (list == null) {
            list2 = new LinkedList<View>();
        }
        list2.add(view);
        return list2;
    }
    
    private View getCachedView(final List<View> list) {
        View view2;
        if (list != null && list.size() > 0) {
            final View view = list.get(0);
            list.remove(0);
            view2 = view;
        }
        else {
            view2 = null;
        }
        return view2;
    }
    
    private void recycleView(final View view, final int n) {
        final int itemsCount = this.wheel.getViewAdapter().getItemsCount();
        int i;
        if (n < 0 || (i = n) >= itemsCount) {
            i = n;
            if (!this.wheel.isCyclic()) {
                this.emptyItems = this.addView(view, this.emptyItems);
                return;
            }
        }
        while (i < 0) {
            i += itemsCount;
        }
        this.items = this.addView(view, this.items);
    }
    
    public void clearAll() {
        if (this.items != null) {
            this.items.clear();
        }
        if (this.emptyItems != null) {
            this.emptyItems.clear();
        }
    }
    
    public View getEmptyItem() {
        return this.getCachedView(this.emptyItems);
    }
    
    public View getItem() {
        return this.getCachedView(this.items);
    }
    
    public int recycleItems(final LinearLayout linearLayout, int n, final ItemsRange itemsRange) {
        final int n2 = n;
        final int n3 = 0;
        int n4 = n;
        n = n2;
        int n5;
        int n6;
        for (int i = n3; i < linearLayout.getChildCount(); i = n5, n4 = n6) {
            if (!itemsRange.contains(n)) {
                this.recycleView(linearLayout.getChildAt(i), n);
                linearLayout.removeViewAt(i);
                n5 = i;
                n6 = n4;
                if (i == 0) {
                    n6 = n4 + 1;
                    n5 = i;
                }
            }
            else {
                n5 = i + 1;
                n6 = n4;
            }
            ++n;
        }
        return n4;
    }
}
