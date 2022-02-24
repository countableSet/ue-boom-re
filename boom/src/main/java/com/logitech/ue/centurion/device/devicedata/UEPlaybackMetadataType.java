// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UEPlaybackMetadataType
{
    ALBUM(3), 
    ARTIST(2), 
    ERROR(0), 
    TITLE(1), 
    UNKNOWN(-1);
    
    private static final SparseArray<UEPlaybackMetadataType> map;
    private final int mCode;
    
    static {
        (map = new SparseArray()).put(UEPlaybackMetadataType.ERROR.getCode(), (Object)UEPlaybackMetadataType.ERROR);
        UEPlaybackMetadataType.map.put(UEPlaybackMetadataType.TITLE.getCode(), (Object)UEPlaybackMetadataType.TITLE);
        UEPlaybackMetadataType.map.put(UEPlaybackMetadataType.ALBUM.getCode(), (Object)UEPlaybackMetadataType.ALBUM);
        UEPlaybackMetadataType.map.put(UEPlaybackMetadataType.ARTIST.getCode(), (Object)UEPlaybackMetadataType.ARTIST);
    }
    
    private UEPlaybackMetadataType(final int mCode) {
        this.mCode = mCode;
    }
    
    public static UEPlaybackMetadataType getPlaybackDataType(final int n) {
        return (UEPlaybackMetadataType)UEPlaybackMetadataType.map.get(n, (Object)UEPlaybackMetadataType.UNKNOWN);
    }
    
    public int getCode() {
        return this.mCode;
    }
}
