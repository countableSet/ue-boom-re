// 
// Decompiled by Procyon v0.5.36
// 

package uk.co.chrisjenx.calligraphy;

import android.view.LayoutInflater$Factory;
import android.annotation.TargetApi;
import android.view.ViewGroup;
import org.xmlpull.v1.XmlPullParser;
import android.os.Build$VERSION;
import java.lang.reflect.Method;
import android.view.LayoutInflater$Factory2;
import android.util.AttributeSet;
import android.view.View;
import android.content.Context;
import java.lang.reflect.Field;
import android.view.LayoutInflater;

class CalligraphyLayoutInflater extends LayoutInflater implements CalligraphyActivityFactory
{
    private static final String[] sClassPrefixList;
    private final int mAttributeId;
    private final CalligraphyFactory mCalligraphyFactory;
    private Field mConstructorArgs;
    private boolean mSetPrivateFactory;
    
    static {
        sClassPrefixList = new String[] { "android.widget.", "android.webkit." };
    }
    
    protected CalligraphyLayoutInflater(final Context context, final int mAttributeId) {
        super(context);
        this.mSetPrivateFactory = false;
        this.mConstructorArgs = null;
        this.mAttributeId = mAttributeId;
        this.mCalligraphyFactory = new CalligraphyFactory(mAttributeId);
        this.setUpLayoutFactories(false);
    }
    
    protected CalligraphyLayoutInflater(final LayoutInflater layoutInflater, final Context context, final int mAttributeId, final boolean upLayoutFactories) {
        super(layoutInflater, context);
        this.mSetPrivateFactory = false;
        this.mConstructorArgs = null;
        this.mAttributeId = mAttributeId;
        this.mCalligraphyFactory = new CalligraphyFactory(mAttributeId);
        this.setUpLayoutFactories(upLayoutFactories);
    }
    
    private View createCustomViewInternal(View view, final View view2, final String s, final Context context, final AttributeSet set) {
        if (!CalligraphyConfig.get().isCustomViewCreation()) {
            view = view2;
        }
        else {
            Label_0109: {
                if ((view = view2) != null) {
                    break Label_0109;
                }
                view = view2;
                if (s.indexOf(46) <= -1) {
                    break Label_0109;
                }
                if (this.mConstructorArgs == null) {
                    this.mConstructorArgs = ReflectionUtils.getField(LayoutInflater.class, "mConstructorArgs");
                }
                final Object[] array = (Object[])ReflectionUtils.getValue(this.mConstructorArgs, this);
                final Object o = array[0];
                array[0] = context;
                ReflectionUtils.setValue(this.mConstructorArgs, this, array);
                try {
                    view = this.createView(s, (String)null, set);
                }
                catch (ClassNotFoundException ex) {
                    array[0] = o;
                    ReflectionUtils.setValue(this.mConstructorArgs, this, array);
                    view = view2;
                }
                finally {
                    array[0] = o;
                    ReflectionUtils.setValue(this.mConstructorArgs, this, array);
                }
            }
        }
        return (View)view;
    }
    
    private void setPrivateFactoryInternal() {
        if (!this.mSetPrivateFactory && CalligraphyConfig.get().isReflection()) {
            if (!(this.getContext() instanceof LayoutInflater$Factory2)) {
                this.mSetPrivateFactory = true;
            }
            else {
                final Method method = ReflectionUtils.getMethod(LayoutInflater.class, "setPrivateFactory");
                if (method != null) {
                    ReflectionUtils.invokeMethod(this, method, new PrivateWrapperFactory2((LayoutInflater$Factory2)this.getContext(), this, this.mCalligraphyFactory));
                }
                this.mSetPrivateFactory = true;
            }
        }
    }
    
    private void setUpLayoutFactories(final boolean b) {
        if (!b) {
            if (Build$VERSION.SDK_INT >= 11 && this.getFactory2() != null && !(this.getFactory2() instanceof WrapperFactory2)) {
                this.setFactory2(this.getFactory2());
            }
            if (this.getFactory() != null && !(this.getFactory() instanceof WrapperFactory)) {
                this.setFactory(this.getFactory());
            }
        }
    }
    
    public LayoutInflater cloneInContext(final Context context) {
        return new CalligraphyLayoutInflater(this, context, this.mAttributeId, true);
    }
    
    public View inflate(final XmlPullParser xmlPullParser, final ViewGroup viewGroup, final boolean b) {
        this.setPrivateFactoryInternal();
        return super.inflate(xmlPullParser, viewGroup, b);
    }
    
    @TargetApi(11)
    public View onActivityCreateView(final View view, final View view2, final String s, final Context context, final AttributeSet set) {
        return this.mCalligraphyFactory.onViewCreated(this.createCustomViewInternal(view, view2, s, context, set), context, set);
    }
    
    @TargetApi(11)
    protected View onCreateView(final View view, final String s, final AttributeSet set) throws ClassNotFoundException {
        return this.mCalligraphyFactory.onViewCreated(super.onCreateView(view, s, set), this.getContext(), set);
    }
    
    protected View onCreateView(final String s, final AttributeSet set) throws ClassNotFoundException {
        View view = null;
        final String[] sClassPrefixList = CalligraphyLayoutInflater.sClassPrefixList;
        final int length = sClassPrefixList.length;
        int n = 0;
    Label_0042_Outer:
        while (true) {
            Label_0048: {
                if (n >= length) {
                    break Label_0048;
                }
                final String s2 = sClassPrefixList[n];
                while (true) {
                    try {
                        view = this.createView(s, s2, set);
                        ++n;
                        continue Label_0042_Outer;
                        // iftrue(Label_0063:, onCreateView = view != null)
                        View onCreateView;
                        while (true) {
                            onCreateView = super.onCreateView(s, set);
                            return this.mCalligraphyFactory.onViewCreated(onCreateView, onCreateView.getContext(), set);
                            continue;
                        }
                        Label_0063: {
                            return this.mCalligraphyFactory.onViewCreated(onCreateView, onCreateView.getContext(), set);
                        }
                    }
                    catch (ClassNotFoundException ex) {
                        continue;
                    }
                    break;
                }
            }
        }
    }
    
    public void setFactory(final LayoutInflater$Factory factory) {
        if (!(factory instanceof WrapperFactory)) {
            super.setFactory((LayoutInflater$Factory)new WrapperFactory(factory, this, this.mCalligraphyFactory));
        }
        else {
            super.setFactory(factory);
        }
    }
    
    @TargetApi(11)
    public void setFactory2(final LayoutInflater$Factory2 factory2) {
        if (!(factory2 instanceof WrapperFactory2)) {
            super.setFactory2((LayoutInflater$Factory2)new WrapperFactory2(factory2, this.mCalligraphyFactory));
        }
        else {
            super.setFactory2(factory2);
        }
    }
    
    @TargetApi(11)
    private static class PrivateWrapperFactory2 extends WrapperFactory2
    {
        private final CalligraphyLayoutInflater mInflater;
        
        public PrivateWrapperFactory2(final LayoutInflater$Factory2 layoutInflater$Factory2, final CalligraphyLayoutInflater mInflater, final CalligraphyFactory calligraphyFactory) {
            super(layoutInflater$Factory2, calligraphyFactory);
            this.mInflater = mInflater;
        }
        
        @Override
        public View onCreateView(final View view, final String s, final Context context, final AttributeSet set) {
            return this.mCalligraphyFactory.onViewCreated(this.mInflater.createCustomViewInternal(view, this.mFactory2.onCreateView(view, s, context, set), s, context, set), context, set);
        }
    }
    
    private static class WrapperFactory implements LayoutInflater$Factory
    {
        private final CalligraphyFactory mCalligraphyFactory;
        private final LayoutInflater$Factory mFactory;
        private final CalligraphyLayoutInflater mInflater;
        
        public WrapperFactory(final LayoutInflater$Factory mFactory, final CalligraphyLayoutInflater mInflater, final CalligraphyFactory mCalligraphyFactory) {
            this.mFactory = mFactory;
            this.mInflater = mInflater;
            this.mCalligraphyFactory = mCalligraphyFactory;
        }
        
        public View onCreateView(final String s, final Context context, final AttributeSet set) {
            View view;
            if (Build$VERSION.SDK_INT < 11) {
                view = this.mCalligraphyFactory.onViewCreated(this.mInflater.createCustomViewInternal(null, this.mFactory.onCreateView(s, context, set), s, context, set), context, set);
            }
            else {
                view = this.mCalligraphyFactory.onViewCreated(this.mFactory.onCreateView(s, context, set), context, set);
            }
            return view;
        }
    }
    
    @TargetApi(11)
    private static class WrapperFactory2 implements LayoutInflater$Factory2
    {
        protected final CalligraphyFactory mCalligraphyFactory;
        protected final LayoutInflater$Factory2 mFactory2;
        
        public WrapperFactory2(final LayoutInflater$Factory2 mFactory2, final CalligraphyFactory mCalligraphyFactory) {
            this.mFactory2 = mFactory2;
            this.mCalligraphyFactory = mCalligraphyFactory;
        }
        
        public View onCreateView(final View view, final String s, final Context context, final AttributeSet set) {
            return this.mCalligraphyFactory.onViewCreated(this.mFactory2.onCreateView(view, s, context, set), context, set);
        }
        
        public View onCreateView(final String s, final Context context, final AttributeSet set) {
            return this.mCalligraphyFactory.onViewCreated(this.mFactory2.onCreateView(s, context, set), context, set);
        }
    }
}
