/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import java.lang.annotation.Annotation;

public class Nullability {
    private Nullability() {
    }

    public static boolean allowsNull(Annotation[] arrannotation) {
        for (Annotation annotation : arrannotation) {
            Class<? extends Annotation> class_ = annotation.annotationType();
            if (!"Nullable".equals(class_.getSimpleName())) continue;
            return true;
        }
        return false;
    }
}

