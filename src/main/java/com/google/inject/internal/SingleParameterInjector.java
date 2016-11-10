/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.InternalFactory;
import com.google.inject.spi.Dependency;

final class SingleParameterInjector<T> {
    private static final Object[] NO_ARGUMENTS = new Object[0];
    private final Dependency<T> dependency;
    private final BindingImpl<? extends T> binding;

    SingleParameterInjector(Dependency<T> dependency, BindingImpl<? extends T> bindingImpl) {
        this.dependency = dependency;
        this.binding = bindingImpl;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private T inject(Errors errors, InternalContext internalContext) throws ErrorsException {
        Dependency dependency = internalContext.pushDependency(this.dependency, this.binding.getSource());
        try {
            T t2 = this.binding.getInternalFactory().get(errors.withSource(this.dependency), internalContext, this.dependency, false);
            return t2;
        }
        finally {
            internalContext.popStateAndSetDependency(dependency);
        }
    }

    static Object[] getAll(Errors errors, InternalContext internalContext, SingleParameterInjector<?>[] arrsingleParameterInjector) throws ErrorsException {
        if (arrsingleParameterInjector == null) {
            return NO_ARGUMENTS;
        }
        int n2 = errors.size();
        int n3 = arrsingleParameterInjector.length;
        Object[] arrobject = new Object[n3];
        for (int i2 = 0; i2 < n3; ++i2) {
            SingleParameterInjector singleParameterInjector = arrsingleParameterInjector[i2];
            try {
                arrobject[i2] = singleParameterInjector.inject(errors, internalContext);
                continue;
            }
            catch (ErrorsException errorsException) {
                errors.merge(errorsException.getErrors());
            }
        }
        errors.throwIfNewErrors(n2);
        return arrobject;
    }
}

