/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

import java.util.Hashtable;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemNumber;
import org.apache.xalan.transformer.Counter;
import org.apache.xml.dtm.DTMManager;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPathContext;

public class CountersTable
extends Hashtable {
    static final long serialVersionUID = 2159100770924179875L;
    private transient NodeSetDTM m_newFound;
    transient int m_countersMade = 0;

    Vector getCounters(ElemNumber elemNumber) {
        Vector vector = (Vector)this.get(elemNumber);
        return null == vector ? this.putElemNumber(elemNumber) : vector;
    }

    Vector putElemNumber(ElemNumber elemNumber) {
        Vector vector = new Vector();
        this.put(elemNumber, vector);
        return vector;
    }

    void appendBtoFList(NodeSetDTM nodeSetDTM, NodeSetDTM nodeSetDTM2) {
        int n2 = nodeSetDTM2.size();
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            nodeSetDTM.addElement(nodeSetDTM2.item(i2));
        }
    }

    public int countNode(XPathContext xPathContext, ElemNumber elemNumber, int n2) throws TransformerException {
        int n3 = 0;
        Vector vector = this.getCounters(elemNumber);
        int n4 = vector.size();
        int n5 = elemNumber.getTargetNode(xPathContext, n2);
        if (-1 != n5) {
            Counter counter;
            int n6;
            for (n6 = 0; n6 < n4; ++n6) {
                counter = (Counter)vector.elementAt(n6);
                n3 = counter.getPreviouslyCounted(xPathContext, n5);
                if (n3 <= 0) continue;
                return n3;
            }
            n3 = 0;
            if (this.m_newFound == null) {
                this.m_newFound = new NodeSetDTM(xPathContext.getDTMManager());
            }
            while (-1 != n5) {
                if (0 != n3) {
                    for (n6 = 0; n6 < n4; ++n6) {
                        counter = (Counter)vector.elementAt(n6);
                        int n7 = counter.m_countNodes.size();
                        if (n7 <= 0 || counter.m_countNodes.elementAt(n7 - 1) != n5) continue;
                        n3 += n7 + counter.m_countNodesStartCount;
                        if (n7 > 0) {
                            this.appendBtoFList(counter.m_countNodes, this.m_newFound);
                        }
                        this.m_newFound.removeAllElements();
                        return n3;
                    }
                }
                this.m_newFound.addElement(n5);
                ++n3;
                n5 = elemNumber.getPreviousNode(xPathContext, n5);
            }
            Counter counter2 = new Counter(elemNumber, new NodeSetDTM(xPathContext.getDTMManager()));
            ++this.m_countersMade;
            this.appendBtoFList(counter2.m_countNodes, this.m_newFound);
            this.m_newFound.removeAllElements();
            vector.addElement(counter2);
        }
        return n3;
    }
}

