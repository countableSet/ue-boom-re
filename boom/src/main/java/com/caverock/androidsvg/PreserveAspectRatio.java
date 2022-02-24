// 
// Decompiled by Procyon v0.5.36
// 

package com.caverock.androidsvg;

public class PreserveAspectRatio
{
    public static final PreserveAspectRatio BOTTOM;
    public static final PreserveAspectRatio END;
    public static final PreserveAspectRatio FULLSCREEN;
    public static final PreserveAspectRatio FULLSCREEN_START;
    public static final PreserveAspectRatio LETTERBOX;
    public static final PreserveAspectRatio START;
    public static final PreserveAspectRatio STRETCH;
    public static final PreserveAspectRatio TOP;
    public static final PreserveAspectRatio UNSCALED;
    private Alignment alignment;
    private Scale scale;
    
    static {
        UNSCALED = new PreserveAspectRatio(null, null);
        STRETCH = new PreserveAspectRatio(Alignment.None, null);
        LETTERBOX = new PreserveAspectRatio(Alignment.XMidYMid, Scale.Meet);
        START = new PreserveAspectRatio(Alignment.XMinYMin, Scale.Meet);
        END = new PreserveAspectRatio(Alignment.XMaxYMax, Scale.Meet);
        TOP = new PreserveAspectRatio(Alignment.XMidYMin, Scale.Meet);
        BOTTOM = new PreserveAspectRatio(Alignment.XMidYMax, Scale.Meet);
        FULLSCREEN = new PreserveAspectRatio(Alignment.XMidYMid, Scale.Slice);
        FULLSCREEN_START = new PreserveAspectRatio(Alignment.XMinYMin, Scale.Slice);
    }
    
    public PreserveAspectRatio(final Alignment alignment, final Scale scale) {
        this.alignment = alignment;
        this.scale = scale;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this != o) {
            if (o == null) {
                b = false;
            }
            else if (this.getClass() != o.getClass()) {
                b = false;
            }
            else {
                final PreserveAspectRatio preserveAspectRatio = (PreserveAspectRatio)o;
                if (this.alignment != preserveAspectRatio.alignment) {
                    b = false;
                }
                else if (this.scale != preserveAspectRatio.scale) {
                    b = false;
                }
            }
        }
        return b;
    }
    
    public Alignment getAlignment() {
        return this.alignment;
    }
    
    public Scale getScale() {
        return this.scale;
    }
    
    public enum Alignment
    {
        None("None", 0), 
        XMaxYMax("XMaxYMax", 9), 
        XMaxYMid("XMaxYMid", 6), 
        XMaxYMin("XMaxYMin", 3), 
        XMidYMax("XMidYMax", 8), 
        XMidYMid("XMidYMid", 5), 
        XMidYMin("XMidYMin", 2), 
        XMinYMax("XMinYMax", 7), 
        XMinYMid("XMinYMid", 4), 
        XMinYMin("XMinYMin", 1);
        
        private Alignment(final String name, final int ordinal) {
        }
    }
    
    public enum Scale
    {
        Meet("Meet", 0), 
        Slice("Slice", 1);
        
        private Scale(final String name, final int ordinal) {
        }
    }
}
