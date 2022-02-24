// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

class Caller
{
    private final Function commit;
    private final Function complete;
    private final Context context;
    private final Function persist;
    private final Function replace;
    private final Function resolve;
    private final Function validate;
    
    public Caller(final Scanner scanner, final Context context) {
        this.validate = scanner.getValidate();
        this.complete = scanner.getComplete();
        this.replace = scanner.getReplace();
        this.resolve = scanner.getResolve();
        this.persist = scanner.getPersist();
        this.commit = scanner.getCommit();
        this.context = context;
    }
    
    public void commit(final Object o) throws Exception {
        if (this.commit != null) {
            this.commit.call(this.context, o);
        }
    }
    
    public void complete(final Object o) throws Exception {
        if (this.complete != null) {
            this.complete.call(this.context, o);
        }
    }
    
    public void persist(final Object o) throws Exception {
        if (this.persist != null) {
            this.persist.call(this.context, o);
        }
    }
    
    public Object replace(final Object o) throws Exception {
        Object call = o;
        if (this.replace != null) {
            call = this.replace.call(this.context, o);
        }
        return call;
    }
    
    public Object resolve(final Object o) throws Exception {
        Object call = o;
        if (this.resolve != null) {
            call = this.resolve.call(this.context, o);
        }
        return call;
    }
    
    public void validate(final Object o) throws Exception {
        if (this.validate != null) {
            this.validate.call(this.context, o);
        }
    }
}
