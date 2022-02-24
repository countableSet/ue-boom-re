// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.widget.AdapterView$OnItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import butterknife.ButterKnife;

public class SettingsFragment$$ViewBinder<T extends SettingsFragment> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        ((AdapterView)finder.findRequiredView(o, 16908298, "method 'onItemClicked'")).setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                t.onItemClicked(n);
            }
        });
    }
    
    public void unbind(final T t) {
    }
}
