/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.b.c;

import com.a.b.a.a.b.d.b;
import com.a.b.a.a.b.d.d.e;
import com.a.b.d;
import com.a.b.f;
import com.a.b.g;

public class a
extends f<d, d> {
    @Override
    public boolean beforeCollision(g g2, d d2, d d3) {
        e e2;
        double d4;
        if (d2 instanceof e && !(d3 instanceof b)) {
            return false;
        }
        if (d3 instanceof e && !(d2 instanceof b)) {
            return false;
        }
        if (d2 instanceof e && d3.equals(((e)d2).h())) {
            return false;
        }
        if (d3 instanceof e && d2.equals(((e)d3).h())) {
            return false;
        }
        if (d2 instanceof e) {
            e2 = (e)d2;
            d4 = e2.o();
            if (d3 instanceof com.a.b.a.a.b.d.e.a ? d4 > e2.n() : d4 < e2.m() || d4 > e2.n()) {
                return false;
            }
        }
        if (d3 instanceof e) {
            e2 = (e)d3;
            d4 = e2.o();
            if (d2 instanceof com.a.b.a.a.b.d.e.a ? d4 > e2.n() : d4 < e2.m() || d4 > e2.n()) {
                return false;
            }
        }
        return true;
    }
}

