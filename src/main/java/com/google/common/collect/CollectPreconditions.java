/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import com.google.common.base.Preconditions;

final class CollectPreconditions {
    static void checkEntryNotNull(Object object, Object object2) {
        if (object == null) {
            throw new NullPointerException("null key in entry: null=" + object2);
        }
        if (object2 == null) {
            throw new NullPointerException("null value in entry: " + object + "=null");
        }
    }

    static int checkNonnegative(int n2, String string) {
        if (n2 < 0) {
            throw new IllegalArgumentException(string + " cannot be negative but was: " + n2);
        }
        return n2;
    }

    static void checkRemove(boolean bl) {
        Preconditions.checkState(bl, "no calls to next() since the last call to remove()");
    }
}

