/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.base;

import java.util.Arrays;

public final class Objects {
    public static boolean equal(Object object, Object object2) {
        return object == object2 || object != null && object.equals(object2);
    }

    public static /* varargs */ int hashCode(Object ... arrobject) {
        return Arrays.hashCode(arrobject);
    }
}

