/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.util.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public final class Uninterruptibles {
    public static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
        boolean bl = false;
        do {
            try {
                V v2 = future.get();
                return v2;
            }
            catch (InterruptedException interruptedException) {
                bl = true;
                continue;
            }
            break;
        } while (true);
        finally {
            if (bl) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

