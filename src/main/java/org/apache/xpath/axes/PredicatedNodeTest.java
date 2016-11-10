/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.axes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.axes.SubContextList;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.patterns.NodeTest;

public abstract class PredicatedNodeTest
extends NodeTest
implements SubContextList {
    static final long serialVersionUID = -6193530757296377351L;
    protected int m_predCount = -1;
    protected transient boolean m_foundLast = false;
    protected LocPathIterator m_lpi;
    transient int m_predicateIndex = -1;
    private Expression[] m_predicates;
    protected transient int[] m_proximityPositions;
    static final boolean DEBUG_PREDICATECOUNTING = false;

    PredicatedNodeTest(LocPathIterator locPathIterator) {
        this.m_lpi = locPathIterator;
    }

    PredicatedNodeTest() {
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, TransformerException {
        try {
            objectInputStream.defaultReadObject();
            this.m_predicateIndex = -1;
            this.resetProximityPositions();
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new TransformerException(classNotFoundException);
        }
    }

    public Object clone() throws CloneNotSupportedException {
        PredicatedNodeTest predicatedNodeTest = (PredicatedNodeTest)Object.super.clone();
        if (null != this.m_proximityPositions && this.m_proximityPositions == predicatedNodeTest.m_proximityPositions) {
            predicatedNodeTest.m_proximityPositions = new int[this.m_proximityPositions.length];
            System.arraycopy(this.m_proximityPositions, 0, predicatedNodeTest.m_proximityPositions, 0, this.m_proximityPositions.length);
        }
        if (predicatedNodeTest.m_lpi == this) {
            predicatedNodeTest.m_lpi = (LocPathIterator)predicatedNodeTest;
        }
        return predicatedNodeTest;
    }

    public int getPredicateCount() {
        if (-1 == this.m_predCount) {
            return null == this.m_predicates ? 0 : this.m_predicates.length;
        }
        return this.m_predCount;
    }

    public void setPredicateCount(int n2) {
        if (n2 > 0) {
            Expression[] arrexpression = new Expression[n2];
            for (int i2 = 0; i2 < n2; ++i2) {
                arrexpression[i2] = this.m_predicates[i2];
            }
            this.m_predicates = arrexpression;
        } else {
            this.m_predicates = null;
        }
    }

    protected void initPredicateInfo(Compiler compiler, int n2) throws TransformerException {
        int n3 = compiler.getFirstPredicateOpPos(n2);
        if (n3 > 0) {
            this.m_predicates = compiler.getCompiledPredicates(n3);
            if (null != this.m_predicates) {
                for (int i2 = 0; i2 < this.m_predicates.length; ++i2) {
                    this.m_predicates[i2].exprSetParent(this);
                }
            }
        }
    }

    public Expression getPredicate(int n2) {
        return this.m_predicates[n2];
    }

    public int getProximityPosition() {
        return this.getProximityPosition(this.m_predicateIndex);
    }

    public int getProximityPosition(XPathContext xPathContext) {
        return this.getProximityPosition();
    }

    public abstract int getLastPos(XPathContext var1);

    protected int getProximityPosition(int n2) {
        return n2 >= 0 ? this.m_proximityPositions[n2] : 0;
    }

    public void resetProximityPositions() {
        int n2 = this.getPredicateCount();
        if (n2 > 0) {
            if (null == this.m_proximityPositions) {
                this.m_proximityPositions = new int[n2];
            }
            for (int i2 = 0; i2 < n2; ++i2) {
                try {
                    this.initProximityPosition(i2);
                    continue;
                }
                catch (Exception exception) {
                    throw new WrappedRuntimeException(exception);
                }
            }
        }
    }

    public void initProximityPosition(int n2) throws TransformerException {
        this.m_proximityPositions[n2] = 0;
    }

    protected void countProximityPosition(int n2) {
        int[] arrn = this.m_proximityPositions;
        if (null != arrn && n2 < arrn.length) {
            int[] arrn2 = arrn;
            int n3 = n2;
            arrn2[n3] = arrn2[n3] + 1;
        }
    }

    public boolean isReverseAxes() {
        return false;
    }

    public int getPredicateIndex() {
        return this.m_predicateIndex;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    boolean executePredicates(int n2, XPathContext xPathContext) throws TransformerException {
        int n3 = this.getPredicateCount();
        if (n3 == 0) {
            return true;
        }
        PrefixResolver prefixResolver = xPathContext.getNamespaceContext();
        try {
            this.m_predicateIndex = 0;
            xPathContext.pushSubContextList(this);
            xPathContext.pushNamespaceContext(this.m_lpi.getPrefixResolver());
            xPathContext.pushCurrentNode(n2);
            for (int i2 = 0; i2 < n3; ++i2) {
                int n4;
                XObject xObject = this.m_predicates[i2].execute(xPathContext);
                if (2 == xObject.getType()) {
                    int n5;
                    n4 = this.getProximityPosition(this.m_predicateIndex);
                    if (n4 != (n5 = (int)xObject.num())) {
                        boolean bl = false;
                        return bl;
                    }
                    if (this.m_predicates[i2].isStableNumber() && i2 == n3 - 1) {
                        this.m_foundLast = true;
                    }
                } else if (!xObject.bool()) {
                    n4 = 0;
                    return (boolean)n4;
                }
                this.countProximityPosition(++this.m_predicateIndex);
            }
        }
        finally {
            xPathContext.popCurrentNode();
            xPathContext.popNamespaceContext();
            xPathContext.popSubContextList();
            this.m_predicateIndex = -1;
        }
        return true;
    }

    public void fixupVariables(Vector vector, int n2) {
        super.fixupVariables(vector, n2);
        int n3 = this.getPredicateCount();
        for (int i2 = 0; i2 < n3; ++i2) {
            this.m_predicates[i2].fixupVariables(vector, n2);
        }
    }

    protected String nodeToString(int n2) {
        if (-1 != n2) {
            DTM dTM = this.m_lpi.getXPathContext().getDTM(n2);
            return dTM.getNodeName(n2) + "{" + (n2 + 1) + "}";
        }
        return "null";
    }

    public short acceptNode(int n2) {
        XPathContext xPathContext = this.m_lpi.getXPathContext();
        try {
            xPathContext.pushCurrentNode(n2);
            XObject xObject = this.execute(xPathContext, n2);
            if (xObject != NodeTest.SCORE_NONE) {
                if (this.getPredicateCount() > 0) {
                    this.countProximityPosition(0);
                    if (!this.executePredicates(n2, xPathContext)) {
                        short s2 = 3;
                        return s2;
                    }
                }
                short s3 = 1;
                return s3;
            }
        }
        catch (TransformerException transformerException) {
            throw new RuntimeException(transformerException.getMessage());
        }
        finally {
            xPathContext.popCurrentNode();
        }
        return 3;
    }

    public LocPathIterator getLocPathIterator() {
        return this.m_lpi;
    }

    public void setLocPathIterator(LocPathIterator locPathIterator) {
        this.m_lpi = locPathIterator;
        if (this != locPathIterator) {
            locPathIterator.exprSetParent(this);
        }
    }

    public boolean canTraverseOutsideSubtree() {
        int n2 = this.getPredicateCount();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!this.getPredicate(i2).canTraverseOutsideSubtree()) continue;
            return true;
        }
        return false;
    }

    public void callPredicateVisitors(XPathVisitor xPathVisitor) {
        if (null != this.m_predicates) {
            int n2 = this.m_predicates.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                PredOwner predOwner = new PredOwner(this, i2);
                if (!xPathVisitor.visitPredicate(predOwner, this.m_predicates[i2])) continue;
                this.m_predicates[i2].callVisitors(predOwner, xPathVisitor);
            }
        }
    }

    public boolean deepEquals(Expression expression) {
        if (!super.deepEquals(expression)) {
            return false;
        }
        PredicatedNodeTest predicatedNodeTest = (PredicatedNodeTest)expression;
        if (null != this.m_predicates) {
            int n2 = this.m_predicates.length;
            if (null == predicatedNodeTest.m_predicates || predicatedNodeTest.m_predicates.length != n2) {
                return false;
            }
            for (int i2 = 0; i2 < n2; ++i2) {
                if (this.m_predicates[i2].deepEquals(predicatedNodeTest.m_predicates[i2])) continue;
                return false;
            }
        } else if (null != predicatedNodeTest.m_predicates) {
            return false;
        }
        return true;
    }

    static Expression[] access$000(PredicatedNodeTest predicatedNodeTest) {
        return predicatedNodeTest.m_predicates;
    }

    class PredOwner
    implements ExpressionOwner {
        int m_index;
        private final PredicatedNodeTest this$0;

        PredOwner(PredicatedNodeTest predicatedNodeTest, int n2) {
            this.this$0 = predicatedNodeTest;
            this.m_index = n2;
        }

        public Expression getExpression() {
            return PredicatedNodeTest.access$000(this.this$0)[this.m_index];
        }

        public void setExpression(Expression expression) {
            expression.exprSetParent(this.this$0);
            PredicatedNodeTest.access$000((PredicatedNodeTest)this.this$0)[this.m_index] = expression;
        }
    }

}

