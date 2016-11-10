/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.collect;

import java.io.Serializable;

final class Count
implements Serializable {
    private int value;

    Count(int n2) {
        this.value = n2;
    }

    public int get() {
        return this.value;
    }

    public int getAndAdd(int n2) {
        int n3 = this.value;
        this.value = n3 + n2;
        return n3;
    }

    public int addAndGet(int n2) {
        return this.value += n2;
    }

    public void set(int n2) {
        this.value = n2;
    }

    public int getAndSet(int n2) {
        int n3 = this.value;
        this.value = n2;
        return n3;
    }

    public int hashCode() {
        return this.value;
    }

    public boolean equals(Object object) {
        return object instanceof Count && ((Count)object).value == this.value;
    }

    public String toString() {
        return Integer.toString(this.value);
    }
}

