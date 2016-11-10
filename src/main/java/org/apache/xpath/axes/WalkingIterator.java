/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.axes;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.axes.AxesWalker;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.axes.WalkerFactory;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.OpMap;

public class WalkingIterator
extends LocPathIterator
implements ExpressionOwner {
    protected AxesWalker m_lastUsedWalker;
    protected AxesWalker m_firstWalker;

    WalkingIterator(Compiler compiler, int n2, int n3, boolean bl) throws TransformerException {
        super(compiler, n2, n3, bl);
        int n4 = OpMap.getFirstChildPos(n2);
        if (bl) {
            this.m_lastUsedWalker = this.m_firstWalker = WalkerFactory.loadWalkers(this, compiler, n4, 0);
        }
    }

    public WalkingIterator(PrefixResolver prefixResolver) {
        super(prefixResolver);
    }

    public int getAnalysisBits() {
        int n2 = 0;
        if (null != this.m_firstWalker) {
            for (AxesWalker axesWalker = this.m_firstWalker; null != axesWalker; axesWalker = axesWalker.getNextWalker()) {
                int n3 = axesWalker.getAnalysisBits();
                n2 |= n3;
            }
        }
        return n2;
    }

    public Object clone() throws CloneNotSupportedException {
        WalkingIterator walkingIterator = (WalkingIterator)super.clone();
        if (null != this.m_firstWalker) {
            walkingIterator.m_firstWalker = this.m_firstWalker.cloneDeep(walkingIterator, null);
        }
        return walkingIterator;
    }

    public void reset() {
        super.reset();
        if (null != this.m_firstWalker) {
            this.m_lastUsedWalker = this.m_firstWalker;
            this.m_firstWalker.setRoot(this.m_context);
        }
    }

    public void setRoot(int n2, Object object) {
        super.setRoot(n2, object);
        if (null != this.m_firstWalker) {
            this.m_firstWalker.setRoot(n2);
            this.m_lastUsedWalker = this.m_firstWalker;
        }
    }

    public int nextNode() {
        if (this.m_foundLast) {
            return -1;
        }
        if (-1 == this.m_stackFrame) {
            return this.returnNextNode(this.m_firstWalker.nextNode());
        }
        VariableStack variableStack = this.m_execContext.getVarStack();
        int n2 = variableStack.getStackFrame();
        variableStack.setStackFrame(this.m_stackFrame);
        int n3 = this.returnNextNode(this.m_firstWalker.nextNode());
        variableStack.setStackFrame(n2);
        return n3;
    }

    public final AxesWalker getFirstWalker() {
        return this.m_firstWalker;
    }

    public final void setFirstWalker(AxesWalker axesWalker) {
        this.m_firstWalker = axesWalker;
    }

    public final void setLastUsedWalker(AxesWalker axesWalker) {
        this.m_lastUsedWalker = axesWalker;
    }

    public final AxesWalker getLastUsedWalker() {
        return this.m_lastUsedWalker;
    }

    public void detach() {
        if (this.m_allowDetach) {
            for (AxesWalker axesWalker = this.m_firstWalker; null != axesWalker; axesWalker = axesWalker.getNextWalker()) {
                axesWalker.detach();
            }
            this.m_lastUsedWalker = null;
            super.detach();
        }
    }

    public void fixupVariables(Vector vector, int n2) {
        this.m_predicateIndex = -1;
        for (AxesWalker axesWalker = this.m_firstWalker; null != axesWalker; axesWalker = axesWalker.getNextWalker()) {
            axesWalker.fixupVariables(vector, n2);
        }
    }

    public void callVisitors(ExpressionOwner expressionOwner, XPathVisitor xPathVisitor) {
        if (xPathVisitor.visitLocationPath(expressionOwner, this) && null != this.m_firstWalker) {
            this.m_firstWalker.callVisitors(this, xPathVisitor);
        }
    }

    public Expression getExpression() {
        return this.m_firstWalker;
    }

    public void setExpression(Expression expression) {
        expression.exprSetParent(this);
        this.m_firstWalker = (AxesWalker)expression;
    }

    public boolean deepEquals(Expression expression) {
        AxesWalker axesWalker;
        if (!super.deepEquals(expression)) {
            return false;
        }
        AxesWalker axesWalker2 = this.m_firstWalker;
        for (axesWalker = ((WalkingIterator)expression).m_firstWalker; null != axesWalker2 && null != axesWalker; axesWalker2 = axesWalker2.getNextWalker(), axesWalker = axesWalker.getNextWalker()) {
            if (axesWalker2.deepEquals(axesWalker)) continue;
            return false;
        }
        if (null != axesWalker2 || null != axesWalker) {
            return false;
        }
        return true;
    }
}

