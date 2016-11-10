/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils.res;

public class CharArrayWrapper {
    private char[] m_char;

    public CharArrayWrapper(char[] arrc) {
        this.m_char = arrc;
    }

    public char getChar(int n2) {
        return this.m_char[n2];
    }

    public int getLength() {
        return this.m_char.length;
    }
}

