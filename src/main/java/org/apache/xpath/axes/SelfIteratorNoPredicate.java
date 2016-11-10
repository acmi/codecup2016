/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.compiler.Compiler;

public class SelfIteratorNoPredicate
extends LocPathIterator {
    SelfIteratorNoPredicate(Compiler compiler, int n2, int n3) throws TransformerException {
        super(compiler, n2, n3, false);
    }

    public SelfIteratorNoPredicate() throws TransformerException {
        super(null);
    }

    public int nextNode() {
        if (this.m_foundLast) {
            return -1;
        }
        int n2 = -1 == this.m_lastFetched ? this.m_context : -1;
        this.m_lastFetched = n2;
        if (-1 != n2) {
            ++this.m_pos;
            return n2;
        }
        this.m_foundLast = true;
        return -1;
    }

    public int asNode(XPathContext xPathContext) throws TransformerException {
        return xPathContext.getCurrentNode();
    }

    public int getLastPos(XPathContext xPathContext) {
        return 1;
    }
}

