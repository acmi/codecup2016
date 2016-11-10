/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xpath.axes.ChildTestIterator;
import org.apache.xpath.compiler.Compiler;

public class AttributeIterator
extends ChildTestIterator {
    AttributeIterator(Compiler compiler, int n2, int n3) throws TransformerException {
        super(compiler, n2, n3);
    }

    protected int getNextNode() {
        this.m_lastFetched = -1 == this.m_lastFetched ? this.m_cdtm.getFirstAttribute(this.m_context) : this.m_cdtm.getNextAttribute(this.m_lastFetched);
        return this.m_lastFetched;
    }

    public int getAxis() {
        return 2;
    }
}

