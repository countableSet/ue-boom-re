// 
// Decompiled by Procyon v0.5.36
// 

package butterknife.internal;

import java.util.Arrays;
import java.util.Set;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class BindingClass
{
    private final String className;
    private final String classPackage;
    private final Map<FieldCollectionViewBinding, int[]> collectionBindings;
    private String parentViewBinder;
    private final List<FieldResourceBinding> resourceBindings;
    private final String targetClass;
    private final Map<Integer, ViewBindings> viewIdMap;
    
    BindingClass(final String classPackage, final String className, final String targetClass) {
        this.viewIdMap = new LinkedHashMap<Integer, ViewBindings>();
        this.collectionBindings = new LinkedHashMap<FieldCollectionViewBinding, int[]>();
        this.resourceBindings = new ArrayList<FieldResourceBinding>();
        this.classPackage = classPackage;
        this.className = className;
        this.targetClass = targetClass;
    }
    
    private void emitBindMethod(final StringBuilder sb) {
        sb.append("  @Override ").append("public void bind(final Finder finder, final T target, Object source) {\n");
        if (this.parentViewBinder != null) {
            sb.append("    super.bind(finder, target, source);\n\n");
        }
        if (!this.viewIdMap.isEmpty() || !this.collectionBindings.isEmpty()) {
            sb.append("    View view;\n");
            final Iterator<ViewBindings> iterator = this.viewIdMap.values().iterator();
            while (iterator.hasNext()) {
                this.emitViewBindings(sb, iterator.next());
            }
            for (final Map.Entry<FieldCollectionViewBinding, int[]> entry : this.collectionBindings.entrySet()) {
                this.emitCollectionBinding(sb, entry.getKey(), entry.getValue());
            }
        }
        if (!this.resourceBindings.isEmpty()) {
            sb.append("    Resources res = finder.getContext(source).getResources();\n");
            for (final FieldResourceBinding fieldResourceBinding : this.resourceBindings) {
                sb.append("    target.").append(fieldResourceBinding.getName()).append(" = res.").append(fieldResourceBinding.getMethod()).append('(').append(fieldResourceBinding.getId()).append(");\n");
            }
        }
        sb.append("  }\n");
    }
    
    private void emitCollectionBinding(final StringBuilder sb, final FieldCollectionViewBinding o, final int[] array) {
        sb.append("    target.").append(o.getName()).append(" = ");
        switch (o.getKind()) {
            default: {
                throw new IllegalStateException("Unknown kind: " + o.getKind());
            }
            case ARRAY: {
                sb.append("Finder.arrayOf(");
                break;
            }
            case LIST: {
                sb.append("Finder.listOf(");
                break;
            }
        }
        for (int i = 0; i < array.length; ++i) {
            if (i > 0) {
                sb.append(',');
            }
            final StringBuilder append = sb.append("\n        finder.<").append(o.getType()).append(">");
            String str;
            if (o.isRequired()) {
                str = "findRequiredView";
            }
            else {
                str = "findOptionalView";
            }
            append.append(str).append("(source, ").append(array[i]).append(", \"");
            emitHumanDescription(sb, Collections.singleton(o));
            sb.append("\")");
        }
        sb.append("\n    );\n");
    }
    
    private void emitFieldBindings(final StringBuilder sb, final ViewBindings viewBindings) {
        final Collection<FieldViewBinding> fieldBindings = viewBindings.getFieldBindings();
        if (!fieldBindings.isEmpty()) {
            for (final FieldViewBinding fieldViewBinding : fieldBindings) {
                sb.append("    target.").append(fieldViewBinding.getName()).append(" = ");
                if (fieldViewBinding.requiresCast()) {
                    sb.append("finder.castView(view").append(", ").append(viewBindings.getId()).append(", \"");
                    emitHumanDescription(sb, fieldBindings);
                    sb.append("\");\n");
                }
                else {
                    sb.append("view;\n");
                }
            }
        }
    }
    
    static void emitHumanDescription(final StringBuilder sb, final Collection<? extends ViewBinding> collection) {
        final Iterator<? extends ViewBinding> iterator = collection.iterator();
        switch (collection.size()) {
            default: {
                for (int i = 0, size = collection.size(); i < size; ++i) {
                    if (i != 0) {
                        sb.append(", ");
                    }
                    if (i == size - 1) {
                        sb.append("and ");
                    }
                    sb.append(((ViewBinding)iterator.next()).getDescription());
                }
                break;
            }
            case 1: {
                sb.append(((ViewBinding)iterator.next()).getDescription());
                break;
            }
            case 2: {
                sb.append(((ViewBinding)iterator.next()).getDescription()).append(" and ").append(((ViewBinding)iterator.next()).getDescription());
                break;
            }
        }
    }
    
    private void emitMethodBindings(final StringBuilder sb, final ViewBindings viewBindings) {
        final Map<ListenerClass, Map<ListenerMethod, Set<MethodViewBinding>>> methodBindings = viewBindings.getMethodBindings();
        if (!methodBindings.isEmpty()) {
            final String s = "";
            final boolean empty = viewBindings.getRequiredBindings().isEmpty();
            String s2 = s;
            if (empty) {
                sb.append("    if (view != null) {\n");
                s2 = "  ";
            }
            for (final Map.Entry<ListenerClass, Map<ListenerMethod, Set<MethodViewBinding>>> entry : methodBindings.entrySet()) {
                final ListenerClass listenerClass = entry.getKey();
                final Map<ListenerMethod, Set<MethodViewBinding>> map = entry.getValue();
                boolean b;
                if (!"android.view.View".equals(listenerClass.targetType())) {
                    b = true;
                }
                else {
                    b = false;
                }
                sb.append(s2).append("    ");
                if (b) {
                    sb.append("((").append(listenerClass.targetType());
                    if (listenerClass.genericArguments() > 0) {
                        sb.append('<');
                        for (int i = 0; i < listenerClass.genericArguments(); ++i) {
                            if (i > 0) {
                                sb.append(", ");
                            }
                            sb.append('?');
                        }
                        sb.append('>');
                    }
                    sb.append(") ");
                }
                sb.append("view");
                if (b) {
                    sb.append(')');
                }
                sb.append('.').append(listenerClass.setter()).append("(\n");
                sb.append(s2).append("      new ").append(listenerClass.type()).append("() {\n");
                for (final ListenerMethod listenerMethod : getListenerMethods(listenerClass)) {
                    sb.append(s2).append("        @Override public ").append(listenerMethod.returnType()).append(' ').append(listenerMethod.name()).append("(\n");
                    final String[] parameters = listenerMethod.parameters();
                    for (int j = 0, length = parameters.length; j < length; ++j) {
                        sb.append(s2).append("          ").append(parameters[j]).append(" p").append(j);
                        if (j < length - 1) {
                            sb.append(',');
                        }
                        sb.append('\n');
                    }
                    sb.append(s2).append("        ) {\n");
                    sb.append(s2).append("          ");
                    boolean b2;
                    if (!"void".equals(listenerMethod.returnType())) {
                        b2 = true;
                    }
                    else {
                        b2 = false;
                    }
                    if (b2) {
                        sb.append("return ");
                    }
                    if (map.containsKey(listenerMethod)) {
                        final Iterator<MethodViewBinding> iterator3 = map.get(listenerMethod).iterator();
                        while (iterator3.hasNext()) {
                            final MethodViewBinding methodViewBinding = iterator3.next();
                            sb.append("target.").append(methodViewBinding.getName()).append('(');
                            final List<Parameter> parameters2 = methodViewBinding.getParameters();
                            final String[] parameters3 = listenerMethod.parameters();
                            for (int k = 0, size = parameters2.size(); k < size; ++k) {
                                final Parameter parameter = parameters2.get(k);
                                final int listenerPosition = parameter.getListenerPosition();
                                if (parameter.requiresCast(parameters3[listenerPosition])) {
                                    sb.append("finder.<").append(parameter.getType()).append(">castParam(p").append(listenerPosition).append(", \"").append(listenerMethod.name()).append("\", ").append(listenerPosition).append(", \"").append(methodViewBinding.getName()).append("\", ").append(k).append(")");
                                }
                                else {
                                    sb.append('p').append(listenerPosition);
                                }
                                if (k < size - 1) {
                                    sb.append(", ");
                                }
                            }
                            sb.append(");");
                            if (iterator3.hasNext()) {
                                sb.append("\n").append("          ");
                            }
                        }
                    }
                    else if (b2) {
                        sb.append(listenerMethod.defaultReturn()).append(';');
                    }
                    sb.append('\n');
                    sb.append(s2).append("        }\n");
                }
                sb.append(s2).append("      });\n");
            }
            if (empty) {
                sb.append("    }\n");
            }
        }
    }
    
    private void emitUnbindMethod(final StringBuilder sb) {
        sb.append("  @Override public void unbind(T target) {\n");
        if (this.parentViewBinder != null) {
            sb.append("    super.unbind(target);\n\n");
        }
        final Iterator<ViewBindings> iterator = this.viewIdMap.values().iterator();
        while (iterator.hasNext()) {
            final Iterator<FieldViewBinding> iterator2 = iterator.next().getFieldBindings().iterator();
            while (iterator2.hasNext()) {
                sb.append("    target.").append(iterator2.next().getName()).append(" = null;\n");
            }
        }
        final Iterator<FieldCollectionViewBinding> iterator3 = this.collectionBindings.keySet().iterator();
        while (iterator3.hasNext()) {
            sb.append("    target.").append(iterator3.next().getName()).append(" = null;\n");
        }
        sb.append("  }\n");
    }
    
    private void emitViewBindings(final StringBuilder sb, final ViewBindings viewBindings) {
        sb.append("    view = ");
        final List<ViewBinding> requiredBindings = viewBindings.getRequiredBindings();
        if (requiredBindings.isEmpty()) {
            sb.append("finder.findOptionalView(source, ").append(viewBindings.getId()).append(", null);\n");
        }
        else if (viewBindings.getId() == -1) {
            sb.append("target;\n");
        }
        else {
            sb.append("finder.findRequiredView(source, ").append(viewBindings.getId()).append(", \"");
            emitHumanDescription(sb, requiredBindings);
            sb.append("\");\n");
        }
        this.emitFieldBindings(sb, viewBindings);
        this.emitMethodBindings(sb, viewBindings);
    }
    
    static List<ListenerMethod> getListenerMethods(final ListenerClass listenerClass) {
        int n = 0;
        List<ListenerMethod> list;
        if (listenerClass.method().length == 1) {
            list = Arrays.asList(listenerClass.method());
        }
        else {
            while (true) {
                while (true) {
                    ArrayList<ListenerMethod> list2;
                    ListenerMethod listenerMethod;
                    try {
                        list2 = new ArrayList<ListenerMethod>();
                        final Class<? extends Enum<?>> callbacks = listenerClass.callbacks();
                        final Enum[] array = (Enum[])callbacks.getEnumConstants();
                        final int length = array.length;
                        list = list2;
                        if (n >= length) {
                            break;
                        }
                        final Enum enum1 = array[n];
                        listenerMethod = callbacks.getField(enum1.name()).getAnnotation(ListenerMethod.class);
                        if (listenerMethod == null) {
                            throw new IllegalStateException(String.format("@%s's %s.%s missing @%s annotation.", callbacks.getEnclosingClass().getSimpleName(), callbacks.getSimpleName(), enum1.name(), ListenerMethod.class.getSimpleName()));
                        }
                    }
                    catch (NoSuchFieldException detailMessage) {
                        throw new AssertionError((Object)detailMessage);
                    }
                    list2.add(listenerMethod);
                    ++n;
                    continue;
                }
            }
        }
        return list;
    }
    
    private ViewBindings getOrCreateViewBindings(final int n) {
        ViewBindings viewBindings;
        if ((viewBindings = this.viewIdMap.get(n)) == null) {
            viewBindings = new ViewBindings(n);
            this.viewIdMap.put(n, viewBindings);
        }
        return viewBindings;
    }
    
    void addField(final int n, final FieldViewBinding fieldViewBinding) {
        this.getOrCreateViewBindings(n).addFieldBinding(fieldViewBinding);
    }
    
    void addFieldCollection(final int[] array, final FieldCollectionViewBinding fieldCollectionViewBinding) {
        this.collectionBindings.put(fieldCollectionViewBinding, array);
    }
    
    boolean addMethod(final int n, final ListenerClass listenerClass, final ListenerMethod listenerMethod, final MethodViewBinding methodViewBinding) {
        final ViewBindings orCreateViewBindings = this.getOrCreateViewBindings(n);
        boolean b;
        if (orCreateViewBindings.hasMethodBinding(listenerClass, listenerMethod) && !"void".equals(listenerMethod.returnType())) {
            b = false;
        }
        else {
            orCreateViewBindings.addMethodBinding(listenerClass, listenerMethod, methodViewBinding);
            b = true;
        }
        return b;
    }
    
    void addResource(final FieldResourceBinding fieldResourceBinding) {
        this.resourceBindings.add(fieldResourceBinding);
    }
    
    String brewJava() {
        final StringBuilder sb = new StringBuilder();
        sb.append("// Generated code from Butter Knife. Do not modify!\n");
        sb.append("package ").append(this.classPackage).append(";\n\n");
        if (!this.resourceBindings.isEmpty()) {
            sb.append("import android.content.res.Resources;\n");
        }
        if (!this.viewIdMap.isEmpty() || !this.collectionBindings.isEmpty()) {
            sb.append("import android.view.View;\n");
        }
        sb.append("import butterknife.ButterKnife.Finder;\n");
        if (this.parentViewBinder == null) {
            sb.append("import butterknife.ButterKnife.ViewBinder;\n");
        }
        sb.append('\n');
        sb.append("public class ").append(this.className);
        sb.append("<T extends ").append(this.targetClass).append(">");
        if (this.parentViewBinder != null) {
            sb.append(" extends ").append(this.parentViewBinder).append("<T>");
        }
        else {
            sb.append(" implements ViewBinder<T>");
        }
        sb.append(" {\n");
        this.emitBindMethod(sb);
        sb.append('\n');
        this.emitUnbindMethod(sb);
        sb.append("}\n");
        return sb.toString();
    }
    
    String getFqcn() {
        return this.classPackage + "." + this.className;
    }
    
    ViewBindings getViewBinding(final int i) {
        return this.viewIdMap.get(i);
    }
    
    void setParentViewBinder(final String parentViewBinder) {
        this.parentViewBinder = parentViewBinder;
    }
}
