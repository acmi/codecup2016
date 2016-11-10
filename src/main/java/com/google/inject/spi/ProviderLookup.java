/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.Element;
import com.google.inject.spi.ElementVisitor;
import com.google.inject.spi.ProviderWithDependencies;
import com.google.inject.util.Types;
import java.lang.reflect.Type;
import java.util.Set;

public final class ProviderLookup<T>
implements Element {
    private final Object source;
    private final Dependency<T> dependency;
    private Provider<T> delegate;

    public ProviderLookup(Object object, Key<T> key) {
        this(object, Dependency.get(Preconditions.checkNotNull(key, "key")));
    }

    public ProviderLookup(Object object, Dependency<T> dependency) {
        this.source = Preconditions.checkNotNull(object, "source");
        this.dependency = Preconditions.checkNotNull(dependency, "dependency");
    }

    @Override
    public Object getSource() {
        return this.source;
    }

    public Key<T> getKey() {
        return this.dependency.getKey();
    }

    public Dependency<T> getDependency() {
        return this.dependency;
    }

    @Override
    public <T> T acceptVisitor(ElementVisitor<T> elementVisitor) {
        return elementVisitor.visit(this);
    }

    public void initializeDelegate(Provider<T> provider) {
        Preconditions.checkState(this.delegate == null, "delegate already initialized");
        this.delegate = Preconditions.checkNotNull(provider, "delegate");
    }

    @Override
    public void applyTo(Binder binder) {
        this.initializeDelegate(binder.withSource(this.getSource()).getProvider(this.dependency));
    }

    public Provider<T> getDelegate() {
        return this.delegate;
    }

    public Provider<T> getProvider() {
        return new ProviderWithDependencies<T>(){

            @Override
            public T get() {
                Preconditions.checkState(ProviderLookup.this.delegate != null, "This Provider cannot be used until the Injector has been created.");
                return ProviderLookup.this.delegate.get();
            }

            @Override
            public Set<Dependency<?>> getDependencies() {
                Key key = ProviderLookup.this.getKey().ofType(Types.providerOf(ProviderLookup.this.getKey().getTypeLiteral().getType()));
                return ImmutableSet.of(Dependency.get(key));
            }

            public String toString() {
                return "Provider<" + ProviderLookup.this.getKey().getTypeLiteral() + ">";
            }
        };
    }

}

