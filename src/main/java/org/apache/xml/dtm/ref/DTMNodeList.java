/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.ref.DTMNodeListBase;
import org.w3c.dom.Node;

public class DTMNodeList
extends DTMNodeListBase {
    private DTMIterator m_iter;

    private DTMNodeList() {
    }

    public DTMNodeList(DTMIterator dTMIterator) {
        if (dTMIterator != null) {
            int n2 = dTMIterator.getCurrentPos();
            try {
                this.m_iter = dTMIterator.cloneWithReset();
            }
            catch (CloneNotSupportedException cloneNotSupportedException) {
                this.m_iter = dTMIterator;
            }
            this.m_iter.setShouldCacheNodes(true);
            this.m_iter.runTo(-1);
            this.m_iter.setCurrentPos(n2);
        }
    }

    public DTMIterator getDTMIterator() {
        return this.m_iter;
    }

    public Node item(int n2) {
        if (this.m_iter != null) {
            int n3 = this.m_iter.item(n2);
            if (n3 == -1) {
                return null;
            }
            return this.m_iter.getDTM(n3).getNode(n3);
        }
        return null;
    }

    public int getLength() {
        return this.m_iter != null ? this.m_iter.getLength() : 0;
    }
}

