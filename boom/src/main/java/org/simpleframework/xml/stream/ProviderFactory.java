// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

final class ProviderFactory
{
    public static Provider getInstance() {
        try {
            return new StreamProvider();
        }
        catch (Throwable t) {
            try {
                final Provider provider = new PullProvider();
            }
            catch (Throwable t2) {
                final Provider provider = new DocumentProvider();
            }
        }
    }
}
