// 
// Decompiled by Procyon v0.5.36
// 

package antistatic.spinnerwheel.adapters;

import android.database.DataSetObserver;
import android.view.ViewGroup;
import android.view.View;

public interface WheelViewAdapter
{
    View getEmptyItem(final View p0, final ViewGroup p1);
    
    View getItem(final int p0, final View p1, final ViewGroup p2);
    
    int getItemsCount();
    
    void registerDataSetObserver(final DataSetObserver p0);
    
    void unregisterDataSetObserver(final DataSetObserver p0);
}
