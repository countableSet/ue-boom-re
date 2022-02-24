// 
// Decompiled by Procyon v0.5.36
// 

package uk.co.chrisjenx.calligraphy;

import android.view.LayoutInflater;
import android.util.AttributeSet;
import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

public class CalligraphyContextWrapper extends ContextWrapper
{
    private final int mAttributeId;
    private CalligraphyLayoutInflater mInflater;
    
    CalligraphyContextWrapper(final Context context) {
        super(context);
        this.mAttributeId = CalligraphyConfig.get().getAttrId();
    }
    
    @Deprecated
    public CalligraphyContextWrapper(final Context context, final int mAttributeId) {
        super(context);
        this.mAttributeId = mAttributeId;
    }
    
    static CalligraphyActivityFactory get(final Activity activity) {
        if (!(activity.getLayoutInflater() instanceof CalligraphyLayoutInflater)) {
            throw new RuntimeException("This activity does not wrap the Base Context! See CalligraphyContextWrapper.wrap(Context)");
        }
        return (CalligraphyActivityFactory)activity.getLayoutInflater();
    }
    
    public static View onActivityCreateView(final Activity activity, final View view, final View view2, final String s, final Context context, final AttributeSet set) {
        return get(activity).onActivityCreateView(view, view2, s, context, set);
    }
    
    public static ContextWrapper wrap(final Context context) {
        return new CalligraphyContextWrapper(context);
    }
    
    public Object getSystemService(final String anObject) {
        Object o;
        if ("layout_inflater".equals(anObject)) {
            if (this.mInflater == null) {
                this.mInflater = new CalligraphyLayoutInflater(LayoutInflater.from(this.getBaseContext()), (Context)this, this.mAttributeId, false);
            }
            o = this.mInflater;
        }
        else {
            o = super.getSystemService(anObject);
        }
        return o;
    }
}
