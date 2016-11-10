/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

import org.apache.xerces.xni.XMLString;

public class XMLStringBuffer
extends XMLString {
    public static final int DEFAULT_SIZE = 32;

    public XMLStringBuffer() {
        this(32);
    }

    public XMLStringBuffer(int n2) {
        this.ch = new char[n2];
    }

    public XMLStringBuffer(char c2) {
        this(1);
        this.append(c2);
    }

    public XMLStringBuffer(String string) {
        this(string.length());
        this.append(string);
    }

    public XMLStringBuffer(char[] arrc, int n2, int n3) {
        this(n3);
        this.append(arrc, n2, n3);
    }

    public XMLStringBuffer(XMLString xMLString) {
        this(xMLString.length);
        this.append(xMLString);
    }

    public void clear() {
        this.offset = 0;
        this.length = 0;
    }

    public void append(char c2) {
        if (this.length + 1 > this.ch.length) {
            int n2 = this.ch.length * 2;
            if (n2 < this.ch.length + 32) {
                n2 = this.ch.length + 32;
            }
            char[] arrc = new char[n2];
            System.arraycopy(this.ch, 0, arrc, 0, this.length);
            this.ch = arrc;
        }
        this.ch[this.length] = c2;
        ++this.length;
    }

    public void append(String string) {
        int n2 = string.length();
        if (this.length + n2 > this.ch.length) {
            int n3 = this.ch.length * 2;
            if (n3 < this.length + n2 + 32) {
                n3 = this.ch.length + n2 + 32;
            }
            char[] arrc = new char[n3];
            System.arraycopy(this.ch, 0, arrc, 0, this.length);
            this.ch = arrc;
        }
        string.getChars(0, n2, this.ch, this.length);
        this.length += n2;
    }

    public void append(char[] arrc, int n2, int n3) {
        if (this.length + n3 > this.ch.length) {
            char[] arrc2 = new char[this.ch.length + n3 + 32];
            System.arraycopy(this.ch, 0, arrc2, 0, this.length);
            this.ch = arrc2;
        }
        System.arraycopy(arrc, n2, this.ch, this.length, n3);
        this.length += n3;
    }

    public void append(XMLString xMLString) {
        this.append(xMLString.ch, xMLString.offset, xMLString.length);
    }
}

