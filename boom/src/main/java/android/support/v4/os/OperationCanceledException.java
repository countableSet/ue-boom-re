// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.os;

public class OperationCanceledException extends RuntimeException
{
    public OperationCanceledException() {
        this((String)null);
    }
    
    public OperationCanceledException(String message) {
        if (message == null) {
            message = "The operation has been canceled.";
        }
        super(message);
    }
}
