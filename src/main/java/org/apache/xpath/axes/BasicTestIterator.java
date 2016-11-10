/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.ChildTestIterator;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.OpMap;

public abstract class BasicTestIterator
extends LocPathIterator {
    static final long serialVersionUID = 3505378079378096623L;

    protected BasicTestIterator() {
    }

    protected BasicTestIterator(PrefixResolver prefixResolver) {
        super(prefixResolver);
    }

    protected BasicTestIterator(Compiler compiler, int n2, int n3) throws TransformerException {
        super(compiler, n2, n3, false);
        int n4 = OpMap.getFirstChildPos(n2);
        int n5 = compiler.getWhatToShow(n4);
        if (0 == (n5 & 4163) || n5 == -1) {
            this.initNodeTest(n5);
        } else {
            this.initNodeTest(n5, compiler.getStepNS(n4), compiler.getStepLocalName(n4));
        }
        this.initPredicateInfo(compiler, n4);
    }

    protected BasicTestIterator(Compiler compiler, int n2, int n3, boolean bl) throws TransformerException {
        super(compiler, n2, n3, bl);
    }

    protected abstract int getNextNode();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int nextNode() {
        VariableStack variableStack;
        int n2;
        block11 : {
            if (this.m_foundLast) {
                this.m_lastFetched = -1;
                return -1;
            }
            if (-1 == this.m_lastFetched) {
                this.resetProximityPositions();
            }
            if (-1 != this.m_stackFrame) {
                variableStack = this.m_execContext.getVarStack();
                n2 = variableStack.getStackFrame();
                variableStack.setStackFrame(this.m_stackFrame);
            } else {
                variableStack = null;
                n2 = 0;
            }
            try {
                int n3;
                while (-1 != (n3 = this.getNextNode()) && 1 != this.acceptNode(n3) && n3 != -1) {
                }
                if (-1 == n3) break block11;
                ++this.m_pos;
                int n4 = n3;
                java.lang.Object var6_6 = null;
                if (-1 != this.m_stackFrame) {
                    variableStack.setStackFrame(n2);
                }
                return n4;
            }
            catch (Throwable throwable) {
                java.lang.Object var6_8 = null;
                if (-1 != this.m_stackFrame) {
                    variableStack.setStackFrame(n2);
                }
                throw throwable;
            }
        }
        this.m_foundLast = true;
        int n5 = -1;
        java.lang.Object var6_7 = null;
        if (-1 != this.m_stackFrame) {
            variableStack.setStackFrame(n2);
        }
        return n5;
    }

    public DTMIterator cloneWithReset() throws CloneNotSupportedException {
        ChildTestIterator childTestIterator = (ChildTestIterator)super.cloneWithReset();
        childTestIterator.resetProximityPositions();
        return childTestIterator;
    }
}

