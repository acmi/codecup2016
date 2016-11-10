/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.axes;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.axes.PathComponent;
import org.apache.xpath.axes.PredicatedNodeTest;
import org.apache.xpath.axes.WalkerFactory;
import org.apache.xpath.axes.WalkingIterator;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.patterns.NodeTest;

public class AxesWalker
extends PredicatedNodeTest
implements Cloneable,
ExpressionOwner,
PathComponent {
    private DTM m_dtm;
    transient int m_root = -1;
    private transient int m_currentNode = -1;
    transient boolean m_isFresh;
    protected AxesWalker m_nextWalker;
    AxesWalker m_prevWalker;
    protected int m_axis = -1;
    protected DTMAxisTraverser m_traverser;

    public AxesWalker(LocPathIterator locPathIterator, int n2) {
        super(locPathIterator);
        this.m_axis = n2;
    }

    public final WalkingIterator wi() {
        return (WalkingIterator)this.m_lpi;
    }

    public void init(Compiler compiler, int n2, int n3) throws TransformerException {
        this.initPredicateInfo(compiler, n2);
    }

    public Object clone() throws CloneNotSupportedException {
        AxesWalker axesWalker = (AxesWalker)super.clone();
        return axesWalker;
    }

    AxesWalker cloneDeep(WalkingIterator walkingIterator, Vector vector) throws CloneNotSupportedException {
        AxesWalker axesWalker = AxesWalker.findClone(this, vector);
        if (null != axesWalker) {
            return axesWalker;
        }
        axesWalker = (AxesWalker)this.clone();
        axesWalker.setLocPathIterator(walkingIterator);
        if (null != vector) {
            vector.addElement(this);
            vector.addElement(axesWalker);
        }
        if (this.wi().m_lastUsedWalker == this) {
            walkingIterator.m_lastUsedWalker = axesWalker;
        }
        if (null != this.m_nextWalker) {
            axesWalker.m_nextWalker = this.m_nextWalker.cloneDeep(walkingIterator, vector);
        }
        if (null != vector) {
            if (null != this.m_prevWalker) {
                axesWalker.m_prevWalker = this.m_prevWalker.cloneDeep(walkingIterator, vector);
            }
        } else if (null != this.m_nextWalker) {
            axesWalker.m_nextWalker.m_prevWalker = axesWalker;
        }
        return axesWalker;
    }

    static AxesWalker findClone(AxesWalker axesWalker, Vector vector) {
        if (null != vector) {
            int n2 = vector.size();
            for (int i2 = 0; i2 < n2; i2 += 2) {
                if (axesWalker != vector.elementAt(i2)) continue;
                return (AxesWalker)vector.elementAt(i2 + 1);
            }
        }
        return null;
    }

    public void detach() {
        this.m_currentNode = -1;
        this.m_dtm = null;
        this.m_traverser = null;
        this.m_isFresh = true;
        this.m_root = -1;
    }

    public int getRoot() {
        return this.m_root;
    }

    public int getAnalysisBits() {
        int n2 = this.getAxis();
        int n3 = WalkerFactory.getAnalysisBitFromAxes(n2);
        return n3;
    }

    public void setRoot(int n2) {
        XPathContext xPathContext = this.wi().getXPathContext();
        this.m_dtm = xPathContext.getDTM(n2);
        this.m_traverser = this.m_dtm.getAxisTraverser(this.m_axis);
        this.m_isFresh = true;
        this.m_foundLast = false;
        this.m_root = n2;
        this.m_currentNode = n2;
        if (-1 == n2) {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_SETTING_WALKER_ROOT_TO_NULL", null));
        }
        this.resetProximityPositions();
    }

    public void setNextWalker(AxesWalker axesWalker) {
        this.m_nextWalker = axesWalker;
    }

    public AxesWalker getNextWalker() {
        return this.m_nextWalker;
    }

    public void setPrevWalker(AxesWalker axesWalker) {
        this.m_prevWalker = axesWalker;
    }

    protected int getNextNode() {
        if (this.m_foundLast) {
            return -1;
        }
        if (this.m_isFresh) {
            this.m_currentNode = this.m_traverser.first(this.m_root);
            this.m_isFresh = false;
        } else if (-1 != this.m_currentNode) {
            this.m_currentNode = this.m_traverser.next(this.m_root, this.m_currentNode);
        }
        if (-1 == this.m_currentNode) {
            this.m_foundLast = true;
        }
        return this.m_currentNode;
    }

    public int nextNode() {
        int n2 = -1;
        AxesWalker axesWalker = this.wi().getLastUsedWalker();
        while (null != axesWalker) {
            n2 = axesWalker.getNextNode();
            if (-1 == n2) {
                axesWalker = axesWalker.m_prevWalker;
                continue;
            }
            if (axesWalker.acceptNode(n2) != 1) continue;
            if (null == axesWalker.m_nextWalker) {
                this.wi().setLastUsedWalker(axesWalker);
                break;
            }
            AxesWalker axesWalker2 = axesWalker;
            axesWalker = axesWalker.m_nextWalker;
            axesWalker.setRoot(n2);
            axesWalker.m_prevWalker = axesWalker2;
        }
        return n2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int getLastPos(XPathContext xPathContext) {
        int n2;
        AxesWalker axesWalker;
        n2 = this.getProximityPosition();
        try {
            axesWalker = (AxesWalker)this.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return -1;
        }
        axesWalker.setPredicateCount(this.m_predicateIndex);
        axesWalker.setNextWalker(null);
        axesWalker.setPrevWalker(null);
        WalkingIterator walkingIterator = this.wi();
        AxesWalker axesWalker2 = walkingIterator.getLastUsedWalker();
        try {
            int n3;
            walkingIterator.setLastUsedWalker(axesWalker);
            while (-1 != (n3 = axesWalker.nextNode())) {
                ++n2;
            }
        }
        finally {
            walkingIterator.setLastUsedWalker(axesWalker2);
        }
        return n2;
    }

    public DTM getDTM(int n2) {
        return this.wi().getXPathContext().getDTM(n2);
    }

    public int getAxis() {
        return this.m_axis;
    }

    public void callVisitors(ExpressionOwner expressionOwner, XPathVisitor xPathVisitor) {
        if (xPathVisitor.visitStep(expressionOwner, this)) {
            this.callPredicateVisitors(xPathVisitor);
            if (null != this.m_nextWalker) {
                this.m_nextWalker.callVisitors(this, xPathVisitor);
            }
        }
    }

    public Expression getExpression() {
        return this.m_nextWalker;
    }

    public void setExpression(Expression expression) {
        expression.exprSetParent(this);
        this.m_nextWalker = (AxesWalker)expression;
    }

    public boolean deepEquals(Expression expression) {
        if (!super.deepEquals(expression)) {
            return false;
        }
        AxesWalker axesWalker = (AxesWalker)expression;
        if (this.m_axis != axesWalker.m_axis) {
            return false;
        }
        return true;
    }
}

