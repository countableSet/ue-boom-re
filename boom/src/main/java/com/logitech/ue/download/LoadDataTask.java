// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.download;

public class LoadDataTask
{
    public byte[] data;
    public LoadDataTaskListener listener;
    public String tag;
    public String url;
    
    public LoadDataTask(final String url, final String s, final LoadDataTaskListener listener) {
        this.url = url;
        this.listener = listener;
    }
}
