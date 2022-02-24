// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.widget.AdapterView$OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.View;
import butterknife.ButterKnife;

public class HowToMenuFragment$$ViewBinder<T extends HowToMenuFragment> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        final View view = finder.findRequiredView(o, 16908298, "field 'mListView' and method 'itemClicked'");
        t.mListView = (ListView)finder.castView(view, 16908298, "field 'mListView'");
        ((AdapterView)view).setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                t.itemClicked(view, n);
            }
        });
    }
    
    public void unbind(final T t) {
        t.mListView = null;
    }
}
