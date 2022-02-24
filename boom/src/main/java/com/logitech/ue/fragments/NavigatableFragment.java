// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.content.res.TypedArray;
import android.content.Context;
import android.view.MenuInflater;
import android.view.Menu;
import android.os.Bundle;
import com.logitech.ue.activities.NavigatorActivity;
import com.logitech.ue.interfaces.OnBackPressedListener;
import android.support.v4.app.Fragment;

public abstract class NavigatableFragment extends Fragment implements OnBackPressedListener
{
    public NavigatorActivity getNavigator() {
        return (NavigatorActivity)this.getActivity();
    }
    
    public int getStatusBarColor() {
        Object o = this.getContext();
        if (o == null) {
            return -16777216;
        }
        o = ((Context)o).obtainStyledAttributes(new int[] { 2130771982 });
        try {
            return ((TypedArray)o).getColor(0, 0);
        }
        catch (Exception ex) {
            final int color = -16777216;
            ((TypedArray)o).recycle();
            return color;
        }
        finally {
            ((TypedArray)o).recycle();
        }
        return -16777216;
    }
    
    public abstract String getTitle();
    
    public int getToolbarBackgroundColor() {
        Object o = this.getContext();
        if (o == null) {
            return -16777216;
        }
        o = ((Context)o).obtainStyledAttributes(new int[] { 2130771984 });
        try {
            return ((TypedArray)o).getColor(0, 0);
        }
        catch (Exception ex) {
            final int color = -16777216;
            ((TypedArray)o).recycle();
            return color;
        }
        finally {
            ((TypedArray)o).recycle();
        }
        return -16777216;
    }
    
    public int getToolbarTextColor() {
        Object o = this.getContext();
        if (o == null) {
            return -1;
        }
        o = ((Context)o).obtainStyledAttributes(new int[] { 2130771985 });
        try {
            return ((TypedArray)o).getColor(0, 0);
        }
        catch (Exception ex) {
            final int color = -1;
            ((TypedArray)o).recycle();
            return color;
        }
        finally {
            ((TypedArray)o).recycle();
        }
        return -1;
    }
    
    @Override
    public boolean onBack() {
        return true;
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setHasOptionsMenu(true);
    }
    
    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        this.getActivity().setTitle((CharSequence)this.getTitle());
    }
    
    @Override
    public void onStart() {
        super.onStart();
        ((NavigatorActivity)this.getActivity()).setToolbarColorsTo(this.getToolbarBackgroundColor(), this.getToolbarTextColor(), this.getStatusBarColor(), true);
    }
}
