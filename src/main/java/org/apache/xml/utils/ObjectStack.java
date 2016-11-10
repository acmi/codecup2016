/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.util.EmptyStackException;
import org.apache.xml.utils.ObjectVector;

public class ObjectStack
extends ObjectVector {
    public ObjectStack() {
    }

    public ObjectStack(int n2) {
        super(n2);
    }

    public Object push(Object object) {
        if (this.m_firstFree + 1 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            Object[] arrobject = new Object[this.m_mapSize];
            System.arraycopy(this.m_map, 0, arrobject, 0, this.m_firstFree + 1);
            this.m_map = arrobject;
        }
        this.m_map[this.m_firstFree] = object;
        ++this.m_firstFree;
        return object;
    }

    public Object pop() {
        Object object = this.m_map[--this.m_firstFree];
        this.m_map[this.m_firstFree] = null;
        return object;
    }

    public Object peek() {
        try {
            return this.m_map[this.m_firstFree - 1];
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            throw new EmptyStackException();
        }
    }

    public void setTop(Object object) {
        try {
            this.m_map[this.m_firstFree - 1] = object;
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            throw new EmptyStackException();
        }
    }

    public boolean empty() {
        return this.m_firstFree == 0;
    }

    public Object clone() throws CloneNotSupportedException {
        return (ObjectStack)super.clone();
    }
}

