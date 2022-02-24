// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

class SessionManager
{
    private ThreadLocal<Reference> local;
    
    public SessionManager() {
        this.local = new ThreadLocal<Reference>();
    }
    
    private Session create(final boolean b) throws Exception {
        final Reference value = new Reference(b);
        if (value != null) {
            this.local.set(value);
        }
        return value.get();
    }
    
    public void close() throws Exception {
        final Reference reference = this.local.get();
        if (reference == null) {
            throw new PersistenceException("Session does not exist", new Object[0]);
        }
        if (reference.clear() == 0) {
            this.local.remove();
        }
    }
    
    public Session open() throws Exception {
        return this.open(true);
    }
    
    public Session open(final boolean b) throws Exception {
        final Reference reference = this.local.get();
        Session session;
        if (reference != null) {
            session = reference.get();
        }
        else {
            session = this.create(b);
        }
        return session;
    }
    
    private static class Reference
    {
        private int count;
        private Session session;
        
        public Reference(final boolean b) {
            this.session = new Session(b);
        }
        
        public int clear() {
            return --this.count;
        }
        
        public Session get() {
            if (this.count >= 0) {
                ++this.count;
            }
            return this.session;
        }
    }
}
