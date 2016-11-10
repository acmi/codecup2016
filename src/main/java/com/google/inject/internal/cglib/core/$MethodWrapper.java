/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

import com.google.inject.internal.cglib.core.$KeyFactory;
import com.google.inject.internal.cglib.core.$ReflectUtils;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class $MethodWrapper {
    private static final MethodWrapperKey KEY_FACTORY = (MethodWrapperKey)((Object)$KeyFactory.create(MethodWrapperKey.class));

    private $MethodWrapper() {
    }

    public static Object create(Method method) {
        return KEY_FACTORY.newInstance(method.getName(), $ReflectUtils.getNames(method.getParameterTypes()), method.getReturnType().getName());
    }

    public static Set createSet(Collection collection) {
        HashSet<Object> hashSet = new HashSet<Object>();
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            hashSet.add($MethodWrapper.create((Method)iterator.next()));
        }
        return hashSet;
    }

    public static interface MethodWrapperKey {
        public Object newInstance(String var1, String[] var2, String var3);
    }

}

