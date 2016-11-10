/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.Key;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.InternalFactory;
import com.google.inject.internal.SingleMemberInjector;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.InjectionPoint;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.List;

final class SingleFieldInjector
implements SingleMemberInjector {
    final Field field;
    final InjectionPoint injectionPoint;
    final Dependency<?> dependency;
    final BindingImpl<?> binding;

    public SingleFieldInjector(InjectorImpl injectorImpl, InjectionPoint injectionPoint, Errors errors) throws ErrorsException {
        this.injectionPoint = injectionPoint;
        this.field = (Field)injectionPoint.getMember();
        this.dependency = injectionPoint.getDependencies().get(0);
        this.field.setAccessible(true);
        this.binding = injectorImpl.getBindingOrThrow(this.dependency.getKey(), errors, InjectorImpl.JitLimitation.NO_JIT);
    }

    @Override
    public InjectionPoint getInjectionPoint() {
        return this.injectionPoint;
    }

    @Override
    public void inject(Errors errors, InternalContext internalContext, Object object) {
        errors = errors.withSource(this.dependency);
        Dependency dependency = internalContext.pushDependency(this.dependency, this.binding.getSource());
        try {
            Object obj = this.binding.getInternalFactory().get(errors, internalContext, this.dependency, false);
            this.field.set(object, obj);
        }
        catch (ErrorsException errorsException) {
            errors.withSource(this.injectionPoint).merge(errorsException.getErrors());
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new AssertionError(illegalAccessException);
        }
        finally {
            internalContext.popStateAndSetDependency(dependency);
        }
    }
}

