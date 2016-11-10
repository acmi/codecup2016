/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.axes.BasicTestIterator;
import org.apache.xpath.compiler.Compiler;

public class ChildTestIterator
extends BasicTestIterator {
    static final long serialVersionUID = -7936835957960705722L;
    protected transient DTMAxisTraverser m_traverser;

    ChildTestIterator(Compiler compiler, int n2, int n3) throws TransformerException {
        super(compiler, n2, n3);
    }

    public ChildTestIterator(DTMAxisTraverser dTMAxisTraverser) {
        super(null);
        this.m_traverser = dTMAxisTraverser;
    }

    protected int getNextNode() {
        this.m_lastFetched = -1 == this.m_lastFetched ? this.m_traverser.first(this.m_context) : this.m_traverser.next(this.m_context, this.m_lastFetched);
        return this.m_lastFetched;
    }

    public DTMIterator cloneWithReset() throws CloneNotSupportedException {
        ChildTestIterator childTestIterator = (ChildTestIterator)super.cloneWithReset();
        childTestIterator.m_traverser = this.m_traverser;
        return childTestIterator;
    }

    public void setRoot(int n2, Object object) {
        super.setRoot(n2, object);
        this.m_traverser = this.m_cdtm.getAxisTraverser(3);
    }

    public int getAxis() {
        return 3;
    }

    public void detach() {
        if (this.m_allowDetach) {
            this.m_traverser = null;
            super.detach();
        }
    }
}

