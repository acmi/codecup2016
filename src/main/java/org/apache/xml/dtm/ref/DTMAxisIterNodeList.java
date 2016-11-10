/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMNodeListBase;
import org.apache.xml.utils.IntVector;
import org.w3c.dom.Node;

public class DTMAxisIterNodeList
extends DTMNodeListBase {
    private DTM m_dtm;
    private DTMAxisIterator m_iter;
    private IntVector m_cachedNodes;
    private int m_last = -1;

    private DTMAxisIterNodeList() {
    }

    public DTMAxisIterNodeList(DTM dTM, DTMAxisIterator dTMAxisIterator) {
        if (dTMAxisIterator == null) {
            this.m_last = 0;
        } else {
            this.m_cachedNodes = new IntVector();
            this.m_dtm = dTM;
        }
        this.m_iter = dTMAxisIterator;
    }

    public Node item(int n2) {
        if (this.m_iter != null) {
            int n3 = this.m_cachedNodes.size();
            if (n3 > n2) {
                int n4 = this.m_cachedNodes.elementAt(n2);
                return this.m_dtm.getNode(n4);
            }
            if (this.m_last == -1) {
                int n5;
                while ((n5 = this.m_iter.next()) != -1 && n3 <= n2) {
                    this.m_cachedNodes.addElement(n5);
                    ++n3;
                }
                if (n5 == -1) {
                    this.m_last = n3;
                } else {
                    return this.m_dtm.getNode(n5);
                }
            }
        }
        return null;
    }

    public int getLength() {
        if (this.m_last == -1) {
            int n2;
            while ((n2 = this.m_iter.next()) != -1) {
                this.m_cachedNodes.addElement(n2);
            }
            this.m_last = this.m_cachedNodes.size();
        }
        return this.m_last;
    }
}

