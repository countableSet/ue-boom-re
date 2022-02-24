// 
// Decompiled by Procyon v0.5.36
// 

package butterknife.internal;

import java.io.IOException;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import java.lang.reflect.Method;
import javax.lang.model.element.VariableElement;
import java.util.BitSet;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.ArrayType;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import java.util.Iterator;
import butterknife.BindString;
import butterknife.BindInt;
import butterknife.BindDrawable;
import butterknife.BindDimen;
import butterknife.BindColor;
import butterknife.BindBool;
import butterknife.Bind;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;
import javax.lang.model.element.TypeElement;
import java.util.Map;
import javax.annotation.processing.RoundEnvironment;
import javax.tools.Diagnostic;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import butterknife.OnTouch;
import butterknife.OnTextChanged;
import butterknife.OnPageChange;
import butterknife.OnLongClick;
import butterknife.OnItemSelected;
import butterknife.OnItemLongClick;
import butterknife.OnItemClick;
import butterknife.OnFocusChange;
import butterknife.OnEditorAction;
import butterknife.OnClick;
import butterknife.OnCheckedChanged;
import javax.lang.model.util.Types;
import javax.annotation.processing.Filer;
import javax.lang.model.util.Elements;
import java.lang.annotation.Annotation;
import java.util.List;
import javax.annotation.processing.AbstractProcessor;

public final class ButterKnifeProcessor extends AbstractProcessor
{
    public static final String ANDROID_PREFIX = "android.";
    private static final String COLOR_STATE_LIST_TYPE = "android.content.res.ColorStateList";
    private static final String DRAWABLE_TYPE = "android.graphics.drawable.Drawable";
    private static final String ITERABLE_TYPE = "java.lang.Iterable<?>";
    public static final String JAVA_PREFIX = "java.";
    private static final List<Class<? extends Annotation>> LISTENERS;
    private static final String LIST_TYPE;
    private static final String NULLABLE_ANNOTATION_NAME = "Nullable";
    public static final String SUFFIX = "$$ViewBinder";
    static final String VIEW_TYPE = "android.view.View";
    private Elements elementUtils;
    private Filer filer;
    private Types typeUtils;
    
    static {
        LIST_TYPE = List.class.getCanonicalName();
        LISTENERS = Arrays.asList(OnCheckedChanged.class, OnClick.class, OnEditorAction.class, OnFocusChange.class, OnItemClick.class, OnItemLongClick.class, OnItemSelected.class, OnLongClick.class, OnPageChange.class, OnTextChanged.class, OnTouch.class);
    }
    
    private String doubleErasure(final TypeMirror typeMirror) {
        final String string = this.typeUtils.erasure(typeMirror).toString();
        final int index = string.indexOf(60);
        String substring = string;
        if (index != -1) {
            substring = string.substring(0, index);
        }
        return substring;
    }
    
    private void error(final Element element, final String format, final Object... args) {
        String format2 = format;
        if (args.length > 0) {
            format2 = String.format(format, args);
        }
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, format2, element);
    }
    
    private void findAndParseListener(RoundEnvironment iterator, final Class<? extends Annotation> clazz, final Map<TypeElement, BindingClass> map, final Set<String> set) {
        iterator = (RoundEnvironment)iterator.getElementsAnnotatedWith(clazz).iterator();
        while (((Iterator)iterator).hasNext()) {
            final Element element = ((Iterator<Element>)iterator).next();
            try {
                this.parseListenerAnnotation(clazz, element, map, set);
            }
            catch (Exception ex) {
                final StringWriter out = new StringWriter();
                ex.printStackTrace(new PrintWriter(out));
                this.error(element, "Unable to generate view binder for @%s.\n\n%s", clazz.getSimpleName(), out.toString());
            }
        }
    }
    
    private Map<TypeElement, BindingClass> findAndParseTargets(final RoundEnvironment roundEnvironment) {
        final LinkedHashMap<TypeElement, BindingClass> linkedHashMap = new LinkedHashMap<TypeElement, BindingClass>();
        final LinkedHashSet<String> set = new LinkedHashSet<String>();
        for (final Element element : roundEnvironment.getElementsAnnotatedWith(Bind.class)) {
            try {
                this.parseBind(element, linkedHashMap, set);
            }
            catch (Exception ex) {
                this.logParsingError(element, Bind.class, ex);
            }
        }
        final Iterator<Class<? extends Annotation>> iterator2 = ButterKnifeProcessor.LISTENERS.iterator();
        while (iterator2.hasNext()) {
            this.findAndParseListener(roundEnvironment, iterator2.next(), linkedHashMap, set);
        }
        for (final Element element2 : roundEnvironment.getElementsAnnotatedWith(BindBool.class)) {
            try {
                this.parseResourceBool(element2, linkedHashMap, set);
            }
            catch (Exception ex2) {
                this.logParsingError(element2, BindBool.class, ex2);
            }
        }
        for (final Element element3 : roundEnvironment.getElementsAnnotatedWith(BindColor.class)) {
            try {
                this.parseResourceColor(element3, linkedHashMap, set);
            }
            catch (Exception ex3) {
                this.logParsingError(element3, BindColor.class, ex3);
            }
        }
        for (final Element element4 : roundEnvironment.getElementsAnnotatedWith(BindDimen.class)) {
            try {
                this.parseResourceDimen(element4, linkedHashMap, set);
            }
            catch (Exception ex4) {
                this.logParsingError(element4, BindDimen.class, ex4);
            }
        }
        for (final Element element5 : roundEnvironment.getElementsAnnotatedWith(BindDrawable.class)) {
            try {
                this.parseResourceDrawable(element5, linkedHashMap, set);
            }
            catch (Exception ex5) {
                this.logParsingError(element5, BindDrawable.class, ex5);
            }
        }
        for (final Element element6 : roundEnvironment.getElementsAnnotatedWith(BindInt.class)) {
            try {
                this.parseResourceInt(element6, linkedHashMap, set);
            }
            catch (Exception ex6) {
                this.logParsingError(element6, BindInt.class, ex6);
            }
        }
        for (final Element element7 : roundEnvironment.getElementsAnnotatedWith(BindString.class)) {
            try {
                this.parseResourceString(element7, linkedHashMap, set);
            }
            catch (Exception ex7) {
                this.logParsingError(element7, BindString.class, ex7);
            }
        }
        for (final Map.Entry<TypeElement, V> entry : linkedHashMap.entrySet()) {
            final String parentFqcn = this.findParentFqcn(entry.getKey(), set);
            if (parentFqcn != null) {
                ((BindingClass)entry.getValue()).setParentViewBinder(parentFqcn + "$$ViewBinder");
            }
        }
        return linkedHashMap;
    }
    
    private static Integer findDuplicate(final int[] array) {
        final LinkedHashSet<Integer> set = new LinkedHashSet<Integer>();
        for (final int n : array) {
            if (!set.add(n)) {
                return n;
            }
        }
        return null;
    }
    
    private String findParentFqcn(TypeElement typeElement, final Set<String> set) {
        TypeElement typeElement2;
        do {
            final TypeMirror superclass = typeElement.getSuperclass();
            if (superclass.getKind() == TypeKind.NONE) {
                return null;
            }
            typeElement2 = (typeElement = (TypeElement)((DeclaredType)superclass).asElement());
        } while (!set.contains(typeElement2.toString()));
        final String packageName = this.getPackageName(typeElement2);
        return packageName + "." + getClassName(typeElement2, packageName);
    }
    
    private static String getClassName(final TypeElement typeElement, final String s) {
        return typeElement.getQualifiedName().toString().substring(s.length() + 1).replace('.', '$');
    }
    
    private BindingClass getOrCreateTargetClass(final Map<TypeElement, BindingClass> map, final TypeElement typeElement) {
        BindingClass bindingClass;
        if ((bindingClass = map.get(typeElement)) == null) {
            final String string = typeElement.getQualifiedName().toString();
            final String packageName = this.getPackageName(typeElement);
            bindingClass = new BindingClass(packageName, getClassName(typeElement, packageName) + "$$ViewBinder", string);
            map.put(typeElement, bindingClass);
        }
        return bindingClass;
    }
    
    private String getPackageName(final TypeElement typeElement) {
        return this.elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
    }
    
    private static boolean hasAnnotationWithName(final Element element, final String s) {
        final Iterator<? extends AnnotationMirror> iterator = element.getAnnotationMirrors().iterator();
        while (iterator.hasNext()) {
            if (s.equals(((AnnotationMirror)iterator.next()).getAnnotationType().asElement().getSimpleName().toString())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isBindingInWrongPackage(final Class<? extends Annotation> clazz, final Element element) {
        boolean b = true;
        final String string = ((TypeElement)element.getEnclosingElement()).getQualifiedName().toString();
        if (string.startsWith("android.")) {
            this.error(element, "@%s-annotated class incorrectly in Android framework package. (%s)", clazz.getSimpleName(), string);
        }
        else if (string.startsWith("java.")) {
            this.error(element, "@%s-annotated class incorrectly in Java framework package. (%s)", clazz.getSimpleName(), string);
        }
        else {
            b = false;
        }
        return b;
    }
    
    private boolean isInaccessibleViaGeneratedCode(final Class<? extends Annotation> clazz, final String s, final Element element) {
        boolean b = false;
        final TypeElement typeElement = (TypeElement)element.getEnclosingElement();
        final Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.STATIC)) {
            this.error(element, "@%s %s must not be private or static. (%s.%s)", clazz.getSimpleName(), s, typeElement.getQualifiedName(), element.getSimpleName());
            b = true;
        }
        if (typeElement.getKind() != ElementKind.CLASS) {
            this.error(typeElement, "@%s %s may only be contained in classes. (%s.%s)", clazz.getSimpleName(), s, typeElement.getQualifiedName(), element.getSimpleName());
            b = true;
        }
        if (typeElement.getModifiers().contains(Modifier.PRIVATE)) {
            this.error(typeElement, "@%s %s may not be contained in private classes. (%s.%s)", clazz.getSimpleName(), s, typeElement.getQualifiedName(), element.getSimpleName());
            b = true;
        }
        return b;
    }
    
    private boolean isInterface(final TypeMirror typeMirror) {
        boolean b = false;
        if (typeMirror instanceof DeclaredType && ((DeclaredType)typeMirror).asElement().getKind() == ElementKind.INTERFACE) {
            b = true;
        }
        return b;
    }
    
    private static boolean isRequiredBinding(final Element element) {
        return !hasAnnotationWithName(element, "Nullable");
    }
    
    private boolean isSubtypeOfType(final TypeMirror typeMirror, final String anObject) {
        final boolean b = true;
        boolean b2;
        if (anObject.equals(typeMirror.toString())) {
            b2 = b;
        }
        else if (typeMirror.getKind() != TypeKind.DECLARED) {
            b2 = false;
        }
        else {
            final DeclaredType declaredType = (DeclaredType)typeMirror;
            final List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
            if (typeArguments.size() > 0) {
                final StringBuilder sb = new StringBuilder(declaredType.asElement().toString());
                sb.append('<');
                for (int i = 0; i < typeArguments.size(); ++i) {
                    if (i > 0) {
                        sb.append(',');
                    }
                    sb.append('?');
                }
                sb.append('>');
                b2 = b;
                if (sb.toString().equals(anObject)) {
                    return b2;
                }
            }
            final Element element = declaredType.asElement();
            if (!(element instanceof TypeElement)) {
                b2 = false;
            }
            else {
                final TypeElement typeElement = (TypeElement)element;
                b2 = b;
                if (!this.isSubtypeOfType(typeElement.getSuperclass(), anObject)) {
                    final Iterator<? extends TypeMirror> iterator = typeElement.getInterfaces().iterator();
                    while (iterator.hasNext()) {
                        if (this.isSubtypeOfType((TypeMirror)iterator.next(), anObject)) {
                            b2 = b;
                            return b2;
                        }
                    }
                    b2 = false;
                }
            }
        }
        return b2;
    }
    
    private void logParsingError(final Element element, final Class<? extends Annotation> clazz, final Exception ex) {
        final StringWriter out = new StringWriter();
        ex.printStackTrace(new PrintWriter(out));
        this.error(element, "Unable to parse @%s binding.\n\n%s", clazz.getSimpleName(), out);
    }
    
    private void parseBind(final Element element, final Map<TypeElement, BindingClass> map, final Set<String> set) {
        if (!this.isInaccessibleViaGeneratedCode(Bind.class, "fields", element) && !this.isBindingInWrongPackage(Bind.class, element)) {
            final TypeMirror type = element.asType();
            if (type.getKind() == TypeKind.ARRAY) {
                this.parseBindMany(element, map, set);
            }
            else if (ButterKnifeProcessor.LIST_TYPE.equals(this.doubleErasure(type))) {
                this.parseBindMany(element, map, set);
            }
            else if (this.isSubtypeOfType(type, "java.lang.Iterable<?>")) {
                this.error(element, "@%s must be a List or array. (%s.%s)", Bind.class.getSimpleName(), ((TypeElement)element.getEnclosingElement()).getQualifiedName(), element.getSimpleName());
            }
            else {
                this.parseBindOne(element, map, set);
            }
        }
    }
    
    private void parseBindMany(final Element element, final Map<TypeElement, BindingClass> map, final Set<String> set) {
        final int n = 0;
        int n2 = 0;
        final TypeElement typeElement = (TypeElement)element.getEnclosingElement();
        final TypeMirror type = element.asType();
        final String doubleErasure = this.doubleErasure(type);
        TypeMirror componentType = null;
        FieldCollectionViewBinding.Kind kind;
        if (type.getKind() == TypeKind.ARRAY) {
            componentType = ((ArrayType)type).getComponentType();
            kind = FieldCollectionViewBinding.Kind.ARRAY;
        }
        else {
            if (!ButterKnifeProcessor.LIST_TYPE.equals(doubleErasure)) {
                throw new AssertionError();
            }
            final List<? extends TypeMirror> typeArguments = ((DeclaredType)type).getTypeArguments();
            if (typeArguments.size() != 1) {
                this.error(element, "@%s List must have a generic component. (%s.%s)", Bind.class.getSimpleName(), typeElement.getQualifiedName(), element.getSimpleName());
                n2 = 1;
            }
            else {
                componentType = (TypeMirror)typeArguments.get(0);
                n2 = n;
            }
            kind = FieldCollectionViewBinding.Kind.LIST;
        }
        TypeMirror upperBound = componentType;
        if (componentType != null) {
            upperBound = componentType;
            if (componentType.getKind() == TypeKind.TYPEVAR) {
                upperBound = ((TypeVariable)componentType).getUpperBound();
            }
        }
        int n3 = n2;
        if (upperBound != null) {
            n3 = n2;
            if (!this.isSubtypeOfType(upperBound, "android.view.View")) {
                n3 = n2;
                if (!this.isInterface(upperBound)) {
                    this.error(element, "@%s List or array type must extend from View or be an interface. (%s.%s)", Bind.class.getSimpleName(), typeElement.getQualifiedName(), element.getSimpleName());
                    n3 = 1;
                }
            }
        }
        if (n3 == 0) {
            final String string = element.getSimpleName().toString();
            final int[] value = element.getAnnotation(Bind.class).value();
            if (value.length == 0) {
                this.error(element, "@%s must specify at least one ID. (%s.%s)", Bind.class.getSimpleName(), typeElement.getQualifiedName(), element.getSimpleName());
            }
            else {
                final Integer duplicate = findDuplicate(value);
                if (duplicate != null) {
                    this.error(element, "@%s annotation contains duplicate ID %d. (%s.%s)", Bind.class.getSimpleName(), duplicate, typeElement.getQualifiedName(), element.getSimpleName());
                }
                assert upperBound != null;
                this.getOrCreateTargetClass(map, typeElement).addFieldCollection(value, new FieldCollectionViewBinding(string, upperBound.toString(), kind, isRequiredBinding(element)));
                set.add(typeElement.toString());
            }
        }
    }
    
    private void parseBindOne(final Element element, final Map<TypeElement, BindingClass> map, final Set<String> set) {
        final boolean b = false;
        final TypeElement typeElement = (TypeElement)element.getEnclosingElement();
        TypeMirror typeMirror2;
        final TypeMirror typeMirror = typeMirror2 = element.asType();
        if (typeMirror.getKind() == TypeKind.TYPEVAR) {
            typeMirror2 = ((TypeVariable)typeMirror).getUpperBound();
        }
        int n = b ? 1 : 0;
        if (!this.isSubtypeOfType(typeMirror2, "android.view.View")) {
            n = (b ? 1 : 0);
            if (!this.isInterface(typeMirror2)) {
                this.error(element, "@%s fields must extend from View or be an interface. (%s.%s)", Bind.class.getSimpleName(), typeElement.getQualifiedName(), element.getSimpleName());
                n = 1;
            }
        }
        final int[] value = element.getAnnotation(Bind.class).value();
        if (value.length != 1) {
            this.error(element, "@%s for a view must only specify one ID. Found: %s. (%s.%s)", Bind.class.getSimpleName(), Arrays.toString(value), typeElement.getQualifiedName(), element.getSimpleName());
            n = 1;
        }
        if (n == 0) {
            final int i = value[0];
            final BindingClass bindingClass = map.get(typeElement);
            BindingClass orCreateTargetClass;
            if (bindingClass != null) {
                final ViewBindings viewBinding = bindingClass.getViewBinding(i);
                orCreateTargetClass = bindingClass;
                if (viewBinding != null) {
                    final Iterator<FieldViewBinding> iterator = viewBinding.getFieldBindings().iterator();
                    orCreateTargetClass = bindingClass;
                    if (iterator.hasNext()) {
                        this.error(element, "Attempt to use @%s for an already bound ID %d on '%s'. (%s.%s)", Bind.class.getSimpleName(), i, iterator.next().getName(), typeElement.getQualifiedName(), element.getSimpleName());
                        return;
                    }
                }
            }
            else {
                orCreateTargetClass = this.getOrCreateTargetClass(map, typeElement);
            }
            orCreateTargetClass.addField(i, new FieldViewBinding(element.getSimpleName().toString(), typeMirror2.toString(), isRequiredBinding(element)));
            set.add(typeElement.toString());
        }
    }
    
    private void parseListenerAnnotation(final Class<? extends Annotation> clazz, final Element element, final Map<TypeElement, BindingClass> map, final Set<String> set) throws Exception {
        if (!(element instanceof ExecutableElement) || element.getKind() != ElementKind.METHOD) {
            throw new IllegalStateException(String.format("@%s annotation must be on a method.", clazz.getSimpleName()));
        }
        final ExecutableElement executableElement = (ExecutableElement)element;
        final TypeElement typeElement = (TypeElement)element.getEnclosingElement();
        final Annotation annotation = element.getAnnotation(clazz);
        final Method declaredMethod = clazz.getDeclaredMethod("value", (Class[])new Class[0]);
        if (declaredMethod.getReturnType() != int[].class) {
            throw new IllegalStateException(String.format("@%s annotation value() type not int[].", clazz));
        }
        final int[] array = (int[])declaredMethod.invoke(annotation, new Object[0]);
        final String string = executableElement.getSimpleName().toString();
        final boolean requiredBinding = isRequiredBinding(element);
        int n = (this.isInaccessibleViaGeneratedCode(clazz, "methods", element) | this.isBindingInWrongPackage(clazz, element)) ? 1 : 0;
        final Integer duplicate = findDuplicate(array);
        if (duplicate != null) {
            this.error(element, "@%s annotation for method contains duplicate ID %d. (%s.%s)", clazz.getSimpleName(), duplicate, typeElement.getQualifiedName(), element.getSimpleName());
            n = 1;
        }
        final ListenerClass listenerClass = clazz.getAnnotation(ListenerClass.class);
        if (listenerClass == null) {
            throw new IllegalStateException(String.format("No @%s defined on @%s.", ListenerClass.class.getSimpleName(), clazz.getSimpleName()));
        }
        int n2;
        for (int length = array.length, i = 0; i < length; ++i, n = n2) {
            final int j = array[i];
            n2 = n;
            if (j == -1) {
                if (array.length == 1) {
                    if (!requiredBinding) {
                        this.error(element, "ID-free binding must not be annotated with @Nullable. (%s.%s)", typeElement.getQualifiedName(), element.getSimpleName());
                        n = 1;
                    }
                    final String targetType = listenerClass.targetType();
                    n2 = n;
                    if (!this.isSubtypeOfType(typeElement.asType(), targetType)) {
                        n2 = n;
                        if (!this.isInterface(typeElement.asType())) {
                            this.error(element, "@%s annotation without an ID may only be used with an object of type \"%s\" or an interface. (%s.%s)", clazz.getSimpleName(), targetType, typeElement.getQualifiedName(), element.getSimpleName());
                            n2 = 1;
                        }
                    }
                }
                else {
                    this.error(element, "@%s annotation contains invalid ID %d. (%s.%s)", clazz.getSimpleName(), j, typeElement.getQualifiedName(), element.getSimpleName());
                    n2 = 1;
                }
            }
        }
        final ListenerMethod[] method = listenerClass.method();
        if (method.length > 1) {
            throw new IllegalStateException(String.format("Multiple listener methods specified on @%s.", clazz.getSimpleName()));
        }
        ListenerMethod listenerMethod;
        if (method.length == 1) {
            if (listenerClass.callbacks() != ListenerClass.NONE.class) {
                throw new IllegalStateException(String.format("Both method() and callback() defined on @%s.", clazz.getSimpleName()));
            }
            listenerMethod = method[0];
        }
        else {
            final Enum enum1 = (Enum)clazz.getDeclaredMethod("callback", (Class<?>[])new Class[0]).invoke(annotation, new Object[0]);
            if ((listenerMethod = enum1.getDeclaringClass().getField(enum1.name()).getAnnotation(ListenerMethod.class)) == null) {
                throw new IllegalStateException(String.format("No @%s defined on @%s's %s.%s.", ListenerMethod.class.getSimpleName(), clazz.getSimpleName(), enum1.getDeclaringClass().getSimpleName(), enum1.name()));
            }
        }
        final List<? extends VariableElement> parameters = executableElement.getParameters();
        if (parameters.size() > listenerMethod.parameters().length) {
            this.error(element, "@%s methods can have at most %s parameter(s). (%s.%s)", clazz.getSimpleName(), listenerMethod.parameters().length, typeElement.getQualifiedName(), element.getSimpleName());
            n = 1;
        }
        TypeMirror typeMirror2;
        final TypeMirror typeMirror = typeMirror2 = executableElement.getReturnType();
        if (typeMirror instanceof TypeVariable) {
            typeMirror2 = ((TypeVariable)typeMirror).getUpperBound();
        }
        if (!typeMirror2.toString().equals(listenerMethod.returnType())) {
            this.error(element, "@%s methods must have a '%s' return type. (%s.%s)", clazz.getSimpleName(), listenerMethod.returnType(), typeElement.getQualifiedName(), element.getSimpleName());
            n = 1;
        }
        if (n == 0) {
            Parameter[] none = Parameter.NONE;
            if (!parameters.isEmpty()) {
                final Parameter[] array2 = new Parameter[parameters.size()];
                final BitSet set2 = new BitSet(parameters.size());
                final String[] parameters2 = listenerMethod.parameters();
                int n3 = 0;
                while (true) {
                    none = array2;
                    if (n3 >= parameters.size()) {
                        break;
                    }
                    TypeMirror typeMirror4;
                    final TypeMirror typeMirror3 = typeMirror4 = ((VariableElement)parameters.get(n3)).asType();
                    if (typeMirror3 instanceof TypeVariable) {
                        typeMirror4 = ((TypeVariable)typeMirror3).getUpperBound();
                    }
                    for (int k = 0; k < parameters2.length; ++k) {
                        if (!set2.get(k) && (this.isSubtypeOfType(typeMirror4, parameters2[k]) || this.isInterface(typeMirror4))) {
                            array2[n3] = new Parameter(k, typeMirror4.toString());
                            set2.set(k);
                            break;
                        }
                    }
                    if (array2[n3] == null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Unable to match @").append(clazz.getSimpleName()).append(" method arguments. (").append(typeElement.getQualifiedName()).append('.').append(element.getSimpleName()).append(')');
                        for (int l = 0; l < array2.length; ++l) {
                            final Parameter parameter = array2[l];
                            sb.append("\n\n  Parameter #").append(l + 1).append(": ").append(((VariableElement)parameters.get(l)).asType().toString()).append("\n    ");
                            if (parameter == null) {
                                sb.append("did not match any listener parameters");
                            }
                            else {
                                sb.append("matched listener parameter #").append(parameter.getListenerPosition() + 1).append(": ").append(parameter.getType());
                            }
                        }
                        sb.append("\n\nMethods may have up to ").append(listenerMethod.parameters().length).append(" parameter(s):\n");
                        final String[] parameters3 = listenerMethod.parameters();
                        for (int length2 = parameters3.length, n4 = 0; n4 < length2; ++n4) {
                            sb.append("\n  ").append(parameters3[n4]);
                        }
                        sb.append("\n\nThese may be listed in any order but will be searched for from top to bottom.");
                        this.error(executableElement, sb.toString(), new Object[0]);
                        return;
                    }
                    ++n3;
                }
            }
            final MethodViewBinding methodViewBinding = new MethodViewBinding(string, Arrays.asList(none), requiredBinding);
            final BindingClass orCreateTargetClass = this.getOrCreateTargetClass(map, typeElement);
            for (final int m : array) {
                if (!orCreateTargetClass.addMethod(m, listenerClass, listenerMethod, methodViewBinding)) {
                    this.error(element, "Multiple listener methods with return value specified for ID %d. (%s.%s)", m, typeElement.getQualifiedName(), element.getSimpleName());
                    return;
                }
            }
            set.add(typeElement.toString());
        }
    }
    
    private void parseResourceBool(final Element element, final Map<TypeElement, BindingClass> map, final Set<String> set) {
        boolean b = false;
        final TypeElement typeElement = (TypeElement)element.getEnclosingElement();
        if (element.asType().getKind() != TypeKind.BOOLEAN) {
            this.error(element, "@%s field type must be 'boolean'. (%s.%s)", BindBool.class.getSimpleName(), typeElement.getQualifiedName(), element.getSimpleName());
            b = true;
        }
        if (!(b | this.isInaccessibleViaGeneratedCode(BindBool.class, "fields", element) | this.isBindingInWrongPackage(BindBool.class, element))) {
            this.getOrCreateTargetClass(map, typeElement).addResource(new FieldResourceBinding(element.getAnnotation(BindBool.class).value(), element.getSimpleName().toString(), "getBoolean"));
            set.add(typeElement.toString());
        }
    }
    
    private void parseResourceColor(final Element element, final Map<TypeElement, BindingClass> map, final Set<String> set) {
        boolean b = false;
        final TypeElement typeElement = (TypeElement)element.getEnclosingElement();
        final boolean b2 = false;
        final TypeMirror type = element.asType();
        int n;
        if ("android.content.res.ColorStateList".equals(type.toString())) {
            n = 1;
        }
        else {
            n = (b2 ? 1 : 0);
            if (type.getKind() != TypeKind.INT) {
                this.error(element, "@%s field type must be 'int' or 'ColorStateList'. (%s.%s)", BindColor.class.getSimpleName(), typeElement.getQualifiedName(), element.getSimpleName());
                b = true;
                n = (b2 ? 1 : 0);
            }
        }
        if (!(b | this.isInaccessibleViaGeneratedCode(BindColor.class, "fields", element) | this.isBindingInWrongPackage(BindColor.class, element))) {
            final String string = element.getSimpleName().toString();
            final int value = element.getAnnotation(BindColor.class).value();
            final BindingClass orCreateTargetClass = this.getOrCreateTargetClass(map, typeElement);
            String s;
            if (n != 0) {
                s = "getColorStateList";
            }
            else {
                s = "getColor";
            }
            orCreateTargetClass.addResource(new FieldResourceBinding(value, string, s));
            set.add(typeElement.toString());
        }
    }
    
    private void parseResourceDimen(final Element element, final Map<TypeElement, BindingClass> map, final Set<String> set) {
        boolean b = false;
        final TypeElement typeElement = (TypeElement)element.getEnclosingElement();
        final boolean b2 = false;
        final TypeMirror type = element.asType();
        int n;
        if (type.getKind() == TypeKind.INT) {
            n = 1;
        }
        else {
            n = (b2 ? 1 : 0);
            if (type.getKind() != TypeKind.FLOAT) {
                this.error(element, "@%s field type must be 'int' or 'float'. (%s.%s)", BindDimen.class.getSimpleName(), typeElement.getQualifiedName(), element.getSimpleName());
                b = true;
                n = (b2 ? 1 : 0);
            }
        }
        if (!(b | this.isInaccessibleViaGeneratedCode(BindDimen.class, "fields", element) | this.isBindingInWrongPackage(BindDimen.class, element))) {
            final String string = element.getSimpleName().toString();
            final int value = element.getAnnotation(BindDimen.class).value();
            final BindingClass orCreateTargetClass = this.getOrCreateTargetClass(map, typeElement);
            String s;
            if (n != 0) {
                s = "getDimensionPixelSize";
            }
            else {
                s = "getDimension";
            }
            orCreateTargetClass.addResource(new FieldResourceBinding(value, string, s));
            set.add(typeElement.toString());
        }
    }
    
    private void parseResourceDrawable(final Element element, final Map<TypeElement, BindingClass> map, final Set<String> set) {
        boolean b = false;
        final TypeElement typeElement = (TypeElement)element.getEnclosingElement();
        if (!"android.graphics.drawable.Drawable".equals(element.asType().toString())) {
            this.error(element, "@%s field type must be 'Drawable'. (%s.%s)", BindDrawable.class.getSimpleName(), typeElement.getQualifiedName(), element.getSimpleName());
            b = true;
        }
        if (!(b | this.isInaccessibleViaGeneratedCode(BindDrawable.class, "fields", element) | this.isBindingInWrongPackage(BindDrawable.class, element))) {
            this.getOrCreateTargetClass(map, typeElement).addResource(new FieldResourceBinding(element.getAnnotation(BindDrawable.class).value(), element.getSimpleName().toString(), "getDrawable"));
            set.add(typeElement.toString());
        }
    }
    
    private void parseResourceInt(final Element element, final Map<TypeElement, BindingClass> map, final Set<String> set) {
        boolean b = false;
        final TypeElement typeElement = (TypeElement)element.getEnclosingElement();
        if (element.asType().getKind() != TypeKind.INT) {
            this.error(element, "@%s field type must be 'int'. (%s.%s)", BindInt.class.getSimpleName(), typeElement.getQualifiedName(), element.getSimpleName());
            b = true;
        }
        if (!(b | this.isInaccessibleViaGeneratedCode(BindInt.class, "fields", element) | this.isBindingInWrongPackage(BindInt.class, element))) {
            this.getOrCreateTargetClass(map, typeElement).addResource(new FieldResourceBinding(element.getAnnotation(BindInt.class).value(), element.getSimpleName().toString(), "getInteger"));
            set.add(typeElement.toString());
        }
    }
    
    private void parseResourceString(final Element element, final Map<TypeElement, BindingClass> map, final Set<String> set) {
        boolean b = false;
        final TypeElement typeElement = (TypeElement)element.getEnclosingElement();
        if (!"java.lang.String".equals(element.asType().toString())) {
            this.error(element, "@%s field type must be 'String'. (%s.%s)", BindString.class.getSimpleName(), typeElement.getQualifiedName(), element.getSimpleName());
            b = true;
        }
        if (!(b | this.isInaccessibleViaGeneratedCode(BindString.class, "fields", element) | this.isBindingInWrongPackage(BindString.class, element))) {
            this.getOrCreateTargetClass(map, typeElement).addResource(new FieldResourceBinding(element.getAnnotation(BindString.class).value(), element.getSimpleName().toString(), "getString"));
            set.add(typeElement.toString());
        }
    }
    
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        final LinkedHashSet<String> set = new LinkedHashSet<String>();
        set.add(Bind.class.getCanonicalName());
        final Iterator<Class<? extends Annotation>> iterator = ButterKnifeProcessor.LISTENERS.iterator();
        while (iterator.hasNext()) {
            set.add(iterator.next().getCanonicalName());
        }
        set.add(BindBool.class.getCanonicalName());
        set.add(BindColor.class.getCanonicalName());
        set.add(BindDimen.class.getCanonicalName());
        set.add(BindDrawable.class.getCanonicalName());
        set.add(BindInt.class.getCanonicalName());
        set.add(BindString.class.getCanonicalName());
        return set;
    }
    
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
    
    @Override
    public void init(final ProcessingEnvironment processingEnv) {
        synchronized (this) {
            super.init(processingEnv);
            this.elementUtils = processingEnv.getElementUtils();
            this.typeUtils = processingEnv.getTypeUtils();
            this.filer = processingEnv.getFiler();
        }
    }
    
    @Override
    public boolean process(Set<? extends TypeElement> typeElement, RoundEnvironment iterator) {
        iterator = (RoundEnvironment)this.findAndParseTargets(iterator).entrySet().iterator();
        while (((Iterator)iterator).hasNext()) {
            final Map.Entry<TypeElement, V> entry = ((Iterator<Map.Entry<TypeElement, V>>)iterator).next();
            typeElement = entry.getKey();
            final BindingClass bindingClass = (BindingClass)entry.getValue();
            try {
                final Writer openWriter = this.filer.createSourceFile(bindingClass.getFqcn(), typeElement).openWriter();
                openWriter.write(bindingClass.brewJava());
                openWriter.flush();
                openWriter.close();
            }
            catch (IOException ex) {
                this.error(typeElement, "Unable to write view binder for type %s: %s", typeElement, ex.getMessage());
            }
        }
        return true;
    }
}
