/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b;

import com.a.b.a.a.b.a;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

class b
implements ThreadFactory {
    private final AtomicInteger b;
    final /* synthetic */ a a;

    b(a a2) {
        this.a = a2;
        this.b = new AtomicInteger();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.setName(String.format("%s#StrategyThread-%d", a.class.getSimpleName(), this.b.incrementAndGet()));
        return thread;
    }
}

