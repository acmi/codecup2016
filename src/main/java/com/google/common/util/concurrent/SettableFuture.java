/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.util.concurrent;

import com.google.common.util.concurrent.AbstractFuture;

public final class SettableFuture<V>
extends AbstractFuture.TrustedFuture<V> {
    public static <V> SettableFuture<V> create() {
        return new SettableFuture<V>();
    }

    private SettableFuture() {
    }

    @Override
    public boolean set(V v2) {
        return super.set(v2);
    }

    @Override
    public boolean setException(Throwable throwable) {
        return super.setException(throwable);
    }
}

