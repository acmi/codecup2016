/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.Annotations;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ProviderMethod;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.Message;
import com.google.inject.spi.ModuleAnnotatedMethodScanner;
import com.google.inject.util.Modules;
import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class ProviderMethodsModule
implements Module {
    private static ModuleAnnotatedMethodScanner PROVIDES_BUILDER = new ModuleAnnotatedMethodScanner(){

        @Override
        public <T> Key<T> prepareMethod(Binder binder, Annotation annotation, Key<T> key, InjectionPoint injectionPoint) {
            return key;
        }

        @Override
        public Set<? extends Class<? extends Annotation>> annotationClasses() {
            return ImmutableSet.of(Provides.class);
        }
    };
    private final Object delegate;
    private final TypeLiteral<?> typeLiteral;
    private final boolean skipFastClassGeneration;
    private final ModuleAnnotatedMethodScanner scanner;

    private ProviderMethodsModule(Object object, boolean bl, ModuleAnnotatedMethodScanner moduleAnnotatedMethodScanner) {
        this.delegate = Preconditions.checkNotNull(object, "delegate");
        this.typeLiteral = TypeLiteral.get(this.delegate.getClass());
        this.skipFastClassGeneration = bl;
        this.scanner = moduleAnnotatedMethodScanner;
    }

    public static Module forModule(Module module) {
        return ProviderMethodsModule.forObject(module, false, PROVIDES_BUILDER);
    }

    public static Module forModule(Object object, ModuleAnnotatedMethodScanner moduleAnnotatedMethodScanner) {
        return ProviderMethodsModule.forObject(object, false, moduleAnnotatedMethodScanner);
    }

    public static Module forObject(Object object) {
        return ProviderMethodsModule.forObject(object, true, PROVIDES_BUILDER);
    }

    private static Module forObject(Object object, boolean bl, ModuleAnnotatedMethodScanner moduleAnnotatedMethodScanner) {
        if (object instanceof ProviderMethodsModule) {
            return Modules.EMPTY_MODULE;
        }
        return new ProviderMethodsModule(object, bl, moduleAnnotatedMethodScanner);
    }

    public Object getDelegateModule() {
        return this.delegate;
    }

    @Override
    public synchronized void configure(Binder binder) {
        for (ProviderMethod providerMethod : this.getProviderMethods(binder)) {
            providerMethod.configure(binder);
        }
    }

    public List<ProviderMethod<?>> getProviderMethods(Binder binder) {
        Object object;
        ArrayList arrayList = Lists.newArrayList();
        HashMultimap<Signature, Method> hashMultimap = HashMultimap.create();
        for (Class class_ = this.delegate.getClass(); class_ != Object.class; class_ = class_.getSuperclass()) {
            for (Method method : class_.getDeclaredMethods()) {
                if ((method.getModifiers() & 10) == 0 && !method.isBridge() && !method.isSynthetic()) {
                    hashMultimap.put(new Signature(method), method);
                }
                if (!(object = this.isProvider(binder, method)).isPresent()) continue;
                arrayList.add(this.createProviderMethod(binder, method, object.get()));
            }
        }
        block2 : for (ProviderMethod providerMethod : arrayList) {
            Method method = providerMethod.getMethod();
            for (Method method2 : hashMultimap.get(new Signature(method))) {
                if (method2.getDeclaringClass().isAssignableFrom(method.getDeclaringClass()) || !ProviderMethodsModule.overrides(method2, method)) continue;
                object = providerMethod.getAnnotation().annotationType() == Provides.class ? "@Provides" : "@" + providerMethod.getAnnotation().annotationType().getCanonicalName();
                binder.addError("Overriding " + (String)object + " methods is not allowed." + "\n\t" + (String)object + " method: %s\n\toverridden by: %s", method, method2);
                continue block2;
            }
        }
        return arrayList;
    }

    private Optional<Annotation> isProvider(Binder binder, Method method) {
        if (method.isBridge() || method.isSynthetic()) {
            return Optional.absent();
        }
        Annotation annotation = null;
        for (Class<? extends Annotation> class_ : this.scanner.annotationClasses()) {
            Annotation annotation2 = method.getAnnotation(class_);
            if (annotation2 == null) continue;
            if (annotation != null) {
                binder.addError("More than one annotation claimed by %s on method %s. Methods can only have one annotation claimed per scanner.", this.scanner, method);
                return Optional.absent();
            }
            annotation = annotation2;
        }
        return Optional.fromNullable(annotation);
    }

    private static boolean overrides(Method method, Method method2) {
        int n2 = method2.getModifiers();
        if (Modifier.isPublic(n2) || Modifier.isProtected(n2)) {
            return true;
        }
        if (Modifier.isPrivate(n2)) {
            return false;
        }
        return method.getDeclaringClass().getPackage().equals(method2.getDeclaringClass().getPackage());
    }

    private <T> ProviderMethod<T> createProviderMethod(Binder binder, Method method, Annotation annotation) {
        void var9_12;
        binder = binder.withSource(method);
        Errors errors = new Errors(method);
        InjectionPoint injectionPoint = InjectionPoint.forMethod(method, this.typeLiteral);
        List list = injectionPoint.getDependencies();
        ArrayList arrayList = Lists.newArrayList();
        for (Dependency key2 : injectionPoint.getDependencies()) {
            arrayList.add(binder.getProvider(key2));
        }
        TypeLiteral typeLiteral = this.typeLiteral.getReturnType(method);
        Key<T> key = this.getKey(errors, typeLiteral, method, method.getAnnotations());
        try {
            Key<T> key2 = this.scanner.prepareMethod(binder, annotation, key, injectionPoint);
        }
        catch (Throwable throwable) {
            binder.addError(throwable);
        }
        Class<? extends Annotation> class_ = Annotations.findScopeAnnotation(errors, method.getAnnotations());
        for (Message message : errors.getMessages()) {
            binder.addError(message);
        }
        return ProviderMethod.create(var9_12, method, this.delegate, ImmutableSet.copyOf(list), arrayList, class_, this.skipFastClassGeneration, annotation);
    }

    <T> Key<T> getKey(Errors errors, TypeLiteral<T> typeLiteral, Member member, Annotation[] arrannotation) {
        Annotation annotation = Annotations.findBindingAnnotation(errors, member, arrannotation);
        return annotation == null ? Key.get(typeLiteral) : Key.get(typeLiteral, annotation);
    }

    public boolean equals(Object object) {
        return object instanceof ProviderMethodsModule && ((ProviderMethodsModule)object).delegate == this.delegate && ((ProviderMethodsModule)object).scanner == this.scanner;
    }

    public int hashCode() {
        return this.delegate.hashCode();
    }

    private final class Signature {
        final Class<?>[] parameters;
        final String name;
        final int hashCode;

        Signature(Method method) {
            this.name = method.getName();
            List list = ProviderMethodsModule.this.typeLiteral.getParameterTypes(method);
            this.parameters = new Class[list.size()];
            int n2 = 0;
            for (TypeLiteral typeLiteral : list) {
                this.parameters[n2] = typeLiteral.getRawType();
            }
            this.hashCode = this.name.hashCode() + 31 * Arrays.hashCode(this.parameters);
        }

        public boolean equals(Object object) {
            if (object instanceof Signature) {
                Signature signature = (Signature)object;
                return signature.name.equals(this.name) && Arrays.equals(this.parameters, signature.parameters);
            }
            return false;
        }

        public int hashCode() {
            return this.hashCode;
        }
    }

}

