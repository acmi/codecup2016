/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  com.google.inject.internal.cglib.proxy.
 */
package com.google.inject.internal.cglib.proxy;

import com.google.inject.internal.asm.$Type;
import com.google.inject.internal.cglib.proxy.;
import com.google.inject.internal.cglib.proxy.$Callback;
import com.google.inject.internal.cglib.proxy.$CallbackGenerator;
import com.google.inject.internal.cglib.proxy.$Dispatcher;
import com.google.inject.internal.cglib.proxy.$DispatcherGenerator;
import com.google.inject.internal.cglib.proxy.$FixedValue;
import com.google.inject.internal.cglib.proxy.$FixedValueGenerator;
import com.google.inject.internal.cglib.proxy.$InvocationHandler;
import com.google.inject.internal.cglib.proxy.$InvocationHandlerGenerator;
import com.google.inject.internal.cglib.proxy.$LazyLoader;
import com.google.inject.internal.cglib.proxy.$LazyLoaderGenerator;
import com.google.inject.internal.cglib.proxy.$MethodInterceptor;
import com.google.inject.internal.cglib.proxy.$MethodInterceptorGenerator;
import com.google.inject.internal.cglib.proxy.$NoOp;
import com.google.inject.internal.cglib.proxy.$NoOpGenerator;
import com.google.inject.internal.cglib.proxy.$ProxyRefDispatcher;

class $CallbackInfo {
    private Class cls;
    private $CallbackGenerator generator;
    private $Type type;
    private static final $CallbackInfo[] CALLBACKS = new $CallbackInfo[]{new $CallbackInfo(.NoOp.class, $NoOpGenerator.INSTANCE), new $CallbackInfo(.MethodInterceptor.class, $MethodInterceptorGenerator.INSTANCE), new $CallbackInfo(.InvocationHandler.class, $InvocationHandlerGenerator.INSTANCE), new $CallbackInfo(.LazyLoader.class, $LazyLoaderGenerator.INSTANCE), new $CallbackInfo(.Dispatcher.class, $DispatcherGenerator.INSTANCE), new $CallbackInfo(.FixedValue.class, $FixedValueGenerator.INSTANCE), new $CallbackInfo(.ProxyRefDispatcher.class, $DispatcherGenerator.PROXY_REF_INSTANCE)};

    public static $Type[] determineTypes(Class[] arrclass) {
        $Type[] arr$Type = new $Type[arrclass.length];
        for (int i2 = 0; i2 < arr$Type.length; ++i2) {
            arr$Type[i2] = $CallbackInfo.determineType(arrclass[i2]);
        }
        return arr$Type;
    }

    public static $Type[] determineTypes($Callback[] arr$Callback) {
        $Type[] arr$Type = new $Type[arr$Callback.length];
        for (int i2 = 0; i2 < arr$Type.length; ++i2) {
            arr$Type[i2] = $CallbackInfo.determineType(arr$Callback[i2]);
        }
        return arr$Type;
    }

    public static $CallbackGenerator[] getGenerators($Type[] arr$Type) {
        $CallbackGenerator[] arr$CallbackGenerator = new $CallbackGenerator[arr$Type.length];
        for (int i2 = 0; i2 < arr$CallbackGenerator.length; ++i2) {
            arr$CallbackGenerator[i2] = $CallbackInfo.getGenerator(arr$Type[i2]);
        }
        return arr$CallbackGenerator;
    }

    private $CallbackInfo(Class class_, $CallbackGenerator $CallbackGenerator) {
        this.cls = class_;
        this.generator = $CallbackGenerator;
        this.type = $Type.getType(class_);
    }

    private static $Type determineType($Callback $Callback) {
        if ($Callback == null) {
            throw new IllegalStateException("Callback is null");
        }
        return $CallbackInfo.determineType($Callback.getClass());
    }

    private static $Type determineType(Class class_) {
        Class class_2 = null;
        for (int i2 = 0; i2 < CALLBACKS.length; ++i2) {
            $CallbackInfo $CallbackInfo = CALLBACKS[i2];
            if (!$CallbackInfo.cls.isAssignableFrom(class_)) continue;
            if (class_2 != null) {
                String string = String.valueOf(class_2);
                String string2 = String.valueOf($CallbackInfo.cls);
                throw new IllegalStateException(new StringBuilder(30 + String.valueOf(string).length() + String.valueOf(string2).length()).append("Callback implements both ").append(string).append(" and ").append(string2).toString());
            }
            class_2 = $CallbackInfo.cls;
        }
        if (class_2 == null) {
            String string = String.valueOf(class_);
            throw new IllegalStateException(new StringBuilder(22 + String.valueOf(string).length()).append("Unknown callback type ").append(string).toString());
        }
        return $Type.getType(class_2);
    }

    private static $CallbackGenerator getGenerator($Type $Type) {
        for (int i2 = 0; i2 < CALLBACKS.length; ++i2) {
            $CallbackInfo $CallbackInfo = CALLBACKS[i2];
            if (!$CallbackInfo.type.equals($Type)) continue;
            return $CallbackInfo.generator;
        }
        String string = String.valueOf($Type);
        throw new IllegalStateException(new StringBuilder(22 + String.valueOf(string).length()).append("Unknown callback type ").append(string).toString());
    }
}

