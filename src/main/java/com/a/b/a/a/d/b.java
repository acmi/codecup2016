/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.d;

import com.a.b.a.a.d.a;
import com.codeforces.commons.process.ThreadUtil;
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
        return ThreadUtil.newThread("OnScreenRenderer#AsyncDrawingThread-" + this.b.incrementAndGet(), runnable, (thread, throwable) -> {
            a.b().error("Can't complete async drawing task in thread '" + thread + "'.", throwable);
        }
        , true);
    }
}

