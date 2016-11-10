/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.Errors;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.Element;
import com.google.inject.spi.ElementVisitor;
import com.google.inject.spi.TypeConverter;

public final class TypeConverterBinding
implements Element {
    private final Object source;
    private final Matcher<? super TypeLiteral<?>> typeMatcher;
    private final TypeConverter typeConverter;

    public TypeConverterBinding(Object object, Matcher<? super TypeLiteral<?>> matcher, TypeConverter typeConverter) {
        this.source = Preconditions.checkNotNull(object, "source");
        this.typeMatcher = Preconditions.checkNotNull(matcher, "typeMatcher");
        this.typeConverter = Preconditions.checkNotNull(typeConverter, "typeConverter");
    }

    @Override
    public Object getSource() {
        return this.source;
    }

    public Matcher<? super TypeLiteral<?>> getTypeMatcher() {
        return this.typeMatcher;
    }

    public TypeConverter getTypeConverter() {
        return this.typeConverter;
    }

    @Override
    public <T> T acceptVisitor(ElementVisitor<T> elementVisitor) {
        return elementVisitor.visit(this);
    }

    @Override
    public void applyTo(Binder binder) {
        binder.withSource(this.getSource()).convertToTypes(this.typeMatcher, this.typeConverter);
    }

    public String toString() {
        return this.typeConverter + " which matches " + this.typeMatcher + " (bound at " + Errors.convert(this.source) + ")";
    }
}

