/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.a.a;

import com.codeforces.commons.reflection.ReflectionUtil;
import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Primitives;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class d {
    private static final Set<Class> a = ImmutableSet.of(Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE);
    private static final Set<Class> b = ImmutableSet.of(Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class);

    public static Object a(Object object, Class<?> class_) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (object == null) {
            if (a.contains(class_)) {
                throw new IllegalArgumentException("Can't get null object as primitive class.");
            }
            return null;
        }
        Class class_2 = object.getClass();
        boolean bl = a.contains(class_2);
        boolean bl2 = b.contains(class_2);
        if ((bl || bl2 || class_2 == String.class) && (class_2 == class_ || bl && Primitives.wrap(class_2) == class_ || bl2 && Primitives.unwrap(class_2) == class_)) {
            return object;
        }
        if (class_2.isEnum() && class_.isEnum()) {
            return d.a(object, class_, class_2);
        }
        if (class_2.isArray() && class_.isArray()) {
            return d.b(object, class_);
        }
        Constructor<?>[] arrconstructor = class_.getConstructors();
        if (arrconstructor.length != 1) {
            throw new IllegalArgumentException("Too many constructors in target " + class_ + '.');
        }
        Constructor constructor = arrconstructor[0];
        Class<?>[] arrclass = constructor.getParameterTypes();
        int n2 = arrclass.length;
        Map<String, List<Field>> map = ReflectionUtil.getFieldsByNameMap(class_2);
        int n3 = map.size();
        if (n3 != n2) {
            while (n3 > n2 && (class_2 = class_2.getSuperclass()) != null) {
                map = ReflectionUtil.getFieldsByNameMap(class_2);
                n3 = map.size();
            }
            if (n3 != n2) {
                throw new IllegalArgumentException(String.format("Source object %s and target %s aren't compatible.", object, class_));
            }
        }
        Object[] arrobject = new Object[n3];
        Iterator<List<Field>> iterator = map.values().iterator();
        for (int i2 = 0; i2 < n3; ++i2) {
            List<Field> list = iterator.next();
            if (list.size() != 1) {
                throw new IllegalArgumentException(String.format("There are multiple fields with name '%s' in %s.", list.get(0).getName(), class_2.getName()));
            }
            Field field = list.get(0);
            Class class_3 = field.getType();
            Class class_4 = arrclass[i2];
            boolean bl3 = a.contains(class_3);
            boolean bl4 = b.contains(class_3);
            if (bl3 || bl4 || class_3 == String.class) {
                if (class_3 == class_4 || bl3 && Primitives.wrap(class_3) == class_4 || bl4 && Primitives.unwrap(class_3) == class_4) {
                    arrobject[i2] = field.get(object);
                    continue;
                }
            } else {
                if (class_3.isEnum() && class_4.isEnum()) {
                    arrobject[i2] = d.a(field.get(object), class_4, class_3);
                    continue;
                }
                if (class_3.isArray() && class_4.isArray()) {
                    arrobject[i2] = d.b(field.get(object), class_4);
                    continue;
                }
            }
            throw new IllegalArgumentException(String.format("Field '%s' of source object and constructor parameter of target %s aren't compatible.", field.getName(), class_.getName()));
        }
        return constructor.newInstance(arrobject);
    }

    private static Object a(Object object, Class<?> class_, Class<?> class_2) {
        if (object == null || class_2 == class_) {
            return object;
        }
        Class class_3 = class_;
        return Enum.valueOf(class_3, ((Enum)object).name());
    }

    private static Object b(Object object, Class<?> class_) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (object == null) {
            return null;
        }
        Class class_2 = class_.getComponentType();
        int n2 = Array.getLength(object);
        Object object2 = Array.newInstance(class_2, n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            Array.set(object2, i2, d.a(Array.get(object, i2), class_2));
        }
        return object2;
    }
}

