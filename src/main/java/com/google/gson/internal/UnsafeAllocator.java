/*
 * Decompiled with CFR 0_119.
 */
package com.google.gson.internal;

import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public abstract class UnsafeAllocator {
    public abstract <T> T newInstance(Class<T> var1) throws Exception;

    public static UnsafeAllocator create() {
        try {
            Class<?> class_ = Class.forName("sun.misc.Unsafe");
            Field field = class_.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            final Object object = field.get(null);
            final Method method = class_.getMethod("allocateInstance", Class.class);
            return new UnsafeAllocator(){

                @Override
                public <T> T newInstance(Class<T> class_) throws Exception {
                    UnsafeAllocator.assertInstantiable(class_);
                    return (T)method.invoke(object, class_);
                }
            };
        }
        catch (Exception exception) {
            try {
                Method method = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
                method.setAccessible(true);
                final int n2 = (Integer)method.invoke(null, Object.class);
                final Method method2 = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, Integer.TYPE);
                method2.setAccessible(true);
                return new UnsafeAllocator(){

                    @Override
                    public <T> T newInstance(Class<T> class_) throws Exception {
                        UnsafeAllocator.assertInstantiable(class_);
                        return (T)method2.invoke(null, class_, n2);
                    }
                };
            }
            catch (Exception exception2) {
                try {
                    final Method method = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
                    method.setAccessible(true);
                    return new UnsafeAllocator(){

                        @Override
                        public <T> T newInstance(Class<T> class_) throws Exception {
                            UnsafeAllocator.assertInstantiable(class_);
                            return (T)method.invoke(null, class_, Object.class);
                        }
                    };
                }
                catch (Exception exception3) {
                    return new UnsafeAllocator(){

                        @Override
                        public <T> T newInstance(Class<T> class_) {
                            throw new UnsupportedOperationException("Cannot allocate " + class_);
                        }
                    };
                }
            }
        }
    }

    private static void assertInstantiable(Class<?> class_) {
        int n2 = class_.getModifiers();
        if (Modifier.isInterface(n2)) {
            throw new UnsupportedOperationException("Interface can't be instantiated! Interface name: " + class_.getName());
        }
        if (Modifier.isAbstract(n2)) {
            throw new UnsupportedOperationException("Abstract class can't be instantiated! Class name: " + class_.getName());
        }
    }

}

