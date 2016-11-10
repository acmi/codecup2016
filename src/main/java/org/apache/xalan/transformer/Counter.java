/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemNumber;
import org.apache.xml.dtm.DTM;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPathContext;

public class Counter {
    static final int MAXCOUNTNODES = 500;
    int m_countNodesStartCount = 0;
    NodeSetDTM m_countNodes;
    int m_fromNode = -1;
    ElemNumber m_numberElem;
    int m_countResult;

    Counter(ElemNumber elemNumber, NodeSetDTM nodeSetDTM) throws TransformerException {
        this.m_countNodes = nodeSetDTM;
        this.m_numberElem = elemNumber;
    }

    int getPreviouslyCounted(XPathContext xPathContext, int n2) {
        int n3 = this.m_countNodes.size();
        this.m_countResult = 0;
        for (int i2 = n3 - 1; i2 >= 0; --i2) {
            int n4 = this.m_countNodes.elementAt(i2);
            if (n2 == n4) {
                this.m_countResult = i2 + 1 + this.m_countNodesStartCount;
                break;
            }
            DTM dTM = xPathContext.getDTM(n4);
            if (dTM.isNodeAfter(n4, n2)) break;
        }
        return this.m_countResult;
    }

    int getLast() {
        int n2 = this.m_countNodes.size();
        return n2 > 0 ? this.m_countNodes.elementAt(n2 - 1) : -1;
    }
}

