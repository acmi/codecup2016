/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

public final class BoolStack
implements Cloneable {
    private boolean[] m_values;
    private int m_allocatedSize;
    private int m_index;

    public BoolStack() {
        this(32);
    }

    public BoolStack(int n2) {
        this.m_allocatedSize = n2;
        this.m_values = new boolean[n2];
        this.m_index = -1;
    }

    public final boolean push(boolean bl) {
        if (this.m_index == this.m_allocatedSize - 1) {
            this.grow();
        }
        boolean bl2 = bl;
        this.m_values[++this.m_index] = bl2;
        return bl2;
    }

    public final boolean pop() {
        return this.m_values[this.m_index--];
    }

    public final boolean popAndTop() {
        --this.m_index;
        return this.m_index >= 0 ? this.m_values[this.m_index] : false;
    }

    public final void setTop(boolean bl) {
        this.m_values[this.m_index] = bl;
    }

    public final boolean peek() {
        return this.m_values[this.m_index];
    }

    public final boolean peekOrFalse() {
        return this.m_index > -1 ? this.m_values[this.m_index] : false;
    }

    public boolean isEmpty() {
        return this.m_index == -1;
    }

    private void grow() {
        this.m_allocatedSize *= 2;
        boolean[] arrbl = new boolean[this.m_allocatedSize];
        System.arraycopy(this.m_values, 0, arrbl, 0, this.m_index + 1);
        this.m_values = arrbl;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

