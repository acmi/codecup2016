/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.CollectPreconditions;
import java.util.Collection;

public final class Collections2 {
    static final Joiner STANDARD_JOINER = Joiner.on(", ").useForNull("null");

    static boolean safeContains(Collection<?> collection, Object object) {
        Preconditions.checkNotNull(collection);
        try {
            return collection.contains(object);
        }
        catch (ClassCastException classCastException) {
            return false;
        }
        catch (NullPointerException nullPointerException) {
            return false;
        }
    }

    static StringBuilder newStringBuilderForCollection(int n2) {
        CollectPreconditions.checkNonnegative(n2, "size");
        return new StringBuilder((int)Math.min((long)n2 * 8, 0x40000000));
    }

    static <T> Collection<T> cast(Iterable<T> iterable) {
        return (Collection)iterable;
    }
}

