/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.MoreObjects;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.Initializable;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.InternalFactory;
import com.google.inject.spi.Dependency;

final class ConstantFactory<T>
implements InternalFactory<T> {
    private final Initializable<T> initializable;

    public ConstantFactory(Initializable<T> initializable) {
        this.initializable = initializable;
    }

    @Override
    public T get(Errors errors, InternalContext internalContext, Dependency dependency, boolean bl) throws ErrorsException {
        return this.initializable.get(errors);
    }

    public String toString() {
        return MoreObjects.toStringHelper(ConstantFactory.class).add("value", this.initializable).toString();
    }
}

