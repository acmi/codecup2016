/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.compiler.Compiler;

public class ChildIterator
extends LocPathIterator {
    ChildIterator(Compiler compiler, int n2, int n3) throws TransformerException {
        super(compiler, n2, n3, false);
        this.initNodeTest(-1);
    }

    public int asNode(XPathContext xPathContext) throws TransformerException {
        int n2 = xPathContext.getCurrentNode();
        DTM dTM = xPathContext.getDTM(n2);
        return dTM.getFirstChild(n2);
    }

    public int nextNode() {
        if (this.m_foundLast) {
            return -1;
        }
        int n2 = -1 == this.m_lastFetched ? this.m_cdtm.getFirstChild(this.m_context) : this.m_cdtm.getNextSibling(this.m_lastFetched);
        this.m_lastFetched = n2;
        if (-1 != n2) {
            ++this.m_pos;
            return n2;
        }
        this.m_foundLast = true;
        return -1;
    }

    public int getAxis() {
        return 3;
    }
}

