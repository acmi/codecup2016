/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref;

import java.util.Vector;
import org.apache.xml.utils.IntVector;

public class DTMStringPool {
    Vector m_intToString = new Vector();
    int[] m_hashStart = new int[101];
    IntVector m_hashChain;

    public DTMStringPool(int n2) {
        this.m_hashChain = new IntVector(n2);
        this.removeAllElements();
        this.stringToIndex("");
    }

    public DTMStringPool() {
        this(512);
    }

    public void removeAllElements() {
        this.m_intToString.removeAllElements();
        for (int i2 = 0; i2 < 101; ++i2) {
            this.m_hashStart[i2] = -1;
        }
        this.m_hashChain.removeAllElements();
    }

    public String indexToString(int n2) throws ArrayIndexOutOfBoundsException {
        if (n2 == -1) {
            return null;
        }
        return (String)this.m_intToString.elementAt(n2);
    }

    public int stringToIndex(String string) {
        int n2;
        if (string == null) {
            return -1;
        }
        int n3 = string.hashCode() % 101;
        if (n3 < 0) {
            n3 = - n3;
        }
        int n4 = n2 = this.m_hashStart[n3];
        while (n4 != -1) {
            if (this.m_intToString.elementAt(n4).equals(string)) {
                return n4;
            }
            n2 = n4;
            n4 = this.m_hashChain.elementAt(n4);
        }
        int n5 = this.m_intToString.size();
        this.m_intToString.addElement(string);
        this.m_hashChain.addElement(-1);
        if (n2 == -1) {
            this.m_hashStart[n3] = n5;
        } else {
            this.m_hashChain.setElementAt(n5, n2);
        }
        return n5;
    }
}

