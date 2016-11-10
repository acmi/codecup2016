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
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.axes.AxesWalker;
import org.apache.xpath.axes.FilterExprIteratorSimple;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.axes.PathComponent;
import org.apache.xpath.axes.WalkingIterator;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.operations.Variable;

public class FilterExprWalker
extends AxesWalker {
    private Expression m_expr;
    private transient XNodeSet m_exprObj;
    private boolean m_mustHardReset = false;
    private boolean m_canDetachNodeset = true;

    public FilterExprWalker(WalkingIterator walkingIterator) {
        super(walkingIterator, 20);
    }

    public void init(Compiler compiler, int n2, int n3) throws TransformerException {
        super.init(compiler, n2, n3);
        switch (n3) {
            case 24: 
            case 25: {
                this.m_mustHardReset = true;
            }
            case 22: 
            case 23: {
                this.m_expr = compiler.compile(n2);
                this.m_expr.exprSetParent(this);
                if (!(this.m_expr instanceof Variable)) break;
                this.m_canDetachNodeset = false;
                break;
            }
            default: {
                this.m_expr = compiler.compile(n2 + 2);
                this.m_expr.exprSetParent(this);
            }
        }
    }

    public void detach() {
        super.detach();
        if (this.m_canDetachNodeset) {
            this.m_exprObj.detach();
        }
        this.m_exprObj = null;
    }

    public void setRoot(int n2) {
        super.setRoot(n2);
        this.m_exprObj = FilterExprIteratorSimple.executeFilterExpr(n2, this.m_lpi.getXPathContext(), this.m_lpi.getPrefixResolver(), this.m_lpi.getIsTopLevel(), this.m_lpi.m_stackFrame, this.m_expr);
    }

    public Object clone() throws CloneNotSupportedException {
        FilterExprWalker filterExprWalker = (FilterExprWalker)super.clone();
        if (null != this.m_exprObj) {
            filterExprWalker.m_exprObj = (XNodeSet)this.m_exprObj.clone();
        }
        return filterExprWalker;
    }

    public short acceptNode(int n2) {
        try {
            if (this.getPredicateCount() > 0) {
                this.countProximityPosition(0);
                if (!this.executePredicates(n2, this.m_lpi.getXPathContext())) {
                    return 3;
                }
            }
            return 1;
        }
        catch (TransformerException transformerException) {
            throw new RuntimeException(transformerException.getMessage());
        }
    }

    public int getNextNode() {
        if (null != this.m_exprObj) {
            int n2 = this.m_exprObj.nextNode();
            return n2;
        }
        return -1;
    }

    public int getLastPos(XPathContext xPathContext) {
        return this.m_exprObj.getLength();
    }

    public void fixupVariables(Vector vector, int n2) {
        super.fixupVariables(vector, n2);
        this.m_expr.fixupVariables(vector, n2);
    }

    public Expression getInnerExpression() {
        return this.m_expr;
    }

    public void setInnerExpression(Expression expression) {
        expression.exprSetParent(this);
        this.m_expr = expression;
    }

    public int getAnalysisBits() {
        if (null != this.m_expr && this.m_expr instanceof PathComponent) {
            return ((PathComponent)((Object)this.m_expr)).getAnalysisBits();
        }
        return 67108864;
    }

    public int getAxis() {
        return this.m_exprObj.getAxis();
    }

    public void callPredicateVisitors(XPathVisitor xPathVisitor) {
        this.m_expr.callVisitors(new filterExprOwner(this), xPathVisitor);
        super.callPredicateVisitors(xPathVisitor);
    }

    public boolean deepEquals(Expression expression) {
        if (!super.deepEquals(expression)) {
            return false;
        }
        FilterExprWalker filterExprWalker = (FilterExprWalker)expression;
        if (!this.m_expr.deepEquals(filterExprWalker.m_expr)) {
            return false;
        }
        return true;
    }

    static Expression access$000(FilterExprWalker filterExprWalker) {
        return filterExprWalker.m_expr;
    }

    static Expression access$002(FilterExprWalker filterExprWalker, Expression expression) {
        filterExprWalker.m_expr = expression;
        return filterExprWalker.m_expr;
    }

    class filterExprOwner
    implements ExpressionOwner {
        private final FilterExprWalker this$0;

        filterExprOwner(FilterExprWalker filterExprWalker) {
            this.this$0 = filterExprWalker;
        }

        public Expression getExpression() {
            return FilterExprWalker.access$000(this.this$0);
        }

        public void setExpression(Expression expression) {
            expression.exprSetParent(this.this$0);
            FilterExprWalker.access$002(this.this$0, expression);
        }
    }

}

