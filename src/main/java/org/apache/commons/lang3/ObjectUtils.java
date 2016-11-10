/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.lang3;

import java.io.Serializable;

public class ObjectUtils {
    public static final Null NULL = new Null();

    public static <T> T defaultIfNull(T t2, T t3) {
        return t2 != null ? t2 : t3;
    }

    @Deprecated
    public static String toString(Object object) {
        return object == null ? "" : object.toString();
    }

    public static class Null
    implements Serializable {
        Null() {
        }
    }

}

