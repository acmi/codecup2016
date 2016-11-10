/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.axes;

import java.io.Serializable;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.axes.AxesWalker;
import org.apache.xpath.axes.FilterExprWalker;
import org.apache.xpath.axes.HasPositionalPredChecker;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.axes.PathComponent;
import org.apache.xpath.axes.PredicatedNodeTest;
import org.apache.xpath.axes.UnionChildIterator;
import org.apache.xpath.axes.WalkerFactory;
import org.apache.xpath.axes.WalkingIterator;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.OpMap;

public class UnionPathIterator
extends LocPathIterator
implements Serializable,
Cloneable,
DTMIterator,
PathComponent {
    protected LocPathIterator[] m_exprs;
    protected DTMIterator[] m_iterators;

    public UnionPathIterator() {
        this.m_iterators = null;
        this.m_exprs = null;
    }

    public void setRoot(int n2, Object object) {
        super.setRoot(n2, object);
        try {
            if (null != this.m_exprs) {
                int n3 = this.m_exprs.length;
                DTMIterator[] arrdTMIterator = new DTMIterator[n3];
                for (int i2 = 0; i2 < n3; ++i2) {
                    DTMIterator dTMIterator;
                    arrdTMIterator[i2] = dTMIterator = this.m_exprs[i2].asIterator(this.m_execContext, n2);
                    dTMIterator.nextNode();
                }
                this.m_iterators = arrdTMIterator;
            }
        }
        catch (Exception exception) {
            throw new WrappedRuntimeException(exception);
        }
    }

    public void addIterator(DTMIterator dTMIterator) {
        if (null == this.m_iterators) {
            this.m_iterators = new DTMIterator[1];
            this.m_iterators[0] = dTMIterator;
        } else {
            DTMIterator[] arrdTMIterator = this.m_iterators;
            int n2 = this.m_iterators.length;
            this.m_iterators = new DTMIterator[n2 + 1];
            System.arraycopy(arrdTMIterator, 0, this.m_iterators, 0, n2);
            this.m_iterators[n2] = dTMIterator;
        }
        dTMIterator.nextNode();
        if (dTMIterator instanceof Expression) {
            ((Expression)((Object)dTMIterator)).exprSetParent(this);
        }
    }

    public void detach() {
        if (this.m_allowDetach && null != this.m_iterators) {
            int n2 = this.m_iterators.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                this.m_iterators[i2].detach();
            }
            this.m_iterators = null;
        }
    }

    public UnionPathIterator(Compiler compiler, int n2) throws TransformerException {
        n2 = OpMap.getFirstChildPos(n2);
        this.loadLocationPaths(compiler, n2, 0);
    }

    public static LocPathIterator createUnionIterator(Compiler compiler, int n2) throws TransformerException {
        UnionPathIterator unionPathIterator = new UnionPathIterator(compiler, n2);
        int n3 = unionPathIterator.m_exprs.length;
        boolean bl = true;
        for (int i2 = 0; i2 < n3; ++i2) {
            LocPathIterator locPathIterator = unionPathIterator.m_exprs[i2];
            if (locPathIterator.getAxis() != 3) {
                bl = false;
                break;
            }
            if (!HasPositionalPredChecker.check(locPathIterator)) continue;
            bl = false;
            break;
        }
        if (bl) {
            UnionChildIterator unionChildIterator = new UnionChildIterator();
            for (int i3 = 0; i3 < n3; ++i3) {
                LocPathIterator locPathIterator = unionPathIterator.m_exprs[i3];
                unionChildIterator.addNodeTest(locPathIterator);
            }
            return unionChildIterator;
        }
        return unionPathIterator;
    }

    public int getAnalysisBits() {
        int n2 = 0;
        if (this.m_exprs != null) {
            int n3 = this.m_exprs.length;
            for (int i2 = 0; i2 < n3; ++i2) {
                int n4 = this.m_exprs[i2].getAnalysisBits();
                n2 |= n4;
            }
        }
        return n2;
    }

    public Object clone() throws CloneNotSupportedException {
        UnionPathIterator unionPathIterator = (UnionPathIterator)super.clone();
        if (this.m_iterators != null) {
            int n2 = this.m_iterators.length;
            unionPathIterator.m_iterators = new DTMIterator[n2];
            for (int i2 = 0; i2 < n2; ++i2) {
                unionPathIterator.m_iterators[i2] = (DTMIterator)this.m_iterators[i2].clone();
            }
        }
        return unionPathIterator;
    }

    protected LocPathIterator createDTMIterator(Compiler compiler, int n2) throws TransformerException {
        LocPathIterator locPathIterator = (LocPathIterator)WalkerFactory.newDTMIterator(compiler, n2, compiler.getLocationPathDepth() <= 0);
        return locPathIterator;
    }

    protected void loadLocationPaths(Compiler compiler, int n2, int n3) throws TransformerException {
        int n4 = compiler.getOp(n2);
        if (n4 == 28) {
            this.loadLocationPaths(compiler, compiler.getNextOpPos(n2), n3 + 1);
            this.m_exprs[n3] = this.createDTMIterator(compiler, n2);
            this.m_exprs[n3].exprSetParent(this);
        } else {
            switch (n4) {
                case 22: 
                case 23: 
                case 24: 
                case 25: {
                    this.loadLocationPaths(compiler, compiler.getNextOpPos(n2), n3 + 1);
                    WalkingIterator walkingIterator = new WalkingIterator(compiler.getNamespaceContext());
                    walkingIterator.exprSetParent(this);
                    if (compiler.getLocationPathDepth() <= 0) {
                        walkingIterator.setIsTopLevel(true);
                    }
                    walkingIterator.m_firstWalker = new FilterExprWalker(walkingIterator);
                    walkingIterator.m_firstWalker.init(compiler, n2, n4);
                    this.m_exprs[n3] = walkingIterator;
                    break;
                }
                default: {
                    this.m_exprs = new LocPathIterator[n3];
                }
            }
        }
    }

    public int nextNode() {
        if (this.m_foundLast) {
            return -1;
        }
        int n2 = -1;
        if (null != this.m_iterators) {
            int n3 = this.m_iterators.length;
            int n4 = -1;
            for (int i2 = 0; i2 < n3; ++i2) {
                int n5 = this.m_iterators[i2].getCurrentNode();
                if (-1 == n5) continue;
                if (-1 == n2) {
                    n4 = i2;
                    n2 = n5;
                    continue;
                }
                if (n5 == n2) {
                    this.m_iterators[i2].nextNode();
                    continue;
                }
                DTM dTM = this.getDTM(n5);
                if (!dTM.isNodeAfter(n5, n2)) continue;
                n4 = i2;
                n2 = n5;
            }
            if (-1 != n2) {
                this.m_iterators[n4].nextNode();
                this.incrementCurrentPos();
            } else {
                this.m_foundLast = true;
            }
        }
        this.m_lastFetched = n2;
        return n2;
    }

    public void fixupVariables(Vector vector, int n2) {
        for (int i2 = 0; i2 < this.m_exprs.length; ++i2) {
            this.m_exprs[i2].fixupVariables(vector, n2);
        }
    }

    public int getAxis() {
        return -1;
    }

    public void callVisitors(ExpressionOwner expressionOwner, XPathVisitor xPathVisitor) {
        if (xPathVisitor.visitUnionPath(expressionOwner, this) && null != this.m_exprs) {
            int n2 = this.m_exprs.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                this.m_exprs[i2].callVisitors(new iterOwner(this, i2), xPathVisitor);
            }
        }
    }

    public boolean deepEquals(Expression expression) {
        if (!super.deepEquals(expression)) {
            return false;
        }
        UnionPathIterator unionPathIterator = (UnionPathIterator)expression;
        if (null != this.m_exprs) {
            int n2 = this.m_exprs.length;
            if (null == unionPathIterator.m_exprs || unionPathIterator.m_exprs.length != n2) {
                return false;
            }
            for (int i2 = 0; i2 < n2; ++i2) {
                if (this.m_exprs[i2].deepEquals(unionPathIterator.m_exprs[i2])) continue;
                return false;
            }
        } else if (null != unionPathIterator.m_exprs) {
            return false;
        }
        return true;
    }

    class iterOwner
    implements ExpressionOwner {
        int m_index;
        private final UnionPathIterator this$0;

        iterOwner(UnionPathIterator unionPathIterator, int n2) {
            this.this$0 = unionPathIterator;
            this.m_index = n2;
        }

        public Expression getExpression() {
            return this.this$0.m_exprs[this.m_index];
        }

        public void setExpression(Expression expression) {
            if (!(expression instanceof LocPathIterator)) {
                WalkingIterator walkingIterator = new WalkingIterator(this.this$0.getPrefixResolver());
                FilterExprWalker filterExprWalker = new FilterExprWalker(walkingIterator);
                walkingIterator.setFirstWalker(filterExprWalker);
                filterExprWalker.setInnerExpression(expression);
                walkingIterator.exprSetParent(this.this$0);
                filterExprWalker.exprSetParent(walkingIterator);
                expression.exprSetParent(filterExprWalker);
                expression = walkingIterator;
            } else {
                expression.exprSetParent(this.this$0);
            }
            this.this$0.m_exprs[this.m_index] = (LocPathIterator)expression;
        }
    }

}

