/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Element;
import com.google.inject.spi.ElementVisitor;

public final class MembersInjectorLookup<T>
implements Element {
    private final Object source;
    private final TypeLiteral<T> type;
    private MembersInjector<T> delegate;

    public MembersInjectorLookup(Object object, TypeLiteral<T> typeLiteral) {
        this.source = Preconditions.checkNotNull(object, "source");
        this.type = Preconditions.checkNotNull(typeLiteral, "type");
    }

    @Override
    public Object getSource() {
        return this.source;
    }

    public TypeLiteral<T> getType() {
        return this.type;
    }

    @Override
    public <T> T acceptVisitor(ElementVisitor<T> elementVisitor) {
        return elementVisitor.visit(this);
    }

    public void initializeDelegate(MembersInjector<T> membersInjector) {
        Preconditions.checkState(this.delegate == null, "delegate already initialized");
        this.delegate = Preconditions.checkNotNull(membersInjector, "delegate");
    }

    @Override
    public void applyTo(Binder binder) {
        this.initializeDelegate(binder.withSource(this.getSource()).getMembersInjector(this.type));
    }

    public MembersInjector<T> getDelegate() {
        return this.delegate;
    }

    public MembersInjector<T> getMembersInjector() {
        return new MembersInjector<T>(){

            @Override
            public void injectMembers(T t2) {
                Preconditions.checkState(MembersInjectorLookup.this.delegate != null, "This MembersInjector cannot be used until the Injector has been created.");
                MembersInjectorLookup.this.delegate.injectMembers(t2);
            }

            public String toString() {
                return "MembersInjector<" + MembersInjectorLookup.this.type + ">";
            }
        };
    }

}

