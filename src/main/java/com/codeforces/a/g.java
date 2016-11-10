/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.a;

import java.util.concurrent.Callable;

static final class g
implements Callable<Integer> {
    final /* synthetic */ Process a;

    g(Process process) {
        this.a = process;
    }

    public Integer a() throws Exception {
        return this.a.waitFor();
    }

    @Override
    public /* synthetic */ Object call() throws Exception {
        return this.a();
    }
}

