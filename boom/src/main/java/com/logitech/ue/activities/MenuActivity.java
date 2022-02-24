// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.activities;

import com.logitech.ue.fragments.NavigatableFragment;
import com.logitech.ue.fragments.MenuMainFragment;
import android.os.Bundle;

public class MenuActivity extends NavigatorActivity
{
    public void finish() {
        super.finish();
        this.overridePendingTransition(2131034132, 2131034129);
    }
    
    public void gotoMainMenuFragment(final Bundle bundle) {
        this.gotoFragment(MenuMainFragment.class, bundle);
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (this.getIntent().getExtras() == null && this.getSupportFragmentManager().findFragmentById(2131624045) == null) {
            this.gotoMainMenuFragment(null);
        }
    }
}
