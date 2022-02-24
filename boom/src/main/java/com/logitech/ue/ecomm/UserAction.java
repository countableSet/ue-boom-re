// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.ecomm;

import java.util.Iterator;
import java.util.HashMap;

public enum UserAction
{
    Dismiss, 
    None, 
    UrlClicked;
    
    private static HashMap<UserAction, String> names;
    
    static {
        (UserAction.names = new HashMap<UserAction, String>()).put(UserAction.None, "none");
        UserAction.names.put(UserAction.Dismiss, "dismiss");
        UserAction.names.put(UserAction.UrlClicked, "clicked-link");
    }
    
    public static UserAction fromString(final String anObject) {
        for (final UserAction key : UserAction.names.keySet()) {
            if (UserAction.names.get(key).equals(anObject)) {
                return key;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return UserAction.names.get(this);
    }
}
