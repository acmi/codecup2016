/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

import java.io.PrintStream;

public final class IntStack {
    private int fDepth;
    private int[] fData;

    public int size() {
        return this.fDepth;
    }

    public void push(int n2) {
        this.ensureCapacity(this.fDepth + 1);
        this.fData[this.fDepth++] = n2;
    }

    public int peek() {
        return this.fData[this.fDepth - 1];
    }

    public int elementAt(int n2) {
        return this.fData[n2];
    }

    public int pop() {
        return this.fData[--this.fDepth];
    }

    public void clear() {
        this.fDepth = 0;
    }

    public void print() {
        System.out.print('(');
        System.out.print(this.fDepth);
        System.out.print(") {");
        int n2 = 0;
        while (n2 < this.fDepth) {
            if (n2 == 3) {
                System.out.print(" ...");
                break;
            }
            System.out.print(' ');
            System.out.print(this.fData[n2]);
            if (n2 < this.fDepth - 1) {
                System.out.print(',');
            }
            ++n2;
        }
        System.out.print(" }");
        System.out.println();
    }

    private void ensureCapacity(int n2) {
        if (this.fData == null) {
            this.fData = new int[32];
        } else if (this.fData.length <= n2) {
            int[] arrn = new int[this.fData.length * 2];
            System.arraycopy(this.fData, 0, arrn, 0, this.fData.length);
            this.fData = arrn;
        }
    }
}

