/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

import com.google.inject.internal.cglib.core.$ReflectUtils;
import com.google.inject.internal.cglib.core.$Transformer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class $MethodInfoTransformer
implements $Transformer {
    private static final $MethodInfoTransformer INSTANCE = new $MethodInfoTransformer();

    public static $MethodInfoTransformer getInstance() {
        return INSTANCE;
    }

    public Object transform(Object object) {
        if (object instanceof Method) {
            return $ReflectUtils.getMethodInfo((Method)object);
        }
        if (object instanceof Constructor) {
            return $ReflectUtils.getMethodInfo((Constructor)object);
        }
        String string = String.valueOf(object);
        throw new IllegalArgumentException(new StringBuilder(27 + String.valueOf(string).length()).append("cannot get method info for ").append(string).toString());
    }
}

