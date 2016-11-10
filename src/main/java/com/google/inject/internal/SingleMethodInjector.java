/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.internal.BytecodeGen;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalContext;
import com.google.inject.internal.SingleMemberInjector;
import com.google.inject.internal.SingleParameterInjector;
import com.google.inject.internal.cglib.core.$CodeGenerationException;
import com.google.inject.internal.cglib.reflect.$FastClass;
import com.google.inject.internal.cglib.reflect.$FastMethod;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.InjectionPoint;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

final class SingleMethodInjector
implements SingleMemberInjector {
    private final InjectorImpl.MethodInvoker methodInvoker;
    private final SingleParameterInjector<?>[] parameterInjectors;
    private final InjectionPoint injectionPoint;

    SingleMethodInjector(InjectorImpl injectorImpl, InjectionPoint injectionPoint, Errors errors) throws ErrorsException {
        this.injectionPoint = injectionPoint;
        Method method = (Method)injectionPoint.getMember();
        this.methodInvoker = this.createMethodInvoker(method);
        this.parameterInjectors = injectorImpl.getParametersInjectors(injectionPoint.getDependencies(), errors);
    }

    private InjectorImpl.MethodInvoker createMethodInvoker(final Method method) {
        int n2;
        try {
            final $FastClass $FastClass = BytecodeGen.newFastClassForMember(method);
            if ($FastClass != null) {
                final int n3 = $FastClass.getMethod(method).getIndex();
                return new InjectorImpl.MethodInvoker(){

                    @Override
                    public /* varargs */ Object invoke(Object object, Object ... arrobject) throws IllegalAccessException, InvocationTargetException {
                        return $FastClass.invoke(n3, object, arrobject);
                    }
                };
            }
        }
        catch ($CodeGenerationException $CodeGenerationException) {
            // empty catch block
        }
        if (!Modifier.isPublic(n2 = method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
            method.setAccessible(true);
        }
        return new InjectorImpl.MethodInvoker(){

            @Override
            public /* varargs */ Object invoke(Object object, Object ... arrobject) throws IllegalAccessException, InvocationTargetException {
                return method.invoke(object, arrobject);
            }
        };
    }

    @Override
    public InjectionPoint getInjectionPoint() {
        return this.injectionPoint;
    }

    @Override
    public void inject(Errors errors, InternalContext internalContext, Object object) {
        Object[] arrobject;
        try {
            arrobject = SingleParameterInjector.getAll(errors, internalContext, this.parameterInjectors);
        }
        catch (ErrorsException errorsException) {
            errors.merge(errorsException.getErrors());
            return;
        }
        try {
            this.methodInvoker.invoke(object, arrobject);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new AssertionError(illegalAccessException);
        }
        catch (InvocationTargetException invocationTargetException /* !! */ ) {
            Throwable throwable = invocationTargetException /* !! */ .getCause() != null ? invocationTargetException /* !! */ .getCause() : invocationTargetException /* !! */ ;
            errors.withSource(this.injectionPoint).errorInjectingMethod(throwable);
        }
    }

}

