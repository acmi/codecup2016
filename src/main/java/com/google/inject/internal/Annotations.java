/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.inject.BindingAnnotation;
import com.google.inject.Key;
import com.google.inject.ScopeAnnotation;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.util.Classes;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import javax.inject.Qualifier;
import javax.inject.Scope;

public class Annotations {
    private static final LoadingCache<Class<? extends Annotation>, Annotation> cache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<Class<? extends Annotation>, Annotation>(){

        @Override
        public Annotation load(Class<? extends Annotation> class_) {
            return Annotations.generateAnnotationImpl(class_);
        }
    });
    private static final Joiner.MapJoiner JOINER = Joiner.on(", ").withKeyValueSeparator("=");
    private static final Function<Object, String> DEEP_TO_STRING_FN = new Function<Object, String>(){

        @Override
        public String apply(Object object) {
            String string = Arrays.deepToString(new Object[]{object});
            return string.substring(1, string.length() - 1);
        }
    };
    private static final AnnotationChecker scopeChecker = new AnnotationChecker(Arrays.asList(ScopeAnnotation.class, Scope.class));
    private static final AnnotationChecker bindingAnnotationChecker = new AnnotationChecker(Arrays.asList(BindingAnnotation.class, Qualifier.class));

    public static boolean isMarker(Class<? extends Annotation> class_) {
        return class_.getDeclaredMethods().length == 0;
    }

    public static boolean isAllDefaultMethods(Class<? extends Annotation> class_) {
        boolean bl = false;
        for (Method method : class_.getDeclaredMethods()) {
            bl = true;
            if (method.getDefaultValue() != null) continue;
            return false;
        }
        return bl;
    }

    public static <T extends Annotation> T generateAnnotation(Class<T> class_) {
        Preconditions.checkState(Annotations.isAllDefaultMethods(class_), "%s is not all default methods", class_);
        return (T)cache.getUnchecked(class_);
    }

    private static <T extends Annotation> T generateAnnotationImpl(final Class<T> class_) {
        final ImmutableMap<String, Object> immutableMap = Annotations.resolveMembers(class_);
        return (T)((Annotation)class_.cast(Proxy.newProxyInstance(class_.getClassLoader(), new Class[]{class_}, new InvocationHandler(){

            @Override
            public Object invoke(Object object, Method method, Object[] arrobject) throws Exception {
                String string = method.getName();
                if (string.equals("annotationType")) {
                    return class_;
                }
                if (string.equals("toString")) {
                    return Annotations.annotationToString(class_, immutableMap);
                }
                if (string.equals("hashCode")) {
                    return Annotations.annotationHashCode(class_, immutableMap);
                }
                if (string.equals("equals")) {
                    return Annotations.annotationEquals(class_, immutableMap, arrobject[0]);
                }
                return immutableMap.get(string);
            }
        })));
    }

    private static ImmutableMap<String, Object> resolveMembers(Class<? extends Annotation> class_) {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        for (Method method : class_.getDeclaredMethods()) {
            builder.put(method.getName(), method.getDefaultValue());
        }
        return builder.build();
    }

    private static boolean annotationEquals(Class<? extends Annotation> class_, Map<String, Object> map, Object object) throws Exception {
        if (!class_.isInstance(object)) {
            return false;
        }
        for (Method method : class_.getDeclaredMethods()) {
            String string = method.getName();
            if (Arrays.deepEquals(new Object[]{method.invoke(object, new Object[0])}, new Object[]{map.get(string)})) continue;
            return false;
        }
        return true;
    }

    private static int annotationHashCode(Class<? extends Annotation> class_, Map<String, Object> map) throws Exception {
        int n2 = 0;
        for (Method method : class_.getDeclaredMethods()) {
            String string = method.getName();
            Object object = map.get(string);
            n2 += 127 * string.hashCode() ^ Arrays.deepHashCode(new Object[]{object}) - 31;
        }
        return n2;
    }

    private static String annotationToString(Class<? extends Annotation> class_, Map<String, Object> map) throws Exception {
        StringBuilder stringBuilder = new StringBuilder().append("@").append(class_.getName()).append("(");
        JOINER.appendTo(stringBuilder, Maps.transformValues(map, DEEP_TO_STRING_FN));
        return stringBuilder.append(")").toString();
    }

    public static boolean isRetainedAtRuntime(Class<? extends Annotation> class_) {
        Retention retention = class_.getAnnotation(Retention.class);
        return retention != null && retention.value() == RetentionPolicy.RUNTIME;
    }

    public static Class<? extends Annotation> findScopeAnnotation(Errors errors, Class<?> class_) {
        return Annotations.findScopeAnnotation(errors, class_.getAnnotations());
    }

    public static Class<? extends Annotation> findScopeAnnotation(Errors errors, Annotation[] arrannotation) {
        Class<? extends Annotation> class_ = null;
        for (Annotation annotation : arrannotation) {
            Class<? extends Annotation> class_2 = annotation.annotationType();
            if (!Annotations.isScopeAnnotation(class_2)) continue;
            if (class_ != null) {
                errors.duplicateScopeAnnotations(class_, class_2);
                continue;
            }
            class_ = class_2;
        }
        return class_;
    }

    static boolean containsComponentAnnotation(Annotation[] arrannotation) {
        for (Annotation annotation : arrannotation) {
            if (!annotation.annotationType().getSimpleName().equals("Component")) continue;
            return true;
        }
        return false;
    }

    public static boolean isScopeAnnotation(Class<? extends Annotation> class_) {
        return scopeChecker.hasAnnotations(class_);
    }

    public static void checkForMisplacedScopeAnnotations(Class<?> class_, Object object, Errors errors) {
        if (Classes.isConcrete(class_)) {
            return;
        }
        Class<? extends Annotation> class_2 = Annotations.findScopeAnnotation(errors, class_);
        if (class_2 != null && !Annotations.containsComponentAnnotation(class_.getAnnotations())) {
            errors.withSource(class_).scopeAnnotationOnAbstractType(class_2, class_, object);
        }
    }

    public static Key<?> getKey(TypeLiteral<?> typeLiteral, Member member, Annotation[] arrannotation, Errors errors) throws ErrorsException {
        int n2 = errors.size();
        Annotation annotation = Annotations.findBindingAnnotation(errors, member, arrannotation);
        errors.throwIfNewErrors(n2);
        return annotation == null ? Key.get(typeLiteral) : Key.get(typeLiteral, annotation);
    }

    public static Annotation findBindingAnnotation(Errors errors, Member member, Annotation[] arrannotation) {
        Annotation annotation = null;
        for (Annotation annotation2 : arrannotation) {
            Class<? extends Annotation> class_ = annotation2.annotationType();
            if (!Annotations.isBindingAnnotation(class_)) continue;
            if (annotation != null) {
                errors.duplicateBindingAnnotations(member, annotation.annotationType(), class_);
                continue;
            }
            annotation = annotation2;
        }
        return annotation;
    }

    public static boolean isBindingAnnotation(Class<? extends Annotation> class_) {
        return bindingAnnotationChecker.hasAnnotations(class_);
    }

    public static Annotation canonicalizeIfNamed(Annotation annotation) {
        if (annotation instanceof javax.inject.Named) {
            return Names.named(((javax.inject.Named)annotation).value());
        }
        return annotation;
    }

    public static Class<? extends Annotation> canonicalizeIfNamed(Class<? extends Annotation> class_) {
        if (class_ == javax.inject.Named.class) {
            return Named.class;
        }
        return class_;
    }

    static class AnnotationChecker {
        private final Collection<Class<? extends Annotation>> annotationTypes;
        private CacheLoader<Class<? extends Annotation>, Boolean> hasAnnotations;
        final LoadingCache<Class<? extends Annotation>, Boolean> cache;

        AnnotationChecker(Collection<Class<? extends Annotation>> collection) {
            this.hasAnnotations = new CacheLoader<Class<? extends Annotation>, Boolean>(){

                @Override
                public Boolean load(Class<? extends Annotation> class_) {
                    for (Annotation annotation : class_.getAnnotations()) {
                        if (!AnnotationChecker.this.annotationTypes.contains(annotation.annotationType())) continue;
                        return true;
                    }
                    return false;
                }
            };
            this.cache = CacheBuilder.newBuilder().weakKeys().build(this.hasAnnotations);
            this.annotationTypes = collection;
        }

        boolean hasAnnotations(Class<? extends Annotation> class_) {
            return this.cache.getUnchecked(class_);
        }

    }

}

