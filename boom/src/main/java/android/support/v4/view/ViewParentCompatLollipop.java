// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.view;

import android.util.Log;
import android.view.View;
import android.view.ViewParent;

class ViewParentCompatLollipop
{
    private static final String TAG = "ViewParentCompat";
    
    public static boolean onNestedFling(final ViewParent obj, final View view, final float n, final float n2, final boolean b) {
        try {
            return obj.onNestedFling(view, n, n2, b);
        }
        catch (AbstractMethodError abstractMethodError) {
            Log.e("ViewParentCompat", "ViewParent " + obj + " does not implement interface " + "method onNestedFling", (Throwable)abstractMethodError);
            return false;
        }
    }
    
    public static boolean onNestedPreFling(final ViewParent obj, final View view, final float n, final float n2) {
        try {
            return obj.onNestedPreFling(view, n, n2);
        }
        catch (AbstractMethodError abstractMethodError) {
            Log.e("ViewParentCompat", "ViewParent " + obj + " does not implement interface " + "method onNestedPreFling", (Throwable)abstractMethodError);
            return false;
        }
    }
    
    public static void onNestedPreScroll(final ViewParent obj, final View view, final int n, final int n2, final int[] array) {
        try {
            obj.onNestedPreScroll(view, n, n2, array);
        }
        catch (AbstractMethodError abstractMethodError) {
            Log.e("ViewParentCompat", "ViewParent " + obj + " does not implement interface " + "method onNestedPreScroll", (Throwable)abstractMethodError);
        }
    }
    
    public static void onNestedScroll(final ViewParent obj, final View view, final int n, final int n2, final int n3, final int n4) {
        try {
            obj.onNestedScroll(view, n, n2, n3, n4);
        }
        catch (AbstractMethodError abstractMethodError) {
            Log.e("ViewParentCompat", "ViewParent " + obj + " does not implement interface " + "method onNestedScroll", (Throwable)abstractMethodError);
        }
    }
    
    public static void onNestedScrollAccepted(final ViewParent obj, final View view, final View view2, final int n) {
        try {
            obj.onNestedScrollAccepted(view, view2, n);
        }
        catch (AbstractMethodError abstractMethodError) {
            Log.e("ViewParentCompat", "ViewParent " + obj + " does not implement interface " + "method onNestedScrollAccepted", (Throwable)abstractMethodError);
        }
    }
    
    public static boolean onStartNestedScroll(final ViewParent obj, final View view, final View view2, final int n) {
        try {
            return obj.onStartNestedScroll(view, view2, n);
        }
        catch (AbstractMethodError abstractMethodError) {
            Log.e("ViewParentCompat", "ViewParent " + obj + " does not implement interface " + "method onStartNestedScroll", (Throwable)abstractMethodError);
            return false;
        }
    }
    
    public static void onStopNestedScroll(final ViewParent obj, final View view) {
        try {
            obj.onStopNestedScroll(view);
        }
        catch (AbstractMethodError abstractMethodError) {
            Log.e("ViewParentCompat", "ViewParent " + obj + " does not implement interface " + "method onStopNestedScroll", (Throwable)abstractMethodError);
        }
    }
}
