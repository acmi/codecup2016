/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b;

import com.a.a.b.f;
import com.codeforces.commons.process.ThreadUtil;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

class g
implements ThreadFactory {
    private final AtomicInteger b;
    final /* synthetic */ f a;

    g(f f2) {
        this.a = f2;
        this.b = new AtomicInteger();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return ThreadUtil.newThread("notreal2d.World#ParallelExecutionThread-" + this.b.incrementAndGet(), runnable, (thread, throwable) -> {
            f.e().error("Can't complete parallel task in thread '" + thread + "'.", throwable);
        }
        , true);
    }
}

