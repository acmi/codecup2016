/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.ref.DTMNodeListBase;
import org.w3c.dom.Node;

public class DTMChildIterNodeList
extends DTMNodeListBase {
    private int m_firstChild;
    private DTM m_parentDTM;

    private DTMChildIterNodeList() {
    }

    public DTMChildIterNodeList(DTM dTM, int n2) {
        this.m_parentDTM = dTM;
        this.m_firstChild = dTM.getFirstChild(n2);
    }

    public Node item(int n2) {
        int n3 = this.m_firstChild;
        while (--n2 >= 0 && n3 != -1) {
            n3 = this.m_parentDTM.getNextSibling(n3);
        }
        if (n3 == -1) {
            return null;
        }
        return this.m_parentDTM.getNode(n3);
    }

    public int getLength() {
        int n2 = 0;
        int n3 = this.m_firstChild;
        while (n3 != -1) {
            ++n2;
            n3 = this.m_parentDTM.getNextSibling(n3);
        }
        return n2;
    }
}

