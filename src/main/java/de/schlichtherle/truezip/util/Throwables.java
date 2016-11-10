/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.util;

import de.schlichtherle.truezip.util.JSE7;
import java.lang.reflect.Constructor;

public class Throwables {
    public static <T extends Throwable> T wrap(T t2) {
        try {
            return (T)((Throwable)t2.getClass().getConstructor(String.class).newInstance(t2.toString())).initCause((Throwable)t2);
        }
        catch (Exception exception) {
            if (JSE7.AVAILABLE) {
                t2.addSuppressed(exception);
            }
            return t2;
        }
    }
}

