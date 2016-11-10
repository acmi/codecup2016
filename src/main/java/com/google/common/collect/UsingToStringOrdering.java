/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.collect.Ordering;
import java.io.Serializable;

final class UsingToStringOrdering
extends Ordering<Object>
implements Serializable {
    static final UsingToStringOrdering INSTANCE = new UsingToStringOrdering();

    @Override
    public int compare(Object object, Object object2) {
        return object.toString().compareTo(object2.toString());
    }

    public String toString() {
        return "Ordering.usingToString()";
    }

    private UsingToStringOrdering() {
    }
}

