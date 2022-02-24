// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.activities;

import com.logitech.ue.fragments.NavigatableFragment;
import com.logitech.ue.fragments.XUPTricksFragment;
import android.os.Bundle;

public class XUPTricksActivity extends NavigatorActivity
{
    private static final String TAG;
    
    static {
        TAG = XUPTricksActivity.class.getSimpleName();
    }
    
    public void finish() {
        super.finish();
        this.overridePendingTransition(2131034132, 2131034128);
    }
    
    public void gotoXUPTricksFragment(final Bundle bundle) {
        this.gotoFragment(XUPTricksFragment.class, bundle);
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setTitle(this.getString(2131165371));
        this.gotoXUPTricksFragment(bundle);
    }
}
