/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.a;

import com.a.a.b.a;
import com.a.a.b.a.c;
import com.codeforces.commons.geometry.Point2D;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class d
extends com.a.a.b.d.d {
    private final Lock d;
    final /* synthetic */ double a;
    final /* synthetic */ a b;
    final /* synthetic */ c c;

    d(c c2, double d2, a a2) {
        this.c = c2;
        this.a = d2;
        this.b = a2;
        this.d = new ReentrantLock();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void a(Point2D point2D, Point2D point2D2) {
        int n2;
        int n3;
        int n4;
        int n5;
        if (this.a > c.a(this.c)) {
            return;
        }
        if (this.b.a() >= 0 && this.b.a() <= 9999) {
            int n6 = (int)this.b.a();
            Point2D point2D3 = c.b(this.c)[n6];
            Point2D point2D4 = c.c(this.c)[n6];
            Point2D point2D5 = this.b.r();
            if (point2D5.getX() >= point2D3.getX() && point2D5.getY() >= point2D3.getY() && point2D5.getX() < point2D4.getX() && point2D5.getY() < point2D4.getY()) {
                return;
            }
            n2 = c.a(this.c, point2D.getX());
            n5 = c.b(this.c, point2D.getY());
            n4 = c.a(this.c, point2D2.getX());
            n3 = c.b(this.c, point2D2.getY());
        } else {
            n2 = c.a(this.c, point2D.getX());
            n5 = c.b(this.c, point2D.getY());
            n4 = c.a(this.c, point2D2.getX());
            n3 = c.b(this.c, point2D2.getY());
            if (n2 == n4 && n5 == n3) {
                return;
            }
        }
        this.d.lock();
        try {
            c.a(this.c, this.b, n2, n5);
            c.b(this.c, this.b, n4, n3);
        }
        finally {
            this.d.unlock();
        }
    }
}

