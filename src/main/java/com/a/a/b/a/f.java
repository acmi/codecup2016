/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.b.a;

import com.a.a.b.a;
import com.a.a.b.a.b;
import com.a.a.b.c.c;
import com.codeforces.commons.math.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class f
extends b {
    private final List<a> a = new LinkedList<a>();

    @Override
    public void a(a a2) {
        f.e(a2);
        if (this.a.contains(a2)) {
            throw new IllegalStateException(a2 + " is already added.");
        }
        this.a.add(a2);
    }

    @Override
    public void b(a a2) {
        if (a2 == null) {
            return;
        }
        Iterator<a> iterator = this.a.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().b(a2)) continue;
            iterator.remove();
            return;
        }
    }

    @Override
    public boolean c(a a2) {
        f.e(a2);
        return this.a.contains(a2);
    }

    @Override
    public List<a> a() {
        return Collections.unmodifiableList(this.a);
    }

    @Override
    public List<a> d(a a2) {
        f.e(a2);
        ArrayList<a> arrayList = new ArrayList<a>();
        boolean bl = false;
        for (a a3 : this.a) {
            if (a3.b(a2)) {
                bl = true;
                continue;
            }
            if (a2.e() && a3.e() || Math.sqr(a3.c().d() + a2.c().d()) < a3.a(a2)) continue;
            arrayList.add(a3);
        }
        if (!bl) {
            throw new IllegalStateException("Can't find " + a2 + '.');
        }
        return Collections.unmodifiableList(arrayList);
    }
}

