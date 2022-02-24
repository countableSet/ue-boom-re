// 
// Decompiled by Procyon v0.5.36
// 

package butterknife;

import java.util.Arrays;
import android.content.Context;
import android.util.Log;
import android.app.Dialog;
import android.app.Activity;
import android.annotation.TargetApi;
import android.view.View;
import android.util.Property;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ButterKnife
{
    static final Map<Class<?>, ViewBinder<Object>> BINDERS;
    static final ViewBinder<Object> NOP_VIEW_BINDER;
    private static final String TAG = "ButterKnife";
    private static boolean debug;
    
    static {
        ButterKnife.debug = false;
        BINDERS = new LinkedHashMap<Class<?>, ViewBinder<Object>>();
        NOP_VIEW_BINDER = (ViewBinder)new ViewBinder<Object>() {
            @Override
            public void bind(final Finder finder, final Object o, final Object o2) {
            }
            
            @Override
            public void unbind(final Object o) {
            }
        };
    }
    
    private ButterKnife() {
        throw new AssertionError((Object)"No instances.");
    }
    
    @TargetApi(14)
    public static <T extends View, V> void apply(final List<T> list, final Property<? super T, V> property, final V v) {
        for (int i = 0; i < list.size(); ++i) {
            property.set((Object)list.get(i), (Object)v);
        }
    }
    
    public static <T extends View> void apply(final List<T> list, final Action<? super T> action) {
        for (int i = 0; i < list.size(); ++i) {
            action.apply(list.get(i), i);
        }
    }
    
    public static <T extends View, V> void apply(final List<T> list, final Setter<? super T, V> setter, final V v) {
        for (int i = 0; i < list.size(); ++i) {
            setter.set(list.get(i), v, i);
        }
    }
    
    public static void bind(final Activity activity) {
        bind(activity, activity, Finder.ACTIVITY);
    }
    
    public static void bind(final Dialog dialog) {
        bind(dialog, dialog, Finder.DIALOG);
    }
    
    public static void bind(final View view) {
        bind(view, view, Finder.VIEW);
    }
    
    public static void bind(final Object o, final Activity activity) {
        bind(o, activity, Finder.ACTIVITY);
    }
    
    public static void bind(final Object o, final Dialog dialog) {
        bind(o, dialog, Finder.DIALOG);
    }
    
    public static void bind(final Object o, final View view) {
        bind(o, view, Finder.VIEW);
    }
    
    static void bind(final Object o, final Object o2, final Finder finder) {
        final Class<?> class1 = o.getClass();
        try {
            if (ButterKnife.debug) {
                Log.d("ButterKnife", "Looking up view binder for " + class1.getName());
            }
            final ViewBinder<Object> viewBinderForClass = findViewBinderForClass(class1);
            if (viewBinderForClass != null) {
                viewBinderForClass.bind(finder, o, o2);
            }
        }
        catch (Exception cause) {
            throw new RuntimeException("Unable to bind views for " + class1.getName(), cause);
        }
    }
    
    public static <T extends View> T findById(final Activity activity, final int n) {
        return (T)activity.findViewById(n);
    }
    
    public static <T extends View> T findById(final Dialog dialog, final int n) {
        return (T)dialog.findViewById(n);
    }
    
    public static <T extends View> T findById(final View view, final int n) {
        return (T)view.findViewById(n);
    }
    
    private static ViewBinder<Object> findViewBinderForClass(Class<?> nop_VIEW_BINDER) throws IllegalAccessException, InstantiationException {
        final ViewBinder<Object> viewBinder = ButterKnife.BINDERS.get(nop_VIEW_BINDER);
        if (viewBinder != null) {
            if (ButterKnife.debug) {
                Log.d("ButterKnife", "HIT: Cached in view binder map.");
            }
            nop_VIEW_BINDER = viewBinder;
        }
        else {
            final String name = ((Class)nop_VIEW_BINDER).getName();
            if (name.startsWith("android.") || name.startsWith("java.")) {
                if (ButterKnife.debug) {
                    Log.d("ButterKnife", "MISS: Reached framework class. Abandoning search.");
                }
                nop_VIEW_BINDER = ButterKnife.NOP_VIEW_BINDER;
            }
            else {
                while (true) {
                    try {
                        ViewBinder<Object> viewBinderForClass;
                        final ViewBinder viewBinder2 = viewBinderForClass = (ViewBinder<Object>)Class.forName(name + "$$ViewBinder").newInstance();
                        if (ButterKnife.debug) {
                            Log.d("ButterKnife", "HIT: Loaded view binder class.");
                            viewBinderForClass = (ViewBinder<Object>)viewBinder2;
                        }
                        ButterKnife.BINDERS.put((Class<?>)nop_VIEW_BINDER, viewBinderForClass);
                        nop_VIEW_BINDER = viewBinderForClass;
                    }
                    catch (ClassNotFoundException ex) {
                        if (ButterKnife.debug) {
                            Log.d("ButterKnife", "Not found. Trying superclass " + ((Class<?>)nop_VIEW_BINDER).getSuperclass().getName());
                        }
                        final ViewBinder<Object> viewBinderForClass = findViewBinderForClass(((Class<?>)nop_VIEW_BINDER).getSuperclass());
                        continue;
                    }
                    break;
                }
            }
        }
        return (ViewBinder<Object>)nop_VIEW_BINDER;
    }
    
    public static void setDebug(final boolean debug) {
        ButterKnife.debug = debug;
    }
    
    public static void unbind(final Object o) {
        final Class<?> class1 = o.getClass();
        try {
            if (ButterKnife.debug) {
                Log.d("ButterKnife", "Looking up view binder for " + class1.getName());
            }
            final ViewBinder<Object> viewBinderForClass = findViewBinderForClass(class1);
            if (viewBinderForClass != null) {
                viewBinderForClass.unbind(o);
            }
        }
        catch (Exception cause) {
            throw new RuntimeException("Unable to unbind views for " + class1.getName(), cause);
        }
    }
    
    public interface Action<T extends View>
    {
        void apply(final T p0, final int p1);
    }
    
    public enum Finder
    {
        ACTIVITY {
            @Override
            protected View findView(final Object o, final int n) {
                return ((Activity)o).findViewById(n);
            }
            
            @Override
            public Context getContext(final Object o) {
                return (Context)o;
            }
        }, 
        DIALOG {
            @Override
            protected View findView(final Object o, final int n) {
                return ((Dialog)o).findViewById(n);
            }
            
            @Override
            public Context getContext(final Object o) {
                return ((Dialog)o).getContext();
            }
        }, 
        VIEW {
            @Override
            protected View findView(final Object o, final int n) {
                return ((View)o).findViewById(n);
            }
            
            @Override
            public Context getContext(final Object o) {
                return ((View)o).getContext();
            }
        };
        
        public static <T> T[] arrayOf(final T... array) {
            return (T[])filterNull((Object[])array);
        }
        
        private static <T> T[] filterNull(final T[] original) {
            int to = 0;
            int n;
            for (int i = 0; i < original.length; ++i, to = n) {
                final T t = original[i];
                n = to;
                if (t != null) {
                    original[to] = t;
                    n = to + 1;
                }
            }
            return Arrays.copyOfRange(original, 0, to);
        }
        
        public static <T> List<T> listOf(final T... array) {
            return new ImmutableList<T>(filterNull(array));
        }
        
        public <T> T castParam(final Object o, final String s, final int n, final String s2, final int n2) {
            return (T)o;
        }
        
        public <T> T castView(final View view, final int n, final String s) {
            return (T)view;
        }
        
        public <T> T findOptionalView(final Object o, final int n, final String s) {
            return this.castView(this.findView(o, n), n, s);
        }
        
        public <T> T findRequiredView(final Object o, final int i, final String str) {
            final Object optionalView = this.findOptionalView(o, i, str);
            if (optionalView == null) {
                throw new IllegalStateException("Required view '" + this.getContext(o).getResources().getResourceEntryName(i) + "' with ID " + i + " for " + str + " was not found. If this view is optional add '@Nullable' annotation.");
            }
            return (T)optionalView;
        }
        
        protected abstract View findView(final Object p0, final int p1);
        
        public abstract Context getContext(final Object p0);
    }
    
    public interface Setter<T extends View, V>
    {
        void set(final T p0, final V p1, final int p2);
    }
    
    public interface ViewBinder<T>
    {
        void bind(final Finder p0, final T p1, final Object p2);
        
        void unbind(final T p0);
    }
}
