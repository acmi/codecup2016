/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Objects;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.Stage;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.internal.Errors;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalFactory;
import com.google.inject.internal.InternalFactoryToProviderAdapter;
import com.google.inject.internal.ProviderToInternalFactoryAdapter;
import com.google.inject.internal.SingletonScope;
import com.google.inject.internal.State;
import com.google.inject.spi.BindingScopingVisitor;
import com.google.inject.spi.ScopeBinding;
import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;

public abstract class Scoping {
    public static final Scoping UNSCOPED = new Scoping(){

        @Override
        public <V> V acceptVisitor(BindingScopingVisitor<V> bindingScopingVisitor) {
            return bindingScopingVisitor.visitNoScoping();
        }

        @Override
        public Scope getScopeInstance() {
            return Scopes.NO_SCOPE;
        }

        public String toString() {
            return Scopes.NO_SCOPE.toString();
        }

        @Override
        public void applyTo(ScopedBindingBuilder scopedBindingBuilder) {
        }
    };
    public static final Scoping SINGLETON_ANNOTATION = new Scoping(){

        @Override
        public <V> V acceptVisitor(BindingScopingVisitor<V> bindingScopingVisitor) {
            return bindingScopingVisitor.visitScopeAnnotation(Singleton.class);
        }

        @Override
        public Class<? extends Annotation> getScopeAnnotation() {
            return Singleton.class;
        }

        public String toString() {
            return Singleton.class.getName();
        }

        @Override
        public void applyTo(ScopedBindingBuilder scopedBindingBuilder) {
            scopedBindingBuilder.in(Singleton.class);
        }
    };
    public static final Scoping SINGLETON_INSTANCE = new Scoping(){

        @Override
        public <V> V acceptVisitor(BindingScopingVisitor<V> bindingScopingVisitor) {
            return bindingScopingVisitor.visitScope(Scopes.SINGLETON);
        }

        @Override
        public Scope getScopeInstance() {
            return Scopes.SINGLETON;
        }

        public String toString() {
            return Scopes.SINGLETON.toString();
        }

        @Override
        public void applyTo(ScopedBindingBuilder scopedBindingBuilder) {
            scopedBindingBuilder.in(Scopes.SINGLETON);
        }
    };
    public static final Scoping EAGER_SINGLETON = new Scoping(){

        @Override
        public <V> V acceptVisitor(BindingScopingVisitor<V> bindingScopingVisitor) {
            return bindingScopingVisitor.visitEagerSingleton();
        }

        @Override
        public Scope getScopeInstance() {
            return Scopes.SINGLETON;
        }

        public String toString() {
            return "eager singleton";
        }

        @Override
        public void applyTo(ScopedBindingBuilder scopedBindingBuilder) {
            scopedBindingBuilder.asEagerSingleton();
        }
    };

    public static Scoping forAnnotation(final Class<? extends Annotation> class_) {
        if (class_ == Singleton.class || class_ == javax.inject.Singleton.class) {
            return SINGLETON_ANNOTATION;
        }
        return new Scoping(){

            @Override
            public <V> V acceptVisitor(BindingScopingVisitor<V> bindingScopingVisitor) {
                return bindingScopingVisitor.visitScopeAnnotation(class_);
            }

            @Override
            public Class<? extends Annotation> getScopeAnnotation() {
                return class_;
            }

            public String toString() {
                return class_.getName();
            }

            @Override
            public void applyTo(ScopedBindingBuilder scopedBindingBuilder) {
                scopedBindingBuilder.in(class_);
            }
        };
    }

    public static Scoping forInstance(final Scope scope) {
        if (scope == Scopes.SINGLETON) {
            return SINGLETON_INSTANCE;
        }
        return new Scoping(){

            @Override
            public <V> V acceptVisitor(BindingScopingVisitor<V> bindingScopingVisitor) {
                return bindingScopingVisitor.visitScope(scope);
            }

            @Override
            public Scope getScopeInstance() {
                return scope;
            }

            public String toString() {
                return scope.toString();
            }

            @Override
            public void applyTo(ScopedBindingBuilder scopedBindingBuilder) {
                scopedBindingBuilder.in(scope);
            }
        };
    }

    public boolean isExplicitlyScoped() {
        return this != UNSCOPED;
    }

    public boolean isNoScope() {
        return this.getScopeInstance() == Scopes.NO_SCOPE;
    }

    public boolean isEagerSingleton(Stage stage) {
        if (this == EAGER_SINGLETON) {
            return true;
        }
        if (stage == Stage.PRODUCTION) {
            return this == SINGLETON_ANNOTATION || this == SINGLETON_INSTANCE;
        }
        return false;
    }

    public Scope getScopeInstance() {
        return null;
    }

    public Class<? extends Annotation> getScopeAnnotation() {
        return null;
    }

    public boolean equals(Object object) {
        if (object instanceof Scoping) {
            Scoping scoping = (Scoping)object;
            return Objects.equal(this.getScopeAnnotation(), scoping.getScopeAnnotation()) && Objects.equal(this.getScopeInstance(), scoping.getScopeInstance());
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(this.getScopeAnnotation(), this.getScopeInstance());
    }

    public abstract <V> V acceptVisitor(BindingScopingVisitor<V> var1);

    public abstract void applyTo(ScopedBindingBuilder var1);

    private Scoping() {
    }

    static <T> InternalFactory<? extends T> scope(Key<T> key, InjectorImpl injectorImpl, InternalFactory<? extends T> internalFactory, Object object, Scoping scoping) {
        if (scoping.isNoScope()) {
            return internalFactory;
        }
        Scope scope = scoping.getScopeInstance();
        SingletonScope.currentInjector.set(new WeakReference<InjectorImpl>(injectorImpl));
        Provider<? extends T> provider = scope.scope(key, new ProviderToInternalFactoryAdapter<T>(injectorImpl, internalFactory));
        return new InternalFactoryToProviderAdapter<T>(provider, object);
    }

    static Scoping makeInjectable(Scoping scoping, InjectorImpl injectorImpl, Errors errors) {
        Class<? extends Annotation> class_ = scoping.getScopeAnnotation();
        if (class_ == null) {
            return scoping;
        }
        ScopeBinding scopeBinding = injectorImpl.state.getScopeBinding(class_);
        if (scopeBinding != null) {
            return Scoping.forInstance(scopeBinding.getScope());
        }
        errors.scopeNotFound(class_);
        return UNSCOPED;
    }

}

