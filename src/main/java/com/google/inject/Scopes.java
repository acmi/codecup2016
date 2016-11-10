/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Singleton;
import com.google.inject.internal.CircularDependencyProxy;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.LinkedBindingImpl;
import com.google.inject.internal.SingletonScope;
import com.google.inject.spi.BindingScopingVisitor;
import com.google.inject.spi.ExposedBinding;
import com.google.inject.spi.PrivateElements;
import java.lang.annotation.Annotation;

public class Scopes {
    public static final Scope SINGLETON = new SingletonScope();
    public static final Scope NO_SCOPE = new Scope(){

        @Override
        public <T> Provider<T> scope(Key<T> key, Provider<T> provider) {
            return provider;
        }

        @Override
        public String toString() {
            return "Scopes.NO_SCOPE";
        }
    };
    private static final BindingScopingVisitor<Boolean> IS_SINGLETON_VISITOR = new BindingScopingVisitor<Boolean>(){

        @Override
        public Boolean visitNoScoping() {
            return false;
        }

        @Override
        public Boolean visitScopeAnnotation(Class<? extends Annotation> class_) {
            return class_ == Singleton.class || class_ == javax.inject.Singleton.class;
        }

        @Override
        public Boolean visitScope(Scope scope) {
            return scope == Scopes.SINGLETON;
        }

        @Override
        public Boolean visitEagerSingleton() {
            return true;
        }
    };

    private Scopes() {
    }

    public static boolean isSingleton(Binding<?> binding) {
        do {
            Injector injector;
            boolean bl;
            LinkedBindingImpl linkedBindingImpl;
            if (bl = binding.acceptScopingVisitor(IS_SINGLETON_VISITOR).booleanValue()) {
                return true;
            }
            if (binding instanceof LinkedBindingImpl) {
                linkedBindingImpl = (LinkedBindingImpl)binding;
                injector = linkedBindingImpl.getInjector();
                if (injector == null) break;
                binding = injector.getBinding(linkedBindingImpl.getLinkedKey());
                continue;
            }
            if (!(binding instanceof ExposedBinding) || (injector = (linkedBindingImpl = (ExposedBinding)binding).getPrivateElements().getInjector()) == null) break;
            binding = injector.getBinding(linkedBindingImpl.getKey());
        } while (true);
        return false;
    }

    public static boolean isScoped(Binding<?> binding, final Scope scope, final Class<? extends Annotation> class_) {
        do {
            boolean bl;
            LinkedBindingImpl linkedBindingImpl;
            Injector injector;
            if (bl = ((Boolean)binding.acceptScopingVisitor(new BindingScopingVisitor<Boolean>(){

                @Override
                public Boolean visitNoScoping() {
                    return false;
                }

                @Override
                public Boolean visitScopeAnnotation(Class<? extends Annotation> class_2) {
                    return class_2 == class_;
                }

                @Override
                public Boolean visitScope(Scope scope2) {
                    return scope2 == scope;
                }

                @Override
                public Boolean visitEagerSingleton() {
                    return false;
                }
            })).booleanValue()) {
                return true;
            }
            if (binding instanceof LinkedBindingImpl) {
                linkedBindingImpl = (LinkedBindingImpl)binding;
                injector = linkedBindingImpl.getInjector();
                if (injector == null) break;
                binding = injector.getBinding(linkedBindingImpl.getLinkedKey());
                continue;
            }
            if (!(binding instanceof ExposedBinding) || (injector = (linkedBindingImpl = (ExposedBinding)binding).getPrivateElements().getInjector()) == null) break;
            binding = injector.getBinding(linkedBindingImpl.getKey());
        } while (true);
        return false;
    }

    public static boolean isCircularProxy(Object object) {
        return object instanceof CircularDependencyProxy;
    }

}

