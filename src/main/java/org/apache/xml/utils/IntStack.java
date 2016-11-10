/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.util.EmptyStackException;
import org.apache.xml.utils.IntVector;

public class IntStack
extends IntVector {
    public IntStack() {
    }

    public IntStack(int n2) {
        super(n2);
    }

    public int push(int n2) {
        if (this.m_firstFree + 1 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            int[] arrn = new int[this.m_mapSize];
            System.arraycopy(this.m_map, 0, arrn, 0, this.m_firstFree + 1);
            this.m_map = arrn;
        }
        this.m_map[this.m_firstFree] = n2;
        ++this.m_firstFree;
        return n2;
    }

    public final int pop() {
        return this.m_map[--this.m_firstFree];
    }

    public final void quickPop(int n2) {
        this.m_firstFree -= n2;
    }

    public final int peek() {
        try {
            return this.m_map[this.m_firstFree - 1];
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            throw new EmptyStackException();
        }
    }

    public void setTop(int n2) {
        try {
            this.m_map[this.m_firstFree - 1] = n2;
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            throw new EmptyStackException();
        }
    }

    public boolean empty() {
        return this.m_firstFree == 0;
    }

    public Object clone() throws CloneNotSupportedException {
        return (IntStack)super.clone();
    }
}

