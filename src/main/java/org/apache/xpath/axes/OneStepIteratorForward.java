/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xpath.Expression;
import org.apache.xpath.axes.ChildTestIterator;
import org.apache.xpath.axes.WalkerFactory;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.OpMap;

public class OneStepIteratorForward
extends ChildTestIterator {
    static final long serialVersionUID = -1576936606178190566L;
    protected int m_axis = -1;

    OneStepIteratorForward(Compiler compiler, int n2, int n3) throws TransformerException {
        super(compiler, n2, n3);
        int n4 = OpMap.getFirstChildPos(n2);
        this.m_axis = WalkerFactory.getAxisFromStep(compiler, n4);
    }

    public OneStepIteratorForward(int n2) {
        super(null);
        this.m_axis = n2;
        int n3 = -1;
        this.initNodeTest(n3);
    }

    public void setRoot(int n2, Object object) {
        super.setRoot(n2, object);
        this.m_traverser = this.m_cdtm.getAxisTraverser(this.m_axis);
    }

    protected int getNextNode() {
        this.m_lastFetched = -1 == this.m_lastFetched ? this.m_traverser.first(this.m_context) : this.m_traverser.next(this.m_context, this.m_lastFetched);
        return this.m_lastFetched;
    }

    public int getAxis() {
        return this.m_axis;
    }

    public boolean deepEquals(Expression expression) {
        if (!super.deepEquals(expression)) {
            return false;
        }
        if (this.m_axis != ((OneStepIteratorForward)expression).m_axis) {
            return false;
        }
        return true;
    }
}

