// 
// Decompiled by Procyon v0.5.36
// 

package butterknife.internal;

final class Parameter
{
    static final Parameter[] NONE;
    private final int listenerPosition;
    private final String type;
    
    static {
        NONE = new Parameter[0];
    }
    
    Parameter(final int listenerPosition, final String type) {
        this.listenerPosition = listenerPosition;
        this.type = type;
    }
    
    int getListenerPosition() {
        return this.listenerPosition;
    }
    
    String getType() {
        return this.type;
    }
    
    public boolean requiresCast(final String anObject) {
        return !this.type.equals(anObject);
    }
}
