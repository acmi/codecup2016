/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.holder;

import org.apache.commons.lang3.mutable.Mutable;

public class Holders {
    public static <T> void setQuietly(Mutable<T> mutable, T t2) {
        if (mutable != null) {
            try {
                mutable.setValue(t2);
            }
            catch (RuntimeException runtimeException) {
                // empty catch block
            }
        }
    }
}

