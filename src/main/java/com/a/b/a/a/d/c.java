/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.d;

import com.a.b.a.a.a.b;
import com.a.b.a.a.c.i;
import com.a.b.a.a.d.a;
import java.util.concurrent.TimeUnit;

class c
implements Runnable {
    final /* synthetic */ b a;
    final /* synthetic */ a b;

    c(a a2, b b2) {
        this.b = a2;
        this.a = b2;
    }

    @Override
    public void run() {
        long l2;
        long l3 = l2 = System.nanoTime();
        try {
            a.e e2;
            i i2 = null;
            int n2 = 0;
            while ((e2 = this.a()) != null && e2.a() != null) {
                l2 = this.a(l2, a.a(this.b).get());
                long l4 = System.nanoTime();
                if (l4 - l3 > 1000000000) {
                    l3 = System.nanoTime();
                    a.b(this.b).set(n2);
                    n2 = 0;
                }
                a.a(this.b, e2);
                ++n2;
                i2 = e2.a();
            }
            if (i2 != null) {
                a.a(this.b, i2);
            }
            Thread.sleep(TimeUnit.SECONDS.toMillis(30));
        }
        catch (InterruptedException interruptedException) {
        }
        catch (RuntimeException runtimeException) {
            a.b().error("Got unexpected runtime exception in rendering thread.", runtimeException);
            throw runtimeException;
        }
        a.c(this.b);
        System.exit(0);
    }

    private a.e a() throws InterruptedException {
        return this.a.l() ? (a.e)a.d(this.b).poll(30, TimeUnit.MINUTES) : (a.e)a.d(this.b).poll(30, TimeUnit.SECONDS);
    }

    private long a(long l2, long l3) {
        long l4 = l3 * 1000000;
        long l5 = System.nanoTime();
        long l6 = l4 + l2 - l5;
        if (l6 > 0) {
            try {
                Thread.sleep(l6 / 1000000, (int)(l6 % 1000000));
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
        return System.nanoTime();
    }
}

