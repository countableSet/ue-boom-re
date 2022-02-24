// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.view.View$OnClickListener;
import butterknife.internal.DebouncingOnClickListener;
import android.widget.ImageView;
import android.view.View;
import butterknife.ButterKnife;

public class XUPTutorialDialogFragment$$ViewBinder<T extends XUPTutorialDialogFragment> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        final View view = finder.findRequiredView(o, 2131624137, "field 'mContentView' and method 'onContentClicked'");
        t.mContentView = (ImageView)finder.castView(view, 2131624137, "field 'mContentView'");
        view.setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onContentClicked(view);
            }
        });
        (t.mCloseButton = finder.findRequiredView(o, 2131624156, "field 'mCloseButton' and method 'onCloseClicked'")).setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onCloseClicked(view);
            }
        });
    }
    
    public void unbind(final T t) {
        t.mContentView = null;
        t.mCloseButton = null;
    }
}
