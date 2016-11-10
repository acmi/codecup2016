/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.collect.Platform;

public final class ObjectArrays {
    static final Object[] EMPTY_ARRAY = new Object[0];

    public static <T> T[] newArray(T[] arrT, int n2) {
        return Platform.newArray(arrT, n2);
    }

    static <T> T[] arraysCopyOf(T[] arrT, int n2) {
        T[] arrT2 = ObjectArrays.newArray(arrT, n2);
        System.arraycopy(arrT, 0, arrT2, 0, Math.min(arrT.length, n2));
        return arrT2;
    }

    static void swap(Object[] arrobject, int n2, int n3) {
        Object object = arrobject[n2];
        arrobject[n2] = arrobject[n3];
        arrobject[n3] = object;
    }

    static /* varargs */ Object[] checkElementsNotNull(Object ... arrobject) {
        return ObjectArrays.checkElementsNotNull(arrobject, arrobject.length);
    }

    static Object[] checkElementsNotNull(Object[] arrobject, int n2) {
        for (int i2 = 0; i2 < n2; ++i2) {
            ObjectArrays.checkElementNotNull(arrobject[i2], i2);
        }
        return arrobject;
    }

    static Object checkElementNotNull(Object object, int n2) {
        if (object == null) {
            throw new NullPointerException("at index " + n2);
        }
        return object;
    }
}

