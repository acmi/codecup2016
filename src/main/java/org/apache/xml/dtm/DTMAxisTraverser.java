/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm;

public abstract class DTMAxisTraverser {
    public int first(int n2) {
        return this.next(n2, n2);
    }

    public int first(int n2, int n3) {
        return this.next(n2, n2, n3);
    }

    public abstract int next(int var1, int var2);

    public abstract int next(int var1, int var2, int var3);
}

