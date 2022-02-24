// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.interfaces;

public interface IPage
{
    int getAccentColor();
    
    String getTitle();
    
    void onDeselected();
    
    void onSelected();
    
    void onTransition(final float p0);
}
