// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.content;

public class LoadDeviceInfoTask<T>
{
    public String hexCode;
    public String imageType;
    public ContentLoadListener<T> listener;
    
    public LoadDeviceInfoTask(final String hexCode, final String imageType, final ContentLoadListener<T> listener) {
        this.hexCode = hexCode;
        this.imageType = imageType;
        this.listener = listener;
    }
}
