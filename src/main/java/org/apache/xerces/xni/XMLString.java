/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.xni;

public class XMLString {
    public char[] ch;
    public int offset;
    public int length;

    public XMLString() {
    }

    public XMLString(char[] arrc, int n2, int n3) {
        this.setValues(arrc, n2, n3);
    }

    public XMLString(XMLString xMLString) {
        this.setValues(xMLString);
    }

    public void setValues(char[] arrc, int n2, int n3) {
        this.ch = arrc;
        this.offset = n2;
        this.length = n3;
    }

    public void setValues(XMLString xMLString) {
        this.setValues(xMLString.ch, xMLString.offset, xMLString.length);
    }

    public void clear() {
        this.ch = null;
        this.offset = 0;
        this.length = -1;
    }

    public boolean equals(char[] arrc, int n2, int n3) {
        if (arrc == null) {
            return false;
        }
        if (this.length != n3) {
            return false;
        }
        int n4 = 0;
        while (n4 < n3) {
            if (this.ch[this.offset + n4] != arrc[n2 + n4]) {
                return false;
            }
            ++n4;
        }
        return true;
    }

    public boolean equals(String string) {
        if (string == null) {
            return false;
        }
        if (this.length != string.length()) {
            return false;
        }
        int n2 = 0;
        while (n2 < this.length) {
            if (this.ch[this.offset + n2] != string.charAt(n2)) {
                return false;
            }
            ++n2;
        }
        return true;
    }

    public String toString() {
        return this.length > 0 ? new String(this.ch, this.offset, this.length) : "";
    }
}

