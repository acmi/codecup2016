/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.e.a.a;

import com.a.b.a.a.e.a.a.d;
import com.codeforces.commons.process.ThreadUtil;
import java.net.ServerSocket;

class e
implements ThreadUtil.Operation<Void> {
    final /* synthetic */ int a;
    final /* synthetic */ d b;

    e(d d2, int n2) {
        this.b = d2;
        this.a = n2;
    }

    public Void a() throws Throwable {
        d.a(this.b, new ServerSocket(this.a));
        d.b(this.b).setSoTimeout(d.a(this.b));
        d.b(this.b).setReceiveBufferSize(d.n());
        return null;
    }

    @Override
    public /* synthetic */ Object run() throws Throwable {
        return this.a();
    }
}

