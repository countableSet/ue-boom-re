// 
// Decompiled by Procyon v0.5.36
// 

package antistatic.spinnerwheel.adapters;

import java.util.LinkedList;
import java.util.Iterator;
import android.view.ViewGroup;
import android.view.View;
import android.database.DataSetObserver;
import java.util.List;

public abstract class AbstractWheelAdapter implements WheelViewAdapter
{
    private List<DataSetObserver> datasetObservers;
    
    @Override
    public View getEmptyItem(final View view, final ViewGroup viewGroup) {
        return null;
    }
    
    protected void notifyDataChangedEvent() {
        if (this.datasetObservers != null) {
            final Iterator<DataSetObserver> iterator = this.datasetObservers.iterator();
            while (iterator.hasNext()) {
                iterator.next().onChanged();
            }
        }
    }
    
    protected void notifyDataInvalidatedEvent() {
        if (this.datasetObservers != null) {
            final Iterator<DataSetObserver> iterator = this.datasetObservers.iterator();
            while (iterator.hasNext()) {
                iterator.next().onInvalidated();
            }
        }
    }
    
    @Override
    public void registerDataSetObserver(final DataSetObserver dataSetObserver) {
        if (this.datasetObservers == null) {
            this.datasetObservers = new LinkedList<DataSetObserver>();
        }
        this.datasetObservers.add(dataSetObserver);
    }
    
    @Override
    public void unregisterDataSetObserver(final DataSetObserver dataSetObserver) {
        if (this.datasetObservers != null) {
            this.datasetObservers.remove(dataSetObserver);
        }
    }
}
