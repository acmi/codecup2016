/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.cache;

import com.google.common.cache.LongAddable;
import com.google.common.cache.Striped64;
import java.io.Serializable;

final class LongAdder
extends Striped64
implements LongAddable,
Serializable {
    @Override
    final long fn(long l2, long l3) {
        return l2 + l3;
    }

    @Override
    public void add(long l2) {
        long l3;
        Striped64.Cell[] arrcell = this.cells;
        if (arrcell != null || !this.casBase(l3 = this.base, l3 + l2)) {
            int n2;
            Striped64.Cell cell;
            long l4;
            boolean bl = true;
            int[] arrn = (int[])threadHashCode.get();
            if (arrn == null || arrcell == null || (n2 = arrcell.length) < 1 || (cell = arrcell[n2 - 1 & arrn[0]]) == null || !(bl = cell.cas(l4 = cell.value, l4 + l2))) {
                this.retryUpdate(l2, arrn, bl);
            }
        }
    }

    @Override
    public void increment() {
        this.add(1);
    }

    public long sum() {
        long l2 = this.base;
        Striped64.Cell[] arrcell = this.cells;
        if (arrcell != null) {
            for (Striped64.Cell cell : arrcell) {
                if (cell == null) continue;
                l2 += cell.value;
            }
        }
        return l2;
    }

    public String toString() {
        return Long.toString(this.sum());
    }

    @Override
    public long longValue() {
        return this.sum();
    }

    @Override
    public int intValue() {
        return (int)this.sum();
    }

    @Override
    public float floatValue() {
        return this.sum();
    }

    @Override
    public double doubleValue() {
        return this.sum();
    }
}

